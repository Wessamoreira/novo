package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import relatorio.negocio.comuns.academico.AlunosReprovadosRelVO;
import relatorio.negocio.interfaces.academico.AlunosReprovadosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AlunosReprovadosRel extends SuperRelatorio implements AlunosReprovadosRelInterfaceFacade {

	private AlunosReprovadosRelVO alunosReprovadosRelVO;

	public AlunosReprovadosRel() {
	}

	public void validarDados(String tipoRelatorio, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina) throws ConsistirException {
		if (tipoRelatorio.equals("")) {
			throw new ConsistirException("O Tipo de Relatório deve ser informado para a geração do relatório.");
		}
		if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
		}
		if (curso == null || curso.getCodigo() == 0) {
			throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
		}
		if (tipoRelatorio.equals("turma")) {
			if (turma == null || turma.getCodigo() == 0) {
				throw new ConsistirException("A Turma deve ser informada para a geração do relatório.");
			}
		} else if (tipoRelatorio.equals("disciplina") && (disciplina == null || disciplina.getCodigo() == 0)) {
			throw new ConsistirException("A Disciplina deve ser informada para a geração do relatório.");
		}
	}

	public List<AlunosReprovadosRelVO> criarObjeto(String motivoReprovacao, String tipoRelatorio, String situacaoAlunoReposicao, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao, Boolean desconsiderarAlunosCursandoDisciplinaAposReprovacao, String tipoLayout, Boolean filtrarSituacaoAtualMatricula) throws Exception {
		List<AlunosReprovadosRelVO> alunosReprovadosRelVOs = new ArrayList<AlunosReprovadosRelVO>(0);
		SqlRowSet tabelaResultado = executarConsultaParametrizada(motivoReprovacao, tipoRelatorio, situacaoAlunoReposicao, unidadeEnsino.getCodigo(), curso.getCodigo(), turma, disciplina.getCodigo(), ano, semestre,filtroRelatorioAcademicoVO, desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao, desconsiderarAlunosCursandoDisciplinaAposReprovacao, tipoLayout, filtrarSituacaoAtualMatricula);
		while (tabelaResultado.next()) {
			alunosReprovadosRelVOs.add(montarDados(tabelaResultado, tipoRelatorio));
		}
		return alunosReprovadosRelVOs;
	}

	private SqlRowSet executarConsultaParametrizada(String motivoReprovacao, String tipoRelatorio, String situacaoAlunoReposicao, Integer unidadeEnsino, Integer curso, TurmaVO turmaVO, Integer disciplina, String ano, String semestre,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao, Boolean desconsiderarAlunosCursandoDisciplinaAposReprovacao, String tipoLayout, Boolean filtrarSituacaoAtualMatricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct ue.nome as nomeunidadeensino, c.nome as nomecurso, t.identificadorturma as nometurma, ");
		sqlStr.append("m.matricula, p.nome as nomealuno, p.email as emailaluno, p.codigo as codigoaluno, d.nome as nomedisciplina, h.mediafinal, mp.ano, mp.semestre,  ");
		if (!situacaoAlunoReposicao.trim().isEmpty() && situacaoAlunoReposicao.equals("COM_REPOSICAO")) {
			sqlStr.append(" true ");
		} else if (!situacaoAlunoReposicao.trim().isEmpty() && situacaoAlunoReposicao.equals("SEM_REPOSICAO")) {
			sqlStr.append(" false ");
		} else {
			sqlStr.append(" ((select count(distinct codigo) from historico hist where m.matricula = hist.matricula ");
			sqlStr.append(" and (hist.disciplina = d.codigo or hist.disciplina in (select equivalente from disciplinaequivalente  where disciplina = d.codigo )) ");			
			sqlStr.append(" and hist.matriculaperiodoturmadisciplina <> matriculaperiodoturmadisciplina.codigo ");
			sqlStr.append(" and hist.situacao not in ('RE', 'RF') limit 1) > 0) ");
		}
		sqlStr.append(" as realizouReposicao ");
		sqlStr.append("FROM historico h ");
		sqlStr.append("INNER JOIN disciplina d                    ON h.disciplina       = d.codigo ");
		sqlStr.append("INNER JOIN matricula m                     ON h.matricula        = m.matricula ");
		sqlStr.append("INNER JOIN pessoa p                        ON m.aluno            = p.codigo ");
		sqlStr.append("INNER JOIN curso c                         ON m.curso            = c.codigo ");
		sqlStr.append("INNER JOIN unidadeensino ue                ON m.unidadeensino    = ue.codigo ");
		sqlStr.append("INNER JOIN matriculaperiodo mp             ON h.matriculaperiodo = mp.codigo ");
		sqlStr.append("LEFT JOIN matriculaperiodoturmadisciplina ON h.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma t ON t.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma t ON t.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma t ON MatriculaPeriodoTurmaDisciplina.turma = t.codigo and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turmaPratica is null ");
			}
		} else {
			sqlStr.append("INNER JOIN turma t ON ((matriculaperiodoturmadisciplina.codigo is null and mp.turma = t.codigo) or (matriculaperiodoturmadisciplina.codigo is not null and matriculaperiodoturmadisciplina.turma = t.codigo)) ");
		}
		
