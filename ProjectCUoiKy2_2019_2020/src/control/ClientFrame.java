package control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.ChatFrame;
import view.ClientPanel;
import view.LoginPanel;
import view.RoomPanel;
import view.SignUpPanel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;


public class ClientFrame extends JFrame implements Runnable {
    String serverHost;
    public static final String NICKNAME_EXIST = "This nickname is already login in another place! Please using another nickname";
    public static final String NICKNAME_VALID = "This nickname is OK";
    public static final String NICKNAME_INVALID = "Nickname or password is incorrect";
    public static final String SIGNUP_SUCCESS = "Sign up successful!";
    public static final String ACCOUNT_EXIST = "This nickname has been used! Please use another nickname!";
    
    String name;
    String room;
    Socket socketOfClient;
    BufferedWriter bw;
    BufferedReader br;
    
    JPanel mainPanel;
    LoginPanel loginPanel;
    ClientPanel clientPanel;
    
    SignUpPanel signUpPanel;
    RoomPanel roomPanel;
    
    Thread clientThread;
    boolean isRunning;
    
    JMenuBar menuBar;
    JMenu menuShareFile;
    JMenuItem itemSendFile;
    JMenu menuAccount;
    JMenuItem itemLeaveRoom, itemLogout, itemChangePass;
    
    SendFileFrame sendFileFrame;
    
    StringTokenizer tokenizer;
    String myDownloadFolder;
    
    Socket socketOfSender, socketOfReceiver;
    
    DefaultListModel<String> listModel, listModelThisRoom, listModel_rp;
        
    boolean isConnectToServer;
    //kiem tra nguoi dung kich hay kich lien tuc vao jlist
    int timeClicked = 0;    
    
    Hashtable<String, ChatFrame> listReceiver;
    
