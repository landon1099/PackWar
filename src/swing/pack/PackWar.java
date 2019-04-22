package swing.pack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import utils.PropertiesUtil;
import bean.PackBean;
import bean.SvnBean;


public class PackWar {
	
	public static List<String> checkLocalSvn(String path) throws SVNException, IOException {
//		double startTime = System.currentTimeMillis();
		//获取路径下项目所有文件列表
//		List<String> fileList = new ArrayList<String>();
//		fileList = getUrls(path, 1, fileList);
		//获取webRootName
//		String webRootName = null;
//		for (String url : fileList) {
//			if (url.contains("WEB-INF")) {
//				String[] webinfUris = url.split("WEB-INF");
//				String[] webinfUri = webinfUris[0].split("/");
//				webRootName = webinfUri[webinfUri.length-1];
//				break;
//			}
//		}
		
		//获取编译版本号
//		double version = 0.0;
//		version = getVersion(fileList);
		
		//校验本地SVN
		List<String> changedList = new ArrayList<>();
		changedList = checkSvn(changedList, path);
		return changedList;
		
		//计算运行用时
//		double endTime = System.currentTimeMillis();
//		double tmpTime = (endTime - startTime)/1000;
//		tmpTime = (double)(Math.round(tmpTime*100))/100;
//		System.out.println("------校验本地SVN用时：【" + tmpTime + "s】------");
		
//		JSONObject json = new JSONObject();
//	    json.put("jsonData", jsonData);
//	    json.put("version", version);
//	    json.put("webRootName", webRootName);
//	    response.setContentType("text/html;charset=UTF-8");
//	    response.getWriter().write(json.toString());
	}
	
	
	/**
	 * 获取目录下的文件路径
	 * 
	 * type=0遍历当前目录
	 * type=1遍历所有文件
	 * @param path
	 * @param type
	 */
	public static List<String> getUrls(String path, int type, List<String> list) {
		if (!new File(path).exists()) {
			System.out.println("---errorin getUrls---目录不存在"+path);
			return null;
		}
		File file = new File(path);
		if (file.isFile()) {
			System.out.println("---errorin getUrls---输入的路径为文件，请选择目录！");
			return null;
		}
		File[] fileLists = file.listFiles();
		if (type == 0) {
			for (int i = 0; i < fileLists.length; i++) { 
				String temp = fileLists[i].getAbsolutePath().replaceAll("\\\\", "/");
				temp = temp.split("/")[temp.split("/").length-1];
				list.add(temp);
			}
			return list;
		}
		if (type == 1) {
			for (int i = 0; i < fileLists.length; i++) {
				if (fileLists[i].isDirectory()) {
					//过滤.开头目录
					String fileName = fileLists[i].getName();
					String temp = fileName.split("\\.")[0].trim();
					if (!"".equals(temp)) {
						getUrls(fileLists[i].getAbsolutePath(), 1, list);
					}
				}else {
					list.add(fileLists[i].getAbsolutePath().replaceAll("\\\\", "/"));
				}
			}
		}
		return list;
	}
	
	public static double getVersion(List<String> fileList) {
		double version = 0.0;
		for (String url : fileList) {
			if (url.endsWith(".class")) {
				try{
					FileInputStream fis = new FileInputStream(url);
					int length = fis.available();
					byte[] data = new byte[length];
					fis.read(data);
					fis.close();
					//解析文件数据
					int major_version = (((int)data[6]) << 8) + data[7];
//					System.out.println("主版本号(version):"+major_version+"----------");
					switch (major_version){
					case 51:
						version = 1.7;
						break;
					case 50:
						version = 1.6;
						break;
					case 49:
						version = 1.5;
						break;
					case 48:
						version = 1.4;
						break;
					case 52:
						version = 1.8;
						break;
					case 53:
						version = 1.9;
						break;
					default :
						version = 0.0;
						break;
					}
					
				}catch(Exception e){
					System.out.println(e);
				}
				return version;
			}
		}
		return version;
	}
	
	/**
	 * 校验本地SVN
	 * @param changedList
	 * @param projectUrl
	 * @return
	 * @throws IOException
	 * @throws SVNException 
	 */
	public static List<String> checkSvn(List<String> changedList, String projectUrl)
			throws SVNException, IOException {
		List<String> tmpList = new ArrayList<>();
		File dirFile = new File(projectUrl);
		String[] list = dirFile.list();
		for(String fileName : list) {
			if (".svn".equals(fileName)) {
				tmpList = getUrls(projectUrl, 1, tmpList);
				for(String url : tmpList){
					url = url.replaceAll("\\\\", "/");
					//System.out.println("------对比本地文件：" + url);
					if (!url.contains("classes")) {
						//System.out.println(url);
						if (isFileChanged(url)) {
							changedList.add(url);
						}
					}
				}
				break ;
			}
		}
		//存储项目目录下所有变化文件的上级目录
		List<String> tempList = new ArrayList<String>();
		for (String path : changedList) {
			int length = path.split("/").length;
			String[] tmpStrs = path.split("/");
			for (int i = tmpStrs.length-1; i>length; i--) {
				String temp = path.split("/")[path.split("/").length-1];
				path = path.replaceAll("/"+temp, "");
				if (!tempList.contains(path)) {
					tempList.add(path);
				}
			}
		}
		changedList.addAll(tempList);
		return changedList;
	}
	
