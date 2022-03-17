package it.prova.gestionesatelliti.exceptions;

public class SatelliteRientratoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SatelliteRientratoException(String message) {
		super(message);
	}
}