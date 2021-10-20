package com.vpi.springboot.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Restaurante;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface PromocionRepositorio extends ProductoBaseRepository<Promocion> {
	@Query("SELECT u FROM Promocion u WHERE u.restaurante = :restaurante")
	Page<Promocion> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	

	@Query("SELECT u FROM Promocion u WHERE u.restaurante = :restaurante")
	List<Promocion> findAllByRestauranteSimple(@Param("restaurante") Restaurante restaurante);
}