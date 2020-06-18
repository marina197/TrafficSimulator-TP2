package es.ucm.fdi.eventos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Coche;
import es.ucm.fdi.model.vehiculos.Vehiculo;

public class EventoNuevoCoche extends EventoNuevoVehiculo{
	private int resistencia;
	private double probabilidad;
	private long semilla;
	private int duracionMaximaAveria;
	
	public EventoNuevoCoche(int tiempo, String id, int velocidadMaxima, String[] itinerario, int resistencia,
			double probabilidad, int duracionMaximaAveria, long semilla) {
		super(tiempo, id, velocidadMaxima, itinerario);
		this.resistencia = resistencia;
		this.probabilidad = probabilidad;
		this.duracionMaximaAveria = duracionMaximaAveria;
		this.semilla = semilla;
	}
	
	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		List<CruceGenerico<?>> iti = ParserCarreteras.parseaListaCruces(this.itinerario, mapa);
		if (iti == null || iti.size() < 2)
			throw new ErrorDeSimulacion("En el itinerario el coche " + this.id + " hay menos de 2 cruces.");
		Vehiculo v = new Coche(this.id, this.velocidadMaxima, this.resistencia, this.probabilidad, this.semilla, this.duracionMaximaAveria, iti);
		mapa.addVehiculo(v.getId(), v);
	}

}
