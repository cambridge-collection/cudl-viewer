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
public class GitHelper {

    private String localPathMasters ;
    private Repository localRepomasters;
    private Git gitmasters;
    private String username ;
    private String password ;
    private String url ;    
    Boolean success = false;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GitHelper.class.getName());

    public GitHelper(String localPathMasters,String username,String password,String url){
        this.localPathMasters = localPathMasters;
        this.username = username;
        this.password = password;
        this.url = url;        
    }
    
    /**
     * Pushes changes to remote repository using the specified refSpec.    
     * 
     * @param refspec
     * @return
     */
    protected Boolean push(String refspec) {

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
    
    /**
     * Commits *all* changed files and adds any new files to local repository. 
     * A push is required to push these changes to BitBucket.
     * 
     * @return
     */
    protected Boolean commit() {

        try {
            //master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.add().addFilepattern(".").call();
            gitmasters.commit().setMessage("admin interface commit").call();
            success = true;
        } catch (IOException ex) {
        	ex.printStackTrace();
            logger.error("IO Exception" + ex);
            success = false;
        } catch (RefAlreadyExistsException ex) {
        	ex.printStackTrace();
            logger.error("RefAlreadyExistsException" +ex);
            success = false;
        } catch (CheckoutConflictException ex) {
        	ex.printStackTrace();
        	logger.error("CheckoutConflictException" +ex);
            success = false;
        } catch (GitAPIException ex) {
        	ex.printStackTrace();
            logger.error("GitAPIException" +ex);
            success = false;
        }
        return success;
    }
    
    /**
     * Deletes file from git local repository and commits the change
     * A push is required to push these changes to BitBucket.
     * 
     * @param filePath
     * @return
     */
    protected Boolean delete(String filePath) {

        try {
            //master
            localRepomasters = new FileRepository(localPathMasters + "/.git");
            gitmasters = new Git(localRepomasters);
            gitmasters.rm().addFilepattern(filePath).call();
            gitmasters.commit().setMessage("admin interface commit").call();
            success = true;
        } catch (IOException ex) {
        	ex.printStackTrace();
            logger.error("IO Exception" + ex);
            success = false;
        } catch (RefAlreadyExistsException ex) {
        	ex.printStackTrace();
            logger.error("RefAlreadyExistsException" +ex);
            success = false;
        } catch (CheckoutConflictException ex) {
        	ex.printStackTrace();
        	logger.error("CheckoutConflictException" +ex);
            success = false;
        } catch (GitAPIException ex) {
        	ex.printStackTrace();
            logger.error("GitAPIException" +ex);
            success = false;
        }
        return success;
    }

}
