package com.quad.core.swing;

public final class RateChooser
{
  private int rate;
  
  public RateChooser(int rate) {
    this.rate = rate;
  }
  
  public int getRate() {
    return rate;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("");
    if (rate == 0) {
      buf = buf.append("Unlimited");
    } else {
      buf = buf.append(rate).append(" fps");
    }
    return buf.toString();
  }
}
