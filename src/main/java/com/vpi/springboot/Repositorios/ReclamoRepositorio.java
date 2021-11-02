package com.vpi.springboot.Repositorios;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;

@Repository
public interface ReclamoRepositorio extends JpaRepository<Reclamo, Integer> {
	@Query("SELECT u FROM Reclamo u WHERE u.restaurante = :restaurante")
	Page<Reclamo> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
		
	/////////// FUNCIONES DE CLIENTE ///////////
	////////////////////////////////////////////
	
	//RESTAURANTE
	final static String queryClienteRestaurante = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente) "
			+ "and r.restaurante IN "
			+ "(SELECT res "
			+ "FROM Restaurante res "
			+ "WHERE UPPER(res.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
			+ "or UPPER(res.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
		
	@Query(queryClienteRestaurante)
	Page<Reclamo> findAllByClienteRestaurante(@Param("cliente") Cliente cliente, @Param("restaurante") String restaurante, 
			Pageable pageable);
	
	//ESTADO
	final static String queryClienteEstado = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.estado = :estado "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente)";
		
	@Query(queryClienteEstado)
	Page<Reclamo> findAllByClienteEstado(@Param("cliente") Cliente cliente, @Param("estado") EnumEstadoReclamo estado, 
			Pageable pageable);
	
	//FECHA
	final static String queryClienteFecha = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente)";
		
	@Query(queryClienteFecha)
	Page<Reclamo> findAllByClienteFecha(@Param("cliente") Cliente cliente, @Param("dateI") LocalDateTime dateI, 
			@Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	//ESTADO + FECHA
	final static String queryClienteEstadoFecha = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.estado = :estado "
			+ "and r.fecha > :dateI and r.fecha < :dateF "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente)";
		
	@Query(queryClienteEstadoFecha)
	Page<Reclamo> findAllByClienteEstadoFecha(@Param("cliente") Cliente cliente, @Param("estado") EnumEstadoReclamo estado, 
			@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	//RESTAURANTE + ESTADO
	final static String queryClienteRestauranteEstado = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.estado = :estado "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente) "
			+ "and r.restaurante IN "
			+ "(SELECT res "
			+ "FROM Restaurante res "
			+ "WHERE UPPER(res.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
			+ "or UPPER(res.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
		
	@Query(queryClienteRestauranteEstado)
	Page<Reclamo> findAllByClienteRestauranteEstado(@Param("cliente") Cliente cliente, @Param("restaurante") String restaurante, 
			@Param("estado") EnumEstadoReclamo estado, Pageable pageable);
	
	//RESTAURANTE + FECHA
	final static String queryClienteRestauranteFecha = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente) "
			+ "and r.restaurante IN "
			+ "(SELECT res "
			+ "FROM Restaurante res "
			+ "WHERE UPPER(res.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
			+ "or UPPER(res.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
		
	@Query(queryClienteRestauranteFecha)
	Page<Reclamo> findAllByClienteRestauranteFecha(@Param("cliente") Cliente cliente, @Param("restaurante") String restaurante, 
			@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	//RESTAURANTE + FECHA + ESTADO
	final static String queryClienteRestauranteEstadoFecha = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
			+ "and r.estado = :estado "
			+ "and r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente) "
			+ "and r.restaurante IN "
			+ "(SELECT res "
			+ "FROM Restaurante res "
			+ "WHERE UPPER(res.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
			+ "or UPPER(res.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
		
	@Query(queryClienteRestauranteEstadoFecha)
	Page<Reclamo> findAllByClienteRestauranteEstadoFecha(@Param("cliente") Cliente cliente, @Param("restaurante") String restaurante, 
			@Param("estado") EnumEstadoReclamo estado, @Param("dateI") LocalDateTime dateI, 
			@Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	/////////// FUNCIONES DE RESTAURANTE ///////////
	////////////////////////////////////////////////
	
	//CLIENTE
	final static String queryRestauranteCliente = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.pedido IN "
		+ "(SELECT ped.id "
		+ "FROM Pedido ped "
		+ "WHERE ped.cliente IN "
		+ "(SELECT cl "
		+ "FROM Cliente cl "
		+ "WHERE UPPER(cl.mail) LIKE CONCAT('%',UPPER(:cliente),'%') "
		+ "or UPPER(cl.nickname) LIKE CONCAT('%',UPPER(:cliente),'%'))) "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteCliente)
	Page<Reclamo> findAllByRestauranteCliente(@Param("cliente") String cliente, @Param("restaurante") Restaurante restaurante, 
			Pageable pageable);
	
	//ESTADO
	final static String queryRestauranteEstado = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.estado = :estado "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteEstado)
	Page<Reclamo> findAllByRestauranteEstado(@Param("estado") EnumEstadoReclamo estado, @Param("restaurante") Restaurante restaurante, 
			Pageable pageable);
	
	//FECHA
	final static String queryRestauranteFecha = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteFecha)
	Page<Reclamo> findAllByRestauranteFecha(@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, 
			@Param("restaurante") Restaurante restaurante, 
			Pageable pageable);

	//CLIENTE + ESTADO
	final static String queryRestauranteClienteEstado = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.estado = :estado "
		+ "and r.pedido IN "
		+ "(SELECT ped.id "
		+ "FROM Pedido ped "
		+ "WHERE ped.cliente IN "
		+ "(SELECT cl "
		+ "FROM Cliente cl "
		+ "WHERE UPPER(cl.mail) LIKE CONCAT('%',UPPER(:cliente),'%') "
		+ "or UPPER(cl.nickname) LIKE CONCAT('%',UPPER(:cliente),'%'))) "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteClienteEstado)
	Page<Reclamo> findAllByRestauranteClienteEstado(@Param("cliente") String cliente, @Param("restaurante") Restaurante restaurante, 
			@Param("estado") EnumEstadoReclamo estado, Pageable pageable);
	
	//CLIENTE + FECHA
	final static String queryRestauranteClienteFecha = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
		+ "and r.pedido IN "
		+ "(SELECT ped.id "
		+ "FROM Pedido ped "
		+ "WHERE ped.cliente IN "
		+ "(SELECT cl "
		+ "FROM Cliente cl "
		+ "WHERE UPPER(cl.mail) LIKE CONCAT('%',UPPER(:cliente),'%') "
		+ "or UPPER(cl.nickname) LIKE CONCAT('%',UPPER(:cliente),'%'))) "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteClienteFecha)
	Page<Reclamo> findAllByRestauranteClienteFecha(@Param("cliente") String cliente, @Param("restaurante") Restaurante restaurante, 
			@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	//ESTADO + FECHA
	final static String queryRestauranteEstadoFecha = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.fecha > :dateI and r.fecha < :dateF "
		+ "and r.estado = :estado "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteEstadoFecha)
	Page<Reclamo> findAllByRestauranteEstadoFecha(@Param("restaurante") Restaurante restaurante, @Param("estado") EnumEstadoReclamo estado,
			@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, Pageable pageable);
	
	//CLIENTE + ESTADO + FECHA
	final static String queryRestauranteClienteEstadoFecha = 
		"SELECT r "
		+ "FROM Reclamo r "
		+ "WHERE r.estado = :estado "
		+ "and r.fecha > :dateI and r.fecha < :dateF "
		+ "and r.pedido IN "
		+ "(SELECT ped.id "
		+ "FROM Pedido ped "
		+ "WHERE ped.cliente IN "
		+ "(SELECT cl "
		+ "FROM Cliente cl "
		+ "WHERE UPPER(cl.mail) LIKE CONCAT('%',UPPER(:cliente),'%') "
		+ "or UPPER(cl.nickname) LIKE CONCAT('%',UPPER(:cliente),'%'))) "
		+ "and r.restaurante = :restaurante";
		
	@Query(queryRestauranteClienteEstadoFecha)
	Page<Reclamo> findAllByRestauranteClienteEstadoFecha(@Param("cliente") String cliente, @Param("restaurante") Restaurante restaurante, 
			@Param("estado") EnumEstadoReclamo estado, @Param("dateI") LocalDateTime dateI, 
			@Param("dateF") LocalDateTime dateF, Pageable pageable);

	//genérica Cliente
	final static String queryCliente = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE ped.cliente = :cliente)";
	
	@Query(queryCliente)
	Page<Reclamo> findAllByCliente(@Param("cliente") Cliente cliente, Pageable pageable);
	
	//genérica restaurante
//	final static String queryRestaurante = 
//			"SELECT r "
//			+ "FROM Reclamo r "
//			+ "WHERE r.pedido IN "
//			+ "(SELECT ped.id "
//			+ "FROM Pedido ped "
//			+ "WHERE ped.restaurante = :restaurante)";
//	
//	@Query(queryRestaurante)
//	Page<Reclamo> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
}