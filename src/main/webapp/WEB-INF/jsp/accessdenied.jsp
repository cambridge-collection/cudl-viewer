<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<cudl:generic-page>
	<cudl:nav activeMenuIndex="${2}" displaySearch="true" title="Access Denied"/>

	<%-- FIXME: move this script out to UI build. --%>
	<script>
    	function showOrHideOpenId() {
    		var div = document.getElementById('openid_input_area');
    		if (div.style.display = 'none') {
    			div.style.display = 'inline';
    		} else {
    			div.style.display = 'none';
    		}
    	}

    	function onSumbitMyOpenId() {
    		var username = document.forms["myopenid_form"]["openid_username"].value;
    		document.forms["myopenid_form"]["openid_identifier"].value = "http://"
    				+ username + ".myopenid.com/";
    		return true;
    	}
    </script>

    <div class="campl-row campl-content campl-recessed-content">
    	<div class="campl-wrap clearfix">
    		<div class="campl-column12  campl-main-content" id="content">
    			<div class="campl-content-container">
    				<h2>Access Denied</h2>
    				<div id="error">${error}</div>

    				<div class="grid_18">
    					You do not have permission to access the page requested. <br /> <br />
    				</div>
    			</div>
    		</div>
    	</div>
    </div>
</cudl:generic-page>
