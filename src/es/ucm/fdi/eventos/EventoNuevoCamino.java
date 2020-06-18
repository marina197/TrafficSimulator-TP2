package es.ucm.fdi.eventos;

import es.ucm.fdi.model.carreteras.Camino;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;

public class EventoNuevoCamino extends EventoNuevaCarretera{

	public EventoNuevoCamino(int tiempo, String id, String origen, String destino, int velocidadMaxima, int longitud) {
		super(tiempo, id, origen, destino, velocidadMaxima, longitud);
	}

	@Override
	protected Carretera creaCarretera(CruceGenerico<?> cruceOrigen, CruceGenerico<?> cruceDestino) {
		return new Camino(this.id, this.longitud, this.velocidadMaxima, cruceOrigen, cruceDestino);
	}

}
