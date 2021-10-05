package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.GeoLocalizacion;

public class DTDireccion implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String calle;
	private String nroPuerta;
	private String nombre;
	private DTGeoLocalizacion geoLocalizacion;
	private String cliente;

	public DTDireccion() {
		super();
	}

	// Puede haber una direccion sin cliente
	public DTDireccion(String calle, String nroPuerta, String nombre, GeoLocalizacion geoLocalizacion) {
		super();
		this.calle = calle;
		this.nroPuerta = nroPuerta;
		this.nombre = nombre;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
	}

	public DTDireccion(String calle, String nroPuerta, String nombre, String cliente, GeoLocalizacion geoLocalizacion) {
		super();
		this.calle = calle;
		this.nroPuerta = nroPuerta;
		this.nombre = nombre;
		this.cliente = cliente;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNroPuerta() {
		return nroPuerta;
	}

	public void setNroPuerta(String nroPuerta) {
		this.nroPuerta = nroPuerta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public DTGeoLocalizacion getGeoLocalizacion() {
		return geoLocalizacion;
	}

	public void setGeoLocalizacion(DTGeoLocalizacion geoLocalizacion) {
		this.geoLocalizacion = geoLocalizacion;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
}