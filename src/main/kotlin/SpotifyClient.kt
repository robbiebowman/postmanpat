package com.robbiebowman.slackapp

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.net.URI


class SpotifyClient() {


    val client: SpotifyApi

    init {
        this.client = SpotifyApi.Builder()
                .setClientId(System.getenv("SPOTIFY_CLIENT_ID"))
                .setClientSecret(System.getenv("SPOTIFY_CLIENT_SECRET"))
                .setRedirectUri(URI.create("http://localhost/spotify_redirect"))
                .build()
    }

    val authorizationCodeUriRequest: AuthorizationCodeUriRequest = client.authorizationCodeUri()
            .scope("playlist-modify-public,playlist-modify-public")
            .show_dialog(true)
            .build()

    fun authorise(code: String) {
        val creds = client
                .authorizationCode(code)
                .build().execute()
        client.accessToken = creds.accessToken
        client.refreshToken= creds.refreshToken
    }

    fun makePlaylist(name: String) {
        val currentUsersProfile = client.currentUsersProfile.build().execute()
        client.createPlaylist(currentUsersProfile.id, name).build().execute()
    }
}