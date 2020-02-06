package ulcambridge.foundations.viewer.dao;

public interface ItemStatusOracle {
    boolean isIIIFEnabled(String itemId);
    boolean isTaggingEnabled(String itemId);
}
