var startQueue = null;
var performQueue = null;
var completeQueue = null;
var formElements = {};
var batchSignatureRestForm = (function () {
	(function () {
		window.Queue = function () {
			this.items = [];
			this.writerCount = 0;
			this.readerCount = 0;
		};
		window.Queue.prototype.add = function (e) {
			this.items.push(e);
		};
		window.Queue.prototype.addRange = function (array) {
			for (var i = 0; i < array.length; i++) {
				this.add(array[i]);
			}
		};
		var _process = function (inQueue, processor, outQueue, endCallback) {
			var obj = inQueue.items.shift();
			if (obj !== undefined) {
				processor(obj, function (result) {
					if (result != null && outQueue != null) {
						outQueue.add(result);
					}
					_process(inQueue, processor, outQueue, endCallback);
				});
			} else if (inQueue.writerCount > 0) {
				setTimeout(function () {
					_process(inQueue, processor, outQueue, endCallback);
				}, 200);
			} else {
				--inQueue.readerCount;
				if (outQueue != null) {
					--outQueue.writerCount;
				}
				if (inQueue.readerCount == 0 && endCallback) {
					endCallback();
				}
			}
		};
		window.Queue.prototype.process = function (processor, options) {
			var threads = options.threads || 1;
			this.readerCount = threads;
			if (options.output) {
				options.output.writerCount = threads;
			}
			for (var i = 0; i < threads; i++) {
				_process(this, processor, options.output, options.completed);
			}
		};
	})();
})();


var pki = new LacunaWebPKI()
var extensaoLacunaInstaladaNavegador = false;
var quantidadeXmlAssinar = 0;
var quantidadeXmlProcessado = 0;
var quantidadeXmlAssinado = 0;
var urlAcessoExternoAplicacao = "";
var ocorreuErro = false;

/*
	function com a finalidade de validar se a extensão da LACUNA está instalada no navegador do usuário,
	sendo que a extensão é suportada apenas para o (CHROME, EDGE, FIREFOX)
	autor:   Felipi Alves
	chamado: 42281
*/
function isExtensionInstalledLacuna() {
	$._chromeExtensionId = 'dcngeagmmhegagicpcmpinaoklddcgon';
	$._firefoxExtensionId = 'webpki@lacunasoftware.com';
	$._edgeExtensionId = 'd2798a85-9698-425a-add7-3db79a39ca8a';
	var meta = document.getElementById($._chromeExtensionId) || document.getElementById($._firefoxExtensionId.replace(/[^A-Za-z0-9_]/g, '_')) || document.getElementById($._edgeExtensionId);
	if (meta === null) {
		return false;
	} else {
		return true;
	}
}

function validarExtensaoLacunaInstaladaNavegador() {
	extensaoLacunaInstaladaNavegador = isExtensionInstalledLacuna();
}

/*
	function para atualizar o valor do boolean "extensaoLacunaInstalada" em "MapaDocumentoAssinadoPessoaControle" 
	na tela mapaDocumentoAssinadoPessoaModal.xhtml
	autor:   Felipi Alves
	chamado: 42281
*/
function alterarValorBooleanExtensaoLacunaInstalada() {
	document.getElementById('formConfirmarAssinaturaXml:extensaoLacunaInstalada').value = extensaoLacunaInstaladaNavegador;
	renderedizarFormConfirmarAssinatura();
}

/*
	function para verificar qual o navegador que o usuario está logado, ao saber qual o navegador
	vai atualizar a String "navegadorLogado" em "MapaDocumentoAssinadoPessoaControle" 
	na tela mapaDocumentoAssinadoPessoaModal.xhtml
	autor:   Felipi Alves
	chamado: 42281
*/
function verificarNavegador() {
	let navegador = "";
	if ((navigator.userAgent.indexOf("Opera") || navigator.userAgent.indexOf('OPR')) != -1) {
		navegador = 'OPERA';
	} else if (navigator.userAgent.indexOf("Edg") != -1) {
		navegador = 'EDGE';
	} else if (navigator.userAgent.indexOf("Chrome") != -1) {
		navegador = 'CHROME';
	} else if (navigator.userAgent.indexOf("Safari") != -1) {
		navegador = 'SAFARI';
	} else if (navigator.userAgent.indexOf("Firefox") != -1) {
		navegador = 'FIREFOX';
	} else if ((navigator.userAgent.indexOf("MSIE") != -1) || (!!document.documentMode == true)) {
		navegador = 'IE';
	} else {
		navegador = 'UNKNOWN';
	}
	document.getElementById('formConfirmarAssinaturaXml:navegadorUsuarioLogado').value = navegador;
	renderedizarFormConfirmarAssinatura();
}

