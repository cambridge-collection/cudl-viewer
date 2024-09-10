<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="ADVANCED_SEARCH" title="${collection.title}">
    <cudl:nav activeMenuIndex="${2}" displaySearch="true" title="Search"/>

    <div id="main_content" class="container">
        <div class="row">
            <div class="container" id="content">
                <div class="advancedsearchform box">
                    <h2>Search</h2>
                    <form:form modelAttribute="searchForm" method="GET" action="/search">
                    <div id="search" class="accordion col-md-6 accordion-flush">
                        <div class="accordion-item">
                            <div id="general" class="accordion-collapse collapse show">
                                <div class="accordion-body px-0">
                                    <div id="basic-search" class="advancedsearch-section">
                                        <div class="row">
                                            <div class="col-md-12">
                                    <span class="hint--right" data-hint="Search keywords in metadata or transcriptions">
                                        <form:input path="keyword" type="text" value="" name="keyword"/>
                                    </span>
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
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion-item">
                            <h2 class="accordion-header mt-0" id="advanced-header">
                                <button class="accordion-button collapsed px-0 pb-0 h3" type="button" data-bs-toggle="collapse" data-bs-target="#advanced-search" aria-expanded="false" aria-controls="collapseOne">Advanced options</button>
                            </h2>
                            <div id="advanced-search" class="accordion-collapse collapse ${form.hasAdvancedParams() ? 'show' : ''}" aria-labelledby="advanced-header" >
                                <div class="accordion-body px-0">
                                    <div class="advancedsearch-section">
                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="fullText">Full Text</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search transcription/translation data">
                                        <form:input path="fullText" type="text" value="" name="textual_content"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                <span class="hint--right" data-hint="Applies to full text search">
                                    <div class="col-md-3 text-end" style="display:table-cell">
                                        <form:radiobutton path="textJoin" class="radiobutton" value="and" name="textJoin" id="textJoin1"/>
                                        <form:label path="textJoin" class="_rbl" for="textJoin1"> All of these words </form:label>
                                    </div>
                                    <div class="col-md-3 text-end" style="display:table-cell">
                                        <form:radiobutton path="textJoin" class="radiobutton" value="or" name="textJoin" id="textJoin2"/>
                                        <form:label path="textJoin" class="_rbl" for="textJoin2"> Any of these words </form:label>
                                    </div>
                                </span>
                                        </div>

                                            <%--<div class="row">
                                                <div class="col-md-2 ps-4 text-end">
                                                    <form:label path="excludeText"> excluding </form:label>
                                                </div>
                                                <div class="col-md-10">
                                                    <span class="hint--right" data-hint="Exclude transcription results that mention these words" style="display:table;">
                                                            <form:input path="excludeText" type="text" value="" name="excludeText" />
                                                    </span>
                                                </div>
                                            </div>--%>
                                    </div>

                                    <div class="_mib"></div>

                                    <div class="advancedsearch-section clearfix">
                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="FacetCollection">Collection</form:label>
                                            </div>
                                            <div class="col-md-10">
                                                <form:select path="FacetCollection">
                                                    <form:option value="" label="--- Select ---"/>
                                                    <form:options items="${form.collections}" itemValue="title" itemLabel="title" multiple="false"/>
                                                </form:select>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="shelfLocator">Classmark</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="e.g. MS Add.3996">
                                        <form:input path="shelfLocator" type="text" size="35" value="" name="shelfLocator"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="title">Title</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for titles that include these words, e.g. Letter">
                                        <form:input path="title" type="text" size="35" value="" name="title"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="author">Author</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items by this person, e.g. Darwin">
                                        <form:input path="author" type="text" size="35" value="" name="name"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="subject">Subject</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items about this subject, e.g. Mathematics">
                                        <form:input path="subject" type="text" size="35" value="" name="subject"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="language">Language</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items in a language, e.g. Latin">
                                        <form:input path="language" type="text" size="35" value="" name="languages"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="place">Place</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items from (or associated with) a place, e.g. London">
                                        <form:input path="place" type="text" size="35" value="" name="origin-place"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="location">Location</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items in a physical location, e.g. University Library">
                                        <form:input path="location" type="text" size="35" value="" name="physicalLocation"/>
                                    </span>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-2">
                                                <form:label class="right" path="yearStart">Year</form:label>
                                            </div>
                                            <div class="col-md-10">
                                    <span class="hint--right" data-hint="Search for items created in a year (or range of years) e.g. 1200" style="display:table;">
                                        <div style="display:table-cell;">
                                            <form:input path="yearStart" type="text" value="" name="yearStart"/>
                                        </div>
                                        <div style="display:table-cell;padding-right:6px;padding-left:6px;text-align:center;width:40px;">
                                            <form:label path="yearEnd"> to </form:label>
                                        </div>
                                        <div style="display:table-cell;">
                                            <form:input path="yearEnd" type="text" value="" placeholder="optional" name="yearEnd"/>
                                        </div>
                                    </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="search-buttons" class="advancedsearch-section col-md-6 clearfix text-end mt-4 px-0">
                        <button type="reset" class="campl-btn">Reset</button>
                        <button type="submit" class="campl-btn campl-primary-cta">Search</button>
                    </div>
                </div>
                </form:form>

                <div style="color: #999">
                    <br/>The characters <b>?</b> and <b>*</b> can be used as
                    wildcards in your search.<br/> Use <b>?</b> to represent one
                    unknown character and <b>*</b> to represent any number of unknown
                    characters.
                </div>
            </div>
        </div>
    </div>
    </div>
</cudl:generic-page>
