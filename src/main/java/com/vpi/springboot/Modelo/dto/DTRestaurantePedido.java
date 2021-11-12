package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="RestaurantePedido")
public class DTRestaurantePedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String _id;
	private String nombre;
	private int cantPedidos;
	
	
	public DTRestaurantePedido() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DTRestaurantePedido(String mailRestaurante, int cantPedidos) {
		super();
		this._id = mailRestaurante;
		this.cantPedidos = cantPedidos;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMailRestaurante() {
		return _id;
	}

	public void setMailRestaurante(String mailRestaurante) {
		this._id = mailRestaurante;
	}

	public int getCantPedidos() {
		return cantPedidos;
	}

	public void setCantPedidos(int cantPedidos) {
		this.cantPedidos = cantPedidos;
	}
	
}
