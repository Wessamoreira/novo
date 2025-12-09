var timerID = null;
var timerRunning = false;

function stopclock(){
    clearTimeout(timerID);
    timerRunning = false;

}

function adicionarHtml(html, id){
    obj=document.getElementsByTagName(id);
    obj[i].innerHTML=html;

}

function zeraFormaPagamento(campo){
    form[campo].value = 0;
}

function startclock () {
    stopclock();
    showtime();
}


function fecharModal(panel, e) {
    var keyCode = e.keyCode;

    if(keyCode == 27){
        Richfaces.hideModalPanel(panel);
        document.getElementById('form').click();
        return false;
    }
}

function abrirPopupMaximizado2(URL, nomeJanela, comprimento, altura) {		 
	fecharPopupEspecifico(nomeJanela);
    var win = window.open(URL, nomeJanela, 'left=0, top=0, width='+ (screen.width-10) +', height=' + (screen.height-110) + ', dependent=yes, maximize=yes, fullscreen=yes, scrollbars=yes, focus=yes');          
    win.focus();       
}

//function abrirPopupMaximizado2(URL, nomeJanela, comprimento, altura, removerPopup) {
//	adicionarPopup(nomeJanela);
//    if(removerPopup) {
//		var win = window.open(null, nomeJanela);		
//		win.close();	
//    };	    				             
//    var win = window.open(URL, nomeJanela, 'left=0, top=0, width='+ (screen.width-10) +', height=' + (screen.height-110) + ', dependent=yes, maximize=yes, fullscreen=yes, scrollbars=yes, focus=yes');          
//    win.focus();       
//}

function abrirPopup2(URL, nomeJanela, comprimento, altura) {
	return abrirPopupMaximizado2(URL, nomeJanela, comprimento, altura);
//    var posTopo = ((screen.height / 2) - (altura / 2)) - 25;
//    var posEsquerda = ((screen.width / 2) -(comprimento / 2));
//    var atributos = 'left=' + posEsquerda + ', screenX=' + posEsquerda + ', top=' + posTopo + ', screenY=' + posTopo + ', width=' + comprimento + ", height=" + altura + ', dependent=yes, menubar=yes, toolbar=yes, resizable=yes , scrollbars=yes' ;
//    window.open(URL,nomeJanela);
//    return false;
}


function contar(campo, cont, total){
    var tam = document.getElementById(campo).value.length;
    var total1 = total - 1;
    if(tam > total1){
        document.getElementById(campo).value = document.getElementById(campo).value.substring(0,total1);
        tam = total;
        alert('Aten\u00e7\u00e3o,voc\u00ea atingiu o limite m\u00e1ximo de caracteres !');
    }
    document.getElementById(cont).value = tam + " de " + total;
}

function showtime () {
    var now = new Date();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds()
    var timeValue = "" + ((hours >12) ? hours -12 :hours)
    timeValue += ((minutes < 10) ? ":0" : ":") + minutes
    timeValue += ((seconds < 10) ? ":0" : ":") + seconds
    timeValue += (hours >= 12) ? " P.M." : " A.M."
    if (document.clock.face != null) {
        document.clock.face.value = timeValue;
        timerID = setTimeout("showtime()",1000);
    }
    timerRunning = true;
}

function flash(file, width, height){
    document.write("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0' width='" + width + "' height='" + height + "'>");
    document.write("<param name='movie' value='" + file + "'>");
    document.write("<param name='quality' value='high'>");
    document.write("<embed src='" + file + "' quality='high' pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash' type='application/x-shockwave-flash' width='" + width + "' height='" + height + "'></embed>");
    document.write("</object>");
}


function liberarBackingBeanMemoria() {
    var hiddenCommandLink = window.document.getElementById("formCadastro:idLiberarBackingBeanMemoria");
    if (hiddenCommandLink) {
        hiddenCommandLink .fireEvent("onclick");
    } else {
        var hiddenCommandLink = window.document.getElementById("form:idLiberarBackingBeanMemoria");
        if (hiddenCommandLink) {
            hiddenCommandLink .fireEvent("onclick");
        }
    }
}



function windowUnloadedAltF4(event) {
    alert(event);
    if ((event.altLeft || event.altKey) &&
        (event.keyCode == 115)) {
        //Captura o ALT F4 do IE / FIREFOX
        liberarBackingBeanMemoria();
    }
}
function getNavegadorFF() {
    //navigator.appVersion
    var nomeNavegador = navigator.appName;
    var pos = nomeNavegador.userAgent.indexOf("Firefox") != -1
    if (pos == -1) {
        return false;
    } else {
        return true;
    }
}



function getNavegadorIE() {
    //navigator.appVersion
    var nomeNavegador = navigator.appName;
    var pos = nomeNavegador.indexOf('Microsoft',0);
    if (pos == -1) {
        return false;
    } else {
        return true;
    }
}

function windowUnloaded(event) {
    alert(event);
    if (getNavegadorIE()) {
        if ((window.event.clientY < 0) && (window.event.clientY < 0)) {
            //Captura o Clicar no FECHAR da Tela do IE
            liberarBackingBeanMemoria();
        }
    }
}

//function setaFormularioHistoricoTurma(form) {
//    formHistorico = form;
//}
function setaValorHistoricoTurma(form) {
 
    window.close();
}


function setFocus(form, campo) {
    form[campo].focus();
}

function focus(campo) {
	document.getElementById(campo).focus();
}


function testarCodigofecharJanela(form) {
    var codigo = form["form:codigo"].value;
    if (codigo != 0) {
        window.close();
    }
}

function abrirPopupHistorico(URL, form, nomeJanela, comprimento, altura) {
    formHistorico = form;
    var disciplina = form["form:disciplina"].value;
    var turma = form["form:turma"].value;

    if (disciplina == 0){
        alert("A Disciplina de ser informada para ser poss�vel informar a as notas");
    } else if (turma == 0){
        alert("A Turma de ser informada para ser poss�vel informar a as notas");
    } else {
        return abrirPopup(URL, nomeJanela, comprimento, altura);
    }
}

function abrirPopupArquivoEnvioTcm(URL, form, nomeJanela, comprimento, altura) {
    var codigo = form["form:codigo"].value;
    if (codigo == 0) {
        alert("O arquivo de integra��o com o tcm deve ser gravado antes da visualiza��o");
    } else {
        return abrirPopup(URL, nomeJanela, comprimento, altura);
    }
}

