package com.vpi.springboot.Repositorios;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Administrador;

@Transactional
public interface AdministradorRepositorio extends UserBaseRepository<Administrador> {
	//x.mail LIKE CONCAT('%',UPPER(:mail),'%') enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	
	@Query("SELECT a FROM Administrador a WHERE UPPER(a.mail) LIKE CONCAT('%',UPPER(:mail),'%')")
	public Page<Administrador> buscarAdministradorNombre(@Param("mail") String mail, @Param("page") Pageable page);
		
	@Query("SELECT a FROM Administrador a")
	public Page<Administrador> buscarAdministrador(@Param("page") Pageable page);
	
//-------------------------------------------------------------------------------------------------
	
	@Query("SELECT a FROM Administrador a WHERE a.bloqueado = true AND UPPER(a.mail) LIKE CONCAT('%',UPPER(:mail),'%')")
	public Page<Administrador> buscarAdministradorBloqueadoNombre(@Param("mail") String mail, @Param("page") Pageable page);
		
	@Query("SELECT a FROM Administrador a WHERE a.bloqueado = true")
	public Page<Administrador> buscarAdministradorBloqueado(@Param("page") Pageable page);
	
//------------------------------------------------------------------------------------------------
	
	@Query("SELECT a FROM Administrador a WHERE a.bloqueado = false AND UPPER(a.mail) LIKE CONCAT('%',UPPER(:mail),'%')")
	public Page<Administrador> buscarAdministradorDesbloqueadoNombre(@Param("mail") String mail, @Param("page") Pageable page);
		
	@Query("SELECT a FROM Administrador a WHERE a.bloqueado = false")
	public Page<Administrador> buscarAdministradorDesbloqueado(@Param("page") Pageable page);
}