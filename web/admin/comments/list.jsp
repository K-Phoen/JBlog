<%@page import="metier.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="../../jspf/header.jspf" %>

<h2>Gestion des catégories</h2>

<%
List<Comment> list = (List<Comment>) request.getAttribute("elems");

if(list.isEmpty()) {
%>
<p>
    Aucune catégorie à afficher.
</p>
<%
} else {
%>
<table>
	<thead>
		<tr>
            <th>Autheur</th>
            <th>Mail</th>
            <th>Date</th>
            <th>Article</th>
            <th>Valide</th>
			<th>Action</th>
		</tr>
	</thead>

	<tbody>
        <%
        for(Comment c : list) {
        %>
		<tr>
            <td><%= HTML.escape(c.getAuthor()) %></td>
            <td><%= HTML.escape(c.getMail()) %></td>
            <td><%= c.dateToString("dd-MM-yyyy") %></td>
            <td><%= HTML.escape(c.getArticleTitle()) %></td>
            <td><%= (c.isValid() ? "Oui" : "Non") %></td>
			<td>
                <a href="./admin/comments/edit/<%= c.getId() %>/">
                    <img src="images/pencil.png" alt="Editer" title="Editer" />
                </a>
                <a href="./admin/comments/delete/<%= c.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer ce commentaire ? ?');">
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
    <li><a href="./admin/articles/">Gérer les articles</a></li>
</ul>

<%@include file="../../jspf/footer.jspf" %>