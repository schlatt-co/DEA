package io.github.jroy.dea;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.ChestShop.Economy.Economy;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ShopListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTransaction(TransactionEvent event) {
    /*
      Notifications:
      All sells > $0
      All buys > $5000
    */
    double value = event.getExactPrice().doubleValue();
    if (value == 0) return;
    String verb = "sold";
    String preposition = "to";
    if (event.getTransactionType() == TransactionEvent.TransactionType.BUY) {
      verb = "bought";
      preposition = "from";
    }

    String message = ":shopping_cart:`" + event.getClient().getName() + "` " + verb + " `" +
        MaterialUtil.getItemList(event.getStock()) + "` @ `" + Economy.formatBalance(event.getExactPrice()) + "`";

    if (event.getTransactionType() == TransactionEvent.TransactionType.BUY && value >= 5000) {
      WebhookManager.getInstance().sendMessage(message, true);
    } else {
      WebhookManager.getInstance().sendMessage(message, value >= 5000);
    }
  }
}
