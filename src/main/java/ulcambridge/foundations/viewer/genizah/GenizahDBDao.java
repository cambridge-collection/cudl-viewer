package ulcambridge.foundations.viewer.genizah;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * Genizah bibliography data from a database.
 * 
 * @author gilleain
 *
 */
public class GenizahDBDao implements GenizahDao {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<AuthorBibliography> getTitlesByAuthor(String author) {
		String query = "SELECT Author, DA, DO, ET, M1, PB, PY, TI FROM Author LEFT JOIN Bibliograph " +
					   "ON Author.Title = Bibliograph.ID WHERE Author LIKE ?"; 

		String percentWrappedString = "%" + author + "%";
		
		return jdbcTemplate.query(
				query,
				new Object[] { percentWrappedString },
				new ResultSetExtractor<List<AuthorBibliography>>() {
					@Override
					public List<AuthorBibliography> extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						Map<String, List<BibliographyEntry>> authorMap = 
								new HashMap<String, List<BibliographyEntry>>();
						while (resultSet.next()) {
							String author = resultSet.getString("Author");
							String title = resultSet.getString("TI");
							List<BibliographyEntry> bibliography;
							if (authorMap.containsKey(author)) {
								bibliography = authorMap.get(author);
							} else {
								bibliography = new ArrayList<BibliographyEntry>();
								authorMap.put(author, bibliography);
							}
							BibliographyEntry entry = new BibliographyEntry(title);
							fillBibliographyEntry(resultSet, entry);
							bibliography.add(entry);
						}
						List<AuthorBibliography> authorBibliographies = new ArrayList<AuthorBibliography>();
						for (String authorKey : authorMap.keySet()) {
							authorBibliographies.add(new AuthorBibliography(authorKey, authorMap.get(authorKey)));
						}
						return authorBibliographies;
					}
				}
		);
		
	}

	@Override
	public List<BibliographyEntry> getTitlesByKeyword(String keyword) {
		String query = "SELECT DA, DO, ET, M1, PB, PY, TI FROM Bibliograph WHERE TI LIKE ?";

		String percentWrappedString = "%" + keyword + "%";
		return jdbcTemplate.query(query, new Object[] { percentWrappedString },
				new RowMapper<BibliographyEntry>() {
					public BibliographyEntry mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						BibliographyEntry entry = new BibliographyEntry(
								resultSet.getString("TI")
					    );
						fillBibliographyEntry(resultSet, entry);
						return entry;
					}
				});
	}
	
	private void fillBibliographyEntry(ResultSet resultSet, BibliographyEntry entry) throws SQLException {
		entry.setDate(resultSet.getString("DA"));
		entry.setDoi(resultSet.getString("DO"));
		entry.setEdition(resultSet.getString("ET"));
		entry.setNumber(resultSet.getString("M1"));
		entry.setPublisher(resultSet.getString("PB"));
		entry.setYear(resultSet.getString("PY"));
	}

	@Override
	public List<Fragment> getFragmentsByClassmark(String classmark) {
		String query = "SELECT LB, Title FROM Fragment WHERE LB = ?";

		return jdbcTemplate.query(query, new Object[] { classmark },
				new RowMapper<Fragment>() {
					public Fragment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return new Fragment(
								resultSet.getString("LB"),
								resultSet.getInt("Title")
					    );
					}
				});
	}

}
