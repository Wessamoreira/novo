package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.enumeradores.OrdemEstudoDisciplinasOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoProvaPresencialEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberarAcessoProximaDisciplinaEnum;
import negocio.comuns.ead.enumeradores.TipoControleTempoLimiteConclusaoDisciplinaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ConfiguracaoEADInterfaceFacade;

@Repository
@Lazy
public class ConfiguracaoEAD extends ControleAcesso implements ConfiguracaoEADInterfaceFacade {

	private static final long serialVersionUID = 8539167640234060018L;

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

	public ConfiguracaoEAD() throws Exception {
		super();
		setIdEntidade("ConfiguracaoEAD");
	}

	@Override
	public void persistir(ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(configuracaoEADVO);
		if (configuracaoEADVO.isNovoObj()) {
			incluir(configuracaoEADVO, controlarAcesso, usuarioVO);
		} else {
			alterar(configuracaoEADVO, controlarAcesso, usuarioVO);
		}
	}

	public void validarDados(ConfiguracaoEADVO obj) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_descricao"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getParametrosMonitoramentoAvaliacaoOnlineVO())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_parametroMonitoramentoAvaliacao"));
		}
		if(!obj.getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()){
			if (obj.getTempoLimiteConclusaoDisciplina() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_TempoLimiteConclusaoDisciplina"));
			}
			if (obj.getTempoLimiteConclusaoTodasDisciplinas() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_TempoLimiteConclusaoTodasDisciplinas"));
			}
			if (obj.getTempoLimiteConclusaoCursoIncluindoTCC() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_TempoLimiteConclusãoCursoIncluindoTCC"));
			}	
		}
		
		if (!obj.getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE) &&
				!obj.getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.IMEDIATO_APOS_O_INICIO_DO_CURSO)) {
			if (obj.getValorControleLiberacaoAvalicaoOnline() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_ValorControleLiberacaoAvaliacaoOnline"));
			}
		}
		if (!obj.getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE)) {
			if (obj.getNrDiasEntreAvalicaoOnline() < 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_NrDiasEntreAvaliacaoOnline"));
			}
			if (obj.getNrVezesPodeRepetirAvaliacaoOnline() < 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_NrVezesPodeRepetirAvaiacaoOnline"));
			}
		}
		if (obj.getOrdemEstudoDisciplinasOnline() == null) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_OrdemEstudoDisciplinasOnline"));
		}
		if (obj.getTipoControleLiberacaoProvaPresencial() == null) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_TipoControleLiberacaoProvaPresencial"));
		}
		
		
		if (obj.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.CONTEUDO_LIDO) || obj.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.PONTUACAO_ALCANCADA_ESTUDOS)) {
			if (obj.getValorControleLiberarAcessoProximaDisciplina() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_ValorControleLiberacaoProximaDisciplina"));
			}
		}
		if (obj.getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva()) {
			if (obj.getVariavelNotaCfgPadraoAtividadeDiscursiva().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_VariavelNotaCfgAtividadeDiscursiva"));
			}
		}
		if (obj.getCalcularMediaFinalAposRealizacaoAvaliacaoOnline()) {
			if (obj.getVariavelNotaCfgPadraoAvaliacaoOnline().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_VariavelNotaCfgAvaliacaoOnline"));
			}
		}
		if (obj.getControlarTempoLimiteRealizarProvaPresencial()) {
			if (obj.getTempoLimiteRealizacaoProvaPresencial() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEADVO_TempoLimiteRealizarProvaPresencial"));
			}
		}
		if(obj.getNotificarAluno()) {
			if(obj.getNotUmPrazoConclusaoEstudos() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoUmPrazoConclusaoEstudos"));
			}
			if(obj.getNotDoisPrazoConclusaoEstudos() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoDoisPrazoConclusaoEstudos"));
			}
			if(obj.getNotTresPrazoConclusaoEstudos() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoTresPrazoConclusaoEstudos"));
			}
		}
		if(obj.getNotificarAlunoDiasFicarSemLogarSistema()) {
			if(obj.getNotUmDiasSemLogarSistema() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoUmQueFicarSemLogarNoSistema"));
			}
			if(obj.getNotDoisDiasSemLogarSistema() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoDoisQueFicarSemLogarNoSistema"));
			}
			if(obj.getNotTresDiasSemLogarSistema() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoTresQueFicarSemLogarNoSistema"));
			}
		}
		if(obj.getNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso()) {
			if(obj.getNotUmAtividadeDiscursivaPrazoConclusaoCurso() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoUmAtividadeDiscursiva"));
			}
			if(obj.getNotDoisAtividadeDiscursivaPrazoConclusaoCurso() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoDoisAtividadeDiscursiva"));
			}
			if(obj.getNotTresAtividadeDiscursivaPrazoConclusaoCurso() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoTresAtividadeDiscursiva"));
			}
		}
		if(obj.getNotificarProfessorDuvidasNaoRespondidas()) {
			if(obj.getNotificacaoProfessorDiasDuvidasNaoRespondidas() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoDuvidasNaoRespondidasProfessor"));
			}
		}
		if(obj.getNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor()) {
			if(obj.getGrupoDestinatariosVO().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_grupoDestinatario"));
			}
			if(obj.getNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas() <= 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoEAD_notificacaoDuvidasNaoRespondidasGrupoDestinatario"));
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), controlarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO configuracaoead( ");
			sql.append("descricao, tipocontroletempolimiteconclusaodisciplina, ");
			sql.append("tempolimiteconclusaodisciplina, tempolimiteconclusaotodasdisciplinas, ");
			sql.append("tempolimiteconclusaocursoincluindotcc, controlartempolimiterealizarprovapresencial, ");
			sql.append("tempolimiterealizarprovapresencial, tipocontroleliberacaoavaliacaoonline, ");
			sql.append("valorcontroleliberacaoavaliacaoonline, nrvezespoderepetiravaliacaoonline, ");
			sql.append("nrdiasentreavaliacaoonline, ordemestudodisciplinasonline, nrmaximodisciplinassimultaneas, ");
			sql.append("tipocontroleliberaracessoproximadisciplina, valorcontroleliberaracessoproximadisciplinas, ");
			sql.append("tipocontroleliberacaoprovapresencial, valorcontroleliberacaoprovapresencial, ");
			sql.append("tempolimiterealizacaoprovapresencial, variavelnotacfgpadraoatividadediscursiva,variavelnotacfgpadraoavaliacaoonline,situacao ,instrucoesavaliacaoonline, calcularmediafinalaposrealizacaoatividadediscursiva, calcularmediafinalaposrealizacaoavaliacaoonline, variavelnotacfgpadraoprovapresencial, "); 
			sql.append("calcularmediafinalaposrealizacaoprovapresencial, permitiracessoconsultaconteudodisciplinaconclusaocurso, tempolimiteacessoconsultaconteudodisciplinaaposconclusao, notumprazoconclusaoestudos, notdoisprazoconclusaoestudos, nottresprazoconclusaoestudos, notificaraluno, notumdiassemlogarsistema, notdoisdiassemlogarsistema, nottresdiassemlogarsistema, notificaralunodiasficarsemlogarsistema, notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso, notUmAtividadeDiscursivaPrazoConclusaoCurso, notDoisAtividadeDiscursivaPrazoConclusaoCurso, ");
			sql.append("notTresAtividadeDiscursivaPrazoConclusaoCurso, notificarprofessorduvidasnaorespondidas, notificacaoprofessordiasduvidasnaorespondidas, notificarcoodenadorduvidasprofessornaorespondidas,  ");
			sql.append("notificargrupodestinatarioduvidasnaorespondidasprofessor, notificacaogrupodestinatariodiasduvidasnaorespondidas, grupodestinatarios, parametrosMonitoramentoAvaliacaoOnline, permitirAcessoEadSemConteudo )");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			configuracaoEADVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, configuracaoEADVO.getDescricao());
					sqlInserir.setString(2, configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().name());
					sqlInserir.setDouble(3, configuracaoEADVO.getTempoLimiteConclusaoDisciplina());
					sqlInserir.setInt(4, configuracaoEADVO.getTempoLimiteConclusaoTodasDisciplinas());
					sqlInserir.setInt(5, configuracaoEADVO.getTempoLimiteConclusaoCursoIncluindoTCC());
					sqlInserir.setBoolean(6, configuracaoEADVO.getControlarTempoLimiteRealizarProvaPresencial());
					sqlInserir.setInt(7, configuracaoEADVO.getTempoLimiteRealizacaoProvaPresencial());
					sqlInserir.setString(8, configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().name());
					sqlInserir.setInt(9, configuracaoEADVO.getValorControleLiberacaoAvalicaoOnline());
					sqlInserir.setInt(10, configuracaoEADVO.getNrVezesPodeRepetirAvaliacaoOnline());
					sqlInserir.setInt(11, configuracaoEADVO.getNrDiasEntreAvalicaoOnline());
					sqlInserir.setString(12, configuracaoEADVO.getOrdemEstudoDisciplinasOnline().name());
					sqlInserir.setInt(13, configuracaoEADVO.getNrMaximoDisciplinasSimultaneas());
					sqlInserir.setString(14, configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().name());
					sqlInserir.setInt(15, configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina());
					sqlInserir.setString(16, configuracaoEADVO.getTipoControleLiberacaoProvaPresencial().name());
					sqlInserir.setInt(17, configuracaoEADVO.getValorControleLiberacaoProvaPresencial());
					sqlInserir.setInt(18, configuracaoEADVO.getTempoLimiteRealizacaoProvaPresencial());
					if(configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva()) {
						sqlInserir.setString(19, configuracaoEADVO.getVariavelNotaCfgPadraoAtividadeDiscursiva());						
					} else {
						sqlInserir.setNull(19, 0);						
					}
					if(configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAvaliacaoOnline()) {
						sqlInserir.setString(20, configuracaoEADVO.getVariavelNotaCfgPadraoAvaliacaoOnline());						
					} else {
						sqlInserir.setNull(20, 0);						
					}
					sqlInserir.setString(21, configuracaoEADVO.getSituacao().name());
					sqlInserir.setString(22, configuracaoEADVO.getInstrucoesAvaliacaoOnline());
					sqlInserir.setBoolean(23, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva());
					sqlInserir.setBoolean(24, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAvaliacaoOnline());
					sqlInserir.setString(25, configuracaoEADVO.getVariavelNotaCfgPadraoProvaPresencial());
					sqlInserir.setBoolean(26, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoProvaPresencial());
					sqlInserir.setBoolean(27, configuracaoEADVO.getPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso());
					sqlInserir.setInt(28, configuracaoEADVO.getTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao());
					sqlInserir.setInt(29, configuracaoEADVO.getNotUmPrazoConclusaoEstudos());
					sqlInserir.setInt(30, configuracaoEADVO.getNotDoisPrazoConclusaoEstudos());
					sqlInserir.setInt(31, configuracaoEADVO.getNotTresPrazoConclusaoEstudos());
					sqlInserir.setBoolean(32, configuracaoEADVO.getNotificarAluno());
					sqlInserir.setInt(33, configuracaoEADVO.getNotUmDiasSemLogarSistema());
					sqlInserir.setInt(34, configuracaoEADVO.getNotDoisDiasSemLogarSistema());
					sqlInserir.setInt(35, configuracaoEADVO.getNotTresDiasSemLogarSistema());
					sqlInserir.setBoolean(36, configuracaoEADVO.getNotificarAlunoDiasFicarSemLogarSistema());
					sqlInserir.setBoolean(37, configuracaoEADVO.getNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso());
					sqlInserir.setInt(38, configuracaoEADVO.getNotUmAtividadeDiscursivaPrazoConclusaoCurso());
					sqlInserir.setInt(39, configuracaoEADVO.getNotDoisAtividadeDiscursivaPrazoConclusaoCurso());
					sqlInserir.setInt(40, configuracaoEADVO.getNotTresAtividadeDiscursivaPrazoConclusaoCurso());
					sqlInserir.setBoolean(41, configuracaoEADVO.getNotificarProfessorDuvidasNaoRespondidas());
					sqlInserir.setInt(42, configuracaoEADVO.getNotificacaoProfessorDiasDuvidasNaoRespondidas());
					sqlInserir.setBoolean(43, configuracaoEADVO.getNotificarCoodenadorDuvidasProfessorNaoRespondidas());
					sqlInserir.setBoolean(44, configuracaoEADVO.getNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor());
					sqlInserir.setInt(45, configuracaoEADVO.getNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas());
					if (configuracaoEADVO.getGrupoDestinatariosVO().getCodigo() != 0) {
						sqlInserir.setInt(46, configuracaoEADVO.getGrupoDestinatariosVO().getCodigo());
					} else {
						sqlInserir.setNull(46, 0);
					}
					if (configuracaoEADVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo() != 0) {
						sqlInserir.setInt(47, configuracaoEADVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
					} else {
						sqlInserir.setNull(47, 0);
					}
					sqlInserir.setBoolean(48, configuracaoEADVO.isPermitirAcessoEadSemConteudo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						configuracaoEADVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			configuracaoEADVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE configuracaoead ");
			sql.append("SET descricao=?, ");
			sql.append("tipocontroletempolimiteconclusaodisciplina=?, tempolimiteconclusaodisciplina=?, ");
			sql.append("tempolimiteconclusaotodasdisciplinas=?, tempolimiteconclusaocursoincluindotcc=?, ");
			sql.append("controlartempolimiterealizarprovapresencial=?, tempolimiterealizarprovapresencial=?, ");
			sql.append("tipocontroleliberacaoavaliacaoonline=?, valorcontroleliberacaoavaliacaoonline=?, ");
			sql.append("nrvezespoderepetiravaliacaoonline=?, nrdiasentreavaliacaoonline=?, ");
			sql.append("ordemestudodisciplinasonline=?, nrmaximodisciplinassimultaneas=?, ");
			sql.append("tipocontroleliberaracessoproximadisciplina=?, valorcontroleliberaracessoproximadisciplinas=?, ");
			sql.append("tipocontroleliberacaoprovapresencial=?, valorcontroleliberacaoprovapresencial=?, ");
			sql.append("tempolimiterealizacaoprovapresencial=?, variavelnotacfgpadraoatividadediscursiva=?,variavelnotacfgpadraoavaliacaoonline=?,situacao=?, ");
			sql.append("instrucoesavaliacaoonline = ?, calcularmediafinalaposrealizacaoatividadediscursiva = ?, calcularmediafinalaposrealizacaoavaliacaoonline = ?, variavelnotacfgpadraoprovapresencial = ?,  calcularmediafinalaposrealizacaoprovapresencial = ?, " + "permitiracessoconsultaconteudodisciplinaconclusaocurso = ?, tempolimiteacessoconsultaconteudodisciplinaaposconclusao = ?, notumprazoconclusaoestudos = ?, notdoisprazoconclusaoestudos = ?,  nottresprazoconclusaoestudos = ?, notificaraluno = ?, notificaralunodiasficarsemlogarsistema = ?, " + "notumdiassemlogarsistema = ?, notdoisdiassemlogarsistema = ?, nottresdiassemlogarsistema = ?, notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso = ?, notUmAtividadeDiscursivaPrazoConclusaoCurso = ?, notDoisAtividadeDiscursivaPrazoConclusaoCurso = ?, " + "notTresAtividadeDiscursivaPrazoConclusaoCurso = ?, notificarprofessorduvidasnaorespondidas = ?, notificacaoprofessordiasduvidasnaorespondidas = ?, ");
			sql.append("notificarcoodenadorduvidasprofessornaorespondidas = ?, notificargrupodestinatarioduvidasnaorespondidasprofessor = ?, notificacaogrupodestinatariodiasduvidasnaorespondidas = ?, grupodestinatarios = ?, ");
			sql.append("parametrosMonitoramentoAvaliacaoOnline = ?, permitirAcessoEadSemConteudo=? ");
			sql.append(" WHERE codigo = ?  " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, configuracaoEADVO.getDescricao());
					sqlAlterar.setString(2, configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().name());
					sqlAlterar.setDouble(3, configuracaoEADVO.getTempoLimiteConclusaoDisciplina());
					sqlAlterar.setInt(4, configuracaoEADVO.getTempoLimiteConclusaoTodasDisciplinas());
					sqlAlterar.setInt(5, configuracaoEADVO.getTempoLimiteConclusaoCursoIncluindoTCC());
					sqlAlterar.setBoolean(6, configuracaoEADVO.getControlarTempoLimiteRealizarProvaPresencial());
					sqlAlterar.setInt(7, configuracaoEADVO.getTempoLimiteRealizacaoProvaPresencial());
					sqlAlterar.setString(8, configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().name());
					sqlAlterar.setInt(9, configuracaoEADVO.getValorControleLiberacaoAvalicaoOnline());
					sqlAlterar.setInt(10, configuracaoEADVO.getNrVezesPodeRepetirAvaliacaoOnline());
					sqlAlterar.setInt(11, configuracaoEADVO.getNrDiasEntreAvalicaoOnline());
					sqlAlterar.setString(12, configuracaoEADVO.getOrdemEstudoDisciplinasOnline().name());
					sqlAlterar.setInt(13, configuracaoEADVO.getNrMaximoDisciplinasSimultaneas());
					sqlAlterar.setString(14, configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().name());
					sqlAlterar.setInt(15, configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina());
					sqlAlterar.setString(16, configuracaoEADVO.getTipoControleLiberacaoProvaPresencial().name());
					sqlAlterar.setInt(17, configuracaoEADVO.getValorControleLiberacaoProvaPresencial());
					sqlAlterar.setInt(18, configuracaoEADVO.getTempoLimiteRealizacaoProvaPresencial());
					if(configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva()) {
						sqlAlterar.setString(19, configuracaoEADVO.getVariavelNotaCfgPadraoAtividadeDiscursiva());						
					} else {
						sqlAlterar.setNull(19, 0);						
					}
					if(configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAvaliacaoOnline()) {
						sqlAlterar.setString(20, configuracaoEADVO.getVariavelNotaCfgPadraoAvaliacaoOnline());						
					} else {
						sqlAlterar.setNull(20, 0);						
					}
					sqlAlterar.setString(21, configuracaoEADVO.getSituacao().name());
					sqlAlterar.setString(22, configuracaoEADVO.getInstrucoesAvaliacaoOnline());
					sqlAlterar.setBoolean(23, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva());
					sqlAlterar.setBoolean(24, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoAvaliacaoOnline());
					sqlAlterar.setString(25, configuracaoEADVO.getVariavelNotaCfgPadraoProvaPresencial());
					sqlAlterar.setBoolean(26, configuracaoEADVO.getCalcularMediaFinalAposRealizacaoProvaPresencial());
					sqlAlterar.setBoolean(27, configuracaoEADVO.getPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso());
					sqlAlterar.setInt(28, configuracaoEADVO.getTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao());
					sqlAlterar.setInt(29, configuracaoEADVO.getNotUmPrazoConclusaoEstudos());
					sqlAlterar.setInt(30, configuracaoEADVO.getNotDoisPrazoConclusaoEstudos());
					sqlAlterar.setInt(31, configuracaoEADVO.getNotTresPrazoConclusaoEstudos());
					sqlAlterar.setBoolean(32, configuracaoEADVO.getNotificarAluno());
					sqlAlterar.setBoolean(33, configuracaoEADVO.getNotificarAlunoDiasFicarSemLogarSistema());
					sqlAlterar.setInt(34, configuracaoEADVO.getNotUmDiasSemLogarSistema());
					sqlAlterar.setInt(35, configuracaoEADVO.getNotDoisDiasSemLogarSistema());
					sqlAlterar.setInt(36, configuracaoEADVO.getNotTresDiasSemLogarSistema());
					sqlAlterar.setBoolean(37, configuracaoEADVO.getNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso());
					sqlAlterar.setInt(38, configuracaoEADVO.getNotUmAtividadeDiscursivaPrazoConclusaoCurso());
					sqlAlterar.setInt(39, configuracaoEADVO.getNotDoisAtividadeDiscursivaPrazoConclusaoCurso());
					sqlAlterar.setInt(40, configuracaoEADVO.getNotTresAtividadeDiscursivaPrazoConclusaoCurso());
					sqlAlterar.setBoolean(41, configuracaoEADVO.getNotificarProfessorDuvidasNaoRespondidas());
					sqlAlterar.setInt(42, configuracaoEADVO.getNotificacaoProfessorDiasDuvidasNaoRespondidas());
					sqlAlterar.setBoolean(43, configuracaoEADVO.getNotificarCoodenadorDuvidasProfessorNaoRespondidas());
					sqlAlterar.setBoolean(44, configuracaoEADVO.getNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor());
					sqlAlterar.setInt(45, configuracaoEADVO.getNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas());
					if (configuracaoEADVO.getGrupoDestinatariosVO().getCodigo() != 0) {
						sqlAlterar.setInt(46, configuracaoEADVO.getGrupoDestinatariosVO().getCodigo());
					} else {
						sqlAlterar.setNull(46, 0);
					}
					if (configuracaoEADVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo() != 0) {
						sqlAlterar.setInt(47, configuracaoEADVO.getParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo());
					} else {
						sqlAlterar.setNull(47, 0);
					}
					sqlAlterar.setBoolean(48, configuracaoEADVO.isPermitirAcessoEadSemConteudo());
					sqlAlterar.setInt(49, configuracaoEADVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(configuracaoEADVO, controlarAcesso, usuarioVO);
				return;
			}
			configuracaoEADVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluir(final ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder("DELETE FROM ConfiguracaoEAD where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), configuracaoEADVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ConfiguracaoEADVO consultarPorConfiguracao(Integer configuracao) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM ConfiguracaoEAD where configuracoes = ?", configuracao);
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return new ConfiguracaoEADVO();
	}

	public List<ConfiguracaoEADVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ConfiguracaoEADVO> configuracaoEADVOs = new ArrayList<ConfiguracaoEADVO>(0);
		while (tabelaResultado.next()) {
			configuracaoEADVOs.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return configuracaoEADVOs;
	}

	public ConfiguracaoEADVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) {
		ConfiguracaoEADVO obj = new ConfiguracaoEADVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoControleTempoLimiteConclusaoDisciplina"))) {
			obj.setTipoControleTempoLimiteConclusaoDisciplina(TipoControleTempoLimiteConclusaoDisciplinaEnum.valueOf(tabelaResultado.getString("tipoControleTempoLimiteConclusaoDisciplina")));
		}
		obj.setTempoLimiteConclusaoDisciplina(tabelaResultado.getDouble("tempoLimiteConclusaoDisciplina"));
		obj.setTempoLimiteConclusaoTodasDisciplinas(tabelaResultado.getInt("tempoLimiteConclusaoTodasDisciplinas"));
		obj.setTempoLimiteConclusaoCursoIncluindoTCC(tabelaResultado.getInt("tempoLimiteConclusaoCursoIncluindoTcc"));
		obj.setControlarTempoLimiteRealizarProvaPresencial(tabelaResultado.getBoolean("controlarTempoLimiteRealizarProvaPresencial"));
		obj.setTempoLimiteRealizacaoProvaPresencial(tabelaResultado.getInt("tempoLimiteRealizacaoProvaPresencial"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoControleLiberacaoAvaliacaoOnline"))) {
			obj.setTipoControleLiberacaoAvaliacaoOnline(TipoControleLiberacaoAvaliacaoOnlineEnum.valueOf(tabelaResultado.getString("tipoControleLiberacaoAvaliacaoOnline")));
		}
		obj.setValorControleLiberacaoAvalicaoOnline(tabelaResultado.getInt("valorcontroleliberacaoavaliacaoonline"));
		obj.setNrVezesPodeRepetirAvaliacaoOnline(tabelaResultado.getInt("nrvezespoderepetiravaliacaoonline"));
		obj.setNrDiasEntreAvalicaoOnline(tabelaResultado.getInt("nrdiasentreavaliacaoonline"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("ordemestudodisciplinasonline"))) {
			obj.setOrdemEstudoDisciplinasOnline(OrdemEstudoDisciplinasOnlineEnum.valueOf(tabelaResultado.getString("ordemEstudoDisciplinasOnline")));
		}
		obj.setNrMaximoDisciplinasSimultaneas(tabelaResultado.getInt("nrmaximodisciplinassimultaneas"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoControleLiberarAcessoProximaDisciplina"))) {
			obj.setTipoControleLiberarAcessoProximaDisciplina(TipoControleLiberarAcessoProximaDisciplinaEnum.valueOf(tabelaResultado.getString("tipoControleLiberarAcessoProximaDisciplina")));
		}
		obj.setValorControleLiberarAcessoProximaDisciplina(tabelaResultado.getInt("valorControleLiberarAcessoProximaDisciplinas"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoControleLiberacaoProvaPresencial"))) {
			obj.setTipoControleLiberacaoProvaPresencial(TipoControleLiberacaoProvaPresencialEnum.valueOf(tabelaResultado.getString("tipoControleLiberacaoProvaPresencial")));
		}
		obj.setValorControleLiberacaoProvaPresencial(tabelaResultado.getInt("valorControleLiberacaoProvaPresencial"));
		obj.setTempoLimiteRealizacaoProvaPresencial(tabelaResultado.getInt("tempoLimiteRealizacaoProvaPresencial"));
		obj.setVariavelNotaCfgPadraoAtividadeDiscursiva(tabelaResultado.getString("variavelNotaCfgPadraoAtividadeDiscursiva"));
		obj.setVariavelNotaCfgPadraoAvaliacaoOnline(tabelaResultado.getString("variavelNotaCfgPadraoAvaliacaoOnline"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
		}
		obj.setInstrucoesAvaliacaoOnline(tabelaResultado.getString("instrucoesavaliacaoonline"));
		obj.setCalcularMediaFinalAposRealizacaoAtividadeDiscursiva(tabelaResultado.getBoolean("calcularmediafinalaposrealizacaoatividadediscursiva"));
		obj.setCalcularMediaFinalAposRealizacaoAvaliacaoOnline(tabelaResultado.getBoolean("calcularmediafinalaposrealizacaoavaliacaoonline"));
		obj.setVariavelNotaCfgPadraoProvaPresencial(tabelaResultado.getString("variavelnotacfgpadraoprovapresencial"));
		obj.setCalcularMediaFinalAposRealizacaoProvaPresencial(tabelaResultado.getBoolean("calcularmediafinalaposrealizacaoprovapresencial"));
		obj.setPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso(tabelaResultado.getBoolean("permitiracessoconsultaconteudodisciplinaconclusaocurso"));
		obj.setTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao(tabelaResultado.getInt("tempolimiteacessoconsultaconteudodisciplinaaposconclusao"));
		obj.setNotUmPrazoConclusaoEstudos(tabelaResultado.getInt("notumprazoconclusaoestudos"));
		obj.setNotDoisPrazoConclusaoEstudos(tabelaResultado.getInt("notdoisprazoconclusaoestudos"));
		obj.setNotTresPrazoConclusaoEstudos(tabelaResultado.getInt("nottresprazoconclusaoestudos"));
		obj.setNotificarAluno(tabelaResultado.getBoolean("notificaraluno"));
		obj.setNotificarAlunoDiasFicarSemLogarSistema(tabelaResultado.getBoolean("notificaralunodiasficarsemlogarsistema"));
		obj.setNotUmDiasSemLogarSistema(tabelaResultado.getInt("notumdiassemlogarsistema"));
		obj.setNotDoisDiasSemLogarSistema(tabelaResultado.getInt("notdoisdiassemlogarsistema"));
		obj.setNotTresDiasSemLogarSistema(tabelaResultado.getInt("nottresdiassemlogarsistema"));
		obj.setNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso(tabelaResultado.getBoolean("notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso"));
		obj.setNotUmAtividadeDiscursivaPrazoConclusaoCurso(tabelaResultado.getInt("notUmAtividadeDiscursivaPrazoConclusaoCurso"));
		obj.setNotDoisAtividadeDiscursivaPrazoConclusaoCurso(tabelaResultado.getInt("notDoisAtividadeDiscursivaPrazoConclusaoCurso"));
		obj.setNotTresAtividadeDiscursivaPrazoConclusaoCurso(tabelaResultado.getInt("notTresAtividadeDiscursivaPrazoConclusaoCurso"));
		obj.setNotificarProfessorDuvidasNaoRespondidas(tabelaResultado.getBoolean("notificarprofessorduvidasnaorespondidas"));
		obj.setNotificacaoProfessorDiasDuvidasNaoRespondidas(tabelaResultado.getInt("notificacaoprofessordiasduvidasnaorespondidas"));
		obj.setNotificarCoodenadorDuvidasProfessorNaoRespondidas(tabelaResultado.getBoolean("notificarcoodenadorduvidasprofessornaorespondidas"));
		obj.setNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor(tabelaResultado.getBoolean("notificargrupodestinatarioduvidasnaorespondidasprofessor"));
		obj.setPermitirAcessoEadSemConteudo(tabelaResultado.getBoolean("permitirAcessoEadSemConteudo"));
		obj.setNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas(tabelaResultado.getInt("notificacaogrupodestinatariodiasduvidasnaorespondidas"));
		obj.getGrupoDestinatariosVO().setCodigo(tabelaResultado.getInt("grupodestinatarios"));
		obj.getParametrosMonitoramentoAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("parametrosMonitoramentoAvaliacaoOnline"));

		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		return obj;
	}

	@Override
	public ConfiguracaoEADVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM ConfiguracaoEAD where codigo = " + codigo);
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return new ConfiguracaoEADVO();
	}

	@Override
	public List<ConfiguracaoEADVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM configuracaoead WHERE descricao ilike('").append(descricao).append("%') order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);

	}

	@Override
	public ConfiguracaoEADVO consultarConfiguracaoEADASerUsada(int nivelMontarDados, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, nivelMontarDados, usuario, unidadeEnsino);
		if (configuracoesVO != null) {
			return consultarPorConfiguracao(configuracoesVO.getCodigo());
		}
		return new ConfiguracaoEADVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoConfiguracaoEAD(final Integer codigo, final String situacao, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE configuracaoead set situacao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

					sqlAlterar.setString(1, situacao);
					sqlAlterar.setInt(2, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConfiguracaoEADVO> consultarConfiguracoesEADAtivasTurma(Integer codigoConfiguracaoEADTurma) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("select codigo, descricao");
		sqlStr.append(" from configuracaoead where situacao like 'ATIVO'");
		if(!codigoConfiguracaoEADTurma.equals(0)) {
			sqlStr.append(" union ");
			sqlStr.append("select codigo, descricao");
			sqlStr.append(" from configuracaoead where codigo = ").append(codigoConfiguracaoEADTurma);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoEADVO obj = new ConfiguracaoEADVO();
		List<ConfiguracaoEADVO> list = new ArrayList<ConfiguracaoEADVO>();
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));

			list.add(obj);
			obj = new ConfiguracaoEADVO();
		}
		return list;
	}

	@Override
	public ConfiguracaoEADVO consultarConfiguracaoEADPorTurma(Integer codigoTurma) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("select * from configuracaoead ");
		sqlStr.append(" inner join turma on turma.configuracaoead = configuracaoead.codigo ");
		sqlStr.append(" where turma.codigo =").append(codigoTurma);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return new ConfiguracaoEADVO();
	}
	
	@Override
	public boolean validarConfiguracaoEadInformadaParaTurma(Integer codigoTurma) throws Exception {		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(1) as qtd from configuracaoead ");
		sqlStr.append(" inner join turma on turma.configuracaoead = configuracaoead.codigo ");
		sqlStr.append(" where turma.codigo =").append(codigoTurma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtd") > 0 ? true : false;
		}
		return false;
	}
	
	@Override
	public boolean validarConfiguracaoEadPermitirAcessoSemConteudo(Integer codigoTurma) throws Exception {		
		StringBuilder sqlStr = new StringBuilder();
		Boolean qtd = null;
		sqlStr.append("select permitirAcessoEadSemConteudo from configuracaoead ");
		sqlStr.append(" inner join turma on turma.configuracaoead = configuracaoead.codigo ");
		sqlStr.append(" where turma.codigo =").append(codigoTurma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			qtd = tabelaResultado.getBoolean("permitirAcessoEadSemConteudo");
		}
		return qtd == null || !qtd ? false : true;
		
	}
	
	@Override
	public String consultarVariavelNotaCfgPadraoAvaliacaoOnlineDaConfiguracaoEadPorTurma(Integer codigoTurma) throws Exception {		
		StringBuilder sqlStr = new StringBuilder();
		Boolean qtd = null;
		sqlStr.append("select variavelNotaCfgPadraoAvaliacaoOnline from configuracaoead ");
		sqlStr.append(" inner join turma on turma.configuracaoead = configuracaoead.codigo ");
		sqlStr.append(" where turma.codigo =").append(codigoTurma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("variavelNotaCfgPadraoAvaliacaoOnline");
		}
		return "";
		
	}
	
	@Override
	public ConfiguracaoEADVO clonarConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) throws Exception {
		ConfiguracaoEADVO cloneConfiguracaoEADVO = (ConfiguracaoEADVO) configuracaoEADVO.clone();
		cloneConfiguracaoEADVO.setDescricao(configuracaoEADVO.getDescricao() + "- CLONE");
		cloneConfiguracaoEADVO.setCodigo(0);
		cloneConfiguracaoEADVO.setNovoObj(true);
		cloneConfiguracaoEADVO.setSituacao(SituacaoEnum.EM_CONSTRUCAO);
		
		return cloneConfiguracaoEADVO;
	}
}
