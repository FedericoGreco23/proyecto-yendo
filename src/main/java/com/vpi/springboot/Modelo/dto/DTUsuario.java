package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Usuario;

public class DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mail;
	private String telefono;
	private String foto;
	private Boolean bloqueado;
	private Boolean activo;
	private String tipoUsuario;

	public DTUsuario() {
		super();
	}

	public DTUsuario(String mail, String telefono, String foto, Boolean bloqueado, Boolean activo, String tipoUsuario) {
		super();
		this.mail = mail;
		this.telefono = telefono;
		this.foto = foto;
		this.bloqueado = bloqueado;
		this.activo = activo;
		this.tipoUsuario = tipoUsuario;
	}

	public DTUsuario(Usuario user, String tipoUsuario) {
		super();
		this.mail = user.getMail();
		this.telefono = user.getTelefono();
		this.foto = user.getFoto();
		this.bloqueado = user.getBloqueado();
		this.activo = user.getActivo();
		this.tipoUsuario = tipoUsuario;
	}

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

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
}
