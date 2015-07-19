<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'REL_FUNCIONARIO'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form class="formulario" action="<c:url value="/relatorios/funcionarios"/>" method="post" target="_blank">

    <h2><fmt:message key="itensMenu.REL_FUNCIONARIO" /></h2>
	
	<div class="linha">
		<div class="coluna obrigatorio">
			<fmt:message key="relatorioFuncionario.funcionario" /> <span>*</span><br/>
			<select name="relatorio.funcionario.id">
				<option value=""><fmt:message key="selecione" /></option>
				<c:forEach items="${funcionarios}" var="funcionario">
					<option value="${funcionario.id}" <c:if test="${funcionario == relatorio.funcionario}" >selected="selected"</c:if> >${funcionario.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="relatorioObra.obra" /><br/>
			<select name="relatorio.obra.id">
				<option value=""><fmt:message key="todas" /></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == relatorio.obra}" >selected="selected"</c:if> >${obra.nome}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="relatorioObra.centroCusto" /><br/>
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
			<fmt:message key="relatorioFuncionario.dataDe" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataDe" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataDe}" />" size="40" />			
		</div>
		<div class="coluna">
			<fmt:message key="relatorioFuncionario.dataAte" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataAte" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataAte}" />" size="40" />			
		</div>
	</div>
	<div>
		<fmt:message key="relatorioFuncionario.listar" />
	</div>
	<div>
		<div class="coluna" style="height: 100px;">
			<input type="radio" name="relatorio.filtroLista" value="pontos" <c:if test="${relatorio.pontos}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioFuncionario.pontos" />
			<br/>
			<input type="radio" name="relatorio.filtroLista" value="faltas" <c:if test="${relatorio.faltas}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioFuncionario.faltas" />
			<br/>
			<input type="radio" name="relatorio.filtroLista" value="abonos" <c:if test="${relatorio.abonos}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioFuncionario.abonos" />
			<br/>
			<input type="radio" name="relatorio.filtroLista" value="ferias" <c:if test="${relatorio.ferias}">checked="checked"</c:if> /> 
			<fmt:message key="relatorioFuncionario.ferias" />
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<div class="coluna"></div>
			<div class="coluna">
				<button type="submit" class="imgBtn relatorio" name="relatorio.excel" value="false" title="<fmt:message key="confirmar" />" >PDF</button>
				<button type="submit" formaction="<c:url value="/relatorios/funcionarios.xls"/>" class="imgBtn relatorio" name="relatorio.excel" value="true" title="<fmt:message key="confirmar" />" >XLS</button>
			</div>
		</div>
	</div>
	
</form>

</div>