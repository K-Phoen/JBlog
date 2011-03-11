<%@page import="models.SessionModel"%>
<%@page import="libs.form.Form"%>
<%@page import="libs.BBCode"%>
<%@page import="libs.HTML"%>
<%@page import="metier.Comment"%>
<%@page import="metier.Article"%>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

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
        <h2 class="post_title">
            <a href="" rel="bookmark"><%= HTML.escape(article.getTitle()) %></a>
            <%
            if((Boolean) request.getAttribute("IS_LOGGED_IN")) {
            %>
            <a href="./admin/articles/edit/<%= article.getId() %>/"><img src="images/pencil.png" alt="Editer" /></a>
            <a href="./admin/articles/delete/<%= article.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer cet article ?');">
                <img src="images/delete.png" alt="Supprimer" title="Supprimer" />
            </a>
            <%
            }
            %>
        </h2>
        
        <div class="post_subdetails">
            <span class="post_categories">
                Par <%= HTML.escape(article.getAuthor().getDisplayName()) %> dans
                <a href="./article/categorie/<%= HTML.escape(article.getCategory().getSlug()) %>"><%= HTML.escape(article.getCategory().getTitle()) %></a>
            </span>
        </div>
        
        <div class="entry">
            <%= article.getContent() %>
        </div>
        
        <p class="postmetadata">
            Tags: tata, tutu
        </p>
        
        <p class="postmetadata alt">
            <small>
                Posté le <%=  article.dateToString("dd-MM-yyyy") %> par <%= article.getAuthor().getDisplayName() %>
            </small>
        </p>           
    </div>
</div>

<h2>Commentaires</h2>

<div class="comments_content">
    <ol class="commentlist">
        <%
        for(Comment c : article.getComments()) {
        %>
        <li class="comment even thread-even depth-1" id="comment-1"> 
            <div id="div-comment-1" class="comment-body"> 
                <div class="comment-author vcard"> 
                    <img alt="" src="http://0.gravatar.com/avatar/ad516503a11cd5ca435acc9bb6523536?s=32" class="avatar avatar-32 photo avatar-default" height="32" width="32" />
                    Le <%=  c.dateToString("dd-MM-yyyy") %>,
                    <cite class="fn">    
                        <a href="mailto:<%= c.getMail() %>" rel="external nofollow" class="url"><%= c.getAuthor() %></a>
                    </cite>
                    <span class="says">dit :</span>
                </div>

                <p>
                    <%= BBCode.parse(HTML.escape(c.getContent())) %>
                </p>
            </div>
        </li>
        <%
        }
        %>
    </ol>
</div>
    
    
<h2>Réagir</h2>
<script type="text/javascript">
function insertTag(startTag, endTag, textareaId) {
    var field  = document.getElementById(textareaId); 
    var scroll = field.scrollTop;
    field.focus();

    if (window.ActiveXObject) { // C'est IE
        var textRange = document.selection.createRange();            
        var currentSelection = textRange.text;

        textRange.text = startTag + currentSelection + endTag;
        textRange.moveStart("character", -endTag.length - currentSelection.length);
        textRange.moveEnd("character", -endTag.length);
        textRange.select();     
    } else { // Ce n'est pas IE
        var startSelection = field.value.substring(0, field.selectionStart);
        var currentSelection = field.value.substring(field.selectionStart, field.selectionEnd);
        var endSelection = field.value.substring(field.selectionEnd);

        field.value = startSelection + startTag + currentSelection + endTag + endSelection;
        field.focus();
        field.setSelectionRange(startSelection.length + startTag.length, startSelection.length + startTag.length + currentSelection.length);
    }

    field.scrollTop = scroll; // et on redéfinit le scroll.
}    
</script>

<%@include file="jspf/erreurs.jspf" %>

<form method="post" action="">
    <%
    if(!((Boolean) request.getAttribute("IS_LOGGED_IN"))) {
    %>
	<p> 
		<label for=nom>Nom : </label> 
		<%= form.field("nom") %>
	</p> 
	<p> 
		<label for=mail>Mail : </label> 
        <%= form.field("mail") %>
	</p> 
    <%
    }
    %>
	<p> 
		<label for=comment>Commentaire : </label> 
        
        <br />
        <img src="images/text_bold.png" onclick="insertTag('[b]', '[/b]', 'comment')" title="Mettre en gras" alt="Gras" />
        <img src="images/text_italic.png" onclick="insertTag('[i]', '[/i]', 'comment')" title="Mettre en italique" alt="Italique" />
        <img src="images/text_underline.png" onclick="insertTag('[u]', '[/u]', 'comment')" title="Souligner" alt="Souligné" />
        <br />
        
		<%= form.field("comment") %>
	</p> 
	<p> 
		<input id="Envoyer" name="Envoyer" value="Envoyer" type="submit"  /> 
	</p> 

</form>

<%@include file="jspf/footer.jspf" %>