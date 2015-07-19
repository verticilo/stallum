<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CONTAS_PAGAR'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3><fmt:message key="listarContasPagar.titulo" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/contasPagar/nova"/>"><img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" /></a>
			</div>
			
			<form class="filtro" action="<c:url value="/contasPagar/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoContaPagar.${ordenacao}" /></option>
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
		
		<form class="lista" action="<c:url value="/contasPagar/pagamento" />" method="post">
	
		<div class="cabecalho">
			<div style="width: 80px;"><fmt:message key="contaPagar.data" /></div>
			<div style="width: 140px;"><fmt:message key="contaPagar.origem" /></div>
			<div style="width: 140px;"><fmt:message key="contaPagar.fornecedor" /></div>
			<div style="width: 80px; text-align: right;"><fmt:message key="contaPagar.valor" /></div>
			<div style="width: 80px;"><fmt:message key="contaPagar.vencto" /></div>
			<div style="width: 80px;"><fmt:message key="contaPagar.status" /></div>
		</div>
		
		<c:forEach items="${contasPagar}" var="contaPagar" varStatus="idx">
			<input type="hidden" name="contasPagar[${idx.count}].id" value="${contaPagar.id}" />
			<input type="hidden" name="contasPagar[${idx.count}].versao" value="${contaPagar.versao}" />
			<div class="linha">
				<div style="width: 80px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.data}" /></div>
				<div style="width: 140px;">${contaPagar.origem.nome}</div>
				<div style="width: 140px;">${contaPagar.fornecedor.nomeCurto}</div>
				<div style="width: 80px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${contaPagar.valor}" minFractionDigits="2" /></div>
				<div style="width: 80px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.vencto}" /></div>
				<div style="width: 80px;"><fmt:message key="statusConta.${contaPagar.status}" /></div>
				<div class="botoes" style="width: auto;">
					<a href="<c:url value="/contasPagar/${contaPagar.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" title="<fmt:message key="editar"/>" /></a>
					<c:if test="${contaPagar.status == 'ABERTA'}">					
						<button class="imgBtn" type="submit" name="idConta" title="<fmt:message key="formContaPagar.pagar" />" value="${contaPagar.id}" ><img src="<c:url value="/images/liquidar.png"/>" /></button>
						<input type="checkbox" name="contasPagar[${idx.count}].marcada" <c:if test="${contaPagar.marcada}">checked="checked"</c:if> />			
					</c:if>
				</div>
			</div>		
		</c:forEach>
		
		</form>
		
	</div>
    
</div>