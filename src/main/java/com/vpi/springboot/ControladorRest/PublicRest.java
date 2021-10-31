package com.vpi.springboot.ControladorRest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
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
	

	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		if (!authenticationRequest.getUsername().contains("admin")) {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			} catch (BadCredentialsException e) {
				throw new Exception("Usuario o contraseña son incorrectas", e);
			}
		}

		final MyDetails userDetails = (MyDetails) userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		if (!userDetails.getUser().getActivo()) {
			throw new Exception("Este usuario se encuentra inactivo, comuniquese con un administrador");
		} else if (userDetails.getUser().getBloqueado()) {
			throw new Exception("Este usuario se encuentra bloqueado, comuniquese con un administrador");
		} else if (userDetails.getUser().getClass() == Restaurante.class) {
			Restaurante r = (Restaurante) userDetails.getUser();
			if (r.getEstado().name().contains(EnumEstadoRestaurante.EN_ESPERA.name())) {

				throw new Exception("Este restaurante se encuentra en espera de aprobación");
			} else if (r.getEstado().name().contains(EnumEstadoRestaurante.RECHAZADO.name())) {

				throw new Exception("Este restaurante fue rechazado");
			}
		}

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/crearCliente", method = RequestMethod.POST)
	public ResponseEntity<?> altaCliente(@RequestBody Cliente usuario) {
		try {
			return new ResponseEntity<>(clienteService.altaCliente(usuario), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/crearRestaurante")
	public ResponseEntity<?> crearRestaurante(@RequestBody Restaurante rest) {
		try {
			return new ResponseEntity<>(restService.altaRestaurante(rest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/recuperar", method = RequestMethod.POST)
	public ResponseEntity<?> recuperarPassword(@RequestParam String mail) {
		try {
			return new ResponseEntity<>(service.recuperarPassword(mail), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/verificar", method = RequestMethod.POST)
	public ResponseEntity<?> verificarMail(@RequestParam String mail) {
		try {
			return new ResponseEntity<>(service.verificarMail(mail), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/listarRestaurantes")
	public Map<String, Object> listarRestaurantesAbiertos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int horarioApertura, 
			@RequestParam(defaultValue = "") String nombre, @RequestParam(defaultValue = "")String categoria, 
			@RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "0") int order) {
		try {
			return service.listarRestaurantes(page, size, horarioApertura, nombre, categoria, sort, order);
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
	public ResponseEntity<DTRestaurante> getRestaurante(@RequestParam(defaultValue = "") String mail) {
		try {
			DTRestaurante respuesta = service.getRestaurante(mail);
			return new ResponseEntity<DTRestaurante>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRestaurante>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/buscarRestaurante")
	public ResponseEntity<?> buscarRestaurante(@RequestParam(defaultValue = "") String texto, @RequestParam(defaultValue = "") String nombreCategoria) {
		try {
			List<DTBuscarRestaurante> respuesta = service.buscarRestaurante(texto, nombreCategoria);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@CrossOrigin(origins = "*", allowedHeaders = "*")
//	@GetMapping("/getProductos/{restaurante}")
//	public Map<String, Object> listarMenusRestaurante(@RequestParam(required = false) String attr,
//			@RequestParam(defaultValue = "1") int order, @RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "5") int size, @PathVariable(required = true) String restaurante) {
//		try {
//			return service.listarMenusRestaurante(attr, order, page, size, restaurante);
//		} catch (RestauranteException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getMenus/{restaurante}")
	public ResponseEntity<?> listarMenus(@PathVariable(required = true) String restaurante) {
		try {
			return new ResponseEntity<>(service.listarMenus(restaurante), HttpStatus.OK);
		} catch (RestauranteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getPromociones/{restaurante}")
	public ResponseEntity<?> listarPromociones(@PathVariable(required = true) String restaurante) {
		try {
			return new ResponseEntity<>(service.listarPromocionesRestaurante(restaurante), HttpStatus.OK);
		} catch (RestauranteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/buscarMenusPromociones/{restaurante}")
	public ResponseEntity<?> buscarMenusPromociones(@PathVariable(required = true) String restaurante,
			@RequestParam(required = true) String producto) {
		try {
			return new ResponseEntity<>(service.buscarMenusPromociones(restaurante, producto), HttpStatus.OK);
		} catch (RestauranteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/cargarDatos")
	public String cargarDatos() {
		try {
			//restService.cargarDatos();
			restService.cargarDatos();
		}catch(Exception e) {
			return "oops, estimado frontend algo se ha ido a la mierda. Consulte a su backend de confianza"; 
		}
		return "rock and roll nene!";
	}
	

}