package ulcambridge.foundations.viewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ulcambridge.foundations.viewer.model.Collection;

public class CollectionsDBDao implements CollectionsDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<String> getCollectionIds() {

		String query = "SELECT collectionid FROM collections ORDER BY collectionorder";

		return (List<String>) jdbcTemplate.query(query, 
				new RowMapper<String>() {
					public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return resultSet.getString("collectionid");
					}
				});
	}
	

	public Collection getCollection(String collectionId) {

		String query = "SELECT collectionid, title, summaryurl, sponsorsurl, type, collectionorder, parentcollectionid, taggingstatus FROM collections WHERE collectionid = ? ORDER BY collectionorder";

		return (Collection) jdbcTemplate.queryForObject(query, new Object[] { collectionId }, 
				new RowMapper<Collection>() {
					public Collection mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return new Collection(resultSet.getString("collectionid"),
								resultSet.getString("title"), getItemIds(resultSet.getString("collectionid")),
								resultSet.getString("summaryurl"), resultSet.getString("sponsorsurl"), 
								resultSet.getString("type"), resultSet.getString("parentcollectionid"), 
								resultSet.getBoolean("taggingstatus"));
					}
				});
	}	

	private List<String> getItemIds(String collectionId) {

		String query = "SELECT itemid FROM itemsincollection WHERE collectionid = ? AND visible=true ORDER BY itemorder";

		return (List<String>) jdbcTemplate.query(query, new Object[] { collectionId },
			new RowMapper<String>() {
			public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return resultSet.getString("itemid");
			}
		});
	}

}