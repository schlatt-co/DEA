package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Economy.Economy;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ShopListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTransaction(TransactionEvent event) {
    String transactionType = " bought ";
    if (event.getTransactionType() == TransactionEvent.TransactionType.SELL) {
      transactionType = " sold ";
      // Only log transactions < $5000 if they are a sell sign
      WebhookManager.getInstance().sendMessage(event.getClient().getName() + transactionType + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()) + " from " + event.getSign().getLine(0), false);
    }
    if (event.getExactPrice().abs().doubleValue() >= 5000) {
      WebhookManager.getInstance().sendMessage(event.getClient().getName() + transactionType + MaterialUtil.getItemList(event.getStock()) + " @ " + Economy.formatBalance(event.getExactPrice()) + " from " + event.getSign().getLine(0), true);
    }
  }
}
