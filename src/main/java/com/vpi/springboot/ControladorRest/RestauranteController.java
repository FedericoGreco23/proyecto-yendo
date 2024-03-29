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

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.exception.PermisosException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil;
import com.vpi.springboot.security.util.JwtUtil.keyInfoJWT;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {

	@Autowired
	private RestauranteService service;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HttpServletRequest request;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crearMenu")
	public ResponseEntity<?> altaMenu(@RequestBody Producto menu) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.altaMenu(menu, getMailFromJwt()), HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminarMenu/{id}")
	public ResponseEntity<?> bajaMenu(@PathVariable int id) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.bajaMenu(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/modificarMenu")
	public ResponseEntity<?> modificarMenu(@RequestBody Producto menu) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.modificarMenu(menu), HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getPedidos")
	public ResponseEntity<?> listarPedidos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String id,
			@RequestParam(defaultValue = "") String fecha, @RequestParam(defaultValue = "") String estado,
			@RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "1") int order) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(
					service.listarPedidos(page, size, getMailFromJwt(), id, fecha, estado, sort, order),
					HttpStatus.OK);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/abrirRestaurante")
	public ResponseEntity<?> abrirRestaurante() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.abrirRestaurante(getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/cerrarRestaurante")
	public ResponseEntity<?> cerrarRestaurante() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.cerrarRestaurante(getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/confirmarPedido")
	public ResponseEntity<?> confirmarPedido(@RequestParam int idPedido) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.confirmarPedido(idPedido), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/rechazarPedido")
	public ResponseEntity<?> rechazarPedido(@RequestParam int idPedido) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.rechazarPedido(idPedido), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/modificarDescuento/{idProducto}")
	public ResponseEntity<?> modificarDescuento(@PathVariable int idProducto, @RequestParam int descuento) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.modificarDescuentoProducto(idProducto, descuento), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/modificarPromocion")
	public ResponseEntity<?> modificarPromocion(@RequestBody Promocion promo) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.modificarPromocion(promo), HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminarPromocion/{id}")
	public ResponseEntity<?> bajaPromocion(@PathVariable int idPromo) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.bajaPromocion(idPromo), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/altaPromocion")
	public ResponseEntity<?> altaPromocion(@RequestBody DTPromocionConPrecio promocion) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.altaPromocion(promocion, getInfoFromJwt("mail")), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/calificarCliente/{mailCliente}")
	public ResponseEntity<?> calificarCliente(@PathVariable String mailCliente,
			@RequestBody Calificacion calificacion) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.calificarCliente(mailCliente, getInfoFromJwt("mail"), calificacion),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/eliminarCalificacion/{mailCliente}")
	public ResponseEntity<?> bajaCalificacionCliente(@PathVariable String mailCliente) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.bajaCalificacionCliente(mailCliente, getInfoFromJwt("mail")),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/buscarPedido")
	public ResponseEntity<?> buscarPedidoRecibido(@RequestParam(defaultValue = "0") int numeroPedido) {
		try {
			return new ResponseEntity<>(service.buscarPedidoRecibido(numeroPedido), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getReclamos")
	public ResponseEntity<?> listarReclamos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String cliente,
			@RequestParam(defaultValue = "") String fecha, @RequestParam(defaultValue = "") String estado,
			@RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "1") int order) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(
					service.listarReclamos(page, size, cliente, estado, fecha, sort, order, getMailFromJwt()),
					HttpStatus.OK);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/consultarCalificacion")
    public ResponseEntity<?> consultarCalificacion(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "0") int order) {
        try {
            return new ResponseEntity<>(service.consultarCalificacion(page, size, sort, order, getMailFromJwt()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

	@PostMapping("resolucionReclamo")
	public ResponseEntity<?> resolucionReclamo(@RequestParam(required = true) int idReclamo,
			@RequestParam(required = true) Boolean aceptoReclamo, @RequestParam(defaultValue = "") String comentario) {
		try {
			DTRespuesta respuesta = service.resolucionReclamo(idReclamo, aceptoReclamo, comentario);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getCalCliente/{cliente}")
	public ResponseEntity<?> getCalificacionCliente(@PathVariable String cliente) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.getCalificacionCliente(cliente, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/balanceVenta")
	public ResponseEntity<?> getBalanceVenta(@RequestBody Map<String, String> intervalo) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.getBalanceVentaByFecha(intervalo.get("inicio"), intervalo.get("fin"), getMailFromJwt(), 
					intervalo.get("devuelto"),intervalo.get("metodoPago")), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getEstado")
	public ResponseEntity<?> getEstado() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.getEstado(getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/abrir")
	public ResponseEntity<?> cambiarRestaurantes() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			return new ResponseEntity<>(service.checkRestauranteResponse(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//ESTA NO SE USA, SE HACE AUTOMATICO. LA DEJO POR LAS DUDAS
	@PostMapping("/resPed")
	public ResponseEntity<?> guardaRestaurantesEnMongo(){
		try {
			return new ResponseEntity<>(service.guaradarEnMongo(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/// PRIVADAS PARA JWT ///
	/////////////////////////
	private Boolean esRestaurante() {
		return getInfoFromJwt(keyInfoJWT.user_type.name()).contains("RESTAURANTE");
	}

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
				break;
			case "user_type":
				infoSolicitada = jwtUtil.extractUserType(jwt);
				break;
			}
		}

		return infoSolicitada;
	}

	private String getMailFromJwt() {
		// obtenemos el token del header y le sacamos "Bearer "
		final String authorizationHeader = request.getHeader("Authorization");

		String mail = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			mail = jwtUtil.extractUsername(jwt);
		}
		return mail;
	}
}
