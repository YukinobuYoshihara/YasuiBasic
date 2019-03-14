<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/common/include.jsp" />
<title>エラーが発生しました</title>
<style>
body {
	background: #f9fee8;
	margin: 0;
	padding: 20px;
	text-align: center;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	color: #666666;
}

.error_page {
	width: 800px;
	padding: 50px;
	margin: auto;
}

.error_page h1 {
	margin: 20px 0 0;
}

.error_page p {
	margin: 10px 0;
	padding: 0;
}

a {
	color: #9caa6d;
	text-decoration: none;
}

a:hover {
	color: #9caa6d;
	text-decoration: underline;
}
</style>
</head>
<body class="login">
	<div class="error_page">
		<img alt="sorry" src="http://localhost:8080/YasuiBasic/img/sad.gif">
		<h1>We're sorry...</h1>
		<p>
			不正なメソッドでページがアクセスされました。 <br /> <a
				href="http://localhost:8080/YasuiBasic/">トップページ</a>から探していただくか、アクセスしたURLを<a
				href="mailto:foo@bar.com"> YASUI家具ヘルプデスク </a> までご連絡ください。
		</p>
		<hr />
		<p>
			<a href="http://localhost:8080/YasuiBasic/"> サイトのトップページに戻る </a>
		</p>
	</div>

</body>
</html>
