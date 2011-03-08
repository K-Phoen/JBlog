<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="jspf/header.jspf" %>

<h2>Redirection en cours ...</h2>

<p>
    <%= request.getAttribute("redir_msg") %>
</p>

<p>
    <a href="<%= request.getAttribute("redir_url") %>">Ne pas attendre</a>
</p>

<%@include file="jspf/footer.jspf" %>