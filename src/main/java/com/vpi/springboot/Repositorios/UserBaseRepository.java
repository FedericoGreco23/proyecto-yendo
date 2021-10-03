package com.vpi.springboot.Repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.vpi.springboot.Modelo.Usuario;

/**
 * todos los métodos de este repo estarán disponibles para los hijos
 * @author feder
 *
 * @param <T>
 */
@NoRepositoryBean
public interface UserBaseRepository<T extends Usuario> 
extends CrudRepository<T, String> {
	
//	//The value of #{#entityName} will be the entity type T
//	@Query("select u from #{#entityName} as u where u.email = ?1 ")
//	public T findByEmail(String email);
 
}