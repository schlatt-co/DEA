package io.github.jroy.dea;

import dev.tycho.stonks.api.event.TransactionLogEvent;
import dev.tycho.stonks.model.logging.Transaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class StonksListener implements Listener {

  private final WebhookManager webhookManager;

  public StonksListener(WebhookManager webhookManager) {
    this.webhookManager = webhookManager;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onCompanyTransactionLogEvent(TransactionLogEvent event) {
    Transaction transaction = event.getTransaction();
    if (transaction.amount == 0) return;
    String emoji = transaction.amount > 0 ? ":arrow_up_small:" : ":arrow_down_small:";
    String message = emoji + " `" + event.getPlayer().getName() + "` " + transaction.message + " @ `" + event.getCompany().name + "`#`" + event.getAccount().name
        + "` (`" + event.getAccount().pk + "`), value: `$" + transaction.amount + "`";
    webhookManager.pushCurrencyMessage(this, message,Math.abs(transaction.amount) >= 5000, transaction.amount);

  }
}
