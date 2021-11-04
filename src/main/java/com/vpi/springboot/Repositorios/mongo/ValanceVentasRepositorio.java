package com.vpi.springboot.Repositorios.mongo;

import javax.transaction.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.vpi.springboot.Modelo.dto.ValanceVentaDTO;

@Transactional
public interface ValanceVentasRepositorio extends MongoRepository<ValanceVentaDTO, String> {
	

}
