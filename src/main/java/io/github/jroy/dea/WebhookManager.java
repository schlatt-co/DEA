package io.github.jroy.dea;

import club.minnced.discord.webhook.WebhookClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class WebhookManager implements Runnable {

  private final WebhookClient webhookClient;
  private final WebhookClient priorityWebhookClient;

  private final HashMap<Object, LinkedHashMap<String, RepeatedMessage>> messageMap;
  private final HashMap<Object, LinkedHashMap<String, RepeatedMessage>> priorityMessageMap;


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


  private void sendOldMessages(HashMap<Object, LinkedHashMap<String, RepeatedMessage>> map, boolean forcePriority) {
    long millis = System.currentTimeMillis();
    for (Object sender : map.keySet()) {
      HashMap<String, RepeatedMessage> messageMap = map.get(sender);
      Iterator<String> i = messageMap.keySet().iterator();
      while(i.hasNext()) {
        String messageString = i.next();
        RepeatedMessage meta = messageMap.get(messageString);
        // Check if the message is old, if it is then send it and remove it from the map
        if (meta.isOld(millis)) {
          String newMessageString = messageString;
          double value = meta.getTotalValue();
          // If the message has been repeated add the repeat text
          if (meta.repeats > 1) {
            newMessageString = "**x" + meta.getRepeats() + "**  " + messageString;
            if (Math.abs(value) > 0)
              newMessageString += "   **total value `$" + value + "`**";
          }
          webhookClient.send(newMessageString);
          if (forcePriority || Math.abs(value) >= 5000) {
            priorityWebhookClient.send(newMessageString);
          }
          i.remove();
        }
      }
    }
  }


  public void updateMessageInMap(HashMap<Object, LinkedHashMap<String, RepeatedMessage>> map,
                                 Object sender, String message, double value) {
    // Insert a new entry into this map for the sender object
    if (!map.containsKey(sender)) {
      map.put(sender, new LinkedHashMap<>());
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
    sendOldMessages(messageMap, false);
    sendOldMessages(priorityMessageMap, true);
  }


  static class RepeatedMessage {
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
