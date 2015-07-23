<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>YunSearch</title>
<style type="text/css">
body {
	width: 800px;
	margin: 0 auto;
	padding-top: 30px;
	text-align: center;
	background-color: #fff;
}

#keyword {
	margin-top: 30px;
	width: 300px;
}

#title {
	color: white font-size:  48pt;
}

#text {
	
}
</style>
</head>
<h2 align="center">YunYun Search</h2>
<body text="black" link="green">
	<div>

		<form action="search" method="POST">
			<div>
				<input id="keyword" type="text" name="search" size="80"
					style="width: 440px; height: 30px;" /> <input width="40%"
					id="submit" type="submit" value="Yunsearch" style="height: 30px;" />
				<input type="hidden" value="1" name="pageno" />
			</div>
		</form>
	</div>

</body>
</html>