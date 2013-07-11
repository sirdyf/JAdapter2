/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import java.sql.Connection;
import java.util.Vector;
import javax.swing.table.TableModel;

/**
 *
 * @author dima
 */
public interface ModelMainInterface {
	public void SetTableEditable(boolean flag);
	public void SetColumnEditable(boolean flag, String columnName);
	public int GetColumnIndex(String fieldName);
	public TableModel GetModel();
	public  Vector<String> GetColumnDataVector(String name);
	public void Update();
	public int getColumnCount();
	public int getRowCount();
	public boolean AddRemoteData(String Url,String UserName,String Password);//либо забираем удалённые данные сами
	public boolean AddData(TableModel data);//либо где то их храним и добавляем
	public boolean SaveAllToTempDBTable(String name);//сохраняем все данные во временную таблицу
    public void AddColumn(String colName, String type, String afterColumnName);//добавить колонку
	public void DeleteColumn(String colName);
//    public Connection GetConnection();
	public String[] GetColumnNames();
	public void AddColumnLast(String colName, String type);
	public void UpdateSructure();
	public Class getColumnClass(int column);
}
