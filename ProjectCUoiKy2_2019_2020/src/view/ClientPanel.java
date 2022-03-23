package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

public class ClientPanel extends JPanel {
	JFileChooser chooser;
    String filePath;
    private JButton btExit;
    private JButton btSend;
    private JTextArea taInput;
    private JList<String> onlineList;
    private JTextPane tpMessage;
    private  JList<String> onlineListThisRoom;
    private JLabel lbRoom;
    private JLabel lbLike;
    private JLabel lbPacman;
    private JLabel lbCry;
    private JLabel lbDislike;
    private JLabel lbGrin;
    private JLabel lbSmile;
    public JButton getBtExit() {
        return btExit;
    }

    public JButton getBtSend() {
        return btSend;
    }

    public JTextArea getTaInput() {
        return taInput;
    }
    
    public JTextPane getTpMessage() {
        return tpMessage;
    }

    
    public JList<String> getOnlineList() {
        return onlineList;
    }

    public JList<String> getOnlineListThisRoom() {
        return onlineListThisRoom;
    }

    public JLabel getLbRoom() {
        return lbRoom;
    }

    public JLabel getLbLike() {
        return lbLike;
    }

    public JLabel getLbPacMan() {
        return lbPacman;
    }

    public JLabel getLbCry() {
        return lbCry;
    }

    public JLabel getLbDislike() {
        return lbDislike;
    }

    public JLabel getLbGrin() {
        return lbGrin;
    }

    public JLabel getLbSmile() {
        return lbSmile;
    }
    
	/**
	 * Create the panel.
	 */
	public ClientPanel() {
		setLayout(null);
		
		 lbRoom = new JLabel("Room ?");
		lbRoom.setForeground(Color.BLUE);
		lbRoom.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbRoom.setBounds(10, 11, 273, 86);
		add(lbRoom);
		
		JLabel lblNewLabel = new JLabel("Messager content");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(10, 141, 188, 51);
		add(lblNewLabel);
		
		JLabel lblOnline = new JLabel("Online");
		lblOnline.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOnline.setBounds(701, 11, 83, 51);
		add(lblOnline);
		
		JLabel lblOnlineThisRoom = new JLabel("Online this room");
		lblOnlineThisRoom.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOnlineThisRoom.setBounds(338, 11, 188, 51);
		add(lblOnlineThisRoom);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 203, 774, 231);
		add(scrollPane);
		
		tpMessage = new JTextPane();
		scrollPane.setViewportView(tpMessage);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(208, 69, 318, 115);
		add(scrollPane_1);
		
		 onlineListThisRoom = new JList();
		scrollPane_1.setViewportView(onlineListThisRoom);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(595, 69, 189, 115);
		add(scrollPane_2);
		
		 onlineList = new JList();
		scrollPane_2.setViewportView(onlineList);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(22, 458, 504, 62);
		add(scrollPane_3);
		
		 taInput = new JTextArea();
		scrollPane_3.setViewportView(taInput);
		
		 btSend = new JButton("Send");
		btSend.setBounds(595, 454, 151, 66);
		add(btSend);
		
		btExit = new JButton("Exit");
		btExit.setBounds(595, 542, 159, 47);
		add(btExit);
		
		 lbLike = new JLabel("");
		lbLike.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/like2.png")));
		lbLike.setBounds(10, 542, 63, 51);
		add(lbLike);
		
		 lbDislike = new JLabel("");
		lbDislike.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/dislike.png")));
		lbDislike.setBounds(83, 542, 63, 47);
		add(lbDislike);
		
		 lbPacman = new JLabel("");
		lbPacman.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/pacman.png")));
		lbPacman.setBounds(156, 542, 48, 47);
		add(lbPacman);
		
		lbSmile = new JLabel("");
		lbSmile.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/smile.png")));
		lbSmile.setBounds(208, 542, 65, 47);
		add(lbSmile);
		
		 lbGrin = new JLabel("");
		lbGrin.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/grin.png")));
		lbGrin.setBounds(283, 542, 54, 47);
		add(lbGrin);
		
		 lbCry = new JLabel("");
		lbCry.setIcon(new ImageIcon(ClientPanel.class.getResource("/images/cry.png")));
		lbCry.setBounds(357, 546, 63, 43);
		add(lbCry);
		chooser = new JFileChooser();

	}
	
    public void appendMessage2(String msg1, String msg2, Color c1, Color c2) {  //thiet lap mau text 2 dung
        tpMessage.setEditable(true);
        //msg1 truoc
        int len = tpMessage.getDocument().getLength();
        
        StyleContext sc = StyleContext.getDefaultStyleContext();
        
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c1);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Impact"); //  FontFamily
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);     
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);      //kich thuoc font
        
        tpMessage.setCaretPosition(len);    //vi tri text chen
        tpMessage.setCharacterAttributes(aset, false);  //
        tpMessage.replaceSelection(msg1);   //chen text vao vi tri tren
        
        //chen msg2
        len = len + msg1.length();
        
        sc = StyleContext.getDefaultStyleContext();
        
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c2); 
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Arial"); //  FontFamily
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);    
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);      //kich thuoc font
        
        tpMessage.setCaretPosition(len);
        tpMessage.setCharacterAttributes(aset, false);
        tpMessage.replaceSelection(msg2+"\n");   //xuong dong
        
        len = len + msg2.length();
        tpMessage.setCaretPosition(len);
        tpMessage.setEditable(false);
    }
    
    
    public void appendMessage2(String message, Color color) {
        int len = tpMessage.getDocument().getLength();
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Comic Sans MS"); // 
        
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);     
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);      //kích thước font
        
        tpMessage.setCaretPosition(len);    
        tpMessage.setCharacterAttributes(aset, false);  
        tpMessage.replaceSelection(message+"\n"); 
        
    }   
     
    //we should use this method
    public void appendMessage(String msg1, String msg2, Color c1, Color c2) {  //
        //chen msg1:
        int len = tpMessage.getDocument().getLength();
        StyledDocument doc = (StyledDocument) tpMessage.getDocument();
        
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Serif");
        StyleConstants.setBold(sas, true);
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c1);
        
        try {
            doc.insertString(len, msg1, sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //msg2
        doc = (StyledDocument) tpMessage.getDocument();
        len = len+msg1.length();
        
        sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Arial");
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c2);
        
        try {
            doc.insertString(len, msg2+"\n", sas);      //phai xuong dong
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tpMessage.setCaretPosition(len+msg2.length());
    }
    
    public void appendMessage(String message, Color color) {
        int len = tpMessage.getDocument().getLength();
        StyledDocument doc = (StyledDocument) tpMessage.getDocument();
        
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Comic Sans MS");
        StyleConstants.setItalic(sas, true);
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, color);
        
        try {
            doc.insertString(len, message+"\n", sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tpMessage.setCaretPosition(len+message.length());
    }
}
