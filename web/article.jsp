<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="libs.form.fields.TextField"%>
<%@page import="libs.form.Form"%>
<%@page import="metier.Article"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="jspf/header.jspf" %>

<%
Article article = (Article) request.getAttribute("article");
%>

<div>
    <div class="date">
        <span class="day"><%= article.dateToString("dd") %></span><br />
        <span class="month"><%= article.dateToString("MM") %></span><br />
        <span class="year"><%= article.dateToString("yy") %></span>
    </div>
    
    <div class="post_content_right">
        <h2 class="post_title"><a href="" rel="bookmark"><%= article.getTitle() %></a></h2>
        
        <div class="post_subdetails">
            <span class="post_categories">Posté dans Général</span>
        </div>
        
        <div class="entry">
            <%= article.getContent() %>
        </div>
        
        <p class="postmetadata">
            Tags: tata, tutu
        </p>
        
        <p class="postmetadata alt">
            <small>
                Posté le <%=  article.dateToString("dd-MM-yyyy") %> par moi
            </small>
        </p>           
    </div>
</div>

<h2>Commentaires</h2>
<%
Form form = (Form) request.getAttribute("form");

if(!form.getErrors().isEmpty()) {
%>
<h3>Erreurs</h3>

<ul>
    <%
    Map<String, List<String>> errors = form.getErrors();
    for(String field : errors.keySet()) {
        for(String error : errors.get(field)) {
    %>
    <li><%= form.field(field).getLabel() %> : <%= error %></li>
    <%
        }
    }
    %>
</ul>
<%
}

out.print(form);
%>

<%@include file="jspf/footer.jspf" %>