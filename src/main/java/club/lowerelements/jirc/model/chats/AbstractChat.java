package club.lowerelements.jirc;

public abstract class AbstractChat implements Chat {
  protected MessageList messages = new MessageList();

  public AbstractChat() {}

  @Override
  public String toString() {
    return getChatName();
  }

  @Override
  public MessageList getMessageList() {
    return messages;
  }
}
