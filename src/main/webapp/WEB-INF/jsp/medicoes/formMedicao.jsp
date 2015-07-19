<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'MEDICOES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formMedicao" class="formulario" action="<c:url value="/medicoes"/>" method="post">

    <input type="hidden" name="medicao.id" value="${medicao.id}" />
    <input type="hidden" name="medicao.repetir" value="${medicao.repetir}" />
    <input type="hidden" name="medicao.versao" value="${medicao.versao}" />
    
	<h2><fmt:message key="formMedicao.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="medicao.obra" /> <span>*</span><br/>
			<select name="medicao.obra.id">
				<option value=""></option>
				<c:forEach items="${obras}" var="obra">
					<option value="${obra.id}" <c:if test="${obra == medicao.obra}" > selected="selected"</c:if> >${obra.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="medicao.empresa" /><br/>
			<select name="medicao.empresa.id">
				<option value=""></option>
				<c:forEach items="${empresas}" var="empresa">
					<option value="${empresa.id}" <c:if test="${empresa == medicao.empresa}" > selected="selected"</c:if> >${empresa.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<div class="subColuna obrigatorio">
				<fmt:message key="medicao.notaFiscal" /> <span>*</span><br/>
				<input type="text" name="medicao.notaFiscal" value="${medicao.notaFiscal}" size="15" />
			</div>
			<div class="subColuna">
				<fmt:message key="medicao.cancelada" /><br/>
				<input type="checkbox" name="medicao.cancelada" <c:if test="${medicao.cancelada}">checked="checked"</c:if> />
			</div>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="medicao.valor" /> <span>*</span><br/>
			<input type="text" class="mask recalc" title="<fmt:message key="formato.valor" />" name="medicao.valor" value="<fmt:formatNumber type="NUMBER" value="${medicao.valor}" minFractionDigits="2" />" size="40" /> 
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="medicao.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="medicao.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${medicao.data}" />" size="40" />
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="medicao.iss" /><br/>
				<input type="text" class="mask recalc" title="<fmt:message key="formato.percentual" />" name="medicao.iss" value="<fmt:formatNumber type="NUMBER" value="${medicao.iss}" minFractionDigits="2" />" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="medicao.valorIss" /><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" id="valorIss" value="<fmt:formatNumber type="NUMBER" value="${medicao.valorIss}" minFractionDigits="2" />" size="15" disabled="disabled" /> 
			</div>
		</div>	
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="medicao.inss" /><br/>
				<input type="text" class="mask recalc" title="<fmt:message key="formato.percentual" />" name="medicao.inss" value="<fmt:formatNumber type="NUMBER" value="${medicao.inss}" minFractionDigits="2" />" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="medicao.valorInss" /><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" id="valorInss" value="<fmt:formatNumber type="NUMBER" value="${medicao.valorInss}" minFractionDigits="2" />" size="15" disabled="disabled" /> 
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="medicao.clss" /><br/>
				<input type="text" class="mask recalc" title="<fmt:message key="formato.percentual" />" name="medicao.clss" value="<fmt:formatNumber type="NUMBER" value="${medicao.clss}" minFractionDigits="2" />" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="medicao.valorClss" /><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" id="valorClss" value="<fmt:formatNumber type="NUMBER" value="${medicao.valorClss}" minFractionDigits="2" />" size="15" disabled="disabled" /> 
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="medicao.irrf" /><br/>
				<input type="text" class="mask recalc" title="<fmt:message key="formato.percentual" />" name="medicao.irrf" value="<fmt:formatNumber type="NUMBER" value="${medicao.irrf}" minFractionDigits="2" />" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="medicao.valorIrrf" /><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" id="valorIrrf" value="<fmt:formatNumber type="NUMBER" value="${medicao.valorIrrf}" minFractionDigits="2" />" size="15" disabled="disabled" /> 
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="medicao.valorLiquido" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" id="valorLiquido" value="<fmt:formatNumber type="NUMBER" value="${medicao.valorLiquido}" minFractionDigits="2" />" size="40" disabled="disabled" /> 
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty medicao.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/medicoes/nova"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button id="btnConfirmar" type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
			<c:if test="${sessaoUsuario.administrador}">
				<a class="lnkRemover imgBtn" href="<c:url value="/medicoes/${medicao.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
			</c:if>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="medicao.remover" />">
  <p><fmt:message key="medicao.remover.msg" /></p>
</div>

<script>
$(function() {
	
	$(".recalc").blur(function() {
		var mask = $(this).attr("title");
		var valor = strToFloat($("input[name='medicao.valor']").val());
		if (valor == 0) {
			$("#valorIss").val(floatToStr(0, mask));
			$("#valorInss").val(floatToStr(0, mask));
			$("#valorClss").val(floatToStr(0, mask));
			$("#valorIrrf").val(floatToStr(0, mask));
			$("#valorLiquido").val(floatToStr(0, mask));
		} else {
			var iss = strToFloat($("input[name='medicao.iss']").val());
			var inss = strToFloat($("input[name='medicao.inss']").val());
			var clss = strToFloat($("input[name='medicao.clss']").val());
			var irrf = strToFloat($("input[name='medicao.irrf']").val());
			$("#valorIss").val(floatToStr(valor * (iss / 100), mask));
			$("#valorInss").val(floatToStr(valor * (inss / 100), mask));
			$("#valorClss").val(floatToStr(valor * (clss / 100), mask));
			$("#valorIrrf").val(floatToStr(valor * (irrf / 100), mask));
			$("#valorLiquido").val(floatToStr(valor - strToFloat($("#valorIss").val()) - strToFloat($("#valorInss").val()) - strToFloat($("#valorClss").val()) - strToFloat($("#valorIrrf").val()), mask));
		}
	});
		
});
</script>