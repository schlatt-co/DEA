package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;

public class WebhookManager {

  private final WebhookClient webhookClient;
  private final WebhookClient priorityWebhookClient;

  public WebhookManager(WebhookClient webhookClient, WebhookClient priorityWebhookClient) {
    this.webhookClient = webhookClient;
    this.priorityWebhookClient = priorityWebhookClient;
  }

  public void sendMessage(String message, Boolean priority) {
    webhookClient.send(message);
    if (priority) {
      priorityWebhookClient.send(message);
    }
  }
}
