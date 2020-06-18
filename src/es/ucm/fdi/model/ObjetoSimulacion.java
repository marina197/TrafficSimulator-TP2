package es.ucm.fdi.model;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;

/**
 * <h1>Clase ObjetoSimulacion</h1>
 * 
 * <p>
 * Objetos de la simulación.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public abstract class ObjetoSimulacion {
	/**
	 * identificador del objeto
	 */
	protected String id;

	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador del objeto
	 */
	public ObjetoSimulacion(String id) {
		this.id = id;
	}

	/**
	 * Devuelve el identificador del objeto.
	 * 
	 * @return identificador del objeto
	 */
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return null;
	}

	/**
	 * Hace avanzar los objetos de la simulación. 
	 * 
	 * @throws ErrorDeSimulacion El objeto de la simulación no ha podido avanzar.
	 */
	public abstract void avanza() throws ErrorDeSimulacion;

	/**
	 * Genera el informe de los objetos de la simulación.
	 * 
	 * @param tiempo tiempo en el que se encuentra la simulación
	 * @return informe del objeto de simulación.
	 */
	public String generaInforme(int tiempo) {
		IniSection is = new IniSection(this.getNombreSeccion());
		is.setValue("id", this.id);
		is.setValue("time", tiempo);
		this.completaDetallesSeccion(is);
		return is.toString();
	}

	/**
	 * Devuelve el nombre de la sección.
	 * 
	 * @return nombre de la sección
	 */
	protected abstract String getNombreSeccion();

	/**
	 * Completa el informe del objeto.
	 * 
	 * @param is sección
	 */
	protected abstract void completaDetallesSeccion(IniSection is);
}
