package com.vpi.springboot.mongoServices;

import javax.validation.ConstraintViolationException;

import com.vpi.springboot.Modelo.Pedidos;
import com.vpi.springboot.exception.PedidosException;

public interface PedidosService {

	public void createTodo(Pedidos pedido) throws ConstraintViolationException, PedidosException;
}
