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
    String transactionType = "bought";
    String msg = "\uD83D\uDED2" + event.getClient().getName() + " %s " + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()) + " from " + event.getSign().getLine(0) + "\uD83D\uDED2";
    if (event.getTransactionType() == TransactionEvent.TransactionType.SELL) {
      transactionType = "sold";
      // Only log transactions < $5000 if they are a sell sign
      webhookClient.send(String.format(msg, transactionType));
    }
    if (event.getExactPrice().abs().doubleValue() >= 5000) {
      prioClient.send(String.format(msg, transactionType));
    }
  }
}
