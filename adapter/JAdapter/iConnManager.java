/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import java.sql.Connection;

/**
 *
 * @author fomin_dmitry
 */
public interface iConnManager {

    Connection CreateExternConnection(String addr, String name, String pass);

    Connection GetConnection();

    String GetLogin();

    String GetPassword();

    String GetUrl();
    
    //ConnManager getInstance();
}
