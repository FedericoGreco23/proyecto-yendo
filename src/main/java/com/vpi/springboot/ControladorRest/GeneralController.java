package com.vpi.springboot.ControladorRest;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/general/")
public class GeneralController {

	@Autowired
	private GeneralService service;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
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

	@PostMapping("registrarPago")
	public ResponseEntity<?> registrarPago(@RequestParam(required = true) int idPedido) {
		try {
			DTRespuesta respuesta = service.registrarPago(idPedido);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("devolucionPedido")
	public ResponseEntity<?> devolucionPedido(@RequestBody(required = true) Pedido pedido) {
		try {
			return new ResponseEntity<>(service.devolucionPedido(pedido), HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}