package es.ucm.fdi.view.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.view.VentanaPrincipal;

@SuppressWarnings("serial")
public class BarraMenu extends JMenuBar {

	public BarraMenu(VentanaPrincipal mainWindow, Controlador controlador) {
		super();
		// MANEJO DE FICHEROS
		JMenu menuFicheros = new JMenu("Ficheros");
		this.add(menuFicheros);
		this.creaMenuFicheros(menuFicheros, mainWindow);
		// SIMULADOR
		JMenu menuSimulador = new JMenu("Simulador");
		this.add(menuSimulador);
		this.creaMenuSimulador(menuSimulador, controlador, mainWindow);
		// INFORMES
		JMenu menuReport = new JMenu("Informes");
		this.add(menuReport);
		this.creaMenuInformes(menuReport, mainWindow);
	}

	private void creaMenuFicheros(JMenu menu, VentanaPrincipal mainWindow) {
		JMenuItem cargar = new JMenuItem("Carga Eventos");
		cargar.setMnemonic(KeyEvent.VK_L);
		cargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		cargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.cargaFichero();
			}
		});
		menu.add(cargar);

		JMenuItem salvar = new JMenuItem("Salva Eventos");
		salvar.setMnemonic(KeyEvent.VK_S);
		salvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		salvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salvaEvento();
			}
		});
		menu.add(salvar);
		menu.addSeparator();

		JMenuItem salvarInformes = new JMenuItem("Salva Informe");
		salvarInformes.setMnemonic(KeyEvent.VK_R);
		salvarInformes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		salvarInformes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salvaInforme();
			}
		});
		menu.add(salvarInformes);
		menu.addSeparator();

		JMenuItem salir = new JMenuItem("Salir");
		salir.setMnemonic(KeyEvent.VK_E);
		salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		salir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salir();
			}
		});
		menu.add(salir);
	}

	private void creaMenuSimulador(JMenu menuSimulador, Controlador controlador, VentanaPrincipal mainWindow) {
		JMenuItem ejecuta = new JMenuItem("Ejecuta");
		ejecuta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainWindow.getSteps() > 1) {
					mainWindow.ejecutaHebra();
				} else
					controlador.ejecuta(mainWindow.getSteps());
			}
		});

		JMenuItem reinicia = new JMenuItem("Reinicia");
		reinicia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.reinicia();
			}
		});
		
		JCheckBoxMenuItem redirigirSalida = new JCheckBoxMenuItem("Redirigir Salida");
		redirigirSalida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.redirigirSalida(redirigirSalida.isSelected());
			}
		});
		
		menuSimulador.add(ejecuta);
		menuSimulador.add(reinicia);
		menuSimulador.add(redirigirSalida);
	}

	private void creaMenuInformes(JMenu menuReport, VentanaPrincipal mainWindow) {
		JMenuItem generaInformes = new JMenuItem("Generar");
		generaInformes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.generaInformes();
			}
		});
		menuReport.add(generaInformes);
		
		JMenuItem limpiaAreaInformes = new JMenuItem("Clear");
		limpiaAreaInformes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.borraReports();
			}
		});
		
		menuReport.add(limpiaAreaInformes);
	}
}
