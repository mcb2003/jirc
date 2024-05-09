package club.lowerelements.jirc;

import java.time.Instant;
import org.kitteh.irc.client.library.element.User;

public class NoticeMessage extends Message {
  private User sender;
  private String message;

  public NoticeMessage(Instant ts, User u, String msg) {
    super(ts);
    sender = u;
    message = msg;
  }

  public NoticeMessage(long ts, User u, String msg) {
    super(ts);
    sender = u;
    message = msg;
  }

  public NoticeMessage(User u, String msg) {
    super();
    sender = u;
    message = msg;
  }

  @Override
  public String toString() {
    if (sender == null) {
      // Server notices
      return "[Notice] " + message;
    }
    return "[Notice] " + sender.getNick() + ": " + message;
  }
}
