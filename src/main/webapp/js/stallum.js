function limitaTexto(element, lblLimite, limite) {
	disp = limite - element.value.length;
	if (disp <= 0) {
		element.value = element.value.substring(0, limite);
		disp = 0;
	}
	document.getElementById(lblLimite).innerHTML = disp;
}

function paginar(acao) {
	document.getElementById("acaoPaginacao").value = acao;
	document.getElementById("filtrar").click();
}

function validaArquivo(element, maxKB, tipoUpload) {
	
	vldTipo = element.name + "MsgTipo";
	vldTam  = element.name + "MsgTamanho";
	img 	= element.files[0];
	tipo 	= img.type.substring(img.type.indexOf("/") + 1, img.type.lenght);
	valido	= true;

	
	if (valido && (tipoUpload == 1 && "jpeg,gif,png".indexOf(tipo) == -1)
		//|| (tipoUpload == 2 && "photoshop,psd|coreldraw,cdr|illustrator,ai|acad,dwg".indexOf(img.type) == -1)
	   ) {
		document.getElementById(vldTipo).style.display = "block";
		element.value = "";
		valido = false;
	} else {
		document.getElementById(vldTipo).style.display = "none";
	}
	
	if (valido && img.size > maxKB * 1024) {
		document.getElementById(vldTam).style.display = "block";
		element.value = "";
		valido = false; 
	} else {
		document.getElementById(vldTam).style.display = "none";
	}

}

function exibirElemento(idEl, exibir, display) {
	if (exibir) {
		document.getElementById(idEl).style.display = display;
	} else {
		document.getElementById(idEl).style.display = "none";
	}
}

function strToFloat(str) {
	return strToFloat(str, "99.999.999.999,99");
}
function strToFloat(str, msk) {
	if (str === undefined || str == null || str == "")
		return 0.0;	
	var pto = str.lastIndexOf(".");
	var vrg = str.lastIndexOf(",");
	if (pto > vrg) { // 99,999.99
		str = str.replace(/[,]/g, "");
	} else if (vrg > pto) { // 99.999,99
		str = str.replace(/[.]/g, "").replace(",", ".");
	}
	return parseFloat(str);
}

function floatToStr(str) {
	return floatToStr(str, "99.999.999.999,99");
}
function floatToStr(num, msk) {
	
	if (msk === undefined || msk == null)
		msk = "99.999.999.999,99";
	
	var sm = "."; 
	var sd = ","; // Sep. decimal ',' -> 99.999,99
	
	var pto = msk.lastIndexOf(".");
	var vrg = msk.lastIndexOf(",");	
	
	if (pto > vrg) { // 99,999.99
		str = num.rep;
		sm = ",";
		sd = ".";
	} else if (vrg == pto) { // 999
		sd = "";
	}
	
	var str = (num + '').split(".");
	var int = str[0];
			
	if (sm == ".") {

		if (int.length > 3)
			int = int.replace(/([0-9]{3})$/g, ".$1");
		if (int.length == 7) {
			int = int.replace(/([0-9]{3}).([0-9]{3})$/g, "$1.$2");
		} else if (int.length == 11) {
			int = int.replace(/([0-9]{3}).([0-9]{3}).([0-9]{3})$/g, "$1.$2.$3");
		} else {
			if (int.length > 6)
				int = int.replace(/([0-9]{3}).([0-9]{3})$/g, ".$1.$2");
			if (int.length > 9)
				int = int.replace(/([0-9]{3}).([0-9]{3}).([0-9]{3})$/g, ".$1.$2.$3");
		}
				
	} else if (sm == ",") {
		
		if (int.length > 3)
			int = int.replace(/([0-9]{3})$/g, ",$1");
		if (int.length == 7) {
			int = int.replace(/([0-9]{3}),([0-9]{3})$/g, "$1,$2");
		} else if (int.length == 11) {
			int = int.replace(/([0-9]{3}),([0-9]{3}),([0-9]{3})$/g, "$1,$2,$3");
		} else {
			if (int.length > 6)
				int = int.replace(/([0-9]{3}),([0-9]{3})$/g, ",$1,$2");
			if (int.length > 9)
				int = int.replace(/([0-9]{3}),([0-9]{3}),([0-9]{3})$/g, ",$1,$2,$3");
		}
				
	}

	// Casas decimais
	var dec = "";
	var ncd = 0; // Numero de casas
	msk = msk.replace(/9/g, "0");

	// Numero de casas decimais pela mascara
	if (vrg >= 0)
		ncd = msk.length - vrg - 1;
	// Se o numero tem casas decimais, atribui a 'dec'
	if (str.length > 1) {
		dec = parseFloat("0." + str[1]);
		dec = dec.toFixed(ncd);
		if (dec >= 1) 
			int = parseInt(int, 0) + 1;
		dec = dec.split(".")[1];
	}
	
	// Se o numero de decimais for menor que o da mascara, acrescenta zeros
	if (dec.length < ncd)
		dec = msk.substring(vrg + 1, vrg + 1 + ncd - dec.length) + dec;
	// Se a mascara nao tem casa dec, retorna int
	if (ncd == 0)
		return int + sd + Math.round(parseFloat("0" + sd + dec));
	
	// Retorna o numero com as casas decimais, conforme a mascara
	return int + sd + dec;
}

function maskHHMM(str) {
	
	if (str === undefined || str == null || str == '')
		return str;
	
	// TODO implementar máscara [-]99:99
	
}

function diasDecorridos(dt1, dt2) {
	
    // variáveis auxiliares
    var minuto = 60000; 
    var dia = minuto * 60 * 24;
    var horarioVerao = 0;
    
    // ajusta o horario de cada objeto Date
    dt1.setHours(0);
    dt1.setMinutes(0);
    dt1.setSeconds(0);
    dt2.setHours(0);
    dt2.setMinutes(0);
    dt2.setSeconds(0);
    
    
    // determina o fuso horário de cada objeto Date
    var fh1 = dt1.getTimezoneOffset();
    var fh2 = dt2.getTimezoneOffset(); 
    
    // retira a diferença do horário de verão
    if(dt2 > dt1){
      horarioVerao = (fh2 - fh1) * minuto;
    } 
    else{
      horarioVerao = (fh1 - fh2) * minuto;    
    }
    
    var dif = Math.abs(dt2.getTime() - dt1.getTime()) - horarioVerao;
    return Math.floor(dif / dia);
}

function anosDecorridos(dt1, dt2) {
	return Math.floor(diasDecorridos(dt1, dt2) / 365);
}