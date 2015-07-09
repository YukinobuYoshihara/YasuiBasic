package jp.recruit.bean;

public class ContentsBean implements java.io.Serializable{

	private static final long serialVersionUID = 8673698103099456835L;
	String title;
	String keywd;
	String descript;
	String mid;
	int skip;
	String role;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		if(title!=null&&!title.isEmpty()){
			this.title = title;
		}else{
			System.err.println("(ContentsBean)Titleが空またはnullです。");
		}
	}
	/**
	 * @return the keywd
	 */
	public String getKeywd() {
		return keywd;
	}
	/**
	 * @param keywd the keywd to set
	 */
	public void setKeywd(String keywd) {
		if(keywd!=null&&!keywd.isEmpty()){
			this.keywd = keywd;
		}else{
			//System.err.println("(ContentsBean)keywdが空またはnullです。");
		}
	}
	/**
	 * @return the descript
	 */
	public String getDescript() {
		return descript;
	}
	/**
	 * @param descript the descript to set
	 */
	public void setDescript(String descript) {
		if(descript!=null&&!descript.isEmpty()){
			this.descript = descript;
		}else{
			System.err.println("(ContentsBean)descriptが空またはnullです。");
		}
	}
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		if(mid!=null&&!mid.isEmpty()){
			this.mid = mid;
		}else{
			System.err.println("(ContentsBean)midが空またはnullです。");
		}
	}
	/**
	 * @return the skip
	 */
	public int getSkip() {
		return skip;
	}
	/**
	 * @param skip the skip to set
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		if(role!=null&&!role.isEmpty()){
			this.role = role;
		}else{
			System.err.println("(ContentsBean)roleが空またはnullです。");
		}
	}
}
