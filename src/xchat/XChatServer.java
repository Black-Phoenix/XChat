package xchat;

import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class XChatServer implements Runnable {

    ServerSocket ss;
    Socket socket;

    public XChatServer() {
        try {
            String port = JOptionPane.showInputDialog("Enter Port Number [Hint:Default port = 8080]");
            if ("".equals(port)) {
                port = "8080";
            }
            String regex = "\\d+";
            while (true) {
                if (port.matches(regex)) {
                    if (Integer.parseInt(port) < 0 || Integer.parseInt(port) > 65535) {
                        port = JOptionPane.showInputDialog("<html> <b>INVALID PORT</b> : Enter Port Number [Hint:Default port = 8080]</html>");
                    } else {
                        break;
                    }
                } else {
                    port = JOptionPane.showInputDialog("<html> <b>INVALID PORT</b> : Enter Port Number [Hint:Default port = 8080]</html>");
                }
            }
            int Port = Integer.parseInt(port);
            this.ss = new ServerSocket(Port);
        } catch (java.lang.NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "Thank you for using XChat");
            System.exit(4);
        } catch (java.lang.NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "<html><br>INVALID PORT</br></html>");
        } catch (IOException ex) {
            boolean flag = true;
            while (flag) {
                JOptionPane.showMessageDialog(null, "Unable to Open Port. [Please try another Port]");
                String port = JOptionPane.showInputDialog("Enter Port Number");
                if (port == null) {
                    System.exit(4);
                }
                int Port = Integer.parseInt(port);
                try {
                    this.ss = new ServerSocket(Port);
                    flag = false;
                } catch (IOException ex1) {

                }
            }
        }
    }

    @Override
    public void run() {
        WaitingClient wclient = new WaitingClient();
        wclient.setLocation(800, 400);
        wclient.setVisible(true);
        try {
            socket = ss.accept();
        } catch (IOException ex) {
            Logger.getLogger(XChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        wclient.dispose();
        XChatScreen xchat = new XChatScreen("Server", socket);
        xchat.setTitle("XChat:Connected");
        xchat.setLocation(600, 400);
        xchat.setResizable(false);
        xchat.setVisible(true);
        new Thread(xchat).start();
    }
}
