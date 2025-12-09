ALTER TABLE IF EXISTS gradecurricular ADD COLUMN IF NOT EXISTS totalCargaHorariaDisciplinasObrigatorias int;

UPDATE
	gradecurricular
SET
	totalcargahorariadisciplinasobrigatorias = t.total
FROM
	(
	SELECT
		sum(COALESCE (g.cargahoraria, 0)) AS total,
		gradecurricular.codigo AS gradeCurricular
	FROM
		gradecurricular
	INNER JOIN periodoletivo p ON
		p.gradecurricular = gradecurricular.codigo
	INNER JOIN gradedisciplina g ON
		g.periodoletivo = p.codigo
		AND g.tipodisciplina NOT IN ('LO', 'OP')
	GROUP BY
		gradecurricular.codigo
) AS t
WHERE
	t.gradeCurricular = gradecurricular.codigo
	AND gradecurricular.totalcargahorariadisciplinasobrigatorias IS NULL;