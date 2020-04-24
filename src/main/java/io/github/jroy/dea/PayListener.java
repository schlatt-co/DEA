package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PayListener implements Listener {

  private final WebhookClient webhookClient;

  public PayListener(WebhookClient webhookClient) {
    this.webhookClient = webhookClient;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPayCommand(PlayerCommandPreprocessEvent event) {
    if (event.getMessage().replace("/", "").startsWith("pay")) {
      webhookClient.send(event.getPlayer().getName() + " executed /" + event.getMessage());
    }
  }
}
