package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
	private Map<LocalDate, Map<Integer, List<String>>> fechaidPedidoMonto= new HashMap<>();
	
	

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


	public Map<LocalDate, Map<Integer, List<String>>> getFechaidPedidoMonto() {
		return fechaidPedidoMonto;
	}


	public void setFechaidPedidoMonto( Map<LocalDate, Map<Integer, List<String>>> fechaidPedidoMonto) {
		this.fechaidPedidoMonto = fechaidPedidoMonto;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	
	
	
	
	

}
