package es.ucm.fdi.control;

import java.util.List;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;

public interface ObservadorSimuladorTrafico {
	// notifica errores
	void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e);

	// notifica el avance de los objetos de simulación
	void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos);

	// notifica que se ha generado un nuevo evento
	void addEvento(int tiempo, MapaCarreteras mapa, List<Evento> eventos);

	// notifica que la simulacion se ha reiniciado
	void reinicia(int tiempo, MapaCarreteras mapa, List<Evento> eventos);

}
