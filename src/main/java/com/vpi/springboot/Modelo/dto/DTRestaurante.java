package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
	private List<DTPedido> pedidos;
	private List<DTReclamo> reclamos;
	private DTGeoLocalizacion geoLocalizacion;
	private List<DTProducto> productos;
	private String diasAbierto;

	public DTRestaurante() {
		super();
	}

	// En caso de que no queramos iniciar el DT con geolocalizacion o lista de
	// productos
	public DTRestaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
			Boolean activo, LocalDate fechaCreacion, String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura, Integer costoDeEnvio, String diasAbierto) {
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
	}

	public DTRestaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
			Boolean activo, LocalDate fechaCreacion, String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura, Integer costoDeEnvio,
			GeoLocalizacion geoLocalizacion, List<Producto> productos, String diasAbierto) {
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

		for (Producto pro : productos) {
			this.productos.add(new DTProducto(pro));
		}
	}
	
	//Funcion constructora para buscarUsuario
	public DTRestaurante(String mail, String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado, LocalTime horarioApertura, LocalTime horarioCierre, 
			LocalTime tiempoEstimadoMaximo, LocalTime tiempoEstimadoMinimo, LocalDate fechaApertura, Integer costoDeEnvio, LocalDate fechaCreacion, String diasAbierto) {
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
	}

	public DTRestaurante(Restaurante res) {
		super();
		this.nombre = res.getNombre();
		this.direccion = res.getDireccion();
		this.calificacionPromedio = res.getCalificacionPromedio();
		this.estado = res.getEstado();
		this.horarioApertura = res.getHorarioApertura();
		this.horarioCierre = res.getHorarioCierre();
		this.fechaApertura = res.getFechaApertura();
		this.costoDeEnvio = res.getCostoDeEnvio();
		this.geoLocalizacion = new DTGeoLocalizacion(res.getGeoLocalizacion());
		this.diasAbierto = res.getDiasAbierto();

		for (Producto pro : res.getProductos()) {
			this.productos.add(new DTProducto(pro));
		}
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

	public List<DTPedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<DTPedido> pedidos) {
		this.pedidos = pedidos;
	}

	public List<DTReclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<DTReclamo> reclamos) {
		this.reclamos = reclamos;
	}

	public DTGeoLocalizacion getGeoLocalizacion() {
		return geoLocalizacion;
	}

	public void setGeoLocalizacion(DTGeoLocalizacion geoLocalizacion) {
		this.geoLocalizacion = geoLocalizacion;
	}

	public List<DTProducto> getProductos() {
		return productos;
	}

	public void setProductos(List<DTProducto> productos) {
		this.productos = productos;
	}

	public String getDiasAbierto() {
		return diasAbierto;
	}

	public void setDiasAbierto(String diasAbierto) {
		this.diasAbierto = diasAbierto;
	}
}