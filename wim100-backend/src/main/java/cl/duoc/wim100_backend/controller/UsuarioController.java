package cl.duoc.wim100_backend.controller;

import cl.duoc.wim100_backend.dto.UsuarioRequest;
import cl.duoc.wim100_backend.dto.UsuarioResponse;
import cl.duoc.wim100_backend.model.Usuario;
import cl.duoc.wim100_backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listar().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return toResponse(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest req) {
        Usuario creado = service.crear(req.usuario(), req.password(), req.nombre(), req.rol());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getUsuario(), u.getNombre(), u.getRol());
    }
}
