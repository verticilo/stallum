<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'FORNECEDORES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3><fmt:message key="itensMenu.FORNECEDORES" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/fornecedores/novo"/>">
					<img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" />
				</a>
			</div>
			
			<form class="filtro" action="<c:url value="/fornecedores/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoFornecedor.${ordenacao}" /></option>
						</c:forEach>
					</select>
					
					<c:if test="${sessaoUsuario.administrador}">
						<input type="checkbox" name="filtro.removidos" <c:if test="${filtro.removidos}">checked="checked"</c:if> /> <fmt:message key="filtroFornecedor.removidos" />
					</c:if>
				
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
			<div style="width: 220px;"><fmt:message key="fornecedor.razaoSocial" /></div>
			<div style="width: 150px;"><fmt:message key="fornecedor.nomeCurto" /></div>
			<div style="width: 90px;"><fmt:message key="listarFornecedores.telefone" /></div>
			<div style="width: 180px;"><fmt:message key="fornecedor.email" /></div>
		</div>
		
		<c:forEach items="${listaFornecedores}" var="fornecedor">
			<div class="linha">
				<div style="width: 220px;"><a href="<c:url value="/fornecedores/${fornecedor.id}/editar"/>">${fornecedor.razaoSocial}</a></div>
				<div style="width: 150px;">${fornecedor.nomeCurto}</div>
				<div style="width: 90px;">${fornecedor.ddd1} ${fornecedor.telefone1}</div>
				<div style="width: 180px; text-transform: lowercase;">${fornecedor.email}</div>
				<div style="width: auto;">
					<a href="<c:url value="/fornecedores/${fornecedor.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" width="16" height="16" title="<fmt:message key="editar"/>" /></a>
					<c:if test="${sessaoUsuario.administrador and not fornecedor.ativo}">
						<a href="<c:url value="/fornecedores/${fornecedor.id}/editar"/>"><img src="<c:url value="/images/remover.png"/>" width="16" height="16" title="<fmt:message key="recuperar"/>" /></a>
					</c:if>
				</div>
			</div>		
		</c:forEach>
		
	</div>
    
</div>