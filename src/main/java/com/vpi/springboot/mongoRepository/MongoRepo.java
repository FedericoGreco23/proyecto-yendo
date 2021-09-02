package com.vpi.springboot.mongoRepository;

import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Pedidos;

@Repository
public interface MongoRepo extends org.springframework.data.mongodb.repository.MongoRepository<Pedidos, String> {

}
