package com.vpi.springboot.ControladorRest;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {

	@Autowired
	private RestauranteService service;

//	@GetMapping("/getallClientes")
//	public List<Cliente> getAllUser() {
//		return userService.getAllClientes();
//	}

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

	@PostMapping("/eliminarMenu/{id}")
	public ResponseEntity<?> bajaMenu(@PathVariable int id) {
		try {
			service.bajaMenu(id);
			return new ResponseEntity<String>("Eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

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
}