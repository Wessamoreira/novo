var selectedDocs = [];
	var selectDocuments = function () {
		selectedDocs = [];
		var docs = document.querySelectorAll("[docId]");
		docsArray = [];
		for (i = 0; i < docs.length; i++) {
			var docSel = docs[i].querySelector("[docSel]");
			var docKey = docs[i].querySelector("[docKey]");

			if (docSel.checked) {
				if (!docKey.value) {
					alert('Document Key must be filled');
					return;
				}
				selectedDocs.push(docs[i]);
				docsArray.push(docKey.value);
			}
		}
		return docsArray;
	};

	var updateSelectedDocuments = function (signStatus) {
		for (i = 0; i < selectedDocs.length; i++) {
			if (signStatus[i] == 1) {
				selectedDocs[i].querySelector("[status]").innerHTML = "&#x2714;";
				selectedDocs[i].querySelector("[status]").style.color = "green";
			} else if (signStatus[i] == 0) {
				selectedDocs[i].querySelector("[status]").innerHTML = "&#x2714;";
				selectedDocs[i].querySelector("[status]").style.color = "#c09853";
			} else if (signStatus[i] == -1) {
				selectedDocs[i].querySelector("[status]").innerHTML = "&#x2718;";
				selectedDocs[i].querySelector("[status]").style.color = "red";
			}
		}
	};

	var addDoc = function () {
		var docs = document.getElementById("docs");
		var doc = docs.querySelector("div:last-of-type");
		var clnDoc = doc.cloneNode(true);
		clnDoc.style.display = "";
		var docKey = clnDoc.querySelector("[docKey]");
		var docSel = clnDoc.querySelector("[docSel]");
		var docStatus = clnDoc.querySelector("[status]");
		var closeBtn = clnDoc.querySelector("[closeBtn]");
		closeBtn.style.display = "";
		var nameDoc = doc.querySelector("[doc]");
		var nameDocCln = clnDoc.querySelector("[doc]");
		var index = Number(nameDoc.attributes["name"].value)
		index++;
		nameDocCln.attributes["name"].value = index
		nameDocCln.innerText = 'Document ' + index
		docKey.value = '';
		docSel.checked = true;
		docStatus.innerText = "";
		docs.appendChild(clnDoc);
	};

	var removeDoc = function (doc) {
		doc.parentElement.removeChild(doc);
	};

	var SignBatchDocumentsString = function () {
		var docs = document.querySelector('#docsToSign').value;
		return SignBatchDocuments(docs);
	};

	var SignBatchDocumentsArray = function () {
		var docs = selectDocuments();
		if (!docs) return;
		SignBatchDocuments(docs);
	};

	//Sample function to handle the single signature response
	var singleSignature = function (signStatus) {
		//Handling if response is an array
		Array.isArray(signStatus) && (signStatus = signStatus[0]);
		if (signStatus > 0) {
			registrarAssinaturaSEI();
		} else if (signStatus == 0) {
			registrarAssinaturaSEI();
		} else {
			alert('Signature error.');
		}
	};

	var batchSignatureMessage = function (signStatus) {
		//Handling if response is an array
		Array.isArray(signStatus) && (signStatus = signStatus[0]);
		if (signStatus > 0) {
			return "Successful signature!";
		} else if (signStatus == 0) {
			return "Already signed!";
		} else {
			return 'Signature error.';
		}
	};

	//Sample function to handle the batch signature response, updating main page if it's array or just showing an alert with status if it's string
	var batchSignature = function (signStatus) {
		if (Array.isArray(signStatus)) {
			updateSelectedDocuments(signStatus);
		} else {
			signStatus = signStatus.toString();
			var messages = "";
			var status = signStatus.split(",");

			for (var i = 0; i < status.length; i++) {
				var message = batchSignatureMessage(status[i]);
				messages += ('Code: ' + status[i] + ' - Message: ' + message + '\n');
			}
			alert('Status response from signatures \n\n' + messages);
		}
	};

	//Sample function for signing single document.
	var SignSingleDocument = function (urlArquivo) {
		var url = urlArquivo;		
		if (!url) {
			alert('The signUrl param must be filled.');
			return;
		}

		//This is the object which configures the message to confirm the closing action. If omitted, the default will be used.
		var confirmConfig = {
			textOkButton: 'Sim',
			textCancelButton: 'Não',
			textMessage:
				'Interromper o processo de assinatura causará inconsistência no status da assinatura. No entanto, da próxima vez que tentar assinar o status será atualizado. Deseja realmente interromper o processo de assinatura?' 
		};

		var height = url.indexOf('AssinarEletronicoFrame') > -1
			? 830
			: 600;
		certisignIntegration = new window.CertisignIntegration(url, 850, height, false, confirmConfig).sign()
			.onSignCompleted(function (signStatus, statusObj) {
				
				singleSignature(signStatus);
				
			});
	};

	//Sample function for signing a batch document
	var SignBatchDocuments = function (docs) {
		var signerIndividualIdentificationCode = document.querySelector('#individualIdentificationCode').value;
		var url = document.querySelector('#batchSignUrl').value;

		if (!url) {
			alert('The batchSignUrl param must be filled.');
			return;
		}
		if (!signerIndividualIdentificationCode) {
			alert('The signer param must be filled.');
			return;
		}
		if (!docs) {
			alert('The doclist param must be filled.');
			return;
		}


		//----------------------------------------------------------------------------------------------------------------------------
		//    *** CertisignIntegration constructor Params ***
		//----------------------------------------------------------------------------------------------------------------------------
		//    - url [string]: Use <server>/Assinatura/assinarloteframe to batch signature or the completed url to sign a sigle document
		//    - width [int]: modal width
		//    - heigth [int]: modal height
		//    - closeOnFinish [bool]: Closes the modal on finishing signature
		//    - confirmCloseConfig [object]: Configures the message to confirm the closing action. If omitted, the default will be used.
		//    - disableCloseInProcessing [bool]: Defines the close action behaviour
		//          true: Disables the close action ('X' in the top-right corner of the modal), preventing its closing.
		//          false: Allows close without any confimation
		//          null or undefined: Shows the confimation massage defined in 'confirmCloseConfig' param
		//----------------------------------------------------------------------------------------------------------------------------
		var confirmConfig = {
			textOkButton: 'Sim',
			textCancelButton: 'Não',
			textMessage:
				'Interromper o processo de assinatura causará inconsistência nos status das assinaturas já efetuadas. No entanto, da próxima vez que tentar assinar os status serão atualizados. Deseja realmente interromper o processo de assinatura em lote?'
		};

		certisignIntegration = new window.CertisignIntegration(url, 400, 400, false, confirmConfig)
			.sign(docs, signerIndividualIdentificationCode)
			.onSignCompleted(function (signStatus, statusObj) {
				batchSignature(signStatus);
			});
	};

	function mascaraCertsign(o, f) {
		v_obj = o;
		v_fun = f;
		setTimeout("execmascara()", 1);
	}

	function execmascara() {
		v_obj.value = v_fun(v_obj.value)
	}

	function mcnpj(v) {
		v = v.replace(/\D/g, "");
		v = v.replace(/^(\d{2})(\d)/, "$1.$2");
		v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
		v = v.replace(/\.(\d{3})(\d)/, ".$1/$2");
		v = v.replace(/(\d{4})(\d)/, "$1-$2");
		return v
	}

	function mcpf(v) {
		v = v.replace(/\D/g, "");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
		return v
    }