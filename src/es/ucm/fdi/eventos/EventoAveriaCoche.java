package es.ucm.fdi.eventos;

import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.vehiculos.Vehiculo;

public class EventoAveriaCoche extends Evento {

	protected String[] vehiculos;
	protected int duracion;

	public EventoAveriaCoche(int tiempo, String[] vehiculos, int duracion) {
		super(tiempo);
		this.vehiculos = vehiculos;
		this.duracion = duracion;
	}

	@Override
	public void ejecuta(MapaCarreteras mapa) throws ErrorDeSimulacion {
		// Avería coches
		List<Vehiculo> v = ParserCarreteras.parseaListaVehiculos(this.vehiculos, mapa);
		if (v == null)
			throw new ErrorDeSimulacion("El vehículo no existe");
		else {
			for (Vehiculo elem : v) {
				elem.setTiempoAveria(this.duracion);
			}
		}
	}

	@Override
	public String toString() {
		return "Make Vehicle Faulty";
	}
}
