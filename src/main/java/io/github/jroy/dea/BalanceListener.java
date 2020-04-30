package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
    diff = diff.setScale(2, RoundingMode.DOWN);

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(0);
    df.setGroupingUsed(false);

    String modifier = diff.signum() == -1 ? "decreased" : "increased";
    String emote = diff.signum() == -1 ? "\uD83E\uDD75" : "\uD83E\uDD11";
    String msg = emote + event.getPlayer().getName() + "'s balance " + modifier + " by $" + df.format(diff) + emote;
    webhookClient.send(msg);
    if (diff.abs().doubleValue() >= 5000) {
      prioClient.send(msg);
    }
  }
}
