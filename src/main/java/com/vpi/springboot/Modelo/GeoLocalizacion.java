package com.vpi.springboot.Modelo;

public class GeoLocalizacion {

    private Integer id;
    private Double latitud;
    private Double longitud;

    public GeoLocalizacion() {
    }

    public GeoLocalizacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
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