function abrirPopupArquivoBB(URL, form, nomeJanela, comprimento, altura) {
    var codigo = form["form:codigo"].value;
    if (codigo == 0) {
        alert("O arquivo de integra��o com o banco deve ser gravado antes da visualiza��o");
    } else {
        return abrirPopup(URL, nomeJanela, comprimento, altura);
    }
}

function abrirPopupEnviarDadosIpasgo(URL, form, nomeJanela, comprimento, altura) {
    var codigo = form["form:codigo"].value;
    if (codigo == 0) {
        alert("O arquivo de integra��o com o ipasgo deve ser gerado antes da visualiza��o");
    } else {
        return abrirPopup(URL, nomeJanela, comprimento, altura);
    }
}

function abrirPopup(URL, nomeJanela, comprimento, altura) {
	return abrirPopupMaximizado2(URL, nomeJanela, comprimento, altura);
//    var posTopo = ((screen.height / 2) - (altura / 2)) - 25;
//    var posEsquerda = ((screen.width / 2) -(comprimento / 2));
//    var atributos = 'left=' + posEsquerda + ', screenX=' + posEsquerda + ', top=' + posTopo + ', screenY=' + posTopo + ', width=' + comprimento + ', height=' + altura + ' dependent=yes, menubar=no, toolbar=no, resizable=yes , scrollbars=yes' ;
//    newwin = window.open(URL, nomeJanela, atributos);
//    newwin.focus();
//    return true;
}

function abrirPopup3(URL) {
    window.open(URL);
    return true;
}

function popup(URL, nomeJanela, comprimento, altura) {
    var posTopo = ((screen.height / 2) - (altura / 2));
    var posEsquerda = ((screen.width / 2) -(comprimento / 2));
    var atributos = 'left=' + posEsquerda + ', screenX=' + posEsquerda + ', top=' + posTopo + ', screenY=' + posTopo + ', width=' + comprimento + ", height=" + altura + ', dependent=yes, menubar=no, toolbar=no, resizable=yes , scrollbars=yes' ;
    newwin = window.open(URL, nomeJanela, atributos);
    if(newwin.focus()){
        newwin.close();
        newwin = window.open(URL, nomeJanela, atributos);
    }
    newwin.focus();
    newwin.submit();
    return true;
}

function abrirPopupMaximizado(URL, nomeJanela) {
    abrirPopupMaximizado2(URL, nomeJanela, 1000, 1000);    
    return false;
}

function fecharJanela() {
    window.close();
}

function Tecla(e) {
    TeclasAtalho(e);
    if (document.all) // Internet Explorer
        var tecla = event.keyCode;
    else if(document.layers) // Nestcape
        var tecla = e.which;
    if (tecla > 47 && tecla < 58) // numeros de 0 a 9
        return true;
    else {
        if (tecla != 8) { // backspace
            event.keyCode = 0;
        } else {
            return true;
        }
    }
}

function TeclasAtalho(e) {
    if (e.shiftKey) {
        if (e.keyCode == 43) {
            event.keyCode = 0;
            document.forms[0].incluir.click();
        } else if (e.keyCode == 45) {
            event.keyCode = 0;
            document.forms[0].excluir.click();
        } else if (e.keyCode == 46) {
            event.keyCode = 0;
            document.forms[0].gravar.click();
        } else if (e.keyCode == 42) {
            event.keyCode = 0;
            document.forms[0].consultar.click();
        }
    }
}

function getSelText() {
    var txt = '';
    if (window.getSelection() != '') {
        txt = window.getSelection();
    } else if (document.getSelection()  != '') {
        txt = document.getSelection();    
    }
    return txt;
}


//onkeypress="return mascara(document.rcfDownload, 'str_cep', '99999-999', event);">
function mascara(objForm, strField, sMask, evtKeyPress) {
    var obj = document.getElementById(strField);
    var i, nCount, sValue, fldLen, mskLen,bolMask, sCod, nTecla;
    var textoSel = getSelText();
    
    TeclasAtalho(evtKeyPress);
    if(document.all) { // Internet Explorer
        nTecla = evtKeyPress.keyCode;
    } else if(document.layers) { // Nestcape
        nTecla = evtKeyPress.which;
    } else {
        nTecla = evtKeyPress.which;
        if (nTecla == 8) {
            return true;
        }
        if (nTecla == 0) {
            return true;
        }
    }    
    sValue = obj.value;
    if((sValue.length - (textoSel != '' ? textoSel.length: 0)) == sMask.length){
    	return false;
    }
    if (sMask == "(99)9999-9999") {
        if (sValue.length == 11) {
            sMask = '(99)99999-9999';
        }
    }
    // Limpa todos os caracteres de formata��o que
    // j� estiverem no campo.
    sValue = sValue.toString().replace( "-", "" );
    sValue = sValue.toString().replace( "-", "" );
    sValue = sValue.toString().replace( ":", "" );
    sValue = sValue.toString().replace( ":", "" );
    sValue = sValue.toString().replaceAll( ".", "" );
    sValue = sValue.toString().replace( "/", "" );
    sValue = sValue.toString().replace( "/", "" );
    sValue = sValue.toString().replace( "(", "" );
    sValue = sValue.toString().replace( "(", "" );
    sValue = sValue.toString().replace( ")", "" );
    sValue = sValue.toString().replace( ")", "" );
    sValue = sValue.toString().replace( " ", "" );
    sValue = sValue.toString().replace( " ", "" );
    fldLen = sValue.length;
    mskLen = sMask.length;


    i = 0;
    nCount = 0;
    sCod = "";
    mskLen = fldLen;

    while (i <= mskLen) {
        bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ":") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/"))
        bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

	if (bolMask) {
            sCod += sMask.charAt(i);
            mskLen++;
	} else {
            sCod += sValue.charAt(nCount);
            nCount++;
	}
	i++;
    }

    if (sMask.length == sCod.length) {
    	evtKeyPress.keyCode = 0;
    }
    obj.value = sCod;
    if (nTecla != 8) { // backspace
        if (sMask.charAt(i-1) == "9") { // apenas n�meros...
            return ((nTecla > 47) && (nTecla < 58));
	} // n�meros de 0 a 9
	else { // qualquer caracter...
            return true;
	}
    } else {
        return true;
    }
}


