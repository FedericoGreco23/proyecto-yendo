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
			+ "WHERE UPPER(ped.cliente) LIKE CONCAT('%',UPPER(:cliente),'%')) "
			+ "and r.restaurante IN "
			+ "(SELECT res.mail "
			+ "FROM Restaurante res "
			+ "WHERE UPPER(res.mail) LIKE CONCAT('%',UPPER(:restaurante),'%') "
			+ "or UPPER(res.nombre) LIKE CONCAT('%',UPPER(:restaurante),'%'))";
		
	@Query(queryClienteRestaurante)
	Page<Reclamo> findAllByClienteRestaurante(@Param("cliente") Cliente cliente, @Param("restaurante") String restaurante, 
			Pageable pageable);
	
	//genérica
	final static String queryCliente = 
			"SELECT r "
			+ "FROM Reclamo r "
			+ "WHERE r.pedido IN "
			+ "(SELECT ped.id "
			+ "FROM Pedido ped "
			+ "WHERE UPPER(ped.cliente) LIKE CONCAT('%',UPPER(:cliente),'%'))";
	
	@Query(queryCliente)
	Page<Reclamo> findAllByCliente(@Param("cliente") Cliente cliente, Pageable pageable);

}