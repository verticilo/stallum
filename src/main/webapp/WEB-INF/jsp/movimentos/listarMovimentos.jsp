<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'FUNCIONARIOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3>${filtro.funcionario.nomeCurto} - <fmt:message key="listarMovimentos.titulo" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a id="lnkNovo" href="<c:url value="/funcionarios/${filtro.funcionario.id}/movimentos/novo"/>">
					<img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" />
				</a>
			</div>
			
			<form class="filtro" action="<c:url value="/funcionarios/${filtro.funcionario.id}/movimentos/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoMovimento.${ordenacao}" /></option>
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
			<div style="width: 150px;"><fmt:message key="movimento.data" /></div>
			<div style="width: 120px;"><fmt:message key="movimento.novaFuncao" /></div>
			<div style="width: 120px;"><fmt:message key="movimento.novoStatus" /></div>
			<div style="width: 120px; text-align: right;"><fmt:message key="movimento.bonus" /></div>
		</div>
		
		<c:forEach items="${listaMovimentos}" var="movimento">
			<div class="linha">
				<div style="width: 150px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${movimento.data}" /></div>
				<div style="width: 120px;">${movimento.novaFuncao.nome}</div>
				<div style="width: 120px;"><fmt:message key="statusFuncionario.${movimento.novoStatus}" /></div>
				<div style="width: 120px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${movimento.bonus}" minFractionDigits="2" /></div>
				<div style="width: auto;">
					<a href="<c:url value="/movimentos/${movimento.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" width="16" height="16" title="<fmt:message key="editar"/>" /></a>
				</div>
			</div>		
		</c:forEach>		
		
	</div>
    
</div>

<div id="confirmarModal" title="<fmt:message key="movimento.remover" />">
  <p><fmt:message key="movimento.remover.msg" /></p>
</div>