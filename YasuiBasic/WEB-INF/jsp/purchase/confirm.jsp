<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/jsp/common/tdk.jsp" />
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<c:import url="/WEB-INF/jsp/common/navmenu.jsp" />
	<c:if test="${canOrder == false}">
		<h2>注文情報が正しくありません</h2>
		<p>戻るボタンを押して、商品一覧から発注しなおしてください</p>
		<c:if test="${not empty errormessage}">
			<c:forEach var="message" items="${errormessage}"
				varStatus="statusError">
				<span class="errormsg">(Error)：${message}</span>
				<br />
			</c:forEach>
			<c:remove var="errormessage" />
			<%--表示が終わったエラーメッセージはセッションから削除する --%>
		</c:if>

	</c:if>
	<c:if test="${canOrder == true}">
		<h2>下記の内容で発注しますか？</h2>
		<table class="itemlist">
			<tr>
				<th>商品番号</th>
				<th>商品名</th>
				<th>商品画像</th>
				<th>サイズ</th>
				<th>価格</th>
				<th>在庫</th>
				<th>注文数</th>
				<th>小計</th>
			</tr>
			<c:set var="sum" value='<%=0%>' />
			<c:forEach var="orderitem" items="${items}" varStatus="status">
				<tr>
					<td><c:out value="${orderitem.itemId}" /></td>
					<td><c:out value="${orderitem.itemName}" /></td>
					<td><a href=<c:out value="${orderitem.imageUrl}" />>商品画像</a></td>
					<td><c:out value="${orderitem.itemSize}" /></td>
					<td><fmt:formatNumber type="currency" groupingUsed="true"
							currencySymbol="\\" 
							value="${orderitem.price}" minFractionDigits="0" /></td>
					<td><c:out value="${orderitem.stock}" /></td>
					<td><c:out value="${orderitem.order}" /></td>
					<td><fmt:formatNumber
							value="${orderitem.order * orderitem.price}" type="CURRENCY"
							groupingUsed="true" currencySymbol="\\" minFractionDigits="0" />
					</td>
					<c:set var="sum" value="${sum+(orderitem.order * orderitem.price)}" />
				</tr>
			</c:forEach>
			<tr>
				<td colspan="7">合計</td>
				<td><fmt:formatNumber value="${sum}" type="CURRENCY"
						groupingUsed="true" currencySymbol="\\" minFractionDigits="0" />
				</td>
			</tr>
		</table>
	</c:if>
	<div style="display:inline-flex">
	<form method="GET" action="ListItem">
		<input type="submit" name="goindex" value="戻る" style="width: 100px;" />
	</form>
	<c:if test="${canOrder == true}">
		<form method="POST" action="PurchaseComplete">
			<input type="submit" name="order" value="発注する" style="width: 100px;" />
		</form>
	</c:if>
	</form>
	</div>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>