/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import autopartner.Cfg;
import java.sql.*;
import java.util.logging.Logger;

/**
 *
 * @author dima
 */
public class ConnManager implements iConnManager {

    private static final Logger log = Logger.getLogger(ConnManager.class.getName());
//    private static ConnManager _instance = null;
    private Cfg cfg;
    private String url = null, user = null, passwd = null;
    final private static String driverNameAP74 = "com.mysql.jdbc.Driver";
    private static Connection connection = null;

    protected ConnManager() {}
    
    private void CreateDefaultConnection(){
        log.info("Create connection:");
        cfg = Cfg.getInstance();
        url = cfg.Get("UrlDB");
        user = cfg.Get("LoginBD");
        passwd = cfg.Get("PasswordBD");
        log.fine(url + " : " + user + " : " + passwd);
        try {
            log.info("Try connecting...");
            Class.forName(driverNameAP74);//пытаемся узнать существует ли такой драйвер
            //если нет вывыливаемся по эксепшену
            //System.err.println("Opening db connection");

            ConnManager.connection = DriverManager.getConnection(url, user, passwd);
            log.info("..Ok");
        } catch (ClassNotFoundException ex) {
            log.warning("Cannot find the database driver classes.");
            log.throwing("ConnManager", "constructor", ex);
            System.err.println(ex);
        } catch (SQLException ex) {
            log.warning("Cannot connect to this database.");
            log.throwing("ConnManager", "constructor", ex);
        } catch (Exception e) {
            log.warning("Cannot create model.");
            log.throwing("ConnManager", "constructor", e);
        }
        log.info("Remote connection...");
    }

//    protected synchronized ConnManager getInstance() {
//        if (_instance == null) {
//            _instance = new ConnManager();
//            _instance.CreateDefaultConnection();
//        }
//        return _instance;
//    }
//------------------------PUBLIC

    @Override
    public synchronized Connection GetConnection() {
        if (ConnManager.connection == null){
            this.CreateDefaultConnection();
        }
        return ConnManager.connection;
    }

    @Override
    public Connection CreateExternConnection(String addr, String name, String pass) {

        Connection c = null;

        try {
            //Class.forName(driverNameAP74);

            c = DriverManager.getConnection(addr, name, pass);

//        } catch (ClassNotFoundException ex) {
//            System.err.println("Cannot find the database driver classes.");
//            System.err.println(ex);
            log.info("..Ok");
        } catch (SQLException ex) {
            System.err.println("Cannot connect to this database.");
            System.err.println(ex);
        } catch (Exception e) {
            System.err.println("Cannot create model.");
            System.err.println(e);
        }
        return c;
    }

    @Override
    public String GetUrl() {
        return new String(this.url);
    }

    @Override
    public String GetLogin() {
        return new String(this.user);
    }

    @Override
    public String GetPassword() {
        return new String(this.passwd);
    }
}
