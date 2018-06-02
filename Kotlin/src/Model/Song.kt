/**
 * Class Song
 */
package model

/**
 * keyword 'data' adds methods: equals, hashCode, toString, copy, componentN
 */
data class Song(
        var location: String) {

    val id = CPT++
    val id3v1: ID3v1 = ID3v1(this.location)

    /* Initialisateur/constructeur
    init {
        println("init Song")
    }
    */
    /*
    constructor(l:String) : this(l) {

    }
    */
    // way of defining class attribute
    companion object {
        var CPT = 0
    }
    override fun toString():String {
        return  "## SONG ###############################\n" +
                "Id : " + this.id + "\n" +
                "Location : " + this.location + "\n" +
                "Artist : " + this.id3v1+ "\n"
    }

}
