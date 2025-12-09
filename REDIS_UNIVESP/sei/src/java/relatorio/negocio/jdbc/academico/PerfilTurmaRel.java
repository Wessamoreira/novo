package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.PerfilTurmaRelVO;
import relatorio.negocio.interfaces.academico.PerfilTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.parametroRelatorio.academico.PerfilTurmaSuperParametroRelVO;

@Repository
@Scope("singleton")
@Lazy
public class PerfilTurmaRel extends SuperRelatorio implements PerfilTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public PerfilTurmaRel() {
	}

	public void validarDados(Integer unidadeEnsino, String identificarTurma) throws Exception {
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(identificarTurma)) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
	}

	public List<PerfilTurmaRelVO> criarObjeto(Integer curso, Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean apresentarFoto, String caminhoImagemPadrao, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarAlunoPendenteFinanceiramente, boolean permitirRealizarLancamentoAlunosPreMatriculados, Integer codigoUnidadeEnsino) throws Exception {
		List<PerfilTurmaRelVO> listaRelatorio = consultarAlunosTurmaSituacao(curso, turma, ano, semestre, situacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiro, usuario, perfilTurmaSuperParametroRelVO, configuracaoGeralSistemaVO, apresentarFoto, caminhoImagemPadrao, filtroRelatorioAcademicoVO, apresentarAlunoPendenteFinanceiramente, permitirRealizarLancamentoAlunosPreMatriculados, codigoUnidadeEnsino);
		if (!listaRelatorio.isEmpty()) {
			for (PerfilTurmaRelVO perfilTurmaRelVO : listaRelatorio) {
				perfilTurmaRelVO.setFormacaoAcademicaRelVOs(consultaFormacaoAcademica(perfilTurmaRelVO.getCodigoAluno(), perfilTurmaSuperParametroRelVO));
			}
		}
		return listaRelatorio;
	}

	private List<FormacaoAcademicaVO> consultaFormacaoAcademica(Integer codigoAluno, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO) throws Exception {
		List<FormacaoAcademicaVO> lista = new ArrayList<FormacaoAcademicaVO>(0);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT curso, instituicao, escolaridade, datafim, anoDataFim, semestreDataFim FROM formacaoacademica WHERE pessoa = ");
		sql.append(codigoAluno.intValue());
		sql.append(" ORDER BY datafim ");
		SqlRowSet dadosSql = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (dadosSql.next()) {
			lista.add(montarDadosFormacaoAcademica(dadosSql, perfilTurmaSuperParametroRelVO));
		}
		return lista;
	}

	private FormacaoAcademicaVO montarDadosFormacaoAcademica(SqlRowSet dadosSql, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO) throws Exception {
		FormacaoAcademicaVO obj = new FormacaoAcademicaVO();
		obj.setCurso(dadosSql.getString("curso"));
		obj.setInstituicao(dadosSql.getString("instituicao"));
		obj.setEscolaridade(dadosSql.getString("escolaridade"));
		obj.setDataFim(dadosSql.getDate("dataFim"));
		obj.setAnoDataFim(dadosSql.getString("anoDataFim"));
		obj.setSemestreDataFim(dadosSql.getString("semestreDataFim"));
		executarMontagemEscolaridadeAluno(perfilTurmaSuperParametroRelVO, obj.getEscolaridade());
		return obj;
	}

	public void executarMontagemEscolaridadeAluno(PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, String escolaridade) {
		if (escolaridade.equals("EF")) {
			perfilTurmaSuperParametroRelVO.setQtdeFundamental(perfilTurmaSuperParametroRelVO.getQtdeFundamental() + 1);
		} else if (escolaridade.equals("EM")) {
			perfilTurmaSuperParametroRelVO.setQtdeMedio(perfilTurmaSuperParametroRelVO.getQtdeMedio() + 1);
		} else if (escolaridade.equals("TE")) {
			perfilTurmaSuperParametroRelVO.setQtdeTecnico(perfilTurmaSuperParametroRelVO.getQtdeTecnico() + 1);
		} else if (escolaridade.equals("GR")) {
			perfilTurmaSuperParametroRelVO.setQtdeGraduacao(perfilTurmaSuperParametroRelVO.getQtdeGraduacao() + 1);
		} else if (escolaridade.equals("EP")) {
			perfilTurmaSuperParametroRelVO.setQtdeEspecializacao(perfilTurmaSuperParametroRelVO.getQtdeEspecializacao() + 1);
		} else if (escolaridade.equals("MS")) {
			perfilTurmaSuperParametroRelVO.setQtdeMestrado(perfilTurmaSuperParametroRelVO.getQtdeMestrado() + 1);
		} else if (escolaridade.equals("DR")) {
			perfilTurmaSuperParametroRelVO.setQtdeDoutorado(perfilTurmaSuperParametroRelVO.getQtdeDoutorado() + 1);
		} else if (escolaridade.equals("PD")) {
			perfilTurmaSuperParametroRelVO.setQtdePosDoutorado(perfilTurmaSuperParametroRelVO.getQtdePosDoutorado() + 1);
		}
	}

	public List<PerfilTurmaRelVO> consultarAlunosTurmaSituacao(Integer curso, Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean apresentarFoto, String caminhoImagemPadrao, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarAlunoPendenteFinanceiramente, boolean permitirRealizarLancamentoAlunosPreMatriculados, Integer codigoUnidadeEnsino) throws Exception {
		TurmaVO turmaVO = new TurmaVO();
		turmaVO.setCodigo(turma);
		getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, NivelMontarDados.BASICO, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT m.matricula, pessoa.codigo as codigoAluno, pessoa.nome as nome,  sem_acentos(pessoa.nome) as nomePessoaSemAcento, pessoa.email, pessoa.email2, ");
		sqlStr.append(" dadosComerciais.nomeEmpresa, pessoa.dataNasc, pessoa.sexo, unidadeensino.nome as unidadeensino, curso.nome as curso, curso.codigo, ");
		sqlStr.append(" turma.identificadorTurma as turma, p2.nome as coordenador, cidade.nome as cidade, ");
		sqlStr.append(" arquivo.pastaBaseArquivo as pastaBaseArquivoPessoa, arquivo.nome as nomeArquivoPessoa , arquivo.codigo as codigoArquivoPessoa");
		sqlStr.append(" FROM Matricula as m ");
		sqlStr.append(" INNER JOIN unidadeensino ON m.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" INNER JOIN curso ON m.curso = curso.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodo as mp ON m.matricula = mp.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = m.aluno");
		sqlStr.append(" left join arquivo on arquivo.codigo = pessoa.arquivoImagem");
		sqlStr.append(" INNER JOIN turma ON mp.turma = turma.codigo");
		sqlStr.append(" LEFT JOIN cidade ON pessoa.cidade = cidade.codigo");
		sqlStr.append(" LEFT JOIN dadosComerciais ON pessoa.codigo = dadosComerciais.pessoa and empregoatual = true");
		sqlStr.append(" LEFT JOIN cursocoordenador cc ON cc.codigo = (case when (cc.turma = turma.codigo and cc.curso = curso.codigo) then cc.codigo else case when (");
		sqlStr.append(" cc.unidadeensino = turma.codigo and cc.curso = curso.codigo ) then cc.codigo else case when(cc.curso = curso.codigo) then cc.codigo end end end)");
		sqlStr.append(" LEFT JOIN funcionario on funcionario.codigo = (select cc.funcionario from cursocoordenador cc where ");
		sqlStr.append(" cc.tipoCoordenadorCurso = '").append(TipoCoordenadorCursoEnum.GERAL.name()).append("' ");
		sqlStr.append(" and true = (case when (cc.turma = turma.codigo and cc.curso = curso.codigo) then true else ");
		sqlStr.append(" case when (cc.unidadeensino = unidadeensino.codigo and cc.curso = curso.codigo ) then true else ");
		sqlStr.append(" case when(cc.curso = curso.codigo) then true end end end) order by cc.turma, cc.unidadeensino, cc.curso limit 1)");
		sqlStr.append(" left JOIN pessoa as p2 ON funcionario.pessoa = p2.codigo ");
		sqlStr.append(" WHERE 1 = 1 ");		
		if(!turmaVO.getIntegral()) {
			sqlStr.append(" AND (mp.ano = '").append(ano).append("') ");
		}
		if(turmaVO.getSemestral()) {
			sqlStr.append(" AND (mp.semestre = '").append(semestre).append("') ");
		}		
			sqlStr.append(" and exists (select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina ");
			sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
			sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo =  mp.codigo ");
			sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
			sqlStr.append(" where matriculaperiodoturmadisciplina.matriculaperiodo = mp.codigo ");
			if((!turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) || (turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL))) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turmaVO.getCodigo());		
			}else if(turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turmapratica = ").append(turmaVO.getCodigo());
				
			}else if(turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turmateorica = ").append(turmaVO.getCodigo());
			}else if(turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.turma in (select turmaagrupada.turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo()).append(") ");
				sqlStr.append(" and ( (matriculaperiodoturmadisciplina.disciplina in (select turmadisciplina.disciplina from turmaagrupada inner join turmadisciplina on turmadisciplina.turma = turmaorigem  where turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
				sqlStr.append(" or (matriculaperiodoturmadisciplina.disciplina in (select disciplinaequivalente.equivalente from turmaagrupada inner join turmadisciplina on turmadisciplina.turma = turmaorigem inner join disciplinaequivalente on disciplinaequivalente.disciplina = turmadisciplina.disciplina where turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
				sqlStr.append(" or (matriculaperiodoturmadisciplina.disciplina in (select disciplinaequivalente.disciplina from turmaagrupada inner join turmadisciplina on turmadisciplina.turma = turmaorigem inner join disciplinaequivalente on disciplinaequivalente.disciplina = turmadisciplina.disciplina  where turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
				sqlStr.append(" or (matriculaperiodoturmadisciplina.disciplina in (select turmadisciplina.disciplinaEquivalenteTurmaAgrupada from turmaagrupada inner join turmadisciplina on turmadisciplina.turma = turmaorigem  where disciplinaEquivalenteTurmaAgrupada is not null and turmaorigem = ").append(turmaVO.getCodigo()).append(")) ");
				sqlStr.append(" ) ");
			}
			if(usuario.getIsApresentarVisaoProfessor()) {
				sqlStr.append(" and exists (select horarioturma.codigo from horarioturma  ");
				sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
				sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
				if(turmaVO.getTurmaAgrupada()) {
					sqlStr.append(" where (horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
					sqlStr.append(" or horarioturmadiaitem.disciplina in (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente =  matriculaperiodoturmadisciplina.disciplina ) ");
					sqlStr.append(" or horarioturmadiaitem.disciplina in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina =  matriculaperiodoturmadisciplina.disciplina ) ");
					sqlStr.append(" or horarioturmadiaitem.disciplina in (select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplinaEquivalenteTurmaAgrupada is not null and turmadisciplina.disciplinaEquivalenteTurmaAgrupada =  matriculaperiodoturmadisciplina.disciplina ) ");
					sqlStr.append(" ) ");
				}else {
					sqlStr.append(" where horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");
				}
				sqlStr.append(" and horarioturma.turma =  ").append(turmaVO.getCodigo());
				if(!turmaVO.getIntegral()) {
					sqlStr.append(" AND (horarioturma.anovigente = '").append(ano).append("') ");
				}
				if(turmaVO.getSemestral()) {
					sqlStr.append(" AND (horarioturma.semestrevigente = '").append(semestre).append("') ");
				}	
				
				sqlStr.append(" limit 1) ");
			}
			
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
			sqlStr.append(" limit 1) ");
		
		
		if (filtroRelatorioAcademicoVO != null) {
			sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
			sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		} else {
			if (!apresentarAlunoPendenteFinanceiramente) {
				sqlStr.append(" AND mp.situacao != 'PF'");
			}
		}
		
		if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados) {
			sqlStr.append(" and mp.situacaoMatriculaPeriodo <> 'PR' ");
		}
		
		if (!apresentarAlunoPendenteFinanceiramente) {
			sqlStr.append(" AND mp.situacao != 'PF'");
		}
		if (!situacao.equals("")) {
			sqlStr.append(" AND m.situacao = '");
			sqlStr.append(situacao);
			sqlStr.append("' ");
		}
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			sqlStr.append(" AND m.unidadeensino = ").append(codigoUnidadeEnsino);
		}
		sqlStr.append(" ORDER BY nomePessoaSemAcento ");	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario, perfilTurmaSuperParametroRelVO, configuracaoGeralSistemaVO, apresentarFoto, caminhoImagemPadrao));
	}

	public static List<PerfilTurmaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean apresentarFoto, String caminhoImagemPadrao) throws Exception {
		List<PerfilTurmaRelVO> vetResultado = new ArrayList<PerfilTurmaRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario, perfilTurmaSuperParametroRelVO, configuracaoGeralSistemaVO, apresentarFoto, caminhoImagemPadrao));
		}
		return vetResultado;
	}

	public static PerfilTurmaRelVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario, PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean apresentarFoto, String caminhoImagemPadrao) throws Exception {
		PerfilTurmaRelVO obj = new PerfilTurmaRelVO();
		obj.setCodigoAluno(new Integer(dadosSQL.getInt("codigoAluno")));
		obj.setAluno(dadosSQL.getString("nome"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setLocalTrabalho(dadosSQL.getString("nomeEmpresa"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setCoordenadorCurso(dadosSQL.getString("coordenador"));
		obj.setTurma(dadosSQL.getString("turma"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setDataNasc(dadosSQL.getDate("dataNasc"));
		obj.setIdade(Uteis.calcularIdadePessoa(new Date(), obj.getDataNasc()));
		perfilTurmaSuperParametroRelVO.setSomaIdade(perfilTurmaSuperParametroRelVO.getSomaIdade() + obj.getIdade());
		if (obj.getSexo().equals("M")) {
			perfilTurmaSuperParametroRelVO.setQtdeMasculino(perfilTurmaSuperParametroRelVO.getQtdeMasculino() + 1);
		} else {
			perfilTurmaSuperParametroRelVO.setQtdeFeminino(perfilTurmaSuperParametroRelVO.getQtdeFeminino() + 1);
		}
		if (apresentarFoto) {
			if (dadosSQL.getInt("codigoArquivoPessoa") > 0) {
				obj.setCodigoArquivo(dadosSQL.getInt("codigoArquivoPessoa"));
				obj.setNomeArquivo(dadosSQL.getString("nomeArquivoPessoa"));
				obj.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator);
				obj.setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/");
				obj.setFotoAluno(obj.getPastaBaseArquivo() + obj.getNomeArquivo());
			} else {
				obj.setFotoAluno(caminhoImagemPadrao);
			}
		}
		return obj;
	}

	public String getDesignIReportRelatorioPerfilTurma() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePerfilTurmaRel() + ".jrxml");
	}

	public String getCaminhoBaseRelatorioPerfilTurma() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getDesignIReportRelatorioPerfilTurmaFoto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePerfilTurmaRel() + "Foto.jrxml");
	}

	public String getIdEntidadePerfilTurmaRel() {
		return ("PerfilTurmaRel");
	}
}
