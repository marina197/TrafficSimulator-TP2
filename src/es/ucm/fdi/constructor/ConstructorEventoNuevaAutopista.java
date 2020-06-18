package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevaAutopista;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevaAutopista extends ConstructorEventoNuevaCarretera{
	public ConstructorEventoNuevaAutopista() {
		this.etiqueta = "new_road";
		this.claves = new String[] { "time", "id", "src", "dest", "max_speed", "length","lanes", "type" };
		this.valoresPorDefecto = new String[] {"", "", "", "", "", "","", "lanes"};
	}
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta)) && section.getValue("type").equals("lanes")) {
			return new EventoNuevaAutopista(
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[0], 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, this.claves[1]),
					ConstructorEventos.identificadorValido(section, claves[2]),
					ConstructorEventos.identificadorValido(section, claves[3]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[4]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[5]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[6]));
		} else
			return null;
	}
	
	@Override
	public String toString() {
		return "Nueva Autopista";
	}
}
