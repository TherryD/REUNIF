package org.sqlite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by IntelliJ IDEA. User: david_donn Date: 19/01/2010 Time: 11:50:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class FetchSizeTest {

    private Connection conn;

    @BeforeEach
    public void connect() throws Exception {
        conn = DriverManager.getConnection("jdbc:sqlite:");
    }

    @AfterEach
    public void close() throws SQLException {
        conn.close();
    }

    @Test
    public void testFetchSize() throws SQLException {
        assertEquals(conn.prepareStatement("create table s1 (c1)").executeUpdate(), 0);
        PreparedStatement insertPrep = conn.prepareStatement("insert into s1 values (?)");
        insertPrep.setInt(1, 1);
        assertEquals(insertPrep.executeUpdate(), 1);
        insertPrep.setInt(1, 2);
        assertEquals(insertPrep.executeUpdate(), 1);
        insertPrep.setInt(1, 3);
        assertEquals(insertPrep.executeUpdate(), 1);
        insertPrep.setInt(1, 4);
        assertEquals(insertPrep.executeUpdate(), 1);
        insertPrep.setInt(1, 5);
        assertEquals(insertPrep.executeUpdate(), 1);
        insertPrep.close();

        PreparedStatement selectPrep = conn.prepareStatement("select c1 from s1");
        ResultSet rs = selectPrep.executeQuery();
        rs.setFetchSize(2);
        assertTrue(rs.next());
        assertTrue(rs.next());
        assertTrue(rs.next());
        assertTrue(rs.next());
        assertTrue(rs.next());
        assertFalse(rs.next());
    }

}