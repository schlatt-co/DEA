package io.github.jroy.dea;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneListener implements Listener {


  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockMine(BlockBreakEvent event) {
    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) > 3) {
      StringBuilder msg = new StringBuilder(":warning::warning::warning::warning::warning::warning:\n")
          .append(":tools:`").append(event.getPlayer().getName()).append("` has used a `fortune ")
          .append(item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS)).append("` item!\n")
          .append(":warning::warning::warning::warning::warning::warning:");
      WebhookManager.getInstance().sendMessage(msg.toString(), true);
      event.getPlayer().getInventory().remove(item);
      event.setCancelled(true);
    }
  }
}
