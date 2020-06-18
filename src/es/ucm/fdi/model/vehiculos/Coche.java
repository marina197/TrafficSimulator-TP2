package es.ucm.fdi.model.vehiculos;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.cruces.CruceGenerico;

/**
 * <h1>Clase Coche</h1>
 * 
 * <p>
 * Realiza las operaciones de los coches.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Coche extends Vehiculo {

	/**
	 * Km de la última avería
	 */
	protected int kmUltimaAveria;
	/**
	 * Resistencia
	 */
	protected int resistenciaKm;
	/**
	 * duración máxima de la avería
	 */
	protected int duracionMaximaAveria;
	/**
	 * Probabilidad de avería
	 */
	protected double probabilidadDeAveria;
	/**
	 * Número aleatorio
	 */
	protected Random numAleatorio;

	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador del vehículo
	 * @param velocidadMaxima velocidad máxima
	 * @param resistencia resistencia del vehículo
	 * @param probabilidad probabilidad de avería
	 * @param semilla semilla para la probabilidad
	 * @param duracionMaximaAveria duración máxima de avería
	 * @param iti itinerario de cruces
	 * @throws ErrorDeSimulacion No se ha podido crear correctamente el vehiculo
	 */
	public Coche(String id, int velocidadMaxima, int resistencia, double probabilidad, long semilla,
			int duracionMaximaAveria, List<CruceGenerico<?>> iti) throws ErrorDeSimulacion {
		super(id, velocidadMaxima, iti);
		this.resistenciaKm = resistencia;
		this.duracionMaximaAveria = duracionMaximaAveria;
		this.probabilidadDeAveria = probabilidad;
		this.numAleatorio = new Random(semilla);
	}

	@Override
	public void avanza() throws ErrorDeSimulacion {
		if (this.tiempoAveria > 0) {
			this.kmUltimaAveria = this.kilometraje;
		} else {
			if ((this.kilometraje - this.kmUltimaAveria > this.resistenciaKm)
					&& (this.numAleatorio.nextDouble() < this.probabilidadDeAveria)) {
				this.setTiempoAveria(this.numAleatorio.nextInt(this.duracionMaximaAveria) + 1);
			}
		}
		super.avanza();
	}

	@Override
	protected void completaDetallesSeccion(IniSection is) {
		super.completaDetallesSeccion(is);
		is.setValue("type", "car");
	}

}
