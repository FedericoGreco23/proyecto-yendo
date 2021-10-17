package com.vpi.springboot.Repositorios;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;

@Transactional
public interface RestauranteRepositorio extends UserBaseRepository<Restaurante> {
	//x.mail LIKE CONCAT('%',UPPER(:mail),'%') enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	

	//no usar, si se rompe arrojar error de nombre repetido
	@Query("SELECT r FROM Restaurante r WHERE UPPER(r.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(r.nombre) LIKE CONCAT('%',UPPER(:texto),'%')")
	public Page<Restaurante> buscarRestauranteNombre(@Param("texto") String texto, @Param("page") Pageable page);
	
	@Query("SELECT r FROM Restaurante r")
	public Page<Restaurante> buscarRestaurante(@Param("page") Pageable page);
	
	@Query("SELECT r FROM Restaurante r WHERE UPPER(r.mail) LIKE UPPER(:nombre)")
	public Restaurante existeRestauranteNombre(@Param("nombre") String nombre);
//	@Query(value = "SELECT p FROM Restaurante p WHERE p.nombre = ?1", nativeQuery = true)
//	@Query(value = "SELECT u FROM Restaurante u WHERE u.nombre = ?1", nativeQuery = true)
	@Query("SELECT u FROM Restaurante u WHERE u.nombre = :nombre")
	Restaurante findByNombre(@Param("nombre") String nombre); 
	
	@Query("SELECT u FROM Restaurante u WHERE u.estado = :estado")
	Page<Restaurante> findByEstado(@Param("estado") EnumEstadoRestaurante estado, Pageable pageable);
	
	@Query("SELECT u FROM Restaurante u WHERE u.estado = :estado AND u.bloqueado = FALSE AND u.activo = TRUE")
	Page<Restaurante> buscarRestaurantesPorEstadoNoBloqueadosYActivos(@Param("estado") EnumEstadoRestaurante estado, Pageable pageable);
//	@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
//	User findUserByStatusAndNameNamedParams(
//	  @Param("status") Integer status, 
//	  @Param("name") String name);
}