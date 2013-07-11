package autopartner;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

public class Tools {

	private Tools() {
	}

	// Для получения первого дня месяца текущей даты
	public static String GetFirstMountch() {
		long curTime = System.currentTimeMillis();
		String curStringDate = new SimpleDateFormat("dd.MM.yy").format(curTime);
		curStringDate = "01" + curStringDate.substring(3, curStringDate.length());
		return curStringDate;
	}

	// Для получения текущей даты
	public static String GetCurrentData() {
		long curTime = System.currentTimeMillis();
		String curStringDate = new SimpleDateFormat("dd.MM.yy").format(curTime);
		return curStringDate;
	}

	// Проверить чтобы дата не была в виде "  .  .  "
	public static String NormalDate(String inputValue) {

		inputValue = inputValue.replace(" ", "");

		if (inputValue.length() < 8) {
			return null;
		}

		return inputValue;
	}

	// Проверить чтобы время не была в виде "  :  "
	public static String NormalTime(String inputValue) {

		inputValue = inputValue.replace(" ", "");

		if (inputValue.length() < 5) {
			return null;
		}

		return inputValue;
	}

	// Проверить допустимость введенной даты
	public static boolean isValidDate(String inputValue) {

		inputValue = inputValue.replace(" ", "");

		if (inputValue.length() < 8) {
			return false;
		}

		Calendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.clear();
		// Разобрать строку на три составляющие (день, месяц, год)
		try {
			int d = Integer.parseInt(inputValue.substring(0, 2));
			int m = Integer.parseInt(inputValue.substring(3, 5));
			int y = 0;
			if (inputValue.length() > 8) {
				y = Integer.parseInt(inputValue.substring(6, 10));
			} else {
				y = Integer.parseInt(inputValue.substring(6, 8));
			}
			cal.set(y, m - 1, d);
			java.util.Date dt = cal.getTime();
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		} catch (IllegalArgumentException iae) {
			return false;
		}
	}

	// Проверка валидности времени
	public static boolean isValidTime(String inputValue) {

		inputValue = inputValue.replace(" ", "");

		if (inputValue.length() < 5) {
			return false;
		}

		// Разобрать строку на составляющие (час, мин)
		int h = Integer.parseInt(inputValue.substring(0, 2));
		int m = Integer.parseInt(inputValue.substring(3, 5));
		if ((h <= 23) && (m <= 59)) {
			return true;
		}
		return false;
	}

	//получить из строки формата "dd.MM.yyyy" формат SQL "yyyy-mm-dd"
	public static java.sql.Date GetSQLDateFromString(String date) {
		try {
			SimpleDateFormat sdt = new SimpleDateFormat("dd.MM.yyyy");
			java.util.Date utilDate = sdt.parse(date);
			//sdt.applyPattern(date); -посмотреть
			//sdt.getDateFormatSymbols()-посмотреть
			//sdt.getNumberFormat()-посмотреть

			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (ParseException ex) {
			//Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println(Tools.class.getName() + "rerror:" + ex);
		}
		return null;//возвращаю null чтобы узнавать об успешности преобразования
	}

	// Чтобы задать пустую дату пришлось свой форматер делать
	public static void FormaterForDate(javax.swing.JFormattedTextField DataField) {

		try {
			MaskFormatter formatter = new MaskFormatter("**.**.**");
			formatter.setValidCharacters("0123456789 _");
			DataField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
		} catch (java.text.ParseException ex) {
			System.out.println("Не установился форматер для даты!");
		}

	}

	// Форматер для ввода даты строгий в формате ##.##.##
	public static void FormaterForStrongDate(javax.swing.JFormattedTextField DataField) {

		try {
			MaskFormatter formatter = new MaskFormatter("##.##.##");
			DataField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
		} catch (java.text.ParseException ex) {
			System.out.println("Не установился форматер для даты!");
		}

	}

	// Чтобы задать пустое время пришлось свой форматер делать
	public static void FormaterForTime(javax.swing.JFormattedTextField TimeField) {
		try {
			MaskFormatter formatter = new MaskFormatter("**:**");
			formatter.setValidCharacters("0123456789 ");
			TimeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
		} catch (java.text.ParseException ex) {
			System.out.println("Не установился форматер для времени!");
		}
	}

		// свой форматер для тройного телефона
	public static void FormaterForPhone(javax.swing.JFormattedTextField TimeField) {
		try {
			MaskFormatter formatter = new MaskFormatter("***-***-**** (***-***-****) (***-***-****)");
			formatter.setValidCharacters("0123456789 ");
			TimeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
		} catch (java.text.ParseException ex) {
			System.out.println("Не установился форматер для времени!");
		}
	}

	// Конвертирует коротку дату в dd.mm.yyyy
	public static String ConvertDate(String inputData) {

		SimpleDateFormat sdts = new SimpleDateFormat("dd.MM.yy");
		SimpleDateFormat sdtl = new SimpleDateFormat("dd.MM.yyyy");

		try {
			return sdtl.format(sdts.parse(inputData));
		} catch (ParseException ex) {
			System.out.println("Не удалось преобразовать дату Tools.ConvertDate");
		}
		return "";
	}

	// Конвертирует коротку дату в yyyy-dd-mm
	public static String ConvertDateToMySQL(String inputData) {

		SimpleDateFormat sdts = new SimpleDateFormat("dd.MM.yy");
		SimpleDateFormat sdtl = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return sdtl.format(sdts.parse(inputData));
		} catch (ParseException ex) {
			System.out.println("Не удалось преобразовать дату Tools.ConvertDate");
		}
		return "";
	}

