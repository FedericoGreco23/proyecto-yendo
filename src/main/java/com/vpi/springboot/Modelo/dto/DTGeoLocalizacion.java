package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.GeoLocalizacion;

public class DTGeoLocalizacion implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Double latitud;
    private Double longitud;
	
	public DTGeoLocalizacion() {
		super();
	}

	public DTGeoLocalizacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

	public DTGeoLocalizacion(GeoLocalizacion geo) {
		this.latitud = geo.getLatitud();
		this.longitud = geo.getLongitud();
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
}