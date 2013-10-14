package ulcambridge.foundations.viewer.genizah;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ulcambridge.foundations.viewer.model.Properties;

public class ClassmarkConverterTest extends TestCase {
	
	public ClassmarkConverterTest(String testName) {
		super(testName);
	}
	
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ClassmarkConverterTest.class);
	}
	
	public JdbcTemplate getJDBCSource() {
		return new JdbcTemplate(getDataSource());
	}
	
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		dataSource.setDriverClassName(Properties.getString("gn.jdbc.driver"));
		dataSource.setUrl(Properties.getString("gn.jdbc.url"));
		dataSource.setUsername(Properties.getString("gn.jdbc.user"));
		dataSource.setPassword(Properties.getString("gn.jdbc.password"));
		return dataSource;
	}
	
//	public void testDB() {
//		JdbcTemplate jdbcTemplate = getJDBCSource();
//		final Map<String, String> labelClassmarkMap = jdbcTemplate.query(
//				"SELECT LB, Classmark FROM Fragment", 
//				new ResultSetExtractor<Map<String, String>>() {
//
//					@Override
//					public Map<String, String> extractData(ResultSet resultSet)
//							throws SQLException, DataAccessException {
//						Map<String, String> map = new HashMap<String, String>();
//						while (resultSet.next()) {
//							String label = resultSet.getString("LB");
//							String classmark = resultSet.getString("Classmark");
//							map.put(label, classmark);
//						}
//						return map;
//					}
//					
//				}
//		);
//		for (String label : labelClassmarkMap.keySet()) {
//			String dbClassmark = labelClassmarkMap.get(label);
//			String newClassmark = ClassmarkConverter.toNormal(label);
//			//System.out.println(label + "\t" + dbClassmark + "\t" + newClassmark);
//		}
//	}
	
	public void testToNormal() {
		String nonNormal = "T-S 24.74";
		String normal = ClassmarkConverter.toNormal(nonNormal);
		assertNotNull(normal);
		assertEquals(normal, "MS-TS-00024-00074");
	}
	
	public void testFromNormal() {
		String normal = "MS-TS-00024-00074";
		String nonNormal = ClassmarkConverter.fromNormal(normal);
		assertNotNull(nonNormal);
		assertEquals(nonNormal, "TS 24 74");
	}

}
