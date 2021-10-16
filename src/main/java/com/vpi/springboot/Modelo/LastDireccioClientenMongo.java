package com.vpi.springboot.Modelo;


import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="lastDireccioClientenMongo")
public class LastDireccioClientenMongo {
	

	@Id
	private String mailCliente;
	private Integer idDireccion;
	
	
	

	

	


	
}
