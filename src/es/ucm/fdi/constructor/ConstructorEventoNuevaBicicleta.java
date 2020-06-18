package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoNuevaBicicleta;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoNuevaBicicleta extends ConstructorEventoNuevoVehiculo{
	public ConstructorEventoNuevaBicicleta() {
		this.etiqueta = "new_vehicle";
		this.claves = new String[] { "time", "id", "itinerary","max_speed", "type" };
		this.valoresPorDefecto = new String[] { "", "", "", "", "bike" };
	}
	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if ((section.getValue("type") != null && section.getTag().equals(this.etiqueta)) && section.getValue("type").equals("bike")) {
			return new EventoNuevaBicicleta(
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[0], 0),
					// extrae el valor del campo "id" de la sección
					ConstructorEventos.identificadorValido(section, this.claves[1]),
					ConstructorEventos.parseaIntNoNegativo(section, this.claves[3]),
					section.getValue(claves[2]).split(","));
		} else
			return null;
	}
	
	@Override
	public String toString() {
		return "Nueva Bicicleta";
	}
}
