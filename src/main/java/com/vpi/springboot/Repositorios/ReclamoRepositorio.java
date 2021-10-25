package com.vpi.springboot.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;

@Repository
public interface ReclamoRepositorio extends JpaRepository<Reclamo, Integer> {
	@Query("SELECT u FROM Reclamo u WHERE u.restaurante = :restaurante")
	Page<Reclamo> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	@Query("SELECT u FROM Reclamo u WHERE u.cliente = :cliente")
	Page<Reclamo> findAllByCliente(@Param("cliente") Cliente restaurante, Pageable pageable);

}