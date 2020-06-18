package es.ucm.fdi.model.carreteras;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.cruces.CruceGenerico;

/**
 * <h1>Clase Autopista</h1>
 * 
 * <p>
 * Realiza las operaciones de las autopistas
 * </p>
 * 
 * @author Marina Lozano Lahuerta
 * @author Pablo López Veleiro
 */
public class Autopista extends Carretera{

	/**
	 * Número de carriles
	 */
	protected int numCarriles;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id identificador de la autopista
	 * @param length longitud de la autopista
	 * @param maxSpeed máxima velocidad permitida
	 * @param src cruce de origen
	 * @param dest cruce de destino
	 * @param numCarriles número de carriles
	 */
	public Autopista(String id, int length, int maxSpeed, CruceGenerico<?> src, CruceGenerico<?> dest, int numCarriles) {
		super(id, length, maxSpeed, src, dest);
		this.numCarriles = numCarriles;
	}

	@Override
	protected int calculaVelocidadBase() {
		return Math.min(this.velocidadMaxima,
				((this.velocidadMaxima * this.numCarriles) / Math.max(this.vehiculos.size(), 1)) + 1);
	}

	@Override
	protected int calculaFactorReduccion(int obstaculos) {
		return obstaculos < this.numCarriles ? 1 : 2;
	}

	@Override
	protected void completaDetallesSeccion(IniSection is) {
		super.completaDetallesSeccion(is);
		is.setValue("type", "lanes");
	}
}
