/*
 * @author dyf 2012
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * http://download.oracle.com/javase/1.4.2/docs/api/javax/swing/JTable.html
 * http://download.oracle.com/javase/1.4.2/docs/api/javax/swing/table/TableModel.html
 * http://mindprod.com/jgloss/jtable.html
 * http://mindprod.com/jgloss/calendar.html
 * http://dmivic.chat.ru/JDBC/introTOC.doc.html
 * http://www.javaportal.ru/java/articles/JDBC_java_BD.html
 * http://www.javable.com/tutorials/fesunov/lesson8/
 * http://habrahabr.ru/blogs/java/75661/ - Public Morosov
 * http://download.oracle.com/javase/tutorial/reflect/member/ctorLocation.html - поиск конструктора
 * http://www.quizful.net/post/java-reflection-api
 * Class.getDeclaredConstructor (String.class). NewInstance ( "HERESMYARG");
 * Class.getDeclaredConstructors(types list).newInstance(args list);
 */
package JAdapter;

import autopartner.Asrt;
import autopartner.Tools;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

// Класс для работы с БД,связи БД-модель и грида.
public class JDBCAdapter extends AbstractTableModel implements ModelInterface {

    protected JTableData jtData = null;//массив данных
    private static Logger log = Logger.getLogger(ConnManager.class.getName());
    private static final long serialVersionUID = 1L;
    protected boolean blockWorkWithDB = false;
    protected boolean isTableEditable = true;
    protected String nameConnect = "";
    protected DBInterface dbTable = null;//интерфейс для работы с БД
    // --------------------------------Constructors----------------

    private JDBCAdapter() {
        super();
    }
// -------------- public constructors ------------------------
    // connect to local
    public JDBCAdapter(String TableName_, String PrimaryKey_, String Query_) {
        this();
        log.fine("try get connect...");
        this.dbTable = new JDBTable(TableName_,PrimaryKey_,Query_);
        this.jtData = new JTableData();
        log.fine("costructor with parapetrs:" + TableName_ + "," + PrimaryKey_ + "," + Query_);
//        JDBCAdapterConnect(TableName, PrimaryKey, Query);
        this.CreateModel(true);//пробуем создать таблицу
    }
// connect to external
    public JDBCAdapter(String Url, String UserName, String Password,
            String TableName, String PrimaryKey, String Query) {
        super();
        //driverName=DrvName;
        log.info("Full construcotr for Remote access.");
        log.info("Try create model");
        boolean r=false;
        this.dbTable = new JDBTable(TableName,PrimaryKey,Query);
        this.jtData = new JTableData();
        
        this.dbTable.ConnectToExtern(this.jtData,Url,UserName,Password);
        if (r){
            this.CreateModel(true);//пробуем создать таблицу
        }else{
            log.warning("error: connect("+TableName+","+PrimaryKey+","+Query+")");
        }

    }
//---------------------------------Others protected--------------------------

    protected int GetMaxID() {
        int max = 0, curID = 0;
        int col = this.GetColumnIndex(this.dbTable.getPrmKey());
        for (int i = 0; i < getRowCount(); i++) {
            curID = this.GetId(col);
            if (max < curID) {
                max = curID;
            }
        }
        return max;
    }

    protected Vector<Object> GetHeader() {
        Vector<Object> head = new Vector();
        for (int i = 0; i < this.getColumnCount(); i++) {
            head.addElement(this.jtData.columnNames[i]);
        }
        return head;
    }

    protected String GetHeaderString() {
        String head = "";
        for (int i = 0; i < this.getColumnCount(); i++) {
            head = head + " " + this.getColumnName(i);
        }
        return head;
    }

    protected Vector<Object> GetAllData() {
        Vector<Object> resSet = new Vector();
        for (int i = 0; i < this.jtData.rows.size(); i++) {
            resSet.addElement(this.jtData.rows.get(i));
        }
        return resSet;
    }

