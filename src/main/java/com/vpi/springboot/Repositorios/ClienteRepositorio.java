package com.vpi.springboot.Repositorios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Cliente;

@Transactional
public interface ClienteRepositorio extends UserBaseRepository<Cliente> {
	//x.mail LIKE CONCAT('%',UPPER(:mail),'%') enrealidad lo que busca es si el parametro de entrada para buscar esta contenido en la columna mail
	//Usamos UPPER en las entradas y variables para que discrimine mayusculas y minusculas
	
	/*@Query("SELECT c FROM Cliente c WHERE UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public List<Cliente> buscarClienteNombre(@Param("texto") String texto);*/
	
	@Query("SELECT c FROM Cliente c WHERE UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public Page<Cliente> buscarClienteNombre(@Param("texto") String texto, @Param("page") Pageable page);
	
	/*@Query("SELECT c FROM Cliente c")
	public List<Cliente> buscarCliente();*/
		
	@Query("SELECT c FROM Cliente c")
	public Page<Cliente> buscarCliente(@Param("page") Pageable page);
	
	@Query("SELECT c FROM Cliente c where c.nickname = :nickname")
	Optional<Cliente> findByNickname(@Param("nickname") String nickname);
}