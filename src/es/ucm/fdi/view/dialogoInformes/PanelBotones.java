package es.ucm.fdi.view.dialogoInformes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelBotones extends JPanel{

	public PanelBotones(DialogoInformes dialogoInformes) {
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogoInformes.setVisible(false);
			}
		});
		this.add(cancelar);
		
		JButton generar = new JButton("Generar");
		generar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogoInformes.generaInforme();
				dialogoInformes.setVisible(false);
			}
		});
		this.add(generar);
	}

	
}
