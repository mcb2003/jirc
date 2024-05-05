package club.lowerelements.jirc;

public class NetworkInfo {
  public String host;
  public short port;
  public Boolean tls;
  public String nick, user, realName;

  @Override
  public String toString() {
    String scheme = tls ? "ircs" : "irc";
    return String.format("Server: %s://%s:%d\nUser: %s!%s (%s)", scheme, host,
                         port, nick, user, realName);
  }
}
