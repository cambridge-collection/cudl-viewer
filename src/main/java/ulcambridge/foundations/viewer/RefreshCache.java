/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author rekha
 */
public class RefreshCache {

    private CollectionFactory collectionFactory;
    private ItemFactory itemFactory;

    @Autowired
    public void setCollectionFactory(CollectionFactory factory) {
        this.collectionFactory = factory;
    }

    @Autowired
    public void setItemFactory(ItemFactory factory) {
        this.itemFactory = factory;
    }

    @Scheduled(fixedRate = 5000)
    public void refresh() {
        this.collectionFactory.init();
        ItemFactory itemfactory = new ItemFactory();
        itemfactory.clearItemCache();
    }
}
