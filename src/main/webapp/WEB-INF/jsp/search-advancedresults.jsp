<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page
	import="java.util.*,
	java.net.URLEncoder,ulcambridge.foundations.viewer.search.*,
	ulcambridge.foundations.viewer.model.Item,
	ulcambridge.foundations.viewer.ItemFactory,
	ulcambridge.foundations.viewer.forms.SearchForm,
	org.owasp.encoder.Encode,
	java.text.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Advanced Search" />
</jsp:include>

<!-- result css, query duration and sticky menu -->
<script type="text/javascript" src="/scripts/moment.min.js"></script>
<script src="/scripts/bootstrap-slider.min.js"></script>
<!-- <script type="text/javascript" src="/scripts/jquery.sticky.js"></script> -->
<link rel="stylesheet" href="/styles/advancedsearch.css">
<link rel="stylesheet" href="/styles/bootstrap-slider.min.css">

<%
	SearchResultSet resultSet = ((SearchResultSet) request.getAttribute("results"));
	SearchForm form = ((SearchForm) request.getAttribute("form"));
%>

<!--  script for ajax pagination -->
<script type="text/javascript">
$(function() {
    'use strict';

    function objAddKeyValuePair(obj, pair) {
        obj[pair[0]] = pair[1];
        return obj;
    }

    function unionKeys(objA, objB) {
        return Object.keys(
            Object.keys(objA).concat(Object.keys(objB))
                .reduce(function(unique, k) {
                    unique[k] = undefined;
                    return unique;
                }, {}));
    }

    function parseQuery(q) {
        return q.replace(/^\?/, '')
            .split('&')
            .map(function(s) {
                var i = s.indexOf('=');
                return i == -1 ? [_decodeURIComponent(s), '']
                               : [_decodeURIComponent(s.substr(0, i)),
                                  _decodeURIComponent(s.substr(i + 1))];
            })
            .reduce(objAddKeyValuePair, {});
    }

    /** As decodeURIComponent() but interprets + as space. */
    function _decodeURIComponent(s) {
        return decodeURIComponent(s.replace(/\+/g, ' '))
    }

    function serialiseQuery(params) {
        var keys = Object.keys(params);
        keys.sort();

        return serialiseQueryPairs(
                keys.map(function(k) { return [k, params[k]]; }));
    }

    function serialiseQueryPairs(keyValuePairs) {
        return '?' + keyValuePairs
            .map(function(kvp) {
                return encodeURIComponent(kvp[0]) + '=' + encodeURIComponent(kvp[1]);
            })
            .join('&');
    }

    /** Get the values that are not the same in a and b. */
    function diffState(a, b) {
        return unionKeys(a, b)
            .filter(function(key) {
                return a.hasOwnProperty(key) !== b.hasOwnProperty(key) ||
                    a[key] !== b[key];
            })
            .map(function(key) {
                var diff = {};

                if(a.hasOwnProperty(key))
                    diff.left = a[key];
                if(b.hasOwnProperty(key))
                    diff.right = b[key];

                return [key, diff];
            })
            .reduce(objAddKeyValuePair, {});
    }

    function getSearchQueryString(state) {

        var page = parseInt(state.page);

        // The search endpoint uses start and end rather than page.
        var queryParams = $.extend({}, state, {
            start: (page - 1) * pageLimit,
            end: page * pageLimit
        });
        delete queryParams.page;
        delete queryParams.tagging;

        return serialiseQuery(queryParams);
    }

    /**
     * Prevent breaks inside text at spaces by replacing spaces with
     * non-breaking spaces.
     */
    function noBreak(text) {
        return text.replace(/ /g, "\u00A0");
    }

    function renderResult(result) {
        var item = result.item;
        var imageStyle = {};
        if (item.thumbnailOrientation == "portrait") {
            imageStyle["height"] = "100%";
        } else if (item.thumbnailOrientation == "landscape") {
            imageStyle["width"] = "100%";
        }
        var title = item.title.join(", ");
        if (result.itemType == "essay") {
            title = "Essay: "
                    + title;
        }

        var itemDiv = $("<div>")
            .attr("class", "collections_carousel_item")
            .append(
                $("<div>")
                    .addClass("collections_carousel_image_box campl-column4")
                    .append(
                        $("<div>")
                            .addClass("collections_carousel_image")
                            .append(
                                $("<a>")
                                    .attr("href", "/view/" + encodeURIComponent(item.id) + "/" + encodeURIComponent(result.startPage))
                                    .append(
                                        $("<img>")
                                            .attr({
                                                src: result.pageThumbnailURL,
                                                alt: item.id
                                            }).css(imageStyle)
                                    )
                            )
                    ),
                $("<div>")
                    .addClass("collections_carousel_text campl-column8")
                    .append(
                        $("<h3>")
                            .append(
                                $("<a>")
                                    .attr("href", "/view/" + encodeURIComponent(item.id) + "/" + encodeURIComponent(result.startPage))
                                    .append(title),
                                $("<span>")
                                    .css({
                                        color: "#999",
                                        "font-weight": "normal",
                                        "font-size": "14px"
                                    })
                                    .append(
                                        " (",
                                        $("<span>")
                                            .attr("title", "Shelf locator")
                                            .text(item.shelfLocator.map(noBreak).join(", ")),
                                        String(item.shelfLocator) ? " " : "",
                                        "Page: ", document.createTextNode(result.startPageLabel), ")"
                                    )
                            ),
                        document.createTextNode(item.abstractShort),
                        $("<br><br>"),
                        $("<ul>")
                            .append(
                                result.snippets.filter(Boolean).map(function(snippet) {
                                    return $("<li>")
                                        .append(
                                            $("<span>").html(styleSnippet(snippet))
                                        )[0];
                                })
                            )
                    ),
                $("<div>").addClass("clear")
            );

        return itemDiv[0];
    }

    function renderResults(results) {
        return results.map(renderResult);
    }

    function pad(s, len, char) {
        return Array(Math.max(0, len - s.length) + 1).join(char) + s;
    }

    function formatNumber(n) {
        var negative = n < 0;
        n = Math.abs(n);
        var fraction = n - Math.floor(n);
        n = Math.floor(n);
        var result = [];

        while(true) {
            if(n < 1000) {
                result.unshift(n);
                break;
            }

            result.unshift(pad('' + (n - (Math.floor(n / 1000) * 1000)), 3, '0'));
            n = Math.floor(n / 1000)
        }

        return (negative ? '-' : '') + result.join(',') + ('' + fraction).substr(1);
    }
    window.formatNumber = formatNumber;

    function renderResultInfo(count, time) {
        return [
            document.createTextNode('About ' + formatNumber(count) + ' results ('),
            $('<span>')
                .attr('id', 'reqtime')
                .text(time / 1000 + ' seconds')[0],
            document.createTextNode(')')
        ];
    }

    function getFacetParam(facetField) {
        return 'facet' + facetField.substr(0, 1).toUpperCase()
                + facetField.substr(1);
    }

    function renderFacet(state, group, facet) {
        var facetState = $.extend({}, state);
        facetState[getFacetParam(group.field)] = facet.value;

        var url = serialiseQuery(facetState);

        return $('<li>')
            .append(
                $('<a>')
                    .attr('href', url)
                    .data('state', facetState)
                    .text(facet.value),
                document.createTextNode(' (' + facet.occurrences + ')')
            )[0];
    }

    function renderFacetTree(state, facets) {
        return $(facets.map(function(facetGroup) {
            return $('<li>')
                .append(
                    $('<strong>')
                        .append(
                            $('<span>').html('&#9662 '),
                            document.createTextNode(facetGroup.label)
                        ),
                    $('<ul>')
                        .addClass('campl-unstyled-list')
                        .append(
                            facetGroup.facets.map(renderFacet.bind(undefined, state, facetGroup))
                        )
                )[0];
        }));
    }

    function renderSelectedFacet(state, selectedFacet) {
        var facetState = $.extend({}, state);
        delete facetState[getFacetParam(selectedFacet.field)];

        var url = serialiseQuery(facetState);

        return $('<div>')
            .addClass('search-facet-selected')
            .append(
                $('<a>')
                    .addClass('search-close')
                    .attr('href', url)
                    .attr('title', 'Remove')
                    .data('state', facetState)
                    .append(
                        'in ',
                        $('<b>')
                            .append($('<span>').text(selectedFacet.value)[0]),
                        ' (', document.createTextNode(selectedFacet.field), ') ❌'
                    )
            )[0];
    }

    function renderSelectedFacets(state, selectedFacets) {
        return selectedFacets.map(renderSelectedFacet.bind(undefined, state));
    }

    function renderChangeQueryUrl(state) {

        var query = serialiseQueryPairs(
            Object.keys(state)
                .filter(function(k) {
                    return k !== 'page';
                })
                .map(function(k) { return [k, state[k]]; })
        );

        return './query' + query;
    }

    function loadPage(state) {
        setBusy(true);

        if(activeXhr)
            activeXhr.abort();

        var startTime = Date.now();

        var xhr;
        activeXhr = xhr = $.ajax({
            "url": '/search/JSON' + getSearchQueryString(state)
        })
        .always(function() {
            setBusy(false);
            if(activeXhr === xhr)
                activeXhr = null;
        })
        .done(function(data) {

            paging.setPage(parseInt(state.page));

            // query duration
            $("#reqtime").text((Date.now() - startTime) / 1000 + ' seconds');


            $('#collections_carousel')
                .empty()
                .append(renderResults(data));
        });

        return false;
    }

    function requery(state) {

        if(typeof state.page != 'number')
            throw new Error('state.page not a number');

        var startTime = Date.now();

        setBusy(true);

        if(activeXhr)
            activeXhr.abort();

        var xhr;
        activeXhr = xhr = $.ajax({
            "url": '/search/JSONAdvanced' + getSearchQueryString(state)
        })
        .always(function() {
            setBusy(false);
            if(activeXhr === xhr)
                activeXhr = null;
        })
        .done(function(data) {
            // Reset the pagination for the new data
            paging.setNumber(data.info.hits);
            paging.setPage(parseInt(state.page));

            // query duration
            var requestTime = Date.now() - startTime;
            $("#reqtime").text(requestTime / 1000 + ' seconds');

            $('#collections_carousel')
                .empty()
                .append(renderResults(data.items));

            $('.resultcount')
                .empty()
                .append(renderResultInfo(data.info.hits, requestTime));

            $('.searchexample').toggleClass('hidden', data.info.hits > 0);

            $('#tree')
                .empty()
                .append(renderFacetTree(state, data.facets.available));

            $('#selected_facets')
                .empty()
                .append(renderSelectedFacets(state, data.facets.selected))

            $('.query-actions .change-query')
                .attr('href', renderChangeQueryUrl(state));
        });
    }

    function getSpinner() {
        // Setup spinner.
        var opts = {
          lines: 13 // The number of lines to draw
        , length: 0 // The length of each line
        , width: 27 // The line thickness
        , radius: 63 // The radius of the inner circle
        , scale: 1 // Scales overall size of the spinner
        , corners: 1 // Corner roundness (0..1)
        , color: '#000' // #rgb or #rrggbb or array of colors
        , opacity: 0.1 // Opacity of the lines
        , rotate: 0 // The rotation offset
        , direction: 1 // 1: clockwise, -1: counterclockwise
        , speed: 1.5 // Rounds per second
        , trail: 44 // Afterglow percentage
        , fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
        , zIndex: 2e9 // The z-index (defaults to 2000000000)
        , className: 'spinner' // The CSS class to assign to the spinner
        , top: '50%' // Top position relative to parent
        , left: '50%' // Left position relative to parent
        , shadow: true // Whether to render a shadow
        , hwaccel: true // Whether to use hardware acceleration
        , position: 'fixed' // Element positioning
        }

        return new Spinner(opts);
    }

    function formatPagination(type) {
        switch (type) {

            case 'block':
                if (!this.active)
                    return '<span class="disabled">' + this.value + '</span>';
                else if (this.value != this.page)
                    return '<em><a href="">' + this.value + '</a></em>';
                return '<span class="current">' + this.value + '</span>';

            case 'right':

            case 'left':
                if (!this.active) {
                    return '';
                }
                return '<a href="">' + this.value + '</a>';

            case 'next':
                if (this.active)
                    return '<a href="" class="next"><img src="/img/interface/icon-fwd-btn-larger.png" class="pagination-fwd"/></a>';
                return '<span class="disabled"><img src="/img/interface/icon-fwd-btn-larger.png" class="pagination-fwd"/></span>';

            case 'prev':
                if (this.active)
                    return '<a href="" class="prev"><img src="/img/interface/icon-back-btn-larger.png" class="pagination-back"/></a>';
                return '<span class="disabled"><img src="/img/interface/icon-back-btn-larger.png" class="pagination-back"/></span>';

            case 'first':
                if (this.active)
                    return '<a href="" class="first">|<</a>';
                return '<span class="disabled">|<</span>';

            case 'last':
                if (this.active)
                    return '<a href="" class="last">>|</a>';
                return '<span class="disabled">>|</span>';

            case "leap":
                if (this.active)
                    return "...";
                return "";

            case 'fill':
                if (this.active)
                    return "...";
                return "";
        }
    }

    function setStatePage(page) {
        // This gets called by jQuery paging as the paginator is being created.
        // There's no need to do anything at this point.
        if(paging === undefined) {
            return;
        }
        requestState($.extend({}, currentState, {page: '' + page}));
    }

    /**
     * Update query state of the page to match the specified state.
     */
    function showState(state) {
        var change = diffState(currentState, state);

        if(Object.keys(change).length === 0) {
            return;
        }

        // Just page changed
        if(Object.keys(change).length === 1 && change.page) {
            loadPage(state);
        }
        // Facets/recallScale changed, perform new query
        else {
            requery(state);
        }
        currentState = state;
    }

    function requestState(state, mode) {
        mode = mode || 'push';

        if(!(mode === 'push' || mode === 'replace')) {
            throw new Error('Unknown mode: ' + mode);
        }

        // Don't add browser history for identical states
        if(Object.keys(diffState(currentState, state)).length === 0) {
            return;
        }

        var url = serialiseQuery(state);

        (mode === 'replace' ? history.replaceState : history.pushState)
            .call(history, state, '', url);
        showState(state);
    }

    function parseState(query, defaults) {
        defaults = defaults || {};
        var state = $.extend({page: 1}, defaults, parseQuery(query));
        state.page = parseInt(state.page);
        return state;
    }

    // The active ajax request for search results (if any)
    var activeXhr = undefined;

    var busyCount = 0;

    function setBusy(busy) {
        var prevCount = busyCount;
        busyCount = Math.max(0, busyCount + (busy ? 1 : -1));

        if(prevCount === 0 && busyCount) {
            var body = $('body');
            spinner.spin(body[0]);
            body.addClass('loading');
        }
        else if(prevCount && busyCount === 0) {
            var body = $('body');
            spinner.stop();
            body.removeClass('loading');
        }
    }

    var pageLimit = 20;
    var numResults = <%=resultSet.getNumberOfResults()%>;

    // Setup pagination
    var paging = $(".pagination").paging(numResults, {
        format : "< (q-) ncnnnnnn (-p) >",  //[< (q-) ncnnnnnn (-p) >]
        perpage : pageLimit,
        lapping : 0,
        page : 1,
        onSelect: setStatePage,
        onFormat : formatPagination
    });

    var spinner = getSpinner();
    window._spinner = spinner;
    var currentState = parseState(window.location.search);

    // The page is rendered w/out results, so we have to always fetch them
    // initially. Deleting the current page means it always changes initially.
    var initialPage = currentState.page;
    delete currentState.page;

    // Ensure we get a non-null state when returning to the first page. Also
    // load the first page of data.
    requestState($.extend({}, currentState, {page: initialPage}), 'replace');

    // Show the stored state when browser history is accessed.
    $(window).on('popstate', function(e) {
        var state = e.originalEvent.state;

        // Safari fires a popstate event on page load with null state
        if(state !== null) {
            showState(state);
        }
    });

    $("#recall-slider-input")
        .on("change", function(e) {
            var recallScale = e.value.newValue;
            requestState($.extend({}, currentState, {
                recallScale: recallScale,
                page: 1 // Reset page as it's a new query
            }));
        })

    // Handle facet activation and deactivation
    $('#tree,#selected_facets').on('click', 'a', function(e) {
        var state = $(e.currentTarget).data('state') ||
                parseState(e.currentTarget.search);

        requestState(state);
        return false;
    });
});
</script>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-main-content" id="content">
			<!-- <div> -->
				<div class="campl-column4 campl-secondary-content">

					<div class="searchform box">
					
						<div class="campl-content-container">
							<ul>
							    <c:if test="${fn:length(form.keyword) > 0}">
							        <li>
							            <span>Keyword: <b><c:out value="${form.keyword}"/></b></span>
							        </li>

							        <c:if test="${enableTagging}">
							               <div class="recall-slider">
                                               <input id="recall-slider-input" type="text" name="recallScale"
                                                       data-slider-value="${fn:escapeXml(form.recallScale)}"
                                                       data-slider-min="0"
                                                       data-slider-max="1"
                                                       data-slider-step="0.1"
                                                       data-slider-ticks="[0, 0.5, 1]"
                                                       data-slider-ticks-labels='["Curated<br>metadata", "Secondary<br>literature", "Crowd-<br>sourced"]'
                                                       data-slider-tooltip="hide">
                                               <input type="hidden" name="tagging" value="1">
                                           </div>
							        </c:if>
								</c:if>
								<%
									if (!form.getFullText().isEmpty()) {
										out.println("<li><span>Full Text: <b>" + Encode.forHtml(form.getFullText()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getExcludeText().isEmpty()) {
										out.println("<li><span>Exclude Text: <b>" + Encode.forHtml(form.getExcludeText()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getShelfLocator().isEmpty()) {
										out.println("<li><span>Classmark: <b>" + Encode.forHtml(form.getShelfLocator()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getFileID().isEmpty()) {
										out.println("<li><span>CUDL ID: <b>" + Encode.forHtml(form.getFileID()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getTitle().isEmpty()) {
										out.println("<li><span>Title: <b>" + Encode.forHtml(form.getTitle()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getAuthor().isEmpty()) {
										out.println("<li><span>Author: <b>" + Encode.forHtml(form.getAuthor()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getSubject().isEmpty()) {
										out.println("<li><span>Subject: <b>" + Encode.forHtml(form.getSubject()) + "</b></span></li>");
									}
								%>
								<%
									if (!form.getLocation().isEmpty()) {
										out.println("<li><span>Location: <b>" + Encode.forHtml(form.getLocation()) + "</b></span></li>");
									}
								%>
								<%
									if (form.getYearStart() != null && form.getYearEnd() != null) {
										out.println("<li><span>Year: <b>" + form.getYearStart() + "</b> to <b>" + form.getYearEnd() + "</b></span></li>");
									}
								%>
							</ul>

                            <div class="query-actions">
                                <a class="change-query campl-btn campl-primary-cta" href="/search/advanced/query?<%=SearchUtil.getURLParameters(form)%>">
                                    Change Query
                                </a>
                            </div>

                            <div id="selected_facets">
                                <%
                                    Iterator<String> facetsUsed = form.getFacets().keySet().iterator();
                                    while (facetsUsed.hasNext()) {
                                        String facetName = facetsUsed.next();
                                        String facetValue = form.getFacets().get(facetName);
                                %>
                                <div class="search-facet-selected">
                                    <%-- <form action="/search/advanced/results"> --%>
                                        <a class="search-close" href="?<%=SearchUtil.getURLParametersWithoutFacet(form, facetName)%>&amp;" title="Remove">
                                            in <b><% out.print("<span>" + Encode.forHtml(facetValue) + "</span>"); %></b>
                                            <% out.println(" (" + Encode.forHtml(facetName) + ")"); %> ❌
                                        </a>
                                    <%-- </form> --%>
                                </div>
                                <%
                                    }
                                %>
                            </div>
							<%
								if (resultSet.getSpellingSuggestedTerm() != null && !resultSet.getSpellingSuggestedTerm().equals("")) {
									out.println("Did you mean <a href=\"/search?keyword=" + resultSet.getSpellingSuggestedTerm() + "\">" + resultSet.getSpellingSuggestedTerm() + "</a> ?");
								}
								out.println("<div class=\"_mib\"></div>");
								out.println("<div class=\"campl-column12 resultcount\"> About " +
										NumberFormat.getNumberInstance(Locale.US).format(resultSet.getNumberOfResults()) +
										" results (<span id=\"reqtime\"></span></span>)</div>");
							%>
						</div>
						
						<%
							if (resultSet.getNumberOfResults() > 0) {
						%>
						<div class="campl-content-container">
							<h5>Refine by:</h5>

							<ol id="tree" class="campl-unstyled-list">
							<%
								List<FacetGroup> facetGroups = resultSet.getFacets();

								if (facetGroups != null) {
									for (int i = 0; i < facetGroups.size(); i++) {
										FacetGroup facetGroup = (FacetGroup) facetGroups.get(i);
										String fieldLabel = facetGroup.getFieldLabel();
										String field = facetGroup.getField();
										List<Facet> facets = facetGroup.getFacets();

										// Do not print out the facet for a field already faceting on
										if (!form.getFacets().containsKey(field)) {
											out.println("<li><strong><span>&#9662; </span>" + fieldLabel + "</strong><ul class='campl-unstyled-list'>");

											for (int j = 0; j < facets.size(); j++) {
												Facet facet = facets.get(j);
												
												out.print("<li><a href='?" + SearchUtil.getURLParametersWithExtraFacet(form, Encode.forHtmlAttribute(field), Encode.forHtmlAttribute(facet.getBand())) + "'>");
												out.print(Encode.forHtml(facet.getBand()) + "</a> (" + facet.getOccurences() + ")</li>");
											}
											out.println("</ul></li>");
										}
									}
								}
							%>
							</ol>
						</div>
						<%
							}
						%>
						
					</div>
				</div>

				<!-- <div class="campl-column8" id="pagination_container"> -->
				<div class="campl-column8 camp-content">

					<%
						List<SearchResult> results = resultSet.getResults();
                        boolean gotResults = resultSet.getNumberOfResults() > 0;

						out.println(
						    "<div class=\"searchexample campl-content-container" +
						     (gotResults ? " hidden" : "") +
						     "\">");

                        out.println("<p class=\"box\">We couldn't find any items your query.</p>");

                        out.println("<h5>Example Searches</h5><br/><p>");
                        out.println("Searching for <span class=\"search\">newton</span> Searches the metadata for 'newton'<br/>");
                        out.println("Searching for <span class=\"search\">isaac newton</span> Searches for 'isaac' AND 'newton'<br/>");
                        out.println("Searching for <span class=\"search\">\"isaac newton\"</span> Searches for the phrase 'isaac newton'<br/>");
                        out.println("The characters <b>?</b> and <b>*</b> can be used as wildcards in your search.<br />");
                        out.println("Use <b>?</b> to represent one unknown character and <b>*</b> to represent any number of unknown characters.<br/>");
                        out.println("</p></div>");
					%>

					<!-- <div class="pagination toppagination"></div> -->
					<!-- start of list -->
					<div id="collections_carousel" class="collections_carousel">
					</div>
					<!-- end of list -->
					<div class="pagination toppagination"></div>
					
				</div>

			<!-- </div> -->
		</div>
	</div>
</div>

<script type="text/javascript">
    $(function() {
        $('#recall-slider-input').slider();
    });

	// collapsible list
	$("#tree > li > strong").on("click", function(e) {
		if($(this).parent().find("span").html().match("^\u25be")) $(this).parent().find("span").html("\u25b8 ")
		else $(this).parent().find("span").html("\u25be ")
		$(this).parent().find("ul").toggleClass("hide");
	});
	
	//style snippet
	function styleSnippet(s) {
		s = s.replace(/<b>/g, "<span class=\"campl-search-term\">");
		s = s.replace(/<\/b>/g, "</span>");
		return s;
	}
</script>

<jsp:include page="header/footer-full.jsp" />
