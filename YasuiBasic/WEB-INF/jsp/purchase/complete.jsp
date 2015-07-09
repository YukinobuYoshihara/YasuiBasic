<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/jsp/common/tdk.jsp" />
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<c:import url="/WEB-INF/jsp/common/navmenu.jsp" />
	<c:choose>
	<c:when test="${not empty sessionScope.errormessage}">
		<c:forEach var="message" items="${errormessage}"
			varStatus="statusError">
			<span class="errormsg">(Error)：${message}</span>
			<br />
		</c:forEach>
		<c:remove var="errormessage" />
		<%--表示が終わったエラーメッセージはセッションから削除する --%>
		<h2>エラーのため、注文を完了できませんでした</h2>
	</c:when>
	<c:otherwise>
	<h2>注文を完了しました</h2>
	</c:otherwise>
	</c:choose>
	<form method="POST" action="/YasuiBasic/ListItem">
		<input type="submit" name="return" value="商品一覧に戻る" />
	</form>
	<c:remove var="items" />
	<c:remove var="orderitem" />
	<c:remove var="canOrder" />
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>