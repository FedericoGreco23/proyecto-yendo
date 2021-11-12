package com.vpi.springboot.Repositorios.mongo;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;

@Transactional
public interface RestaurantePedidosRepositorio extends MongoRepository<DTRestaurantePedido, String> {
	
	 @Query("select d from DTRestaurantePedido d where d.mailRestaurante = :mailRestaurante")
	 DTRestaurantePedido findBymailRestaurante(@Param("mailRestaurante") String mailRestaurante);


	/* @Query("SELECT r FROM DTRestaurantePedido r")
	 public Page<DTRestaurantePedido> findAll(Pageable page);*/
}
