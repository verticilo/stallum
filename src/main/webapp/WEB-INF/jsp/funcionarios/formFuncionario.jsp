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

<form id="formFuncionario" class="formulario" action="<c:url value="/funcionarios"/>" method="post">

    <input id="idFunc" type="hidden" name="funcionario.id" value="${funcionario.id}" />
    <input type="hidden" name="funcionario.versao" value="${funcionario.versao}" />
    <input type="hidden" name="funcionario.endereco.id" value="${funcionario.endereco.id}" />
    <input type="hidden" name="funcionario.endereco.versao" value="${funcionario.endereco.versao}" />
    <c:if test="${not empty funcionario.id}">
	    <input type="hidden" name="funcionario.empresa.versao" value="${funcionario.empresa.versao}" />
	    <input type="hidden" name="funcionario.funcao.id" value="${funcionario.funcao.id}" />
	    <input type="hidden" name="funcionario.funcao.versao" value="${funcionario.funcao.versao}" />
    	<input type="hidden" name="funcionario.salarioBase" value="<fmt:formatNumber type="NUMBER" value="${funcionario.salarioBase}" minFractionDigits="2" />" />
    	<input type="hidden" name="funcionario.anuenioSalario" value="<fmt:formatNumber type="NUMBER" value="${funcionario.anuenioSalario}" minFractionDigits="2" />" />
    	<input type="hidden" name="funcionario.sindicato.id" value="${funcionario.sindicato.id}" />
    	<input type="hidden" name="funcionario.sindicato.versao" value="${funcionario.sindicato.versao}" />
    	<input type="hidden" name="funcionario.status" value="${funcionario.status}" />
    	<input type="hidden" name="funcionario.dataDemissao" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcionario.dataDemissao}" />" />
    </c:if>

	<h2><fmt:message key="formFuncionario.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.nome" /> <span>*</span><br/>
			<input type="text" name="funcionario.nome" value="${funcionario.nome}" size="40" />
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.apelido" /><br/>
			<input type="text" name="funcionario.apelido" value="${funcionario.apelido}" size="40" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="funcionario.empresa" /> <span>*</span><br/>
				<select id="empresa" name="funcionario.empresa.id">
					<option value=""></option>
					<c:forEach items="${empresas}" var="empresa">
						<option value="${empresa.id}" <c:if test="${empresa == funcionario.empresa}" > selected="selected"</c:if> >${empresa.nomeCurto} </option>
					</c:forEach>
				</select>
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.centroCusto" /><br/>
				<select name="funcionario.centroCusto.id">
					<option value=""></option>
					<c:forEach items="${centrosCusto}" var="centroCusto">
						<option value="${centroCusto.id}" <c:if test="${centroCusto == funcionario.centroCusto}" > selected="selected"</c:if> >${centroCusto.nome} </option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.funcao" /> <span>*</span><br/>
			<select id="funcao" name="funcionario.funcao.id" <c:if test="${not empty funcionario.id}">disabled="disabled"</c:if> >
				<option value=""></option>
				<c:forEach items="${funcoes}" var="funcao">
					<option value="${funcao.id}" <c:if test="${funcao == funcionario.funcao}" > selected="selected"</c:if> >${funcao} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.salarioBase" /><br/>
				<input id="sal" type="text" name="funcionario.salarioBase" class="mask" title="<fmt:message key="formato.valor" />" value="<fmt:formatNumber type="NUMBER" value="${funcionario.salarioBase}" minFractionDigits="2" />" size="15" disabled="disabled" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.anuenioSalario" /><br/>
				<input id="anSal" type="text" name="funcionario.anuenioSalario" value="<fmt:formatNumber type="NUMBER" value="${funcionario.anuenioSalario}" minFractionDigits="2" />" size="15" disabled="disabled" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.bonusSalario" /><br/>
				<input id="bnSal" type="text" class="mask recalc" title="<fmt:message key="formato.percentual" />" name="funcionario.bonusSalario" value="<fmt:formatNumber type="NUMBER" value="${funcionario.bonusSalario}" minFractionDigits="2" />" size="15" <c:if test="${not empty funcionario.id}">disabled="disabled"</c:if> />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.anuenioBonus" /><br/>
				<input id="anBon" class="mask recalc" title="<fmt:message key="formato.percentual" />" type="text" name="funcionario.anuenioBonus" value="<fmt:formatNumber type="NUMBER" value="${funcionario.anuenioBonus}" minFractionDigits="2" />" size="15" disabled="disabled" />
			</div>
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.encargos" /><br/>
				<input id="encargo1" class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="funcionario.encargo1" value="<fmt:formatNumber type="NUMBER" value="${funcionario.encargo1}" minFractionDigits="2" />" size="3" disabled="disabled" />
				<input id="encargo2" class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="funcionario.encargo2" value="<fmt:formatNumber type="NUMBER" value="${funcionario.encargo2}" minFractionDigits="2" />" size="3" disabled="disabled" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.salarioFamilia" /><br/>
				<input id="salFamilia" class="mask" title="<fmt:message key="formato.valor" />" type="text" name="funcionario.salarioFamilia" value="<fmt:formatNumber type="NUMBER" value="${funcionario.salarioFamilia}" minFractionDigits="2" />" size="15" disabled="disabled" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.salarioHora" /><br/>
				<input id="salHor" class="mask" title="<fmt:message key="formato.valor" />" type="text" name="funcionario.salarioHora" value="<fmt:formatNumber type="NUMBER" value="${funcionario.salarioHora}" minFractionDigits="2" />" size="5" disabled="disabled" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.salarioMes" /><br/>
				<input id="salMes" class="mask" title="<fmt:message key="formato.valor" />" type="text" name="funcionario.salarioMes" value="<fmt:formatNumber type="NUMBER" value="${funcionario.salarioMes}" minFractionDigits="2" />" size="10" disabled="disabled" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.custoMes" /><br/>
				<input id="cusMes" class="mask" title="<fmt:message key="formato.valor" />" type="text" name="funcionario.custoMes" value="<fmt:formatNumber type="NUMBER" value="${funcionario.custoMes}" minFractionDigits="2" />" size="10" disabled="disabled" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<div class="subColuna">
				<fmt:message key="funcionario.ctps" /> <span>*</span><br/>
				<input type="text" name="funcionario.ctps" value="${funcionario.ctps}" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.serieCtps" /> <span>*</span><br/>
				<input type="text" name="funcionario.serieCtps" value="${funcionario.serieCtps}" size="15" />
			</div>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.dataAdmissao" /> <span>*</span><br/>
			<input id="dtAdm" type="text" class="mask recalc" title="<fmt:message key="formato.maskData" />" name="funcionario.dataAdmissao" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcionario.dataAdmissao}" />" size="40" />
		</div>		
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<div class="subColuna">
				<fmt:message key="funcionario.rg" /> <span>*</span><br/>
				<input type="text" name="funcionario.rg" value="${funcionario.rg}" size="15" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.orgaoRg" /> <span>*</span><br/>
				<input type="text" name="funcionario.orgaoRg" value="${funcionario.orgaoRg}" size="15" />
			</div>
		</div>
		<div class="coluna obrigatorio">
			<div class="subColuna">
				<fmt:message key="funcionario.dataRg" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="funcionario.dataRg" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcionario.dataRg}" />" size="9" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.ufRg" /> <span>*</span><br/>
				<select name="funcionario.ufRg">
					<c:forEach items="${estados}" var="estado">
						<option value="${estado}" <c:if test="${estado == funcionario.ufRg}" > selected="selected"</c:if> >${estado.nome} </option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.nomePai" /> <span>*</span><br/>
			<input type="text" name="funcionario.nomePai" value="${funcionario.nomePai}" size="40" /> 
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.nomeMae" /> <span>*</span><br/>
			<input type="text" name="funcionario.nomeMae" value="${funcionario.nomeMae}" size="40" /> 
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.cpf" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.cpf" />" name="funcionario.cpf" value="${funcionario.cpf}" size="40" /> 
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.dataNascimento" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="funcionario.dataNascimento" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcionario.dataNascimento}" />" size="40" />
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcionario.pisPasep" /> <span>*</span><br/>
			<input type="text" name="funcionario.pisPasep" value="${funcionario.pisPasep}" size="40" /> 
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.tsRh" /><br/>
			<input type="text" name="funcionario.tsRh" value="${funcionario.tsRh}" size="40" /> 
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.banco" /><br/>
				<input type="text" name="funcionario.banco" value="${funcionario.banco}" size="15" /> 
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.agencia" /><br/>
				<input type="text" name="funcionario.agencia" value="${funcionario.agencia}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.contaCorrente" /><br/>
			<input type="text" name="funcionario.contaCorrente" value="${funcionario.contaCorrente}" size="40" />
		</div>		
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="funcionario.sindicato" /><br/>
			<input id="sindicato" type="text" name="funcionario.sindicato.nome" value="${funcionario.sindicato.nome}" size="40" disabled="disabled" /> 
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.email" /><br/>
			<input class="email" type="text" name="funcionario.email" value="${funcionario.email}" size="40" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.dddTelefone" /><br/>
				<input type="text" name="funcionario.dddTelefone" value="${funcionario.dddTelefone}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.telefone" /><br/>
				<input type="text" name="funcionario.telefone" value="${funcionario.telefone}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="funcionario.dddCelular" /><br/>
				<input type="text" name="funcionario.dddCelular" value="${funcionario.dddCelular}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="funcionario.celular" /><br/>
				<input type="text" name="funcionario.celular" value="${funcionario.celular}" size="15" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="endereco.cep" /><br/>
			<input id="cep" type="text" style="float:left;" name="funcionario.endereco.cep" value="${funcionario.endereco.cep}" size="15" />
			<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.logradouro" /> <span>*</span><br/>
			<input id="rua" type="text" name="funcionario.endereco.logradouro" value="${funcionario.endereco.logradouro}" size="40" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="endereco.numero" /> <span>*</span><br/>
				<input type="text" name="funcionario.endereco.numero" value="${funcionario.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="funcionario.endereco.complemento" value="${funcionario.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="funcionario.endereco.bairro" value="${funcionario.endereco.bairro}" size="40" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cidade" /> <span>*</span><br/>
			<input id="cidade" type="text" name="funcionario.endereco.cidade" value="${funcionario.endereco.cidade}" size="40" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.estado" /> <span>*</span><br/>
			<select id="uf" name="funcionario.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == funcionario.endereco.estado}" > selected="selected"</c:if> >${estado.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="funcionario.status" /><br/>
			<input type="text" name="funcionario.status" value="<fmt:message key="statusFuncionario.${funcionario.status}" />" size="40" disabled="disabled" />
		</div>
		<div class="coluna">
			<fmt:message key="funcionario.dataDemissao" /><br/>
			<input type="text" name="funcionario.dataDemissao" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcionario.dataDemissao}" />" size="40" disabled="disabled" />
		</div>
	</div>	
	<div>		
		<div class="linha">
			<fmt:message key="funcionario.obs" /><br/>
			<textarea name="funcionario.obs" rows="5" cols="35">${funcionario.obs}</textarea>			
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty funcionario.id}">
				<a class="imgBtn" href="<c:url value="/funcionarios/${funcionario.id}/movimentos/listar"/>"><img src="<c:url value="/images/medicoes.png"/>" title="<fmt:message key="funcionario.movimentos"/>" /></a>
				<a class="imgBtn" href="<c:url value="/funcionarios/${funcionario.id}/dependentes/listar"/>"><img src="<c:url value="/images/despesas.png"/>" title="<fmt:message key="funcionario.dependentes"/>" /></a>
				<c:if test="${funcionario.status == 'DEMITIDO'}">
					<a class="lnkRemover imgBtn" href="<c:url value="/funcionarios/${funcionario.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				</c:if>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/funcionarios/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
	<c:if test="${funcionario.status == 'REMOVIDO'}">
		<a style="float:right;" href="<c:url value="/funcionarios/${funcionario.id}/recuperar"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="recuperar"/>" /></a>
		<span style="float:right; margin: 7px;"><fmt:message key="alteradoEm" /> ${funcionario.alteradoEm}</span>
	</c:if>
		
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="funcionario.remover" />">
  <p><fmt:message key="funcionario.remover.msg" /></p>
</div>

<script>
$(function() {
	
	$("#empresa").change(function() {
		buscaParametros();
	});
	
	$("#funcao").change(function() {
		buscaParametros();
	});
	
	$(".recalc").blur(function() {
		recalculaSalario();
	});
	
	function buscaParametros() {

		if ($("#empresa").val() == "" || $("#funcao").val() == "")
			return;
		
		var strUrl = "/stallum/funcionarios/parametrosJson/" + $("#empresa").val() + "/" + $("#funcao").val();
		if ($("#idFunc").val() != "")
			strUrl = strUrl + "/" + $("#idFunc").val();
		
		$.ajax({
			url: strUrl,
			success: function(result) {  
				$("#sal").val(result.salarioBase);
				$("#sindicato").val(result.sindicato.nome);
				$("#salFamilia").val(result.salarioFamilia);
				$("#encargo1").val(result.encargo1);
				$("#encargo2").val(result.encargo2);
				recalculaSalario();
			},
			error: function() {
				$("#sal").val("");
				$("#sindicato").val("");
				$("#salFamilia").val("");
				$("#encargo1").val("");
				$("#encargo2").val("");
			}
	    });
	}
	
	function recalculaSalario() {
		
		var mask = $("#bnSal").attr("title");
		var dtAdm = $("#dtAdm").val();
		if (dtAdm != "") {
			var diaAdm = parseFloat(dtAdm.split("/")[0]);
			var mesAdm = parseFloat(dtAdm.split("/")[1]);
			var anoAdm = parseFloat(dtAdm.split("/")[2]);
			var qtAnos = anosDecorridos(new Date(anoAdm, mesAdm - 1, diaAdm), new Date());
			$("#anSal").val(floatToStr(qtAnos, mask));
		}
		
		mask = $("#sal").attr("title");
		var salBase = strToFloat($("#sal").val());
		if (salBase == 0) {
			$("#salHora").val(floatToStr(0, mask));
			$("#salMes").val(floatToStr(0, mask));
		} else {
			var sal = salBase * (1 + (strToFloat($("#anSal").val()) / 100));
			sal = sal * (1 + (strToFloat($("#encargo1").val()) / 100));

			var aliqBon = strToFloat($("#bnSal").val()) + strToFloat($("#anBon").val());
			var bon = salBase * (aliqBon / 100);
			bon = bon * (1 + (strToFloat($("#encargo2").val()) / 100));
			
			var salHor = sal + (strToFloat($("#salFamilia").val()) / 220);
			var cusHor = sal + bon + (strToFloat($("#salFamilia").val()) / 220);
			
			$("#salHor").val(floatToStr(salHor, mask));
			$("#salMes").val(floatToStr(salHor * 220, mask));
			$("#cusMes").val(floatToStr(cusHor * 220, mask));
		}
	}
	
});

recalculaSalario();
</script>