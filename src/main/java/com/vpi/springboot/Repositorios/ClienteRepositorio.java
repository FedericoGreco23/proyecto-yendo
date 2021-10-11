package com.vpi.springboot.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Cliente;

@Transactional
public interface ClienteRepositorio extends UserBaseRepository<Cliente> {
	//Si no funciona el <CONTAINS '%asdf%'> probar con <= :asdf>
	//x.mail CONTAINS '%nickname%' enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	
	/////////////////////////////////////////////////////////////
	//AGREGAR CRITERIO DE BUSQUEDA DE ANTIGUEDAD CORRECTAMENTE//
	///////////////////////////////////////////////////////////
	
//--------------------------------------------------------------------------------------------------//
	
	/*@Query("SELECT c FROM Cliente c WHERE c.antiguedad >= antiguedad AND c.mail CONTAINS '%nickname%' OR c.nickname CONTAINS '%nickname%'")
	public List<Cliente> buscarClienteTodo(@Param("antiguedad") int antiguedad, @Param("nickname") String nickname);
	
	@Query("SELECT c FROM CLiente c WHERE c.antiguedad >= antiguedad")
	public List<Cliente> buscarClienteAntiguedad(@Param("antiguedad") int antiguedad);*/
	
	@Query("SELECT c FROM Cliente c WHERE UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public List<Cliente> buscarClienteNombre(@Param("texto") String texto);
	
	@Query("SELECT c FROM Cliente c")
	public List<Cliente> buscarCliente();
		
}