package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.BonReception;

public class BonReceptionDatabase extends EntityCoreDatabase<BonReception> {

    public BonReceptionDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
        super(
            (idCol == null) ? "id_bon" : idCol,
            (tableName == null) ? "BonReception" : tableName
        );
    }

    public BonReceptionDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
        super(
            conn,
            (idCol == null) ? "id_bon" : idCol,
            (tableName == null) ? "BonReception" : tableName
        );
    }

    public BonReceptionDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
        super(
            conn,
            (idCol == null) ? "id_bon" : idCol,
            (tableName == null) ? "BonReception" : tableName
        );
    }

    @Override
    protected String getIdValue(BonReception obj) {
        return obj.getId_bon();
    }

    @Override
    protected int getColumnCount() {
        return 3;
    }

    @Override
    protected int getUpdateParameterCount() {
        return 2;
    }

    @Override
    protected void setAddParameters(PreparedStatement statement, BonReception obj) throws SQLException {
        statement.setString(1, obj.getId_bon());
        statement.setDate(2, java.sql.Date.valueOf(obj.getDate_reception()));
        statement.setString(3, obj.getId_fournisseur());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, BonReception obj) throws SQLException {
        statement.setDate(1, java.sql.Date.valueOf(obj.getDate_reception()));
        statement.setString(2, obj.getId_fournisseur());
    }

    @Override
    protected String buildUpdateSetClause() {
        return "date_reception = ?, id_fournisseur = ?";
    }

    @Override
    public BonReception mapResultSetToEntity(ResultSet result) throws SQLException {
        BonReception bon = new BonReception();
        bon.setId_bon(result.getString("id_bon"));
        bon.setDate_reception(result.getDate("date_reception").toLocalDate());
        bon.setId_fournisseur(result.getString("id_fournisseur"));
        return bon;
    }

    @Override
    public String getSearchCondition() {
        return "id_bon LIKE ? OR id_fournisseur LIKE ?";
    }

    @Override
    public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
        String searchPattern = "%" + keyword + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
    }

    @Override
    public String generatedIdPK() throws SQLException {
        long idx = super.countAll();
        return "BON" + String.format("%03d", idx);
    }
} 