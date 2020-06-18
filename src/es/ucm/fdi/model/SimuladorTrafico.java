package es.ucm.fdi.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ucm.fdi.control.ObservadorSimuladorTrafico;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;

/**
 * <h1>Clase SimuladorTrafico</h1>
 *
 * <p>
 * Controla y hace funcionar el simulador de tráfico.
 * </p>
 *
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class SimuladorTrafico implements Observador<ObservadorSimuladorTrafico> {
    /**
     * mapa de las carreteras del simulador
     */
    private MapaCarreteras mapa;
    /**
     * lista de los eventos del simulador.
     */
    private List<Evento> eventos;
    /**
     * contador de la simulación
     */
    private int contadorTiempo;

    private List<ObservadorSimuladorTrafico> observadores;
    private Comparator<Evento> cmp;

    /**
     * Constructor de la clase
     */
    public SimuladorTrafico() {
        this.mapa = new MapaCarreteras();
        this.contadorTiempo = 0;
        cmp = new Comparator<Evento>() {
            @Override
            public int compare(Evento o1, Evento o2) {
                if (o1.getTiempo() > o2.getTiempo())
                    return 1;
                else if (o1.getTiempo() < o2.getTiempo())
                    return -1;
                else
                    return 0;
            }
        };
        this.eventos = new SortedArrayList<>(cmp); // estructura ordenada por “tiempo”
        this.observadores = new ArrayList<>();
    }
    
	/**
	 * Ejecuta el simulador
	 * 
	 * @param pasosSimulacion número de pasos de la simulación
	 * @param ficheroSalida fichero de salida en el que escribir el informe
	 * @throws ErrorDeSimulacion No se ha ejecutado correctamente el paso de la simulación.
	 * @throws IOException No se ha podido escribir correctamente el informe de la simulación.
	 */
	public void ejecuta(int pasosSimulacion, OutputStream ficheroSalida) throws ErrorDeSimulacion, IOException {
		String report = "";
		int limiteTiempo = this.contadorTiempo + pasosSimulacion - 1;
		List<Evento> aux = new ArrayList<Evento>();
		while (this.contadorTiempo <= limiteTiempo) {
			for (Evento e : eventos) {
				if (e.getTiempo() == this.contadorTiempo)
					aux.add(e);
			}
			for (Evento elem : aux) {
				if (elem.getTiempo() == this.contadorTiempo) {
					elem.ejecuta(mapa);
					eventos.remove(elem);
				}
			}
			mapa.actualizar();
			this.contadorTiempo++;
			report += mapa.generateReport(contadorTiempo);
			for (ObservadorSimuladorTrafico obs : observadores) {
				obs.avanza(contadorTiempo, mapa, eventos);
			}
			if (ficheroSalida != null)
				ficheroSalida.write(report.getBytes());
		}
	}

    /**
     * inserta un evento en “eventos”, controlando que el tiempo de
     * ejecución del evento sea menor que “contadorTiempo”
     *
     * @param e evento a insertar
     * @throws ErrorDeSimulacion
     */
    public void insertaEvento(Evento e) throws ErrorDeSimulacion {
        if (e != null) {
            if (e.getTiempo() < this.contadorTiempo) {
                ErrorDeSimulacion err = new ErrorDeSimulacion("Error al insertar eventos");
                this.notificaError(err);
                throw err;
            } else {
                this.eventos.add(e);
                this.notificaNuevoEvento(); // Se notifica a los observadores
            }
        } else {
            ErrorDeSimulacion err = new ErrorDeSimulacion("Error al insertar eventos");
            this.notificaError(err);// Se notifica a los observadores
            throw err;
        }
    }

    public void notificaError(ErrorDeSimulacion err) {
        for (ObservadorSimuladorTrafico o : this.observadores) {
            o.errorSimulador(this.contadorTiempo, this.mapa, this.eventos, err);
        }
    }

    private void notificaNuevoEvento() {
        for (ObservadorSimuladorTrafico o : this.observadores) {
            o.addEvento(this.contadorTiempo, this.mapa, this.eventos);
        }
    }

    @Override
    public void addObservador(ObservadorSimuladorTrafico o) {
        if (o != null && !this.observadores.contains(o)) {
            this.observadores.add(o);
        }
    }

    @Override
    public void removeObservador(ObservadorSimuladorTrafico o) {
        if (o != null && !this.observadores.contains(o)) {
            this.observadores.remove(o);
        }
    }

    public void reinicia() {
        // Reinicia todos sus atributos y notifica a los observadores dicha acción.
        this.mapa.reinicia();
        this.contadorTiempo = 0;
        this.eventos.clear();
        for (ObservadorSimuladorTrafico obs : observadores) {
            obs.reinicia(contadorTiempo, mapa, eventos);
        }
    }

}
