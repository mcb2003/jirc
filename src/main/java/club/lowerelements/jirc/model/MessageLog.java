package club.lowerelements.jirc;

public interface MessageLog {
  String getLogName();
  default boolean isLogReadOnly() { return false; }
  MessageList getMessageList();
}
