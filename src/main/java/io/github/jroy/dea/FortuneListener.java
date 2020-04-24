package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneListener implements Listener {

  private final WebhookClient client;
  private final WebhookClient prioClient;

  public FortuneListener(WebhookClient client, WebhookClient prioClient) {
    this.client = client;
    this.prioClient = prioClient;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockMine(BlockBreakEvent event) {
    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) > 3) {
      String msg = event.getPlayer().getName() + " has used a fortune " + item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + " item!";
      client.send(msg);
      prioClient.send(msg);
      event.getPlayer().getInventory().remove(item);
      event.setCancelled(true);
    }
  }
}
