package server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;

public class UserDatabase {
	private Connection conn;
	private final String data_name="chat";
	  public final String username = "root";
	    public final String pas = "";
	    public final String URL_MYSQL = "jdbc:mysql://localhost:3306/"+data_name;
	    public final String USER_TABLE = "user_tb";
	    
	    private PreparedStatement pst;
	    private ResultSet rs;
	    private Statement st;
	    public Connection connect() {
	    	try {
	    		//goi tep data
	    		Class.forName("com.mysql.jdbc.Driver");     
	            
	            conn = (Connection) DriverManager.getConnection(URL_MYSQL, username, pas);
	            System.out.println("Connect successfull");
			} catch (SQLException ex) {
				Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	            System.out.println("Khong the ket noi!");
			}catch (ClassNotFoundException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	        }
			return conn;
	    	
	    }
	    public ResultSet getData() {
	        try {
	            st = conn.createStatement(); 
	            rs = st.executeQuery("SELECT * FROM "+USER_TABLE);
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	        return rs;
	    }
	    private void showData() {
	        rs = getData();
	        try {
	            while(rs.next()) {
	                System.out.printf("%-15s %-4s", rs.getString(1), rs.getString(2));
	                System.out.println("");
	            }
	            
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    public int insertUser(User u) {
	        System.out.println("Before: name = "+u.name+" - pass = "+u.pass);
	        try {
	            pst = conn.prepareCall("INSERT INTO "+USER_TABLE+" VALUES ('"+u.name+"', '"+u.pass+"')");
	            int kq = pst.executeUpdate();
	            if(kq > 0) System.out.println("Insert successful!");
	            System.out.println("After: name = "+u.name+" - pass = "+u.pass);
	            return kq;
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return -1;
	    }
	    public int createUser(User u) {
	        try {
	            pst = conn.prepareStatement("INSERT INTO "+USER_TABLE+" VALUE(?,?);");
	            pst.setString(1, u.name);
	            pst.setString(2, u.pass);
	            return pst.executeUpdate();
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return -1;
	    }
	    public int checkUser(String name, String pass) {    //return 1 = account is correct
	        try {
	            pst = conn.prepareStatement("SELECT * FROM "+USER_TABLE+" WHERE name = '" + name + "' AND pass = '" + pass +"'");
	            rs = pst.executeQuery();
	            
	            if(rs.first()) {
	                //user and pass is correct:
	                return 1;
	            }
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	            
	        }
	        return 0;
	    }
	    
	    public void closeConnection() {
	        try {
	            if(rs!=null) rs.close();
	            if(pst!=null) pst.close();
	            if(st!=null) st.close();
	            if(conn!=null) conn.close();
	        } catch (SQLException ex) {
	            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
	            System.out.println("Close Server");
	        }
	    }
	    
	    public static void main(String[] args) {
	        UserDatabase ud = new UserDatabase();
	        ud.connect();
	        ud.showData();
	        ud.closeConnection();
	        
	        System.out.println("============");
	        ud.connect();
	        ud.insertUser(new User("b", "1"));
	        ud.showData();
	    }

}
