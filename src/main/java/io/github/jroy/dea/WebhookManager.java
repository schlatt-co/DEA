package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;

import java.util.HashMap;

public class WebhookManager implements Runnable {

  private final WebhookClient webhookClient;
  private final WebhookClient priorityWebhookClient;

  private final HashMap<Object, HashMap<String, RepeatedMessage>> messageMap;
  private final HashMap<Object, HashMap<String, RepeatedMessage>> priorityMessageMap;


  public WebhookManager(WebhookClient webhookClient, WebhookClient priorityWebhookClient) {
    this.webhookClient = webhookClient;
    this.priorityWebhookClient = priorityWebhookClient;
    messageMap = new HashMap<>();
    priorityMessageMap = new HashMap<>();
  }

  public void pushMessage(Object sender, String message, Boolean priority) {
    pushCurrencyMessage(sender, message, priority, 0);
  }

  public void pushCurrencyMessage(Object sender, String message, Boolean priority, double value) {
    // Update the message into the correct map for priority
    updateMessageInMap(priority ? priorityMessageMap : messageMap, sender, message, value);
  }


  private void sendOldMessages(HashMap<Object, HashMap<String, RepeatedMessage>> map, WebhookClient client) {
    long millis = System.currentTimeMillis();

    for (Object sender : map.keySet()) {
      HashMap<String, RepeatedMessage> messageMap = map.get(sender);
      for (String messageString : messageMap.keySet()) {
        RepeatedMessage meta = messageMap.get(messageString);
        // Check if the message is old, if it is then send it and remove it from the map
        if (meta.isOld(millis)) {
          String newMessageString = messageString;
          if (meta.repeats > 1) {
            newMessageString = "**x5** " + messageString;
            if (meta.getTotalValue() > 0)
              newMessageString += "   **total value `$" + String.valueOf(meta.getTotalValue()) + "`**";
          }
          client.send(newMessageString);
          messageMap.remove(messageString);
        }
      }
    }
  }


  public void updateMessageInMap(HashMap<Object, HashMap<String, RepeatedMessage>> map,
                                 Object sender, String message, double value) {
    // Insert a new entry into this map for the sender object
    if (!map.containsKey(sender)) {
      map.put(sender, new HashMap<>());
    }

    // Get the messages hashmap from the main hashmap
    HashMap<String, RepeatedMessage> messages = map.get(sender);
    long millis = System.currentTimeMillis();
    // Handle new and existing messages differnetly
    if (messages.containsKey(message)) {
      // Update the message time
      RepeatedMessage meta = messages.get(message);
      meta.repeat(millis);
    } else {
      // Create a new message
      messages.put(message, new RepeatedMessage(value, millis));
    }
  }

  @Override
  public void run() {
    sendOldMessages(messageMap, webhookClient);
    sendOldMessages(priorityMessageMap, priorityWebhookClient);
  }


  class RepeatedMessage {
    private int repeats;
    private final double individualValue;
    private long lastRepeatTime;

    public int getRepeats() {
      return repeats;
    }

    public double getTotalValue() {
      return individualValue * (double) repeats;
    }


    public RepeatedMessage(double individualValue, long currentMillis) {
      this.repeats = 1;
      this.individualValue = individualValue;
      this.lastRepeatTime = currentMillis;
    }

    public void repeat(long currentMillis) {
      lastRepeatTime = currentMillis;
      repeats++;
    }

    public boolean isOld(long currentMillis) {
      // if the message is 4 seconds old then it is "old"
      return (currentMillis - lastRepeatTime >= 4000);
    }

  }


}
