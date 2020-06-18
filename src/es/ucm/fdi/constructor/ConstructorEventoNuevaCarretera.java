package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevaCarretera;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevaCarretera extends ConstructorEventos {

	public ConstructorEventoNuevaCarretera() {
		this.etiqueta = "new_road";
		this.claves = new String[] { "time", "id", "src", "dest", "max_speed", "length" };
		this.valoresPorDefecto = new String[] {"", "", "","","",""};
	}


	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		Evento e = null;
		if (!section.getTag().equals(this.etiqueta))
			return null;
		else {
			if (section.getValue("type") != null) {
				return e;
			} else {
				return new EventoNuevaCarretera(
						// Extrae el valor del campo "time" en la sección, 0 es el valor por defecto en
						// caso de no especificar el tiempo.
						ConstructorEventos.parseaIntNoNegativo(section, claves[0], 0),
						// extrae el valor del campo "id" de la sección
						ConstructorEventos.identificadorValido(section, claves[1]),
						ConstructorEventos.identificadorValido(section, claves[2]),
						ConstructorEventos.identificadorValido(section, claves[3]),
						ConstructorEventos.parseaIntNoNegativo(section, claves[4]),
						ConstructorEventos.parseaIntNoNegativo(section, claves[5]));
			}
		}
	}

	@Override
	public String toString() {
		return "Nueva Carretera";
	}
}
