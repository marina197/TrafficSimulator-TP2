package es.ucm.fdi.eventos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Bicicleta;
import es.ucm.fdi.model.vehiculos.Vehiculo;

public class EventoNuevaBicicleta extends EventoNuevoVehiculo{

	public EventoNuevaBicicleta(int tiempo, String id, int velocidadMaxima, String[] itinerario) {
		super(tiempo, id, velocidadMaxima, itinerario);
	}

	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		// si iti es null o tiene menos de dos cruces lanzar excepción
		// en otro caso crear el vehículo y añadirlo al mapa.
		List<CruceGenerico<?>> iti = ParserCarreteras.parseaListaCruces(this.itinerario, mapa);
		if (iti == null || iti.size() < 2)
			throw new ErrorDeSimulacion("En el itinerario la bicicleta " + this.id + " hay menos de 2 cruces.");
		Vehiculo v = new Bicicleta(this.id, this.velocidadMaxima, iti);
		mapa.addVehiculo(v.getId(), v);
	}
}
