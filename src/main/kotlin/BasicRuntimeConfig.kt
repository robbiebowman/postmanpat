package com.robbiebowman.slackapp

import com.hubspot.slack.client.SlackClient
import com.hubspot.slack.client.SlackClientFactory
import com.hubspot.slack.client.SlackClientRuntimeConfig

object BasicRuntimeConfig {

    val client: SlackClient
        get() = SlackClientFactory.DefaultSlackClientFactory.defaultFactory().build(get())

    fun get(): SlackClientRuntimeConfig {
        return SlackClientRuntimeConfig.builder()
                .setTokenSupplier { System.getenv("SLACK_BOT_AUTH_TOKEN") ?: "" }
                .build()
    }
}