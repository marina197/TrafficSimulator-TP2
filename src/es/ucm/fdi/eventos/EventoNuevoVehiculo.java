package es.ucm.fdi.eventos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;

public class EventoNuevoVehiculo extends Evento {

	protected String id;
	protected Integer velocidadMaxima;
	protected String[] itinerario;

	public EventoNuevoVehiculo(int tiempo, String id, int velocidadMaxima,
			String[] itinerario) {
		super(tiempo);
		this.id = id;
		this.velocidadMaxima = velocidadMaxima;
		this.itinerario = itinerario;
	}

	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		// si iti es null o tiene menos de dos cruces lanzar excepción
		// en otro caso crear el vehículo y añadirlo al mapa.
		List<CruceGenerico<?>> iti = ParserCarreteras.parseaListaCruces(this.itinerario, mapa);
		if (iti == null || iti.size() < 2)
			throw new ErrorDeSimulacion("En el itinerario del vehiculo " + this.id + " hay menos de 2 cruces.");
		Vehiculo v = new Vehiculo(this.id, this.velocidadMaxima, iti);
		mapa.addVehiculo(v.getId(), v);
	}

	@Override
	public String toString() {
		return "New Vehicle";
	}
	
}
