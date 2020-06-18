package es.ucm.fdi.eventos;

import es.ucm.fdi.model.cruces.CruceCongestionado;
import es.ucm.fdi.model.cruces.CruceGenerico;

public class EventoNuevoCruceCongestionado extends EventoNuevoCruce{
	
	public EventoNuevoCruceCongestionado(int time, String id) {
		super(time, id);
	}

	@Override
	protected CruceGenerico<?> creaCruce(){
		return new CruceCongestionado(this.id);
	}

}