function mascaraMatricula(objForm, strField, evtKeyPress) {
    var nTecla;
    var textoSel = getSelText();
    if (textoSel != '') {
        return true;
    }
    TeclasAtalho(evtKeyPress);
    if(document.all) { // Internet Explorer
        nTecla = evtKeyPress.keyCode;
    } else if(document.layers) { // Nestcape
        nTecla = evtKeyPress.which;
    } else {
        nTecla = evtKeyPress.which;
        if (nTecla == 8) {
            return true;
        }
    }

    if ((nTecla != 65) && (nTecla != 97) && (nTecla!=67) && (nTecla!=99)
        && (nTecla != 73) && (nTecla != 83) && (nTecla != 115) && (nTecla!= 85) && (nTecla != 117) && (nTecla != 105)){
        return false;
    }
}



function atualizarAgenciaContaCorrente(objForm) {
    if (objForm["form:contaCaixa"].checked) {
        objForm["form:agencia"].disabled = true;
    } else {
        objForm["form:agencia"].disabled = false;
    }
}

//function selecionarFornecedor(form, name){
//    var numeroLinha = name.substring(19);
//    var posFim = numeroLinha.indexOf(':', 0);
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoNomeFornecedor = "formConsulta:items:" + numeroLinha + ":nome";
//    var nomeCampoCodigoFornecedor = "formConsulta:items:" + numeroLinha + ":codigo";
//    var nomeFornecedor = form[nomeCampoNomeFornecedor].value;
//    var codigoFornecedor = form[nomeCampoCodigoFornecedor].value;
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:nomeFornecedor"].value = nomeFornecedor ;
//    formConsulta["form:codigoFornecedor"].value = codigoFornecedor ;
//    formConsulta["form:nomeFornecedor"].focus();
//    fecharJanela();
//}
//
//function atualizaValorPagamento(form, name){
//    var numeroLinha = name.substring(13);
//    var posFim = numeroLinha.indexOf(':', 0);
//
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoValorEntrada = "formContaPga" + numeroLinha + ":valorEntrada";
//
//    var valorPagamento = form[nomeCampoValorEntrada].value;
//
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:valorPagamento"].value = valorPagamento ;
//    fecharJanela();
//}
//
//function atualizaValorRecebimento(form, name){
//    var numeroLinha = name.substring(13);
//    var posFim = numeroLinha.indexOf(':', 0);
//
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoValorEntrada = "formContaRec" + numeroLinha + ":valorEntrada";
//
//    var valorRecebimento = form[nomeCampoValorEntrada].value;
//
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:valor"].value = valorRecebimento ;
//    fecharJanela();
//}

//function selecionarPlanoContaCredito(form, name){
//    var numeroLinha = name.substring(19);
//    var posFim = numeroLinha.indexOf(':', 0);
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoNome = "formConsulta:items:" + numeroLinha + ":nome";
//    var nomeCampoCodigo = "formConsulta:items:" + numeroLinha + ":codigo";
//    var nome = form[nomeCampoNome].value;
//    var codigo = form[nomeCampoCodigo].value;
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:planoContaCredito"].value = nome ;
//    formConsulta["form:codigoPlanoContaCredito"].value = codigo ;
//    formConsulta["form:planoContaCredito"].focus();
//    fecharJanela();
//}
//
//function selecionarPlanoContaDebito(form, name){
//    var numeroLinha = name.substring(19);
//    var posFim = numeroLinha.indexOf(':', 0);
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoNome = "formConsulta:items:" + numeroLinha + ":nome";
//    var nomeCampoCodigo = "formConsulta:items:" + numeroLinha + ":codigo";
//    var nome = form[nomeCampoNome].value;
//    var codigo = form[nomeCampoCodigo].value;
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:planoContaDebito"].value = nome ;
//    formConsulta["form:codigoPlanoContaDebito"].value = codigo ;
//    formConsulta["form:planoContaDebito"].focus();
//    fecharJanela();
//}
//function selecionarCentroDespesa(form, name){
//    var numeroLinha = name.substring(19);
//    var posFim = numeroLinha.indexOf(':', 0);
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoNome = "formConsulta:items:" + numeroLinha + ":descricao";
//    var nomeCampoCodigo = "formConsulta:items:" + numeroLinha + ":codigo";
//    var nome = form[nomeCampoNome].value;
//    var codigo = form[nomeCampoCodigo].value;
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:centroDespesa"].value = nome ;
//    formConsulta["form:codigoCentroDespesa"].value = codigo ;
//    formConsulta["form:centroDespesa"].focus();
//    fecharJanela();
//}
//
//function selecionarCentroReceita(form, name){
//    var numeroLinha = name.substring(19);
//    var posFim = numeroLinha.indexOf(':', 0);
//    numeroLinha = numeroLinha.substring(0, posFim);
//    var nomeCampoNome = "formConsulta:items:" + numeroLinha + ":descricao";
//    var nomeCampoCodigo = "formConsulta:items:" + numeroLinha + ":codigo";
//    var nome = form[nomeCampoNome].value;
//    var codigo = form[nomeCampoCodigo].value;
//    var formConsulta = window.opener.document.forms[0];
//    formConsulta["form:centroReceita"].value = nome ;
//    formConsulta["form:codigoCentroReceita"].value = codigo ;
//    formConsulta["form:centroReceita"].focus();
//    fecharJanela();
//}

function changeButton(btn,img,largura,altura){
    btn.style.cssText = " border: none; background-color: transparent; width: " + largura +"px; height: " + altura + "px; background-image: url('imagens/" + img + "'); background-repeat: no-repeat;"
}

//TODO Alberto 03/03/2011
function somenteNumero(e)    {
    var tecla = window.event?event.keyCode:e.which;
//    alert(tecla);
    if (window.event)
    { if((tecla<33 && tecla!=32)||(tecla > 46 && tecla < 58 && tecla != 47)) return true; else event.keyCode = 0; }
    else
    { if((tecla<33 && tecla!=32)|| (tecla > 46 && tecla < 58&& tecla != 47)) return true; else return false; }
}

function somenteNumeroBarra(e)    {
    var tecla = window.event?event.keyCode:e.which;
    if (window.event)
    { if((tecla<33 && tecla!=32)||(tecla > 46 && tecla < 58)) return true; else event.keyCode = 0; }
    else
    { if((tecla<33 && tecla!=32)|| (tecla > 46 && tecla < 58)) return true; else return false; }
}

function somenteNumeroVirgula(e)    {
    var tecla = window.event?event.keyCode:e.which;
    if (window.event)
    { if((tecla<33 && tecla!=32)|| ((tecla > 46 && tecla < 58 && tecla != 47) || tecla == 44)) return true; else event.keyCode = 0; }
    else
    { if((tecla<33 && tecla!=32)|| ((tecla > 46 && tecla < 58 && tecla != 47) || tecla == 44)) return true; else return false; }
}
//TODO Alberto 03/03/2011

