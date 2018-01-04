import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Funciones {
	
	String css ;
	boolean responsive = false;
	String cssRecortado;
	
	public  boolean comprobarAnalytics(String html){
		boolean encontrado =false;
		if((html.contains("analytics.js")) || (html.contains("ga.js")) || (html.contains("googletagmanager"))){
			encontrado = true;
		}
		return encontrado;
	}
	
	public boolean comprobarResponsive(String html, String url) {
		//Escribo la forma tipica de meter boostrap, si el dueño de la pagina lo ha cambiado de nombre hay que comprobarlo (como css normal).
		if (html.contains("bootstrap.min.css")) {
			responsive = true;
			return responsive;
		}

		Pattern pat = Pattern.compile("href=(.*)\\.css\\??[a-zA-Z0-9_ .&#=;]*");
		Matcher css = pat.matcher(html);
		
		System.out.println("URL: "+ url + " ");
		// Recorro todos los css que he encontrado
		while ((responsive == false) && css.find()) {
			
			cssRecortado = css.group().substring(6);

			// Comprobar que el .css tiene estructura de URL, porque a veces se usan rutas relativas
			if (!cssRecortado.contains("http") ) {					
				cssRecortado = url + "/" + cssRecortado;	
			}
			//Para ver lo codigo css analizados 
			//System.out.println("Analizando: "+cssRecortado);
			// Si me da un codigo 200 es que existe y debo de comprobarlo.	
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
						// No hacer nada, porque es un codigo 400 (de error/ no existe)
					} else {
						// Cualquier otro tipo que no sea 200

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
					}
					}

		}
		return responsive;
	}
	
	//Funcion para buscar RegExp de Email en paginas y subpaginas
	public String buscarEmail(String html, String url) {
		String email = "Sin email";
		// Compruebo en el HTML que este la expREg de un email
		Pattern pat = Pattern.compile("[_a-z0-9\\-]+(\\.[_a-z0-9\\-]+)*\\@[_a-z0-9\\-]+(\\.[a-z]{1,4})",Pattern.CASE_INSENSITIVE);
		Matcher emails = pat.matcher(html);
		if (emails.find()) {
			emails.reset();
			while (emails.find()) {
				email = emails.group();
				// Doy prioridad a este tipo de emails (contacto@... ||info@...)
				if (email.contains("contacto") || email.contains("info")) {
					return (email);
				}
				// Sino hay ninguno con este formato devolvere el ultimo que haya encontrado
			}

		} else {
			// Si no encuenta un email se encontraran las url del html y se buscara por ellas (solo en las que se indique en el filtro de la funcion)

			Pattern pat2 = Pattern.compile("((?:https\\:\\/\\/)|(?:http\\:\\/\\/)|(?:www\\.))?([a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(?:\\??)[a-zA-Z0-9\\-\\._\\?\\,\\'\\/\\\\+ &%\\$#\\=~]+)",Pattern.CASE_INSENSITIVE);
			Matcher urlAanalizar = pat2.matcher(html);
			if (urlAanalizar.find()) {
				urlAanalizar.reset();
				while (urlAanalizar.find()) {
					// Uso el IF como filtro y evitar que analice todas las URL (asi me encontrara todas las paginas de contacto(español/ingles))
					String urlAnalizar = urlAanalizar.group();
					if (!urlAnalizar.contains("http")) {					
						urlAnalizar = url + "/" + urlAnalizar;	
					}
					if (urlAnalizar.contains("contact")) {
						if (comprobarCodigoRespuesta(urlAnalizar).equals("200")) { // Esto es porque la url debe de existir
							String htmlAUX = URLaString(urlAnalizar);
							Matcher matAUX = pat.matcher(htmlAUX);
							if (matAUX.find()) {
								matAUX.reset();
								while (matAUX.find()) {
									email = matAUX.group();
									// Doy prioridad a este tipo de emails (contacto@... || info@...)
									if (email.contains("contacto") || email.contains("info")) {
										return (email);
									}
									// Sino hay ninguno con este formato devolvere el ultimo que haya encontrado
								}

							}
						}
					}

				}
			}
		}
		return (email);
	}
	
	//Funcion que devuelve el codigo de la cabecera.
	public String comprobarCodigoRespuesta(String url) {
		URL u;
		int codigo = 0;
		try {
			u = new URL(url);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("HEAD");
			huc.connect();
			codigo = huc.getResponseCode();

		} catch (MalformedURLException e) {
			System.out.println("AVISO: La url " + url + " no se puede analizar por la extension.");
			;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(codigo);

	}
	
	
	//Pre: La URL debe de existir 
	public static String URLaString(String url) {
		URL link;
		StringBuilder builder;
		String linea;
		BufferedReader in = null;
		String html = null;
		try {
			builder = new StringBuilder();
			link = new URL(url);
			in = new BufferedReader(new InputStreamReader(link.openStream()));

			while ((linea = in.readLine()) != null) {
				builder.append(linea + "\r\n");
			}

			html = builder.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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