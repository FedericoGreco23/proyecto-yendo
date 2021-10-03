package com.vpi.springboot.Repositorios;




import javax.transaction.Transactional;

import com.vpi.springboot.Modelo.Usuario;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface UsuarioRepositorio extends UserBaseRepository<Usuario> {
	
}