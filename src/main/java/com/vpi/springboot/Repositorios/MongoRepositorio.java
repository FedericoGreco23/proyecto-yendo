package com.vpi.springboot.Repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Carrito;


	@Repository
	public interface MongoRepositorio extends MongoRepository<Carrito, String> {

	}

