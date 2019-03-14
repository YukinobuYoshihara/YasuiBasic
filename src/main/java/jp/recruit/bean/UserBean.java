package jp.recruit.bean;

public class UserBean implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5137407898783534871L;
	String id;
	String name;
	String passwd;
	String descript;
	String role;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		if(id!=null&&!id.equals("")){
			this.id = id;
		}else{
			System.err.println("(UserBean)idが空またはnullです。");
		}

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name!=null&&!name.equals("")){
			this.name = name;
		}else{
			System.err.println("(UserBean)nameが空またはnullです。");
		}
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		if(passwd!=null&&!passwd.equals("")){
			this.passwd = passwd;
		}else{
			System.err.println("(UserBean)passwdが空またはnullです。");
		}
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		if(descript!=null&&!descript.equals("")){
			this.descript = descript;
		}else{
			System.err.println("(UserBean)descriptが空またはnullです。");
		}
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		if(role!=null&&!role.equals("")){
			this.role = role;
		}else{
			System.err.println("(UserBean)roleが空またはnullです。");
		}
	}
}
