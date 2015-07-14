/*
 * copies json from master to branch in git- so puppet can pick up the json from branch for the live cudl website
 */
package ulcambridge.foundations.viewer.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 *
 * @author rekha
 */
public class GitJsonCopy {

    private final static String localPathMasters = "/tmp/jgitTest/cudl-data/";
    private final static String localPathBranch = "/tmp/jgitTest/Branch/cudl-data/";
    private Repository localRepomasters, localRepobranch;
    private static Git gitmasters, gitbranch;
    private final String username = "rr494";
    private final String password = "";
    private final static String url = "https://bitbucket.org/CUDL/cudl-data.git";
    Boolean success = false;

    protected Boolean merge() {

        try {
            //master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            //master:branch
            gitmasters.push().setRefSpecs(new RefSpec("cudl-data-m:cudl-data-golive")).setRemote(url).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
            success = true;
        } catch (IOException ex) {
            Logger.getLogger(GitJsonCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (RefAlreadyExistsException ex) {
            Logger.getLogger(GitJsonCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (CheckoutConflictException ex) {
            Logger.getLogger(GitJsonCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (GitAPIException ex) {
            Logger.getLogger(GitJsonCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

}
