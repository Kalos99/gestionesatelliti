package it.prova.gestionesatelliti.exceptions;

public class SatelliteInOrbitaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SatelliteInOrbitaException (String message) {
		super(message);
	}
}