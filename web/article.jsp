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

<div class="comments_content">
    <ol class="commentlist">
        <li class="comment even thread-even depth-1" id="comment-1"> 
            <div id="div-comment-1" class="comment-body"> 
                <div class="comment-author vcard"> 
                    <img alt="" src="http://0.gravatar.com/avatar/ad516503a11cd5ca435acc9bb6523536?s=32" class="avatar avatar-32 photo avatar-default" height="32" width="32" />
                    <cite class="fn">
                        <a href="http://url.com" rel="external nofollow" class="url">Monsieur WordPress</a>,
                        le 1 mars 2011 à 11h12
                    </cite>
                    <span class="says">dit :</span>
                </div>

                <p>
                    Contenu du commentaire.
                </p>
            </div>
        </li>
    </ol>
</div>
    
    
<h2>Réagir</h2>

<%@include file="jspf/form.jspf" %>

<%@include file="jspf/footer.jspf" %>