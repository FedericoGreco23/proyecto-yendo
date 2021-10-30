package com.vpi.springboot.Repositorios;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.Categoria;
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

	@Query("SELECT u FROM Restaurante u WHERE UPPER(u.nombre) LIKE CONCAT('%',UPPER(:nombre),'%') AND u.estado = :estado AND u.bloqueado = FALSE AND u.activo = TRUE")
	Page<Restaurante> buscarRestaurantesPorEstadoNoBloqueadosYActivosPorNombre(@Param("nombre") String nombre, @Param("estado") EnumEstadoRestaurante estado, Pageable pageable);
	
	//	@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
//	User findUserByStatusAndNameNamedParams(
//	  @Param("status") Integer status, 
//	  @Param("name") String name);
	
	@Query("SELECT r FROM Restaurante r WHERE UPPER(r.nombre) LIKE CONCAT('%',UPPER(:texto),'%') AND r.estado = :estado AND r.bloqueado = FALSE AND r.activo = TRUE")
	List<Restaurante> buscarRestauranteDesdeClientePorNombre(@Param("texto") String texto, @Param("estado") EnumEstadoRestaurante estado);
	
	@Query("SELECT r FROM Restaurante r INNER JOIN r.categorias c WHERE UPPER(c.nombre) LIKE UPPER(:categoria) AND UPPER(r.nombre) LIKE CONCAT('%',UPPER(:texto),'%') AND r.estado = :estado AND r.bloqueado = FALSE AND r.activo = TRUE")
	List<Restaurante> buscarRestauranteDesdeClientePorNombreYCategoria(@Param("texto") String texto, @Param("categoria") String categoria, @Param("estado") EnumEstadoRestaurante estado);
	
	@Query("SELECT r FROM Restaurante r INNER JOIN r.categorias c WHERE UPPER(c.nombre) LIKE UPPER(:categoria) AND r.estado = :estado AND r.bloqueado = FALSE AND r.activo = TRUE")
	List<Restaurante> buscarRestauranteDesdeClientePorCategoria(@Param("categoria") String categoria, @Param("estado") EnumEstadoRestaurante estado);
	
	
	@Query(value = "SELECT \"restaurante_mail\", COUNT(*) FROM Pedido GROUP BY \"restaurante_mail\" ORDER BY count(*) DESC", nativeQuery = true)
	List<Object[]> buscarRestaurantesConMasPedidos();
}