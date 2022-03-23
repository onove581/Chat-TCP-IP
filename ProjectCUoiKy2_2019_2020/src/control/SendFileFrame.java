package control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.ClientFrame;
import control.SendFileFrame;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SendFileFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tfFilePath;
	private JTextField tfReceiver;
	 String name;
	    Socket socketOfClient;
	    BufferedWriter bw;
	    BufferedReader br;
	/**
	 * Launch the application.
	 */
	    public JTextField getTfFilePath() {
	        return tfFilePath;
	    }

	    public JTextField getTfReceiver() {
	        return tfReceiver;
	    }
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendFileFrame frame = new SendFileFrame();
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
	public SendFileFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 473, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfFilePath = new JTextField();
		tfFilePath.setBounds(10, 113, 266, 35);
		contentPane.add(tfFilePath);
		tfFilePath.setColumns(10);
		
		tfReceiver = new JTextField();
		tfReceiver.setBounds(10, 219, 266, 35);
		contentPane.add(tfReceiver);
		tfReceiver.setColumns(10);
		
		JButton btBrowse = new JButton("brown");
		btBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 btBrowseActionPerformed(e);
			}
		});
		btBrowse.setBounds(301, 113, 105, 35);
		contentPane.add(btBrowse);
		
		JButton btSendFile = new JButton("send file");
		btSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btSendFileActionPerformed(e);
			}
		});
		btSendFile.setBounds(297, 219, 109, 35);
		contentPane.add(btSendFile);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 327, 439, 35);
		contentPane.add(progressBar);
	}
	 private void btBrowseActionPerformed(java.awt.event.ActionEvent evt) {
	        displayOpenDialog();
	    }

	    private void btSendFileActionPerformed(java.awt.event.ActionEvent evt) {
	        String filePath = tfFilePath.getText();
	        String reciever = tfReceiver.getText();
	        File file = new File(filePath);
	        
	        this.sendToServer("CMD_SENDFILE_REQUEST|"+name+"|"+reciever+"|"+file.getName());
	        System.out.println("(SendFileFrame.java) CMD_SENDFILE_REQUEST|"+name+"|"+reciever+"|"+file.getName());
	    }

	    private void displayOpenDialog() {
	        JFileChooser chooser = new JFileChooser();
	        int kq = chooser.showOpenDialog(this);
	        if(kq == JFileChooser.APPROVE_OPTION) {
	            tfFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
	        } else tfFilePath.setText("");
	    }
	    
	    public void sendToServer(String line) {
	        try {
	            this.bw.write(line);
	            this.bw.newLine();  
	            this.bw.flush();
	        } catch (java.net.SocketException e) {
	            JOptionPane.showMessageDialog(this, "Server is close, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
	        } catch (IOException ex) {
	            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    
	   
}
