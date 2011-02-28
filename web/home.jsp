<%@page import="java.util.ArrayList"%>
<%@page import="metier.Article"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="jspf/header.jspf" %>

<%
List<Article> list = (List<Article>) request.getAttribute("elems");
if(list == null)
    list = new ArrayList<Article>();

for(Article a : list) {
%>
<div id="post-<%= a.getId() %>">
    <div class="date">
        <span class="day"><%= a.dateToString("dd") %></span><br />
        <span class="month"><%= a.dateToString("MM") %></span><br />
        <span class="year"><%= a.dateToString("yy") %></span>
    </div>

    <div class="post_content_right">
        <h2 class="post_title">
            <a href="./article/<%= a.getUrl() %>" rel="bookmark"><%= a.getTitle() %></a>
        </h2>

        <div class="post_subdetails">
            <span class="post_categories">Dans Général</span>
            <span class="comments_box"><%= a.getNbComs() %> commentaires</span>
        </div>

        <div class="entry">
            <%= a.getContent() %>
        </div>

        <!--        <p class="postmetadata">
            Tags: toto, tata
        </p>-->
    </div>
</div>
<%
 }
%>

<div class="navigation">
    <span class="alignleft">Older</span>
    <span class="alignright">Newer</span>
</div>
        
<%@include file="jspf/footer.jspf" %>