//onkeypress="modalKeyHandler('panelAluno', 'formAluno:btnConsultar')"
function modalKeyHandler(modalClose, botaoClick) {
    var code = window.event.keyCode;
    if(code == 27) {
        Richfaces.hideModalPanel(modalClose);
        return false;
    }
    if (code == 13) {
        document.getElementById(botaoClick).click();
        return false;
    }
    return false;
}

/*
function temValor(  ){
    while ()
}
 */

function abrirPopupMaximizada(URL, nomeJanela) {
	abrirPopupMaximizado2(URL, nomeJanela, 1000, 1000);
    return false;
}

function alterarPopupParaTelaCheia(URL, nomeJanela) {
    var comprimento = screen.width;
    var altura = screen.height;
    window.moveTo(0,0);
    window.resizeTo(comprimento,altura);
    return false;
}

function mascaraTodos(objForm, strField, sMask, evtKeyPress) {
    var i, nCount, sValue, fldLen, mskLen,bolMask, sCod, nTecla;
    nTecla = (evtKeyPress.which) ? evtKeyPress.which : evtKeyPress.keyCode;
    sValue = objForm[strField].value;
    // Limpa todos os caracteres de formata��o que
    // j� estiverem no campo.
    expressao = /[\.\/\-\(\)\,\;\: ]/gi;
    sValue = sValue.toString().replace(expressao, '');
    fldLen = sValue.length;
    mskLen = sMask.length;

    i = 0;
    nCount = 0;
    sCod = "";
    mskLen = fldLen;

    while (i <= mskLen) {
        bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/") || (sMask.charAt(i) == ",") || (sMask.charAt(i) == ";") || (sMask.charAt(i) == ":"))
        bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " "))

        if (bolMask) {
            sCod += sMask.charAt(i);
            mskLen++; }
        else {
            sCod += sValue.charAt(nCount);
            nCount++;
        }

        i++;
    }

    objForm[strField].value = sCod;

    if (nTecla != 8 && nTecla != 13)
    { // backspace enter
        if (sMask.charAt(i-1) == "9")
        { // apenas n�meros...
            return ((nTecla > 47) && (nTecla < 58));
        } // n�meros de 0 a 9
        else
        {
            if (sMask.charAt(i-1) == "x")
            { // apenas letras... Sem espaco
                return ((nTecla > 64) && (nTecla < 123));
            } // maiusculas e minusculas de A a z sem acentos
            else
            { // qualquer caracter...
                return true;
            }
        }
    }
    else
    {
        return true;
    }
}


function checa_seguranca(pass,  campo){

    var senha = document.getElementById(pass).value;
    var entrada = 0;
    var resultadoado;


    if(senha.length < 6){
        entrada = entrada - 1;
    }

    if(!senha.match(/[a-z_]/i) || !senha.match(/[0-9]/)){
        entrada = entrada - 1;
    }

    if(!senha.match(/\W/)){
        entrada = entrada - 1;
    }

    if(entrada == 0){
        resultado = ' Seguran�a: <font color=\'#99C55D\'>Excelente</font>';
    } else if(entrada == -1){
        resultado = ' Seguran�a: <font color=\'#7F7FFF\'>Boa</font>';
    } else if(entrada == -2){
        resultado = ' Seguran�a: <font color=\'#A04040\'>Fraca</font>';
    } else if(entrada == -3){
        resultado = ' Seguran�a: <font color=\'#FF5F55\'>Muito Fraca</font>';
    }

    document.getElementById(campo).innerHTML = resultado;

    return;
}

function startDownload(url){
    window.open(url, "Download");
}

//window.onbeforeunload=teste;
//
//function teste(){
//
//    alert(window.event.click);
//    var target=document.getElementById("form:idLiberarBackingBeanMemoria");
//    if(target == null){
//        target = document.getElementById("formCadastro:idLiberarBackingBeanMemoria");
//    }
//    if (document.dispatchEvent) { // W3C
//        var oEvent = document.createEvent( "MouseEvents" );
//        oEvent.initMouseEvent("click", true, true,window, 1, 1, 1, 1, 1, false, false, false, false, 0, target);
//        target.dispatchEvent( oEvent );
//    } else {
//        if(document.fireEvent) { // IE
//            alert("Opcao IE")
//            target.fireEvent("onclick");
//        }else{
//            alert("Opcao 3")
//            document.getElementById(id).click();
//        }
//    }
//    return true;
//}

function fireElement(id) {
	
    var target=document.getElementById(id);
    if (document.dispatchEvent) { // W3C    
        var oEvent = document.createEvent( "MouseEvents" );
        oEvent.initMouseEvent("click", true, true,window, 1, 1, 1, 1, 1, false, false, false, false, 0, target);
        target.dispatchEvent( oEvent );
    } else {    	
    	document.getElementById(id).click();
//        if(document.fireEvent) { // IE
//        	alert(document.fireEvent);
//            target.fireEvent("onclick");
//        }else{
//
//            document.getElementById(id).click();
//        }
    }
    return true;
}


function valorMaximo(event, maxValue) {

	if(isNaN(event.key)){
		return false;
	}
	var input = Number(event.key);
	if(input > maxValue){
		event.target.value = maxValue;
	}else {
		event.target.value = input;
	}
	
	return true;
}




function executarClickBotao(idBotao) {
    jQuery(function($) {
        $(document).ready(function() {
            $("#form\\:"+idBotao).click();
        });
    });
}
function marcarLinhaSelecionada(nomeForm, linha, tabela, cor, vetor){
    var colunas = vetor.split(",");
    for (var j=0; j<colunas.length; j++){
        document.getElementById(nomeForm+':'+tabela+':'+linha+':'+colunas[j]).style.backgroundColor = cor;
    }
}
function desmarcarLinhaSelecionada(nomeForm, linha, tabela, cor, vetor){
    var colunas = vetor.split(",",vetor.length);
    for (var j=0; j<colunas.length; j++){
    	if(document.getElementById(nomeForm+':'+tabela+':'+linha+':'+colunas[j]) != null){
    		document.getElementById(nomeForm+':'+tabela+':'+linha+':'+colunas[j]).style.backgroundColor = cor;
    	}
    }
}

