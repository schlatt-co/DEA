package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DEA extends JavaPlugin {

  @Override
  public void onEnable() {
    getConfig().addDefault("webhookUrl", "url");
    getConfig().options().copyDefaults(true);
    saveConfig();
    if (Objects.requireNonNull(getConfig().getString("webhookUrl")).equals("url")) {
      getLogger().severe("Invalid webhook url!");
      return;
    }
    getLogger().info("Registering webhook...");
    WebhookClient client = new WebhookClientBuilder(Objects.requireNonNull(getConfig().getString("webhookUrl"))).setDaemon(true).build();
    getServer().getPluginManager().registerEvents(new PotionListener(this, client), this);
    getServer().getPluginManager().registerEvents(new FortuneListener(client), this);
    getServer().getPluginManager().registerEvents(new PayListener(client), this);
    if (getServer().getPluginManager().getPlugin("ChestShop") != null) {
      getServer().getPluginManager().registerEvents(new ShopListener(client), this);
    }
    if (getServer().getPluginManager().getPlugin("Essentials") != null) {
      getServer().getPluginManager().registerEvents(new BalanceListener(client), this);
    }
  }
}
