package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Reclamo;

public class DTReclamo implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String comentario;
	private LocalDateTime fecha;
	private EnumEstadoReclamo estado;
	private String resolucion;
	private DTPedido pedido;
	private String restaurante;

	public DTReclamo() {
		super();
	}

	public DTReclamo(int id, String comentario, LocalDateTime fecha, EnumEstadoReclamo estado, Pedido pedido, String resolucion, String restaurante) {
		super();
		this.id = id;
		this.comentario = comentario;
		this.fecha = fecha;
		this.estado = estado;
		this.resolucion = resolucion;
		this.pedido = new DTPedido(pedido);
		this.restaurante = restaurante;
	}

	public DTReclamo(Reclamo rec) {
		super();
		this.id = rec.getId();
		this.comentario = rec.getComentario();
		this.fecha = rec.getFecha();
		this.estado = rec.getEstado();
		this.resolucion = rec.getResolucion();
		this.pedido = new DTPedido(rec.getPedido());
		this.restaurante = rec.getRestaurante().getNombre();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public EnumEstadoReclamo getEstado() {
		return estado;
	}

	public void setEstado(EnumEstadoReclamo estado) {
		this.estado = estado;
	}

	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public DTPedido getPedido() {
		return pedido;
	}

	public void setPedido(DTPedido pedido) {
		this.pedido = pedido;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}
}