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
	// x.mail LIKE CONCAT('%',UPPER(:mail),'%') enrealidad lo que busca es si el
	// parametro de entrada para buscar esta contenido en la columna mail
	// Usamos UPPER en las entradas y variables para que discrimine mayusculas y
	// minusculas

	@Query("SELECT c FROM Cliente c WHERE UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public Page<Cliente> buscarClienteNombre(@Param("texto") String texto, @Param("page") Pageable page);

	@Query("SELECT c FROM Cliente c")
	public Page<Cliente> buscarCliente(@Param("page") Pageable page);
	
//-------------------------------------------------------------------------------------------------
	
	@Query("SELECT c FROM Cliente c WHERE c.bloqueado = true AND UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public Page<Cliente> buscarClienteBloqueadoNombre(@Param("texto") String texto, @Param("page") Pageable page);

	@Query("SELECT c FROM Cliente c WHERE c.bloqueado = true")
	public Page<Cliente> buscarClienteBloqueado(@Param("page") Pageable page);
	
//-------------------------------------------------------------------------------------------------
	
	@Query("SELECT c FROM Cliente c WHERE c.bloqueado = false AND UPPER(c.mail) LIKE CONCAT('%',UPPER(:texto),'%') OR UPPER(c.nickname) LIKE CONCAT('%',UPPER(:texto),'%')")
	public Page<Cliente> buscarClienteDesbloqueadoNombre(@Param("texto") String texto, @Param("page") Pageable page);

	@Query("SELECT c FROM Cliente c WHERE c.bloqueado = false")
	public Page<Cliente> buscarClienteDesbloqueado(@Param("page") Pageable page);
	
//-------------------------------------------------------------------------------------------------

	@Query("SELECT a FROM Cliente a WHERE UPPER(a.mail) LIKE CONCAT('%',UPPER(:mail),'%')")
	List<Cliente> buscarClientesMail(@Param("mail") String mail);

	@Query("SELECT a FROM Cliente a WHERE UPPER(a.nickname) LIKE CONCAT('%',UPPER(:nicknames),'%')")
	List<Cliente> buscarClientesNickname(@Param("nickname") String nickname);

	@Query("SELECT c FROM Cliente c where c.nickname = :nickname")
	Optional<Cliente> findByNickname(@Param("nickname") String nickname);
}