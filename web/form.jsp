<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="jspf/header.jspf" %>

<%
if(request.getAttribute("PAGE_TITLE") != null) {
%>
<h2><%= request.getAttribute("PAGE_TITLE") %></h2>
<%
}
%>
<%@include file="jspf/form.jspf" %>
<%@include file="jspf/footer.jspf" %>