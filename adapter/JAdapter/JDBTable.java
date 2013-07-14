/*
 * @author dima
 */
package JAdapter;

import autopartner.Asrt;
import autopartner.Tools;
import java.sql.*;
import java.util.Vector;
import java.text.SimpleDateFormat;

public class JDBTable implements DBInterface {
// позволяет получить соединение с базой(один connection на весь сеанс работы приложения)
    JAdapter.iConnManager conMan = null;

    public void setConMan(ConnManager conMan) {
        this.conMan = conMan;
    }
    //текущее соединение с базой. Не обязательно = соединению севнса, т.к. может быть внешним соединением
    Connection connection = null;
    protected String _query;//изменить область видимости
    protected String tblName = "";// имя таблицы для setValueAt(..)
    protected String prmKey;//ключевое поле
    protected JTableData jData = null;

    public static Class getColClass(int type) {//для рендера или сравнения при сортировке?
        String cname;

        switch (type) {
            case Types.BIT: {
                cname = "java.lang.Boolean";
                break;
            }
            case Types.TINYINT: {
                cname = "java.lang.Byte";
                break;
            }
            case Types.SMALLINT: {
                cname = "java.lang.Short";
                break;
            }
            case Types.INTEGER: {
                cname = "java.lang.Integer";
                break;
            }
            case Types.BIGINT: {
                cname = "java.lang.Long";
                break;
            }
            case Types.FLOAT:
            case Types.REAL: {
                cname = "java.lang.Float";
                break;
            }
            case Types.DOUBLE: {
                cname = "java.lang.Double";
                break;
            }
            case Types.NUMERIC: {
                cname = "java.lang.Number";
                break;
            }
            case Types.DECIMAL: {
                cname = "java.math.BigDecimal";
                break;
            }
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR: {
                cname = "java.lang.String";
                break;
            }
            case Types.DATE: {
                cname = "java.sql.Date";
                break;
            }
            case Types.TIME: {
                cname = "java.sql.Time";
                break;
            }
            case Types.TIMESTAMP: {
                cname = "java.util.Date"; //sql.Timestamp";
                break;
            }
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY: {
                cname = "byte[]";
                break;
            }
            case Types.OTHER:
            case Types.JAVA_OBJECT: {
                cname = "java.lang.Object";
                break;
            }
            case Types.CLOB: {
                cname = "java.sql.Clob";
                break;
            }
            case Types.BLOB: {
                cname = "java.ssql.Blob";
                break;
            }
            case Types.REF: {
                cname = "java.sql.Ref";
                break;
            }
            case Types.STRUCT: {
                cname = "java.sql.Struct";
                break;
            }
            default: {
                cname = "java.lang.String";
                break;
                //return super.getColumnClass(column);
            }
        }
        try {
            return Class.forName(cname);
        } catch (Exception e) {
            //e.printStackTrace();
            return String.class;
        }
    }

    public JDBTable(String tableName,String primaryKey,String query) {
        _query = query;
        tblName = tableName;
        prmKey = primaryKey;
        
        conMan = new ConnManager();
        connection = conMan.GetConnection();
    }
    
    // для нового соединения нужно передать новый,пустой JTableData
 
    // ResultSetMetaData must have more one column!
    private void extractMetadata(final ResultSetMetaData metaData) throws SQLException {
        if (metaData == null) {
            System.err.println(this.getClass().toString()+" extractMetadata() argument is null");
            return ;
        }
        Object tmpObj = null;
        int numberOfColumns = metaData.getColumnCount();
        if (numberOfColumns<2) return ;
        
        jData.columnNames = new String[numberOfColumns];
        jData.bIsEditableColumn = new boolean[numberOfColumns];
        jData.columnType = new int[numberOfColumns];
        jData.vColObject = new Vector<Object>();
        for (int column = 0; column < numberOfColumns; column++) {
            jData.columnNames[column] = metaData.getColumnLabel(column + 1);
            jData.columnType[column] = metaData.getColumnType(column + 1);

            Class cl = getColClass(jData.columnType[column]);//metaData.getColumnClassName
            jData.bIsEditableColumn[column] = metaData.isWritable(column + 1);
            tmpObj = dbRepresentation(column, "0");
            if (tmpObj == null) {
                System.err.println("extractMetadata error:can`t create column type-" + cl.getName());
                jData.vColObject.add("error");
            } else {
                jData.vColObject.add(tmpObj);
            }
        }
    }

