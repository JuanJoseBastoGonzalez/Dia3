package com.example.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.services.ClaseContacto.ClaseContactoService;
import com.example.persistence.entities.ClaseContacto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clase-contacto")
public class ClaseContactoController {

    @Autowired
    private ClaseContactoService servicio;

    @GetMapping
    public List<ClaseContacto> listarClaseContactos() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseContacto> ver(@PathVariable Long id) {
        Optional<ClaseContacto> claseContactoOpt = servicio.findById(id);
        return claseContactoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ClaseContacto claseContacto, BindingResult resultado) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(claseContacto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody ClaseContacto claseContacto, BindingResult resultado, @PathVariable Long id) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        Optional<ClaseContacto> claseContactoOpt = servicio.update(id, claseContacto);
        return claseContactoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClaseContacto> eliminar(@PathVariable Long id) {
        Optional<ClaseContacto> claseContactoOpt = servicio.delete(id);
        return claseContactoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<?> validar(BindingResult resultado) {
        Map<String, String> errores = new HashMap<>();
        resultado.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
