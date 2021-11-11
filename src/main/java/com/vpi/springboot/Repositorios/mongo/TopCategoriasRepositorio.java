package com.vpi.springboot.Repositorios.mongo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.dto.DTProductoVendido;
import com.vpi.springboot.Modelo.dto.DTTopCategoria;

@Transactional
public interface TopCategoriasRepositorio extends MongoRepository<DTTopCategoria, String>  {

	/*@Query("SELECT p.categoria, COUNT(p.cantidad) FROM DTProductoVendido p GROUP BY p.categoria")
	List<DTTopCategoria> findAllBy(Sort sort);*/

	/*@Query(value = "SELECT COUNT(p) FROM DTTopCategoria p GROUP BY p.categoria", nativeQuery = true)
	List<DTTopCategoria> findAllBy(Sort sort);*/
	
	/*@Query("SELECT COUNT(p.cantidad) as cantidad, p.categoria FROM DTTopCategoria p WHERE p.cantidad > :cantidad GROUP BY p.categoria")
	List<DTTopCategoria> findByCantidad(@Param("cantidad") int cantidad, Sort sort);*/
	
	/*@Query("SELECT COUNT(p.cantidad) as cantidad, p.categoria FROM DTTopCategoria p WHERE p.categoria LIKE :categoria GROUP BY p.categoria")
	List<DTTopCategoria> findByCategoria(@Param("categoria") String categoria, Sort sort);*/
}