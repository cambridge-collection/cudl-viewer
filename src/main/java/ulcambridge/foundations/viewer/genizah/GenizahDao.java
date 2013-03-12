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
	
	public List<FragmentReferenceList> getFragmentReferencesByClassmark(String classmark);
	
	public List<BibliographyReferenceList> getBibliographyReferencesByAuthor(String queryString);
	
	public List<BibliographyReferenceList> getBibliographyReferencesByKeyword(String queryString);

}
