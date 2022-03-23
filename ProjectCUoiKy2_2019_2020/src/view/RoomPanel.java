package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class RoomPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private JLabel  lbRoom1;
	private JLabel  lbRoom2;
	private JLabel  lbRoom3;
	private JLabel  lbRoom4;
	private JLabel  lbRoom5;
	private JList onlineList_rp;
	
	 public JLabel getLbRoom1() {
	        return lbRoom1;
	    }

	    public JLabel getLbRoom2() {
	        return lbRoom2;
	    }

	    public JLabel getLbRoom3() {
	        return lbRoom3;
	    }

	    public JLabel getLbRoom4() {
	        return lbRoom4;
	    }

	    public JLabel getLbRoom5() {
	        return lbRoom5;
	    }

	    public JList<String> getOnlineList_rp() {
	        return onlineList_rp;
	    }
	public RoomPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CHOOSE ROOM");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(10, 34, 117, 78);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Online");
		lblNewLabel_1.setForeground(new Color(0, 0, 153));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel_1.setBounds(101, 102, 159, 58);
		add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(101, 159, 74, 318);
		add(scrollPane);
		
		 onlineList_rp = new JList();
		 scrollPane.setViewportView(onlineList_rp);
		 onlineList_rp.setToolTipText("double-click to send a message");
		 onlineList_rp.setFont(new Font("Poor Richard", Font.PLAIN, 14));
		 onlineList_rp.setModel(new AbstractListModel() {
		 	String[] values = new String[] {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
		 	public int getSize() {
		 		return values.length;
		 	}
		 	public Object getElementAt(int index) {
		 		return values[index];
		 	}
		 });
		
		 lbRoom1 = new JLabel("ROOM 1");
		lbRoom1.setForeground(Color.BLUE);
		lbRoom1.setBackground(new Color(0, 51, 153));
		lbRoom1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbRoom1.setBounds(20, 139, 66, 52);
		add(lbRoom1);
		
		 lbRoom2 = new JLabel("ROOM 2");
		lbRoom2.setForeground(Color.BLUE);
		lbRoom2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbRoom2.setBackground(new Color(0, 51, 153));
		lbRoom2.setBounds(20, 224, 66, 52);
		add(lbRoom2);
		
		lbRoom3 = new JLabel("ROOM 3");
		lbRoom3.setForeground(Color.BLUE);
		lbRoom3.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbRoom3.setBackground(new Color(0, 51, 153));
		lbRoom3.setBounds(20, 313, 66, 52);
		add(lbRoom3);
		
		 lbRoom4 = new JLabel("ROOM 4");
		lbRoom4.setForeground(Color.BLUE);
		lbRoom4.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbRoom4.setBackground(new Color(0, 51, 153));
		lbRoom4.setBounds(20, 376, 66, 52);
		add(lbRoom4);
		
		 lbRoom5 = new JLabel("ROOM 5");
		lbRoom5.setForeground(Color.BLUE);
		lbRoom5.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbRoom5.setBackground(new Color(0, 51, 153));
		lbRoom5.setBounds(20, 439, 66, 52);
		add(lbRoom5);

	}
}
