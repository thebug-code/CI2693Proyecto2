package ve.usb.libGrafo

import java.io.File
import java.util.LinkedList

public class GrafoDirigidoCosto : Grafo {
    private var numDeVertices : Int = 0
    private var numDeLados : Int = 0
    private var adjList : Array<LinkedList<ArcoCosto>>
    private val identificadores: Array<Array<Int>>

    /**
     * Construye un grafo a partir del número de vertices [numDeVertices].
     * Precondición: [numDeVertices] > 0
     * Postcondición: [adjList.size] = [numDeVertices]
     * Tiempo de la operacion: O(|V|)
    */
    constructor(numDeVertices: Int) {
        this.numDeVertices = numDeVertices   
        this.adjList = Array(numDeVertices) { LinkedList() } 
        identificadores = Array(numDeVertices) { Array(numDeVertices) { -1 } }
    }

    /**
     * Construye un grafo a partir de un archivo [nombreArchivo]. 
     */
    constructor(nombreArchivo: String)  {
        val file = File(nombreArchivo)

        // Verificar que el archivo existe
        if (!file.exists()) {
            throw RuntimeException("El archivo dado no existe.")
        }

        val lines: List<String> = file.readLines()
        this.numDeVertices = lines[0].split(" ")[0].toInt()
        this.adjList = Array(numDeVertices) { LinkedList() }      
        identificadores = Array(numDeVertices) { Array(numDeVertices) { -1 } }

        var i: Int
        var j: Int
        var c: Double
        for (lado in 1 until lines[0].split(" ")[1].toInt() + 1) {
            i = lines[lado].split(" ")[0].toInt() - 1
            j = lines[lado].split(" ")[1].toInt() - 1
            c = lines[lado].split(" ")[2].toDouble()

            // Id de la lado-énesima calle
            identificadores[i][j] = lado - 1
            identificadores[j][i] = lado - 1

            // Agregar arista al grafo
            this.agregarArcoCosto(ArcoCosto(i, j, c))
            this.agregarArcoCosto(ArcoCosto(j, i, c))
            this.numDeLados--
        }
    }
    
    /**
     * Agrega el lado [a] al grafo dirigido, en el caso de que lo agrega retorna true, o false si el lado [a] ya
     * ya estaba presente en el grafo (y no lo agrega). Si uno (o ambos) de los vertices de [a] no pertenecean al
     * grafo entonces se lanza un RuntimeException.
     * Precondición: Los vertices de [a] partenecen al grafo.
     * Tiempo de la operación: O(1).
     */
    fun agregarArcoCosto(a: ArcoCosto) : Boolean {
        // Verificar que los vertices del pertenecen al grafo
        if (!estaElVerticeEnElGrafo(a.x) || !estaElVerticeEnElGrafo(a.y)) {
            throw RuntimeException("Uno de los vertices del lado a agregar no esta en el grafo.")
        }
        // Verificar que el lado esta en el grafo
        if (estaElLadoEnElGrafo(a)) return false
        
        // Agregar el lado al grafo
        this.adjList[a.x].add(a)
        this.numDeLados++
        return true
    }
    
    /**
     * Retorna el identificador del lado ([u], [v]) (directo).
     * Precondición: [u] y [v] pertenecen al grafo, el lado ([u]. [v]) es lado
     * directo perteneciente al grafo.
     * Tiempo de la operación: O(1).
     */
    fun id(u: Int, v: Int): Int = this.identificadores[u][v]

    /**
     * Retorna el grado del vértice [v].
     * Precondición: el vértice [v] pertenece al grafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    override fun grado(v: Int) : Int {
        // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.") 
        }
        return this.gradoExterior(v) + this.gradoInterior(v)
    }

    /**
     * Retorna el grado exterior del vértice [v].
     * Precondición: el vértice [v] pertenece al digrafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    fun gradoExterior(v: Int) : Int {
       // Verificar que el vertice v pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.") 
        }
        return this.adjList[v].size
    }

    /**
     * Retorna el grado interior del vértice [v]
     * Precondición: el vértice [v] pertenece al digrafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación O(|E|+|V|).
    */
    fun gradoInterior(v: Int) : Int {
       // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }

        var count : Int = 0
        for (k in 0 until this.numDeVertices) {
            for (i in this.adjList[k]) {
                if (i.y == v) {
                    count++
                }
            }
        }
        return count
    }

