package com.vpi.springboot.ControladorRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTUsuario;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("public/")
public class GeneralController {

	@Autowired
	private GeneralService service;

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/getUsuarios")
	public List<DTUsuario> getUsuarios(@RequestParam(defaultValue = "0") int page, 
									@RequestParam(defaultValue = "5") int size,
									@RequestParam(defaultValue = "0") int tipoUsuario) {
		return service.listarUsuariosRegistrados(page, size, tipoUsuario);
	}

	@PostMapping("/recuperar")
	public ResponseEntity<?> recuperarPassword(@RequestParam String mail) {
		try {
			service.recuperarPassword(mail);
			return new ResponseEntity<String>(mail, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/verificar")
	public ResponseEntity<?> verificarMail(@RequestParam String mail) {
		try {
			service.verificarMail(mail);
			return new ResponseEntity<String>("Verificación enviada.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/activar")
	public ResponseEntity<?> activarCuenta(@RequestParam(required = true) String mail, 
										   @RequestParam(required = true) int tipoUsuario) {
		try {
			System.out.println("Dentro de GeneralController /activar");
			service.activarCuenta(mail, tipoUsuario);
			return new ResponseEntity<String>("Cuenta " + mail + " activada.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> iniciarSesion(@RequestParam String mail, @RequestParam String pass) {
		try {
			String response = service.iniciarSesion(mail, pass);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@PostMapping("/crear")
	public ResponseEntity<DTRespuesta> altaCliente(@RequestBody Cliente usuario) {
		try {
			clienteService.altaCliente(usuario);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Cliente agregado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}