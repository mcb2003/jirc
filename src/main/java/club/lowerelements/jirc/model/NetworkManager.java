package club.lowerelements.jirc;

import java.util.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class NetworkManager implements TreeModel {
  private List<Network> networks = new ArrayList<>();

  // Listeners:
  private List<TreeModelListener> listeners = new ArrayList<>();
  private NetworkListener networkListener = new NetworkListener();

  public void addNetwork(Network n) {
    n.addNetworkListener(networkListener);
    networks.add(n);
    n.connect();

    fireTreeNodesInsertedEvent(n, networks.size() - 1);
  }

  public void fireTreeNodesInsertedEvent(Network n, int index) {
    Object[] path = {this};
    int[] indices = {index};
    Object[] children = {n};
    fireTreeNodesInsertedEvent(path, indices, children);
  }

  public void fireTreeNodesInsertedEvent(Object[] path, int[] indices,
                                         Object[] children) {
    var e = new TreeModelEvent(this, path, indices, children);
    for (var l : listeners) {
      l.treeNodesInserted(e);
    }
  }

  public void fireTreeNodesChangedEvent(Network n, int index) {
    Object[] path = {this};
    int[] indices = {index};
    Object[] children = {n};
    fireTreeNodesChangedEvent(path, indices, children);
  }

  public void fireTreeNodesChangedEvent(Object[] path, int[] indices,
                                        Object[] children) {
    var e = new TreeModelEvent(this, path, indices, children);
    for (var l : listeners) {
      l.treeNodesChanged(e);
    }
  }

  @Override
  public void addTreeModelListener(TreeModelListener l) {
    listeners.add(l);
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    listeners.remove(l);
  }

  @Override
  public Object getChild(Object parent, int index) {
    if (parent instanceof NetworkManager mgr) {
      return mgr.networks.get(index);
    }
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    if (parent instanceof NetworkManager mgr) {
      return mgr.networks.size();
    }
    return 0;
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof NetworkManager mgr) {
      return mgr.networks.indexOf((Network)child);
    }
    return -1;
  }

  @Override
  public Object getRoot() {
    return this;
  }

  @Override
  public boolean isLeaf(Object node) {
    return !(node instanceof NetworkManager || node instanceof Network);
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}

  class NetworkListener implements Network.Listener {
    @Override
    public void nameChanged(Network.NameChangedEvent e) {
      Network n = e.getNetwork();
      int index = networks.indexOf(n);
      if (index != -1) {
        fireTreeNodesChangedEvent(n, index);
      }
    }
  }
}
