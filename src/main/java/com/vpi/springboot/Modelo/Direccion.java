package com.vpi.springboot.Modelo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Direccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String calleNro;
	
	@OneToOne (cascade = CascadeType.ALL)
	private GeoLocalizacion geoLocalizacion;
	
	@ManyToOne
	@JoinColumn(name = "clienteMail")
	private Cliente cliente;

	public Direccion() {
		super();
	}

	// Puede haber una direccion sin cliente
	public Direccion(String calleNro, GeoLocalizacion geoLocalizacion) {
		super();
		this.calleNro = calleNro;
		this.geoLocalizacion = geoLocalizacion;
	}

	public Direccion(String calleNro, Cliente cliente, GeoLocalizacion geoLocalizacion) {
		super();
		this.calleNro = calleNro;
		this.cliente = cliente;
		this.geoLocalizacion = geoLocalizacion;
	}
	
	public Direccion(int id, String calleNro, Cliente cliente, GeoLocalizacion geoLocalizacion) {
		super();
		this.id = id;
		this.calleNro = calleNro;
		this.cliente = cliente;
		this.geoLocalizacion = geoLocalizacion;
	}

	public String getCalleNro() {
		return calleNro;
	}

	public void setCalleNro(String calleNro) {
		this.calleNro = calleNro;
	}

	public GeoLocalizacion getGeoLocalizacion() {
		return geoLocalizacion;
	}

	public void setGeoLocalizacion(GeoLocalizacion geoLocalizacion) {
		this.geoLocalizacion = geoLocalizacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Direccion [id=" + id + ", calleNro=" + calleNro + ", geoLocalizacion=" + geoLocalizacion + ", cliente="
				+ cliente + "]";
	}

	
}