function marcarLinhaSelecionadaTabelaDinamica(nomeForm, linha, tabela, cor, vetor, qtdeColunas, nomeColunaDinamica){
    var colunas = vetor.split(",");
    var tamVetor = colunas.length;
    var novoVetor;
    if(qtdeColunas>0){
        tamVetor = tamVetor+(qtdeColunas-1);
        novoVetor = new Array(tamVetor);
        var numNovoVetor = 0;
        for(var i=0;i<colunas.length;i++){
            if(colunas[i] == nomeColunaDinamica){
                for(var k=0;k<=qtdeColunas;k++){
                    novoVetor[numNovoVetor] = colunas[i];
                    numNovoVetor++;
                }
            }else{
                novoVetor[numNovoVetor] = colunas[i];
                numNovoVetor++;
            }
        }
    }else{
        novoVetor = colunas;
    }
    var numRow = 0;
    for (var j=0; j<novoVetor.length; j++){
        document.getElementById(nomeForm+numRow+':'+tabela+':'+linha+':'+novoVetor[j]).style.backgroundColor = cor;
        if(qtdeColunas>0 && novoVetor[j] == nomeColunaDinamica && novoVetor[j+1] == nomeColunaDinamica){
            numRow++;
        }
    }
}

function desmarcarLinhaSelecionadaTabelaDinamica(nomeForm, linha, tabela, vetor, corPar, corImpar, qtdeColunas, nomeColunaDinamica){
    var colunas = vetor.split(",");
    var tamVetor = colunas.length;
    var novoVetor;
    if(qtdeColunas>0){
        tamVetor = tamVetor+(qtdeColunas-1);
        novoVetor = new Array(tamVetor);
        var numNovoVetor = 0;
        for(var i=0;i<colunas.length;i++){
            if(colunas[i] == nomeColunaDinamica){
                for(var k=0;k<=qtdeColunas;k++){
                    novoVetor[numNovoVetor] = colunas[i];
                    numNovoVetor++;
                }
            }else{
                novoVetor[numNovoVetor] = colunas[i];
                numNovoVetor++;
            }
        }
    }else{
        novoVetor = colunas;
    }
    var numRow = 0;
    for (var j=0; j<novoVetor.length; j++){
        if(linha%2 == 0){
            document.getElementById(nomeForm+numRow+':'+tabela+':'+linha+':'+novoVetor[j]).style.backgroundColor = corPar;
        }else{
            document.getElementById(nomeForm+numRow+':'+tabela+':'+linha+':'+novoVetor[j]).style.backgroundColor = corImpar;
        }
        if(qtdeColunas>0 && novoVetor[j] == nomeColunaDinamica && novoVetor[j+1] == nomeColunaDinamica){
            numRow++;
        }
    }
}



function focusNextCampo(nomeClasse){
    $('.'+nomeClasse).change(function() {
        $(this).closest("table").next("table").toggle(this.checked);
    });
}



function validarTamanhoUploadArquivo(e, idUpload, tamanho){
	if(e.memo.entry.size > (tamanho*1024*1024)){		    	 
    	alert('Arquivo n�o Enviado. Tamanho M�ximo Permitido '+tamanho+'MB.');
    	var entry = FileUploadEntry.getComponent(document.getElementById(idUpload+":fileItems"));		    	
    	entry.clear(entry, true); 
    	return false;		    			    	
    }
    return true;				
}	

function validarTamanhoUploadArquivoExtensao(e, idUpload, tamanho, extensao){
	if(e.memo.entry.size > (tamanho*1024*1024)){		    	 
    	alert('Arquivo n�o Enviado. Tamanho M�ximo Permitido '+tamanho+'MB.');
    	var entry = FileUploadEntry.getComponent(document.getElementById(idUpload+":fileItems"));		    	
    	entry.clear(entry, true); 
    	return false;		    			    	
    }
   
    if(extensao.indexOf("*") == -1 && extensao.indexOf(e.memo.entry.fileName.substring(e.memo.entry.fileName.lastIndexOf(".")+1, e.memo.entry.fileName.length)) == -1){
    	alert('Arquivo n�o Enviado. Extens�o inv�lida utilizar apenas as seguintes exten��es: '+extensao+'.');
    	var entry = FileUploadEntry.getComponent(document.getElementById(idUpload+":fileItems"));		    	
    	entry.clear(entry, true); 
    	return false;	    	
    }
    
    return true;				
}

function simularTab(field, event) {
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if (keyCode == 13) {
		var i;
		for (i = 0; i < field.form.elements.length; i++)
		if (field == field.form.elements[i])
		break;
		i = (i + 1) % field.form.elements.length;
		field.form.elements[i].focus();
		return false;
	}
	else
	return true;
}

function autoGrowTextArea(textField) {
  if (textField.clientHeight < textField.scrollHeight) {
    textField.style.height = textField.scrollHeight + "px";
    if (textField.clientHeight < textField.scrollHeight) {
      textField.style.height = (textField.scrollHeight * 2 - textField.clientHeight) + "px";
    }
  }
}

//MASCARA CEP
function MascaraCep(cep){
	if(mascaraInteiro(cep)==false){
	event.returnValue = false;
}	
return formataCampo(cep, '00.000-000', event);
}

//valida CEP
function ValidaCep(cep){	
	exp = /\d{2}\.\d{3}\-\d{3}/
	if(!exp.test(cep.value))
		alert('Numero de Cep Invalido!');		
}

function arrumaEnter (field, event) {
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if (keyCode == 13) {
	var i;
	for (i = 0; i < field.form.elements.length; i++)
	if (field == field.form.elements[i])
	break;
	i = (i + 1) % field.form.elements.length;
	field.form.elements[i].focus();
	return false;
	}
	else
	return true;
}

function limitarCampo(obj, total){
	return obj.value.length <= total;
}