    protected void getDBdata(final ResultSet resultSet) throws SQLException {
        Object tmpObj = null, dbObj = null;
        jData.rows = new Vector();
        while (resultSet.next()) {
            Vector newRow = new Vector();
            int cc = this.jData.columnNames.length;
            for (int i = 1; i <= cc; i++) {
                dbObj = resultSet.getObject(i);
                tmpObj = dbRepresentation(i - 1, dbObj);
                if (tmpObj == null) {
                    newRow.addElement(new String("error"));
                } else {
                    newRow.addElement(tmpObj);
                }
            }
            jData.rows.addElement(newRow);
        }
    }
    
    public void setPrmKey(String prmKey) {
        this.prmKey = prmKey;
    }

    protected String GetColumnAsDBtype(int column) {
        Class colClass = this.getColClass(column);

        if (colClass == java.lang.Boolean.class) {
            return "BIT";
        }
        if (colClass == java.lang.Byte.class) {
            return "TINYINT";
        }
        if (colClass == java.lang.Short.class) {
            return "SMALLINT";
        }
        if (colClass == java.lang.Integer.class) {
            return "INTEGER";
        }
        if (colClass == java.lang.Long.class) {
            return "BIGINT";
        }
        if (colClass == java.lang.Float.class) {
            return "FLOAT";//REAL
        }
        if (colClass == java.lang.Double.class) {
            return "DOUBLE";
        }
        if (colClass == java.lang.Number.class) {
            return "NUMERIC";
        }
        if (colClass == java.math.BigDecimal.class) {
            return "DECIMAL";
        }
        if (colClass == java.lang.String.class) {
            return "CHAR(100)";//VARCHAR LONGVARCHAR
        }
        if (colClass == java.sql.Date.class) {
            return "DATE";
        }
        if (colClass == java.sql.Time.class) {
            return "TIME";
        }
        if (colClass == java.util.Date.class) {
            return "TIMESTAMP";
        }
        return "CHAR";
    }

    protected String GetRowAsStringDBtype(int row) {
        String strRow = "";
        Object tmpObj = null;
        for (int i = 0; i < this.jData.columnNames.length; i++) {
            tmpObj = this.getValueAt(row, i);
            strRow += this.ConvertValues(tmpObj) + ",";
        }
        int l = strRow.length();
        String sqlTxt = strRow.substring(0, l - 1);
        return sqlTxt;
    }

    protected Vector<Object> CreateEmptyRow() {
        Vector<Object> tmpObj = new Vector();
        for (int i = 0; i < this.jData.vColObject.size(); i++) {
            tmpObj.addElement(null);//vColObject.get(i));
        }
        return tmpObj;
    }

    public Object getValueAt(int aRow, int aColumn) {
        if ((aColumn < 0) || (aColumn > jData.columnNames.length)) {
            return "getValueAt num column error";
        }
        if ((aRow < 0) || (aRow > jData.rows.size())) {
            return "getValueAt num column error";
        }

        Vector row = (Vector) jData.rows.elementAt(aRow);
        Object t = null;//new Object();
        t = row.elementAt(aColumn);

        return t;
    }

