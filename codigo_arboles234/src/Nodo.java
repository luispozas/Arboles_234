package arboles_234;

public class Nodo {
	static final int NODO_4 = 3;
	static final int NODO_3 = 2;
	static final int NODO_2 = 1;
	Nodo _padre = null;
	int _numElem = 0;  /*Elementos de la clave.*/
    int data[] = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};  /* Supongo que es un arbol de enteros (simplificar). */
    Nodo hijos[] = new Nodo[NODO_4 + 1]; /* claves + 1 = numero de hijos. */
    
    public Nodo() {}
    
    /* "crearNodo2" crea un nodo-2 con una clave y sus dos hijos (hijo1 < hijo2). */
    private Nodo crearNodo2(int clave, Nodo hijo1, Nodo hijo2) {
    	Nodo ret = new Nodo();
    	ret.insertarClave(clave);
    	ret.conectarHijo(0, hijo1);
    	ret.conectarHijo(1, hijo2);
    	return ret;
    }
    
    /* "padre2_combinarHijos2" combina tres nodos-2 (this es el padre y los argumentos los hijos). */
    public Nodo padre2_combinarHijos2(Nodo n1, Nodo n2) {
		insertarClave(n1.getClave(0));
		insertarClave(n2.getClave(0));
		conectarHijo(0, n1.getHijo(0));
		conectarHijo(1, n1.getHijo(1));
		conectarHijo(2, n2.getHijo(0));
		conectarHijo(3, n2.getHijo(1));
		return this;
    }
    
    
    /* Combina dos nodos-2 con un padre de tipo nodo-3/4. */
    public Nodo padre34_combinarNodos_2(Nodo n1, Nodo n2, int pos_hijo) {
    	int clave;
    	if(pos_hijo == this._numElem) {
    		clave = this.data[idxClaveDelHijo(pos_hijo-1)];
    		eliminaClave(pos_hijo-1);
    	}
    	else {
    		clave = this.data[idxClaveDelHijo(pos_hijo)];
    		eliminaClave(pos_hijo);
    	}
    	Nodo nuevo = crearNodo2(clave, n1, n2);
    	Nodo hijo = nuevo.padre2_combinarHijos2(n1, n2);
    	conectarHijo(pos_hijo, hijo);
    	return this;	
    }
    
    public int idxClaveDelHijo(int pos_hijo) {
    	return  pos_hijo == this._numElem? pos_hijo-1 : pos_hijo;
    }
    
	/* "robarNodoHermanoDcha" roba un nodo a un hermano de la derecha a traves
	 * del padre para no romper el invariante. */
	public Nodo robarNodoHermanoDcha(Nodo n1, Nodo hermano, int pos_hijo) {
		return robarNodoHermano(n1, hermano, pos_hijo, 0, 0, 2, pos_hijo+1);
	}
	
	/* "robarNodoHermanoIzq" roba un nodo a un hermano de la izquierda a traves
	 * del padre para no romper el invariante. */
	public Nodo robarNodoHermanoIzq(Nodo n1, Nodo hermano, int pos_hijo) {
		return robarNodoHermano(n1, hermano, pos_hijo, hermano.getNumElem()-1, hermano.getNumElem(), 0, pos_hijo-1);
	}
	
	public Nodo robarNodoHermano(Nodo n1, Nodo hermano, int pos_hijo, int i_clave_UP, int i_hijo_herm, int i_conectar, int i_herm){
		int pos_clave = idxClaveDelHijo(pos_hijo);
		int bajarCLave = eliminaClave(pos_clave);
		int subirClave = hermano.eliminaClave(i_clave_UP);
		insertarClave(subirClave);
		Nodo cambia = hermano.getHijo(i_hijo_herm);
		n1.insertarClave(bajarCLave);
		n1.conectarHijo(i_conectar, cambia);
		conectarHijo(pos_hijo, n1);
		conectarHijo(i_herm, hermano);
		return this;
	}
	
	/* "buscaElemento" busca un elemento en el nodo. Devuelve la posicion donde
	 * lo ha encontrado, en caso contrario "-1". */
	public int buscaElemento(int elem){
		for(int i = 0; i < _numElem; i++)
			if(data[i] == elem) return i;
		return -1;
	}
	
	/* "dividirNodoCuatro" divide un nodo-4 en dos nodos-2 quitando
	 * la clave del MEDIO. Devuelve los dos nodos nuevos y la clave
	 * que ha tenido que eliminar a consecuencia de esta particion. */
	public Object[] dividirNodoCuatro(){
		Nodo n1 = crearNodo2(data[0], hijos[0], hijos[1]);
		Nodo n2 = crearNodo2(data[2], hijos[2], hijos[3]);
		n1.setPadre(this.getPadre());
		n2.setPadre(this.getPadre());
		return new Object[] {n1, n2, data[1]};
	}

	/* "conectarHijo" conecta un nodo_hijo en la poscicon "pos_hijo"
	 * y devuelve "true" si ha concurrido de manera satisfactoria, "false"
	 * en caso contrario. */
	public boolean conectarHijo(int pos_hijo, Nodo nodo){
		if(pos_hijo > NODO_4+1) return false; /* Excede el numero maximo de hijos. */
		for(int i = NODO_4; i > pos_hijo; --i)
			hijos[i] = hijos[i-1];
	    hijos[pos_hijo] = nodo;
	    if(nodo != null) nodo.setPadre(this);
	    return true;
	}
	
	public boolean desconectarHijo(int pos_hijo) {
		if(pos_hijo < 0 || pos_hijo > 3) return false;
		for(int i = pos_hijo; i < NODO_4; ++i)
			hijos[i] = hijos[i+1];
		hijos[NODO_4] = null;
		return true;
	}
	
	/* "eliminaClave" elimina una clave de la posicion "pos" del 
	 * array "data" y devuleve la clave que ha eliminado. */
	public int eliminaClave(int pos){
	    int temp = data[pos];
	    for(int i = pos; i < NODO_4-1; ++i)
	    	data[i] = data[i+1];
	    data[NODO_4-1] = Integer.MAX_VALUE;
	    _numElem--;
	    return temp;
	}
	
	/* "insertarClave" devuelve la posicion del array "data"
	 * en la cual ha insertado ordenadamente la nueva clave. */
	public int insertarClave(int clave) {
		if(maximoNumClaves()) return -1;
		int i = 0;
		while(data[i] < clave && i < _numElem) i++; /* Busco la poscicion. */
		for(int pos = _numElem-1; pos >= i; --pos) { /* Hago hueco para insertar. */
			data[pos+1] = data[pos];
		}
		data[i] = clave; /* Inserto. */
		_numElem++;
		return i;
	}
	
	public boolean esNodo_2() {
		return (_numElem == NODO_2);	
	}
	
	public boolean esNodo_3() {
		return (_numElem == NODO_3);	
	}
	
	public boolean esNodo_4() {
		return (_numElem == NODO_4);	
	}
	
	public void setPadre(Nodo padre) {
		_padre = padre;
	}
	
    public Nodo getPadre() {
		return _padre;
	}
    
    public Nodo getHijo(int pos) {
    	return hijos[pos];
    }
    
	public int getClave(int pos){
	    return data[pos];
	}
	
	public int getNumElem() {
		return _numElem;
	}
	
	public boolean maximoNumClaves() {
		return (_numElem == NODO_4);
	}
	
	public boolean minimoNumClaves() {
		return (_numElem == NODO_2);
	}
	
	public boolean esHoja() {
		for(int i = 0; i <= _numElem; ++i) {
			if(hijos[i] != null) return false;
		}
		return true;
	}
	
	public boolean esRaiz() {
		return (_padre == null);
	}
}
