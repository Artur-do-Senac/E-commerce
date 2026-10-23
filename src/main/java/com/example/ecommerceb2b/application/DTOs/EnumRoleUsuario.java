package com.example.ecommerceb2b.application.DTOs;

// Papéis (roles) do sistema. O nome precisa começar com ROLE_ porque o Spring Security acrescenta esse prefixo:
// hasRole("ADMIN") no SecurityConfiguration enxerga a role ROLE_ADMIN.
public enum EnumRoleUsuario {
    // Administra tudo: usuários, empresas, categorias, produtos e pedidos (é o único que exclui registros)
    ROLE_ADMIN,
    // Opera a loja: empresas, categorias, produtos e pedidos (não gerencia usuários e não exclui registros)
    ROLE_GERENTE,
    // Cliente: consulta categorias e produtos e cria pedidos
    ROLE_USER
}
