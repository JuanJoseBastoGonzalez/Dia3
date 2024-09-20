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

import com.example.domain.services.DetalleTransaccion.DetalleTransaccionService;
import com.example.persistence.entities.DetalleTransaccion;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/detalle-transacciones")
public class DetalleTransaccionController {

    @Autowired
    private DetalleTransaccionService servicio;

    @GetMapping
    public List<DetalleTransaccion> listarDetalles() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleTransaccion> ver(@PathVariable Long id) {
        Optional<DetalleTransaccion> detalleOpt = servicio.findById(id);
        return detalleOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DetalleTransaccion detalleTransaccion, BindingResult resultado) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(detalleTransaccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody DetalleTransaccion detalleTransaccion, BindingResult resultado, @PathVariable Long id) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        Optional<DetalleTransaccion> detalleOpt = servicio.update(id, detalleTransaccion);
        return detalleOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleTransaccion> eliminar(@PathVariable Long id) {
        Optional<DetalleTransaccion> detalleOpt = servicio.delete(id);
        return detalleOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<?> validar(BindingResult resultado) {
        Map<String, String> errores = new HashMap<>();
        resultado.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
