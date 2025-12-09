package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDefinicaoPeriodoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.ead.AvaliacaoOnlineInterfaceFacade;

/**
 * @author Victor Hugo 10/10/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnline extends ControleAcesso implements AvaliacaoOnlineInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AvaliacaoOnline.idEntidade = idEntidade;
	}

	public AvaliacaoOnline() throws Exception {
		super();
		setIdEntidade("AvaliacaoOnline");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(avaliacaoOnlineVO);
			AvaliacaoOnline.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO avaliacaoonline(" + "nome, tipouso, disciplina, situacao, datacriacao, responsavelcriacao, " + "dataativacao, responsavelativacao, datainativacao, responsavelinativacao, " + "tipogeracaoprovaonline, quantidadenivelquestaomedio, quantidadenivelquestaofacil, " + "quantidadenivelquestaodificil, quantidadequalquernivelquestao, " + "notaporquestaonivelmedio, notaporquestaonivelfacil, notaporquestaoniveldificil, " + "percentualaprovacao , notamaximaavaliacao, conteudo, usoexclusivoprofessor, professor, politicaselecaoquestao, regradistribuicaoquestao, " + "permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno, parametromonitoramentoavaliacaoonline, tempoLimiteRealizacaoAvaliacaoOnline, " + "variavelNotaCfgPadraoAvaliacaoOnlineRea,  unidadeconteudo, qtdDiasLimiteResponderAvaliacaOnline , regraDefinicaoPeriodoAvaliacaoOnline, "
					+ "turma, dataInicioAvaliacaoFixo, dataTerminoAvaliacaoFixo, ano, semestre, apresentarNotaDaQuestao , apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao, " 
					+ "limiteTempoProvaAlunoDentroPeriodoRealizacao, randomizarApenasQuestoesCadastradasPeloProfessor , nrDiasEntreAvalicaoOnline , nrVezesPodeRepetirAvaliacaoOnline  )" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ? ,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			avaliacaoOnlineVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);

					sqlInserir.setString(1, avaliacaoOnlineVO.getNome());
					sqlInserir.setString(2, avaliacaoOnlineVO.getTipoUso().name());
					if (avaliacaoOnlineVO.getDisciplinaVO().getCodigo() > 0) {
						sqlInserir.setInt(3, avaliacaoOnlineVO.getDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, avaliacaoOnlineVO.getSituacao().name());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(avaliacaoOnlineVO.getDataCriacao()));
					if (avaliacaoOnlineVO.getReposnsavelCriacao().getCodigo() != 0) {
						sqlInserir.setInt(6, avaliacaoOnlineVO.getReposnsavelCriacao().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (avaliacaoOnlineVO.getReposnsavelAtivacao().getCodigo() > 0) {
						sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(avaliacaoOnlineVO.getDataAtivacao()));
						sqlInserir.setInt(8, avaliacaoOnlineVO.getReposnsavelAtivacao().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
						sqlInserir.setNull(8, 0);
					}
					if (avaliacaoOnlineVO.getReposnsavelInativacao().getCodigo() > 0) {
						sqlInserir.setTimestamp(9, Uteis.getDataJDBCTimestamp(avaliacaoOnlineVO.getDataInativacao()));
						sqlInserir.setInt(10, avaliacaoOnlineVO.getReposnsavelInativacao().getCodigo());
					} else {
						sqlInserir.setNull(9, 0);
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setString(11, avaliacaoOnlineVO.getTipoGeracaoProvaOnline().name());
					sqlInserir.setInt(12, avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio());
					sqlInserir.setInt(13, avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil());
					sqlInserir.setInt(14, avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil());
					sqlInserir.setInt(15, avaliacaoOnlineVO.getQuantidadeQualquerNivelQuestao());
					sqlInserir.setDouble(16, avaliacaoOnlineVO.getNotaPorQuestaoNivelMedio());
					sqlInserir.setDouble(17, avaliacaoOnlineVO.getNotaPorQuestaoNivelFacil());
					sqlInserir.setDouble(18, avaliacaoOnlineVO.getNotaPorQuestaoNivelDificil());
					sqlInserir.setInt(19, avaliacaoOnlineVO.getPercentualAprovacao());
					sqlInserir.setDouble(20, avaliacaoOnlineVO.getNotaMaximaAvaliacao());

					if (!avaliacaoOnlineVO.getConteudoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(21, avaliacaoOnlineVO.getConteudoVO().getCodigo());
					} else {
						sqlInserir.setNull(21, 0);
					}
					sqlInserir.setBoolean(22, avaliacaoOnlineVO.getUsoExclusivoProfessor());
					if (!avaliacaoOnlineVO.getProfessor().getCodigo().equals(0)) {
						sqlInserir.setInt(23, avaliacaoOnlineVO.getProfessor().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					sqlInserir.setString(24, avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum().getName());
					sqlInserir.setString(25, avaliacaoOnlineVO.getRegraDistribuicaoQuestaoEnum().getName());
					sqlInserir.setBoolean(26, avaliacaoOnlineVO.getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno());
					sqlInserir.setInt(27, avaliacaoOnlineVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
					sqlInserir.setInt(28, avaliacaoOnlineVO.getTempoLimiteRealizacaoAvaliacaoOnline());
					sqlInserir.setString(29, avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea());
					if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getUnidadeConteudoVO())) {
						sqlInserir.setInt(30, avaliacaoOnlineVO.getUnidadeConteudoVO().getCodigo());
					} else {
						sqlInserir.setNull(30, 0);
					}
					sqlInserir.setInt(31, avaliacaoOnlineVO.getQtdDiasLimiteResponderAvaliacaOnline());
					sqlInserir.setString(32, avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().name());
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getTurmaVO(), 33, sqlInserir);
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo(), Types.TIMESTAMP, 34, sqlInserir);
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo(), Types.TIMESTAMP, 35, sqlInserir);
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getAno(), 36, sqlInserir);
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getSemestre(), 37, sqlInserir);
					Uteis.setValuePreparedStatement(avaliacaoOnlineVO.isApresentarNotaDaQuestao(), 38, sqlInserir);
					sqlInserir.setBoolean(39, avaliacaoOnlineVO.getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao());
					sqlInserir.setBoolean(40, avaliacaoOnlineVO.isLimiteTempoProvaAlunoDentroPeriodoRealizacao());
					sqlInserir.setBoolean(41, avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor());
					sqlInserir.setInt(42, avaliacaoOnlineVO.getNrDiasEntreAvalicaoOnline());
					sqlInserir.setInt(43, avaliacaoOnlineVO.getNrVezesPodeRepetirAvaliacaoOnline());					
					return sqlInserir;
				}

			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						avaliacaoOnlineVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().persistirQuestoesAvaliacaoOnline(avaliacaoOnlineVO, usuarioVO);
			getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().persistir(avaliacaoOnlineVO, usuarioVO);
			avaliacaoOnlineVO.setDataInicioAvaliacaoFixoAntesAlteracao(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo());
			avaliacaoOnlineVO.setDataTerminoAvaliacaoFixoAntesAlteracao(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo());
		} catch (Exception e) {
			avaliacaoOnlineVO.setNovoObj(Boolean.TRUE);
			avaliacaoOnlineVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AvaliacaoOnlineVO avaliacaoOnlineVO) throws Exception {
		if (avaliacaoOnlineVO.getTipoUso().isTurma() &&  !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getTurmaVO())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_turma"));
		}
		if (avaliacaoOnlineVO.getTipoUso().isTurma() && (avaliacaoOnlineVO.getTurmaVO().getSemestral() || avaliacaoOnlineVO.getTurmaVO().getAnual()) && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getAno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_ano"));
		}
		if (avaliacaoOnlineVO.getTipoUso().isTurma() && avaliacaoOnlineVO.getTurmaVO().getSemestral() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getSemestre())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_semestre"));
		}
		if ((avaliacaoOnlineVO.getTipoUso().isTurma() || avaliacaoOnlineVO.getTipoUso().isDisciplina()) &&  !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDisciplinaVO())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_disciplina"));
		}
		if (avaliacaoOnlineVO.getNome().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_digiteoNomedaAvaliacao"));
		}
		if (avaliacaoOnlineVO.getUsoExclusivoProfessor()) {
			if (avaliacaoOnlineVO.getProfessor().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_informeUmProfessor"));
			}
		}
		if(avaliacaoOnlineVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_informeUmParametroMonitoramentoAvaliacaoOnline"));
		}
		if (avaliacaoOnlineVO.getPercentualAprovacao().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_informeOPercentualdeAcertosparaConsiderarAprovado"));
		}
		if(avaliacaoOnlineVO.getTempoLimiteRealizacaoAvaliacaoOnline().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_tempoLimiteRealizacaoAvaliacaoOnlineObrigatorio"));
		}
		if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isNumeroDiaEspecifico() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getQtdDiasLimiteResponderAvaliacaOnline())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_qtdDiasLimiteResponderAvaliacaOnline"));
		}
		if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isPeriodoDiaFixo() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_dataInicioAvaliacaoFixo"));
		}
		if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isPeriodoDiaFixo() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_dataTerminoAvaliacaoFixo"));
		}
		if(Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo()) && Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo()) && UteisData.getCompareDataComHora(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo(), avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo()) > 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_dataInicioAvaliacaoFixoMaiordataTerminoAvaliacaoFixo"));
		}
		if(Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo()) && Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo()) && UteisData.getCompareDataComHora(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo(), avaliacaoOnlineVO.getDataInicioAvaliacaoFixo()) < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_dataTerminoAvaliacaoFixoMenordataInicioAvaliacaoFixo"));
		}
		if(( avaliacaoOnlineVO.getTipoUso().isRea() || avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().equals(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA)) && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea().trim())) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_variavelNotaCfgPadraoAvaliacaoOnlineRea"));
		}		
		if ( (avaliacaoOnlineVO.getTipoUso().isRea() ||  avaliacaoOnlineVO.getTipoUso().isTurma())  && avaliacaoOnlineVO.getNrDiasEntreAvalicaoOnline() < 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_NrDiasEntreAvalicaoOnline"));
		}
		if( (avaliacaoOnlineVO.getTipoUso().isRea() ||  avaliacaoOnlineVO.getTipoUso().isTurma()) &&  avaliacaoOnlineVO.getNrVezesPodeRepetirAvaliacaoOnline() < 0 ) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_VezesPodeRepetirAvaliacaoOnline"));
		}
		if (avaliacaoOnlineVO.getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_nenhumaQuestaoIncluidaNaAvaliacaoOnline"));
		}
		if (avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil().equals(0) && avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil().equals(0) && avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio().equals(0) && avaliacaoOnlineVO.getQuantidadeQualquerNivelQuestao().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_informeaoMenosUmaQuestaoaserIncluidanaAvaliacaoOnLine"));
		}
		
		if (avaliacaoOnlineVO.getTempoLimiteRealizacaoAvaliacaoOnline() < 0) {
			throw new Exception(UteisJSF.internacionalizar("O Campo Tempo de Realização (Em Minutos) Não Pode Ser Negativo."));
		}
		
		Uteis.checkState(Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil()) && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getNotaPorQuestaoNivelFacil()), "O campo Nota Por Questão Fáceis deve ser informado");
		Uteis.checkState(Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio()) && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getNotaPorQuestaoNivelMedio()), "O campo Nota Por Questão Medianas deve ser informado.");
		Uteis.checkState(Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil()) && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getNotaPorQuestaoNivelDificil()), "O campo Nota Por Questão Difíceis deve ser informado.");
		if(avaliacaoOnlineVO.getTipoUso().isTurma() && !Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea())) {
			String variavelNotaCfgUtilizar =  getFacadeFactory().getConfiguracaoEADFacade().consultarVariavelNotaCfgPadraoAvaliacaoOnlineDaConfiguracaoEadPorTurma(avaliacaoOnlineVO.getTurmaVO().getCodigo()); 
			Uteis.checkState(!Uteis.isAtributoPreenchido(variavelNotaCfgUtilizar), "Não foi localizado a Variável de Nota da Avaliacao Online na Configuração do Ead para essa turma sendo assim ou informe a Variável Nota na Avaliação ou altere o Cadastro da Configuração do Ead.");
		}
		if(avaliacaoOnlineVO.getTipoUso().isGeral()) {
			avaliacaoOnlineVO.setAno("");
			avaliacaoOnlineVO.setSemestre("");
			avaliacaoOnlineVO.setTurmaVO(new TurmaVO());
			avaliacaoOnlineVO.setDisciplinaVO(new DisciplinaVO());			
		}else if(avaliacaoOnlineVO.getTipoUso().isDisciplina()) {
			avaliacaoOnlineVO.setAno("");
			avaliacaoOnlineVO.setSemestre("");
			avaliacaoOnlineVO.setTurmaVO(new TurmaVO());
		}
		
		if(avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum().isQualquerQuestao()) {
			avaliacaoOnlineVO.setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum.NENHUM);
		}
		
		if(!avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isPeriodoDiaFixo()) {
			avaliacaoOnlineVO.setDataInicioAvaliacaoFixo(null);
			avaliacaoOnlineVO.setDataTerminoAvaliacaoFixo(null);
		}else if(!avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isNumeroDiaEspecifico()) {
			avaliacaoOnlineVO.setQtdDiasLimiteResponderAvaliacaOnline(0);
		}
		if(!avaliacaoOnlineVO.getIsPermiteInformarTemaAssunto()) {
			avaliacaoOnlineVO.getAvaliacaoOnlineTemaAssuntoVOs().clear();
	}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO.getIsApresentarVisaoProfessor()) {
			avaliacaoOnlineVO.setUsoExclusivoProfessor(true);
			avaliacaoOnlineVO.setProfessor(usuarioVO.getPessoa());
		}
		if (avaliacaoOnlineVO.getCodigo() == 0) {
			incluir(avaliacaoOnlineVO, verificarAcesso, usuarioVO);
		} else {
			alterar(avaliacaoOnlineVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(avaliacaoOnlineVO);
		AvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
		final String sql = "UPDATE avaliacaoonline" + " SET nome=?, tipouso=?, disciplina=?, situacao=?, " + "tipogeracaoprovaonline=?, quantidadenivelquestaomedio=?, quantidadenivelquestaofacil=?, " + "quantidadenivelquestaodificil=?, quantidadequalquernivelquestao=?, notaporquestaonivelmedio=?, notaporquestaonivelfacil=?, notaporquestaoniveldificil=?, " + "percentualaprovacao=? , notamaximaavaliacao = ?, conteudo = ?, usoexclusivoprofessor = ?, professor = ?, politicaselecaoquestao = ?, regradistribuicaoquestao = ?, " + "permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno = ?, parametromonitoramentoavaliacaoonline = ?, tempoLimiteRealizacaoAvaliacaoOnline = ?, " + "variavelNotaCfgPadraoAvaliacaoOnlineRea=?, unidadeconteudo=?, qtdDiasLimiteResponderAvaliacaOnline=?, regraDefinicaoPeriodoAvaliacaoOnline=?, "
				+ "turma=?, dataInicioAvaliacaoFixo=?, dataTerminoAvaliacaoFixo=?, ano=?, semestre=?, apresentarNotaDaQuestao=? , apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao = ?,"
				+ "limiteTempoProvaAlunoDentroPeriodoRealizacao=?, randomizarApenasQuestoesCadastradasPeloProfessor = ?  ,  nrDiasEntreAvalicaoOnline=?  , nrVezesPodeRepetirAvaliacaoOnline=? " 
				+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, avaliacaoOnlineVO.getNome());
				sqlAlterar.setString(2, avaliacaoOnlineVO.getTipoUso().name());
				if (avaliacaoOnlineVO.getDisciplinaVO().getCodigo() > 0) {
					sqlAlterar.setInt(3, avaliacaoOnlineVO.getDisciplinaVO().getCodigo());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, avaliacaoOnlineVO.getSituacao().name());

				sqlAlterar.setString(5, avaliacaoOnlineVO.getTipoGeracaoProvaOnline().name());
				sqlAlterar.setInt(6, avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio());
				sqlAlterar.setInt(7, avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil());
				sqlAlterar.setInt(8, avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil());
				sqlAlterar.setInt(9, avaliacaoOnlineVO.getQuantidadeQualquerNivelQuestao());
				sqlAlterar.setDouble(10, avaliacaoOnlineVO.getNotaPorQuestaoNivelMedio());
				sqlAlterar.setDouble(11, avaliacaoOnlineVO.getNotaPorQuestaoNivelFacil());
				sqlAlterar.setDouble(12, avaliacaoOnlineVO.getNotaPorQuestaoNivelDificil());
				sqlAlterar.setInt(13, avaliacaoOnlineVO.getPercentualAprovacao());
				sqlAlterar.setDouble(14, avaliacaoOnlineVO.getNotaMaximaAvaliacao());
				if (avaliacaoOnlineVO.getConteudoVO().getCodigo() != 0) {
					sqlAlterar.setInt(15, avaliacaoOnlineVO.getConteudoVO().getCodigo());
				} else {
					sqlAlterar.setNull(15, 0);
				}
				sqlAlterar.setBoolean(16, avaliacaoOnlineVO.getUsoExclusivoProfessor());
				
				if (!avaliacaoOnlineVO.getProfessor().getCodigo().equals(0)) {
					sqlAlterar.setInt(17, avaliacaoOnlineVO.getProfessor().getCodigo());
				} else {
					sqlAlterar.setNull(17, 0);
				}
				sqlAlterar.setString(18, avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum().getName());
				sqlAlterar.setString(19, avaliacaoOnlineVO.getRegraDistribuicaoQuestaoEnum().getName());
				sqlAlterar.setBoolean(20, avaliacaoOnlineVO.getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno());
				sqlAlterar.setInt(21, avaliacaoOnlineVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
				sqlAlterar.setInt(22, avaliacaoOnlineVO.getTempoLimiteRealizacaoAvaliacaoOnline());
				sqlAlterar.setString(23, avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea());
				if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getUnidadeConteudoVO())) {
					sqlAlterar.setInt(24, avaliacaoOnlineVO.getUnidadeConteudoVO().getCodigo());
				} else {
					sqlAlterar.setNull(24, 0);
				}
				sqlAlterar.setInt(25, avaliacaoOnlineVO.getQtdDiasLimiteResponderAvaliacaOnline());
				sqlAlterar.setString(26, avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().name());
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getTurmaVO(), 27, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo(), Types.TIMESTAMP, 28, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo(), Types.TIMESTAMP, 29, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getAno(), 30, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getSemestre(), 31, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.isApresentarNotaDaQuestao(), 32, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao(), 33, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.isLimiteTempoProvaAlunoDentroPeriodoRealizacao(), 34, sqlAlterar);
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), 35, sqlAlterar);
				sqlAlterar.setInt(36, avaliacaoOnlineVO.getNrDiasEntreAvalicaoOnline());
				sqlAlterar.setInt(37, avaliacaoOnlineVO.getNrVezesPodeRepetirAvaliacaoOnline());		
				Uteis.setValuePreparedStatement(avaliacaoOnlineVO.getCodigo(), 38, sqlAlterar);
				return sqlAlterar;
			}
		}) == 0) {
			incluir(avaliacaoOnlineVO, false, usuarioVO);
			return;
		};
		if (avaliacaoOnlineVO.getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
			getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().excluirAvaliacaoOnlineQuestao(avaliacaoOnlineVO.getCodigo(), false, usuarioVO);
		} else {
			getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().persistirQuestoesAvaliacaoOnline(avaliacaoOnlineVO, usuarioVO);
		}
		getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().persistir(avaliacaoOnlineVO, usuarioVO);
		avaliacaoOnlineVO.setDataInicioAvaliacaoFixoAntesAlteracao(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo());
		avaliacaoOnlineVO.setDataTerminoAvaliacaoFixoAntesAlteracao(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (avaliacaoOnlineVO.getTipoUso().equals(TipoUsoEnum.DISCIPLINA)) {
				getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().exluirQuestaoAvaliacaoOnline(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs(), usuarioVO);
			}
			AvaliacaoOnline.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM avaliacaoonline WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, avaliacaoOnlineVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}	
	
	@Override
	public AvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setNome(tabelaResultado.getString("nome"));
		obj.setTipoUso(TipoUsoEnum.valueOf(tabelaResultado.getString("tipouso")));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("disciplina"))) {
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		}
		obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setDataCriacao(tabelaResultado.getDate("datacriacao"));
		obj.getReposnsavelCriacao().setCodigo(tabelaResultado.getInt("responsavelcriacao"));
		obj.setDataAtivacao(tabelaResultado.getDate("dataativacao"));
		obj.getReposnsavelAtivacao().setCodigo(tabelaResultado.getInt("responsavelativacao"));
		obj.setDataInativacao(tabelaResultado.getDate("datainativacao"));
		obj.getReposnsavelInativacao().setCodigo(tabelaResultado.getInt("responsavelinativacao"));
		obj.setTipoGeracaoProvaOnline(TipoGeracaoProvaOnlineEnum.valueOf(tabelaResultado.getString("tipogeracaoprovaonline")));
		obj.setQuantidadeNivelQuestaoMedio(tabelaResultado.getInt("quantidadenivelquestaomedio"));
		obj.setQuantidadeNivelQuestaoFacil(tabelaResultado.getInt("quantidadenivelquestaofacil"));
		obj.setQuantidadeNivelQuestaoDificil(tabelaResultado.getInt("quantidadenivelquestaodificil"));
		obj.setQuantidadeQualquerNivelQuestao(tabelaResultado.getInt("quantidadequalquernivelquestao"));
		obj.setNotaPorQuestaoNivelMedio(tabelaResultado.getDouble("notaporquestaonivelmedio"));
		obj.setNotaPorQuestaoNivelFacil(tabelaResultado.getDouble("notaporquestaonivelfacil"));
		obj.setNotaPorQuestaoNivelDificil(tabelaResultado.getDouble("notaporquestaoniveldificil"));
		obj.setPercentualAprovacao(tabelaResultado.getInt("percentualaprovacao"));
		obj.setNotaMaximaAvaliacao(tabelaResultado.getDouble("notamaximaavaliacao"));
		obj.setTempoLimiteRealizacaoAvaliacaoOnline(tabelaResultado.getInt("tempoLimiteRealizacaoAvaliacaoOnline"));
		obj.setVariavelNotaCfgPadraoAvaliacaoOnlineRea(tabelaResultado.getString("variavelNotaCfgPadraoAvaliacaoOnlineRea"));
		obj.getParametrosMonitoramentoAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("parametromonitoramentoavaliacaoonline"));
		obj.setQtdDiasLimiteResponderAvaliacaOnline(tabelaResultado.getInt("qtdDiasLimiteResponderAvaliacaOnline"));
		obj.setRegraDefinicaoPeriodoAvaliacaoOnline(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.valueOf(tabelaResultado.getString("regraDefinicaoPeriodoAvaliacaoOnline")));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setDataInicioAvaliacaoFixo(tabelaResultado.getTimestamp("dataInicioAvaliacaoFixo"));
		obj.setDataTerminoAvaliacaoFixo(tabelaResultado.getTimestamp("dataTerminoAvaliacaoFixo"));
		obj.setDataInicioAvaliacaoFixoAntesAlteracao(tabelaResultado.getTimestamp("dataInicioAvaliacaoFixo"));
		obj.setDataTerminoAvaliacaoFixoAntesAlteracao(tabelaResultado.getTimestamp("dataTerminoAvaliacaoFixo"));
		obj.setApresentarNotaDaQuestao(tabelaResultado.getBoolean("apresentarNotaDaQuestao"));
		obj.setApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao(tabelaResultado.getBoolean("apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao"));
		obj.setLimiteTempoProvaAlunoDentroPeriodoRealizacao(tabelaResultado.getBoolean("limiteTempoProvaAlunoDentroPeriodoRealizacao"));
		obj.setRandomizarApenasQuestoesCadastradasPeloProfessor(tabelaResultado.getBoolean("randomizarApenasQuestoesCadastradasPeloProfessor"));
        obj.setNrDiasEntreAvalicaoOnline(tabelaResultado.getInt("nrDiasEntreAvalicaoOnline"));
        obj.setNrVezesPodeRepetirAvaliacaoOnline(tabelaResultado.getInt("nrVezesPodeRepetirAvaliacaoOnline"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getDouble("conteudo"))) {
			obj.getConteudoVO().setCodigo(tabelaResultado.getInt("conteudo"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getDouble("unidadeconteudo"))) {
			obj.getUnidadeConteudoVO().setCodigo(tabelaResultado.getInt("unidadeconteudo"));
		}
		obj.setUsoExclusivoProfessor(tabelaResultado.getBoolean("usoexclusivoprofessor"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("professor"))) {
			obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
		}
		obj.setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.valueOf(tabelaResultado.getString("politicaselecaoquestao")));
		obj.setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum.valueOf(tabelaResultado.getString("regradistribuicaoquestao")));
		obj.setPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno(tabelaResultado.getBoolean("permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			if (obj.getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
				obj.setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().consultarAvaliacaoOnlineQuestaoComQuestaoPorAvaliacaoOnline(obj.getCodigo(), usuarioLogado));
			}
		}
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			if (!obj.getDisciplinaVO().getCodigo().equals(0)) {
				obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			if (!obj.getTurmaVO().getCodigo().equals(0)) {
				obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			
			if (!obj.getConteudoVO().getCodigo().equals(0)) {
				obj.setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(obj.getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, usuarioLogado));
			}
			if (obj.getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
				obj.setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().consultarPorAvaliacaoOnline(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			}
			if (!obj.getReposnsavelCriacao().getCodigo().equals(0)) {
				obj.setReposnsavelCriacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getReposnsavelCriacao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			if (!obj.getReposnsavelAtivacao().getCodigo().equals(0)) {
				obj.setReposnsavelAtivacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getReposnsavelAtivacao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			if (!obj.getReposnsavelInativacao().getCodigo().equals(0)) {
				obj.setReposnsavelInativacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getReposnsavelInativacao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			if (obj.getUsoExclusivoProfessor()) {
				obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			obj.setParametrosMonitoramentoAvaliacaoOnlineVO(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultarPorChavePrimaria(obj.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			if (obj.getIsPermiteInformarTemaAssunto()) {
				obj.setAvaliacaoOnlineTemaAssuntoVOs(getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().consultarAvaliacaoOnlineTemaAssuntoPorAvaliacaoOnline(obj.getCodigo(), usuarioLogado));
			}
			return obj;
		}
		return obj;
	}

	@Override
	public List<AvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AvaliacaoOnlineVO> vetResultado = new ArrayList<AvaliacaoOnlineVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonline WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineVO();
	}
	
	private StringBuilder getSqlPadraoConsultar() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT avaliacaoonline.nome, avaliacaoonline.professor, avaliacaoonline.tipogeracaoprovaonline , ");
		sqlStr.append(" professor.nome as nomeprofessor, avaliacaoonline.codigo, avaliacaoonline.tipouso, avaliacaoonline.situacao,  avaliacaoonline.nrDiasEntreAvalicaoOnline, avaliacaoonline.nrVezesPodeRepetirAvaliacaoOnline , ");
		sqlStr.append(" disciplina.nome as nomedisciplina, disciplina.codigo as codigodisicplina,   ");
		sqlStr.append(" turma.identificadorturma as identificadorturma, turma.codigo as codigoturma   ");
		sqlStr.append(" FROM avaliacaoonline  ");
		sqlStr.append(" left join disciplina on disciplina.codigo = avaliacaoonline.disciplina ");
		sqlStr.append(" left join turma on turma.codigo = avaliacaoonline.turma ");
		sqlStr.append(" left join pessoa as professor on professor.codigo = avaliacaoonline.professor");
		return sqlStr;
	}
	
	@Override
	public List<AvaliacaoOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (valorConsulta.length() < 2) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		if (campoConsulta.equals("nome")) {
			return consultarPorNome(valorConsulta, nivelMontarDados, usuarioLogado);
		} else if (campoConsulta.equals("disciplina")) {
			return consultarPorNomeDisciplina(valorConsulta, nivelMontarDados, usuarioLogado);
		} else if (campoConsulta.equals("turma")) {
			return consultarPorNomeTurma(valorConsulta, nivelMontarDados, usuarioLogado);
		}
		return null;
	}

	@Override
	public List<AvaliacaoOnlineVO> consultarPorNomeDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsultar();
		sqlStr.append(" where upper(sem_acentos(disciplina.nome)) like upper(sem_acentos(?))  ");
		sqlStr.append(" and avaliacaoonline.tipoUso != '").append(TipoUsoEnum.REA).append("'");
		if (usuarioLogado.getIsApresentarVisaoProfessor()) {
			sqlStr.append(" and avaliacaoonline.professor = ").append(usuarioLogado.getPessoa().getCodigo());
		}
		sqlStr.append(" ORDER BY avaliacaoonline.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultar(tabelaResultado);
	}
	
	public List<AvaliacaoOnlineVO> consultarPorNomeTurma(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsultar();
		sqlStr.append(" where upper(sem_acentos(turma.identificadorturma)) like upper(sem_acentos(?))  ");
		sqlStr.append(" and avaliacaoonline.tipoUso != '").append(TipoUsoEnum.REA).append("'");
		if (usuarioLogado.getIsApresentarVisaoProfessor()) {
			sqlStr.append(" and avaliacaoonline.professor = ").append(usuarioLogado.getPessoa().getCodigo());
		}
		sqlStr.append(" ORDER BY avaliacaoonline.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultar(tabelaResultado);
	}	

	@Override
	public List<AvaliacaoOnlineVO> consultarPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsultar();
		sqlStr.append(" WHERE upper(sem_acentos(avaliacaoonline.nome)) like upper(sem_acentos(?)) ");
		sqlStr.append(" and avaliacaoonline.tipoUso != '").append(TipoUsoEnum.REA).append("'");
		if (usuarioLogado.getIsApresentarVisaoProfessor()) {
			sqlStr.append(" and avaliacaoonline.professor = ").append(usuarioLogado.getPessoa().getCodigo());
		}
		sqlStr.append(" ORDER BY avaliacaoonline.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultar(tabelaResultado);
	}
	
	private List<AvaliacaoOnlineVO> montarDadosConsultar(SqlRowSet tabelaResultado) {
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		List<AvaliacaoOnlineVO> list = new ArrayList<AvaliacaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setTipoGeracaoProvaOnline(TipoGeracaoProvaOnlineEnum.valueOf(tabelaResultado.getString("tipogeracaoprovaonline")));
			obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
			obj.setTipoUso(TipoUsoEnum.valueOf(tabelaResultado.getString("tipouso")));
			obj.setNrDiasEntreAvalicaoOnline(tabelaResultado.getInt("nrDiasEntreAvalicaoOnline"));
	        obj.setNrVezesPodeRepetirAvaliacaoOnline(tabelaResultado.getInt("nrVezesPodeRepetirAvaliacaoOnline"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("codigoturma"));
			obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigodisicplina"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("nomedisciplina"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("professor"))) {
				obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
				obj.getProfessor().setNome(tabelaResultado.getString("nomeprofessor"));
			}
			list.add(obj);
			obj = new AvaliacaoOnlineVO();
		}
		return list;
	}
	
	@Override
	public AvaliacaoOnlineVO consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(Integer matriculaPeriodoTurmaDisciplina, String codigoOrigemAvaliacaoOnline, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!Uteis.isAtributoPreenchido(codigoOrigemAvaliacaoOnline)){
			sqlStr.append("	(select 1 as ordem, avaliacaoonline.* from turmadisciplinaconteudo");
			sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = turmadisciplinaconteudo.avaliacaoonline ");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina as mptd on mptd.turma = turmadisciplinaconteudo.turma and mptd.disciplina = turmadisciplinaconteudo.disciplina");
			sqlStr.append(" and mptd.conteudo = turmadisciplinaconteudo.conteudo");
			sqlStr.append(" where mptd.codigo = ").append(matriculaPeriodoTurmaDisciplina);
			sqlStr.append(" and turmadisciplinaconteudo.turma = mptd.turma ");
			sqlStr.append(" and turmadisciplinaconteudo.disciplina = mptd.disciplina ");
			sqlStr.append(" and (turmadisciplinaconteudo.ano ||'/'||turmadisciplinaconteudo.semestre) <= (mptd.ano||'/'||mptd.semestre) limit 1)");
			sqlStr.append("	union");
			sqlStr.append("	(select 2 as ordem, avaliacaoonline.* from turma ");
			sqlStr.append("	inner join avaliacaoonline on avaliacaoonline.codigo = turma.avaliacaoonline");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina as mptd on mptd.turma = turma.codigo");
			sqlStr.append(" where mptd.codigo = ").append(matriculaPeriodoTurmaDisciplina);
			sqlStr.append("	and turma.codigo = mptd.turma");
			sqlStr.append(" )");
			sqlStr.append("	order by ordem limit 1");
		}else {
			sqlStr.append("	select avaliacaoonline.* ");
			sqlStr.append("	from avaliacaoonline ");
			sqlStr.append("	inner join conteudoUnidadePaginaRecursoEducacional as cupre on cupre.avaliacaoonline =avaliacaoonline.codigo ");
			sqlStr.append("	where cupre.codigo = '").append(codigoOrigemAvaliacaoOnline).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		return obj;
	}
	
	@Override
	public List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorTurmaAlunoDeveResponder(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("	SELECT avaliacaoonline.* FROM avaliacaoonline ");
		sqlStr.append("	INNER JOIN turma ON avaliacaoonline.tipouso = '").append(TipoUsoEnum.TURMA).append("' AND avaliacaoonline.turma = turma.codigo ");
		sqlStr.append("	INNER JOIN disciplina ON avaliacaoonline.disciplina = disciplina.codigo ");
		sqlStr.append("	INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(sqlStr);
		sqlStr.append(" WHERE avaliacaoonline.situacao = '").append(SituacaoEnum.ATIVO).append("' AND ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<AvaliacaoOnlineVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			lista.add(obj);
		}
		return lista;
	}

	@Override
	public void adicionarQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO) throws Exception {
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
			if (avaliacaoOnlineQuestaoVO2.getQuestaoVO().getCodigo().equals(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ListaExercicio_questaoJaAdicionada"));
			}
		}
		avaliacaoOnlineQuestaoVO.setOrdemApresentacao(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().size() + 1);
		avaliacaoOnlineQuestaoVO.getQuestaoVO().setOpcaoRespostaQuestaoVOs(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorQuestao(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo()));
		avaliacaoOnlineQuestaoVO.setMaximixado(false);
		avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().add(avaliacaoOnlineQuestaoVO);
		
		if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL)) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoFacil(avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil() + 1);
		} else if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO)) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoMedio(avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio() + 1);
		} else if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.DIFICIL)) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoDificil(avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil() + 1);
		}
	}

	@Override
	public void removerQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		int x = 0;
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
			if (avaliacaoOnlineQuestaoVO2.getQuestaoVO().getCodigo().equals(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo())) {
				if(!avaliacaoOnlineQuestaoVO.getCodigo().equals(0)) {
					getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().excluir(avaliacaoOnlineQuestaoVO, false, usuarioVO);
				}
				avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs().remove(x);
				break;
			}
			x++;
		}
		x = 1;
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
			avaliacaoOnlineQuestaoVO2.setOrdemApresentacao(x++);
		}
		if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.FACIL) && avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil() > 0) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoFacil(avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil() - 1);
		} else if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.MEDIO) && avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio() > 0) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoMedio(avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio() - 1);
		} else if (avaliacaoOnlineQuestaoVO.getQuestaoVO().getNivelComplexidadeQuestao().equals(NivelComplexidadeQuestaoEnum.DIFICIL) && avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil() > 0) {
			avaliacaoOnlineVO.setQuantidadeNivelQuestaoDificil(avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil() - 1);
		}
	}

	@Override
	public void alterarOrdemApresentacaoQuestaoAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2) {
		int ordem1 = avaliacaoOnlineQuestaoVO.getOrdemApresentacao();
		avaliacaoOnlineQuestaoVO.setOrdemApresentacao(avaliacaoOnlineQuestaoVO2.getOrdemApresentacao());
		avaliacaoOnlineQuestaoVO2.setOrdemApresentacao(ordem1);
		Ordenacao.ordenarLista(avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs(), "ordemApresentacao");
	}

	@Override
	public List<AvaliacaoOnlineVO> consultarAvaliacoesOnlinesPorCodigoDisciplinaETipoUsoGeral(Integer codigoDisciplina, UsuarioVO usuarioLogado) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select avaliacaoonline.codigo, avaliacaoonline.nome");
		sqlStr.append(" from avaliacaoonline");
		sqlStr.append(" inner join disciplina on disciplina.codigo = avaliacaoonline.disciplina");
		sqlStr.append(" where disciplina.codigo = ").append(codigoDisciplina);
		sqlStr.append(" where avaliacaoonline.tipouso = 'DISCIPLINA'");
		sqlStr.append(" and avaliacaoonline.situacao = 'ATIVO'");
		sqlStr.append(" union all");
		sqlStr.append(" select avaliacaoonline.codigo, avaliacaoonline.nome");
		sqlStr.append(" from avaliacaoonline");
		sqlStr.append(" where avaliacaoonline.tipouso = 'GERAL'");
		sqlStr.append(" and avaliacaoonline.situacao = 'ATIVO'");
		sqlStr.append(" and avaliacaoonline.professor is null");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		List<AvaliacaoOnlineVO> list = new ArrayList<AvaliacaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));

			list.add(obj);
			obj = new AvaliacaoOnlineVO();
		}
		return list;
	}

	@Override
	public AvaliacaoOnlineVO consultarAvaliacaoOnlineDaTurmaDisciplinaAnoSemestreOuDefaultTurma(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineVO obj = consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), calendarioAtividadeMatriculaVO.getCodOrigem(), usuarioLogado);
		if (Uteis.isAtributoPreenchido(obj)) {			
			if (obj.getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
				obj.setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(obj, obj.getQuantidadeNivelQuestaoFacil(), obj.getQuantidadeNivelQuestaoMedio(), obj.getQuantidadeNivelQuestaoDificil(), obj.getQuantidadeQualquerNivelQuestao(), avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getAvaliacaoOnlineTemaAssuntoVOs(), usuarioLogado));
			}
		} else {
			throw new Exception("Nenhuma Avaliação Encontrada");
		}
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoAvaliacaoOnlinePorConteudo(SituacaoEnum situacao, ConteudoVO conteudoVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append("UPDATE avaliacaoonline set situacao ='").append(situacao.name()).append("', ");
		if(situacao.isAtivo()){
			sb.append("dataativacao ='").append(Uteis.getDataJDBC(new Date())).append("', ");
			sb.append("responsavelativacao = ").append(usuarioLogado.getCodigo()).append(" ");	
		}else{
			sb.append("dataativacao = null, responsavelativacao = null ");
		}
		sb.append(" where codigo in ( ");
		sb.append(" SELECT distinct ConteudoUnidadePaginaRecursoEducacional.avaliacaoonline from ConteudoUnidadePaginaRecursoEducacional ");
		sb.append(" inner join ConteudoUnidadePagina on ConteudoUnidadePagina.codigo = ConteudoUnidadePaginaRecursoEducacional.conteudoUnidadePagina ");
		sb.append(" inner join UnidadeConteudo on UnidadeConteudo.codigo = ConteudoUnidadePagina.unidadeConteudo ");
		sb.append(" where UnidadeConteudo.conteudo =  ").append(conteudoVO.getCodigo());
		sb.append(" and ConteudoUnidadePaginaRecursoEducacional.tiporecursoeducacional = '").append(TipoRecursoEducacionalEnum.AVALIACAO_ONLINE.name()).append("'");
		sb.append(" ) ");
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAvaliacaoOnlineJaAtivada(AvaliacaoOnlineVO obj, boolean verificarAcesso, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception {
		AvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
		validarDados(obj);
		alterar(obj, "avaliacaoonline", new AtributoPersistencia().add("randomizarapenasquestoescadastradaspeloprofessor", obj.getRandomizarApenasQuestoesCadastradasPeloProfessor())
				.add("variavelNotaCfgPadraoAvaliacaoOnlineRea", obj.getVariavelNotaCfgPadraoAvaliacaoOnlineRea())
				.add("apresentarNotaDaQuestao", obj.isApresentarNotaDaQuestao())
				.add("dataInicioAvaliacaoFixo", Uteis.getDataJDBCTimestamp(obj.getDataInicioAvaliacaoFixo()))
				.add("dataTerminoAvaliacaoFixo", Uteis.getDataJDBCTimestamp(obj.getDataTerminoAvaliacaoFixo()))
				.add("tempoLimiteRealizacaoAvaliacaoOnline", obj.getTempoLimiteRealizacaoAvaliacaoOnline())
				.add("notaMaximaAvaliacao", obj.getNotaMaximaAvaliacao())
				.add("notaPorQuestaoNivelDificil", obj.getNotaPorQuestaoNivelDificil())
				.add("notaPorQuestaoNivelFacil", obj.getNotaPorQuestaoNivelFacil())
				.add("notaPorQuestaoNivelMedio", obj.getNotaPorQuestaoNivelMedio())
				.add("quantidadeQualquerNivelQuestao", obj.getQuantidadeQualquerNivelQuestao())
				.add("quantidadeNivelQuestaoDificil", obj.getQuantidadeNivelQuestaoDificil())
				.add("quantidadeNivelQuestaoFacil", obj.getQuantidadeNivelQuestaoFacil())
				.add("quantidadeNivelQuestaoMedio", obj.getQuantidadeNivelQuestaoMedio())
				.add("permiterepeticoesdequestoesapartirsegundaavaliacaoonlinealuno", obj.getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno())
				.add("limitetempoprovaalunodentroperiodorealizacao", obj.isLimiteTempoProvaAlunoDentroPeriodoRealizacao()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		if (progressBarVO != null && (UteisData.getCompareDataComHora(obj.getDataInicioAvaliacaoFixo(), obj.getDataInicioAvaliacaoFixoAntesAlteracao()) != 0 || UteisData.getCompareDataComHora(obj.getDataTerminoAvaliacaoFixo(), obj.getDataTerminoAvaliacaoFixoAntesAlteracao()) != 0)) {
			realizarGeracaoCalendarioAtividadeMatriculaAvalicaoOnlineTurmaEmLote(obj, progressBarVO, usuarioVO);
			obj.setDataInicioAvaliacaoFixoAntesAlteracao(obj.getDataInicioAvaliacaoFixo());
			obj.setDataTerminoAvaliacaoFixoAntesAlteracao(obj.getDataTerminoAvaliacaoFixo());
		}		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoAvaliacaoOnline(final AvaliacaoOnlineVO avaliacaoOnlineVO, boolean verificarAcesso, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			AtributoPersistencia campos = new AtributoPersistencia();
			campos.add("situacao", avaliacaoOnlineVO.getSituacao().name());
			if(avaliacaoOnlineVO.getSituacao().isAtivo()) {
				campos.add("dataativacao", Uteis.getDataJDBC(avaliacaoOnlineVO.getDataAtivacao()));
				campos.add("responsavelativacao", avaliacaoOnlineVO.getReposnsavelAtivacao());	
			}else if(avaliacaoOnlineVO.getSituacao().isInativo()) {
				campos.add("datainativacao",  Uteis.getDataJDBCTimestamp(avaliacaoOnlineVO.getDataInativacao()));
				campos.add("responsavelinativacao", avaliacaoOnlineVO.getReposnsavelInativacao());	
			}
			alterar(avaliacaoOnlineVO, "avaliacaoonline", campos, new AtributoPersistencia().add("codigo", avaliacaoOnlineVO.getCodigo()), usuarioVO);
			if(progressBarVO != null && avaliacaoOnlineVO.getSituacao().isAtivo()) {
				realizarGeracaoCalendarioAtividadeMatriculaAvalicaoOnlineTurmaEmLote(avaliacaoOnlineVO, progressBarVO, usuarioVO);
			} 
			if (avaliacaoOnlineVO.getSituacao().isInativo() && avaliacaoOnlineVO.getTipoUso().isTurma() && avaliacaoOnlineVO.isExcluirCalendarioAtividadeAguardandoRealizacao()) {
				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().exluirPorAvaliacaoOnlineTipoUsoTurma(avaliacaoOnlineVO.getCodigo().toString(), false, usuarioVO);	
			}
		} catch (Exception e) {
			throw e;
		}
	}	
	
	private void realizarGeracaoCalendarioAtividadeMatriculaAvalicaoOnlineTurmaEmLote(AvaliacaoOnlineVO avaliacaoOnlineVO, ProgressBarVO progressBarVO, UsuarioVO usuario) {
		if(!Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getListaHistoricoAluno())) {
			progressBarVO.incrementar();
			return;
		}
		for (HistoricoVO historico : avaliacaoOnlineVO.getListaHistoricoAluno()) {
			try {
				progressBarVO.setStatus(avaliacaoOnlineVO.getPreencherStatusProgressBarVO(progressBarVO, historico));
				historico.getMatriculaPeriodoTurmaDisciplina().setMatricula(historico.getMatricula().getMatricula());
				historico.getMatriculaPeriodoTurmaDisciplina().setMatriculaPeriodo(historico.getMatriculaPeriodo().getCodigo());
				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlinePorTurma(historico.getMatriculaPeriodoTurmaDisciplina(), avaliacaoOnlineVO, true, usuario);
			} catch (Exception e) {
				 e.printStackTrace();
			} finally {
				progressBarVO.incrementar();
			}
		}
	}
	
	/**
	 * Método para montar dados combo box retornando as avaliações on-line
	 * vinculadas a um conteúdo ou que não possua vinculo algum e que não seja
	 * cadastradas por um professor ou que tenham vinculo com professor.
	 */
	@Override
	public List<AvaliacaoOnlineVO> consultarAvaliacoesOnlinesPorDisciplinaEConteudoEAvaliacoesOnlinesRandomicas(Integer codigoDisciplina, Integer codigoConteudo, Boolean consultarPorProfessor, Integer codigoProfessor) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select avaliacaoonline.codigo, avaliacaoonline.nome from avaliacaoonline");
		sqlStr.append(" where ((avaliacaoonline.tipoUso = '").append(TipoUsoEnum.DISCIPLINA).append("' and avaliacaoonline.disciplina = ").append(codigoDisciplina).append(" and (avaliacaoonline.conteudo = ").append(codigoConteudo + " or avaliacaoonline.conteudo is null) )");
		sqlStr.append(" or (avaliacaoonline.tipoUso = '").append(TipoUsoEnum.GERAL).append("'))");
		if (consultarPorProfessor) {
			sqlStr.append(" and avaliacaoonline.professor = ").append(codigoProfessor);
		} else {
			sqlStr.append(" and avaliacaoonline.professor is null");
		}
		sqlStr.append(" and avaliacaoonline.tipoUso != '").append(TipoUsoEnum.REA).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		AvaliacaoOnlineVO avaliacaoOnlineVO = null;
		List<AvaliacaoOnlineVO> avaliacaoOnlineVOs = new ArrayList<AvaliacaoOnlineVO>();

		while (rs.next()) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
			avaliacaoOnlineVO.setCodigo(rs.getInt("codigo"));
			avaliacaoOnlineVO.setNome(rs.getString("nome"));

			avaliacaoOnlineVOs.add(avaliacaoOnlineVO);
		}

		return avaliacaoOnlineVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AvaliacaoOnlineVO clonar(AvaliacaoOnlineVO avaliacaoOnlineVO) throws Exception {
		AvaliacaoOnlineVO avaliacaoOnlineVO2 = avaliacaoOnlineVO.clone();
		avaliacaoOnlineVO2.setDataInativacao(null);
		avaliacaoOnlineVO2.setDataAtivacao(null);
		avaliacaoOnlineVO2.setDataCriacao(new Date());
		avaliacaoOnlineVO2.setCodigo(0);
		avaliacaoOnlineVO2.setSituacao(SituacaoEnum.EM_CONSTRUCAO);
		avaliacaoOnlineVO2.setNome(avaliacaoOnlineVO.getNome() + " - Clone");
		avaliacaoOnlineVO2.getAvaliacaoOnlineQuestaoVOs().clear();
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : avaliacaoOnlineVO.getAvaliacaoOnlineQuestaoVOs()) {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 = avaliacaoOnlineQuestaoVO.clone();
			avaliacaoOnlineQuestaoVO2.setAvaliacaoOnlineVO(avaliacaoOnlineVO2);
			avaliacaoOnlineVO2.getAvaliacaoOnlineQuestaoVOs().add(avaliacaoOnlineQuestaoVO2);
		}
		return avaliacaoOnlineVO2;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Boolean verificarAvaliacaoOnlineMatriculaExistente(Integer codigoAvaliacaoOnline) throws Exception {
		String sqlStr = "SELECT codigo FROM avaliacaoonlinematricula WHERE avaliacaoonline = " + codigoAvaliacaoOnline+" limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void validarQuantidadeQuestoesPorNivel(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		ConsistirException consistirException = new ConsistirException();
		AvaliacaoOnlineVO avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(avaliacaoOnlineVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if (avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)) {
			compararQuantidadeQuestoesTotal(avaliacaoOnlineVO, avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes, consistirException);
		} else if (avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getRegraDistribuicaoQuestaoEnum().equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_DISTRUIBUIDA_ENTRE_ASSUNTOS)) {
			compararQuantidadeQuestoesPorNivel(avaliacaoOnlineVO, avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes, consistirException);
		} else if (avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getRegraDistribuicaoQuestaoEnum().equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO)) {
			List<Integer> temaAssuntos = getFacadeFactory().getTemaAssuntoFacade().consultarTemasAssuntosPorConteudo(avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getUnidadeConteudoVO().getCodigo(), !matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo().equals(0) ? matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo() : avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getConteudoVO().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getPoliticaSelecaoQuestaoEnum(), usuarioVO);
			Long quantidadeTemaAssuntos = temaAssuntos.stream().distinct().count();
			avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.setQuantidadeNivelQuestaoDificil(avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoDificil() * quantidadeTemaAssuntos.intValue());
			avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.setQuantidadeNivelQuestaoMedio(avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoMedio() * quantidadeTemaAssuntos.intValue());
			avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.setQuantidadeNivelQuestaoFacil(avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoFacil() * quantidadeTemaAssuntos.intValue());
			avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.setQuantidadeQualquerNivelQuestao(avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeQualquerNivelQuestao() * quantidadeTemaAssuntos.intValue());
			compararQuantidadeQuestoesPorNivel(avaliacaoOnlineVO, avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes, consistirException);
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}
	
	private void compararQuantidadeQuestoesPorNivel(AvaliacaoOnlineVO avaliacaoOnlineVOComQuestoesGeradas, AvaliacaoOnlineVO avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes, ConsistirException consistirException) throws Exception {
		if (avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeQualquerNivelQuestao() < avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeQualquerNivelQuestao()) {
			consistirException.getListaMensagemErro().add("Não existem Questões suficientes para geração da Avaliação Online Randomicamente. Tem-se apenas " + +avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeQualquerNivelQuestao() + ". Deveria haver " + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeQualquerNivelQuestao() + ".");
		}
		if (avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoDificil() < avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoDificil()) {
			consistirException.getListaMensagemErro().add("Não existem Questões Difíceis suficientes para geração da Avaliação Online Randomicamente. Tem-se apenas " + avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoDificil() + ". Deveria haver " + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoDificil() + ".");
		}
		if (avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoMedio() < avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoMedio()) {
			consistirException.getListaMensagemErro().add("Não existem Questões Medianas suficientes para geração da Avaliação Online Randomicamente. Tem-se apenas " + avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoMedio() + ". Deveria haver " + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoMedio() + ".");
		}
		if (avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoFacil() < avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoFacil()) {
			consistirException.getListaMensagemErro().add("Não existem Questões Fáceis suficientes para geração da Avaliação Online Randomicamente. Tem-se apenas " + avaliacaoOnlineVOComQuestoesGeradas.getQuantidadeNivelQuestaoFacil() + ". Deveria haver " + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoFacil() + ".");
		}
	}
	
	private void compararQuantidadeQuestoesTotal(AvaliacaoOnlineVO avaliacaoOnlineVOComQuestoesGeradas, AvaliacaoOnlineVO avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes, ConsistirException consistirException) throws Exception {
		Integer quantidadeTotalQuestoes = avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoDificil() + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoFacil() + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeNivelQuestaoMedio() + avaliacaoOnlineVOComValoresOriginaisParaQuantidadeQuestoes.getQuantidadeQualquerNivelQuestao();
		if (avaliacaoOnlineVOComQuestoesGeradas.getAvaliacaoOnlineQuestaoVOs().size() < quantidadeTotalQuestoes) {
			consistirException.getListaMensagemErro().add("Não existem Questões suficientes para geração da Avaliação Online Randomicamente.");
		}
	}
	
	@Override
	public List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoEAvaliacaoOnline(Integer avaliacaoOnline, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct variavel, titulo from configuracaoacademiconota where tipousonota = 'AVALIACAO_ONLINE' ");
//		if (!avaliacaoOnline.equals(0)) {
//			sb.append(" union ");
//			sb.append(" select variavelnotacfgpadraoavaliacaoonlinerea as variavel, variavelnotacfgpadraoavaliacaoonlinerea as titulo from avaliacaoonline ");
//			sb.append(" where variavelnotacfgpadraoavaliacaoonlinerea is not null and variavelnotacfgpadraoavaliacaoonlinerea <> '' ");
//			sb.append(" and avaliacaoonline.codigo = ").append(avaliacaoOnline);
//		}
		sb.append(" order by titulo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			lista.add(new SelectItem(tabelaResultado.getString("variavel"), tabelaResultado.getString("titulo")));
		}
		return lista;
	}
	
	@Override
	public List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT avaliacaoonline.codigo, avaliacaoonline.nome, conteudo.descricao as conteudo, disciplina.codigo as codigoDisciplina, disciplina.nome as disciplina");
		sqlStr.append(" FROM avaliacaoonline ");
		sqlStr.append(" left join conteudo on conteudo.codigo = avaliacaoonline.conteudo");
		sqlStr.append(" left join disciplina on disciplina.codigo = avaliacaoonline.disciplina");
		sqlStr.append(" WHERE upper(sem_acentos(avaliacaoonline.nome)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ");

		sqlStr.append(" ORDER BY avaliacaoonline.codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		List<AvaliacaoOnlineVO> list = new ArrayList<AvaliacaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("conteudo"))) {
				obj.getConteudoVO().setDescricao(tabelaResultado.getString("conteudo"));
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("disciplina"))) {
				obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigoDisciplina"));
				obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina"));
			}

			list.add(obj);
			obj = new AvaliacaoOnlineVO();
		}
		return list;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineVO consultarPorCodigoAvaliacaoCodigoDisciplina(Integer codigo, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonline WHERE codigo = ?";
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sql += " and disciplina = ?";
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo, disciplina);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineVO();
	}
	
	@Override
	public List<AvaliacaoOnlineVO> consultarAvaliacaoOnlinePorNomeAvaliacaoDisciplina(String valorConsulta, Integer disciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT avaliacaoonline.codigo, avaliacaoonline.nome, conteudo.descricao as conteudo, disciplina.codigo as codigoDisciplina, disciplina.nome as disciplina");
		sqlStr.append(" FROM avaliacaoonline ");
		sqlStr.append(" left join conteudo on conteudo.codigo = avaliacaoonline.conteudo");
		sqlStr.append(" left join disciplina on disciplina.codigo = avaliacaoonline.disciplina");
		sqlStr.append(" WHERE upper(sem_acentos(avaliacaoonline.nome)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and avaliacaoonline.disciplina = ").append(disciplina);
		}
		
		sqlStr.append(" ORDER BY avaliacaoonline.codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		List<AvaliacaoOnlineVO> list = new ArrayList<AvaliacaoOnlineVO>(0);
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("conteudo"))) {
				obj.getConteudoVO().setDescricao(tabelaResultado.getString("conteudo"));
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("disciplina"))) {
				obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigoDisciplina"));
				obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina"));
			}

			list.add(obj);
			obj = new AvaliacaoOnlineVO();
		}
		return list;
	}
	
	@Override
	public List<AvaliacaoOnlineVO> consultarPorQuestaoFixa(Integer questao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct avaliacaoOnline.* from avaliacaoOnline ");
		sb.append(" inner join avaliacaoonlinequestao on avaliacaoonlinequestao.avaliacaoOnline = avaliacaoOnline.codigo ");
		sb.append(" where avaliacaoonlinequestao.questao = ? ");
		sb.append(" and avaliacaoOnline.tipoGeracaoProvaOnline = '").append(TipoGeracaoProvaOnlineEnum.FIXO).append("' ");
		sb.append(" order by avaliacaoOnline.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), questao);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	@Override
	public void inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		
			if(avaliacaoOnlineVO.getUsoExclusivoProfessor()) {
				matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(avaliacaoOnlineVO.getProfessor().getCodigo());
				matriculaPeriodoTurmaDisciplinaVO.getProfessor().setNome(avaliacaoOnlineVO.getProfessor().getNome());
			}else if(usuarioVO.getIsApresentarVisaoProfessor()) {
				matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(usuarioVO.getPessoa().getCodigo());
				matriculaPeriodoTurmaDisciplinaVO.getProfessor().setNome(usuarioVO.getPessoa().getNome());
}
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getTurmaVO())) {
				matriculaPeriodoTurmaDisciplinaVO.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(avaliacaoOnlineVO.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
				matriculaPeriodoTurmaDisciplinaVO.setAno(avaliacaoOnlineVO.getAno());
				matriculaPeriodoTurmaDisciplinaVO.setSemestre(avaliacaoOnlineVO.getSemestre());				
			}
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getDisciplinaVO())) {
				matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(avaliacaoOnlineVO.getDisciplinaVO().getCodigo());
				matriculaPeriodoTurmaDisciplinaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(avaliacaoOnlineVO.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
				
			}
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getConteudoVO())) {
				matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(avaliacaoOnlineVO.getConteudoVO().getCodigo());
			}
			
		
	}

	@Override
	public AvaliacaoOnlineMatriculaVO realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, Boolean simulandoAvaliacao) throws Exception {
		validarDados(avaliacaoOnlineVO);
		if (avaliacaoOnlineVO.getTipoUso().isTurma() && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurma())) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getDisciplina())) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		if (!matriculaPeriodoTurmaDisciplinaVO.getTurma().getIntegral() && matriculaPeriodoTurmaDisciplinaVO.getAno().isEmpty()) {
			throw new Exception("O campo ANO deve ser informado.");
		}
		if (!matriculaPeriodoTurmaDisciplinaVO.getTurma().getIntegral() && matriculaPeriodoTurmaDisciplinaVO.getAno().length() != 4) {
			throw new Exception("O campo ANO deve ser informado com 4 dígitos.");
		}
		if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getSemestral() && matriculaPeriodoTurmaDisciplinaVO.getSemestre().isEmpty()) {
			throw new Exception("O campo SEMESTRE deve ser informado.");
		}
		if (avaliacaoOnlineVO.getTipoGeracaoProvaOnline().isRandomicoPorComplexidade() && !avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO) && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo())) {
			throw new Exception("O campo CONTEÚDO deve ser informado.");
		}
		
		AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		avaliacaoOnlineMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
		avaliacaoOnlineMatriculaVO.setAvaliacaoOnlineVO(avaliacaoOnlineVO);
		realizarDefinicaoConfiguracaoEADUtilizarSimulacaoAvaliacao(matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineMatriculaVO, usuarioVO);
