package es.ucm.fdi.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.ucm.fdi.constructor.ParserEventos;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniError;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.SimuladorTrafico;

public class Controlador {
	private SimuladorTrafico simulador;
	private OutputStream ficheroSalida;
	private InputStream ficheroEntrada;
	private int pasosSimulacion;

	public Controlador(SimuladorTrafico sim, int pasosSimulacion, InputStream is, OutputStream os) {
		this.simulador = sim;
		this.pasosSimulacion = pasosSimulacion;
		this.ficheroEntrada = is;
		this.ficheroSalida = os;
	}

	public void ejecuta() throws ErrorDeSimulacion, IOException {
		// lee los eventos y se los manda al simulador
		this.cargaEventos(this.ficheroEntrada);
		this.simulador.ejecuta(pasosSimulacion, this.ficheroSalida);
	}

	public void ejecuta(int pasos) {
		try {
			this.simulador.ejecuta(pasos, this.ficheroSalida);
		} catch (ErrorDeSimulacion | IOException e) {
			ErrorDeSimulacion err = new ErrorDeSimulacion(e.getMessage());
			this.notificaError(err);
		}
	}
	
	public void reinicia() {
		this.simulador.reinicia();
	}

	public void notificaError(ErrorDeSimulacion err) {
		this.simulador.notificaError(err);
	}

	public void cargaEventos(InputStream inStream) throws ErrorDeSimulacion {
		Ini ini;
		try {
			// lee el fichero y carga su atributo iniSections
			ini = new Ini(inStream);
		} catch (IOException | IniError e) {
			throw new ErrorDeSimulacion("Error en la lectura de eventos: " + e.getMessage());
		}
		// recorremos todas los elementos de iniSections para generar el evento
		// correspondiente
		for (IniSection sec : ini.getSections()) {
			try {
				// parseamos la sección para ver a que evento corresponde
				Evento e = ParserEventos.parseaEvento(sec);
				if (e != null)
					this.simulador.insertaEvento(e);
				else {
					throw new ErrorDeSimulacion("Evento desconocido: " + sec.getTag());
				}
			} catch (IllegalArgumentException e) {
				ErrorDeSimulacion err = new ErrorDeSimulacion(e.getMessage());
				this.notificaError(err);
			}
		}
	}

	public void addObserver(ObservadorSimuladorTrafico o) {
		this.simulador.addObservador(o);
	}

	public void removeObserver(ObservadorSimuladorTrafico o) {
		this.simulador.removeObservador(o);
	}

	public OutputStream getFicheroSalida() {
		return ficheroSalida;
	}

	public void setFicheroSalida(OutputStream ficheroSalida) {
		this.ficheroSalida = ficheroSalida;
	}
	
}
