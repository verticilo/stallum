<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'APONT_NOVO'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form class="formulario" action="<c:url value="/apontamentos/lancamento"/>" method="post">

    <h2><fmt:message key="itensMenu.APONT_NOVO" /></h2>
	
	<div class="linha">
		<div class="coluna obrigatorio">
			<fmt:message key="relatorioFuncionario.obra" /> <span>*</span><br/>
			<select name="relatorio.obra.id">
				<option value=""><fmt:message key="selecione" /></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == relatorio.obra}" >selected="selected"</c:if> >${obra.nome}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="relatorioFuncionario.semanaDe" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="relatorio.dataDe" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataDe}" />" size="40" />			
		</div>
		<div class="coluna">
		</div>
	</div>

	<div>
		<div class="linha">
			<div class="coluna"></div>
			<div class="coluna">
				<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
			</div>
		</div>
	</div>
	
</form>

</div>