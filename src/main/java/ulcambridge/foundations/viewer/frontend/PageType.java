package ulcambridge.foundations.viewer.frontend;

import org.springframework.util.Assert;

public enum PageType {
    STANDARD("page-standard"),
    DOCUMENT("page-document"),
    ADVANCED_SEARCH("page-advancedsearch"),
    TRANSCRIPTION("page-transcription"),
    COLLECTION_ORGANISATION("page-collection-organisation"),
    ADVANCED_SEARCH_RESULTS("page-advanced-search-results"),
    ERROR_500("page-error-500"),
    FEEDBACK("page-feedback");

    private final String chunkName;

    PageType(String chunkName) {
        Assert.hasText(chunkName);
        this.chunkName = chunkName;
    }

    public String getChunkName() {
        return this.chunkName;
    }
}
