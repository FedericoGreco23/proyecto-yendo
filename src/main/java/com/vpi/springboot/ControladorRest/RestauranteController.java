package com.vpi.springboot.ControladorRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.dto.DTRespuesta;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	

	@PostMapping("/abrirRestaurante")
	ResponseEntity<?> abrirRestaurante(@RequestParam String mail){
		try {
			restauranteService.abrirRestaurante(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante abierto"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/cerrarRestaurante")
	ResponseEntity<?> cerrarRestaurante(@RequestParam String mail) {
		try {
			restauranteService.cerrarRestaurante(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante cerrado"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
