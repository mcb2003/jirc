package club.lowerelements.jirc;

import club.lowerelements.jirc.Network.Status;
import java.util.*;
import net.engio.mbassy.bus.common.DeadMessage;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.element.ISupportParameter;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.client.ClientReceiveMotdEvent;
import org.kitteh.irc.client.library.event.client.ISupportParameterEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent;

public class NetworkEventHandler {
  private Network network;

  public NetworkEventHandler(Network network) { this.network = network; }

  @Handler
  public void onConnected(ClientConnectionEstablishedEvent e) {
    network.setStatus(Status.NEGOTIATING);
  }

  @Handler
  public void onNegotiationComplete(ClientNegotiationCompleteEvent e) {
    network.setStatus(Status.CONNECTED);
  }

  @Handler
  public void onDisconnected(ClientConnectionEndedEvent e) {
    network.setStatus(Status.DISCONNECTED);
  }

  @Handler
  public void onISupportParam(ISupportParameterEvent e) {
    // If this is a network name, fire the NameChangedEvent
    if (e.getParameter() instanceof ISupportParameter.Network param) {
      network.setName(param.getNetworkName());
    }
  }

  @Handler
  public void onServerNotice(ServerNoticeEvent e) {
    network.getMessageList().addMessage(new Message(e.getMessage()));
  }

  @Handler
  public void onMotd(ClientReceiveMotdEvent e) {
    Optional<List<String>> motd = e.getMotd();
    motd.ifPresent(msgs -> {
      network.getMessageList().addMessages(msgs.stream().map(Message::new));
    });
  }

  @Handler
  public void onDeadMessage(DeadMessage msg) {
    Object o = msg.getMessage();
    System.err.println("Didn't handle: " + o);
  }
}
