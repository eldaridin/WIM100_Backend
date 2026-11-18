package cl.duoc.wim100_backend.service;

import cl.duoc.wim100_backend.dto.LoginRequest;
import cl.duoc.wim100_backend.dto.LoginResponse;
import cl.duoc.wim100_backend.exception.CredencialesInvalidasException;
import cl.duoc.wim100_backend.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioService usuarioService, PasswordEncoder encoder) {
        this.usuarioService = usuarioService;
        this.encoder = encoder;
    }

    public LoginResponse login(LoginRequest req) {
        Usuario u = usuarioService.buscarPorUsuario(req.usuario())
                // matches() compara la contraseña escrita con el hash guardado
                .filter(x -> encoder.matches(req.password(), x.getPassword()))
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));
        return new LoginResponse(u.getId(), u.getUsuario(), u.getNombre(), u.getRol());
    }
}
