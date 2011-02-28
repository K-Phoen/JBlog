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
        <span class="day">M</span><br />
        <span class="month">d</span><br />
        <span class="year">Y</span>
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
                Posté le 10/10/10 par moi
            </small>
        </p>           
    </div>
</div>

<%@include file="jspf/footer.jspf" %>