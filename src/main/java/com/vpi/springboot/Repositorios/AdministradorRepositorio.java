package com.vpi.springboot.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Administrador;

@Transactional
public interface AdministradorRepositorio extends UserBaseRepository<Administrador> {
	//Si no funciona el <CONTAINS '%asdf%'> probar con <= :asdf>
	//x.mail CONTAINS '%nickname%' enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	
	/////////////////////////////////////////////////////////////
	//AGREGAR CRITERIO DE BUSQUEDA DE ANTIGUEDAD CORRECTAMENTE//
	///////////////////////////////////////////////////////////
	
//--------------------------------------------------------------------------------------------------//

	/*@Query("SELECT a FROM Administrador a WHERE a.antiguedad >= antiguedad AND a.mail CONTAINS '%mail%'")
	public List<Administrador> buscarAdministradorTodo(@Param("antiguedad") int antiguedad, @Param("mail") String mail);
	
	@Query("SELECT a FROM Administrador a WHERE a.antiguedad >= antiguedad")
	public List<Administrador> buscarAdministradorAntiguedad(@Param("antiguedad") int antiguedad);*/
	
	@Query("SELECT a FROM Administrador a WHERE UPPER(a.mail) LIKE CONCAT('%',UPPER(:mail),'%')")
	public List<Administrador> buscarAdministradorNombre(@Param("mail") String mail);
	
	@Query("SELECT a FROM Administrador a")
	public List<Administrador> buscarAdministrador();
}