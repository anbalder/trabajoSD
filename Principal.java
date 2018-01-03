import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Para ejecutar este programa hace falta tener JRE en vez de la JDK
public class Principal {

	public static void main(String[] args) {

		System.out.println("Escribe de modo que quieres procesar las URL del archivo .CSV");
		System.out.println("1 - Comprobar Analytics y Responive.");
		System.out.println("2 - Extraer los emails de contacto de las paginas.");
		System.out.println("0 - Salir");
		Scanner scMenu = new Scanner(System.in);
		String modo  = scMenu.nextLine();

		if(modo.equals("1")){
		
		System.out.println("Escriba la ruta de un archivo .CSV separado por ;");
		Scanner sc1 = new Scanner(System.in);
		String archivo = sc1.nextLine();
		
		Boolean responsive = false;
		Boolean analytics = false;
		String[] datos = null;
		List<String> listaPaginas = new ArrayList <String> ();
		int contadorPag = 0;

		File archivoAprocesar = new File(archivo);
		// Si existe sino error
		if (archivoAprocesar.exists()) {

			BufferedReader br = null;
			String line = "";
			// Se define separador ";"
			String cvsSplitBy = ";";
			try {
				br = new BufferedReader(new FileReader(archivoAprocesar));
				// Para ver la primera linea del archivo
				line = br.readLine();
				String[] datosCabecera = line.split(cvsSplitBy);

				// Comprobar estructura del archivo
				if (datosCabecera[0].equals("URL")) {
					ExecutorService  pool = Executors.newCachedThreadPool();
					List<Callable<List<Boolean>>> resultados= new ArrayList<Callable<List<Boolean>>>();
					while ((line = br.readLine()) != null) {
						datos = line.split(cvsSplitBy);
						//System.out.println(datos[0]);
						resultados.add(new ProcesarCsv(datos[0]));
						listaPaginas.add(datos[0]);
					}
					List<Future<List<Boolean>>> resultadoProcesado = pool.invokeAll(resultados);
					System.out.println("RESULTADOS:");
					System.out.println("------------------------------");
					for (Future<List<Boolean>> rp : resultadoProcesado){
						int contador = 0;
						List<Boolean> resultadoBoolean = new ArrayList<Boolean>();
						resultadoBoolean = rp.get();
						for(Boolean resultado : resultadoBoolean){
							contador ++;
							if(contador == 1){
								responsive = resultado;
							}
							if(contador == 2){
								analytics = resultado;
							}
						}
					System.out.println(listaPaginas.get(contadorPag) + ":" + "\n" + "Responsive: " + responsive);
					System.out.println("Analytics: " + analytics);
					contadorPag++;
					}
				} else {
					System.out.println("La estructura del archivo es erronea");
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			System.out.println("El fichero no existe!");
		}

	 }else{
		 if(modo.equals("2")){
			System.out.println("Escriba la ruta de un archivo .CSV separado por ;");
			Scanner sc2 = new Scanner(System.in);
			String archivo = sc2.nextLine();
			
			
			String[] datos = null;
			List<String> listaPaginas = new ArrayList <String> ();
			int contadorPag = 0;

			File archivoAprocesar = new File(archivo);
			// Si existe sino error
			if (archivoAprocesar.exists()) {

				BufferedReader br = null;
				String line = "";
				// Se define separador ";"
				String cvsSplitBy = ";";
				try {
					br = new BufferedReader(new FileReader(archivoAprocesar));
					// Para ver la primera linea del archivo
					line = br.readLine();
					String[] datosCabecera = line.split(cvsSplitBy);

					// Comprobar estructura del archivo
					if (datosCabecera[0].equals("URL")) {
						ExecutorService  pool = Executors.newCachedThreadPool();
						List<Callable<String>> resultados= new ArrayList<Callable<String>>();
						while ((line = br.readLine()) != null) {
							datos = line.split(cvsSplitBy);
							//System.out.println(datos[0]);
							resultados.add(new ProcesarCsvEmails(datos[0]));
							listaPaginas.add(datos[0]);
						}
						List<Future<String>> resultadoProcesado = pool.invokeAll(resultados);
						System.out.println("RESULTADOS:");
						System.out.println("------------------------------");
						String email ="";
						for (Future<String> rp : resultadoProcesado){
							email = rp.get();
							
						System.out.println(listaPaginas.get(contadorPag) + ":" + "\n" + "Email de contacto: " +email);
						contadorPag++;
						}
					} else {
						System.out.println("La estructura del archivo es erronea");
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				System.out.println("El fichero no existe!");
			}
		 }else{
			 System.exit(0);
		 }
		 
	 }
	}

}