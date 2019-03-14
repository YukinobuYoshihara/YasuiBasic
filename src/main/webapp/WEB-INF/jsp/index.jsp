<!doctype html>
<html>
<head>
<title>安い家具のお求めならYASUI家具 | YASUI家具オンラインショップ</title>
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<c:if test="${not empty errormessage}">
		<ul></ul>
		<c:forEach var="message" items="${errormessage}"
			varStatus="statusError">
			<li class="errormsg">(Error)：${message}</li>
		</c:forEach>
		<c:remove var="errormessage" />
		<%--表示が終わったエラーメッセージはセッションから削除する --%>
	</c:if>
	<form method="POST" action="/YasuiBasic/Login">
		<fieldset style="border: none">
			<legend>顧客ID：</legend>
			<input type="text" name="username" size="20" maxlength="30"
				value="${sessionScope.username}" style="ime-mode: disabled" />
		</fieldset>
		<fieldset style="border: none">
			<legend>パスワード：</legend>
			<input type="password" name="password" size="20" maxlength="30"
				value="${sessionScope.username}" style="ime-mode: disabled" />
		</fieldset>
		<fieldset style="border: none">
			<input type="submit" name="submit" value="ログイン">
		</fieldset>
	</form>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>
