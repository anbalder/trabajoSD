import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;


public class ProcesarCsvEmails implements Callable<String>{
	
	protected String url ="";
	
	
	public ProcesarCsvEmails (String url){
		this.url=url;
	}

	public String call()   {
		Funciones fun = new Funciones(); 
		
		//Hace falta comprobar que la URL existe, asi que compruebo su codigo y hago la redireccion si hace falta
		
		URL obj;
		String email="";
		try {
			obj = new URL(url);
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
		email = fun.buscarEmail(html,url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (email);
	}

}