package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.eventos.EventoAveriaCoche;
import es.ucm.fdi.ini.IniSection;

public class ConstructorEventoAveriaCoche extends ConstructorEventos {

	public ConstructorEventoAveriaCoche() {
		this.etiqueta = "make_vehicle_faulty";
		this.claves = new String[] { "time", "vehicles", "duration" };
		this.valoresPorDefecto=new String[] {"","",""};
	}

	@Override
	public Evento parser(IniSection section) throws IllegalArgumentException {
		if (!section.getTag().equals(this.etiqueta) || section.getValue("type") != null)
			return null;
		else
			return new EventoAveriaCoche(
					// Extrae el valor del campo "time" en la sección, 0 es el valor por defecto en
					// caso de no especificar el tiempo.
					ConstructorEventos.parseaIntNoNegativo(section,claves[0], 0),
					section.getValue(claves[1]).split(","),
					ConstructorEventos.parseaIntNoNegativo(section, claves[2]));
	}

	@Override
	public String toString() {
		return "Avería Vehículo";
	}
}
