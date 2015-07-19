$(document).ready(function() {

	$(".redir").click(function() {
		location.href = $(this).attr('value');
	});
	
	$("#voltarModal").hide();
	$(".btnVoltar").click(function() {
		var url = $(this).attr('value');
		$("#voltarModal").dialog({
			resizable: false,
		    height:200,
		    modal: true,
		    buttons: {
		    	"Confirmar": function() {
		    		$(this).dialog("close");
		    		if (url !== undefined && url != null)
		    			location.href = url;
		    		else
		    			window.history.back();
		        },
		        "Cancelar": function() {
		          $(this).dialog("close");
		        }
		    }
		});
	});
	
	$("#buscaCep").click(function(){

		var cor = $("#rua").css("color");
		var corTmp = "#aaa";
		
		$("#rua").css({'color': corTmp});
		$("#bairro").css({'color': corTmp});
		$("#cidade").css({'color': corTmp});
		$("#uf").css({'color': corTmp});
		
		$("#rua").val("aguarde ...");
		$("#bairro").val("...");
		$("#cidade").val("...");
		$("#uf").val("...");
		
		consulta = $.trim($("#cep").val());
		
		if (consulta != "") {
		
			consulta = consulta.replace('-','');
			
			$.getScript("http://cep.republicavirtual.com.br/web_cep.php?cep="+consulta+"&formato=javascript", function(){
			
			        // unescape - Decodifica uma string codificada com o metodo escape.
			        rua = unescape(resultadoCEP.tipo_logradouro) + " " + unescape(resultadoCEP.logradouro);
			        bairro = unescape(resultadoCEP.bairro);
			        cidade = unescape(resultadoCEP.cidade);
			        uf = unescape(resultadoCEP.uf);
			
			        $("#rua").css({'color': cor});
			        $("#bairro").css({'color': cor});
			        $("#cidade").css({'color': cor});
			        $("#uf").css({'color': cor});

			        $("#rua").val(rua);
			        $("#bairro").val(bairro);
			        $("#cidade").val(cidade);
			        $("#uf").val(uf).attr('selected', true);
	
	        });
			
		}
		
	});
	
	$(".wmark").show(function() {
		$(this).watermark($(this).attr("title"));
	});
		
	$(".mask").show(function() {
		$(this).mask($(this).attr("title"), {reverse: true});
	});

	$(".mask2").show(function() {
		$(this).mask($(this).attr("title"), {reverse: false});
	});
	
	$(".sHora").blur(function() {
		
//		var valor = $(this).val();
//		var ok = valor.match(/^[-|+]?\d*[0-9](\:\d[0-9])?$/);
//		alert(ok);
//		if (!ok)
//			$(this).focus();
	});
	
	$("#formBox").show(function() {
		if ($("input[name='exibirForm']").val() != "true")
			$("#formBox").hide();
		$("#lnkNovo").click(function(ev) { ev.preventDefault(); $("#formBox").show(); });
		$("#lnkCancelar").click(function(ev) { ev.preventDefault(); $("input[type=text]").val(""); $("#formBox").hide(); });
		$("#lnkConfirmar").click(function(ev) { ev.preventDefault(); $("#lnkEnviar").click(); });
	});
	
	$("#confirmarModal").hide();
	$(".lnkRemover").click(function(ev) {
		ev.preventDefault();
		var url = $(this).attr("href");
		$("#confirmarModal").dialog({
			resizable: false,
		    height:200,
		    modal: true,
		    buttons: {
		    	"Confirmar": function() {
		    		$(this).dialog("close");
		    		location.href = url;
		        },
		        "Cancelar": function() {
		          $(this).dialog("close");
		        }
		    }
		});
	});
	
	$("#erroModal").hide();
	$("#lnkErro").click(function(ev) {
		ev.preventDefault();
		$("#erroModal").dialog({
			height:250,
			width: 400,
			modal: true,
			buttons: {
				"Ok": function() {
					$(this).dialog("close");
				}
			}
		});
	});
		
});