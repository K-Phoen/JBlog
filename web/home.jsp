<%@page import="java.util.ArrayList"%>
<%@page import="metier.Article"%>
<%@page import="java.util.List"%>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="jspf/header.jspf" %>

<%
List<Article> list = (List<Article>) request.getAttribute("elems");
if(list == null)
    list = new ArrayList<Article>();

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
                <a href="./article/<%= a.getUrl() %>" rel="bookmark"><%= a.getTitle() %></a>
            </h2>

            <div class="post_subdetails">
                <span class="post_categories">Par <%= a.getAuthor().getDisplayName() %> dans <%= a.getCategory().getTitle() %></span>
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
        <span class="alignleft"><a href="./page/1">Older</a></span>
        <span class="alignright"><a href="./page/2">Newer</a></span>
    </div>
<%
}
%>
        
<%@include file="jspf/footer.jspf" %>