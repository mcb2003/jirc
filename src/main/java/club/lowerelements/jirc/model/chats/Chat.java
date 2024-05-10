package club.lowerelements.jirc;

public interface Chat {
  String getChatName();
  default boolean isChatReadOnly() { return !(this instanceof Messageable); }
  MessageList getMessageList();
  Network getNetwork();
}
