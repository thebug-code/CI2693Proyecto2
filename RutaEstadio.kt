import ve.usb.libGrafo.*

import java.io.File

// Main
fun main(args: Array<String>) {
    val file = File(args[0])
    val lines: List<String> = file.readLines()
    
    val s = lines[lines.size - 1].split(" ")[0].toInt()   // Intersección de la casa de Tom
    val obj = lines[lines.size - 1].split(" ")[1].toInt() // Intersección del estadio

    // Construir grafo 
    val mapa = GrafoDirigidoCosto(args[0])

    // Obtener programaciones de limpiezas de las calles
    val progradLimpiezas = programacionesLimpiezas(lines)
    
    val dijkstra = CCM_Dijkstra(mapa, s - 1, progradLimpiezas)

    // Mostras resultados
    print("${s} ")
    dijkstra.obtenerCaminoDeCostoMinimo(obj - 1).forEach {
        print("${it.sumidero() + 1} ")
    }
    println()
    println(dijkstra.costo(obj - 1).toInt())
}

/**
 * Retorna un arreglo con las programaciones de las limpiezas de las calles de CaracasSur,
 * dado un string con tales programaciones.
 * Precondición: Cada programación de limpieza en [lines] corresponde a tres números enteros
 * separados por un espacio en blanco, el primero es el id de la calle, el segundo es el tiempo 
 * de inicio de la limpieza y el tercero es el tiempo de finalización.
 * Postcondición: la i-énesima posición de arreglo contiene un ArrayList de pares con 
 * la programación de limpieza de la calle con id i.
 */
fun programacionesLimpiezas(lines: List<String>): Array<ArrayList<Pair<Int,Int>>> {
    val n = lines[0].split(" ")[1].toInt()
    val progradLimpiezas: Array<ArrayList<Pair<Int,Int>>> = Array(n) { arrayListOf() }

    var ki: List<String>
    for(i in n + 2 until lines.size - 1) {
        ki = lines[i].split(" ") 
        progradLimpiezas[ki[0].toInt() - 1].add(Pair(ki[1].toInt(), ki[2].toInt())) // (ti , tf)
    }

    return progradLimpiezas
}
