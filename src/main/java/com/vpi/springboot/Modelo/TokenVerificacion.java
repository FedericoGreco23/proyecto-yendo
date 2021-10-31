package com.vpi.springboot.Modelo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class TokenVerificacion {
	// El token expira después de un día
	private static final int expiracion = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String token;
	@OneToOne(targetEntity = Cliente.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private Cliente usuario;
	private Date fechaExpiracion;
	
	public TokenVerificacion() {
		super();
	}

	public TokenVerificacion(Cliente usuario) {
		this.usuario = usuario;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiracion);
		// Genera un token random
		this.token = UUID.randomUUID().toString();
		this.fechaExpiracion = new Date(cal.getTime().getTime());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Cliente getUsuario() {
		return usuario;
	}

	public void setUsuario(Cliente usuario) {
		this.usuario = usuario;
	}

	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
}
