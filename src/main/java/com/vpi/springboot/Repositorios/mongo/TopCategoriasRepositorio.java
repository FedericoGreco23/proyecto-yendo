package com.vpi.springboot.Repositorios.mongo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vpi.springboot.Modelo.dto.DTProductoVendido;
import com.vpi.springboot.Modelo.dto.DTTopCategoria;

@Transactional
public interface TopCategoriasRepositorio extends MongoRepository<DTTopCategoria, String>  {

	/*@Query("SELECT p.categoria, COUNT(p.cantidad) FROM DTProductoVendido p GROUP BY p.categoria")
	List<DTTopCategoria> findAllBy(Sort sort);*/

	@Query("SELECT p.categoria, COUNT(p.cantidad) FROM DTTopCategoria p GROUP BY p.categoria")
	List<DTTopCategoria> findAllBy(Sort sort);
}
