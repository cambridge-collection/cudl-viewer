package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * 
 * @author Lei
 * 
 */
public class TermCombiner {

	private static final Logger logger = LoggerFactory.getLogger(TermCombiner.class);
	
	private int annoWeight;

	private int removedTagWeight;

	private int tagWeight;

	private int metaWeight;

	public TermCombiner() {
		this.annoWeight = Integer.parseInt(Properties.getString("weight.anno"));
		this.removedTagWeight = Integer.parseInt(Properties.getString("weight.removedtag"));
		this.tagWeight = Integer.parseInt(Properties.getString("weight.tag"));
		this.metaWeight = Integer.parseInt(Properties.getString("weight.meta"));
	}

	public DocumentTerms combine_Tag_RemovedTag(DocumentTags docTags, DocumentTags docRemovedTags) {

		int ratio_T_R = this.tagWeight / this.removedTagWeight;

		DocumentTerms docRTTerms = combine(docTags, docRemovedTags, ratio_T_R);
		removeInvalidTerms(docRTTerms.getTerms());

		return docRTTerms;
	}

	public DocumentTerms combine_Anno_Tag(DocumentAnnotations docAnnotations, DocumentTags docTags) {

		int ratio_A_T = this.tagWeight / this.annoWeight;

		DocumentTerms docATTerms = combine(docTags, docAnnotations, ratio_A_T);
		removeInvalidTerms(docATTerms.getTerms());

		return docATTerms;
	}

	public DocumentTerms combine_Anno_Tag_RemovedTag(DocumentAnnotations docAnnotations, DocumentTags docTags, DocumentTags docRemovedTags) {

		int ratio_A_R = this.annoWeight / this.removedTagWeight;
		int ratio_A_T = this.tagWeight / this.annoWeight;

		DocumentTerms docARTerms = combine(docAnnotations, docRemovedTags, ratio_A_R);
		DocumentTerms docARTTerms = combine(docTags, docARTerms, ratio_A_T);
		removeInvalidTerms(docARTTerms.getTerms());

		return docARTTerms;
	}

	public DocumentTerms updateToMetaLevel(DocumentAnnotations docAnnotations) {

		int ratio_A_M = this.metaWeight / this.annoWeight;

		DocumentTerms docAMTerms = combine(docAnnotations, ratio_A_M);
		removeInvalidTerms(docAMTerms.getTerms());

		return docAMTerms;
	}

	public DocumentTerms updateToMetaLevel(DocumentTags docTags, DocumentTags docRemovedTags) {
		// combine tags with removed tags
		DocumentTerms docRTTerms = combine_Tag_RemovedTag(docTags, docRemovedTags);
		
		int ratio_T_M = this.metaWeight / this.tagWeight;

		DocumentTerms docRTMTerms = combine(docRTTerms, ratio_T_M);
		removeInvalidTerms(docRTMTerms.getTerms());
		
		return docRTMTerms;
	}

	public DocumentTerms updateToMetaLevel(DocumentAnnotations docAnnotations, DocumentTags docTags, DocumentTags docRemovedTags) {
		// combine annotations, tags and removed tags
		DocumentTerms docARTTerms = combine_Anno_Tag_RemovedTag(docAnnotations, docTags, docRemovedTags);
		
		int ratio_T_M = this.metaWeight / this.tagWeight;

		DocumentTerms docARTMTerms = combine(docARTTerms, ratio_T_M);
		removeInvalidTerms(docARTMTerms.getTerms());

		return docARTMTerms;
	}

	private DocumentTerms combine(DocumentTerms docTerms1, DocumentTerms docTerms2, int ratio) {
		List<Term> terms1 = docTerms1.getTerms();
		List<Term> terms2 = docTerms2.getTerms();

		List<Term> mergedTerms = mergeTerms(terms1, terms2, ratio);

		return new DocumentTerms(docTerms1.getDocumentId(), mergedTerms.size(), mergedTerms);
	}

	private DocumentTerms combine(DocumentTerms docTerms, int ratio) {
		List<Term> terms = docTerms.getTerms();		

		List<Term> updatedTerms = updateTerms(terms, ratio);

		return new DocumentTerms(docTerms.getDocumentId(), updatedTerms.size(), updatedTerms); 
	}

	/**
	 * merge two term lists, recalculate raw if duplicates
	 * 
	 * @param terms1 with higher level, e.g., tag
	 * @param terms2 with lower level, e.g., annotation
	 * @param ratio = terms1.weight / terms2.weight
	 */
	private List<Term> mergeTerms(List<Term> terms1, List<Term> terms2, int ratio) {
		// separate the same and the different terms
		Set<Term> sames = new HashSet<Term>(terms1);
		Set<Term> diffs1 = new HashSet<Term>(terms1);
		Set<Term> diffs2 = new HashSet<Term>(terms2);
		sames.retainAll(terms2);
		diffs1.removeAll(sames);
		diffs2.removeAll(sames);
		List<Term> sameTerms = new ArrayList<Term>(sames);
		List<Term> diffTerms1 = new ArrayList<Term>(diffs1);
		List<Term> diffTerms2 = new ArrayList<Term>(diffs2);

		// recalculate raw based on ratio
		List<Term> t1 = updateTerms(sameTerms, ratio, terms1, terms2);
		List<Term> t2 = updateTerms(diffTerms2, ratio);
		t1.addAll(t2);
		t1.addAll(diffTerms1);
		
		return t1;
	}
	
	/**
	 * recalculate raw
	 * 
	 * @param terms
	 * @param ratio
	 */
	private List<Term> updateTerms(List<Term> terms, int ratio) {
		List<Term> ts = new ArrayList<Term> ();
		for (Iterator<Term> it = terms.iterator(); it.hasNext();) {
			Term term = it.next();
			int r = term.getRaw();

			r = (r >= 0) ? (int) Math.floor((double) r / ratio) : (int) Math.ceil((double) r / ratio);

			term.setRaw(r);
			
			if (r > 0)
				ts.add(term);
		}
		
		return ts;
	}
	
	private List<Term> updateTerms(List<Term> terms, int ratio, List<Term> terms1, List<Term> terms2) {
		List<Term> ts = new ArrayList<Term> ();
		for (Iterator<Term> it = terms.iterator(); it.hasNext();) {
			Term term = it.next();
			int i1 = terms1.indexOf(term), 
				i2 = terms2.indexOf(term);
			Term t1 = terms1.get(i1), 
				 t2 = terms2.get(i2);
			int r1 = t1.getRaw(), r2 = t2.getRaw();

			int r = (r2 >= 0) ? (int) Math.floor((double) r2 / ratio) : (int) Math.ceil((double) r2 / ratio);
			
			term.setRaw(r1 + r);
			
			if ((r1+r) > 0)
				ts.add(term);
		}
		
		return ts;
	}

	/**
	 * remove item if raw is negative
	 * 
	 * @param docTerms
	 */
	private List<Term> removeInvalidTerms(List<Term> terms) {
		for (Iterator<Term> it = terms.iterator(); it.hasNext();) {
			Term term = it.next();
			if (term.getRaw() <= 0)
				it.remove();
		}
		return terms;
	}

}
