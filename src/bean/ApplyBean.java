package bean;

public class ApplyBean {
	
	private String svnUpperUrl; //升级包上级路径
	private String svnUrl; //升级包路径
	private String apply_no; //发布申请号
	
	public String getSvnUpperUrl() {
		return svnUpperUrl;
	}
	public void setSvnUpperUrl(String svnUpperUrl) {
		this.svnUpperUrl = svnUpperUrl;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getApply_no() {
		return apply_no;
	}
	public void setApply_no(String apply_no) {
		this.apply_no = apply_no;
	}
	
}
