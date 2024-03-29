package com.vpi.springboot.ControladorRest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/cliente/")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private HttpServletRequest request;

	@GetMapping("/getDireccion")
	public List<DTDireccion> getDireccionUsuario() {
		try {
			// obtiene mail del jwt del header. Si mail es null, devuelve null
			String mail = getMailFromJwt();
			return mail != null ? clienteService.getDireccionCliente(mail) : null;
		} catch (UsuarioException e) {
			return null;
		}
	}

	@PostMapping(path = "/agregarDireccion", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> agregarDireccion(@RequestBody DTDireccion direccion) throws Exception {
		try {
			return new ResponseEntity<>(clienteService.altaDireccion(direccion, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("bajaCuenta")
	public ResponseEntity<?> bajaCuenta() throws Exception {
		try {
			return new ResponseEntity<>(clienteService.bajaCuenta(getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("modificarDireccion")
	public ResponseEntity<?> modificarDireccion(@RequestParam int id, @RequestBody DTDireccion direccionNueva) throws Exception {
		try {
			return new ResponseEntity<>(clienteService.modificarDireccion(id, direccionNueva, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("eliminarDireccion")
	public ResponseEntity<?> eliminarDireccion(@RequestParam Integer id) throws Exception {
		try {
			return new ResponseEntity<>(clienteService.eliminarDireccion(id, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("agregarACarrito")
	public ResponseEntity<?> agregarACarrito(@RequestParam int producto, Integer cantidad, String mailRestaurante) throws Exception {
		try {
			String mail = getMailFromJwt();
			return new ResponseEntity<>(clienteService.agregarACarrito(producto, cantidad, mail, mailRestaurante), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getLastDireccion")
	public ResponseEntity<?> getLastDireccion() {
		try {
			String mail = getMailFromJwt();
			String respuesta = clienteService.getUltimaDireccionSeleccionada(mail);
			System.out.println(respuesta);
			return mail != null ? new ResponseEntity<String>(respuesta, HttpStatus.OK) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping(path = "/lastDireccion/{idDireccion}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> lastDireccion(@PathVariable String idDireccion) {
		try {
			clienteService.setUltimaDireccionSeleccionada(Integer.valueOf(idDireccion), getMailFromJwt());
			return new ResponseEntity<>(new DTRespuesta("Direccion actualizada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCarrito")
	public DTCarrito verCarrito() {
		try {
			String mail = getMailFromJwt();
			return mail != null ? clienteService.verCarrito(mail) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/altaPedido")
	public ResponseEntity<?> altaPedido(@RequestParam int idCarrito, @RequestParam int metodoPago,
			@RequestParam int idDireccion, @RequestParam String comentario) throws Exception {
		try {
			String mail = getMailFromJwt();
			EnumMetodoDePago pago;
			if (metodoPago == 1) {
				pago = EnumMetodoDePago.PAYPAL;
			} else {
				pago = EnumMetodoDePago.EFECTIVO;
			}

			return new ResponseEntity<>(clienteService.altaPedido(idCarrito, pago, idDireccion, mail, comentario), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/altaReclamo")
	public ResponseEntity<DTRespuesta> altaReclamo(@RequestParam int idPedidoReclamado,
			@RequestParam String Comentario) throws Exception {
		try {
			return new ResponseEntity<DTRespuesta>(
					clienteService.altaReclamo(idPedidoReclamado, getMailFromJwt(), Comentario), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/eliminarProductoCarrito")
	public ResponseEntity<?> eliminarProductoCarrito(@RequestParam int idProducto, @RequestParam int cantABorrar) throws Exception {
		try {
			;
			return new ResponseEntity<>(
					clienteService.eliminarProductoCarrito(idProducto, cantABorrar, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/eliminarCarrito")
	public ResponseEntity<?> eliminarCarrito(@RequestParam int idCarrito) throws Exception {
		try {
			return new ResponseEntity<>(clienteService.eliminarCarrito(idCarrito, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getPedidos")
	public ResponseEntity<?> listarPedidos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String restaurante,
			@RequestParam(defaultValue = "") String fecha, @RequestParam(defaultValue = "") String sort,
			@RequestParam(defaultValue = "1") int order) {
		try {
			return new ResponseEntity<>(
					clienteService.listarPedidos(size, page, restaurante, fecha, sort, order, getMailFromJwt()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/calificarRestaurante/{mailRestaurante}")
	public ResponseEntity<?> calificarRestaurante(@PathVariable String mailRestaurante,
			@RequestBody Calificacion calificacion) throws Exception {
		try {
			return new ResponseEntity<>(
					clienteService.calificarRestaurante(getMailFromJwt(), mailRestaurante, calificacion),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/eliminarCalificacion/{mailRestaurante}")
	public ResponseEntity<?> bajaCalificacionRestaurante(@PathVariable String mailRestaurante) throws Exception {
		try {
			return new ResponseEntity<>(clienteService.bajaCalificacionRestaurante(getMailFromJwt(), mailRestaurante),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
//			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getReclamos")
	public ResponseEntity<?> listarReclamos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, 
			@RequestParam(defaultValue = "") String estado, @RequestParam(defaultValue = "") String fecha, 
			@RequestParam(defaultValue = "") String restaurante, @RequestParam(defaultValue = "") String sort,
			@RequestParam(defaultValue = "1") int order) {
		try {
			return new ResponseEntity<>(
					clienteService.listarReclamos(size, page, estado, fecha, restaurante, sort, order, getMailFromJwt()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/buscarPedido")
	public ResponseEntity<?> buscarPedidoRealizado(@RequestParam(defaultValue = "0") int numeroPedido) {
		try {
			return new ResponseEntity<>(clienteService.buscarPedidoRealizado(numeroPedido), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCalRestaurante/{restaurante}")
	public ResponseEntity<?> getCalificacionRestaurante(@PathVariable String restaurante) {
		try {
			return new ResponseEntity<>(clienteService.getCalificacionRestaurante(getMailFromJwt(), restaurante), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	
	@PostMapping("/setToken")
	public ResponseEntity<?> setToken(@RequestParam(required = true) String token, @RequestParam(defaultValue = "") String mailCliente) {
		try {
			return new ResponseEntity<>(clienteService.setToken(token, mailCliente), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}