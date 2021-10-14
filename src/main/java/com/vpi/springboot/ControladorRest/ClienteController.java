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
import com.vpi.springboot.Modelo.dto.DTRespuesta;
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

	@PostMapping(path = "/agregarDireccion", produces = "application/json")
	@ResponseBody
	public ResponseEntity<DTRespuesta> agregarDireccion(@RequestBody DTDireccion direccion, @RequestParam String mail) {
		try {
			clienteService.altaDireccion(direccion, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Direccion agregada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("bajaCuenta")
	public ResponseEntity<?> bajaCuenta(@RequestParam String mail) {
		try {
			clienteService.bajaCuenta(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Cuenta dada de baja con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("modificarDireccion")
	public ResponseEntity<?> modificarDireccion(@RequestParam int id, @RequestParam String mail,
			@RequestBody DTDireccion direccionNueva) {
		try {
			clienteService.modificarDireccion(id, direccionNueva, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Dirección modificada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("eliminarDireccion")
	public ResponseEntity<?> modificarDireccion(@RequestParam Integer id, @RequestParam String mail) {
		try {
			clienteService.eliminarDireccion(id, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Dirección eliminada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}