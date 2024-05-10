package club.lowerelements.jirc;

import club.lowerelements.jirc.Network.Status;
import java.time.Instant;
import java.util.*;
import net.engio.mbassy.bus.common.DeadMessage;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.element.*;
import org.kitteh.irc.client.library.event.capabilities.*;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.client.ClientReceiveMotdEvent;
import org.kitteh.irc.client.library.event.client.ISupportParameterEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;
import org.kitteh.irc.client.library.event.helper.CapabilityNegotiationRequestEvent;
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent;

public class NetworkEventHandler {
  private Network network;

  public NetworkEventHandler(Network network) { this.network = network; }

  @Handler
  public void onConnected(ClientConnectionEstablishedEvent e) {
    network.setStatus(Status.NEGOTIATING);
  }

  @Handler
  public void supportedCapabilities(CapabilitiesSupportedListEvent e) {
      requestCapabilities(e, e.getSupportedCapabilities());
  }

  @Handler
  public void newCapabilities(CapabilitiesNewSupportedEvent e) {
      requestCapabilities(e, e.getNewCapabilities());
  }

  public void requestCapabilities(CapabilityNegotiationRequestEvent e, List<CapabilityState> supported) {
      // Request the caps we want, based on what's supported
      String[] wanted = {"echo-message", "invite-notify"};
      for (var cap : supported) {
          String name = cap.getName();
          if (Arrays.stream(wanted).anyMatch(name::equals)) {
              e.addRequest(name);
          }
      }
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
    network.getMessageList().addMessage(
        new NoticeMessage(null, e.getMessage()));
  }

  @Handler
  public void onMotd(ClientReceiveMotdEvent e) {
    Instant ts = Instant.now();
    Optional<List<String>> motd = e.getMotd();
    motd.ifPresent(msgs -> {
      network.getMessageList().addMessages(
          msgs.stream().map(m -> new GenericMessage(ts, m)));
    });
  }

  @Handler
  public void onJoin(ChannelJoinEvent e) {
    var channel = network.getOrAddChat(e.getChannel());
    String joinMessage = String.format("%s joined %s", e.getUser().getNick(),
                                       e.getChannel().getName());
    channel.getMessageList().addMessage(new GenericMessage(joinMessage));
  }

  @Handler
  public void onDeadMessage(DeadMessage msg) {
    Object o = msg.getMessage();
    System.err.println("Didn't handle: " + o);
  }
}
