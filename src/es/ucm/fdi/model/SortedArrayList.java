package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import es.ucm.fdi.excepciones.ErrorDeSimulacion;

/**
 * <h1>Clase SortedArrayList</h1>
 * 
 * <p>
 * Ordena los eventos del simulador.
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
@SuppressWarnings("serial")
public class SortedArrayList<E> extends ArrayList<E> {

	/**
	 * Comparador
	 */
	private Comparator<E> cmp;

	/**
	 * Constructor de la clase
	 * @param cmp comparador
	 */
	public SortedArrayList(Comparator<E> cmp) {
		this.cmp = cmp;
	}

	@Override
	public boolean add(E e) {// Ordenado por tiempo.
		if (this.isEmpty()) {
			super.add(0, e);
			return true;
		} else {
			int i = 0;
			boolean encontrado = false;
			while (!encontrado && i < this.size()) {
				if (cmp.compare(super.get(i), e) == 1) {
					encontrado = true;
				} else
					i++;
			}
			super.add(i, e);
			return true;
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean add = true;
		for (E e : c) {
			add = this.add(e) && add;
		}
		return add;
	}
	
	@Override
	public void add(int index, E element) {
		try {
			throw new ErrorDeSimulacion("No se puede añadir el evento");
		} catch (ErrorDeSimulacion e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> e) {
		try {
			
			throw new ErrorDeSimulacion("No se pueden añadir los eventos");
		} catch (ErrorDeSimulacion e1) {
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	public E set(int index, E element) {
		try {
			throw new ErrorDeSimulacion("No se puede cambiar el evento");
		} catch (ErrorDeSimulacion e) {
			e.printStackTrace();
		}
		return null;
	}

}
