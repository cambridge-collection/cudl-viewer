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
	
	private static final String bibliographyColumnNames = 
			"CY, DA, DO, ET, M1, NV, OP, PB, PY, RN, RP, " +
			"SN, SP, ST, SV, T2, TI, TT, TY, VL ";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private String convertWildcards(String queryString) {
		return queryString.replace("*", "%");
	}
	
	private String addWildcards(String queryString) {
		return queryString = "%" + queryString + "%";
	}
	
	@Override
	public List<BibliographySearchResult> authorSearch(String author) {
		String authorQuery = "SELECT Author, Title FROM Author WHERE Author LIKE ?";
//		String percentWrappedString = convertWildcards(author);
		String percentWrappedString = addWildcards(author);
		
		final Map<String, List<Integer>> authorTitleMap = jdbcTemplate.query(
				authorQuery,
				new Object[] { percentWrappedString },
				new ResultSetExtractor<Map<String, List<Integer>>>() {
					
					@Override
					public Map<String, List<Integer>> extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						Map<String, List<Integer>> authorMap = 
								new HashMap<String, List<Integer>>();
						while (resultSet.next()) {
							String author = resultSet.getString("Author");
							int title = resultSet.getInt("Title");
							List<Integer> titleIds;
							if (authorMap.containsKey(author)) {
								titleIds = authorMap.get(author);
							} else {
								titleIds = new ArrayList<Integer>();
								authorMap.put(author, titleIds);
							}
							titleIds.add(title);
						}
						return authorMap;
					}
				}
		);
		
		String bibInfoQuery = "SELECT " + bibliographyColumnNames + 
							  "FROM Bibliograph WHERE ID = ?";
		List<BibliographySearchResult> results = new ArrayList<BibliographySearchResult>();
		for (final String authorString : authorTitleMap.keySet()) {
			List<Integer> titleIds = authorTitleMap.get(authorString);
			for (final int id : titleIds) {
				final List<BibliographySearchResult> biblioResults = jdbcTemplate.query(
						bibInfoQuery,
						new Object[] { id },
						new RowMapper<BibliographySearchResult>() {
							
							@Override
							public BibliographySearchResult mapRow(
									ResultSet resultSet, int rowNum) throws SQLException {
								String title = resultSet.getString("TI");
								BibliographyEntry entry = new BibliographyEntry(id, title);
								fillBibliographyEntry(resultSet, entry);
								return new BibliographySearchResult(
												authorString, entry, 0);
							}
						}
				);
				results.addAll(biblioResults);
			}
		}
		
		for (BibliographySearchResult result : results) {
			fillBibliographyAuthors(result.getBibliographyEntry());
			fillBibliographyRefCount(result);
		}
		
		return results;
	}

	@Override
	public List<BibliographySearchResult> keywordSearch(String keyword) {
			String bibInfoQuery = "SELECT Bibliograph.ID, count(*) as RefCount, " + 
							  bibliographyColumnNames  + 
							  "FROM Bibliograph JOIN Reference " +
							  "ON Bibliograph.ID = Reference.Title " + 
							  "WHERE TI LIKE ? GROUP BY Bibliograph.ID";
//		String percentWrappedString = convertWildcards(keyword);
		String percentWrappedString = addWildcards(keyword);
		
		final List<BibliographySearchResult> results = jdbcTemplate.query(
				bibInfoQuery,
				new Object[] { percentWrappedString },
				new RowMapper<BibliographySearchResult>() {
					
					@Override
					public BibliographySearchResult mapRow(ResultSet resultSet, int rowIndex)
								throws SQLException, DataAccessException {
						BibliographyEntry entry = new BibliographyEntry(
								resultSet.getInt("ID"),
								resultSet.getString("TI")
						);
						fillBibliographyEntry(resultSet, entry);
						int refCount = resultSet.getInt("RefCount");
						return new BibliographySearchResult("", entry, refCount);
					}
				}
		);
		for (BibliographySearchResult result : results) {
			fillBibliographyAuthors(result.getBibliographyEntry());
		}
		return results;
	}
	
	@Override
	public List<FragmentSearchResult> classmarkSearch(String classmark) {
		String query = "SELECT LB, Classmark, count(*) as RefCount " +
					   "FROM Fragment JOIN Reference ON Fragment.ID = Reference.Fragment " +
					   "WHERE LB LIKE ? GROUP BY Classmark";
		String percentWrappedString = convertWildcards(classmark);
		
		return jdbcTemplate.query(
				query,
				new Object[] { percentWrappedString },
				new RowMapper<FragmentSearchResult>() {
					
					@Override
					public FragmentSearchResult mapRow(ResultSet resultSet, int rowIndex)
							throws SQLException, DataAccessException {
						String label = resultSet.getString("LB");
						String classmark = resultSet.getString("Classmark");
						int refCount = resultSet.getInt("RefCount");
						return new FragmentSearchResult("", new Fragment(label, classmark), refCount);
					}
				}
			);
	}

	@Override
	public BibliographyReferenceList getBibliographyReferencesByTitleId(final int id) {
		String query = "SELECT LB, Classmark, C4, C6 " +
					   "FROM Reference JOIN Fragment ON Reference.Fragment = Fragment.ID " +
					   "WHERE Title = ?";
		
		final List<BibliographyReferences> refs = jdbcTemplate.query(
				query,
				new Object[] { id },
				new RowMapper<BibliographyReferences>() {
					
					@Override
					public BibliographyReferences mapRow(ResultSet resultSet, int rowIndex)
							throws SQLException, DataAccessException {
						String label = resultSet.getString("LB");
						String classmark = resultSet.getString("Classmark");
						String refType = resultSet.getString("C4");
						String refPosition = resultSet.getString("C6"); // location in text
						return new BibliographyReferences(
								refType, refPosition, new Fragment(label, classmark));
					}
				}
			);
		String bibliographyQuery = "SELECT " + bibliographyColumnNames + 
								   "FROM Bibliograph WHERE ID = ?";
		BibliographyReferenceList bibList = jdbcTemplate.query(bibliographyQuery, 
								  new Object[] { id }, 
								  new ResultSetExtractor<BibliographyReferenceList>() {

									@Override
									public BibliographyReferenceList extractData(
											ResultSet resultSet)
											throws SQLException, DataAccessException {
										resultSet.next();
										String title = resultSet.getString("TI");
										BibliographyEntry entry = new BibliographyEntry(id, title);
										fillBibliographyEntry(resultSet, entry);
										return new BibliographyReferenceList(entry, refs);
									}
			}
		);
		fillBibliographyAuthors(bibList.getBibligraphyEntry());
		return bibList;
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
	
	private void fillBibliographyRefCount(final BibliographySearchResult bibResult) {
		BibliographyEntry bibEntry = bibResult.getBibliographyEntry();
		String authorQuery = "SELECT count(*) as RefCount from Reference where Title = " + bibEntry.getId();
		jdbcTemplate.query(authorQuery, new ResultSetExtractor<Object>() {

			@Override
			public Object extractData(ResultSet resultSet) 
					throws SQLException, DataAccessException {
				while (resultSet.next()) {
					int refCount = resultSet.getInt("RefCount");
					bibResult.setRefCount(refCount);
				}
				// don't care about the return, as we are adding data to an existing object
				return null;
			}
			
		});
	}
	
	private void fillBibliographyEntry(ResultSet resultSet, BibliographyEntry entry) throws SQLException {
		entry.setDate(resultSet.getString("DA"));
		entry.setDoi(resultSet.getString("DO"));
		entry.setEdition(resultSet.getString("ET"));
		entry.setNumber(resultSet.getString("M1"));
		entry.setNumberVols(resultSet.getString("NV"));
		entry.setOriginalPublisher(resultSet.getString("OP"));
		entry.setPublisher(resultSet.getString("PB"));
		entry.setYear(resultSet.getString("PY"));
		entry.setResearchNotes(resultSet.getString("RN"));
		entry.setReprintEdition(resultSet.getString("RP"));
		entry.setIsbn(resultSet.getString("SN"));
		entry.setStartPage(resultSet.getString("SP"));
		entry.setShortTitle(resultSet.getString("ST"));
		entry.setSvCol(resultSet.getString("SV"));
		entry.setSecondaryTitle(resultSet.getString("T2"));
		entry.setTranslatedTitle(resultSet.getString("TT"));
		entry.setType(resultSet.getString("TY"));
		entry.setVolume(resultSet.getString("VL"));
	}
	
	@Override
	public FragmentReferenceList getFragmentReferencesByClassmark(final String classmark) {
		String query = "SELECT LB, Bibliograph.ID, C4, C6, " + bibliographyColumnNames +
						"FROM Fragment JOIN Reference ON Fragment.ID = Reference.Fragment " +
						"JOIN Bibliograph ON Reference.Title = Bibliograph.ID " +
						"WHERE Classmark = ?";
		final FragmentReferenceList fragmentBibliographies = jdbcTemplate.query(
				query,
				new Object[] { classmark },
				new ResultSetExtractor<FragmentReferenceList>() {
					@Override
					public FragmentReferenceList extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						List<FragmentReferences> fragRefs = new ArrayList<FragmentReferences>();
						Fragment fragment = null;
						while (resultSet.next()) {
							String label = resultSet.getString("LB");
							int biblioId = resultSet.getInt("ID");
							String title = resultSet.getString("TI");
							String refType = resultSet.getString("C4");		// m, x, tx, etc
							String refPosition = resultSet.getString("C6"); // location in text
							
							if (fragment == null) {
								fragment = new Fragment(label, classmark);
							}
							
							BibliographyEntry entry = new BibliographyEntry(biblioId, title);
							fillBibliographyEntry(resultSet, entry);
							fillBibliographyAuthors(entry);
							fragRefs.add(new FragmentReferences(refType, refPosition, entry));
						}
						return new FragmentReferenceList(fragment, fragRefs);
					}
				}
		);
		return fragmentBibliographies;
	}
	
}
