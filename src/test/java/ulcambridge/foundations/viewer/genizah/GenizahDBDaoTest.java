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
			Class.forName("com.mysql.jdbc.Driver");
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
		GenizahDBDao dao = new GenizahDBDao();
		dao.setDataSource(getDataSource());
		return dao;
	}
	
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://found-dom01.lib.cam.ac.uk:3306/genizahbibdev");
		dataSource.setUsername("genbibdevuser");
		dataSource.setPassword("resuvedbibneg");
		return dataSource;
	}
	
	public void fragmentBiblioByClassmark() {
		GenizahDao dao = getDaoSource();
		FragmentReferenceList fragmentRefs = dao.getFragmentReferencesByClassmark("T-S Ar.37.170");
		for (FragmentReferences ref : fragmentRefs.getFragmentReferences()) {
			System.out.println(ref.getEntry().getTitle() + "\t" + ref.getTypeList());
		}
	}
	
	public void fragmentBiblioByClassmark_WildcardAtEnd() {
		GenizahDao dao = getDaoSource();
		FragmentReferenceList fragmentRefs = dao.getFragmentReferencesByClassmark("T-S Ar.37.17*");
		for (FragmentReferences ref : fragmentRefs.getFragmentReferences()) {
			System.out.println(ref.getEntry().getTitle() + "\t" + ref.getTypeList());
		}
	}
	
	public void authorSearch() {
		GenizahDao dao = getDaoSource();
		List<BibliographySearchResult> results = dao.authorSearch("Outh");
		for (BibliographySearchResult result : results) {
			BibliographyEntry entry = result.getBibliographyEntry();
			System.out.println(entry.getTitle() + "\t" + entry.getYear()
					+ "\t" + entry.getAuthors().size() +  " Authors " );
		}
	}
	
	public void keywordSearch() {
		GenizahDao dao = getDaoSource();
		List<BibliographySearchResult> results = dao.keywordSearch("*Bird*");
		for (BibliographySearchResult result : results) {
			BibliographyEntry entry = result.getBibliographyEntry();
			System.out.println(entry.getTitle() + "\t" + entry.getYear()
					+ "\t" + entry.getAuthors().size() +  " Authors " 
					+ "\t" + result.getRefCount() + " Refs");
		}
	}
	
	public void classmarkSearch() {
		GenizahDao dao = getDaoSource();
		List<FragmentSearchResult> results = dao.classmarkSearch("T-S Ar.54.9*");
		for (FragmentSearchResult result : results) {
			Fragment fragment = result.getFragment();
			System.out.println(fragment.getLabel() + "\t" + result.getRefCount());
		}
	}
	
	public void getBibRefsByTitleId() {
		GenizahDao dao = getDaoSource();
		BibliographyReferenceList refList = dao.getBibliographyReferencesByTitleId(2907);
		System.out.println(refList.getBibligraphyEntry().getTitle());
		for (BibliographyReferences ref : refList.getBibliographyReferences()) {
			System.out.println(ref.getTypeReadableForm() + "\t" + ref.getFragment().getLabel());
		}
	}
	
	public void getFragRefsByClassmarkId() {
		GenizahDao dao = getDaoSource();
		FragmentReferenceList refList = dao.getFragmentReferencesByClassmark("MS-TS-AR-00054-00009");
		System.out.println(refList.getFragment().getLabel());
		for (FragmentReferences ref : refList.getFragmentReferences()) {
			System.out.println(ref.getTypeReadableForm() + "\t" + ref.getEntry().getTitle());
		}
	}

}
