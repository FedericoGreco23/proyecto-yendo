package com.vpi.springboot.ControladorRest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.AdministradorService;
import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.exception.PermisosException;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil;
import com.vpi.springboot.security.util.JwtUtil.keyInfoJWT;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/admin/")
public class AdministradorController {

	@Autowired
	private AdministradorService service;
	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HttpServletRequest request;

//	@GetMapping("/getallClientes")
//	public List<Cliente> getAllUser() {
//		return userService.getAllClientes();
//	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crear")
	public ResponseEntity<?> altaAdministrador(@RequestBody Administrador admin) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}
		try {
			return new ResponseEntity<DTRespuesta>(service.crearAdministrador(admin), HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminar")
	public ResponseEntity<?> eliminarUsuario(@RequestParam String mail, @RequestParam String clienteRestaurante) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}
		try {
			return new ResponseEntity<>(service.eliminarUsuario(mail, clienteRestaurante), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/bloquear")
	public ResponseEntity<?> bloquearUsuario(@RequestParam String mail, @RequestParam String clienteRestaurante) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}
		try {
			return new ResponseEntity<>(service.bloquearUsuario(mail, clienteRestaurante), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/desbloquear")
	public ResponseEntity<?> desbloquearUsuario(@RequestParam String mail, @RequestParam String clienteRestaurante) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}
		try {
			return new ResponseEntity<>(service.desbloquearUsuario(mail, clienteRestaurante), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/buscarUsuario")
	public Map<String, Object> buscarUsuario(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int tipoUsuario,
			@RequestParam(defaultValue = "0") Integer antiguedadUsuario,
			@RequestParam(defaultValue = "") String texto,
			@RequestParam(defaultValue = "") String sort,
			@RequestParam(defaultValue = "0") int order,
			@RequestParam(defaultValue = "0") int estado) {
		return service.buscarUsuario(page, size, tipoUsuario, antiguedadUsuario, texto, sort, order, estado);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getUsuarios")
	public Map<String, Object> getUsuarios(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int tipoUsuario)
			throws UsuarioException {
		if (!esAdmin()) {
			throw new UsuarioException(PermisosException.NoPermisosException("ADMIN"));
		}
		return service.listarUsuariosRegistrados(page, size, tipoUsuario);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getRestaurantes")
	public Map<String, Object> getRestaurantes(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "3") int tipo)
			throws UsuarioException {
		if (!esAdmin()) {
			throw new UsuarioException(PermisosException.NoPermisosException("ADMIN"));
		}
		return service.listarRestaurantes(page, size, tipo);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/cambiarEstado/{varRestaurante}")
	public ResponseEntity<?> cambiarEstadoRestaurante(@PathVariable String varRestaurante,
			@RequestParam(required = true) int estado) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}
		try {
			return new ResponseEntity<>(service.cambiarEstadoRestaurante(varRestaurante, estado), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * CONTROLES DE SEGURIDAD
	 */

	private Boolean esAdmin() {
		return getInfoFromJwt(keyInfoJWT.user_type.name()).contains("ADMIN");
	}

	/**
	 * 
	 * @param info: mail, user_type
	 * @return un String extraido del jwt conteniendo la info solicitada
	 */
	private String getInfoFromJwt(String infoName) {
		// obtenemos el token del header y le sacamos "Bearer "
		final String authorizationHeader = request.getHeader("Authorization");

		String infoSolicitada = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			switch (infoName) {
			case "mail":
				infoSolicitada = jwtUtil.extractUsername(jwt);
			case "user_type":
				infoSolicitada = jwtUtil.extractUserType(jwt);

			}

		}
		return infoSolicitada;
	}
	
	@GetMapping("/topRestaurantes")
	public ResponseEntity<?> topRestaurantes(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		try {
			return new ResponseEntity<>(service.restaurantesConMasPedidos(page, size), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/topProductos")
	public ResponseEntity<?> topProductos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		try {
			return new ResponseEntity<>(service.topProductos(page, size), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/topCategorias")
	public ResponseEntity<?> topCategorias() {
		try {
			return new ResponseEntity<>(service.topCategorias(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/balanceVenta")
	public ResponseEntity<?> getBalanceVenta(@RequestBody Map<String, String> consulta) {
		if (!esAdmin()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("ADMIN")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(restauranteService.getBalanceVentaByFechaDosRestaurantes(consulta.get("inicio"), consulta.get("fin"), consulta.get("mail1"), 
					consulta.get("devuelto"),consulta.get("metodoPago"),consulta.get("mail2")), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}