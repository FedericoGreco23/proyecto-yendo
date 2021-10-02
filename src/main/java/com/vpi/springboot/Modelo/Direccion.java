package com.vpi.springboot.Modelo;

public class Direccion {
    private int id;
    private String calle;
    private String nroPuerta;
    private String nombre;
    private GeoLocalizacion geoLocalizacion;

    public Direccion() {
    }

    public Direccion(String calle, String nroPuerta, String nombre, GeoLocalizacion geoLocalizacion) {
        this.calle = calle;
        this.nroPuerta = nroPuerta;
        this.nombre = nombre;
        this.geoLocalizacion = geoLocalizacion;
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

    public GeoLocalizacion getGeoLocalizacion() {
        return geoLocalizacion;
    }

    public void setGeoLocalizacion(GeoLocalizacion geoLocalizacion) {
        this.geoLocalizacion = geoLocalizacion;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", calle='" + calle + '\'' +
                ", nroPuerta='" + nroPuerta + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
