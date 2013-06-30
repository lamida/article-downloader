/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lamida.restui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.lamida.rest.Job;
import net.lamida.rest.RestParameter;

/**
 *
 * @author lamida
 */
public class RestUi {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("config.properties")));
        String endPoint = properties.getProperty("guardian.article.api_endpoint");
        String apiKey = properties.getProperty("guardian.article.api_key");
        String format = properties.getProperty("guardian.article.api_response_format");
        RestParameter param = new RestParameter();
        param.setEndPoint(endPoint);
        param.setApiKey(apiKey);
        param.setFormat(format);
        param.setProvider("The Guardian");
        param.setOrderByList("newest, oldest, relevance");
        Map<String, RestParameter> parameters = new HashMap<String, RestParameter>();
        parameters.put(param.getProvider(), param);
        
        // add more provider
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        final Main ui = new Main(parameters);
        ui.init();
    }

    /**
     * @param args the command line arguments
     */
    public static void main2(String[] args) {
        String endPoint = "http://content.guardianapis.com/search";
        String apiKey = "827p92h8prnwjrnh7z7qr94j";
        String format = "json";
        String pageSize = "50";
        RestParameter param = new RestParameter();
        param.setEndPoint(endPoint);
        param.setApiKey(apiKey);
        param.setFormat(format);
        param.setPageSize(pageSize);
        param.setProvider("The Guardian");
        param.setOrderByList("newest, oldest, relevance");
        Map<String, RestParameter> parameters = new HashMap<String, RestParameter>();
        parameters.put(param.getProvider(), param);


        // add more provider
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        final Main ui = new Main(parameters);
        ui.init();

    }
}
