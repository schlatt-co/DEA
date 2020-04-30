package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
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

    new WebhookManager(client, prioClient);

    getServer().getPluginManager().registerEvents(new PotionListener(this), this);
    getServer().getPluginManager().registerEvents(new FortuneListener(), this);
    getServer().getPluginManager().registerEvents(new PayListener(), this);
    if (getServer().getPluginManager().getPlugin("ChestShop") != null) {
      getServer().getPluginManager().registerEvents(new ShopListener(), this);
    }
    if (getServer().getPluginManager().getPlugin("Essentials") != null) {
      getServer().getPluginManager().registerEvents(new BalanceListener(), this);
    }
  }
}
