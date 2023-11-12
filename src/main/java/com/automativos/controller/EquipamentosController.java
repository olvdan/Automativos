package com.automativos.controller;

import com.automativos.model.Equipamentos;
import com.automativos.service.EquipamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentosController {

    @Autowired
    private EquipamentosService equipamentosService;

    @GetMapping(produces = "application/json")
    public List<Object> listar() {
        return equipamentosService.listarEquipamentos();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Equipamentos equipamentos) {
        try {
            ResponseEntity<?> response = equipamentosService.adicionarEquipamento(equipamentos);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar equipamento");
        }
    }

    @PutMapping("/{patrimonio}")
    public ResponseEntity<?> atualizarEquipamento(@PathVariable Long patrimonio, @RequestBody Equipamentos equipamentoAtualizado) {
        return equipamentosService.atualizarEquipamento(patrimonio, equipamentoAtualizado);
    }

    @PatchMapping("/{patrimonio}")
    public ResponseEntity<?> atualizarParcialEquipamento(
            @PathVariable Long patrimonio,
            @RequestBody Map<String, Object> camposAtualizados) {
        return equipamentosService.atualizarParcialEquipamento(patrimonio, camposAtualizados);
    }

    @DeleteMapping("/{patrimonio}")
    public ResponseEntity<String> deletarEquipamento(@PathVariable Long patrimonio) {
        return equipamentosService.deletarEquipamento(patrimonio);
    }
}
