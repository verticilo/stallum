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

<form id="formContaReceber" class="formulario" action="<c:url value="/contasReceber"/>" method="post">

    <input type="hidden" name="contaReceber.id" value="${contaReceber.id}" />
    <input type="hidden" name="contaReceber.dataRecto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.dataRecto}" />" />
    <input type="hidden" name="contaReceber.acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.acrescimos}" />" />
    <input type="hidden" name="contaReceber.valorRecebido" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valorRecebido}" />" />
    <input type="hidden" name="contaReceber.status" value="${contaReceber.status}" />
    <input type="hidden" name="contaReceber.versao" value="${contaReceber.versao}" />
    
	<h2><fmt:message key="formContaReceber.titulo" /></h2>
	
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaReceber.conta" /> <span>*</span><br/>
			<select name="contaReceber.conta.id">
				<option value=""></option>
				<c:forEach items="${contas}" var="conta">
					<option value="${conta.id}" <c:if test="${conta == contaReceber.conta}" > selected="selected"</c:if> >${conta}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaReceber.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.data}" />" size="40" />
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaReceber.cliente" /> <span>*</span><br/>
			<select name="contaReceber.cliente.id">
				<option value=""></option>
				<c:forEach items="${clientes}" var="cliente">
					<option value="${cliente.id}" <c:if test="${cliente == contaReceber.cliente}" > selected="selected"</c:if> >${cliente.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.tipo" /> <span>*</span><br/>
			<select name="contaReceber.tipo">
				<option value=""></option>
				<c:forEach items="${tipos}" var="tipo">
					<option value="${tipo}" <c:if test="${tipo == contaReceber.tipo}" > selected="selected"</c:if> ><fmt:message key="tipoDocumento.${tipo}" /></option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaReceber.numero" /><br/>
			<input type="text" name="contaReceber.numero" value="${contaReceber.numero}" size="40" />
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.origem" /> <span>*</span><br/>
			<select name="contaReceber.origem.id">
				<option value=""></option>
				<c:forEach items="${origens}" var="origem">
					<option value="${origem.id}" <c:if test="${origem == contaReceber.origem}" > selected="selected"</c:if> >${origem}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaReceber.valor" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaReceber.valor" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valor}" minFractionDigits="2" />" size="40" /> 
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.vencto" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaReceber.vencto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.vencto}" />" size="40" />
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaReceber.dataRecto" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaReceber.dataRecto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.dataRecto}" />" size="40" disabled="disabled" />
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.acrescimos" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaReceber.acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.acrescimos}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaReceber.valorRecebido" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaReceber.valorRecebido" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valorRecebido}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
		<div class="coluna">
			<fmt:message key="contaReceber.status" /><br/>
			<input type="text" name="contaReceber.status" value="${contaReceber.status}" size="40" disabled="disabled" />
		</div>
	</div>	
	<div class="linha">
		<div>
			<fmt:message key="contaReceber.obs" /><br/>
			<textarea name="contaReceber.obs" rows="4" cols="45">${contaReceber.obs}</textarea>
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty contaReceber.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/contasReceber/nova"/>" title="<fmt:message key="nova" />" ></button>
			</c:if>
			<c:if test="${contaReceber.status == 'ABERTA'}">					
				<button type="button" class="imgBtn liquidar redir" value="<c:url value="/contasReceber/${contaReceber.id}/recebimento"/>" title="<fmt:message key="formContaReceber.receber" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>