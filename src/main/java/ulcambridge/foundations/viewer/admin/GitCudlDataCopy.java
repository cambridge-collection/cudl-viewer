/*
 * copies json from master to branch in git- so puppet can pick up the json from branch for the live cudl website
 */
package ulcambridge.foundations.viewer.admin;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 *
 * @author rekha
 */
public class GitCudlDataCopy {

    private String localPathMasters ;
    private Repository localRepomasters;
    private Git gitmasters;
    private String username ;
    private String password ;
    private String url ;
    private String refspec;
    Boolean success = false;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GitCudlDataCopy.class.getName());

    public GitCudlDataCopy(String localPathMasters,String username,String password,String url,String refspec){
        this.localPathMasters = localPathMasters;
        this.username = username;
        this.password = password;
        this.url = url;
        this.refspec = refspec;
    }
    protected Boolean gitcopy() {

        try {
            //master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            //refspec = master:branch
            gitmasters.push().setRefSpecs(new RefSpec(refspec)).setRemote(url).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
            success = true;
        } catch (IOException ex) {
            System.out.println("success from IOexception="+success);
            logger.error("IO Exception" + ex);
            success = false;
        } catch (RefAlreadyExistsException ex) {
            logger.error("RefAlreadyExistsException" +ex);
            success = false;
        } catch (CheckoutConflictException ex) {
            logger.error("CheckoutConflictException" +ex);
            success = false;
        } catch (GitAPIException ex) {
            System.out.println("success from gitexception="+success);
            logger.error("GitAPIException" +ex);
            success = false;
        }
        return success;
    }

}
