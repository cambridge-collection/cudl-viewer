package ulcambridge.foundations.viewer.dao;

import ulcambridge.foundations.viewer.model.Item;


public interface ItemsDao {
    /**
     * Get the CUDL Item metadata for an item ID.
     *
     * @return The loaded Item; never null.
     * @throws org.springframework.dao.DataAccessException
     */
    Item getItem(String itemId);
}
