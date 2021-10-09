package com.vpi.springboot.ControladorRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.UsuarioService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController 
@RequestMapping("public/")
public class UserController {
	
	@Autowired
	private UsuarioService userService;
	
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getallClientes")
	public List<Cliente> getAllUser() {
		return userService.getAllClientes();
	}


	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crear")
	public ResponseEntity<?> createTodo(@RequestBody Cliente usuario) {
		try {
			userService.createTodo(usuario);
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	}