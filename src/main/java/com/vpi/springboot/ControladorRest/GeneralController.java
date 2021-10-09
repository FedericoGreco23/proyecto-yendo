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

import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/general/")
public class GeneralController {

	@Autowired
	private GeneralService service;

	@GetMapping("/getUsuarios")
	public List<String> getUsuarios() {
		return service.listarUsuariosRegistrados();
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
			return new ResponseEntity<String>("Verificado correctamente.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/activar")
	public ResponseEntity<?> activarCuenta(@RequestParam String mail, @RequestParam String tipo) {
		try {
			service.activarCuenta(mail, tipo);
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
}