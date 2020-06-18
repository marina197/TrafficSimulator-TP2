package es.ucm.fdi.view.dialogoInformes;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.view.VentanaPrincipal;

@SuppressWarnings("serial")
public class PanelObjSim<T> extends JPanel {

	private ListModel<T> listModel;
	private JList<T> objList;

	public PanelObjSim(String titulo) {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(VentanaPrincipal.bordePorDefecto, titulo, TitledBorder.LEFT,
				TitledBorder.TOP));
		
		this.listModel = new ListModel<T>();
		this.objList = new JList<T>(this.listModel);
		
		addCleanSelectionListner(objList);
		this.add(new JScrollPane(objList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}

	private void addCleanSelectionListner(JList<?> list) {
		list.addKeyListener(new KeyListener() {
			// limpiar la seleccion de items pulsando “c”
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == DialogoInformes.TECLALIMPIAR)
					list.clearSelection();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});
	}

	public List<T> getSelectedItems() {
		List<T> l = new ArrayList<>();
		for (int i : this.objList.getSelectedIndices()) {
			l.add(listModel.getElementAt(i));
		}
		return l;
	}

	public void setList(List<T> lista) {
		this.listModel.setList(lista);
	}
}
