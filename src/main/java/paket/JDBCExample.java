package paket;

import java.sql.*;

public class JDBCExample {
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";


    public static void main(String[] args) {
        Connection con = null;
        CallableStatement cstmt;
        ResultSet rs;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("podkluchenie k baze...");
            String connectionUrl = "jdbc:sqlserver://217.29.21.60:6889;databaseName=KOVER-SAMOLET;user=sa;password=Afina954120";
            con = DriverManager.getConnection(connectionUrl);
            System.out.println("est konnect...");
            System.out.println("sozdanie zaprosa...");
            cstmt = con.prepareCall("{call xml_parser(?,?)}");
            cstmt.setString(1, "<xml><action>login</action><login>test</login><password>147852</password></xml>");
            cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            cstmt.execute();
            System.out.println(cstmt.getString(2));
            cstmt.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("PoKAAAA!");
    }
}