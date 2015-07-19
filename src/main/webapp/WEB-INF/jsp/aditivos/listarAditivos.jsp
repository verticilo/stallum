<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'ADITIVOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3><fmt:message key="listarAditivos.titulo" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/aditivos/novo"/>"><img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" /></a>
			</div>
			
			<form class="filtro" action="<c:url value="/aditivos/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
				<input type="hidden" name="filtro.obra.id" 	value="${filtro.obra.id}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoAditivo.${ordenacao}" /></option>
						</c:forEach>
					</select>
					
				</div>
	
				<div>
				
					<button type="submit" id="filtrar"><img src="<c:url value="/images/pesquisar.png" />" title="<fmt:message key="pesquisar" />" /></button>
					<button type="button" onclick="paginar(5)"><img src="<c:url value="/images/cancelar.png" />" title="<fmt:message key="limpar" />" /></button>
				
				</div>
				
				<div class="paginacao">
					<!-- Botao 'primeira pagina' -->
					<button type="button" onclick="paginar(1)" <c:if test="${filtro.paginacao.paginaAtual <= 1}">disabled="disabled"</c:if> ><img src="<c:url value="/images/primeiro.png" />" title="<fmt:message key="primeiro" />" /></button>	
					
					<!-- Botao 'pagina anterior' -->
					<button type="button" onclick="paginar(2)" <c:if test="${filtro.paginacao.paginaAtual <= 1}">disabled="disabled"</c:if> ><img src="<c:url value="/images/anterior.png" />" title="<fmt:message key="anterior" />" /></button>
					
					<input type="text" style="text-align: center;" name="filtro.paginacao.paginaAtual" value="${filtro.paginacao.paginaAtual }" size="1" <c:if test="${filtro.paginacao.totalPaginas == 1}">disabled="disabled"</c:if> /> 
					<fmt:message key="paginacao.de" /> 
					${filtro.paginacao.totalPaginas }
					
					<!-- Botao 'proxima pagina' -->
					<button type="button" onclick="paginar(3)" <c:if test="${filtro.paginacao.paginaAtual >= filtro.paginacao.totalPaginas}">disabled="disabled"</c:if> ><img src="<c:url value="/images/proximo.png" />" title="<fmt:message key="proximo" />" /></button>	
					
					<!-- Botao 'ultima pagina' -->
					<button type="button" onclick="paginar(4)" <c:if test="${filtro.paginacao.paginaAtual >= filtro.paginacao.totalPaginas}">disabled="disabled"</c:if> ><img src="<c:url value="/images/ultimo.png" />" title="<fmt:message key="ultimo" />" /></button>
				</div>
				
			</form>
						
		</div>
	
		<div class="cabecalho">
			<div style="width: 200px;"><fmt:message key="aditivo.obra" /></div>
			<div style="width: 70px;"><fmt:message key="aditivo.numero" /></div>
			<div style="width: 200px;"><fmt:message key="aditivo.descricao" /></div>
			<div style="width: 70px;"><fmt:message key="aditivo.data" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="aditivo.valor" /></div>
		</div>
		
		<c:forEach items="${listaAditivos}" var="aditivo">
			<div class="linha">
				<div style="width: 200px;"><a href="<c:url value="/obras/${aditivo.obra.id}/editar"/>">${aditivo.obra.nome}</a></div>
				<div style="width: 70px;"><a href="<c:url value="/aditivos/${aditivo.id}/editar"/>">${aditivo.numero}</a></div>
				<div style="width: 200px;">${aditivo.descricao}</div>
				<div style="width: 70px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${aditivo.data}" /></div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${aditivo.valor}" minFractionDigits="2" /></div>
				<div style="width: auto;">
					<a href="<c:url value="/aditivos/${aditivo.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" width="16" height="16" title="<fmt:message key="editar"/>" /></a>					
				</div>
			</div>		
		</c:forEach>
		
	</div>
    
</div>