package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class BalanceListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBalanceUpdate(UserBalanceUpdateEvent event) {
    BigDecimal diff = event.getNewBalance().subtract(event.getOldBalance());
    String modifier = diff.signum() == -1 ? "decreased" : "increased";
    String msg = event.getPlayer().getName() + "'s balance " + modifier + " by $" + diff.toString();

    WebhookManager.getInstance().sendMessage(msg, diff.abs().doubleValue() >= 5000);
  }
}
