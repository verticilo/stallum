<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'REL_OBRA'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formRelatorioObra" class="formulario" action="<c:url value="/relatorios/obra"/>" method="post" target="_blank">

    <h2><fmt:message key="itensMenu.REL_OBRA" /></h2>
	
	<div class="linha obrigatorio">
		<fmt:message key="relatorioObra.obra" /> <span>*</span><br/>
		<select name="relatorio.obra.id">
			<option value=""></option>
			<c:forEach items="${obras}" var="obra">
				<option value="${obra.id}" <c:if test="${obra == relatorio.obra}" >selected="selected"</c:if> >${obra.nome}</option>
			</c:forEach>
		</select>
		<h2></h2>
	</div>
	<div class="linha">
		<div class="coluna ">
			<input type="checkbox" name="relatorio.aditivos" <c:if test="${relatorio.aditivos}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioObra.aditivos" />
			<br/>
			<input type="checkbox" name="relatorio.despesas" <c:if test="${relatorio.despesas}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioObra.despesas" />
		</div>
		<div class="coluna ">
			<input type="checkbox" name="relatorio.medicoes" <c:if test="${relatorio.medicoes}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioObra.medicoes" />
			<br/>
			<input type="checkbox" name="relatorio.apontamentos" <c:if test="${relatorio.apontamentos}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioObra.apontamentos" />
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="relatorioObra.dataDe" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataDe" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataDe}" />" size="40" />			
		</div>
		<div class="coluna">
			<fmt:message key="relatorioObra.dataAte" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataAte" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataAte}" />" size="40" />			
		</div>
	</div>
		
	<div>
		<div class="linha">
			<div class="coluna"></div>
			<div class="coluna">
				<button type="submit" class="imgBtn relatorio" name="relatorio.excel" value="false" title="<fmt:message key="confirmar" />" >PDF</button>
				<button type="submit" formaction="<c:url value="/relatorios/obra.xls"/>" class="imgBtn relatorio" name="relatorio.excel" value="true" title="<fmt:message key="confirmar" />" >XLS</button>
			</div>
		</div>
	</div>
	
</form>

</div>