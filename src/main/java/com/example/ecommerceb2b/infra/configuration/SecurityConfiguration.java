package com.example.ecommerceb2b.infra.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Quem pode o quê (sem token = 401, com token mas sem a role necessária = 403):
 *
 *   Endpoint                                                    ADMIN   GERENTE   USER
 *   POST /auth/login e Swagger                                  público, sem token
 *   POST /usuarios/admin (exige a secretKey no corpo)           público, sem token
 *   POST /auth/logout                                           qualquer usuário logado
 *   /usuarios (listar, buscar, criar, editar, status, excluir)  x
 *   /empresas (listar, buscar, criar, editar, status)           x       x
 *   /categorias e /produtos (listar, buscar)                    x       x         x
 *   /categorias e /produtos (criar, editar, status)             x       x
 *   /pedidos (listar, buscar, editar, status)                   x       x
 *   POST /pedidos (criar pedido)                                x       x         x
 *   DELETE em empresas, categorias, produtos e pedidos          x
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                // A ordem das regras importa: vale a primeira que combinar com a requisição, então as mais específicas vêm antes
                .authorizeHttpRequests(auth -> auth

                        // Públicas: não precisam de token
                        // (/error é por onde o Spring devolve os erros reais, como 400 e 500: sem liberar, todo erro apareceria como 403)
                        .requestMatchers("/auth/login",
                                "/error",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Criação de administrador: protegida pela secretKey enviada no corpo (o app JavaFX chama sem token)
                        .requestMatchers(HttpMethod.POST, "/usuarios/admin").permitAll()

                        // Usuários: somente ADMIN
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Excluir empresas, categorias, produtos e pedidos: somente ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/empresas/**", "/categorias/**", "/produtos/**", "/pedidos/**").hasRole("ADMIN")

                        // Consultar categorias e produtos: qualquer role
                        .requestMatchers(HttpMethod.GET, "/categorias/**", "/produtos/**").hasAnyRole("ADMIN", "GERENTE", "USER")

                        // Criar pedido: qualquer role
                        .requestMatchers(HttpMethod.POST, "/pedidos").hasAnyRole("ADMIN", "GERENTE", "USER")

                        // O que sobrou de empresas, categorias, produtos e pedidos (consultar, criar, editar e mudar status): ADMIN e GERENTE
                        .requestMatchers("/empresas/**", "/categorias/**", "/produtos/**", "/pedidos/**").hasAnyRole("ADMIN", "GERENTE")

                        // Qualquer outra rota (ex.: /auth/logout): basta estar logado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
