package com.vpi.springboot.Repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Restaurante;

@Repository
public interface CalificacionRestauranteRepositorio
		extends JpaRepository<CalificacionRestaurante, CalificacionRestauranteId> {
	
	@Query("SELECT u FROM CalificacionRestaurante u WHERE u.restaurante = :restaurante")
	List<CalificacionRestaurante> findByRestaurante(Restaurante restaurante);
	
	@Query("SELECT c FROM CalificacionRestaurante c WHERE c.restaurante = :restaurante")
	Page<CalificacionRestaurante> consultarCalificacion(Restaurante restaurante, Pageable pageable);
	
	@Query("SELECT u FROM CalificacionRestaurante u WHERE u.cliente = :cliente and u.restaurante = :restaurante")
	CalificacionRestaurante findByClienteRestaurante(@Param("cliente") Cliente cliente,
			@Param("restaurante") Restaurante restaurante);
}