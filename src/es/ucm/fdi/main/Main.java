package es.ucm.fdi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.control.Controlador;
import es.ucm.fdi.excepciones.ErrorDeSimulacion;
import es.ucm.fdi.model.SimuladorTrafico;
import es.ucm.fdi.view.VentanaPrincipal;

public class Main {

	private final static Integer limiteTiempoPorDefecto = 10;
	private static Integer limiteTiempo = null;
	private static String ficheroEntrada = null;
	private static String ficheroSalida = null;

	private static ModoEjecucion modo = ModoEjecucion.BATCH;
	
	public enum ModoEjecucion {
		BATCH("batch"), GUI("gui");

		private String descModo;

		private ModoEjecucion(String modeDesc) {
			descModo = modeDesc;
		}

		public static ModoEjecucion parse(String param) {
			for (ModoEjecucion modo : ModoEjecucion.values()) {
				if (modo.descModo.equals(param))
					return modo;
			}
			return null;
		}
	}

	private static void parseaOpcionModo(CommandLine linea) throws ParseException {
		String mode = linea.getOptionValue("m");
		if(mode !=null) {
			modo = Main.ModoEjecucion.parse(mode);
			if (modo == null)
				throw new ParseException("Modo no válido");
		}
	}

	private static void ParseaArgumentos(String[] args) {

		// define the valid command line options
		Options opcionesLineaComandos = Main.construyeOpciones();

		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine linea = parser.parse(opcionesLineaComandos, args);
			parseaOpcionHELP(linea, opcionesLineaComandos);
			parseaOpcionModo(linea);
			parseaOpcionFicheroIN(linea);
			parseaOpcionFicheroOUT(linea);
			parseaOpcionSTEPS(linea);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!

			String[] resto = linea.getArgs();
			if (resto.length > 0) {
				String error = "Illegal arguments:";
				for (String o : resto)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	private static Options construyeOpciones() {
		Options opcionesLineacomandos = new Options();

		opcionesLineacomandos.addOption(Option.builder("h").longOpt("help").desc("Muestra la ayuda.").build());
		opcionesLineacomandos.addOption(
				Option.builder("i").longOpt("input").hasArg().desc("Fichero de entrada de eventos.").build());
		opcionesLineacomandos.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Fichero de salida, donde se escriben los informes.").build());
		opcionesLineacomandos.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Pasos que ejecuta el simulador en su bucle principal (el valor por defecto es "
						+ Main.limiteTiempoPorDefecto + ").")
				.build());
		opcionesLineacomandos.addOption(Option.builder("m").longOpt("mode").hasArg()
				.desc("'batch' para el modo batch y 'gui' para modo GUI (por defecto el modo es 'batch').").build());
		
		return opcionesLineacomandos;
	}

	private static void parseaOpcionHELP(CommandLine linea, Options opcionesLineaComandos) {
		if (linea.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), opcionesLineaComandos, true);
			System.exit(0);
		}
	}

	private static void parseaOpcionFicheroIN(CommandLine linea) throws ParseException {
		Main.ficheroEntrada = linea.getOptionValue("i");
		if (Main.modo.equals(Main.ModoEjecucion.BATCH)) {
			if (Main.ficheroEntrada == null) {
				throw new ParseException("El fichero de eventos no existe");
			}
		}
	}

	private static void parseaOpcionFicheroOUT(CommandLine linea) throws ParseException {
		Main.ficheroSalida = linea.getOptionValue("o");
	}

	private static void parseaOpcionSTEPS(CommandLine linea) throws ParseException {
		String t = linea.getOptionValue("t", Main.limiteTiempoPorDefecto.toString());
		try {
			Main.limiteTiempo = Integer.parseInt(t);
			assert (Main.limiteTiempo < 0);
		} catch (Exception e) {
			throw new ParseException("Valor inválido para el límite de tiempo: " + t);
		}
	}

	private static void iniciaModoEstandar() throws IOException, ErrorDeSimulacion {
		InputStream is = new FileInputStream(new File(Main.ficheroEntrada));
		OutputStream os = Main.ficheroSalida == null ? System.out : new FileOutputStream(new File(Main.ficheroSalida));
		SimuladorTrafico sim = new SimuladorTrafico();
		Controlador ctrl = new Controlador(sim, Main.limiteTiempo, is, os);
		ctrl.ejecuta();
		is.close();
		os.close();
		System.out.println("Done!");
	}

	private static void iniciaModoGrafico() throws FileNotFoundException, InvocationTargetException, InterruptedException {
		SimuladorTrafico sim = new SimuladorTrafico();
		OutputStream os = null;
		Controlador ctrl = new Controlador(sim, Main.limiteTiempo, null, os);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VentanaPrincipal(Main.ficheroEntrada, ctrl);
			}
		});
	}

	public static void main(String[] args) {
		// example command lines:
		//
		// -i resources/examples/events/basic/ex1.ini
		// -i resources/examples/events/advanced/ex1.ini -t 100
		// -m batch -i resources/examples/events/basic/ex1.ini -t 20
		// -m batch -i resources/examples/events/advanced/ex1.ini
		// -m gui -i resources/examples/events/basic/ex1.ini
		// -m gui -i resources/examples/events/advanced/ex1.ini
		// --help
		//
		try {
			Main.ParseaArgumentos(args);

			if (Main.modo.equals(Main.ModoEjecucion.GUI))
				Main.iniciaModoGrafico();
			else {
				Main.ejecutaFicheros("resources/examples/events/basic");
				Main.ejecutaFicheros("resources/examples/events/advanced");
				//Main.iniciaModoEstandar();
			}
		} catch (ErrorDeSimulacion eds) {
			System.out.println(eds.getMessage());
		} catch (InvocationTargetException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void ejecutaFicheros(String path) throws IOException, ErrorDeSimulacion {
		File dir = new File(path);

		if (!dir.exists()) {
			throw new FileNotFoundException(path);
		}

		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		});

		for (File file : files) {
			System.out.println(file.getName());
			Main.ficheroEntrada = file.getAbsolutePath();
			Main.ficheroSalida = file.getAbsolutePath() + ".out";
			Main.limiteTiempo = 120;
			Main.iniciaModoEstandar();
		}
	}
	
	public static String getFicheroSalida() {
		return ficheroSalida;
	}

}
