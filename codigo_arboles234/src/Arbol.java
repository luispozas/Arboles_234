package arboles_234;

public class Arbol {
	private Nodo _raiz;

	public Arbol () {
		_raiz = new Nodo();
	}
	
	public boolean buscar(Nodo act, int clave){
		if(act == null) return false;
		if(act.buscaElemento(clave) != -1) return true;
		else {
			return buscar((Nodo)siguienteHijo(act, clave)[0], clave);	
		}
	}
	
	public void insertar(Nodo act, int clave){
		Nodo continua = quitarNodosCuatro(act);
		if(continua.esHoja()) continua.insertarClave(clave);
		else insertar((Nodo)siguienteHijo(continua, clave)[0], clave);
	}

	public boolean borrar(Nodo act, int clave) {
		Nodo continua = quitarNodosDos(act, clave); //Si no se realiza nada continua = act.
		if(continua == null) return false;
		int pos = continua.buscaElemento(clave);
		if(pos != -1) {
			if(continua.esHoja()) 
				continua.eliminaClave(pos);
			else {
				int claveNueva = buscarMinHD(continua.getHijo(pos+1), clave-1);
				continua.eliminaClave(pos);
				continua.insertarClave(claveNueva);
			}
			return true;
		}
		return borrar((Nodo)siguienteHijo(continua, clave)[0], clave);	
	}
	
	/*	"buscarMinHD" se le pasa el nodo en el que estamos y la clave del padre, de
	 * 	esta manera cuando llame a "siguienteHijo" siempre devolvera el menor ya que
	 * 	todos los de ese arbol son mayores que el padre. Finalmente lo borra y lo devuelve.*/
	
	private int buscarMinHD(Nodo act, int clave) {
		Nodo continua = quitarNodosDos(act, clave);
		if(continua.esHoja()) return continua.eliminaClave(0);
		return buscarMinHD((Nodo)siguienteHijo(continua, clave)[0], clave);
	}

	/* "quitarNodosDos" evita que todo nodo en curso no es un nodo-2, para lograrlo,
	 * antes de descender a un nodo-2, lo transforma en un nodo-3 o en un nodo-4.
	 * Cabe destacar que si no se produce una transformacion se devuelve el mismo nodo. */
	
	private Nodo quitarNodosDos(Nodo padre, int clave) { /* Para comprender mejor esta funcion, ver "borrado_casos.jpeg". */
		if(padre == null) return null;
		Object[] tmp = siguienteHijo(padre, clave);
		Nodo sig = (Nodo)tmp[0];
		int pos_hijo = (int)tmp[1];
		
		if(padre.esHoja()) return padre; /* Solo puede ser un nodo_2 la raiz del arbol. */
		if(sig.esNodo_3() || sig.esNodo_4()) return padre; /* No se hace nada. */
		Nodo hermano = (Nodo) getHermano(padre, pos_hijo)[0]; /* Se elige a un hermano para poder realizar las combinaciones o rotaciones. */
		boolean hermanoDerecho = (boolean) getHermano(padre, pos_hijo)[1];
		if(sig.esNodo_2() && hermano.esNodo_2()) {
			if(padre.esNodo_2()) {
				if(hermanoDerecho) return padre.padre2_combinarHijos2(sig, hermano);
				else return padre.padre2_combinarHijos2(hermano, sig);
			}
			else if(padre.esNodo_3() || padre.esNodo_4()) {
				if(hermanoDerecho) return padre.padre34_combinarNodos_2(sig, hermano, pos_hijo);
				else return padre.padre34_combinarNodos_2(hermano, sig, pos_hijo-1);
			}
		}
		if(sig.esNodo_2() && (hermano.esNodo_3() || hermano.esNodo_4())) {
			if(hermanoDerecho) return padre.robarNodoHermanoDcha(sig, hermano, pos_hijo);
			else return padre.robarNodoHermanoIzq(sig, hermano, pos_hijo);
		}
		return padre;
	}
	
	/* Siempre intenta devolver el hermano de la derecha, salvo cuando el nodo hijo previamente
	 * elegido es el ultimo, en este caso se coge el hermano de la izquierda. La funcion devuelve
	 * si el elegido ha sido el hermano derecho (true) en caso contrario (false). */
	private Object[] getHermano(Nodo padre, int pos_hijo) {
		if(padre.getNumElem() == pos_hijo) return new Object[] {padre.getHijo(pos_hijo-1), false};
		return new Object[] {padre.getHijo(pos_hijo+1), true};
	}
	
	/* Devuelve el sieguiente hijo que corresponde siguiendo la busqueda de la clave */
	public Object[] siguienteHijo(Nodo nodo, int clave) {
		int i = 0, tamNodo = nodo.getNumElem();
		while (i < tamNodo && !(clave < nodo.getClave(i)))
			++i;
		/* Si no ha encontrado ninguno menor, devuelve el ultimo hijo (tamNodo) y su posicion. */
		return new Object[] {nodo.getHijo(i), i};
	}
	
	/* "quitarNodosCuatro" elimina nodos-4 tranformandolos en dos nodos-2 y devuelve
	 * el nodo padre (si se ha subido una clave hacia arriba) o el actual para que
	 * de esta manera pueda proseguir el "insertar" */
	public Nodo quitarNodosCuatro(Nodo nodo) {
		int pos = 0;
		if(nodo.maximoNumClaves()) {
			Object[] tmp = nodo.dividirNodoCuatro();
			Nodo n1 = (Nodo) tmp[0];
			Nodo n2 = (Nodo) tmp[1];
			int subirClave = (int) tmp[2];
			if(nodo.esRaiz()) { /* Si es la raiz del arbol. */
				Nodo nuevaRaiz = new Nodo();
				nuevaRaiz.insertarClave(subirClave);
				n1.setPadre(nuevaRaiz);
				n2.setPadre(nuevaRaiz);
				nuevaRaiz.conectarHijo(0, n1);
				nuevaRaiz.conectarHijo(1, n2);
				this._raiz = nuevaRaiz;
				return nuevaRaiz;
			}
			else { /* Entonces tiene un padre que puede ser un nodo 2/3 (Por el invariante). */
				Nodo padre = nodo.getPadre();
				n1.setPadre(padre);
				n2.setPadre(padre);
				pos = padre.insertarClave(subirClave);
				padre.desconectarHijo(pos);
				padre.conectarHijo(pos, n1);
				padre.conectarHijo(pos+1, n2);	
				return padre;
			}
		}
		return nodo;
    }
	
	public Nodo getRaiz() {
		return _raiz;
	}

	public void setRaiz(Nodo _raiz) {
		this._raiz = _raiz;
	}
}