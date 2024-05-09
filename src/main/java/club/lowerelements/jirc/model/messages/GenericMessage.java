package club.lowerelements.jirc;

import java.time.Instant;

public class GenericMessage extends Message {
  private String message;

  public GenericMessage(Instant ts, String msg) {
    super(ts);
    message = msg;
  }

  public GenericMessage(long ts, String msg) {
    super(ts);
    message = msg;
  }

  public GenericMessage(String msg) {
    super();
    message = msg;
  }

  @Override
  public String toString() {
    return message;
  }
}
