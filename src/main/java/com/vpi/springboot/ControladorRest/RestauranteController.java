package com.vpi.springboot.ControladorRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTRespuesta;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {
	

}
