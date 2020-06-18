package es.ucm.fdi.eventos;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;

public class ParserCarreteras {

	public static List<CruceGenerico<?>> parseaListaCruces(String[] itinerario, MapaCarreteras mapa) throws ErrorDeSimulacion {
		int i = 0;
		boolean seguir = true;
		CruceGenerico<?> c = null;
		List<CruceGenerico<?>> listaCruces = new ArrayList<CruceGenerico<?>>();

		while (i < itinerario.length && seguir) {
			c = mapa.getCruce(itinerario[i]);
			if (c == null)
				seguir = false;
			else {
				listaCruces.add(c);
				i++;
			}
		}

		if (listaCruces.size() == itinerario.length)
			return listaCruces;
		else
			return null;
	}

	public static List<Vehiculo> parseaListaVehiculos(String[] vehiculos, MapaCarreteras mapa)
			throws ErrorDeSimulacion {
		int i = 0;
		boolean seguir = true;
		Vehiculo v = null;
		List<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();

		while (i < vehiculos.length && seguir) {
			v = mapa.getVehiculo(vehiculos[i]);
			if (v == null)
				seguir = false;
			else {
				listaVehiculos.add(v);
				i++;
			}
		}
		if (listaVehiculos.size() == vehiculos.length)
			return listaVehiculos;
		else
			return null;
	}

}
