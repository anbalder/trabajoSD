import java.net.HttpURLConnection;
import java.net.URL;
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
		
		//Hace falta comprobar que la URL existe, asi que compruebo su codigo y hago la redireccion si hace falta
		
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
			//Cambio la url a donde apunta la redireccion
			
				url = conn.getHeaderField("Location");
			System.out.println("AVISO: La pagina "+obj.toString()+" ha sido redirigida a: "+ url+ "\n"+ "------------------------------------");
		
		}

		
		String html = Funciones.URLaString(url);
		
		//Funciones del html
		boolean responsive = fun.comprobarResponsive(html,url);
		boolean analytics = fun.comprobarAnalytics(html);

		resultado.add(responsive);
		resultado.add(analytics);
		return (resultado);
	}

}