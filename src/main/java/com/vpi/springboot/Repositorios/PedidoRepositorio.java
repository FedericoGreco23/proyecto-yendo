package com.vpi.springboot.Repositorios;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Restaurante;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {
	@Query("SELECT u FROM Pedido u WHERE u.restaurante = :restaurante")
	Page<Pedido> findAllByRestaurante(@Param("restaurante") Restaurante restaurante, Pageable pageable);

	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente")
	Page<Pedido> findAllByCliente(@Param("cliente") Cliente cliente, Pageable pageable);

//	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente")
//	Page<Pedido> findAllByClienteNombre(@Param("cliente") Cliente restaurante, Pageable pageable);
//	
//	@Query("SELECT u FROM Pedido u WHERE u.cliente = :cliente")
//	Page<Pedido> findAllByClienteMail(@Param("cliente") Cliente restaurante, Pageable pageable);

	@Query("SELECT a FROM Pedido a WHERE UPPER(a.id) LIKE CONCAT('%',UPPER(:id),'%')")
	Page<Pedido> findPageById(@Param("id") int id, Pageable pageable);
	
	@Query("SELECT a FROM Pedido a WHERE a.fecha > :dateI and a.fecha < :dateF")
	Page<Pedido> findPageByDate(@Param("date") LocalDateTime dateI, LocalDateTime dateF, Pageable pageable);

	@Query("SELECT u FROM Pedido u WHERE u.id = :numeroPedido")
	Pedido buscarPedidoPorNumero(@Param("numeroPedido") int numeroPedido);

}