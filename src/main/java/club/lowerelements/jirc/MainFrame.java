package club.lowerelements.jirc;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MainFrame extends JFrame {
  private JTree chatsTree;
  private JList messageList;
  private JTextField messageField;

  MainFrame(Controller controller) {
    addWindowListener(controller.getWindowListener());

    setTitle("JIRC");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    setJMenuBar(makeMenuBar(controller));
    add(makeToolBar(), BorderLayout.NORTH);

    chatsTree = new JTree();
    chatsTree.getAccessibleContext().setAccessibleName("Channels");
    chatsTree.setRootVisible(false);

    var messagePanel = new JPanel(new BorderLayout());

    messageList = new JList();
    messageList.getAccessibleContext().setAccessibleName("Messages");
    messagePanel.add(new JScrollPane(messageList), BorderLayout.CENTER);

    messageField = new JTextField();
    messageField.getAccessibleContext().setAccessibleName("Message");
    messagePanel.add(messageField, BorderLayout.PAGE_END);

    var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   new JScrollPane(chatsTree), messagePanel);
    splitPane.setResizeWeight(0.25);
    splitPane.setDividerLocation(0.25);
    add(splitPane, BorderLayout.CENTER);
  }

  private JMenuBar makeMenuBar(Controller controller) {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    JMenuItem item;

    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);

    item = new JMenuItem(controller.getNewWindowAction());
    menu.add(item);
    item = new JMenuItem(controller.getExitAction());
    menu.add(item);

    menuBar.add(menu);

    return menuBar;
  }

  private JToolBar makeToolBar() { return new JToolBar("Main Toolbar"); }
}
