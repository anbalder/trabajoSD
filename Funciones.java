import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Funciones {
	
	String css ;
	boolean responsive;
	String cssRecortado;
	
	public boolean comprobarAnalytics(String html){
		boolean encontrado =false;
		if(!(html.equals("analytics.js")) || !(html.equals("ga.js")) || !(html.equals("googletagmanager"))){
			encontrado = true;
		}
		return encontrado;
	}
	
	public boolean comprobarResponsive(String html, URL url){
		boolean responsive = false;
		if(html.equals("bootstrap.min.css")){
			responsive=true;
			return responsive;
		}
		//Pattern p = Pattern.compile("/href=(.*)\\.css\\??[a-zA-Z0-9_ .&#=;]*/", html);
		//Matcher m = p.matcher(css);
		
		 Pattern pat = Pattern.compile("/href=(.*)\\.css\\??[a-zA-Z0-9_ .&#=;]*/");
	     Matcher css = pat.matcher(html);
		
		int i = 0;
		//Recorro todos los css que he encontrado
		while((i< css.groupCount()) && (responsive==false)){
			cssRecortado = css.group(i).substring(6);
			//Si me da un codigo 200 es que existe y debo de comprobarlo.
			try {
				if(comprobarCss(cssRecortado) == "200"){
					//Guardo el codigo HTML del .CSS para comprobar mendiante Exp Regulares si tiene mediaQuerys 
					String codigoCss = fileToString(cssRecortado);
					
					Pattern patMediaQuery = Pattern.compile("/@media\\??[a-zA-Z0-9_ .&#=;]*\\((min|max)\\-width/"); //Esta expresion regular encuentra los media e ignora los print
				    Matcher matMediaQuery = patMediaQuery.matcher(codigoCss);			
						if(!matMediaQuery.find()){
							responsive = false;
						}
						else{
							responsive = true;
						}			
				}
				else{
					if(comprobarCss(cssRecortado) == "400"){
						//No hacer nada, porque es un codigo 
					}
					else{
						//Cualquier otro tipo que no sea 200
						
						//Guardo el codigo HTML del .CSS para comprobar mendiante Exp Regulares si tiene mediaQuerys 
						String codigoCss = fileToString(cssRecortado);
						
						Pattern patMediaQuery = Pattern.compile("/@media\\??[a-zA-Z0-9_ .&#=;]*\\((min|max)\\-width/"); //Esta expresion regular encuentra los media e ignora los print
					    Matcher matMediaQuery = patMediaQuery.matcher(codigoCss);			
							if(!matMediaQuery.find()){
								responsive = false;
							}
							else{
								responsive = true;
							}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
		return responsive;
	}
	
	//Funcion que devuelve el codigo de la cabecera.
	public String comprobarCss(String url) throws IOException{
		URL u = new URL ( url);
		HttpURLConnection huc =  ( HttpURLConnection )u.openConnection (); 
		huc.setRequestMethod ("HEAD"); 
		huc.connect () ; 
		int codigo = huc.getResponseCode() ;
		return String.valueOf(codigo);
	}
	
	
	//Pre: La URL debe de existir 
	public static String URLaString(String url) throws IOException {

		URL link;
		StringBuilder builder = new StringBuilder();

		link = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));

		String linea;

		while ((linea = in.readLine()) != null) {
			builder.append(linea);
		}

		in.close();

		String html = builder.toString();
		return html;

	}
	
	//Creo que no se usa
		public String fileToString(String filename) throws IOException
		{
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    StringBuilder builder = new StringBuilder();
		    String line;    

		    while((line = reader.readLine()) != null)
		    {
		        builder.append(line);
		    }

		    reader.close();
		    return builder.toString();
		}

}
