package com.robbiebowman.slackapp

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import io.reactivex.rxjava3.subjects.PublishSubject
import spark.Spark.*


object SlackBot {

    const val botUsername = "postman_pat"

    val newMessages: PublishSubject<Pair<SlackMessagePosted, SlackSession>> = PublishSubject.create<Pair<SlackMessagePosted, SlackSession>>()

    val authCodes = SpotifyRedirectListener.authCodes(80)

    @JvmStatic
    fun main(args: Array<String>) {

        val session = SlackSessionFactory.createWebSocketSlackSession(System.getenv("SLACK_BOT_AUTH_TOKEN"))
        session.connect()
        val channel = session.findChannelByName("bot_test") //make sure bot is a member of the channel.
        newMessages
                .filter { it.first.messageContent.contains(Regex("^\\d+")) }
                .filter {
                    val msg = it.first.messageContent
                    msg.contains(Regex("spotify:\\w+:")) || msg.contains("https://open.spotify.com")
                }
                .subscribe { it.second.sendMessage(it.first.channel, "I hear ya") }
        session.addMessagePostedListener { msg, sesh -> if (msg.sender.userName != botUsername) newMessages.onNext(Pair(msg, sesh)) }
        val spotifyClient = SpotifyClient()
        authCodes.subscribe{
            spotifyClient.authorise(it)
            spotifyClient.makePlaylist("test_playlist")
        }
    }
}