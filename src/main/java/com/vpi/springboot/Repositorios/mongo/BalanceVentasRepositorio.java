package com.vpi.springboot.Repositorios.mongo;

import javax.transaction.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.vpi.springboot.Modelo.dto.BalanceVentaDTO;

@Transactional
public interface BalanceVentasRepositorio extends MongoRepository<BalanceVentaDTO, String> {
	

}
