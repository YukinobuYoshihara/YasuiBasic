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
				<c:when test="${canChange == false}">
					<h2>在庫変更ができませんでした</h2>
					<p>戻るボタンを押して、商品一覧から変更してください</p>
					<c:if test="${not empty sessionScope.errormessage}">
						<c:forEach var="message" items="${errormessage}"
							varStatus="statusError">
							<span class="errormsg">(Error)：${message}</span>
							<br />
						</c:forEach>
						<c:remove var="errormessage" />
						<%--表示が終わったエラーメッセージはセッションから削除する --%>
					</c:if>
					<c:set var="destination" value="/YasuiBasic/ChangeStock" />
					<form method="POST" action="${destination}">
						<input type="submit" name="goindex" value="戻る" />
					</form>
				</c:when>
				<c:when test="${canChange == true}">
					<h2>下記の内容で在庫を変更しますか？</h2>
					<table class="itemlist">
						<tr bgcolor="#00ccff">
							<th>商品番号</th>
							<th>商品名</th>
							<th>商品画像</th>
							<th>サイズ</th>
							<th>価格</th>
							<th>新在庫</th>
						</tr>
						<c:forEach var="item" items="${changeStock}" varStatus="status">
							<c:choose>
								<c:when test="${status.count%2==0}">
									<c:set var="bgcol" value="#FFffCC" />
								</c:when>
								<c:otherwise>
									<c:set var="bgcol" value="#EEeeAA" />
								</c:otherwise>
							</c:choose>
							<tr bgcolor="${bgcol}">
								<td><c:out value="${item.itemId}" /></td>
								<td><c:out value="${item.itemName}" /></td>
								<td><a href=<c:out value="${item.imageUrl}" />>商品画像</a></td>
								<td><c:out value="${item.itemSize}" /></td>
								<td><fmt:formatNumber type="currency" groupingUsed="true"
										currencySymbol="\\" value="${item.price}" 
										minFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" groupingUsed="true"
										value="${item.stock}" minFractionDigits="0" /></td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>

	<div id="goreturn">
		<form method="GET" action="/YasuiBasic/ChangeStock">
			<input type="submit" name="goindex" value="戻る" style="width: 100px;" />
		</form>
		<form method="POST" action="/YasuiBasic/ChangeStockComplete">
			<c:if test="${canChange == true}">
				<input type="submit" name="change" value="変更する" style="width: 100px;" />
			</c:if>
		</form>
	</div>

	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>