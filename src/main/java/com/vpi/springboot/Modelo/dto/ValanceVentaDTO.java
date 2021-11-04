package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Id;

public class ValanceVentaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String _id;
	private String restauranteMail;
	private Integer total;
	private Map<Integer, Integer> idPedidoMonto;
	
	
	

}
