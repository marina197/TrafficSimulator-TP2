package es.ucm.fdi.model;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase CarreteraEntranteConIntervalo</h1>
 * 
 * <p>
 * Realiza las operaciones de las carreteras entrantes con intervalo.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class CarreteraEntranteConIntervalo extends CarreteraEntrante {

	/**
	 * Tiempo que ha de transcurrir para poner el semáforo de la carretera en rojo.
	 */
	private int intervaloDeTiempo;
	/**
	 * Se incrementa cada vez que avanza un vehículo
	 */
	private int unidadesDeTiempoUsadas;
	/**
	 * Controla que en cada paso con el semáforo en verde, ha pasado un vehículo.
	 */
	private boolean usoCompleto;
	/**
	 * Controla que al menos ha pasado un vehículo mientras el semáforo estaba en verde.
	 */
	private boolean usadaPorUnVehiculo;
	/**
	 * número de vehículos que han pasado en un cruce
	 */
	private int vehiculosPasado;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param carretera Carretera
	 * @param intervalTiempo intervalo de tiempo
	 */
	public CarreteraEntranteConIntervalo(Carretera carretera, int intervalTiempo) {
		super(carretera);
		this.intervaloDeTiempo = intervalTiempo;
		this.unidadesDeTiempoUsadas = 0;
		this.usoCompleto = false;
		this.usadaPorUnVehiculo = false;
		this.vehiculosPasado = 0;
	}
	
	@Override
	public void avanzaPrimerVehiculo() throws ErrorDeSimulacion {
		this.unidadesDeTiempoUsadas++;
		if (!this.getColaVehiculos().isEmpty()) {
			Vehiculo v = getColaVehiculos().removeFirst();
			v.moverASiguienteCarretera();
			this.usadaPorUnVehiculo = true;
			this.vehiculosPasado++;
		} else {
			this.usoCompleto = false;
		}
		
		if (this.vehiculosPasado == this.intervaloDeTiempo) {
			this.usoCompleto = true;
		}
	}
	
	/**
	 * Comprueba si se ha consumido el tiempo
	 * 
	 * @return si se ha consumido el tiempo true, en otro caso false
	 */
	public boolean tiempoConsumido() {
		return this.unidadesDeTiempoUsadas >= this.intervaloDeTiempo;
	}

	/**
	 * Comprueba si se ha usado en cada intervalo
	 * 
	 * @return si se ha usado en cada intervalo true, en otro caso false
	 */
	public boolean usoCompleto() {
		return this.usoCompleto;
	}

	/**
	 * Devuelve las unidades de tiempo usadas
	 * 
	 * @return unidades de tiempo usadas
	 */
	public int getUnidadesDeTiempoUsadas() {
		return unidadesDeTiempoUsadas;
	}

	/**
	 * Establece las unidades de tiempo usadas
	 * 
	 * @param unidadesDeTiempoUsadas unidades de tiempo usadas
	 */
	public void setUnidadesDeTiempoUsadas(int unidadesDeTiempoUsadas) {
		this.unidadesDeTiempoUsadas = unidadesDeTiempoUsadas;
	}

	/**
	 * Devuelve el intervalo de tiempo
	 * 
	 * @return intervalo de tiempo
	 */
	public int getIntervaloDeTiempo() {
		return intervaloDeTiempo;
	}

	/**
	 * Establece si se ha usado en cada intervalo
	 * 
	 * @param usoCompleto uso completo del cruce
	 */
	public void setUsoCompleto(boolean usoCompleto) {
		this.usoCompleto = usoCompleto;
	}

	/**
	 * Establece si se ha usado por un vehículo
	 * 
	 * @param usadaPorUnVehiculo usado por un vehículo
	 */
	public void setUsadaPorUnVehiculo(boolean usadaPorUnVehiculo) {
		this.usadaPorUnVehiculo = usadaPorUnVehiculo;
	}

	/**
	 * Devuelve si se ha usado por un vehículo
	 * 
	 * @return usado por un vehículo
	 */
	public boolean usada() {
		return this.usadaPorUnVehiculo;
	}

	/**
	 * Establece el intervalo de tiempo
	 * 
	 * @param intervaloDeTiempo intervalo de tiempo
	 */
	public void setIntervaloDeTiempo(int intervaloDeTiempo) {
		this.intervaloDeTiempo = intervaloDeTiempo;
	}

	/**
	 * Devuelve el número de vehículos en la cola
	 * 
	 * @return numero de vehiculos en la cola
	 */
	public int numVehiculosEnCola() {
		return this.getColaVehiculos().size();
	}

	public int getvehiculosPasado() {
		return vehiculosPasado;
	}

	public void setvehiculosPasado(int vehiculosPasado) {
		this.vehiculosPasado = vehiculosPasado;
	}
}
