INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://apps.univesp.br/manual-do-aluno/calendario-academico/' ,
'far fa-calendar-alt' ,
'FALSE' ,
'Calendário Academico' ) RETURNING codigo;



INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://apps.univesp.br/manual-do-aluno/calendario-provas/' ,
'fas fa-calendar-alt' ,
'FALSE' ,
'Calendário de Provas' ) RETURNING codigo;


INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://apps.univesp.br/manual-do-aluno/' ,
'fas fa-book' ,
'FALSE' ,
'Manual do Aluno' ) RETURNING codigo;



INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://login.microsoftonline.com/login.srf?wa=wsignin1.0&amp;whr=aluno.univesp.br' ,
'fas fa-laptop' ,
'FALSE' ,
'Portal Office 365' ) RETURNING codigo;


INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://atendimento.univesp.br/sae/portal.html' ,
'fas fa-headset' ,
'FALSE' ,
'Sistema de Atendimento - SAE' ) RETURNING codigo;




INSERT
	INTO
	LinksUteis ( administrativo ,
	aluno ,
	professor ,
	nomeCreated ,
	created ,
	codigoCreated ,
	link ,
	icone ,
	coordenador ,
	descricao )
VALUES ( 'FALSE' ,
'TRUE' ,
'FALSE' ,
'' ,
'2022-04-26 10:13:23.606-03' ,
1 ,
'https://assets.univesp.br/tutoriais/tutorial_requerimento.pdf' ,
'fas fa-file-pdf' ,
'FALSE' ,
'Tutorial Solicitação Revisão de Prova' ) RETURNING codigo;






delete  from dashboard where tipodashboard = 'MENU_TOPO_LINK_UTEIS';


