package controller

import model.Song

fun main(args: Array<String>) {
    println("coucou")

    val s0 = Song("/home/c0")
    val s1 = Song(location="/home/c1")
    val s2 = Song(location="/home/c2")
    val s3 = Song(location="/home/c3")

    var playlist = listOf(s0, s1, s2, s3)

    for(s in playlist)
        println(s)

    println("byebye")
}