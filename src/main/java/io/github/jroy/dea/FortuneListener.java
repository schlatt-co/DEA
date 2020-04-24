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

  public FortuneListener(WebhookClient client) {
    this.client = client;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockMine(BlockBreakEvent event) {
    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) > 3) {
      client.send(event.getPlayer().getName() + " has used a fortune " + item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + " item!");
      event.getPlayer().getInventory().remove(item);
      event.setCancelled(true);
    }
  }
}