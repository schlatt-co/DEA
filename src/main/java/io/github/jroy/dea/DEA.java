package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DEA extends JavaPlugin {

  @Override
  public void onEnable() {
    getConfig().addDefault("webhookUrl", "url");
    getConfig().addDefault("webhookUrlPrio", "url");
    getConfig().options().copyDefaults(true);
    saveConfig();
    if (Objects.requireNonNull(getConfig().getString("webhookUrl")).equals("url")
        || Objects.requireNonNull(getConfig().getString("webhookUrlPrio")).equals("url")) {
      getLogger().severe("Invalid webhook url(s)!");
      return;
    }
    getLogger().info("Registering webhook...");
    WebhookClient client = new WebhookClientBuilder(Objects.requireNonNull(getConfig().getString("webhookUrl"))).setDaemon(true).build();
    WebhookClient prioClient = new WebhookClientBuilder(Objects.requireNonNull(getConfig().getString("webhookUrlPrio"))).setDaemon(true).build();

    WebhookManager webhookManager = new WebhookManager(client, prioClient);

    getServer().getPluginManager().registerEvents(new PotionListener(this, webhookManager), this);
    getServer().getPluginManager().registerEvents(new FortuneListener(webhookManager), this);
    getServer().getPluginManager().registerEvents(new PayListener(webhookManager), this);
    if (getServer().getPluginManager().getPlugin("Stonks") != null) {
      getServer().getPluginManager().registerEvents(new StonksListener(webhookManager), this);
    }
    if (getServer().getPluginManager().getPlugin("ChestShop") != null) {
      getServer().getPluginManager().registerEvents(new ShopListener(webhookManager), this);
    }
    if (getServer().getPluginManager().getPlugin("Essentials") != null) {
      getServer().getPluginManager().registerEvents(new BalanceListener(webhookManager), this);
    }

    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, webhookManager, 0L, 20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats


  }
}
