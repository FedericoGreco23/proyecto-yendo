package com.vpi.springboot.Repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Carrito;

	@Repository
	public interface MongoRepositorioCarrito extends MongoRepository<Carrito, String> {
		
		
		@Query("{'mail': ?0, 'activo': ?1}")
		Carrito findByMailAndActivo(String mail, boolean activo);


	}

