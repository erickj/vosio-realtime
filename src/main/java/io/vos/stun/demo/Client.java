package io.vos.stun.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client {

  /**
   *
   * @param args - Arguments passed from the command line
   **/
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Usage: Client <hostname>");
    }
    String remoteHost = args[0];
    if (remoteHost == null) {
      throw new IllegalArgumentException("Usage: Client <hostname>");
    }

    try {
      DatagramSocket socket = new DatagramSocket();

      byte[] buffer = new byte[1500];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    } catch (IOException e) {
        e.printStackTrace();
    }


  }
}
