package club.lowerelements.jirc;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder.Server.SecurityType;
import org.kitteh.irc.client.library.util.StsUtil;

public class NetworkInfo {
  public String host;
  public short port;
  public boolean tls;
  public String nick, user, realName;

  public Client.Builder getClientBuilder() {
    var secure = tls ? SecurityType.SECURE : SecurityType.INSECURE;
    var builder = Client.builder();
    builder.nick(nick).user(user).realName(realName);
    builder.server().host(host).port(port, secure);
    builder.management().stsStorageManager(StsUtil.getDefaultStorageManager());
    // builder.listeners().input(System.out::println);
    // builder.listeners().output(System.out::println);
    // builder.listeners().exception(Throwable::printStackTrace);

    return builder;
  }

  @Override
  public String toString() {
    String scheme = tls ? "ircs" : "irc";
    return String.format("Server: %s://%s:%d\nUser: %s!%s (%s)", scheme, host,
                         port, nick, user, realName);
  }
}
