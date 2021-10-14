package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTGeoLocalizacion;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.RestauranteException;

@Service
public class RestauranteService implements RestauranteServicioInterfaz {
	
	@Autowired
	private RestauranteRepositorio restauranteRepo;
	
	@Autowired
	private GeoLocalizacionRepositorio geoRepo;
	
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
		
		restauranteRepo.save(rest);	
	}
}
