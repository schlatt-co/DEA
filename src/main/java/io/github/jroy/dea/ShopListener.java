package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Economy.Economy;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ShopListener implements Listener {

  private final WebhookClient webhookClient;
  private final WebhookClient prioClient;

  public ShopListener(WebhookClient webhookClient, WebhookClient prioClient) {
    this.webhookClient = webhookClient;
    this.prioClient = prioClient;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTransaction(TransactionEvent event) {
    if (event.getTransactionType() == TransactionEvent.TransactionType.SELL) {
      if (event.getExactPrice().abs().doubleValue() >= 5000) {
        prioClient.send(event.getClient().getName() + " sold " + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()));
      }
      webhookClient.send(event.getClient().getName() + " sold " + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()));
    }
  }
}
