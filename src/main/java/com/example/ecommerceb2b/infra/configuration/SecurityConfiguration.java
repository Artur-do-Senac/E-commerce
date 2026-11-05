package com.example.ecommerceb2b.infra.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Quem pode o quê (sem token = 401, com token mas sem a role necessária = 403):
 *
 *   Endpoint                                                    ADMIN   GERENTE   USER
 *   POST /auth/login e Swagger                                  público, sem token
 *   POST /usuarios/admin (cold start, só sem nenhum ADMIN)      público, sem token
 *   GET /usuarios/buscarusuariologado                           x       x         x
 *   /usuarios (listar, buscar, criar, editar, status, excluir)  x
 *   POST /empresas (criar)                                      x
 *   /empresas (listar, buscar, editar, status)                  x       x   (GERENTE só enxerga/edita a própria empresa)
 *   /categorias e /produtos (listar, buscar)                    x       x         x
 *   /categorias e /produtos (criar, editar, status)             x       x
 *   /pedidos (listar, buscar, editar, status)                   x       x
 *   POST /pedidos (criar pedido)                                x       x         x
 *   DELETE em empresas, categorias, produtos e pedidos          x
 *   Qualquer outra rota                                         basta estar logado
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
                        // /error: o Spring redireciona para lá ao devolver um erro (ex.: 403). Se ela exigisse login, o 403 de quem não tem a role viraria 401
                        .requestMatchers("/auth/login",
                                "/error",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Cold start: o app desktop cria o primeiro ADMIN sem token (o Service só aceita quando ainda não existe nenhum ADMIN e a chave secreta confere)
                        .requestMatchers(HttpMethod.POST, "/usuarios/admin").permitAll()

                        // Dados de quem está logado: qualquer role (o front busca isso logo após o login, inclusive de GERENTE e USER)
                        .requestMatchers(HttpMethod.GET, "/usuarios/buscarusuariologado").authenticated()

                        // Usuários: somente ADMIN
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Criar empresa: somente ADMIN (GERENTE só consulta e edita a própria empresa)
                        .requestMatchers(HttpMethod.POST, "/empresas").hasRole("ADMIN")

                        // Excluir empresas, categorias, produtos e pedidos: somente ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/empresas/**", "/categorias/**", "/produtos/**", "/pedidos/**").hasRole("ADMIN")

                        // Consultar categorias e produtos: qualquer role
                        .requestMatchers(HttpMethod.GET, "/categorias/**", "/produtos/**").hasAnyRole("ADMIN", "GERENTE", "USER")

                        // Criar pedido: qualquer role
                        .requestMatchers(HttpMethod.POST, "/pedidos").hasAnyRole("ADMIN", "GERENTE", "USER")

                        // O que sobrou de empresas, categorias, produtos e pedidos (consultar, criar, editar e mudar status): ADMIN e GERENTE
                        .requestMatchers("/empresas/**", "/categorias/**", "/produtos/**", "/pedidos/**").hasAnyRole("ADMIN", "GERENTE")

                        // Qualquer outra rota: basta estar logado
                        .anyRequest().authenticated()
                )
                // Sem token (ou sem login) numa rota protegida: 401. Sem isso o Spring responderia 403
                .exceptionHandling(erro -> erro.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // O JwtFilter é um @Component, então o Spring Boot o registraria sozinho no servidor, fora da cadeia de segurança.
    // Desligando esse registro, o filtro só roda dentro do SecurityFilterChain: mudar as regras acima (ex.: permitAll) passa a valer de verdade
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registro = new FilterRegistrationBean<>(jwtFilter);
        registro.setEnabled(false);
        return registro;
    }
}
