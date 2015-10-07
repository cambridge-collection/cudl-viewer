package ulcambridge.foundations.viewer.frontend;

import ulcambridge.foundations.frontend.FrontEndBuild;

/**
 * Created by hal on 06/10/15.
 */
public interface BuildFactory {
    public FrontEndBuild getBuild(PageType pageType);

}