/*
	function para atualizar o valor da String "jsonListaCertificadoInstalado" em "MapaDocumentoAssinadoPessoaControle" 
	na tela mapaDocumentoAssinadoPessoaModal.xhtml, e após será convertida em uma lista para a apresentação na tela
	autor:   Felipi Alves
	chamado: 42281
*/
function alterarValorListaCertificadosLacunaInstalados(certs) {
	if (certs != null) {
		document.getElementById('formConfirmarAssinaturaXml:jsonListaCertificadosLacunaInstalados').value = JSON.stringify(certs);
	} else {
		document.getElementById('formConfirmarAssinaturaXml:jsonListaCertificadosLacunaInstalados').value = "";
	}
	montarListaCertificadosLacunaInstalados();
}

/*
	function que será chamada pela tela e tem como a sua finalidade consultar os certificados instalados na maquina
	do usuario, e após verificar que existe e vai pegar esses certificados e enviar um json com os certificados para a tela
	para tratar e montar a lista de certificados corretamente
	autor:   Felipi Alves
	chamado: 42281
*/
function consultarCertificadosInstaladosLacuna() {
	validarExtensaoLacunaInstaladaNavegador();
	alterarValorBooleanExtensaoLacunaInstalada();
	verificarNavegador();
	if (extensaoLacunaInstaladaNavegador) {
		pki.init(onWebPkiReady);
		function onWebPkiReady() {
			pki.listCertificates().success(function(certs) {
				alterarValorListaCertificadosLacunaInstalados(certs);
			});
		}
	}
	renderedizarFormConfirmarAssinatura();
}

/*
	function que vai executar o jsFunction na tela "mapaDocumentoAssinadoPessoa.xhtml" que por sua vex realizara o encerramento
	do progress bar que está sendo executdado no controlado
	autor:   Felipi Alves
	chamado: 42281
*/
function realizarCancelamentoProcessoAssinaturaXmlLacuna() {
	pki = null;
	extensaoLacunaInstaladaNavegador = null;
	quantidadeXmlAssinar = null;
	quantidadeXmlProcessado = null;
	quantidadeXmlAssinado = null;
	urlAcessoExternoAplicacao = null;
	ocorreuErro = null;
    cancelarProgressBarAssinaturaXmlLacuna();
}

/*
	function responsavel pelo inicio da assinatura dos xmls selecionados na tela "mapaDocumentoAssinadoPessoa.xhtml"
	autor:   Felipi Alves
	chamado: 42281
*/
function iniciarAssinaturaLoteXmlLacuna() {
	try {
		// json de uma lista com o código de todos os documentos assinados selecionados que o xml serão assinados
		const jsonListaDocumentoAssinadoAssinar = document.getElementById('form:jsonListaDocumentoAssinadoAssinarLacuna').value;
		
		// thumbprintCertificado: string com o thumbprint do certificado selecionado, thumbprint que por sua vez é o atributo utilizado para realizar a asinatura no xml
		const thumbprintCertificado = document.getElementById('formConfirmarAssinaturaXml:thumbprintCertificadoSelecionado').value;
		
		// ordemAssinaturaAssinarXmlLacuna: string com a ordem de assinatura que está sendo realizado a assinatura dos xmls assinados
		const ordemAssinaturaAssinarXmlLacuna = document.getElementById('form:ordemAssinaturaAssinarXmlLacuna').value;
		
		// codigoPessoaLogadaAssinarXmlLacuna: código da pessoa logada que é a responsavel pela assinatura do xml
		const codigoPessoaLogadaAssinarXmlLacuna = document.getElementById('form:codigoPessoaLogadaAssinarXmlLacuna').value;
		
		// urlAcessoExternoAplicacao: url que será utilizada para a conexão do javaScript com o servlet DocumentoAssinadoRS, que irá fazer diretamente as chamadas de API da lacuna
		urlAcessoExternoAplicacao = document.getElementById('form:urlAcessoExternoAplicacaoSei').value;
		
		// listaDocumentoAssinadoAssinar: lista de documentos assinados que serão assinados
		var listaDocumentoAssinadoAssinar = null;
		
		quantidadeXmlAssinar = 0;
		quantidadeXmlProcessado = 0;
		ocorreuErro = false;
		if (jsonListaDocumentoAssinadoAssinar != null && jsonListaDocumentoAssinadoAssinar != "" && thumbprintCertificado != null && thumbprintCertificado != "") {
			listaDocumentoAssinadoAssinar = JSON.parse(jsonListaDocumentoAssinadoAssinar);
			
			// preauthorizeSignatures: function da extensão da LACUNA que vai informar a propria qual certificado vai ser utilizado para assinar e quantos documentos serão assinados
			pki.preauthorizeSignatures({
				certificateThumbprint: thumbprintCertificado,
				signatureCount: listaDocumentoAssinadoAssinar.length
			}).success(startBatch(listaDocumentoAssinadoAssinar, thumbprintCertificado, ordemAssinaturaAssinarXmlLacuna, codigoPessoaLogadaAssinarXmlLacuna));
		}
	} catch (error) {
		alert(error);
		realizarCancelamentoProcessoAssinaturaXmlLacuna();
	} finally {
		jsonListaDocumentoAssinadoAssinar = null;
		thumbprintCertificado = null;
		ordemAssinaturaAssinarXmlLacuna = null;
		codigoPessoaLogadaAssinarXmlLacuna = null;
		listaDocumentoAssinadoAssinar = null;
	} 
}

