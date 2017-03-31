package ulcambridge.foundations.viewer.search

import spock.lang.Specification
import ulcambridge.foundations.viewer.forms.SearchForm

class SearchUtilSpec extends Specification {

    def getSampleForm() {
        def form = new SearchForm()
        form.setKeyword("something")
        form.setFacetSubject("test subject")
        form.setFacetDate("test date")
        return form
    }

    def "params contain expected values"() {
        given:
        def form = sampleForm

        when:
        def query = SearchUtil.getURLParameters(form)

        then:
        query =~ regex

        where:
        regex << [
            /\bkeyword=something\b/,
            /\bfacetSubject=test%20subject\b/,
            /\bfacetDate=test%20date\b/
        ]
    }

    def "adding a facet retains existing facets"() {
        given:
        def form = sampleForm

        when:
        def query = SearchUtil.getURLParametersWithExtraFacet(
            form, "bob", "bobvalue")

        then:
        query =~ regex

        where:
        regex << [
            /\bkeyword=something\b/,
            /\bfacetSubject=test%20subject\b/,
            /\bfacetDate=test%20date\b/,
            /\bfacetBob=bobvalue\b/,
        ]
    }

    def "removing a retains other facets"() {
        given:
        def form = sampleForm

        when:
        def query = SearchUtil.getURLParametersWithoutFacet(form, "subject")

        then:
        query =~ /\bkeyword=something\b/
        query =~ /\bfacetDate=test%20date\b/
        query !=~ /\bfacetSubject=test%20subject\b/
    }
}
