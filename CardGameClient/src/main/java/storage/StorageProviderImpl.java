package storage;

import client.model.User;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StorageProviderImpl implements StorageProvider {

    private static final String DB_FILE = "./storage/data.sqlite3";
    private static SqlJetDb mDatabase;

    private static StorageProvider sInstance = new StorageProviderImpl();

    public static StorageProvider getInstance() {
        return sInstance;
    }

    private class Tables {
        public static final String USERS = "users";
    }

    private StorageProviderImpl() {
        try {
            mDatabase = SqlJetDb.open(new File(DB_FILE), true);
            mDatabase.beginTransaction(SqlJetTransactionMode.WRITE);
            mDatabase.getOptions().setUserVersion(1);
            mDatabase.commit();

            setupTables();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }

        if (mDatabase != null) {
        }
    }

    private void setupTables() throws SqlJetException {
        mDatabase.beginTransaction(SqlJetTransactionMode.WRITE);
        mDatabase.createTable("CREATE TABLE IF NOT EXISTS " + Tables.USERS + " (username TEXT NOT NULL PRIMARY KEY, password TEXT NOT NULL);");
        mDatabase.commit();
    }

    //#region tables
    private ISqlJetTable getUsersTable() throws SqlJetException {
        return mDatabase.getTable(Tables.USERS);
    }
    //#endregion

    //#region inteface

    @Override
    public Map<String, User> getRegisteredUsers() {
        Map<String, User> users = new HashMap<>();

        try {
            ISqlJetTable table = getUsersTable();
            mDatabase.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetCursor cursor = table.open();

            while (!cursor.eof()) {
                String username = cursor.getString("username");
                String password = cursor.getString("password");

                users.put(username, new User(username, password));
                cursor.next();
            }

            mDatabase.commit();
        }
        catch (SqlJetException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Status addRegisteredUser(User user) {
        if (user.isDefault()) {
            return Status.Error;
        }

        try {
            mDatabase.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetCursor cursor = getUsersTable().lookup(getUsersTable().getPrimaryKeyIndexName(), user.getUsername());

            if (cursor.eof()) {
                if (getUsersTable().insert(user.getUsername(), user.getPassword()) >= 0) {
                    mDatabase.commit();
                    return Status.Ok;
                }
            }
            mDatabase.commit();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }

        return Status.Error;
    }

    //#endregion
}
