package com.vpi.springboot.Modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.vpi.springboot.Modelo.dto.DTGeoLocalizacion;

@Entity
public class GeoLocalizacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double latitud;
    private Double longitud;

    public GeoLocalizacion() {
    }

    public GeoLocalizacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    
    public GeoLocalizacion(DTGeoLocalizacion geo) {
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

    @Override
    public String toString() {
        return "GeoLocalizacion{" +
                "id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
