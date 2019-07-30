<html>
<%@page language="java" contentType="text/html;" pageEncoding="utf-8" isELIgnored="false"  %>
<body>
<h2>Hello World!</h2>
<form method="post" action="${pageContext.request.contextPath}/manage/product/upload.do" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>
</body>
</html>
