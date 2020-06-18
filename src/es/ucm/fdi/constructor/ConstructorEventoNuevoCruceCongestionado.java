package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevoCruceCongestionado;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevoCruceCongestionado extends ConstructorEventoNuevoCruce{
	public ConstructorEventoNuevoCruceCongestionado() {
		this.etiqueta = "new_junction";
		this.claves = new String[] { "time", "id", "type" };
		this.valoresPorDefecto = new String[] { "", "", "mc" };
	}
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta)) && section.getValue("type").equals("mc")) {
			return new EventoNuevoCruceCongestionado(
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[0], 0),
					ConstructorEventos.identificadorValido(section, this.claves[1]));
		} else
			return null;
	}
	
	@Override
	public String toString() {
		return "Nuevo Cruce Congestionado";
	}
}
