<%@tag description="HTML meta tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute name="name" required="false" type="java.lang.String" %>
<%@attribute name="httpEquiv" required="false" type="java.lang.String" %>
<%@attribute name="charset" required="false" type="java.lang.String" %>
<%@attribute name="itemprop" required="false" type="java.lang.String" %>
<%@attribute name="property" required="false" type="java.lang.String" %>
<%@attribute name="content" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:tag name="meta" selfClose="${true}">
	<jsp:attribute name="attributes">
		<cudl:attr name="name" value="${name}" skipEmpty="${true}"/>
		<cudl:attr name="http-equiv" value="${httpEquiv}" skipEmpty="${true}"/>
		<cudl:attr name="charset" value="${charset}" skipEmpty="${true}"/>
		<cudl:attr name="itemprop" value="${itemprop}" skipEmpty="${true}"/>
		<cudl:attr name="property" value="${property}" skipEmpty="${true}"/>
		<cudl:attr name="content" value="${content}" skipEmpty="${true}"/>
	</jsp:attribute>
</cudl:tag>
