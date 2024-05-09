package club.lowerelements.jirc;

import java.time.Instant;
import org.kitteh.irc.client.library.element.User;

public class PrivMessage extends Message {
  private User sender;
  private String message;

  public PrivMessage(Instant ts, User u, String msg) {
    super(ts);
    sender = u;
    message = msg;
  }

  public PrivMessage(long ts, User u, String msg) {
    super(ts);
    sender = u;
    message = msg;
  }

  public PrivMessage(User u, String msg) {
    super();
    sender = u;
    message = msg;
  }

  @Override
  public String toString() {
    return sender.getNick() + ": " + message;
  }
}
