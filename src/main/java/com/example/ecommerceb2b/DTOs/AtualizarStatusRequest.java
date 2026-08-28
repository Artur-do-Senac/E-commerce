package com.example.ecommerceb2b.DTOs;

import com.example.ecommerceb2b.entities.EnumStatusUsuario;
import lombok.Data;


public record AtualizarStatusRequest(EnumStatusUsuario status) {
}
