package jp.recruit.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import jp.recruit.bean.ContentsBean;


public class ContentsDao extends BaseDao {
	public HashMap<String,ContentsBean> getAllContents() throws SQLException{
		HashMap<String,ContentsBean> contents = new HashMap<String,ContentsBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.mid,c.title,c.keywd,c.descript,c.role,c.skip" +
				" FROM YASUI.contents c";
		try{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ContentsBean content = new ContentsBean();
				content.setMid(rs.getString("mid"));
				content.setTitle(rs.getString("title"));
				content.setKeywd(rs.getString("keywd"));
				content.setDescript(rs.getString("descript"));
				content.setRole(rs.getString("role"));
				content.setSkip(rs.getInt("skip"));
				contents.put(content.getMid(), content);
			}
			rs.close();
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return contents;
	}

}
