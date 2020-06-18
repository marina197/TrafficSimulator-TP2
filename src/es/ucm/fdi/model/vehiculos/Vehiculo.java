package es.ucm.fdi.model.vehiculos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.ObjetoSimulacion;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;

/**
 * <h1>Clase Vehiculo</h1>
 * 
 * <p>
 * Clase que representa el objeto de la simulación Vehiculo
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Vehiculo extends ObjetoSimulacion {

	/**
	 * Carretera en la que está el vehículo
	 */
	protected Carretera carretera;
	/**
	 * Velocidad máxima
	 */
	protected int velocidadMaxima;
	/**
	 * Velocidad actual
	 */
	protected int velocidadActual;
	/**
	 * Distancia recorrida
	 */
	protected int kilometraje;
	/**
	 * Localización en la carretera
	 */
	protected int localizacion;
	/**
	 * Tiempo que estará averiado
	 */
	protected int tiempoAveria;
	/**
	 * Itinerario a recorrer (mínimo 2)
	 */
	protected List<CruceGenerico<?>> itinerario;

	/**
	 * Indica si ha llegado a su destino
	 */
	private boolean haLlegado;
	/**
	 * Indica si se encuentra en un cruce.
	 */
	protected boolean estEnCruce;
	/**
	 * Contador del itinerario
	 */
	protected int contadorLista = 0;

	/**
	 * Constructor de la clase.
	 * 
	 * @param id identificador del vehiculo
	 * @param velocidadMaxima velocidad máxima a la que el vehículo puede ir
	 * @param iti itinerario que tiene que recorrer el vehiculo
	 * @throws ErrorDeSimulacion El itinerario del vehículo tiene menos de dos cruces o la
	 *             velocidad máxima es menor que 0.
	 */
	public Vehiculo(String id, int velocidadMaxima, List<CruceGenerico<?>> iti) throws ErrorDeSimulacion {
		super(id);
		if (velocidadMaxima >= 0 && iti.size() >= 2) {
			this.itinerario = iti;
			this.velocidadMaxima = velocidadMaxima;
			this.velocidadActual = 0;
			this.kilometraje = 0;
			this.localizacion = 0;
			this.tiempoAveria = 0;
			this.estEnCruce = false;
			this.haLlegado = false;
		} else
			throw new ErrorDeSimulacion("Vehículo " + id
					+ ": la velocidad máxima del vehículo es menor que 0 o el itinerario tiene menos de dos cruces.");

		carretera = null;
	}

	/**
	 * Devuelve la localización del vehículo en la carretera
	 * 
	 * @return locacización del vehículo en la carretera
	 */
	public int getLocalizacion() {
		return this.localizacion;
	}

	/**
	 * Devuelve el tiempo de avería del vehículo
	 * 
	 * @return tiempo de avería del vehículo
	 */
	public int getTiempoDeAveria() {
		return tiempoAveria;
	}

	/**
	 * Hace avanzar al vehículo en la carretera.
	 */
	public void avanza() throws ErrorDeSimulacion {
		if (this.tiempoAveria > 0) { // Está averiado
			this.tiempoAveria--;
		} else {
			if (!this.estEnCruce) {
				int temp = this.localizacion;
				this.localizacion += this.velocidadActual;
				if (this.localizacion >= this.carretera.getLongitud()) {
					this.localizacion = this.carretera.getLongitud();
					this.kilometraje += this.localizacion - temp;
					this.estEnCruce = true;
					this.carretera.entraVehiculoAlCruce(this);
					this.velocidadActual = 0;
				} else {
					kilometraje += velocidadActual;
				}
			} else
				this.velocidadActual = 0;
		}
	}

	/**
	 * Mueve el vehículo a la siguiente carretera de su itinerario.
	 * 
	 * @throws ErrorDeSimulacion La carretera siguiente no existe.
	 */
	public void moverASiguienteCarretera() throws ErrorDeSimulacion {
		if (carretera != null) {
			carretera.saleVehiculo(this);
			if (this.contadorLista == itinerario.size() - 1) {
				this.haLlegado = true;
				carretera = null;
				velocidadActual = 0;
				localizacion = 0;
			} else {
				this.contadorLista++;
				this.carretera = carretera.getCruceDestino().carreteraHaciaCruce(itinerario.get(contadorLista));
				if (carretera == null)
					throw new ErrorDeSimulacion("Vehículo " + this.getId() + ": La carretera siguiente no existe.");
				this.carretera.entraVehiculo(this);
				this.localizacion = 0;
				this.velocidadActual = 0;
				this.estEnCruce = false;
			}
		} else {
			if (this.contadorLista == 0) {
				this.contadorLista++;
				carretera = this.itinerario.get(this.contadorLista - 1)
						.carreteraHaciaCruce(this.itinerario.get(this.contadorLista));
				if (carretera == null)
					throw new ErrorDeSimulacion("Vehículo " + this.getId() + ": La carretera siguiente no existe.");
				carretera.entraVehiculo(this);
				localizacion = 0;
			}
		}
	}

	/**
	 * Establece el tiempo de duración de la avería.
	 * 
	 * @param duracionAveria duración de la avería
	 */
	public void setTiempoAveria(Integer duracionAveria) {
		// Comprobar que “carretera” no es null.
		// Se fija el tiempo de avería de acuerdo con el enunciado.
		if (carretera != null) {
			tiempoAveria += duracionAveria;
			// Si el tiempo de avería es finalmente positivo, entonces
			if (tiempoAveria > 0)
				velocidadActual = 0;
			// la “velocidadActual” se pone a 0
		}
	}

	/**
	 * Establece la velocidad actual del vehículo
	 * 
	 * @param velocidad velocidad del vehículo
	 */
	public void setVelocidadActual(int velocidad) {
		if (velocidad < 0)
			velocidadActual = 0;
		else {
			if (velocidad > velocidadMaxima)
				velocidadActual = velocidadMaxima;
			else {
				velocidadActual = velocidad;
			}
		}
	}

	/**
	 * Completa los detalles de la sección vehicle
	 */
	protected void completaDetallesSeccion(IniSection is) {
		is.setValue("speed", this.velocidadActual);
		is.setValue("kilometrage", this.kilometraje);
		is.setValue("faulty", this.tiempoAveria);
		is.setValue("location", this.haLlegado ? "arrived" : "(" + this.carretera + "," + this.getLocalizacion() + ")");
	}
	
	public Carretera getCarretera() {
		return carretera;
	}
	
	public boolean getArrived(){
		return this.haLlegado;
	}

	public int getVelocidadActual() {
		return velocidadActual;
	}

	public int getKilometraje() {
		return kilometraje;
	}

	public List<CruceGenerico<?>> getItinerario() {
		return itinerario;
	}

	@Override
	protected String getNombreSeccion() {
		return "vehicle_report";
	}
	
	@Override
	public String toString() {
		return this.getId();
	}
}
