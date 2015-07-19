<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'APONTAMENTOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

	<h3><fmt:message key="itensMenu.APONTAMENTOS" /> - <a href="<c:url value="/obras/${obra.id}/editar"/>">${obra.nome}</a></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/apontamentos/novo/"/>">
					<img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" />
				</a>
			</div>
			
			<form class="filtro" action="<c:url value="/apontamentos/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
			
				<div>
			
					<input class="wmark" title="<fmt:message key="palavraChave" />" type="text" name="filtro.palavraChave" value="${filtro.palavraChave }" />
					
					<select name="filtro.ordenacao">
						<c:forEach items="${filtro.listaOrdenacao}" var="ordenacao">
							<option value="${ordenacao}" <c:if test="${ordenacao == filtro.ordenacao}">selected="selected"</c:if> ><fmt:message key="ordenacaoApontamento.${ordenacao}" /></option>
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
			<div style="width: 200px;"><fmt:message key="ponto.funcionario" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="ponto.diasTrab" /></div>
			<div style="width: 50px; text-align: right;"><fmt:message key="ponto.abonos" /></div>
			<div style="width: 50px; text-align: right;"><fmt:message key="ponto.faltas" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="ponto.custoHoraNormal" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="ponto.custoHoraExtra" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="ponto.encargos" /></div>
			<div style="width: 70px; text-align: right;"><fmt:message key="ponto.extras" /></div>
		</div>
		
		<c:forEach items="${listaPontos}" var="ponto">
			<c:set var="ttHn" value="${ttHn + ponto.custoHoraNormal}" />
			<c:set var="ttHe" value="${ttHe + ponto.custoHoraExtra}" />
			<c:set var="ttEn" value="${ttEn + ponto.encargos}" />
			<c:set var="ttEx" value="${ttEx + ponto.extras}" />
			<div class="linha">
				<div style="width: 200px;">${ponto.funcionario.nomeCurto}</div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.diasTrab}" maxFractionDigits="0" /></div>
				<div style="width: 50px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.abonos}" maxFractionDigits="0" /></div>
				<div style="width: 50px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.faltas}" maxFractionDigits="0" /></div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.custoHoraNormal}" minFractionDigits="2" /></div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.custoHoraExtra}" minFractionDigits="2" /></div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.encargos}" minFractionDigits="2" /></div>
				<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ponto.extras}" minFractionDigits="2" /></div>
			</div>		
		</c:forEach>
		<div class="linha" style="border-bottom: 0px; border-top: 2px solid #ccc;">
			<div style="width: 200px;"></div>
			<div style="width: 70px; text-align: right;"> </div>
			<div style="width: 50px; text-align: right;"> </div>
			<div style="width: 50px; text-align: right;"> </div>
			<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ttHn}" minFractionDigits="2" /></div>
			<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ttHe}" minFractionDigits="2" /></div>
			<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ttEn}" minFractionDigits="2" /></div>
			<div style="width: 70px; text-align: right;"><fmt:formatNumber type="NUMBER" value="${ttEx}" minFractionDigits="2" /></div>
		</div>
	</div>
    
</div>