import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Test {
	
	public static void main(String[] args) throws SVNException {
		
		DAVRepositoryFactory.setup();
	    SVNRepositoryFactoryImpl.setup();
		File impDir = new File("D:\\新建文件夹\\新建文件夹 (2)");
		SVNClientManager ourClientManager = null;
		try {
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
			ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, "may", "may"); 
			//svn-加入
			ourClientManager.getWCClient().doAdd(new File[] { impDir },false,false,false,SVNDepth.fromRecurse(true),false,false,true);
			//svn-提交
			SVNCommitInfo doCommit = ourClientManager.getCommitClient().doCommit(new File[] { impDir }, true, "", null, null, true, false, SVNDepth.INFINITY);
			System.out.println(doCommit.toString());
			//svn-加入-添加版本控制
			//SVNCommitInfo commitInfo = ourClientManager.getCommitClient().doImport(impDir, SVNURL.parseURIEncoded("file:///D:/local_svn/trunk/HeimaShop/新建文本文档.txt"),"import operation!",null, false,false,SVNDepth.INFINITY); 
		} catch (SVNException e) {
			System.out.println("==errorin SvnServiceImpl.uploadFile：上传失败！");
			e.printStackTrace();
		}
	}
	
	/**
	 * Puts directories and files under version control
	 * @param clientManager
	 * 			SVNClientManager
	 * @param wcPath 
	 * 			work copy path
	 */
	public static void addEntry(SVNClientManager clientManager, File wcPath) {
		try {
			clientManager.getWCClient().doAdd(new File[] { wcPath }, false,
					false, false, SVNDepth.fromRecurse(true), false, false,
					true);
		} catch (SVNException e) {
//			logger.error(e.getErrorMessage(), e);
		}
	}
	 

	

	
	/**
     * @Description: 获取版本库中某一目录下的所有条目。可以根据参数选择是否进行递归
     * @param repository
     * @param path  svn路径（相对路径）前面不需要/
     */
    public static List<SVNDirEntry> listEntries(SVNRepository repository, String path)throws SVNException{
    	List<SVNDirEntry> result = new LinkedList<>();
        //获取版本库的path目录下的所有条目。参数－1表示是最新版本。
        Collection<?> entry = repository.getDir(path, -1, null, (Collection<?>) null);
        Iterator<?> iterator = entry.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry svnDirEntry = (SVNDirEntry) iterator.next();
            //System.out.println("path:" + "/" + (path.equals("") ? "" : path + "/") + svnDirEntry.getName() + ",(author:" + svnDirEntry.getAuthor() + ",revision:" + svnDirEntry.getRevision() + ",date:" + svnDirEntry.getDate() + ")");
            result.add(svnDirEntry);
        }
        return result;
    }

    
    
}
