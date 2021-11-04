package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection="BalanceVenta")
public class BalanceVentaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String _id;//mail
	private Double total;
	private Map<Integer, Double> idPedidoMonto= new HashMap<>();
	
	

	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}



	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public Map<Integer, Double> getIdPedidoMonto() {
		return idPedidoMonto;
	}


	public void setIdPedidoMonto(Map<Integer, Double> idPedidoMonto) {
		this.idPedidoMonto = idPedidoMonto;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	
	
	
	
	

}
