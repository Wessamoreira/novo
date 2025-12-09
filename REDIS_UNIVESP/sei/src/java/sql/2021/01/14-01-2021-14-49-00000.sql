CREATE OR REPLACE VIEW public.integracaocatraca
AS SELECT t.codigoexterno,
    t.cpf,
    t.nome,
    t.telefone,
    t.email,
    t.datanascimento,
    t.sexo,
    (((((((t.endereco::text || ', '::text) ||
        CASE
            WHEN t.numero::text <> ''::text THEN 'nº '::text || t.numero::text
            ELSE ''::text
        END) || ' - '::text) || t.setor::text) || ' - '::text) || t.cidade::text) || '-'::text) || t.estado::text AS endereco,
    t.categoria,
    t.subcategoria,
    t.codresponsavel,
    t.nomeresponsavel,
    t.empresa,
    t.lotacao,
    t.secao,
    t.matricula,
    t.ano,
    t.semestre,
    t.tipoautorizacao,
    t.situacao,
    t.pin
   FROM ( SELECT DISTINCT pessoa.codigo AS codigoexterno,
            replace(replace(pessoa.cpf::text, '.'::text, ''::text), '-'::text, ''::text)::character varying(14) AS cpf,
            pessoa.nome,
            pessoa.celular AS telefone,
                CASE
                    WHEN pessoa.email IS NOT NULL AND pessoa.email::text <> ''::text THEN pessoa.email
                    ELSE pessoa.email2
                END AS email,
            pessoa.datanasc AS datanascimento,
            pessoa.sexo,
                CASE
                    WHEN pessoa.endereco IS NULL THEN ''::character varying
                    ELSE pessoa.endereco
                END AS endereco,
                CASE
                    WHEN pessoa.numero IS NULL THEN ''::character varying
                    ELSE pessoa.numero
                END AS numero,
                CASE
                    WHEN pessoa.setor IS NULL THEN ''::character varying
                    ELSE pessoa.setor
                END AS setor,
                CASE
                    WHEN cidade.nome IS NULL THEN ''::character varying
                    ELSE cidade.nome
                END AS cidade,
                CASE
                    WHEN estado.sigla IS NULL THEN ''::character varying
                    ELSE estado.sigla
                END AS estado,
            ''::text AS categoria,
            ''::text AS subcategoria,
            ''::text AS codresponsavel,
            ''::text AS nomeresponsavel,
            ''::text AS empresa,
            ''::text AS lotacao,
            ''::text AS secao,
                CASE
                    WHEN usuario.tipousuario::text = 'AL'::text THEN matricula.matricula
                    ELSE ( SELECT funcionario.matricula
                       FROM funcionario
                      WHERE funcionario.pessoa = pessoa.codigo
                      ORDER BY funcionario.codigo DESC
                     LIMIT 1)
                END AS matricula,
                CASE
                    WHEN usuario.tipousuario::text = 'AL'::text THEN ( SELECT matriculaperiodo_1.ano
                       FROM matriculaperiodo matriculaperiodo_1
                      WHERE matriculaperiodo_1.matricula::text = matricula.matricula::text
                      ORDER BY ((matriculaperiodo_1.ano::text || '/'::text) || matriculaperiodo_1.semestre::text) DESC
                     LIMIT 1)
                    ELSE NULL::character varying
                END AS ano,
                CASE
                    WHEN usuario.tipousuario::text = 'AL'::text THEN ( SELECT matriculaperiodo_1.semestre
                       FROM matriculaperiodo matriculaperiodo_1
                      WHERE matriculaperiodo_1.matricula::text = matricula.matricula::text
                      ORDER BY ((matriculaperiodo_1.ano::text || '/'::text) || matriculaperiodo_1.semestre::text) DESC
                     LIMIT 1)
                    ELSE NULL::character varying
                END AS semestre,
                CASE
                    WHEN usuario.tipousuario::text = 'AL'::text THEN 'ALUNO'::text
                    ELSE 'FUNCIONARIO'::text
                END AS tipoautorizacao,
                CASE
                    WHEN usuario.tipousuario::text = 'AL'::text AND (matriculaperiodo.situacaomatriculaperiodo::text = ANY (ARRAY['AT'::text, 'PR'::text])) THEN 'ATIVO'::text
                    ELSE
                    CASE
                        WHEN usuario.tipousuario::text = 'AL'::text AND (matriculaperiodo.situacaomatriculaperiodo::text <> ALL (ARRAY['AT'::text, 'PR'::text])) THEN 'INATIVO'::text
                        ELSE
                        CASE
                            WHEN pessoa.ativo THEN 'ATIVO'::text
                            ELSE 'INATIVO'::text
                        END
                    END
                END AS situacao,
            usuario.tipousuario,
            replace(replace(btrim(pessoa.cpf::text), '-'::text, ''::text), '.'::text, ''::text) AS pin
           FROM usuario
             JOIN pessoa ON pessoa.codigo = usuario.pessoa
             LEFT JOIN matricula ON matricula.aluno = pessoa.codigo AND (matricula.matricula::text IN ( SELECT mat.matricula
                   FROM matricula mat
                     JOIN curso c ON c.codigo = mat.curso
                     JOIN matriculaperiodo mp ON mp.matricula::text = mat.matricula::text AND mp.codigo = (( SELECT mpi.codigo
                           FROM matriculaperiodo mpi
                          WHERE mpi.matricula::text = mat.matricula::text
                          ORDER BY ((mpi.ano::text || ''::text) || mpi.semestre::text) DESC, mpi.codigo DESC, mpi.data DESC
                         LIMIT 1))
                  WHERE mat.aluno = pessoa.codigo AND (c.periodicidade::text <> 'IN'::text AND mp.ano::text >= '2015'::text OR c.periodicidade::text = 'IN'::text AND mp.data >= '2015-01-01 00:00:00'::timestamp without time zone)
                  ORDER BY mp.situacaomatriculaperiodo, mat.situacao, ((mp.ano::text || ''::text) || mp.semestre::text) DESC, mp.codigo DESC, mp.data DESC, mat.data DESC
                 LIMIT 1))
             LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula::text = matricula.matricula::text AND matriculaperiodo.codigo = (( SELECT mpi.codigo
                   FROM matriculaperiodo mpi
                  WHERE mpi.matricula::text = matricula.matricula::text
                  ORDER BY ((mpi.ano::text || ''::text) || mpi.semestre::text) DESC, mpi.codigo DESC, mpi.data DESC
                 LIMIT 1))
             LEFT JOIN curso ON curso.codigo = matricula.curso
             LEFT JOIN cidade ON cidade.codigo = pessoa.cidade
             LEFT JOIN estado ON estado.codigo = cidade.estado
          WHERE (usuario.tipousuario::text = ANY (ARRAY['AL'::character varying::text, 'FU'::character varying::text, 'PR'::character varying::text, 'DM'::character varying::text, 'DC'::character varying::text, 'CO'::character varying::text])) AND pessoa.ativo AND (usuario.tipousuario::text <> 'AL'::text OR usuario.tipousuario::text = 'AL'::text AND matricula.situacao::text <> 'PC'::text AND matriculaperiodo.codigo IS NOT NULL)) t;
   