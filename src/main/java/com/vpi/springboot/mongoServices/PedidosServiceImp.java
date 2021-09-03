package com.vpi.springboot.mongoServices;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Pedidos;
import com.vpi.springboot.exception.PedidosException;
import com.vpi.springboot.mongoRepository.MongoRepo;

@Service
public class PedidosServiceImp implements PedidosService {
	
	@Autowired
	private MongoRepo repo;
	
	@Override
	public void createTodo(Pedidos pedido) throws ConstraintViolationException, PedidosException {
		Optional<Pedidos> optionalPedidos = repo.findById(pedido.getId());
		if(optionalPedidos.isPresent()) {
			throw new PedidosException(PedidosException.PedidoYaExiste());
		}else {
			pedido.setCreatedAt(new Date(System.currentTimeMillis()));
			repo.save(pedido);
		}
	}

	@Override
	public List<Pedidos> getAllPedidos() {
		List<Pedidos> pedidos = repo.findAll();
		if(pedidos.size() > 0) {
			return pedidos;
		}else {
			return new ArrayList<Pedidos>();
		}
	}

	@Override
	public Pedidos getPedido(String id) throws PedidosException {
		Optional<Pedidos> pedido = repo.findById(id);
		if(pedido.isPresent()) {
			return pedido.get();
		}else {
			throw new PedidosException(PedidosException.NotFoundException(id));
		}
	}

}
