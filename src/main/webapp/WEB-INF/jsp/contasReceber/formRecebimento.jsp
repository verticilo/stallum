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

<form id="formRecebimento" class="formulario" action="<c:url value="/contasReceber/receber"/>" method="post">

	<input id="totCta1" type="hidden" name="recebimento.totalContas" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalContas}" minFractionDigits="2" />" />
	<input id="totAcr1" type="hidden" name="recebimento.totalAcrescimos" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalAcrescimos}" minFractionDigits="2" />" />
	<input id="totPgt1" type="hidden" name="recebimento.totalRecto" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalRecto}" minFractionDigits="2" />" />
	<input type="hidden" name="contaReceber.status" value="${contaReceber.status}" />

	<h2><fmt:message key="formRecebimento.titulo" /></h2>
	
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="recebimento.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="recebimento.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${recebimento.data}" />" size="40" />
		</div>
		<div class="coluna">
			<fmt:message key="recebimento.contaCorrente" /> <span>*</span><br/>
			<select name="recebimento.contaCorrente.id">
				<option value=""></option>
				<c:forEach items="${contasCorrente}" var="conta">
					<option value="${conta.id}" <c:if test="${conta.id == recebimento.contaCorrente.id}" > selected="selected"</c:if> >${conta}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="linha">
		<div class="coluna obrigatorio">
			<fmt:message key="recebimento.formaPagto" /> <span>*</span><br/>
			<select name="recebimento.formaPagto">
				<option value="DINHEIRO"><fmt:message key="formaPagto.DINHEIRO" /></option>
				<c:forEach items="${formasPagto}" var="formaPagto">
					<option value="${formaPagto}" <c:if test="${formaPagto == recebimento.formaPagto}" > selected="selected"</c:if> ><fmt:message key="formaPagto.${formaPagto}" /></option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="recebimento.numDocto" /><br/>
			<input type="text" name="recebimento.numDocto" value="${recebimento.numDocto}" size="40" />
		</div>
	</div>
	<div class="linha">
		<div class="coluna obrigatorio">
			<fmt:message key="recebimento.totalContas" /><br/>
			<input id = "totCta2" type="text" class="mask" title="<fmt:message key="formato.valor" />" name="recebimento.totalContas" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalContas}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
		<div class="coluna">
			<fmt:message key="recebimento.totalAcrescimos" /><br/>
			<input id="totAcr2" type="text" class="mask" title="<fmt:message key="formato.valor" />" name="recebimento.totalAcrescimos" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalAcrescimos}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
	</div>	
	<div class="linha">
		<div class="coluna">
			<fmt:message key="recebimento.totalRecto" /><br/>
			<input id="totPgt2" type="text" class="mask" title="<fmt:message key="formato.valor" />" name="recebimento.totalRecto" value="<fmt:formatNumber type="NUMBER" value="${recebimento.totalRecto}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
		<div class="coluna">
			<fmt:message key="recebimento.status" /><br/>
			<input type="text" name="recebimento.status" value="${recebimento.status}" size="40" disabled="disabled" />
		</div>
	</div>
	
	<div>
		<div class="linha" style="height: auto; vertical-align: middle;">
			<fmt:message key="recebimento.bordero" /> <span>*</span><br/>

			<div class="lista">
			
			<div class="cabecalho">
				<div style="width: 135px;"><fmt:message key="contaReceber.conta" /></div>
				<div style="width: 137px;"><fmt:message key="contaReceber.cliente" /></div>
				<div style="width: 75px;"><fmt:message key="contaReceber.vencto" /></div>
				<div style="width: 87px; text-align: right;"><fmt:message key="contaReceber.valor" /></div>
				<div style="width: 87px; text-align: right;"><fmt:message key="contaReceber.acrescimos" /></div>
				<div style="width: 87px; text-align: right;"><fmt:message key="contaReceber.valorRecebido" /></div>
			</div>
			
			<c:forEach items="${recebimento.contasRecebidas}" var="contaReceber" varStatus="idx">
				<input type="hidden" name="contasReceber[${idx.count}].id" value="${contaReceber.id}" />
				<input type="hidden" name="contasReceber[${idx.count}].marcada" value="true" />
				<input id="val1-${idx.count}" type="hidden" name="contasReceber[${idx.count}].valor" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valor}" minFractionDigits="2" />" />
				<input id="acr1-${idx.count}" type="hidden" name="contasReceber[${idx.count}].acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.acrescimos}" minFractionDigits="2" />" />
				<input id="pgt1-${idx.count}" type="hidden" name="contasReceber[${idx.count}].valorRecebido" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valorRecebido}" minFractionDigits="2" />" />
				<div class="linha" style="height: auto; padding: 0;">
					<div style="width: 137px; padding-top: 7px;">${contaReceber.conta.nome}</div>
					<div style="width: 137px; padding-top: 7px;">${contaReceber.cliente.nomeCurto}</div>
					<div style="width: 75px; padding-top: 7px;"><fmt:formatDate type="date" dateStyle="MEDIUM" value="${contaReceber.vencto}" /></div>
					<div style="width: 87px; text-align: right;"><input id="val2-${idx.count}" class="mask valCta" title="<fmt:message key="formato.valor" />" type="text" name="contasReceber[${idx.count}].valor" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valor}" minFractionDigits="2" />" size="9" disabled="disabled" /></div>
					<div style="width: 87px;"><input id="acr2-${idx.count}" class="mask valAcr" title="<fmt:message key="formato.valor" />" type="text" name="contasReceber[${idx.count}].acrescimos" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.acrescimos}" minFractionDigits="2" />" size="9" /> </div>
					<div style="width: 87px; "><input id="pgt2-${idx.count}" class="mask valPag" title="<fmt:message key="formato.valor" />" type="text" name="contasReceber[${idx.count}].valorRecebido" value="<fmt:formatNumber type="NUMBER" value="${contaReceber.valorRecebido}" minFractionDigits="2" />" size="9" disabled="disabled" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>
		
	<div class="linha">
		<div>
			<fmt:message key="recebimento.obs" /><br/>
			<textarea name="recebimento.obs" rows="4" cols="45">${recebimento.obs}</textarea>
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty recebimento.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/recebimentos/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<script>
$(function() {
	
	$(".valAcr").blur(function() {
		calcTotais($(this));
	});
	
	$(".valAcr").show(function() {
		calcTotais($(this));
	});
	
	function calcTotais(el) {
		var id = el.attr('id').split('-')[1];
		var mask = el.attr('title');
		var totCta = strToFloat($("#totCta1").val(), mask);
		var totAcr = 0.0;
		var totPag = 0.0;
		var valCta = strToFloat($("#val1-" + id).val());
		var valAcr = strToFloat($("#acr2-" + id).val());
		var valPag = valCta + valAcr;
		$("#pgt1-" + id).val(floatToStr(valPag, mask));
		$("#pgt2-" + id).val(floatToStr(valPag, mask));
		$(".valAcr").each(function(){
			totAcr += strToFloat($(this).val(), mask);
		});
		$(".valPag").each(function(){
			totPag += strToFloat($(this).val(), mask);
		});
		$("#totAcr1").val(floatToStr(totAcr, mask));
		$("#totAcr2").val(floatToStr(totAcr, mask));
		$("#totPgt1").val(floatToStr(totPag, mask));
		$("#totPgt2").val(floatToStr(totPag, mask));
	}
	
});
</script>
