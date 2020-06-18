package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.control.ObservadorSimuladorTrafico;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.main.Main;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;
import es.ucm.fdi.view.areaTexto.PanelAreaTexto;
import es.ucm.fdi.view.areaTexto.PanelEditorEventos;
import es.ucm.fdi.view.areaTexto.PanelInformes;
import es.ucm.fdi.view.dialogoInformes.DialogoInformes;
import es.ucm.fdi.view.menus.BarraMenu;
import es.ucm.fdi.view.menus.ToolBar;
import es.ucm.fdi.view.swing.grafico.ComponenteMapa;
import es.ucm.fdi.view.tablas.ModeloTablaCarreteras;
import es.ucm.fdi.view.tablas.ModeloTablaCruces;
import es.ucm.fdi.view.tablas.ModeloTablaEventos;
import es.ucm.fdi.view.tablas.ModeloTablaVehiculos;
import es.ucm.fdi.view.tablas.PanelTabla;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame implements
		ObservadorSimuladorTrafico {

	private Controlador controlador;
	public static Border bordePorDefecto = BorderFactory.createLineBorder(
			Color.black, 2);
	// SUPERIOR PANEL
	static private final String[] columnIdEventos = { "#", "Tiempo", "Tipo" };
	private PanelAreaTexto panelEditorEventos;
	private PanelAreaTexto panelInformes;
	private PanelTabla<Evento> panelColaEventos;
	// MENU AND TOOL BAR
	private JFileChooser fc;
	private ToolBar toolbar;
	// GRAPHIC PANEL
	private ComponenteMapa componenteMapa;
	// STATUS BAR (INFO AT THE BOTTOM OF THE WINDOW)
	private PanelBarraEstado panelBarraEstado;

	// INFERIOR PANEL
	static private final String[] columnIdVehiculo = { "ID", "Carretera",
			"Localizacion", "Vel.", "Km", "Tiempo. Ave.", "Itinerario" };
	static private final String[] columnIdCarretera = { "ID", "Origen",
			"Destino", "Longitud", "Vel. Max", "Vehiculos" };
	static private final String[] columnIdCruce = { "ID", "Verde", "Rojo" };
	private PanelTabla<Vehiculo> panelVehiculos;
	private PanelTabla<Carretera> panelCarreteras;
	private PanelTabla<CruceGenerico<?>> panelCruces;
	// REPORT DIALOG
	private DialogoInformes dialogoInformes; // opcional
	// MODEL PART - VIEW CONTROLLER MODEL
	private File ficheroActual;

	private Thread t = null;
	
	public VentanaPrincipal(String ficheroEntrada, Controlador ctrl) {
		super("Simulador de Tráfico");
		this.controlador = ctrl;
		this.ficheroActual = ficheroEntrada != null ? new File(ficheroEntrada) : null;
		this.initGUI();

		try {
			if (this.ficheroActual != null) {
				String s = leeFichero(this.ficheroActual);
				this.panelEditorEventos.setTexto(s);
				this.panelEditorEventos.setBorde(this.ficheroActual.getName());
			}
		} catch (FileNotFoundException e) {
			this.muestraDialogoError("Error durante la lectura del fichero: " + e.getMessage());
		}
		// añadimos la ventana principal como observadora
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			// al salir pide confirmación
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}
		});

		JPanel panelPrincipal = this.creaPanelPrincipal();
		this.setContentPane(panelPrincipal);
		// BARRA DE ESTADO INFERIOR
		// (contiene una JLabel para mostrar el estado del simulador)
		this.addBarraEstado(panelPrincipal);

		// PANEL QUE CONTIENE EL RESTO DE COMPONENTES
		// (Lo dividimos en dos paneles (superior e inferior)
		JPanel panelCentral = this.createPanelCentral();
		panelPrincipal.add(panelCentral, BorderLayout.CENTER);

		// PANEL SUPERIOR
		this.createPanelSuperior(panelCentral);
		// MENU
		BarraMenu menubar = new BarraMenu(this, this.controlador);
		this.setJMenuBar(menubar);
		// PANEL INFERIOR
		this.createPanelInferior(panelCentral);
		// BARRA DE HERRAMIENTAS
		this.addToolBar(panelPrincipal);

		// FILE CHOOSER
		this.fc = new JFileChooser();
		// REPORT DIALOG (OPCIONAL)
		this.dialogoInformes = new DialogoInformes(this, this.controlador);
		this.pack();
		this.setVisible(true);
	}

	private void addToolBar(JPanel panelPrincipal) {
		this.toolbar = new ToolBar(this, this.controlador);
		panelPrincipal.add(this.toolbar, BorderLayout.PAGE_START);
	}

	private void createPanelInferior(JPanel panelCentral) {
		JPanel panelInferior = new JPanel();
		JPanel panelTablas = new JPanel(new BorderLayout());
		JPanel panelMapa = new JPanel(new BorderLayout());

		panelInferior.setLayout(new GridLayout(1, 2));
		// Gridlayout permite configurar tamaño porcentaje
		panelTablas.setLayout(new GridLayout(3, 1));
		panelInferior.add(panelTablas);
		panelInferior.add(panelMapa);
		panelCentral.add(panelInferior);

		this.panelVehiculos = new PanelTabla<Vehiculo>("Vehiculos",
				new ModeloTablaVehiculos(VentanaPrincipal.columnIdVehiculo,
						this.controlador));
		this.panelCarreteras = new PanelTabla<Carretera>("Carretera",
				new ModeloTablaCarreteras(VentanaPrincipal.columnIdCarretera,
						this.controlador));
		this.panelCruces = new PanelTabla<CruceGenerico<?>>("Cruces",
				new ModeloTablaCruces(VentanaPrincipal.columnIdCruce,
						this.controlador));

		panelTablas.add(panelVehiculos);
		panelTablas.add(panelCarreteras);
		panelTablas.add(panelCruces);

		this.componenteMapa = new ComponenteMapa(this.controlador);
		// Añadir un ScroolPane al panel inferior donde se coloca la componente.
		panelMapa.add(new JScrollPane(this.componenteMapa));
	}

	private void createPanelSuperior(JPanel panelCentral) {
		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
		panelCentral.add(panelSuperior);
		this.panelEditorEventos = new PanelEditorEventos("Eventos", "", true,
				this);
		this.panelColaEventos = new PanelTabla<Evento>("Cola Eventos",
				new ModeloTablaEventos(VentanaPrincipal.columnIdEventos,
						this.controlador));
		this.panelInformes = new PanelInformes("Informes", false,
				this.controlador);

		panelSuperior.add(this.panelEditorEventos);
		panelSuperior.add(this.panelColaEventos);
		panelSuperior.add(this.panelInformes);
	}

	private void addBarraEstado(JPanel panelPrincipal) {
		this.panelBarraEstado = new PanelBarraEstado(
				"¡Bienvenido al simulador!", this.controlador);
		panelPrincipal.add(this.panelBarraEstado, BorderLayout.PAGE_END);
	}

	private JPanel creaPanelPrincipal() {
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		return panelPrincipal;
	}

	private JPanel createPanelCentral() {
		JPanel panelCentral = new JPanel();
		// para colocar el panel superior e inferior
		panelCentral.setLayout(new GridLayout(2, 1));
		return panelCentral;
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.muestraDialogoError(e.getMessage());
	}

	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {}

	@Override
	public void addEvento(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {}

	@Override
	public void reinicia(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {}

	public void cargaFichero() {
		int returnVal = this.fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichero = this.fc.getSelectedFile();
			try {
				String s = leeFichero(fichero);
				this.controlador.reinicia();
				this.ficheroActual = fichero;
				this.panelEditorEventos.setTexto(s);
				this.panelEditorEventos.setBorde(this.ficheroActual.getName());
				this.panelBarraEstado.setMensaje("Fichero " + fichero.getName()
						+ " de eventos cargado en el editor");
			} catch (FileNotFoundException e) {
				this.muestraDialogoError("Error durante la lectura del fichero: "
						+ e.getMessage());
			}
		}
	}

	private String leeFichero(File fichero) throws FileNotFoundException {
		String a = "", s = "";
		try {
			FileReader f = new FileReader(fichero);
			BufferedReader br = new BufferedReader(f);
			while ((a = br.readLine()) != null) {
				s += a;
				s += "\n";
			}
			br.close();
		} catch (IOException e) {
			throw new FileNotFoundException(fichero.getName());
		}
		return s;
	}

	public void muestraDialogoError(String error) {
		JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void generaInformes() {
		this.dialogoInformes.setLocation(this.dialogoInformes.getParent()
				.getLocation().x + 150, this.dialogoInformes.getParent()
				.getLocation().y + 100);
		this.dialogoInformes.pack();
		this.dialogoInformes.mostrar();
	}

	public String getTextoEditorEventos() {
		return this.panelEditorEventos.getAreatexto().getText();
	}

	public int getSteps() {
		return this.toolbar.getPasos();
	}

	public void inserta(String string) {
		this.panelEditorEventos.inserta(string);
	}

	public void salvaEvento() {
		int returnVal = this.fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichero = this.fc.getSelectedFile();
			try {
				this.escribeFichero(fichero, this.getTextoEditorEventos());
				this.ficheroActual = fichero;
				this.panelBarraEstado
						.setMensaje("Evento guardado en el fichero " + fichero.getName());
			} catch (IOException e) {
				this.muestraDialogoError("Error durante la escritura del fichero: "
						+ e.getMessage());
			}
		}
	}

	private void escribeFichero(File fichero, String content) throws IOException {
		try {
			FileWriter f = new FileWriter(fichero);
			BufferedWriter bw = new BufferedWriter(f);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			throw new IOException(fichero.getName());
		}
	}

	public void salir() {
		int salir = JOptionPane.showOptionDialog(this,
				"¿Seguro que quieres salir?", "Salir de la aplicación",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (salir == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void setMensaje(String string) {
		this.panelBarraEstado.setMensaje(string);
	}

	public void salvaInforme() {
		String s = this.panelInformes.getTexto();
		int returnVal = this.fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichero = this.fc.getSelectedFile();
			this.ficheroActual = fichero;
			try {
				this.escribeFichero(fichero, s);
			} catch (IOException e) {
				this.muestraDialogoError("Error durante la escritura del fichero: "
						+ e.getMessage());
			}

			this.panelBarraEstado.setMensaje("Informe guardado en el fichero "
					+ fichero.getName());
		}
	}

	public void limpiarEventos() {
		this.panelEditorEventos.limpiar();
		this.panelEditorEventos.setBorde("Eventos");
	}

	public void borraReports() {
		this.panelInformes.limpiar();
	}

	public void insertarReports(String texto) {
		this.panelInformes.inserta(texto);
	}

	public int getTime() {
		return Integer.parseInt(this.toolbar.getTime());
	}
	
	public int getDelay() {
		return this.toolbar.getDelay();
	}
	
	public void redirigirSalida(boolean redirige) {
		//CAMBIAR panelInforme extiende OutPutStream
		//TODO
		if (redirige) {
			if (Main.getFicheroSalida() == null)
				this.controlador.setFicheroSalida(System.out);
			else {
				try {
					this.controlador.setFicheroSalida(new FileOutputStream(new File(Main.getFicheroSalida())));
				} catch (FileNotFoundException e) {
					this.controlador.setFicheroSalida(System.out);
				}
			}
		} else {
			this.controlador.setFicheroSalida(null);
		}
	}
	
	public void ejecutaHebra() {
		if (t == null) {
			t = new Thread() {
				public void run() {
					int i = 0;
					while (i < getSteps() && !Thread.interrupted()) {
						desactiva();
						controlador.ejecuta(1);
						try {
							Thread.sleep(getDelay());
						} catch (InterruptedException e) {
							t.interrupt();
						}
						i++;
						if (i == getSteps())
							activa();
					}
					t = null;
				}
			};
			t.start();
		}
	}
	
	// Desactiva botones
	public void desactiva() {
		for (JComponent c : this.toolbar.getLista())
			c.setEnabled(false);
	}

	//Activa botones
	public void activa() {
		for (JComponent c : this.toolbar.getLista())
			c.setEnabled(true);
	}

	public Thread getT() {
		return t;
	}
}
