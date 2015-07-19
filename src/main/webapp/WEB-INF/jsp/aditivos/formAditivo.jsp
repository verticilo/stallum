<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'ADITIVOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formAditivo" class="formulario" action="<c:url value="/aditivos"/>" method="post">

    <input type="hidden" name="aditivo.id" value="${aditivo.id}" />
    <input type="hidden" name="aditivo.versao" value="${aditivo.versao}" />
    
	<h2><fmt:message key="formAditivo.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="aditivo.obra" /> <span>*</span><br/>
			<select name="aditivo.obra.id">
				<option value=""></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == aditivo.obra}" > selected="selected"</c:if> >${obra.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="aditivo.numero" /><br/>
			<input type="text" name="aditivo.numero" value="${aditivo.numero}" size="40" disabled="disabled" />
		</div>
	</div>
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="aditivo.descricao" /> <span>*</span><br/>
			<input type="text" name="aditivo.descricao" value="${aditivo.descricao}" size="95" />
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="aditivo.valor" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="aditivo.valor" value="<fmt:formatNumber type="NUMBER" value="${aditivo.valor}" minFractionDigits="2" />" size="40" /> 
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="aditivo.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="aditivo.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${aditivo.data}" />" size="40" />
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty aditivo.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/aditivos/${aditivo.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/aditivos/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="aditivo.remover" />">
  <p><fmt:message key="aditivo.remover.msg" /></p>
</div>