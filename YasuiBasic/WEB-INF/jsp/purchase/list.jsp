<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/jsp/common/tdk.jsp" />
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<c:import url="/WEB-INF/jsp/common/navmenu.jsp" />
	<c:if test="${not empty errormessage}">
		<c:forEach var="message" items="${errormessage}"
			varStatus="statusError">
			<span class="errormsg">(エラー)：${message}</span>
			<br />
		</c:forEach>
	</c:if>
	<h2>商品一覧</h2>
	<form method="POST" action="PurchaseConfirm">
		<table class="itemlist">
			<tr>
				<th>商品番号</th>
				<th>商品名</th>
				<th>商品画像</th>
				<th>サイズ</th>
				<th>価格</th>
				<th>在庫</th>
				<th>注文数</th>
			</tr>
			<c:forEach var="item" items="${items}" varStatus="status">
				<tr>
					<td><c:out value="${item.itemId}" /></td>
					<td><c:out value="${item.itemName}" /></td>
					<td><a href=<c:out value="${item.imageUrl}"/>>商品画像</a></td>
					<td><c:out value="${item.itemSize}" /></td>
					<td><fmt:formatNumber type="currency" groupingUsed="true" minFractionDigits="0"
							currencySymbol="\\" value="${item.price}" /></td>

					<!-- 注文数量の処理 -->
					<c:choose>
						<%--stockの数が0以下またはstockの数をorderが上回る場合 --%>
						<c:when test="${item.stock <=0}">
							<td>なし</td>
							<td>現在注文できません</td>
						</c:when>
						<c:otherwise>
							<td><c:out value="${item.stock}" /></td>
							<td><input type="number" name="${fn:escapeXml(item.itemId)}" size="25" maxlength="8"
								min="0" max="${item.stock}" style="text-align: right" value="${fn:escapeXml(item.order)}" /></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</table>
		<input type="submit" value="確認" />
	</form>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
	<c:remove var="items"/>
</body>
</html>