package com.vpi.springboot.Repositorios.mongo;

import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.LastDireccioClientenMongo;

@Repository
public interface UltimaDireccionRepositorio extends org.springframework.data.mongodb.repository.MongoRepository<LastDireccioClientenMongo, String> {

}