    protected String ConvertValues(Object o) {
        if (o == null) {
            return "NULL";
        }
        Class cls = o.getClass();
        if ((cls == java.lang.String.class)
                || (cls == java.sql.Date.class)
                || (cls == java.sql.Time.class)
                || (cls == java.util.Date.class)) {
            return "'" + o.toString() + "'";
        }
        return o.toString();
    }
    //пересмотреть логику работы: ловить исключения на самом низу, сверху логировать. Обеспечивать устойчивость адаптера и всех ф-ций.Адаптер сигнализирует об ошибке и возвращает фиктивную таблицу
     @Override
    public void ConnectToExtern(JTableData jtd,String Url, String UserName, String Password) {
        jData = jtd;
        if (jData==null){
            System.err.println("error: JDBTable().setParam(..) argument JTableData is NULL");
            return ;
        }
        if (jtd.rows.isEmpty() == false){
            System.err.println("error: JDBTable().setParam(..) argument JTableData is NOT empty");
            return ;
        }
        Connection connection_ = conMan.CreateExternConnection(Url, UserName, Password);
        if (connection_==null) {
            System.err.println("JDBTable.Connect(..) connection is NULL");
//            return false;
        }
        this.connection = connection_;
    }
      
    
    @Override
    public void fillData(boolean newTable) {
        if (connection == null) {//|| statement == null){
            System.err.println("There is no database to execute the query.");
            return;
        }
//            try (Statement statement = connection.createStatement()) {
        try {
                Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery(_query)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                if (newTable == true) {
                    extractMetadata(metaData);
                }
                getDBdata(resultSet);
            }
        } catch (SQLException ex) {
            System.err.println("error: JDBTable.fillData; query="+_query);
            System.err.println(ex);
        }//finally
    }

    @Override
    public Object dbRepresentation(int column, Object value) {
        if ((column < 0) || (column > this.jData.columnNames.length)) {
            System.err.println("error index column=" + column);
            return null;
        }
        if (value == null) {
            //System.err.println("value=null");
            return null;//"nullValue";
        }
        Object tmpObj = null;
        Class cl = getColClass(jData.columnType[column]);
        if (value.getClass() == cl) { //.vColObject.get(column).getClass()) {
            return value;
        }
        // если на входе объект,класс которого не соответствует необходимому,пробуем преобразовать
        if (cl == java.lang.Boolean.class) {//ждём bool
            if (value.getClass() == java.lang.String.class) {//получили String
                if (value.toString().equalsIgnoreCase("true")) {
                    return true;
                }
                if (value.toString().equalsIgnoreCase("0")) {
                    return false;
                }
            }
            System.err.println("dbRepresentation WARNING: from " + value.getClass().getName() + " to " + cl.getName());
            return false;
        }
        if (cl == java.sql.Date.class) {//ждём дату
            if (value.getClass() == java.lang.String.class) {//получили String
                if (value.toString().equalsIgnoreCase("0")) {
                    return new java.sql.Date(0);
                }
                java.sql.Date _dt = Tools.GetSQLDateFromString(value.toString());
                if (_dt != null) {
                    return _dt;
                }
            }
            if (value.getClass() == java.util.Date.class) {//получили java.util.Date
                java.util.Date mD = (java.util.Date) value;
                java.sql.Date mDate = new java.sql.Date(mD.getTime());
                System.err.println(value.toString());
                System.err.println(mDate.toString());
                return mDate;//new java.sql.Date(2011, 10, 14);
            }
            System.err.println("dbRepresentation WARNING: from" + value.getClass().getName() + " to " + cl.getName());
            return new java.sql.Date(2011, 10, 14);
        }
        if (cl == java.sql.Time.class) {//ждём время
            if (value.getClass() == java.lang.String.class) {//получили String
                if (value.toString().equalsIgnoreCase("0")) {
                    return new java.sql.Time(0);
                }
                //пробуем преобразовать
                String st = "";
                java.util.Date _dt = null;
                java.sql.Time _tm = null;
                try {
                    SimpleDateFormat sdt = new SimpleDateFormat("HH:mm");
                    _dt = sdt.parse(value.toString());

                } catch (Exception e) {
                    System.err.println("time convert error");
                }
                _tm = new java.sql.Time(_dt.getTime());
                return _tm;
            }
            System.err.println("dbRepresentation WARNING: from " + value.getClass().getName() + " to " + cl.getName());
        }//time
        if (cl == java.util.Date.class) {
        }//timestamp

        //остальные типы пробуем проинициализировать строкой
        try {
            java.lang.reflect.Constructor constructor =
                    cl.getConstructor(new Class[]{String.class});// create an instance
            tmpObj = constructor.newInstance(new Object[]{value.toString()});
            return tmpObj;// получилось!
        } catch (Exception e) {
            System.err.println("exception - " + e);
        }
        System.err.println("dbRepresentation error: from" + value.getClass().getName() + " to " + cl.getName());
        return "error";
    }

    @Override
    public void DeleteColumn(String colName) {
        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return;
        }

