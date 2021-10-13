package com.vpi.springboot.ControladorRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Logica.MyUserDetailsService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.AuthenticationRequest;
import com.vpi.springboot.Modelo.dto.AuthenticationResponse;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.security.util.JwtUtil;
import com.vpi.springboot.security.util.MyDetails;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/public/")
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
	
	

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Usuario o contraseña son incorrectas", e);
		}


		final MyDetails userDetails = (MyDetails) userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public ResponseEntity<DTRespuesta> altaCliente(@RequestBody Cliente usuario) {
		try {
			clienteService.altaCliente(usuario);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Cliente agregado con éxito"), HttpStatus.OK);
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
}
