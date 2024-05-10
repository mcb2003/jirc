package club.lowerelements.jirc;

import java.util.*;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.abstractbase.ActorChannelMessageEventBase;
import org.kitteh.irc.client.library.event.channel.ChannelNoticeEvent;

public class Channel extends AbstractChat implements Messageable {
  private org.kitteh.irc.client.library.element.Channel channel;

  public Channel(Network network,
                 org.kitteh.irc.client.library.element.Channel channel) {
    super(network);
    this.channel = channel;
    channel.getClient().getEventManager().registerEventListener(this);
  }

  public org.kitteh.irc.client.library.element.Channel getChannel() {
    return channel;
  }

  @Override
  public String getChatName() {
    return channel.getName();
  }

  @Override
  public MessageReceiver getMessageReceiver() {
    return channel;
  }

  @Handler
  public void onMessage(ActorChannelMessageEventBase<User> e) {
    if (!e.getChannel().equals(channel)) {
      return; // Not our channel
    }
    if (e instanceof ChannelNoticeEvent) {
      messages.addMessage(new NoticeMessage(e.getActor(), e.getMessage()));
    } else {
      messages.addMessage(new PrivMessage(e.getActor(), e.getMessage()));
    }
  }
}
