package com.vpi.springboot.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Modelo.Pedidos;
import com.vpi.springboot.exception.PedidosException;
import com.vpi.springboot.mongoRepository.MongoRepo;
import com.vpi.springboot.mongoServices.PedidosService;

@RestController
public class PedidosController {

	@Autowired
	private MongoRepo mongoRepo;

	@Autowired
	private PedidosService mongoService;

	@GetMapping("/pedidos")
	public ResponseEntity<?> getAllPedidos() {
		List<Pedidos> pedidos = mongoRepo.findAll();
		if (pedidos.size() > 0) {
			return new ResponseEntity<List<Pedidos>>(pedidos, HttpStatus.OK);
		} else
			return new ResponseEntity<>("No se encontraron pedidos", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/pedidosService")
	public ResponseEntity<?> getAllPedidosService() {
		List<Pedidos> pedidos = mongoService.getAllPedidos();
		return new ResponseEntity<>(pedidos, pedidos.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	@PostMapping("/pedidos")
	public ResponseEntity<?> createTodo(@RequestBody Pedidos pedido) {
		try {
			pedido.setCreatedAt(new Date(System.currentTimeMillis()));
			mongoRepo.save(pedido);
			return new ResponseEntity<Pedidos>(pedido, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/pedidosService")
	public ResponseEntity<?> createTodoService(@RequestBody Pedidos pedido) {
		try {
			mongoService.createTodo(pedido);
			return new ResponseEntity<Pedidos>(pedido, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (PedidosException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/pedidos/{id}")
	public ResponseEntity<?> getSinglePedido(@PathVariable("id") String id) {
		Optional<Pedidos> pedidosOptional = mongoRepo.findById(id);
		if (pedidosOptional.isPresent()) {
			return new ResponseEntity<>(pedidosOptional.get(), HttpStatus.OK);
		} else
			return new ResponseEntity<>("no se encontre un pedido con la id " + id, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/pedidosService/{id}")
	public ResponseEntity<?> getSinglePedidoService(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(mongoService.getPedido(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("No se encontre un pedido con la id " + id, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/pedidos/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody Pedidos pedido) {
		Optional<Pedidos> pedidosOptional = mongoRepo.findById(id);
		if (pedidosOptional.isPresent()) {
			Pedidos pedidostoSave = pedidosOptional.get();
			pedidostoSave.setComida(pedido.getComida() != null ? pedido.getComida() : pedidostoSave.getComida());
			pedidostoSave.setValoracion(pedido.getValoracion());
			pedidostoSave.setUpdatedAt(new Date(System.currentTimeMillis()));
			mongoRepo.save(pedidostoSave);
			return new ResponseEntity<Pedidos>(pedidostoSave, HttpStatus.OK);
		} else
			return new ResponseEntity<>("no se encontre un pedido con la id " + id, HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/pedidosService/{id}")
	public ResponseEntity<?> updateByIdService(@PathVariable("id") String id, @RequestBody Pedidos pedido) {
		try {
			mongoService.updatePedido(id, pedido);
			return new ResponseEntity<>("Actualizado pedido " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch(PedidosException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}			
	}

//	@DeleteMapping("/pedidos/{id}")
//	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
//		try {
//			mongoRepo.deleteById(id);
//			return new ResponseEntity<>("Se ha eliminado exitosamente  " + id, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		}
//	}
	
	@DeleteMapping("/pedidos/{id}")
	public ResponseEntity<?> deleteByIdService(@PathVariable("id") String id) {
		try {
			mongoService.deletePedido(id);
			return new ResponseEntity<>("Se ha eliminado exitosamente  " + id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
