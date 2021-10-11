package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.vpi.springboot.Modelo.Usuario;

public class DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mail;
	private String telefono;
	private String foto;
	private Boolean bloqueado;
	private Boolean activo;
	protected LocalDate fechaCreacion;

	public DTUsuario() {
		super();
	}

	public DTUsuario(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo, LocalDate fechaCreacion) {
		super();
		this.mail = mail;
		this.telefono = telefono;
		this.foto = foto;
		this.bloqueado = bloqueado;
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;
	}

	public DTUsuario(Usuario user) {
		super();
		this.mail = user.getMail();
		this.telefono = user.getTelefono();
		this.foto = user.getFoto();
		this.bloqueado = user.getBloqueado();
		this.activo = user.getActivo();
		this.fechaCreacion = user.getFechaCreacion();
	}
	
	public DTUsuario(String mail, String foto, LocalDate fechaCreacion) {
		super();
		this.mail = mail;
		this.foto = foto;
		this.fechaCreacion = fechaCreacion;
	}
	
	//Funcion constructora necesaria de DTRestaurante para buscarUsuario
	public DTUsuario(String mail, LocalDate fechaCreacion) {
		super();
		this.mail = mail;
		this.fechaCreacion = fechaCreacion;
	}
	
	public DTUsuario(String mail, String telefono, String foto, Boolean bloqueado, Boolean activo, LocalDate fechaCreacion) {
		super();
		this.mail = mail;
		this.telefono = telefono;
		this.foto = foto;
		this.bloqueado = bloqueado;
		this.activo = activo;
		this.fechaCreacion = fechaCreacion;
	}
	
	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
