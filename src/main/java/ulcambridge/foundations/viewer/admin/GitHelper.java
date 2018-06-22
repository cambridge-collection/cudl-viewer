/*
 * copies json from master to branch in git- so puppet can pick up the json from branch for the live cudl website
 */
package ulcambridge.foundations.viewer.admin;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 *
 * @author rekha
 */
public class GitHelper {

    private String localPathMasters;
    private Repository localRepomasters;
    private Git gitmasters;
    private String url;
    private boolean success = false;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(GitHelper.class.getName());

    public GitHelper(String localPathMasters, String url) {
        this.localPathMasters = localPathMasters;
        this.url = url;
    }

    protected String getLastRevision() {
        try {
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            Iterator<RevCommit> iter = gitmasters.log().call().iterator();
            if (iter.hasNext()) {
                RevCommit commit = iter.next();
                return ObjectId.toString(commit.getId());
            }
        } catch (IOException | GitAPIException ex) {
            logger.warn("Error in getLastRevision", ex);
        }
        return null;
    }

    /**
     * Pushes changes to remote repository using the specified refSpec.
     *
     * @param refspec
     * @return
     */
    protected boolean push(String username, String password, String refspec) {

        try {
            // master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            // refspec = master:branch
            gitmasters
                    .push()
                    .setRefSpecs(new RefSpec(refspec))
                    .setRemote(url)
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(username,
                                    password)).call();
            success = true;
        } catch (IOException | GitAPIException ex) {
            logger.error("Error in push", ex);
            success = false;
        }
        return success;
    }

    /**
     * Commits *all* changed files and adds any new files to local repository. A
     * push is required to push these changes to BitBucket.
     *
     * @return
     */
    protected boolean commit(String name, String email, String message) {

        try {
            // master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.add().addFilepattern(".").call();
            gitmasters.commit().setCommitter(new PersonIdent(name, email))
                    .setMessage(message).call();

            success = true;
        } catch (IOException | GitAPIException ex) {
            logger.error("Error in commit", ex);
            success = false;
        }

        return success;
    }

    /**
     * Deletes file from git local repository and commits the change A push is
     * required to push these changes to BitBucket.
     *
     * @param filePath absolute path, MUST start with admin.git.content.localpath
     * @return
     */
    protected boolean delete(String filePath, String name, String email,
            String message) {

        try {
            // convert from full path to relative path to git
            filePath = filePath.replace(localPathMasters, "");

            // master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.rm().addFilepattern(filePath).call();
            gitmasters.commit().setCommitter(new PersonIdent(name, email))
                    .setMessage(message).call();
            success = true;
        } catch (IOException | GitAPIException ex) {
            logger.error("Error in delete", ex);
            success = false;
        }

        return success;
    }

}
