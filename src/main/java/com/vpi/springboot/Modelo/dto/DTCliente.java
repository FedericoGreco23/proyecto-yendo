package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Cliente;

public class DTCliente extends DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nickname;
	private Float calificacionPromedio;
	private Float saldoBono;
	private String nombre;
	private String apellido;
	private String tokenDispositivo;

	public DTCliente() {
		super();
	}

	public DTCliente(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo,
			String nickname, Float calificacionPromedio, Float saldoBono, String nombre, String apellido,
			String tokenDispositivo) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo);
		this.nickname = nickname;
		this.calificacionPromedio = calificacionPromedio;
		this.saldoBono = saldoBono;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tokenDispositivo = tokenDispositivo;
	}

	public DTCliente(Cliente user) {
		super(user.getMail(), user.getContrasenia(), user.getTelefono(), user.getFoto(), user.getBloqueado(),
				user.getActivo());
		this.nickname = user.getNickname();
		this.calificacionPromedio = user.getCalificacionPromedio();
		this.saldoBono = user.getSaldoBono();
		this.nombre = user.getNombre();
		this.apellido = user.getApellido();
		this.tokenDispositivo = user.getTokenDispositivo();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Float getCalificacionPromedio() {
		return calificacionPromedio;
	}

	public void setCalificacionPromedio(Float calificacionPromedio) {
		this.calificacionPromedio = calificacionPromedio;
	}

	public Float getSaldoBono() {
		return saldoBono;
	}

	public void setSaldoBono(Float saldoBono) {
		this.saldoBono = saldoBono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTokenDispositivo() {
		return tokenDispositivo;
	}

	public void setTokenDispositivo(String tokenDispositivo) {
		this.tokenDispositivo = tokenDispositivo;
	}
}
