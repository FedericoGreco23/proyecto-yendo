package com.vpi.springboot.Repositorios;




import javax.transaction.Transactional;

import com.vpi.springboot.Modelo.Producto;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface ProductoRepositorio extends ProductoBaseRepository<Producto> {
	
}