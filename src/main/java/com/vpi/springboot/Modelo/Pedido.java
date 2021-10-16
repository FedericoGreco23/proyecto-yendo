package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;

@Entity
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime fecha;
	private Double costoTotal;
	@Enumerated(EnumType.STRING)
	private EnumEstadoPedido estadoPedido;
	@Enumerated(EnumType.STRING)
	private EnumMetodoDePago metodoDePago;
	private int carrito;
	@ManyToOne
	@JoinColumn(name = "clienteMail")
	private Cliente cliente;
	@ManyToOne
	@JoinColumn(name = "restauranteMail")
	private Restaurante restaurante;
	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Reclamo> reclamos = new ArrayList<>();

	public Pedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedido,
			EnumMetodoDePago metodoDePago, Integer carrito) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Double getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(Double costoTotal) {
		this.costoTotal = costoTotal;
	}

	public EnumEstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedidido(EnumEstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public EnumMetodoDePago getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(EnumMetodoDePago metodoDePago) {
		this.metodoDePago = metodoDePago;
	}

	public int getCarrito() {
		return carrito;
	}

	public void setCarrito(int carrito) {
		this.carrito = carrito;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<Reclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}
}
