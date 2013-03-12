package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Single access point for data in the database.
 *  
 * @author gilleain
 *
 */
public interface GenizahDao {
	
	public List<AuthorBibliography> getTitlesByAuthor(String author);
	
	public List<BibliographyEntry> getTitlesByKeyword(String keyword);
	
	public List<Fragment> getFragmentsByClassmark(String classmark);
	
	public List<FragmentReferences> getFragmentReferences(String classmark);

}
