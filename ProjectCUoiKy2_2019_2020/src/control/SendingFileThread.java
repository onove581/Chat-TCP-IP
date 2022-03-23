package control;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

//Thread gui file llen server
public class SendingFileThread extends Thread {

    String sender, receiver;
    String filePath;
    Socket socketOfSender;
    BufferedWriter bw;
    BufferedReader br;
    JProgressBar progressBar;
    SendFileFrame frameToDisplayDialog;

    private final int BUFFER_SIZE = 100;
    
    public SendingFileThread(String sender, String receiver, String filePath, Socket socket, SendFileFrame frameToDisplayDialog, JProgressBar progressBar) {
        this.sender = sender;
        this.receiver = receiver;
        this.filePath = filePath;
        this.socketOfSender = socket;
        this.frameToDisplayDialog = frameToDisplayDialog;
        this.progressBar = progressBar;
        
        try {
            bw = new BufferedWriter(new OutputStreamWriter(socketOfSender.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socketOfSender.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(SendingFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void sendToServer(String line) {
        try {
            this.bw.write(line);
            this.bw.newLine();   //dung newline sau do dung  readLine()
            this.bw.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(null, "Server is close, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String recieveFromServer() {
        try {
            return this.br.readLine();  
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public void run() {
        try {
            File file = new File(filePath);     
            int leng = (int) file.length();     
            
            this.sendToServer("CMD_SENDFILETOSERVER|"+sender+"|"+receiver+"|"+file.getName()+"|"+leng);
            
            System.out.println("[SendingFileThread.java] CMD_SENDFILETOSERVER|"+sender+"|"+receiver+"|"+file.getName()+"|"+leng);   
            
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));       
            OutputStream out = socketOfSender.getOutputStream();    
            
            byte []buffer = new byte[BUFFER_SIZE];
            int count=0, percent=0;
            while((count = bis.read(buffer)) > 0) {
                
                out.write(buffer, 0, count);    //gui tung phan cua file
                
            }
            out.flush();
            
            out.close();
            bis.close();
            
            JOptionPane.showMessageDialog(frameToDisplayDialog, "File successfully sent!", "Sucess", JOptionPane.INFORMATION_MESSAGE);
            socketOfSender.close();
        } catch (IOException ex) {
            Logger.getLogger(SendingFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

