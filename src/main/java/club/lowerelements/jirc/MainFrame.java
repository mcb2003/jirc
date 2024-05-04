package club.lowerelements.jirc;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MainFrame extends JFrame {
  private JTree chatsTree;
  private JList messageList;
  private JTextField messageField;

  public MainFrame() {
    setTitle("JIRC");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    setJMenuBar(makeMenuBar());
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

  private JMenuBar makeMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);

    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.setMnemonic(KeyEvent.VK_X);
    exitItem.addActionListener(e -> this.dispose());
    fileMenu.add(exitItem);
    menuBar.add(fileMenu);

    return menuBar;
  }

  private JToolBar makeToolBar() { return new JToolBar("Main Toolbar"); }
}