//        try {
            String sqlText;
            sqlText = "ALTER TABLE `" + tblName + "` DROP COLUMN `" + colName;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
            }catch(SQLException e) {
                System.err.println(e);
            }
//        } catch (SQLException e) {
//            System.err.println(e);
//        }
    }

    @Override
    public void AddColumn(String colName, String type, String afterColumnName) {//пример "CHAR(10) NULL" или "FLOAT(8,2) DEFAULT 0"
        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return;
        }

        try {

            String sqlText;
            sqlText = "ALTER TABLE `" + tblName + "` ADD COLUMN `" + colName + "` " + type + " AFTER `" + afterColumnName + "`";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    @Override
    public void AddColumnLast(String colName, String type) {//пример "CHAR(10) NULL" или "FLOAT(8,2) DEFAULT 0"
        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return;
        }

        try {

            String sqlText;
            sqlText = "ALTER TABLE `" + tblName + "` ADD COLUMN `" + colName + "` " + type;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public boolean AddRemoteData(String Url, String UserName, String Password) {
        Connection _connection = null;
        try {
//            Class.forName(JAdapter.ConnManager.);//driverName);
//            _connection = DriverManager.getConnection(url, user, passwd);
            //тут добавляем данные
            Object tmpObj = null;
            _connection = conMan.CreateExternConnection(Url, UserName, Password);
            if (_connection == null) {
                return false;
            }
            try (Statement statement = _connection.createStatement(); 
                    ResultSet resultSet = statement.executeQuery(_query)) {
                //ResultSetMetaData metaData = resultSet.getMetaData();
                // Начнем получать данные из БД
                Object dbObj = null;
    //			rows.clear();
    //			rows = new Vector();
                while (resultSet.next()) {
                    Vector newRow = new Vector();
                    for (int i = 1; i <= this.jData.columnNames.length; i++) {
                        dbObj = resultSet.getObject(i);
                        tmpObj = dbRepresentation(i - 1, dbObj);
                        if (tmpObj == null) {
                            newRow.addElement(dbObj);
                        } else {
                            newRow.addElement(tmpObj);
                        }
                    }
                    // прибавим новую  запись
                    jData.rows.addElement(newRow);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCAdapter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean SaveAllToTempDBTable(String tableName) {
        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return false;
        }
        try {
            String sqlText;
            sqlText = "DROP TABLE IF EXISTS `" + tableName + "`";//"ALTER TABLE `" + tblName + "` ADD COLUMN `" + colName + "` " + type + " AFTER `" + afterColumnName + "`";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
                sqlText = "CREATE TEMPORARY TABLE `" + tableName + "` ("; //[IF NOT EXISTS]
                for (int j = 0; j < this.jData.columnNames.length; j++) {
                    sqlText += "`" + this.jData.columnNames[j] + "` " + this.GetColumnAsDBtype(j) + ",";//+"(255)"
                }
                int l = sqlText.length();
                String sqlTxt = sqlText.substring(0, l - 1);
                sqlTxt += ")";
                System.err.println(sqlTxt);
                statement.executeUpdate(sqlTxt);
                for (int j = 0; j < this.jData.rows.size(); j++) {
                    sqlText = "INSERT INTO `" + tableName + "` (";
                    for (int i = 0; i < this.jData.columnNames.length; i++) {
                        sqlText += "`" + this.jData.columnNames[i] + "`,";
                    }
                    l = sqlText.length();
                    sqlTxt = sqlText.substring(0, l - 1) + ") VALUES (";
                    sqlTxt += this.GetRowAsStringDBtype(j);
                    sqlTxt += ")";
                    System.err.println(sqlTxt);
                    statement.executeUpdate(sqlTxt);
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return true;
    }

    @Override
    public void MarkRowDeleted(int id) {

        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return;
        }
        try {
            String sqlText = "UPDATE " + this.tblName + " SET del=true WHERE id=" + id;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void DeleteRow(int id) {

        if (Asrt.is_int_negative(id) == true) {
            return;
        }
        if (connection == null) {//|| statement == null){
            System.err.println("There is no database to execute the query.");
            return;
        }
        try {
            String sqlText = "DELETE FROM " + this.tblName + " WHERE id=" + id;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    @Override
    public int AddRow() {

        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return -1;
        }
        ResultSet rs = null;
        int autoIncKeyFromFunc = -1;
        try {

            String sqlText;
            sqlText = "INSERT INTO `" + tblName + "` VALUES ()";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlText);
                //-----------------
                // Use the MySQL LAST_INSERT_ID()
                // function to do the same thing as getGeneratedKeys()

                rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

                if (rs.next()) {
                    autoIncKeyFromFunc = rs.getInt(1);
                    Vector<Object> lObj = this.CreateEmptyRow();
                    lObj.setElementAt(new Integer(autoIncKeyFromFunc), 0);
                    this.jData.rows.addElement(lObj);
                    //   if(sorter.getRowFilter() != null){
                    //   sorter.modelStructureChanged(); } else
    //            fireTableRowsInserted(autoIncKeyFromFunc-1, autoIncKeyFromFunc-1);
                } else {
                    // throw an exception from here
                }

                rs.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        return autoIncKeyFromFunc;
    }

    @Override
    public void UpdateRow(int id) {
        if (connection == null) {
            System.err.println("There is no database to execute the query.");
            return;
        }
        ResultSet rs = null;
        try {

            String sqlText;
            sqlText = this._query;
            sqlText = sqlText.toLowerCase();
            if (sqlText.indexOf("where") > 0) {
                sqlText = sqlText + " and id=" + id;
            } else {
                sqlText = sqlText + " where id=" + id;
            }
            try (Statement statement = connection.createStatement()) {
                rs = statement.executeQuery(sqlText);

                if (rs.next()) {
                    Vector newRow = new Vector();
                    for (int i = 1; i <= jData.columnNames.length; i++) {
                        newRow.addElement(rs.getObject(i));
                    }
    //				rows.setElementAt(newRow, FindRowID(id));//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }
                rs.close();
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void setValueAt(Object value, int row, int column) {//редактирует только this.tableName;
        if (connection == null) {//|| statement == null){
            System.err.println("There is no database to execute the query.");
            return;
        }

        //if (value==null){		}//по идее можно данные в ячейке установить в null (проверить!)

        try {
            //Эта функция возвращает название поля по его номеру
            String columnName = jData.columnNames[column];
            Object v = dbRepresentation(column, value);
            if (v == null) {
                return;//пока нельзя записать null в базу(!)
            }
            Class cls = v.getClass();
            String name = cls.getName();

            String query = "";
            if (name.equals("java.lang.String")
                    || name.equals("java.sql.Date")
                    || name.equals("java.sql.Time")
                    || name.equals("java.util.Date")) {
                query = "UPDATE " + this.tblName
                        + " SET `" + columnName + "` = '"
                        + v
                        + "' WHERE " + prmKey + " =";
            } else {
                query = "UPDATE " + this.tblName
                        + " SET `" + columnName + "` = "
                        + v
                        + " WHERE " + prmKey + " =";
            }
            //метод  dbRepresentation(column, value) преобразует значения в строку, для некоторых типов обычное toString() не подойдет
            for (int col = 0; col < jData.columnNames.length; col++) {
                String colName = jData.columnNames[col];
                if (colName.equals(prmKey)) {
                    query = query + getValueAt(row, col);//dbRepresentation(col, getValueAt(row, col));
                    break;
                }
            }
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            }

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @Override
    public String getQueryText() {
        return _query;
    }

    public void setQueryText(String q) {
        _query = q;
    }

    @Override
    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    @Override
    public String getPrmKey() {
        return prmKey;
    }


}
