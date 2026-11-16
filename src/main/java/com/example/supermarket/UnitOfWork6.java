package com.example.supermarket;

import java.sql.Connection;
import java.sql.SQLException;

/** One JDBC transaction for creating a user, customer profile and account. */
public final class UnitOfWork6 implements AutoCloseable {
    private final Connection connection;
    private boolean completed;

    public UnitOfWork6(Database database) throws SQLException {
        connection = database.open();
        connection.setAutoCommit(false);
    }

    public Connection connection() { return connection; }

    public void commit() throws SQLException {
        connection.commit();
        completed = true;
    }

    public void rollback() throws SQLException {
        connection.rollback();
        completed = true;
    }

    @Override public void close() throws SQLException {
        try {
            if (!completed) connection.rollback();
        } finally {
            connection.close();
        }
    }
}
