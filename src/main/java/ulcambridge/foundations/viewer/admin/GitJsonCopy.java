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
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class GitJsonCopy {

    private final static String localPathMasters = Properties.getString("git.localpath");
    private Repository localRepomasters;
    private static Git gitmasters;
    private final String username = Properties.getString("git.username");
    private final String password = Properties.getString("git.password");
    private final static String url = Properties.getString("git.url");
    private final static String refspec = Properties.getString("git.refspec");
    Boolean success = false;

    protected Boolean merge() {

        try {
            //master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            //refspec = master:branch
            gitmasters.push().setRefSpecs(new RefSpec(refspec)).setRemote(url).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
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
