<%@page import="java.util.ArrayList"%>
<%@page import="metier.Article"%>
<%@page import="java.util.List"%>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="jspf/header.jspf" %>

<%
if(request.getAttribute("IS_LOGGED_IN") != null) {
%>
<p style="float:right">
    <a href="./admin/articles/new/"><img src="images/add.png" alt="" /> Nouvel article</a></span>
</p>
<div style="clear:both"></div>
<%
}
%>

<%
List<Article> list = (List<Article>) request.getAttribute("elems");

if(list.isEmpty()) {
%>
<p>
    Aucun article à afficher.
</p>
<%
} else {
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
                <a href="./article/<%= HTML.escape(a.getSlug()) %>" rel="bookmark"><%= HTML.escape(a.getTitle()) %></a>
                <%
                if((Boolean) request.getAttribute("IS_LOGGED_IN")) {
                %>
                <a href="./admin/articles/edit/<%= a.getId() %>/"><img src="images/pencil.png" alt="Editer" /></a>
                <a href="./admin/articles/delete/<%= a.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer cet article ?');">
                    <img src="images/delete.png" alt="Supprimer" title="Supprimer" />
                </a>
                <%
                }
                %>
            </h2>

            <div class="post_subdetails">
                <span class="post_categories">
                    Par <%= HTML.escape(a.getAuthor().getDisplayName()) %> dans
                    <a href="./article/categorie/<%= HTML.escape(a.getCategory().getSlug()) %>"><%= HTML.escape(a.getCategory().getTitle()) %></a></span>
                <span class="comments_box"><%= a.getNbComs() %> commentaires</span>
            </div>

            <div class="entry">
                <%= a.getContent() %>
            </div>

            <!--
            <p class="postmetadata">
                Tags: toto, tata
            </p>
            -->
        </div>
    </div>
    
    <div style="clear:both; margin-top:110px"></div>
    <%
    }
    %>
    
    <div class="navigation" style="clear:both; margin-top:120px">
        <%
        if(request.getAttribute("PREV_PAGE") != null) {
        %>
            <span class="alignleft"><a href="./page/<%= request.getAttribute("PREV_PAGE") %>">Newer</a></span>
        <%
        }
        %>
        
        <%
        if(request.getAttribute("NEXT_PAGE") != null) {
        %>
            <span class="alignright"><a href="./page/<%= request.getAttribute("NEXT_PAGE") %>">Older</a></span>
        <%
        }
        %>
    </div>
<%
}
%>
        
<%@include file="jspf/footer.jspf" %>