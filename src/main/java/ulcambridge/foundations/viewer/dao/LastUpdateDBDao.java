package ulcambridge.foundations.viewer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class LastUpdateDBDao implements LastUpdateDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Hashtable<String, Timestamp> getLastUpdate() {

		String query = "SELECT description, last_updated FROM last_update";

		try {
			return (Hashtable<String, Timestamp>) jdbcTemplate.query(query,

			new ResultSetExtractor<Hashtable<String, Timestamp>>() {
				@Override
				public Hashtable<String, Timestamp> extractData(ResultSet rs)
						throws SQLException, DataAccessException {

					Hashtable<String, Timestamp> table = new Hashtable<String, Timestamp>();
					while (rs.next()) {
						table.put(rs.getString("description"),
								rs.getTimestamp("last_updated"));
					}
					return table;
				}
			});

		} catch (Exception e) {
			return null;
		}
	}
	
	
    public boolean setLastUpdate(String description, Timestamp timestamp){
    	
        try {            

            String sql = "UPDATE last_update SET last_updated=? WHERE description=?";
            
			jdbcTemplate.update(sql, new Object[] { timestamp, description });			
            
        } catch (DataAccessException ex) {
            ex.printStackTrace();
           return false;            		
        }
        
        return true;        
    }

}
