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
			</c:if>
			<h2>変更したい在庫数量を指定してください</h2>
			<form method="POST" action="ChangeStockConfirm">
				<table class="itemlist">
					<tr bgcolor="#00ccff">
						<th>商品番号</th>
						<th>商品名</th>
						<th>商品画像</th>
						<th>サイズ</th>
						<th>価格</th>
						<th>現在の在庫</th>
						<th>変更後の在庫</th>
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
									minFractionDigits="0" currencySymbol="\\" value="${item.price}" /></td>
							<td><fmt:formatNumber type="number" groupingUsed="true"
									minFractionDigits="0" value="${item.stock}" /></td>
							<td><input type="number" name="${fn:escapeXml(item.itemId)}" size="25" maxlength="8"
								min="0" max="99999999" style="text-align: right" value="${fn:escapeXml(item.stock)}" /></td>
						</tr>
					</c:forEach>
				</table>
				<input type="submit" value="確認" />
			</form>
		</c:otherwise>
	</c:choose>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>