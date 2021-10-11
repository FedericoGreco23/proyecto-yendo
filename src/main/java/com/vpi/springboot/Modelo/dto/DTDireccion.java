package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.GeoLocalizacion;

public class DTDireccion implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String calleNro;
	private DTGeoLocalizacion geoLocalizacion;
	private String cliente;

	public DTDireccion() {
		super();
	}

	// Puede haber una direccion sin cliente
	public DTDireccion(int id, String calleNro, GeoLocalizacion geoLocalizacion) {
		super();
		this.id = id;
		this.calleNro = calleNro;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
	}
	
	public DTDireccion(String calleNro, GeoLocalizacion geoLocalizacion) {
		super();
		this.calleNro = calleNro;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
	}

	public DTDireccion(String calleNro, String cliente, GeoLocalizacion geoLocalizacion) {
		super();
		this.calleNro = calleNro;
		this.cliente = cliente;
		this.geoLocalizacion = new DTGeoLocalizacion(geoLocalizacion);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCalleNro() {
		return calleNro;
	}

	public void setCalleNro(String calleNro) {
		this.calleNro = calleNro;
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