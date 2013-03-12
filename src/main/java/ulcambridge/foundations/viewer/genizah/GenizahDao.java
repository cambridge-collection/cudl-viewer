package ulcambridge.foundations.viewer.genizah;

import java.util.List;

/**
 * Single access point for data in the database.
 *  
 * @author gilleain
 *
 */
public interface GenizahDao {
	
	public List<BibliographySearchResult> authorSearch(String author);
	
	public List<BibliographySearchResult> keywordSearch(String keyword);
	
	public List<FragmentSearchResult> classmarkSearch(String classmark);

	public FragmentReferenceList getFragmentReferencesByClassmark(String classmark);
	
	public BibliographyReferenceList getBibliographyReferencesByTitleId(int id);
	
}
