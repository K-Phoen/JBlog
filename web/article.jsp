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
            <%= request.getAttribute("form") %>
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

<%@include file="jspf/footer.jspf" %>