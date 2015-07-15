<%@page import="jp.recruit.bean.ItemBean"%>
<!doctype html>
<html>
<head>
<c:remove var="isLogin"/>
<c:remove var="newItem"/>
<c:remove var="items"/>
<c:remove var="orderitems"/>
<c:remove var="stockitems"/>
<c:remove var="canAdd"/>
<c:remove var="canOrder"/>
<c:remove var="canChange"/>
<c:remove var="errormessage"/>
<c:remove var="role"/>
<c:remove var="username"/>
<c:remove var="descript"/>
<c:remove var="id"/>
<c:remove var="exception"/>
<%
	//セッション廃棄
	session.invalidate();
%>
<title>YASUI家具オンラインショップ：ログアウト画面</title>
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<h2>ログアウトしました</h2>
	<form method="GET" action="index">
		<input type="submit" name="return" value="ログイン画面に戻る" />
	</form>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>