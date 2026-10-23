package com.example.ecommerceb2b.infra.configuration;

import com.example.ecommerceb2b.application.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String metodo = request.getMethod();

        // Rotas públicas (as mesmas liberadas no SecurityConfiguration) e o pré-flight do CORS (OPTIONS) passam sem token
        if(metodo.equals("OPTIONS")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v2/api-docs")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/webjars")
                || uri.equals("/auth/login")
                || (uri.equals("/usuarios/admin") && metodo.equals("POST"))
        ){
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring("Bearer ".length());

            try {
                // Valida o JWT e consulta o token no banco (existe, ativo e dentro da validade)
                var usuarioLogado = tokenService.verificadorToken(token);

                // O usuário logado é o principal e as roles dele são as authorities:
                // é isso que o hasRole/hasAnyRole do SecurityConfiguration consulta para liberar ou negar (403) cada endpoint
                var user = new UsernamePasswordAuthenticationToken(
                        usuarioLogado, null, usuarioLogado.authorities()
                );

                SecurityContextHolder.getContext().setAuthentication(user);

            } catch (Exception e) {

                tokenInvalido(response);
                return;
            }
        } else {
            tokenInvalido(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void tokenInvalido(HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println("Token inválido!");
    }
}
