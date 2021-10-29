package com.vpi.springboot.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;

/**
 * Repository for the entity Person.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface ProductoRepositorio extends ProductoBaseRepository<Producto> {

	@Query("SELECT u FROM Producto u WHERE u.nombre = :nombre and u.restaurante = :restaurante")
	Producto findByNombre(@Param("nombre") String nombre, 
						  @Param("restaurante") Restaurante restaurante);
	
	@Query("SELECT u FROM Producto u WHERE u.restaurante = :restaurante")
	Page<Producto> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	
	@Query("SELECT u FROM Producto u WHERE u.id = :ids AND u.restaurante = :mail")
	Producto findByIdAndRest(@Param("ids") Integer ids, 
						  @Param("mail") Restaurante mail);
						  
	@Query("SELECT u FROM Producto u WHERE u.restaurante = :restaurante and u.activo = true")
	List<Producto> findAllByRestaurante(@Param("restaurante") Restaurante restaurante);
	
	final static String queryNombreDescripcion =
			"SELECT pro "
			+ "FROM Producto pro "
			+ "WHERE pro.restaurante = :restaurante "
			+ "and (UPPER(pro.nombre) LIKE CONCAT('%',UPPER(:producto),'%') "
			+ "or UPPER(pro.descripcion) LIKE CONCAT('%',UPPER(:producto),'%'))";
	
	@Query(queryNombreDescripcion)
	List<Producto> findAllByParametro(@Param("restaurante") Restaurante restaurante, @Param("producto") String producto);
}