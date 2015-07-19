<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'DESPESAS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formDespesa" class="formulario" action="<c:url value="/despesas"/>" method="post">

    <input type="hidden" name="despesa.id" value="${despesa.id}" />
    <input type="hidden" name="despesa.versao" value="${despesa.versao}" />
    
	<h2><fmt:message key="formDespesa.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="despesa.descricao" /> <span>*</span><br/>
			<input type="text" name="despesa.descricao" value="${despesa.descricao}" size="98" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="despesa.obra" /><br/>
			<select name="despesa.obra.id">
				<option value=""></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == despesa.obra}" > selected="selected"</c:if> >${obra.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="despesa.centroCusto" /><br/>
			<select name="despesa.centroCusto.id">
				<option value=""></option>
				<c:forEach items="${centrosCusto}" var="centroCusto">
					<option value="${centroCusto.id}" <c:if test="${centroCusto == despesa.centroCusto}" > selected="selected"</c:if> >${centroCusto.nome}</option>
				</c:forEach>
			</select>			
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="despesa.valor" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="despesa.valor" value="<fmt:formatNumber type="NUMBER" value="${despesa.valor}" minFractionDigits="2" />" size="40" /> 
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="despesa.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="despesa.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${despesa.data}" />" size="40" />
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty despesa.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/despesas/${despesa.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/despesas/nova"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="despesa.remover" />">
  <p><fmt:message key="despesa.remover.msg" /></p>
</div>