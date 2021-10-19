package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;

@Entity
public class Reclamo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String comentario;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime fecha;
	@Enumerated(EnumType.STRING)
	private EnumEstadoReclamo estado;
	private String resolucion;
	@ManyToOne
	@JoinColumn(name = "idPedido")
	private Pedido pedido;
	@ManyToOne
	@JoinColumn(name = "restauranteMail")
	private Restaurante restaurante;

	public Reclamo(String comentario, LocalDateTime fecha, EnumEstadoReclamo estado, String resolucion) {
		super();
		this.comentario = comentario;
		this.fecha = fecha;
		this.estado = estado;
		this.resolucion = resolucion;
	}

	public Reclamo() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public EnumEstadoReclamo getEstado() {
		return estado;
	}

	public void setEstado(EnumEstadoReclamo estado) {
		this.estado = estado;
	}

}
