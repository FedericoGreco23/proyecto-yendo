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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.UsuarioService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;
import com.vpi.springboot.Modelo.dto.DTListasTiposDeUsuarios;
import com.vpi.springboot.Modelo.dto.DTUsuario;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController 
@RequestMapping("api/usuario/")
public class UserController {
	
	@Autowired
	private UsuarioService userService;

	@GetMapping("/getallClientes")
	public List<Cliente> getAllUser() {
		return userService.getAllClientes();
	}

	@PostMapping("/crear")
	public ResponseEntity<?> createTodo(@RequestBody Cliente usuario) {
		try {
			userService.createTodo(usuario);
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/buscarUsuario")
	public List<DTUsuario> buscarUsuario(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int tipoUsuario, @RequestParam Integer antiguedadUsuario, @RequestParam String texto) {
		return userService.buscarUsuario(page, size, tipoUsuario, antiguedadUsuario, texto);
	}


}