package service;

import java.io.File;
import java.util.List;
import java.util.Map;

import bean.SvnBean;
import bean.SvnLogBean;
import bean.SvnLoginBean;

public interface SvnService {
	
	List<SvnLogBean> getLogByTime(SvnBean svnBean);

	List<Map<String, String>> getLogByVersion(SvnBean svnBean, List<Map<String, String>> list);

	List<Map<String, String>> checkDelete(SvnBean svnBean, List<Map<String, String>> list);

	int testSvnConnection(SvnLoginBean loginBean);

	String uploadFile(File file, SvnLoginBean loginBean);

	List<String> getSvnDirs(String name, String password, String svnUrl);
	
}