    public ClientFrame(String name) {
        this.name = name;
        socketOfClient = null;
        bw = null;
        br = null;
        isRunning = true;
        listModel = new DefaultListModel<>();
        listModelThisRoom = new DefaultListModel<>();
        listModel_rp = new DefaultListModel<>();
        isConnectToServer = false;
        listReceiver = new Hashtable<>();
        
        mainPanel = new JPanel();
        loginPanel = new LoginPanel();
        clientPanel = new ClientPanel();
        roomPanel = new RoomPanel();
        
        signUpPanel = new SignUpPanel();
        mainPanel.add(signUpPanel);
        GroupLayout gl_signUpPanel = new GroupLayout(signUpPanel);
        gl_signUpPanel.setHorizontalGroup(
        	gl_signUpPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(177)
        			.addComponent(signUpPanel.getTfHost(), GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(177)
        			.addComponent(signUpPanel.getTfID(), GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(177)
        			.addComponent(signUpPanel.getTfPass(), GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(177)
        			.addComponent(signUpPanel.getTfPass2(), GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(34)
        			.addComponent(signUpPanel.getLbBack_signup(), GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
        			.addGap(79)
        			.addComponent(signUpPanel.getBtSignUp(), GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
        );
        gl_signUpPanel.setVerticalGroup(
        	gl_signUpPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_signUpPanel.createSequentialGroup()
        			.addGap(127)
        			.addComponent(signUpPanel.getTfHost(), GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        			.addGap(11)
        			.addComponent(signUpPanel.getTfID(), GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(signUpPanel.getTfPass(), GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        			.addGap(11)
        			.addComponent(signUpPanel.getTfPass2(), GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        			.addGap(45)
        			.addGroup(gl_signUpPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_signUpPanel.createSequentialGroup()
        					.addGap(2)
        					.addComponent(signUpPanel.getLbBack_signup(), GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
        				.addComponent(signUpPanel.getBtSignUp(), GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)))
        );
        signUpPanel.setLayout(gl_signUpPanel);
        signUpPanel.setVisible(false);
        
       
        
     
        mainPanel.add(loginPanel);
        GroupLayout gl_loginPanel = new GroupLayout(loginPanel);
        gl_loginPanel.setHorizontalGroup(
        	gl_loginPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(10)
        			.addComponent(loginPanel.getLbBack_login(), GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(166)
        			.addComponent(loginPanel.getTfHost(), GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(166)
        			.addComponent(loginPanel.getTfNickname(), GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(166)
        			.addComponent(loginPanel.getTfPass(), GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(105)
        			.addComponent(loginPanel.getBtOK(), GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
        );
        gl_loginPanel.setVerticalGroup(
        	gl_loginPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_loginPanel.createSequentialGroup()
        			.addGap(49)
        			.addComponent(loginPanel.getLbBack_login(), GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
        			.addGap(135)
        			.addComponent(loginPanel.getTfHost(), GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        			.addGap(24)
        			.addComponent(loginPanel.getTfNickname(), GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        			.addGap(26)
        			.addComponent(loginPanel.getTfPass(), GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        			.addGap(42)
        			.addComponent(loginPanel.getBtOK(), GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
        );
        loginPanel.setLayout(gl_loginPanel);
        mainPanel.add(roomPanel);
        GroupLayout gl_roomPanel = new GroupLayout(roomPanel);
        gl_roomPanel.setHorizontalGroup(
        	gl_roomPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_roomPanel.createSequentialGroup()
        			.addGap(20)
        			.addGroup(gl_roomPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(roomPanel.getLbRoom1(), GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
        				.addComponent(roomPanel.getLbRoom2(), GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
        				.addComponent(roomPanel.getLbRoom3(), GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
        				.addComponent(roomPanel.getLbRoom4(), GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
        				.addComponent(roomPanel.getLbRoom5(), GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)))
        );
        gl_roomPanel.setVerticalGroup(
        	gl_roomPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_roomPanel.createSequentialGroup()
        			.addGap(139)
        			.addComponent(roomPanel.getLbRoom1(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        			.addGap(33)
        			.addComponent(roomPanel.getLbRoom2(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        			.addGap(37)
        			.addComponent(roomPanel.getLbRoom3(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        			.addGap(11)
        			.addComponent(roomPanel.getLbRoom4(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        			.addGap(11)
        			.addComponent(roomPanel.getLbRoom5(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
        );
        roomPanel.setLayout(gl_roomPanel);
        mainPanel.add(clientPanel);
        GroupLayout gl_clientPanel = new GroupLayout(clientPanel);
        gl_clientPanel.setHorizontalGroup(
        	gl_clientPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_clientPanel.createSequentialGroup()
        			.addGap(10)
        			.addComponent(clientPanel.getLbRoom(), GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_clientPanel.createSequentialGroup()
        			.addGap(595)
        			.addComponent(clientPanel.getBtSend(), GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
        		.addGroup(gl_clientPanel.createSequentialGroup()
        			.addGap(10)
        			.addComponent(clientPanel.getLbLike(), GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
        			.addGap(10)
        			.addComponent(clientPanel.getLbDislike(), GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
        			.addGap(10)
        			.addComponent(clientPanel.getLbPacMan(), GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
        			.addGap(4)
        			.addComponent(clientPanel.getLbSmile(), GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
        			.addGap(10)
        			.addComponent(clientPanel.getLbGrin(), GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
        			.addGap(20)
        			.addComponent(clientPanel.getLbCry(), GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
        			.addGap(175)
        			.addComponent(clientPanel.getBtExit(), GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
        );
        gl_clientPanel.setVerticalGroup(
        	gl_clientPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_clientPanel.createSequentialGroup()
        			.addGap(11)
        			.addComponent(clientPanel.getLbRoom(), GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
        			.addGap(357)
        			.addComponent(clientPanel.getBtSend(), GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        			.addGap(22)
        			.addGroup(gl_clientPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(clientPanel.getLbLike(), GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        				.addComponent(clientPanel.getLbDislike(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(clientPanel.getLbPacMan(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(clientPanel.getLbSmile(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(clientPanel.getLbGrin(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addGroup(gl_clientPanel.createSequentialGroup()
        					.addGap(4)
        					.addComponent(clientPanel.getLbCry(), GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
        				.addComponent(clientPanel.getBtExit(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)))
        );
        clientPanel.setLayout(gl_clientPanel);
        
        addEventsForWelcomePanel();
        addEventsForSignUpPanel();
        addEventsForLoginPanel();
        addEventsForClientPanel();
        addEventsForRoomPanel();
        
        menuBar = new JMenuBar();  
        menuShareFile = new JMenu();    
        menuAccount = new JMenu();
        itemLeaveRoom = new JMenuItem();
        itemLogout = new JMenuItem();
        itemChangePass = new JMenuItem();
        itemSendFile = new JMenuItem();     
        
        menuAccount.setText("Account");
        itemLogout.setText("Logout");
        itemLeaveRoom.setText("Leave room");
        itemChangePass.setText("Change password");
        menuAccount.add(itemLeaveRoom);
        menuAccount.add(itemChangePass);
        menuAccount.add(itemLogout);
        
        menuShareFile.setText("File sharing");
        itemSendFile.setText("Send a file");
        menuShareFile.add(itemSendFile);
        
        menuBar.add(menuAccount);
        menuBar.add(menuShareFile);
        //mainPanel.setSize(570, 450);
        
        loginPanel.setVisible(true);
        roomPanel.setVisible(false);
        clientPanel.setVisible(false);
        itemLeaveRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int kq = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure to leave this room?", "Notice", JOptionPane.YES_NO_OPTION);
                if(kq == JOptionPane.YES_OPTION) {
                    leaveRoom();
                }
            }
        });
        itemChangePass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(ClientFrame.this, "chua hoan thien", "Lá»—i", JOptionPane.ERROR_MESSAGE);
            }
        });
        itemLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int kq = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure to logout?", "Notice", JOptionPane.YES_NO_OPTION);
                if(kq == JOptionPane.YES_OPTION) {
                    try {
                        isConnectToServer = false;
                        socketOfClient.close();
                        ClientFrame.this.setVisible(false);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    new ClientFrame(null).setVisible(true);
                }
            }
        });
        itemSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                openSendFileFrame();
            }
        });
        menuBar.setVisible(false);
        
        setJMenuBar(menuBar);
        pack();
        
        getContentPane().add(mainPanel);
        setSize(500, 600);
        setLocation(100,50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(name);
    }
    
    
    private void addEventsForWelcomePanel() {
        
       
   
        
    }

    private void addEventsForSignUpPanel() {
        signUpPanel.getLbBack_signup().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               
                signUpPanel.setVisible(false);
                loginPanel.setVisible(true);
                clientPanel.setVisible(false);
                roomPanel.setVisible(false);
            }
        });
        signUpPanel.getBtSignUp().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btSignUpEvent();
            }
        });
    }

    private void addEventsForLoginPanel() {
        loginPanel.getTfNickname().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) btOkEvent();
            }
            
        });
        loginPanel.getTfPass().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) btOkEvent();
            }
            
        });
        loginPanel.getBtOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btOkEvent();
            }
        });
        loginPanel.getLbBack_login().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               
                signUpPanel.setVisible(true);
                loginPanel.setVisible(false);
                clientPanel.setVisible(false);
                roomPanel.setVisible(false);
            }
        });
    }

    private void addEventsForClientPanel() {
        clientPanel.getBtSend().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btSendEvent();
            }
        });
        clientPanel.getBtExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btExitEvent();
            }
        });
        clientPanel.getTaInput().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    btSendEvent();
                    btClearEvent();
                }
            }
        });
        //events for emotion icons:
        clientPanel.getLbLike().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|LIKE");
            }
        });
        clientPanel.getLbDislike().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|DISLIKE");
            }
        });
        clientPanel.getLbPacMan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|PAC_MAN");
            }
        });
        clientPanel.getLbCry().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|CRY");
            }
        });
        clientPanel.getLbGrin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|GRIN");
            }
        });
        clientPanel.getLbSmile().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|SMILE");
            }
        });
        
        clientPanel.getOnlineList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                openPrivateChatInsideRoom();
            }
        });
    }
    
    private void addEventsForRoomPanel() {
        roomPanel.getLbRoom1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom1().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom2().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom2().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom3().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom3().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom4().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom4().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom5().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom5().getText();
                labelRoomEvent();
            }
        });
        
        
        roomPanel.getOnlineList_rp().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                openPrivateChatOutsideRoom();
            }
        });
    }
    
    private void openPrivateChatInsideRoom() {
        timeClicked++;
        if(timeClicked == 1) {
            Thread countingTo500ms = new Thread(counting);
            countingTo500ms.start();
        }

        if(timeClicked == 2) {  
            String nameClicked = clientPanel.getOnlineList().getSelectedValue();
            if(nameClicked.equals(ClientFrame.this.name)) {
                JOptionPane.showMessageDialog(ClientFrame.this, "Can't send a message to yourself!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(!listReceiver.containsKey(nameClicked)) {   
                ChatFrame pc = new ChatFrame();
                pc.sender = name;
                pc.receiver = nameClicked;
                pc.bw = ClientFrame.this.bw;
                pc.br = ClientFrame.this.br;

                pc.getLbReceiver().setText("Private chat with \""+pc.receiver+"\"");
                pc.setTitle(pc.receiver);
                pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pc.setVisible(true);

                listReceiver.put(nameClicked, pc);
            } else {
                ChatFrame pc = listReceiver.get(nameClicked);
                pc.setVisible(true);
            }
        }
    }
            
    private void openPrivateChatOutsideRoom() {
        timeClicked++;
        if(timeClicked == 1) {
            Thread countingTo500ms = new Thread(counting);
            countingTo500ms.start();
        }

        if(timeClicked == 2) {  
            String privateReceiver = roomPanel.getOnlineList_rp().getSelectedValue();
            if(!listReceiver.containsKey(privateReceiver)) {    
                ChatFrame pc = new ChatFrame();
                pc.sender = name;
                pc.receiver = privateReceiver;
                pc.bw = ClientFrame.this.bw;
                pc.br = ClientFrame.this.br;

                pc.getLbReceiver().setText("frame chat with \""+pc.receiver+"\"");
                pc.setTitle(pc.receiver);
                pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pc.setVisible(true);

                listReceiver.put(privateReceiver, pc);
            } else {
                ChatFrame pc = listReceiver.get(privateReceiver);
                pc.setVisible(true);
            }
        }
    }
    
    Runnable counting = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeClicked = 0;
        }
    };
    
    private void labelRoomEvent() {
        this.clientPanel.getTpMessage().setText("");
        this.sendToServer("CMD_ROOM|"+this.room);
        try {
            Thread.sleep(200);      
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.roomPanel.setVisible(false);
        this.clientPanel.setVisible(true);
        this.setTitle("\""+this.name+"\" - "+this.room);
        clientPanel.getLbRoom().setText(this.room);
    }
    
    private void leaveRoom() {
        this.sendToServer("CMD_LEAVE_ROOM|"+this.room);
        try {
            Thread.sleep(200);     
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.roomPanel.setVisible(true);
        this.clientPanel.setVisible(false);
        //clear the textPane message:
        clientPanel.getTpMessage().setText("");
        this.setTitle("\""+this.name+"\"");
    }
    
    
    ////////////////////////Events////////////////////////////
    private void btOkEvent() {
        String hostname = loginPanel.getTfHost().getText().trim();
        String nickname = loginPanel.getTfNickname().getText().trim();
        String pass = loginPanel.getTfPass().getText().trim();
        
        this.serverHost = hostname;
        this.name = nickname;
        
        if(hostname.equals("") || nickname.equals("") || pass.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill up all fields", "Notice!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!isConnectToServer) {
            isConnectToServer = true;   //chi can connect 1 lan
           
            this.connectToServer(hostname); //tao socket ket noi toi server
        }    
        this.sendToServer("CMD_CHECK_NAME|" +this.name+"|"+pass);       //gui ten yeu cau dang nhap
        
        //server phan hoi ten vua nhap co hop le
        String response = this.recieveFromServer();
        if(response != null) {
            if (response.equals(NICKNAME_EXIST) || response.equals(NICKNAME_INVALID)) {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
                
            } else {
                //ten hop le, vao chat
                loginPanel.setVisible(false);
                roomPanel.setVisible(true);
                clientPanel.setVisible(false);
                this.setTitle("\""+name+"\"");

                menuBar.setVisible(true);

                
                clientThread = new Thread(this);
                clientThread.start();
                this.sendToServer("CMD_ROOM|"+this.room);     //

                System.out.println("this is \""+name+"\"");
                
            }
        } else System.out.println("[btOkEvent()] Server is not open yet, or already closed!");
    }
    
    private void btSignUpEvent() {
        String pass = this.signUpPanel.getTfPass().getText();
        String pass2 = this.signUpPanel.getTfPass2().getText();
        if(!pass.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String nickname = signUpPanel.getTfID().getText().trim();
            String hostName = signUpPanel.getTfHost().getText().trim();
            if(hostName.equals("") || nickname.equals("") || pass.equals("") || pass2.equals("")) {
                JOptionPane.showMessageDialog(this, "Please fill up all fields", "Notice!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(!isConnectToServer) {
                isConnectToServer = true;   
                this.connectToServer(hostName); 
            }    
            this.sendToServer("CMD_SIGN_UP|" +nickname+"|"+pass);       
        
            String response = this.recieveFromServer();
            if(response != null) {
                if(response.equals(NICKNAME_EXIST) || response.equals(ACCOUNT_EXIST)) {
                    JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response+"\nYou can now go back and login to join chat room", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    signUpPanel.clearTf();
                }
            }
        }
        
    }
            
    private void btSendEvent() {
        String message = clientPanel.getTaInput().getText().trim();
        if(message.equals("")) clientPanel.getTaInput().setText("");
        else {
            this.sendToServer("CMD_CHAT|" + message);       //gui data toi server
            this.btClearEvent();
        }
        
    }

    private void btClearEvent() {
        clientPanel.getTaInput().setText("");
    }

    private void btExitEvent() {
        try {
            isRunning = false;
            //this.disconnect();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void openSendFileFrame() {
        sendFileFrame = new SendFileFrame();
        
        //gui thong tin client nay sang frame khac
        sendFileFrame.name = this.name;
        sendFileFrame.socketOfClient = this.socketOfClient;
        sendFileFrame.bw = this.bw;
        sendFileFrame.br = this.br;
        
        sendFileFrame.setVisible(true);
        sendFileFrame.setLocation(450, 250);
        sendFileFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    ////////////////////////End of Events////////////////////////////   
    
    public void connectToServer(String hostAddress) {   
        try {
            socketOfClient = new Socket(hostAddress, 9999);
            bw = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            
        } catch (java.net.UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Host IP is not correct.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (java.net.ConnectException e) {
            JOptionPane.showMessageDialog(this, "Server is unreachable, maybe server is not open yet, or can't find this host.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch(java.net.NoRouteToHostException e) {
            JOptionPane.showMessageDialog(this, "Can't find this host!\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public void sendToServer(String line) {
        try {
            this.bw.write(line);
            this.bw.newLine();   
            this.bw.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, "Server is closed, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.lang.NullPointerException e) {
            System.out.println("[sendToServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String recieveFromServer() {
        try {
            return this.br.readLine();  
        } catch (java.lang.NullPointerException e) {
            System.out.println("[recieveFromServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void disconnect() {
        System.out.println("disconnect()");
        try {
            if(br!=null) this.br.close();
            if(bw!=null) this.bw.close();
            if(socketOfClient!=null) this.socketOfClient.close();
            System.out.println("trong khoi try catch");
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClientFrame client = new ClientFrame(null);
        client.setVisible(true);
    }

    @Override
    public void run() {
        String response;
        String sender, receiver, fileName;
        String msg;
        String cmd, icon;
        
        while(isRunning) {
            response = this.recieveFromServer();   //nhan phan hoi tu server
            tokenizer = new StringTokenizer(response, "|");
            cmd = tokenizer.nextToken();
            switch (cmd) {
                case "CMD_CHAT":    //nhan duoc goi tin
                    sender = tokenizer.nextToken();
                    msg = response.substring(cmd.length()+sender.length()+2, response.length());
                    
                    if(sender.equals(this.name)) this.clientPanel.appendMessage(sender+": ", msg, Color.BLACK, new Color(0, 102, 204));
                    else this.clientPanel.appendMessage(sender+": ", msg, Color.MAGENTA, new Color(56, 224, 0));
                    
                    
                    break;
                    
                case "CMD_ChatFrame":
                    sender = tokenizer.nextToken();
                    msg = response.substring(cmd.length()+sender.length()+2, response.length());
                    
                    ChatFrame pc;
                    pc = listReceiver.get(sender);
                    
                    if(pc == null) {
                        pc = new ChatFrame();
                        pc.sender = name;
                        pc.receiver = sender;
                        pc.bw = ClientFrame.this.bw;
                        pc.br = ClientFrame.this.br;

                        pc.getLbReceiver().setText("ChatFrame chat with \""+pc.receiver+"\"");
                        pc.setTitle(pc.receiver);
                        pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        pc.setVisible(true);    

                        listReceiver.put(sender, pc);
                    } else {
                        pc.setVisible(true);
                    }
                    pc.appendMessage_Left(sender+": ", msg);
                    break;
                    
                case "CMD_ONLINE_USERS":
                    listModel.clear();
                    listModel_rp.clear();
                    while(tokenizer.hasMoreTokens()) {
                        cmd = tokenizer.nextToken();
                        listModel.addElement(cmd);
                        listModel_rp.addElement(cmd);
                    }
                    clientPanel.getOnlineList().setModel(listModel);
                    
                    listModel_rp.removeElement(this.name);
                    roomPanel.getOnlineList_rp().setModel(listModel_rp);
                    break;
                    
                case "CMD_ONLINE_THIS_ROOM":
                    listModelThisRoom.clear();
                    while(tokenizer.hasMoreTokens()) {
                        cmd = tokenizer.nextToken();
                        listModelThisRoom.addElement(cmd);
                    }
                    clientPanel.getOnlineListThisRoom().setModel(listModelThisRoom);
                    break;
                    
                case "CMD_SERVERISBUSY":
                    JOptionPane.showMessageDialog(this, "Server is busy, please try to send file later", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                  
                case "CMD_SENDFILE_REQUEST":
                    
                    sender = tokenizer.nextToken();
                    receiver = tokenizer.nextToken();
                    fileName = tokenizer.nextToken();
                    
                    int choose = JOptionPane.showConfirmDialog(this, "\""+sender+"\" want to send a file to you\nFile name: "+fileName+"\nDo you want to accept?", "Notice", JOptionPane.YES_NO_OPTION);
                    if(choose == JOptionPane.YES_OPTION) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int kq = chooser.showSaveDialog(this);
                        if(kq == JFileChooser.APPROVE_OPTION) {
                            myDownloadFolder = chooser.getSelectedFile().getAbsolutePath();
                        } else {
                            myDownloadFolder = "D:";
                            JOptionPane.showMessageDialog(this, "The default folder to save file is in D:\\", "Notice", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                        try {
                            socketOfReceiver = new Socket(serverHost, 9999);   
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketOfReceiver.getOutputStream()));
                            writer.write("CMD_SENDFILE_I_AM_THE_RECEIVER|"+this.name);
                            writer.newLine();   
                            writer.flush();
                            
                            new ReceivingFileThread(socketOfReceiver, myDownloadFolder, fileName).start();    
                            System.out.println("start receiving file");
                        } catch (IOException ex) {
                            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        sendToServer("CMD_SENDFILE_ACCEPT|"+sender+"|"+receiver);
                    } else {
                        sendToServer("CMD_SENDFILE_DENY|"+sender+"|"+receiver);
                    }
                    break;
                    
                case "CMD_RECEIVER_NOT_EXIST":
                    JOptionPane.showMessageDialog(sendFileFrame, "The receiver's name is wrong", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    
                case "CMD_SENDFILE_DENY":
                    sender = tokenizer.nextToken();
                    receiver = tokenizer.nextToken();
                    JOptionPane.showMessageDialog(sendFileFrame, "\""+receiver+"\" don't want to receive your file!", "Send failed!", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case "CMD_SENDFILE_ACCEPT":     //khi dong y nhan file
                    //now we send file to server:
                    String filePath = sendFileFrame.getTfFilePath().getText();
                    receiver = sendFileFrame.getTfReceiver().getText();
                
                    try {
                        socketOfSender = new Socket(serverHost, 9999);     
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketOfSender.getOutputStream()));
                        writer.write("CMD_SENDFILE_I_AM_SENDER|"+this.name);
                        writer.newLine();
                        writer.flush();
                        
                        new SendingFileThread(this.name, receiver, filePath, socketOfSender, sendFileFrame, null).start();  
                        System.out.println("start sending file");
                    } catch (IOException ex) {
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    break;
                    
                case "CMD_ICON":
                    icon = tokenizer.nextToken();
                    cmd = tokenizer.nextToken();    //cmd = sender
                    
                    if(cmd.equals(this.name)) this.clientPanel.appendMessage(cmd+": ", "\n  ", Color.BLACK, Color.BLACK);
                    else this.clientPanel.appendMessage(cmd+": ", "\n   ", Color.MAGENTA, Color.MAGENTA);
                    
                    switch (icon) {
                        case "LIKE":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/like2.png")));
                            break;
                            
                            
                        case "DISLIKE":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/dislike.png")));
                            break;
                            
                            
                        case "PAC_MAN":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/pacman.png")));
                            break;
                            
                        case "SMILE":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/smile.png")));
                            break;
                            
                        case "GRIN":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/grin.png")));
                            break;
                            
                        case "CRY":
                            this.clientPanel.getTpMessage().insertIcon(new ImageIcon(getClass().getResource("/images/cry.png")));
                            break;
                            
                        default:
                            throw new AssertionError("The icon is invalid, or can't find icon!");
                    }
                    
                    break;
                    
                default:
                    if(!response.startsWith("CMD_")) {      //tin nhan thuong
                        if(response.equals("Warnning: Server has been closed!")) {
                            this.clientPanel.appendMessage(response, Color.RED);
                        }
                        else this.clientPanel.appendMessage(response, new Color(153, 153, 153));
                    }
                  
                    
            }
        }
        System.out.println("Disconnected to server!");
    }


}

