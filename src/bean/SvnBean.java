package bean;


public class SvnBean {
	
	private String url;
	private Long startVersion;
//	private Long endVersion;
	private String begin;
	private String end;
	private String author;
	private SvnLoginBean svnLoginBean;
	
	public SvnLoginBean getSvnLoginBean() {
		return svnLoginBean;
	}
	public void setSvnLoginBean(SvnLoginBean svnLoginBean) {
		this.svnLoginBean = svnLoginBean;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getStartVersion() {
		return startVersion;
	}
	public void setStartVersion(Long startVersion) {
		this.startVersion = startVersion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
}
