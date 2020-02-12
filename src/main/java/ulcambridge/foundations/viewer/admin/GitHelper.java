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
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: don't catch exceptions here - let the caller handle them instead of using C-style return values.
 * TODO: don't mutate state in every method - either have final fields or local variables
 */
public class GitHelper {

    private String localPathMasters;
    private Repository localRepomasters;
    private Git gitmasters;
    private String url;
    private boolean success = false;
    private static final Logger LOG = LoggerFactory.getLogger(GitHelper.class);

    public GitHelper(String localPathMasters, String url) {
        this.localPathMasters = localPathMasters;
        this.url = url;
    }

    protected String getLastRevision() {
        try {
            localRepomasters = new FileRepository(localPathMasters + "/.git");

            if(!localRepomasters.getObjectDatabase().exists()) {
                LOG.debug("No revision available: Git repo does not exist at {}", localRepomasters.getDirectory());
                return null;
            }

            gitmasters = new Git(localRepomasters);
            Iterator<RevCommit> iter = gitmasters.log().call().iterator();
            if (iter.hasNext()) {
                RevCommit commit = iter.next();
                return ObjectId.toString(commit.getId());
            }
        } catch (IOException | GitAPIException ex) {
            LOG.error("Error in getLastRevision", ex);
        }
        return null;
    }

    /**
     * Pushes changes to remote repository using the specified refSpec.
     */
    protected boolean push(String username, String password, String refspec) {
        CredentialsProvider creds = new UsernamePasswordCredentialsProvider(username, password);

        try {
            // master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.push()
                .setRefSpecs(new RefSpec(refspec))
                .setRemote(url)
                .setCredentialsProvider(creds)
                .call();
            success = true;
        } catch (IOException | GitAPIException ex) {
            LOG.error("Error in push", ex);
            success = false;
        }
        return success;
    }

    /**
     * Commits *all* changed files and adds any new files to local repository. A
     * push is required to push these changes to BitBucket.
     */
    protected boolean commit(String name, String email, String message) {

        try {
            // master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.add().addFilepattern(".").call();
            gitmasters.commit()
                .setCommitter(new PersonIdent(name, email))
                .setMessage(message)
                .call();
            success = true;
        } catch (IOException | GitAPIException ex) {
            LOG.error("Error in commit", ex);
            success = false;
        }

        return success;
    }

    /**
     * Deletes file from git local repository and commits the change A push is
     * required to push these changes to BitBucket.
     *
     * @param filePath absolute path, MUST start with admin.git.content.localpath
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
            gitmasters.commit()
                .setCommitter(new PersonIdent(name, email))
                .setMessage(message)
                .call();
            success = true;
        } catch (IOException | GitAPIException ex) {
            LOG.error("Error in delete", ex);
            success = false;
        }

        return success;
    }

}
