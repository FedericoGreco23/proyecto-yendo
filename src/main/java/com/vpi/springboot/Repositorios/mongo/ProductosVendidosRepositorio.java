package com.vpi.springboot.Repositorios.mongo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
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

	/*@Query("SELECT p.categoria, COUNT(p.cantidad) FROM DTProductoVendido p GROUP BY p.categoria ORDER BY p.cantidad DESC")
	List<DTProductoVendido> findAllBy();*/
	
	@Query("SELECT p.categoria, COUNT(p.cantidad) FROM DTProductoVendido p GROUP BY p.categoria")
	List<DTProductoVendido> findAllBy(Sort sort);
}
