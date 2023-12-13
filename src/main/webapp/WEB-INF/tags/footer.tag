<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="date" class="java.util.Date" />

<!-- default footer -->
<div class="global-footer">
  <div class="section container mt-5">
    <div class="region region-global-footer row">
            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/footer.html"/>
    </div>
  </div>
</div>
