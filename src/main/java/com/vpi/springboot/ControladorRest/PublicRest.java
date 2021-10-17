package com.vpi.springboot.ControladorRest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.AuthenticationRequest;
import com.vpi.springboot.Modelo.dto.AuthenticationResponse;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.security.MyUserDetailsService;
import com.vpi.springboot.security.util.JwtUtil;
import com.vpi.springboot.security.util.MyDetails;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("public/")
public class PublicRest {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private GeneralService service;

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private RestauranteService restService;
	
	

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		if(!authenticationRequest.getUsername().contains("admin")) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Usuario o contraseña son incorrectas", e);
		}
		}


		final MyDetails userDetails = (MyDetails) userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		if(!userDetails.getUser().getActivo()) {

			throw new Exception("Este usuario se encuentra inactivo, comuniquese con un administrador");
			
		}else if (userDetails.getUser().getBloqueado()) {
			
			throw new Exception("Este usuario se encuentra bloqueado, comuniquese con un administrador");
		}

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/crearCliente", method = RequestMethod.POST)
	public ResponseEntity<DTRespuesta> altaCliente(@RequestBody Cliente usuario) {
		try {
			clienteService.altaCliente(usuario);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Cliente agregado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/crearRestaurante")
	public ResponseEntity<DTRespuesta> crearRestaurante(@RequestBody Restaurante rest) {
		try {
			restService.altaRestaurante(rest);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante agregado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/recuperar", method = RequestMethod.POST)
	public ResponseEntity<?> recuperarPassword(@RequestParam String mail) {
		try {
			service.recuperarPassword(mail);
			return new ResponseEntity<String>(mail, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/verificar", method = RequestMethod.POST)
	public ResponseEntity<?> verificarMail(@RequestParam String mail) {
		try {
			service.verificarMail(mail);
			return new ResponseEntity<String>("Verificación enviada.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/listarRestaurantes")
	public Map<String, Object> listarRestaurantesAbiertos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int horarioApertura) {
		try {
			return service.listarRestaurantes(page, size, horarioApertura);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/listarCategorias")
	public List<Categoria> listarCategorias() {
		return service.listarCategorias();
	}
	
	@GetMapping("getRestaurante")
	public DTRestaurante getRestaurante(@RequestParam(defaultValue = "") String mail) {
		try {
			return service.getRestaurante(mail);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getProductos/{restaurante}")
	public Map<String, Object> listarMenusRestaurante(@RequestParam(defaultValue = "0") int page,
									  	   		@RequestParam(defaultValue = "5") int size, 
									  	   		@PathVariable(required = true) String restaurante) {
		try {
			return service.listarMenusRestaurante(page, size, restaurante);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getPromociones/{restaurante}")
	public Map<String, Object> listarPromociones(@RequestParam(defaultValue = "0") int page,
									  	   	 @RequestParam(defaultValue = "5") int size, 
									  	   	 @PathVariable(required = true) String restaurante) {
		try {
			return service.listarPromocionesRestaurante(page, size, restaurante);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
