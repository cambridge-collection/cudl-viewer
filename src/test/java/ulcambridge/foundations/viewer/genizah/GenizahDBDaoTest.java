package ulcambridge.foundations.viewer.genizah;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class GenizahDBDaoTest {
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	@Test
	public void byAuthor() {
		GenizahDao dao = getDaoSource();
		List<AuthorBibliography> authorBib = dao.getTitlesByAuthor("Out");
		Assert.assertTrue(authorBib.size() > 0);
		AuthorBibliography autBib0 = authorBib.get(0);
		Assert.assertTrue(autBib0.getBibliography().size() > 0);
		for (AuthorBibliography autBib : authorBib) {
			for (BibliographyEntry bibEntry : autBib.getBibliography()) {
				System.out.print(bibEntry.getTitle() + "\t|");
				for (String author : bibEntry.getAuthors()) {
					System.out.print(author + "|");
				}
				System.out.println();
			}
		}
	}
	
	@Test
	public void byKeyword() {
		GenizahDao dao = getDaoSource();
		List<BibliographyEntry> bib = dao.getTitlesByKeyword("Bible");
		Assert.assertTrue(bib.size() > 0);
		for (BibliographyEntry bibEntry : bib) {
			System.out.print(bibEntry.getTitle() + "\t|");
			for (String author : bibEntry.getAuthors()) {
				System.out.print(author + "|");
			}
			System.out.println();
		}
	}
	
	@Test
	public void byClassmark() {
		GenizahDao dao = getDaoSource();
		List<Fragment> fragments = dao.getFragmentsByClassmark("T-S 12.192");
		Assert.assertTrue(fragments.size() == 1);
	}
	
	@Test
	public void fragmentBiblioByClassmark() {
		GenizahDao dao = getDaoSource();
		List<FragmentReferenceList> fragmentRefs = dao.getFragmentReferencesByClassmark("T-S Ar.37.170");
		Assert.assertTrue(fragmentRefs.size() == 1);
		for (FragmentReferenceList fragmentBib : fragmentRefs) {
			for (FragmentReferences ref : fragmentBib.getBibliographyReferences()) {
				System.out.println(ref.getEntry().getTitle() + "\t" + ref.getTypeReadableForm());
			}
		}
	}
	
	@Test
	public void fragmentBiblioByClassmark_WildcardAtEnd() {
		GenizahDao dao = getDaoSource();
		List<FragmentReferenceList> fragmentRefs = dao.getFragmentReferencesByClassmark("T-S Ar.37.17*");
		Assert.assertTrue(fragmentRefs.size() > 1);
		for (FragmentReferenceList fragmentBib : fragmentRefs) {
			for (FragmentReferences ref : fragmentBib.getBibliographyReferences()) {
				System.out.println(ref.getEntry().getTitle() + "\t" + ref.getTypeList());
			}
		}
	}
	
	@Test
	public void biblioFragmentByKeyword() {
		GenizahDao dao = getDaoSource();
		List<BibliographyReferenceList> bibRefList = dao.getBibliographyReferencesByKeyword("*Bird*");
		Assert.assertTrue(bibRefList.size() == 1);
		for (BibliographyReferenceList bibRef : bibRefList) {
			System.out.println(bibRef.getBibligraphyEntry().getTitle());
			for (FragmentReferences ref : bibRef.getBibliographyReferences()) {
				System.out.println(ref.getTypeReadableForm());
			}
		}
	}

}
