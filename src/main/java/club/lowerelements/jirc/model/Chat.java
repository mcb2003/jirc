package club.lowerelements.jirc;

public interface Chat {
  String getChatName();
  default boolean isLogReadOnly() { return false; }
  MessageList getMessageList();
}
