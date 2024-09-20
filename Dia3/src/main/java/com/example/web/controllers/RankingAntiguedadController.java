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

import com.example.domain.services.RankingAntiguedad.RankingAntiguedadService;
import com.example.persistence.entities.RankingAntiguedad;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rankings-antiguedad")
public class RankingAntiguedadController {

    @Autowired
    private RankingAntiguedadService servicio;

    @GetMapping
    public List<RankingAntiguedad> listarRankings() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankingAntiguedad> ver(@PathVariable Long id) {
        Optional<RankingAntiguedad> rankingOpt = servicio.findById(id);
        return rankingOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RankingAntiguedad ranking, BindingResult resultado) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(ranking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody RankingAntiguedad ranking, BindingResult resultado, @PathVariable Long id) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        Optional<RankingAntiguedad> rankingOpt = servicio.update(id, ranking);
        return rankingOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RankingAntiguedad> eliminar(@PathVariable Long id) {
        Optional<RankingAntiguedad> rankingOpt = servicio.delete(id);
        return rankingOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<?> validar(BindingResult resultado) {
        Map<String, String> errores = new HashMap<>();
        resultado.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
