package paket;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DatabaseUtility {
    static String connectionUrl = "jdbc:sqlserver://217.29.21.60:6889;databaseName=KOVER-SAMOLET;user=sa;password=Afina954120";
    public static ComboPooledDataSource getDataSource() throws PropertyVetoException{

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(connectionUrl);
        /*cpds.setUser("root");
        cpds.setPassword("password");*/

        // Optional Settings
        cpds.setInitialPoolSize(5);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(40);
        cpds.setMaxStatements(200);

        return cpds;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try
        {
            ComboPooledDataSource dataSource = DatabaseUtility.getDataSource();
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement("SELECT * FROM blacklist");

            System.out.println("The Connection Object is of Class: " + connection.getClass());

            resultSet = pstmt.executeQuery();
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
            }

        }
        catch (Exception e)
        {
            connection.rollback();
            e.printStackTrace();
        }
    }
}
