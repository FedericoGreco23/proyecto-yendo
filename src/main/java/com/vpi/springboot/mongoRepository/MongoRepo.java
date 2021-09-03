package com.vpi.springboot.mongoRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Pedidos;

@Repository
public interface MongoRepo extends org.springframework.data.mongodb.repository.MongoRepository<Pedidos, String> {

	@Query("{'Pedidos' : ?0")
	Optional<Pedidos> findByPedidos(String pedido);
}
