package club.lowerelements.jirc;

import org.kitteh.irc.client.library.element.MessageReceiver;

public interface Messageable {
    MessageReceiver getMessageReceiver();
}
