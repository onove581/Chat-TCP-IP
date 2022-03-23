package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginPanel extends JPanel {
	private JTextField tfHost;
	private JTextField tfNickname;
	private JTextField tfPass;
	private JButton btOK;
	private JLabel lbBack_login;
	public JButton getBtOK() {
        return btOK;
    }

    public JTextField getTfHost() {
        return tfHost;
    }

    public JTextField getTfNickname() {
        return tfNickname;
    }

    public JLabel getLbBack_login() {
        return lbBack_login;
    }

    public JTextField getTfPass() {
        return tfPass;
    }

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);
		
		lbBack_login = new JLabel("Sign up");
		lbBack_login.setIcon(new ImageIcon(LoginPanel.class.getResource("/images/back.png")));
		lbBack_login.setBounds(10, 49, 95, 61);
		add(lbBack_login);
		
		JLabel lblNewLabel_1 = new JLabel("LOGIN");
		lblNewLabel_1.setForeground(Color.BLUE);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 27));
		lblNewLabel_1.setBounds(122, 117, 248, 76);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Ip adress");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2.setBounds(25, 232, 101, 42);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Name");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2_1.setBounds(25, 285, 101, 42);
		add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("Pass");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2_2.setBounds(25, 340, 101, 42);
		add(lblNewLabel_2_2);
		
		tfHost = new JTextField();
		tfHost.setBounds(166, 245, 204, 29);
		add(tfHost);
		tfHost.setColumns(10);
		
		tfNickname = new JTextField();
		tfNickname.setColumns(10);
		tfNickname.setBounds(166, 298, 204, 29);
		add(tfNickname);
		
		tfPass = new JTextField();
		tfPass.setColumns(10);
		tfPass.setBounds(166, 353, 204, 29);
		add(tfPass);
		
		btOK = new JButton("Button");
		btOK.setBackground(Color.PINK);
		btOK.setForeground(Color.RED);
		btOK.setFont(new Font("Tahoma", Font.BOLD, 14));
		btOK.setBounds(105, 424, 170, 42);
		add(btOK);

	}
}
