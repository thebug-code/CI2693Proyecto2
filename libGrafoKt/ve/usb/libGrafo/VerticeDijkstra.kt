package ve.usb.libGrafo

public class VerticeDijkstra(var v: Int, var pre: VerticeDijkstra? = null, var d: Double = Double.POSITIVE_INFINITY) : Comparable<VerticeDijkstra> {
    override fun compareTo(other: VerticeDijkstra) : Int {
        when {
            this.d > other.d -> return 1
            this.d < other.d -> return -1
            else -> return 0
        }
    }
    
    override fun equals(other: Any?) : Boolean {
        if (other == null || other !is VerticeDijkstra) return false
        return this.d == other.d && this.v == other.v
    }

    override fun toString() : String = "($v, $d, ${pre?.v})"
}
