<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CONTAS_RECEBER'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3><fmt:message key="listarContasReceber.titulo" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/contasReceber/nova"/>"><img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" /></a>
			</div>
			
			<form class="filtro" action="<c:url value="/contasReceber/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoContaReceber.${ordenacao}" /></option>
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
		
		<form class="lista" action="<c:url value="/contasReceber/recebimento" />" method="post">
	
		<div class="cabecalho">
			<div style="width: 80px;"><fmt:message key="contaReceber.data" /></div>
			<div style="width: 140px;"><fmt:message key="contaReceber.origem" /></div>
			<div style="width: 140px;"><fmt:message key="contaReceber.cliente" /></div>
			<div style="width: 80px; text-align: right;"><fmt:message key="contaReceber.valor" /></div>
			<div style="width: 80px;"><fmt:message key="contaReceber.vencto" /></div>
			<div style="width: 80px;"><fmt:message key="contaReceber.status" /></div>
		</div>
		
		<c:forEach items="${contasReceber}" var="contaReceber" varStatus="idx">
			<input type="hidden" name="contasReceber[${idx.count}].id" value="${contaReceber.id}" />
			<input type="hidden" name="contasReceber[${idx.count}].versao" value="${contaReceber.versao}" />
			<div class="linha">
				<div style="width: 80px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.data}" /></div>
				<div style="width: 140px;">${contaReceber.origem.nome}</div>
				<div style="width: 140px;">${contaReceber.cliente.nomeCurto}</div>
				<div style="width: 80px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${contaReceber.valor}" minFractionDigits="2" /></div>
				<div style="width: 80px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.vencto}" /></div>
				<div style="width: 80px;"><fmt:message key="statusConta.${contaReceber.status}" /></div>
				<div class="botoes" style="width: auto;">
					<a href="<c:url value="/contasReceber/${contaReceber.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" title="<fmt:message key="editar"/>" /></a>
					<c:if test="${contaReceber.status == 'ABERTA' and not empty contaReceber.vencto}">					
						<button class="imgBtn" type="submit" name="idConta" title="<fmt:message key="formContaReceber.receber" />" value="${contaReceber.id}" ><img src="<c:url value="/images/liquidar.png"/>" /></button>
						<input type="checkbox" name="contasReceber[${idx.count}].marcada" <c:if test="${contaReceber.marcada}">checked="checked"</c:if> />			
					</c:if>
				</div>
			</div>		
		</c:forEach>
		
		</form>
		
	</div>
    
</div>