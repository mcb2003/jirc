package club.lowerelements.jirc;

import java.util.*;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.ISupportParameter;
import org.kitteh.irc.client.library.event.client.ISupportParameterEvent;

public class Network {
  private Client client;
  private NetworkInfo info;
  private String name;

  private Set<Listener> listeners = new HashSet<>();

  public Network(NetworkInfo ni) {
    info = ni;
    name = ni.host;

    client = ni.getClientBuilder().build();
    client.getEventManager().registerEventListener(this);
  }

  public void connect() { client.connect(); }

  @Override
  public String toString() {
    return name;
  }

  public NetworkInfo getNetworkInfo() { return info; }

  public void addNetworkListener(Listener l) { listeners.add(l); }
  public void removeNetworkListener(Listener l) { listeners.remove(l); }

  public void fireNameChangedEvent(String oldName, String newName) {
    var e = new NameChangedEvent(oldName, newName);
    for (var l : listeners) {
      l.nameChanged(e);
    }
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

  public interface Listener extends EventListener {
    public default void nameChanged(NameChangedEvent e) {}
  }
}
