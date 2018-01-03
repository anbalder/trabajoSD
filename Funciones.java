import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Funciones {
	
	String css ;
	boolean responsive = false;
	String cssRecortado;
	
	public  boolean comprobarAnalytics(String html){
		boolean encontrado =false;
		if(!(html.equals("analytics.js")) || !(html.equals("ga.js")) || !(html.equals("googletagmanager"))){
			encontrado = true;
		}
		return encontrado;
	}
	
	public boolean comprobarResponsive(String html, String url) {
		//Escribo las forma tipica de meter boostrap, si el due–o de la pagina lo ha cambiado de nombre debere comprobarlo.
		if (html.contains("bootstrap.min.css")) {
			responsive = true;
			return responsive;
		}

		Pattern pat = Pattern.compile("href=(.*)\\.css\\??[a-zA-Z0-9_ .&#=;]*");
		Matcher css = pat.matcher(html);
		
		System.out.println("URL: "+ url + " ");
//		while (css.find()) {
//		  System.out.print("Start index: " + css.start());
//          System.out.print(" End index: " + css.end() + " ");
//          System.out.println(css.group());
//		}
		// Recorro todos los css que he encontrado
		while ((responsive == false) && css.find()) {
			
				cssRecortado = css.group().substring(6);

				// Comprobar que el .css tiene estructura de URL, porque a veces se usan rutas relativas
				if (!cssRecortado.contains("http")) {					
					cssRecortado = url + "/" + cssRecortado;	
				}
				//Para ver lo codigo css analizados 
				System.out.println("Analizando: "+cssRecortado);
				// Si me da un codigo 200 es que existe y debo de comprobarlo.
				try {
					if (comprobarCodigoRespuesta(cssRecortado).equals("200")) {
						// Guardo el codigo HTML del .CSS para comprobar mendiante Exp Regulares si tiene mediaQuerys
						String codigoCss = URLaString(cssRecortado);
						
						// Esta expresion regular  encuentra los  media  expresion ignora los print
						Pattern patMediaQuery = Pattern.compile("@media\\??[a-zA-Z0-9_ .&#=;]*\\((min|max)\\-width"); 
						
						Matcher matMediaQuery = patMediaQuery.matcher(codigoCss);
						if (!matMediaQuery.find()) {
							responsive = false;
						} else {
							responsive = true;
							return responsive;
						}
					} else {
						if (comprobarCodigoRespuesta(cssRecortado).equals("400") || comprobarCodigoRespuesta(cssRecortado).equals("404") || comprobarCodigoRespuesta(cssRecortado).equals("403") ) {
							// No hacer nada, porque es un codigo 400
						} else {
							// Cualquier otro tipo que no sea 200

							// Guardo el codigo HTML del .CSS para comprobar
							// mendiante Exp Regulares si tiene mediaQuerys
							String codigoCss = URLaString(cssRecortado);
							// Esta expresion regular  encuentra los  media  expresion ignora los print
							Pattern patMediaQuery = Pattern.compile("@media\\??[a-zA-Z0-9_ .&#=;]*\\((min|max)\\-width"); 	
							Matcher matMediaQuery = patMediaQuery.matcher(codigoCss);
							if (!matMediaQuery.find()) {
								responsive = false;
							} else {
								responsive = true;
								return responsive;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		return responsive;
	}
	
	//Funcion que devuelve el codigo de la cabecera.
	public String comprobarCodigoRespuesta(String url) throws IOException{
		URL u = new URL ( url);
		HttpURLConnection huc =  ( HttpURLConnection )u.openConnection(); 
		huc.setRequestMethod ("HEAD"); 
		huc.connect () ; 
		int codigo = huc.getResponseCode() ;
		return String.valueOf(codigo);
	}
	
	
	//Pre: La URL debe de existir 
	public static String URLaString(String url){
		URL link;
		StringBuilder builder;
		String linea;
		BufferedReader in = null;
		String html = null;
		try{
			builder = new StringBuilder();
			link = new URL(url);
			in = new BufferedReader(new InputStreamReader(link.openStream()));
			
			while ((linea = in.readLine()) != null) {
				builder.append(linea + "\r\n");
			}
			
			html = builder.toString();
			
		} catch (IOException e){
			e.printStackTrace();
		}  finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return html;
	}
}