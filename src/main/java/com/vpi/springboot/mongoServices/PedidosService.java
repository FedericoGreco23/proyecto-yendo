package com.vpi.springboot.mongoServices;

import java.util.List;

import javax.validation.ConstraintViolationException;

import com.vpi.springboot.Modelo.Pedidos;
import com.vpi.springboot.exception.PedidosException;

public interface PedidosService {

	public void createTodo(Pedidos pedido) throws ConstraintViolationException, PedidosException;
	
	public List<Pedidos> getAllPedidos();
	
	public Pedidos getPedido(String id) throws PedidosException;
}
