<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CONTAS_PAGAR'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formContaPagar" class="formulario" action="<c:url value="/contasPagar"/>" method="post">

    <input type="hidden" name="contaPagar.id" value="${contaPagar.id}" />
    <input type="hidden" name="contaPagar.dataPagto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.dataPagto}" />" />
    <input type="hidden" name="contaPagar.acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.acrescimos}" />" />
    <input type="hidden" name="contaPagar.valorPago" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.valorPago}" />" />
    <input type="hidden" name="contaPagar.status" value="${contaPagar.status}" />
    <input type="hidden" name="contaPagar.versao" value="${contaPagar.versao}" />
    
	<h2><fmt:message key="formContaPagar.titulo" /></h2>
	
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaPagar.conta" /> <span>*</span><br/>
			<select name="contaPagar.conta.id">
				<option value=""></option>
				<c:forEach items="${contas}" var="conta">
					<option value="${conta.id}" <c:if test="${conta == contaPagar.conta}" > selected="selected"</c:if> >${conta}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="contaPagar.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaPagar.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.data}" />" size="40" />
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaPagar.fornecedor" /> <span>*</span><br/>
			<select name="contaPagar.fornecedor.id">
				<option value=""></option>
				<c:forEach items="${fornecedores}" var="fornecedor">
					<option value="${fornecedor.id}" <c:if test="${fornecedor == contaPagar.fornecedor}" > selected="selected"</c:if> >${fornecedor.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="contaPagar.tipo" /> <span>*</span><br/>
				<select id="tipo" name="contaPagar.tipo">
					<option value=""></option>
					<c:forEach items="${tipos}" var="tipo">
						<option value="${tipo}" <c:if test="${tipo == contaPagar.tipo}" > selected="selected"</c:if> ><fmt:message key="tipoDocumento.${tipo}" /></option>
					</c:forEach>
				</select>
			</div>
			<div id="parcs" class="subColuna" style="display: none;">
				<fmt:message key="contaPagar.parcelas" /> <span>*</span><br/>
				<input type="text" class="mask" title="999" name="contaPagar.parcs" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.parcs}" />" size="3" /> / 
				<input type="text" class="mask" title="999" name="contaPagar.interv" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.interv}" />" size="3" /> 
			</div>
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaPagar.numero" /><br/>
			<input type="text" name="contaPagar.numero" value="${contaPagar.numero}" size="40" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="contaPagar.origem" /> <span>*</span><br/>
			<select name="contaPagar.origem.id">
				<option value=""></option>
				<c:forEach items="${origens}" var="origem">
					<option value="${origem.id}" <c:if test="${origem == contaPagar.origem}" > selected="selected"</c:if> >${origem}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaPagar.valor" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaPagar.valor" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.valor}" minFractionDigits="2" />" size="40" /> 
		</div>
		<div class="coluna">
			<fmt:message key="contaPagar.vencto" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaPagar.vencto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.vencto}" />" size="40" />
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaPagar.dataPagto" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="contaPagar.dataPagto" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaPagar.dataPagto}" />" size="40" disabled="disabled" />
		</div>
		<div class="coluna">
			<fmt:message key="contaPagar.acrescimos" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaPagar.acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.acrescimos}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaPagar.valorPago" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaPagar.valorPago" value="<fmt:formatNumber type="NUMBER" value="${contaPagar.valorPago}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
		<div class="coluna">
			<fmt:message key="contaPagar.status" /><br/>
			<input type="text" name="contaPagar.status" value="${contaPagar.status}" size="40" disabled="disabled" />
		</div>
	</div>	
	<div class="linha">
		<div>
			<fmt:message key="contaPagar.obs" /><br/>
			<textarea name="contaPagar.obs" rows="4" cols="45">${contaPagar.obs}</textarea>
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty contaPagar.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/contasPagar/nova"/>" title="<fmt:message key="nova" />" ></button>
			</c:if>
			<c:if test="${contaPagar.status == 'ABERTA'}">					
				<button type="button" class="imgBtn liquidar redir" value="<c:url value="/contasPagar/${contaPagar.id}/pagamento"/>" title="<fmt:message key="formContaPagar.pagar" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<script>
$(function() {
	
	$("#tipo").show(function() {
		parcelas($(this).val());
	});
	$("#tipo").change(function() {
		parcelas($(this).val());
	});
	
	function parcelas(val) {
		if (val == 'CARNE') {
			$("#parcs").show();
		} else {
			$("#parcs").hide();			
		}
	}
	
});
</script>
