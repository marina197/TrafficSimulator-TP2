package es.ucm.fdi.model;

import java.util.LinkedList;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase CarreteraEntrante</h1>
 * 
 * <p>
 * Realiza las operaciones de las carreteras entrantes.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class CarreteraEntrante {
	/**
	 * Carretera
	 */
	private Carretera carretera;
	/**
	 * lista de vehículos en la carretera
	 */
	private LinkedList<Vehiculo> colaVehiculos;
	/**
	 * true=verde, false=rojo
	 */
	private boolean semaforo;

	/**
	 * Constructor de la clase
	 * 
	 * @param carretera Carretera
	 */
	public CarreteraEntrante(Carretera carretera) {
		this.setCarretera(carretera);
		this.setColaVehiculos(new LinkedList<Vehiculo>());
		this.setSemaforo(false);
	}

	/**
	 * Establece el color del semáforo
	 * 
	 * @param color color del semáforo
	 */
	public void ponSemaforo(boolean color) {
		this.setSemaforo(color);
	}

	/**
	 * coge el primer vehiculo de la cola, lo elimina, y le manda que se mueva a su siguiente carretera.
	 * 
	 * @throws ErrorDeSimulacion No ha podido avanzar
	 */
	public void avanzaPrimerVehiculo() throws ErrorDeSimulacion {
		if (!this.getColaVehiculos().isEmpty()) {
			Vehiculo v = getColaVehiculos().removeFirst();
			v.moverASiguienteCarretera();
		}
	}

	@Override
	public String toString() {
		return this.getCarretera().getId();
	}

	public boolean tieneSemaforoVerde() {
		return semaforo;
	}

	public void setSemaforo(boolean semaforo) {
		this.semaforo = semaforo;
	}

	public Carretera getCarretera() {
		return carretera;
	}

	public void setCarretera(Carretera carretera) {
		this.carretera = carretera;
	}

	public LinkedList<Vehiculo> getColaVehiculos() {
		return colaVehiculos;
	}

	public void setColaVehiculos(LinkedList<Vehiculo> colaVehiculos) {
		this.colaVehiculos = colaVehiculos;
	}

}
