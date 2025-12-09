package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.administrativo.enumeradores.TipoGerarAgendaCampanhaEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecorrenciaCampanhaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.PoliticaRedistribuicaoProspectAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoMetaEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Campanha extends ControleAcesso implements CampanhaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6204545056886575409L;
	private static String idEntidade;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	public Campanha() throws Exception {
		super();
		setIdEntidade("Campanha");
	}

	public CampanhaVO novo() throws Exception {
		Campanha.incluir(getIdEntidade());
		CampanhaVO obj = new CampanhaVO();
		return obj;
	}

	public static void validarDados(CampanhaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDescricao() == null || obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo descrição deve ser informado.");
		}
		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo unidade ensino deve ser informado.");
		}
		if (obj.getTipoCampanha() == null || obj.getTipoCampanha().name().isEmpty()) {
			throw new ConsistirException("O campo tipo campanha deve ser informado.");
		}
		if (obj.getPeriodoInicio() == null) {
			throw new ConsistirException("O campo período inicio deve ser informado.");
		}
		if (obj.getPeriodoFim() == null) {
			throw new ConsistirException("O campo período fim deve ser informado.");
		}
		if (obj.getPeriodoInicio() == obj.getPeriodoFim()) {
			throw new ConsistirException("O campo período inicio deve ser diferente do período fim.");
		}
		if (obj.getWorkflow().getCodigo() == null || obj.getWorkflow().getCodigo() == 0) {
			throw new ConsistirException("O campo workflow (dados básicos) deve ser informado para geração da agenda.");
		}
		if (obj.getMeta().getCodigo() == null || obj.getMeta().getCodigo() == 0) {
			throw new ConsistirException("O campo meta (dados básicos) deve ser informado para geração da agenda.");
		}
		if (obj.getHoraInicial().equals("")) {
			throw new ConsistirException("O campo HORA INÍCIO GERAR AGENDA deve ser informado.");
		}
		if (obj.getHoraFinal().equals("")) {
			throw new ConsistirException("O campo HORA FINAL GERAR AGENDA deve ser informado.");
		}
		if ((!obj.getHoraInicioIntervalo().equals("")) && (obj.getHoraFimIntervalo().equals(""))) {
			throw new ConsistirException("O campo HORA FINAL INTERVALO deve ser informado (Hora inícial do intervalo foi informada).");
		}
		if (obj.getTipoCampanha().equals(TipoCampanhaEnum.PRE_INSCRICAO)) {
			if (Uteis.isAtributoPreenchido(obj.getListaCampanhaColaborador())) {
				for (CampanhaColaboradorVO ccVO : obj.getListaCampanhaColaborador()) {
					if (!Uteis.isAtributoPreenchido(ccVO.getListaCampanhaColaboradorCursoVOs())) {
						throw new ConsistirException("O campo CURSO (Colaboradores) deve ser informado.");
					}
				}
			} else {
				throw new ConsistirException("O campo PARTICIPANTE (Colaboradores) deve ser informado.");
			}
		}
	}

	public void validarCampanhaGerarAgenda(CampanhaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getWorkflow().getCodigo() == null || obj.getWorkflow().getCodigo() == 0) {
			throw new ConsistirException("O campo workflow (dados básicos) deve ser informado para geração da agenda.");
		}
		if (obj.getMeta().getCodigo() == null || obj.getMeta().getCodigo() == 0) {
			throw new ConsistirException("O campo meta (dados básicos) deve ser informado para geração da agenda.");
		}
		if (obj.getListaCampanhaColaborador().isEmpty()) {
			throw new ConsistirException("Deve ser informado algum colaborador para geração da agenda.");
		}

		if (obj.getHoraInicial().isEmpty()) {
			throw new ConsistirException("O campo HORA INICIAL (Dados Básicos) deve ser informado.");
		}

		if ((Integer.parseInt(obj.getHoraInicial().substring(0, 2)) > 18) || (Integer.parseInt(obj.getHoraInicial().substring(3, 5)) > 59)) {
			throw new ConsistirException("Não é possível gravar um compromisso depois das 18 horas");
		}

		if ((Integer.parseInt(obj.getHoraInicial().substring(0, 2)) < 8)) {
			throw new ConsistirException("Não é possível gravar um compromisso antes das 8 da manhã");
		}

		if ((Integer.parseInt(obj.getHoraInicial().substring(0, 2)) >= 12) && (Integer.parseInt(obj.getHoraInicial().substring(0, 2)) < 14)) {
			throw new ConsistirException("Não é possível gravar um compromisso entre meio dia e quatorze horas");
		}

		if ((Integer.parseInt(obj.getHoraInicial().substring(0, 2)) >= 12) && Uteis.getDiaSemanaEnum(obj.getPeriodoInicio()).equals(DiaSemana.SABADO)) {
			throw new ConsistirException("Não é possível gravar um compromisso no sábado após o meio dia");
		}
	}

	public void validarDadosExclusao(CampanhaVO obj) throws Exception {
		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			throw new ConsistirException("O objeto não pôde ser excluído porque ainda não foi salvo.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CampanhaVO obj, List<CampanhaColaboradorVO> objetosColaborador, List<CampanhaMidiaVO> objetosMidia, List<CampanhaPublicoAlvoVO> objetosPublicoAlvo, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(obj, true);
			final String sql = "INSERT INTO campanha(descricao, unidadeensino, curso, periodoinicio, periodofim, " + "workflow, meta, possuiAgenda, tipocampanha, objetivo, publicoAlvo, horaInicial, considerarSabado, considerarFeriados, horaFinal, horaInicioIntervalo, horaFimIntervalo, dataInicialVerificarJaExisteAgendaProspect, " + 
                                "dataFinalVerificarJaExisteAgendaProspect,recorrente,tipoRecorrencia,dataRecorrencia, politicaGerarAgenda) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getCurso().getCodigo() != 0) {
						sqlInserir.setInt(3, obj.getCurso().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getPeriodoInicio()));
					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getPeriodoFim()));
					sqlInserir.setInt(6, obj.getWorkflow().getCodigo());
					if (obj.getMeta().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getMeta().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setBoolean(8, obj.getPossuiAgenda());
					sqlInserir.setString(9, obj.getTipoCampanha().toString());
					sqlInserir.setString(10, obj.getObjetivo());
					sqlInserir.setString(11, obj.getPublicoAlvo());
					sqlInserir.setString(12, obj.getHoraInicial());
					sqlInserir.setBoolean(13, obj.getConsiderarSabado());
					sqlInserir.setBoolean(14, obj.getConsiderarFeriados());

					sqlInserir.setString(15, obj.getHoraFinal());
					sqlInserir.setString(16, obj.getHoraInicioIntervalo());
					sqlInserir.setString(17, obj.getHoraFimIntervalo());

					sqlInserir.setDate(18, Uteis.getDataJDBC(obj.getDataInicialVerificarJaExisteAgendaProspect()));
					sqlInserir.setDate(19, Uteis.getDataJDBC(obj.getDataFinalVerificarJaExisteAgendaProspect()));
					sqlInserir.setBoolean(20, obj.getRecorrente());
					if (obj.getRecorrente()) {
						sqlInserir.setString(21, obj.getTipoRecorrencia().toString());
						sqlInserir.setDate(22, Uteis.getDataJDBC(obj.getDataRecorrencia()));
					} else {
						sqlInserir.setNull(21, 0);
						sqlInserir.setNull(22, 0);
					}
					sqlInserir.setString(23, obj.getPoliticaGerarAgenda().toString());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getCampanhaColaboradorFacade().incluirCampanhaColaborador(obj.getCodigo(), objetosColaborador);
			getFacadeFactory().getCampanhaMidiaFacade().incluirCampanhaMidia(obj.getCodigo(), objetosMidia);
			getFacadeFactory().getCampanhaPublicoAlvoFacade().incluirCampanhaPublicoAlvo(obj.getCodigo(), objetosPublicoAlvo, usuario);
			obj.setSituacao("EC");
			this.gravarSituacao(obj, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarSituacao(final CampanhaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(obj, false);
			Campanha.alterar(getIdEntidade());
			final String sql = "UPDATE campanha SET situacao=? WHERE ((codigo =? ))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarCampanha(final CampanhaVO obj, UsuarioVO usuario) throws Exception {
		try {
			// validarDados(obj);
			// realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(obj,
			// false);
			obj.setSituacao("CA");
			Campanha.alterar(getIdEntidade());
			final String sql = "UPDATE campanha SET situacao=? WHERE ((codigo =? ))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarCancelamentoCompromissosNaoIniciacaoCampanha(obj.getCodigo(), false, usuario);
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().executarCancelamentoCompromissoCampanhaPublicoAlvoProspectNaoIniciacaoCampanha(obj.getCodigo(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void finalizarCampanha(final CampanhaVO obj, UsuarioVO usuario) throws Exception {
		try {
			// validarDados(obj);
			// realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(obj,
			// false);
			obj.setSituacao("FI");
			Campanha.alterar(getIdEntidade());
			final String sql = "UPDATE campanha SET situacao=? WHERE ((codigo =? ))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarAgenda(final CampanhaVO obj) throws Exception {
		try {
			final String sql = "UPDATE campanha SET possuiAgenda=? WHERE ((codigo =? ))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getPossuiAgenda());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CampanhaVO obj, List<CampanhaColaboradorVO> objetosColaborador, List<CampanhaMidiaVO> objetosMidia, List<CampanhaPublicoAlvoVO> objetosPublicoAlvo, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(obj, false);
			Campanha.alterar(getIdEntidade());
			final String sql = "UPDATE campanha SET descricao=?, unidadeensino=?,  curso=?, periodoinicio=?, periodofim=?, workflow=?, meta=?, possuiAgenda=?, tipocampanha=?, objetivo=?, "
					+ "publicoalvo=?, horaInicial=?, considerarSabado=?, considerarFeriados=?, horaFinal=?, horaInicioIntervalo=?, horaFimIntervalo=?, "
					+ "dataInicialVerificarJaExisteAgendaProspect=?, " + "dataFinalVerificarJaExisteAgendaProspect=?, recorrente=?, tipoRecorrencia=?,"
					+ "dataRecorrencia=?, politicaGerarAgenda=? WHERE ((codigo =? ))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getCurso().getCodigo() != 0) {
						sqlAlterar.setInt(3, obj.getCurso().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getPeriodoInicio()));
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getPeriodoFim()));
					if (obj.getWorkflow().getCodigo() != 0) {
						sqlAlterar.setInt(6, obj.getWorkflow().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getMeta().getCodigo() != 0) {
						sqlAlterar.setInt(7, obj.getMeta().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setBoolean(8, obj.getPossuiAgenda());
					sqlAlterar.setString(9, obj.getTipoCampanha().toString());
					sqlAlterar.setString(10, obj.getObjetivo());
					sqlAlterar.setString(11, obj.getPublicoAlvo());
					sqlAlterar.setString(12, obj.getHoraInicial());
					sqlAlterar.setBoolean(13, obj.getConsiderarSabado());
					sqlAlterar.setBoolean(14, obj.getConsiderarFeriados());
					sqlAlterar.setString(15, obj.getHoraFinal());
					sqlAlterar.setString(16, obj.getHoraInicioIntervalo());
					sqlAlterar.setString(17, obj.getHoraFimIntervalo());
					sqlAlterar.setDate(18, Uteis.getDataJDBC(obj.getDataInicialVerificarJaExisteAgendaProspect()));
					sqlAlterar.setDate(19, Uteis.getDataJDBC(obj.getDataFinalVerificarJaExisteAgendaProspect()));
					sqlAlterar.setBoolean(20, obj.getRecorrente());
					if (obj.getRecorrente()) {
						sqlAlterar.setString(21, obj.getTipoRecorrencia().toString());
						sqlAlterar.setDate(22, Uteis.getDataJDBC(obj.getDataRecorrencia()));
					} else {
						sqlAlterar.setNull(21, 0);
						sqlAlterar.setNull(22, 0);
					}
                    sqlAlterar.setString(23, obj.getPoliticaGerarAgenda().toString());
					sqlAlterar.setInt(24, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getCampanhaColaboradorFacade().alterarCampanhaColaborador(obj.getCodigo(), objetosColaborador);
			getFacadeFactory().getCampanhaMidiaFacade().alterarCampanhaMidia(obj.getCodigo(), objetosMidia);
			getFacadeFactory().getCampanhaPublicoAlvoFacade().alterarCampanhaPublicoAlvo(obj.getCodigo(), objetosPublicoAlvo, usuario);
		} catch (Exception e) {
			throw e;
		}

	}

	public List<CampanhaVO> consultar(String campoConsulta, String valorConsulta, Integer unidadeEnsino, String situacao, UsuarioVO usuario) throws Exception {
		List<CampanhaVO> objs = new ArrayList<CampanhaVO>(0);
		if (campoConsulta.equals("descricao")) {
			objs = consultarPorDescricao(valorConsulta, situacao, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsulta.equals("unidadeensino")) {
			objs = consultarPorUnidadeEnsino(valorConsulta, situacao, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsulta.equals("curso")) {
			objs = consultarPorCurso(valorConsulta, situacao, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsulta.equals("situacao")) {
			objs = consultarPorSituacao(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		return objs;
	}

	public List<CampanhaVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception {
		return consultar(campoConsulta, valorConsulta, 0, "TO", usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CampanhaVO obj, UsuarioVO usuario) throws Exception {
		try {
			Campanha.excluir(getIdEntidade());
			validarDadosExclusao(obj);
			getFacadeFactory().getCampanhaColaboradorFacade().excluirCampanhaColaborador(obj.getCodigo());
			getFacadeFactory().getCampanhaMidiaFacade().excluirCampanhaMidia(obj.getCodigo());
			getFacadeFactory().getCampanhaPublicoAlvoFacade().excluirCampanhaPublicoAlvo(obj.getCodigo(), usuario);
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().excluirCompromissoCampanhaPublicoAlvoPorCampanha(obj.getCodigo(), usuario);
			String sql = "DELETE FROM campanha WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder str = new StringBuilder();
		// Dados campanha
		str.append("SELECT campanha.codigo AS codigo, campanha.descricao AS descricao, unidadeensino.nome AS unidadeensino, curso.nome AS curso, campanha.situacao AS situacao, campanha.tipocampanha ");
		str.append(" FROM campanha ");

		str.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = campanha.unidadeensino");
		str.append(" LEFT JOIN curso ON curso.codigo = campanha.curso ");
		return str;
	}

	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder str = new StringBuilder();
		// Dados campanha
		str.append(" SELECT  distinct campanha.codigo AS codigo, campanha.descricao AS descricao, ");
		str.append(" campanha.possuiAgenda AS possuiAgenda,  campanha.considerarSabado AS considerarSabado, campanha.considerarFeriados AS considerarFeriados, campanha.horaFinal AS horaFinal, ");
		str.append(" campanha.horaInicioIntervalo AS horaInicioIntervalo, campanha.horaFimIntervalo AS horaFimIntervalo, campanha.naoGerarAgendaParaProspectsComAgendaJaExistente AS naoGerarAgendaParaProspectsComAgendaJaExistente, ");
		str.append(" campanha.dataInicialVerificarJaExisteAgendaProspect AS dataInicialVerificarJaExisteAgendaProspect, campanha.dataFinalVerificarJaExisteAgendaProspect AS dataFinalVerificarJaExisteAgendaProspect, ");
		str.append(" campanha.recorrente as recorrente, campanha.tiporecorrencia as tiporecorrencia, campanha.datarecorrencia as datarecorrencia, campanha.unidadeensino as unidadeensino, ");
		str.append(" campanha.periodoInicio AS periodoInicio, campanha.horainicial AS horainicial, campanha.periodoFim AS periodoFim, campanha.workflow, campanha.tipocampanha AS tipoCampanha, ");
		str.append(" campanha.politicaGerarAgenda AS politicaGerarAgenda, campanha.meta AS meta, campanha.objetivo AS objetivo, campanha.publicoalvo AS publicoAlvo, campanha.gerarAgendaConsultorRespProspect AS gerarAgendaConsultorRespProspect, ");
		//Dados do Curso
		str.append(" curso.nome AS curso, curso.codigo AS codigoCurso, campanha.situacao AS situacao, ");
		//Dados do WorkFlow
		str.append(" workflow.tempoMedioGerarAgenda AS workflow_tempoMedioGerarAgenda, ");
		//Dados da Meta
		str.append(" meta.tipometa AS meta_tipoMeta, meta.considerarSabado AS meta_considerarSabado ");
		str.append(" FROM campanha  ");
		str.append(" LEFT JOIN curso ON curso.codigo = campanha.curso  ");
		str.append(" LEFT JOIN workflow ON workflow.codigo = campanha.workflow ");
		str.append(" LEFT JOIN meta ON meta.codigo = campanha.meta ");
		return str;
	}

	public CampanhaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaCompleta().append("WHERE campanha.codigo = ").append(codigoPrm);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorUnidadeEnsino(valorConsulta, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorUnidadeEnsino(valorConsulta, situacao, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("WHERE unidadeensino.nome ");
		sqlStr.append(" ilike ('").append(valorConsulta.toUpperCase()).append("%') ");
		if ((!situacao.equals("")) && (!situacao.equals("TO"))) {
			if ((situacao.equals("AC"))) { // situacao criada somente para
											// filtrar todas as campanhas ativas
											// e em construcao ao mesmo tempo
				sqlStr.append(" and ((situacao = 'AT') OR (situacao = 'EC')) ");
			} else {
				sqlStr.append(" and situacao = '").append(situacao).append("' ");
			}
		}
		if (unidadeensino != null) {
			if (unidadeensino != 0) {
				sqlStr.append(" and campanha.unidadeensino = ").append(unidadeensino).append("");
			}
		}
		Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(usuario);
		Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(usuario);
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
					sqlStr.append(" and campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA' ");
		} else if (visualizarVendas) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' ");
		}
		sqlStr.append(" ORDER BY unidadeensino");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorUnidadeEnsinoPorSituacaoPorTipoCampanha(Integer codigoUnidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("WHERE unidadeensino.codigo =  ").append(codigoUnidadeEnsino);
		sqlStr.append(" and tipocampanha = '").append(tipoCampanhaEnum).append("' ");
		if (Uteis.isAtributoPreenchido(situacao)) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}
		sqlStr.append(" ORDER BY unidadeensino");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("WHERE upper(situacao)");
		Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(usuario);
		Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(usuario);
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
					sqlStr.append(" and campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA' ");
		} else if (visualizarVendas) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' ");
		}
		sqlStr.append("like('").append(valorConsulta.toUpperCase()).append("%') ORDER BY situacao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorCurso(valorConsulta, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorCurso(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorCurso(valorConsulta, situacao, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorCurso(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("WHERE upper(curso.nome) ");
		sqlStr.append("like('").append(valorConsulta.toUpperCase()).append("%') ");
		if ((!situacao.equals("")) && (!situacao.equals("TO"))) { // todas as
																	// situacoes
			if (situacao.equals("AC")) { // situacao criada para filtras todas
											// as campanhas ativas e em
											// construcao
				sqlStr.append(" and ((situacao = 'AT') or (situacao = 'EC')) ");
			} else {
				sqlStr.append(" and situacao = '").append(situacao).append("'");
			}
		}
		if (unidadeensino != null) {
			if (unidadeensino != 0) {
				sqlStr.append(" and campanha.unidadeensino = ").append(unidadeensino).append("");
			}
		}
		Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(usuario);
		Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(usuario);
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
					sqlStr.append(" and campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA' ");
		} else if (visualizarVendas) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' ");
		}
		sqlStr.append(" ORDER BY curso");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorDescricao(String valorConsulta, Integer unidade, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorDescricao(valorConsulta, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultarPorDescricao(valorConsulta, situacao, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE upper(campanha.descricao) ");
		sqlStr.append("like(?) ");
		if ((!situacao.equals("")) && (!situacao.equals("TO"))) {
			if (situacao.equals("AC")) { // situacao especial criada para
											// definir que devem ser listadas as
											// campanhas ativas e em construção
											// - facilitar na busca do usuário
				sqlStr.append(" and ((situacao = 'AT') or (situacao = 'EC'))");
			} else {
				sqlStr.append(" and situacao = '").append(situacao).append("'");
			}
		}
		if (unidadeensino != null) {
			if (unidadeensino != 0) {
				sqlStr.append(" and campanha.unidadeensino = ").append(unidadeensino).append("");
			}
		}
		
		Boolean visualizarCobranca = verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(usuario);
		Boolean visualizarVendas = verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(usuario);
		if (visualizarCobranca && visualizarVendas) {

		} else if (visualizarCobranca) {
					sqlStr.append(" and campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA' ");
		} else if (visualizarVendas) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA' ");
		}
		sqlStr.append(" ORDER BY campanha.descricao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase() +"%");
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	
	
	
	public Boolean verificarPermissaoVisualizarPermitirVisualizarCampanhaCobranca(UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaCobranca", usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public Boolean verificarPermissaoVisualizarPrmitirVisualizarCampanhaVendas(UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaVendas", usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public List<CampanhaVO> consultarPorCodigoCampanhaColaboradoFuncionarioPorTipoCampanha(Integer valorConsulta, Integer unidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("  ");
		sqlStr.append(" inner join campanhacolaborador on campanhacolaborador.campanha = campanha.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
		sqlStr.append(" inner join  funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		sqlStr.append(" where funcionario.pessoa = ").append(valorConsulta);
		sqlStr.append(" and (tipocampanha = '").append(tipoCampanhaEnum).append("' or tipocampanha = '").append(tipoCampanhaEnum).append("_COBRANCA')");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if (!situacao.isEmpty()) {
			sqlStr.append(" and situacao = '").append(situacao).append("'");
		}
		sqlStr.append(" ORDER BY campanha.descricao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<CampanhaVO> consultarPorListaUnidadeEnsinoPorListaCurso(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<CursoVO> listaCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sqlStr.append(" and campanha.unidadeensino in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaUnidadeEnsino)).append(" )");
		}
		if (Uteis.isAtributoPreenchido(listaCurso)) {
			sqlStr.append(" and campanha.curso in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaCurso)).append(" )");
		}
		sqlStr.append(" ORDER BY campanha.periodoinicio desc , campanha.descricao, case when campanha.situacao = 'AT' then 0 when campanha.situacao = 'FI' then 1 when campanha.situacao = 'EC' then 2  else 3 end");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	public static List<CampanhaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CampanhaVO> vetResultado = new ArrayList<CampanhaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static List<CampanhaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CampanhaVO> vetResultado = new ArrayList<CampanhaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasicos(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static CampanhaColaboradorVO montarDadosCampanhaColaborador(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCampanha().setCodigo(dadosSQL.getInt("campanha"));
		montarDadosFuncionarioCargo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.getFuncionarioCargoVO().setCodigo(dadosSQL.getInt("funcionarioCargo"));
		montarDadosCampanha(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setQtdContato(dadosSQL.getInt("qtdContato"));
		obj.setQtdSucesso(dadosSQL.getInt("qtdSucesso"));
		obj.setQtdCaptacao(dadosSQL.getInt("qtdCaptacao"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	public static void montarDadosFuncionarioCargo(CampanhaColaboradorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioCargoVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioCargoVO(new FuncionarioCargoVO());
			return;
		}

		obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosCampanha(CampanhaColaboradorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCampanha().getCodigo().intValue() == 0) {
			obj.setCampanha(new CampanhaVO());
			return;
		}

		obj.setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static CampanhaVO montarDadosBasicos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CampanhaVO obj = new CampanhaVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino"));
		obj.getCurso().setNome(dadosSQL.getString("curso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		if (dadosSQL.getString("tipoCampanha") != null) {
			obj.setTipoCampanha(TipoCampanhaEnum.valueOf(dadosSQL.getString("tipoCampanha")));
		}
		return obj;
	}

	public static CampanhaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CampanhaVO obj = new CampanhaVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getCurso().setNome(dadosSQL.getString("curso"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setTipoCampanha(TipoCampanhaEnum.valueOf(dadosSQL.getString("tipoCampanha")));
		obj.setPoliticaGerarAgenda(PoliticaGerarAgendaEnum.valueOf(dadosSQL.getString("politicaGerarAgenda")));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
			return obj;
		}
		obj.setPeriodoInicio(dadosSQL.getDate("periodoInicio"));
		obj.setHoraInicial(dadosSQL.getString("horainicial"));

		obj.setHoraFinal(dadosSQL.getString("horafinal"));
		obj.setHoraInicioIntervalo(dadosSQL.getString("horaInicioIntervalo"));
		obj.setHoraFimIntervalo(dadosSQL.getString("horaFimIntervalo"));

		obj.setPeriodoFim(dadosSQL.getDate("periodoFim"));
		obj.getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow")));
		obj.getWorkflow().setTempoMedioGerarAgenda(new Integer(dadosSQL.getInt("wf_tempoMedioGerarAgenda")));
		obj.getMeta().setCodigo(new Integer(dadosSQL.getInt("meta")));
		obj.getMeta().setTipoMeta(TipoMetaEnum.valueOf(dadosSQL.getString("meta_tipoMeta")));
		obj.getMeta().setConsiderarSabado(dadosSQL.getBoolean("meta_considerarSabado"));
	
		obj.setObjetivo(dadosSQL.getString("objetivo"));
		obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setConsiderarSabado(dadosSQL.getBoolean("considerarSabado"));
		obj.setConsiderarFeriados(dadosSQL.getBoolean("considerarFeriados"));

		obj.setDataInicialVerificarJaExisteAgendaProspect(dadosSQL.getDate("dataInicialVerificarJaExisteAgendaProspect"));
		obj.setDataFinalVerificarJaExisteAgendaProspect(dadosSQL.getDate("dataFinalVerificarJaExisteAgendaProspect"));
		obj.setRecorrente(dadosSQL.getBoolean("recorrente"));
		obj.setTipoRecorrencia(TipoRecorrenciaCampanhaEnum.valueOf(dadosSQL.getString("tipoRecorrencia")).toString());
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static CampanhaVO montarDadosCompleto(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CampanhaVO obj = new CampanhaVO();
		while (dadosSQL.next()) {
			if (obj.getCodigo() == 0) {
				obj.setCodigo((dadosSQL.getInt("codigo")));
				obj.setDescricao(dadosSQL.getString("descricao"));
				obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
				obj.getCurso().setNome(dadosSQL.getString("curso"));
				obj.getCurso().setCodigo(dadosSQL.getInt("codigoCurso"));
				if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
					return obj;
				}
				obj.setPeriodoInicio(dadosSQL.getDate("periodoInicio"));
				obj.setPeriodoFim(dadosSQL.getDate("periodoFim"));
				obj.setHoraInicial(dadosSQL.getString("horainicial"));

				obj.setHoraFinal(dadosSQL.getString("horafinal"));
				obj.setHoraInicioIntervalo(dadosSQL.getString("horaInicioIntervalo"));
				obj.setHoraFimIntervalo(dadosSQL.getString("horaFimIntervalo"));

				obj.setConsiderarSabado(dadosSQL.getBoolean("considerarSabado"));
				obj.setConsiderarFeriados(dadosSQL.getBoolean("considerarFeriados"));

				obj.getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow")));
				obj.getWorkflow().setTempoMedioGerarAgenda(new Integer(dadosSQL.getInt("workflow_tempoMedioGerarAgenda")));
				obj.getMeta().setCodigo(new Integer(dadosSQL.getInt("meta")));
				if (dadosSQL.getString("meta_tipoMeta") != null) {
					obj.getMeta().setTipoMeta(TipoMetaEnum.valueOf(dadosSQL.getString("meta_tipoMeta")));
				}
				obj.getMeta().setConsiderarSabado(dadosSQL.getBoolean("meta_considerarSabado"));
				obj.setTipoCampanha(TipoCampanhaEnum.valueOf(dadosSQL.getString("tipoCampanha")));
                obj.setPoliticaGerarAgenda(PoliticaGerarAgendaEnum.valueOf(dadosSQL.getString("politicaGerarAgenda")));
				obj.setObjetivo(dadosSQL.getString("objetivo"));
				obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
				obj.setSituacao(dadosSQL.getString("situacao"));
				obj.setPossuiAgenda(dadosSQL.getBoolean("possuiAgenda"));
				obj.setDataInicialVerificarJaExisteAgendaProspect(dadosSQL.getDate("dataInicialVerificarJaExisteAgendaProspect"));
				obj.setDataFinalVerificarJaExisteAgendaProspect(dadosSQL.getDate("dataFinalVerificarJaExisteAgendaProspect"));
				obj.setRecorrente(dadosSQL.getBoolean("recorrente"));
				obj.setTipoRecorrencia(dadosSQL.getString("tipoRecorrencia"));
				obj.setNovoObj(Boolean.FALSE);
			}
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
				return obj;
			}
			// Dados campanha colaborador
			obj.setListaCampanhaColaborador(getFacadeFactory().getCampanhaColaboradorFacade().consultarCampanhaColaboradorPorCampanha(obj.getCodigo(), usuario));
			// Dados campanha midia
			obj.setListaCampanhaMidia(getFacadeFactory().getCampanhaMidiaFacade().consultarCampanhaMidiaPorCampanha(obj.getCodigo(), usuario));
			// Dados campanha publico alvo
			obj.setListaCampanhaPublicoAlvo(getFacadeFactory().getCampanhaPublicoAlvoFacade().consultarCampanhaPublicoAlvoPorCampanha(obj.getCodigo(), usuario));
            montarDadosCampanhaPublicoAlvoProspect(obj, usuario);
			// Dados meta item
                        montarDadosCampanhaPublicoAlvoProspect(obj, usuario);
			montarDadosMeta(obj, usuario);
		}
		return obj;
	}
        
        public static void montarDadosCampanhaPublicoAlvoProspect(CampanhaVO obj, UsuarioVO usuario) throws Exception {
            for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : obj.getListaCampanhaPublicoAlvo()) {
                List<CampanhaPublicoAlvoProspectVO> listaProspectPublicoAlvo = getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().consultarPorCampanhaPublicoAlvo(campanhaPublicoAlvoVO.getCodigo());
                campanhaPublicoAlvoVO.setCampanhaPublicoAlvoProspectVOs(listaProspectPublicoAlvo);
            }
        }

	public boolean verificarConsultorPadraoEstaNaListaConsultoresDefinidosParaCampanha(CampanhaVO campanha, FuncionarioVO consultor) throws Exception {
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(consultor.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	public CampanhaColaboradorVO obterConsultorDistribuicaoParaGerarAgenda(CampanhaVO campanha, Integer codigoFuncionario) throws Exception {
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
				if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)) {
					if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(codigoFuncionario)) {
						return campanhaColaboradorVO;
					}
				}
			} else {
				if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(codigoFuncionario)) {
					return campanhaColaboradorVO;
				}
			}
		}
		throw new Exception("Colaborador (Código Funcionário: " + codigoFuncionario + ") não faz parte desta campanha. Não foi possível gerar agenda para o mesmo.");
	}

	public CampanhaColaboradorVO obterConsultorDistribuicaoRotacionadaParaGerarAgenda(CampanhaVO campanha, Integer consultorDistribuir) throws Exception {
		Integer contador = 1;
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (!campanhaColaboradorVO.getFuncionarioCargoVO().getAtivo()) {
				continue;
			}
			if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
				if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)) {
					if (contador.equals(consultorDistribuir)) {
						return campanhaColaboradorVO;
					}
					contador = contador + 1;
				}
			} else {
				if (contador.equals(consultorDistribuir)) {
					return campanhaColaboradorVO;
				}
				contador = contador + 1;
				
			}
		}
		throw new Exception("Ocorreu um problema no rotacionamento de prospects por consultor (Não existe um consultor na posição" + consultorDistribuir + ").");
	}
	
	public CampanhaColaboradorVO obterConsultorPriorizandoConsultorUltimaInteracao(CampanhaVO campanha, Integer codigoPessoaProspect, UsuarioVO usuarioVO) throws Exception {
		FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarConsultorUltimaInteracaoProspectPorProspect(codigoPessoaProspect, usuarioVO);
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
				if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)) {
					if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(funcionarioVO.getCodigo())) {
						return campanhaColaboradorVO;
					}
				}
			} else {
				if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(funcionarioVO.getCodigo())) {
					return campanhaColaboradorVO;
				}
			}
		}
		throw new Exception("Colaborador (Código Funcionário: " + funcionarioVO.getCodigo() + ") não faz parte desta campanha. Não foi possível gerar agenda para o mesmo.");
	}

	/**
	 * Este método visa garantir que o rotacionamento de Consultores para
	 * distribuição dos prospects funcione de forma adequada. Desta maneira, a
	 * variável consultorDistribuir é incrimentada para que o próximo consultor
	 * seja o alvo da próxima distribuição (geração de agenda). Contudo, caso
	 * após o incremento, seja alcançado o fim da lista, então o mesmo é
	 * reiniciado para a primeira posição (consultor 1).
	 * 
	 * @param campanha
	 * @param consultorDistribuir
	 * @throws Exception
	 */
	public void avancarRotacionamentoConsultorCampanha(CampanhaVO campanha) throws Exception {
//		Integer tamanhoLista = campanha.getListaCampanhaColaborador().size();
		Integer tamanhoLista = 0;
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getFuncionarioCargoVO().getAtivo()) {
				tamanhoLista++;
			}
		}
		
		if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
			tamanhoLista = campanha.getTamanhoListaRedistribuicao();
		}
		campanha.setConsultorDistribuir(campanha.getConsultorDistribuir() + 1);
		if (campanha.getConsultorDistribuir() > tamanhoLista) {
			campanha.setConsultorDistribuir(1);
		}
	}

	/**
	 * 
	 * @param campanha
	 * @param campanhaPublicoAlvoProspectVO
	 * @param campanhaColaboradorVO
	 * @return
	 * @throws Exception
	 */
	public Boolean verificarAgendaProspectJaFoiGerada(CampanhaVO campanha, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO) throws Exception {
		Integer codigoPessoaProspectControlarAgendaJaGerada = campanhaPublicoAlvoProspectVO.getCodigoProspectPessoa();

		String tipoProspectPessoa = "Aluno";
		if ((!campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0))) {
			tipoProspectPessoa = "Prospect";
		}

		Integer resultado = campanha.getMapaProspectsDistribuidos().get(tipoProspectPessoa + "_" + codigoPessoaProspectControlarAgendaJaGerada);
		if (resultado != null) {
			return true;
		}
		return false;
	}

	public void montarCompromissoEPersistirAgendaProspectConsultor(CampanhaVO campanha, CampanhaColaboradorVO campanhaColaborador, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, UsuarioVO usuario) throws Exception {

		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarGeracaoCompromissoRotacionamento(agenda, agendaPessoaHorario, campanha, usuario, campanhaPublicoAlvoProspectVO.getProspect(), campanhaColaborador.getHoraGeracaoProximaAgenda());
		campanhaColaborador.setDataUltimaAgendaGerada(Uteis.getDateTime(campanhaColaborador.getDataGeracaoProximaAgenda(), Uteis.getHoraDaString(campanhaColaborador.getHoraGeracaoProximaAgenda()), Uteis.getMinutosDaString(campanhaColaborador.getHoraGeracaoProximaAgenda()), 0));
		campanhaColaborador.setNumeroAgendasGeradasParaData(campanhaColaborador.getNumeroAgendasGeradasParaData() + 1);
                boolean reiniciouHorario = false;
		if (campanhaColaborador.getNumeroAgendasGeradasParaData().compareTo(campanhaColaborador.getQtdContato()) >= 0) {
			// Se entrarmos aqui é por que atingimos o número máximos de
			// contatos estabelecidos como meta
			// para o dia de trabalho do consultor em questão. Então temos que
			// avançar de dia, e zerar a contagem
			// de prospects agendados para o dia, para que o controle seja
			// iniciado novamente.
			// Quando entramos aqui, a troca de dia é realizada não em
			// consequencia de atingir o final do expediente
			// mas sim em função da meta do dia de contatos já ter sido
			// atingida.
			Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
			campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
			agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agenda.getCodigo(), proximoDiaGerarAgenda, Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuario);
			if (agendaPessoaHorario.getCodigo() == 0) {
				agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(proximoDiaGerarAgenda), Uteis.getMesData(proximoDiaGerarAgenda), Uteis.getAnoData(proximoDiaGerarAgenda), Uteis.getDiaSemanaEnum(proximoDiaGerarAgenda), true);
			}
			campanhaColaborador.setHoraGeracaoProximaAgenda(campanhaColaborador.getHoraInicioGerarAgenda());
			campanhaColaborador.setNumeroAgendasGeradasParaData(0);
                        reiniciouHorario = true;
		}

		Integer tempoMedioCadaAgenda = campanha.getWorkflow().getTempoMedioGerarAgenda();
		String proximoHorarioAgenda = "";
                if (!reiniciouHorario) {
                    if (!tempoMedioCadaAgenda.equals(0)) {
                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), campanha.getWorkflow().getTempoMedioGerarAgenda());
                    } else {
                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), 1);
                    }
                } else {
                    proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
                }
		if ((!campanhaColaborador.getHoraFimIntervalo().equals("")) && ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFimIntervalo()) < 0))) {
			// Se existe uma horario final para o intervalo e
			// proximoHorarioAgenda é menor que o mesmo
			// é por que ainda estamos no primeiro periodo de trabalho do
			// consultor, entao temos que verificar
			// se já atingimos o final do primeiro periodo. Caso sim, já
			// assumimos o fim do intervalo como
			// proximoHorarioAgenda
			if (!campanhaColaborador.getHoraInicioIntervalo().equals("")) {
				if (proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraInicioIntervalo()) >= 0) {
					// Atingimos o horário do intervalor da pessoa, logo o
					// proximo horário a ser gerada agenda
					// é no retorno do intervalo.
					proximoHorarioAgenda = campanhaColaborador.getHoraFimIntervalo();
				}
			}
			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
		} else {
			// Se entrarmos aqui é por que não existe intervalo programado ou já
			// estamos gerando
			// agenda para o segundo período de trabalho do consultor (parte da
			// tarde por exemplo).
			// Logo temos que avaliar se já atingimos o final do expediente.
			// Caso sim, temos que
			// ir para o próximo dia, pois não é possível mais gerar agenda no
			// mesmo dia.
			if ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFinalGerarAgenda()) >= 0)) {
				// Avancar para o próximo dia
				Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
				campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
				agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agenda.getCodigo(), proximoDiaGerarAgenda, Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuario);
				if (agendaPessoaHorario.getCodigo() == 0) {
					agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(proximoDiaGerarAgenda), Uteis.getMesData(proximoDiaGerarAgenda), Uteis.getAnoData(proximoDiaGerarAgenda), Uteis.getDiaSemanaEnum(proximoDiaGerarAgenda), true);
				}
				proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
				campanhaColaborador.setNumeroAgendasGeradasParaData(0);
			}
			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
		}
	}

	public Date gerarAgendaProspectConsultor(CampanhaVO campanha, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, CampanhaColaboradorVO campanhaColaboradorVO, UsuarioVO usuario) throws Exception {
		campanha.setNumeroProspectsDistribuidos(campanha.getNumeroProspectsDistribuidos() + 1);
		Integer codigoPessoaProspectControlarAgendaJaGerada = campanhaPublicoAlvoProspectVO.getCodigoProspectPessoa();
		String tipoProspectPessoa = "Aluno";
		
		if ((!campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0))) {
			getFacadeFactory().getProspectsFacade().carregarDados(campanhaPublicoAlvoProspectVO.getProspect(), usuario);
			tipoProspectPessoa = "Prospect";
		}
		campanha.getMapaProspectsDistribuidos().put(tipoProspectPessoa + "_" + codigoPessoaProspectControlarAgendaJaGerada, campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());

		AgendaPessoaVO agenda = realizarValidacaoAgenda(campanhaColaboradorVO, campanha, usuario);
		AgendaPessoaHorarioVO agendaPessoaHorario = realizarValidacaoAgendaPessoaHorarioExiste(agenda, campanhaColaboradorVO.getDataGeracaoProximaAgenda(), campanha.getConsiderarSabado(), campanha.getConsiderarFeriados(), usuario);
                // atualizando a data base para geracao de age,da pois ao obter agendaPessoaHoraria, pode-se
                // ter encontrado feriados e/ou finais de semana.
                campanhaColaboradorVO.setDataGeracaoProximaAgenda(agendaPessoaHorario.getDataCompromisso());
                
		// // System.out.println("AGENDA GERADA CONSULTOR: " +
		// campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome()
		// + " PROSPECT " + campanha.getNumeroProspectsDistribuidos() + ": " +
		// codigoPessoaProspectControlarAgendaJaGerada + " " +
		// campanhaPublicoAlvoProspectVO.getProspect().getNome());

		if ((campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0)) && (!campanhaPublicoAlvoProspectVO.getPessoa().getCodigo().equals(0))) {
			// Como o CRM é orientado para o prospect, caso estejamos gerando
			// uma agenda para
			// aluno, que não possui um prospect relacionado, então temos que
			// gerar
			// este prospect e linkar os registros para que funcione
			// adequadamente no
			// restante do CRM

			// Carregamos os dados pessoa, que serão úteis para buscar e gerar
			// um prospect correspodente
			getFacadeFactory().getPessoaFacade().carregarDados(campanhaPublicoAlvoProspectVO.getPessoa(), usuario);
			ProspectsVO prospectPessoa = new ProspectsVO();
			prospectPessoa.setTipoProspect(TipoProspectEnum.FISICO);
			prospectPessoa.setCpf(campanhaPublicoAlvoProspectVO.getPessoa().getCPF());
			prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(prospectPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			boolean encontrouProspect = false;
			if (prospectPessoa.getCodigo().equals(0)) {
				// Se entrarmos auqi é por que de fato não existe um Prospect
				// para a pessoa
				// Assim, temos que gerar um prospect e gravá-lo, passando a
				// referenciá-lo no compromisso abaixo.
				if (Uteis.isAtributoPreenchido(campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim())) {
					prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim(), false, usuario);
				}
				if (prospectPessoa.getCodigo().equals(0)) {
					prospectPessoa = campanhaPublicoAlvoProspectVO.getPessoa().gerarProspectsVOAPartirPessoaVO();
					if (campanhaPublicoAlvoProspectVO.getCampanhaPublicoAlvo().getTipoPublicoAlvo().equals("CD")) {
						prospectPessoa.setUnidadeEnsino(campanhaPublicoAlvoProspectVO.getCampanhaPublicoAlvo().getUnidadeEnsino());
					}
					if (campanhaPublicoAlvoProspectVO.getCampanhaPublicoAlvo().getTipoPublicoAlvo().equals("AL")) {
						MatriculaVO matricula = new MatriculaVO();
						matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaUnicaMatricula(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo(), 0, false, usuario);
						prospectPessoa.setUnidadeEnsino(matricula.getUnidadeEnsino());
					}
					prospectPessoa = campanhaPublicoAlvoProspectVO.getPessoa().gerarProspectsVOAPartirPessoaVO();
					getFacadeFactory().getProspectsFacade().incluirSemValidarDados(prospectPessoa, Boolean.FALSE, usuario, null);
				} else {
					encontrouProspect = true;
				}
			} else {
				encontrouProspect = true;
			}
			if (encontrouProspect) {
				if (prospectPessoa.getPessoa().getCodigo().equals(0)) {
					// Se entrar aqui é por que encontramos o prospect, mas o
					// mesmo não está
					// vinculado ao aluno (pessoa) correspondete. Logo temos que
					// fazer este vinculo.
					prospectPessoa.getPessoa().setCodigo(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo());
					getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectPessoa, usuario);
				}
				if (!prospectPessoa.getPessoa().getCodigo().equals(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo())) {
					throw new Exception("Ocorreu um erro ao identificar o prospect da pessoa de código: " + campanhaPublicoAlvoProspectVO.getPessoa().getCodigo() + ". Prospect encontrado por CPF " + campanhaPublicoAlvoProspectVO.getPessoa().getCPF() + " E-mail " + campanhaPublicoAlvoProspectVO.getPessoa().getEmail() + " está vinculado a outra pessoa.");
				}
			}

			campanhaPublicoAlvoProspectVO.setProspect(prospectPessoa);
		}

		montarCompromissoEPersistirAgendaProspectConsultor(campanha, campanhaColaboradorVO, campanhaPublicoAlvoProspectVO, agenda, agendaPessoaHorario, usuario);

		return new Date();
	}

	/**
	 * Este método verifica se deve ser gerado para o prospect, considerando o
	 * controle para nao gerar agenda duplicada para o prospect.
	 * 
	 * @param campanha
	 * @param campanhaPublicoAlvoVO
	 * @param campanhaPublicoAlvoProspectVO
	 * @param usuario
	 * @return
	 */
	public boolean verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, UsuarioVO usuario) {
		try {
			if (campanhaPublicoAlvoVO.getNaoGerarAgendaParaProspectsComAgendaJaExistente()) {
				// se este controle de nao gerar agenda duplicada está desligado
				// é por que o usuário não deseja que nos preocupemos com isto
				// logo a agenda do prospect deverá ser gerada sempre.
				if (campanha.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA)) {
					return !getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCobranca(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo(), campanha.getDataInicialVerificarJaExisteAgendaProspect(), campanha.getDataFinalVerificarJaExisteAgendaProspect());
				}				
				if (campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0)) {
					return true;
				}
				boolean possuiAgendaEmAberto = false;
				
				possuiAgendaEmAberto = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCaptacao(campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), campanha.getDataInicialVerificarJaExisteAgendaProspect(), campanha.getDataFinalVerificarJaExisteAgendaProspect());					
				
				if (possuiAgendaEmAberto) {
					// Se entrar aqui é por que a campanha foi parametrizada
					// para nao
					// gerar agenda duplicada e o prospect em questão já tem
					// agenda
					// no período informado. Logo não poderemos gerar agenda
					// para ele.
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Date distribuirProspectsCampanhaPublicoAlvoProspectVO(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, UsuarioVO usuario) throws Exception {
		progressBar.setProgresso(0L);
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
			 
			if (!verificarAgendaProspectJaFoiGerada(campanha, campanhaPublicoAlvoProspectVO)) {
				// Só itemos processar um prospect, caso não tenha sido gerada
				// nenhuma agenda para o mesmo
				// Temos que controlar isto, pois um prospect pode ser
				// selecionado mais de uma vez em função
				// de mais de um curso de interesse ou situação em diferentes
				// publico Alvo

				if (verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoProspectVO, usuario)) {

//					if ((campanha.getGerarAgendaConsultorRespProspect().booleanValue()) && (campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo() > 0) && (verificarConsultorPadraoEstaNaListaConsultoresDefinidosParaCampanha(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao()))) {
					if ((campanhaPublicoAlvoVO.getTipoGerarAgendaCampanha().equals(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_RESPONSAVEL)) && (campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo() > 0) && (verificarConsultorPadraoEstaNaListaConsultoresDefinidosParaCampanha(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao()))) {
						// Quando a opção GerarAgendaConsultorRespProspect está
						// marcada, significa que o prospect e/ou pessoa
						// deverá ser gerenciada pelo seu consultor responsável.
						// Portanto, a distribuição
						// do prospect, deve privilegiar o consultor
						// responsavel, caso ele exista. Caso não exista uma
						// consultor
						// responsável definido para o prospect ou este
						// consultor não esteja selecionado para a campanha,
						// então o sistema irá seguir a política de distribuição
						// e gerar a agenda para o consultor apropriado -
						// consultorDistribuir.
						// Caso seja gerada a agenda para o
						// ConsultorResponsavel, não precisamos avancar no
						// rotacionamento
						// dos consultores, pois não seguimos o fluxo
						// convencional de distribuição
						CampanhaColaboradorVO campanhaColaboradorVO = obterConsultorDistribuicaoParaGerarAgenda(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo());
						gerarAgendaProspectConsultor(campanha, campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, usuario);
					} else {
						CampanhaColaboradorVO campanhaColaboradorVO = obterConsultorDistribuicaoRotacionadaParaGerarAgenda(campanha, campanha.getConsultorDistribuir());
						gerarAgendaProspectConsultor(campanha, campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, usuario);
						// Atualizar o atributo consultorDistribuir
						avancarRotacionamentoConsultorCampanha(campanha);
					}
				}
			}
			progressBar.setProgresso(progressBar.getProgresso() + 1);
			progressBar.setStatus("Carregando dados do Prospect n° " + campanhaPublicoAlvoProspectVO.getPessoa().getNome() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");	
		}
		return new Date();
	}

	@Override
	public void carregarDadosCompletosConsultoresCampanha(CampanhaVO campanha, UsuarioVO usuario) throws Exception {
		campanha.setListaCampanhaColaborador(getFacadeFactory().getCampanhaColaboradorFacade().montarListaCampanhaColaborador(campanha.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Os atributos DataGeracaoProximaAgenda e HoraGeracaoProximaAgenda são
	 * inicializados com a data de inicio e hora de inicio da campanha. Isto
	 * para cada um dos consultores adicionados para campanha. Assim, estas
	 * variaveris serão utilizadas para controlar qual a data/horario da proxima
	 * agenda a ser gerada.
	 * 
	 * @param campanha
	 * @param usuario
	 * @throws Exception
	 */
	public void inicializarDataEHoraInicioGeracaoAgendaConsultores(CampanhaVO campanha, UsuarioVO usuario) throws Exception {
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
		
			if(campanha.getPeriodoInicio().compareTo(new Date()) < 0 ){
				campanhaColaboradorVO.setDataGeracaoProximaAgenda(getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(new Date(), campanha.getUnidadeEnsino().getCidade().getCodigo(), campanha.getConsiderarSabado(), campanha.getConsiderarFeriados(), ConsiderarFeriadoEnum.ACADEMICO, usuario));	
			}else{
				campanhaColaboradorVO.setDataGeracaoProximaAgenda(getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(campanha.getPeriodoInicio(), campanha.getUnidadeEnsino().getCidade().getCodigo(), campanha.getConsiderarSabado(), campanha.getConsiderarFeriados(), ConsiderarFeriadoEnum.ACADEMICO, usuario));
			}
			campanhaColaboradorVO.setHoraGeracaoProximaAgenda(campanhaColaboradorVO.getHoraInicioGerarAgenda());
			campanhaColaboradorVO.setNumeroAgendasGeradasParaData(0);
		}
	}

	public Date obterMaiorDataCompromissoAgendadoConsultores(CampanhaVO campanha) throws Exception {
		Date maiorData = Uteis.getDate("01/01/1980");
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			Date dataCompromisso = campanhaColaboradorVO.getDataUltimaAgendaGerada();
			if (dataCompromisso.compareTo(maiorData) > 0) {
				maiorData = dataCompromisso;
			}
		}
		return maiorData;
	}

	public Date gerarAgendaDistribuindoProspectsPorConsultor(CampanhaVO campanha, ProgressBarVO progressBar, UsuarioVO usuario) throws Exception {
		Date maiorDataCompromisso = new Date();

		// A variavel consultorDistribuir indica qual o consultor deverá receber
		// o próximo prospect
		// No caso, ela está iniciado com 1, indicando que o primeiro da lista,
		// será
		// o primeiro a receber um prospect (agenda). Depois o sistema irá
		// automaticamente,
		// incrementar esta variavel, para distribuir o próximo prospect para o
		// segundo
		// consultor e assim por diante. Obviamente, que quando o último
		// consultor receber
		// um prospect, a mesma será novamente reiniciada para 1.
		campanha.setConsultorDistribuir(1);
		campanha.setNumeroProspectsDistribuidos(0);
		campanha.setMapaProspectsDistribuidos(new HashMap<String, Integer>());
		campanha.setMetaContatosASeremRealizadosPorDiaPorConsultor(getFacadeFactory().getCampanhaPublicoAlvoFacade().montarQuantidadeTotalContatosProspectsDia(campanha.getCodigo()));
		carregarDadosCompletosConsultoresCampanha(campanha, usuario);
		inicializarDataEHoraInicioGeracaoAgendaConsultores(campanha, usuario);
		// //System.out.println("======================== GERAR AGENDA =======================");
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanha.getListaCampanhaPublicoAlvo()) {
			distribuirProspectsCampanhaPublicoAlvoProspectVO(campanha, campanhaPublicoAlvoVO, progressBar, usuario);
		}
		maiorDataCompromisso = obterMaiorDataCompromissoAgendadoConsultores(campanha);
		// // System.out.println("----------------- FIM GERAR AGENDA -------------------------");
		// // System.out.println("----- PROSPECTS AGENDADOS: " +
		// campanha.getNumeroProspectsDistribuidos() + " -------");
		// // System.out.println("----- ÚLTIMO AGENDAMENTO: " +
		// maiorDataCompromisso + " -------");
		// // System.out.println("------------------------------------------------------------");
		return maiorDataCompromisso;
	}

	public void gerenciarListaCampanha(CampanhaVO campanha) throws Exception {

		for (int i = 0; i < campanha.getListaCampanhaPublicoAlvo().size(); i++) {
			CampanhaPublicoAlvoVO campanhaPublicoAlvo = campanha.getListaCampanhaPublicoAlvo().get(i);
			campanhaPublicoAlvo.getListaProspectsPorColaborador().clear();
			campanhaPublicoAlvo.setCampanha(campanha);
			List<ProspectsVO> listaProspects = getFacadeFactory().getCampanhaPublicoAlvoFacade().montarProspectsGeracaoAgenda(campanhaPublicoAlvo);
			Iterator<ProspectsVO> j = listaProspects.iterator();
			while (j.hasNext()) {
				ProspectsVO p = (ProspectsVO) j.next();
				if (campanhaPublicoAlvo.getListaProspectsPorColaborador().containsKey(p.getConsultorPadrao().getCodigo().intValue())) {
					List<ProspectsVO> lista = (List<ProspectsVO>) campanhaPublicoAlvo.getListaProspectsPorColaborador().get(p.getConsultorPadrao().getCodigo().intValue());
					lista.add(p);
				} else {
					List<ProspectsVO> lista = new ArrayList<ProspectsVO>();
					lista.add(p);
					campanhaPublicoAlvo.getListaProspectsPorColaborador().put(p.getConsultorPadrao().getCodigo(), lista);
				}
			}
		}
	}


	public void realizarAgendamentoPublicoAlvo(HashMap<String, Object> mapaResultados, CampanhaVO campanha, CampanhaColaboradorVO campanhaColaborador, AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CompromissoAgendaPessoaHorarioVO compromisso, Integer prospectsAgendados, Double qtdeDiasProspect, Double quantidadeProspectsSeremContactados, String ultimaHoraRegistrada, Integer prospectsAgendadosColaborador, Integer sequenciaProspects, Integer sequenciaPublicoAlvo, UsuarioVO usuario, Boolean considerarSabado, Boolean considerarFeriados, Date maiorDataCompromisso, Integer prospectsAgendadosDia, List<ProspectsVO> listaProspects) throws Exception {
		for (int i = sequenciaPublicoAlvo; i < campanha.getListaCampanhaPublicoAlvo().size(); i++) {
			CampanhaPublicoAlvoVO campanhaPublicoAlvo = campanha.getListaCampanhaPublicoAlvo().get(i);
			campanhaPublicoAlvo.setCampanha(campanha);
			// List<ProspectsVO> listaProspects =
			// getFacadeFactory().getCampanhaPublicoAlvoFacade().montarProspectsGeracaoAgenda(campanhaPublicoAlvo);
			if (prospectsAgendadosColaborador == quantidadeProspectsSeremContactados.intValue()) {
				break;
			} else if (prospectsAgendados.equals(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarSomaTotalProspects(campanha.getListaCampanhaPublicoAlvo()))) {
				break;
			}
			realizarAgendamentoProspects(mapaResultados, listaProspects, campanha, campanhaColaborador, usuario, agenda, agendaPessoaHorario, compromisso, prospectsAgendados, qtdeDiasProspect, quantidadeProspectsSeremContactados, prospectsAgendadosColaborador, sequenciaProspects, ultimaHoraRegistrada, considerarSabado, considerarFeriados, maiorDataCompromisso, prospectsAgendadosDia);
			prospectsAgendados = prospectsAgendados + ((Integer) mapaResultados.get("prospectsAgendadosTemp"));
			prospectsAgendadosColaborador = prospectsAgendadosColaborador + ((Integer) mapaResultados.get("prospectsAgendadosColaboradorTemp"));
			sequenciaProspects = ((Integer) mapaResultados.get("sequenciaProspects"));
			agendaPessoaHorario = ((AgendaPessoaHorarioVO) mapaResultados.get("agendaPessoaHorario"));
			if (prospectsAgendadosColaborador == quantidadeProspectsSeremContactados.intValue()) {
				break;
			} else if (prospectsAgendados.equals(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarSomaTotalProspects(campanha.getListaCampanhaPublicoAlvo()))) {
				break;
			} else if (sequenciaProspects == listaProspects.size()) {
				sequenciaProspects = 0;
			}
			sequenciaPublicoAlvo++;
		}
		mapaResultados.put("prospectsAgendados", prospectsAgendados);
		mapaResultados.put("sequenciaProspects", sequenciaProspects);
		mapaResultados.put("sequenciaPublicoAlvo", sequenciaPublicoAlvo);
	}

	public void realizarAgendamentoProspects(HashMap<String, Object> mapaResultados, List<ProspectsVO> listaProspects, CampanhaVO campanha, CampanhaColaboradorVO campanhaColaborador, UsuarioVO usuario, AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CompromissoAgendaPessoaHorarioVO compromisso, Integer prospectsAgendados, Double qtdeDiasProspect, Double quantidadeProspectsSeremContactados, Integer prospectsAgendadosColaborador, Integer sequenciaProspects, String ultimaHoraRegistrada, Boolean considerarSabado, Boolean considerarFeriados, Date maiorDataCompromisso, Integer prospectsAgendadosDia) throws Exception {
		Integer prospectsAgendadosTemp = 0;
		Integer prospectsAgendadosColaboradorTemp = 0;
		for (int i = sequenciaProspects; i < listaProspects.size(); i++) {
			compromisso = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarGeracaoCompromisso(agenda, agendaPessoaHorario, campanha, usuario, listaProspects.get(i), prospectsAgendados, ultimaHoraRegistrada, considerarSabado, considerarFeriados, campanhaColaborador.getHoraFinalGerarAgenda());
			if (compromisso.getDataCompromisso().after(maiorDataCompromisso)) {
				maiorDataCompromisso = compromisso.getDataCompromisso();
			}
			agendaPessoaHorario = compromisso.getAgendaPessoaHorario();
			if (compromisso.getHora().compareTo(campanhaColaborador.getHoraFinalGerarAgenda()) >= 0) {
				ultimaHoraRegistrada = campanha.getHoraInicial();
			} else {
				ultimaHoraRegistrada = compromisso.getHora();
			}
			prospectsAgendadosTemp = i - sequenciaProspects;
			prospectsAgendadosColaboradorTemp++;
			sequenciaProspects++;		
			if ((prospectsAgendadosColaborador + prospectsAgendadosColaboradorTemp) == quantidadeProspectsSeremContactados.intValue()) {
				break;
			}
		}
		mapaResultados.put("maiorDataCompromisso", maiorDataCompromisso);
		mapaResultados.put("prospectsAgendadosTemp", prospectsAgendadosTemp);
		mapaResultados.put("prospectsAgendadosColaboradorTemp", prospectsAgendadosColaboradorTemp);
		mapaResultados.put("sequenciaProspects", sequenciaProspects);
		mapaResultados.put("agendaPessoaHorario", agendaPessoaHorario);
	}

	public AgendaPessoaVO realizarValidacaoAgenda(CampanhaColaboradorVO campanhaColaborador, CampanhaVO campanha, UsuarioVO usuario) throws Exception {
		AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(campanhaColaborador.getFuncionarioCargoVO().getFuncionarioVO().getPessoa(), usuario);
		if (agenda.getCodigo() == 0) {
			agenda.setPessoa(campanhaColaborador.getFuncionarioCargoVO().getFuncionarioVO().getPessoa());
			getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuario);
		}
		return agenda;
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AgendaPessoaHorarioVO realizarValidacaoAgendaPessoaHorarioExiste(AgendaPessoaVO agenda, Date dataCompromisso, Boolean considerarSabado, Boolean considerarFeriados, UsuarioVO usuarioLogado) throws Exception {
        Date dataCompromissoValidada = getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(dataCompromisso, 0, considerarSabado, considerarFeriados, ConsiderarFeriadoEnum.NENHUM, usuarioLogado);
                
		AgendaPessoaHorarioVO agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorCodigoAgendaPessoa(0, agenda.getCodigo(), 0, dataCompromissoValidada, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		if (agendaPessoaHorario.getCodigo() == 0) {
			agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(dataCompromissoValidada), Uteis.getMesData(dataCompromissoValidada), Uteis.getAnoData(dataCompromissoValidada), Uteis.getDiaSemanaEnum(dataCompromissoValidada), true);
			agenda.getAgendaPessoaHorarioVOs().add(agendaPessoaHorario);
			getFacadeFactory().getAgendaPessoaHorarioFacade().incluirAgendaPessoaHorarios(agenda.getCodigo(), agenda.getAgendaPessoaHorarioVOs(), usuarioLogado);
		}
		return agendaPessoaHorario;
	}

	public List<SelectItem> consultarListaSelectItemCampanha(Integer unidadeEnsino, String situacao, Obrigatorio obrigatorio, Integer vendedor) throws Exception {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		if (obrigatorio.equals(Obrigatorio.NAO)) {
			selectItems.add(new SelectItem(0, ""));
		}
		StringBuilder sb = new StringBuilder("");
		SqlRowSet rs = null;
		try {
			sb.append("Select codigo, descricao from campanha ");

			sb.append(" where periodoInicio <= current_date ");
			if (situacao != null && !situacao.trim().isEmpty()) {
				sb.append(" and situacao = '").append(situacao).append("' ");
			}
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sb.append(" and unidadeensino = ").append(unidadeEnsino);
			}
			if (vendedor != null && vendedor > 0) {
				sb.append(" and ").append(vendedor).append(" in ((select responsavel from interacaoworkflow where campanha.codigo = interacaoworkflow.campanha and  responsavel =  ").append(vendedor).append(" limit 1) ");
				sb.append(" union ");
				sb.append(" select usuario.codigo from campanhacolaborador  ");
				sb.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo");
				sb.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
				sb.append(" inner join usuario on usuario.pessoa = funcionario.pessoa");
				sb.append(" where campanhacolaborador.campanha = campanha.codigo and usuario.codigo = ").append(vendedor).append(")");
			}

			sb.append(" order by periodoInicio desc ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (rs.next()) {
				selectItems.add(new SelectItem(rs.getInt("codigo"), rs.getString("descricao")));
			}
			return selectItems;
		} catch (Exception e) {
			throw e;
		} finally {
			obrigatorio = null;
			unidadeEnsino = null;
			situacao = null;
			rs = null;
			sb = null;
		}
	}

	/**
	 * 
	 * @param campanha
	 * @param campanhaColaboradorVO
	 * @param listaCampanhaColaboradorAlterarCompromisso
	 * @param dataIncial
	 * @param dataFinal
	 * @param tipoAlteracaoColaborador
	 * @param usuarioLogado
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Date persistirAlteracaoCompromissoPorCampanha(CampanhaVO campanha, CampanhaColaboradorVO campanhaColaboradorVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorAlterarCompromisso, Date dataIncial, Date dataFinal, String tipoAlteracaoColaborador, Date dataNovoCompromisso, String horaNovoCompromisso, Boolean considerarSabado, Boolean considerarFeriados, UsuarioVO usuarioLogado) throws Exception {

		HashMap<String, Object> mapaResultados = new HashMap<String, Object>();
		Date maiorDataCompromisso = new Date();
		if (dataNovoCompromisso.before(campanha.getPeriodoInicio())) {
			throw new ConsistirException("O campo descrição deve ser informado.");
		}
		realizarValidacaoAlteracaoCompromissoPorCampanha(campanha, campanhaColaboradorVO, listaCampanhaColaboradorAlterarCompromisso, dataIncial, dataFinal, tipoAlteracaoColaborador, dataNovoCompromisso, horaNovoCompromisso);
		campanha.setHoraInicial(horaNovoCompromisso);
		// listar os compromisso
		List<CompromissoAgendaPessoaHorarioVO> listaCompromisso = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorColaborador(campanhaColaboradorVO, dataIncial, dataFinal, false, usuarioLogado);
		List<ProspectsVO> listaProspectsVOs = new ArrayList<ProspectsVO>();
		for (CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO : listaCompromisso) {
			listaProspectsVOs.add(compromissoAgendaPessoaHorarioVO.getProspect());
		}
		// distribuicao novos participantes
		if (tipoAlteracaoColaborador.equals("SelecionarParticipante")) {
			// criar os novos compromisso para esses participantes
			realizarAgendamentoAlteracaoColaborador(mapaResultados, listaCampanhaColaboradorAlterarCompromisso, listaProspectsVOs, campanha, dataNovoCompromisso, usuarioLogado, considerarSabado, considerarFeriados, maiorDataCompromisso);
			// adicionar os novos participantes a campanha
			realizarAdicionarNovosParticipantesNaCampanha(campanha, listaCampanhaColaboradorAlterarCompromisso);

		} else {
			// distriuicao entres os participantes
			campanha.getListaCampanhaColaborador().remove(campanhaColaboradorVO);
			realizarAgendamentoAlteracaoColaborador(mapaResultados, campanha.getListaCampanhaColaborador(), listaProspectsVOs, campanha, dataNovoCompromisso, usuarioLogado, considerarSabado, considerarFeriados, maiorDataCompromisso);
			// criar os novos compromisso para esses participantes
		}
		// validar se o campanhaColaboradorVO sera removido da campanha
		if (campanhaColaboradorVO.getQtdCompromissoCampanha().equals(campanhaColaboradorVO.getQtdCompromissoPeriodo())) {
			// getFacadeFactory().getCampanhaColaboradorFacade().excluirObjCampanhaColaborador(campanha.getListaCampanhaColaborador(),
			// campanha,
			// campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
			getFacadeFactory().getCampanhaColaboradorFacade().excluir(campanhaColaboradorVO, campanha.getListaCampanhaColaborador());
		}

		// validar se o campanhaColaboradorVO sera removido da campanha
		String listaCodigoCompromisso = Uteis.realizarMontagemDeValoresSeparadosPorVirgula(listaCompromisso, "getCodigo");
		if (!listaCodigoCompromisso.isEmpty()) {
			String sqlUpdate = "update interacaoworkflow set CompromissoAgendaPessoaHorario = null where CompromissoAgendaPessoaHorario in (" + listaCodigoCompromisso + ")";
			getConexao().getJdbcTemplate().update(sqlUpdate);
			String sql = "DELETE FROM CompromissoAgendaPessoaHorario WHERE codigo in (" + listaCodigoCompromisso + ")";
			getConexao().getJdbcTemplate().update(sql);
		}
		return (Date) mapaResultados.get("maiorDataCompromisso");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAgendamentoAlteracaoColaborador(HashMap<String, Object> mapaResultados, List<CampanhaColaboradorVO> listaCampanhaColaboradorAlterarCompromisso, List<ProspectsVO> listaProspectsVOs, CampanhaVO campanha, Date dataIncial, UsuarioVO usuario, Boolean considerarSabado, Boolean considerarFeriados, Date maiorDataCompromisso) throws Exception {

		Integer prospectsAgendados = 0;
		Integer prospectsAgendadosDia = 0;
		Integer prospectsAgendadosColaborador = 0;
		Integer sequenciaProspects = 0;
		String ultimaHoraRegistrada = "";
		AgendaPessoaVO agenda = new AgendaPessoaVO();
		AgendaPessoaHorarioVO agendaPessoaHorario = new AgendaPessoaHorarioVO();
		CompromissoAgendaPessoaHorarioVO compromisso = new CompromissoAgendaPessoaHorarioVO();
		Double qtdeDiasProspect = 0.0;
		Double quantidadeProspectsSeremContactados = 0.0;

		for (CampanhaColaboradorVO campanhaColaborador : listaCampanhaColaboradorAlterarCompromisso) {
			qtdeDiasProspect = qtdeDiasProspect + campanhaColaborador.getQtdContato();
		}
		for (CampanhaColaboradorVO campanhaColaborador : listaCampanhaColaboradorAlterarCompromisso) {
			ultimaHoraRegistrada = "";
			prospectsAgendadosColaborador = 0;
			agenda = realizarValidacaoAgenda(campanhaColaborador, campanha, usuario);
			agendaPessoaHorario = realizarValidacaoAgendaPessoaHorarioExiste(agenda, dataIncial, considerarSabado, considerarFeriados, usuario);
			if (campanhaColaborador.getQtdContato() > 0 && qtdeDiasProspect > 0) {
				quantidadeProspectsSeremContactados = campanhaColaborador.getQtdContato() / qtdeDiasProspect;
			}
			quantidadeProspectsSeremContactados = quantidadeProspectsSeremContactados * listaProspectsVOs.size();
			quantidadeProspectsSeremContactados = Uteis.arredondar(quantidadeProspectsSeremContactados, 0, 0);

			realizarAgendamentoAlteracaoCompromissoPublicoAlvo(mapaResultados, listaProspectsVOs, campanha, campanhaColaborador, agenda, agendaPessoaHorario, compromisso, prospectsAgendados, qtdeDiasProspect, quantidadeProspectsSeremContactados, ultimaHoraRegistrada, prospectsAgendadosColaborador, sequenciaProspects, usuario, considerarSabado, considerarFeriados, maiorDataCompromisso, prospectsAgendadosDia);
			prospectsAgendados = ((Integer) mapaResultados.get("prospectsAgendados"));
			sequenciaProspects = ((Integer) mapaResultados.get("sequenciaProspects"));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAgendamentoAlteracaoCompromissoPublicoAlvo(HashMap<String, Object> mapaResultados, List<ProspectsVO> listaProspectsVOs, CampanhaVO campanha, CampanhaColaboradorVO campanhaColaborador, AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CompromissoAgendaPessoaHorarioVO compromisso, Integer prospectsAgendados, Double qtdeDiasProspect, Double quantidadeProspectsSeremContactados, String ultimaHoraRegistrada, Integer prospectsAgendadosColaborador, Integer sequenciaProspects, UsuarioVO usuario, Boolean considerarSabado, Boolean considerarFeriados, Date maiorDataCompromisso, Integer prospectsAgendadosDia) throws Exception {

		if (prospectsAgendadosColaborador == quantidadeProspectsSeremContactados.intValue()) {
			return;
		} else if (prospectsAgendados == listaProspectsVOs.size()) {
			return;
		}
		realizarAgendamentoProspects(mapaResultados, listaProspectsVOs, campanha, campanhaColaborador, usuario, agenda, agendaPessoaHorario, compromisso, prospectsAgendados, qtdeDiasProspect, quantidadeProspectsSeremContactados, prospectsAgendadosColaborador, sequenciaProspects, ultimaHoraRegistrada, considerarSabado, considerarFeriados, maiorDataCompromisso, prospectsAgendadosDia);
		
		prospectsAgendados = prospectsAgendados + ((Integer) mapaResultados.get("prospectsAgendadosTemp"));
		prospectsAgendadosColaborador = prospectsAgendadosColaborador + ((Integer) mapaResultados.get("prospectsAgendadosColaboradorTemp"));
		sequenciaProspects = ((Integer) mapaResultados.get("sequenciaProspects"));
		agendaPessoaHorario = ((AgendaPessoaHorarioVO) mapaResultados.get("agendaPessoaHorario"));

		mapaResultados.put("prospectsAgendados", prospectsAgendados);
		mapaResultados.put("sequenciaProspects", sequenciaProspects);
		if (prospectsAgendadosColaborador == quantidadeProspectsSeremContactados.intValue()) {
			return;
		} else if (prospectsAgendados == listaProspectsVOs.size()) {
			return;
		} else if (sequenciaProspects == listaProspectsVOs.size()) {
			sequenciaProspects = 0;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAdicionarNovosParticipantesNaCampanha(CampanhaVO campanha, List<CampanhaColaboradorVO> listaCampanhaColaboradorAlterarCompromisso) throws Exception {
		for (CampanhaColaboradorVO novoColaborador : listaCampanhaColaboradorAlterarCompromisso) {
			boolean existeColaborador = false;
			for (CampanhaColaboradorVO colaboradorExistente : campanha.getListaCampanhaColaborador()) {
				if (novoColaborador.getFuncionarioCargoVO().getCodigo().equals(colaboradorExistente.getFuncionarioCargoVO().getCodigo())) {
					existeColaborador = true;
					break;
				}
			}
			if (!existeColaborador) {
				novoColaborador.setCampanha(campanha);
				getFacadeFactory().getCampanhaColaboradorFacade().incluir(novoColaborador);
				campanha.getListaCampanhaColaborador().add(novoColaborador);
			}
		}
	}

	public void realizarValidacaoAlteracaoCompromissoPorCampanha(CampanhaVO campanha, CampanhaColaboradorVO campanhaColaboradorVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorAlterarCompromisso, Date dataIncial, Date dataFinal, String tipoAlteracaoColaborador, Date dataNovoCompromisso, String horaNovoCompromisso) throws Exception {
		if (dataIncial == null) {
			throw new Exception("O campo Período Alteração deve ser informado.");
		}
		if (dataFinal == null) {
			throw new Exception("O campo Até deve ser informado.");
		}
		if (dataNovoCompromisso == null) {
			throw new Exception("O campo Data Compromisso deve ser informado.");
		}
		if (dataNovoCompromisso.before(dataIncial) || dataNovoCompromisso.after(dataFinal)) {
			throw new Exception("O campo Data Compromisso tem que estar entre o Período Alteração.");
		}
		if (horaNovoCompromisso == null || horaNovoCompromisso.isEmpty()) {
			throw new Exception("O campo Hora deve ser informado.");
		}
		if (tipoAlteracaoColaborador == null || tipoAlteracaoColaborador.isEmpty()) {
			throw new Exception("O campo Tipo Alteração Compromisso deve ser informado.");
		}
		if ((Uteis.isAtributoPreenchido(horaNovoCompromisso) && (Integer.parseInt(horaNovoCompromisso.substring(0, 2)) > Integer.parseInt(campanhaColaboradorVO.getHoraFinalGerarAgenda().substring(0, 2)))) || (Integer.parseInt(horaNovoCompromisso.substring(3, 5)) > 59)) {
			throw new ConsistirException("Não é possível gravar um compromisso depois das " + campanhaColaboradorVO.getHoraFinalGerarAgenda() + " horas para " + campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
		}
		if ((Integer.parseInt(horaNovoCompromisso.substring(0, 2)) < Integer.parseInt(campanhaColaboradorVO.getHoraInicioGerarAgenda().substring(0, 2)))) {
			throw new ConsistirException("Não é possível gravar um compromisso antes das " + campanhaColaboradorVO.getHoraInicioGerarAgenda() + " horas para " + campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
		}
		if ((Uteis.isAtributoPreenchido(campanhaColaboradorVO.getHoraInicioIntervalo()) && campanhaColaboradorVO.getHoraInicioIntervalo().length() > 2)
			&& (Uteis.isAtributoPreenchido(campanhaColaboradorVO.getHoraFimIntervalo()) && campanhaColaboradorVO.getHoraFimIntervalo().length() > 2)
			&& (Integer.parseInt(horaNovoCompromisso.substring(0, 2)) >= Integer.parseInt(campanhaColaboradorVO.getHoraInicioIntervalo().substring(0, 2))) && (Integer.parseInt(horaNovoCompromisso.substring(0, 2)) < Integer.parseInt(campanhaColaboradorVO.getHoraFimIntervalo().substring(0, 2)))) {
			throw new ConsistirException("Não é possível gravar um compromisso entre " + campanhaColaboradorVO.getHoraInicioIntervalo() + " e " + campanhaColaboradorVO.getHoraFimIntervalo() + " para " + campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome());
		}
		if ((Integer.parseInt(horaNovoCompromisso.substring(0, 2)) >= 12) && Uteis.getDiaSemanaEnum(dataNovoCompromisso).equals(DiaSemana.SABADO)) {
			throw new ConsistirException("Não é possível gravar um compromisso no sábado após o meio dia");
		}

		if (dataIncial.before(campanha.getPeriodoInicio())) {
			throw new Exception("O campo Período não pode ser menor que a data inicial da campanha (" + Uteis.getData(campanha.getPeriodoInicio()) + ") .");
		}
		if (tipoAlteracaoColaborador.equals("SelecionarParticipante") && listaCampanhaColaboradorAlterarCompromisso.isEmpty()) {
			throw new Exception("Deve ser informado pelo menos um participante.");
		}
	}

	public void realizarValidacaoSeExisteCampanhaPreInscricaoParaUnidadeEnsino(CampanhaVO campanhaVO, boolean incluir) throws Exception {
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.PRE_INSCRICAO)) {
			String sql = "";
			if (incluir) {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + " and situacao = 'AT' and tipoCampanha = '" + TipoCampanhaEnum.PRE_INSCRICAO + "' ";
			} else {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + "  and situacao = 'AT' and codigo <> " + campanhaVO.getCodigo() + " and tipoCampanha = '" + TipoCampanhaEnum.PRE_INSCRICAO + "'";
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			sql = null;
			if (tabelaResultado.next()) {
				throw new ConsistirException("Já existe um campanha do tipo pré-inscrição cadastro para essa unidade de ensino.");
			}
		}
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.LIGACAO_RECEPTIVA)) {
			String sql = "";
			if (incluir) {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + " and situacao = 'AT' and tipoCampanha = '" + TipoCampanhaEnum.LIGACAO_RECEPTIVA + "' ";
			} else {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + "  and situacao = 'AT' and codigo <> " + campanhaVO.getCodigo() + " and tipoCampanha = '" + TipoCampanhaEnum.LIGACAO_RECEPTIVA + "'";
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			sql = null;
			if (tabelaResultado.next()) {
				throw new ConsistirException("Já existe um campanha do tipo Ligação Receptiva cadastro para essa unidade de ensino.");
			}
		}
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA)) {
			String sql = "";
			if (incluir) {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + " and situacao = 'AT' and tipoCampanha = '" + TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA + "' ";
			} else {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + "  and situacao = 'AT' and codigo <> " + campanhaVO.getCodigo() + " and tipoCampanha = '" + TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA + "'";
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			sql = null;
			if (tabelaResultado.next()) {				
				throw new ConsistirException("Já existe um campanha do tipo Contactar Prospects Existentes Sem Agenda cadastro para essa unidade de ensino.");
			}
		}
		if (campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA)) {
			String sql = "";
			if (incluir) {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + " and situacao = 'AT' and tipoCampanha = '" + TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA + "' ";
			} else {
				sql = "SELECT * FROM campanha WHERE unidadeEnsino = " + campanhaVO.getUnidadeEnsino().getCodigo().intValue() + "  and situacao = 'AT' and codigo <> " + campanhaVO.getCodigo() + " and tipoCampanha = '" + TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA + "'";
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			sql = null;
			if (tabelaResultado.next()) {
				throw new ConsistirException("Já existe um campanha do tipo Contactar Prospects Existentes Sem Agenda - Cobrança cadastro para essa unidade de ensino.");
			}
		}
	}

	public List<CampanhaVO> consultarCampanhaRecorrente() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select codigo from campanha WHERE campanha.recorrente = 'true' and campanha.situacao = 'AT' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CampanhaVO> vetResultado = new ArrayList<CampanhaVO>(0);
		while (tabelaResultado.next()) {
			CampanhaVO obj = new CampanhaVO();
			obj = this.consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			vetResultado.add(obj);
		}
		tabelaResultado = null;
		return vetResultado;
	}

	@Override
	public void executarCriacaoCampanhaRecorrenteCriacaoAgenda() {
		try {

			List<CampanhaVO> lista = new ArrayList<CampanhaVO>(0);
			lista.addAll(consultarCampanhaRecorrente());
			UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			for (CampanhaVO obj : lista) {
				try {
					if (validarTipoRecorrenciaCampanha(obj)) {
						getFacadeFactory().getCampanhaPublicoAlvoFacade().adicionarObjCampanhaPublicoAlvoVOs(obj, obj.getListaCampanhaPublicoAlvo().get(0), Boolean.TRUE, new ArrayList<SegmentacaoProspectVO>(0));
						gerarAgendaDistribuindoProspectsPorConsultor(obj, new ProgressBarVO(), usuResp);
						alterarDataRecorrenciaCampanha(obj.getCodigo());
					}
				} catch (Exception e) {
					////System.out.println("A Campanha recorrente de código" + obj.getCodigo() + " e de descrição " + obj.getDescricao() + " não pode ser gerada pela trhead pelo seguinte erro:" + e.getMessage());
				}
			}
		} catch (Exception e) {
			// System.out.println("Não foi possível executar a thread de Geração de Campanha/Agenda Recorrente pelo seguinte motivo " + e.getMessage() + " no dia " + Uteis.getDataAtual());
		}
	}

	private Boolean validarTipoRecorrenciaCampanha(CampanhaVO obj) throws Exception {
		try {
			if (obj.getTipoRecorrencia().equals("DI")) {
				return Boolean.TRUE;
			}
			if (obj.getTipoRecorrencia().equals("SE")) {
				return verificarDataCriacaoCampanhaRecorrenteSemanal(obj);
			}
			if (obj.getTipoRecorrencia().equals("QI")) {
				return verificarDataCriacaoCampanhaRecorrenteQuinzenal(obj);
			}
			if (obj.getTipoRecorrencia().equals("ME")) {
				return verificarDataCriacaoCampanhaRecorrenteMensal(obj);
			}
			if (obj.getTipoRecorrencia().equals("BI")) {
				return verificarDataCriacaoCampanhaRecorrenteBimestral(obj);
			}
			if (obj.getTipoRecorrencia().equals("TR")) {
				return verificarDataCriacaoCampanhaRecorrenteTrimestral(obj);
			}
			if (obj.getTipoRecorrencia().equals("SM")) {
				return verificarDataCriacaoCampanhaRecorrenteSemestral(obj);
			}
			if (obj.getTipoRecorrencia().equals("AN")) {
				return verificarDataCriacaoCampanhaRecorrenteAnual(obj);
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteSemanal(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 7), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteQuinzenal(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 15), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteMensal(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 30), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteBimestral(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 60), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteTrimestral(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 90), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteSemestral(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 120), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean verificarDataCriacaoCampanhaRecorrenteAnual(CampanhaVO obj) throws Exception {
		if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(Uteis.obterDataFutura(obj.getDataRecorrencia(), 365), new Date())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataRecorrenciaCampanha(Integer campanha) throws Exception {
		try {
			String str = "UPDATE campanha SET dataRecorrencia=? where codigo = ?";
			getConexao().getJdbcTemplate().update(str, new Object[] { new Date(), campanha });
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void finalizarAgendaCompromissoContaReceber(NegociacaoRecebimentoVO negociacao) throws Exception{
		try {
			for(ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimento : negociacao.getContaReceberNegociacaoRecebimentoVOs()){
				atualizarAgendaCompromissoHorarioBaixaContaReceber(contaReceberNegociacaoRecebimento.getContaReceber());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void atualizarAgendaCompromissoHorarioBaixaContaReceber(ContaReceberVO contaReceber) throws Exception{
		try {
			if(validarContasVecidas(contaReceber)){
				List<CompromissoAgendaPessoaHorarioVO> lista = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
				StringBuilder sql = new StringBuilder();
				sql.append(" SELECT compromissoagendapessoahorario.codigo from compromissoagendapessoahorario ");
				sql.append(" inner join prospects on prospects.codigo = compromissoagendapessoahorario.prospect");
				sql.append(" inner join campanha on compromissoagendapessoahorario.campanha = campanha.codigo");
				sql.append(" inner join pessoa on pessoa.codigo = prospects.pessoa");
				sql.append(" inner join contareceber on contareceber.pessoa = pessoa.codigo ");
				sql.append(" where campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA' ");
				sql.append(" and contareceber.responsavelfinanceiro is null ");
				sql.append(" and compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO'  ");
				sql.append(" and contareceber.codigo = ").append(contaReceber.getCodigo());
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				while (tabelaResultado.next()) {
					CompromissoAgendaPessoaHorarioVO compromisso  = new CompromissoAgendaPessoaHorarioVO();
					compromisso.setCodigo(tabelaResultado.getInt("codigo"));
					lista.add(compromisso);
				}
				for(CompromissoAgendaPessoaHorarioVO obj : lista){
					String str = "UPDATE compromissoagendapessoahorario SET tiposituacaocompromissoenum=? where codigo = ?";
					getConexao().getJdbcTemplate().update(str, new Object[] { TipoSituacaoCompromissoEnum.REALIZADO.toString(), obj.getCodigo() });
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Boolean validarContasVecidas(ContaReceberVO contaReceber) {
		StringBuilder sql = new StringBuilder();
		Integer contasVencidas = 0;
		sql.append(" select count(codigo) from contareceber ");
		sql.append(" where pessoa = ").append(contaReceber.getPessoa().getCodigo());
		sql.append(" and datavencimento < current_date ");
		sql.append(" and situacao = 'AR' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			contasVencidas=tabelaResultado.getInt("count");	
		}
		if(contasVencidas > 0){
			return Boolean.FALSE;
		}else {
			return Boolean.TRUE;
		}
	}
	@Override
	public CampanhaVO consultarCampanhaPorCodigoCompromisso(Integer codigoCompromisso,int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception{
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM campanha ");
			sql.append(" INNER JOIN compromissoagendapessoahorario on compromissoagendapessoahorario.campanha = campanha.codigo ");
			sql.append(" WHERE compromissoagendapessoahorario.codigo =");
			sql.append(codigoCompromisso);
			sql.append("");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			CampanhaVO obj = new CampanhaVO();
			if(tabelaResultado.next()){
				obj = montarDados(tabelaResultado, nivelMontarDados, usuarioLogado);
			}
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}
	
		
	public static void montarDadosMeta(CampanhaVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getMeta().getCodigo().intValue() == 0) {
			return;
		}
		obj.setMeta(getFacadeFactory().getMetaFacade().consultarPorChavePrimaria(obj.getMeta().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
	}
	
	public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String tipoCampanha, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE upper(campanha.descricao) ");
		sqlStr.append("like('").append(valorConsulta.toUpperCase()).append("%')");
		if ((!situacao.equals("")) && (!situacao.equals("TO"))) {
			if (situacao.equals("AC")) { // situacao especial criada para
											// definir que devem ser listadas as
											// campanhas ativas e em construção
											// - facilitar na busca do usuário
				sqlStr.append(" and ((situacao = 'AT') or (situacao = 'EC'))");
			} else {
				sqlStr.append(" and situacao = '").append(situacao).append("'");
			}
		}
		if (unidadeensino != null) {
			if (unidadeensino != 0) {
				sqlStr.append(" and campanha.unidadeensino = ").append(unidadeensino).append("");
			}
		}
		
		if (tipoCampanha != null) {
                    sqlStr.append(" and campanha.tipocampanha = '").append(tipoCampanha).append("' ");
		} 
                sqlStr.append(" ORDER BY campanha.descricao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public Date realizarCarregamentoAgendaDistribuindoProspectsPorConsultor(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, Boolean alterarConsultorPadraoProspect, UsuarioVO usuario, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception {
		validarCampanhaGerarAgenda(campanha);
		// A variavel consultorDistribuir indica qual o consultor deverá receber
		// o próximo prospect
		// No caso, ela está iniciado com 1, indicando que o primeiro da lista,
		// será
		// o primeiro a receber um prospect (agenda). Depois o sistema irá
		// automaticamente,
		// incrementar esta variavel, para distribuir o próximo prospect para o
		// segundo
		// consultor e assim por diante. Obviamente, que quando o último
		// consultor receber
		// um prospect, a mesma será novamente reiniciada para 1.
		campanha.setConsultorDistribuir(1);
		campanha.setNumeroProspectsDistribuidos(0);
		campanha.setMapaProspectsDistribuidos(new HashMap<String, Integer>());
		campanha.setMetaContatosASeremRealizadosPorDiaPorConsultor(getFacadeFactory().getCampanhaPublicoAlvoFacade().montarQuantidadeTotalContatosProspectsDia(campanha.getCodigo()));
		// carregarDadosCompletosConsultoresCampanha(campanha, usuario);
		inicializarDataEHoraInicioGeracaoAgendaConsultores(campanha, usuario);
		if (Uteis.isAtributoPreenchido(campanhaPublicoAlvoVO.getRegistroEntrada().getFuncionarioCargoSugerido())) {
			Optional<CampanhaColaboradorVO> findFirst = campanha.getListaCampanhaColaborador().stream().filter(p -> p.getFuncionarioCargoVO().getCodigo().equals(campanhaPublicoAlvoVO.getRegistroEntrada().getFuncionarioCargoSugerido().getCodigo())).findFirst();
			if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getFuncionarioCargoVO())) {
				campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().clear();
				for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
					realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, findFirst.get(), alterarConsultorPadraoProspect, usuario);
				}
			}
		} else {
			realizarDistribuicaoProspectsCampanhaPublicoAlvoProspectVO(campanha, campanhaPublicoAlvoVO, progressBar, alterarConsultorPadraoProspect, usuario, alterarConsultorCompromissoProspectJaIniciado);
		}
		Date maiorDataCompromisso = new Date();
		maiorDataCompromisso = obterMaiorDataCompromissoAgendadoConsultores(campanha);
		return maiorDataCompromisso;
	}
	
	public Date realizarDistribuicaoProspectsCampanhaPublicoAlvoProspectVO(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar,Boolean alterarConsultorPadraoProspect, UsuarioVO usuario, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception {
		campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().clear();		
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
			progressBar.incrementar();
			Boolean compromissoProspectJaIniciadoAgendaCampanha = false;
			Boolean prospectSemConsultorAgendaCampanha = !campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(0) ? false : true;
			if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
				if (!verificarProspectEstaNaListaConsultorSelecionadoRedistribuicao(campanhaPublicoAlvoVO.getMapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs(), campanhaPublicoAlvoProspectVO.getProspect().getCodigo())) {
					continue;
				}
				if (!alterarConsultorCompromissoProspectJaIniciado && !campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
						&& !campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA)) {
					compromissoProspectJaIniciadoAgendaCampanha = true;
					CampanhaColaboradorVO consultorIniciouAgendaVO =  getFacadeFactory().getCampanhaColaboradorFacade().obterConsultorIniciouCampanhaProspect(campanha.getListaCampanhaColaborador(), campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), usuario);
					if (consultorIniciouAgendaVO != null && !consultorIniciouAgendaVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(0)) {
						campanhaPublicoAlvoProspectVO.setConsultorDistribuicaoVO(consultorIniciouAgendaVO.getFuncionarioCargoVO().getFuncionarioVO());
					}
				}
			}
			
			if ((!verificarAgendaProspectJaFoiGerada(campanha, campanhaPublicoAlvoProspectVO) && !campanha.getRealizandoRedistribuicaoProspectAgenda())
					|| (campanha.getRealizandoRedistribuicaoProspectAgenda() && !compromissoProspectJaIniciadoAgendaCampanha 
					&& (campanha.getPoliticaRedistribuicaoProspectAgenda().equals(PoliticaRedistribuicaoProspectAgendaEnum.TODOS) ? true : campanhaPublicoAlvoProspectVO.getPoliticaRedistribuicaoProspectAgenda().equals(campanha.getPoliticaRedistribuicaoProspectAgenda())))
					|| (campanha.getRealizandoRedistribuicaoProspectAgenda() && prospectSemConsultorAgendaCampanha)) {
				// Só itemos processar um prospect, caso não tenha sido gerada
				// nenhuma agenda para o mesmo
				// Temos que controlar isto, pois um prospect pode ser
				// selecionado mais de uma vez em função
				// de mais de um curso de interesse ou situação em diferentes
				// publico Alvo
				
//				if (verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoProspectVO, usuario)) {

//					if ((campanha.getGerarAgendaConsultorRespProspect().booleanValue()) && (campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo() > 0) && (verificarConsultorPadraoEstaNaListaConsultoresDefinidosParaCampanha(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao()))) {
					if (campanhaPublicoAlvoVO.getTipoGerarAgendaCampanha().equals(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_RESPONSAVEL)) {
						// Quando a opção GerarAgendaConsultorRespProspect está
						// marcada, significa que o prospect e/ou pessoa
						// deverá ser gerenciada pelo seu consultor responsável.
						// Portanto, a distribuição
						// do prospect, deve privilegiar o consultor
						// responsavel, caso ele exista. Caso não exista uma
						// consultor
						// responsável definido para o prospect ou este
						// consultor não esteja selecionado para a campanha,
						// então o sistema irá seguir a política de distribuição
						// e gerar a agenda para o consultor apropriado -
						// consultorDistribuir.
						// Caso seja gerada a agenda para o
						// ConsultorResponsavel, não precisamos avancar no
						// rotacionamento
						// dos consultores, pois não seguimos o fluxo
						// convencional de distribuição
						CampanhaColaboradorVO campanhaColaboradorVO = null;
						if (campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo() > 0
							&& verificarConsultorPadraoEstaNaListaConsultoresDefinidosParaCampanha(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao()) ) {
							
							campanhaColaboradorVO = obterConsultorDistribuicaoParaGerarAgenda(campanha, campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo());
							realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, alterarConsultorPadraoProspect, usuario);
						} else {
							campanhaColaboradorVO = obterConsultorDistribuicaoRotacionadaParaGerarAgenda(campanha, campanha.getConsultorDistribuir());
							realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, alterarConsultorPadraoProspect, usuario);
							avancarRotacionamentoConsultorCampanha(campanha);
						}
					} else if (campanhaPublicoAlvoVO.getTipoGerarAgendaCampanha().equals(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA)) {
						CampanhaColaboradorVO campanhaColaboradorVO = obterConsultorDistribuicaoRotacionadaParaGerarAgenda(campanha, campanha.getConsultorDistribuir());
						realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, alterarConsultorPadraoProspect, usuario);
						avancarRotacionamentoConsultorCampanha(campanha);
						
//						System.out.println(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa().getNome() + " - Prospect: "+ campanhaPublicoAlvoProspectVO.getProspect().getNome());
					} else if (campanhaPublicoAlvoVO.getTipoGerarAgendaCampanha().equals(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_ULTIMA_INTERACAO)) {
						CampanhaColaboradorVO campanhaColaboradorVO = obterConsultorPriorizandoConsultorUltimaInteracao(campanha, campanhaPublicoAlvoProspectVO.getPessoa().getCodigo(), usuario);
						if (campanhaColaboradorVO != null) {
							realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, alterarConsultorPadraoProspect, usuario);
						} else {
							campanhaColaboradorVO = obterConsultorDistribuicaoRotacionadaParaGerarAgenda(campanha, campanha.getConsultorDistribuir());
							realizarVinculoProspectConsultor(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoVO.getMapConsultorVOs(), campanhaPublicoAlvoProspectVO, campanhaColaboradorVO, alterarConsultorPadraoProspect, usuario);
							avancarRotacionamentoConsultorCampanha(campanha);
						}
					}
					realizarCriacaoDadosEstatisticosPublicoAlvo(campanha, campanhaPublicoAlvoVO, campanhaPublicoAlvoProspectVO, usuario);
//				}
				if (campanha.getRealizandoRedistribuicaoProspectAgenda()) {
					campanhaPublicoAlvoProspectVO.setTipoDistribuicaoProspectCampanhaPublicoAlvo(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR);
				}
			}
//			progressBar.setProgresso(progressBar.getProgresso() + 1);
//			progressBar.setStatus("Carregando dados do Prospect ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");	
		}
		return new Date();
	}
	
	public Boolean verificarProspectEstaNaListaConsultorSelecionadoRedistribuicao(HashMap<Integer, CampanhaPublicoAlvoProspectVO> mapCampanhaPublicoAlvoProspectVOs, Integer codigoProspect) {
		if (mapCampanhaPublicoAlvoProspectVOs.containsKey(codigoProspect)) {
			return true;
		}
		return false;
	}
	
	public void realizarCriacaoDadosEstatisticosPublicoAlvo(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, UsuarioVO usuarioVO) {
		if (campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso().compareTo(campanhaVO.getPeriodoFim()) > 0) {
			campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().add(campanhaPublicoAlvoProspectVO);
		}
	}
	
	public void realizarVinculoProspectConsultor(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, HashMap<Integer, FuncionarioVO> mapConsultorUtilizarVOs, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, CampanhaColaboradorVO campanhaColaboradorVO, Boolean alterarConsultorPadraoProspect, UsuarioVO usuario) throws Exception {
		campanha.setNumeroProspectsDistribuidos(campanha.getNumeroProspectsDistribuidos() + 1);
		Integer codigoPessoaProspectControlarAgendaJaGerada = campanhaPublicoAlvoProspectVO.getCodigoProspectPessoa();
		String tipoProspectPessoa = "Aluno";
		if ((!campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0))) {
			getFacadeFactory().getProspectsFacade().carregarDados(campanhaPublicoAlvoProspectVO.getProspect(), usuario);

			tipoProspectPessoa = "Prospect";
		}
		campanha.getMapaProspectsDistribuidos().put(tipoProspectPessoa + "_" + codigoPessoaProspectControlarAgendaJaGerada, campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
		
		if (!mapConsultorUtilizarVOs.containsKey(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo())) {
			FuncionarioVO consultorDistribuicaoVO = inicializarDadosConsultorDistribuicao(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO());
			mapConsultorUtilizarVOs.put(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo(), consultorDistribuicaoVO);
			consultorDistribuicaoVO.setDataPrimeiroCompromisso(campanhaColaboradorVO.getDataGeracaoProximaAgenda());
			consultorDistribuicaoVO.setDataUltimoCompromisso(campanhaColaboradorVO.getDataGeracaoProximaAgenda());
			campanhaPublicoAlvoProspectVO.setConsultorDistribuicaoVO(consultorDistribuicaoVO);
		} else {
			FuncionarioVO consultorDistribuicaoVO = mapConsultorUtilizarVOs.get(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo());
			consultorDistribuicaoVO.setDataUltimoCompromisso(campanhaColaboradorVO.getDataGeracaoProximaAgenda());
			campanhaPublicoAlvoProspectVO.setConsultorDistribuicaoVO(consultorDistribuicaoVO);
		}

		if ((campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0)) && (!campanhaPublicoAlvoProspectVO.getPessoa().getCodigo().equals(0))) {
			// Como o CRM é orientado para o prospect, caso estejamos gerando
			// uma agenda para
			// aluno, que não possui um prospect relacionado, então temos que
			// gerar
			// este prospect e linkar os registros para que funcione
			// adequadamente no
			// restante do CRM

			// Carregamos os dados pessoa, que serão úteis para buscar e gerar
			// um prospect correspodente
//			getFacadeFactory().getPessoaFacade().carregarDados(campanhaPublicoAlvoProspectVO.getPessoa(), usuario);
			ProspectsVO prospectPessoa = new ProspectsVO();
			prospectPessoa.setTipoProspect(TipoProspectEnum.FISICO);
			prospectPessoa.setCpf(campanhaPublicoAlvoProspectVO.getPessoa().getCPF());
			if (!campanhaPublicoAlvoProspectVO.getPessoa().getCPF().equals("")) {
				prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(prospectPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}
			boolean encontrouProspect = false;
			if (prospectPessoa.getCodigo().equals(0)) {
				// Se entrarmos auqi é por que de fato não existe um Prospect
				// para a pessoa
				// Assim, temos que gerar um prospect e gravá-lo, passando a
				// referenciá-lo no compromisso abaixo.
				if (!campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim().equals("")) {
					if (!campanhaPublicoAlvoVO.getTipoPublicoAlvo().equals("RF")) {
						prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim(), false, usuario);
					} else {
						prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorNomeEmailUnico(campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim(), campanhaPublicoAlvoProspectVO.getPessoa().getNome(), false, usuario);
					}
				} else {
					prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorNomeEmailUnico(campanhaPublicoAlvoProspectVO.getPessoa().getEmail().trim(), campanhaPublicoAlvoProspectVO.getPessoa().getNome(), false, usuario);
				}
				if (prospectPessoa.getCodigo().equals(0)) {
					getFacadeFactory().getPessoaFacade().carregarDados(campanhaPublicoAlvoProspectVO.getPessoa(), usuario);
					prospectPessoa = campanhaPublicoAlvoProspectVO.getPessoa().gerarProspectsVOAPartirPessoaVO();
					if (campanhaPublicoAlvoVO.getTipoPublicoAlvo().equals("CD")) {
						prospectPessoa.setUnidadeEnsino(campanhaPublicoAlvoVO.getUnidadeEnsino());
					}
					if (campanhaPublicoAlvoVO.getTipoPublicoAlvo().equals("AL")) {
						MatriculaVO matricula = new MatriculaVO();
						matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaUnicaMatricula(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo(), 0, false, usuario);
						prospectPessoa.setUnidadeEnsino(matricula.getUnidadeEnsino());
					}
					prospectPessoa = campanhaPublicoAlvoProspectVO.getPessoa().gerarProspectsVOAPartirPessoaVO();
//					getFacadeFactory().getProspectsFacade().incluirSemValidarDados(prospectPessoa, Boolean.FALSE, usuario, null);
				} else {
					encontrouProspect = true;
				}
			} else {
				encontrouProspect = true;
			}
			if (encontrouProspect) {
				if (prospectPessoa.getPessoa().getCodigo().equals(0)) {
					// Se entrar aqui é por que encontramos o prospect, mas o
					// mesmo não está
					// vinculado ao aluno (pessoa) correspondete. Logo temos que
					// fazer este vinculo.
					prospectPessoa.getPessoa().setCodigo(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo());
//					getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectPessoa, usuario);
				}
				if (!prospectPessoa.getPessoa().getCodigo().equals(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo())) {
					throw new Exception("Ocorreu um erro ao identificar o prospect da pessoa de código: " + campanhaPublicoAlvoProspectVO.getPessoa().getCodigo() + ". Prospect encontrado por CPF " + campanhaPublicoAlvoProspectVO.getPessoa().getCPF() + " E-mail " + campanhaPublicoAlvoProspectVO.getPessoa().getEmail() + " está vinculado a outra pessoa.");
				}
			}
			prospectPessoa.setConsultorUltimaInteracao(getFacadeFactory().getFuncionarioFacade().consultarConsultorUltimaInteracaoProspectPorProspect(campanhaPublicoAlvoProspectVO.getPessoa().getCodigo(), usuario));
			if (!prospectPessoa.getConsultorPadrao().getCodigo().equals(0)) {
				prospectPessoa.setConsultorPadrao(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(prospectPessoa.getConsultorPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			prospectPessoa.setResponsavelFinanceiro(campanhaPublicoAlvoProspectVO.getProspect().getResponsavelFinanceiro());
			campanhaPublicoAlvoProspectVO.setProspect(prospectPessoa);
		}
		campanhaPublicoAlvoProspectVO.setCompromissoCampanhaPublicoAlvoProspectVO(montarSimulacaoCompromissoAgendaProspectConsultor(campanha, campanhaColaboradorVO, campanhaPublicoAlvoProspectVO, alterarConsultorPadraoProspect, usuario));
		
	}
	
	public FuncionarioVO inicializarDadosConsultorDistribuicao(FuncionarioVO consultorVO) {
		FuncionarioVO obj = new FuncionarioVO();
		obj.setCodigo(consultorVO.getCodigo());
		obj.getPessoa().setCodigo(consultorVO.getPessoa().getCodigo());
		obj.getPessoa().setNome(consultorVO.getPessoa().getNome());
		return obj;
	}
	
	@Override
	public void realizarMontagemListaConsultorProspect(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		campanhaPublicoAlvoVO.getListaCampanhaConsultorProspectVOs().clear();
		campanhaPublicoAlvoVO.setTotalProspectSemConsultor(null);
		campanhaVO.setTotalGeralProspectSemConsultor(null);
		
		Map<Integer, FuncionarioVO> mapCampanhaConsultorVOs = realizarCalculoQuantidadeProspectPorConsultor(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		Map<Integer, FuncionarioVO> mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs = realizarCalculoQuantidadeProspectUltrapassouDataLimiteCampanhaPorConsultor(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		for (FuncionarioVO consultor : mapCampanhaConsultorVOs.values()) {
			if (!mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.isEmpty() && mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.containsKey(consultor.getCodigo())) {
				consultor.setQuantidadeCompromissoUltrapassouDataLimiteCampanha(mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.get(consultor.getCodigo()).getQuantidadeCompromissoUltrapassouDataLimiteCampanha());
			}
			campanhaPublicoAlvoVO.getListaCampanhaConsultorProspectVOs().add(consultor);
		}
	}
	
	public Map<Integer, FuncionarioVO> realizarCalculoQuantidadeProspectPorConsultor(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		Map<Integer, FuncionarioVO> mapCampanhaConsultorVOs = new HashMap<Integer, FuncionarioVO>(0);
		campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().clear();
		removerResponsavelFinanceiroDuplicado(campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), campanhaPublicoAlvoVO);
		// CALCULAR TOTAL PROSPECT POR CONSULTOR
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setCampanhaPublicoAlvoVO(campanhaPublicoAlvoVO);
			if (!mapCampanhaConsultorVOs.containsKey(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())) {
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setTotalProspectPorConsultor(0);
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setTotalProspectPorConsultor(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getTotalProspectPorConsultor() + 1);
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setDataPrimeiroCompromisso(campanhaPublicoAlvoProspectVO.getDataCompromisso());
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setDataUltimoCompromisso(campanhaPublicoAlvoProspectVO.getDataCompromisso());
				mapCampanhaConsultorVOs.put(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo(), campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO());
			} else {
				FuncionarioVO consultorVO = mapCampanhaConsultorVOs.get(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo());
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setDataUltimoCompromisso(campanhaPublicoAlvoProspectVO.getDataCompromisso());
				consultorVO.setTotalProspectPorConsultor(consultorVO.getTotalProspectPorConsultor() + 1);
			}
			realizarCriacaoDadosEstatisticosPublicoAlvo(campanhaVO, campanhaPublicoAlvoVO, campanhaPublicoAlvoProspectVO, usuarioVO);
		}
		return mapCampanhaConsultorVOs;
	}
	
	public void removerResponsavelFinanceiroDuplicado(List<CampanhaPublicoAlvoProspectVO>  listaCampanhaPublicoAlvoProspectVOs, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO) {
		for (PessoaVO responsavelFinanceiroDuplicadoVO : campanhaPublicoAlvoVO.getListaResponsavelFinanceiroDuplicadoVOs()) {
			int index = 0;
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
				if ((responsavelFinanceiroDuplicadoVO.getNome().equals(campanhaPublicoAlvoProspectVO.getPessoa().getNome()) &&
						(responsavelFinanceiroDuplicadoVO.getCPF().replace(".", "").replace("-", "").equals(campanhaPublicoAlvoProspectVO.getPessoa().getCPF().replace(".", "").replace("-", ""))
								|| responsavelFinanceiroDuplicadoVO.getEmail().equals(campanhaPublicoAlvoProspectVO.getPessoa().getEmail())))) {
					listaCampanhaPublicoAlvoProspectVOs.remove(index);
					break;
				}
				index++;
			}
		}
	}
	
	public Map<Integer, FuncionarioVO> realizarCalculoQuantidadeProspectUltrapassouDataLimiteCampanhaPorConsultor(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		// SOMAR QUANTIDADE PROSPECT ULTRAPASSOU LIMITE DATA
		Map<Integer, FuncionarioVO> mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs = new HashMap<Integer, FuncionarioVO>(0);
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs()) {
			if (!mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.containsKey(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())) {
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setQuantidadeCompromissoUltrapassouDataLimiteCampanha(0);
				campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setQuantidadeCompromissoUltrapassouDataLimiteCampanha(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getQuantidadeCompromissoUltrapassouDataLimiteCampanha() + 1);
				mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.put(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo(), campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO());
			} else {
				FuncionarioVO consultorVO = mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs.get(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo());
				consultorVO.setQuantidadeCompromissoUltrapassouDataLimiteCampanha(consultorVO.getQuantidadeCompromissoUltrapassouDataLimiteCampanha() + 1);
			}
		}
		return mapCampanhaConsultorCompromissoUltrapassouDataLimiteVOs;
	}
	
	@Override
	public List<FuncionarioVO> realizarMontagemListaConsultorProspectTotalizador(CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		List<FuncionarioVO> listaConsultorPublicoAlvoProspectVOs = new ArrayList<FuncionarioVO>(0);
		Map<Integer, FuncionarioVO> mapCampanhaConsultorVOs = new HashMap<Integer, FuncionarioVO>(0);
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {

			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {

				if (!mapCampanhaConsultorVOs.containsKey(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())) {
					campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setTotalProspectPorConsultor(0);
					campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setTotalProspectPorConsultor(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getTotalProspectPorConsultor() + 1);
					mapCampanhaConsultorVOs.put(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo(), campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO());

				} else {
					FuncionarioVO consultorVO = mapCampanhaConsultorVOs.get(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo());
					consultorVO.setTotalProspectPorConsultor(consultorVO.getTotalProspectPorConsultor() + 1);
				}
			}
		}
		for (FuncionarioVO consultor : mapCampanhaConsultorVOs.values()) {
			listaConsultorPublicoAlvoProspectVOs.add(consultor);
		}
		return listaConsultorPublicoAlvoProspectVOs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsPorConsultor(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, FuncionarioVO consultorSelecionadoVO, Boolean carregarSomenteCompromissoUltrapassouDataCampanha, UsuarioVO usuarioVO) {
		List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		
		if (consultorSelecionadoVO != null) {
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(consultorSelecionadoVO.getCodigo())) {
					if (carregarSomenteCompromissoUltrapassouDataCampanha) {
						if (campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso().compareTo(campanhaVO.getPeriodoFim()) > 0) {
							listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
						}
					} else {
						listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
					}
				}
			}
		} else {
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (carregarSomenteCompromissoUltrapassouDataCampanha) {
					if (campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso().compareTo(campanhaVO.getPeriodoFim()) > 0) {
						listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
					}
				} else {
					listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
				}
			}
		}
		return Ordenacao.ordenarLista(listaCampanhaPublicoAlvoProspectVOs, "dataCompromisso");
	}
	
	@Override
	public List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsSemConsultor(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(0)) {
					listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
				}
			}
		
		return listaCampanhaPublicoAlvoProspectVOs;
	}
	
	@Override
	public List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsPorConsultorGeral(CampanhaVO campanhaVO, FuncionarioVO consultorVO, UsuarioVO usuarioVO) {
		List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(consultorVO.getCodigo())) {
					listaCampanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
				}
			}
		}
		return listaCampanhaPublicoAlvoProspectVOs;
	}
	
	/*@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AgendaPessoaHorarioVO realizarSimulacaoAgendaPessoaHorarioPorAgendaEConsultor(AgendaPessoaVO agenda, Date dataCompromisso, Boolean considerarSabado, Boolean considerarFeriados, UsuarioVO usuarioLogado) throws Exception {
		Date dataCompromissoValidada = getFacadeFactory().getFeriadoFacade().realizarValidacaoEObterDataProximoDiaUtil(dataCompromisso, 0, considerarSabado, considerarFeriados, false, usuarioLogado);
		AgendaPessoaHorarioVO agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorCodigoAgendaPessoa(0, agenda.getCodigo(), 0, dataCompromissoValidada, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		if (agendaPessoaHorario.getCodigo() == 0) {
			agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(dataCompromissoValidada), Uteis.getMesData(dataCompromissoValidada), Uteis.getAnoData(dataCompromissoValidada), Uteis.getDiaSemanaEnum(dataCompromissoValidada), true);
		}
		return agendaPessoaHorario;
	}*/
	
	public CompromissoCampanhaPublicoAlvoProspectVO montarSimulacaoCompromissoAgendaProspectConsultor(CampanhaVO campanha, CampanhaColaboradorVO campanhaColaborador, CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO, Boolean alterarConsultorPadraoProspect, UsuarioVO usuario) throws Exception {

		CompromissoCampanhaPublicoAlvoProspectVO compromissoCampanhaPublicoAlvoProspectVO = getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().executarGeracaoCompromissoRotacionamentoSimulacao(campanha, campanhaColaborador.getDataGeracaoProximaAgenda(), usuario, campanhaPublicoAlvoProspectVO.getProspect(), campanhaColaborador.getHoraGeracaoProximaAgenda());
		campanhaColaborador.setDataUltimaAgendaGerada(Uteis.getDateTime(campanhaColaborador.getDataGeracaoProximaAgenda(), Uteis.getHoraDaString(campanhaColaborador.getHoraGeracaoProximaAgenda()), Uteis.getMinutosDaString(campanhaColaborador.getHoraGeracaoProximaAgenda()), 0));
		campanhaColaborador.setNumeroAgendasGeradasParaData(campanhaColaborador.getNumeroAgendasGeradasParaData() + 1);
		compromissoCampanhaPublicoAlvoProspectVO.setAlterarConsultorPadraoProspect(alterarConsultorPadraoProspect);
        boolean reiniciouHorario = false;
		if (campanhaColaborador.getNumeroAgendasGeradasParaData().compareTo(campanhaColaborador.getQtdContato()) >= 0) {
			// Se entrarmos aqui é por que atingimos o número máximos de
			// contatos estabelecidos como meta
			// para o dia de trabalho do consultor em questão. Então temos que
			// avançar de dia, e zerar a contagem
			// de prospects agendados para o dia, para que o controle seja
			// iniciado novamente.
			// Quando entramos aqui, a troca de dia é realizada não em
			// consequencia de atingir o final do expediente
			// mas sim em função da meta do dia de contatos já ter sido
			// atingida.
			Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
			proximoDiaGerarAgenda = getFacadeFactory().getFeriadoFacade().obterDataFuturaProximoDiaUtil(proximoDiaGerarAgenda, campanha.getUnidadeEnsino().getCidade().getCodigo(), campanha.getConsiderarSabado(), campanha.getConsiderarFeriados(), ConsiderarFeriadoEnum.ACADEMICO, usuario);
//			campanha.getConsiderarFeriados();
//			campanha.getConsiderarSabado();
			
			campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
			campanhaColaborador.setHoraGeracaoProximaAgenda(campanhaColaborador.getHoraInicioGerarAgenda());
			campanhaColaborador.setNumeroAgendasGeradasParaData(0);
            reiniciouHorario = true;
		}

		Integer tempoMedioCadaAgenda = campanha.getWorkflow().getTempoMedioGerarAgenda();
		String proximoHorarioAgenda = "";
                if (!reiniciouHorario) {
                    if (!tempoMedioCadaAgenda.equals(0)) {
                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), campanha.getWorkflow().getTempoMedioGerarAgenda());
                    } else {
                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), 1);
                    }
                } else {
                    proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
                }
		if ((!campanhaColaborador.getHoraFimIntervalo().equals("")) && ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFimIntervalo()) < 0))) {
			// Se existe uma horario final para o intervalo e
			// proximoHorarioAgenda é menor que o mesmo
			// é por que ainda estamos no primeiro periodo de trabalho do
			// consultor, entao temos que verificar
			// se já atingimos o final do primeiro periodo. Caso sim, já
			// assumimos o fim do intervalo como
			// proximoHorarioAgenda
			if (!campanhaColaborador.getHoraInicioIntervalo().equals("")) {
				if (proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraInicioIntervalo()) >= 0) {
					// Atingimos o horário do intervalor da pessoa, logo o
					// proximo horário a ser gerada agenda
					// é no retorno do intervalo.
					proximoHorarioAgenda = campanhaColaborador.getHoraFimIntervalo();
				}
			}
			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
		} else {
			// Se entrarmos aqui é por que não existe intervalo programado ou já
			// estamos gerando
			// agenda para o segundo período de trabalho do consultor (parte da
			// tarde por exemplo).
			// Logo temos que avaliar se já atingimos o final do expediente.
			// Caso sim, temos que
			// ir para o próximo dia, pois não é possível mais gerar agenda no
			// mesmo dia.
			if ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFinalGerarAgenda()) >= 0)) {
				// Avancar para o próximo dia
				Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
				campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
				proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
				campanhaColaborador.setNumeroAgendasGeradasParaData(0);
			}
			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
		}
		return compromissoCampanhaPublicoAlvoProspectVO;
	}

	/**
	 * Método responsável por gerar agenda de todo publico alvo
	 */
	@Override
	public void executarGeracaoAgendaDeAcordoPublicoAlvo(final CampanhaVO campanhaVO, final ProgressBarVO progressBar, final UsuarioVO usuarioVO) throws Exception {
		try {
			for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
				executarGeracaoAgendaDeAcordoPublicoAlvoEspecifico(campanhaVO, campanhaPublicoAlvoVO, progressBar,usuarioVO);
			}
			campanhaVO.setPossuiAgenda(true);
		} catch (Exception e) {
			progressBar.setException(e);
			progressBar.setForcarEncerramento(true);
		} finally {

		}			
	}
	
	/**
	 * Método responsável por gerar agenda de todo publico alvo
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarGeracaoAgendaDeAcordoPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception {
		
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
			
			if (campanhaVO.getRealizandoRedistribuicaoProspectAgenda() 
					&& (campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.NAO_REDISTRIBUIR) 
					|| campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA))
					|| (!campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
						&& !campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA))) {
				progressBar.incrementar();
				progressBar.setStatus("Carregando dados do Prospect n° " + campanhaPublicoAlvoProspectVO.getPessoa().getNome() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				continue;
			}
			// Primeiramente verificamos se existe prospect existente
			// Caso não exista iremos incluir um novo
			if (campanhaPublicoAlvoProspectVO.getProspect().getCodigo().equals(0)) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(campanhaPublicoAlvoProspectVO.getProspect(), Boolean.FALSE, usuarioVO, null);
			} else {
				getFacadeFactory().getProspectsFacade().alterarPessoaProspect(campanhaPublicoAlvoProspectVO.getProspect(), usuarioVO);
			}
			// Verificamos se já existe agenda para o consultor responsável
			AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa(), usuarioVO);
			if (!Uteis.isAtributoPreenchido(agenda)) {
				agenda.setPessoa(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa());
				getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuarioVO);
			}
			AgendaPessoaHorarioVO agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorCodigoAgendaPessoa(0, agenda.getCodigo(), 0, campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (agendaPessoaHorario.getCodigo() == 0) {
				agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso()), Uteis.getMesData(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso()), Uteis.getAnoData(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso()), Uteis.getDiaSemanaEnum(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso()), true);
				agenda.getAgendaPessoaHorarioVOs().add(agendaPessoaHorario);
				getFacadeFactory().getAgendaPessoaHorarioFacade().incluirAgendaPessoaHorarios(agenda.getCodigo(), agenda.getAgendaPessoaHorarioVOs(), usuarioVO);
			}
			// Criação do compromisso
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluirCompromissoAgendaPessoaHorarioPorCampanhaProspectNaoInicializado(campanhaVO.getCodigo(), campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), usuarioVO);
			CompromissoAgendaPessoaHorarioVO compromissoIncluirVO = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().inicializarDadosCompromissoAgendaPorCompromissoCamapnhaPublicoAlvo(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO(), agendaPessoaHorario, campanhaVO, campanhaPublicoAlvoProspectVO.getProspect(), usuarioVO);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(compromissoIncluirVO, usuarioVO);
			progressBar.incrementar();
			progressBar.setStatus("Carregando dados do Prospect n° " + campanhaPublicoAlvoProspectVO.getPessoa().getNome() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			if(campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getAlterarConsultorPadraoProspect()
					&& !campanhaPublicoAlvoProspectVO.getProspect().getConsultorPadrao().getCodigo().equals(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())){
				campanhaPublicoAlvoProspectVO.getProspect().setConsultorPadrao(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO());
				campanhaPublicoAlvoProspectVO.getProspect().setAcaoCompromissoAguardandoExcecucao("");
				campanhaPublicoAlvoProspectVO.getProspect().setExcluirCompromissoNaoIniciadoPassado(false);
				campanhaPublicoAlvoProspectVO.getProspect().setFinalizarCompromissoParalizado(false);
				getFacadeFactory().getProspectsFacade().alterarConsultorProspect(campanhaPublicoAlvoProspectVO.getProspect(), usuarioVO);
			}
		}
		campanhaVO.setPossuiAgenda(true);
	}

	@Override
	public void inicializarDadosCompromissoUltrapassouDataLimite(CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			campanhaPublicoAlvoVO.getListaCampanhaPublicoAlvoProspectUltrapassaramLimiteDataFinalCampanhaVOs().clear();
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				realizarCriacaoDadosEstatisticosPublicoAlvo(campanhaVO, campanhaPublicoAlvoVO, campanhaPublicoAlvoProspectVO, usuarioVO);
			}
		}
	}
	
	@Override
	public void realizarRedistribuicaoConsultorPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, Boolean alterarConsultorPadraoProspect, UsuarioVO usuarioVO, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception {
		campanhaVO.setRealizandoRedistribuicaoProspectAgenda(true);
		//selecionados prospects no tipo de redistribuição para Nao Redistribuir
		removerAgendaConsultorNaoRedistribuir(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		//Remove todos os consultores selecionados no tipo de redistribuição para Remover Agenda
		removerAgendaConsultorRedistribuicao(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		//Realiza o carregamento dos prospects selecionados no tipo de redistribuição para Redistribuir
		realizarCarregamentoCampanhaPublicoAlvoProspectPublicoAlvoRedistribuir(campanhaVO, campanhaPublicoAlvoVO, progressBar, usuarioVO);
		//Realiza a redistribuição dos prospects
		realizarCarregamentoAgendaDistribuindoProspectsPorConsultor(campanhaVO, campanhaPublicoAlvoVO, progressBar, alterarConsultorPadraoProspect, usuarioVO, alterarConsultorCompromissoProspectJaIniciado);
		//Realiza o cálulo de quantos prospects por consultor
		realizarMontagemListaConsultorProspect(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		//atualiza a média de prospect por consultor 
		inicializarDadosMediaProspectPorColaborador(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		
		Ordenacao.ordenarLista(campanhaPublicoAlvoVO.getListaCampanhaConsultorProspectVOs(), "nomeConsultor");
		alterarSituacaoAgenda(campanhaPublicoAlvoVO, "Agenda Não Gerada!", usuarioVO);
	}
	
	/**
	 * Método responsável por verificar qual política de redistribuição será utilizada
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 */
	@Override
	public void realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanhaPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
			TipoSituacaoCompromissoEnum situacaoAtualProspectAgenda = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarSituacaoAtualCompromissoPorProspectCampanha(campanhaVO.getCodigo(), campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso(), usuarioVO);
			
			if (situacaoAtualProspectAgenda != null) {
				campanhaPublicoAlvoProspectVO.setSituacaoAtualCompromissoAgendaEnum(situacaoAtualProspectAgenda);
				if (situacaoAtualProspectAgenda.equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
					campanhaPublicoAlvoProspectVO.setPoliticaRedistribuicaoProspectAgenda(PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_COM_AGENDA_NAO_REALIZADA);
				}
			} else {
				campanhaPublicoAlvoProspectVO.setSituacaoAtualCompromissoAgendaEnum(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA);
				if (!campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(0)) {
					campanhaPublicoAlvoProspectVO.setPoliticaRedistribuicaoProspectAgenda(PoliticaRedistribuicaoProspectAgendaEnum.PROSPECT_SEM_AGENDA);
				} 
			}

		}
	}
	
	/*Método responsável por realizar a redistribuição dos prospects na agenda da campanha
	 * (non-Javadoc)
	 * @see negocio.interfaces.administrativo.CampanhaInterfaceFacade#realizarRedistribuicaoConsultorPublicoAlvo(negocio.comuns.administrativo.CampanhaVO, negocio.comuns.utilitarias.ProgressBarVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public void realizarRedistribuicaoConsultorPublicoAlvo(CampanhaVO campanhaVO, ProgressBarVO progressBar,Boolean alterarConsultorPadraoProspect, UsuarioVO usuarioVO, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception {
		campanhaVO.setRealizandoRedistribuicaoProspectAgenda(true);
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			realizarRedistribuicaoConsultorPublicoAlvoEspecifico(campanhaVO, campanhaPublicoAlvoVO, progressBar, alterarConsultorPadraoProspect, usuarioVO, alterarConsultorCompromissoProspectJaIniciado);			
		}
	}
	
	public void realizarCarregamentoCampanhaPublicoAlvoProspectPublicoAlvoRedistribuir(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception {
		Map<Integer, FuncionarioVO> mapConsultorSelecionadoDistribuicaoVOs = obterConsultorRedistribuicao(campanhaVO);
		carregarCampanhaPublicoAlvoProspectRedistribuicaoConsultorSelecionado(campanhaVO, campanhaPublicoAlvoVO, mapConsultorSelecionadoDistribuicaoVOs, usuarioVO);
	}
	
	
	public Map<Integer, FuncionarioVO> obterConsultorRedistribuicao(CampanhaVO campanhaVO) {
		Map<Integer, FuncionarioVO> mapConsultorVOs = new HashMap<Integer, FuncionarioVO>(0);
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaVO.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)) {
				if (!mapConsultorVOs.containsKey(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo())) {
					mapConsultorVOs.put(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo(), campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO());
				}
			}
		}
		return mapConsultorVOs;
	}
	
	public Boolean removerAgendaConsultorRedistribuicao(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		List<FuncionarioVO> listaFuncionarioRemoverVOs = obterColaboradorRemoverAgendaRedistribuicao(campanhaVO, usuarioVO);
		for (FuncionarioVO funcionarioVO : listaFuncionarioRemoverVOs) {
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
//				Somente poderá realizar o prospect cuja situação seja aguardando contato ou ele ainda não possua agenda
				if (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
						|| campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA)) {
					
					if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(funcionarioVO.getCodigo())) {
						campanhaPublicoAlvoProspectVO.setConsultorDistribuicaoVO(null);
						campanhaPublicoAlvoProspectVO.setTipoDistribuicaoProspectCampanhaPublicoAlvo(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA);
					}
				}
			}
		}
		return realizarVerificacaoRemoveuAgendaTodosFuncionario(listaFuncionarioRemoverVOs, campanhaVO, usuarioVO);
	}
	
	public Boolean realizarVerificacaoRemoveuAgendaTodosFuncionario(List<FuncionarioVO> listaFuncionarioRemoverVOs, CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		if (listaFuncionarioRemoverVOs.size() == campanhaVO.getListaCampanhaColaborador().size()) {
			return true;
		}
		return false;
	}
	
	public List<FuncionarioVO> obterColaboradorRemoverAgendaRedistribuicao(CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		List<FuncionarioVO> listaFuncionarioRemoverVOs = new ArrayList<FuncionarioVO>(0);
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaVO.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA)) {
				listaFuncionarioRemoverVOs.add(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO());
			}
		}
		return listaFuncionarioRemoverVOs;
	}
	
	public void removerAgendaConsultorNaoRedistribuir(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		List<FuncionarioVO> listaFuncionarioRemoverVOs = obterColaboradorNaoRedistribuir(campanhaVO, usuarioVO);
		for (FuncionarioVO funcionarioVO : listaFuncionarioRemoverVOs) {
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(funcionarioVO.getCodigo())) {					
					campanhaPublicoAlvoProspectVO.setTipoDistribuicaoProspectCampanhaPublicoAlvo(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.NAO_REDISTRIBUIR);
				}
			}
		}		
	}
	
	public List<FuncionarioVO> obterColaboradorNaoRedistribuir(CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		List<FuncionarioVO> listaFuncionarioNaoRedistribuirVOs = new ArrayList<FuncionarioVO>(0);
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaVO.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.NAO_REDISTRIBUIR)) {
				listaFuncionarioNaoRedistribuirVOs.add(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO());
			}
		}
		return listaFuncionarioNaoRedistribuirVOs;
	}
	
	public void carregarCampanhaPublicoAlvoProspectRedistribuicaoConsultorSelecionado(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, Map<Integer, FuncionarioVO> mapConsultorDistribuicaoVOs, UsuarioVO usuarioVO) {
		campanhaPublicoAlvoVO.getMapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs().clear();
		if (mapConsultorDistribuicaoVOs.isEmpty()) {
			return;
		}
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				if (mapConsultorDistribuicaoVOs.containsKey(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo()) || campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo().equals(0)) {
					campanhaPublicoAlvoVO.getMapCampanhaPublicoAlvoProspectRedistribuicaoAgendaVOs().put(campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), campanhaPublicoAlvoProspectVO);
				}
		}
	}
	
	/**
	 * Método responsável por verificar qual política de redistribuição será utilizada
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 */
	@Override
	public void realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanha(CampanhaVO campanhaVO, UsuarioVO usuarioVO) {
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanhaPublicoAlvoEspecifico(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);			
		}
	}

	
	/**
	 * Método Responsável por regerar a agenda
	 * Nós iremos remover todos os compromissos do prospect cujo boolean esteja marcado para regerar a agenda
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void regerarAgendaCampanha(CampanhaVO campanhaVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getCampanhaColaboradorFacade().alterarCampanhaColaborador(campanhaVO.getCodigo(), campanhaVO.getListaCampanhaColaborador());
		for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
			getFacadeFactory().getCampanhaPublicoAlvoFacade().alterarCampanhaPublicoAlvoRegeracaoAgenda(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);			
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluirCompromissoAgendaPessoaHorarioPorCampanhaProspect(campanhaVO.getCodigo(), campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), usuarioVO);
			executarGeracaoAgendaDeAcordoPublicoAlvoEspecifico(campanhaVO, campanhaPublicoAlvoVO, progressBar, usuarioVO);
			alterarSituacaoAgenda(campanhaPublicoAlvoVO, "Agenda Regerada com Sucesso!", usuarioVO);
		}						
	}
	
	/**
	 * Método Responsável por regerar a agenda
	 * Nós iremos remover todos os compromissos do prospect cujo boolean esteja marcado para regerar a agenda
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void regerarAgendaCampanhaPublicoAlvoEspecifico(CampanhaVO campanhaVO, ProgressBarVO progressBar, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) throws Exception {
		// É necessário incluir o colaborador que foi posto para realizar a distribuição
		getFacadeFactory().getCampanhaColaboradorFacade().alterarCampanhaColaborador(campanhaVO.getCodigo(), campanhaVO.getListaCampanhaColaborador());
		// Altera ou Cria a campanha publico alvo e regera os prospects para regeração da agenda 
		getFacadeFactory().getCampanhaPublicoAlvoFacade().alterarCampanhaPublicoAlvoRegeracaoAgenda(campanhaVO, campanhaPublicoAlvoVO, usuarioVO);
		
		/**
		 * Remoção do vínculo de interações nos compromissos
		 */
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().removerVinculoInteracaoWorkFlowCompromissoAgendaPessoaHorario(campanhaVO.getCodigo(), campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), usuarioVO);		
		/**
		 * Os compromissos serão excluidos para a nova geração
		 */
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluirCompromissoAgendaPessoaHorarioPorCampanhaProspect(campanhaVO.getCodigo(), campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), usuarioVO);
		/**
		 * Aqui será gerada novamente a agenda para a nova redistribuição da
		 * agenda
		 */
		executarGeracaoAgendaDeAcordoPublicoAlvoEspecifico(campanhaVO, campanhaPublicoAlvoVO, progressBar, usuarioVO);
		alterarSituacaoAgenda(campanhaPublicoAlvoVO, "Agenda Regerada com Sucesso!", usuarioVO);
	}
	
	@Override
	public Boolean consultarExistenciaCampanhaPossuiAgenda(Integer campanha, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo from compromissoagendapessoahorario where campanha = ").append(campanha).append(" limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void inicializarDadosConsultorIncluidoCampanha(CampanhaVO campanhaVO, UsuarioVO usuarioVO) throws Exception {
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaVO.getListaCampanhaColaborador()) {
			if (campanhaColaboradorVO.getCodigo().equals(0) && !campanhaColaboradorVO.getFuncionarioCargoVO().getCodigo().equals(0)) {
				campanhaColaboradorVO.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(campanhaColaboradorVO.getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
		}
	}
	
	public void alterarSituacaoAgenda(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, String situacao, UsuarioVO usuarioVO) {
		campanhaPublicoAlvoVO.setSituacaoAgenda(situacao);
	}
	
	@Override
	public void inicializarDadosQuantidadeProspectPorSituacaoAtualCampanhaPorCampanhaPublicoAlvoEspecifico(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, UsuarioVO usuarioVO) {
		for (CampanhaColaboradorVO campanhaColaboradorVO : listaCampanhaColaboradorVOs) {
			campanhaColaboradorVO.setQuantidadeProspectSemAgenda(0);
			campanhaColaboradorVO.setQuantidadeProspectComAgendaNaoRealizada(0);
			campanhaColaboradorVO.getListaProspectSemAgendaVOs().clear();
			campanhaColaboradorVO.getListaProspectComAgendaNaoRealizadaVOs().clear();
			
			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
				
				if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())) {
					if (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA)) {
						campanhaColaboradorVO.setQuantidadeProspectSemAgenda(campanhaColaboradorVO.getQuantidadeProspectSemAgenda() + 1);
						campanhaColaboradorVO.getListaProspectSemAgendaVOs().add(campanhaPublicoAlvoProspectVO);
					} else if (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
						campanhaColaboradorVO.setQuantidadeProspectComAgendaNaoRealizada(campanhaColaboradorVO.getQuantidadeProspectComAgendaNaoRealizada() + 1);
						campanhaColaboradorVO.getListaProspectComAgendaNaoRealizadaVOs().add(campanhaPublicoAlvoProspectVO);
					}
				}
			}
		}
	}

	@Override
	public void inicializarDadosQuantidadeProspectPorSituacaoAtualCampanha(CampanhaVO campanhaVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, UsuarioVO usuarioVO) {
		for (CampanhaColaboradorVO campanhaColaboradorVO : listaCampanhaColaboradorVOs) {
			campanhaColaboradorVO.setQuantidadeProspectSemAgenda(0);
			campanhaColaboradorVO.setQuantidadeProspectComAgendaNaoRealizada(0);
			campanhaColaboradorVO.getListaProspectSemAgendaVOs().clear();;
			campanhaColaboradorVO.getListaProspectComAgendaNaoRealizadaVOs().clear();
			
			for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
				for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
					if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo())) {
						if (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA)) {
							campanhaColaboradorVO.setQuantidadeProspectSemAgenda(campanhaColaboradorVO.getQuantidadeProspectSemAgenda() + 1);
							campanhaColaboradorVO.getListaProspectSemAgendaVOs().add(campanhaPublicoAlvoProspectVO);
						} else if (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
							campanhaColaboradorVO.setQuantidadeProspectComAgendaNaoRealizada(campanhaColaboradorVO.getQuantidadeProspectComAgendaNaoRealizada() + 1);
							campanhaColaboradorVO.getListaProspectComAgendaNaoRealizadaVOs().add(campanhaPublicoAlvoProspectVO);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void inicializarDadosMediaProspectPorColaborador(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) {
		Integer qtdeConsultor = 0;
    	for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaVO.getListaCampanhaColaborador()) {
    		if (!campanhaColaboradorVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA)) {
    			qtdeConsultor++;
    		}
    	}
    	if (qtdeConsultor > 0) {
    		campanhaPublicoAlvoVO.setMediaProspectColaborador(campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs().size() / qtdeConsultor);
    	}
		
	}

	@Override
	public CampanhaVO consultarCampanhaPorTipoCampanhaUnidadeEnsino(Integer unidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append("  ");
		sqlStr.append(" where (tipocampanha = '").append(tipoCampanhaEnum).append("')");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if (!situacao.isEmpty()) {
			sqlStr.append(" and situacao = '").append(situacao).append("'");
		}
		sqlStr.append(" ORDER BY campanha.descricao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDadosBasicos(tabelaResultado, nivelMontarDados, usuario);
		}
		return null;
	}

	@Override
	public boolean verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(CampanhaVO campanha, ProspectsVO prospectsVO, UsuarioVO usuario) {
		try {
			if (prospectsVO.getCodigo().equals(0)) {
				return true;
			}
			boolean possuiAgendaEmAberto = false;

			possuiAgendaEmAberto = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCaptacao(prospectsVO.getCodigo(), campanha.getDataInicialVerificarJaExisteAgendaProspect(), campanha.getDataFinalVerificarJaExisteAgendaProspect());

			if (possuiAgendaEmAberto) {
				// Se entrar aqui é por que a campanha foi parametrizada
				// para nao
				// gerar agenda duplicada e o prospect em questão já tem
				// agenda
				// no período informado. Logo não poderemos gerar agenda
				// para ele.
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public CampanhaColaboradorVO obterConsultorDistribuicaoRotacionadaParaGerarAgendaLigacaoReceptiva(CampanhaVO campanha, Integer consultorDistribuir) throws Exception {
		Integer contador = 1;
		for (CampanhaColaboradorVO campanhaColaboradorVO : campanha.getListaCampanhaColaborador()) {
			if (contador.equals(consultorDistribuir)) {
				return campanhaColaboradorVO;
			}
			contador = contador + 1;
		}
		throw new Exception("Ocorreu um problema no rotacionamento de prospects por consultor (Não existe um consultor na posição" + consultorDistribuir + ").");
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoCampanhaPublicoAlvoProspectAdicionadoDinamicamente(CampanhaVO campanhaVO, UsuarioVO usuarioVO) throws Exception{
		if(campanhaVO.getSituacao().equals("AT")){
			List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspectVOs = getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().consultarProspectsVinculadoCampanhaSemVinculoPublicoAlvo(campanhaVO, usuarioVO);
			if(!campanhaPublicoAlvoProspectVOs.isEmpty()){
				CampanhaPublicoAlvoVO campanhaPublicoAlvoVO = realizarObtencaoCampanhaPublicoAlvoAdicionadoDinamicamente(campanhaVO, usuarioVO);
				getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().incluirCampanhaPublicoAlvoProspect(campanhaPublicoAlvoVO.getCodigo(), campanhaPublicoAlvoProspectVOs, usuarioVO);
				campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs().addAll(campanhaPublicoAlvoProspectVOs);	
				for(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO2:campanhaVO.getListaCampanhaPublicoAlvo()){
					if(campanhaPublicoAlvoVO2.getAdicionadoDinamicamente()){
						return;
					}
				}
				campanhaVO.getListaCampanhaPublicoAlvo().add(campanhaPublicoAlvoVO);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private CampanhaPublicoAlvoVO realizarObtencaoCampanhaPublicoAlvoAdicionadoDinamicamente(CampanhaVO campanhaVO, UsuarioVO usuarioVO) throws Exception{
		for(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO:campanhaVO.getListaCampanhaPublicoAlvo()){
			if(campanhaPublicoAlvoVO.getAdicionadoDinamicamente()){
				return campanhaPublicoAlvoVO;
			}
		}
		CampanhaPublicoAlvoVO campanhaPublicoAlvoVO =  new CampanhaPublicoAlvoVO();
		campanhaPublicoAlvoVO.setCampanha(campanhaVO);
		campanhaPublicoAlvoVO.setTipoGerarAgendaCampanha(TipoGerarAgendaCampanhaEnum.GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA);
		if(campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO)){
			campanhaPublicoAlvoVO.setTipoPublicoAlvo("CD");
		}else if(campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_ALUNOS_COBRANCA) || campanhaVO.getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA)){
			campanhaPublicoAlvoVO.setTipoPublicoAlvo("AL");
		}else{
			campanhaPublicoAlvoVO.setTipoPublicoAlvo("PR");
		}
		campanhaPublicoAlvoVO.setAdicionadoDinamicamente(true);
		getFacadeFactory().getCampanhaPublicoAlvoFacade().incluirSemSubordinada(campanhaPublicoAlvoVO, usuarioVO);
		return campanhaPublicoAlvoVO;
		
	}
	
}
