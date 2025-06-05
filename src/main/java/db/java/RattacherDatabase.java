package db.java;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Rattacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RattacherDatabase extends EntityCoreDatabase<Rattacher>{

    public RattacherDatabase(String idColumn, String tableName) throws ConnectionFailedException, LoadPropertiesException {
        super(idColumn, tableName);
    }

    public RattacherDatabase(Connection connection, String idColumn, String tableName) {
        super(connection, idColumn, tableName);
    }

    public RattacherDatabase(ConfigDatabase con_connection, String idColumn, String tableName) throws ConnectionFailedException {
        super(con_connection, idColumn, tableName);
    }

    @Override
    protected String getIdValue(Rattacher obj) {
        return obj.getId_bon() + "-" + obj.getId_f();
    }

    @Override
    protected int getColumnCount() {
        return 2;
    }

    @Override
    protected int getUpdateParameterCount() {
        return 2;
    }

    @Override
    protected void setAddParameters(PreparedStatement statement, Rattacher obj) throws SQLException {
        statement.setString(1, obj.getId_f());
        statement.setString(2, obj.getId_bon());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Rattacher obj) throws SQLException {
        statement.setString(1, obj.getId_f());
        statement.setString(2, obj.getId_bon());
    }

    @Override
    protected String buildUpdateSetClause() {
        return "id_f = ? , id_bon = ? ";
    }

    @Override
    public Rattacher mapResultSetToEntity(ResultSet result) throws SQLException {
        return new Rattacher(result.getString("id_f"),result.getString("id_bon"));
    }

    @Override
    public String getSearchCondition() {
        return "id_f LIKE ? OR id_bon LIKE ?";
    }

    @Override
    public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        statement.setString(1, like);
        statement.setString(2, like);
    }

    @Override
    public String generatedIdPK() throws SQLException {
        return null;
    }
}
