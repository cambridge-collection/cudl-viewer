package ulcambridge.foundations.viewer.genizah;

import java.util.List;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class GenizahDBDaoTest extends TestCase {
	
	static {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GenizahDBDaoTest(String testName) {
		super(testName);
	}
	
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(GenizahDBDaoTest.class);
	}
	
	public GenizahDao getDaoSource() {
		// XXX - Jenkins server has no DB connection
//		GenizahDBDao dao = new GenizahDBDao();
//		dao.setDataSource(getDataSource());
		GenizahMockDao dao = new GenizahMockDao();
		return dao;
	}
	
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		//dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setDriverClassName("org.postgresql.Driver");
		//dataSource.setUrl("jdbc:mysql://found-dom01.lib.cam.ac.uk:3306/genizahbibdev");
		dataSource.setUrl("jdbc:postgresql://cudl-postgres.cmzjzpssbgnq.eu-west-1.rds.amazonaws.com:5432/genizahbibdev?autoReconnect=true");
		//dataSource.setUsername("genbibdevuser");
		dataSource.setUsername("cudldev");
		//dataSource.setPassword("resuvedbibneg");
		dataSource.setPassword("anwqxJcONu1NjUl");
		return dataSource;
	}
	
	public void testFragmentBiblioByClassmark() {
		GenizahDao dao = getDaoSource();
		FragmentReferenceList fragmentRefs = 
				dao.getFragmentReferencesByClassmark("MS-TS-AR-00054-00093");
		assertNotNull(fragmentRefs);
		List<FragmentReferences> refs = fragmentRefs.getFragmentReferences(); 
		assertTrue(refs.size() > 0);
		assertNotNull(refs.get(0).getEntry());
		assertNotNull(refs.get(0).getEntry().getTitle());
		assertNotNull(refs.get(0).getTypeList());
	}
	
	public void testAuthorSearch() {
		GenizahDao dao = getDaoSource();
		List<BibliographySearchResult> results = dao.authorSearch("Outh*");
		assertNotNull(results);
		assertTrue(results.size() > 0);
		assertNotNull(results.get(0).getBibliographyEntry());
		assertNotNull(results.get(0).getBibliographyEntry().getTitle());
	}
	
	public void testKeywordSearch() {
		GenizahDao dao = getDaoSource();
		List<BibliographySearchResult> results = dao.keywordSearch("*Bird*");
		assertNotNull(results);
		assertTrue(results.size() > 0);
		assertNotNull(results.get(0).getBibliographyEntry());
		assertNotNull(results.get(0).getBibliographyEntry().getTitle());
	}
	
	public void testClassmarkSearch() {
		GenizahDao dao = getDaoSource();
		List<FragmentSearchResult> results = dao.classmarkSearch("T-S Ar.54.9*");
		assertNotNull(results);
		assertTrue(results.size() > 0);
		assertNotNull(results.get(0).getFragment());
		assertNotNull(results.get(0).getFragment().getLabel());
	}
	
	public void testGetBibRefsByTitleId() {
		GenizahDao dao = getDaoSource();
		BibliographyReferenceList refList = dao.getBibliographyReferencesByTitleId(2907);
		assertNotNull(refList);
		assertNotNull(refList.getBibligraphyEntry());
		assertNotNull(refList.getBibliographyReferences());
	}
	
	public void testGetFragRefsByClassmarkId() {
		GenizahDao dao = getDaoSource();
		FragmentReferenceList refList = dao.getFragmentReferencesByClassmark("MS-TS-AR-00054-00009");
		assertNotNull(refList);
		assertNotNull(refList.getFragment());
		assertNotNull(refList.getFragmentReferences());
	}
	
	public void testClassmarkBug_FOUNDVIEW_151() {
		GenizahDao dao = getDaoSource();
		List<FragmentSearchResult> results = dao.classmarkSearch("T-S 24.74");
		assertNotNull(results);
		assertTrue(results.size() > 0);
		assertNotNull(results.get(0).getFragment());
		assertNotNull(results.get(0).getFragment().getLabel());
	}

}
