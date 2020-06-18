package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoCruceCircular;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoCruceCircular extends ConstructorEventoNuevoCruce {
	
	public ConstructorEventoNuevoCruceCircular() {
		this.etiqueta = "new_junction";
		this.claves = new String[] { "time", "id", "type", "min_time_slice", "max_time_slice" };
		this.valoresPorDefecto = new String[] { "", "", "rr", "", "" };
	}
	
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta)) && section.getValue("type").equals("rr")) {
			return new EventoNuevoCruceCircular(
					// Extrae el valor del campo "time" en la sección, 0 es el valor por defecto en
					// caso de no especificar el tiempo.
					ConstructorEventos.parseaIntNoNegativo(section, "time", 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, "id"),
					ConstructorEventos.parseaInt(section, claves[3]),
					ConstructorEventos.parseaInt(section, claves[4]));
		} else
			return null;
	}

	@Override
	public String toString() {
		return "Nuevo Cruce Circular";
	}

}
