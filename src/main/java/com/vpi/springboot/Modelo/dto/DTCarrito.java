package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTCarrito implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private List<DTProductoCarrito> dtProductoCarritoList= new ArrayList<DTProductoCarrito>();
	private String mailRestaurante;

  
	 public DTCarrito(long id,List<DTProductoCarrito> dtProductoCarritoList, String mailRestaurante) {
	     this.id = id;   
		 this.dtProductoCarritoList = dtProductoCarritoList;
		 this.mailRestaurante = mailRestaurante;
	        
	    }
	 
	 public DTCarrito(List<DTProductoCarrito> dtProductoCarritoList) {
	        this.dtProductoCarritoList = dtProductoCarritoList;
	    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<DTProductoCarrito> getDtProductoCarritoList() {
        return dtProductoCarritoList;
    }

    public void setDtProductoCarritoList(List<DTProductoCarrito> dtProductoCarritoList) {
        this.dtProductoCarritoList = dtProductoCarritoList;
    }

    public String getMailRestaurante() {
		return mailRestaurante;
	}

	public void setMailRestaurante(String mailRestaurante) {
		this.mailRestaurante = mailRestaurante;
	}

	@Override
    public String toString() {
        return "DTCarrito{" +
                "dtProductoCarritoList=" + dtProductoCarritoList +
                '}';
    }
}
