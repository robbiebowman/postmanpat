package com.robbiebowman.slackapp

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import spark.Spark

object SpotifyRedirectListener {

    private val authCodes: PublishSubject<String> = PublishSubject.create()

    fun authCodes(port: Int): Observable<String> {
        Spark.port(port)
        Spark.get("/spotify_redirect") { req, res -> authCodes.onNext(req.queryParams("code")) }
        return authCodes
    }

}