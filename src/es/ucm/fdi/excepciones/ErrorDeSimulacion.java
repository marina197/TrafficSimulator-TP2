package es.ucm.fdi.excepciones;

/**
 * <h1>Clase ErrorDeSimulacion</h1>
 * 
 * <p>
 * Excepción lanzada cuando se produce un error al cargar o ejecutar la
 * simulación
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
@SuppressWarnings("serial")
public class ErrorDeSimulacion extends Exception {

	/**
	 * Constructor de la clase
	 * 
	 * @param mensaje mensaje provocado por una excepción
	 */
	public ErrorDeSimulacion(String mensaje) {
		super(mensaje);
	}

}