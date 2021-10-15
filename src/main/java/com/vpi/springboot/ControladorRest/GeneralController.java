package com.vpi.springboot.ControladorRest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.exception.RestauranteException;

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
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getProductos/{restaurante}")
	public Map<String, Object> getMenusRestaurante(@RequestParam(defaultValue = "0") int page,
									  	   		@RequestParam(defaultValue = "5") int size, 
									  	   		@PathVariable(required = true) String restaurante) {
		try {
			return service.listarMenusRestaurante(page, size, restaurante);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}
}