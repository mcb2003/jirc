package club.lowerelements.jirc;

import java.util.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class NetworkManager implements TreeModel {
  private List<Network> networks = new ArrayList<>();
  private List<TreeModelListener> listeners = new ArrayList<>();

  public void addNetwork(Network n) {
    networks.add(n);

    Object[] path = {getRoot()};
    int[] indices = {networks.size() - 1};
    Object[] children = {n};
    var e = new TreeModelEvent(this, path, indices, children);

    for (var l : listeners) {
      l.treeNodesInserted(e);
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
}