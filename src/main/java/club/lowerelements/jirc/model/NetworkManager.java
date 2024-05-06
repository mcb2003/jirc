package club.lowerelements.jirc;

import java.util.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class NetworkManager {
  private List<Network> networks = new ArrayList<>();
  private ChatsModel model;

  // Listeners:
  private NetworkListener networkListener = new NetworkListener();

  public NetworkManager() { this.model = new ChatsModel(this); }

  public ChatsModel getModel() { return model; }

  public Network getNetwork(int index) { return networks.get(index); }

  public int getNetworkCount() { return networks.size(); }

  public int getNetworkIndex(Network n) { return networks.indexOf(n); }

  public void addNetwork(Network n) {
    n.addNetworkListener(networkListener);
    networks.add(n);
    model.fireTreeNodesInsertedEvent(n, networks.size() - 1);
    n.connect();
  }

  class NetworkListener implements Network.Listener {
    @Override
    public void nameChanged(Network.NameChangedEvent e) {
      Network n = e.getNetwork();
      int index = networks.indexOf(n);
      if (index != -1) {
        model.fireTreeNodesChangedEvent(n, index);
      }
    }

    @Override
    public void statusChanged(Network.StatusChangedEvent e) {
      Network n = e.getNetwork();
      int index = networks.indexOf(n);
      if (index != -1) {
        model.fireTreeNodesChangedEvent(n, index);
      }
    }
  }
}
