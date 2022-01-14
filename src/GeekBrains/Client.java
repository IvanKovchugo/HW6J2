package GeekBrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

        JTextArea in;
        JTextField out;
        BufferedReader reader;
        PrintWriter writer;
        Socket sock;

    public class Reader implements Runnable {
    public void run() {
        String massage;
        try {
            while ((massage = reader.readLine()) !=null) {
                System.out.println("Me " + massage);
                in.append(massage + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

        private void setUpNetworking() {
            try {
                sock = new Socket("127.0.0.1", 5000);
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                System.out.println("Соеденение установлено!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    public void go() {
        JFrame frame = new JFrame("Chat");
        JPanel mainPanel = new JPanel();
        in = new JTextArea(15, 50);
        in.setLineWrap(true);
        in.setWrapStyleWord(true);
        in.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(in);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        out = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.println(getTime() + ": " +  out.getText());
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                out.setText("");
                out.requestFocus();
            }
        });
        mainPanel.add(scrollPane);
        mainPanel.add(out);
        mainPanel.add(sendButton);
        setUpNetworking();

        Thread readerTread = new Thread(new Reader());
        readerTread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(600, 400);
        frame.setVisible(true);


    }
    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss ").format(new Date());
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.go();
    }

    public class SendButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
                try {
                    writer.println(out.getText());
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                out.setText("");
                out.requestFocus();
            }

    }
}
