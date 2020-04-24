package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class BalanceListener implements Listener {

  private final WebhookClient webhookClient;
  private final WebhookClient prioClient;

  public BalanceListener(WebhookClient webhookClient, WebhookClient prioClient) {
    this.webhookClient = webhookClient;
    this.prioClient = prioClient;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBalanceUpdate(UserBalanceUpdateEvent event) {
    BigDecimal diff = event.getNewBalance().subtract(event.getOldBalance());
    String modifier = diff.signum() == -1 ? "decreased" : "increased";
    webhookClient.send(event.getPlayer().getName() + "'s balance " + modifier + " by $" + diff.toString());
    if (diff.abs().doubleValue() >= 5000) {
      prioClient.send(event.getPlayer().getName() + "'s balance " + modifier + " by $" + diff.toString());
    }
  }
}