    protected void CreateModel(boolean newTable) {//fl= true-(пере)создать модель, false-обновить данные(структура не меняется)

        this.dbTable.fillData(newTable);

        if (newTable == true) {
            fireTableStructureChanged();
        } else {
            fireTableDataChanged();
        }

    }

    protected int GetInt(int row, int col) {
        int id = 0;
        Object tmpO = getValueAt(row, col);
        if (tmpO == null) {
            return -1;
        }
        try {
            id = new Integer(tmpO.toString());
        } catch (Exception e) {
            System.err.println("error convert:" + e);
        }
        return id;
    }

    protected Vector<Object> CreateEmptyRow() {
        Vector<Object> tmpObj = new Vector();
        for (int i = 0; i < this.jtData.vColObject.size(); i++) {
            tmpObj.addElement(null);//vColObject.get(i));
        }
        return tmpObj;
    }

    protected String GetStringAt(int row, int col) {
        if ((col < 0) || (col > getColumnCount())) {
            return "Null";
        }
        if ((row < 0) || (row > getRowCount())) {
            return "Null";
        }
        Object obj = getValueAt(row, col);
        if (obj == null) {
            return "Null";
        }
        return obj.toString();
    }

    public void DeleteColumn(String colName) {
        this.dbTable.DeleteColumn(colName);
        this.CreateModel(true); //БД изменилась - перечитать данные, чтобы обновить таблицу и модель
    }

    public void AddColumn(String colName, String type, String afterColumnName) {//пример "CHAR(10) NULL" или "FLOAT(8,2) DEFAULT 0"
        this.dbTable.AddColumn(colName, type, afterColumnName);
        this.CreateModel(true); //БД изменилась - перечитать данные, чтобы обновить таблицу и модель
    }

    public void AddColumnLast(String colName, String type) {//пример "CHAR(10) NULL" или "FLOAT(8,2) DEFAULT 0"
        this.dbTable.AddColumnLast(colName, type);
        this.CreateModel(true); //БД изменилась - перечитать данные, чтобы обновить таблицу и модель
    }

    public void UpdateSructure() {
        this.CreateModel(true);
    }

    public boolean AddRemoteData(String Url, String UserName, String Password) {
        this.dbTable.AddRemoteData(Url, UserName, Password);
        this.blockWorkWithDB = true;//теперь обновлять и т.п. нельзя,т.к. потеряем результат объединения
        fireTableDataChanged();
        return true;
    }

    /**
     * Добавить данные из другой таблицы
     * @param data - произаольная модель(TableModel)
     * @return - истина,если данные добавились
     * @see "добавляет только,если совпадают имена столбцов!"
     */
    public boolean AddData(TableModel data) {
        if (data.getRowCount() < 1) {
            return false;
        }
//итак,есть 2 табле модел,надо их объединить:
//1.ищем совпадающие колонки
        Map<Integer, String> columnNum = new HashMap<Integer, String>();
        //List<String> columnName=new ArrayList();
        String name1 = "", name2 = "";
        for (int i = 0; i < this.getColumnCount(); i++) {
            name1 = this.getColumnName(i);
            for (int j = 0; j < data.getColumnCount(); j++) {
                name2 = data.getColumnName(j);
                if (name1.equals(name2)) {
                    //columnName.add(name1);
                    columnNum.put(j, name2);//запоминаем индекс и имя столбца
                    break;
                }
            }
        }
        if (columnNum.isEmpty() == true) {
            return false;
        }
//2.добавляем данные
        //Iterator Iter=columnName.iterator();
        Vector newRow = null;
        Object tmpObj = null;
        for (int iRow = 0; iRow < data.getRowCount(); iRow++) {//проходим по строкам
            newRow = this.CreateEmptyRow();
            for (Map.Entry<Integer, String> entry : columnNum.entrySet()) {//перебираем все совпавшие колонки
                tmpObj = data.getValueAt(iRow, entry.getKey());//получаем значение
                newRow.setElementAt(tmpObj, entry.getKey());//заполняем
            }
            this.jtData.rows.addElement(newRow);//добавляем новую,заполненную строку

            int numID = this.GetMaxID() + 1;//текущий максимум

            int prmColumn = this.GetColumnIndex(this.dbTable.getPrmKey());
            this.setValueAt(numID + 1, this.getRowCount(), prmColumn);
        }
        this.blockWorkWithDB = true;//теперь обновлять и т.п. нельзя,т.к. потеряем результат объединения
        return true;
    }

