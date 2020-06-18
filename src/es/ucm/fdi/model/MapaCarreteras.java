package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase MapaCarreteras</h1>
 * 
 * <p>
 * Contiene la información de las carreteras.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class MapaCarreteras {
	/**
	 * Lista de carreteras
	 */
	private List<Carretera> carreteras;
	/**
	 * Lista de cruces
	 */
	private List<CruceGenerico<?>> cruces;
	/**
	 * Lista de vehículos
	 */
	private List<Vehiculo> vehiculos;
	// estructuras para agilizar la búsqueda (id,valor)
	/**
	 * Mapa de carreteras
	 */
	private Map<String, Carretera> mapaDeCarreteras;
	/**
	 * Mapa de cruces
	 */
	private Map<String, CruceGenerico<?>> mapaDeCruces;
	/**
	 * Mapa de vehículos
	 */
	private Map<String, Vehiculo> mapaDeVehiculos;

	/**
	 * Constructor de la clase
	 */
	public MapaCarreteras() {
		this.carreteras = new ArrayList<Carretera>();
		this.cruces = new ArrayList<CruceGenerico<?>>();
		this.vehiculos = new ArrayList<Vehiculo>();
		this.mapaDeCarreteras = new HashMap<String, Carretera>();
		this.mapaDeCruces = new HashMap<String, CruceGenerico<?>>();
		this.mapaDeVehiculos = new HashMap<String, Vehiculo>();
		// inicializa los atributos a sus constructoras por defecto.
		// Para carreteras, cruces y vehículos puede usarse ArrayList.
		// Para los mapas puede usarse HashMap
	}

	/**
	 * Añade el cruce a la lista y al mapa.
	 * 
	 * @param idCruce identificador del cruce
	 * @param cruceGenerico Cruce a añadir.
	 * @throws ErrorDeSimulacion El cruce ya ha sido añadido con anterioridad
	 */
	public void addCruce(String idCruce, CruceGenerico<?> cruceGenerico) throws ErrorDeSimulacion {
		if(!mapaDeCruces.containsKey(idCruce)) {
			mapaDeCruces.put(idCruce, cruceGenerico);
			cruces.add(cruceGenerico);
		}
		else throw new ErrorDeSimulacion("El cruce " + idCruce + " ya existe, no puede ser añadido");
		// comprueba que “idCruce” no existe en el mapa.
		// Si no existe, lo añade a “cruces” y a “mapaDeCruces”.
		// Si existe lanza una excepción.
	}

	/**
	 * Añade el vehículo a la lista y al mapa.
	 * 
	 * @param idVehiculo identificador del vehiculo
	 * @param vehiculo Vehiculo a añadir
	 * @throws ErrorDeSimulacion El vehiculo ya ha sido añadido con anterioridad
	 */
	public void addVehiculo(String idVehiculo, Vehiculo vehiculo) throws ErrorDeSimulacion {
		if(!mapaDeVehiculos.containsKey(idVehiculo)) {
			mapaDeVehiculos.put(idVehiculo, vehiculo);
			vehiculos.add(vehiculo);
			vehiculo.moverASiguienteCarretera();
		}
		else throw new ErrorDeSimulacion("El vehiculo " + idVehiculo + " ya existe, no puede ser añadido");
		// comprueba que “idVehiculo” no existe en el mapa.
		// Si no existe, lo añade a “vehiculos” y a “mapaDeVehiculos”,
		// y posteriormente solicita al vehiculo que se mueva a la siguiente
		// carretera de su itinerario (moverASiguienteCarretera).
		// Si existe lanza una excepción.
	}

	/**
	 * Añade la carretera a la lista y al mapa
	 * 
	 * @param idCarretera identificador de la carretera
	 * @param origen Cruce de origen de la carretera
	 * @param carretera Carretera a añadir
	 * @param destino cruce de destino de la carretera
	 * @throws ErrorDeSimulacion La carretera ya ha sido añadida con anterioridad
	 */
	public void addCarretera(String idCarretera, CruceGenerico<?> origen, Carretera carretera, CruceGenerico<?> destino) throws ErrorDeSimulacion {
		if(!mapaDeCarreteras.containsKey(idCarretera)) {
			mapaDeCarreteras.put(idCarretera, carretera);
			carreteras.add(carretera);
			
			origen.addCarreteraSalienteAlCruce(destino, carretera);
			destino.addCarreteraEntranteAlCruce(idCarretera, carretera);
		}
		else throw new ErrorDeSimulacion("La carretera " + idCarretera + " ya existe, no puede ser añadida");
		// comprueba que “idCarretera” no existe en el mapa.
		// Si no existe, lo añade a “carreteras” y a “mapaDeCarreteras”,
		// y posteriormente actualiza los cruces origen y destino como sigue:
		// - Añade al cruce origen la carretera, como “carretera saliente”
		// - Añade al crude destino la carretera, como “carretera entrante”
		// Si existe lanza una excepción.
	}

	/**
	 * Genera el informe de los objetos de la simulación:
	 * 	cruces, carreteras y vehículos.
	 * 
	 * @param time momento de la simulación en el que se escribe el informe
	 * @return Informe
	 */
	public String generateReport(int time) {
		String report = "";
		for (CruceGenerico<?> c : this.cruces)
			report += c.generaInforme(time) + "\n";
		for (Carretera ca : this.carreteras)
			report += ca.generaInforme(time) + "\n";
		for (Vehiculo v : this.vehiculos)
			report += v.generaInforme(time) + "\n";

		return report;
	}

	/**
	 * Actualiza las carreteras y los vehículos
	 * 
	 * @throws ErrorDeSimulacion No se ha podido actualizar
	 */
	public void actualizar() throws ErrorDeSimulacion {
		for (Carretera c : carreteras)
			c.avanza();
		
		for (CruceGenerico<?> elem : cruces)
			elem.avanza();
	}

	/**
	 * Devuelve el cruce que coincide con el identificador dado.
	 * 
	 * @param id identificador del cruce
	 * @return Cruce que coincide con el identificador dado.
	 * @throws ErrorDeSimulacion No existe el cruce pedido
	 */
	public CruceGenerico<?> getCruce(String id) throws ErrorDeSimulacion {
		CruceGenerico<?> c = mapaDeCruces.get(id);
		if (c != null)
			return c;
		else
			throw new ErrorDeSimulacion("El cruce '" + id + "' no existe.");
		// devuelve el cruce con ese “id” utilizando el mapaDeCruces.
		// si no existe el cruce lanza excepción.
	}

	/**
	 * Devuelve el vehículo que coincide con el identificador dado.
	 * 
	 * @param id identificador del vehículo
	 * @return Vehiculo que coincide con el identificador dado.
	 * @throws ErrorDeSimulacion No existe el vehículo pedido
	 */
	public Vehiculo getVehiculo(String id) throws ErrorDeSimulacion {
		Vehiculo v = mapaDeVehiculos.get(id);
		if (v != null)
			return v;
		else
			throw new ErrorDeSimulacion("El vehículo '" + id + "' no existe.");
		// devuelve el vehículo con ese “id” utilizando el mapaDeVehiculos.
		// si no existe el vehículo lanza excepción.
	}

	/**
	 * Devuelve la carretera que coincide con el identificador dado.
	 * 
	 * @param id identificador de la carretera
	 * @return Carretera que coincide con el identificador dado.
	 * @throws ErrorDeSimulacion No existe la carretera pedida
	 */
	public Carretera getCarretera(String id) throws ErrorDeSimulacion {
		Carretera c = mapaDeCarreteras.get(id);
		if (c != null)
			return c;
		else
			throw new ErrorDeSimulacion("La carretera '" + id + "' no existe.");
		// devuelve la carretera con ese “id” utilizando el mapaDeCarreteras.
		// si no existe la carretra lanza excepción.
	}

	public List<CruceGenerico<?>> getCruces() {
		return this.cruces;
	}

	public List<Carretera> getCarreteras() {
		return this.carreteras;
	}

	public List<Vehiculo> getVehiculos() {
		return this.vehiculos;
	}

	public void reinicia() {
		this.carreteras.clear();
		this.cruces.clear();
		this.mapaDeCarreteras.clear();
		this.mapaDeCruces.clear();
		this.mapaDeVehiculos.clear();
		this.vehiculos.clear();
	}
}
