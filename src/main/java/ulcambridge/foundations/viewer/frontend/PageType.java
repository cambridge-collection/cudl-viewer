package ulcambridge.foundations.viewer.frontend;

import org.springframework.util.Assert;

public enum PageType {
    STANDARD("page-standard"),
    DOCUMENT("page-document"),
    ADVANCED_SEARCH("page-advancedsearch"),
    TRANSCRIPTION("page-transcription"),
    LOGIN("page-login"),
    COLLECTION_ORGANISATION("page-collection-organisation"),
    MY_LIBRARY("page-my-library"),
    ADVANCED_SEARCH_RESULTS("page-advanced-search-results"),
    ERROR_500("page-error-500");

    private final String chunkName;

    PageType(String chunkName) {
        Assert.hasText(chunkName);
        this.chunkName = chunkName;
    }

    public String getChunkName() {
        return this.chunkName;
    }
}
