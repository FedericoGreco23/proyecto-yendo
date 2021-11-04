package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Restaurante;

public class DTListarRestaurante implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mail;
	private String foto;
	private String nombre;
	private String direccion;
	private Float calificacionPromedio;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	private LocalTime tiempoEstimadoMinimo;
	private LocalTime tiempoEstimadoMaximo;
	private Integer costoDeEnvio;
	private String diasAbierto;
	private Boolean abierto;
	private List<DTCategoria> categorias;
	
	public DTListarRestaurante() {
		// TODO Auto-generated constructor stub
	}
	
	//Lo devuelvo para listarRestaurantesAbiertos
	public DTListarRestaurante(String mail, String foto, String nombre, String direccion, Float calificacionPromedio,
			LocalTime horarioApertura, LocalTime horarioCierre, LocalTime tiempoEstimadoMinimo, LocalTime tiempoEstimadoMaximo,
			Integer costoDeEnvio, String diasAbierto, Boolean abierto) {
		super();
		this.mail = mail;
		this.foto = foto;
		this.nombre = nombre;
		this.direccion = direccion;
		this.calificacionPromedio = calificacionPromedio;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.tiempoEstimadoMinimo = tiempoEstimadoMinimo;
		this.tiempoEstimadoMaximo = tiempoEstimadoMaximo;
		this.costoDeEnvio = costoDeEnvio;
		this.diasAbierto = diasAbierto;
		this.abierto = abierto;
	}
	
	public DTListarRestaurante(Restaurante r) {
		super();
		this.mail = r.getMail();
		this.foto = r.getFoto();
		this.nombre = r.getNombre();
		this.direccion = r.getDireccion();
		this.calificacionPromedio = r.getCalificacionPromedio();
		this.horarioApertura = r.getHorarioApertura();
		this.horarioCierre = r.getHorarioCierre();
		this.tiempoEstimadoMinimo = r.getTiempoEstimadoMinimo();
		this.tiempoEstimadoMaximo = r.getTiempoEstimadoMaximo();
		this.costoDeEnvio = r.getCostoDeEnvio();
		this.diasAbierto = r.getDiasAbierto();
		this.abierto = r.getAbierto();
		this.categorias = new ArrayList<DTCategoria>();
		for(Categoria cat :r.getCategorias()) {
			this.categorias.add(new DTCategoria(cat.getNombre(),cat.getFoto()));
		}
	}

	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

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

	public Integer getCostoDeEnvio() {
		return costoDeEnvio;
	}

	public void setCostoDeEnvio(Integer costoDeEnvio) {
		this.costoDeEnvio = costoDeEnvio;
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

}