    /**
     * Retorna el número de lados del grafo.
     * Precondicion: true.
     * Postcondicion: el valor retornado es mayor o igual que cero.
     * Tiempo de Complejidad: O(1).
    */
    override fun obtenerNumeroDeLados() : Int = this.numDeLados

    /**
     * Retorna el número de vértices del digrafo.
     * Precondición: true.
     * Postcondición: el valor retornado es mayor que cero.
     * Tiempo de la operación: O(1).
    */
    override fun obtenerNumeroDeVertices() : Int = this.numDeVertices

    /** 
     * Retorna los lados adyacentes de un vértice [v].
     * Precondición: el vértice [v] pertenece al digrafo.
     * Postcondición: un vértice de cada lado de la lista es [v].
     * Tiempo de la operación: O(1).
     */    
     override fun adyacentes(v: Int) : Iterable<ArcoCosto> {
       // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.") 
        }
        return this.adjList[v]
    }    

    /** 
     * Retorna los lados adyacentes del lado [l]. 
     * Precondición: [l] pertenece al grafo.
     * Postcondición: un vértice de cada lado de la lista es [l.fuente()] o [l.sumidero()].
     * Tiempo de la operación: O(|E|)
     */     
     fun ladosAdyacentes(l: ArcoCosto) : Iterable<ArcoCosto> {
        // Verificar que el lado esta en el grafo 
        if (!estaElLadoEnElGrafo(l)) { 
            throw RuntimeException("El lado dado no esta en el grafo.")
        }

        var adjLadosL : MutableList<ArcoCosto> = mutableListOf()
        for (i in 0 until this.numDeVertices) {
            for (k in this.adjList[i]) {
                if ((i == l.fuente() || i == l.sumidero() || k.sumidero() == l.fuente() || k.sumidero() == l.sumidero()) &&
                    !((i == l.fuente() && k.sumidero() == l.sumidero()))) { 
                        adjLadosL.add(k)
                    }
            }
        } 
        return adjLadosL
    }

    /**
     * Clase interna con la implementación concreta de Grafo dirigido iterator.
    */
    inner class GrafoDirigidoCostoIterador(g: GrafoDirigidoCosto) : Iterator<ArcoCosto> {
        var ll : MutableList<ArcoCosto> = mutableListOf()
        init {
            for (i in g.adjList) ll.addAll(i)
        }
        
        var x = 0
        var actual = ll[0]
        var llIterable = ll.listIterator()
        
        override fun hasNext(): Boolean = llIterable.hasNext()

        override fun next(): ArcoCosto {
            if (!llIterable.hasNext()) {     
                throw RuntimeException("No hay más elementos que iterar.")  
            }

            var y = actual
            if (x + 1 < ll.size) {
                actual = ll[x+1]
		        x++
            }
            llIterable.next()
            return y
        }
    }

    /**
     * Retorna un iterador de todos los lados del digrafo.
     * Precondición: true.
     * Poscondición: true.
     * Tiempo de Complejidad: O(E).
    */
    override operator fun iterator(): Iterator<ArcoCosto> = GrafoDirigidoCostoIterador(this)
    
    /**
     * Retorna un string con la representación del contenido del digrafo.
     * Precondición: true
     * Postcondión: true.
     * Tiempo de la operación: O(|E|).
    */
    override fun toString(): String {
        var grafoEnString: String = "\n\tGrafo Dirigido Costo\nVertices -> Adyacentes\n"
        for ((verticieInicial, arcosIncidentes) in adjList.withIndex()){
        
            // Se escribe el identificador del vertice inicial
            grafoEnString += "     |${verticieInicial}| "
        
            // se escribe los vertices finales y el peso de cada lado en el resto de la linea            
            for (arco in arcosIncidentes){
                grafoEnString += "-> [${arco.sumidero()}|${arco.costo}] "
            }
        
            // salto de linea
            grafoEnString += "\n"
        }
        return grafoEnString
    }
     
    /**
     * Determina si el lado [l] está en el grafo.
     * Precondición: El vértice fuente pertenece al grafo.
     * Tiempo de la operación: O(|E|).
    */
    private fun estaElLadoEnElGrafo(l: ArcoCosto) : Boolean {
        if (l in this.adjList[l.fuente()]) return true
        return false
    }

    /**
     * Indica si el vértice [v] pertenece al grafo; en caso afirmativo retorna true, 
     * de lo contrario retorna false.
     * Precondición: true
     * Tiempo de la Operación: O(1).
    */  
    fun estaElVerticeEnElGrafo(v: Int) : Boolean = v >= 0 && v < this.numDeVertices

}
