Ext.onReady(function() {
    Ext.QuickTips.init();
    var cmp1 = new MyViewport({
        renderTo: Ext.getBody()
    });
    cmp1.show();
});
