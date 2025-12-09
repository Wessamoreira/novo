var preInscricaoModule = angular.module('matriculaOnlineExternaApp', ['ui.bootstrap', 'ngRoute', 'chieffancypants.loadingBar', 'ngAnimate', 'ui.mask']);

var urlBase = "http://localhost:8080/UNIVESP_1.0.0/webservice/matriculaOnlineExternaRS";
var urlBaseWebServices = "http://localhost:8080/UNIVESP_1.0.0/webservice";

preInscricaoModule.config(['$httpProvider', '$routeProvider', function ($httpProvider, $routeProvider) {
	$routeProvider.
		when('/', {
			templateUrl: 'views/cursos.html?uid=20170915',
			controller: 'cursoControle'
		}).
		when('/preInscricao', {
			templateUrl: 'views/preInscricao.html?uid=20170915',
			controller: 'preIncricaoControle'
		}).
		when('/matricula', {
			templateUrl: 'views/matricula.html?uid=20170915',
			controller: 'matriculaControle'
		}).
		when('/realizarPagamento', {
			templateUrl: 'views/realizarPagamento.html?uid=20170915',
			controller: 'pagamentoControle'
		}).
		when('/contratoMatriculaExterna', {
			templateUrl: 'views/contratoMatriculaExterna.html?uid=20170915',
			controller: 'pagamentoControle'
		}).
		otherwise({
			redirectTo: '/'
		});
}]);

preInscricaoModule.config(function (cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = true;
});

preInscricaoModule.run(function ($rootScope) {
	$rootScope.$on('scope.stored', function (event, data) {
		console.log("scope.stored", data);
	});
});


preInscricaoModule.controller('cursoControle', function ($scope, $http, $location, Scopes, $timeout, cfpLoadingBar) {

	Scopes.store('cursoControle', $scope);
	$scope.banners = [];
	$scope.myInterval = 3000;

	/**
	 *BANNERS
	 **/
	if ($scope.codigoCurso == null || $scope.codigoCurso == 0 || $scope.codigoBanner == null || $scope.codigoBanner == 0) {	
		$http.get(urlBase + '/banners').
			success(function (data) {
				if(data.banner != null){				
					$scope.banners = [].concat(data.banner);
				}else{
					$scope.banners = [].concat(data);
				}			
				console.log(data);
			}).error(function (error) {
				alert(error.mensagem);
		});
	}else{
		$location.path('/preInscricao');
	};

	/**
	 * SLIDER
	 **/

	$scope.consultarCurso = function consultarCurso(codigoCurso, codigoBanner) {
		cfpLoadingBar.start();		
		$scope.codigoCurso = codigoCurso;
		$scope.codigoBanner = codigoBanner;
		$location.path('/preInscricao');
		cfpLoadingBar.complete();
	};
});


preInscricaoModule.controller('preIncricaoControle', function ($scope, $http, $location, Scopes, $timeout, cfpLoadingBar) {

	Scopes.store('preIncricaoControle', $scope);
	$scope.pessoaObject = {};
	if(Scopes.get('cursoControle').codigoCurso == null || Scopes.get('cursoControle').codigoCurso == 0){
		alert("Curso n\u00e3o selecionado.");
		$location.path('/');
	}
	$http.get(urlBase + '/consultarCurso/' + Scopes.get('cursoControle').codigoCurso).
		success(function (data) {
			$scope.curso = data;
			var dis = [].concat(data.gradeDisciplina.disciplinas);
			$scope.curso.gradeDisciplina.disciplinas = dis;
			console.log($scope.curso);
		}).error(function (status) {
			console.log(status);
		});
	/**
	 *Cadastrar Pré-Inscrição
	 **/
	$scope.cadastrarPreInscricao = function cadastrarPreInscricao() {
		 if ($scope.pessoaObject.nome == null || $scope.pessoaObject.nome.length == 0) {			
			alert("O campo NOME deve ser informado!");
			return;
		}else if($scope.pessoaObject.email == null || $scope.pessoaObject.email.indexOf('@') == -1  || $scope.pessoaObject.email.indexOf('.') == -1 || $scope.pessoaObject.length == 0){
			alert("Digite um e-mail v\u00e1lido!");
			return;
		}
		cfpLoadingBar.start();
		$scope.pessoaObject.codigoCurso = Scopes.get('cursoControle').codigoCurso;
		var parametros = JSON.stringify($scope.pessoaObject);
		$http.post(urlBase + '/cadastrarPreInscricao', parametros, { headers: { 'Content-Type': 'application/json' } }).
			success(function (data) {
				$scope.pessoaObject = data;
				alert("Pr\u00e9-Inscri\u00e7\u00e3o realizada com Sucesso");
				cfpLoadingBar.complete();
				$location.path('/matricula');
			}).error(function (error) {
				console.log(error);
				alert(error.mensagem);
			});
	};

	$scope.voltarTelaInicial = function voltarTelaInicial() {
		$location.path('/');
	};
});

