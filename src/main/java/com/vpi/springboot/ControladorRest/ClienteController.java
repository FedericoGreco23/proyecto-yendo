package com.vpi.springboot.ControladorRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.Usuario;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController 
@RequestMapping("api/cliente/")
public class ClienteController {
		
		@Autowired
		private ClienteService clienteService;

		@GetMapping("/obtenerTodos")
		public List<Cliente> getAllUser() {
			return clienteService.obtenerClientes();
		}


		@PostMapping("/crear")
		public ResponseEntity<?> altaCliente(@RequestBody Cliente usuario) {
			try {
				clienteService.altaCliente(usuario);
				return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@PostMapping("/agregarDireccion")
		public ResponseEntity<?> agregarDireccion(@RequestBody Direccion direccion, @RequestHeader String mail) {
			try {
				clienteService.altaDireccion(direccion, mail);
				return new ResponseEntity<String>("Direccion agragada", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@PostMapping("bajaCuenta")
		public ResponseEntity<?> bajaCuenta(@RequestHeader String mail){
			try {
				clienteService.bajaCuenta(mail);
				return new ResponseEntity<String>("Cuenta dada de baja", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		


}