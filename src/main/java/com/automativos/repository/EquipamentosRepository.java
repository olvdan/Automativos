package com.automativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automativos.model.Equipamentos;

import java.util.Optional;

@Repository
public interface EquipamentosRepository extends JpaRepository<Equipamentos, Long> {
	
	//Verificação de Existência pelo serialnumber
	boolean existsBySerialnumber(String serialnumber);
	Optional<Equipamentos> findBySerialnumber(String serialnumber);
	
}
