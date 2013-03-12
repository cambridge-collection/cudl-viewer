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
	
	private static final String bibliographyColumnNames = "DA, DO, ET, M1, PB, PY, TI, VL ";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private String convertWildcards(String queryString) {
		return queryString.replace("*", "%");
	}

	@Override
	public List<AuthorBibliography> getTitlesByAuthor(String author) {
		String query = "SELECT Author, Bibliograph.ID," + bibliographyColumnNames + 
					   "FROM Author " +
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
		entry.setVolume(resultSet.getString("VL"));
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
	
	@Override
	public List<BibliographyReferences> getBibliographyReferencesByKeyword(String queryString) {
		String query = "SELECT LB, Classmark, Bibliograph.ID, C4, " + bibliographyColumnNames + 
					   "FROM Bibliograph JOIN Reference ON Reference.Title = Bibliograph.ID " +
					   "JOIN Fragment ON Fragment.ID = Reference.Fragment " +
					   "WHERE TI LIKE ?";
		String convertedQueryString = convertWildcards(queryString);
//		String convertedQueryString = queryString + "%";
		
		final List<BibliographyReferences> bibliographyReferences = jdbcTemplate.query(
				query,
				new Object[] { convertedQueryString },
				new ResultSetExtractor<List<BibliographyReferences>>() {
					@Override
					public List<BibliographyReferences> extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						Map<Integer, Map<String, List<String>>> fragmentReferenceMap = 
								new HashMap<Integer, Map<String, List<String>>>();
						// TODO : use fragment as a key directly in the fragmentMap (add equals/hashcode to Fragment)
						Map<String, Fragment> fragmentLookup = new HashMap<String, Fragment>();
						Map<Integer, BibliographyEntry> referenceLookup = new HashMap<Integer, BibliographyEntry>();
						while (resultSet.next()) {
							String label = resultSet.getString("LB");
							String classmark = resultSet.getString("Classmark");
							int biblioId = resultSet.getInt("ID");
							String title = resultSet.getString("TI");
							String refType = resultSet.getString("C4");		// m, x, tx, etc
							Map<String, List<String>> references;
							if (fragmentReferenceMap.containsKey(biblioId)) {
								references = fragmentReferenceMap.get(biblioId);
								List<String> refTypes;
								if (references.containsKey(label)) {
									refTypes = references.get(label); 
								} else {
									refTypes = new ArrayList<String>();
									references.put(label, refTypes);
								}
								refTypes.add(refType);
							} else {
								references = new HashMap<String, List<String>>();
								List<String> refTypes = new ArrayList<String>();
								refTypes.add(refType);
								references.put(label, refTypes);
								fragmentReferenceMap.put(biblioId, references);
								fragmentLookup.put(label, new Fragment(label, classmark));
							}
							// add a new referring work to the lookup
							if (!referenceLookup.containsKey(biblioId)) {
								BibliographyEntry entry = new BibliographyEntry(biblioId, title);
								fillBibliographyEntry(resultSet, entry);
								referenceLookup.put(biblioId, entry);
							}
						}
						List<BibliographyReferences> bibliographyReferences = new ArrayList<BibliographyReferences>();
						for (int biblioId : referenceLookup.keySet()) {
							BibliographyEntry entry = referenceLookup.get(biblioId);
							Map<String, List<String>> fragmentMap = fragmentReferenceMap.get(biblioId);
							List<Reference> references = new ArrayList<Reference>();
							for (String fragmentKey : fragmentMap.keySet()) {
								for (String refType : fragmentMap.get(fragmentKey)) {
									references.add(new Reference(refType, entry));
								}
							}
							bibliographyReferences.add(new BibliographyReferences(entry, references));
						}
						return bibliographyReferences;
					}
				}
		);
		
		for (BibliographyReferences bibliographyReferenceList : bibliographyReferences) {
			fillBibliographyAuthors(bibliographyReferenceList.getBibligraphyEntry());
		}
		
		return bibliographyReferences;
	}
	
	@Override
	public List<BibliographyReferences> getBibliographyReferencesByAuthor(String queryString) {
		return null;
	}

	@Override
	public List<FragmentReferences> getFragmentReferences(String classmarkQueryString) {
		String query = "SELECT LB, Classmark, Bibliograph.ID, C4, C6, " + bibliographyColumnNames +
						"FROM Fragment JOIN Reference ON Fragment.ID = Reference.Fragment " +
						"JOIN Bibliograph ON Reference.Title = Bibliograph.ID " +
						"WHERE LB LIKE ?";
		String convertedQueryString = convertWildcards(classmarkQueryString);
		final List<FragmentReferences> fragmentBibliographies = jdbcTemplate.query(
				query,
				new Object[] { convertedQueryString },
				new ResultSetExtractor<List<FragmentReferences>>() {
					@Override
					public List<FragmentReferences> extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						Map<String, Map<Integer, List<String>>> fragmentReferenceMap = 
								new HashMap<String, Map<Integer, List<String>>>();
						// TODO : use fragment as a key directly in the fragmentMap (add equals/hashcode to Fragment)
						Map<String, Fragment> fragmentLookup = new HashMap<String, Fragment>();
						Map<Integer, BibliographyEntry> referenceLookup = new HashMap<Integer, BibliographyEntry>();
						while (resultSet.next()) {
							String label = resultSet.getString("LB");
							String classmark = resultSet.getString("Classmark");
							int biblioId = resultSet.getInt("ID");
							String title = resultSet.getString("TI");
							String refType = resultSet.getString("C4");		// m, x, tx, etc
							String refPosition = resultSet.getString("C6"); // location in text
							Map<Integer, List<String>> references;
							if (fragmentReferenceMap.containsKey(label)) {
								references = fragmentReferenceMap.get(label);
								List<String> refTypes;
								if (references.containsKey(biblioId)) {
									refTypes = references.get(biblioId); 
								} else {
									refTypes = new ArrayList<String>();
									references.put(biblioId, refTypes);
								}
								refTypes.add(refType);
							} else {
								references = new HashMap<Integer, List<String>>();
								List<String> refTypes = new ArrayList<String>();
								refTypes.add(refType);
								references.put(biblioId, refTypes);
								fragmentReferenceMap.put(label, references);
								fragmentLookup.put(label, new Fragment(label, classmark));
							}
							// add a new referring work to the lookup
							if (!referenceLookup.containsKey(biblioId)) {
								BibliographyEntry entry = new BibliographyEntry(biblioId, title);
								fillBibliographyEntry(resultSet, entry);
								referenceLookup.put(biblioId, entry);
							}
						}
						List<FragmentReferences> fragmentBibliographies = new ArrayList<FragmentReferences>();
						for (String fragmentKey : fragmentReferenceMap.keySet()) {
							Fragment fragment = fragmentLookup.get(fragmentKey);
							Map<Integer, List<String>> referenceMap = fragmentReferenceMap.get(fragmentKey);
							List<Reference> references = new ArrayList<Reference>();
							for (int biblioId : referenceMap.keySet()) {
								BibliographyEntry entry = referenceLookup.get(biblioId);
								for (String refType : referenceMap.get(biblioId)) {
									references.add(new Reference(refType, entry));
								}
							}
							fragmentBibliographies.add(new FragmentReferences(fragment, references));
						}
						return fragmentBibliographies;
					}
				}
		);
		
		// TODO : annoying to have to do this, need to re-work this!
		List<BibliographyEntry> uniqueBibliography = new ArrayList<BibliographyEntry>();
		for (FragmentReferences fragmentBibliography : fragmentBibliographies) {
			List<BibliographyEntry> bibliography = fragmentBibliography.getBibliography();
			for (BibliographyEntry bibEntry : bibliography) {
				if (!uniqueBibliography.contains(bibEntry)) {
					uniqueBibliography.add(bibEntry);
				}
			}
		}
		
		for (final BibliographyEntry bibEntry : uniqueBibliography) {
			fillBibliographyAuthors(bibEntry);
		}
		
		return fragmentBibliographies;
	}

}
