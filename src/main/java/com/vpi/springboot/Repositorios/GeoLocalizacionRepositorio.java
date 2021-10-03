package com.vpi.springboot.Repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.GeoLocalizacion;



@Repository
public interface GeoLocalizacionRepositorio extends JpaRepository<GeoLocalizacion, Integer>{

}