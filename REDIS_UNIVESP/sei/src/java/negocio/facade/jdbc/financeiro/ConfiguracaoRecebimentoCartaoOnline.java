package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConfiguracaoRecebimentoCartaoOnlineInterfaceFacade;

/**
 * @author Victor Hugo 08/03/2016 5.0.4.0
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoRecebimentoCartaoOnline extends ControleAcesso implements ConfiguracaoRecebimentoCartaoOnlineInterfaceFacade {
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
		ConfiguracaoRecebimentoCartaoOnline.idEntidade = idEntidade;
	}

	public ConfiguracaoRecebimentoCartaoOnline() {
		super();
		setIdEntidade("ConfiguracaoRecebimentoCartaoOnline");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(configuracaoRecebimentoCartaoOnlineVO, usuarioVO);
			ConfiguracaoRecebimentoCartaoOnline.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO configuracaorecebimentocartaoonline(");
			sql.append("            descricao, situacao, tiponivelconfiguracaorecebimentocartaoonline, ");
			sql.append("            unidadeensino, tiponiveleducacional, curso, turma, ");
			sql.append("            usarconfiguracaovisaoalunopais, ");
			sql.append("            usarconfiguracaovisaocandidato, usarconfiguracaovisaoadministrativa, ");
			sql.append("            valorminimorecebimentocartaocredito, permiterecebercontabiblioteca, ");
			sql.append("            permiterecebercontacontratoreceita, permiterecebercontaoutros, ");
			sql.append("            permiterecebercontadevolucaocheque, permiterecebercontainclusaoreposicao, ");
			sql.append("            permiterecebercontainscricaoprocessoseletivo, permiterecebercontarequerimento, ");
			sql.append("            permiterecebercontanegociacao, habilitarrenegociacaoonline, permiterecebercontamatricula, ");
			sql.append("            habilitarrecebimentomatriculaonline, habilitarrecebimentomatricularenovacaoonline, ");
			sql.append("            permiterecebercontamensalidade, habilitarrecebimentomensalidadematriculaonline, ");
			sql.append("            habilitarrecebimentomensalidaderenovacaoonline, datacriacao, ");
			sql.append("            responsavelcriacao, permitirReceberContaVencida, numeroMaximoDiasReceberContaVencida, configuracaofinanceira, permiterecebercontamaterialdidatico, ");
			sql.append("            numeroMaximoDiasReceberContaVencidaDebito, valorMinimoRecebimentoCartaoDebito, permitirReceberContaVencidaDebito, permiteReceberContaContratoReceitaDebito, permiteReceberContaOutrosDebito, permiteReceberContaDevolucaoChequeDebito, ");
			sql.append("            permiteReceberContaInclusaoReposicaoDebito, permiteReceberContaInscricaoProcessoSeletivoDebito, permiteReceberContaRequerimentoDebito, permiteReceberContaNegociacaoDebito, permiteReceberContaMatriculaDebito, permiteReceberContaMensalidadeDebito, ");
			sql.append("            permiteReceberContaMaterialDidaticoDebito, habilitarRenegociacaoOnlineDebito, habilitarRecebimentoMatriculaOnlineDebito, habilitarRecebimentoMatriculaRenovacaoOnlineDebito, ");
			sql.append("			habilitarRecebimentoMensalidadeMatriculaOnlineDebito, habilitarRecebimentoMensalidadeRenovacaoOnlineDebito, permiteReceberContaBibliotecaDebito, permitirCartao,");
			sql.append("            apresentarOpcaoRecorrenciaAluno, utilizarOpcaoRecorrenciaDefaulMarcado, formaPadraoDataBaseCartaoRecorrente, orientacaoRecorrenciaAluno, exigirConfirmacaoRecorrencia, mensagemConfirmacaoRecorrencia)");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
			sql.append("            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			configuracaoRecebimentoCartaoOnlineVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 1;
					sqlInserir.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getDescricao());
					sqlInserir.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getSituacao().getName());
					sqlInserir.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().getName());
					if(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo().equals(0)) {
						sqlInserir.setNull(i++, 0);						
					} else {
						sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo());						
					}
					if(!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL)) {
						sqlInserir.setNull(i++, 0);						
					} else {
						sqlInserir.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getTipoNivelEducacional().name());						
					}
					if(configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo().equals(0) || (!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO))) {
						if((!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA))) {
							sqlInserir.setNull(i++, 0);													
						} else {
							sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo());						
						}
					} else {
						sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo());						
					}
					if(configuracaoRecebimentoCartaoOnlineVO.getTurmaVO().getCodigo().equals(0) || (!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA))) {
						sqlInserir.setNull(i++, 0);						
					} else {
						sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getTurmaVO().getCodigo());						
					}
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoAlunoPais());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoCandidato());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoAdministrativa());
					sqlInserir.setDouble(i++, configuracaoRecebimentoCartaoOnlineVO.getValorMinimoRecebimentoCartaoCredito());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBiblioteca());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceita());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutros());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoCheque());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicao());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivo());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimento());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacao());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnline());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatricula());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnline());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidade());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnline());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeRenovacaoOnline());
					sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(configuracaoRecebimentoCartaoOnlineVO.getDataCriacao()));
					sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getReposnsavelCriacao().getCodigo());
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencida());
					if(configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencida()) {
						sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencida());						
					} else {
						sqlInserir.setNull(i++, 0);						
					}
					if(!Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO().getCodigo())){
						sqlInserir.setNull(i++, 0);
					}else{
						sqlInserir.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO().getCodigo());
					}
					sqlInserir.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMaterialDidatico());
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencidaDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getValorMinimoRecebimentoCartaoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencidaDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceitaDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutrosDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoChequeDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicaoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimentoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacaoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatriculaDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidadeDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMaterialDidaticoDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnlineDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnlineDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnlineDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnlineDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeRenovacaoOnlineDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBibliotecaDebito(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermitirCartao(), i++, sqlInserir);
					
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getApresentarOpcaoRecorrenciaAluno(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getUtilizarOpcaoRecorrenciaDefaulMarcado(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getFormaPadraoDataBaseCartaoRecorrente(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getOrientacaoRecorrenciaAluno(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getExigirConfirmacaoRecorrencia(), i++, sqlInserir);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getMensagemConfirmacaoRecorrencia(), i++, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						configuracaoRecebimentoCartaoOnlineVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().persistirRebimentoAdministrativoVOs(configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs(), false, usuarioVO);
		} catch (Exception e) {
			configuracaoRecebimentoCartaoOnlineVO.setNovoObj(Boolean.TRUE);
			configuracaoRecebimentoCartaoOnlineVO.setCodigo(0);
			throw e;
		}
	}

	public void validarDados(ConfiguracaoRecebimentoCartaoOnlineVO obj, UsuarioVO usuarioLogado) throws ConsistirException {
		ConsistirException ce = new ConsistirException();
		try {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(obj.getConfiguracaoFinanceiroVO().getConfiguracoesVO().getCodigo(), true, usuarioLogado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			if(!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)){
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_configuracaoFinanceiroObrigatorio"));
			}
			if(Uteis.isAtributoPreenchido(obj.getConfiguracaoFinanceiroVO()) && !Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas())){
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_deveSerCadastradoUmResponsavelOperacoesExternas"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(verificarUnicidadeCampos(obj, usuarioLogado)){
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_verificarUnicidade"));
		}
		if(obj.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO)) {
			if(obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_unidadeEnsinoObrigatorio"));
			}
		} else if(obj.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL)) {
			if(obj.getTipoNivelEducacional().getValor().isEmpty()) {
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_nivelEducacaoObrigatorio"));
			}
		} else if(obj.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO)) {
			if(obj.getCursoVO().getCodigo().equals(0)) {
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_cursoObrigatorio"));
			}
		} else if(obj.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA)) {
			if(obj.getTurmaVO().getCodigo().equals(0)) {
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_turmaObrigatorio"));
			}
		}
		if(obj.getDescricao().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_descricaoObrigatorio"));
		}
		if(obj.getUsarConfiguracaoVisaoAdministrativa() || obj.getUsarConfiguracaoVisaoAlunoPais()){
			if (PermitirCartaoEnum.AMBOS.equals(obj.getPermitirCartao()) ||	PermitirCartaoEnum.CREDITO.equals(obj.getPermitirCartao())) {
				if(obj.getPermitirReceberContaVencida()) {
					if(obj.getNumeroMaximoDiasReceberContaVencida().equals(0)) {
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_numeroMaximoDiasReceberContaVencidaObrigatorio"));
					}
				}
			}
			if (PermitirCartaoEnum.AMBOS.equals(obj.getPermitirCartao()) || PermitirCartaoEnum.DEBITO.equals(obj.getPermitirCartao())) {
				if(obj.getPermitirReceberContaVencidaDebito()) {
					if(obj.getNumeroMaximoDiasReceberContaVencidaDebito().equals(0)) {
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_numeroMaximoDiasReceberContaVencidaObrigatorioDebito"));
					}
				}
			}
		}
		if(obj.getUsarConfiguracaoVisaoAdministrativa() || obj.getUsarConfiguracaoVisaoAlunoPais()){
			if (PermitirCartaoEnum.AMBOS.equals(obj.getPermitirCartao()) ||	PermitirCartaoEnum.CREDITO.equals(obj.getPermitirCartao())) {
				if(obj.getPermiteReceberContaNegociacao() || obj.getPermiteReceberContaMatricula() || obj.getPermiteReceberContaMensalidade()) {
					if(obj.getConfiguracaoFinanceiroCartaoRecebimentoVOs().isEmpty()) {
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_regrasParcelamentoObrigatorio"));
					}
				}
			}
			if (PermitirCartaoEnum.AMBOS.equals(obj.getPermitirCartao()) || PermitirCartaoEnum.DEBITO.equals(obj.getPermitirCartao())) {
				if(obj.getPermiteReceberContaNegociacaoDebito() || obj.getPermiteReceberContaMatriculaDebito() || obj.getPermiteReceberContaMensalidadeDebito()) {
					if(obj.getConfiguracaoFinanceiroCartaoRecebimentoVOs().isEmpty()) {
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_regrasParcelamentoObrigatorioDebito"));
					}
				}
			}
		}
		if (!ce.getListaMensagemErro().isEmpty()) {
			throw ce;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoRecebimentoCartaoOnlineVO.getCodigo() == 0) {
			configuracaoRecebimentoCartaoOnlineVO.setReposnsavelCriacao(usuarioVO);
			incluir(configuracaoRecebimentoCartaoOnlineVO, verificarAcesso, usuarioVO);
		} else {
			alterar(configuracaoRecebimentoCartaoOnlineVO, verificarAcesso, usuarioVO);
		}
		getAplicacaoControle().removerConfiguracaoRecebimentoCartaoOnline(configuracaoRecebimentoCartaoOnlineVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(configuracaoRecebimentoCartaoOnlineVO, usuarioVO);
			ConfiguracaoRecebimentoCartaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE configuracaorecebimentocartaoonline");
			sql.append("   SET descricao=?, situacao=?, tiponivelconfiguracaorecebimentocartaoonline=?, ");
			sql.append("       unidadeensino=?, tiponiveleducacional=?, curso=?, turma=?, ");
			sql.append("       usarconfiguracaovisaoalunopais=?, ");
			sql.append("       usarconfiguracaovisaocandidato=?, usarconfiguracaovisaoadministrativa=?, ");
			sql.append("       valorminimorecebimentocartaocredito=?, permiterecebercontabiblioteca=?, ");
			sql.append("       permiterecebercontacontratoreceita=?, permiterecebercontaoutros=?, ");
			sql.append("       permiterecebercontadevolucaocheque=?, permiterecebercontainclusaoreposicao=?, ");
			sql.append("       permiterecebercontainscricaoprocessoseletivo=?, permiterecebercontarequerimento=?, ");
			sql.append("       permiterecebercontanegociacao=?, habilitarrenegociacaoonline=?, ");
			sql.append("       permiterecebercontamatricula=?, habilitarrecebimentomatriculaonline=?, ");
			sql.append("       habilitarrecebimentomatricularenovacaoonline=?, permiterecebercontamensalidade=?, ");
			sql.append("       habilitarrecebimentomensalidadematriculaonline=?, habilitarrecebimentomensalidaderenovacaoonline=?, ");
			sql.append("	   permitirReceberContaVencida=?, numeroMaximoDiasReceberContaVencida=?, configuracaofinanceira=?, permiterecebercontamaterialdidatico=?, ");
			sql.append("       numeroMaximoDiasReceberContaVencidaDebito=?, valorMinimoRecebimentoCartaoDebito=?, permitirReceberContaVencidaDebito=?, permiteReceberContaContratoReceitaDebito=?, permiteReceberContaOutrosDebito=?, permiteReceberContaDevolucaoChequeDebito=?, ");
			sql.append("       permiteReceberContaInclusaoReposicaoDebito=?, permiteReceberContaInscricaoProcessoSeletivoDebito=?, permiteReceberContaRequerimentoDebito=?, permiteReceberContaNegociacaoDebito=?, permiteReceberContaMatriculaDebito=?, permiteReceberContaMensalidadeDebito=?, ");
			sql.append("       permiteReceberContaMaterialDidaticoDebito=?, habilitarRenegociacaoOnlineDebito=?, habilitarRecebimentoMatriculaOnlineDebito=?, habilitarRecebimentoMatriculaRenovacaoOnlineDebito=?, ");
			sql.append("	   habilitarRecebimentoMensalidadeMatriculaOnlineDebito=?, habilitarRecebimentoMensalidadeRenovacaoOnlineDebito=?, permiteReceberContaBibliotecaDebito=?, permitirCartao=?, ");
			sql.append("       apresentarOpcaoRecorrenciaAluno=?, utilizarOpcaoRecorrenciaDefaulMarcado=?, formaPadraoDataBaseCartaoRecorrente=?, orientacaoRecorrenciaAluno=?, exigirConfirmacaoRecorrencia=?, mensagemConfirmacaoRecorrencia=? ");
			sql.append(" WHERE configuracaorecebimentocartaoonline.codigo = ? ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 1;
					sqlAlterar.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getDescricao());
					sqlAlterar.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getSituacao().getName());
					sqlAlterar.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().getName());
					if(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(i++, 0);						
					} else {
						sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo());						
					}
					if(!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL)) {
						sqlAlterar.setNull(i++, 0);						
					} else {
						sqlAlterar.setString(i++, configuracaoRecebimentoCartaoOnlineVO.getTipoNivelEducacional().name());						
					}
					if(configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo().equals(0) || (!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO))) {
						if((!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA))) {
							sqlAlterar.setNull(i++, 0);													
						} else {
							sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo());						
						}
					} else {
						sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo());						
					}
					if(configuracaoRecebimentoCartaoOnlineVO.getTurmaVO().getCodigo().equals(0) || (!configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA))) {
						sqlAlterar.setNull(i++, 0);						
					} else {
						sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getTurmaVO().getCodigo());						
					}
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoAlunoPais());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoCandidato());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getUsarConfiguracaoVisaoAdministrativa());
					sqlAlterar.setDouble(i++, configuracaoRecebimentoCartaoOnlineVO.getValorMinimoRecebimentoCartaoCredito());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBiblioteca());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceita());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutros());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoCheque());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicao());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivo());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimento());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacao());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnline());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatricula());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnline());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidade());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnline());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeRenovacaoOnline());
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencida());
					if(configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencida()) {
						sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencida());						
					} else {
						sqlAlterar.setNull(i++, 0);						
					}
					if(!Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO().getCodigo())){
						sqlAlterar.setNull(i++, 0);
					}else{
						sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO().getCodigo());
					}
					sqlAlterar.setBoolean(i++, configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMaterialDidatico());
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencidaDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getValorMinimoRecebimentoCartaoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencidaDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceitaDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutrosDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoChequeDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicaoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimentoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacaoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatriculaDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidadeDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMaterialDidaticoDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnlineDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnlineDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnlineDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnlineDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeRenovacaoOnlineDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBibliotecaDebito(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getPermitirCartao(), i++, sqlAlterar);
					
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getApresentarOpcaoRecorrenciaAluno(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getUtilizarOpcaoRecorrenciaDefaulMarcado(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getFormaPadraoDataBaseCartaoRecorrente(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getOrientacaoRecorrenciaAluno(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getExigirConfirmacaoRecorrencia(), i++, sqlAlterar);
					Uteis.setValuePreparedStatement(configuracaoRecebimentoCartaoOnlineVO.getMensagemConfirmacaoRecorrencia(), i++, sqlAlterar);
					sqlAlterar.setInt(i++, configuracaoRecebimentoCartaoOnlineVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(configuracaoRecebimentoCartaoOnlineVO, false, usuarioVO);
			};
			getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().persistirRebimentoAdministrativoVOs(configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs(), false, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoRecebimentoCartaoOnline.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().excluirPorCodigoConfiguracaoRecebimentoCartaoOnline(configuracaoRecebimentoCartaoOnlineVO.getCodigo(), false, usuarioVO);
			String sql = "DELETE FROM ConfiguracaoRecebimentoCartaoOnline WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, configuracaoRecebimentoCartaoOnlineVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public ConfiguracaoRecebimentoCartaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoRecebimentoCartaoOnlineVO obj = new ConfiguracaoRecebimentoCartaoOnlineVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.setTipoNivelConfiguracaoRecebimentoCartaoOnline(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.valueOf(tabelaResultado.getString("tipoNivelConfiguracaoRecebimentoCartaoOnline")));
		obj.setConfiguracaoFinanceiroVO(getAplicacaoControle().getConfiguracaoFinanceiroPorCodigoVO(tabelaResultado.getInt("configuracaofinanceira")));		
		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeensino"))) {
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("unidadeensino"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		if(Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoNivelEducacional"))) {
			obj.setTipoNivelEducacional(TipoNivelEducacional.valueOf(tabelaResultado.getString("tipoNivelEducacional")));
		}
		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("curso"))) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("curso"), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioLogado));
		}
		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("turma"))) {
			obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turma"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		obj.setUsarConfiguracaoVisaoAlunoPais(tabelaResultado.getBoolean("usarConfiguracaoVisaoAlunoPais"));
		obj.setUsarConfiguracaoVisaoCandidato(tabelaResultado.getBoolean("usarConfiguracaoVisaoCandidato"));
		obj.setUsarConfiguracaoVisaoAdministrativa(tabelaResultado.getBoolean("usarConfiguracaoVisaoAdministrativa"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setValorMinimoRecebimentoCartaoCredito(tabelaResultado.getDouble("valorMinimoRecebimentoCartaoCredito"));
		obj.setPermiteReceberContaBiblioteca(tabelaResultado.getBoolean("permiteReceberContaBiblioteca"));
		obj.setPermiteReceberContaContratoReceita(tabelaResultado.getBoolean("permiteReceberContaContratoReceita"));
		obj.setPermiteReceberContaOutros(tabelaResultado.getBoolean("permiteReceberContaOutros"));
		obj.setPermiteReceberContaDevolucaoCheque(tabelaResultado.getBoolean("permiteReceberContaDevolucaoCheque"));
		obj.setPermiteReceberContaInclusaoReposicao(tabelaResultado.getBoolean("permiteReceberContaInclusaoReposicao"));
		obj.setPermiteReceberContaInscricaoProcessoSeletivo(tabelaResultado.getBoolean("permiteReceberContaInscricaoProcessoSeletivo"));
		obj.setPermiteReceberContaRequerimento(tabelaResultado.getBoolean("permiteReceberContaRequerimento"));
		obj.setPermiteReceberContaNegociacao(tabelaResultado.getBoolean("permiteReceberContaNegociacao"));
		obj.setHabilitarRenegociacaoOnline(tabelaResultado.getBoolean("habilitarRenegociacaoOnline"));
		obj.setPermiteReceberContaMatricula(tabelaResultado.getBoolean("permiteReceberContaMatricula"));
		obj.setPermiteReceberContaMaterialDidatico(tabelaResultado.getBoolean("permiterecebercontamaterialdidatico"));
		obj.setHabilitarRecebimentoMatriculaOnline(tabelaResultado.getBoolean("habilitarRecebimentoMatriculaOnline"));
		obj.setHabilitarRecebimentoMatriculaRenovacaoOnline(tabelaResultado.getBoolean("habilitarRecebimentoMatriculaRenovacaoOnline"));
		obj.setPermiteReceberContaMensalidade(tabelaResultado.getBoolean("permiteReceberContaMensalidade"));
		obj.setHabilitarRecebimentoMensalidadeMatriculaOnline(tabelaResultado.getBoolean("habilitarRecebimentoMensalidadeMatriculaOnline"));
		obj.setHabilitarRecebimentoMensalidadeRenovacaoOnline(tabelaResultado.getBoolean("habilitarRecebimentoMensalidadeRenovacaoOnline"));
		obj.setPermitirReceberContaVencida(tabelaResultado.getBoolean("permitirrecebercontavencida"));
		obj.setNumeroMaximoDiasReceberContaVencida(tabelaResultado.getInt("numeromaximodiasrecebercontavencida"));
		
		obj.setValorMinimoRecebimentoCartaoDebito(tabelaResultado.getDouble("valorMinimoRecebimentoCartaoDebito"));
		obj.setPermiteReceberContaBibliotecaDebito(tabelaResultado.getBoolean("permiteReceberContaBibliotecaDebito"));
		obj.setPermiteReceberContaContratoReceitaDebito(tabelaResultado.getBoolean("permiteReceberContaContratoReceitaDebito"));
		obj.setPermiteReceberContaOutrosDebito(tabelaResultado.getBoolean("permiteReceberContaOutrosDebito"));
		obj.setPermiteReceberContaDevolucaoChequeDebito(tabelaResultado.getBoolean("permiteReceberContaDevolucaoChequeDebito"));
		obj.setPermiteReceberContaInclusaoReposicaoDebito(tabelaResultado.getBoolean("permiteReceberContaInclusaoReposicaoDebito"));
		obj.setPermiteReceberContaInscricaoProcessoSeletivoDebito(tabelaResultado.getBoolean("permiteReceberContaInscricaoProcessoSeletivoDebito"));
		obj.setPermiteReceberContaRequerimentoDebito(tabelaResultado.getBoolean("permiteReceberContaRequerimentoDebito"));
		obj.setPermiteReceberContaNegociacaoDebito(tabelaResultado.getBoolean("permiteReceberContaNegociacaoDebito"));
		obj.setHabilitarRenegociacaoOnlineDebito(tabelaResultado.getBoolean("habilitarRenegociacaoOnlineDebito"));
		obj.setPermiteReceberContaMatriculaDebito(tabelaResultado.getBoolean("permiteReceberContaMatriculaDebito"));
		obj.setPermiteReceberContaMaterialDidaticoDebito(tabelaResultado.getBoolean("permiterecebercontamaterialdidaticoDebito"));
		obj.setHabilitarRecebimentoMatriculaOnlineDebito(tabelaResultado.getBoolean("habilitarRecebimentoMatriculaOnlineDebito"));
		obj.setHabilitarRecebimentoMatriculaRenovacaoOnlineDebito(tabelaResultado.getBoolean("habilitarRecebimentoMatriculaRenovacaoOnlineDebito"));
		obj.setPermiteReceberContaMensalidadeDebito(tabelaResultado.getBoolean("permiteReceberContaMensalidadeDebito"));
		obj.setHabilitarRecebimentoMensalidadeMatriculaOnlineDebito(tabelaResultado.getBoolean("habilitarRecebimentoMensalidadeMatriculaOnlineDebito"));
		obj.setHabilitarRecebimentoMensalidadeRenovacaoOnlineDebito(tabelaResultado.getBoolean("habilitarRecebimentoMensalidadeRenovacaoOnlineDebito"));
		obj.setPermitirReceberContaVencidaDebito(tabelaResultado.getBoolean("permitirrecebercontavencidaDebito"));
		obj.setNumeroMaximoDiasReceberContaVencidaDebito(tabelaResultado.getInt("numeromaximodiasrecebercontavencidaDebito"));
		obj.setPermitirCartao(PermitirCartaoEnum.valueOf(tabelaResultado.getString("permitircartao")));
		
		obj.setApresentarOpcaoRecorrenciaAluno(tabelaResultado.getBoolean("apresentarOpcaoRecorrenciaAluno"));
		obj.setUtilizarOpcaoRecorrenciaDefaulMarcado(tabelaResultado.getBoolean("utilizarOpcaoRecorrenciaDefaulMarcado"));
		if (tabelaResultado.getString("formaPadraoDataBaseCartaoRecorrente") != null) {
			obj.setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.valueOf(tabelaResultado.getString("formaPadraoDataBaseCartaoRecorrente")));
		}
		obj.setOrientacaoRecorrenciaAluno(tabelaResultado.getString("orientacaoRecorrenciaAluno"));
		obj.setExigirConfirmacaoRecorrencia(tabelaResultado.getBoolean("exigirConfirmacaoRecorrencia"));
		obj.setMensagemConfirmacaoRecorrencia(tabelaResultado.getString("mensagemConfirmacaoRecorrencia"));
		
		obj.setDataCriacao(tabelaResultado.getDate("dataCriacao"));
		obj.getReposnsavelCriacao().setCodigo(tabelaResultado.getInt("responsavelCriacao"));		
		obj.setPermitirCartao(PermitirCartaoEnum.valueOf(tabelaResultado.getString("permitircartao")));
		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("responsavelAtivacao"))) {
			obj.setDataAtivacao(tabelaResultado.getDate("dataAtivacao"));
			obj.getReposnsavelAtivacao().setCodigo(tabelaResultado.getInt("responsavelAtivacao"));
		}
		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("responsavelInativacao"))) {
			obj.setDataAtivacao(tabelaResultado.getDate("dataInativacao"));
			obj.getReposnsavelAtivacao().setCodigo(tabelaResultado.getInt("responsavelInativacao"));
		}
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
//			if(!obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
//				obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuarioLogado));
//			}
//			if(!obj.getCursoVO().getCodigo().equals(0)) {
//				obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), nivelMontarDados, false, usuarioLogado));
//			}
//			if(!obj.getTurmaVO().getCodigo().equals(0)) {
//				obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), nivelMontarDados, usuarioLogado));
//			}
			obj.getConfiguracaoFinanceiroCartaoRecebimentoVOs().addAll(getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().consultarPorCodigoConfiguracaoRecebimentoCartaoOnlineVO(obj.getCodigo(), nivelMontarDados, usuarioLogado));
		}
		return obj;
	}

	public List<ConfiguracaoRecebimentoCartaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ConfiguracaoRecebimentoCartaoOnlineVO> vetResultado = new ArrayList<ConfiguracaoRecebimentoCartaoOnlineVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoRecebimentoCartaoOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		return getAplicacaoControle().getConfiguracaoRecebimentoCartaoOnlineVO(codigo, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoRecebimentoCartaoOnlineVO consultarPorChavePrimariaUnica(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {		
		String sql = "SELECT * FROM ConfiguracaoRecebimentoCartaoOnline WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ConfiguracaoRecebimentoCartaoOnlineVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOAtivo(final ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if(configuracaoRecebimentoCartaoOnlineVO.getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_registroNaoGravadoAtivar"));
			}
			ConfiguracaoRecebimentoCartaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE ConfiguracaoRecebimentoCartaoOnline" + " SET " + "situacao=?, dataativacao=?, responsavelativacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, configuracaoRecebimentoCartaoOnlineVO.getSituacao().name());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(configuracaoRecebimentoCartaoOnlineVO.getDataAtivacao()));
					if (configuracaoRecebimentoCartaoOnlineVO.getReposnsavelAtivacao().getCodigo() != 0) {
						sqlAlterar.setInt(3, configuracaoRecebimentoCartaoOnlineVO.getReposnsavelAtivacao().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, configuracaoRecebimentoCartaoOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOInativo(final ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoRecebimentoCartaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE ConfiguracaoRecebimentoCartaoOnline" + " SET " + "situacao=?, datainativacao=?, responsavelinativacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, configuracaoRecebimentoCartaoOnlineVO.getSituacao().name());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(configuracaoRecebimentoCartaoOnlineVO.getDataInativacao()));
					if (configuracaoRecebimentoCartaoOnlineVO.getReposnsavelInativacao().getCodigo() != 0) {
						sqlAlterar.setInt(3, configuracaoRecebimentoCartaoOnlineVO.getReposnsavelInativacao().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, configuracaoRecebimentoCartaoOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultar(String campoConsulta, String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (campoConsulta.equals("descricao")) {
			if(valorConsulta.length() < 2) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorDescricao(Uteis.removeCaractersEspeciais(valorConsulta).toUpperCase(), situacaoEnum, nivelMontarDados, usuarioVO);
		} else if(campoConsulta.equals("unidadeensino")) {
			return consultarPorUnidadeEnsino(Integer.parseInt(valorConsulta), situacaoEnum, nivelMontarDados, usuarioVO);
		} else if(campoConsulta.equals("nivelEducacional")) {
			return consultarPorNivelEducacional(valorConsulta, situacaoEnum, nivelMontarDados, usuarioVO);
		} else if(campoConsulta.equals("curso")) {
			return consultarPorCurso(Integer.parseInt(valorConsulta), situacaoEnum, nivelMontarDados, usuarioVO);
		} else if(campoConsulta.equals("turma")) {
			return consultarPorTurma(valorConsulta, situacaoEnum, nivelMontarDados, usuarioVO);
		}
		return null;
	}
	
	@Override
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorDescricao(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS)) {
			sqlStr.append(getSQLConsultaBasica());
		} else {
			sqlStr.append(getSQLConsultaCompleta());
		}
		sqlStr.append(" WHERE sem_acentos(configuracaorecebimentocartaoonline.descricao) ilike (sem_acentos('" + valorConsulta + "%')) ");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.situacao = '").append(situacaoEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorUnidadeEnsino(Integer valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS)) {
			sqlStr.append(getSQLConsultaBasica());
		} else {
			sqlStr.append(getSQLConsultaCompleta());
		}
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.unidadeensino = ").append(valorConsulta);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.situacao = '").append(situacaoEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorNivelEducacional(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS)) {
			sqlStr.append(getSQLConsultaBasica());
		} else {
			sqlStr.append(getSQLConsultaCompleta());
		}
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponiveleducacional = '").append(valorConsulta).append("'");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.situacao = '").append(situacaoEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorCurso(Integer valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS)) {
			sqlStr.append(getSQLConsultaBasica());
		} else {
			sqlStr.append(getSQLConsultaCompleta());
		}
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.curso = ").append(valorConsulta);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.situacao = '").append(situacaoEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	public List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorTurma(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS)) {
			sqlStr.append(getSQLConsultaBasica());
		} else {
			sqlStr.append(getSQLConsultaCompleta());
		}
		sqlStr.append(" INNER JOIN turma on turma.codigo = configuracaorecebimentocartaoonline.turma");
		sqlStr.append(" WHERE upper(turma.identificadorturma) like('" + valorConsulta.toUpperCase() + "%') ");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.situacao = '").append(situacaoEnum).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	public StringBuilder getSQLConsultaBasica() throws Exception {
	    	StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT configuracaorecebimentocartaoonline.codigo, descricao, configuracaorecebimentocartaoonline.situacao, tiponivelconfiguracaorecebimentocartaoonline, ");
		sqlStr.append("       configuracaorecebimentocartaoonline.unidadeensino, tiponiveleducacional, configuracaorecebimentocartaoonline.curso, turma, usarconfiguracaovisaoalunopais, ");
		sqlStr.append("       usarconfiguracaovisaocandidato, usarconfiguracaovisaoadministrativa, configuracaofinanceira ");
		sqlStr.append("FROM configuracaorecebimentocartaoonline");
		return sqlStr;
	}
	
	public StringBuilder getSQLConsultaCompleta() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT codigo, descricao, situacao, tiponivelconfiguracaorecebimentocartaoonline, ");
		sqlStr.append("	       unidadeensino, tiponiveleducacional, curso, turma, usarconfiguracaovisaoalunopais, ");
		sqlStr.append("	       usarconfiguracaovisaocandidato, usarconfiguracaovisaoadministrativa, ");
		sqlStr.append("	       valorminimorecebimentocartaocredito, permiterecebercontabiblioteca, ");
		sqlStr.append("	       permiterecebercontacontratoreceita, permiterecebercontaoutros, ");
		sqlStr.append("	       permiterecebercontadevolucaocheque, permiterecebercontainclusaoreposicao, ");
		sqlStr.append("	       permiterecebercontainscricaoprocessoseletivo, permiterecebercontarequerimento, ");
		sqlStr.append("	       permiterecebercontanegociacao, habilitarrenegociacaoonline, permiterecebercontamatricula, ");
		sqlStr.append("	       habilitarrecebimentomatriculaonline, habilitarrecebimentomatricularenovacaoonline, ");
		sqlStr.append("	       permiterecebercontamensalidade, habilitarrecebimentomensalidadematriculaonline, ");
		sqlStr.append("	       habilitarrecebimentomensalidaderenovacaoonline, datacriacao, ");
		sqlStr.append("	       responsavelcriacao, dataativacao, responsavelativacao, datainativacao, ");
		sqlStr.append("	       responsavelinativacao, configuracaofinanceira, permiterecebercontamaterialdidatico, ");
		sqlStr.append("        numeroMaximoDiasReceberContaVencidaDebito, valorMinimoRecebimentoCartaoDebito, permitirReceberContaVencidaDebito, permiteReceberContaContratoReceitaDebito, permiteReceberContaOutrosDebito, permiteReceberContaDevolucaoChequeDebito, ");
		sqlStr.append("        permiteReceberContaInclusaoReposicaoDebito, permiteReceberContaInscricaoProcessoSeletivoDebito, permiteReceberContaRequerimentoDebito, permiteReceberContaNegociacaoDebito, permiteReceberContaMatriculaDebito, permiteReceberContaMensalidadeDebito, ");
		sqlStr.append("        permiteReceberContaMaterialDidaticoDebito, habilitarRenegociacaoOnlineDebito, habilitarRecebimentoMatriculaOnlineDebito, habilitarRecebimentoMatriculaRenovacaoOnlineDebito, ");
		sqlStr.append("		   habilitarRecebimentoMensalidadeMatriculaOnlineDebito, habilitarRecebimentoMensalidadeRenovacaoOnlineDebito, permiteReceberContaBibliotecaDebito, permitirCartao ");
		sqlStr.append(" FROM configuracaorecebimentocartaoonline");
		return sqlStr;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ConfiguracaoRecebimentoCartaoOnlineVO clonar(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) throws Exception {
		return configuracaoRecebimentoCartaoOnlineVO.clone();
	}
	
	@Override
	public void adicionarConfiguracaoFinanceiroCartaoRecebimento(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioVO) throws ConsistirException {
		for (ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO2 : configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
			getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().validarDados(configuracaoFinanceiroCartaoRecebimentoVO, configuracaoFinanceiroCartaoRecebimentoVO2);			
		}
		configuracaoFinanceiroCartaoRecebimentoVO.setConfiguracaoRecebimentoCartaoOnlineVO(configuracaoRecebimentoCartaoOnlineVO);;
		//configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs().add(configuracaoFinanceiroCartaoRecebimentoVO);
	}
	
	@Override
	public void removerConfiguracaoFinanceiroCartaoRecebimento(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioVO) throws Exception {
		if(configuracaoFinanceiroCartaoRecebimentoVO.getCodigo().equals(0)) {
			configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs().remove(configuracaoFinanceiroCartaoRecebimentoVO);
		} else {
			getFacadeFactory().getConfiguracaoFinanceiroCartaoRecebimentoFacade().excluir(configuracaoFinanceiroCartaoRecebimentoVO, false, usuarioVO);
			configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs().remove(configuracaoFinanceiroCartaoRecebimentoVO);
		}
	}
	
	@Override
	public Boolean verificarUnicidadeCampos(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioLogado) throws ConsistirException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE 1=1 "); 
		 
		if(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.GERAL)) {
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelConfiguracaoRecebimentoCartaoOnline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.GERAL).append("'");
		}
		if(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL)) {
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelConfiguracaoRecebimentoCartaoOnline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL).append("'");
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelEducacional = '").append(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelEducacional()).append("'");
			if(Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo())){
				sqlStr.append(" and (configuracaorecebimentocartaoonline.unidadeensino = ").append(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo()).append(" or configuracaorecebimentocartaoonline.unidadeensino is null)");	
			}else{
				sqlStr.append(" and  configuracaorecebimentocartaoonline.unidadeensino is null ");
			}
		}
		if(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO)) {
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelConfiguracaoRecebimentoCartaoOnline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO).append("'");
			sqlStr.append(" AND configuracaorecebimentocartaoonline.curso = ").append(configuracaoRecebimentoCartaoOnlineVO.getCursoVO().getCodigo());
			if(Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo())){
				sqlStr.append(" and (configuracaorecebimentocartaoonline.unidadeensino = ").append(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo()).append(" or configuracaorecebimentocartaoonline.unidadeensino is null)");	
			}else{
				sqlStr.append(" and  configuracaorecebimentocartaoonline.unidadeensino is null ");
			}
		} 
		if(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA)) {
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelConfiguracaoRecebimentoCartaoOnline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA).append("'");
			sqlStr.append(" AND configuracaorecebimentocartaoonline.turma = ").append(configuracaoRecebimentoCartaoOnlineVO.getTurmaVO().getCodigo());
			if(Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo())){
				sqlStr.append(" and configuracaorecebimentocartaoonline.unidadeensino = ").append(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo()).append(" ");	
			}
		} 
		if(configuracaoRecebimentoCartaoOnlineVO.getTipoNivelConfiguracaoRecebimentoCartaoOnline().equals(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO)) {
			sqlStr.append(" AND configuracaorecebimentocartaoonline.tipoNivelConfiguracaoRecebimentoCartaoOnline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO).append("'");
			sqlStr.append(" AND configuracaorecebimentocartaoonline.unidadeensino = ").append(configuracaoRecebimentoCartaoOnlineVO.getUnidadeEnsinoVO().getCodigo()).append(" ");
		} 
		sqlStr.append(" AND codigo != ").append(configuracaoRecebimentoCartaoOnlineVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ConfiguracaoRecebimentoCartaoOnlineVO consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(Integer codigoTurma, Integer codigoCurso, String tipoNivelEducacional, Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String visaoUtilizar = "";
		if(usuarioLogado != null && usuarioLogado.getVisaoLogar().equals("")){
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoalunopais = true";
		} else if(usuarioLogado != null && usuarioLogado.getIsApresentarVisaoAdministrativa()) {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoadministrativa = true";
		} else if(usuarioLogado != null && (usuarioLogado.getIsApresentarVisaoAluno() || usuarioLogado.getIsApresentarVisaoPais()  || usuarioLogado.getIsApresentarVisaoCoordenador() || usuarioLogado.getIsApresentarVisaoProfessor())) {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoalunopais = true";
		} else {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaocandidato = true";
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT 1 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.turma = ").append(codigoTurma);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 2 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.curso = ").append(codigoCurso);
		sqlStr.append("	AND configuracaorecebimentocartaoonline.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 3 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.curso = ").append(codigoCurso);
		sqlStr.append("	AND configuracaorecebimentocartaoonline.unidadeensino is null");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 4 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponiveleducacional = '").append(tipoNivelEducacional).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 5 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponiveleducacional = '").append(tipoNivelEducacional).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.unidadeensino is null");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 6 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 7 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.GERAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" order by ordem limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO = null;
		if(tabelaResultado.next()) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
			configuracaoRecebimentoCartaoOnlineVO = consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), nivelMontarDados, usuarioLogado);
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}
	
	
	@Override
	public ConfiguracaoRecebimentoCartaoOnlineVO consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(String matricula, Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String visaoUtilizar = "";
		if(usuarioLogado != null && usuarioLogado.getVisaoLogar().equals("")){
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoalunopais = true";
		} else if(usuarioLogado != null && usuarioLogado.getIsApresentarVisaoAdministrativa()) {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoadministrativa = true";
		} else if(usuarioLogado != null && (usuarioLogado.getIsApresentarVisaoAluno() || usuarioLogado.getIsApresentarVisaoPais()  || usuarioLogado.getIsApresentarVisaoCoordenador() || usuarioLogado.getIsApresentarVisaoProfessor())) {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaoalunopais = true";
		} else {
			visaoUtilizar = " AND configuracaorecebimentocartaoonline.usarconfiguracaovisaocandidato = true";
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("   with matr as(");
		sqlStr.append(" ");
		sqlStr.append("   	select curso.codigo as curso, "+codigoUnidadeEnsino+" as unidadeensino, matriculaperiodo.turma as turma, curso.niveleducacional from	matricula ");
		sqlStr.append("   	inner join curso on curso.codigo = matricula.curso");
		sqlStr.append("   	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append("   	and matriculaperiodo.codigo  = (");
		sqlStr.append("   	 select m.codigo from matriculaperiodo m where m.matricula = matricula.matricula AND m.situacaomatriculaperiodo != 'PC' order by (m.ano||'/'||m.semestre) desc, case when m.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, m.codigo desc");
		sqlStr.append("   	)");
		sqlStr.append("   	where matricula.matricula = ? ");
		sqlStr.append("   )");
		
		sqlStr.append(" SELECT 1 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.turma = matr.turma ");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.TURMA).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 2 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.curso = matr.curso AND configuracaorecebimentocartaoonline.unidadeensino = matr.unidadeensino ");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 3 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.curso = matr.curso ");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.unidadeensino is null");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.CURSO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 4 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline ");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.tiponiveleducacional = matr.niveleducacional and configuracaorecebimentocartaoonline.unidadeensino = matr.unidadeensino ");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 5 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.tiponiveleducacional = matr.niveleducacional ");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.unidadeensino is null");
		sqlStr.append(" AND configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.NIVEL_EDUCACIONAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 6 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" inner join matr on configuracaorecebimentocartaoonline.unidadeensino = matr.unidadeensino ");
		sqlStr.append(" where configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" union ");
		sqlStr.append(" SELECT 7 as ordem, configuracaorecebimentocartaoonline.codigo FROM configuracaorecebimentocartaoonline");
		sqlStr.append(" WHERE configuracaorecebimentocartaoonline.tiponivelconfiguracaorecebimentocartaoonline = '").append(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.GERAL).append("'");
		sqlStr.append("	AND configuracaorecebimentocartaoonline.situacao = 'ATIVO'");
		sqlStr.append(visaoUtilizar);
		sqlStr.append(" order by ordem limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO = null;
		if(tabelaResultado.next()) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
			configuracaoRecebimentoCartaoOnlineVO = consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), nivelMontarDados, usuarioLogado);
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}
	
	@Override
	public void verificarContasRecebimentoOnline(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, ConsistirException consistirException, Boolean negociacao, Boolean mensalidadeRenovacaoOnline, Boolean mensalidadeMatriculaOnline, Boolean matriculaRenovacaoOnline, Boolean matriculaOnline, Boolean renegociacaoOnline, UsuarioVO usuarioVO) throws Exception {
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : contaReceberNegociacaoRecebimentoVOs) {
			if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())) {
				if(!matriculaOnline && !matriculaRenovacaoOnline && !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatricula()) {
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
				} else {
					if(!negociacao) {
						if(matriculaOnline && !configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnline()) {
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
						} else if(matriculaRenovacaoOnline && !configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline()) {
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
						}
					}
				}
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor()) 
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBiblioteca()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoCheque()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.OUTROS.getValor())
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutros()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.getValor())
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceita()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor())) {
				if(!mensalidadeMatriculaOnline && !mensalidadeRenovacaoOnline && !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidade()) {
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
				} else {
					if(!negociacao) {
						if(mensalidadeMatriculaOnline && !configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnline()) {
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
						} else if(mensalidadeRenovacaoOnline && !configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline()) {
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
						}
					}
				}
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getValor()) 
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivo()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor()) 
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimento()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor())) {
				if(!configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacao()) {
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
				} else {
					if(!negociacao) {
						if(renegociacaoOnline && !configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnline()) {
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
						} 
					}
				}
			} else if(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor())
					&& !configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicao()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_naoPermiteRecebimentoOnline").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()));
			} 
			if(!verificarContaVendida(contaReceberNegociacaoRecebimentoVO.getContaReceber(), configuracaoRecebimentoCartaoOnlineVO)){
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_contaVencidaAcimaDoMaximoPermitido").replace("{0}", contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem_apresentar()).replace("{1}", configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencida().toString()));
			}
			
			if(!verificarValorAReceberValorMinimo(configuracaoRecebimentoCartaoOnlineVO, contaReceberNegociacaoRecebimentoVO.getValorTotal(), usuarioVO)) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoRecebimentoCartaoOnline_valorDaContaMenorValorMinimo"));
			}
		}
	}
	
	
	
	public Boolean verificarContaVendida(ContaReceberVO contaReceberVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) throws Exception {
		if((contaReceberVO.getDataVencimento().compareTo(new Date()) < 0) && configuracaoRecebimentoCartaoOnlineVO.getPermitirReceberContaVencida()) {
			if(Uteis.getObterDiferencaDiasEntreDuasData(new Date(), contaReceberVO.getDataVencimento()) <= configuracaoRecebimentoCartaoOnlineVO.getNumeroMaximoDiasReceberContaVencida()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	@Override
	public Boolean verificarContasRecebimentoOnlineContaReceberVO(ContaReceberVO contaReceberVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, ConsistirException consistirException, Boolean negociacao, Boolean mensalidadeRenovacaoOnline, Boolean mensalidadeMatriculaOnline, Boolean matriculaRenovacaoOnline, Boolean matriculaOnline, Boolean renegociacaoOnline, UsuarioVO usuarioVO) throws Exception {
		if (!verificarContaVendida(contaReceberVO, configuracaoRecebimentoCartaoOnlineVO)) {
			return false;
		}
		if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())) {
			if (!matriculaOnline && !matriculaRenovacaoOnline && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMatricula()) {
				return true;
			} else {
				if (!negociacao) {
					if (matriculaOnline && configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnline()) {
						return true;
					} else if (matriculaRenovacaoOnline && configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline()) {
						return true;
					}
				}
			}
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaBiblioteca()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaDevolucaoCheque()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.OUTROS.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaOutros()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaContratoReceita()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMaterialDidatico()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor())) {
			if (!mensalidadeMatriculaOnline && !mensalidadeRenovacaoOnline && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaMensalidade()) {
				return true;
			} else {
				if (!negociacao) {
					if (mensalidadeMatriculaOnline && configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMensalidadeMatriculaOnline()) {
						return true;
					} else if (mensalidadeRenovacaoOnline && configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaRenovacaoOnline()) {
						return true;
					}
				}
			}
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInscricaoProcessoSeletivo()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaRequerimento()) {
			return true;
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor())) {
			if (configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaNegociacao()) {
				return true;
			} else {
				if (!negociacao) {
					if (renegociacaoOnline && configuracaoRecebimentoCartaoOnlineVO.getHabilitarRenegociacaoOnline()) {
						return true;
					}
				}
			}
		} else if (contaReceberVO.getTipoOrigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor()) && configuracaoRecebimentoCartaoOnlineVO.getPermiteReceberContaInclusaoReposicao()) {
			return true;
		}
		
		return false;
	}
	
//	@Override
//	public Boolean realizarVerificacaoRecebimentoOnlinePermitePorParcelasValorAReceber(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs, Double valorAReceber, UsuarioVO usuarioVO) throws Exception {
//		for (ConfiguracaoFinanceiroCartaoRecebimentoVO obj : configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
////			if ((quantidadeParcelas <= obj.getParcelasAte()) && (valorAReceber >= obj.getValorMinimo())) {
//			if (valorAReceber >= obj.getValorMinimo() &&
//					(VisaoParcelarEnum.TODAS.equals(obj.getVisao()) ||
//					 (VisaoParcelarEnum.ADMINISTRATIVA.equals(obj.getVisao()) && usuarioVO.getIsApresentarVisaoAdministrativa()) ||
//					 (VisaoParcelarEnum.ALUNO_E_PAIS.equals(obj.getVisao()) && !usuarioVO.getIsApresentarVisaoAdministrativa()))
//				) {
//				for (ContaReceberNegociacaoRecebimentoVO conta : contaReceberNegociacaoRecebimentoVOs) {
//					long diasAtraso = Uteis.nrDiasEntreDatas(new Date(), conta.getContaReceber().getDataVencimento());
//					if ((obj.getQtdeDiasInicialParcelarContaVencida() == 0 && diasAtraso <= 0) ||
//						(diasAtraso >= obj.getQtdeDiasInicialParcelarContaVencida() && diasAtraso <= obj.getQtdeDiasFinalParcelarContaVencida())) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	@Override
	public Boolean verificarValorAReceberValorMinimo(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, Double valorAReceber, UsuarioVO usuarioVO) throws ConsistirException {
		if(valorAReceber < configuracaoRecebimentoCartaoOnlineVO.getValorMinimoRecebimentoCartaoCredito()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void validarSeExisteConfiguracaoCartaoRecebimento(ConfiguracaoRecebimentoCartaoOnlineVO conf) throws Exception {
		if(!Uteis.isAtributoPreenchido(conf)){
			throw new Exception("Não foi encontrado uma configuração recebimento cartão Online ativa para esse curso ou unidade ensino.");	
		}
		//if(!conf.getHabilitarRecebimentoMatriculaOnline() && !conf.getHabilitarRecebimentoMensalidadeMatriculaOnline()){
		//	throw new Exception("Não está habilitado para configuração recebimento cartão Online o recurso de pagamento de Matrícula Online e Mensalidade Online.");	
		//}
	}
	
	
	@Override
	public void validarSeExisteConfiguracaoCartaoRecebimentoMatriculaOnline(ConfiguracaoRecebimentoCartaoOnlineVO conf) throws Exception {
		
		if(!conf.getHabilitarRecebimentoMatriculaOnline() && !conf.getHabilitarRecebimentoMensalidadeMatriculaOnline()){
			throw new Exception("Não está habilitado para configuração recebimento cartão Online o recurso de pagamento de Matrícula Online e Mensalidade Online.");	
		}
	}
}
