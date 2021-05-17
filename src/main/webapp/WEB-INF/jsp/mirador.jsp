<%@page autoFlush="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
    <title>Simple Viewer</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        #viewer {
            width: 100%;
            height: 100%;
            position: fixed;
        }
    </style>

    <script src="/mirador-ui/mirador.min.js"></script>
</head>
<body>

<div id="viewer"></div>

<script type="text/javascript">

    const manifestId = '<c:out value="${baseURL}"/>/iiif/<c:out value="${id}"/>';

    let miradorInstance = Mirador.viewer(${miradorUtils.getMiradorConfig()});

    const windows = miradorInstance.store.getState().windows;
    const windowId = Object.values(windows)
            .find(w => w.manifestId === manifestId).id;

    // Add the right hand annotations panels.
    <c:forEach var = "i" begin = "1" end = "${miradorUtils.getCompanionWindowsProperty()}">
        miradorInstance.store.dispatch(miradorInstance.actions.addCompanionWindow(windowId,
            ${miradorUtils.getCompanionWindows(i)}));
    </c:forEach>

</script>
</body>
</html>
