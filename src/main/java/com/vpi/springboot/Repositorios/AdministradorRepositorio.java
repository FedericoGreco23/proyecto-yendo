package com.vpi.springboot.Repositorios;




import javax.transaction.Transactional;

import com.vpi.springboot.Modelo.Administrador;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface AdministradorRepositorio extends UserBaseRepository<Administrador> {
	
}