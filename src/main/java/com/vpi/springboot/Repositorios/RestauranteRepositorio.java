package com.vpi.springboot.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Restaurante;


@Transactional
public interface RestauranteRepositorio extends UserBaseRepository<Restaurante> {
	//Si no funciona el <CONTAINS '%asdf%'> probar con <= :asdf>
	//x.mail CONTAINS '%nickname%' enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	
	/////////////////////////////////////////////////////////////
	//AGREGAR CRITERIO DE BUSQUEDA DE ANTIGUEDAD CORRECTAMENTE//
	///////////////////////////////////////////////////////////
	
//--------------------------------------------------------------------------------------------------//

	/*@Query("SELECT r FROM Restaurante r WHERE r.antiguedad >= antiguedad AND r.mail CONTAINS '%nombreRestaurante%' OR r.nombre CONTAINS '%nombreRestaurante%'")
	public List<Restaurante> buscarRestauranteTodo(@Param("antiguedad") int antiguedad, @Param("nombreRestaurante") String nombreRestaurante);
	
	@Query("SELECT r FROM Restaurante r WHERE r.antiguedad >= antiguedad")
	public List<Restaurante> buscarRestauranteAntiguedad(@Param("antiguedad") int antiguedad);*/
	
	@Query("SELECT r FROM Restaurante r WHERE UPPER(r.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(r.nombre) LIKE CONCAT('%',UPPER(:texto),'%')")
	public List<Restaurante> buscarRestauranteNombre(@Param("texto") String texto);
	
	@Query("SELECT r FROM Restaurante r")
	public List<Restaurante> buscarRestaurante();
}