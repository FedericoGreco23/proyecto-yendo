package com.vpi.springboot.ControladorRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Usuario;
import com.vpi.springboot.Modelo.dto.DTDireccion;

import com.vpi.springboot.exception.UsuarioException;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/cliente/")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/getDireccion/{mail}")
	public List<DTDireccion> getDireccionUsuario(@PathVariable String mail) {
		try {
			return clienteService.getDireccionCliente(mail);
		} catch (UsuarioException e) {
			return null;
		}
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

		

		@PostMapping(path="/agregarDireccion", produces = "application/json")
		@ResponseBody
		public ResponseEntity<?> agregarDireccion(@RequestBody DTDireccion direccion, @RequestParam String mail) {
			try {
				clienteService.altaDireccion(direccion, mail);
				return new ResponseEntity<DTDireccion>(direccion, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@PostMapping("bajaCuenta")
		public ResponseEntity<?> bajaCuenta(@RequestParam String mail){
			try {
				clienteService.bajaCuenta(mail);
				return new ResponseEntity<String>("Cuenta dada de baja", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
	/*	
		@PostMapping("modificarDireccion")
		public ResponseEntity<?> modificarDireccion(@RequestBody Direccion direccionVieja, 
													@RequestBody DTDireccion direccionNueva,
													@PathVariable String mail){
			try {
				clienteService.modificarDireccion(direccionVieja,direccionNueva,mail);
				return new ResponseEntity<String>("Direccion modificada", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	*/

		@PostMapping("modificarDireccion")
		public ResponseEntity<?> modificarDireccion(@RequestBody Direccion direccionVieja,
													@RequestParam String calleNro,
													@RequestParam String mail,
													@RequestParam Double latitud,
													@RequestParam Double longitud
													){
			DTDireccion direccionNueva = new DTDireccion(calleNro, new GeoLocalizacion(latitud, longitud));
			try {
				clienteService.modificarDireccion(direccionVieja,direccionNueva,mail);
				return new ResponseEntity<String>("Direccion modificada", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@PostMapping("eliminarDireccion")
		public ResponseEntity<?> modificarDireccion(@RequestParam Integer id,
													@RequestParam String mail){
			try {
				clienteService.eliminarDireccion(id,mail);
				return new ResponseEntity<String>("Direccion eliminada", HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
}