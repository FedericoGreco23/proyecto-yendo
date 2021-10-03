package com.vpi.springboot.Repositorios;




import javax.transaction.Transactional;

import com.vpi.springboot.Modelo.Restaurante;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface RestauranteRepositorio extends UserBaseRepository<Restaurante> {
	
}