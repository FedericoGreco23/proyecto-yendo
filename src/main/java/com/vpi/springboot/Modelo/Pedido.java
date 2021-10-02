package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;

public class Pedido {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime fecha;
	private Double costoTotal;
	@Enumerated(EnumType.STRING)
	private EnumEstadoPedido estadoPedidido;
	@Enumerated(EnumType.STRING)
	private EnumMetodoDePago metodoDePago;
	private int carrito;
	@ManyToOne
	@JoinColumn(name="mail")
	private Cliente cliente;
	@ManyToOne
	@JoinColumn(name="mail")
	private Restaurante restaurante;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
	private List<Reclamo> reclamos;
	
	
	public Pedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido, EnumMetodoDePago metodoDePago,
			Integer carrito) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedidido = estadoPedidido;
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


	public EnumEstadoPedido getEstadoPedidido() {
		return estadoPedidido;
	}


	public void setEstadoPedidido(EnumEstadoPedido estadoPedidido) {
		this.estadoPedidido = estadoPedidido;
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
