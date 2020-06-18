package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoCoche;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoCoche extends ConstructorEventoNuevoVehiculo {
	public ConstructorEventoNuevoCoche() {
		this.etiqueta = "new_vehicle";
		this.claves = new String[] { "time", "id", "itinerary","max_speed", "resistance", "fault_probability",
				"max_fault_duration", "seed",  "type" };
		this.valoresPorDefecto = new String[] {"","","","","","","","","car"};
	}
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta))
				&& section.getValue("type").equals("car")) {
			return new EventoNuevoCoche(ConstructorEventos.parseaIntNoNegativo(section, this.claves[0], 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, this.claves[1]),
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[3]),
					section.getValue(claves[2]).split(","),
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[4]),
					ConstructorEventos.parseaDoubleNoNegativo(section, this.claves[5], 0),
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[6]),
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[7]));
		} else
			return null;
	}

	@Override
	public String toString() {
		return "Nuevo Coche";
	}
}
