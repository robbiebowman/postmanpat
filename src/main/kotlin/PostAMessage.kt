package com.robbiebowman.slackapp

import com.hubspot.algebra.Result
import com.hubspot.slack.client.SlackClient
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams
import com.hubspot.slack.client.models.response.SlackError
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PostAMessage {
    private val LOG = LoggerFactory.getLogger(PostAMessage::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        println(System.getenv("SLACK_BOT_AUTH_TOKEN"))

        val client = BasicRuntimeConfig.client

        val response = messageChannel("bot_test", client)
        LOG.info("Got: {}", response)
    }

    fun messageChannel(channelToPostIn: String, slackClient: SlackClient): ChatPostMessageResponse {
        val postResult = slackClient.postMessage(
                ChatPostMessageParams.builder()
                        .setText("Hello, world!")
                        .setChannelId(channelToPostIn)
                        .build()
        ).join()

        return postResult.unwrapOrElseThrow() // release failure here as a RTE
    }
}