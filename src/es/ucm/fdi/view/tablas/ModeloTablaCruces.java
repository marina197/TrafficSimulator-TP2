package es.ucm.fdi.view.tablas;

import java.util.List;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.cruces.CruceGenerico;

@SuppressWarnings("serial")
public class ModeloTablaCruces extends ModeloTabla<CruceGenerico<?>> {

	public ModeloTablaCruces(String[] columnIdEventos, Controlador ctrl) {
		super(columnIdEventos, ctrl);
	}

	@Override
	// necesario para que se visualicen los datos
	public Object getValueAt(int indiceFil, int indiceCol) {
		// Poner el tiempo que le queda para cambiar de verde a rojo
		// TODO
		
		Object s = null;
		String verde = "[";
		String rojo = "[";

		verde += this.lista.get(indiceFil).verde();
		rojo += this.lista.get(indiceFil).rojo();

		switch (indiceCol) {
		case 0:
			s = this.lista.get(indiceFil).getId();
			break;
		case 1:
			s = verde + "]";
			break;
		case 2:
			s = rojo + "]";
			break;
		default:
			assert (false);
		}
		return s;
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.lista = mapa.getCruces();
		this.fireTableStructureChanged();
	}

	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.lista = mapa.getCruces();
		this.fireTableStructureChanged();
	}

}
