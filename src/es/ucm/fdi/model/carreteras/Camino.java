package es.ucm.fdi.model.carreteras;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.cruces.CruceGenerico;

/**
 * <h1>Clase Camino</h1>
 * 
 * <p>
 * Realiza las operaciones de los caminos
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Camino extends Carretera{

	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador de la carretera
	 * @param longitud longitud del camino
	 * @param velocidadMaxima velocidad máxima
	 * @param cruceOrigen cruce de origen
	 * @param cruceDestino cruce de destino
	 */
	public Camino(String id, int longitud, int velocidadMaxima, CruceGenerico<?> cruceOrigen, CruceGenerico<?> cruceDestino) {
		super(id, longitud, velocidadMaxima, cruceOrigen, cruceDestino);
	}

	@Override
	protected int calculaVelocidadBase() {
		return this.velocidadMaxima;
	}
	
	@Override
	protected int calculaFactorReduccion(int obstacles) {
		return obstacles + 1;
	}
	
	@Override
	protected void completaDetallesSeccion(IniSection is) {
		super.completaDetallesSeccion(is);
		is.setValue("type", "dirt");
	}
}
