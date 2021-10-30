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
	private String direccion;
	private String comentario;
	private Boolean pago;
	

	public Pedido() {
		super();
		// TODO Auto-generated constructor stub
	}

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
	
	public Pedido(LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedido,
			EnumMetodoDePago metodoDePago, Integer carrito, String direccion, Restaurante resto, Cliente cliente, String comentario) {
		super();
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		this.direccion = direccion;
		this.restaurante = resto;
		this.cliente = cliente;
		this.comentario = comentario;
		
	}
	
	public Pedido(LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedido,
			EnumMetodoDePago metodoDePago, Integer carrito, String direccion, Restaurante resto, Cliente cliente, String comentario, Boolean pago) {
		super();
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		this.direccion = direccion;
		this.restaurante = resto;
		this.cliente = cliente;
		this.comentario = comentario;
		this.pago = pago;
		
	}
	
	public Pedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedido,
			EnumMetodoDePago metodoDePago, Integer carrito, String direccion, Restaurante resto, Cliente cliente, String comentario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		this.direccion = direccion;
		this.restaurante = resto;
		this.cliente = cliente;
		this.comentario = comentario;
		
	}
	
	public Pedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedido,
			EnumMetodoDePago metodoDePago, Integer carrito, String direccion, String comentario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		this.direccion = direccion;
		this.comentario = comentario;
	}
	
	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	

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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setEstadoPedido(EnumEstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}
}
