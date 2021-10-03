package com.vpi.springboot.Repositorios;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;
import com.vpi.springboot.Modelo.CalificacionRestaurante;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Repository
public interface CalificacionRestauranteRepositorio extends JpaRepository<CalificacionRestaurante, CalificacionRestauranteId> {
	
}