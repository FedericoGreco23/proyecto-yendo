package com.vpi.springboot.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.IdCompuestas.CalificacionClienteId;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.Cliente;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Repository
public interface CalificacionClienteRepositorio extends JpaRepository<CalificacionCliente, CalificacionClienteId> {
	
	@Query("SELECT u FROM CalificacionCliente u WHERE u.cliente = :cliente")
	List<CalificacionCliente> findByCliente(Cliente cliente);
}