preInscricaoModule.controller('matriculaControle', function ($scope, $http, $location, Scopes, $timeout, cfpLoadingBar) {
	if(Scopes.get('preIncricaoControle') == null){
		$location.path('/');
	}
	if(Scopes.get('preIncricaoControle').curso == null || Scopes.get('preIncricaoControle').curso == 0){
		alert("Curso n\u00e3o selecionado.");
		$location.path('/');
	}
	if(Scopes.get('preIncricaoControle').pessoaObject == null || Scopes.get('preIncricaoControle').pessoaObject.nome == null  || Scopes.get('preIncricaoControle').pessoaObject.nome.length == 0){
		alert("Pessoa n\u00e3o localizada.");
		$location.path('/');
	}
	$scope.estados = [];
	$scope.cidades = [];
	$scope.pessoaObject = Scopes.get('preIncricaoControle').pessoaObject;
	consultarEstado($scope);
	Scopes.store('matriculaControle', $scope);
	console.log($scope.pessoaObject);
	$scope.curso = Scopes.get('preIncricaoControle').curso;
	$scope.codigoBanner = Scopes.get('cursoControle').codigoBanner;
	$http.get(urlBase + '/consultarDadosParaRealizarMatriculaOnlineExterna/' + Scopes.get('cursoControle').codigoCurso + '/' + Scopes.get('cursoControle').codigoBanner + '/' + $scope.pessoaObject.codigo).
		success(function (data) {
			$scope.matricula = data;
			console.log($scope.matricula);
			if($scope.matricula.mensagem != ''){
				console.log($scope.matricula.mensagem);
				alert($scope.matricula.mensagem);
			}else{
				if ($scope.matricula.possuiMaisDeUmaUnidadeEnsino == 'true' || $scope.matricula.unidadeEnsinos[0] != null) {
					$scope.matricula.codigoUnidadeEnsino = $scope.matricula.unidadeEnsinos[0].codigo;
				} else {				
					$scope.matricula.codigoUnidadeEnsino = $scope.matricula.unidadeEnsinos.codigo;				
				}
				if ($scope.matricula.possuiMaisDeUmTurno == 'true' || $scope.matricula.turnos[0] != null) {
					$scope.matricula.codigoTurno = $scope.matricula.turnos[0].codigo;
				} else {
					$scope.matricula.codigoTurno = $scope.matricula.turnos.codigo;
				}
				if ($scope.matricula.possuiMaisDeUmProcessoMatricula == 'true' || $scope.matricula.processoMatriculas[0] != null) {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas[0].codigo;
				} else {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas.codigo;
				}
				if ($scope.matricula.possuiMaisDeUmaTurma == 'true' || $scope.matricula.turmas[0] != null) {
					$scope.matricula.codigoTurma = $scope.matricula.turmas[0].codigo;
				} else {
					$scope.matricula.codigoTurma = $scope.matricula.turmas.codigo;
				}
				if (Number.parseInt($scope.matricula.codigoTurma) > 0) {
					$scope.atualizarDadosQuandoTurmaAlterado();
				}
				if ($scope.matricula.possuiMaisDeUmaCondicaoDePagamento == 'true' || ($scope.matricula.condicaoPagamentos != undefined && $scope.matricula.condicaoPagamentos[0] != null)) {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos[0].codigo;
					angular.forEach($scope.matricula.condicaoPagamentos, function (value, key) {
						if (value.codigo == $scope.matricula.codigoCondicaoPagamento) {
							$scope.condicaoPagamento = value;
						}
					});
				} else {
					if ($scope.matricula.condicaoPagamentos != undefined) {
						$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos.codigo;									
					}				
					$scope.condicaoPagamento = $scope.matricula.condicaoPagamentos;
				}
				if($scope.pessoaObject.cidade.estado.codigo.length > 0){
					consultarCidade($scope);
				}else if($scope.estados.length > 0){
					$scope.pessoaObject.cidade.estado.codigo = $scope.estados[0].codigo;
					consultarCidade($scope);
				}
			}
		}).error(function (status) {
			console.log(status);
		});

	$scope.atualizarDadosQuandoTurnoAlterado = function atualizarDadosQuandoTurnoAlterado() {
		cfpLoadingBar.start();
		$http.get(urlBase + '/atualizarDadosQuandoTurnoAlterado/' + $scope.matricula.codigoUnidadeEnsino + '/' + $scope.curso.codigo + '/' + $scope.matricula.codigoTurno + '/' + $scope.curso.gradeDisciplina.codigo + '/' + $scope.codigoBanner + '/' + $scope.pessoaObject.codigo).
			success(function (data) {
				$scope.matricula = data;
				if ($scope.matricula.possuiMaisDeUmaTurma == 'true' || $scope.matricula.turmas[0] != null) {
					$scope.matricula.codigoTurma = $scope.matricula.turmas[0].codigo;
				} else {
					$scope.matricula.codigoTurma = $scope.matricula.turmas.codigo;
				}
				
				if ($scope.matricula.possuiMaisDeUmaCondicaoDePagamento == 'true' || $scope.matricula.condicaoPagamentos[0] != null) {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos[0].codigo;
					angular.forEach($scope.matricula.condicaoPagamentos, function (value, key) {
						if (value.codigo == $scope.matricula.codigoCondicaoPagamento) {
							$scope.condicaoPagamento = value;
						}
					});
				} else {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos.codigo;
					$scope.matricula.condicaoPagamentos = $scope.condicaoPagamento;
				}
				if ($scope.matricula.possuiMaisDeUmProcessoMatricula == 'true' || $scope.matricula.processoMatriculas[0] != null) {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas[0].codigo;
				} else {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas.codigo;
				}
			}).error(function (status) {
				console.log(status);
			});
		cfpLoadingBar.complete();
	};

	$scope.atualizarDadosQuandoTurmaAlterado = function atualizarDadosQuandoTurmaAlterado() {
		cfpLoadingBar.start();
		$http.get(urlBase + '/atualizarDadosQuandoTurmaAlterado/' + $scope.codigoUnidadeEnsino + '/' + $scope.curso.codigo + '/' + $scope.codigoTurno + '/' + $scope.curso.gradeDisciplina.codigo + '/' + $scope.codigoBanner + '/' + $scope.pessoaObject.codigo + '/' + $scope.codigoTurma).
			success(function (data) {
				$scope.matricula = data;
				if ($scope.matricula.possuiMaisDeUmaCondicaoDePagamento == 'true' || ($scope.matricula.condicaoPagamentos != undefined && $scope.matricula.condicaoPagamentos[0] != null)) {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos[0].codigo;
					angular.forEach($scope.matricula.condicaoPagamentos, function (value, key) {
						if (value.codigo == $scope.matricula.codigoCondicaoPagamento) {
							$scope.condicaoPagamento = value;
						}
					});
				} else {
					if ($scope.matricula.condicaoPagamentos != undefined && Number.parseInt($scope.matricula.condicaoPagamentos.codigo) > 0) {
						$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos.codigo;	
						$scope.matricula.condicaoPagamentos = $scope.condicaoPagamento;
					} else {
						$scope.matricula.codigoCondicaoPagamento = 0;
						$scope.condicaoPagamento = {};
					}
				}
			}).error(function (status) {
				console.log(status);
			});
		cfpLoadingBar.complete();
	};

	$scope.atualizarDadosQuandoUnidadeEnsinoAlterado = function atualizarDadosQuandoUnidadeEnsinoAlterado() {
		cfpLoadingBar.start();
		$http.get(urlBase + '/atualizarDadosQuandoUnidadeEnsinoAlterado/' + Scopes.get('cursoControle').codigoCurso + '/' + Scopes.get('cursoControle').codigoBanner + '/' + $scope.matricula.codigoUnidadeEnsino).
		
			success(function (data) {
				$scope.matricula = data;
				if ($scope.matricula.possuiMaisDeUmTurno == 'true' || $scope.matricula.turnos[0] != null) {
					$scope.matricula.codigoTurno = $scope.matricula.turnos[0].codigo;
				} else {
					$scope.matricula.codigoTurno = $scope.matricula.turnos.codigo;
				}
				if ($scope.matricula.possuiMaisDeUmProcessoMatricula == 'true' || $scope.matricula.processoMatriculas[0] != null) {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas[0].codigo;
				} else {
					$scope.matricula.codigoProcessoMatricula = $scope.matricula.processoMatriculas.codigo;
				}
				if ($scope.matricula.possuiMaisDeUmaTurma == 'true' || $scope.matricula.turmas[0] != null) {
					$scope.matricula.codigoTurma = $scope.matricula.turmas[0].codigo;
				} else {
					$scope.matricula.codigoTurma = $scope.matricula.turmas.codigo;
				}
				if ($scope.matricula.possuiMaisDeUmaCondicaoDePagamento == 'true' || $scope.matricula.condicaoPagamentos[0] != null) {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos[0].codigo;
					angular.forEach($scope.matricula.condicaoPagamentos, function (value, key) {
						if (value.codigo == $scope.matricula.codigoCondicaoPagamento) {
							$scope.condicaoPagamento = value;
						}
					});
				} else {
					$scope.matricula.codigoCondicaoPagamento = $scope.matricula.condicaoPagamentos.codigo;
					$scope.matricula.condicaoPagamentos = $scope.condicaoPagamento;
				}
			}).error(function (status) {
				console.log(status);
			});
		cfpLoadingBar.complete();
	};

	$scope.consultarCondicaoPagamento = function consultarCondicaoPagamento() {
		console.log($scope.matricula.codigoCondicaoPagamento);
		if ($scope.matricula.possuiMaisDeUmaCondicaoDePagamento == 'true' || $scope.matricula.condicaoPagamentos[0] != null) {
			angular.forEach($scope.matricula.condicaoPagamentos, function (value, key) {
				if (value.codigo == $scope.matricula.codigoCondicaoPagamento) {
					$scope.condicaoPagamento = value;
				}
			});
		} else {
			$scope.matricula.condicaoPagamentos = $scope.condicaoPagamento;
		}
	};
	
	function consultarEstado($scope) {
		cfpLoadingBar.start();
		$http.get(urlBaseWebServices + '/estado/pais/brasil').success(function (data) {
					if(data != null){
						if(data.estado != null && data.estado.length > 0){
							$scope.estados = [].concat(data.estado);
						}else if(data.length > 0){
							$scope.estados = [].concat(data);
						}					
						$scope.estados = [].concat(data.estado);
						console.log($scope.estados);					
					}
				}).error(function (status) {
					cfpLoadingBar.complete();
					console.log(status);
				});
		cfpLoadingBar.complete();
	};
	
	function consultarCidade($scope) {
		cfpLoadingBar.start();				
		$http.get(urlBaseWebServices + '/cidade/estado/'+ $scope.pessoaObject.cidade.estado.codigo).success(function (data) {
					if(data != null){
						if(data.cidade != null && data.cidade.length > 0){
							$scope.cidades = [].concat(data.cidade);
						}else if(data.length > 0){
							$scope.cidades = [].concat(data);
						}
						console.log($scope.cidades);					
					}
				}).error(function (status) {
					cfpLoadingBar.complete();
					console.log(status);
				});
		cfpLoadingBar.complete();
	};
	
	$scope.consultarEndereco = function consultarEndereco() {
		cfpLoadingBar.start();
		if($scope.pessoaObject.cep.length > 0){
			var parametros = JSON.stringify($scope.pessoaObject);
			$http.post(urlBase + '/consultarEndereco', parametros, { headers: { 'Content-Type': 'application/json' } }).success(function (data) {
					$scope.pessoaObject = data;
					if($scope.pessoaObject.cidade.estado.codigo.length > 0){
						consultarCidade($scope);
						$scope.cidades = {};
						$scope.cidades = [].concat(data.cidade);
					}
					console.log($scope.estados);					
				}).error(function (status) {
					cfpLoadingBar.complete();
					console.log(status);
				});
		}
		cfpLoadingBar.complete();
	};
	

	$scope.realizarMatricula = function realizarMatricula() {
		cfpLoadingBar.start();
		console.log($scope.matricula.codigoUnidadeEnsino);
		$scope.matricula.unidadeEnsino.codigo = $scope.matricula.codigoUnidadeEnsino;
		$scope.matricula.turno.codigo = $scope.matricula.codigoTurno;
		$scope.matricula.processoMatricula.codigo = $scope.matricula.codigoProcessoMatricula;
		$scope.matricula.turma.codigo = $scope.matricula.codigoTurma;
		$scope.matricula.condicaoPagamento.codigo = $scope.matricula.codigoCondicaoPagamento;
		$scope.matricula.curso.codigo = $scope.pessoaObject.codigoCurso;
		$scope.matricula.curso.gradeDisciplina.codigo = $scope.curso.gradeDisciplina.codigo;
		$scope.matricula.pessoa = $scope.pessoaObject;
		$scope.matricula.codigoBanner = $scope.codigoBanner;
		if ($scope.matricula.pessoa.nome == null || $scope.matricula.pessoa.nome.trim == '' || $scope.matricula.pessoa.nome == 0){
			alert("O campo NOME deve ser informado!");
		}else if ($scope.matricula.pessoa.email == null || $scope.matricula.pessoa.email.indexOf('@') == -1  || $scope.matricula.pessoa.email.indexOf('.') == -1  || $scope.matricula.pessoa.email == 0 ){
			alert("O campo E-MAIL deve ser informado!");			
		}else if(($scope.matricula.pessoa.telefoneResidencial == null || $scope.matricula.pessoa.telefoneResidencial.length == 0  || $scope.matricula.pessoa.telefoneResidencial.trim == '') && ($scope.matricula.pessoa.celular == null || $scope.matricula.pessoa.celular == '' || $scope.matricula.pessoa.celular.length == 0)){
			alert("Pelo menos 1 campo de TELEFONE deve ser informado!");
		}else if($scope.matricula.pessoa.cpf == null || $scope.matricula.pessoa.cpf.trim == '' || ($scope.matricula.pessoa.cpf.length != 11 && $scope.matricula.pessoa.cpf.length != 14)){
			alert("O campo CPF deve ser informado!");
		}else if($scope.matricula.pessoa.rg == null || $scope.matricula.pessoa.rg.trim == ''  || $scope.matricula.pessoa.rg.length == 0){ 
			alert("O campo RG deve ser informado!");
		}else if($scope.matricula.pessoa.endereco == null || $scope.matricula.pessoa.endereco.trim == ''  || $scope.matricula.pessoa.endereco.length == 0){ 
			alert("O campo ENDERE\u00c7O deve ser informado!");
		}else if($scope.matricula.pessoa.numero == null || $scope.matricula.pessoa.numero.trim == '' || $scope.matricula.pessoa.numero.length == 0){ 
			alert("O campo N\u00daMERO deve ser informado!");	
		} else {
			var parametros = JSON.stringify($scope.matricula);
			$http.post(urlBase + '/matricularAluno/', parametros, { headers: { 'Content-Type': 'application/json' } }).
				success(function (data) {
					$scope.matricula = data;
					console.log(data);
					if ($scope.matricula.matriculaRealizadaComSucesso == 'true' || $scope.matricula.matriculaRealizadaComSucesso == true || data.matriculaRealizadaComSucesso == 'true' || data.matriculaRealizadaComSucesso == true) {
						alert('Matr\u00edcula efetuada com sucesso. Segue seu n\u00famero de matr\u00edcula: ' + $scope.matricula.matricula);
						if (($scope.matricula.mensagemErroContrato && $scope.matricula.mensagemErroContrato != '') && $scope.matricula.assinarDigitalmenteContrato == 'false') {
							alert($scope.matricula.mensagemErroContrato);
							$location.path('/realizarPagamento');
						} else if($scope.matricula.assinarDigitalmenteContrato == 'true'){
						    $location.path('/contratoMatriculaExterna');
						} else{
							$location.path('/realizarPagamento');
						}
					} else {
						alert('Aconteceu um erro inesperado. Efetue sua matr\u00edcula com a institui\u00e7\u00e3o de ensino.'+ $scope.matricula.mensagem);
					}
				}).error(function (status) {
					cfpLoadingBar.complete();
					console.log(status);
				});
		}
		cfpLoadingBar.complete();
	};
	
});

