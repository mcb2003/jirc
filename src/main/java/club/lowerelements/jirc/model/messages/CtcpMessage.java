package club.lowerelements.jirc;

import java.time.Instant;
import org.kitteh.irc.client.library.element.User;

public class CtcpMessage extends Message {
  private User sender;
  private String command, message;

  public CtcpMessage(Instant ts, User u, String msg) {
    super(ts);
    sender = u;
    int index = msg.indexOf(' ');
    if (index == -1) {
      command = msg;
      message = "";
    } else {
      command = msg.substring(0, index);
      message = msg.substring(index + 1, msg.length()).trim();
    }
    command = command.toUpperCase().intern();
  }

  public CtcpMessage(long ts, User u, String msg) {
    this(Instant.ofEpochMilli(ts), u, msg);
  }

  public CtcpMessage(User u, String msg) { this(Instant.now(), u, msg); }

  @Override
  public String toString() {
    // == is correct, String is interned
    if (command == "ACTION") {
      // /me message
      return String.format("* %s %s", sender.getNick(), message);
    }

    // Otherwise it's something else
    String str = String.format("CTCP %s from %s", command, sender.getNick());
    if (!message.isEmpty()) {
      str += ": ";
      str += message;
    }
    return str;
  }
}
