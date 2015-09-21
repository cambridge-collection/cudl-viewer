package ulcambridge.foundations.viewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import ulcambridge.foundations.viewer.model.Item;

public class ItemsDBDao implements ItemsDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Item getItem(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean getItemTaggingStatus(String itemId) {
		
		String query = "SELECT taggingstatus FROM items WHERE itemid = ?";
		
		return jdbcTemplate.query(query, new Object[] { itemId }, 
				new ResultSetExtractor<Boolean>() {
					@Override
					public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
						while (rs.next()) {
							return rs.getBoolean("taggingstatus");
						}
						return false;
					}
				});
	}

}