preInscricaoModule.controller('pagamentoControle', function ($scope, $http, $location, Scopes, $filter, $timeout, cfpLoadingBar) {
	if(Scopes.get('matriculaControle') == null){
		$location.path('/');
	}
	Scopes.store('pagamentoControle', $scope);

	$scope.matricula = Scopes.get('matriculaControle').matricula;
	console.log($scope.matricula);
	$http.get(urlBase + '/consultarContaReceberAlunoNovaMatricula/' + $scope.matricula.matricula + '/' + $scope.matricula.codigoMatriculaPeriodo + '/' + $scope.matricula.unidadeEnsino.codigo).
		success(function (data) {
			$scope.negociacaoRecebimento = data;
			if ($scope.negociacaoRecebimento.valorTotalAPagar == 0.0) {
//				$location.path('/');
			}
			$scope.negociacaoRecebimento.matriculaRSVO = $scope.matricula;
			if($scope.negociacaoRecebimento.mensagem != ''){
				console.log($scope.negociacaoRecebimento.mensagem);
				alert($scope.negociacaoRecebimento.mensagem);
			}else{
				console.log($scope.negociacaoRecebimento.bandeiraRSVOs);
				$scope.bandeiras = [].concat($scope.negociacaoRecebimento.bandeiraRSVOs);
				console.log($scope.bandeiras);
				if ($scope.negociacaoRecebimento.bandeiraRSVOs != null && $scope.negociacaoRecebimento.bandeiraRSVOs.length > 0) {
					$scope.montarDadosPagamentoCartaoCredito();
					angular.forEach($scope.formaPagamentoMatriculaOnlineExternas, function (value, key) {
						value.valor = $scope.negociacaoRecebimento.valorTotalAPagar;
						value.valorApresentar = "R$ "+$scope.negociacaoRecebimento.valorTotalAPagar.toString().replace(".", ",");
					});
				}
				console.log($scope.formaPagamentoMatriculaOnlineExternas);
				console.log($scope.negociacaoRecebimento);
			}
		}).error(function (status) {
			console.log(status);
		});
		
	$scope.montarDadosPagamentoCartaoCredito = function () {
		$scope.formaPagamentoMatriculaOnlineExternas = [{codigoConfiguracaoFinanceiroCartao: 0, bandeira: "", numeroCartao: "", nomeNoCartao: "", mesValidade: 0, anoValidade: 0, codigoDeVerificacao: "", valor: 0.0, valorApresentar:"R$ 0,00" }];

		$scope.meses = [{ id: '01', nome: 'Janeiro' }, { id: '02', nome: 'Fevereiro' }, { id: '03', nome: 'Mar\u00e7o' }, { id: '04', nome: 'Abril' }, { id: '05', nome: 'Maio' }, { id: '06', nome: 'Junho' }, { id: '07', nome: 'Julho' }, { id: '08', nome: 'Agosto' }, { id: '09', nome: 'Setembro' }, { id: '10', nome: 'Outubro' }, { id: '11', nome: 'Novembro' }, { id: '12', nome: 'Dezembro' }];

		$scope.anos = [];

		var date = new Date();

		var ano = Number($filter('date')(date, 'yyyy'));

		for (var i = 0; i < 10; i++) {
			$scope.anos.push(ano);
			ano = ano + 1;
		}
	};

	$scope.adicionarCartaoCredito = function adicionarCartaoCredito() {
		$scope.formaPagamentoMatriculaOnlineExternas.push({ formaPagamentoMatriculaOnlineExterna: { codigoConfiguracaoFinanceiroCartao: 0, bandeira: "", numeroCartao: "", nomeNoCartao: "", mesValidade: 0, anoValidade: 0, codigoDeVerificacao: "", valor: 0.0, valorApresentar:"R$ 0,00"} });
	};

	$scope.removerCartaoCredito = function removerCartaoCredito(index) {
		$scope.formaPagamentoMatriculaOnlineExternas.splice(index, 1);
	};

	$scope.realizarPagamentoCartaoCredito = function realizarPagamentoCartaoCredito() {
		cfpLoadingBar.start();
		$scope.negociacaoRecebimento.matricula = $scope.matricula.matricula;
		$scope.negociacaoRecebimento.codigoMatriculaPeriodo = $scope.matricula.codigoMatriculaPeriodo;
		$scope.negociacaoRecebimento.codigoUnidadeEnsino = $scope.matricula.unidadeEnsino.codigo;
		
		var alerta = false;
		angular.forEach($scope.formaPagamentoMatriculaOnlineExternas, function (value, key) {
			if(value.codigoConfiguracaoFinanceiroCartao == null || value.codigoConfiguracaoFinanceiroCartao == 0){
				alerta = true;
				alert('O campo BANDEIRA deve ser informado!');
				return;
			}else if(value.valorApresentar == null || value.valorApresentar.length == 0  || value.valorApresentar == 'R$ 0,00'){
				alerta = true;
				alert('O campo VALOR deve ser informado!');
				return;
			}else if(value.numeroCartao == null || value.numeroCartao.length == 0){
				alerta = true;
				alert('O campo N\u00daMERO CART\u00c3O deve ser informado!');
				return;
			}else if(value.nomeNoCartao == null || value.nomeNoCartao.length == 0){
				alerta = true;
				alert('O campo NOME DO CART\u00c3O deve ser informado!');
				return;
			}else if(value.mesValidade == null || value.mesValidade.length == 0){
				alerta = true;
				alert('O campo M\u00caS VALIDADE deve ser informado!');
				return;
			}else if(value.anoValidade == null || value.anoValidade.length == 0){
				alerta = true;
				alert('O campo ANO VALIDADE deve ser informado!');
				return;
			}else if(value.codigoDeVerificacao == null || value.codigoDeVerificacao.length == 0){
				alerta = true;
				alert('O campo N\u00daMERO DE VERIFICA\u00c7\u00c3O DO CART\u00c3O (CVV) deve ser informado!');
				return;
			}else{
			 var valor = value.valorApresentar;
			 if(valor.indexOf('R$')  != -1){
				 valor = valor.replace('R$', '');
			 }
			 while(valor.indexOf('.') != -1 && valor.indexOf(',') != -1){
				valor = valor.replace('.', '');
			 }
			 while(valor.indexOf(' ')){
				 valor = valor.replace(' ', '');
			 }
			 if(valor.indexOf(',')  != -1){
				 valor = valor.replace(',', '.');
			 }
			 if(valor != ''){
				value.valor = Number(valor);			 
			 }else{
				value.valor = Number(0.0);			 
			 }
			}
		});
		if(alerta == false){			
		$scope.negociacaoRecebimento.formaPagamentoMatriculaOnlineExternas = [].concat($scope.formaPagamentoMatriculaOnlineExternas);
		console.log($scope.negociacaoRecebimento.formaPagamentoMatriculaOnlineExternas);
		var parametros = JSON.stringify($scope.negociacaoRecebimento);
		$http.post(urlBase + '/realizarPagamentoCartaoCredito', parametros, { headers: { 'Content-Type': 'application/json' } })
			.success(function (data) {
				$scope.negociacaoRecebimento = data;
				console.log($scope.negociacaoRecebimento);
				if ($scope.negociacaoRecebimento.pagamentoConfirmado == 'true' || $scope.negociacaoRecebimento.pagamentoConfirmado == true || data.pagamentoConfirmado == true || data.pagamentoConfirmado == 'true') {
					alert('Pagamento confirmado.');
					window.open($scope.negociacaoRecebimento.linkDownloadComprovantePagamento);
					$location.path('/');
				} else {
					alert('Pagamento pendente, entre em contato com a Institui\u00e7\u00e3o de Ensino.' + $scope.negociacaoRecebimento.mensagem);
				}
			}).error(function (status) {
				console.log(status);
			});
		}
		cfpLoadingBar.complete();
	};

	$scope.realizarImpressaoBoleto = function realizarImpressaoBoleto() {
		cfpLoadingBar.start();
		var parametros = JSON.stringify($scope.negociacaoRecebimento);
		$http.post(urlBase + '/realizarImpressaoBoletoBancario', parametros, { headers: { 'Content-Type': 'application/json' } })
			.success(function (data) {
				$scope.negociacaoRecebimento = data;
				window.open($scope.negociacaoRecebimento.linkDownloadBoleto);
				console.log($scope.negociacaoRecebimento);
			}).error(function (status) {
				console.log(status);
			});
		cfpLoadingBar.complete();
	};
	
	$scope.registrarAssinaturaContratoPorAluno  = function registrarAssinaturaContratoPorAluno() {
		var parametros = JSON.stringify($scope.matricula);
		$http.post(urlBase + '/registrarAssinaturaContratoPorAluno', parametros, { headers: { 'Content-Type': 'application/json' } })
			.success(function (data) {
				$scope.matricula = data;
				$location.path('/realizarPagamento');
			}).error(function (status) {
				console.log(status);
			});
	};
	
	
	$scope.registrarIndeferimentoContratoPorAluno = function registrarIndeferimentoContratoPorAluno() {
			$scope.matricula.motivoRecusa = $scope.motivoRecusa;
			var parametros = JSON.stringify($scope.matricula);
			$http.post(urlBase + '/registrarIndeferimentoContratoPorAluno', parametros, { headers: { 'Content-Type': 'application/json' } }).
				success(function (data) {
					$scope.matricula = data;
					alert('Sua Matr\u00edcula foi criado no nosso sistema, iremos avaliar o motivo da recusa do contrato e em breve daremos o retorno.');
						$location.path('/');
				}).error(function (status) {
					cfpLoadingBar.complete();
					console.log(status);
				});
		}
	});
	


preInscricaoModule.factory('Scopes', function ($rootScope) {
	var mem = {};

	return {
		store: function (key, value) {
			$rootScope.$emit('scope.stored', key);
			mem[key] = value;
		},
		get: function (key) {
			return mem[key];
		}
	};
});