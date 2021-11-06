package com.vpi.springboot.Repositorios.mongo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.dto.DTProductoVendido;

@Transactional
public interface ProductosVendidosRepositorio extends MongoRepository<DTProductoVendido, String> {

	//Tira error, usando como ejemplo la de RestaurantePedidosRepositorio es igual
	//consultar luego con el que hizo el otro repositorio ¿?
	/*@Query("SELECT p FROM DTProductoVendido p WHERE p.idProducto = :idProducto")
	DTProductoVendido findByIdProducto(@Param("idProducto") String idProducto);*/

}
