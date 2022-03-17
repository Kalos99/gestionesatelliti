package it.prova.gestionesatelliti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.exceptions.SatelliteGiaLanciatoException;
import it.prova.gestionesatelliti.exceptions.SatelliteInOrbitaException;
import it.prova.gestionesatelliti.exceptions.SatelliteNonDisattivatoException;
import it.prova.gestionesatelliti.exceptions.SatelliteRientratoException;
import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService{
	
	@Autowired
	private SatelliteRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		repository.save(satelliteInstance);	
	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		repository.save(satelliteInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long id) {
		Satellite satelliteDaRimuovere = repository.findById(id).get();
		if(satelliteDaRimuovere.getDataDiLancio() != null && satelliteDaRimuovere.getDataDiRientro() == null) {
			throw new SatelliteGiaLanciatoException("Impossibile rimuovere il satellite: è già stato lanciato e non è ancora rientrato");
		}
		if(satelliteDaRimuovere.getDataDiRientro() != null && satelliteDaRimuovere.getStato() != StatoSatellite.DISATTIVATO) {
			throw new SatelliteNonDisattivatoException("Impossibile rimuovere il satellite: non è stato ancora disattivato");
		}
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public List<Satellite> findByExample(Satellite example) {
		Specification<Satellite> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")), "%" + example.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + example.getCodice().toUpperCase() + "%"));

			if (example.getDataDiLancio() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataDiLancio"), example.getDataDiLancio()));

			if (example.getDataDiRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataDiRientro"), example.getDataDiRientro()));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));
			
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return repository.findAll(specificationCriteria);
	}

	@Override
	@Transactional
	public void lancia(Long id) {
		Satellite satelliteDaLanciare = repository.findById(id).get();
		if(satelliteDaLanciare.getDataDiLancio() != null) {
			throw new SatelliteInOrbitaException("Il satellite è già stato lanciato");
		}
		satelliteDaLanciare.setDataDiLancio(new Date());
		satelliteDaLanciare.setStato(StatoSatellite.IN_MOVIMENTO);
	}

	@Override
	@Transactional
	public void rientra(Long id) {
		Satellite satelliteDaFarRientrare = repository.findById(id).get();
		if(satelliteDaFarRientrare.getDataDiRientro() != null) {
			throw new SatelliteRientratoException("Il satellite è già rientrato");
		}
		satelliteDaFarRientrare.setDataDiRientro(new Date());
		satelliteDaFarRientrare.setStato(StatoSatellite.DISATTIVATO);
	}

	@Override
	@Transactional
	public List<Satellite> trovaSatellitiLanciatiPrimaDi(Date dataInput) {
		return repository.findByDataDiLancioBefore(dataInput);
	}

	@Override
	@Transactional
	public List<Satellite> trovaSatellitiDisattivatiNonRientrati(StatoSatellite statoInput) {
		return repository.findByStatoIsAndDataDiRientroNull(statoInput);
	}

	@Override
	@Transactional
	public List<Satellite> trovaSatellitiFissiLanciatiPrimaDi(StatoSatellite statoInput, Date dataInput) {
		return repository.findByStatoIsAndDataDiLancioBefore(statoInput, dataInput);
	}
}