    /**
     * Сохраняет текущие данные во временную таблицу БД,используя текущее соединение
     * @param name - имя временной таблицы в БД
     * @return - истина в случае удачи
     * @see "Если накидать данные и сохранить их,то можно в последствии сделать по ним выборку для группировки и сортировки"
     */
    public boolean SaveAllToTempDBTable(String tableName) {
        this.dbTable.SaveAllToTempDBTable(tableName);
        return true;
    }

    /**
     * Ищет строку по id
     * @param id
     * @return возвращает строку
     */
    public int FindRowID(int id) {
        if (this.getRowCount() < 1) {
            Asrt.perr("Нельзя найти,пустая таблица!");
            return -1;//предупреждение - пустая таблица!
        }
        //получаем номер колонки с первичным ключом
        int colPrimaryKey = GetColumnIndex(this.dbTable.getPrmKey());
        //проходим по всей колонке
        Object tmpObj = null;
        Object Id = id;
        for (int i = 0; i < getRowCount(); i++) {
            tmpObj = getValueAt(i, colPrimaryKey);
            if (tmpObj != null) {
                if (tmpObj.equals(Id)) {
                    return i;
                }
            }
        }
        Asrt.perr("Не найден ИД=" + id);
        return -1;
    }
//---------------------------- GET -----------------------------------

    /**
     * Возвращает столбец
     * @param name-имя столбца
     * @return вектор-столбец
     */
    public Vector<String> GetColumnDataVector(String name) {
        Vector<String> strList = new Vector<String>();

        if (Asrt.is_str(name) == false) {
            return strList;
        }
//        if (name == null) {
//			Asrt.perr("Collumn "+name + " not found!");
//            return strList;
//        }
//
//        if (name.length() == 0) {
//            return strList;
//        }

        int col = GetColumnIndex(name);

        if (col < 0) {
            Asrt.perr("Column " + name + " not found");
            return strList;
        }

        String str = null;
        Object obj = null;
        for (int i = 0; i < getRowCount(); i++) {
            obj = getValueAt(i, col);
            str = null;
            if (obj != null) {
                str = obj.toString();
            }
            if (str == null) {
                strList.add("Null");
            } else {
                strList.add(str); //проверить работу при получении null от гета!
            }
        }
        return strList;
    }

    /**
     * Получить ИД строки по номеру строки
     * @param rowModel - номер строки
     * @return ИД строки
     */
    public int GetId(int rowModel) {
        if (Asrt.is_int_negative(rowModel) == true) {
            return -1;
        }
        int colModel = GetColumnIndex(this.dbTable.getPrmKey());
        int res = GetInt(rowModel, colModel);
        return res;
    }

    /**
     * Получить значение ячейки модели
     * @param rowModel - строка
     * @param fieldName - имя столбца
     * @return значение ячейки
     */
    public Object GetValues(int rowModel, String fieldName) {
        int colModel = GetColumnIndex(fieldName);
        return getValueAt(rowModel, colModel);
    }

    public TableModel GetModel() {
        return this;
    }

    /**
     * получить номер столбца по имнени
     * @param colName - имя столбца
     * @return номер столбца
     */
    public int GetColumnIndex(String colName) {
        if (Asrt.is_str(colName) == false) {
            return -1;
        }
        String clName = null;
        for (int col = 0; col < getColumnCount(); col++) {
            clName = getColumnName(col);
            if (clName.equalsIgnoreCase(colName)) {
                return col;
            }
        }
        //Asrt.perr("Не найден столбец:" + colName);
        return -1;
    }

    public final String[] GetColumnNames() {
        return this.jtData.columnNames;//не безопасно - возможно поменять извне

    }
//-------------------------- SET --------------------------------------