function maskTelefone(campo , teclaPress) {
	somenteNumero1(teclaPress);
	var obj = document.getElementById(campo);
	var onlyNumber = obj.value.toString().replace(/\D/g,"");
	if (onlyNumber.length >= 4 && onlyNumber.length <= 7) { // 1234567 vira 123-4567
		obj.value = onlyNumber.substr(0, 3) + "-" + onlyNumber.substr(3);
	} else if (onlyNumber.length == 8) { // 12345678 vira 1234-5678
		obj.value = onlyNumber.substr(0, 4) + "-" + onlyNumber.substr(4, 4);
	} else if (onlyNumber.length == 9) { // 123456789 vira 12345-6789
		obj.value = onlyNumber.substr(0, 5) + "-" + onlyNumber.substr(5, 4);
	} else if (onlyNumber.length == 10) { // 1234567890 vira (12) 3456-7890
		if( onlyNumber.substr(0, 4) == '0800') {
			obj.value =  onlyNumber.substr(0, 4) + "-" +  onlyNumber.substr(4, 2) + "-" + onlyNumber.substr(6, 4);
		}else{
			obj.value = "(" + onlyNumber.substr(0, 2) + ") " + onlyNumber.substr(2, 4) + "-" + onlyNumber.substr(6, 4);	
		}
	} else if (onlyNumber.length == 11) { // 12345678901 vira (12) 34567-8901
		if( onlyNumber.substr(0, 4) == '0800') {
			obj.value =  onlyNumber.substr(0, 4) + "-" +  onlyNumber.substr(4, 3) + "-" + onlyNumber.substr(7, 4);
		}else{
			obj.value = "(" + onlyNumber.substr(0, 2) + ") " + onlyNumber.substr(2, 5) + "-" + onlyNumber.substr(7, 4);	
		}
	} else if (onlyNumber.length == 12) { // 123456789012 vira (12 34)5678-9012
		obj.value = "(" + onlyNumber.substr(0, 2) + " " + onlyNumber.substr(2, 2)  + ") " + onlyNumber.substr(4, 4) + "-" + onlyNumber.substr(8, 4);
	} else if (onlyNumber.length == 13) { // 1234567890123 vira (12 34)56789-0123
		obj.value = "(" + onlyNumber.substr(0, 2) + " " + onlyNumber.substr(2, 2) + ") " + onlyNumber.substr(4, 5) + "-" + onlyNumber.substr(9, 4);
	} else {
		obj.value = onlyNumber;
	} 
}

function somenteNumero1(event) {
	event = (event) ? event : window.event;
    var charCode = (event.which) ? event.which : event.keyCode;
    if ((charCode == 8) || (charCode > 31  && (charCode == 8 || charCode < 48 || charCode > 57))) {
        return false;
    }
    return true;
}

function simularAcessoCoordenador() {
//	window.close();
//	window.opener.close();
	//var  janela1 = window.open('academico.xhtml', 'academico.xhtml');
	
	
	window.open('../../visaoCoordenador/telaInicialVisaoCoordenador.xhtml', 'VisaoAdministrativa');
	                   
	
	//window.opener.location.href='../../visaoCoordenador/telaInicialVisaoCoordenador.xhtml';
	
}

function simularAcessoProfessor() {
//	window.close();
//	window.opener.close();
	//var  janela1 = window.open('academico.xhtml', 'academico.xhtml');
	
	
	window.open('../../visaoProfessor/telaInicialVisaoProfessor.xhtml', 'VisaoAdministrativa');
	
	//window.opener.location.href='../../visaoProfessor/telaInicialVisaoProfessor.xhtml';
	
}

function simularAcessoFichaAluno() {
//	window.close();
//	window.opener.close();
	//var  janela1 = window.open('academico.xhtml', 'academico.xhtml');
	
	
	window.open('../../visaoAluno/telaInicialVisaoAluno.xhtml', 'VisaoAdministrativa');
	
	//window.opener.location.href='../../visaoAluno/telaInicialVisaoAluno.xhtml';
	
}

function simularAcessoFichaAlunoAvaliacaoInst() {
	
	window.open('../../visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml', 'VisaoAdministrativa', 'left=0, top=0, width='+ (screen.width-10) +', height=' + (screen.height-110) + ', dependent=yes, maximize=yes, fullscreen=yes, scrollbars=yes');
	
//	window.close();
//	window.opener.close();
//	var  janela1 = window.open('academico.xhtml', 'academico.xhtml');
//	janela1.location.href='../../visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml';
//	closePopup();
}

function simularAcessoVisaoAluno() {
	
	window.open('../../visaoAluno/telaInicialVisaoAluno.xhtml', 'VisaoAdministrativa');
	
//	window.close();
//	window.opener.location.href='../../visaoAluno/telaInicialVisaoAluno.xhtml'
//	closePopup();
}

function simularAcessoVisaoAlunoAvaliacaoInst() {	
	window.open('../../visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml', 'VisaoAdministrativa');
	
//	window.close();
//	window.opener.location.href='../../visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml'
}

function validarData(dateString,campo) {
	if (dateString != "") {
	var patternValidaData = /^(((0[1-9]|[12][0-9]|3[01])([-.\/])(0[13578]|10|12)([-.\/])(\d{4}))|(([0][1-9]|[12][0-9]|30)([-.\/])(0[469]|11)([-.\/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([-.\/])(02)([-.\/])(\d{4}))|((29)(\.|-|\/)(02)([-.\/])([02468][048]00))|((29)([-.\/])(02)([-.\/])([13579][26]00))|((29)([-.\/])(02)([-.\/])([0-9][0-9][0][48]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][2468][048]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][13579][26])))$/;
	var ano = dateString.substr(6, 4);
	var result = patternValidaData.test(dateString);
	if (result == false){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();
		alert('A data informada é inválida. Formato válido: dd/MM/yyyy');
	}else if (ano < 1800){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();
		alert('Ano informado inválido, ano valido maior que 1800 ');
	}
}
};


function validarDataHora(dateString,campo) {
	if (dateString != "") {
	var patternValidaData = /^(((0[1-9]|[12][0-9]|3[01])([-.\/])(0[13578]|10|12)([-.\/])(\d{4}))|(([0][1-9]|[12][0-9]|30)([-.\/])(0[469]|11)([-.\/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([-.\/])(02)([-.\/])(\d{4}))|((29)(\.|-|\/)(02)([-.\/])([02468][048]00))|((29)([-.\/])(02)([-.\/])([13579][26]00))|((29)([-.\/])(02)([-.\/])([0-9][0-9][0][48]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][2468][048]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][13579][26]))) (2[0-3]|[0-1][0-9]):[0-5][0-9]$/;
	var result = patternValidaData.test(dateString);
	if (result == false){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();		
		alert('A data/hora informada é inválida. Formato válido: dd/MM/yyyy HH:mm');
	}
}
};

function validarDataHora(dateString,campo, mascara) {
	if (dateString != "") {
	var patternValidaData = /^(((0[1-9]|[12][0-9]|3[01])([-.\/])(0[13578]|10|12)([-.\/])(\d{4}))|(([0][1-9]|[12][0-9]|30)([-.\/])(0[469]|11)([-.\/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([-.\/])(02)([-.\/])(\d{4}))|((29)(\.|-|\/)(02)([-.\/])([02468][048]00))|((29)([-.\/])(02)([-.\/])([13579][26]00))|((29)([-.\/])(02)([-.\/])([0-9][0-9][0][48]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][2468][048]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][13579][26]))) (2[0-3]|[0-1][0-9]):[0-5][0-9]$/;
	var result = patternValidaData.test(dateString);
	if (result == false){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();		
		alert('A data/hora informada é inválida. Formato válido: '+mascara+'.');
	}
}
};

