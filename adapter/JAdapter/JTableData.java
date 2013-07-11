/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import java.util.Vector;

/**
 *
 * @author fomin_dmitry
 */
public class JTableData {
	protected Vector<Object> vColObject = null;//классы колонок
	protected int[] columnType = {};
	protected boolean[] bIsEditableColumn = {};    
	protected Vector rows = null;// здесь будут  храниться все данные таблицы БД
	public String[] columnNames = {};// здесь будут содержаться названия полей таблицы БД
}
