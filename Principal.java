import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Principal {

	public static void main(String[] args) {

		System.out.println("Escriba la ruta de un archivo .CSV separado por ;");
		Scanner sc = new Scanner(System.in);
		String archivo = sc.nextLine();

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
				String[] datosCabecera = line.split(cvsSplitBy);
				
				// Comprobar estructura del archivo
				if (datosCabecera[0] == "URL") {
					ExecutorService  pool = Executors.newCachedThreadPool();
					List<Callable<List<Boolean>>> resultados= new ArrayList<Callable<List<Boolean>>>();
					while ((line = br.readLine()) != null) {
						String[] datos = line.split(cvsSplitBy);
						
						resultados.add(new ProcesarCsv(datos[0]));
					}
					List<Future<List<Boolean>>> resultadoProcesado = pool.invokeAll(resultados);
					
					

				} else {
					System.out.println("La estructura del archivo es erronea");
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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

	}

}
