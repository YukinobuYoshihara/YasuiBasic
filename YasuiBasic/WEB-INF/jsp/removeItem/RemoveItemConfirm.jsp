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
			<c:choose>
				<c:when test="${canRemove == false}">
					<h2>削除対象がありません</h2>
					<p>戻るボタンを押して、商品一覧から選択しなおしてください</p>
					<c:if test="${not empty sessionScope.errormessage}">
						<c:forEach var="message" items="${errormessage}"
							varStatus="statusError">
							<span class="errormsg">(Error)：${message}</span>
							<br />
						</c:forEach>
						<c:remove var="errormessage" />
						<%--表示が終わったエラーメッセージはセッションから削除する --%>
					</c:if>
					<form method="POST" action="/YasuiBasic/RemoveItem">
						<input type="submit" name="goindex" value="戻る" />
					</form>
				</c:when>
				<c:otherwise>
					<h2>下記の商品を削除しますか？</h2>
					<c:if test="${canRemove == true}">
						<table class="itemlist">
							<tr>
								<th>商品番号</th>
								<th>商品名</th>
								<th>商品画像</th>
								<th>サイズ</th>
								<th>価格</th>
								<th>在庫</th>
							</tr>
							<c:forEach var="targetItem" items="${targetItems}"
								varStatus="status">
								<tr>
									<td><c:out value="${targetItem.itemNum}" /></td>
									<td><c:out value="${targetItem.itemName}" /></td>
									<td><a href=<c:out value="${targetItem.imageUrl}"/>>商品画像</a></td>
									<td><c:out value="${targetItem.itemSize}" /></td>
									<td><fmt:formatNumber type="currency" groupingUsed="true"
										currencySymbol="\\" value="${targetItem.price}" 
										minFractionDigits="0" /></td>
									<td><fmt:formatNumber type="number" groupingUsed="true"
										value="${targetItem.stock}" minFractionDigits="0" /></td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					<div id="goreturn">
						<form method="POST" action="/YasuiBasic/RemoveItem">
							<input type="submit" name="goindex" value="戻る" />
						</form>
						<c:if test="${canRemove == true}">
							<form method="POST" action="/YasuiBasic/RemoveItemComplete">
								<input type="submit" name="remove" value="削除する" />
							</form>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>
			<c:import url="/WEB-INF/jsp/common/footer.jsp" />
		</c:otherwise>
	</c:choose>
</body>
</html>