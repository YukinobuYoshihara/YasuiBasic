<%@page import="jp.recruit.bean.ItemBean"%>
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
		<c:when test="${role != 'administrator'}"><%--フィルターと二段構え --%>
			<h2>管理者以外はこの画面にアクセスすることはできません。</h2>
			<form method="POST" action="Index">
				<input type="submit" name="return" value="ログイン画面に戻る" />
			</form>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${canAdd == false}">
					<c:if test="${not empty errormessage}">
						<c:forEach var="message" items="${errormessage}"
							varStatus="statusError">
							<span class="errormsg">(Error)：${message}</span>
							<br />
						</c:forEach>
					</c:if>
					<h2>追加する商品情報が正しくありません</h2>
					<p>戻るボタンを押して、入力しなおしてください</p>
				</c:when>
				<c:otherwise>
					<c:if test="${not empty sessionScope.errormessage}">
						<c:forEach var="message" items="${errormessage}"
							varStatus="statusError">
							<span class="errormsg">(Error)：${message}</span>
							<br />
						</c:forEach>
						<c:remove var="errormessage" />
						<%--表示が終わったエラーメッセージはセッションから削除する --%>
					</c:if>
					<c:if test="${canAdd == true}">
						<table class="itemlist">
							<tr bgcolor="#00ccff">
								<th>商品番号</th>
								<th>商品名</th>
								<th>商品画像</th>
								<th>サイズ</th>
								<th>価格</th>
								<th>在庫</th>
							</tr>
							<c:set var="bgcol" value="#FFffCC" />
							<tr bgcolor="${bgcol}">
								<td><c:out value="${newItem.itemId}" /></td>
								<td><c:out value="${newItem.itemName}" /></td>
								<td><a href=<c:out value="${newItem.imageUrl}" />>商品画像</a></td>
								<td><c:out value="${newItem.itemSize}" /></td>
								<td><fmt:formatNumber type="currency" groupingUsed="true"
										currencySymbol="\\" 
										value="${newItem.price}" minFractionDigits="0" /></td>
								<td><fmt:formatNumber type="number" groupingUsed="true"
										value="${newItem.stock}" minFractionDigits="0" /></td>
							</tr>
						</table>
					</c:if>
					<div id="goreturn">
					<form method="GET" action="AddItem">
						<input type="submit" name="goindex" value="戻る" />
					</form>
					<form method="POST" action="AddItemComplete">
						<c:if test="${canAdd == true}">
							<input type="submit" name="addItem" value="追加する" />
						</c:if>
					</form>
					</div>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<%--roleチェックのc:if --%>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>