<%@ page isErrorPage="true"%>
<%@ page contentType="text/html; charset=UTF-8" session="false"%>
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
		<img alt="sorry" src="img/sad.gif">
		<h1>We're sorry...</h1>
		<p>
			現在の画面において、致命的なエラーが発生しました。 <br />
			しばらくたってもエラーが改善されない場合には、本画面のキャプチャを添付の上、 <a href="mailto:foo@bar.com">
				YASUI家具ヘルプデスク </a> までご連絡ください。
		</p>
		<hr />
		<p>
			<a href="http://localhost:8080/YasuiBasic/"> サイトのトップページに戻る </a>
		</p>
	</div>
	<c:if test="${initParam['mode.debug']=='true'}">
		<dl>
			<dt>例外情報（概要）</dt>
			<dd>${requestScope['javax.servlet.error.exception']}</dd>
			<dt>例外クラス</dt>
			<dd>${requestScope['javax.servlet.error.exception_type']}</dd>
			<dt>例外情報（メッセージ）</dt>
			<dd>${requestScope['javax.servlet.error.message']}</dd>
			<dt>例外が発生したURI</dt>
			<dd>${requestScope['javax.servlet.error.request_uri']}</dd>
			<dt>ステータスコード</dt>
			<dd>${requestScope['javax.servlet.error.status_code']}</dd>
			<dt>例外タイプ</dt>
			<dd>${requestScope['javax.servlet.error.exception_type']}</dd>
			<dt>サーブレット名</dt>
			<dd>${requestScope['javax.servlet.error.servlet_name']}</dd>
			<dt>スタックトレース</dt>
			<dd><%	exception.printStackTrace(new java.io.PrintWriter(out));%></dd>
		</dl>
	</c:if>
</body>
</html>
