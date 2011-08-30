/** 
 * Script to setup the document view page. 
 */
var viewer;    
    
function init() {

    viewer = new Seadragon.Viewer("center");
    updateCurrentPage();

}    
Ext.onReady(init);
    
