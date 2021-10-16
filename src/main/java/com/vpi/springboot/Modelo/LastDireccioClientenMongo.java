package com.vpi.springboot.Modelo;


import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="lastDireccioClientenMongo")
public class LastDireccioClientenMongo {
	

	@Id
	private String _id;
	private Integer idDireccion;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Integer getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(Integer idDireccion) {
		this.idDireccion = idDireccion;
	}
	
	
	

	

	


	
}
