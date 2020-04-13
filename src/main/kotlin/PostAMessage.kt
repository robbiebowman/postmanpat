package com.robbiebowman.slackapp

import com.hubspot.horizon.shaded.org.jboss.netty.channel.ChannelEvent
import com.hubspot.horizon.shaded.org.jboss.netty.channel.DownstreamMessageEvent
import com.hubspot.slack.client.SlackClient
import com.hubspot.slack.client.methods.params.channels.ChannelsFilter
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse
import org.slf4j.LoggerFactory
import org.jsoup.Jsoup


object PostAMessage {
    private val LOG = LoggerFactory.getLogger(PostAMessage::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val client = BasicRuntimeConfig.client

        val response = messageChannel(client, "bot_test", randomWikiFact())
        LOG.info("Got: {}", response)
    }

    fun randomWikiFact(): String {
        val randomArticle = Jsoup.connect("https://en.wikipedia.org/wiki/Special:Random").get()

        val headline = randomArticle.select("#firstHeading")
        val paragraphs = randomArticle.select(".mw-parser-output > p")

        println(headline.text())
        return paragraphs.first { !it.text().isBlank() }.text()
                .replace(Regex("\\[[\\d\\w]+]"), "")
    }

    fun messageChannel(slackClient: SlackClient, channelToPostIn: String, message: String): ChatPostMessageResponse {
        val postResult = slackClient.postMessage(
                ChatPostMessageParams.builder()
                        .setText(message)
                        .setChannelId(channelToPostIn)
                        .build()
        ).join()

        return postResult.unwrapOrElseThrow() // release failure here as a RTE
    }
}