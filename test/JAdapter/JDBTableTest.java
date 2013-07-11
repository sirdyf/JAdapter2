/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import java.sql.ResultSet;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author fomin_dmitry
 */
public class JDBTableTest {
    iConnManager cmMock;
    JDBTable instance;
    Connection conMock;
    Statement stMock;
    ResultSet rsMock;
    JTableData jtd;
        String Url = "url";
        String UserName = "name";
        String Password = "pass";

    @Before
    public void setUp() {

    }
    public JDBTableTest() throws SQLException {
        jtd = new JTableData();
        cmMock= mock(iConnManager.class);
        instance = new JDBTable(cmMock);
        conMock = mock(Connection.class);
        stMock = mock(Statement.class);
        rsMock = mock(ResultSet.class);
        when(cmMock.CreateExternConnection(anyString(),anyString(),anyString())).thenReturn(conMock);
        when(conMock.createStatement()).thenReturn(stMock);
        when(stMock.executeQuery(anyString())).thenReturn(rsMock);
    }
    
    @Test
    public void testSetParam() {
        System.out.println("setParam test");

        instance.Connect(jtd, Url, UserName, Password);
        verify(cmMock).getInstance();
        verify(cmMock).CreateExternConnection(Url,UserName,Password);
    }
    @Test
    public void testFillData() throws SQLException {
        System.out.println("testFillData");
        instance.Connect(jtd, null, null, null);
        JDBTable spyDBT=spy(instance);
        spyDBT.fillData(false);
        verify(conMock).createStatement();
        verify(stMock).executeQuery(anyString());
        verify(rsMock).getMetaData();
        //verify(spyDBT).extractMetadata();
        //verify(spyDBT).getDBdata(rsMock);
    }
    @Test
    public void extractMetadataTest(){
        System.out.println("extractMetadataTest");
        instance.Connect(jtd, null, null, null);
        
    }
/*

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
   
    @After
    public void tearDown() {
    }

    @Test
    public void testGetColClass() {
        System.out.println("getColClass");
        int type = 0;
        Class expResult = null;
        Class result = JDBTable.getColClass(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDbRepresentation() {
        System.out.println("dbRepresentation");
        int column = 0;
        Object value = null;
        JDBTable instance = new JDBTable();
        Object expResult = null;
        Object result = instance.dbRepresentation(column, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteColumn() {
        System.out.println("DeleteColumn");
        String colName = "";
        JDBTable instance = new JDBTable();
        instance.DeleteColumn(colName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddColumn() {
        System.out.println("AddColumn");
        String colName = "";
        String type = "";
        String afterColumnName = "";
        JDBTable instance = new JDBTable();
        instance.AddColumn(colName, type, afterColumnName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddColumnLast() {
        System.out.println("AddColumnLast");
        String colName = "";
        String type = "";
        JDBTable instance = new JDBTable();
        instance.AddColumnLast(colName, type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddRemoteData() {
        System.out.println("AddRemoteData");
        String Url = "";
        String UserName = "";
        String Password = "";
        JDBTable instance = new JDBTable();
        boolean expResult = false;
        boolean result = instance.AddRemoteData(Url, UserName, Password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSaveAllToTempDBTable() {
        System.out.println("SaveAllToTempDBTable");
        String tableName = "";
        JDBTable instance = new JDBTable();
        boolean expResult = false;
        boolean result = instance.SaveAllToTempDBTable(tableName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testMarkRowDeleted() {
        System.out.println("MarkRowDeleted");
        int id = 0;
        JDBTable instance = new JDBTable();
        instance.MarkRowDeleted(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteRow() {
        System.out.println("DeleteRow");
        int id = 0;
        JDBTable instance = new JDBTable();
        instance.DeleteRow(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddRow() {
        System.out.println("AddRow");
        JDBTable instance = new JDBTable();
        int expResult = 0;
        int result = instance.AddRow();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateRow() {
        System.out.println("UpdateRow");
        int id = 0;
        JDBTable instance = new JDBTable();
        instance.UpdateRow(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetValueAt() {
        System.out.println("setValueAt");
        Object value = null;
        int row = 0;
        int column = 0;
        JDBTable instance = new JDBTable();
        instance.setValueAt(value, row, column);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetQueryText() {
        System.out.println("getQueryText");
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.getQueryText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetQueryText() {
        System.out.println("setQueryText");
        String q = "";
        JDBTable instance = new JDBTable();
        instance.setQueryText(q);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTblName() {
        System.out.println("getTblName");
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.getTblName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetTblName() {
        System.out.println("setTblName");
        String tblName = "";
        JDBTable instance = new JDBTable();
        instance.setTblName(tblName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPrmKey() {
        System.out.println("getPrmKey");
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.getPrmKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPrmKey() {
        System.out.println("setPrmKey");
        String prmKey = "";
        JDBTable instance = new JDBTable();
        instance.setPrmKey(prmKey);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetColumnAsDBtype() {
        System.out.println("GetColumnAsDBtype");
        int column = 0;
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.GetColumnAsDBtype(column);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRowAsStringDBtype() {
        System.out.println("GetRowAsStringDBtype");
        int row = 0;
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.GetRowAsStringDBtype(row);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateEmptyRow() {
        System.out.println("CreateEmptyRow");
        JDBTable instance = new JDBTable();
        Vector expResult = null;
        Vector result = instance.CreateEmptyRow();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        int aRow = 0;
        int aColumn = 0;
        JDBTable instance = new JDBTable();
        Object expResult = null;
        Object result = instance.getValueAt(aRow, aColumn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testConvertValues() {
        System.out.println("ConvertValues");
        Object o = null;
        JDBTable instance = new JDBTable();
        String expResult = "";
        String result = instance.ConvertValues(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     
     */
}
