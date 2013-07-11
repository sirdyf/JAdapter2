/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JAdapter;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
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
public class JDBCAdapterTest {
    DBInterface dbiMock;
    String query;
    JDBCAdapter adapter;
    public JDBCAdapterTest() {
        query="SELECT *";
        dbiMock = mock(DBInterface.class);
        adapter = new JDBCAdapter(dbiMock,"test", "id", query);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test()
    public void testJDBCAdapter() {
        verify(dbiMock).Connect(adapter.jtData,"test","id",query);//, anyString(),anyString(),anyString());
        verify(dbiMock).fillData(true);
        //JDBCAdapter aMock=spy(new JDBCAdapter(dbiMock,"a","b","c"));
    }
    @Test
    public void testCreateModel(){
        JDBCAdapter aMock=spy(adapter);
        aMock.CreateModel(true);
        verify(aMock).fireTableStructureChanged();
        aMock.CreateModel(false);
        verify(aMock).fireTableDataChanged();
        
    }

}
