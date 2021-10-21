package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;

public class DTRestaurante extends DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String direccion;
	private Float calificacionPromedio;
	private EnumEstadoRestaurante estado;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	private LocalTime tiempoEstimadoMinimo;
	private LocalTime tiempoEstimadoMaximo;
	private LocalDate fechaApertura;
	private Integer costoDeEnvio;
	private DTGeoLocalizacion geoLocalizacion;
	private List<String> productos = new ArrayList<>();
	private List<String> categorias = new ArrayList<>();
	private String diasAbierto;
	private Boolean abierto;

	public DTRestaurante() {
		super();
	}

	// En caso de que no queramos iniciar el DT con geolocalizacion o lista de
	// productos
	public DTRestaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
			Boolean activo, LocalDate fechaCreacion, String nombre, String direccion, Float calificacionPromedio,
			EnumEstadoRestaurante estado, LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura,
			Integer costoDeEnvio, String diasAbierto, Boolean abierto) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.estado = estado;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.fechaApertura = fechaApertura;
		this.costoDeEnvio = costoDeEnvio;
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;
	}

	public DTRestaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
			Boolean activo, LocalDate fechaCreacion, String nombre, String direccion, Float calificacionPromedio,
			EnumEstadoRestaurante estado, LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura,
			Integer costoDeEnvio, GeoLocalizacion geoLocalizacion, List<Producto> productos, List<Categoria> categorias,
			String diasAbierto, Boolean abierto) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.estado = estado;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.fechaApertura = fechaApertura;
		this.costoDeEnvio = costoDeEnvio;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;

		for (Categoria cat : categorias) {
			this.productos.add(cat.getNombre());
		}
		
		for (Producto pro : productos) {
			this.productos.add(pro.getNombre());
		}
	}

	// Funcion constructora para buscarUsuario
	public DTRestaurante(String mail, String nombre, String direccion, Float calificacionPromedio,
			EnumEstadoRestaurante estado, LocalTime horarioApertura, LocalTime horarioCierre,
			LocalTime tiempoEstimadoMaximo, LocalTime tiempoEstimadoMinimo, LocalDate fechaApertura,
			Integer costoDeEnvio, LocalDate fechaCreacion, String diasAbierto, Boolean abierto) {
		super(mail, fechaCreacion);
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.estado = estado;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.tiempoEstimadoMaximo = tiempoEstimadoMaximo;
		this.tiempoEstimadoMinimo = tiempoEstimadoMinimo;
		this.fechaApertura = fechaApertura;
		this.costoDeEnvio = costoDeEnvio;
		this.fechaCreacion = fechaCreacion;
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;
	}

	public DTRestaurante(Restaurante res) {
		super(res.getMail(), res.getContrasenia(), res.getTelefono(), res.getFoto(), res.getBloqueado(),
				res.getActivo(), res.getFechaCreacion());
		this.nombre = res.getNombre();
		this.direccion = res.getDireccion();
		this.calificacionPromedio = res.getCalificacionPromedio();
		this.estado = res.getEstado();
		this.horarioApertura = res.getHorarioApertura();
		this.horarioCierre = res.getHorarioCierre();
		this.tiempoEstimadoMaximo = res.getTiempoEstimadoMaximo();
		this.tiempoEstimadoMinimo = res.getTiempoEstimadoMinimo();
		this.fechaApertura = res.getFechaApertura();
		this.costoDeEnvio = res.getCostoDeEnvio();
		this.diasAbierto = res.getDiasAbierto();
		this.abierto = res.getAbierto();

		if (res.getGeoLocalizacion() != null)
			this.geoLocalizacion = new DTGeoLocalizacion(res.getGeoLocalizacion());
		else
			this.geoLocalizacion = new DTGeoLocalizacion();
		this.diasAbierto = res.getDiasAbierto();

		if (res.getProductos() != null || !res.getProductos().isEmpty()) {
			for (Producto pro : res.getProductos()) {
				this.productos.add(pro.getNombre());
			}
		}

		if (res.getCategorias() != null || !res.getCategorias().isEmpty()) {
			for (Categoria cat : res.getCategorias()) {
				this.categorias.add(cat.getNombre());
			}
		}
	}

	public DTRestaurante(String mail, String foto, String nombre, String direccion, Float calificacionPromedio,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalTime tiempoEstimadoMinimo,
			LocalTime tiempoEstimadoMaximo, Integer costoDeEnvio, GeoLocalizacion geoLocalizacion,
			String diasAbierto, Boolean abierto) {
		super(mail, foto);
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.tiempoEstimadoMinimo = tiempoEstimadoMinimo;
		this.tiempoEstimadoMaximo = tiempoEstimadoMaximo;
		this.costoDeEnvio = costoDeEnvio;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;

		/*for (Producto pro : productos) {
			this.productos.add(pro.getNombre());
		}*/
	}

//----------------------GETTERS Y SETTERS---------------------------------------------------------

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Float getCalificacionPromedio() {
		return calificacionPromedio;
	}

	public void setCalificacionPromedio(Float calificacionPromedio) {
		this.calificacionPromedio = calificacionPromedio;
	}

	public EnumEstadoRestaurante getEstado() {
		return estado;
	}

	public void setEstado(EnumEstadoRestaurante estado) {
		this.estado = estado;
	}

	public LocalTime getHorarioApertura() {
		return horarioApertura;
	}

	public void setHorarioApertura(LocalTime horarioApertura) {
		this.horarioApertura = horarioApertura;
	}

	public LocalTime getHorarioCierre() {
		return horarioCierre;
	}

	public void setHorarioCierre(LocalTime horarioCierre) {
		this.horarioCierre = horarioCierre;
	}

	public LocalTime getTiempoEstimadoMinimo() {
		return tiempoEstimadoMinimo;
	}

	public void setTiempoEstimadoMinimo(LocalTime tiempoEstimadoMinimo) {
		this.tiempoEstimadoMinimo = tiempoEstimadoMinimo;
	}

	public LocalTime getTiempoEstimadoMaximo() {
		return tiempoEstimadoMaximo;
	}

	public void setTiempoEstimadoMaximo(LocalTime tiempoEstimadoMaximo) {
		this.tiempoEstimadoMaximo = tiempoEstimadoMaximo;
	}

	public LocalDate getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(LocalDate fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Integer getCostoDeEnvio() {
		return costoDeEnvio;
	}

	public void setCostoDeEnvio(Integer costoDeEnvio) {
		this.costoDeEnvio = costoDeEnvio;
	}

	public DTGeoLocalizacion getGeoLocalizacion() {
		return geoLocalizacion;
	}

	public void setGeoLocalizacion(DTGeoLocalizacion geoLocalizacion) {
		this.geoLocalizacion = geoLocalizacion;
	}

	public List<String> getProductos() {
		return productos;
	}

	public void setProductos(List<String> productos) {
		this.productos = productos;
	}

	public String getDiasAbierto() {
		return diasAbierto;
	}

	public void setDiasAbierto(String diasAbierto) {
		this.diasAbierto = diasAbierto;
	}

	public Boolean getAbierto() {
		return abierto;
	}

	public void setAbierto(Boolean abierto) {
		this.abierto = abierto;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}
}