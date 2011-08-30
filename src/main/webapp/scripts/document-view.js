/** 
 * Script to setup the document view page. 
 */
var viewer;    
    
function init() {

	Seadragon.Config.imagePath = "/img/";
    viewer = new Seadragon.Viewer("center");
    
    updateCurrentPage();

}    
Ext.onReady(init);
    
