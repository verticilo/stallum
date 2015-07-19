<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'REL_OCORRENCIA'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formExtratoFuncionario" class="formulario" action="<c:url value="/relatorios/ocorrencias"/>" method="post" target="_blank">

    <h2><fmt:message key="itensMenu.REL_OCORRENCIA" /></h2>
	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.dataDe" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataDe" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataDe}" />" size="40" />			
		</div>
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.dataAte" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataAte" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataAte}" />" size="40" />			
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.obra" /><br/>
			<select name="relatorio.obra.id">
				<option value=""><fmt:message key="todas" /></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == relatorio.obra}" >selected="selected"</c:if> >${obra.nome}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.centroCusto" /><br/>
			<select id="cCusto" name="relatorio.centroCusto.id">
				<option value=""><fmt:message key="todos" /></option>
				<c:forEach items="${centrosCusto}" var="centroCusto">
					<option value="${centroCusto.id}" <c:if test="${centroCusto == relatorio.centroCusto}" >selected="selected"</c:if> >${centroCusto.nome}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.funcionario" /><br/>
			<select name="relatorio.funcionario.id">
				<option value=""><fmt:message key="todos" /></option>
				<c:forEach items="${funcionarios}" var="funcionario">
					<option value="${funcionario.id}" <c:if test="${funcionario == relatorio.funcionario}" >selected="selected"</c:if> >${funcionario.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.empresa" /><br/>
			<select name="relatorio.empresa.id">
				<option value=""><fmt:message key="todas" /></option>
				<c:forEach items="${empresas}" var="empresa">
					<option value="${empresa.id}" <c:if test="${empresa == relatorio.empresa}" >selected="selected"</c:if> >${empresa.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>	
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="funcionario.status" /><br/>
			<select name="relatorio.status">
				<option value=""><fmt:message key="todos" /></option>
				<c:forEach items="${status}" var="sts">
					<option value="${sts}" <c:if test="${sts == relatorio.status}" >selected="selected"</c:if> >${sts}</option>
				</c:forEach>
			</select>
		</div>	
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.analitico" /><br/>
			<input type="checkbox" name="relatorio.analitico" <c:if test="${relatorio.analitico}">checked="checked"</c:if> />
		</div>	
		<div class="coluna">
		</div>	
	</div>	
		
	<div>
		<div class="linha">
			<div class="coluna"></div>
			<div class="coluna">
				<button type="submit" class="imgBtn relatorio" name="relatorio.excel" value="false" title="<fmt:message key="confirmar" />" >PDF</button>
				<button type="submit" formaction="<c:url value="/relatorios/ocorrencias.xls"/>" class="imgBtn relatorio" name="relatorio.excel" value="true" title="<fmt:message key="confirmar" />" >XLS</button>
			</div>
		</div>
	</div>
	
</form>

</div>