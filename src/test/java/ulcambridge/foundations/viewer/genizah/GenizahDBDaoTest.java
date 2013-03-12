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
		GenizahDBDao dao = new GenizahDBDao();
		dao.setDataSource(getDataSource());
		List<AuthorBibliography> authorBib = dao.getTitlesByAuthor("Out");
		Assert.assertTrue(authorBib.size() > 0);
		AuthorBibliography autBib0 = authorBib.get(0);
		Assert.assertTrue(autBib0.getBibliography().size() > 0);
	}
	
	@Test
	public void byKeyword() {
		GenizahDBDao dao = new GenizahDBDao();
		dao.setDataSource(getDataSource());
		List<BibliographyEntry> bib = dao.getTitlesByKeyword("Greek");
		Assert.assertTrue(bib.size() > 0);
	}
	
	@Test
	public void byClassmark() {
		GenizahDBDao dao = new GenizahDBDao();
		dao.setDataSource(getDataSource());
		List<Fragment> fragments = dao.getFragmentsByClassmark("T-S 12.192");
		Assert.assertTrue(fragments.size() == 1);
	}

}

