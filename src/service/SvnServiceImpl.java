package service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.print.Doc;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.sun.org.apache.xml.internal.security.Init;

import bean.ChangedUrls;
import bean.SvnBean;
import bean.SvnLogBean;
import bean.SvnLoginBean;

public class SvnServiceImpl implements SvnService {
	
	/**
	 * 初始化repository
	 * @param svnBean
	 * @return
	 * @throws SVNException
	 */
	@SuppressWarnings("deprecation")
	public SVNRepository init(SvnBean svnBean) throws SVNException {
		String url = svnBean.getUrl();
		String name = svnBean.getSvnLoginBean().getName();
		String password = svnBean.getSvnLoginBean().getPassword();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        ISVNAuthenticationManager authManager =  SVNWCUtil.createDefaultAuthenticationManager( name , password );
        repository.setAuthenticationManager( authManager );
		return repository;
	}
	
    public static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();

        /*
         * For using over file:///
         */
        //FSRepositoryFactory.setup();
    }

	@Override
	public List<SvnLogBean> getLogByTime(final SvnBean svnBean) {
		setupLibrary();
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startRevision = 0;
        long endRevision = -1; //最新版本
        final List<SvnLogBean> logBeanList = new ArrayList<SvnLogBean>();
		SVNRepository repository = null;
		try {
			repository = init(svnBean);
			repository.log( new String[] { "" }, startRevision , endRevision , true , true ,
                new ISVNLogEntryHandler() {  
	                @Override  
	                public void handleLogEntry(SVNLogEntry svnlogentry)  throws SVNException {  
	                	//依据提交时间进行过滤
	                	Date begin = null;
	                    Date end = null;
	            		try {
	            			begin = format.parse(svnBean.getBegin());
	            			end = format.parse(svnBean.getEnd());
	            		} catch (ParseException e1) {
	            			e1.printStackTrace();
	            		}
	                    if (svnlogentry.getDate().after(begin) && svnlogentry.getDate().before(end)) {  
	                        //依据提交人过滤
							String author = svnBean.getAuthor();
							if (StringUtils.isNotBlank(author)) {
								if (author.equals(svnlogentry.getAuthor())) {
									fillResult(svnlogentry);
								}
							} else {
								fillResult(svnlogentry);
							}
	                    }
	                }
	                public void fillResult(SVNLogEntry svnlogentry) {
	                	SvnLogBean logBean = new SvnLogBean();
	                	List<ChangedUrls> changedUrlsList = new ArrayList<>();
	                	logBean.setVersion(svnlogentry.getRevision());//版本
	                	logBean.setAuthor(svnlogentry.getAuthor());//作者
	                	logBean.setDate(format.format(svnlogentry.getDate()));//日期
	                	logBean.setInfo(svnlogentry.getMessage());//日志信息
	                	Map<String, SVNLogEntryPath> changedPaths = svnlogentry.getChangedPaths();
	                	Map<String, String> tmpMap = new HashMap<>();
	                	for (Entry<String, SVNLogEntryPath> entry : changedPaths.entrySet()) {
	                		ChangedUrls changed_urls = new ChangedUrls();
//	                		System.out.println("changed_urls.setStatus(entry.getKey());" + entry.getKey());
	                		String status = decodeStatus(entry.getValue().getType());
	                		changed_urls.setUrl(status + "：" + entry.getKey());
//	                		changed_urls.setStatus(status);
//	                		System.out.println("changed_urls.setUrl(entry.getValue().toString());" + entry.getValue().toString());
	                		changedUrlsList.add(changed_urls);
	                		if (StringUtils.isNotBlank(status)) {
	                			tmpMap.put(status, entry.getKey());
							}
						}
	                	List<String> options = new ArrayList<>();
	                	for (Entry<String, String> entry : tmpMap.entrySet()) {
	                		options.add(entry.getKey());
	                	}
	                	logBean.setChangedUrls(changedUrlsList);
	                	logBean.setOptions(options);
	                	logBeanList.add(logBean);
	                }
				}
			);
		} catch (SVNException e) {
			System.out.println("==errorin SvnServiceImpl.getLogByTime");
			e.printStackTrace();
		}
		return logBeanList;
	}
	
	public static String decodeStatus(char c) {
		String result = "";
		switch (Character.toString(c)) {
		case "M":
			result = "修改";
			break;
		case "D":
			result = "删除";
			break;
		case "A":
			result = "新增";
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public  List<Map<String, String>> getLogByVersion(SvnBean svnBean, List<Map<String, String>> list) {
		setupLibrary();
		final Map<String, String> cMap = list.get(0);//新增、修改
		final Map<String, String> dMap = list.get(1);//删除
		final List<Map<String, String>> reList = new ArrayList<>();
        final String author = svnBean.getAuthor();
        long startRevision = svnBean.getStartVersion();
        long endRevision = -1; //最新版本
		SVNRepository repository = null;
		try {
			repository = init(svnBean);
			repository.log( new String[] { "" }, startRevision , endRevision , true , true ,
                new ISVNLogEntryHandler() {  
	                @Override  
	                public void handleLogEntry(SVNLogEntry svnlogentry)  throws SVNException {  
                        //依据提交人过滤
                        if ("all".equals(author)) {  
                        	fillResult(svnlogentry);
                        } else {
                        	if (author.equals(svnlogentry.getAuthor())) {
                        		fillResult(svnlogentry);
                        	}
                        }
	                }
	                public void fillResult(SVNLogEntry svnlogentry) {
	                	for (Entry<String, SVNLogEntryPath> entry : svnlogentry.getChangedPaths().entrySet()) {
	                		String status = entry.getValue().getType() + "";
	                		if ("D".equals(status)) {
	                			dMap.put(entry.getKey(), "D");
	                		} else {
	                			cMap.put(entry.getKey(), status);
	                		}
	                		reList.add(cMap);
	                		reList.add(dMap);
						}
	                }
				}
			);
		} catch (SVNException e) {
			System.out.println("");
			e.printStackTrace();
		}
		return reList;
	}

	@Override
	public List<Map<String, String>> checkDelete(SvnBean svnBean, List<Map<String, String>> list) {
		setupLibrary();
		Map<String, String> cMap = list.get(0);
		Map<String, String> dMap = list.get(1);
		SVNRepository repository = null;
		SVNNodeKind nodeKind;
		String url = "";
		List<String> tmpList = null;
		try {
			Set<Entry<String, String>> entrySet = dMap.entrySet();
			tmpList = new ArrayList<>();
			for (Entry<String, String> entry : entrySet) {
				url = entry.getKey();
				repository = init(svnBean);
				nodeKind = repository.checkPath(url, -1);
				boolean result = nodeKind == SVNNodeKind.NONE ? true : false;
				if (result) {
					cMap.remove(url);
					System.out.println("------cMap移出删除的文件：" + url);
				} else {
					tmpList.add(url);
				}
			}
			if (tmpList.size() > 0) {
				for (String tmpUri : tmpList) {
					dMap.put(tmpUri, "A");
				}
			}
		} catch (SVNException e) {
			e.printStackTrace();
			System.out.println("==errorsin SvnServiceImpl.checkDelete  路径：" + url);
		}
		list = new ArrayList<>();
		list.add(cMap);
		list.add(dMap);
		return list;
	}

	/**
	* 测试SVN账户密码
	* 1:连接成功，0:账号密码错误(确认权限)，-1:连接失败(检查网络)
	*/
	public int testSvnConnection(SvnLoginBean loginBean) {
		try {
			testConnection(loginBean.getSvnUrl(), loginBean.getName(), loginBean.getPassword());
		} catch (SVNException expection) {
			System.out.println("==SVN连接失败：" + expection.getMessage().toString());
			if (expection.getMessage().contains("Authentication")) {
				//svn: E170001: Authentication required for
				System.out.println("==errorin SvnServiceImpl.testSvnConnection：用户名密码错误！(确认是否有权限)");
				return 0;
			} else {
				System.out.println("==errorin SvnServiceImpl.testSvnConnection：SVN地址连接失败！");
				return -1;
			}
		}
		System.out.println("------SVN：连接成功！");
		return 1;
	}
	private static String STRING_EMPTY = "";
	private static int NEGATIVE_ONE = -1;
	/**
	* 测试SVN地址是否正确
	*/
	public boolean testConnection(String svnUrl, String name, String password) throws SVNException {
		setupLibrary();
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		ISVNAuthenticationManager auth = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(auth);
		SVNNodeKind nodeKind = repository.checkPath(STRING_EMPTY, NEGATIVE_ONE);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No project at URL");
			throw new SVNException(err);
		}
		return true;
	}
	
	public List<String> getSvnDirs(String name, String password, String url){
	    setupLibrary();
		SVNRepository repository = null;
		List<SVNDirEntry> svnDirInfos = null;
        try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
			ISVNAuthenticationManager authManager =  SVNWCUtil.createDefaultAuthenticationManager( name , password );
			repository.setAuthenticationManager( authManager );
			svnDirInfos = listEntries(repository, "");
		} catch (SVNException e) {
			System.out.println("==errorin svnserviceImpl.getSvnDirs() 获取svn路径失败!");
			e.printStackTrace();
		}
        List<String> nameList = new ArrayList<>();
        for (SVNDirEntry svnDirEntry : svnDirInfos) {
        	String fileNm = svnDirEntry.getName();
        	nameList.add(fileNm);
		}
        Collections.sort(nameList);
		return nameList;
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

    /**
     * svn上传文件
     */
	@Override
	public String uploadFile(File file, SvnLoginBean loginBean) {
		String result = "";
		setupLibrary();
		File impDir = new File(loginBean.getSvnUrl());
		SVNClientManager ourClientManager = null;
		try {
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
			ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, loginBean.getName(), loginBean.getPassword()); 
			//svn-加入
			ourClientManager.getWCClient().doAdd(new File[] { impDir },false,false,false,SVNDepth.fromRecurse(true),false,false,true);
			//svn-提交
			result = "1";
			//svn-加入-添加版本控制
			//SVNCommitInfo commitInfo = ourClientManager.getCommitClient().doImport(impDir, SVNURL.parseURIEncoded("file:///D:/local_svn/trunk/HeimaShop/新建文本文档.txt"),"import operation!",null, false,false,SVNDepth.INFINITY); 
		} catch (SVNException e) {
			result = "-1";
			System.out.println("==errorin SvnServiceImpl.uploadFile()：加入失败！");
			e.printStackTrace();
		}
		SVNCommitInfo doCommit = null;
		try {
			doCommit = ourClientManager.getCommitClient().doCommit(new File[] { impDir }, true, "", null, null, true, false, SVNDepth.INFINITY);
		} catch (SVNException e) {
			result = "-2";
			System.out.println("==errorin SvnServiceImpl.uploadFile()：提交失败！");
			e.printStackTrace();
		}
		System.out.println("------提交成功：" + doCommit.toString());
		return result;
	}
	
}
