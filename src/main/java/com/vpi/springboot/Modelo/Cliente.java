package com.vpi.springboot.Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpi.springboot.Modelo.dto.DTCliente;

@Entity
public class Cliente extends Usuario {

	@Column(unique = true)
	private String nickname;
	private Float calificacionPromedio;
	private Float saldoBono;
	private String nombre;
	private String apellido;
	private String tokenDispositivo;
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Direccion> direcciones = new ArrayList<>();
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Pedido> pedidos = new ArrayList<>();
	private Boolean verificado;

	public Cliente() {
		super();
	}

	public Cliente(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, Boolean activo,
			LocalDate fechaCreacion, String nickname, Float calificacionPromedio, Float saldoBono, String nombre,
			String apellido, String tokenDispositivo) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
		this.nickname = nickname;
		this.calificacionPromedio = calificacionPromedio;
		this.saldoBono = saldoBono;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tokenDispositivo = tokenDispositivo;
		this.verificado = false;
	}

	// Funcion para pasar a DT de buscarUsuario y otros
	public DTCliente getDatos() {
		return new DTCliente(this.getMail(), this.getTelefono(), this.getFoto(), this.getBloqueado(), this.getActivo(),
				this.getFechaCreacion(), this.getNickname(), this.getCalificacionPromedio(), this.getNombre(),
				this.getApellido());
	}

//----------------------GETTERS Y SETTERS---------------------------------------------------------

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

	public List<Direccion> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<Direccion> direcciones) {
		this.direcciones = direcciones;
	}

	public void addDireccion(Direccion dir) {
		this.direcciones.add(dir);
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public void addPedido(Pedido pedido) {
		this.pedidos.add(pedido);
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}
}