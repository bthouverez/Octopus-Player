/**
 * Class Song
 */
package Model

class Song(loc: String) {
    private val location = loc
    private val id = CPT++

    init {
        println("init Song")
    }

    companion object {
        var CPT = 0
    }

    override fun toString():String {
        return  "## SONG ###############################\n" +
                "Id : " + this.id + "\n" +
                "Current Id : " + Song.CPT + "\n" +
                "Location : " + this.location + "\n"
    }
    
}