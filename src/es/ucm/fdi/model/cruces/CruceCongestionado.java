package es.ucm.fdi.model.cruces;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.CarreteraEntranteConIntervalo;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;
/**
 * <h1>Clase CruceCongestionado</h1>
 * 
 * <p>
 * Clase que realiza las operaciones de los cruces congestionados
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */

public class CruceCongestionado extends CruceGenerico<CarreteraEntranteConIntervalo> {

	/**
	 * Constructor de la clase
	 * @param id identificador del cruce
	 */
	public CruceCongestionado(String id) {
		super(id);
	}

	@Override
	protected void actualizaSemaforos() {
		if (this.indiceSemaforoVerde == -1) {
			int max = -1, i = -1;
			for (CarreteraEntranteConIntervalo ci : this.carreterasEntrantes) {
				if (ci.numVehiculosEnCola() > max) {
					max = ci.numVehiculosEnCola();
					i++;
				}
			}
			this.indiceSemaforoVerde = i;

			CarreteraEntranteConIntervalo cc = this.carreterasEntrantes.get(this.indiceSemaforoVerde);
			cc.ponSemaforo(true);
			cc.setIntervaloDeTiempo(Math.max(cc.numVehiculosEnCola() / 2, 1));
			cc.setUnidadesDeTiempoUsadas(0);
			cc.setUsadaPorUnVehiculo(false);
			cc.setUsoCompleto(false);
		} else {
			CarreteraEntranteConIntervalo ri, rj = null;
			ri = this.carreterasEntrantes.get(this.indiceSemaforoVerde % this.carreterasEntrantes.size());
			ri.setUnidadesDeTiempoUsadas(ri.getUnidadesDeTiempoUsadas() + 1);
			if (ri.tiempoConsumido()) {
				ri.ponSemaforo(false);
				// Inicializar atributos de ri

				ri.setIntervaloDeTiempo(0);
				ri.setUnidadesDeTiempoUsadas(0);
				ri.setUsadaPorUnVehiculo(false);
				ri.setUsoCompleto(false);

				// Buscar la carretera con más vehículos
				int max = -1, i = 0, j = 0;
				if (this.carreterasEntrantes.size() > 1) {
					for (CarreteraEntranteConIntervalo ci : this.carreterasEntrantes) {
						if (!ci.toString().equals(ri.toString())) {
							if (ci.numVehiculosEnCola() > max) {
								max = ci.numVehiculosEnCola();
								j = i;
							}
						}
						i++;
					}
				} else
					j = 0;

				this.indiceSemaforoVerde = j;
				rj = this.carreterasEntrantes.get(this.indiceSemaforoVerde % this.carreterasEntrantes.size());
				rj.ponSemaforo(true);
				rj.setIntervaloDeTiempo(Math.max(rj.numVehiculosEnCola() / 2, 1));
				rj.setUnidadesDeTiempoUsadas(0);
				rj.setUsadaPorUnVehiculo(false);
				rj.setUsoCompleto(false);
			}
		}
	}

	@Override
	protected CarreteraEntranteConIntervalo creaCarreteraEntrante(Carretera carretera) {
		CarreteraEntranteConIntervalo cei = new CarreteraEntranteConIntervalo(carretera, 0);
		return cei;
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

		for (CarreteraEntranteConIntervalo c : this.carreterasEntrantes) {
			if (!coma)
				coma = true;
			else
				value += ",";
			value += "(" + c.getCarretera().getId() + ","
					+ (c.tieneSemaforoVerde() ? "green" + (c.getIntervaloDeTiempo() > 0 ? ":" + c.getIntervaloDeTiempo() : "")
							: "red")
					+ "," + "[";
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
		is.setValue("type", "mc");
	}
	
	@Override
	public String verde() {
		String value = "";
		boolean coma = false;
		boolean coma2 = false;
		for (CarreteraEntranteConIntervalo c : this.carreterasEntrantes) {
			if (c.tieneSemaforoVerde()) {
				if (!coma)
					coma = true;
				else
					value += ",";
				value += "(" + c.getCarretera().getId() + "," + "green"
						+ (c.getIntervaloDeTiempo() > 0 ? ":" + c.getIntervaloDeTiempo() : "") + "," + "[";
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
