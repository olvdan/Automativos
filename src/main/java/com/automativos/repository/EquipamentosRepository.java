package com.automativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automativos.model.Equipamentos;

@Repository
public interface EquipamentosRepository extends JpaRepository<Equipamentos, Long> {

}
