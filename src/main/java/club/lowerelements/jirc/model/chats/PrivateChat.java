package club.lowerelements.jirc;

import java.util.*;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.abstractbase.ActorPrivateMessageEventBase;
import org.kitteh.irc.client.library.event.user.*;

public class PrivateChat extends AbstractChat implements Messageable {
  private org.kitteh.irc.client.library.element.User user;

  public PrivateChat(Network network,
                     org.kitteh.irc.client.library.element.User user) {
    super(network);
    this.user = user;
    network.getClient().getEventManager().registerEventListener(this);
  }

  public org.kitteh.irc.client.library.element.User getUser() { return user; }

  @Override
  public String getChatName() {
    return user.getNick();
  }

  @Override
  public MessageReceiver getMessageReceiver() {
    return user;
  }
}
