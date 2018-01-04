# Trabajo Sistemas Distribuidos
Universidad de La Rioja - 2018

Nuestra aplicación tiene 2 modos de analizar:
1) Comprueba si una página web es responsive y dispone de Google analytics. Para ello se le pasa un archivo de extensión .csv con una lista de links a distintas páginas web.
2) Analiza la página web en busca de un email de contacto y en caso de no encontrar nada en la ella analiza las subpáginas de esta. El archivo que se le debe de pasar es con el mismo formato que en el modo anterior
Para ello, hemos utilizado las siguientes clases:
-	Funciones: contiene los métodos “comprobarAnalitycs()”, que dado un html, busca unas cadenas determinadas contenidas en el código html de la página web. Si encuentra una de ellas, ésta tiene Google analitycs. 
Otro método incluido en esta clase es “comprobarResponsive()”, que dado un html y una url, nos devuelve un booleano “true” si la página web es responsive o no. Éste lo hace, como el anterior, comprobando si el código de la página contiene una cadena determinada (dispone de la hoja de estilos Bootstrap), en el caso de que esto no resulte, recorreremos los .css en busca de Media Queries.
Otro método que tiene esta clase es “buscarEmail()”que dado un html y una url busca es ese código un email de contacto y en caso de no encontrarlo lo busca por las subpáginas.
El siguiente método que encontramos es “comprobarCodigoRespuesta()”, que dado un url, devuelve el código de respuesta (HEAD).
Por último, el método “URLaString()”, que dada una url, la devuelve en forma de String con los saltos de línea correspondientes. 
-	ProcesarCsv: Esta clase comprueba si realmente la página web es responsive y tiene Gooogle analitycs.
-	ProcesarCsvEmails: Esta clase comprueba si la página dispone de un email de contacto.
-	Principal: En esta clase se comprueba la estructura de la página web y devuelve por pantalla “true” o “false” en el caso de que sean o no sean responsive y analytics.
