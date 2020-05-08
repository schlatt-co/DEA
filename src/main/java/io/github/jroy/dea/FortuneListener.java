package io.github.jroy.dea;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneListener implements Listener {

  private final WebhookManager webhookManager;

  public FortuneListener(WebhookManager webhookManager) {
    this.webhookManager = webhookManager;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockMine(BlockBreakEvent event) {
    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) > 3) {
      String msg = ":warning::warning::warning::warning::warning::warning:\n" +
          ":tools:`" + event.getPlayer().getName() + "` has used a `fortune " +
          item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + "` item!\n" +
          ":warning::warning::warning::warning::warning::warning:";
      webhookManager.pushMessage(this, msg, true);
      event.getPlayer().getInventory().remove(item);
      event.setCancelled(true);
    }
  }
}
