package ve.usb.libGrafo

import java.util.LinkedList
import java.util.PriorityQueue
import kotlin.math.min
import kotlin.math.abs
import kotlin.math.ceil

import kotlin.Double.Companion.POSITIVE_INFINITY as inf

/**
 * Implementación del algoritmo de Dijkstra para encontrar los
 * caminos de costo mínimo desde un vértice fuente [s] fijo.
 */
public class CCM_Dijkstra(val g: GrafoDirigidoCosto, val s: Int, val prodLimpiezas: Array<ArrayList<Pair<Int,Int>>>) {
    private val V = g.obtenerNumeroDeVertices()
    private var vertices = Array(V) { VerticeDijkstra(it) }
    private var Q: PriorityQueue<VerticeDijkstra> = PriorityQueue(V)

    /**
     * Ejecutar algoritmo de Dijkstra para obtener los caminos de costo mínimo 
     * desde el vértice fuente fijo [s] hasta todos lo demas vértices del grafo [g].
     * Precondición: [s] pertenece al grafo y no existen lados con pesos negativos en el grafo.
     * Postcondición: Para todo vertice v en [g], se tiene que vertices[v].d = δ([s], v) y
     *                [g].π es un árbol de caminos de costo mínimo con raíz [s].
     * Tiempo de la operación: O(|E|log|V|).
     */
    init {
        // Precondición
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice fuente no pertenece al grafo")
        }

        //Inicializar fuente fija
        vertices[s].d = 0.0

        // Inicializar cola de prioridad
        Q.addAll(vertices)

        while (!Q.isEmpty()) {
            var u = Q.poll().v
            for (ady in g.adyacentes(u)) {
                if (ady.costo() < 0.0) {
                    throw RuntimeException("El lado ${ady} tiene costo negativo.")
                }
                relajacion(ady)
            }
        }
    }

    /**
     * Indica si hay un camino desde el vértice [s] hasta el vértice [v]. En caso
     * afirmativo retorna true, de lo contrario retorna false. 
     * Precondición: [v] y [s] pertenecen al grafo [g].
     * Postcondicion: [existeUnCamino] = true si existe un camino desde [s] a [v].
     *                [existeUnCamino] = false si no existe un camino desde [s] a [v].
     * Tiempo de la operación: O(1).
     */
    fun existeUnCamino(v: Int): Boolean {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("El vértice ${v} no pertenece al grafo")
        }

        return inf != vertices[v].d
    }

    /**
     * Retorna la distancia del camino de costo mínimo desde [s] hasta el vértice [v].
     * Precondición: [v] pertenece al grafo.
     * Postcondición: [costo] = δ([s], [v]).
     * Tiempo de la operación: O(1).
     */
    fun costo(v: Int): Double {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("El vértice ${v} no pertenece al grafo.")
        }

        return vertices[v].d
    }

    /**
     * Retorna los arcos del camino de costo mínimo desde el vértice fuente [s] a [v].
     * Precondición: [v] pertenece al grafo.
     * Postcondición: w([obtenerCaminoDeCostoMinimo]) = δ(s, [v]).
     * Tiempo de la operación: O(|E|).
     */
    fun obtenerCaminoDeCostoMinimo(v: Int): Iterable<ArcoCosto> {
        var vertice = vertices[v]
        var camino: LinkedList<ArcoCosto> = LinkedList()

        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vértice ${v} no pertenece al grafo.")
        }
        if (!existeUnCamino(v)) {
            throw RuntimeException("No existe un camino desde el vértice fuente ${s} hasta ${v}.")
        }

        while (vertice.pre != null) {
            var arco =
                    ArcoCosto(
                            vertice.pre!!.v,
                            vertice.v,
                            vertice.d - vertice.pre!!.d
                    )
            camino.push(arco)
            vertice = vertices[vertice.pre!!.v]
        }
        return camino
    }

    /**
     * Relaja el lado [lado] del grafo [g] y implicitamente actualiza la cola [Q].
     * Precondición: [lado] pertenece al grafo.
     * Postcondición: v.d ≤ u.d + w(u, v), dónde u = lado.fuente(), v = lado.sumidero() y
     * w(u,v) = lado.costo().
     * Tiempo de la operacion: O(|V|log|V|).
     */
    private fun relajacion(lado: ArcoCosto) {
        var u = lado.fuente()
        var v = lado.sumidero()

        var costo = w(lado)
        if (vertices[v].d > vertices[u].d + costo) {
            this.Q.remove(vertices[v])
            vertices[v].d = vertices[u].d + costo
            vertices[v].pre = vertices[u]
            this.Q.add(vertices[v])
        }
    }
    
    /**
     * Retorna el costo del lado [l] (el tiempo que le toma a Tom cruzar la
     * calle [l]).
     * Precondición: [l] pertenece al grafo [g].
     * Postcondición: [w] es un entero no negativo.
     * Tiempo de la operación: O(|E|).
     */
    fun w(l: ArcoCosto): Int {
        val idCalle = g.id(l.fuente(), l.sumidero())   // id de la calle
        val t = vertices[l.fuente()].d.toInt()         // Tiempo actual
        val limp = prodLimpiezas[idCalle]              // Cronograma de limpiezas
        val tSinNieve = l.costo().toInt()              // Tiempo en cruzar la calle sin nieve
        val n = limp.size

        limp.sortBy{ it.second } // Ordenar programaciones de limpiezas
    
        // Se busca el tf que indica la última vez que se limpio la calle 
        var i = -1
        for ((ll, item) in limp.withIndex()) {
            if (item.second > i && item.second <= t) i = ll
        }
        
        var t1 = 0
        if (i != -1) t1 = limp[i].second
 
        // Tiempo en cruzar la calle
        var tEnCruzar = min(ceil(((1.0 + (t - t1) / 100.0)) * tSinNieve).toInt(), 100500 * tSinNieve)

        if (i + 1 >= n) return tEnCruzar // No hay más limpiezas programadas

        i++
        while ((t + tEnCruzar >= limp[i].first && t + tEnCruzar <= limp[i].second) || 
                t + tEnCruzar > limp[i].second) {

                tEnCruzar = limp[i].second - t + tSinNieve
                i++

                if (i == n) break
        }

        return tEnCruzar
   }
    
    /**
     * Indica si el vertice [v] pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondición: true.
     * Tiempo de la operación: O(1).
     */
    private fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()
}