	/**
	 * 检查本地文件状态是否改变
	 * @return Boolean
	 * @throws IOException
	 * @throws SVNException 
	 */
	public static Boolean isFileChanged(String url) {
		Boolean resultBoolean = false;
		SVNClientManager ourClientManager = SVNClientManager.newInstance(null, null, null); 
        File compFile = new File(url);
        SVNStatusClient statusClient = ourClientManager.getStatusClient();
        SVNStatus doStatus = null;
		try {
			doStatus = statusClient.doStatus(compFile, false);
			SVNStatusType nodeStatus = doStatus.getContentsStatus();
			if (!SVNStatusType.STATUS_NORMAL.equals(nodeStatus)) {
				resultBoolean = true;
				System.out.println("---"+ nodeStatus +"---：" + url);
			}
		} catch (SVNException e) {
			if (!url.contains("META-INF")) {
				resultBoolean = true;
				e.printStackTrace();
			}
		}
        return resultBoolean;
	}
	
	
	public static String packWar(String pPath, String gPath, List<String> cList, List<String> strList) {
		PropertiesUtil pros = new PropertiesUtil();
		pros.setValue("swing_pro_tmp", pPath);
		pros.setValue("swing_gen_tmp", gPath);
		
		PackBean packBean = new PackBean();
		packBean.setWebRootName(strList.get(0));
		packBean.setPack_addr(gPath);
		packBean.setProjectUrl(pPath + "/");
		String[] split = pPath.split("/");
		packBean.setProjectName(split[split.length - 1]);
		//设置生成文件名
		String fileStr = strList.get(0) + strList.get(1);
		packBean.setFileStr(fileStr);
		//生成war文件
		String msg = "以下文件在webRoot目录中不存在，请检查webroot目录(或classes目录)：";
		String msgOld = "以下文件在webRoot目录中不存在，请检查webroot目录(或classes目录)：";
		for(int i=0; i<cList.size(); i++){
			if (StringUtils.isNotBlank(cList.get(i))) {
				File file = new File(cList.get(i));
				if (file.isFile()) {
					//打包
					String temp = generateWar(cList.get(i), packBean);
					if (!"".equals(temp)) {
						msg += ""+temp+",";
					}
				} else if (file.isDirectory()) {
					//不处理文件目录
					/*if (!file.exists()) {
						file.mkdir();
					}*/
				}
			}
		}
		if (msg.equals(msgOld)) {
			return "";
		} else {
			return msg;
		}
	}
	
	public static String getWebroot(String path) {
		//获取路径下项目所有文件列表
		List<String> fileList = new ArrayList<String>();
		fileList = getUrls(path, 1, fileList);
		//获取webRootName
		String webRootName = null;
		for (String url : fileList) {
			if (url.contains("WEB-INF")) {
				String[] webinfUris = url.split("WEB-INF");
				String[] webinfUri = webinfUris[0].split("/");
				webRootName = webinfUri[webinfUri.length-1];
				break;
			}
		}
		//获取编译版本号
		double version = 0.0;
		version = getVersion(fileList);
		return webRootName;
	}
	
	/**
	 * 生成war包文件
	 * @param path
	 */
	public static String generateWar(String path, PackBean packBean) {
		String backupPath = path;
		String projectName = packBean.getProjectName();
		String webRootName = packBean.getWebRootName();
		// e:/projects/TestProject/
		String projectUrl = packBean.getProjectUrl();
		// e:/projects/B_Pack_To
		String pack_addr = packBean.getPack_addr();
		String fileStr = packBean.getFileStr();
		//提前处理.java后缀
		if (path.endsWith(".java")) {
			path = path.replace(".java", ".class");
		}
		String generate_path = "";
		//处理资源目录（不包含webRootName的目录）
		if (!path.contains(projectName+"/"+webRootName)) {
			String temp_uri = path.replace(projectUrl, ""); //  src/com/itheima/dao/UserDao.java
			String path_head = projectUrl + webRootName + "/WEB-INF/classes";
			String[] uriStrings = temp_uri.split("/");
			for (int count = 1; count< uriStrings.length; count++) {
				String path_foot = "";
				for (int i = count; i < uriStrings.length; i++) {
					path_foot = path_foot + "/" + uriStrings[i];
				}
				path = path_head + path_foot;
				//System.out.println(path);
				if (new File(path).exists()) {
					break;
				}
			}
		}
		String oldStr = projectUrl;
		String newStr = pack_addr + "/" + fileStr + "/";
		generate_path = path.replace(oldStr, newStr);
		if (new File(path).exists()) {
			copyFile(path, generate_path);
		} else {
			return backupPath;
		}
		
		//检查$path
		if (path.endsWith(".class")) {
			String fileName = path.split("/")[path.split("/").length-1].replace(".class", "");
			String upperUrl = (new File(path)).getParent().replaceAll("\\\\", "/");
			List<String> tmpList = new ArrayList<String>();
			tmpList = getUrls(upperUrl, 0, tmpList);
			for (String string : tmpList) {
				if (string.contains(fileName+"$")) {
					path = upperUrl+"/"+string;
					String tmp_generate_path =  generate_path.replace(fileName+".class", string);
					copyFile(path, tmp_generate_path);
				}
			}
		}
		return "";
	}
	
	/**
	 * 复制文件
	 * @param path
	 * @param generate_path
	 */
	public static void copyFile(String path, String generate_path) {
		FileInputStream input = null;
		FileOutputStream output = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File newWarFile = new File(generate_path);
			if (!newWarFile.getParentFile().exists()) {
				boolean result = newWarFile.getParentFile().mkdirs();
				if (!result) {
					System.out.println("---errorin copyFile---创建文件夹失败：" + generate_path);
				}
			}
			input = new FileInputStream(path);
			output = new FileOutputStream(generate_path);
			bis = new BufferedInputStream(input);
			bos = new BufferedOutputStream(output);
			int ch = 0;
			while ((ch = bis.read()) != -1) {
				bos.write(ch);
			}
			System.out.println("------生成war包文件:"+generate_path);
		} catch (Exception e) {
			System.out.println("---errorin copyFile---生成war包文件出错：" + generate_path);
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
		}
	}
}
