package com.vpi.springboot.Repositorios;




import javax.transaction.Transactional;

import com.vpi.springboot.Modelo.Promocion;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface PromocionRepositorio extends ProductoBaseRepository<Promocion> {
	
}