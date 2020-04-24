package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayListener implements Listener {

  private final WebhookClient webhookClient;
  private final WebhookClient prioClient;

  public PayListener(WebhookClient webhookClient, WebhookClient prioClient) {
    this.webhookClient = webhookClient;
    this.prioClient = prioClient;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPayCommand(PlayerCommandPreprocessEvent event) {
    String msg = event.getMessage();
    if (msg.replace("/", "").startsWith("pay")) {
      webhookClient.send(event.getPlayer().getName() + " executed " + msg);
      if (amountAboveThreshold(msg, 5000)) {
        prioClient.send(event.getPlayer().getName() + " executed " + msg);
      }
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
      prioClient.send("Error parsing \"" + msg + "\"");
      System.out.println(m.groupCount());
    }
    return false;
  }

}
