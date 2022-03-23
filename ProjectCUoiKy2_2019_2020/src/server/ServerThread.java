package server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class ServerThread extends Thread {
	//socketOfServer de phan biet cac server thread
    Socket socketOfServer;      //socket de ket noi socket client toi 
    BufferedWriter bw;
    BufferedReader br;
    String clientName, clientPass, clientRoom;
    public static Hashtable<String, ServerThread> listUser = new Hashtable<>();
    // clientName la ten client
    // khi client yeu cau ket noi server thi server dung thread tao doi tuong server thread de xu ly client
      //do đó cần cho thằng ServerThread đó vào list để lúc nào muốn gửi tin tới bất kỳ thằng client khác hoặc gửi tin tới mọi client thì lấy thằng ServerThread trong listUser ra, và từ thằng ServerThread đó
      //. bw để gui data toi client và br de duoc data tu client gui ve
    public static final String NICKNAME_EXIST = "This nickname is already login in another place! Please using another nickname";
    public static final String NICKNAME_VALID = "This nickname is OK";
    public static final String NICKNAME_INVALID = "Nickname or password is incorrect";
    public static final String SIGNUP_SUCCESS = "Sign up successful!";
    public static final String ACCOUNT_EXIST = "This nickname has been used! Please use another nickname!";
    
    public JTextArea taServer;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
    StringTokenizer tokenizer;
    private final int BUFFER_SIZE = 100;
    
    String senderName, receiverName;    //gui file va nhan file
    static Socket senderSocket, receiverSocket;     //gui va nhan file tren client
    
    UserDatabase userDB;
    
    static boolean isBusy = false;     
    //kiem tra server co nhan file hay la khong 
    public ServerThread(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
        this.bw = null;
        this.br = null;
        
        clientName = "";
        clientPass = "";
        clientRoom = "";
        
        userDB = new UserDatabase();
        userDB.connect();
    }
    
    public void appendMessage(String message) {
        taServer.append(message);
        //vi tri con tro chuot sau doan text
        taServer.setCaretPosition(taServer.getText().length() - 1);     
    }
    
    public String recieveFromClient() {
        try {
            return br.readLine();
        } catch (IOException ex) {
            System.out.println(clientName+" is disconnected!");
        }
        return null;
    }
    //gui tin toi client 
    public void sendToClient(String response) {     
        try {
            bw.write(response);
            bw.newLine();
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendToSpecificClient(ServerThread socketOfClient, String response) {     //gui tin voi nguoi cu the
        try {
            BufferedWriter writer = socketOfClient.bw;
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendToSpecificClient(Socket socket, String response) {     //gui tin toi nguoi cu the
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void notifyToAllUsers(String message) {
        //dau tien server socket client trong listuser.sau do lay ten va tin nhan gui qua cac client khac thong qua
        // socketOfServer cac client va duoc luu trong list user
        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread st;
        BufferedWriter writer;
        
        while(clients.hasMoreElements()) {
            st = clients.nextElement();
            writer = st.bw;

            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void notifyToUsersInRoom(String message) {
        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread st;
        BufferedWriter writer;
        
        while(clients.hasMoreElements()) {
            st = clients.nextElement();
            if(st.clientRoom.equals(this.clientRoom)) {     //gui trong cung 1 phong
                writer = st.bw;

                try {
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void notifyToUsersInRoom(String room, String message) {      //gui messs toi room
        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread st;
        BufferedWriter writer;
        
        while(clients.hasMoreElements()) {
            st = clients.nextElement();
            if(st.clientRoom.equals(room)) {
                writer = st.bw;

                try {
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void closeServerThread() {
        try {
            br.close();
            bw.close();
            socketOfServer.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getAllUsers() {
        StringBuffer kq = new StringBuffer();
        String temp = null;
        
        Enumeration<String> keys = listUser.keys();
        if(keys.hasMoreElements()) {
            String str = keys.nextElement();
            kq.append(str);
        }
        
        while(keys.hasMoreElements()) {
            temp = keys.nextElement();
            kq.append("|").append(temp);
        }
        
        return kq.toString();
    }
    // so luong nguoi trong phong 
    public String getUsersThisRoom() {
        StringBuffer kq = new StringBuffer();
        String temp = null;
        ServerThread st;
        Enumeration<String> keys = listUser.keys();
        
        while(keys.hasMoreElements()) {
            temp = keys.nextElement();
            st = listUser.get(temp);
            if(st.clientRoom.equals(this.clientRoom))  kq.append("|").append(temp);
        }
        
        if(kq.equals("")) return "|";
        return kq.toString();  
    }
    
    public String getUsersAtRoom(String room) {
        StringBuffer kq = new StringBuffer();
        String temp = null;
        ServerThread st;
        Enumeration<String> keys = listUser.keys();
        
        while(keys.hasMoreElements()) {
            temp = keys.nextElement();
            st = listUser.get(temp);
            if(st.clientRoom.equals(room))  kq.append("|").append(temp);
        }
        
        if(kq.equals("")) return "|";
        return kq.toString();  
    }
    // tao socket gui file va khi gui xong tu dong dong file
    public void clientQuit() {
       
        if(clientName != null) {
            
            this.appendMessage("\n["+sdf.format(new Date())+"] Client \""+clientName+"\" is disconnected!");
            listUser.remove(clientName);
            if(listUser.isEmpty()) this.appendMessage("\n["+sdf.format(new Date())+"] Now there's no one is connecting to server\n");
            notifyToAllUsers("CMD_ONLINE_USERS|"+getAllUsers());
            notifyToUsersInRoom("CMD_ONLINE_THIS_ROOM"+getUsersThisRoom());
            notifyToUsersInRoom(clientName+" has quitted");
        }
    }
    //update room trong listuser
    public void changeUserRoom() {      
        ServerThread st = listUser.get(this.clientName);
        //st la server thread yeu cau doi room
        st.clientRoom = this.clientRoom;
        listUser.put(this.clientName, st);    
    }
    
    public void removeUserRoom() {
        ServerThread st = listUser.get(this.clientName);
        st.clientRoom = this.clientRoom;
        listUser.put(this.clientName, st);
        
    }
    
    @Override
    public void run() {
        try {
            //tao luong vao ra socket vs client
            bw = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            
            boolean isUserExist = true;
            String message, sender, receiver, fileName;
            StringBuffer str;
            String cmd, icon;
            while(true) {   //cho client gui va phan hoi
                try {
                    message = recieveFromClient();
                    tokenizer = new StringTokenizer(message, "|");
                    cmd = tokenizer.nextToken();
                    
                    switch (cmd) {
                        case "CMD_CHAT":
                            str = new StringBuffer(message);
                            str = str.delete(0, 9);
                          //this.clientName ten client gui tin
                            notifyToUsersInRoom("CMD_CHAT|" + this.clientName+"|"+str.toString());    
                            break;
                            
                        case "CMD_ChatFrame":
                            String privateSender = tokenizer.nextToken();
                            String privateReceiver = tokenizer.nextToken();
                            String messageContent = message.substring(cmd.length()+privateSender.length()+privateReceiver.length()+3, message.length());
                            
                            //ServerThread st_sender = listUser.get(privateSender);
                            ServerThread st_receiver = listUser.get(privateReceiver);
                            //sendToSpecificClient(st_sender, "CMD_PRIVATECHAT|" + privateSender + "|" + messageContent);
                            sendToSpecificClient(st_receiver, "CMD_PRIVATECHAT|" + privateSender + "|" + messageContent);
                            
                            System.out.println("[ServerThread] message = "+messageContent);
                            break;
                            
                        case "CMD_ROOM":
                            clientRoom = tokenizer.nextToken();
                            changeUserRoom();
                            notifyToAllUsers("CMD_ONLINE_USERS|"+getAllUsers());
                            notifyToUsersInRoom("CMD_ONLINE_THIS_ROOM"+getUsersThisRoom());
                            notifyToUsersInRoom(clientName+" has just entered!");
                            break;
                            
                        case "CMD_LEAVE_ROOM":
                            String room = clientRoom;
                            clientRoom = "";    //neu bang null thi loi
                            removeUserRoom();
                            notifyToUsersInRoom(room, "CMD_ONLINE_THIS_ROOM"+getUsersAtRoom(room));
                            notifyToUsersInRoom(room, clientName+" has just leaved this room!");      
                            
                            break;
                            
                        case "CMD_CHECK_NAME":
                            clientName = tokenizer.nextToken();
                            clientPass = tokenizer.nextToken();
                            isUserExist = listUser.containsKey(clientName);
                            
                            if(isUserExist) {  // nghia la co nguoi dang nhap vao nick do roi
                                sendToClient(NICKNAME_EXIST);
                            }
                            else {  //chua ai dang nhap
                                int kq = userDB.checkUser(clientName, clientPass);
                                if(kq == 1) {
                                    sendToClient(NICKNAME_VALID);
                                    //ten dung thi cho vao Hashtable va client
                                    this.appendMessage("\n["+sdf.format(new Date())+"] Client \""+clientName+"\" is connecting to server");
                                    listUser.put(clientName, this);     //them vao listuser
                                } else sendToClient(NICKNAME_INVALID);
                            }
                            break;
                            
                        case "CMD_SIGN_UP":
                            String name = tokenizer.nextToken();
                            String pass = tokenizer.nextToken();
                            System.out.println("name, pass = "+name+" - "+pass);
                            isUserExist = listUser.containsKey(name);
                            
                            if(isUserExist) {
                                sendToClient(NICKNAME_EXIST);
                            } else {
                                int kq = userDB.insertUser(new User(name, pass));
                                if(kq > 0) {
                                    sendToClient(SIGNUP_SUCCESS);
                                } else sendToClient(ACCOUNT_EXIST);
                            }
                            break;
                            
                        case "CMD_ONLINE_USERS":
                            sendToClient("CMD_ONLINE_USERS|"+getAllUsers());
                            notifyToUsersInRoom("CMD_ONLINE_THIS_ROOM"+getUsersThisRoom());
                            break;
                        
                        case "CMD_SENDFILE_REQUEST":    //gui tin request toi receiver
                            if(isBusy) {
                                sendToClient("CMD_SERVERISBUSY|server is busy");
                            } else {
                                isBusy = true;
                                sender = tokenizer.nextToken();
                                receiver = tokenizer.nextToken();
                                fileName = tokenizer.nextToken();

                                ServerThread threadOfReceiver = listUser.get(receiver);
                                if(threadOfReceiver != null) {
                                    sendToSpecificClient(threadOfReceiver, "CMD_SENDFILE_REQUEST|"+sender+"|"+receiver+"|"+fileName);
                                } else {
                                	//gui tin cho thang gui file de bao cao ko co ten nguoi dung
                                    sendToClient("CMD_RECEIVER_NOT_EXIST|There's no receiver named "+receiver);     
                                    isBusy = false;
                                }
                            }
                            break;
                        
                        case "CMD_SENDFILE_DENY":   //bao nguoi dc gui khong muon nhan file
                            sender = tokenizer.nextToken();
                            receiver = tokenizer.nextToken();
                            
                            ServerThread threadOfSender = listUser.get(sender);
                            sendToSpecificClient(threadOfSender, "CMD_SENDFILE_DENY|"+sender+"|"+receiver);
                            isBusy = false;
                            break;
                            
                        case "CMD_SENDFILE_I_AM_SENDER":
                            senderSocket = this.socketOfServer;
                            System.out.println("(server) CMD_SENDFILE_I_AM_SENDER");
                            senderName = tokenizer.nextToken();
                            break;
                            
                        case "CMD_SENDFILE_I_AM_THE_RECEIVER":
                            receiverSocket = this.socketOfServer;   //luu cho receiver
                            System.out.println("(server) CMD_SENDFILE_I_AM_THE_RECEIVER");
                            receiverName = tokenizer.nextToken();   //chua quan tam toi receiver
                            break;
                            
                        case "CMD_SENDFILE_ACCEPT":   //gui tin bao muon nhan file
                            sender = tokenizer.nextToken();
                            receiver = tokenizer.nextToken();
                            
                            threadOfSender = listUser.get(sender);
                            sendToSpecificClient(threadOfSender, "CMD_SENDFILE_ACCEPT|"+sender+"|"+receiver);
                            break;
                            
                        case "CMD_SENDFILETOSERVER":    //the sender sends a file to server:
                            System.out.println("(server) message = "+message);
                            sender = tokenizer.nextToken();
                            receiver = tokenizer.nextToken();
                            fileName = tokenizer.nextToken();
                            int len = Integer.valueOf(tokenizer.nextToken());
                            
                            // //thong bao cho receiver nhan file
                            sendToSpecificClient(receiverSocket, "CMD_SENDFILETOCLIENT|"+sender+"|"+receiver+"|"+fileName);
                            System.out.println("CMD_SENDFILETOCLIENT|"+sender+"|"+receiver+"|"+fileName);

                            // //gui lien tuc phan cua file tu sender toi receiver
                            InputStream is = senderSocket.getInputStream();   //lay luong vao tu sender
                            OutputStream os = receiverSocket.getOutputStream();    //lay luong ra tu  receiver

                            byte[] buffer = new byte[BUFFER_SIZE];
                            int count;
                            while((count = is.read(buffer)) > 0) {  //is doc xong luu vao buffer
                                os.write(buffer, 0, count);         //os lay buffer gui cho receiver
                            }

                            os.flush();
                            os.close();
                            is.close();
                            
                            isBusy = false;
                            break;
                            
                        case "CMD_ICON":
                            icon = tokenizer.nextToken();
                            notifyToUsersInRoom("CMD_ICON|"+icon+"|"+this.clientName);
                            break;
                            
                        default:
                            notifyToAllUsers(message);
                            break;
                    }
                    
                } catch (Exception e) {
                    clientQuit();
                    break;
                }
            }
        } catch (IOException ex) {
            clientQuit();
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.closeServerThread();
    }
}
