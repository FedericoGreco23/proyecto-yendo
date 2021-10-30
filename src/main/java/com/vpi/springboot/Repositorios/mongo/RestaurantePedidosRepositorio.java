package com.vpi.springboot.Repositorios.mongo;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;

@Transactional
public interface RestaurantePedidosRepositorio extends MongoRepository<DTRestaurantePedido, String> {
	
	 @Query("select d from DTRestaurantePedido d where d.mailRestaurante = :mailRestaurante")
	 DTRestaurantePedido findBymailRestaurante(@Param("mailRestaurante") String mailRestaurante);


	/* @Query("SELECT r FROM DTRestaurantePedido r")
	 public Page<DTRestaurantePedido> findAll(Pageable page);*/
}
