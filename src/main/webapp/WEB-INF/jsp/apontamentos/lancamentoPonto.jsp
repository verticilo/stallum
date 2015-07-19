<div>

	<h3>${relatorio.obra.nome} (<fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataDe}" /> - <fmt:formatDate type="date" dateStyle="MEDIUM" value="${relatorio.dataAte}" />)</h3>
	
	<div class="lista">
	
		<div class="cabecalho">
			<div style="width: 400px;"><fmt:message key="funcionario.nome" /></div>
			<div style="width: 100px;"><fmt:message key="diaSemana.SEG" /></div>
			<div style="width: 100px;"><fmt:message key="diaSemana.TER" /></div>
			<div style="width: 100px;"><fmt:message key="diaSemana.QUA" /></div>
			<div style="width: 100px;"><fmt:message key="diaSemana.QUI" /></div>
			<div style="width: 100px;"><fmt:message key="diaSemana.SEX" /></div>
		</div>
		
		<c:forEach items="${funcionarios}" var="funcionario" varStatus="idx">
			<div class="linha" <c:if test="${idx.count mod 2 eq 0 }">style="background-color: #f0f0f0;"</c:if> >
				<div style="width: 400px;">${funcionario.nome}</div>
				<div style="width: 100px;">
					<input type="checkbox" class="pto" id="${funcionario.id}-pres-seg" /> 
					<input type="checkbox" class="pto" id="${funcionario.id}-ocor-seg" /> 
				</div>
				<div style="width: 100px;">
					<input type="checkbox" class="pto" id="${funcionario.id}-pres-ter" /> 
					<input type="checkbox" class="pto" id="${funcionario.id}-ocor-ter" /> 
				</div>
				<div style="width: 100px;">
					<input type="checkbox" class="pto" id="${funcionario.id}-pres-qua" /> 
					<input type="checkbox" class="pto" id="${funcionario.id}-ocor-qua" /> 
				</div>
				<div style="width: 100px;">
					<input type="checkbox" class="pto" id="${funcionario.id}-pres-qui" /> 
					<input type="checkbox" class="pto" id="${funcionario.id}-ocor-qui" /> 
				</div>
				<div style="width: 100px;">
					<input type="checkbox" class="pto" id="${funcionario.id}-pres-sex" /> 
					<input type="checkbox" class="pto" id="${funcionario.id}-ocor-sex" /> 
				</div>
			</div>		
		</c:forEach>
		
		<div class="linha">
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="button" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" id="btnEnviar" ></button>
		</div>
		
	</div>
    
</div>

<div id="dialog-form" title="Ocorrência">
  <form action="${relatorio.obra.id}|${relatorio.strDataDe}">
  	<div style="width: 100%; margin: 7px; text-align: left;">
      Motivo
      <br/>
      <select id="motivoFalta">
		<option value=""></option>
		<c:forEach items="${motivosFalta}" var="motivo">
		  <option value="${motivo}"><fmt:message key="motivoFalta.${motivo}" /></option>
		</c:forEach>
	  </select>
  	</div>
  	<div style="width: 100%; margin: 7px; text-align: left;">
      Hora Entrada/Saída<br/>
      <input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" id="horaEntrada" size="6" /> 
      / 
      <input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" id="horaSaida" size="6" />
	  <br/>
  	</div>
  	<div style="width: 100%; margin: 7px; text-align: left;">
      OBS
      <br/>
      <textarea id="obs" rows="2" cols="30" ></textarea>
  	</div>
 
      <!-- Allow form submission with keyboard without duplicating the dialog button -->
      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
  </form>
</div>

<script>
$(function() {
	
	var dialog, form, id, dia, ocor, idFunc;
	var lista = [];
	var item;
	
	$(".pto").change(function() {
		
		id = $(this).attr("id");
		idFunc = Number(id.split('-')[0]);
		dia = id.split('-')[2];
		ocor = id.split('-')[1] == 'ocor';
		
		if ($(this).is(":checked")) {
			
			addPresenca();
			if (ocor) {
				$("#" + idFunc + "-pres-" + dia).attr("checked", "checked");
				dialog.dialog( "open" );
			}
		
		} else if (ocor) {
			removeItem();
			addPresenca();
		} else {
			$("#" + idFunc + "-ocor-" + dia).removeAttr("checked");
			removeItem();
		}
	});
	
	dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 300,
      width: 350,
      modal: true,
      buttons: {
        "Confirma": addOcorrencia,
        Cancel: function() {
          dialog.dialog( "close" );
		  $("#" + idFunc + "-ocor-" + dia).removeAttr("checked");
        }
      },
      close: function() {
        form[ 0 ].reset();
      }
    });
 
    form = dialog.find( "form" ).on( "submit", function( event ) {
      event.preventDefault();
      addOcorrencia();
    });
    
    function addPresenca() {
    	lista[lista.length] = {idFuncionario: idFunc, diaSemana: dia};
    }
    
    function addOcorrencia() {
    	
    	removeItem();
    	
    	item = {pontoNovo: {
	    			idFuncionario: idFunc,
	    			diaSemana: dia,
	    			motivoFalta: $("#motivoFalta").val(),
	    			horaEntrada: $("#horaEntrada").val(),
	    			horaSaida: $("#horaSaida").val(),
	    			obs: $("#obs").val()
    			}};
    	
    	lista[lista.length] = item;
    	
        dialog.dialog( "close" );
    }
    
    function removeItem() {
    	for (idx = 0; idx < lista.length; idx++) {
    		if (lista[idx].idFuncionario == idFunc && lista[idx].diaSemana == dia) {
    			lista.splice(idx, 1);
    			break;
    		}
    	}
    }
    
    $("#btnEnviar").click(function() {
    	
    	for (idx = 0; idx < lista.length; idx++) {
			    		
    	}
    	
    	var params = dialog.find( "form" ).attr("action").split('|');
    	var strUrl = "/stallum/apontamentos/lancar/" + params[0] + "/" + params[1];
    	var json = JSON.stringify(lista);
		
    	alert(json);
    	
    	$.post(strUrl, lista, function(result) {  
			alert("OK");
	    });
    	
    });
			
});
</script>