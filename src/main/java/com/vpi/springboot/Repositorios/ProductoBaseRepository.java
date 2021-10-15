package com.vpi.springboot.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.vpi.springboot.Modelo.Producto;

/**
 * todos los métodos de este repo estarán disponibles para los hijos
 * @author feder
 *
 * @param <T>
 */
@NoRepositoryBean
public interface ProductoBaseRepository<T extends Producto> 
extends JpaRepository<T, Integer> {
	
 
}