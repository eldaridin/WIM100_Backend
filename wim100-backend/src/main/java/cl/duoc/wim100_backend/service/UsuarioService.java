package cl.duoc.wim100_backend.service;

import cl.duoc.wim100_backend.exception.RecursoNoEncontradoException;
import cl.duoc.wim100_backend.exception.UsuarioDuplicadoException;
import cl.duoc.wim100_backend.model.Rol;
import cl.duoc.wim100_backend.model.Usuario;
import cl.duoc.wim100_backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario " + id));
    }

    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return repository.findByUsuario(usuario.trim().toLowerCase());
    }

    public Usuario crear(String usuario, String password, String nombre, Rol rol) {
        String nombreUsuario = usuario.trim().toLowerCase();
        if (repository.existsByUsuarioIgnoreCase(nombreUsuario)) {
            throw new UsuarioDuplicadoException("Ya existe el usuario " + nombreUsuario);
        }
        return repository.save(new Usuario(
                nombreUsuario,
                encoder.encode(password),
                nombre.trim(),
                rol
        ));
    }
}
