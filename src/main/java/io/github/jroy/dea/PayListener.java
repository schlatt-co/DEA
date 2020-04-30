package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayListener implements Listener {


  @EventHandler(priority = EventPriority.MONITOR)
  public void onPayCommand(PlayerCommandPreprocessEvent event) {
    String msg = event.getMessage();
    if (msg.replace("/", "").startsWith("pay")) {
      WebhookManager.getInstance().sendMessage(event.getPlayer().getName() + " executed " + msg,
          amountAboveThreshold(msg, 5000));
    }
  }

  private boolean amountAboveThreshold(String msg, double threshold) {
    String regex = "/pay ([^\\s]+) (\\d*\\.?\\d*)";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(msg);
    if (m.find() && m.groupCount() >= 2) {
      String name = m.group(1);
      double amount = Double.parseDouble(m.group(2));
      return (amount >= threshold);
    } else {
      WebhookManager.getInstance().sendMessage("Error parsing \"" + msg + "\"", true);
      System.out.println(m.groupCount());
    }
    return false;
  }

}
