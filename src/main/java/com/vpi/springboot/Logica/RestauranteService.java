package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.RestauranteException;

@Service
public class RestauranteService implements RestauranteServicioInterfaz {
	
	@Autowired
	private RestauranteRepositorio restauranteRepo;
	
	@Override
	public void altaRestaurante(Restaurante rest) throws RestauranteException {
		
		//Seccion verificar que nombreRestaurante o restauranteMail no exista ya
		Optional<Restaurante> busquedaMail = restauranteRepo.findById(rest.getMail());
		Restaurante busquedaNombre = null;
		busquedaNombre = restauranteRepo.existeRestauranteNombre(rest.getNombre());
		if(busquedaMail.isPresent()) {
			throw new RestauranteException(RestauranteException.MailYaExiste(rest.getMail()));
		}else if (busquedaNombre != null){
			throw new RestauranteException(RestauranteException.RestauranteYaExiste(rest.getNombre()));
		}
		//Fin de seccion
		
		rest.setActivo(true);
		rest.setBloqueado(false);
		rest.setCalificacionPromedio(5.0f);
		rest.setFechaCreacion(LocalDate.now());
		rest.setEstado(EnumEstadoRestaurante.EN_ESPERA);
		rest.setFechaApertura(null);
		rest.setProductos(null);
		rest.setReclamos(null);
		rest.setPedidos(null);
		rest.setAbierto(false);
		
		restauranteRepo.save(rest);	
	}
	
	@Override
	public void abrirRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(true);
		restauranteRepo.save(restaurante.get());
	}
	
	@Override
	public void cerrarRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(false);
		restauranteRepo.save(restaurante.get());
	}
}
