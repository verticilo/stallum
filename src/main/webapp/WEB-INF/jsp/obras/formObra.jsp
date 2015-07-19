<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'OBRAS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formObra" class="formulario" action="<c:url value="/obras"/>" method="post">

    <input type="hidden" name="obra.id" value="${obra.id}" />
    <input type="hidden" name="obra.versao" value="${obra.versao}" />
    <input type="hidden" name="obra.status" value="${obra.status}" />
    <input type="hidden" name="obra.endereco.id" value="${obra.endereco.id}" />
    <input type="hidden" name="obra.endereco.versao" value="${obra.endereco.versao}" />
    
	<h2><fmt:message key="formObra.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.nome" /> <span>*</span><br/>
			<input type="text" name="obra.nome" value="${obra.nome}" size="40" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.empresa" /> <span>*</span><br/>
			<select name="obra.empresa.id">
				<option value=""></option>
				<c:forEach items="${empresas}" var="empresa">
					<option value="${empresa.id}" <c:if test="${empresa == obra.empresa}" > selected="selected"</c:if> >${empresa.nomeCurto} </option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.centroCusto" /><br/>
			<select name="obra.centroCusto.id">
				<option value=""></option>
				<c:forEach items="${centrosCusto}" var="centroCusto">
					<option value="${centroCusto.id}" <c:if test="${centroCusto == obra.centroCusto}" >selected="selected"</c:if> >${centroCusto.nome}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.dataInicio" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="obra.dataInicio" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${obra.dataInicio}" />" size="15" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.cliente" /> <span>*</span><br/>
			<select name="obra.cliente.id">
				<option value=""></option>
				<c:forEach items="${clientes}" var="cliente">
					<option value="${cliente.id}" <c:if test="${cliente == obra.cliente}" > selected="selected"</c:if> >${cliente.nomeCurto} </option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="obra.sindicato" /> <span>*</span><br/>
			<select name="obra.sindicato.id">
				<option value=""></option>
				<c:forEach items="${sindicatos}" var="sindicato">
					<option value="${sindicato.id}" <c:if test="${sindicato == obra.sindicato}" > selected="selected"</c:if> >${sindicato.nome} </option>
				</c:forEach>
			</select>
		</div>	
	</div>	
	<div>	
		<div class="coluna obrigatorio">
			<fmt:message key="obra.contrato" /><br/>
			<input type="text" name="obra.contrato" value="${obra.contrato}" size="40" />
		</div>
		<div class="coluna obrigatorio">
			<c:if test="${sessaoUsuario.administrador}">
				<fmt:message key="obra.valorInicial" /> <span>*</span><br/>
				<input type="text" class="mask recalc" title="<fmt:message key="formato.valor" />" name="obra.valorInicial" value="<fmt:formatNumber type="NUMBER" value="${obra.valorInicial}" minFractionDigits="2" />" size="15" />
			</c:if>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="endereco.cep" /><br/>
			<input id="cep" type="text" style="float:left;" name="obra.endereco.cep" value="${obra.endereco.cep}" size="15" />
			<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.logradouro" /> <span>*</span><br/>
			<input id="rua" type="text" name="obra.endereco.logradouro" value="${obra.endereco.logradouro}" size="40" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="endereco.numero" /> <span>*</span><br/>
				<input type="text" name="obra.endereco.numero" value="${obra.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="obra.endereco.complemento" value="${obra.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="obra.endereco.bairro" value="${obra.endereco.bairro}" size="40" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cidade" /> <span>*</span><br/>
			<input id="cidade" type="text" name="obra.endereco.cidade" value="${obra.endereco.cidade}" size="40" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.estado" /> <span>*</span><br/>
			<select id="uf" name="obra.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == obra.endereco.estado}" > selected="selected"</c:if> >${estado.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="obra.status" /><br/>
			<input type="text" name="obra.status" value="<fmt:message key="statusObra.${obra.status}" />" size="40" disabled="disabled" />
		</div>
		<div class="coluna">
			<fmt:message key="obra.dataStatus" /><br/>
			<input type="text" name="obra.dataStatus" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${obra.dataStatus}" />" size="40" disabled="disabled" />
		</div>
	</div>	
	<div>		
		<div class="linha">
			<fmt:message key="obra.obs" /><br/>
			<textarea name="obra.obs" rows="5" cols="40">${obra.obs}</textarea>			
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty obra.id}">
				<a class="imgBtn" href="<c:url value="/obras/${obra.id}/movimentosObra/listar"/>"><img src="<c:url value="/images/medicoes.png"/>" title="<fmt:message key="obra.movimentos"/>" /></a>
				<c:if test="${obra.status != 'ATIVA'}">
					<a class="lnkRemover imgBtn" href="<c:url value="/obras/${obra.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				</c:if>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/obras/nova"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
	<c:if test="${obra.status == 'REMOVIDA'}">
		<a style="float:right;" href="<c:url value="/obras/${obra.id}/recuperar"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="recuperar"/>" /></a>
		<span style="float:right; margin: 7px;"><fmt:message key="alteradoEm" /> ${obra.alteradoEm}</span>
	</c:if>
		
</form>

<c:if test="${sessaoUsuario.administrador and not empty obra.id}">

	<h3><fmt:message key="formObra.resultado" /></h3>

	<div class="listaInterna">
		<div class="linha">
			<div><fmt:message key="obra.valorInicial" /></div>
			<div id="valIni" style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.valorInicial}" minFractionDigits="2" /></div>(+)
		</div>
		<div class="linha">
			<div><a href="<c:url value="/aditivos/obra/${obra.id}/listar"/>"><fmt:message key="obra.totalAditivos" /></a></div>
			<div id="totAdt" style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalAditivos}" minFractionDigits="2" /></div>(+)
		</div>
		<div class="linha obrigatorio">
			<div><fmt:message key="obra.totalObra" /></div>
			<div id="totObr" style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalObra}" minFractionDigits="2" /></div>(=)
		</div>
		<div class="linha">
			<div><a href="<c:url value="/apontamentos/obra/${obra.id}/listar"/>"><fmt:message key="obra.totalApontamentos" /></a></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalApontamentos}" minFractionDigits="2" /></div>(-)
		</div>
		<div class="linha">
			<div><a href="<c:url value="/despesas/obra/${obra.id}/listar"/>"><fmt:message key="obra.totalDespesas" /></a></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalDespesas}" minFractionDigits="2" /></div>(-)
		</div>
		<div class="linha">
			<div><fmt:message key="obra.totalCustosIndiretos" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalCustosIndiretos}" minFractionDigits="2" /></div>(-)
		</div>
		<div class="linha obrigatorio">
			<div><fmt:message key="obra.custoTotal" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.custoTotal}" minFractionDigits="2" /></div>(=)
		</div>
		<div class="linha">
			<div><a href="<c:url value="/medicoes/obra/${obra.id}/listar"/>"><fmt:message key="obra.totalMedicoes" /></a></div>
			<div id="totMed" style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalMedicoes}" minFractionDigits="2" /></div>
		</div>
		<div class="linha obrigatorio">
			<div><fmt:message key="obra.resultadoAtual" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.resultadoAtual}" minFractionDigits="2" /></div>
			(<fmt:formatNumber type="NUMBER" value="${obra.percResultado}" minFractionDigits="2" />%)
		</div>
		<div class="linha">
			<div><fmt:message key="obra.saldoReceber" /></div>
			<div id="sldRec" style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.saldoReceber}" minFractionDigits="2" /></div>
			(<fmt:formatNumber type="NUMBER" value="${obra.percSaldo}" minFractionDigits="2" />%)
		</div>
		<div class="linha">
			<div><fmt:message key="obra.totalReajustes" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.totalReajustes}" minFractionDigits="2" /></div>
		</div>			
		<div class="linha">
			<div><fmt:message key="obra.mediaFuncionarios" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.mediaFuncionarios}" /></div>
		</div>			
		<div class="linha">
			<div><fmt:message key="obra.resultFuncionario" /></div>
			<div style="text-align: right;"><fmt:formatNumber type="NUMBER" value="${obra.resultFuncionario}" minFractionDigits="2" /></div>
		</div>			
	</div>	
</c:if>	

</div>

<div id="confirmarModal" title="<fmt:message key="obra.remover" />">
  <p><fmt:message key="obra.remover.msg" /></p>
</div>

<script>
$(function() {
	
	$(".recalc").blur(function() {
		var mask = $(this).attr("title");
		var valor = strToFloat($(this).val());
		if (valor == 0) {
			$("#valIni").html(floatToStr(0, mask));
			$("#sldRec").html(floatToStr(0, mask));
			$("#totObr").html(floatToStr(0, mask));
		} else {
			var aditivos = strToFloat($("#totAdt").html());
			var medicoes = strToFloat($("#totMed").html());
			$("#valIni").html(floatToStr(valor, mask));
			$("#totObr").html(floatToStr(valor + aditivos, mask));
			$("#sldRec").html(floatToStr(valor + aditivos - medicoes, mask));
		}
	});
	
});
</script>