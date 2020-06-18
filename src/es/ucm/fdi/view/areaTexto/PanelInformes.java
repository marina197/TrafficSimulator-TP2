package es.ucm.fdi.view.areaTexto;

import java.util.List;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.control.ObservadorSimuladorTrafico;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;

@SuppressWarnings("serial")
public class PanelInformes extends PanelAreaTexto implements ObservadorSimuladorTrafico {
	Controlador controlador;

	public PanelInformes(String titulo, boolean editable, Controlador ctrl) {
		super(titulo, editable);
		controlador = ctrl;
		ctrl.addObserver(this); // Se añade como observador
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.setTexto("");
	}

	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		//TODO
		if (controlador.getFicheroSalida() == null)
			this.setTexto(mapa.generateReport(tiempo));
	}

	@Override
	public void addEvento(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {}

	@Override
	public void reinicia(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.setTexto("");
	}

}
