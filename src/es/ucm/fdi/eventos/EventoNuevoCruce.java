package es.ucm.fdi.eventos;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.Cruce;
import es.ucm.fdi.model.cruces.CruceGenerico;

public class EventoNuevoCruce extends Evento {

	protected String id;

	public EventoNuevoCruce(int time, String id) {
		super(time);
		this.id = id;
	}

	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		// crea el cruce y se lo añade al mapa
		mapa.addCruce(this.id, this.creaCruce());
	}

	protected CruceGenerico<?> creaCruce() {
		return new Cruce(this.id);
	}
	
	@Override
	public String toString() {
		return "New Junction";
	}

}
