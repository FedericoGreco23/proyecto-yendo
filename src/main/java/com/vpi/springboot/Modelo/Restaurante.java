package com.vpi.springboot.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;

@Entity
@Table(name = "RESTAURANTE")
public class Restaurante extends Usuario {

	private String nombre;
	private String direccion;
	private Float calificacionPromedio;
	@Enumerated(EnumType.STRING)
	private EnumEstadoRestaurante estado;
	@Temporal(TemporalType.TIME)
	private LocalTime horarioApertura;
	@Temporal(TemporalType.TIME)
	private LocalTime horarioCierre;
	@Temporal(TemporalType.TIME)
	private LocalTime tiempoEstimadoMinimo;
	@Temporal(TemporalType.TIME)
	private LocalTime tiempoEstimadoMaximo;
	@Temporal(TemporalType.DATE)
	private LocalDate fechaApertura;
	private Integer costoDeEnvio;
	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Pedido> pedidos;
	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Reclamo> reclamos;
	@OneToOne
	private GeoLocalizacion geoLocalizacion;
	
	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Producto> productos;




public Restaurante() {
		super();
		// TODO Auto-generated constructor stub
	}




public Restaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
		Boolean activo, String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura, Integer costoDeEnvio, 
			GeoLocalizacion geoLocalizacion, List<Producto> productos) {

		super(mail, contrasenia, telefono, foto, bloqueado, activo);
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.estado = estado;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.fechaApertura = fechaApertura;
		this.costoDeEnvio = costoDeEnvio;
		this.geoLocalizacion = geoLocalizacion;
		this.productos = productos;
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



	public List<Pedido> getPedidos() {
		return pedidos;
	}



	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}



	public List<Reclamo> getReclamos() {
		return reclamos;
	}



	public void setReclamos(List<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}



	public GeoLocalizacion getGeoLocalizacion() {
		return geoLocalizacion;
	}



	public void setGeoLocalizacion(GeoLocalizacion geoLocalizacion) {
		this.geoLocalizacion = geoLocalizacion;
	}




	public List<Producto> getProductos() {
		return productos;
	}




	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	
}
