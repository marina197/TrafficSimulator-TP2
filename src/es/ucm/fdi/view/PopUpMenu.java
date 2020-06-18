package es.ucm.fdi.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import es.ucm.fdi.constructor.ConstructorEventos;
import es.ucm.fdi.constructor.ParserEventos;

@SuppressWarnings("serial")
public class PopUpMenu extends JPopupMenu {

	public PopUpMenu(VentanaPrincipal mainWindow) {
		
		JMenu plantillas = new JMenu("Nueva Plantilla");
		this.add(plantillas);
		// añadir opciones con sus listeners.
		for (ConstructorEventos ce : ParserEventos.getConstructoresEventos()) {
			JMenuItem mi = new JMenuItem(ce.toString());
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainWindow.inserta(ce.template() + System.lineSeparator());
				}
			});
			plantillas.add(mi);
		}
		
		this.addSeparator();
		JMenuItem cargar = new JMenuItem("Cargar");
		cargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.cargaFichero();
			}
			
		});
		this.add(cargar);
		
		JMenuItem salvar = new JMenuItem("Salvar");
		salvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.salvaEvento();
			}
		});
		this.add(salvar);
		
		JMenuItem limpiar = new JMenuItem("Limpiar");
		limpiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.limpiarEventos();
			}
		});
		this.add(limpiar);
	}

}
