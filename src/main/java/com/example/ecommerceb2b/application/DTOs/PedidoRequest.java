package com.example.ecommerceb2b.application.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoRequest(BigDecimal valorTotal, LocalDateTime data, BigDecimal desconto) {
}
