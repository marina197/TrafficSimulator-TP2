package es.ucm.fdi.eventos;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;

public class EventoNuevaCarretera extends Evento {

	protected String id;
	protected Integer velocidadMaxima;
	protected Integer longitud;
	protected String cruceOrigenId;
	protected String cruceDestinoId;

	public EventoNuevaCarretera(int tiempo, String id, String origen,
			String destino, int velocidadMaxima, int longitud) {
		super(tiempo);
		this.id = id;
		this.cruceOrigenId = origen;
		this.cruceDestinoId = destino;
		this.velocidadMaxima = velocidadMaxima;
		this.longitud = longitud;
	}

	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		// obten cruce origen y cruce destino utilizando el mapa
		CruceGenerico<?> or = mapa.getCruce(this.cruceOrigenId);
		CruceGenerico<?> dest = mapa.getCruce(this.cruceDestinoId);
		if (or == null || dest == null)
			throw new ErrorDeSimulacion("El origen o destino de la carretera es nulo");
		// crea la carretera
		Carretera c = creaCarretera(or, dest);
		// añade al mapa la carretera
		mapa.addCarretera(c.getId(), or, c, dest);
	}
	
	protected Carretera creaCarretera(CruceGenerico<?> cruceOrigen, CruceGenerico<?> cruceDestino) {
		return new Carretera(this.id, this.longitud, this.velocidadMaxima, cruceOrigen, cruceDestino);
	}
	
	@Override
	public String toString() {
		return "New Road";
	}

}
