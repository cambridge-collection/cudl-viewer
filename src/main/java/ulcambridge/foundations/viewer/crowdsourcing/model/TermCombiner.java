package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * 
 * @author Lei
 * 
 */
public class TermCombiner {

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
		removeNegative(docRTTerms.getTerms());

		return docRTTerms;
	}

	public DocumentTerms combine_Anno_Tag(DocumentAnnotations docAnnotations, DocumentTags docTags) {

		int ratio_A_T = this.tagWeight / this.annoWeight;

		DocumentTerms docATTerms = combine(docAnnotations, docTags, ratio_A_T);
		removeNegative(docATTerms.getTerms());

		return docATTerms;
	}

	public DocumentTerms combine_Anno_Tag_RemovedTag(DocumentAnnotations docAnnotations, DocumentTags docTags, DocumentTags docRemovedTags) {

		int ratio_A_R = this.annoWeight / this.removedTagWeight;
		int ratio_A_T = this.tagWeight / this.annoWeight;

		DocumentTerms docARTerms = combine(docAnnotations, docRemovedTags, ratio_A_R);
		DocumentTerms docARTTerms = combine(docTags, docARTerms, ratio_A_T);
		removeNegative(docARTTerms.getTerms());

		return docARTTerms;
	}

	public DocumentTerms combine_Anno_Meta(DocumentAnnotations docAnnotations) {

		int ratio_A_M = this.metaWeight / this.annoWeight;

		DocumentTerms docAMTerms = combine(docAnnotations, ratio_A_M);
		removeNegative(docAMTerms.getTerms());

		return docAMTerms;
	}

	public DocumentTerms combine_Tag_RemovedTag_Meta(DocumentTags docTags, DocumentTags docRemovedTags) {
		// combine tags with removed tags
		DocumentTerms docRTTerms = combine_Tag_RemovedTag(docTags, docRemovedTags);

		int ratio_T_M = this.metaWeight / this.tagWeight;

		DocumentTerms docRTMTerms = combine(docRTTerms, ratio_T_M);
		removeNegative(docRTMTerms.getTerms());

		return docRTMTerms;
	}

	public DocumentTerms combine_Anno_Tag_RemovedTag_Meta(DocumentAnnotations docAnnotations, DocumentTags docTags, DocumentTags docRemovedTags) {
		// combine annotations, tags and removed tags
		DocumentTerms docARTTerms = combine_Anno_Tag_RemovedTag(docAnnotations, docTags, docRemovedTags);

		int ratio_T_M = this.metaWeight / this.tagWeight;

		DocumentTerms docARTMTerms = combine(docARTTerms, ratio_T_M);
		removeNegative(docARTMTerms.getTerms());

		return docARTMTerms;
	}

	private DocumentTerms combine(DocumentTerms docTerms1, DocumentTerms docTerms2, int ratio) {
		List<Term> terms1 = docTerms1.getTerms();
		List<Term> terms2 = docTerms2.getTerms();

		List<Term> combined = merge(terms1, terms2, ratio);

		return new DocumentTerms(docTerms1.getDocumentId(), combined.size(), combined);
	}

	private DocumentTerms combine(DocumentTerms docTerms, int ratio) {
		List<Term> terms = docTerms.getTerms();

		levelUp(terms, ratio);

		return docTerms;
	}

	/**
	 * merge two term lists, recalculate raw if duplicate exists
	 * 
	 * @param terms1
	 * @param terms2
	 * @param ratio = terms1.weight / terms2.weight
	 */
	private List<Term> merge(List<Term> terms1, List<Term> terms2, int ratio) {
		// get similar and different terms
		Set<Term> similar = new HashSet<Term>(terms1);
		Set<Term> different = new HashSet<Term>();
		different.addAll(terms1);
		different.addAll(terms2);

		similar.retainAll(terms2);
		different.removeAll(similar);

		List<Term> _similar = new ArrayList<Term>(similar);
		List<Term> _different = new ArrayList<Term>(different);

		// calculate new raw
		for (Term term : _similar) {
			int i1 = terms1.indexOf(term), i2 = terms2.indexOf(term);
			Term t1 = terms1.get(i1), t2 = terms2.get(i2);
			int r1 = t1.getRaw(), r2 = t2.getRaw();

			int r = (r2 >= 0) ? (int) Math.floor((double) r2 / ratio) : (int) Math.ceil((double) r2 / ratio);

			term.setRaw(r1 + r);
			//_similar.set(i, term);
		}

		_different.addAll(_similar);

		return _different;
	}

	/**
	 * recalculate raw for each item
	 * 
	 * @param terms
	 * @param ratio
	 */
	private void levelUp(List<Term> terms, int ratio) {
		for (Term term : terms) {
			int r = term.getRaw();

			r = (r >= 0) ? (int) Math.floor((double) r / ratio) : (int) Math.ceil((double) r / ratio);

			term.setRaw(r);
		}
	}

	/**
	 * remove item if raw is negative
	 * 
	 * @param docTerms
	 */
	private void removeNegative(List<Term> terms) {
		List<Term> tmp = new ArrayList<Term>();
		for (Term term : terms) {
			if (term.getRaw() <= 0) {
				tmp.add(term);
			}
		}
		terms.removeAll(tmp);
	}

}
