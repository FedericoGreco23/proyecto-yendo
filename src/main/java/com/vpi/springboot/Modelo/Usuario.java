package com.vpi.springboot.Modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Usuario {
	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "MAIL")
	private String mail;
	@Column(name = "CONTRASENIA")
	private String contrasenia;
	@Column(name = "TELEFONO")
	private String telefono;
	@Column(name = "FOTO")
	private String foto;
	@Column(name = "BLOQUEADO")
	private Boolean bloqueado;
	@Column(name = "ACTIVO")
	private Boolean activo;
	@Column(columnDefinition = "DATE")
	private LocalDate fechaCreacion;

	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Usuario(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo, LocalDate fechaCreacion) {
		super();
		this.mail = mail;
		this.contrasenia = contrasenia;
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

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
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
