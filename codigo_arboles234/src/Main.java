package arboles_234;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	static Arbol a = new Arbol();
	static boolean b;
	
	public static void main (String [ ] args) throws FileNotFoundException {
		
		File fichero = new File("casos.txt");
		Scanner s = new Scanner(fichero);
		int elem;
		String op = s.nextLine();
		while (op != "FIN") {
			if(op.length() != 1) break; //Compruebo que tiene formato adecuado.
			elem = s.nextInt(); //Leo el elemento de la operacion
			switch (op) {
				case "i": a.insertar(a.getRaiz(), elem); break; //CASO INSERTAR
				case "d": b = a.borrar(a.getRaiz(), elem); //CASO BORRAR
						  if(b) System.out.println("Borrado del elemento: " + elem + " correcto!"); 
						  else System.out.println("Borrado del elemento: " + elem + " erroneo!"); 
						  break;
				case "s": b = a.buscar(a.getRaiz(), elem); //CASO BUSCAR
						  if(b) System.out.println("Busqueda del elemento: " + elem + " correctamente!"); 
				          else System.out.println("Busqueda del elemento: " + elem + " incorrecta!"); 
				          break;
				default: System.out.println("Error en el formato del texto."); break;
			}
			op = s.nextLine(); //Leo la basura de la linea
			op = s.nextLine(); //Leo la siguiente operacion a realizar
		}
		if (s != null) s.close();

	}
}
