package model

/**
 *
 * @author Babs ID3v1 metadata management Attributes are array of chars of
 * specific size, always filled with spaces to fit the 128 bytes
 */
class ID3v1 {

    // ID3v1 - mp3 tags
    // last 128 bytes (chars)
    // http://id3.org/ID3v1
    //
    //
    // startByte-endByte (size) -> content
    //
    // 0-2 (3) -> id "TAG"
    // 3-32 (30) -> title
    private var title: String =  "TITLE#########################"
    // 33-62 (30) -> artist
    private var artist: String = "ARTIST########################"
    // 63-92 (30) -> album
    private var album: String =  "ALBUM#########################"
    // 93-96 (4) -> year
    private var year: String = "YEAR"
    // 97-126 (28) -> comment
    private var comment: String = "COMMENT#####################"

    /* ID3v1.1
     @Wikipedia The track number is stored in the last two bytes of the
     comment field. If the comment is 29 or 30 characters long, no track number
     can be stored.

     zero-byte[4]        1 	If a track number is stored, this byte contains a binary 0.
     track[4]            1 	The number of the track on the album, or 0. Invalid, if previous byte is not a binary 0.
     */
    var track: Int = 0
    // 126-127 (1) -> genre, Index in a list of genres, or 255
    var genre: Int = 0

    val genreStr: String = GENRES[this.genre]

    companion object {
        // genre is an id linking to following array (80)
        private val GENRES = arrayOf("Blues", "Classic rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-hop", "Jazz", "Metal", "New age", "Oldies", "Autre", "Pop", "RnB", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death metal", "Pranks", "Soundtrack", "Euro techno", "Ambient", "Trip hop", "Vocal", "Jazz-funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Video game", "Sample", "Gospel", "Noise", "Rock alternative", "Bass", "Soul", "Punk", "Space", "Meditative", "Pop instrumental", "Rock instrumental", "Ethnic", "Gothic", "Dark wave", "Techno-industrial", "Electronic", "Pop folk", "Eurodance", "Dream", "Southern rock", "Comedy", "Cult", "Gangsta", "Hit-parade (top 40)", "Christian rap", "Pop/Funk", "Jungle", "Amerindian2", "Cabaret", "New wave", "Psychedelic", "Rave", "Showtunes", "Teaser", "Lo-fi", "Tribal", "Acid punk", "Acid jazz", "Polka", "Vintage", "Theater", "Rock 'n' Roll", "Hard rock")
    }


    fun readFile(args: Array<String>) {
        val inputStream: InputStream = File("kotlination.txt").inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
        lineList.forEach{println(">  " + it)}
    }



    /**
     * Open and extract given mp3 file tags
     * @param uri path to mp3 file
     */
    constructor(uri: String) {



        //val file = File(uri)
        //val raf = RandomAccessFile(file, "r")
        val array = ByteArray(128)
        val nbBytes = 128
        // getting to end of file
        //raf.seek(file.length() - nbBytes)
        // reading last 128 bytes
        //raf.read(array, 0, nbBytes)

        var tags = ""
        for (ii in 0 until nbBytes) {
            tags += array[ii].toChar()
        }

        /*
        var str = tags.substring(3, 33).trim()
        this.title = str.toCharArray()
        str = tags.substring(33, 63).trim()
        this.artist = str.toCharArray()
        str = tags.substring(63, 93).trim()
        this.album = str.toCharArray()
        str = tags.substring(93, 97)
        this.year = str.toCharArray()

        // ID3v1   : comment = 30 bytes
        // ID3v1.1 : if comment <= 28, byte 28 is 0 binary, byte 29 is track number
        val isTrackSet = tags.charAt(125)
        if (isTrackSet != 0) {
            str = tags.substring(97, 127).trim()
            this.comment = str.toCharArray()
            this.track = 0
        } else {
            str = tags.substring(97, 125).trim()
            this.comment = str.toCharArray()
            this.track = tags.charAt(126)
        }
        this.genre = tags.charAt(127)
        */
    }


    // to check, may be not really powerful
    fun write(uri: String): Boolean {
        var nTags = "TAG"

        var cTag = this.title
        //cTag = fillWithSpaces(cTag, 30)
        nTags += cTag

        cTag = this.artist
        //cTag = fillWithSpaces(cTag, 30)
        nTags += cTag

        cTag = this.album
        //cTag = fillWithSpaces(cTag, 30)
        nTags += cTag

        cTag = this.year
        //cTag = fillWithSpaces(cTag, 4)
        nTags += cTag

        cTag = this.comment
        //cTag = fillWithSpaces(cTag, 28)
        nTags += cTag

        nTags += 0.toChar()

        cTag = this.track.toChar() + ""
        nTags += cTag

        cTag = this.genre.toChar() + ""
        nTags += cTag

        println(nTags)
        //val file = File(uri)
        //val raf = RandomAccessFile(file, "rw")
        val array = ByteArray(128)
        val nbBytes = 128
        //raf.seek(file.length() - nbBytes)
        //raf.writeBytes(nTags)
        return true
    }

    /*
    private fun fillWithSpaces(s: String, count: Int): String {
        var s = s
        for (ii in s.length() until count) {
            s += ' '
        }
        return s
    }
    */

    override fun toString(): String {
        var ti = title
        //ti = fillWithSpaces(ti, 30)
        var ar = artist
        //ar = fillWithSpaces(ar, 30)
        var al = album
        //al = fillWithSpaces(al, 30)
        var ye = year
        //ye = fillWithSpaces(ye, 4)
        var co = comment
        //co = fillWithSpaces(co, 28)
        return ("Tags{\n"
                + "   title    = " + ti + " (" + ti.length + ")\n"
                + "   artist   = " + ar + " (" + ar.length + ")\n"
                + "   album    = " + al + " (" + al.length + ")\n"
                + "   year     = " + ye + " (" + ye.length + ")\n"
                + "   comment  = " + co + " (" + co.length + ")\n"
                + "   track    = " + track + "\n"
                + "   genre    = " + GENRES[genre] + "\n }")
    }
}
