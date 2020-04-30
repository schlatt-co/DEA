package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPayCommand(PlayerCommandPreprocessEvent event) {
    String msg = event.getMessage().replace("/", "");
    if (!msg.startsWith("pay")) {
      return;
    }
    String regex = "pay ([^\\s]+) (\\d*\\.?\\d*)";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(msg);
    if (m.find() && m.groupCount() != 2) {
      WebhookManager.getInstance().sendMessage("Error parsing \"" + msg + "\"", true);
      System.out.println(m.groupCount());
      System.out.println(msg);
      return;
    }
    String sender = event.getPlayer().getName();
    String target = m.group(1);
    String amountString = m.group(2);
    double amount = Double.parseDouble(amountString);
    WebhookManager.getInstance().sendMessage(":dollar:`" + sender + "` paid `" + target + "` `$" + amountString + "`",
        amount >= 5000);
  }
}