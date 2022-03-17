package it.prova.gestionesatelliti.service;

import java.util.Date;
//import java.util.Date;
import java.util.List;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteService {

	public List<Satellite> listAllElements();

	public Satellite caricaSingoloElemento(Long id);
	
	public void aggiorna(Satellite satelliteInstance);

	public void inserisciNuovo(Satellite satelliteInstance);

	public void rimuovi(Long id);
	
	public List<Satellite> findByExample(Satellite example);
	
	public void lancia(Long id);
	
	public void rientra(Long id);
	
	public List<Satellite> trovaSatellitiLanciatiPrimaDi(Date dataInput);
	
	public List<Satellite> trovaSatellitiDisattivatiNonRientrati(StatoSatellite statoInput);
	
	public List<Satellite> trovaSatellitiFissiLanciatiPrimaDi(StatoSatellite statoInput, Date dataInput);
}