	// Конвертирует коротку дату в yyyy-dd-mm
	public static String ConvertMySQLDateToShort(String inputData) {

		SimpleDateFormat sdts = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdtl = new SimpleDateFormat("dd.MM.yy");

		try {
			return sdtl.format(sdts.parse(inputData));
		} catch (ParseException ex) {
			System.out.println("Не удалось преобразовать дату Tools.ConvertMySQLDateToShort");
		}
		return "";
	}
//
//    public static String GetDateFormated(Object date){
//        if (date.getClass() != java.sql.Date.class) return "";
//        if (date==null) return "";
//        SimpleDateFormat sdt = new SimpleDateFormat("dd.MM.yyyy");
//        //String s=date.toString();
//        return sdt.format(date);
//    }
//
//    public static int GetInt(JTable table){
//        int row=table.getSelectedRow();
//        int row_model = Tools.GetRowIndexToModel(table, row);
//        int col_index = Tools.GetIndexTableFromName(table, "id");
//        int col_model = Tools.GetColIndexToModel(table, col_index);
//        int id_select_dtp = new Integer(table.getValueAt(row_model, col_model).toString());
//		return id_select_dtp;
//    }
//
//// Возвращает столбец
//    public static Vector<String> GetColumnData(TableModel model, String name) {
//        Vector<String> strList = new Vector<String>();
//
//        if (name == null) {
//            return strList;
//        }
//
//        if (name.length() == 0) {
//            return strList;
//        }
//
//        int col = GetIndexModelFromName(model, name);
//        if (col < 0) {
//            return strList;
//        }
//        String str = null;
//        Object obj = null;
//        for (int i = 0; i < model.getRowCount(); i++) {
//            obj = model.getValueAt(i, col);
//            str = null;
//            if (obj != null) {
//                str = obj.toString();
//            }
//            if (str == null) {
//                strList.add("Null");
//            } else {
//                strList.add(str);//проверить работу при получении null от гета!
//            }
//        }
//        return strList;
//    }
//// заполняет JComboBox данными из модели
//    public static void FillComboData(JComboBox cBox, TableModel model, String fieldName) {
//        cBox.removeAllItems();
//        cBox.setModel(new DefaultComboBoxModel(GetColumnData(model, fieldName)));
//    }
////получить номер колонки для использования при работе с моделью из табличного представлени
//    public static int GetColIndexToModel(JTable table, int viewColumnIndex) {
//        if (viewColumnIndex < 0) {
//            return -1;
//        }
//        return table.convertColumnIndexToModel(viewColumnIndex);
//    }
////получить номер колонки для использования при работе с таблицей из модельного представления
//    public static int GetColIndexToView(JTable table, int modelColumnIndex) {
//        if (modelColumnIndex < 0) {
//            return -1;
//        }
//        return table.convertColumnIndexToView(modelColumnIndex);
//    }
//// аналогично для строки
//    public static int GetRowIndexToModel(JTable table, int viewRowIndex) {
//        if (viewRowIndex < 0) {
//            return -1;
//        }
//        return table.convertRowIndexToModel(viewRowIndex);
//    }
////аналогично для строки
//    public static int GetRowIndexToView(JTable table, int modelRowIndex) {
//        if (modelRowIndex < 0) {
//            return -1;
//        }
//        return table.convertRowIndexToView(modelRowIndex);
//    }
//// получить первичный ключ по значнию ячейки
//    public static int GetIdModelFromColumnValue(TableModel model, String primaryKey, String fieldName, String value) {
//
//        if (model.getRowCount() < 1) {
//            return -1;//предупреждение - пустая таблица!
//        }
//        if (value == null) {
//            return -1;//ошибка - нечего искать!
//        }
//        //получаем номер колонки с первичным ключом
//        int colPrimaryKey = GetIndexModelFromName(model, primaryKey);
//        //получаем номер колонки в которой ищем
//        int colValue = GetIndexModelFromName(model, fieldName);
//        if (colPrimaryKey < 0 || colValue < 0) {
//            return -1;//ошибка! Не нашли поле
//        }
//        String tmpS = "";
//        Object tmpO = null;
//        //проходим по всей колонке
//        for (int i = 0; i < model.getRowCount(); i++) {
//            tmpO = model.getValueAt(i, colValue);
//            if (tmpO == null) {
//                continue;//предупреждение! Нельзя выбрать пустой объект!
//            } else {
//                tmpS = tmpO.toString();
//                if (tmpS.equalsIgnoreCase(value)) {
//                    tmpO = model.getValueAt(i, colPrimaryKey);
//                    if (tmpO == null) {
//                        return -1;//ошибка! Индекс не может быть 0
//                    }
//                    return new Integer(tmpO.toString());
//                }
//            }
//        }
//        return -1;//Ошибка! не нашли!
//    }
//// получить строку из ИД
//    public static int GetRowFromId(TableModel model,String primaryKey,int id){
//        if (model.getRowCount() < 1) {
//            return -1;//предупреждение - пустая таблица!
//        }
//        //получаем номер колонки с первичным ключом
//        int colPrimaryKey = GetIndexModelFromName(model, primaryKey);
//        //проходим по всей колонке
//        Object tmpObj=null;
//        int _id=0;
//        for (int i = 0; i < model.getRowCount(); i++) {
//            tmpObj=model.getValueAt(i, colPrimaryKey);
//            if (tmpObj!=null){
//                try{
//                    _id=new Integer(tmpObj.toString());
//                    if (_id==id) return i;
//                }catch(Exception e){
//                }
//            }
//        }
//        return -1;
//    }
//
//// получить номер столбца по имнени (для модели)
//    public static int GetIndexModelFromName(TableModel model, String colName) {
//        if (colName == null) {
//            return -1;
//        }
//        if (colName.length() == 0) {
//            return -1;
//        }
//        String clName = null;
//        for (int col = 0; col < model.getColumnCount(); col++) {
//            clName = model.getColumnName(col);
//            if (clName.equalsIgnoreCase(colName)) {
//                return col;
//            }
//        }
//        return -1;
//    }
////получить номер столбца по имени (для таблицы)
//    public static int GetIndexTableFromName(JTable table, String fieldName) {
//        JTableHeader th = table.getTableHeader();
//        TableColumnModel tcm = th.getColumnModel();//table.getColumnModel();
//        for (int i = 0; i < tcm.getColumnCount(); i++) {
//            if (fieldName.equalsIgnoreCase(tcm.getColumn(i).getHeaderValue().toString())) {
//                return i;
//            }
//        }
//        return -1;
//    }
//// скрыть столбец в таблице по имени
//    public static void HideColumnByName(JTable table, String name) {
//        int index = GetIndexTableFromName(table, name);
//        if (index < 0) {
//            return;
//        }
//        hideColumn(table, index);
//        //table.getColumnModel().getColumn(index).setMaxWidth(0);
//        //table.getColumnModel().getColumn(index).setMinWidth(0);
//    }
////-----------------------------------
//
//    /**
//     * Делает колонку невидимой
//     * @param table - таблица
//     * @param index - номер колонки
//     */
//    public static void hideColumn(JTable table, int index) {
//        if (index < 0) {
//            return;
//        }
//        table.getColumnModel().getColumn(index).setMaxWidth(0);
//        table.getColumnModel().getColumn(index).setMinWidth(0);
//        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
//        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
//    }
//
//    /**
//     * Поиск по колонке в таблице, нужно нажать на таблицу чтоб получить фокус, а потом набирать
//     * для следующего поиска нужно нажать на клавишу Escape
//     * @param table - таблица в которой производится поиск
//     * @param column - номер колонки, в которой нужно производить поиск.
//     */
//    public static void setFindedColumn(JTable table, final int column) {
//
//        table.addKeyListener(new KeyAdapter() {
//
//            StringBuffer searchString = new StringBuffer();
//            boolean cleared = false;
//
//            @Override
//            public void keyPressed(KeyEvent ke) {
//                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
//                    searchString = new StringBuffer();
//                    cleared = true;
//                }
//            }
//
//            @Override
//            public void keyTyped(KeyEvent ke) {
//                if (cleared) {
//                    cleared = false;
//                    return;
//                }
//                JTable t = (JTable) ke.getSource();
//                String ch = (ke.getKeyChar() + new String()).toLowerCase();
//                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
//                    ch = " ";
//                }
//
//                if (ch.length() == 1) {
//                    ke.consume();
//                    searchString.append(ke.getKeyChar());
//                    String ss = searchString.toString().toLowerCase();
//
//                    for (int i = 0; i < t.getRowCount(); i++) {
//                        String val = t.getValueAt(i, column).toString();
//                        if (val.length() >= ss.length() && val.substring(0, ss.length()).toLowerCase().equalsIgnoreCase(ss)) {
//                            t.setRowSelectionInterval(i, i);
//                            Rectangle cellRect = t.getCellRect(i, column, false);
//                            if (cellRect != null) {
//                                t.scrollRectToVisible(cellRect);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * Растягивает колонки в зависимости от имени
//     * @param table - исходная таблица
//     */
//    public static TableColumnModel resizeColumnsByName(JTable table) {
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        TableColumnModel tcm = table.getColumnModel();
//        int addedSize = table.getFont().getSize() + 5;
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            String hvalue = tcm.getColumn(i).getHeaderValue().toString();
//            tcm.getColumn(i).setPreferredWidth(table.getFontMetrics(table.getFont()).stringWidth(hvalue) + addedSize);
//        }
//        return tcm;
//    }
//
//    /**
//     * Растягивает колонки в зависимости от имени, и значения в колонке. Если значение в колонке больше чем название, колонка растягивается по значению иначе, она растягивается по максимальной по длине строке в хедере колонки.
//     * Поддержка HTML тегов
//     * @param table - исходная таблица
//     */
//    public static TableColumnModel resizeColumnsByNameWithHTMLSupports(JTable table) {
//        int colsCount = table.getColumnCount();
//        TableColumnModel tcm = table.getColumnModel();
//        if (colsCount > 0) {
//            int maxBrs = 0;
//            int addedSize = table.getFont().getSize() + 5;
//
//            int maxWidth = 0;
//            int[] maxBrsArr = new int[colsCount];
//            String[] maxWidthsStrs = new String[colsCount];
//
//            String[] columnIdentifiers = new String[colsCount];
//
//            for (int i = 0; i < colsCount; i++) {
//                String hvalue = tcm.getColumn(i).getHeaderValue().toString();
//
//                //Находим макс кол-во переносов
//                String[] split = hvalue.split("<br>");
//                maxBrsArr[i] = split.length;
//
//                if (!hvalue.contains("<html>")) {
//                    columnIdentifiers[i] = "<html><center>" + hvalue;
//                    if (split.length > maxBrs) {
//                        maxBrs = split.length;
//                    }
//                } else {
//                    columnIdentifiers[i] = hvalue;
//                    if (split.length > maxBrs) {
//                        maxBrs = split.length - 1;
//                    }
//                }
//                //Находим макс кол-во переносов
//                maxBrsArr[i] = split.length;
//                //Находим макс ширину слова
//                maxWidth = 0;
//                for (int s = 0; s < split.length; s++) {
//                    if (split[s].length() >= maxWidth) {
//                        maxWidth = split[s].length();
//                        maxWidthsStrs[i] = split[s];
//                    }
//                }
//
//            }
//            maxBrs++;
//            for (int maxb = 0; maxb < maxBrsArr.length; maxb++) {
//                String addedBrs = "";
//                int tmp = maxBrs - maxBrsArr[maxb];
//
//                for (int i = 0; i < tmp; i++) {
//                    addedBrs += " <br>";
//                }
//                columnIdentifiers[maxb] += addedBrs;
//
//            }
//            ((DefaultTableModel) table.getModel()).setColumnIdentifiers(columnIdentifiers);
//
//            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            for (int i = 0; i < colsCount; i++) {
//
//                String hvalue = maxWidthsStrs[i].replace("<html><center>", " ");
//                int prwidth = table.getFontMetrics(table.getFont()).stringWidth(hvalue) + addedSize;
//                int maxValueSize = getMaxColumnsValueSize(table, i);
//                if (prwidth > maxValueSize) {
//                    tcm.getColumn(i).setPreferredWidth(prwidth);
//                } else {
//                    tcm.getColumn(i).setPreferredWidth(maxValueSize);
//                }
//            }
//        }
//        return tcm;
//    }
//
//    /**
//     * Находит максимальную длину значения колноки
//     * @param table
//     * @param row
//     * @return
//     */
//    public static int getMaxColumnsValueSize(JTable table, int column) {
//        int maxLength = 0;
//        int addedSize = table.getFont().getSize() + 10;
//        for (int row = 0; row < table.getRowCount(); row++) {
//            int length = table.getFontMetrics(table.getFont()).stringWidth(table.getValueAt(row, column).toString()) + addedSize;
//
//            if (length > maxLength) {
//                maxLength = length;
//            }
//        }
//        return maxLength;
//    }
//
//    public static void scrollSelectionToVisible(JTable table) {
//        if (table == null) {
//            return;
//        }
//
//        int minIndex = table.getSelectionModel().getMinSelectionIndex();
//        if ((minIndex < 0) || (minIndex >= table.getRowCount())) {
//            return;
//        }
//
//        int maxIndex = table.getSelectionModel().getMaxSelectionIndex();
//        if ((maxIndex < 0) || (maxIndex >= table.getRowCount())) {
//            return;
//        }
//
//        scrollRowToVisible(table, maxIndex);
//        if (minIndex != maxIndex) {
//            scrollRowToVisible(table, minIndex);
//        }
//    }
//
//    public static void scrollRowToVisible(JTable table, int row) {
//        scrollTableColumnToVisible(table, row, 0);
//    }
//
//    public static void scrollModelColumnToVisible(JTable table, int row, int column) {
//        if (table == null) {
//            return;
//        }
//        scrollTableColumnToVisible(table, row, table.convertColumnIndexToView(column));
//    }
//
//    public static void scrollTableColumnToVisible(JTable table, int row, int column) {
//        if (table == null) {
//            return;
//        }
//        if ((row < 0) || (row >= table.getRowCount())) {
//            return;
//        }
//        if ((column < 0) || (column >= table.getColumnCount())) {
//            return;
//        }
//
//        Rectangle cellRect = table.getCellRect(row, column, true);
//        table.scrollRectToVisible(cellRect);
//    }
//
//    public static void selectRow(JTable table, int row) {
//        selectRow(table, row, true);
//    }
//
//    public static void selectRow(JTable table, int row, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        int rowCount = table.getRowCount();
//        if (rowCount <= 0) {
//            return;
//        }
//
//        stopCellEditing(table);
//        if ((row < 0) || (row >= rowCount)) {
//            table.clearSelection();
//        } else {
//            table.setRowSelectionInterval(row, row);
//            scrollRowToVisible(table, row);
//        }
//        if (grabFocus && !table.hasFocus()) {
//            table.grabFocus();
//        }
//    }
//
//    public static void selectModelCell(JTable table, int row, int column) {
//        selectTableCell(table, row, table.convertColumnIndexToView(column));
//    }
//
//    public static void selectModelCell(JTable table, int row, int column, boolean grabFocus) {
//        selectTableCell(table, row, table.convertColumnIndexToView(column), grabFocus);
//    }
//
//    public static void selectTableCell(JTable table, int row, int column) {
//        selectTableCell(table, row, column, true);
//    }
//
//    public static void selectTableCell(JTable table, int row, int column, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        int rowCount = table.getRowCount();
//        if (rowCount <= 0) {
//            return;
//        }
//        int columnCount = table.getColumnCount();
//        if (columnCount <= 0) {
//            return;
//        }
//
//        stopCellEditing(table);
//        if ((row < 0) || (row >= rowCount) || (column < 0) || (column >= columnCount)) {
//            table.clearSelection();
//        } else {
//            table.setRowSelectionInterval(row, row);
//            table.getSelectionModel().setAnchorSelectionIndex(row);
//            if (table.getColumnSelectionAllowed()) {
//                table.getColumnModel().getSelectionModel().setSelectionInterval(column, column);
//            }
//            table.getColumnModel().getSelectionModel().setAnchorSelectionIndex(column);
//            scrollTableColumnToVisible(table, row, column);
//        }
//        if (grabFocus && !table.hasFocus()) {
//            table.grabFocus();
//        }
//    }
//
//    public static void selectFirst(JTable table) {
//        selectFirst(table, true);
//    }
//
//    public static void selectFirst(JTable table, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        if (table.getRowCount() > 0) {
//            selectRow(table, 0, grabFocus);
//        }
//    }
//
//    public static void selectLast(JTable table) {
//        selectLast(table, true);
//    }
//
//    public static void selectLast(JTable table, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        int rowCount = table.getRowCount();
//        if (rowCount > 0) {
//            selectRow(table, rowCount - 1, grabFocus);
//        }
//    }
//
//    public static void selectPrevious(JTable table) {
//        selectPrevious(table, true);
//    }
//
//    public static void selectPrevious(JTable table, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        ListSelectionModel selModel = table.getSelectionModel();
//        if (selModel == null) {
//            return;
//        }
//        int rowCount = table.getRowCount();
//        if (rowCount <= 0) {
//            return;
//        }
//
//        int index = selModel.getAnchorSelectionIndex() - 1;
//        if (index < 0) {
//            index = 0;
//        }
//        selectRow(table, index, grabFocus);
//    }
//
//    public static void selectNext(JTable table) {
//        selectNext(table, true);
//    }
//
//    public static void selectNext(JTable table, boolean grabFocus) {
//        if (table == null) {
//            return;
//        }
//        ListSelectionModel selModel = table.getSelectionModel();
//        if (selModel == null) {
//            return;
//        }
//        int rowCount = table.getRowCount();
//        if (rowCount <= 0) {
//            return;
//        }
//
//        int index = selModel.getAnchorSelectionIndex();
//        if (index < 0) {
//            index = 0;
//        } else {
//            index++;
//            if (index >= rowCount) {
//                index = rowCount - 1;
//            }
//        }
//        selectRow(table, index, grabFocus);
//    }
//
//    public static void stopCellEditing(JTable table) {
//        if (table == null) {
//            return;
//        }
//        TableCellEditor editor = table.getCellEditor();
//        if (editor == null) {
//            return;
//        }
//        if (!editor.stopCellEditing()) {
//            editor.cancelCellEditing();
//        }
//    }
//
//    public static void cancelCellEditing(JTable table) {
//        if (table == null) {
//            return;
//        }
//        TableCellEditor editor = table.getCellEditor();
//        if (editor == null) {
//            return;
//        }
//        editor.cancelCellEditing();
//    }
//
//    /**
//     * This methid calculate the minimum width of table column, that needed for correct
//     * display of all cell in this column. Method does not consider the distance between
//     * columns in the table. To calculate this value it is necessary to increase result
//     * of the method on table.getColumnModel().getColumnMargin() value.
//     * @param table the table
//     * @param column index of column in the table
//     * @return column width
//     */
//    public static int calculateBestColumnWidth(JTable table, int column) {
//        if (table == null) {
//            return 0;
//        }
//        if ((column < 0) || (column >= table.getColumnCount())) {
//            return 0;
//        }
//
//        int res = 0;
//        TableCellRenderer renderer;
//        Component rendererComponent;
//        Dimension prefferdSize;
//        int maxInd = table.getRowCount();
//        for (int row = 0; row < maxInd; row++) {
//            renderer = table.getCellRenderer(row, column);
//            if (renderer == null) {
//                continue;
//            }
//            rendererComponent = table.prepareRenderer(renderer, row, column);
//            if (rendererComponent == null) {
//                continue;
//            }
//            prefferdSize = rendererComponent.getPreferredSize();
//            if (prefferdSize == null) {
//                continue;
//            }
//            if (prefferdSize.width > res) {
//                res = prefferdSize.width;
//            }
//        }
//        return res;
//    }
}