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
		String query = "SELECT Author, Bibliograph.ID, DA, DO, ET, M1, PB, PY, TI FROM Author " +
					   "LEFT JOIN Bibliograph " +
					   "ON Author.Title = Bibliograph.ID WHERE Author LIKE ?"; 

		String percentWrappedString = "%" + author + "%";
		
		final List<AuthorBibliography> authorBibliographies = jdbcTemplate.query(
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
							int id = resultSet.getInt("ID");
							List<BibliographyEntry> bibliography;
							if (authorMap.containsKey(author)) {
								bibliography = authorMap.get(author);
							} else {
								bibliography = new ArrayList<BibliographyEntry>();
								authorMap.put(author, bibliography);
							}
							BibliographyEntry entry = new BibliographyEntry(id, title);
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
		
		for (AuthorBibliography authorBibliography : authorBibliographies) {
			for (final BibliographyEntry bibEntry : authorBibliography.getBibliography()) {
				fillBibliographyAuthors(bibEntry);
			}
		}
		
		return authorBibliographies;
		
	}
	
	private void fillBibliographyAuthors(final BibliographyEntry bibEntry) {
		String authorQuery = "SELECT Author from Author WHERE Title = " + bibEntry.getId();
		jdbcTemplate.query(authorQuery, new ResultSetExtractor<Object>() {

			@Override
			public Object extractData(ResultSet resultSet) 
					throws SQLException, DataAccessException {
				while (resultSet.next()) {
					String author = resultSet.getString("Author");
					bibEntry.addAuthor(author);
				}
				// don't care about the return, as we are adding data to an existing object
				return null;
			}
			
		});
	}

	@Override
	public List<BibliographyEntry> getTitlesByKeyword(String keyword) {
		String query = "SELECT ID, DA, DO, ET, M1, PB, PY, TI FROM Bibliograph WHERE TI LIKE ?";

		String percentWrappedString = "%" + keyword + "%";
		List<BibliographyEntry> bibEntries = jdbcTemplate.query(
				query, new Object[] { percentWrappedString },
				new RowMapper<BibliographyEntry>() {
					public BibliographyEntry mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						BibliographyEntry entry = new BibliographyEntry(
								resultSet.getInt("ID"),
								resultSet.getString("TI")
					    );
						fillBibliographyEntry(resultSet, entry);
						return entry;
					}
				});
		for (final BibliographyEntry bibEntry : bibEntries) {
			fillBibliographyAuthors(bibEntry);
		}
		return bibEntries;
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
		String query = "SELECT LB, Classmark FROM Fragment WHERE LB LIKE ?";

		String percentTerminatedString = classmark + "%";
		return jdbcTemplate.query(query, new Object[] { percentTerminatedString },
				new RowMapper<Fragment>() {
					public Fragment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return new Fragment(
								resultSet.getString("LB"),
								resultSet.getString("Classmark")
					    );
					}
				});
	}

}
