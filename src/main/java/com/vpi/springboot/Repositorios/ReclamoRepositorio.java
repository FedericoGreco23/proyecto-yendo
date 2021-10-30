package com.vpi.springboot.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;

@Repository
public interface ReclamoRepositorio extends JpaRepository<Reclamo, Integer> {
	@Query("SELECT u FROM Reclamo u WHERE u.restaurante = :restaurante")
	Page<Reclamo> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);
	
	//TODO pensar si van las siguientes:
	//id / restaurante / estado
	
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
	
	//RESTAURANTE
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