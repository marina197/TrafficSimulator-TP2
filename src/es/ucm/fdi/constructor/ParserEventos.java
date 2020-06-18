package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.ini.IniSection;

public class ParserEventos {

	private static ConstructorEventos[] eventos = { 
			new ConstructorEventoNuevoCruce(),
			new ConstructorEventoNuevoCruceCircular(),
			new ConstructorEventoNuevoCruceCongestionado(),
			new ConstructorEventoNuevaCarretera(),
			new ConstructorEventoNuevaAutopista(),
			new ConstructorEventoNuevoCamino(),
			new ConstructorEventoNuevoVehiculo(),
			new ConstructorEventoNuevaBicicleta(),
			new ConstructorEventoNuevoCoche(),
			new ConstructorEventoAveriaCoche() 
			};

	// Bucle de prueba y error
	public static Evento parseaEvento(IniSection sec) throws IllegalArgumentException {
		int i = 0;
		boolean seguir = true;
		Evento e = null;

		while (i < ParserEventos.eventos.length && seguir) {
			// ConstructorEventos contiene el método parse(sec)
			e = ParserEventos.eventos[i].parser(sec);
			if (e != null)
				seguir = false;
			else
				i++;
		}
		return e;
	}

	public static ConstructorEventos[] getConstructoresEventos() {
		return ParserEventos.eventos;
	}

}
