package es.ucm.fdi.model;

public interface Observador<T> {

	public void addObservador(T o);

	public void removeObservador(T o);
}
