package cl.duoc.wim100_backend.dto;

import cl.duoc.wim100_backend.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(max = 40, message = "El usuario no puede superar los 40 caracteres")
        String usuario,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 100, message = "La contraseña no puede superar los 100 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
        String nombre,

        @NotNull(message = "El rol es obligatorio")
        Rol rol
) {
}
