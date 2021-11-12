package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private List<FechaidPedidoMontoDTO> listaPedidos= new ArrayList<FechaidPedidoMontoDTO>();
	
	
	

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




	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public List<FechaidPedidoMontoDTO> getListaPedidos() {
		return listaPedidos;
	}


	public void setListaPedidos(List<FechaidPedidoMontoDTO> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}





}