    public void SetTableEditable(boolean flag) {
        isTableEditable = flag;
    }

    public void SetColumnEditable(boolean flag, String columnName) {
        int col = this.GetColumnIndex(columnName);
        if (Asrt.is_int_negative(col) == true) {
            return;
        }
        this.jtData.bIsEditableColumn[col] = flag;
    }

//----------------- =Ф-ции обновляющие данные таблицы из БД= ----------------------
    public void MarkRowDeleted(int id) {
        if (this.blockWorkWithDB == true) {
            return;
        }
        this.dbTable.MarkRowDeleted(id);
        this.Update();//БД изменилась - перечитать данные,чтобы обновить таблицу и модель
    }

    /**
     * Удаляет строку по id
     * @param id
     */
    public void DeleteRow(int id) {
        if (this.blockWorkWithDB == true) {
            return;
        }

        if (Asrt.is_int_negative(id) == true) {
            return;
        }
        this.dbTable.DeleteRow(id);
        this.Update();//БД изменилась - перечитать данные,чтобы обновить таблицу и модель
    }

    /**
     * Добавить пустую строку
     * @return возвращает ИД!
     */
    public int AddRow() {
        if (this.blockWorkWithDB == true) {
            return -1;
        }
        fireTableDataChanged();
        return this.dbTable.AddRow();
    }

    public void UpdateRow(int id) {
        if (this.blockWorkWithDB == true) {
            return;
        }
        this.dbTable.UpdateRow(id);
        fireTableDataChanged();
    }

    public void Update() {
        if (this.blockWorkWithDB == true) {
            return;
        }

        CreateModel(false);
    }
//-----------=Интерфейс TableModel=------------------------------------------

    @Override
    public Class getColumnClass(int column) {
        // @Override
        // public Class<?> getColumnClass(int col) {
        // Class<?> c = Object.class;
        // try {
        // c = (Class<?>) vColObject.get(col);
        // } catch (RuntimeException e) {
        // System.err.println(e);
        // }
        // return c;
        // }
        if ((column < 0) || (column > getColumnCount())) {
            System.err.println("getColumnClass error: column=" + column);
            return String.class;
        }
        return JDBTable.getColClass(jtData.columnType[column]);
        //return vColObject.get(column).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if ((column < 0) || (column > getColumnCount())) {
            return false;
        }
        if ((row < 0) || (row > getRowCount())) {
            return false;
        }
        return this.jtData.bIsEditableColumn[column] && isTableEditable;
    }

    @Override//Возвращает количество полей таблицы
    public int getColumnCount() {
        return jtData.columnNames.length;
    }

    @Override//Возвращает количество записей
    public int getRowCount() {
        return jtData.rows.size();
    }

    @Override
    public String getColumnName(int column) {
        if ((column < 0) || (column > getColumnCount())) {
            return "null";// возвращаем строку!
        }
        return new String(jtData.columnNames[column]);// проверить!!
    }

    @Override//Возвращает значение ячейки
    public Object getValueAt(int aRow, int aColumn) {
        if ((aColumn < 0) || (aColumn > getColumnCount())) {
            return "getValueAt num column error";
        }
        if ((aRow < 0) || (aRow > getRowCount())) {
            return "getValueAt num column error";
        }

        Vector row = (Vector) jtData.rows.elementAt(aRow);
        Object t = null;//new Object();
        t = row.elementAt(aColumn);

        return t;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {//редактирует только this.tableName;

        if ((column < 0) || (column > getColumnCount())) {
            return;
        }
        if ((row < 0) || (row > getRowCount())) {
            return;
        }

        // PreparedStatement pstmt = connection.prepareStatement(query);
        // pstmt.executeUpdate();  // выполнить изменение
        // pstmt.close();
        // теперь осталось, изменить значение вектора  rows
        Object val;
        //без этой функции работать не  будет,если в setElementAt() вставлять непосредственно value а не val
        val = this.dbTable.dbRepresentation(column, value);
        Vector dataRow = (Vector) jtData.rows.elementAt(row);
        dataRow.setElementAt(val, column);
    }
};
