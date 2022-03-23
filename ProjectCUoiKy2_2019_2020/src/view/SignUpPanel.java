package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;

public class SignUpPanel extends JPanel {
	private JButton btSignUp;
	private JLabel lbBack_signup;
	private JTextField tfHost;
	private JTextField tfID;
	private JPasswordField tfPass;
	private JPasswordField tfPass2;
	
	 public JButton getBtSignUp() {
	        return btSignUp;
	    }

	    public JLabel getLbBack_signup() {
	        return lbBack_signup;
	    }

	    public JTextField getTfID() {
	        return tfID;
	    }

	    public JPasswordField getTfPass() {
	        return tfPass;
	    }

	    public JPasswordField getTfPass2() {
	        return tfPass2;
	    }

	    public JTextField getTfHost() {
	        return tfHost;
	    }
	    
	    public void clearTf() {
	        tfID.setText("");
	        tfPass.setText("");
	        tfPass2.setText("");
	        
	    }

	/**
	 * Create the panel.
	 */
	public SignUpPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sign Up");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 23));
		lblNewLabel.setBounds(112, 27, 112, 59);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server's Address");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(10, 115, 112, 45);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Name");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_1.setBounds(10, 159, 81, 45);
		add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Password");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_2.setBounds(10, 210, 161, 45);
		add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("ReturnPass");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_3.setBounds(10, 266, 161, 45);
		add(lblNewLabel_1_3);
		
		 btSignUp = new JButton("SIGN UP");
		btSignUp.setBackground(Color.CYAN);
		btSignUp.setForeground(Color.BLUE);
		btSignUp.setFont(new Font("Tahoma", Font.BOLD, 15));
		btSignUp.setBounds(156, 360, 140, 45);
		add(btSignUp);
		
		tfHost = new JTextField();
		tfHost.setText("localhost");
		tfHost.setBounds(132, 121, 188, 33);
		add(tfHost);
		tfHost.setColumns(10);
		
		tfID = new JTextField();
		tfID.setColumns(10);
		tfID.setBounds(132, 165, 188, 33);
		add(tfID);
		
		tfPass = new JPasswordField();
		tfPass.setBounds(132, 216, 186, 33);
		add(tfPass);
		
		tfPass2 = new JPasswordField();
		tfPass2.setBounds(132, 266, 186, 33);
		add(tfPass2);
		
		lbBack_signup = new JLabel("Login");
		lbBack_signup.setIcon(new ImageIcon(SignUpPanel.class.getResource("/images/back.png")));
		lbBack_signup.setBounds(34, 346, 112, 45);
		add(lbBack_signup);

	}
}
