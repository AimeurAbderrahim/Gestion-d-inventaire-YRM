package db.java;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Inventorier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventorierDatabase extends EntityCoreDatabase<Inventorier> {

    public InventorierDatabase(String idColumn, String tableName) throws ConnectionFailedException, LoadPropertiesException {
        super(
                (idColumn == null) ? "id_modele" : idColumn,
                (tableName == null) ? "Inventorier" : tableName
        );
    }

    public InventorierDatabase(Connection connection, String idColumn, String tableName) {
        super(
                connection,
                (idColumn == null) ? "id_modele" : idColumn,
                (tableName == null) ? "Inventorier" : tableName
        );
    }

    public InventorierDatabase(ConfigDatabase con_connection, String idColumn, String tableName) throws ConnectionFailedException {
        super(
                con_connection,
                (idColumn == null) ? "id_modele" : idColumn,
                (tableName == null) ? "Inventorier" : tableName
        );
    }

    @Override
    protected String getIdValue(Inventorier obj) {
        // Clé composée, on retourne les deux parties séparées par un tiret
        return obj.getId_modele() + "-" + obj.getId_bon();
    }

    @Override
    protected int getColumnCount() {
        return 3; // id_modele, id_bon, quantite
    }

    @Override
    protected int getUpdateParameterCount() {
        return 3; // quantite + 2 paramètres pour WHERE clause (clé composée)
    }

    @Override
    protected void setAddParameters(PreparedStatement statement, Inventorier obj) throws SQLException {
        statement.setString(1, obj.getId_modele());
        statement.setString(2, obj.getId_bon());
        statement.setInt(3, obj.getQuantite());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Inventorier obj) throws SQLException {
        statement.setInt(1, obj.getQuantite());
        statement.setString(2, obj.getId_modele());
        statement.setString(3, obj.getId_bon());
    }

    @Override
    protected String buildUpdateSetClause() {
        return "quantite = ?";
    }

    @Override
    public Inventorier mapResultSetToEntity(ResultSet result) throws SQLException {
        Inventorier inv = new Inventorier();
        inv.setId_modele(result.getString("id_modele"));
        inv.setId_bon(result.getString("id_bon"));
        inv.setQuantite(result.getInt("quantite"));
        return inv;
    }

    @Override
    public String getSearchCondition() {
        // Exemple : recherche par id_modele ou id_bon
        return "id_modele LIKE ? OR id_bon LIKE ?";
    }

    @Override
    public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        statement.setString(1, like);
        statement.setString(2, like);
    }

    @Override
    public String generatedIdPK() throws SQLException {
        // Pas applicable : la table a une clé composée non auto-générée
        return null;
    }
}
