package club.lowerelements.jirc;

import java.util.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class ChatsModel implements TreeModel {
  private Set<TreeModelListener> listeners = new HashSet<>();
  private NetworkManager root;

  public ChatsModel(NetworkManager root) { this.root = root; }

  void fireTreeNodesInsertedEvent(Network n, int index) {
    Object[] path = {root};
    int[] indices = {index};
    Object[] children = {n};
    fireTreeNodesInsertedEvent(path, indices, children);
  }

  void fireTreeNodesInsertedEvent(Network n, MessageLog l, int index) {
    Object[] path = {root, n};
    int[] indices = {index};
    Object[] children = {l};
    fireTreeNodesInsertedEvent(path, indices, children);
  }

  void fireTreeNodesInsertedEvent(Object[] path, int[] indices,
                                  Object[] children) {
    var e = new TreeModelEvent(this, path, indices, children);
    for (var l : listeners) {
      l.treeNodesInserted(e);
    }
  }

  void fireTreeNodesChangedEvent(Network n, int index) {
    Object[] path = {root};
    int[] indices = {index};
    Object[] children = {n};
    fireTreeNodesChangedEvent(path, indices, children);
  }

  void fireTreeNodesChangedEvent(Object[] path, int[] indices,
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
      return mgr.getNetwork(index);
    } else if (parent instanceof Network net) {
      return net.getChat(index);
    }
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    if (parent instanceof NetworkManager mgr) {
      return mgr.getNetworkCount();
    } else if (parent instanceof Network net) {
      return net.getChatCount();
    }
    return 0;
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof NetworkManager mgr) {
      return mgr.getNetworkIndex((Network)child);
    } else if (parent instanceof Network net) {
      return net.getChatIndex((MessageLog)child);
    }
    return -1;
  }

  @Override
  public Object getRoot() {
    return root;
  }

  @Override
  public boolean isLeaf(Object node) {
    return !(node instanceof NetworkManager || node instanceof Network);
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}
}
