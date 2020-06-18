package es.ucm.fdi.constructor;

import es.ucm.fdi.eventos.Evento;
import es.ucm.fdi.ini.IniSection;

public abstract class ConstructorEventos {

	// Cada clase dará los valores correspondientes a estos atributos en la
	// constructora.
	protected String etiqueta; // Etiqueta de la entrada ("new_road", etc...)
	protected String[] claves; // Campos de la entrada ("time", "vehicles", etc...)
	protected String[] valoresPorDefecto;

	ConstructorEventos() {
		this.etiqueta = null;
		this.claves = null;
	}

	public abstract Evento parser(IniSection sec) throws IllegalArgumentException;

	protected static String identificadorValido(IniSection seccion, String clave) throws IllegalArgumentException {
		String s = seccion.getValue(clave);
		if (!esIdentificadorValido(s))
			throw new IllegalArgumentException("El valor '" + s + "' no es un ID válido");
		else
			return s;
	}

	// identificadores válidos
	// sólo pueden contener letras, números y subrayados
	private static boolean esIdentificadorValido(String id) {
		return id != null && id.matches("[a-z0-9_]+");
	}

	protected static int parseaInt(IniSection seccion, String clave) throws IllegalArgumentException {
		String v = seccion.getValue(clave);
		if (v == null || v.equals(""))
			throw new IllegalArgumentException("Valor inexistente para la clave: " + clave);
		else
			return Integer.parseInt(seccion.getValue(clave));
	}

	protected static int parseaInt(IniSection seccion, String clave, int valorPorDefecto) {
		String v = seccion.getValue(clave);
		if (v != null && !v.equals("")) {
			return Integer.parseInt(seccion.getValue(clave));
		} else
			return valorPorDefecto;
	}

	protected static int parseaIntNoNegativo(IniSection seccion, String clave, int valorPorDefecto) throws IllegalArgumentException {
		int i = ConstructorEventos.parseaInt(seccion, clave, valorPorDefecto);
		if (i < 0)
			throw new IllegalArgumentException("El valor '" + i + "' para '" + clave + "' no es válido");
		else
			return i;
	}
	
	protected static int parseaIntNoNegativo(IniSection seccion, String clave) throws IllegalArgumentException {
		int i = ConstructorEventos.parseaInt(seccion, clave);
		if (i < 0)
			throw new IllegalArgumentException("El valor '" + i + "' para '" + clave + "' no es válido");
		else
			return i;
	}
	
	protected static double parseaDouble(IniSection seccion, String clave, int valorPorDefecto) {
		String v = seccion.getValue(clave);
		return (v != null) ? Double.parseDouble(seccion.getValue(clave)) : valorPorDefecto;
	}
	
	protected static double parseaDoubleNoNegativo(IniSection seccion, String clave, int valorPorDefecto) throws IllegalArgumentException {
		double i = ConstructorEventos.parseaDouble(seccion, clave, valorPorDefecto);
		if (i < 0)
			throw new IllegalArgumentException("El valor '" + i + "' para '" + clave + "' no es válido");
		else
			return i;
	}

	public String template() {
		String evento = "[";
		evento += etiqueta + "]\n";
		for (int i = 0; i < claves.length; i++) {
			evento += claves[i]+" = " + valoresPorDefecto[i] + "\n";
		}

		return evento;
	}

}