function validarDataMesAno(dateString,campo) {
	if (dateString != "") {
	var patternValidaData = /^((0[1-9])|(1[0-2]))\/((2000)|([1-2][0-1][0-9][0-9]))$/;
	var result = patternValidaData.test(dateString);
	if (result == false){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();	
		alert('A data informada é inválida.  Formato válido: MM/yyyy');
	}
}
};

function validarDataDiaMes(dateString,campo) {
	if (dateString != "") {
	var patternValidaData = /^(0[1-9]|[12][0-9]|3[01])\/((0[1-9])|(1[0-2]))$/;
	var result = patternValidaData.test(dateString);
	if (result == false){
		document.getElementById(campo).value='';
		alert('A data informada é inválida.  Formato válido: DD/MM');
	}
}
};
	
	
	function limitarCasasDecimais(obj, value, quantidadeCasaDecimal) {
		var novoValor;
		var valorInteiro;
		var valorDecimal;
			
		if (!value.replace(".", ",").indexOf(",") < 0) {
			valorInteiro = value;
			novoValor = valorInteiro;
		} else {
			valorInteiro = value.substring(0, value.replace(".", ",").indexOf(","));
			valorDecimal = value.substring(value.replace(".", ",").indexOf(",") + 1);
			if (quantidadeCasaDecimal != 0) {
				novoValor = valorInteiro + "," +  valorDecimal.substring(0, quantidadeCasaDecimal);
			} else {
				novoValor = valorInteiro;
			}
		}
		obj.value = novoValor;
	
	};	



function limitarCampos(obj, valor, total){ 
	if(valor.length > total){
	  valorInteiro = valor.substring(0, total);  
	 }else{
	  return true
	 }
	 obj.value = valorInteiro;
	};


