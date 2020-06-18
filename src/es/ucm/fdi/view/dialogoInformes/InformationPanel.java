package es.ucm.fdi.view.dialogoInformes;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel{
	private JLabel informacion;
	
	public InformationPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.informacion = new JLabel("Selecciona objetos para los que quieras generar el informe.");
		this.add(this.informacion);
		
		this.informacion = new JLabel("Usa '" + DialogoInformes.TECLALIMPIAR + "' para deseleccionar todo.");
		this.add(this.informacion);
		this.informacion = new JLabel("Usa Ctrl+A para seleccionar todo.");
		this.add(this.informacion);
		this.informacion=new JLabel("\n");
		this.add(this.informacion);
	}

}
