package it.prova.gestionesatelliti.repository;

import java.util.Date;
//import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite>{
	List<Satellite> findByDataDiLancioBefore(Date dataInput);
	List<Satellite> findByStatoIsAndDataDiRientroNull(StatoSatellite statoInput);
	List<Satellite> findByStatoIsAndDataDiLancioBefore(StatoSatellite statoInput, Date dataInput);
}
