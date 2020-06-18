package es.ucm.fdi.view.tablas;

import java.util.List;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;

@SuppressWarnings("serial")
public class ModeloTablaVehiculos extends ModeloTabla<Vehiculo> {

	public ModeloTablaVehiculos(String[] columnIdEventos, Controlador ctrl) {
		super(columnIdEventos, ctrl);
	}

	@Override
	public Object getValueAt(int indiceFil, int indiceCol) {
		Object s = null;
		switch (indiceCol) {
		case 0:
			s = this.lista.get(indiceFil).getId();
			break;
		case 1:
			//TODO cuando termine que muestre la última carretera.
			if (this.lista.get(indiceFil).getCarretera() != null) 
				s = this.lista.get(indiceFil).getCarretera().getId();
			break;
		case 2:
			if (this.lista.get(indiceFil).getArrived())
				s = "Arrived";
			else
				s = this.lista.get(indiceFil).getLocalizacion();
			break;
		case 3:
			s = this.lista.get(indiceFil).getVelocidadActual();
			break;
		case 4:
			s = this.lista.get(indiceFil).getKilometraje();
			break;
		case 5:
			s = this.lista.get(indiceFil).getTiempoDeAveria();
			break;
		case 6:
			String value = "[";
			boolean coma = false;
			for (CruceGenerico<?> v : this.lista.get(indiceFil).getItinerario()) {
				if (!coma)
					coma = true;
				else
					value += ",";
				value += v.getId();
			}
			s = value + "]";
			break;
		default:
			assert (false);
		}
		return s;
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.lista = mapa.getVehiculos();
		this.fireTableStructureChanged();
	}
	
	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.lista = mapa.getVehiculos();
		this.fireTableStructureChanged();
	}

}