function startBatch(listaDocumentoAssinadoAssinar, thumbprint, ordem, codigoPessoa) {
	quantidadeXmlAssinar = listaDocumentoAssinadoAssinar.length;
	startQueue = new Queue();
	performQueue = new Queue();
	completeQueue = new Queue();

	for (var i = 0; i < listaDocumentoAssinadoAssinar.length; i++) {
		const dados = { 
			codigoDocumentoAssinado: listaDocumentoAssinadoAssinar[i], 
			thumbprintCertificado: thumbprint, 
			ordemAssinatura: ordem, 
			codigoPessoaLogada: codigoPessoa, 
		};
		startQueue.add({index: i, docAssinar: dados});
	}
	startQueue.process(startSignature, {threads: 10, output: performQueue});
	performQueue.process(performSignature, {threads: 10, output: completeQueue});
	completeQueue.process(completeSignature, {threads: 10, completed: onBatchCompleted});
}

function startSignature(step, done) {
	quantidadeXmlProcessado = quantidadeXmlProcessado+1
	if (document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml') != null) {
		document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml').innerText = "Processando " + quantidadeXmlProcessado + " de " + quantidadeXmlAssinar;
	}
	if (ocorreuErro) {
		return;
	}
	fetch((urlAcessoExternoAplicacao + '/webservice/documentoAssinadoRS/xml/inicializaAssinaturaXmlDocumentoAssinado'), {
		method: 'POST',
	  	headers: {
	    	'Content-Type': 'application/json'
	  	},
	  	body: JSON.stringify(step.docAssinar)
	}).then(response => {
		if (!response.ok) {
        	return null;
    	}
	    return response.json();
	}).then(data => {
		if (data != null) {
			done(data)
		} else {
			done()
		}
	}).catch(error => {
		if (error.message == "Failed to fetch") {
			if (!ocorreuErro) {
				alert("Erro ao realizar a assinatura do xml, favor valide a URL Acesso Externo Aplicação na configuração geral sistema padrão, certifique que a url seja a mesma do site");
			}
			if (document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml') != null) {
				document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml').innerText = "Realizando o cancelamento das assinaturas, por favor aguarde a finalização do cancelamento que vai demorar de acordo com o grande número de documentos selecionados";
			}	
			ocorreuErro = true;
			realizarCancelamentoProcessoAssinaturaXmlLacuna();
			window.location.reload(true);
		} else {
	  		done();	
		}
  	});
}

function performSignature(step, done) {
	if (ocorreuErro) {
		return;
	}
	
	// signWithRestPki: function da extensão da LACUNA que vai autorizar o documento de acordo com o "tokenLacuna" para realizar a assinatura, que será feita no servlet
	pki.signWithRestPki({
		token: step.tokenLacuna,
		thumbprint: step.thumbprintCertificado
	}).success(function () {
		done(step);
	}).error(function (error) {
		if (!ocorreuErro) {
			if (error != null && error.trim() == "O conjunto de chaves não está definido.") {
				alert("Não foi encontrado o certificado A3 para realizar a assinatura");
			} else {
				alert(error);
			}
		}
		if (document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml') != null) {
			document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml').innerText = "Realizando o cancelamento das assinaturas, por favor aguarde a finalização do cancelamento que vai demorar de acordo com o grande número de documentos selecionados";
		}
		ocorreuErro = true;
		realizarCancelamentoProcessoAssinaturaXmlLacuna();
		window.location.reload(true);
	});
}

function completeSignature(step, done) {
	fetch((urlAcessoExternoAplicacao + '/webservice/documentoAssinadoRS/xml/finalizaAssinaturaXmlDocumentoAssinado'), {
		method: 'POST',
	  	headers: {
	    	'Content-Type': 'application/json'
	  	},
	  	body: JSON.stringify(step)
	}).then(response => {
	    return response.json();
	}).then(data => {
		quantidadeXmlAssinado = quantidadeXmlAssinado + 1;
		if (document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml') != null) {
			document.getElementById('form:panelProgressBarAssinarXml:labelpanelProgressBarAssinarXml').innerText = "Processando (" + quantidadeXmlProcessado + " de " + quantidadeXmlAssinar + ") - Assinados (" + quantidadeXmlAssinado + " de " + quantidadeXmlAssinar + ")";
		}
		done(step);
	}).catch(error => {
  		done();
	});
}

function onBatchCompleted() {
	if (quantidadeXmlAssinar < 0) {
		quantidadeXmlAssinar = 0;
	}
	if (quantidadeXmlProcessado == quantidadeXmlAssinar) {
		realizarCancelamentoProcessoAssinaturaXmlLacuna();
		window.location.reload(true);
	}
}