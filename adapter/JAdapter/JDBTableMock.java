/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import java.sql.Connection;
import java.sql.Types;
import java.util.Vector;

/**
 *
 * @author afaneor
 */
public class JDBTableMock implements DBInterface{

//    Connection connection = null;
    protected String _query;//изменить область видимости
    protected String tblName = "";// имя таблицы для setValueAt(..)
    protected String prmKey;//ключевое поле
    protected JTableData jData = null;
    
    public JDBTableMock(String tableName,String primaryKey,String query) {
        _query = query;
        tblName = tableName;
        prmKey = primaryKey;
    }
    
    @Override
    public String getQueryText() {
        return _query;
    }

    @Override
    public String getTblName() {
        return tblName;
    }

    @Override
    public String getPrmKey() {
        return prmKey;
    }

    @Override
    public void ConnectToExtern(JTableData jData, String Url, String UserName, String Password) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fillData(boolean isCreateNewTableData) {
        jData.columnNames = new String[2];// здесь будут содержаться названия полей таблицы БД
        jData.columnNames[0]="id";
        jData.columnNames[1]="data";
        
        jData.bIsEditableColumn = new boolean[2];
        jData.bIsEditableColumn[0]=false;
        jData.bIsEditableColumn[1]=false;
        
        jData.columnType = new int[2];//тип содержимого колонки
        jData.columnType[0]=Types.INTEGER;
        jData.columnType[0]=Types.CHAR;

        jData.vColObject = new Vector<Object>();//классы колонок
        jData.vColObject.add(new Integer("1"));
        jData.vColObject.add("stub");
        
        jData.rows = new Vector();// здесь будут  храниться все данные таблицы БД
        Vector newRow = new Vector();
        newRow.addElement(1);
        newRow.addElement("row-1");
        jData.rows.addElement(newRow);
        newRow = new Vector();
        newRow.addElement(2);
        newRow.addElement("row-2");
        jData.rows.addElement(newRow);
    }

    @Override
    public void DeleteColumn(String colName) {}

    @Override
    public void AddColumn(String colName, String type, String afterColumnName) {    }

    @Override
    public void AddColumnLast(String colName, String type) {    }

    @Override
    public boolean AddRemoteData(String Url, String UserName, String Password) {
        return true;
    }

    @Override
    public boolean SaveAllToTempDBTable(String tableName) {
        return true;
    }

    @Override
    public void MarkRowDeleted(int id) {    }

    @Override
    public void DeleteRow(int id) {    }

    @Override
    public int AddRow() {
        return 1;
    }

    @Override
    public void UpdateRow(int id) {    }

    @Override
    public Object dbRepresentation(int column, Object value) {
        return "stub";
    }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
