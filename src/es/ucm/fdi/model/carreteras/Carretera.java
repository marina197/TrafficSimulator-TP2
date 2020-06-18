package es.ucm.fdi.model.carreteras;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.ObjetoSimulacion;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase Carretera</h1>
 * 
 * <p>
 * Realiza las operaciones de las carreteras
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Carretera extends ObjetoSimulacion {
	
	/**
	 * longitud de la carretera
	 */
	protected int longitud;
	/**
	 * velocidad máxima
	 */
	protected int velocidadMaxima;
	/**
	 * cruce del que parte la carretera
	 */
	protected CruceGenerico<?> cruceOrigen;
	/**
	 * cruce al que llega la carretera
	 */
	private CruceGenerico<?> cruceDestino;
	/**
	 * lista ordenada de vehículos en la carretera (ordenada por localización)
	 */
	protected List<Vehiculo> vehiculos;
	/**
	 * orden entre vehículos
	 */
	protected Comparator<Vehiculo> comparadorVehiculo;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador de la carretera
	 * @param length longitud de la carretera
	 * @param maxSpeed máxima velocidad perimitida en la carretera
	 * @param src cruce de origen
	 * @param dest cruce de destino
	 */
	public Carretera(String id, int length, int maxSpeed, CruceGenerico<?> src, CruceGenerico<?> dest) {
		super(id);
		this.longitud = length;
		this.velocidadMaxima = maxSpeed;
		this.cruceOrigen = src;
		this.setCruceDestino(dest);
		this.vehiculos = new LinkedList<Vehiculo>();
		comparadorVehiculo = new Comparator<Vehiculo>() {
			@Override
			public int compare(Vehiculo v1, Vehiculo v2) {
				// Orden por localización de mayor a menor.
				if (v1.getLocalizacion() < v2.getLocalizacion())
					return 1;
				else if (v1.getLocalizacion() > v2.getLocalizacion())
					return -1;
				else
					return 0;
			}
		};
		// se inicializan los atributos de acuerdo con los parámetros.
		// se fija el orden entre los vehículos: (inicia comparadorVehiculo)
		// - la localización 0 es la menor
	}

	@Override
	public void avanza() throws ErrorDeSimulacion {
		List<Vehiculo> vehiculosAux = new LinkedList<Vehiculo>(vehiculos);
		int obstaculos = 0;
		int velBase, factorRed;

		velBase = calculaVelocidadBase();
		factorRed = this.calculaFactorReduccion(obstaculos);

		for (Vehiculo v : vehiculosAux) {
			if (v.getTiempoDeAveria() > 0) {
				v.setVelocidadActual(0);
				obstaculos++;
			} else {
				factorRed = this.calculaFactorReduccion(obstaculos);
				v.setVelocidadActual(velBase / factorRed);
			}
			v.avanza();

		}
		// ordenar la lista de vehículos
		Collections.sort(this.vehiculos, this.comparadorVehiculo);
	}

	/**
	 * Si el vehículo no existe en la carretera, se añade a la lista de vehículos y
	 * se ordena la lista. Si existe no se hace nada.
	 * 
	 * @param vehiculo Vehículo que entra
	 */
	public void entraVehiculo(Vehiculo vehiculo) {
		if (!vehiculos.contains(vehiculo)) {
			vehiculos.add(vehiculo);
		}
	}

	/**
	 * elimina el vehículo de la lista de vehículos
	 * 
	 * @param vehiculo Vehículo a eliminar
	 */
	public void saleVehiculo(Vehiculo vehiculo) {
		vehiculos.remove(vehiculo);
	}

	/**
	 * Añade el vehículo al "cruceDestino" de la carretera.
	 * 
	 * @param v vehículo a añadir al cruce de destino.
	 * @throws ErrorDeSimulacion error producido si el cruce no existe.
	 */
	public void entraVehiculoAlCruce(Vehiculo v) throws ErrorDeSimulacion {
		getCruceDestino().entraVehiculoAlCruce(id, v);
		// añade el vehículo al “cruceDestino” de la carretera”
	}

	/**
	 * Calcula la velocidad base
	 * 
	 * @return velocidad base
	 */
	protected int calculaVelocidadBase() {
		return Math.min(this.velocidadMaxima, (this.velocidadMaxima / Math.max(this.vehiculos.size(), 1)) + 1);
	}

	/**
	 * Calcula el factor de reducción.
	 * 
	 * @param obstaculos número de vehículos averiados delante que reducen la velocidad.
	 * @return factor de reducción.
	 */
	protected int calculaFactorReduccion(int obstaculos) {
		if (obstaculos > 0)
			return 2;
		else
			return 1;
	}

	@Override
	protected String getNombreSeccion() {
		return "road_report";
	}

	@Override
	protected void completaDetallesSeccion(IniSection is) {
		String value = "";
		boolean coma = false;
		for (Vehiculo v : this.vehiculos) {
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + v.getId() + "," + v.getLocalizacion() + ")";
		}
		is.setValue("state", value);
	}

	@Override
	public String toString() {
		return this.getId();
	}

	public CruceGenerico<?> getCruceDestino() {
		return cruceDestino;
	}

	public void setCruceDestino(CruceGenerico<?> cruceDestino) {
		this.cruceDestino = cruceDestino;
	}

	public int getLongitud() {
		return this.longitud;
	}

	public CruceGenerico<?> getCruceOrigen() {
		return this.cruceOrigen;
	}

	public List<Vehiculo> getVehiculos() {
		return this.vehiculos;
	}

	public int getVelocidadMaxima() {
		return this.velocidadMaxima;
	}
	

}