//		sqlStr.append("INNER JOIN turma t                         ON mp.turma           = t.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");
		
		if (motivoReprovacao.equals("falta")) {
			sqlStr.append("AND h.situacao = 'RF' ");
		} else if (motivoReprovacao.equals("nota")) {
			sqlStr.append("AND h.situacao = 'RE' ");
		} else {
			sqlStr.append("AND (h.situacao = 'RE' OR h.situacao = 'RF') ");
		}
	 	sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
	 	sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
	 	if (filtrarSituacaoAtualMatricula) {
	 		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "m"));
	 	}
		
		sqlStr.append("AND ue.codigo = ").append(unidadeEnsino).append(" ");
		sqlStr.append("AND c.codigo = ").append(curso.intValue()).append(" ");
		
		if (tipoRelatorio.equals("turma")) {
//			sqlStr.append("AND t.codigo = ").append(turmaVO.getCodigo()).append(" ");
			if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
				sqlStr.append(" AND ((t.codigo = ").append(turmaVO.getCodigo()).append(" or t.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			} else {
				sqlStr.append(" AND t.codigo = ").append(turmaVO.getCodigo());
			}
		} else if (tipoRelatorio.equals("disciplina")) {
			sqlStr.append("AND d.codigo = ").append(disciplina).append(" ");
		}
		
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append("AND mp.ano = '").append(ano).append("' ");
		}
		
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append("AND mp.semestre = '" ).append(semestre).append("' ");
		}
		if (!situacaoAlunoReposicao.trim().isEmpty() && situacaoAlunoReposicao.equals("COM_REPOSICAO")) {

			sqlStr.append(" and (select count(distinct codigo) from historico hist where m.matricula = hist.matricula ");
			sqlStr.append(" and (hist.disciplina = d.codigo or hist.disciplina in (select equivalente from disciplinaequivalente  where disciplina = d.codigo )) ");
			
			sqlStr.append(" and hist.matriculaperiodoturmadisciplina <> matriculaperiodoturmadisciplina.codigo ");
			sqlStr.append(" and hist.situacao not in ('RE', 'RF') limit 1) > 0 ");

		} else if (!situacaoAlunoReposicao.trim().isEmpty() && situacaoAlunoReposicao.equals("SEM_REPOSICAO")) {
			sqlStr.append(" and (select count(distinct codigo) from historico hist where m.matricula = hist.matricula ");
			sqlStr.append(" and (hist.disciplina = d.codigo or hist.disciplina in (select equivalente from disciplinaequivalente  where disciplina = d.codigo )) ");			
			sqlStr.append(" and hist.matriculaperiodoturmadisciplina <> matriculaperiodoturmadisciplina.codigo ");
			sqlStr.append(" and hist.situacao not in ('RE', 'RF') limit 1) = 0 ");
		}
		
			sqlStr.append(" AND NOT EXISTS (select 1 from historico hist where hist.matricula = h.matricula and hist.disciplina = h.disciplina");
			sqlStr.append(" and hist.situacao in (''");
			if (desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao) {
				sqlStr.append(",'CC','AE','AA','AP'");
			}
			if (desconsiderarAlunosCursandoDisciplinaAposReprovacao) {
				sqlStr.append(",'CS'");
			}
			sqlStr.append(')').append(")");
					
		if (tipoLayout.equals("cursoTurmaAlunoDisciplina")) {
			sqlStr.append("GROUP BY nomecurso, nometurma, nomealuno, p.email, p.codigo, d.nome, d.codigo,  matriculaperiodoturmadisciplina.codigo,  nomeunidadeensino, m.matricula, h.mediafinal, mp.ano, mp.semestre ");
			sqlStr.append("ORDER BY nomecurso, nometurma, nomealuno, d.nome;");
		} else if (tipoLayout.equals("cursoAlunoDisciplina")) {
			sqlStr.append("GROUP BY nomecurso, nomealuno, d.nome, d.codigo, p.email, p.codigo, matriculaperiodoturmadisciplina.codigo, nomeunidadeensino, m.matricula, h.mediafinal, mp.ano, mp.semestre, nometurma ");
			sqlStr.append("ORDER BY nomecurso, nomealuno, d.nome;");
		} else if (tipoLayout.equals("cursoDisciplinaAluno")) {
			sqlStr.append("GROUP BY nomecurso, d.nome,  nomealuno, p.email,d.codigo, p.codigo, matriculaperiodoturmadisciplina.codigo, nomeunidadeensino, m.matricula, h.mediafinal, mp.ano, mp.semestre, nometurma ");
			sqlStr.append("ORDER BY nomecurso, d.nome,  nomealuno;");
		}

		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return tabelaResultado;
		} finally {
			sqlStr = null;
		}
	}

	private AlunosReprovadosRelVO montarDados(SqlRowSet tabelaResultado, String tipoRelatorio) {
		AlunosReprovadosRelVO alunosReprovadosRelVO = new AlunosReprovadosRelVO();
		alunosReprovadosRelVO.setAno(tabelaResultado.getString("ano"));
		alunosReprovadosRelVO.setSemestre(tabelaResultado.getString("semestre"));
		alunosReprovadosRelVO.setNomeUnidadeEnsino(tabelaResultado.getString("nomeUnidadeEnsino"));
		alunosReprovadosRelVO.setNomeCurso(tabelaResultado.getString("nomeCurso"));
		alunosReprovadosRelVO.setNomeTurma(tabelaResultado.getString("nomeTurma"));
		alunosReprovadosRelVO.setNomeDisciplina(tabelaResultado.getString("nomeDisciplina"));
		alunosReprovadosRelVO.setNomeAluno(tabelaResultado.getString("nomeAluno"));
		alunosReprovadosRelVO.setEmailAluno(tabelaResultado.getString("emailAluno"));
		alunosReprovadosRelVO.setCodigoAluno(tabelaResultado.getInt("codigoAluno"));
		alunosReprovadosRelVO.setMatricula(tabelaResultado.getString("matricula"));
		alunosReprovadosRelVO.setNota(tabelaResultado.getDouble("mediaFinal"));
		alunosReprovadosRelVO.setRealizouReposicao(tabelaResultado.getBoolean("realizouReposicao"));
		return alunosReprovadosRelVO;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorioCursoAlunoDisciplina() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeCursoAlunoDisciplina() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorioCursoDisciplinaAluno() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeCursoDisciplinaAluno() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AlunosReprovadosRel");
	}
	
	public static String getIdEntidadeCursoAlunoDisciplina() {
		return ("AlunosPorDisciplinasCursoAlunoDisciplinaLayoutRel");
	}
	
	public static String getIdEntidadeCursoDisciplinaAluno() {
		return ("AlunosPorDisciplinasCursoDisciplinaAlunoLayoutRel");
	}

	public void setAlunosReprovadosRelVO(AlunosReprovadosRelVO alunosReprovadosRelVO) {
		this.alunosReprovadosRelVO = alunosReprovadosRelVO;
	}

	public AlunosReprovadosRelVO getAlunosReprovadosRelVO() {
		if (alunosReprovadosRelVO == null) {
			alunosReprovadosRelVO = new AlunosReprovadosRelVO();
		}
		return alunosReprovadosRelVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public String realizarEnvioComunicacaoInternaAlunos(String motivoReprovacao, String tipoRelatorio, String situacaoAlunoReposicao, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		List<AlunosReprovadosRelVO> alunosReprovadosRelVOs = null;
		try {
			validarDados(tipoRelatorio, unidadeEnsino, curso, turma, disciplina);
			if(Uteis.retiraTags(comunicacaoInternaVO.getMensagem()).isEmpty()){
				throw new ConsistirException("O campo MENSAGEM deve ser informado para a geração do relatório.");
			}
			alunosReprovadosRelVOs = criarObjeto(motivoReprovacao, tipoRelatorio, situacaoAlunoReposicao, unidadeEnsino, curso, turma, disciplina, ano, semestre,filtroRelatorioAcademicoVO, false, false, "", false);
			if (!alunosReprovadosRelVOs.isEmpty()) {
				for (AlunosReprovadosRelVO alunosReprovadosRelVO : alunosReprovadosRelVOs) {
					ComunicacaoInternaVO comunicacaoEnviar = new ComunicacaoInternaVO();
					comunicacaoEnviar.setAssunto(comunicacaoInternaVO.getAssunto());
					comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
					if(usuario.getPessoa().getCodigo() > 0){
						comunicacaoEnviar.getResponsavel().setCodigo(usuario.getPessoa().getCodigo());
						comunicacaoEnviar.getResponsavel().setNome(usuario.getPessoa().getNome());
						comunicacaoEnviar.getResponsavel().setEmail(usuario.getPessoa().getEmail());
					}else{
						comunicacaoEnviar.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
					}
					comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
					comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
					comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
					comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
					comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
					comunicacaoEnviar.setEnviarEmail(true);
					comunicacaoEnviar.setMensagem(getTextoEmailComTagsSubstituidas(comunicacaoInternaVO.getMensagem(), alunosReprovadosRelVO));
					comunicacaoEnviar.setTipoDestinatario("AL");
					comunicacaoEnviar.getAluno().setCodigo(alunosReprovadosRelVO.getCodigoAluno());
					comunicacaoEnviar.getAluno().setNovoObj(false);
					comunicacaoEnviar.getAluno().setNome(alunosReprovadosRelVO.getNomeAluno());
					comunicacaoEnviar.getAluno().setEmail(alunosReprovadosRelVO.getEmailAluno());
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setDestinatario(comunicacaoEnviar.getAluno());
					comunicadoInternoDestinatarioVO.setEmail(comunicacaoEnviar.getAluno().getEmail());
					comunicadoInternoDestinatarioVO.setNome(comunicacaoEnviar.getAluno().getNome());
					comunicadoInternoDestinatarioVO.setCiJaLida(false);
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
					comunicacaoEnviar.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, configuracaoGeralSistemaVO, null);
					comunicacaoEnviar = null;
					comunicadoInternoDestinatarioVO = null;
					alunosReprovadosRelVO = null;
				}
				return UteisJSF.internacionalizar("msg_AlunosReprovadosRel_comAlunoEmail").replace("{0}", String.valueOf(alunosReprovadosRelVOs.size()));
			} else {
				return UteisJSF.internacionalizar("msg_AlunosReprovadosRel_semAlunoEmail");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (alunosReprovadosRelVOs != null) {
				alunosReprovadosRelVOs.clear();
				alunosReprovadosRelVOs = null;
			}
		}
	}

	private String getTextoEmailComTagsSubstituidas(String mensagem, AlunosReprovadosRelVO alunosReprovadosRelVO) {
		mensagem = mensagem.replaceAll("\"../imagens", "\"./imagens");
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.toString(), alunosReprovadosRelVO.getNomeAluno());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.toString(), alunosReprovadosRelVO.getNomeUnidadeEnsino());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.toString(), alunosReprovadosRelVO.getNomeDisciplina());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.toString(), alunosReprovadosRelVO.getNomeCurso());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TURMA.toString(), alunosReprovadosRelVO.getNomeTurma());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.toString(), alunosReprovadosRelVO.getMatricula());
		return mensagem;
	}

}
