package com.vpi.springboot.mongoServices;

import java.sql.Date;
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

}
