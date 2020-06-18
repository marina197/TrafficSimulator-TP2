package es.ucm.fdi.view.tablas;

import java.util.List;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.vehiculos.Vehiculo;

@SuppressWarnings("serial")
public class ModeloTablaCarreteras extends ModeloTabla<Carretera> {

	public ModeloTablaCarreteras(String[] columnIdEventos, Controlador ctrl) {
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
			s = this.lista.get(indiceFil).getCruceOrigen().getId();
			break;
		case 2:
			s = this.lista.get(indiceFil).getCruceDestino().getId();
			break;
		case 3:
			s = this.lista.get(indiceFil).getLongitud();
			break;
		case 4:
			s = this.lista.get(indiceFil).getVelocidadMaxima();
			break;
		case 5:
			String value = "[";
			boolean coma = false;
			for (Vehiculo v : this.lista.get(indiceFil).getVehiculos()) {
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
		this.lista = mapa.getCarreteras();
		this.fireTableStructureChanged();
	}
	
	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.lista = mapa.getCarreteras();
		this.fireTableStructureChanged();
	}

}
