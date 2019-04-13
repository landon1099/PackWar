package bean;

public class PackBean {
	private String webRootName;
	private String projectName;
	private String projectUrl;
	private String pack_addr;
	private String local_addr;
	private String fileStr;
	private String $path;
	
	public String get$path() {
		return $path;
	}
	public void set$path(String $path) {
		this.$path = $path;
	}
	public String getFileStr() {
		return fileStr;
	}
	public void setFileStr(String fileStr) {
		this.fileStr = fileStr;
	}
	public String getWebRootName() {
		return webRootName;
	}
	public void setWebRootName(String webRootName) {
		this.webRootName = webRootName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectUrl() {
		return projectUrl;
	}
	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}
	public String getPack_addr() {
		return pack_addr;
	}
	public void setPack_addr(String pack_addr) {
		this.pack_addr = pack_addr;
	}
	public String getLocal_addr() {
		return local_addr;
	}
	public void setLocal_addr(String local_addr) {
		this.local_addr = local_addr;
	}
	
}
