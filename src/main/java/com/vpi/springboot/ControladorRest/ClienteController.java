package com.vpi.springboot.ControladorRest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.NextSequenceService;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.RestauranteException;
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
			//obtiene mail del jwt del header. Si mail es null, devuelve null
			String mail= getMailFromJwt();
			return mail!=null? clienteService.getDireccionCliente(mail): null;
			
		} catch (UsuarioException e) {
			return null;
		}
	}

	@PostMapping(path = "/agregarDireccion", produces = "application/json")
	@ResponseBody
	public ResponseEntity<DTRespuesta> agregarDireccion(@RequestBody DTDireccion direccion) {
		try {
			String mail= getMailFromJwt();
			clienteService.altaDireccion(direccion, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Direccion agregada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("bajaCuenta")
	public ResponseEntity<?> bajaCuenta() {
		try {
			String mail= getMailFromJwt();
			clienteService.bajaCuenta(mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Cuenta dada de baja con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("modificarDireccion")
	public ResponseEntity<?> modificarDireccion(@RequestParam int id, 
			@RequestBody DTDireccion direccionNueva) {
		try {
			String mail= getMailFromJwt();
			clienteService.modificarDireccion(id, direccionNueva, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Dirección modificada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("eliminarDireccion")
	public ResponseEntity<?> eliminarDireccion(@RequestParam Integer id) {
		try {
			String mail= getMailFromJwt();
			clienteService.eliminarDireccion(id, mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Dirección eliminada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@PostMapping("agregarACarrito")
	public ResponseEntity<?> agregarACarrito(@RequestParam int producto, Integer cantidad, String mailRestaurante){
		try {
			String mail= getMailFromJwt();
			//DTProductoCarrito productoCarrito = new DTProductoCarrito(producto, cantidad);
			clienteService.agregarACarrito(producto,cantidad, mail,mailRestaurante);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Producto agregado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	@GetMapping("/getLastDireccion")
	public ResponseEntity<?> getLastDireccion() {
		try {
			String mail= getMailFromJwt();
			String respuesta = clienteService.getUltimaDireccionSeleccionada(mail);
			System.out.println(respuesta);
			return mail!=null? new ResponseEntity<String>(respuesta, HttpStatus.OK): null;
		} catch (Exception e) {
			return null;
		}
	}


	@PostMapping(path = "/lastDireccion/{idDireccion}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<DTRespuesta> lastDireccion(@PathVariable String idDireccion) {
		try {
			String mail= getMailFromJwt();
			clienteService.setUltimaDireccionSeleccionada(Integer.valueOf(idDireccion), mail);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Direccion actualizada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getCarrito")
	public DTCarrito verCarrito() {
		try {
			String mail= getMailFromJwt();
			return mail!=null? clienteService.verCarrito(mail): null;
		} catch (Exception e) {
			return null;
		}
		
	}

	private String getMailFromJwt() {
		//obtenemos el token del header y le sacamos "Bearer "
        final String authorizationHeader = request.getHeader("Authorization");

        
        String mail = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            mail = jwtUtil.extractUsername(jwt);
        }
        return mail;
	}
	
	@PostMapping("/altaPedido")
	public ResponseEntity<DTRespuesta> altaPedido(@RequestParam int idCarrito,
													@RequestParam int metodoPago, @RequestParam int idDireccion, @RequestParam String comentario){
		try {
			String mail= getMailFromJwt();
			EnumMetodoDePago pago;
			if (metodoPago == 1) {
				pago = EnumMetodoDePago.PAYPAL;
			}else {
				pago = EnumMetodoDePago.EFECTIVO;
			}
			clienteService.altaPedido(idCarrito, pago, idDireccion, mail, comentario);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Pedido enviado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}