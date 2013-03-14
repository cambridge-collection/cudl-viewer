package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

public class GenizahMockDao implements GenizahDao {

	@Override
	public List<BibliographySearchResult> authorSearch(String authorQuery) {
		int id = 1;
		int refCount = 10;
		
		List<BibliographySearchResult> results = new ArrayList<BibliographySearchResult>();
		BibliographyEntry entry = new BibliographyEntry(id, "Title");
		BibliographySearchResult result = new BibliographySearchResult("", entry, refCount);
		results.add(result);
		return results;
	}

	@Override
	public List<BibliographySearchResult> keywordSearch(String keywordQuery) {
		int id = 1;
		int refCount = 10;
		String title = "Title";
		
		List<BibliographySearchResult> results = new ArrayList<BibliographySearchResult>();
		BibliographyEntry entry = new BibliographyEntry(id, title);
		BibliographySearchResult result = new BibliographySearchResult("", entry, refCount);
		results.add(result);
		return results;
	}

	@Override
	public List<FragmentSearchResult> classmarkSearch(String classmarkQuery) {
		String label = "TS 1";
		String classmark = "MS01";
		int refCount = 10;
		
		List<FragmentSearchResult> results = new ArrayList<FragmentSearchResult>();
		Fragment fragment = new Fragment(label, classmark);
		FragmentSearchResult result = new FragmentSearchResult("", fragment, refCount);
		results.add(result);
		return results;
	}

	@Override
	public FragmentReferenceList getFragmentReferencesByClassmark(String classmark) {
		int id = 1;
		String label = "TS 1";
		String refType = "m";
		String title = "Title";
		
		Fragment fragment = new Fragment(label, classmark);
		List<FragmentReferences> fragmentReferences = new ArrayList<FragmentReferences>();
		BibliographyEntry entry = new BibliographyEntry(id, title);
		fragmentReferences.add(new FragmentReferences(refType, entry));
		return new FragmentReferenceList(fragment, fragmentReferences);
	}

	@Override
	public BibliographyReferenceList getBibliographyReferencesByTitleId(int id) {
		String title = "Title";
		String refType = "m";
		String label = "TS 1";
		String classmark = "MS01";
		
		BibliographyEntry entry = new BibliographyEntry(id, title);
		List<BibliographyReferences> bibliographyReferences = new ArrayList<BibliographyReferences>();
		Fragment fragment = new Fragment(label, classmark);
		bibliographyReferences.add(new BibliographyReferences(refType, fragment));
		return new BibliographyReferenceList(entry, bibliographyReferences);
	}

}
