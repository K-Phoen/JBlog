<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="metier.Article"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="../../jspf/header.jspf" %>

<h2>Gestion des articles</h2>

<%
List<Article> list = (List<Article>) request.getAttribute("elems");

if(list.isEmpty()) {
%>
<p>
    Aucun article à afficher.
</p>
<%
} else {
%>
<table>
	<thead>
		<tr>
            <th>Titre</th>
            <th>Catégorie</th>
            <th>Auteur</th>
			<th>Date</th>
			<th>Action</th>
		</tr>
	</thead>

	<tfoot>
		<tr>
			<td colspan="5">
				<%
                if(request.getAttribute("PREV_PAGE") != null) {
                %>
                    <span class="alignleft"><a href="./admin/articles/page/<%= request.getAttribute("PREV_PAGE") %>/">Newer</a></span>
                <%
                }
                %>

                <%
                if(request.getAttribute("NEXT_PAGE") != null) {
                %>
                    <span class="alignright"><a href="./admin/articles/page/<%= request.getAttribute("NEXT_PAGE") %>/">Older</a></span>
                <%
                }
                %>
			</td>
		</tr>
	</tfoot>

	<tbody>
        <%
        for(Article a : list) {
        %>
		<tr>
            <td><a href="./article/<%= HTML.escape(a.getSlug()) %>"><%= HTML.escape(a.getTitle()) %></a></td>
            <td><a href="./article/categorie/<%= HTML.escape(a.getCategory().getSlug()) %>"><%= HTML.escape(a.getCategory().getTitle()) %></a></td>
			<td><%= HTML.escape(a.getAuthor().getDisplayName()) %></td>
            <td><%= a.dateToString("dd-MM-yyyy") %></td>
			<td>
                <a href="./admin/articles/edit/<%= a.getId() %>/">
                    <img src="images/pencil.png" alt="Editer" title="Editer" />
                </a>
                <a href="./admin/articles/delete/<%= a.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer cet article ?');">
                    <img src="images/delete.png" alt="Supprimer" title="Supprimer" />
                </a>
            </td>
		</tr>
		<%
        }
        %>
	</tbody>
</table>
<%
}
%>

<h2>Accès rapide</h2>

<ul>
    <li><a href="./admin/articles/new/">Rédiger un article</a></li>
    <li><a href="./admin/categories/edit/">Créer une nouvelle catégorie</a></li>
    <li><a href="./admin/categories/">Gérer les catégories</a></li>
</ul>

<%@include file="../../jspf/footer.jspf" %>