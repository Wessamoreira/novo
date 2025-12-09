INSERT
	INTO
	permissao ( nomeentidade,
	tituloapresentacao,
	permissoes,
	tipopermissao,
	codperfilacesso,
	responsavel ) (
	SELECT
		'ComunicacaoInterna_importarPlanilhaDestinatario' nomeentidade,
		'Comunicação Interna: Importar Planilha de Destinatário' tituloapresentacao,
		'(0)(1)(2)(3)(9)(12)' permissoes,
		2 tipopermissao,
		codperfilacesso,
		p.responsavel
	FROM
		permissao p
	INNER JOIN perfilacesso p2 ON
		p2.codigo = p.codperfilacesso
		AND p2.ambiente = 'ADMINISTRATIVA'
	WHERE
		nomeentidade = 'ComunicacaoInterna_importarPlanilhaDestinatario'
		AND permissoes = '(0)(1)(2)(3)(9)(12)'
		AND NOT EXISTS (
		SELECT
			1
		FROM
			permissao
		WHERE
			permissao.codperfilacesso = p2.codigo
			AND permissao.nomeentidade = 'ComunicacaoInterna_importarPlanilhaDestinatario' ));