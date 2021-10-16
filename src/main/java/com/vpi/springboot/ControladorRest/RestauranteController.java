package com.vpi.springboot.ControladorRest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.RestauranteException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {
	
	@Autowired
	private RestauranteService service;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crearMenu/{varRestaurante}")
	public ResponseEntity<?> altaMenu(@RequestBody Producto menu, @PathVariable (required = true) String varRestaurante) {
		try {
			service.altaMenu(menu, varRestaurante);
			return new ResponseEntity<Producto>(menu, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminarMenu/{id}")
	public ResponseEntity<?> bajaMenu(@PathVariable int id) {
		try {
			service.bajaMenu(id);
			return new ResponseEntity<String>("Eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/modificarMenu")
	public ResponseEntity<?> modificarMenus(@RequestBody Producto menu) {
		try {
			service.modificarMenu(menu);
			return new ResponseEntity<Producto>(menu, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getPedidos/{restaurante}")
	public Map<String, Object> listarPedidos(@RequestParam(defaultValue = "0") int page,
									  	   	 @RequestParam(defaultValue = "5") int size, 
									  	   	 @PathVariable(required = true) String restaurante) {
		try {
			return service.listarPedidos(page, size, restaurante);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/abrirRestaurante")
	ResponseEntity<?> abrirRestaurante(@RequestParam String mail){
		try {
			service.abrirRestaurante(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante abierto"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/cerrarRestaurante")
	ResponseEntity<?> cerrarRestaurante(@RequestParam String mail) {
		try {
			service.cerrarRestaurante(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante cerrado"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
