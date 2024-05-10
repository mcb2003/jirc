package club.lowerelements.jirc;

public abstract class AbstractChat implements Chat {
  private Network network;
  protected MessageList messages = new MessageList();

  public AbstractChat(Network network) { this.network = network; }

  @Override
  public String toString() {
    return getChatName();
  }

  @Override
  public MessageList getMessageList() {
    return messages;
  }

  @Override
  public Network getNetwork() {
    return network;
  }
}
