UPDATE
	matricula
SET
	classificacaoingresso = x.classificacao
FROM
	(
	SELECT
		*
	FROM
		(
		SELECT
			ROW_NUMBER () OVER ( PARTITION BY CASE
				WHEN resultadoprimeiraopcao = 'AP'
				OR resultadosegundaopcao = 'AP'
				OR resultadoterceiraopcao = 'AP' THEN 1
				ELSE 2
			END,
			procseletivo.codigo,
			unidadeensinocurso.unidadeensino,
			curso.codigo,
			turno.codigo
		ORDER BY
			CASE
				WHEN (procseletivo.regimeaprovacao = 'quantidadeAcertosRedacao') THEN (medianotasprocseletivo + notaredacao)
				ELSE medianotasprocseletivo
			END DESC,
			notaredacao DESC,
			ARRAY(
			SELECT
				RANK
			FROM
				(
				SELECT
					RANK() OVER( PARTITION BY CASE
						WHEN resultadoprimeiraopcao = 'AP'
						OR resultadosegundaopcao = 'AP'
						OR resultadoterceiraopcao = 'AP' THEN 1
						ELSE 2
					END,
					ins.procseletivo,
					uec.unidadeEnsino,
					uec.curso,
					uec.turno,
					dgdps.ordemcriteriodesempate
				ORDER BY
					uec.unidadeensino,
					uec.curso,
					uec.turno ,
					dgdps.ordemcriteriodesempate,
					rdps.nota DESC ),
					ins.codigo AS inscricao,
					uec.unidadeEnsino,
					uec.curso,
					uec.turno,
					rdps.disciplinaprocseletivo,
					dgdps.ordemcriteriodesempate,
					rdps.nota
				FROM
					resultadoprocessoseletivo AS rps
				INNER JOIN inscricao AS ins ON
					ins.codigo = rps.inscricao
				INNER JOIN unidadeensinocurso uec ON
					CASE
						WHEN rps.resultadoprimeiraopcao = 'AP' THEN ins.cursoopcao1
						ELSE CASE
							WHEN rps.resultadosegundaopcao = 'AP' THEN ins.cursoopcao2
							ELSE ins.cursoopcao3
						END
					END = uec.codigo
				INNER JOIN procseletivounidadeensino psue ON
					psue.procseletivo = ins.procseletivo
					AND uec.unidadeensino = psue.unidadeEnsino
				INNER JOIN procseletivocurso psc ON
					psc.unidadeensinocurso = uec.codigo
					AND psc.procseletivounidadeensino = psue.codigo
				INNER JOIN grupodisciplinaprocseletivo gdps ON
					gdps.codigo = psc.grupodisciplinaprocseletivo
				INNER JOIN disciplinasgrupodisciplinaprocseletivo dgdps ON
					dgdps.grupodisciplinaprocseletivo = gdps.codigo
				INNER JOIN resultadodisciplinaprocseletivo rdps ON
					rdps.resultadoprocessoseletivo = rps.codigo
					AND rdps.disciplinaprocseletivo = dgdps.disciplinasprocseletivo
				WHERE
					(rps.resultadoprimeiraopcao = 'AP'
						OR rps.resultadosegundaopcao = 'AP'
						OR rps.resultadoterceiraopcao = 'AP')
					AND CASE
						WHEN (dgdps.ordemcriteriodesempate IS NULL
							OR dgdps.ordemcriteriodesempate = '') THEN 0
						ELSE dgdps.ordemcriteriodesempate::INT
					END > 0) AS t
			WHERE
				t.inscricao = inscricao.codigo
			ORDER BY
				ordemcriteriodesempate ,
				RANK),
			datanasc ) AS classificacao,
			inscricao.codigo AS numeroInscricao,
			pessoa.codigo AS "pessoa.codigo",
			UPPER(pessoa.nome) AS "pessoa.nome",
			pessoa.email,
			pessoa.telefoneres,
			pessoa.celular,
			medianotasprocseletivo,
			notaredacao,
			somatorioacertos,
			curso.nome AS "curso.nome" ,
			turno.nome AS "turno.nome",
			itemprocessoseletivodataprova AS codigoDataProva,
			sala,
			procseletivocurso.numerovaga,
			unidadeEnsino.nome AS "unidadeEnsino.nome",
			procseletivo.regimeAprovacao,
			m.matricula AS "matricula"
		FROM
			resultadoprocessoseletivo
		INNER JOIN inscricao ON
			inscricao.codigo = resultadoprocessoseletivo.inscricao
		INNER JOIN unidadeensinocurso ON
			CASE
				WHEN resultadoprimeiraopcao = 'AP' THEN inscricao.cursoopcao1
				ELSE CASE
					WHEN resultadosegundaopcao = 'AP' THEN inscricao.cursoopcao2
					ELSE inscricao.cursoopcao3
				END
			END = unidadeensinocurso.codigo
		INNER JOIN unidadeEnsino ON
			unidadeEnsino.codigo = unidadeensinocurso.unidadeEnsino
		INNER JOIN procseletivounidadeensino ON
			procseletivounidadeensino.procseletivo = inscricao.procseletivo
			AND unidadeEnsino.codigo = procseletivounidadeensino.unidadeEnsino
		INNER JOIN procseletivocurso ON
			procseletivocurso.unidadeensinocurso = unidadeensinocurso.codigo
			AND procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo
		INNER JOIN curso ON
			curso.codigo = unidadeensinocurso.curso
		INNER JOIN turno ON
			turno.codigo = unidadeensinocurso.turno
		INNER JOIN pessoa ON
			pessoa.codigo = inscricao.candidato
		INNER JOIN procseletivo ON
			procseletivo.codigo = inscricao.procseletivo
		INNER JOIN matricula m ON
			m.aluno = inscricao.candidato
		WHERE
			1 = 1
			AND inscricao.situacao = 'CO'
			AND (resultadoprimeiraopcao = 'AP'
				OR resultadosegundaopcao = 'AP'
				OR resultadoterceiraopcao = 'AP') 
			AND (m.classificacaoingresso IS NULL
			OR m.classificacaoingresso IN (0))) AS t
	WHERE
		1 = 1
	ORDER BY
		"unidadeEnsino.nome",
		"curso.nome",
		"turno.nome",
		classificacao 
) x WHERE matricula.matricula = x.matricula;

UPDATE
	matricula
SET
	totalpontoprocseletivo = CASE
		WHEN p.regimeaprovacao = 'notaPorDisciplina' 
		THEN r.medianotasprocseletivo
		WHEN p.regimeaprovacao = 'quantidadeAcertosRedacao' THEN r.notaredacao
		ELSE r.somatorioacertos
	END
FROM
	matricula AS matri
INNER JOIN inscricao i ON
	i.codigo = matri.inscricao
INNER JOIN procseletivo p ON
	p.codigo = i.procseletivo
INNER JOIN resultadoprocessoseletivo r ON
	r.inscricao = i.codigo
WHERE
	matri.matricula = matricula.matricula
	AND (matri.totalpontoprocseletivo IS NULL
		OR matri.totalpontoprocseletivo IN (0, 0.0));