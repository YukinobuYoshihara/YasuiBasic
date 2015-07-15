<!doctype html>
<html>
<head>
<c:import url="/WEB-INF/jsp/common/tdk.jsp" />
<c:import url="/WEB-INF/jsp/common/include.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/jsp/common/header.jsp" />
	<c:import url="/WEB-INF/jsp/common/navmenu.jsp" />
	<c:if test="${not empty sessionScope.errormessage}">
		<c:forEach var="message" items="${errormessage}"
			varStatus="statusError">
			<span class="errormsg">(Error)：${message}</span>
			<br />
		</c:forEach>
		<c:remove var="errormessage" />
		<%--表示が終わったエラーメッセージはセッションから削除する --%>
	</c:if>
	<c:choose>
		<c:when test="${role != 'administrator'}">
			<h2>管理者以外はこの画面にアクセスすることはできません。</h2>
			<form method="POST" action="ListItem">
				<input type="submit" name="return" value="商品一覧に戻る" />
			</form>
		</c:when>
		<c:otherwise>
			<script>
				jQuery(document).ready(function() {
					// binds form submission and fields to the validation engine
					jQuery("#formID").validationEngine();
				});
			</script>

			<form method="POST" action="AddItemConfirm" id="formID"
				class="formular">
				<fieldset>
					<legend>商品ID</legend>
					<input size=8 value="${nextId}"
						class="validate[required,custom[integer],max[99999]] text-input"
						type="text" name="itemid" id="itemid" />
					（整数5桁まで）商品IDの最大値に1加算した値が、初期状態では表示されています
				</fieldset>
				<fieldset>
					<legend>商品名</legend>
					<input size=52 class="validate[required,maxSize[50]] text-input"
						type="text" name="item_name" id="item_name" autofocus
						placeholder="商品名（色）" /> （最大50文字まで）商品名（色）の形式で入力してください
				</fieldset>
				<fieldset>
					<legend>商品画像URL</legend>
					<input size=52 value="http://localhost:8080/YasuiBasic/img/"
						class="validate[required,maxSize[50]] text-input" type="text"
						name="imgurl" id="imgurl"
						placeholder="http://localhost:8080/YasuiBasic/img/nnnnn.jpg" />
					（最大50文字まで）http://ホスト名:ポート番号/YasuiBasic/img/画像名
				</fieldset>
				<fieldset>
					<legend>商品サイズ</legend>
					<input size=52 class="validate[required,maxSize[50]] text-input"
						type="text" name="item_size" id="item_size" pattern="\d+x\d+x\d+"
						placeholder="150x75x85" /> （最大50文字まで）たてxよこx高さ 単位：cm （例：150x75x85）
				</fieldset>
				<fieldset>
					<legend>商品価格</legend>
					<input size=10
						class="validate[required,custom[integer],max[99999999]] text-input"
						type="number" name="price" id="price" placeholder="99999999" min="0" max="99999999" />
					（整数8桁まで）通貨フォーマットで記述する必要はありません
				</fieldset>
				<fieldset>
					<legend>入庫数量</legend>
					<input size=10
						class="validate[required,custom[integer],max[99999999]] text-input"
						type="number" name="stock" id="stock" placeholder="99999999" min="0" max="99999999" />
					（整数8桁まで）
				</fieldset>
				<div id="goreturn">
				<input type="submit" name="submit" value="確認" /> 
				<input type="reset" name="reset" value="リセット"/>
				</div>
			</form>
		</c:otherwise>
	</c:choose>
	<c:import url="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>