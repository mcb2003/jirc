package club.lowerelements.jirc;

import java.util.*;
import org.kitteh.irc.client.library.Client;

public class Network implements MessageLog {
  private Client client;
  private Status status = Status.DISCONNECTED;

  private NetworkInfo info;
  private String name;
  private MessageList serverMessages = new MessageList();
  private List<MessageLog> chats = new ArrayList<>();

  private Set<Listener> listeners = new HashSet<>();
  private NetworkEventHandler handler = new NetworkEventHandler(this);

  public Network(NetworkInfo ni) {
    info = ni;
    name = ni.host;

    client = ni.getClientBuilder().build();
    client.getEventManager().registerEventListener(handler);
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

  public String getName() { return name; }

  public void setName(String newName) {
    String oldName = name;
    name = newName;
    fireNameChangedEvent(oldName, newName);
  }

  @Override
  public String getLogName() {
    return name;
  }
  @Override
  public boolean isLogReadOnly() {
    return true;
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

  MessageLog getChat(int index) { return chats.get(index); }

  public int getChatCount() { return chats.size(); }

  public int getChatIndex(MessageLog l) { return chats.indexOf(l); }

  public void addNetworkListener(Listener l) { listeners.add(l); }
  public void removeNetworkListener(Listener l) { listeners.remove(l); }

  void fireNameChangedEvent(String oldName, String newName) {
    var e = new NameChangedEvent(oldName, newName);
    for (var l : listeners) {
      l.nameChanged(e);
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
