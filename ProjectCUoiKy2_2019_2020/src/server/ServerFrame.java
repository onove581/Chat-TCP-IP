package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ServerFrame extends JFrame implements Runnable{

	private JPanel contentPane;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    ServerThread serverThread;
    ServerSocket serverSocket;
    JButton btStart, btStop;
    JTextArea taInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 597, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbStateServer = new JLabel("Server Status");
		lbStateServer.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbStateServer.setBounds(141, 11, 269, 58);
		contentPane.add(lbStateServer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 79, 563, 280);
		contentPane.add(scrollPane);
		
		 taInfo = new JTextArea();
		scrollPane.setViewportView(taInfo);
		
		 btStart = new JButton("Start");
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  btStartEvent(e);
			}
		});
		btStart.setBounds(79, 370, 160, 39);
		contentPane.add(btStart);
		
		 btStop = new JButton("Exit");
		btStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btStopEvent(e);
			}
		});
		btStop.setBounds(277, 370, 160, 39);
		contentPane.add(btStop);
		
	}
	public void appendMessage(String message) {
        taInfo.append(message);
        taInfo.setCaretPosition(taInfo.getText().length() - 1);
    }
    
    private void startServer() {
        try {
            serverSocket = new ServerSocket(9999); 
            appendMessage("["+sdf.format(new Date())+"] Server is running and ready to server any client...");
            appendMessage("\n["+sdf.format(new Date())+"] Now there's no one is connecting to server\n");
            
            while(true) {
                Socket socketOfServer = serverSocket.accept();      
                serverThread = new ServerThread(socketOfServer);
                serverThread.taServer = this.taInfo;
                serverThread.start();
            }
            
        } catch (java.net.SocketException e) {
            System.out.println("Server is closed");
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Cong nay duoc su sung");
            System.out.println("Server dang close");
            JOptionPane.showMessageDialog(this, "Cong nay dang duoc su dung", "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            System.exit(0);
        }
    }

    private void btStartEvent(ActionEvent ae) {
        Connection conn = new UserDatabase().connect();
        if(conn == null) {
            JOptionPane.showMessageDialog(this, "Please open MySQL first", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new Thread(this).start();
        
        
    }
    
    private void btStopEvent(ActionEvent ae) {
        int kq = JOptionPane.showConfirmDialog(this, "Are you sure to close server?", "Notice", JOptionPane.YES_NO_OPTION);
        if(kq == JOptionPane.YES_OPTION) {
            try {
               
            	appendMessage("Server is close");

                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startServer();
	}

}
