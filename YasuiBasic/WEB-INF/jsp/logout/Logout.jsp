<%@page import="jp.recruit.bean.ItemBean"%>
<!doctype html>
<html>
<head>
<%
	//セッションの中身を取得できたらすべて削除
	session.removeAttribute("isLogin");
	session.removeAttribute("newItem");
	session.removeAttribute("items");
	session.removeAttribute("orderitems");
	session.removeAttribute("stockitems");
	session.removeAttribute("canAdd");
	session.removeAttribute("canOrder");
	session.removeAttribute("canChange");
	session.removeAttribute("errormessage");
	session.removeAttribute("role");
	session.removeAttribute("username");
	session.removeAttribute("descript");
	session.removeAttribute("id");
	session.removeAttribute("exception");
	//セッション廃棄
	session.invalidate();
%>
<title>YASUI家具オンラインショップ：ログアウト画面</title>
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<h2>ログアウトしました</h2>
	<form method="GET" action="/YasuiBasic/index">
		<input type="submit" name="return" value="ログイン画面に戻る" />
	</form>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>