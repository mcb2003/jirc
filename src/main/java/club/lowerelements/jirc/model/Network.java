package club.lowerelements.jirc;

import java.util.*;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.ISupportParameter;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.client.ISupportParameterEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;

public class Network implements MessageLog {
  private Client client;
  private Status status = Status.DISCONNECTED;

  private NetworkInfo info;
  private String name;
  private MessageList serverMessages = new MessageList();

  private Set<Listener> listeners = new HashSet<>();

  public Network(NetworkInfo ni) {
    info = ni;
    name = ni.host;

    client = ni.getClientBuilder().build();
    client.getEventManager().registerEventListener(this);
  }

  public void connect() {
    client.connect();
    setStatus(status.CONNECTING);
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", name, status);
  }

  public NetworkInfo getNetworkInfo() { return info; }

  @Override
  public String getLogName() {
    return name;
  }
  @Override
  public MessageList getMessageList() {
    return serverMessages;
  }

  public Status getStatus() { return status; }

  public void setStatus(Status newStatus) {
    Status oldStatus = status;
    status = newStatus;
    var e = new StatusChangedEvent(oldStatus, newStatus);
    for (var l : listeners) {
      l.statusChanged(e);
    }
  }

  public void addNetworkListener(Listener l) { listeners.add(l); }
  public void removeNetworkListener(Listener l) { listeners.remove(l); }

  public void fireNameChangedEvent(String oldName, String newName) {
    var e = new NameChangedEvent(oldName, newName);
    for (var l : listeners) {
      l.nameChanged(e);
    }
  }

  @Handler
  public void onConnected(ClientConnectionEstablishedEvent e) {
    setStatus(Status.NEGOTIATING);
  }

  @Handler
  public void onNegotiationComplete(ClientNegotiationCompleteEvent e) {
    setStatus(Status.CONNECTED);
  }

  @Handler
  public void onDisconnected(ClientConnectionEndedEvent e) {
    setStatus(Status.DISCONNECTED);
  }

  @Handler
  public void onISupportParam(ISupportParameterEvent e) {
    // If this is a network name, fire the NameChangedEvent
    if (e.getParameter() instanceof ISupportParameter.Network param) {
      String oldName = name;
      name = param.getNetworkName();
      fireNameChangedEvent(oldName, name);
    }
  }

  public class StatusChangedEvent extends EventObject {
    private Status oldStatus, newStatus;

    public StatusChangedEvent(Status oldStatus, Status newStatus) {
      super(Network.this);
      this.oldStatus = oldStatus;
      this.newStatus = newStatus;
    }

    public Network getNetwork() { return Network.this; }
    public Status getOldStatus() { return oldStatus; }
    public Status getNewStatus() { return newStatus; }
  }

  public class NameChangedEvent extends EventObject {
    private String oldName, newName;

    public NameChangedEvent(String oldName, String newName) {
      super(Network.this);
      this.oldName = oldName;
      this.newName = newName;
    }

    public Network getNetwork() { return Network.this; }
    public String getOldName() { return oldName; }
    public String getNewName() { return newName; }
  }

  public enum Status { DISCONNECTED, CONNECTING, NEGOTIATING, CONNECTED }

  public interface Listener extends EventListener {
    public default void nameChanged(NameChangedEvent e) {}
    public default void statusChanged(StatusChangedEvent e) {}
  }
}
