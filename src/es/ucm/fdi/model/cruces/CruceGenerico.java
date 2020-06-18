package es.ucm.fdi.model.cruces;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.CarreteraEntrante;
import es.ucm.fdi.model.ObjetoSimulacion;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase CruceGenerico</h1>
 * 
 * <p>
 * Contiene la información de los cruces.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
abstract public class CruceGenerico<T extends CarreteraEntrante> extends ObjetoSimulacion {
	/**
	 * Indice que indica qué carretera tiene el semáforo en verde
	 */
	protected int indiceSemaforoVerde;
	/**
	 * Lista de carreteras entrantes
	 */
	protected List<T> carreterasEntrantes;
	/**
	 * Lista de carreteras salientes
	 */
	protected List<Carretera> carreterasSalientes;
	/**
	 * Mapa de carreteras entrantes
	 */
	protected Map<String, T> mapaCarreterasEntrantes;
	/**
	 * Mapa de carreteras salientes
	 */
	protected Map<CruceGenerico<?>, Carretera> mapaCarreterasSalientes;

	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador del cruce
	 */
	public CruceGenerico(String id) {
		super(id);
		this.carreterasEntrantes = new LinkedList<T>();
		this.carreterasSalientes = new LinkedList<Carretera>();
		this.mapaCarreterasEntrantes = new HashMap<String, T>();
		this.mapaCarreterasSalientes = new HashMap<CruceGenerico<?>, Carretera>();
		this.indiceSemaforoVerde = -1;
	}

	/**
	 * Devuelve la carretera que va desde el cruce dado al cruce actual
	 * 
	 * @param cruce Cruce de origen
	 * @return carretera que va desde el cruce dado al cruce actual
	 */
	public Carretera carreteraHaciaCruce(CruceGenerico<?> cruce) {
		return this.mapaCarreterasSalientes.get(cruce);
	}

	/**
	 * Añade al mapa y a la lista la carretera
	 * 
	 * @param idCarretera identificador de la carretera
	 * @param carretera Carretera a añadir
	 */
	public void addCarreteraEntranteAlCruce(String idCarretera, Carretera carretera) {
		T ri = creaCarreteraEntrante(carretera);
		this.mapaCarreterasEntrantes.put(idCarretera, ri);
		this.carreterasEntrantes.add(ri);
	}

	/**
	 * Crea la carretera entrante
	 * 
	 * @param carretera Carretera
	 * @return Carretera entrante
	 */
	abstract protected T creaCarreteraEntrante(Carretera carretera);

	/**
	 * Añade la carretera saliente al mapa y a la lista
	 * 
	 * @param destino Cruce destino
	 * @param carr Carretera
	 */
	public void addCarreteraSalienteAlCruce(CruceGenerico<?> destino, Carretera carr) {
		mapaCarreterasSalientes.put(destino, carr);
		carreterasSalientes.add(carr);
	}

	/**
	 * Añade vehículo al cruce
	 * 
	 * @param idCarretera identificador de la carretera
	 * @param vehiculo Vehiculo que entra al cruce
	 * @throws ErrorDeSimulacion No se ha podido añadir el vehículo al cruce
	 */
	public void entraVehiculoAlCruce(String idCarretera, Vehiculo vehiculo) throws ErrorDeSimulacion {
		T ce = mapaCarreterasEntrantes.get(idCarretera);
		ce.getColaVehiculos().add(vehiculo);
	}

	@Override
	public void avanza() throws ErrorDeSimulacion {

		CarreteraEntrante c = null;
		if (!carreterasEntrantes.isEmpty()) {
			if (this.indiceSemaforoVerde == -1) {
				c = carreterasEntrantes.get(0);
			} else {
				c = carreterasEntrantes.get(this.indiceSemaforoVerde % this.carreterasEntrantes.size());
			}

			c.avanzaPrimerVehiculo();
			this.actualizaSemaforos();
		}
	}

	/**
	 * Actualiza los semáforos
	 */
	abstract protected void actualizaSemaforos();

	@Override
	protected void completaDetallesSeccion(IniSection is) {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;

		for (CarreteraEntrante c : this.carreterasEntrantes) {
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + c.getCarretera().getId() + "," + (c.tieneSemaforoVerde() ? "green" : "red") + "," + "[";
			coma2 = false;
			for (Vehiculo v : c.getColaVehiculos()) {
				if (!coma2)
					coma2 = true;
				else
					value += ",";
				value += v.getId();
			}
			value += "])";
		}

		is.setValue("queues", value);
	}

	public List<T> getCarreteras() {
		return this.carreterasEntrantes;
	}

	public int getIndiceSemaforoVerde() {
		return indiceSemaforoVerde;
	}

	@Override
	public String toString() {
		return this.getId();
	}
	
	public String verde() {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;

		for (CarreteraEntrante c : this.carreterasEntrantes) {
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + c.getCarretera().getId() + "," + "green" + "," + "[";
			coma2 = false;
			for (Vehiculo v : c.getColaVehiculos()) {
				if (!coma2)
					coma2 = true;
				else
					value += ",";
				value += v.getId();
			}
			value += "])";
		}

		return value;
	}
	
	public String rojo() {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;

		for (CarreteraEntrante c : this.carreterasEntrantes) {
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + c.getCarretera().getId() + "," + "red" + "," + "[";
			coma2 = false;
			for (Vehiculo v : c.getColaVehiculos()) {
				if (!coma2)
					coma2 = true;
				else
					value += ",";
				value += v.getId();
			}
			value += "])";
		}

		return value;
	}
}
