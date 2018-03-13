package paket;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
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


        Connection connection = null;
        CallableStatement cstmt = null;
        try{
            ComboPooledDataSource dataSource = DatabaseUtility.getDataSource();
            connection = dataSource.getConnection();
        }
        catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


        while (true) {

            try {
                    TimeUnit.SECONDS.sleep(1);
                    cstmt = connection.prepareCall("{call xml_parser_new(?,?)}");
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
                        System.out.println("cstmt.getString(2) :"+cstmt.getString(2));
                        printWriter.println(cstmt.getString(2));
                    }


                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}