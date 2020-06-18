package es.ucm.fdi.model.vehiculos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.cruces.CruceGenerico;

/**
 * <h1>Clase Bicicleta</h1>
 * 
 * <p>
 * Realiza las operaciones de las bicicletas
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Bicicleta extends Vehiculo{

	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador del camino
	 * @param velocidadMaxima velocidad máxima permitida
	 * @param iti itinerario
	 * @throws ErrorDeSimulacion No se ha podido crear el vehículo
	 */
	public Bicicleta(String id, int velocidadMaxima, List<CruceGenerico<?>> iti) throws ErrorDeSimulacion {
		super(id, velocidadMaxima, iti);
	}
	
	@Override
	public void setTiempoAveria(Integer duracionAveria) {
		if (this.velocidadActual >= this.velocidadMaxima / 2) {
			this.tiempoAveria = duracionAveria;
		}
	}
	
	@Override
	protected void completaDetallesSeccion(IniSection is) {
		super.completaDetallesSeccion(is);
		is.setValue("type", "bike");
	}

}
