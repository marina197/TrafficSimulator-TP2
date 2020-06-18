package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoCruce;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoCruce extends ConstructorEventos {

	public ConstructorEventoNuevoCruce() {
		this.etiqueta = "new_junction";
		this.claves = new String[] { "time", "id" };
		this.valoresPorDefecto = new String[] { "", "" };
	}

	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if (!section.getTag().equals(this.etiqueta) || section.getValue("type") != null)
			return null;
		else {
			return new EventoNuevoCruce(
					// Extrae el valor del campo "time" en la sección, 0 es el valor por defecto en
					// caso de no especificar el tiempo.
					ConstructorEventos.parseaIntNoNegativo(section, "time", 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, "id"));

		}
	}

	@Override
	public String toString() {
		return "Nuevo Cruce";
	}

}
