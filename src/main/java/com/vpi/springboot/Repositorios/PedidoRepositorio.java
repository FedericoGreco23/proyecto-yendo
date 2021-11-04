package com.vpi.springboot.Repositorios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {
	@Query("SELECT u FROM Pedido u WHERE u.restaurante = :restaurante")
	Page<Pedido> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);

	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente and u.costoTotal > 10")
	Page<Pedido> findAllByCliente(@Param("cliente") Cliente cliente, Pageable pageable);

//	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente")
//	Page<Pedido> findAllByClienteNombre(@Param("cliente") Cliente restaurante, Pageable pageable);
//	
//	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente")
//	Page<Pedido> findAllByClienteMail(@Param("cliente") Cliente restaurante, Pageable pageable);

	@Query("SELECT a FROM Pedido a WHERE UPPER(a.id) LIKE CONCAT('%',UPPER(:id),'%')")
	Page<Pedido> findPageById(@Param("id") int id, Pageable pageable);
	
	@Query("SELECT u FROM Pedido u WHERE u.id = :numeroPedido")
	Pedido buscarPedidoPorNumero(@Param("numeroPedido") int numeroPedido);
	
	@Query("SELECT u FROM Pedido u WHERE u.restaurante = :restaurante and u.cliente = :cliente")
	List<Pedido> findByClienteRestaurante(@Param("cliente") Cliente cliente, @Param("restaurante") Restaurante restaurante);
	
	//////////////FUNCIONES PARA LISTAR PEDIDOS RESTAURANTE//////////////
	/////////////////////////////////////////////////////////////////////
	
	//CLIENTE FECHA ESTADO
	final static String queryClienteFechaEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF "
			+ "and ped.estadoPedido = :estado "
			+ "and ped.cliente IN "
			+ "(SELECT u.mail "
			+ "FROM Cliente u "
			+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:mail),'%')"
			+ "or UPPER(u.nickname) LIKE CONCAT('%',UPPER(:mail),'%'))";
	
	@Query(queryClienteFechaEstado)
	Page<Pedido> findByClienteFechaEstado(@Param("mail") String mail, @Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("estado") EnumEstadoPedido estado, @Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//CLIENTE FECHA 
	final static String queryClienteFecha = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF "
			+ "and ped.cliente IN "
			+ "(SELECT u.mail "
			+ "FROM Cliente u "
			+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:mail),'%')"
			+ "or UPPER(u.nickname) LIKE CONCAT('%',UPPER(:mail),'%'))";
	
	@Query(queryClienteFecha)
	Page<Pedido> findByClienteFecha(@Param("mail") String mail, @Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//CLIENTE ESTADO
	final static String queryClienteEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.estadoPedido = :estado "
			+ "and ped.cliente IN "
			+ "(SELECT u.mail "
			+ "FROM Cliente u "
			+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:mail),'%')"
			+ "or UPPER(u.nickname) LIKE CONCAT('%',UPPER(:mail),'%'))";
	
	@Query(queryClienteEstado)
	Page<Pedido> findByClienteEstado(@Param("mail") String mail, @Param("estado") EnumEstadoPedido estado, 
			@Param("restaurante") Restaurante restaurante, Pageable pageable);

	//CLIENTE
	final static String queryCliente = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.cliente IN "
			+ "(SELECT u.mail "
			+ "FROM Cliente u "
			+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:mail),'%')"
			+ "or UPPER(u.nickname) LIKE CONCAT('%',UPPER(:mail),'%'))";
	
	@Query(queryCliente)
	Page<Pedido> findByCliente(@Param("mail") String mail, @Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	
	
	//ID FECHA ESTADO
	final static String queryIdFechaEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.id = :id "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF "
			+ "and ped.estadoPedido = :estado";
	
	@Query(queryIdFechaEstado)
	Page<Pedido> findByIdFechaEstado(@Param("id") int id, @Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("estado") EnumEstadoPedido estado, @Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//ID FECHA
	final static String queryIdFecha = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.id = :id "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF";
	
	@Query(queryIdFecha)
	Page<Pedido> findByIdFecha(@Param("id") int id, @Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//ID ESTADO
	final static String queryIdEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.id = :id "
			+ "and ped.estadoPedido = :estado";
	
	@Query(queryIdEstado)
	Page<Pedido> findByIdEstado(@Param("id") int id, @Param("estado") EnumEstadoPedido estado, 
			@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//ID
	final static String queryId = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.id = :id";
	
	@Query(queryId)
	Page<Pedido> findById(@Param("id") int id, @Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	
	
	//FECHA ESTADO
	final static String queryFechaEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF "
			+ "and ped.estadoPedido = :estado";
	
	@Query(queryFechaEstado)
	Page<Pedido> findByFechaEstado(@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("estado") EnumEstadoPedido estado, @Param("restaurante") Restaurante restaurante, Pageable pageable);
		
	//FECHA
	final static String queryFecha = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.fecha > :dateI "
			+ "and ped.fecha < :dateF";
	
	@Query(queryFecha)
	Page<Pedido> findByFecha(@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//ESTADO
	final static String queryEstado = 
			"SELECT ped FROM Pedido ped "
			+ "WHERE ped.restaurante = :restaurante "
			+ "and ped.costoTotal > 0 "
			+ "and ped.estadoPedido = :estado";
	
	@Query(queryEstado)
	Page<Pedido> findByEstado(@Param("estado") EnumEstadoPedido estado, Restaurante restaurante, Pageable pageable);
	
	
	//////////////FUNCIONES PARA LISTAR PEDIDOS CLIENTE//////////////
	/////////////////////////////////////////////////////////////////
	
	//RESTAURANTE FECHA 
	final static String queryRestauranteFecha = 
		"SELECT ped FROM Pedido ped "
		+ "WHERE ped.cliente = :cliente "
		+ "and ped.costoTotal > 0 "
		+ "and ped.fecha > :dateI "
		+ "and ped.fecha < :dateF "
		+ "and ped.restaurante IN "
		+ "(SELECT u.mail "
		+ "FROM Restaurante u "
		+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
		+ "or UPPER(u.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
	
	@Query(queryRestauranteFecha)
	Page<Pedido> findByRestauranteFecha(@Param("restaurante") String restaurante, @Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF,
			@Param("cliente") Cliente cliente, Pageable pageable);
	
	//CLIENTE FECHA ESTADO
	final static String queryRestaurante = 
		"SELECT ped FROM Pedido ped "
		+ "WHERE ped.cliente = :cliente "
		+ "and ped.costoTotal > 0 "
		+ "and ped.restaurante IN "
		+ "(SELECT u.mail "
		+ "FROM Restaurante u "
		+ "WHERE UPPER(u.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
		+ "or UPPER(u.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
	
	@Query(queryRestaurante)
	Page<Pedido> findByRestaurante(@Param("restaurante") String restaurante, @Param("cliente") Cliente cliente, Pageable pageable);
	
	//CLIENTE FECHA ESTADO
	final static String queryFecha2 = 
		"SELECT ped FROM Pedido ped "
		+ "WHERE ped.cliente = :cliente "
		+ "and ped.costoTotal > 0 "
		+ "and ped.fecha > :dateI "
		+ "and ped.fecha < :dateF";
	
	@Query(queryFecha2)
	Page<Pedido> findByFecha(@Param("dateI") LocalDateTime dateI, @Param("dateF") LocalDateTime dateF, 
			@Param("cliente") Cliente cliente, Pageable pageable);
}