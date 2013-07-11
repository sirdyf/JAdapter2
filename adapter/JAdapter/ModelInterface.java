package JAdapter;

public interface ModelInterface extends ModelMainInterface{
	public int GetId(int modelRow);
	public String getColumnName(int column);
	public Object GetValues(int rowModel, String fieldName);
	public void setValueAt(Object value, int rowModel, int colModel);
	public void DeleteRow(int id);
	public void MarkRowDeleted(int id);
	public int AddRow();
	public void UpdateRow(int row);
	public int FindRowID(int id);
	public String[] GetColumnNames();
//	public void AddColumn(String colName, String type, String afterColumnName);
	public void AddColumnLast(String colName, String type);
//	public void DeleteColumn(String colName);
	public Class getColumnClass(int column);
}
