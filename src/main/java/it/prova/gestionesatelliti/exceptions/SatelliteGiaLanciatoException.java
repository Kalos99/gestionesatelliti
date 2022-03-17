package it.prova.gestionesatelliti.exceptions;

public class SatelliteGiaLanciatoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SatelliteGiaLanciatoException(String message) {
		super(message);
	}
}