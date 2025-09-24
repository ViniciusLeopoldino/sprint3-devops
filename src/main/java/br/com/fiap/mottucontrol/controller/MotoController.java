// src/main/java/br/com/fiap/mottucontrol/controller/MotoController.java
package br.com.fiap.mottucontrol.controller;

import br.com.fiap.mottucontrol.model.Moto;
import br.com.fiap.mottucontrol.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/motos") // Define o endpoint base
public class MotoController {

    @Autowired
    private MotoRepository motoRepository;

    // CREATE (Inclusão)
    @PostMapping
    public ResponseEntity<Moto> criarMoto(@RequestBody Moto moto) {
        Moto novaMoto = motoRepository.save(moto);
        return new ResponseEntity<>(novaMoto, HttpStatus.CREATED);
    }

    // READ (Consulta) - Listar todas
    @GetMapping
    public List<Moto> listarMotos() {
        return motoRepository.findAll();
    }

    // READ (Consulta) - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Moto> buscarMotoPorId(@PathVariable Long id) {
        Optional<Moto> moto = motoRepository.findById(id);
        return moto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE (Alteração)
    @PutMapping("/{id}")
    public ResponseEntity<Moto> atualizarMoto(@PathVariable Long id, @RequestBody Moto motoDetalhes) {
        return motoRepository.findById(id)
                .map(motoExistente -> {
                    motoExistente.setModelo(motoDetalhes.getModelo());
                    motoExistente.setPlaca(motoDetalhes.getPlaca());
                    motoExistente.setAno(motoDetalhes.getAno());
                    Moto motoAtualizada = motoRepository.save(motoExistente);
                    return ResponseEntity.ok(motoAtualizada);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE (Exclusão)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMoto(@PathVariable Long id) {
        return motoRepository.findById(id)
                .map(moto -> {
                    motoRepository.delete(moto);
                    return ResponseEntity.noContent().<Void>build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}