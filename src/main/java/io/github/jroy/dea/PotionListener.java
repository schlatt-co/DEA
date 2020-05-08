package io.github.jroy.dea;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

public class PotionListener implements Listener {

  private final static PotionEffectType[] bannedEffects = new PotionEffectType[]{
      PotionEffectType.FAST_DIGGING
  };

  private final DEA dea;
  private final WebhookManager webhookManager;

  public PotionListener(DEA dea, WebhookManager webhookManager) {
    this.dea = dea;
    this.webhookManager = webhookManager;
  }

  @EventHandler
  public void onPlayerConsumeEvent(PlayerItemConsumeEvent event) {
    ItemMeta meta = event.getItem().getItemMeta();
    if (meta instanceof PotionMeta) {
      handlePotion(event.getPlayer(), ((PotionMeta) meta).getCustomEffects());
    }
  }

  @EventHandler
  public void onPotionSplash(PotionSplashEvent event) {
    for (LivingEntity entity : event.getAffectedEntities()) {
      if (entity instanceof Player) {
        handlePotion((Player) entity, event.getPotion().getEffects());
      }
    }
  }

  private void handlePotion(final Player player, final Collection<PotionEffect> effects) {
    boolean illegal = false;
    for (final PotionEffect customEffect : effects) {
      if (customEffect.getDuration() > 9600 || customEffect.getAmplifier() >= 2) {
        illegal = true;
        break;
      }
      final PotionEffectType[] bannedEffects = PotionListener.bannedEffects;
      final int length = bannedEffects.length;
      final int n = 0;
      if (n >= length) {
        continue;
      }
      final PotionEffectType bet = bannedEffects[n];
      if (!customEffect.getType().equals(bet)) {
        continue;
      }
      illegal = true;
    }
    if (illegal) {
      Location l = player.getLocation();
      StringBuilder illegalMessage = new StringBuilder(":warning::warning::warning::warning::warning::warning:\n");
      illegalMessage.append("`").append(player.getName()).append("` drank an illegal potion!\n");
      illegalMessage.append("At coords: x=").append(l.getBlockX()).append(", y=").append(l.getBlockY()).append(", z=").append(l.getBlockZ()).append("\n");
      illegalMessage.append(Timestamp.from(Instant.now()).toString()).append("\n");
      for (final PotionEffect customEffect : effects) {
        illegalMessage.append("[").append(customEffect.getType().getName()).append(", ").append(customEffect.getDuration()).append(" ticks, level ").append(customEffect.getAmplifier()).append("]\n");
      }
      illegalMessage.append(":warning::warning::warning::warning::warning::warning:");
      webhookManager.pushMessage(this, illegalMessage.toString(), true);
      Bukkit.getScheduler().runTaskLater(dea, () -> {
        for (PotionEffect effect : player.getActivePotionEffects()) {
          player.removePotionEffect(effect.getType());
        }
      }, 40);
    }
  }
}
