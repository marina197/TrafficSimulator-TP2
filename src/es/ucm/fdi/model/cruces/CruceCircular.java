package es.ucm.fdi.model.cruces;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.CarreteraEntranteConIntervalo;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;

/**
 * <h1>Clase CruceCircular</h1>
 * 
 * <p>
 * Realiza las operaciones de los cruces circulares.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */

public class CruceCircular extends CruceGenerico<CarreteraEntranteConIntervalo> {
	/**
	 * Máximo valor del intervalo
	 */
	protected int maxValorIntervalo;
	/**
	 * Mínimo valor del intervalo
	 */
	protected int minValorIntervalo;

	/**
	 * Constructor de la clase
	 * 
	 * @param id
	 *            identificador del cruce
	 * @param minValorIntervalo
	 *            mínimo valor del intervalo
	 * @param maxValorIntervalo
	 *            máximo valor del intervalo
	 */
	public CruceCircular(String id, int minValorIntervalo, int maxValorIntervalo) {
		super(id);
		this.maxValorIntervalo = maxValorIntervalo;
		this.minValorIntervalo = minValorIntervalo;
	}

	@Override
	protected CarreteraEntranteConIntervalo creaCarreteraEntrante(Carretera carretera) {
		CarreteraEntranteConIntervalo cei = new CarreteraEntranteConIntervalo(carretera, maxValorIntervalo);
		return cei;
	}

	@Override
	protected void actualizaSemaforos() {
		if (this.indiceSemaforoVerde == -1) {
			this.carreterasEntrantes.get(0).ponSemaforo(true);
			this.indiceSemaforoVerde = 0;
			this.carreterasEntrantes.get(0).setUnidadesDeTiempoUsadas(0);
		} else {
			CarreteraEntranteConIntervalo ri, rj;
			ri = this.carreterasEntrantes.get(this.indiceSemaforoVerde % this.carreterasEntrantes.size());
			if (ri.tiempoConsumido()) {
				ri.ponSemaforo(false);

				if (ri.usoCompleto())
					ri.setIntervaloDeTiempo(Math.min(ri.getIntervaloDeTiempo() + 1, this.maxValorIntervalo));
				if (!ri.usada())
					ri.setIntervaloDeTiempo(Math.max(ri.getIntervaloDeTiempo() - 1, this.minValorIntervalo));

				ri.setvehiculosPasado(0);

				ri.setUnidadesDeTiempoUsadas(0);
				this.indiceSemaforoVerde++;
				rj = this.carreterasEntrantes.get(indiceSemaforoVerde % this.carreterasEntrantes.size());
				rj.ponSemaforo(true);
				rj.setUsadaPorUnVehiculo(false);
				rj.setUsoCompleto(false);
			}
		}
	}

	@Override
	protected String getNombreSeccion() {
		return "junction_report";
	}

	@Override
	protected void completaDetallesSeccion(IniSection is) {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;
		int tiempo;
		
		for (CarreteraEntranteConIntervalo c : this.carreterasEntrantes) {
			tiempo = c.getIntervaloDeTiempo() - c.getUnidadesDeTiempoUsadas();
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + c.getCarretera().getId() + ","
					+ (c.tieneSemaforoVerde() ? "green" + (c.getIntervaloDeTiempo() > 0 ? ":" + tiempo : "") : "red") + ","
					+ "[";
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
		is.setValue("type", "rr");
	}
	
	@Override
	public String verde() {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;
		int tiempo;

		for (CarreteraEntranteConIntervalo c : this.carreterasEntrantes) {
			if (c.tieneSemaforoVerde()) {
				tiempo = c.getIntervaloDeTiempo() - c.getUnidadesDeTiempoUsadas();
				if (!coma)
					coma = true;
				else
					value += ",";
				value += "(" + c.getCarretera().getId() + "," + "green"
						+ (c.getIntervaloDeTiempo() > 0 ? ":" + tiempo : "") + "," + "[";
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
		}
		return value;
	}
	
	public String rojo() {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;

		for (CarreteraEntranteConIntervalo c : this.carreterasEntrantes) {
			if (!c.tieneSemaforoVerde()) {
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
		}
		return value;
	}

}
