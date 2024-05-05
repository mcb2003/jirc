package club.lowerelements.jirc;

public class Network {
  private NetworkInfo info;

  public Network(NetworkInfo ni) { info = ni; }

  public NetworkInfo getNetworkInfo() { return info; }

  @Override
  public String toString() {
    return info.host;
  }
}
