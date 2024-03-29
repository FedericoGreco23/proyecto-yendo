package com.vpi.springboot.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;

@Entity
@Table(name = "RESTAURANTE")
public class Restaurante extends Usuario {

	private String nombre;
	private String direccion;
	private Float calificacionPromedio;
	@Enumerated(EnumType.STRING)
	private EnumEstadoRestaurante estado;
	@Column(columnDefinition = "TIME")
	private LocalTime horarioApertura;
	@Column(columnDefinition = "TIME")
	private LocalTime horarioCierre;
	@Column(columnDefinition = "TIME")
	private LocalTime tiempoEstimadoMinimo;
	@Column(columnDefinition = "TIME")
	private LocalTime tiempoEstimadoMaximo;
	@Column(columnDefinition = "DATE")
	private LocalDate fechaApertura;
	private Integer costoDeEnvio;
	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Pedido> pedidos = new ArrayList<>();
	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Reclamo> reclamos = new ArrayList<>();
	@OneToOne(cascade = CascadeType.ALL)
	private GeoLocalizacion geoLocalizacion;
	@ManyToMany
	private List<Categoria> categorias = new ArrayList<>();
	private String diasAbierto;
	private Boolean abierto;

	@OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Producto> productos = new ArrayList<>();

	public Restaurante() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Restaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo,
			String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura, Integer costoDeEnvio,
			GeoLocalizacion geoLocalizacion, List<Producto> productos, String diasAbierto, Boolean abierto) {

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
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;
	}

	public Restaurante(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo, LocalDate fechaCreacion,
			String nombre, String direccion, Float calificacionPromedio, EnumEstadoRestaurante estado,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalDate fechaApertura, Integer costoDeEnvio,
			GeoLocalizacion geoLocalizacion, List<Producto> productos, String diasAbierto, Boolean abierto) {

		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
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
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;
	}
	
	//Funcion para buscarUsuario y otros
	public DTRestaurante getDatos() {
		return new DTRestaurante(this.getMail(), this.nombre, this.direccion, this.calificacionPromedio, this.estado, this.horarioApertura, this.horarioCierre, 
				this.tiempoEstimadoMaximo, this.tiempoEstimadoMinimo, this.fechaApertura, this.costoDeEnvio, this.getFechaCreacion(), this.getDiasAbierto(), this.getAbierto());
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
	
	public void addProducto(Producto producto) {
		this.productos.add(producto);
	}

	public void addPedido(Pedido pedido) {
		this.pedidos.add(pedido);
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	public void addCategoria(Categoria cat) {
		// El restaurante no puede tener más de 3 categorías
		if(!this.categorias.contains(cat) && this.categorias.size() < 3)
			this.categorias.add(cat);
	}

	@Override
	public String toString() {
		return "Restaurante [nombre=" + nombre + ", direccion=" + direccion + ", calificacionPromedio="
				+ calificacionPromedio + ", estado=" + estado + ", horarioApertura=" + horarioApertura
				+ ", horarioCierre=" + horarioCierre + ", tiempoEstimadoMinimo=" + tiempoEstimadoMinimo
				+ ", tiempoEstimadoMaximo=" + tiempoEstimadoMaximo + ", fechaApertura=" + fechaApertura
				+ ", costoDeEnvio=" + costoDeEnvio + ", pedidos=" + pedidos + ", reclamos=" + reclamos
				+ ", geoLocalizacion=" + geoLocalizacion + ", categorias=" + categorias + ", diasAbierto=" + diasAbierto
				+ ", abierto=" + abierto + ", productos=" + productos + "]";
	}
	
	
}
