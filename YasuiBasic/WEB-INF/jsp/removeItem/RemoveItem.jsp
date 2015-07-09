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
		<c:when test="${role != 'administrator'}">
			<h2>管理者以外はこの画面にアクセスすることはできません。</h2>
			<form method="POST" action="/YasuiBasic/Index">
				<input type="submit" name="return" value="ログイン画面に戻る" />
			</form>
		</c:when>
		<c:otherwise>
			<c:if test="${not empty errormessage}">
				<c:forEach var="message" items="${errormessage}"
					varStatus="statusError">
					<span class="errormsg">(Error)：${message}</span>
					<br />
				</c:forEach>
				<c:remove var="errormessage" />
				<%--表示が終わったエラーメッセージはセッションから削除する --%>
			</c:if>
			<h2>商品一覧</h2>
			<p>削除したい商品のチェックボックスをチェックしてください</p>
			<form method="POST" action="/YasuiBasic/RemoveItemConfirm">
				<table class="itemlist">
					<tr>
						<th>削除対象</th>
						<th>商品番号</th>
						<th>商品名</th>
						<th>商品画像</th>
						<th>サイズ</th>
						<th>価格</th>
						<th>在庫</th>
					</tr>
					<c:forEach var="item" items="${items}" varStatus="status">
						<tr>
							<td><input type="checkbox" name="${item.itemNum}"
								value="on"></td>
							<td><c:out value="${item.itemNum}" /></td>
							<td><c:out value="${item.itemName}" /></td>
							<td><a href=<c:out value="${item.imageUrl}" />>商品画像</a></td>
							<td><c:out value="${item.itemSize}" /></td>
							<td><fmt:formatNumber type="currency" groupingUsed="true"
									minFractionDigits="0" currencySymbol="\\" value="${item.price}" /></td>
							<td><fmt:formatNumber type="number" groupingUsed="true"
									minFractionDigits="0" value="${item.stock}" /></td>
						</tr>
					</c:forEach>
				</table>
				<input type="submit" value="確認"  />
			</form>
		</c:otherwise>
	</c:choose>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>