package io.github.jroy.dea;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BalanceListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBalanceUpdate(UserBalanceUpdateEvent event) {
    BigDecimal diff = event.getNewBalance().subtract(event.getOldBalance());
    diff = diff.setScale(2, RoundingMode.DOWN);

    // Don't show 0 value transactions
    if (diff.doubleValue() == 0) return;

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(0);
    df.setGroupingUsed(false);

    String modifier = diff.signum() == -1 ? "decreased" : "increased";
    String emote = diff.signum() == -1 ? ":small_red_triangle_down:" : ":small_red_triangle:";
    String msg = emote + "`" + event.getPlayer().getName() + "`'s balance " + modifier + " by `$" + df.format(diff) + "`";
    WebhookManager.getInstance().sendMessage(msg, diff.abs().doubleValue() >= 5000);
  }
}
