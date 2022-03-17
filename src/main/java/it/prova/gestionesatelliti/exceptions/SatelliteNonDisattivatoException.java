package it.prova.gestionesatelliti.exceptions;

public class SatelliteNonDisattivatoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SatelliteNonDisattivatoException(String message) {
		super(message);
	}
}