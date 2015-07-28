package ulcambridge.foundations.viewer.dao;

import java.sql.Timestamp;
import java.util.Hashtable;

public interface LastUpdateDao {

	public Hashtable<String, Timestamp> getLastUpdate();
	
}
