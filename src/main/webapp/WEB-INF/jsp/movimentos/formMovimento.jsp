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

<form id="formMovimento" class="formulario" action="<c:url value="/movimentos"/>" method="post">

    <input type="hidden" name="movimento.id" value="${movimento.id}" />
    <input type="hidden" name="movimento.versao" value="${movimento.versao}" />
    <input type="hidden" name="movimento.funcionario.id" value="${movimento.funcionario.id}" />
    <input type="hidden" name="movimento.funcionario.nome" value="${movimento.funcionario.nome}" />
    <input type="hidden" name="movimento.funcionario.versao" value="${movimento.funcionario.versao}" />
    
    
	<h2><fmt:message key="formMovimento.titulo" /></h2>
	
	<div>
		<div class="coluna">
			<fmt:message key="movimento.funcionario" /><br/>
			<input type="text" name="movimento.funcionario.nome" value="${movimento.funcionario.nome}" size="40" disabled="disabled" />
		</div>		
		<div class="coluna obrigatorio">
			<fmt:message key="movimento.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="movimento.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${movimento.data}" />" size="40" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="movimento.novaFuncao" /><br/>
			<select id="funcao" name="movimento.novaFuncao.id">
				<option value=""></option>
				<c:forEach items="${funcoes}" var="funcao">
					<option value="${funcao.id}" <c:if test="${funcao == movimento.novaFuncao}" >selected="selected"</c:if> >${funcao}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="movimento.novoStatus" /><br/>
				<select id="funcao" name="movimento.novoStatus">
					<option value=""></option>
					<c:forEach items="${listaStatus}" var="status">
						<option value="${status}" <c:if test="${status == movimento.novoStatus}" >selected="selected"</c:if> ><fmt:message key="statusFuncionario.${status}" /></option>
					</c:forEach>
				</select>		
			</div>
			<div class="subColuna">
				<fmt:message key="movimento.bonus" /><br/>
				<input class="mask" title="<fmt:message key="formato.valor" />" type="text" name="movimento.bonus" value="<fmt:formatNumber type="NUMBER" value="${movimento.bonus}" minFractionDigits="2" />" size="10" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="movimento.motivo" /> <span>*</span><br/>
			<textarea name="movimento.motivo" rows="5" cols="35">${movimento.motivo}</textarea>
		</div>
		<c:if test="${not empty movimento.dataFim}">
			<div class="coluna">
				<fmt:message key="movimento.dataFim" /><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="movimento.dataFim" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${movimento.dataFim}" />" size="40" />
			</div>
		</c:if>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty movimento.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/funcionarios/${movimento.funcionario.id}/movimentos/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>