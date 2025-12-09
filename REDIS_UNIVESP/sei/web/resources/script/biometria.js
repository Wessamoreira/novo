function realizarInicioCadastro(pessoa, idDedo) {
//	alert('Acessou');
	criarAppletBiometria();
	var applet = document.getElementById('appletBiometria');
	applet.realizarInicioCadastro(pessoa, idDedo);
}

function realizarCadastro() {
	var applet = document.getElementById('appletBiometria');
	applet.realizarCadastro();
	document.getElementById('form:idDedo').value = applet.getIdDedo();
	document.getElementById('form:idDigital').value = applet.getIdDigital();
	excluirAppletBiometria();
}

function realizarCancelamento() {
	var applet = document.getElementById('appletBiometria');
	applet.realizarCancelamento();
	excluirAppletBiometria();
}

function realizarExclusaoDigital(idDigital) {
	criarAppletBiometria();
	var applet = document.getElementById('appletBiometria');
	applet.realizarExclusaoDigital(idDigital);
	excluirAppletBiometria();
}

function realizarExclusaoDigitaisPessoa(pessoa) {
	criarAppletBiometria();
	var applet = document.getElementById('appletBiometria');
	applet.realizarExclusaoDigitaisPessoa(pessoa);
	excluirAppletBiometria();
}

function excluirAppletBiometria() {
	document.getElementById('formCadastroBiometrico:panelApplet').innerHTML = "";
}

function criarAppletBiometria() {
	excluirAppletBiometria();
	var app = document.createElement('applet');
	app.style= 'position: relative; top: 5px;';
	app.id= 'appletBiometria';
	app.archive= './applet/biometria.jar, ./applet/nkbiosol.jar, ./applet/nkhwsdk.jar';
	app.code= 'com.neokoros.applet.DemoApplet.class';
	app.width = '300';
	app.height = '200';
	app.codebase = './applet';
	param = document.createElement('param');
	param.name = 'server';
	param.value = document.getElementById('form:servidor').value;
//	alert(document.getElementById('form:servidor').value);
	app.appendChild(param);
	document.getElementById('formCadastroBiometrico:panelApplet').appendChild(app);
	alert('Clique em OK para cadastrar a digital.');
}

setInterval(function() {
	var applet = document.getElementById('appletBiometria');
	if (applet != null) {
		document.getElementById('mensagem').innerHTML = applet.getMensagemBio();
		document.getElementById('form:estadoBio').value = applet.getEstadoBio();
	}
}, 1000);