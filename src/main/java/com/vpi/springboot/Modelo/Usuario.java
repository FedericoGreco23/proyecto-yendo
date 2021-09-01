package com.vpi.springboot.Modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int cedula;
	private String nombre;
	
	
	
	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Usuario(String nombre, int cedula) {
		super();
		this.nombre = nombre;
		this.cedula= cedula;
	}



	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCedula() {
		return cedula;
	}
	public void setCedula(int cedula) {
		this.cedula = cedula;
	}
	
	

}
