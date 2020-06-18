package es.ucm.fdi.view.dialogoInformes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.control.ObservadorSimuladorTrafico;
import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.MapaCarreteras;
import es.ucm.fdi.model.carreteras.Carretera;
import es.ucm.fdi.model.cruces.CruceGenerico;
import es.ucm.fdi.model.vehiculos.Vehiculo;
import es.ucm.fdi.view.VentanaPrincipal;

@SuppressWarnings("serial")
public class DialogoInformes extends JDialog implements ObservadorSimuladorTrafico {
	
	protected static final char TECLALIMPIAR = 'c';
	
	private PanelBotones panelBotones;
	private PanelObjSim<Vehiculo> panelVehiculos;
	private PanelObjSim<Carretera> panelCarreteras;
	private PanelObjSim<CruceGenerico<?>> panelCruces;
	private VentanaPrincipal mainWindow;
	
	public DialogoInformes(VentanaPrincipal ventanaPrincipal, Controlador controlador) {
		super(ventanaPrincipal);
		this.mainWindow = ventanaPrincipal;
		controlador.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Genera Informes");
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		this.add(panelPrincipal);
		
		this.panelVehiculos = new PanelObjSim<Vehiculo>("Vehículos");
		this.panelCarreteras = new PanelObjSim<Carretera>("Carreteras");
		this.panelCruces = new PanelObjSim<CruceGenerico<?>>("Cruces");
		this.panelBotones = new PanelBotones(this);
		
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridLayout(1,3));
		panelCentral.add(this.panelCruces);
		panelCentral.add(this.panelCarreteras);
		panelCentral.add(this.panelVehiculos);
		
		panelPrincipal.add(panelCentral);
		
		panelPrincipal.add(this.panelBotones, BorderLayout.PAGE_END);
		
		InformationPanel panelInfo = new InformationPanel();
		panelPrincipal.add(panelInfo, BorderLayout.PAGE_START);
		
		this.setContentPane(panelPrincipal);
		this.pack();
	}

	public void mostrar() {
		this.setVisible(true);
	}

	public void generaInforme() {
		mainWindow.borraReports();
		for (CruceGenerico<?> c : this.getCrucesSeleccionados()) {
			mainWindow.insertarReports(c.generaInforme(mainWindow.getTime()));
			mainWindow.insertarReports("\n");
		}
		for (Carretera c : this.getCarreterasSeleccionadas()) {
			mainWindow.insertarReports(c.generaInforme(mainWindow.getTime()));
			mainWindow.insertarReports("\n");
		}
		for (Vehiculo v : this.getVehiculosSeleccionados()) {
			mainWindow.insertarReports(v.generaInforme(mainWindow.getTime()));
			mainWindow.insertarReports("\n");
		}
	}
	
	private void setMapa(MapaCarreteras mapa) {
		this.panelVehiculos.setList(mapa.getVehiculos());
		this.panelCarreteras.setList(mapa.getCarreteras());
		this.panelCruces.setList(mapa.getCruces());
	}

	public List<Vehiculo> getVehiculosSeleccionados() {
		return this.panelVehiculos.getSelectedItems();
	}

	public List<Carretera> getCarreterasSeleccionadas() {
		return this.panelCarreteras.getSelectedItems();
	}

	public List<CruceGenerico<?>> getCrucesSeleccionados() {
		return this.panelCruces.getSelectedItems();
	}

	@Override
	public void avanza(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.setMapa(mapa);
	}

	@Override
	public void addEvento(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.setMapa(mapa);
	}

	@Override
	public void reinicia(int tiempo, MapaCarreteras mapa, List<Evento> eventos) {
		this.setMapa(mapa);
	}

	@Override
	public void errorSimulador(int tiempo, MapaCarreteras mapa, List<Evento> eventos, ErrorDeSimulacion e) {
		this.setMapa(mapa);
	}

}
