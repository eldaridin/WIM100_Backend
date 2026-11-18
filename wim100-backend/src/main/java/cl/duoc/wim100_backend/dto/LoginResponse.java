package cl.duoc.wim100_backend.dto;

import cl.duoc.wim100_backend.model.Rol;

public record LoginResponse(Long id, String usuario, String nombre, Rol rol) {
}
