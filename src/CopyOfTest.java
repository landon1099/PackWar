import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import service.SvnService;
import service.SvnServiceImpl;



public class CopyOfTest {
	
	   public static void main(String[] args) throws Exception{
	        //1.根据访问协议初始化工厂
	        DAVRepositoryFactory.setup();
	        //2.初始化仓库
	        String url = "file:///D:/local_svn/trunk";
	        SVNRepository svnRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
	        //3.创建一个访问的权限
	        String username = "test";
	        String password = "test";
	        char[] pwd = password.toCharArray();
	        ISVNAuthenticationManager authenticationManager = SVNWCUtil.createDefaultAuthenticationManager(username,pwd);
	        svnRepository.setAuthenticationManager(authenticationManager);
	        /*输出仓库的根目录和UUID*/
	        System.out.println("Repository Root:" + svnRepository.getRepositoryRoot(true));
	        System.out.println("Repository UUID:" + svnRepository.getRepositoryUUID(true));
	        /**
	         * 检验某个URL（可以是文件、目录）是否在仓库历史的修订版本中存在，参数：被检验的URL，修订版本，这里我们想要打印出目录树，所以要求必须是目录
	         * SVNNodeKind的枚举值有以下四种：
	         *  SVNNodeKind.NONE    这个node已经丢失（可能是已被删除）
	         *  SVNNodeKind.FILE    文件
	         *  SVNNodeKind.DIR     目录
	         *  SVNNodeKind.UNKNOW  未知，无法解析
	         * */
	        /*
	         *  被检验的URL，本例有两种等价的写法。
	         *  1.不是以"/"开头的是相对于仓库驱动目录的相对目录，即svnRepository的url，在本例中是：空字符串（url目录是：https://wlyfree-PC:8443/svn/svnkitRepository1/trunk）
	         *  2.以"/"开头的是相对于svnRepository root目录的相对目录，即svnRepository的rootUrl，在本例中是：/trunk（root目录是https://wlyfree-pc:8443/svn/svnkitRepository1）
	         */

	        String checkUrl = "";
	        //修订版本号，-1代表一个无效的修订版本号，代表必须是最新的修订版
	        long revisionNum = -1;
	        SVNNodeKind svnNodeKind = svnRepository.checkPath(checkUrl,revisionNum);
	        if(svnNodeKind == SVNNodeKind.NONE){
	            System.err.println("This is no entry at " + checkUrl);
	            System.exit(1);
	        }else if(svnNodeKind == SVNNodeKind.FILE){
	            System.err.println("The entry at '" + checkUrl + "' is a file while a directory was expected.");
	            System.exit(1);
	        }else{
	            System.err.println("SVNNodeKind的值：" + svnNodeKind);
	        }
	        //打印出目录树结构
	        listEntries(svnRepository,checkUrl);
	        //打印最新修订版的版本号
	        System.err.println("最新修订版版本号：" + svnRepository.getLatestRevision());
	    }
	    private static void listEntries(SVNRepository svnRepository,String path) throws Exception{
	        System.err.println("path:" + path);
	        Collection entry =  svnRepository.getDir(path, -1 ,null,(Collection)null);
	        Iterator iterator = entry.iterator();
	        while(iterator.hasNext()){
	            SVNDirEntry svnDirEntry = (SVNDirEntry)iterator.next();
	            System.out.println("path:" + "/" + (path.equals("") ? "" : path + "/") + svnDirEntry.getName() + ",(author:" + svnDirEntry.getAuthor() + ",revision:" + svnDirEntry.getRevision() + ",date:" + svnDirEntry.getDate() + ")");
	            if(svnDirEntry.getKind() == SVNNodeKind.DIR){
	                String tempPath = (path.equals("") ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName()) ;
//	                listEntries(svnRepository,tempPath);
	            }
	        }
	    }
}
