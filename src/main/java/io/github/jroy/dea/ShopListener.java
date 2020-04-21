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

  public ShopListener(WebhookClient webhookClient) {
    this.webhookClient = webhookClient;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTransaction(TransactionEvent event) {
    if (event.getTransactionType() == TransactionEvent.TransactionType.SELL) {
      webhookClient.send(event.getClient() + " sold " + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()));
    }
  }
}
