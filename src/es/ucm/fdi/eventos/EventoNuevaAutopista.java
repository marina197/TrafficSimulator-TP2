package es.ucm.fdi.eventos;

import es.ucm.fdi.model.carreteras.Autopista;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;

public class EventoNuevaAutopista extends EventoNuevaCarretera {

	protected Integer numCarriles;

	public EventoNuevaAutopista(int tiempo, String id, String origen, String destino, int velocidadMaxima, int longitud,
			int numCarriles) {
		super(tiempo, id, origen, destino, velocidadMaxima, longitud);
		this.numCarriles = numCarriles;
	}

	@Override
	protected Carretera creaCarretera(CruceGenerico<?> cruceOrigen, CruceGenerico<?> cruceDestino) {
		return new Autopista(this.id, this.longitud, this.velocidadMaxima, cruceOrigen, cruceDestino, this.numCarriles);
	}

}
