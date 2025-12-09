<!DOCTYPE html>
<html ng-app="matriculaOnlineExternaApp" lang="pt-BR">
<head>
<meta charset="iso-8859-1" >
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Matrícula On-line Externa</title>

<!-- Bootstrap -->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/bootstrap-select.min.css">
<link href="css/style.css" rel="stylesheet">
<link rel="stylesheet" href="css/bootstrap-select.css">
<link href='css/loading-bar.css' rel='stylesheet' />
<script type="text/javascript" src="js/bootstrap-select.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
</head> 
<body>

<?php
// recebe dados do curso via site wordpress
// campos são preenchidos no cadastro do curso no painel de gerenciamento do wp
// campos estão hidden na página do curso
 
$idBanner = $_POST['banner'];
$idCurso = $_POST['id'];
$idUnidade = $_POST['unidade'];
$idTurno = $_POST['turno'];

?>	

<!--<p>Código do Banner: <?php echo $idBanner; ?></p>
<p>Código do Curso: <?php echo $idCurso; ?></p>
<p>Unidade: <?php echo $idUnidade; ?></p>
<p>Turno: <?php echo $idTurno; ?></p>-->


<div ng-init="codigoCurso = <?php echo $idCurso; ?>"></div>
<div ng-init="codigoBanner = <?php echo $idBanner; ?>"></div>
<!--<div ng-init="codeBanner = 11"></div>-->
<!--<div ng-init="codeCurso = 63">-->

	<div id="header" class="container cliente-bg">
		<img class="" alt="SEI" src="image/logo-sei-otimize.png"></img>
	</div>
	<div class="container">
		<div class="content">
			<div id="main2" class="right">
				<div id="recadosAluno" class="container box marginbottom padding">
					<div ng-view></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular-animate.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.4/ui-bootstrap-tpls.min.js"></script>
	<script type="text/javascript" src="controller/controller.js?UID=20170918"></script>
	<script type="text/javascript" src="js/angular-locale_pt-br.js"></script>
	<script type="text/javascript" src="js/loading-bar.js"></script>
	<script type="text/javascript" src="js/jquery.maskMoney.js"></script>	
	<script type="text/javascript" src="js/jquery.alphanumeric.js"></script>
	<script src="js/mask.js"></script>
	<script src="js/script.js?UID=20170918"></script>	
	
</body>
</html>