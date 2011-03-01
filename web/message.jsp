<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="jspf/header.jspf" %>

<h2>Redirection en cours ...</h2>

<p>
    <%= request.getAttribute("redir_msg") %>
</p>

<%@include file="jspf/footer.jspf" %>