package com.vpi.springboot.Repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Pedido;



@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer>{

}