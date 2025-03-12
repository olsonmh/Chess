package dataaccess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataaccess.MySqlDataAccess;
import dataaccess.DatabaseManager;

public class MysqlTests {
    @Test
    @DisplayName("Positive Configure Database Test")
    public void positiveCreateDatabaseTest() {
        MySqlDataAccess.configureDatabase();

    }
}
