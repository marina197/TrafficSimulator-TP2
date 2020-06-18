package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoVehiculo;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoVehiculo extends ConstructorEventos {

	public ConstructorEventoNuevoVehiculo() {
		this.etiqueta = "new_vehicle";
		this.claves = new String[] { "time", "id", "itinerary", "max_speed" };
		this.valoresPorDefecto = new String[] { "", "", "", "" };
	}

	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if (!section.getTag().equals(this.etiqueta) || section.getValue("type") != null)
			return null;
		else {
			return new EventoNuevoVehiculo(
					// Extrae el valor del campo "time" en la sección, 0 es el valor por defecto en
					// caso de no especificar el tiempo.
					ConstructorEventos.parseaIntNoNegativo(section, claves[0], 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, claves[1]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[3]),
					section.getValue(claves[2]).split(","));
		}

	}

	@Override
	public String toString() {
		return "Nuevo Vehículo";
	}

}
