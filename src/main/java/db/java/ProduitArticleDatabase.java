package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.ProduitArticle;

public class ProduitArticleDatabase extends EntityCoreDatabase<ProduitArticle> {

	public ProduitArticleDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_article" : idCol,
				(tableName == null) ? "ProduitArticle" : tableName
		     );
	}

	public ProduitArticleDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_article" : idCol,
				(tableName == null) ? "ProduitArticle" : tableName
		     );
	}

	public ProduitArticleDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_article" : idCol,
				(tableName == null) ? "ProduitArticle" : tableName
		     );
	}

	@Override
	protected String getIdValue(ProduitArticle obj) {
		return obj.getId_article();
	}

	@Override
	protected int getColumnCount() {
		return 6;
	}

	@Override
	protected int getUpdateParameterCount() {
		return 5;
	}

	@Override
	protected void setAddParameters(PreparedStatement statement, ProduitArticle obj) throws SQLException {
		statement.setString(1, this.generatedIdPK());
		statement.setDate(2, java.sql.Date.valueOf(obj.getDate_peremption()));
		statement.setString(3, obj.getNom_article());
		statement.setInt(4, obj.getQuantite_global());
		statement.setDate(5, java.sql.Date.valueOf(obj.getDate_dachat()));
		statement.setString(6, obj.getId_modele());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, ProduitArticle obj) throws SQLException {
		statement.setDate(1, java.sql.Date.valueOf(obj.getDate_peremption()));
		statement.setString(2, obj.getNom_article());
		statement.setInt(3, obj.getQuantite_global());
		statement.setDate(4, java.sql.Date.valueOf(obj.getDate_dachat()));
		statement.setString(5, obj.getId_modele());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "date_peremption = ?, nom_article = ?, quantite_global = ?, date_dachat = ?, id_modele = ?";
	}

	@Override
	public ProduitArticle mapResultSetToEntity(ResultSet result) throws SQLException {
		ProduitArticle article = new ProduitArticle();
		article.setId_article(result.getString("id_article"));
		article.setDate_peremption(result.getDate("date_peremption").toLocalDate());
		article.setNom_article(result.getString("nom_article"));
		article.setQuantite_global(result.getInt("quantite_global"));
		article.setDate_dachat(result.getDate("date_dachat").toLocalDate());
		article.setId_modele(result.getString("id_modele"));
		return article;
	}

	@Override
	public String getSearchCondition() {
		return "nom_article LIKE ? OR id_article LIKE ?";
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
		return "ART" + String.format("%03d", idx);
	}
}
