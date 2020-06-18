package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoCamino;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoCamino extends ConstructorEventoNuevaCarretera{
	public ConstructorEventoNuevoCamino() {
		this.etiqueta = "new_road";
		this.claves = new String[] { "time", "id", "src", "dest", "max_speed", "length", "type" };
		this.valoresPorDefecto = new String[] {"", "", "", "", "", "", "dirt"};
	}
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta)) && section.getValue("type").equals("dirt")) {
			return new EventoNuevoCamino(
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[0], 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, this.claves[1]),
					ConstructorEventos.identificadorValido(section, claves[2]),
					ConstructorEventos.identificadorValido(section, claves[3]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[4]),
					ConstructorEventos.parseaIntNoNegativo(section, claves[5]));
		} else
			return null;
	}
	
	@Override
	public String toString() {
		return "Nuevo Camino";
	}
}
