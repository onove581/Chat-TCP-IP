package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tfInput_pc;
	public String sender, receiver;
    public BufferedWriter bw;
    public BufferedReader br;
    HTMLEditorKit htmlKit;
    HTMLDocument htmlDoc;  //dùng để insert văn bản dạng html vào tpMessage_pc
    private JLabel lbReceiver;
    private JTextPane tpMessage_pc;
    private JButton btSend_pc;

	/**
	 * Launch the application.
	 */
    public JLabel getLbReceiver() {
        return lbReceiver;
    }
	public static void main(String[] args) {
EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					ChatFrame frame = new ChatFrame();
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
	public ChatFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 504);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 93, 373, 219);
		contentPane.add(scrollPane);
		
		 tpMessage_pc = new JTextPane();
		scrollPane.setViewportView(tpMessage_pc);
		
		tfInput_pc = new JTextField();
		tfInput_pc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfInput_pcActionPerformed(e);
			}
		});
		tfInput_pc.setBounds(20, 349, 266, 33);
		contentPane.add(tfInput_pc);
		tfInput_pc.setColumns(10);
		
		btSend_pc = new JButton("Send");
		btSend_pc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 btSend_pcActionPerformed(e);
			}
		});
		btSend_pc.setBounds(307, 349, 119, 33);
		contentPane.add(btSend_pc);
		
		 lbReceiver = new JLabel("Receive");
		lbReceiver.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbReceiver.setBounds(20, 31, 144, 45);
		contentPane.add(lbReceiver);
		
		htmlKit = new HTMLEditorKit();
		htmlDoc = new HTMLDocument();
		tpMessage_pc.setEditorKit(htmlKit);
		tpMessage_pc.setDocument(htmlDoc);
	}
	
	public void sendToServer(String line) {
        try {
            this.bw.write(line);
            this.bw.newLine();   //dung ham new line de dung readline
            this.bw.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, "Server is closed, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.lang.NullPointerException e) {
            System.out.println("[sendToServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String recieveFromServer() {
        try {
            return this.br.readLine();  //dung readline de doc du lieu
        } catch (java.lang.NullPointerException e) {
            System.out.println("[recieveFromServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void appendMessage(String msg1, String msg2, Color c1, Color c2) {  //thiet lap mau sac cua 2 dong
        //ms1
        int len = tpMessage_pc.getDocument().getLength();
        StyledDocument doc = (StyledDocument) tpMessage_pc.getDocument();
        
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Tahoma");
        StyleConstants.setBold(sas, true);
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c1);
        //StyleConstants.setAlignment(sas, StyleConstants.ALIGN_RIGHT);
        
        try {
            doc.insertString(len, msg1, sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //ms2 sau ms1
        doc = (StyledDocument) tpMessage_pc.getDocument();
        len = len+msg1.length();
        
        sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Arial");
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c2);
        //StyleConstants.setAlignment(sas, StyleConstants.ALIGN_RIGHT);
        
        try {
            doc.insertString(len, msg2+"\n", sas);      //phai xuong dong
        } catch (BadLocationException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tpMessage_pc.setCaretPosition(len+msg2.length());
    }
    
    public void appendMessage_Left(String msg1, String msg2) {      //nguoi ma user chat
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:black; padding: 3px; margin-top: 4px; margin-right:35px; text-align:left; font:normal 12px Tahoma;\"><span style=\"background-color:#f3f3f3;\"><b>" + msg1 + "</b><span style=\"color:black;\">" + msg2 + "</span></span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength());
    }
    
    public void appendMessage_Right(String msg1, String msg2) {     //user
        try { 
            //htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:blue; margin-left:30px; text-align:right; font:normal 12px Tahoma;\"><b>" + msg1 + "</b><span style=\"color:black;\">" + msg2 + "</span></p>", 0, 0, null);
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:white; padding: 3px; margin-top: 4px; margin-left:35px; text-align:right; font:normal 12px Tahoma;\"><span style=\"background-color: #4165ff; -webkit-border-radius: 10px;\">" + msg2 + "</span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength());
    }

    private void sendMessage() {
        String msg = tfInput_pc.getText();
        if(msg.equals("")) return;
        appendMessage_Right(this.sender+": ", msg);
        sendToServer("CMD_PRIVATECHAT|" + this.sender + "|" + this.receiver + "|" + msg);
        tfInput_pc.setText("");
    }
    private void tfInput_pcActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void btSend_pcActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }//GEN-LAST:event_btSend_pcActionPerformed

}
