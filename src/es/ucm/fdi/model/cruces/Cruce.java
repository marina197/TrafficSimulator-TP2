package es.ucm.fdi.model.cruces;

import es.ucm.fdi.model.CarreteraEntrante;
import es.ucm.fdi.model.carreteras.Carretera;

/**
 * <h1>Clase Cruce</h1>
 * 
 * <p>
 * Realiza las operaciones de los cruces.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Cruce extends CruceGenerico<CarreteraEntrante> {
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador del cruce
	 */
	public Cruce(String id) {
		super(id);
	}

	@Override
	protected void actualizaSemaforos() {
		if (this.indiceSemaforoVerde == -1) {
			this.indiceSemaforoVerde++;
			this.carreterasEntrantes.get(indiceSemaforoVerde).ponSemaforo(true);
		} else {
			this.carreterasEntrantes.get(indiceSemaforoVerde % this.carreterasEntrantes.size()).ponSemaforo(false);
			this.indiceSemaforoVerde++;
			this.carreterasEntrantes.get(indiceSemaforoVerde % this.carreterasEntrantes.size()).ponSemaforo(true);
		}
	}


	@Override
	protected String getNombreSeccion() {
		return "junction_report";
	}


	@Override
	protected CarreteraEntrante creaCarreteraEntrante(Carretera carretera) {
		CarreteraEntrante ce = new CarreteraEntrante(carretera);
		return ce;
	}

}