//		realizarDefinicaoConteudoUtilizarSimulacaoAvaliacao(avaliacaoOnlineVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);		
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = realizarCriacaoCalendarioUtilizarSimulacaoAvaliacao(avaliacaoOnlineVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		calendarioAtividadeMatriculaVO.setDataFim(Uteis.getDataAdicionadaEmHoras(new Date(), avaliacaoOnlineVO.getTempoLimiteRealizacaoAvaliacaoOnline()));
		Integer qtdeQuestaoDificil = (getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, null, NivelComplexidadeQuestaoEnum.DIFICIL, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum(), avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineTemaAssuntoVOs, usuarioVO,true));;
		Integer qtdeQuestaoMedio = (getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, null, NivelComplexidadeQuestaoEnum.MEDIO, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum(), avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineTemaAssuntoVOs, usuarioVO,true));
		Integer qtdeQuestaoFacil = (getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, null, NivelComplexidadeQuestaoEnum.FACIL, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), avaliacaoOnlineVO.getPoliticaSelecaoQuestaoEnum(), avaliacaoOnlineVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineTemaAssuntoVOs, usuarioVO,true));
		
		avaliacaoOnlineMatriculaVO.setQtdeQuestaoDificil(qtdeQuestaoDificil);
		avaliacaoOnlineMatriculaVO.setQtdeQuestaoMedio(qtdeQuestaoMedio);
		avaliacaoOnlineMatriculaVO.setQtdeQuestaoFacil(qtdeQuestaoFacil);
		if(avaliacaoOnlineVO.getTipoGeracaoProvaOnline().isRandomicoPorComplexidade() && (avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil() > qtdeQuestaoDificil 
				|| avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio() > qtdeQuestaoMedio
				|| avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil() > qtdeQuestaoFacil
				|| (avaliacaoOnlineVO.getQuantidadeQualquerNivelQuestao()+avaliacaoOnlineVO.getQuantidadeNivelQuestaoDificil()+avaliacaoOnlineVO.getQuantidadeNivelQuestaoMedio()+avaliacaoOnlineVO.getQuantidadeNivelQuestaoFacil()) > (qtdeQuestaoFacil+qtdeQuestaoMedio+qtdeQuestaoDificil))) {
			throw new Exception("Número de questões insuficientes para realização da prova, foram encontradas "+qtdeQuestaoFacil+" FÁCEIS, "+qtdeQuestaoMedio+" MÉDIAS e "+qtdeQuestaoDificil+" DIFÍCEIS. Revise os filtros da simulação ou cadastre novas questões que atenderão esta avaliação.");
		}

		getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarGeracaoAvaliacaoOnline(avaliacaoOnlineMatriculaVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO, true, simulandoAvaliacao);
		
		return avaliacaoOnlineMatriculaVO;
	}

	private CalendarioAtividadeMatriculaVO realizarCriacaoCalendarioUtilizarSimulacaoAvaliacao(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		calendarioAtividadeMatriculaVO.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE);
		calendarioAtividadeMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
		calendarioAtividadeMatriculaVO.setDataInicio(new Date());
		calendarioAtividadeMatriculaVO.setDataFim(new Date());		
		if (avaliacaoOnlineVO.getTipoUso().equals(TipoUsoEnum.TURMA)) {
			calendarioAtividadeMatriculaVO.setTipoOrigem(TipoOrigemEnum.AVALIACAO_ONLINE_TURMA);
			calendarioAtividadeMatriculaVO.setCodOrigem(avaliacaoOnlineVO.getCodigo().toString());
		} else if (avaliacaoOnlineVO.getTipoUso().equals(TipoUsoEnum.REA)) {
			calendarioAtividadeMatriculaVO.setTipoOrigem(TipoOrigemEnum.REA);			
			calendarioAtividadeMatriculaVO.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA);
		} else {
			calendarioAtividadeMatriculaVO.setTipoOrigem(TipoOrigemEnum.NENHUM);
			calendarioAtividadeMatriculaVO.setCodOrigem("");
		}
		return calendarioAtividadeMatriculaVO;
	}
	private void realizarDefinicaoConfiguracaoEADUtilizarSimulacaoAvaliacao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurma().getConfiguracaoEADVO())) {
			avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getTurma().getConfiguracaoEADVO().getCodigo()));
		} else {
			if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getSubturma() && !matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaAgrupada()) {				
				avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaPrincipal()));

			} else if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaAgrupada()) {
				List<TurmaAgrupadaVO> turmaAgrupadaVOs = getFacadeFactory().getTurmaAgrupadaFacade().consultarPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getSubturma() ? matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaPrincipal() : matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				for (TurmaAgrupadaVO turmaAg : turmaAgrupadaVOs) {					
					if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo())) {
						avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(turmaAg.getTurma().getCodigo()));
						break;
					}
				}
			} else {				
				avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo()));
			}
		}
	}
	
	private void realizarDefinicaoConteudoUtilizarSimulacaoAvaliacao(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(avaliacaoOnlineVO.getConteudoVO())) {
			matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(avaliacaoOnlineVO.getConteudoVO().getCodigo());
		} else if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo())) {
			if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getSubturma() && !matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaAgrupada()) {
				matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaPrincipal(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre()));				
			} else if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaAgrupada()) {
				List<TurmaAgrupadaVO> turmaAgrupadaVOs = getFacadeFactory().getTurmaAgrupadaFacade().consultarPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getSubturma() ? matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaPrincipal() : matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				for (TurmaAgrupadaVO turmaAg : turmaAgrupadaVOs) {
					matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(turmaAg.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre()));
					if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo())) {
						break;
					}
				}
			} else {
				matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre()));
			}
		}
	}
	
	
	@Override
	public StringBuilder consultarAvaliacaoOnlineEstaVinculada(Integer avaliacaoOnline) throws Exception {
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append("	select 1 as ordem, 'Turma: '||turma.identificadorTurma||', Disciplina: '||disciplina.nome||case when turma.anual then ' e Ano: '||turmadisciplinaconteudo.ano||'.' else case when turma.semestral then ' e Ano/Semestre: '||turmadisciplinaconteudo.ano||'/'||turmadisciplinaconteudo.semestre||'.' else '.' end end as origem from turmadisciplinaconteudo");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = turmadisciplinaconteudo.avaliacaoonline ");
		sqlStr.append(" inner join turma on turma.codigo = turmadisciplinaconteudo.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = turmadisciplinaconteudo.disciplina ");
		sqlStr.append(" where avaliacaoonline.codigo = ").append(avaliacaoOnline);
		sqlStr.append("	union ");
		sqlStr.append("	select 2 as ordem, 'Turma: '||turma.identificadorTurma||'.' as origem from turma ");
		sqlStr.append("	inner join avaliacaoonline on avaliacaoonline.codigo = turma.avaliacaoonline");			
		sqlStr.append(" where avaliacaoonline.codigo = ").append(avaliacaoOnline);
		sqlStr.append("	union ");
		sqlStr.append("	select 3 as ordem, 'Conteúdo: '||conteudo.descricao||', Disciplina: '||disciplina.nome||', Unidade: '||unidadeconteudo.ordem||', Página: '||conteudounidadepagina.pagina||' e REA: '||cupre.descricao||'.' as origem  ");
		sqlStr.append("	from avaliacaoonline ");
		sqlStr.append("	inner join conteudoUnidadePaginaRecursoEducacional as cupre on cupre.avaliacaoonline =avaliacaoonline.codigo ");
		sqlStr.append("	inner join conteudounidadepagina on conteudounidadepagina.codigo =cupre.conteudounidadepagina ");
		sqlStr.append("	inner join unidadeconteudo on conteudounidadepagina.unidadeconteudo =unidadeconteudo.codigo ");
		sqlStr.append("	inner join conteudo on conteudo.codigo =unidadeconteudo.conteudo ");
		sqlStr.append("	inner join disciplina on disciplina.codigo =conteudo.disciplina ");
		sqlStr.append("	where avaliacaoonline.codigo = '").append(avaliacaoOnline).append("' ");
		sqlStr.append("	order by ordem, origem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		StringBuilder origensConfTurmaConteudo = new StringBuilder("");
		StringBuilder origensTurma = new StringBuilder("");
		StringBuilder origensConteudo = new StringBuilder("");
		while (tabelaResultado.next()) {
			if(tabelaResultado.getInt("ordem") == 1) {
				if(origensConfTurmaConteudo.length() == 0) {
					origensConfTurmaConteudo.append("<div>").append("<span class=\"tituloCampos\">Esta Avaliação está Referenciada no Cadastro Configuração Turma Conteúdo da(s) Turma(s):</span>");
					origensConfTurmaConteudo.append("<ul>");	
				}
				origensConfTurmaConteudo.append("<li style=\"margin-left: 34px;line-height: 16px;\" >").append(tabelaResultado.getString("origem")).append("</li>");
			}else if(tabelaResultado.getInt("ordem") == 2) {
				if(origensTurma.length() == 0) {
					origensTurma.append("<div>").append("<span class=\"tituloCampos\">Esta Avaliação está Referenciada no Cadastro da(s) Turma(s):</span>");
					origensTurma.append("<ul>");	
				}
				origensTurma.append("<li style=\"margin-left: 34px;line-height: 16px;\" >").append(tabelaResultado.getString("origem")).append("</li>");
			}else {
				if(origensConteudo.length() == 0) {
					origensConteudo.append("<div>").append("<span class=\"tituloCampos\">Esta Avaliação está Referenciada no Cadastro do(s) Conteúdo(s):</span>");
					origensConteudo.append("<ul>");	
				}
				origensConteudo.append("<li  style=\"margin-left: 34px;line-height: 16px;\" >").append(tabelaResultado.getString("origem")).append("</li>");
			}				
		}
		StringBuilder origens = new StringBuilder("");
		if(origensConfTurmaConteudo.length() > 0) {
			origensConfTurmaConteudo.append("</ul></div>");	
			origens.append(origensConfTurmaConteudo);
		}
		if(origensTurma.length() > 0) {
			origensTurma.append("</ul></div>");
			origens.append(origensTurma);
		}
		if(origensConteudo.length() > 0) {
			origensConteudo.append("</ul></div>");
			origens.append(origensConteudo);
		}
		origens.append("<div>").append("<span style=\"margin-top:5px\" class=\"tituloCampos\">Confirma a Inativação Desta Avaliação On-line?</span></div>");
		return origens;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineVO consultarAvaliacaoOnlinePorConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();	
		sqlStr.append("SELECT avaliacaoonline.* FROM avaliacaoonline");
		sqlStr.append(" inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.avaliacaoonline = avaliacaoonline.codigo");
		sqlStr.append(" WHERE conteudounidadepaginarecursoeducacional.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString() , codigoConteudoUnidadePaginaRecursoEducacional);
		AvaliacaoOnlineVO obj = new AvaliacaoOnlineVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, nivelMontarDados, usuarioLogado);
		}
		return obj;
	}
}
