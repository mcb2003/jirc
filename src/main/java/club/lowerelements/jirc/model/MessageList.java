package club.lowerelements.jirc;
import java.util.*;
import javax.swing.AbstractListModel;
;

public class MessageList extends AbstractListModel<Message> {
  private List<Message> messages = new ArrayList<>();

  public void addMessage(Message m) {
    messages.add(m);
    int index = messages.size() - 1;
    fireIntervalAdded(this, index, index);
  }

  @Override
  public Message getElementAt(int index) {
    return messages.get(index);
  }

  @Override
  public int getSize() {
    return messages.size();
  }
}
