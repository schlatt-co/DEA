package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;

public class WebhookManager {

  private static WebhookManager instance;

  public static WebhookManager getInstance() {
    return instance;
  }


  private final WebhookClient webhookClient;
  private final WebhookClient priorityWebhookClient;

  public WebhookManager(WebhookClient webhookClient, WebhookClient priorityWebhookClient) {
    this.webhookClient = webhookClient;
    this.priorityWebhookClient = priorityWebhookClient;
    instance = this;
  }

  public void sendMessage(String message, Boolean priority) {
    webhookClient.send(message);
    if (priority) priorityWebhookClient.send(message);
  }
}
