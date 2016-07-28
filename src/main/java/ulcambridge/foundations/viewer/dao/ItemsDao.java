package ulcambridge.foundations.viewer.dao;

import ulcambridge.foundations.viewer.model.Item;


public interface ItemsDao {

    public Item getItem(String itemId);
    public boolean getItemTaggingStatus(String itemId);

    //
    // this interface is shared by ItemsJSONDao and ItemsDBDao, each of which is
    // assigned with one method.
    //
    // - ItemsJSONDao uses JSONReader to get json content;
    // - ItemsDBDao uses dataSource to get item from database. Its purpose is to
    // check if the current document enables tagging feature.
    //
    //

}