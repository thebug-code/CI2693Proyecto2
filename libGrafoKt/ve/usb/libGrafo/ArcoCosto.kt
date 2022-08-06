package ve.usb.libGrafo

public class ArcoCosto(val x: Int, val y: Int, val costo: Double) : Arco(x, y) {
    
    /**
     * Retorna el costo asociado al arco.
     * Tiempo de la operación: O(1)
    */
    fun costo() : Double = costo

    /**
     * Retorna el contenido del arco como un string.
     * Tiempo de la operación: O(1).
    */
    override fun toString() : String = "($x, $y, $costo)"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ArcoCosto) return false
        return this.x == other.x && this.y == other.y
    }
    
} 
