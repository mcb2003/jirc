package club.lowerelements.jirc;

import java.time.Instant;

public abstract class Message {
  private Instant ts;

  public Message(Instant ts) { this.ts = ts; }

  public Message(long ts) { this(Instant.ofEpochMilli(ts)); }

  public Message() { this(Instant.now()); }

  @Override public abstract String toString();
}
