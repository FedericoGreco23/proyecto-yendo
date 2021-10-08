package com.vpi.springboot.ControladorRest;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.AdministradorService;
import com.vpi.springboot.Modelo.Administrador;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController 
@RequestMapping("api/admin/")
public class AdministradorController {
	
	@Autowired
	private AdministradorService service;

//	@GetMapping("/getallClientes")
//	public List<Cliente> getAllUser() {
//		return userService.getAllClientes();
//	}

	@PostMapping("/crear")
	public ResponseEntity<?> altaAdministrador(@RequestBody Administrador admin) {
		try {
			service.crearAdministrador(admin);
			return new ResponseEntity<Administrador>(admin, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	}