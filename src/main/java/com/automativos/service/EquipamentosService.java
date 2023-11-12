package com.automativos.service;

import com.automativos.model.Equipamentos;
import com.automativos.repository.EquipamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EquipamentosService {

    @Autowired
    private EquipamentosRepository equipamentosRepository;

    // GET
    public List<Object> listarEquipamentos() {
        try {
            List<Equipamentos> equipamentos = equipamentosRepository.findAll();
            List<Object> result = new ArrayList<>();

            for (Equipamentos equipamento : equipamentos) {
                List<Object> equipamentosValues = new ArrayList<>();
                equipamentosValues.add(equipamento.getPatrimonio());
                equipamentosValues.add(equipamento.getTipo());
                equipamentosValues.add(equipamento.getMarca());
                equipamentosValues.add(equipamento.getModelo());
                equipamentosValues.add(equipamento.getSerialnumber());
                equipamentosValues.add(equipamento.getUsuario());
                result.add(equipamentosValues);
            }

            return result;
        } catch (Exception e) {
            // Tratar exceção de forma adequada (pode logar, retornar erro personalizado, etc.)
            throw new RuntimeException("Erro ao listar equipamentos", e);
        }
    }

    // POST
    @Transactional
    public ResponseEntity<?> adicionarEquipamento(Equipamentos equipamento) {
        try {
            // Verificar se já existe um equipamento com o mesmo serialnumber
            if (equipamentosRepository.existsBySerialnumber(equipamento.getSerialnumber())) {
                return ResponseEntity.badRequest().body("Já existe um equipamento com o mesmo serialnumber.");
            }

            Equipamentos equipamentoSalvo = equipamentosRepository.save(equipamento);
            return ResponseEntity.ok(equipamentoSalvo);
        } catch (Exception e) {
            // Tratar exceção de forma adequada (pode logar, retornar erro personalizado, etc.)
            throw new RuntimeException("Erro ao adicionar equipamento", e);
        }
    }


    // PUT
    @Transactional
    public ResponseEntity<?> atualizarEquipamento(Long patrimonio, Equipamentos equipamentoAtualizado) {
        try {
            Optional<Equipamentos> equipamentoOptional = equipamentosRepository.findById(patrimonio);

            if (!equipamentoOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Equipamentos equipamentoExistente = equipamentoOptional.get();

            // Verificar se o serialnumber está sendo alterado e se já existe algum equipamento com o novo serialnumber
            if (!equipamentoExistente.getSerialnumber().equals(equipamentoAtualizado.getSerialnumber()) &&
                    equipamentosRepository.existsBySerialnumber(equipamentoAtualizado.getSerialnumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serialnumber já está em uso.");
            }

            // Atualizar os campos
            equipamentoExistente.setTipo(equipamentoAtualizado.getTipo());
            equipamentoExistente.setMarca(equipamentoAtualizado.getMarca());
            equipamentoExistente.setModelo(equipamentoAtualizado.getModelo());
            equipamentoExistente.setSerialnumber(equipamentoAtualizado.getSerialnumber());
            equipamentoExistente.setUsuario(equipamentoAtualizado.getUsuario());

            equipamentosRepository.save(equipamentoExistente);

            // Pode retornar o equipamento atualizado, se necessário
            return ResponseEntity.ok(equipamentoExistente);
        } catch (Exception e) {
            // Tratar exceção de forma adequada (pode logar, retornar erro personalizado, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar equipamento.");
        }
    }

    //PATCH
    @Transactional
    public ResponseEntity<?> atualizarParcialEquipamento(Long patrimonio, Map<String, Object> camposAtualizados) {
        try {
            Optional<Equipamentos> equipamentoOptional = equipamentosRepository.findById(patrimonio);

            if (!equipamentoOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Equipamentos equipamentoExistente = equipamentoOptional.get();

            // Verificar se o serialnumber está sendo alterado e se já existe algum equipamento com o novo serialnumber
            if (camposAtualizados.containsKey("serialnumber")) {
                String novoSerialnumber = (String) camposAtualizados.get("serialnumber");

                if (!equipamentoExistente.getSerialnumber().equals(novoSerialnumber) &&
                        equipamentosRepository.existsBySerialnumber(novoSerialnumber)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serialnumber já está em uso.");
                }
            }

            // Atualizar os campos
            camposAtualizados.forEach((campo, valor) -> {
                switch (campo) {
                    case "tipo":
                        equipamentoExistente.setTipo((String) valor);
                        break;
                    case "marca":
                        equipamentoExistente.setMarca((String) valor);
                        break;
                    case "modelo":
                        equipamentoExistente.setModelo((String) valor);
                        break;
                    case "serialnumber":
                        equipamentoExistente.setSerialnumber((String) valor);
                        break;
                    case "usuario":
                        equipamentoExistente.setUsuario((String) valor);
                        break;
                }
            });

            equipamentosRepository.save(equipamentoExistente);

            // Pode retornar o equipamento atualizado, se necessário
            return ResponseEntity.ok(equipamentoExistente);
        } catch (Exception e) {
            // Tratar exceção de forma adequada (pode logar, retornar erro personalizado, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar parcialmente equipamento.");
        }
    }


    // DELETE
    @Transactional
    public ResponseEntity<String> deletarEquipamento(Long patrimonio) {
        try {
            Optional<Equipamentos> equipamentoOptional = equipamentosRepository.findById(patrimonio);

            if (!equipamentoOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            equipamentosRepository.deleteById(patrimonio);

            return ResponseEntity.ok("Equipamento excluído com sucesso.");
        } catch (Exception e) {
            // Tratar exceção de forma adequada (pode logar, retornar erro personalizado, etc.)
            throw new RuntimeException("Erro ao deletar equipamento", e);
        }
    }
}
