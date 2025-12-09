var campanhaModule = angular.module('campanhaLigacaoReceptivaApp', ['ui.bootstrap', 'ngRoute', 'chieffancypants.loadingBar', 'ngAnimate', 'ui.mask']);

var urlBase = "http://localhost:8086/TRONCO/webservice/campanhaRS";

campanhaModule.config(['$httpProvider', '$routeProvider', function ($httpProvider, $routeProvider) {
	$routeProvider.
		when('/', {
			templateUrl: 'tireSuasDuvidas.html',
			controller: 'campanhaControle'
		}).
		otherwise({
			redirectTo: '/'
		});
}]);

campanhaModule.config(function (cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = true;
});

campanhaModule.run(function ($rootScope) {
	$rootScope.$on('scope.stored', function (event, data) {
		console.log("scope.stored", data);
	});
});

campanhaModule.controller('campanhaControle', function ($scope, $http, $location, Scopes, $timeout, cfpLoadingBar) {

	Scopes.store('campanhaControle', $scope);
	$scope.campanha = {};
	$scope.campanha.codigoCurso = {};
	$scope.campanha.codigoTurno = {};
	 
	$scope.gerarAgendaLigacaoReceptivaTireSuasDuvidasCampanhaCRM = function gerarAgendaLigacaoReceptivaTireSuasDuvidasCampanhaCRM(curso, turno) {
		cfpLoadingBar.start();
		if ($scope.campanha.codigoUnidadeEnsino == null) {
			alert('O Campo UNIDADE DE ENSINO deve ser Informado!');
			return;
		}
		if ($scope.campanha.pessoa.nome == null || $scope.campanha.pessoa.nome == '') {
			alert('O Campo NOME deve ser Informado!');
			return;
		}
		if ($scope.campanha.pessoa.email == null || $scope.campanha.pessoa.email == '') {
			alert('O Campo EMAIL deve ser Informado ou deve ser digitado um EMAIL válido!');
			return;
		}
		if ($scope.campanha.duvida == null || $scope.campanha.duvida == '') {
			alert('O Campo DÚVIDA deve ser Informado!');
			return;
		}
		$scope.campanha.codigoCurso = curso;
		$scope.campanha.codigoTurno = turno;
		
		var parametros = JSON.stringify($scope.campanha);
		$http.post(urlBase + '/gerarAgendaLigacaoReceptivaTireSuasDuvidasCampanhaCRM', parametros, { headers: { 'Content-Type': 'application/json' } }).success(function (data) {
				
				$scope.campanha = data;
				console.log($scope.campanha);
				if ($scope.campanha.mensagem != '') {
					alert($scope.campanha.mensagem);
					
				} else {
					alert('Aconteceu um problema inesperado, por favor entre em contato com o Administrador.' + $scope.campanha.mensagem);
				}
			}).error(function (status) {
				console.log(status);
			});
		cfpLoadingBar.complete();
	};

	$scope.voltarTelaInicial = function voltarTelaInicial() {
		$location.path('/');
	};
	
	$scope.irTelaTireSuasDuvidas = function irTelaTireSuasDuvidas() {
		$location.path('/tireSuasDuvidas');
	};
	
});

campanhaModule.factory('Scopes', function ($rootScope) {
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