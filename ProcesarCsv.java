import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class ProcesarCsv implements Callable<List<Boolean>>{
	
	protected String url ="";
	
	
	public ProcesarCsv (String url){
		this.url=url;
	}

	public List<Boolean> call() throws Exception {
		List<Boolean> resultado = new ArrayList<Boolean>();
		Funciones fun = new Funciones(); 
		String html = Funciones.URLaString(url);
		//Funciones del html
		boolean analytics = fun.comprobarAnalytics(html);
		boolean responsive = fun.comprobarResponsive(html);
		
		resultado.add(analytics);
		resultado.add(responsive);
		return (resultado);
	}

}
