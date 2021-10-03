package com.vpi.springboot.Repositorios;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.IdCompuestas.CalificacionClienteId;
import com.vpi.springboot.Modelo.CalificacionCliente;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Repository
public interface CalificacionClienteRepositorio extends JpaRepository<CalificacionCliente, CalificacionClienteId> {
	
}