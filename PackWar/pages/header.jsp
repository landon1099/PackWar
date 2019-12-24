<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<nav class="navbar navbar-inverse bg-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <%--<a class="navbar-brand" href="#">Nav | </a>--%>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <li><a href="<%=path%>/">Pack War</a></li>
                <%--<li><a href="<%=path%>/svnStat?method=index">SVN STAT</a></li>--%>
            </ul>
        </div>
    </div>
</nav>
