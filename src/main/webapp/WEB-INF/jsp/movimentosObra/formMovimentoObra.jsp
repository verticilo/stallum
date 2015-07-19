<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'OBRAS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formMovimentoObra" class="formulario" action="<c:url value="/movimentosObra"/>" method="post">

    <input type="hidden" name="movimentoObra.id" value="${movimentoObra.id}" />
    <input type="hidden" name="movimentoObra.versao" value="${movimentoObra.versao}" />
    <input type="hidden" name="movimentoObra.obra.id" value="${movimentoObra.obra.id}" />
    <input type="hidden" name="movimentoObra.obra.versao" value="${movimentoObra.obra.versao}" />
    
	<h2><fmt:message key="formMovimentoObra.titulo" /></h2>
	
	<div>
		<div class="coluna">
			<fmt:message key="movimentoObra.obra" /><br/>
			<input type="text" name="movimentoObra.obra.nome" value="${movimentoObra.obra.nome}" size="40" disabled="disabled" />
		</div>		
		<div class="coluna obrigatorio">
			<fmt:message key="movimentoObra.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="movimentoObra.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${movimentoObra.data}" />" size="40" />
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="movimentoObra.novoStatus" /> <span>*</span><br/>
			<select id="funcao" name="movimentoObra.novoStatus">
				<option value=""></option>
				<c:forEach items="${listaStatus}" var="status">
					<option value="${status}" <c:if test="${status == movimentoObra.novoStatus}" >selected="selected"</c:if> ><fmt:message key="statusObra.${status}" /></option>
				</c:forEach>
			</select>		
		</div>
		<div class="coluna">
		</div>
	</div>	
	<div>
		<div class="linha">
			<fmt:message key="movimentoObra.motivo" /><br/>
			<textarea name="movimentoObra.motivo" rows="5" cols="40">${movimentoObra.motivo}</textarea>
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty movimentoObra.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/obras/${movimentoObra.obra.id}/movimentosObra/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>