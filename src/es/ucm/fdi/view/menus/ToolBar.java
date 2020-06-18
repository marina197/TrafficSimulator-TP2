package es.ucm.fdi.view.menus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.control.ObservadorSimuladorTrafico;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.view.Utils;
import es.ucm.fdi.view.VentanaPrincipal;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar implements ObservadorSimuladorTrafico {

	private JSpinner delay;
	private JSpinner steps;
	private JTextField time;

	private List<JComponent> lista = new ArrayList<JComponent>();
	
	public ToolBar(VentanaPrincipal mainWindow, Controlador controlador) {
		super();
		controlador.addObserver(this);

		cargarEventos(mainWindow);
		guardarEventos(mainWindow);
		limpiarEventos(mainWindow);
		this.addSeparator();
		checkIn(mainWindow, controlador);
		ejecuta(mainWindow, controlador);
		stop(mainWindow);
		reinicia(mainWindow, controlador);
		this.addSeparator();

		// Spinner
		this.delay();
		this.spinner();

		// Tiempo
		this.tiempo();

		this.addSeparator();
		// OPCIONAL
		this.generaReports(mainWindow);
		this.borraReports(mainWindow);
		this.guardaReports(mainWindow);
		this.salir(mainWindow);
	}

	private void cargarEventos(VentanaPrincipal mainWindow) {
		JButton botonCargar = new JButton();
		botonCargar.setToolTipText("Carga un fichero de eventos");
		botonCargar.setIcon(new ImageIcon(Utils.loadImage("resources/icons/open.png")));
		botonCargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.cargaFichero();
			}
		});
		this.add(botonCargar);
		this.lista.add(botonCargar);
	}

	private void guardarEventos(VentanaPrincipal mainWindow) {
		JButton botonGuardar = new JButton();
		botonGuardar.setToolTipText("Guarda eventos en un fichero");
		botonGuardar.setIcon(new ImageIcon(Utils.loadImage("resources/icons/save.png")));
		botonGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salvaEvento();
			}
		});
		this.add(botonGuardar);
		this.lista.add(botonGuardar);
	}

	private void limpiarEventos(VentanaPrincipal mainWindow) {
		JButton botonLimpiar = new JButton();
		botonLimpiar.setToolTipText("Limpia la ventana de eventos");
		botonLimpiar.setIcon(new ImageIcon(Utils.loadImage("resources/icons/clear.png")));
		botonLimpiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.limpiarEventos();
			}
		});
		this.add(botonLimpiar);
		this.lista.add(botonLimpiar);
	}

	public void checkIn(VentanaPrincipal mainWindow, Controlador controlador) {
		JButton botonCheckIn = new JButton();
		botonCheckIn.setToolTipText("Carga los eventos al simulador");
		botonCheckIn.setIcon(new ImageIcon(Utils.loadImage("resources/icons/events.png")));
		botonCheckIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controlador.reinicia();
					byte[] contenido = (mainWindow.getTextoEditorEventos()).getBytes();
					controlador.cargaEventos(new ByteArrayInputStream(contenido));
				} catch (ErrorDeSimulacion e1) {
					controlador.notificaError(e1);
				}
				mainWindow.setMensaje("Eventos cargados al simulador!");
			}
		});
		this.add(botonCheckIn);
		this.lista.add(botonCheckIn);
	}

	private void ejecuta(VentanaPrincipal mainWindow, Controlador controlador) {
		JButton botonEjecuta = new JButton();
		botonEjecuta.setToolTipText("Ejecuta el simulador");
		botonEjecuta.setIcon(new ImageIcon(Utils.loadImage("resources/icons/play.png")));
		botonEjecuta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				if (mainWindow.getSteps() > 1) {
					mainWindow.ejecutaHebra();
				}
				else
					controlador.ejecuta(mainWindow.getSteps());
			}
		});
		
		this.add(botonEjecuta);
		this.lista.add(botonEjecuta);
	}

	private void stop(VentanaPrincipal mainWindow) {
		JButton botonStop = new JButton();
		botonStop.setToolTipText("Cancela la simulación");
		botonStop.setIcon(new ImageIcon(Utils.loadImage("resources/icons/stop.png")));
		botonStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainWindow.getT() != null) {
					mainWindow.getT().interrupt();
					mainWindow.activa();
				}
				
			}
		});
		this.add(botonStop);
	}

	private void reinicia(VentanaPrincipal mainWindow, Controlador controlador) {
		JButton botonReinicia = new JButton();
		botonReinicia.setToolTipText("Reinicia el simulador");
		botonReinicia.setIcon(new ImageIcon(Utils.loadImage("resources/icons/reset.png")));
		botonReinicia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.reinicia();
			}
		});
		this.add(botonReinicia);
		this.lista.add(botonReinicia);
	}

	private void delay() {
		this.add(new JLabel(" Delay: "));
		this.delay = new JSpinner(new SpinnerNumberModel(5, 0, 10000, 1));
		this.delay.setToolTipText("delay: 0-10000");
		this.delay.setMaximumSize(new Dimension(70, 70));
		this.delay.setMinimumSize(new Dimension(70, 70));
		this.delay.setValue(0);
		this.add(delay);
		this.lista.add(delay);
	}

	private void spinner() {
		this.add(new JLabel(" Pasos: "));
		this.steps = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
		this.steps.setToolTipText("pasos a ejecutar: 1-1000");
		this.steps.setMaximumSize(new Dimension(70, 70));
		this.steps.setMinimumSize(new Dimension(70, 70));
		this.steps.setValue(1);
		this.add(steps);
		this.lista.add(steps);
	}

	private void tiempo() {
		this.add(new JLabel(" Tiempo: "));
		this.time = new JTextField("0", 5);
		this.time.setToolTipText("Tiempo actual");
		this.time.setMaximumSize(new Dimension(70, 70));
		this.time.setMinimumSize(new Dimension(70, 70));
		this.time.setEditable(false);
		this.add(this.time);
	}

	private void generaReports(VentanaPrincipal mainWindow) {
		// OPCIONAL
		JButton botonGeneraReports = new JButton();
		botonGeneraReports.setToolTipText("Genera informes");
		botonGeneraReports.setIcon(new ImageIcon(Utils.loadImage("resources/icons/report.png")));
		botonGeneraReports.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.generaInformes();
			}
		});
		this.add(botonGeneraReports);
		this.lista.add(botonGeneraReports);
	}

	private void borraReports(VentanaPrincipal mainWindow) {
		JButton botonBorraReports = new JButton();
		botonBorraReports.setToolTipText("Borra informes");
		botonBorraReports.setIcon(new ImageIcon(Utils.loadImage("resources/icons/delete_report.png")));
		botonBorraReports.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.borraReports();
			}
		});
		this.add(botonBorraReports);
		this.lista.add(botonBorraReports);
	}

	private void guardaReports(VentanaPrincipal mainWindow) {
		JButton botonGuardaReports = new JButton();
		botonGuardaReports.setToolTipText("Guarda informes");
		botonGuardaReports.setIcon(new ImageIcon(Utils.loadImage("resources/icons/save_report.png")));
		botonGuardaReports.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salvaInforme();
			}
		});
		this.add(botonGuardaReports);
		this.lista.add(botonGuardaReports);
	}

	private void salir(VentanaPrincipal mainWindow) {
		JButton botonSalir = new JButton();
		botonSalir.setToolTipText("Cierra la aplicación");
		botonSalir.setIcon(new ImageIcon(Utils.loadImage("resources/icons/exit.png")));
		botonSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salir();
			}
		});
		this.add(botonSalir);
		this.lista.add(botonSalir);
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.time.setText("" + tiempo);
	}

	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.time.setText("" + tiempo);
	}

	@Override
	public void addEvento(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.delay.setValue(0);
		this.steps.setValue(1);
		this.time.setText("0");
	}

	@Override
	public void reinicia(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.delay.setValue(0);
		this.steps.setValue(1);
		this.time.setText("0");
	}

	public int getPasos() {
		return (int) this.steps.getValue();
	}

	public String getTime() {
		return time.getText();
	}
	
	public int getDelay() {
		return (int) this.delay.getValue();
	}
	
	public List<JComponent> getLista() {
		return lista;
	}
}
