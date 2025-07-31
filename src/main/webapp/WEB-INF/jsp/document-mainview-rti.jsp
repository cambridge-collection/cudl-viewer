<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<c:set var="title" value="RTI View ${item.title}"/>

<html><head>
    <title>${title}</title>
    <link rel="stylesheet" href="/document-views/rti/skin.css"/>
    <style>
        html, body { margin:0; padding:0; height:100%; }
        #openlime { position:relative; height:100% }
        #openlime > canvas { width:100%; height:100%; overflow:hidden; }
        .openlime-overlaymsg { background:black; color:#fff; opacity:80%; animation:fadeOut 3s; }
        .openlime-dialog.hidden { pointer-events:none; opacity:0; }
        .openlime-dialog.visible { pointer-events:visible; opacity:1; }
        .campl-skipTo { visibility: hidden; }
    </style>
    <script src="/document-views/rti/openlime.min.js"></script>
    <c:if test="${empty pageData}">
        <c:set var="pageData">
            <cudl:default-context>
                <json:property name="docId" value="${docId}"/>
                <json:property name="pageNum" value="${page}"/>
                <json:property name="pageJSON" value="${pageJSON}"/>
                <json:property name="rtiURL" value="${rtiURL}"/>
                <json:property name="displayImageRights" value="${displayImageRights}"/>
                <json:property name="rtiImageServer" value="${rtiImageServer}"/>
                <json:property name="iiifImageServer" value="${iiifImageServer}"/>
            </cudl:default-context>
        </c:set>
    </c:if>
</head>
<body>
    <div id="openlime"></div>
    <div id="info">
        <h3>RTI Viewer Instructions</h3>
        <p>This view allows you to interactively explore the surface by moving the light source. Click and drag on the image to change the lighting direction and reveal surface details.</p>
        <p>Clicking the light bulb icon <img height="30px" src="/document-views/rti/rti-light-bulb.png"/> in the toolbar below the image to disable light mode. Once disabled, you can pan and move the image as normal.</p>
        <p>Clicking the layers icon <img height="30px" src="/document-views/rti/rti-layers.png"/> in the toolbar below the image to view a list of different image layers to visualise.</p>
    </div>

    <script src="/document-views/rti/openlime.min.js"></script>
    <script>
        async function init() {
            let layout = "tarzoom";
            const lime = new OpenLIME.Viewer('#openlime', { background: 'black' });

            const rtiUrl = "${rtiURL}";

            const layer = new OpenLIME.Layer({
                layout: layout,
                type: 'rti',
                url: rtiUrl,
                normals: false
            });
            lime.canvas.addLayer('RTI', layer);
            OpenLIME.Skin.setUrl('/document-views/rti/skin.svg');

            const ui = new OpenLIME.UIBasic(lime, {
                showLightDirections: true,
                attribution: "${displayImageRights}",
                skin: '/document-views/rti/skin.svg'
            });

            ui.actions.light.active = true;
            ui.actions.zoomin.display = true;
            ui.actions.zoomout.display = true;
            ui.actions.rotate.display = true;
            ui.actions.layers.display = true;
            ui.showLightDirections = true;
            ui.showOverlayMessage('loading ...', 2000);

            let openlime = document.querySelector('#openlime');
            let infoDialog = new OpenLIME.UIDialog(openlime, { modal: false });
            infoDialog.setContent(document.getElementById('info'));
            infoDialog.hide();

            let customInfo = {
                title: 'Show Help Information',
                display: true,
                icon: `<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg
   width="19.314583mm"
   height="19.314583mm"
   viewBox="0 0 19.314583 19.314583"
   version="1.1"
   id="svg3279"
   xmlns="http://www.w3.org/2000/svg"
   xmlns:svg="http://www.w3.org/2000/svg">
  <defs
     id="defs3276" />
  <g
     id="g406"
     transform="translate(-0.68252549,0.36637701)">
    <rect
       ry="3.175"
       rx="3.175"
       y="-0.10179351"
       x="0.94710898"
       height="18.785416"
       width="18.785416"
       id="rect2993-9-3"
       style="fill:#ffffff;fill-opacity:1;fill-rule:evenodd;stroke:#666666;stroke-width:0.529167;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" />
    <g
       aria-label="i"
       transform="scale(1.1476023,0.87138203)"
       id="text348"
       style="font-size:25.9103px;line-height:1.25;font-family:'Palace Script MT';font-variant-ligatures:none;letter-spacing:0px;word-spacing:0px;stroke-width:0.303636">
      <path
         d="M 12.106211,18.992435 C 11.225261,18.888794 10.888427,18.50014 10.888427,17.515548 V 7.6696346 H 5.9136497 v 0.6218472 c 1.1400532,0.2072824 1.3473356,0.4404751 1.3732459,1.5287077 v 7.6176275 c -0.02591,1.088233 -0.2072824,1.295515 -1.3732459,1.554618 v 0.621848 H 12.106211 Z M 9.0747063,1.7102658 c -1.1141429,0 -2.0210034,0.9068605 -2.0210034,1.995093 0,1.1659635 0.8550399,2.0210034 1.9950931,2.0210034 1.140053,0 2.021003,-0.8550399 2.021003,-1.9950931 0,-1.1400531 -0.88095,-2.0210033 -1.9950927,-2.0210033 z"
         style="font-weight:bold;font-family:'Nimbus Roman';"
         id="path400" />
    </g>
  </g>
</svg>`,
                task: () => { infoDialog.toggle(); }
            };

            ui.actions = { customInfo, home: ui.actions.home, light: ui.actions.light, zoomin: ui.actions.zoomin,
                zoomout: ui.actions.zoomout, layers: ui.actions.layers, rotate: ui.actions.rotate,
                fullscreen: ui.actions.fullscreen };

            lime.camera.maxFixedZoom = 1;
            window.lime = lime;
        }
        init();
    </script>
</body>
</html>