function colorPicker() {	
	jQuery.noConflict();
	$(document).ready(function() {
		$(".colorPicker").spectrum({
		    preferredFormat: "hex",
		    showInput: true,
		    showPalette: true
		});
		  
		$(".colorPicker").show();
	});
};
	
	function bloquearNumericos(obj){
		obj.value = obj.value.replace(/[0-9/*+-.,]/g,"");
	};

	function focus(id){		
		if(document != null){
			var element = document.getElementById(id);
			if(element != null){
				element.focus();			
			}
		}
	};
	
	function showWebCamAPP() {
		
		Webcam.set({
			width: 320,
			height: 240,
			image_format: 'jpeg',
			jpeg_quality: 90
		});
		Webcam.attach( '#my_camera' );
			};
			
			function take_snapshot(caminhoServlet) {
				Webcam.snap( function(data_uri) {
		    					
                Webcam.upload( data_uri, caminhoServlet, function(code, text) {
                });
				Webcam.freeze();
				} );
			};


function validarHora(horaString, campo) {
	if (horaString != "") {
		var patternValidaData = /^([01][0-9]|2[0-3]):[0-5][0-9]$/;
		var result = patternValidaData.test(horaString);
		if (result == false) {
			document.getElementById(campo).value = '';
			alert('A hora informada \u00e9 inv\u00e1lida.');
		}
	}
};

function carregarTooltip() {
	jQuery.noConflict();
	jQuery( document ).ready(function( $ ) {
		tooltip();
	});
	tooltip = function() {		
		$('.tooltip').tooltipster({
			theme: 'tooltipster-shadow',
	    	trigger: 'click',
	    	interactive : 'true',
	    	multiple: true,
	    	side : 'left'
		});
	}
	
};

function removerPopup(popups){	
	var popup = new Array();
	popup = popups.split(";");		
	for (a in popup ) {		
		if(popup[a] != null && popup[a] != ""){
			var win = window.open(null, popup[a]);		
			win.close();	   
		}	    		
	}	
	popups="";
};

function fecharPopupEspecifico(nomeJanela){
	var win = window.open(null, nomeJanela);		
	win.close();	
};

function proximoFoco(event, sizeAluno, sizeNota, alunoAtual, notaAtual){
	 if ((event.which || event.keyCode) == 9) {
	if(alunoAtual == (sizeAluno - 1) && notaAtual == (sizeNota - 1)){
		document.getElementById('formHistoricoNotaParcialGeral:gravarLancamentoNotaParcial').focus();
		return;
	}
	if(alunoAtual == (sizeAluno - 1)){
		alunoAtual = 0;
		notaAtual++; 
	}else{
		alunoAtual++;
	}		
	document.getElementById('formHistoricoNotaParcialGeral:listaAlunoDetalhe:'+alunoAtual+':listaNotaDetalalhe:'+notaAtual+':notaParcial').focus();
	console.log('formHistoricoNotaParcialGeral:listaAlunoDetalhe:'+alunoAtual+':listaNotaDetalalhe:'+notaAtual+':notaParcial');
	return;
	 }
};


/**
 * Função que valida o pattern(maskara) da data 
 * 
 * @param objForm
 * @param id
 * @param maskara
 * @param evtKeyPress
 * @returns
 */
function mascaraData(objForm, id, maskara, evtKeyPress) {
	if(!somenteNumero(evtKeyPress)){
		return false;
	}
	var obj = document.getElementById(id);
	var valorDigitado = obj.value;
	var textoSel = getSelText();
	if (maskara.length === valorDigitado.length) {
		obj.value = valorDigitado.substring(0 , (valorDigitado.length - 1) );
    	return ;
    }

	contador = 0;
    valor = "";

	while (contador <= valorDigitado.length) {
		
		//o charCodeAt com o valor de 160 refere ao espaco(caracter) '&nbsp'.
		var contemCaracterEspecial = maskara.charAt(contador) == "/" || maskara.charAt(contador) == " " || maskara.charAt(contador) == ":" 
			|| maskara.charCodeAt(contador) == 160;
        
		//Se mascara na posicão do contador x possui caracter especial acrescenta o mesmo na string antes de adicionar o valor.
		if (contemCaracterEspecial) {
			if (maskara.charAt(contador) == "/") {
				valor += "/";
			} else if (maskara.charAt(contador) == " " || maskara.charCodeAt(contador) == 160) {
				valor += " ";
			} else if ( maskara.charAt(contador) == ":") {
				valor += ":";
			}
		} else {
			valor += valorDigitado.charAt(contador);
		}
		contador++;
    }

	obj.value = valor;
}

function validar_Data(Ncampo, pattern){
    var er = /^(((0[1-9]|[12][0-9]|3[01])([-.\/])(0[13578]|10|12)([-.\/])(\d{4}))|(([0][1-9]|[12][0-9]|30)([-.\/])(0[469]|11)([-.\/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([-.\/])(02)([-.\/])(\d{4}))|((29)(\.|-|\/)(02)([-.\/])([02468][048]00))|((29)([-.\/])(02)([-.\/])([13579][26]00))|((29)([-.\/])(02)([-.\/])([0-9][0-9][0][48]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][2468][048]))|((29)([-.\/])(02)([-.\/])([0-9][0-9][13579][26])))$/;
    b = document.getElementById(Ncampo).value;  
                   
        if (b.length == pattern.length){
        	if(pattern.toUpperCase().indexOf('DD/MM/YYYY') >= 0){
            var dia = b.substring(0,2);
            var mes = b.substring(3,5);
            var ano = b.substring(6,10);             
            if ((mes=="04" || mes=="06" || mes=="09" || mes=="11") && (dia > 30)) {
                document.getElementById(Ncampo).focus();   
                document.getElementById(Ncampo).value = "";
                alert("Data incorreta! O mês especificado contém no máximo 30 dias");
                return false;
            } else if ((ano%4!=0) && (mes=="02") && (dia>28)) {
                    document.getElementById(Ncampo).focus();
                    document.getElementById(Ncampo).value = "";
                    alert("Data incorreta! O mês especificado contém no máximo 28 dias.");
                    return false;
              } else if ((ano%4==0) && (mes=="02") && (dia>29)) {
                    document.getElementById(Ncampo).focus();
                    document.getElementById(Ncampo).value = "";
                    alert("Data incorreta! O mês especificado contém no máximo 29 dias.");                      
                    return false;
              } else if (dia>31) {
            	  document.getElementById(Ncampo).focus();
            	  document.getElementById(Ncampo).value = "";
            	  alert("Data incorreta! O dia especificado contém no máximo 31 dias.");                      
            	  return false;
              } else if (mes != "01" && mes != "02" && mes != "03" && mes != "04" && mes != "05" && mes != "06" && mes != "07"
             	 && mes != "08" && mes != "09" && mes != "10" && mes != "11" && mes != "12") {
                  document.getElementById(Ncampo).focus();   
                  document.getElementById(Ncampo).value = "";
                  alert("Data incorreta! O mês deve estar entre 01 e 12.");
                  return false;
              } else if (ano.length == 4 && ((ano < 1900) || (ano > 2199))) {
                  document.getElementById(Ncampo).focus();
                  document.getElementById(Ncampo).value = "";
                  alert("Data incorreta! O ano especificado não é valido.");              
                  return false;
              }
        	}
            if(pattern.indexOf('HH') >=0 || pattern.indexOf('hh') >=0){

            	var hora = pattern.indexOf('HH') >=0 ? b.substring(pattern.indexOf('HH'),pattern.indexOf('HH')+2) : b.substring(pattern.indexOf('hh'),pattern.indexOf('hh')+2) ;            	
            	if(hora > '24'){            		
            		document.getElementById(Ncampo).focus();
                    document.getElementById(Ncampo).value = "";
            		alert("Data incorreta! A hora ("+hora+") não pode ser maior que 24.");
            		 return false;
            	}
            }
            if(pattern.indexOf(':MM' >=0) || pattern.indexOf(':mm') >=0){            	
            	var minuto = pattern.indexOf(':MM') >=0 ? b.substring(pattern.indexOf(':MM')+1,pattern.indexOf(':MM')+3) : b.substring(pattern.indexOf(':mm')+1,pattern.indexOf(':mm')+3) ;
            	if(minuto > '59'){
            		document.getElementById(Ncampo).focus();
                    document.getElementById(Ncampo).value = "";
            		alert("Data incorreta! O minuto ("+minuto+") não pode ser maior que 59.");
            		 return false;
            	}
            }
            if(pattern.indexOf(':SS' >=0) || pattern.indexOf(':ss') >=0){
            	var segundo = pattern.indexOf(':SS') >=0 ? b.substring(pattern.indexOf(':SS')+1,pattern.indexOf(':SS')+3) : b.substring(pattern.indexOf(':ss')+1,pattern.indexOf(':ss')+3) ;
            	if(segundo > '59'){
            		document.getElementById(Ncampo).focus();
                    document.getElementById(Ncampo).value = "";
            		alert("Data incorreta! O segundo ("+segundo+") não pode ser maior que 59.");
            		 return false;
            	}
            }
              
            return true;     
    }else if(b.length != 0){
    	alert("Data incorreta! Informe a data no formato "+pattern);
    	document.getElementById(Ncampo).focus();
        document.getElementById(Ncampo).value = "";
        return false;   
    }
        return true;         
}

function navegacaoGoogleMeet(url) {
	window.open(url, '_blank', 'noreferrer');
}

function navegacaoIde(url) {
	window.open(url, '_blank', );
}

function validarAno(anoString,campo) {	
	if (anoString != "") {
	var ano = anoString;
	if (ano.length != 4){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();	
		alert('A data informada é inválida.  Formato válido: yyyy');
	}else if (ano < 1800){
		document.getElementById(campo).value='';
		document.getElementById(campo).focus();	
		alert('Ano informado inválido, ano valido maior que 1800 ');
	}
}
};

function scrollElemento(elemID){
    var offsetTrail = document.getElementById(elemID);
    var offsetTop = 0;
    while (offsetTrail) {
        offsetTop += offsetTrail.offsetTop;
        offsetTrail = offsetTrail.offsetParent;
    }
    if (navigator.userAgent.indexOf("Mac") != -1 && 
        typeof document.body.leftMargin != "undefined") {
        offsetTop += document.body.topMargin;
    }
    $('html, body').animate({
        scrollTop: (offsetTop - 240)
    }, 2000);
}

 function abrirPopupMaximizado3(URL, nomeJanela, comprimento, altura) {		 
	fecharPopupEspecifico(nomeJanela);
    var win = window.open(URL, nomeJanela, 'left=0, top=0, width='+ (comprimento-10) +', height=' + (altura-110) + ', dependent=yes, scrollbars=yes, focus=yes');          
    win.focus();       
}