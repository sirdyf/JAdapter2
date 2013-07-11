/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

/**
 *
 * @author fomin_dmitry
 */
import java.sql.*;

public interface DBInterface {
    String getQueryText();
    String getTblName();
    String getPrmKey();
    void ConnectToExtern(JTableData jData,String Url, String UserName, String Password);
    void fillData(boolean isCreateNewTableData);//fl= true-(пере)создать модель, false-обновить данные(структура не меняется)
    void DeleteColumn(String colName);
    void AddColumn(String colName, String type, String afterColumnName);
    void AddColumnLast(String colName, String type);
    boolean AddRemoteData(String Url, String UserName, String Password);
    boolean SaveAllToTempDBTable(String tableName);
    void MarkRowDeleted(int id);
    void DeleteRow(int id);
    int AddRow();
    void UpdateRow(int id);
    Object dbRepresentation(int column, Object value);
}
