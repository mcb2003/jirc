package club.lowerelements.jirc;

public class Message {
  private String contents;

  public Message(String contents) { this.contents = contents; }

  @Override
  public String toString() {
    return contents;
  }
}
