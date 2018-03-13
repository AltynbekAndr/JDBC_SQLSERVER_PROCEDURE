package paket;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MyTestThread extends Thread {
    String login;
    String password;
    StringBuffer result = null;
    PrintWriter printWriter = null;
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public MyTestThread(PrintWriter printWriter, String login, String password) {
        this.printWriter = printWriter;
        this.login = login;
        this.password = password;






    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
        String xml = "<xml><action>login</action><login>" + login + "</login><password>" + password + "</password></xml>";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://kover-samolet.333.kg/xml.php?utf=1");
        post.setHeader("User-Agent", null);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("xml", xml));
        HttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader rd = null;
        String line = "";
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs;

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("podkluchenie k baze...");
            String connectionUrl = "jdbc:sqlserver://217.29.21.60:6889;databaseName=KOVER-SAMOLET;user=sa;password=Afina954120";
            con = DriverManager.getConnection(connectionUrl);
            System.out.println("est konnect...");
            System.out.println("sozdanie zaprosa...");
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("GooooDBYE!");

        while (true) {

            try {
                    TimeUnit.SECONDS.sleep(1);
                    cstmt = con.prepareCall("{call xml_parser_new(?,?)}");
                    cstmt.setInt(1, 548);
                    cstmt.registerOutParameter(2, Types.INTEGER);
                    cstmt.execute();
                    /*rd = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()));
                    result = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }*/
                    if (cstmt.getString(2).equals("1")) {
                        printWriter.println(result.toString());
                    }


                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null)
                        con.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

        }
    }
}