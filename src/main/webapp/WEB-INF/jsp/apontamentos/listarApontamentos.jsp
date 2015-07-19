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

	<h3><fmt:message key="itensMenu.APONTAMENTOS" /></h3>
	
	<div class="lista">
	
		<div class="btnFiltro">
		
			<div>
				<a href="<c:url value="/apontamentos/novo/${filtro.strData}"/>">
					<img src="<c:url value="/images/novo.png"/>" title="<fmt:message key="novo" />" />
				</a>
			</div>
			
			<form class="filtro" action="<c:url value="/apontamentos/listar" />" method="post">
	
				<input type="hidden" name="filtro.paginacao.acao" id="acaoPaginacao"/>
				<input type="hidden" name="filtro.paginacao.registrosPorPagina" value="${filtro.paginacao.registrosPorPagina}"/>
				<input type="hidden" name="filtro.paginacao.totalRegistros" 	value="${filtro.paginacao.totalRegistros}"/>
				<input type="hidden" name="filtro.strData" 	value="${filtro.strData}"/>
			
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
		
		<div style="float: left; width: 160px; padding-right: 3px; border-right: 2px solid #ccc; margin-right: 3px;">
		
		<div>
			<div class="calApont" style="width: 99%;">
				<a href="<c:url value="/apontamentos/listar/${filtro.mesAnterior}"/>" ><<</a>
				${filtro.mesAno}
				<a href="<c:url value="/apontamentos/listar/${filtro.proximoMes}"/>" >>></a>
			</div>
			<div class="calApont"><span>D</span></div>
			<div class="calApont"><span>S</span></div>
			<div class="calApont"><span>T</span></div>
			<div class="calApont"><span>Q</span></div>
			<div class="calApont"><span>Q</span></div>
			<div class="calApont"><span>S</span></div>
			<div class="calApont"><span>S</span></div>
			<c:forEach items="${calendario}" var="dia">
				<a href="<c:url value="/apontamentos/listar/${dia.strData}"/>" ><div class="diaApont" style="background-color: ${dia.cor2};">${dia}</div></a>				
			</c:forEach>
			<c:if test="${not empty filtro.strData}">
				<div style="width: 92%; height: auto; text-align: center; border: 2px solid #ccc; margin-top: 5px;">
					<b><fmt:formatDate type="date" dateStyle="MEDIUM" value="${filtro.data}" /></b>
<!-- 					<br/> -->
<%-- 					<c:if test="${not empty filtro.dia}"> --%>
<%-- 						<a href="<c:url value="/apontamentos/nao-lancados/${filtro.strData}"/>" > --%>
<%-- 							<fmt:message key="listarApontamentos.naoLancados" /> --%>
<!-- 						</a> -->
<%-- 					</c:if> --%>
				</div>
			</c:if>			
		</div>
		
		</div>
				
		<div style="float: left; width: 520px;">
		
		<div class="cabecalho">
			<div style="width: 270px;"><fmt:message key="apontamento.obraOuCCusto" /></div>
			<div style="width: 90px;"><fmt:message key="apontamento.data" /></div>
			<div style="width: 80px;"><fmt:message key="apontamento.feriado" /></div>
		</div>
		
		<c:forEach items="${listaApontamentos}" var="apontamento">
			<div class="linha">
				<div style="width: 270px;"><a href="<c:url value="/apontamentos/${apontamento.id}/editar"/>">${apontamento.nomeCurto}</a></div>
				<div style="width: 90px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${apontamento.data}" /></div>
				<div style="width: 80px;"><c:if test="${apontamento.feriado}">X</c:if></div>
				<div style="width: auto;">
					<a href="<c:url value="/apontamentos/${apontamento.id}/editar"/>"><img src="<c:url value="/images/editar.png"/>" width="16" height="16" title="<fmt:message key="editar"/>" /></a>
				</div>
			</div>		
		</c:forEach>
		
		</div>
		
	</div>
    
</div>