package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.script.ScriptEngine;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ContraChequeVO.EnumCampoConsultaContraCheque;
import negocio.comuns.recursoshumanos.ControleLancamentoFolhapagamentoVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.EventoContraChequeRelVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FichaFinanceiraRelVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.recursoshumanos.RescisaoIndividualVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.recursoshumanos.enumeradores.NaturezaEventoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaRescisaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.interfaces.recursoshumanos.ContraChequeInterfaceFacade;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
@Scope
@Lazy
public class ContraCheque extends SuperFacade<ContraChequeVO> implements ContraChequeInterfaceFacade<ContraChequeVO> {

	private static final long serialVersionUID = -40479772822196253L;
	
	private static final String DADOS_NAO_ENCONTRADOS = "Dados não encontrados (Contra Cheque).";

	protected static String idEntidade;

	public ContraCheque() throws Exception {
		super();
		setIdEntidade("FichaFinanceira");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void persistir(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}

		persistirContraChequeEvento(obj, usuarioVO);
	}

	private void persistirContraChequeEvento(ContraChequeVO obj, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getContraChequeEventoInterfaceFacade().persistirTodos(obj, false, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void incluir(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraCheque.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					final StringBuilder sql = new StringBuilder(" INSERT INTO contracheque ")
							.append(" ( competenciafolhapagamento, funcionariocargo, totalprovento, totaldesconto, ")
							.append(" totalreceber, tipoRecebimento, salario, previdencia, optanteTotal, ")
							.append(" FGTS, DSR, salarioFamilia, numeroDependenteSalarioFamilia, previdenciaPropria, ")
							.append(" planoSaude, baseCalculoINSS, valorINSS, baseCalculoIRRF, faixa,  ")
							.append(" dedutivel, numerosDependentes, valorDependente, valorIRRF,informeRendimento, ")
							.append(" RAIS, basecalculoirrfferias, valorIRRFFerias, templatelancamentofolhapagamento  )")
							.append(" VALUES ( ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ? )")
							.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString()); 

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCompetenciaFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalProvento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalDesconto(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getTotalReceber(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoRecebimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOptanteTotal(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getFGTS(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDSR(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalarioFamilia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroDependenteSalarioFamilia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropria(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getPlanoSaude(), ++i, sqlInserir);					
					Uteis.setValuePreparedStatement(obj.getBaseCalculoINSS(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorINSS(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBaseCalculoIRRF(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFaixa(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getDedutivel(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumerosDependentes(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorDependente(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorIRRF(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformeRendimento(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getRAIS(), ++i, sqlInserir);					
					Uteis.setValuePreparedStatement(obj.getBaseCalculoIRRFFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorIRRFFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamentoVO(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

					public Object extractData(final ResultSet arg0) throws SQLException {
						if (arg0.next()) {
							obj.setNovoObj(Boolean.FALSE);
							return arg0.getInt("codigo");
						}
						return null;
					}
				}));
			} catch (Exception e) {
				obj.setNovoObj(Boolean.TRUE);
				e.printStackTrace();
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_erroAoSalvar"));
			}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void alterar(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraCheque.alterar(getIdEntidade(), validarAcesso, usuarioVO);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" UPDATE contracheque SET ")
							.append(" competenciafolhapagamento=?, funcionariocargo=?, totalprovento=?, totaldesconto=?, ")
							.append(" totalreceber=?, tiporecebimento=?, salario=?, previdencia=?, optantetotal=?, ")
							.append(" fgts=?, dsr=?, salariofamilia=?, numerodependentesalariofamilia=?, previdenciapropria=?, ")
							.append(" planosaude=?, basecalculoinss=?, valorinss=?, basecalculoirrf=?, faixa=?, ")
							.append(" dedutivel=?, numerosdependentes=?, valordependente=?, valorirrf=?, informerendimento=?, ")
							.append(" rais=?, basecalculoirrfferias=?, valorIRRFFerias=?, templatelancamentofolhapagamento=? ")
							.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCompetenciaFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalProvento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalDesconto(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getTotalReceber(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoRecebimento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOptanteTotal(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getFGTS(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getDSR(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalarioFamilia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNumeroDependenteSalarioFamilia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropria(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getPlanoSaude(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getBaseCalculoINSS(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorINSS(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getBaseCalculoIRRF(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFaixa(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getDedutivel(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getNumerosDependentes(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorDependente(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorIRRF(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInformeRendimento(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getRAIS(), ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getBaseCalculoIRRFFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorIRRFFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamentoVO(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_erroAoSalvar"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraCheque.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			String sql = "DELETE FROM contracheque WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirTodos(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			getFacadeFactory().getContraChequeEventoInterfaceFacade().excluirContraChequeEventoPorContraCheque(obj, usuarioVO);
			
			ContraCheque.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			String sql = "DELETE FROM contracheque WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public ContraChequeVO consultarPorChavePrimaria(Long codigo) throws Exception {
		String sql = " SELECT * FROM contracheque WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception(DADOS_NAO_ENCONTRADOS);
	}

	
	public ContraChequeVO consultarTodosPorChavePrimaria(Long codigo) throws Exception {
		String sql = getSqlBasico() +  " WHERE contracheque.codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_COMBOBOX);
		}
		throw new Exception(DADOS_NAO_ENCONTRADOS);
	}

	@Override
	public void validarDados(ContraChequeVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_identificador"));
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getCompetenciaFolhaPagamento().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_dataCompetencia"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getPeriodo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_periodo"));
		}
	}

	@Override
	public ContraChequeVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		ContraChequeVO obj = new ContraChequeVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getCompetenciaFolhaPagamento().setCodigo(tabelaResultado.getInt("competenciafolhapagamento"));
		obj.getFuncionarioCargo().setCodigo(tabelaResultado.getInt("funcionariocargo"));
		
		if (Uteis.NIVELMONTARDADOS_TODOS == nivelMontarDados) {
			obj.getCompetenciaFolhaPagamento().setDataCompetencia(tabelaResultado.getDate("datacompetencia"));
			obj.getFuncionarioCargo().setMatriculaCargo(tabelaResultado.getString("matriculacargo"));
			obj.getFuncionarioCargo().setComissionado(tabelaResultado.getBoolean("comissionado"));

			obj.getFuncionarioCargo().getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("nome"));

			obj.getFuncionarioCargo().getCargo().setCodigo(tabelaResultado.getInt("cargo.codigo"));
			obj.getFuncionarioCargo().getCargo().setNome(tabelaResultado.getString("cargo.nome"));
			obj.getFuncionarioCargo().getCargoAtual().setCodigo(tabelaResultado.getInt("cargoatual.codigo"));

			obj.getFuncionarioCargo().getFuncionarioVO().setMatricula(tabelaResultado.getString("funcionario.matricula"));
		}

		obj.setTotalProvento(tabelaResultado.getBigDecimal("totalprovento"));
		obj.setTotalDesconto(tabelaResultado.getBigDecimal("totaldesconto"));
		obj.setTotalReceber(tabelaResultado.getBigDecimal("totalreceber"));

		obj.setTipoRecebimento(tabelaResultado.getString("tiporecebimento"));
		obj.setSalario(tabelaResultado.getBigDecimal("salario"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("previdencia"))) {
			obj.setPrevidencia(PrevidenciaEnum.valueOf(tabelaResultado.getString("previdencia")));
		}
		obj.setOptanteTotal(tabelaResultado.getBoolean("optantetotal"));
		obj.setFGTS(tabelaResultado.getBigDecimal("fgts"));
		obj.setDSR(tabelaResultado.getBigDecimal("dsr"));
		obj.setSalarioFamilia(tabelaResultado.getBigDecimal("salariofamilia"));
		obj.setNumeroDependenteSalarioFamilia(tabelaResultado.getInt("numerodependentesalariofamilia"));
		obj.setPrevidenciaPropria(tabelaResultado.getBigDecimal("previdenciapropria"));
		obj.setPlanoSaude(tabelaResultado.getBigDecimal("planosaude"));
		obj.setBaseCalculoINSS(tabelaResultado.getBigDecimal("basecalculoinss"));
		obj.setValorINSS(tabelaResultado.getBigDecimal("valorinss"));
		obj.setBaseCalculoIRRF(tabelaResultado.getBigDecimal("basecalculoirrf"));
		obj.setFaixa(tabelaResultado.getBigDecimal("faixa"));
		obj.setDedutivel(tabelaResultado.getBigDecimal("dedutivel"));
		obj.setNumerosDependentes(tabelaResultado.getInt("numerosdependentes"));
		obj.setValorDependente(tabelaResultado.getBigDecimal("valordependente"));
		obj.setValorIRRF(tabelaResultado.getBigDecimal("valorirrf"));
		obj.setInformeRendimento(tabelaResultado.getBigDecimal("informerendimento"));
		obj.setRAIS(tabelaResultado.getBigDecimal("rais"));
		obj.setBaseCalculoIRRFFerias(tabelaResultado.getBigDecimal("basecalculoirrfferias"));
		obj.setValorIRRFFerias(tabelaResultado.getBigDecimal("valorIRRFFerias"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("templatelancamentofolhapagamento"))) {
			obj.setTemplateLancamentoFolhaPagamentoVO(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("templatelancamentofolhapagamento"), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}

		return obj;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta (DataModelo dataModelo, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception {
		List<ContraChequeVO> objs = new ArrayList<ContraChequeVO>(0);
		dataModelo.getListaFiltros().clear();
		switch (EnumCampoConsultaContraCheque.valueOf(dataModelo.getCampoConsulta())) {
		case FUNCIONARIO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarContraCheque(dataModelo, "pessoa.nome", dataCompetenciaInicial, dataCompetenciaFinal);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalContraCheque(dataModelo, "pessoa.nome", dataCompetenciaInicial, dataCompetenciaFinal));
			break;
		case CARGO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarContraCheque(dataModelo, "cargo.nome", dataCompetenciaInicial, dataCompetenciaFinal);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalContraCheque(dataModelo, "cargo.nome", dataCompetenciaInicial, dataCompetenciaFinal));
			break;
		case MATRICULA:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarContraCheque(dataModelo, "funcionario.matricula", dataCompetenciaInicial, dataCompetenciaFinal);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalContraCheque(dataModelo, "funcionario.matricula", dataCompetenciaInicial, dataCompetenciaFinal));
			break;
		case MATRICULA_CARGO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarContraCheque(dataModelo, "fc.matriculacargo", dataCompetenciaInicial, dataCompetenciaFinal);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalContraCheque(dataModelo, "funcionariocargo.matriculacargo", dataCompetenciaInicial, dataCompetenciaFinal));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}

	private Integer consultarTotalContraCheque(DataModelo dataModelo,  String campoConsulta, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception {
		ContraCheque.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		dataModelo.getListaFiltros().clear();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(contracheque.codigo) as qtde FROM contracheque ");
        sql.append(" INNER JOIN funcionariocargo ON funcionariocargo.codigo = contracheque.funcionariocargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" INNER JOIN  pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" INNER JOIN competenciafolhapagamento on contracheque.competenciafolhapagamento = competenciafolhapagamento.codigo ");
		sql.append(" LEFT JOIN cargo on funcionariocargo.cargo = cargo.codigo");

		if (campoConsulta.equals("contracheque.codigo")) {
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
        	sql.append(" WHERE contracheque.codigo =?");
        } else {
        	dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
        	sql.append(" WHERE UPPER(sem_acentos(").append(campoConsulta).append(") ) like (UPPER(sem_acentos(?))) ");
        }

		sql.append(" AND competenciafolhapagamento.datacompetencia >= ? ");
    	sql.append(" AND competenciafolhapagamento.datacompetencia <= ? ");
    	
    	dataModelo.getListaFiltros().add(UteisData.getPrimeiroDataMes(dataCompetenciaInicial));
    	dataModelo.getListaFiltros().add(UteisData.getUltimaDataMes(dataCompetenciaFinal));
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<ContraChequeVO> consultarContraCheque(DataModelo dataModelo,  String campoConsulta, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception {
		ContraCheque.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());
        if (campoConsulta.equals("contracheque.codigo")) {
        	sql.append(" WHERE contracheque.codigo =?");
        } else {
        	sql.append(" WHERE UPPER(sem_acentos(").append(campoConsulta).append(") ) like (UPPER(sem_acentos(?))) ");
        }

    	sql.append(" AND competenciafolhapagamento.datacompetencia >= ? ");
    	sql.append(" AND competenciafolhapagamento.datacompetencia <= ? ");
    	
    	dataModelo.getListaFiltros().add(UteisData.getPrimeiroDataMes(dataCompetenciaInicial));
    	dataModelo.getListaFiltros().add(UteisData.getUltimaDataMes(dataCompetenciaFinal));
    	
        sql.append(" order by contracheque.codigo desc");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<ContraChequeVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }

        return lista;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contracheque.*, pessoa.nome,cargo.codigo as \"cargo.codigo\", cargo.nome as \"cargo.nome\", fc.matriculacargo, ");
		sql.append(" competenciafolhapagamento.datacompetencia, fc.comissionado, fc.cargoatual as \"cargoatual.codigo\", ");
		sql.append(" funcionario.matricula as \"funcionario.matricula\" FROM contracheque ");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = contracheque.funcionariocargo ");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN  pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" INNER JOIN competenciafolhapagamento on contracheque.competenciafolhapagamento = competenciafolhapagamento.codigo   ");
		sql.append(" LEFT JOIN templatelancamentofolhapagamento ON templatelancamentofolhapagamento.codigo = contracheque.templatelancamentofolhapagamento");
		sql.append(" LEFT JOIN cargo on fc.cargo = cargo.codigo ");
		return sql.toString();
	}

	@Override 
	public void validarEventoContraChequeItem(ContraChequeEventoVO contraChequeEvento, List<ContraChequeEventoVO> listaContraChequeEvento) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(contraChequeEvento.getEventoFolhaPagamento().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_eventoVazio"));
		}

		for (ContraChequeEventoVO contraChequeEventoVO : listaContraChequeEvento) {
			if (contraChequeEventoVO.getEventoFolhaPagamento().getCodigo().equals(contraChequeEvento.getEventoFolhaPagamento().getCodigo())
					&& !contraChequeEvento.getItemEmEdicao()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_eventoAdicionado"));
			}
		}
	}

	@Override
	public ContraChequeVO consultarPorCodigoECompetenciaAtiva(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ContraCheque.consultar(getIdEntidade(), validarAcesso, usuarioLogado);

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());
        sql.append(" WHERE funcionariocargo.codigo = ? and competenciafolhapagamento.situacao = ?");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo(), SituacaoTipoAdvertenciaEnum.ATIVO.toString());

        if (tabelaResultado.next()) {
        	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
        }

        throw new Exception("Não há ficha financeira para o funcionário com a competência vigente.");
	}

	@Override
	public List<ContraChequeVO> consultarContraChequePorTemplateLancamentoFolha(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		
		sql.append(" WHERE competenciafolhapagamento.situacao = ? ");

		StringBuilder filtros = new StringBuilder(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade()
				.getFiltrosDoTemplate(templateLancamentoFolhaPagamento));

		if (filtros.toString().trim().length() > 0) {
			sql.append(filtros);
		}else {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumFiltroSelecionado"));
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoTipoAdvertenciaEnum.ATIVO.toString());

		List<ContraChequeVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
		}

		return lista;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirContraChequePorLancamento(LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuarioVO) throws Exception {
		try {
			getFacadeFactory().getContraChequeEventoInterfaceFacade().excluirContraChequeEventoPorLancamentoDoContraCheque(lancamento, usuarioVO);
			
			String sql = "DELETE FROM contracheque WHERE lancamentofolhapagamento = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, lancamento.getCodigo() );
			
		} catch (Exception e) {
			throw e;
		}
	}

	// Contracheque sera unico independente do periodo
	// COntracheque eventos terao muitos periodos
	@Deprecated
	/**
	 * Consulta por funcionario cargo e periodo
	 */
	@Override
	public ContraChequeVO consultarPorFuncionarioCargoEPeriodo(String matricula, Integer codigoCompetenciaFolha) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contracheque.* , pessoa.nome,cargo.codigo as \"cargo.codigo\", cargo.nome as \"cargo.nome\", funcionariocargo.matriculacargo,");
		sql.append(" competenciafolhapagamento.datacompetencia,funcionariocargo.comissionado, funcionariocargo.cargoatual as \"cargoatual.codigo\" ,");
		sql.append(" funcionario.matricula as \"funcionario.matricula\" FROM contracheque ");
		sql.append(" INNER JOIN funcionariocargo ON funcionariocargo.codigo = contracheque.funcionariocargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" LEFT JOIN competenciafolhapagamento on contracheque.competenciafolhapagamento = competenciafolhapagamento.codigo ");
		sql.append(" LEFT JOIN cargo on funcionariocargo.cargo = cargo.codigo ");
		sql.append(" LEFT JOIN  pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" LEFT JOIN  competenciaperiodofolhapagamento ON competenciaperiodofolhapagamento.competenciafolhapagamento = competenciafolhapagamento.codigo");
		sql.append(" WHERE funcionariocargo.matriculacargo = ? AND contracheque.periodo = ?");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, codigoCompetenciaFolha);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
        }
		return new ContraChequeVO();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		ContraCheque.idEntidade = idEntidade;
	}

	@Override
	public FichaFinanceiraRelVO montarDadosFichaFinanceira(ContraChequeVO contraChequeVO) {
		 FichaFinanceiraRelVO obj = new FichaFinanceiraRelVO();
		 
		 try {
			 obj.setUnidadeEnsino(contraChequeVO.getFuncionarioCargo().getUnidade().getNome());
			 obj.setPeriodo(contraChequeVO.getPeriodo().getDescricao());
			 obj.setCompetencia(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia());
			 obj.setMatriculaCargo(contraChequeVO.getFuncionarioCargo().getMatriculaCargo());
			 obj.setNomeFuncionario(contraChequeVO.getFuncionarioCargo().getFuncionarioVO().getPessoa().getNome());
			 if (Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getCargo().getNome())) {
				 obj.setNomeCargo(contraChequeVO.getCargoAtual());
			 } 
			 if ( Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getSituacaoFuncionario())) {
				 obj.setSituacao(SituacaoFuncionarioEnum.getEnumPorValor(contraChequeVO.getFuncionarioCargo().getSituacaoFuncionario()).getDescricao());
			 }

			 if(Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getDataAdmissao())) {
				 obj.setDataAdmissao(UteisData.obterDataFormatoTexto_dd_MM_yyyy(contraChequeVO.getFuncionarioCargo().getDataAdmissao()));	 
			 }
			 
			 if(Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getDataDemissao())) {
				 obj.setDataDemissao(UteisData.obterDataFormatoTexto_dd_MM_yyyy(contraChequeVO.getFuncionarioCargo().getDataDemissao()));	 
			 }
			 
			 if (Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getSecaoFolhaPagamento().getDescricao())) {				 
				 obj.setSecaoFolhaPagamento(contraChequeVO.getFuncionarioCargo().getSecaoFolhaPagamento().getDescricao());
			 } else {
				 obj.setSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(
						 contraChequeVO.getFuncionarioCargo().getSecaoFolhaPagamento().getCodigo().longValue()).getDescricao());
			 }
			 obj.setProvento(contraChequeVO.getTotalProvento());
			 obj.setDesconto(contraChequeVO.getTotalDesconto());
			 obj.setAreceber(contraChequeVO.getTotalReceber());
			 obj.setSalario(contraChequeVO.getFuncionarioCargo().getSalario());

			 obj.setPrevidenciaPropria(contraChequeVO.getPrevidenciaPropria());
			 obj.setBaseCalculoInss(contraChequeVO.getBaseCalculoINSS());
			 obj.setBaseCalculoIrrf(contraChequeVO.getBaseCalculoIRRF());
			 
			 obj.setListaEventosDesconto(criarListaDeEventosDoContraChequeDaFichaFinanceira(contraChequeVO.getCodigo(), TipoLancamentoFolhaPagamentoEnum.DESCONTO));
			 obj.setListaEventosProvento(criarListaDeEventosDoContraChequeDaFichaFinanceira(contraChequeVO.getCodigo(), TipoLancamentoFolhaPagamentoEnum.PROVENTO));
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return obj;
	 }
	 
	private List<EventoContraChequeRelVO> criarListaDeEventosDoContraChequeDaFichaFinanceira(Integer codigo, TipoLancamentoFolhaPagamentoEnum tipoLancamento) {
		
		List paramentros = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT e.identificador as identificadorEvento, e.descricao as nomeEvento, ce.referencia as referencia, ce.provento as provento, ce.desconto as desconto from contrachequeevento ce ")
		.append(" inner join eventofolhapagamento e on e.codigo = ce.eventofolhapagamento ")
		.append(" where ce.contracheque = ? ")
		.append(" and e.tipolancamento = ? ");
		
		paramentros.add(codigo);
		paramentros.add(tipoLancamento.name());
		
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), paramentros.toArray());
        
        List<EventoContraChequeRelVO> listaEventosContraCheque = new ArrayList<>();
        
        while (rs.next()) {
        	listaEventosContraCheque.add(montarDadosDosEventosFichaFinanceira(rs));
        }
		return listaEventosContraCheque;
	}

	private EventoContraChequeRelVO montarDadosDosEventosFichaFinanceira(SqlRowSet sqlRowSet) {
		 
		 EventoContraChequeRelVO obj = new EventoContraChequeRelVO();
		 try {
			 obj.setIdentificadorEvento(sqlRowSet.getString("identificadorEvento"));
		     obj.setNomeEvento(sqlRowSet.getString("nomeEvento"));
		     obj.setReferencia(sqlRowSet.getString("referencia"));
		     obj.setProvento(sqlRowSet.getBigDecimal("provento"));
		     obj.setDesconto(sqlRowSet.getBigDecimal("desconto"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	     return obj;
	}
	 
	
	@Override
	public ContraChequeVO consultarUltimoContraCheque() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" ORDER BY contracheque.codigo DESC LIMIT 1");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
        }
        throw new Exception(DADOS_NAO_ENCONTRADOS);
	}

	private String getSQLFichaFinanceira() {
		
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT cc.codigo, u.nome as unidadeEnsino, cp.descricao as periodo, cfp.datacompetencia as competencia, fc.matriculaCargo, p.nome as nomeFuncionario, ")
        .append(" c.nome as nomeCargo, fc.situacaofuncionario as situacao, fc.dataAdmissao as dataAdmissao, fc.dataDemissao as dataDemissao, fc.salario, ")
        .append(" fc.secaofolhapagamento, s.identificador, cc.totalprovento as provento, cc.totaldesconto as desconto, cc.totalreceber as areceber, ")
        .append(" cc.basecalculoirrf as baseCalculoIrrf, cc.basecalculoinss as baseCalculoInss, cc.previdenciapropria as previdenciaPropria ,")
        .append(" cfp.datacompetencia as \"cfp.datacompetencia\", fc.matriculacargo, fc.comissionado , c.codigo as \"cargo.codigo\", c.nome as \"cargo.nome\"")
        .append(" from contracheque cc")
        .append(" left  join templatelancamentofolhapagamento templatelancamentofolhapagamento on templatelancamentofolhapagamento.codigo = cc.templatelancamentofolhapagamento ")
        .append(" inner join competenciafolhapagamento cfp on cfp.codigo = cc.competenciafolhapagamento ") 
        .append(" inner join competenciaperiodofolhapagamento cp on cp.competenciafolhapagamento = cc.competenciafolhapagamento ")
        .append(" inner join funcionariocargo fc on fc.codigo = cc.funcionariocargo ")
        .append(" left join secaofolhapagamento s on s.codigo = fc.secaofolhapagamento ") 
        .append(" inner join funcionario f on fc.funcionario = f.codigo ")
        .append(" inner join pessoa p on f.pessoa = p.codigo ")
        .append(" inner join cargo c on c.codigo = fc.cargo ")
        .append(" inner join unidadeensino u on u.codigo = fc.unidadeensino");

        return sql.toString();
	}

	@Override
	public List<FichaFinanceiraRelVO> montarDadosRelatorioFichaFinanceira(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo) throws Exception {
		return consultarFichaFinanceiraRelatorio(obj, competencia, periodo);
	}
	private List<FichaFinanceiraRelVO> consultarFichaFinanceiraRelatorio(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo) throws Exception {
		List<FichaFinanceiraRelVO> lista = new ArrayList<>();
		List<Object> filtros = new ArrayList<>();

        StringBuilder sql = new StringBuilder(getSQLFichaFinanceira());
        sql.append(" WHERE 1 = 1");

        if (Uteis.isAtributoPreenchido(competencia.getCodigo())) {
        	sql.append(" AND cfp.codigo = ?");
        	filtros.add(competencia.getCodigo());

        	if (Uteis.isAtributoPreenchido(periodo.getCodigo())) {
        		sql.append(" AND cp.codigo = ?");
            	filtros.add(periodo.getCodigo());
        	}
        }
        
        sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(obj));
        sql.append(" order by fc.secaofolhapagamento");

        SqlRowSet dadosSql = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        while (dadosSql.next()) {
        	ContraChequeVO contraChequeVO = this.consultarTodosPorChavePrimaria(dadosSql.getLong("codigo"));
        	if (Uteis.isAtributoPreenchido(contraChequeVO.getCompetenciaFolhaPagamento().getCodigo())) {
        		contraChequeVO.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(contraChequeVO.getCompetenciaFolhaPagamento().getCodigo().longValue()));
        	}

        	if (Uteis.isAtributoPreenchido(contraChequeVO.getFuncionarioCargo().getCodigo())) {
        		contraChequeVO.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(contraChequeVO.getFuncionarioCargo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
        	}
        	contraChequeVO.setPeriodo(periodo);
            lista.add(montarDadosFichaFinanceira(contraChequeVO));
        }
        return lista;
	}

	/**
	 * Inicia a chamada para gerar a folha de pagamento do funcionarios que se enuqadram nos filtros selecionados <br>
	 * Com a opcao de iniciar (apagar os registros dos contras cheques) 
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuarioLogado, List<FuncionarioCargoVO> listaDeFuncionarios) throws Exception{

		if(listaDeFuncionarios == null || listaDeFuncionarios.isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumFuncionarioSelecionado"));
		}

		//realizarCalculoContraChequeDaListaDeFuncionariosFiltrados(lancamento, listaDeFuncionarios , usuarioLogado);
	}

	/**
	 * Valida as configurações financeiras da configuração geral do sistema.
	 * 
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 * @throws ConsistirException
	 */
	private ConfiguracaoFinanceiroVO validarConfiguracoesFinanceiras(UsuarioVO usuarioLogado) throws Exception, ConsistirException {
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(configuracaoGeralSistemaVO.getCodigo(), false, usuarioLogado);

		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getCategoriaDespesaVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_categoriaDespesaNaoVinculada"));
		}

		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getBancoPadraoRemessa())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_bancoPadraoRemessaNaoVinculada"));
		}

		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getFormaPagamentoPadrao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_formaPagamentoPadraoNaoVinculada"));
		}
		return configuracaoFinanceiroVO;
	}

	/**
	 * Consulta os registros da tabela de FuncionarioCargo pelos filtros informados no template
	 * @param lancamento
	 * @param usuarioLogado
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<FuncionarioCargoVO> consultarFuncionarioCargoPelosFiltrosInformadoNoTemplate(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento, UsuarioVO usuarioLogado) throws Exception {
		return getFacadeFactory().getFuncionarioCargoFacade().consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(templateLancamentoFolhaPagamento, Uteis.NIVELMONTARDADOS_TODOS);
	}

	/**
	 * Realizar o calculo dos contra cheques da lista de funcionarios
	 * 
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarCalculoContraChequeDaListaDeFuncionariosFiltrados(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado,
			BigDecimal valorDependente, ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF, List<String> identificadores, ScriptEngine engine,
			CalculoContraCheque calculoContraCheque, FuncionarioCargoVO funcionarioCargo) throws Exception{
		try {
			ContraChequeVO contraChequeVO = validarSeExisteContraChequeFuncionarioCargo(lancamentoFolhaPagamento, funcionarioCargo);

			preencherInformacoesBasicasDoContraCheque(contraChequeVO, funcionarioCargo, lancamentoFolhaPagamento);

			ControleLancamentoFolhapagamentoVO controleLancamento = montarDadosControleLancamento(lancamentoFolhaPagamento, funcionarioCargo, contraChequeVO);

			List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario = consultarEventosDoContraChequeDoFuncionario(controleLancamento, usuarioLogado);
			listaDeEventosDoFuncionario.sort(Comparator.comparing(EventoFolhaPagamentoVO::getPrioridade).thenComparing(EventoFolhaPagamentoVO::getOrdemCalculo));

			calculoContraCheque = CalculoContraCheque.inicializarCalculoContraCheque(funcionarioCargo, valorDependente, valorReferenciaIRRF, identificadores, lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento);
			realizarCalculoContraChequeDoFuncionario(controleLancamento.getContraCheque(), listaDeEventosDoFuncionario, calculoContraCheque, usuarioLogado, engine);

			calculoContraCheque = CalculoContraCheque.inicializarCalculoContraChequeParaRecalculoFolha(calculoContraCheque, funcionarioCargo, valorDependente, valorReferenciaIRRF, lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento);
			recalcularContraChequeCalculado(contraChequeVO, listaDeEventosDoFuncionario, calculoContraCheque, usuarioLogado, engine);

			realizarPresistenciaControleDeLancamentoEFerias(usuarioLogado, controleLancamento);			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_erroAoGerarFolha"));
		}
	}

	private ControleLancamentoFolhapagamentoVO montarDadosControleLancamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, FuncionarioCargoVO funcionarioCargo, ContraChequeVO contraChequeVO) {
		//Consulta dados da marcacao de ferias do funcionariocargo
		ControleMarcacaoFeriasVO controleMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarControlePorFuncionarioCargoEPeriodoGozo(funcionarioCargo, lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO().getDataCompetencia());
		//Consulta dados do controle do lancamento
		ControleLancamentoFolhapagamentoVO controleLancamento = consultarControleLancamentoFolhapagamentoVO(lancamentoFolhaPagamento, controleMarcacaoFerias, contraChequeVO);
		montarDadosDeFeriasDoFuncionario(controleLancamento);
		return controleLancamento;
	}

	private void realizarPresistenciaControleDeLancamentoEFerias(UsuarioVO usuarioLogado,
			ControleLancamentoFolhapagamentoVO controleLancamento) throws Exception {
		if(Uteis.isAtributoPreenchido(controleLancamento.getContraCheque())) {
			realizarPersistenciaControleMarcacaoFerias(controleLancamento, usuarioLogado);
			getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().persistir(controleLancamento, false, usuarioLogado);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ContraChequeVO validarSeExisteContraChequeFuncionarioCargo(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, FuncionarioCargoVO funcionarioCargo) throws Exception {
		ContraChequeVO contraChequeVO = consultarPorFuncionarioCargoECompetencia(funcionarioCargo, lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO());
		if (contraChequeVO == null || contraChequeVO.getCodigo() <= 0) {
			contraChequeVO = new ContraChequeVO();
		} else {
			contraChequeVO.setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarDados(contraChequeVO, lancamentoFolhaPagamento.getPeriodo()));
		}
		return contraChequeVO;
	}

	@Override
	public void gerarContaPagar(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, UsuarioVO usuarioLogado, List<String> errosContaPagar) throws Exception {

		UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);

		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = validarConfiguracoesFinanceiras(usuarioLogado);

		List<ContraChequeVO> lista = getFacadeFactory().getContraChequeInterfaceFacade().consultarContraChequePorTemplateLancamentoFolha(templateLancamentoFolhaPagamentoVO);

		if (Uteis.isAtributoPreenchido(lista)) {
			for (ContraChequeVO contraCheque : lista) {
				if (contraCheque.getTotalReceber().doubleValue() > 0.0) {
					List<ControleLancamentoFolhapagamentoVO> listaControleLancamentoFolha = getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarPorContraCheque(contraCheque.getCodigo());

					gravarContaPagar(usuarioLogado, unidadeEnsinoVO, configuracaoFinanceiroVO, listaControleLancamentoFolha, contraCheque);
				} else {
					errosContaPagar.add("Funcionário: " + contraCheque.getFuncionarioCargo().getFuncionarioVO().getPessoa().getNome()+"\r");
				}
			}
		} else {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LancamentoFolhaPagamento_erroGerarContaPagar"));
		}
		
		if (!lista.isEmpty()) {
			if (!errosContaPagar.isEmpty()) {
				StringBuilder mensagem = new StringBuilder("");
				for (String erro : errosContaPagar) {
					mensagem.append(erro+ "; ");
				}
				throw new ConsistirException(UteisJSF.internacionalizar("msg_erroLancamentoFolhaPagamento_erroGerarContaPagarParaFuncionarios").replace("{0}", mensagem.toString()));
			} else {
			}
		}
	}

	/**
	 * Grava a {@link ContaPagarVO} do funcionario pelos {@link ContraChequeVO} do mesmo no 
	 * periodo da competencia.
	 * 
	 * @param usuarioLogado
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param listaControleLancamentoFolha
	 * @throws Exception
	 * @throws ConsistirException
	 */
	private void gravarContaPagar(UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<ControleLancamentoFolhapagamentoVO> listaControleLancamentoFolha, ContraChequeVO contraCheque) throws Exception, ConsistirException {
		ContraChequeVO contraChequeVO = contraCheque;

		for (ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO : listaControleLancamentoFolha) {
			montarDadosContaPagar(usuarioLogado, unidadeEnsinoVO, configuracaoFinanceiroVO, contraChequeVO, controleLancamentoFolhapagamentoVO.getCompetenciaPeriodoFolhaPagamentoVO(), controleLancamentoFolhapagamentoVO);

			if (Uteis.isAtributoPreenchido(controleLancamentoFolhapagamentoVO.getContaPagarVO().getValor()) && controleLancamentoFolhapagamentoVO.getContaPagarVO().getValor() > 0) {
				getFacadeFactory().getContaPagarFacade().persistir(controleLancamentoFolhapagamentoVO.getContaPagarVO(), false, false, usuarioLogado);

				getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().atualizarContaPagarDoContraCheque(controleLancamentoFolhapagamentoVO, usuarioLogado);
			}
		}
	}

	/**
	 * Monta os dados para persistencia da {@link ContaPagarVO} pelo {@link ContraChequeVO} da {@link CompetenciaFolhaPagamentoVO} inforamda.
	 * 
	 * @param usuarioLogado
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param contraChequeVO
	 * @throws Exception
	 */
	private void montarDadosContaPagar(UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContraChequeVO contraChequeVO, CompetenciaPeriodoFolhaPagamentoVO periodo, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) throws Exception {
		FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(contraChequeVO.getFuncionarioCargo().getFuncionarioVO().getMatricula(), false, usuarioLogado);

		controleLancamentoFolhapagamentoVO.getContaPagarVO().setListaCentroResultadoOrigemVOs(new ArrayList<>());

		controleLancamentoFolhapagamentoVO.getContaPagarVO().setValor(consultarTotalPorContraChequeEPeriodo(contraChequeVO.getCodigo(), periodo.getCodigo()));
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setDataVencimento(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setData(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setSituacao("AP"); //PA(Conta Paga) - AP(Conta à pagar)
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setTipoSacado("FU"); //Funcionario
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setPessoa(controleLancamentoFolhapagamentoVO.getFuncionarioCargo().getFuncionarioVO().getPessoa());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setFuncionario(contraChequeVO.getFuncionarioCargo().getFuncionarioVO());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setUnidadeEnsino(unidadeEnsinoVO);
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.CAIXA_ECONOMICA_PAGAMENTO_SALARIO);
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setTipoOrigem(OrigemContaPagar.FOLHA_PAGAMENTO.getValor());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setNrDocumento(contraChequeVO.getCodigo().toString());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().getResponsavel().setCodigo(usuarioLogado.getCodigo());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setDescricao(controleLancamentoFolhapagamentoVO.getCompetenciaPeriodoFolhaPagamentoVO().getDescricao());

		//Montar dados aba remessa bancaria
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setFuncionario(funcionarioVO);
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setBancoRemessaPagar(configuracaoFinanceiroVO.getBancoPadraoRemessa());
		List<BancoVO> bancos = getFacadeFactory().getBancoFacade().consultarPorNrBanco(funcionarioVO.getNumeroBancoRecebimento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		if (!bancos.isEmpty()) {
			controleLancamentoFolhapagamentoVO.getContaPagarVO().setBancoRecebimento(bancos.get(0));
		}
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setFormaPagamentoVO(configuracaoFinanceiroVO.getFormaPagamentoPadrao());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setContaCorrenteRecebimento(funcionarioVO.getContaCorrenteRecebimento());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setNumeroAgenciaRecebimento(funcionarioVO.getNumeroAgenciaRecebimento());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setDigitoCorrenteRecebimento(funcionarioVO.getDigitoCorrenteRecebimento());
		controleLancamentoFolhapagamentoVO.getContaPagarVO().setDigitoAgenciaRecebimento(funcionarioVO.getDigitoAgenciaRecebimento());

		montarDadosCentroResultadoOrigem(unidadeEnsinoVO, configuracaoFinanceiroVO, controleLancamentoFolhapagamentoVO);
	}

	private double consultarTotalPorContraChequeEPeriodo(int codigoContraCheque, int periodo) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append(" select sum(provento) - sum(desconto) as resultado from contrachequeevento where contracheque = ? and periodo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoContraCheque, periodo);

        return (double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "resultado", TipoCampoEnum.DOUBLE);
	}

	/**
	 * Monta os dados do {@link CentroResultadoOrigemVO}
	 * 
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param contraChequeVO
	 * @return
	 * @throws Exception
	 */
	private void montarDadosCentroResultadoOrigem(UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) throws Exception {

		if (controleLancamentoFolhapagamentoVO.getContaPagarVO().getListaCentroResultadoOrigemVOs().isEmpty()) {
			CentroResultadoOrigemVO centroResultadoOrigemVO = new CentroResultadoOrigemVO();
			centroResultadoOrigemVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
			centroResultadoOrigemVO.setCentroResultadoAdministrativo(unidadeEnsinoVO.getCentroResultadoVO());
			centroResultadoOrigemVO.setDepartamentoVO(controleLancamentoFolhapagamentoVO.getFuncionarioCargo().getDepartamento());
			centroResultadoOrigemVO.setQuantidade(1.0);
			centroResultadoOrigemVO.setPorcentagem(100.0);
	
			if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getCategoriaDespesaVO())) {
				throw new Exception("Nenhuma Categoria de Despesa vinculada na configuração financeira.");
			}
			centroResultadoOrigemVO.setCategoriaDespesaVO(configuracaoFinanceiroVO.getCategoriaDespesaVO());
	
			centroResultadoOrigemVO.setValor(controleLancamentoFolhapagamentoVO.getContaPagarVO().getValor());
			
			if (centroResultadoOrigemVO.isCategoriaDespesaInformada()) {
				List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum = new ArrayList<>();
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(centroResultadoOrigemVO.getCategoriaDespesaVO(), listaSelectItemTipoNivelCentroResultadoEnum);
				if(!listaSelectItemTipoNivelCentroResultadoEnum.isEmpty()){
					centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) listaSelectItemTipoNivelCentroResultadoEnum.get(0).getValue());	
				}
			}

			controleLancamentoFolhapagamentoVO.getContaPagarVO().getListaCentroResultadoOrigemVOs().add(centroResultadoOrigemVO);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosDeFeriasDoFuncionario(ControleLancamentoFolhapagamentoVO controleLancamento) {
		if(Uteis.isAtributoPreenchido(controleLancamento.getControleMarcacaoFeriasVO())) {
			controleLancamento.getFuncionarioCargo().setInicioGozoFerias(controleLancamento.getControleMarcacaoFeriasVO().getMarcacaoFerias().getDataInicioGozo());
			controleLancamento.getFuncionarioCargo().setFinalGozoFerias(controleLancamento.getControleMarcacaoFeriasVO().getMarcacaoFerias().getDataFinalGozo());	
		}
	}

	/**
	 * Consulta o controle do lancamento do contracheque e retorna os dados atualizados populados
	 * @param lancamentoFolhaPagamento
	 * @param controleMarcacaoFerias
	 * @param contraChequeVO 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ControleLancamentoFolhapagamentoVO consultarControleLancamentoFolhapagamentoVO(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, ControleMarcacaoFeriasVO controleMarcacaoFerias, ContraChequeVO contraChequeVO) {
		
		ControleLancamentoFolhapagamentoVO controle;
		try {
			controle = getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarPorFuncionarioCargoCompetenciaEPeriodo(contraChequeVO.getFuncionarioCargo(), lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento.getPeriodo());
		} catch (Exception e) {
			controle = new ControleLancamentoFolhapagamentoVO();
		}

		controle.setContraCheque(contraChequeVO);
		controle.setCompetenciaFolhaPagamentoVO(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO());
		controle.setCompetenciaPeriodoFolhaPagamentoVO(lancamentoFolhaPagamento.getPeriodo());
		controle.setAnoCompetencia(UteisData.getAnoData(lancamentoFolhaPagamento.getDataCompetencia()));
		controle.setMesCompetencia(UteisData.getMesData(lancamentoFolhaPagamento.getDataCompetencia()));
		controle.setControleMarcacaoFeriasVO(controleMarcacaoFerias);
		controle.setTemplateLancamentoFolhaPagamentoVO(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento());
		controle.setFuncionarioCargo(contraChequeVO.getFuncionarioCargo());
		
		return controle;
	}

	private void realizarPersistenciaControleMarcacaoFerias(ControleLancamentoFolhapagamentoVO controleLancamento, UsuarioVO usuarioLogado) throws Exception {
		if (Uteis.isAtributoPreenchido(controleLancamento.getControleMarcacaoFeriasVO().getCodigo())) {
			if (controleLancamento.getContraCheque().getLancadoAdiantamentoFerias()) {
				controleLancamento.getControleMarcacaoFeriasVO().setAdiantamentoFerias(controleLancamento.getContraCheque());
				controleLancamento.getControleMarcacaoFeriasVO().setLancadoAdiantamento(true);
			}

			if(controleLancamento.getContraCheque().getLancadoReciboFerias()) {
				controleLancamento.getControleMarcacaoFeriasVO().setReciboFerias(controleLancamento.getContraCheque());
				controleLancamento.getControleMarcacaoFeriasVO().setLancadoReciboNoContraCheque(true);
			}
			getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().persistir(controleLancamento.getControleMarcacaoFeriasVO(), false, usuarioLogado);
		}
	}

	/**
	 * Preenche as informacoes basicas do Contra Cheque antes do calculo
	 * 
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 * @param lancamentoFolhaPagamento
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherInformacoesBasicasDoContraCheque(ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) {
		contraChequeVO.setTemplateLancamentoFolhaPagamentoVO(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento());
		contraChequeVO.setCompetenciaFolhaPagamento(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO());
		contraChequeVO.setPeriodo(lancamentoFolhaPagamento.getPeriodo());
		contraChequeVO.setFuncionarioCargo(funcionarioCargo);
	}
	
	
	/**
	 * Preenche as informacoes do FuncionarioCargo no ContraCheque <br>
	 * Essas informacoes sao salvas para manter o historico desse valores 
	 * 
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherInformacoesDoFuncionarioCargoNoContraCheque(ContraChequeVO contraChequeVO) {
		FuncionarioCargoVO funcionarioCargo = contraChequeVO.getFuncionarioCargo();

		contraChequeVO.setTipoRecebimento(funcionarioCargo.getTipoRecebimento().getValor());
		contraChequeVO.setSalario(funcionarioCargo.getSalario());
		contraChequeVO.setPrevidencia(funcionarioCargo.getPrevidencia());
		contraChequeVO.setOptanteTotal(funcionarioCargo.getOptanteTotal());
	}

	
	/**
	 * Metodo que retorna os eventos que serao lancados no contrachque do funcionario
	 * de acordo com o template do lancamento
	 * 
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 * @param controleLancamento
	 * @param usuarioLogado
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<EventoFolhaPagamentoVO> consultarEventosDoContraChequeDoFuncionario(ControleLancamentoFolhapagamentoVO controleLancamento,	UsuarioVO usuarioLogado) throws Exception{

		List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario = new ArrayList<>();

		//EVENTOS QUE ESTAO NO CONTRACHHEQUE
		if(controleLancamento.getContraCheque().getContraChequeEventos().size() > 0) {
			adicionarEventosQueEstaoNoContraChequeDoFuncionario(listaDeEventosDoFuncionario, controleLancamento.getContraCheque());
		}

		//EVENTOS DE RESCISAO
		if(controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancarRescisao()) {
			lancarEventosDeRescisao(listaDeEventosDoFuncionario, controleLancamento);
		}

		//EVENTOS DE FERIAS
		if(controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancarFerias() || controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancarAdiantamentoFerias()) {
			lancarEventosDeFolhaDeFerias(listaDeEventosDoFuncionario, controleLancamento);

			boolean inicioFeriasFuncionarioIgualMesCompetencia = validarInicioFeriasFuncionarioIgualMesCompetencia(controleLancamento);

			if(controleLancamento.getControleMarcacaoFeriasVO().getMarcacaoFerias().getPagarParcela13() && controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancarFerias() && inicioFeriasFuncionarioIgualMesCompetencia) {
				SindicatoVO sindicatoVO = getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(controleLancamento.getFuncionarioCargo().getSindicatoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA);
				adicionarEventoDeDecimoTerceiroPrimeiraParcela(listaDeEventosDoFuncionario, controleLancamento, sindicatoVO);
			}
		}

		//EVENTOS DE DECIMO TERCEIRO
		if(controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancar13Parcela1() || controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancar13Parcela2()) {
			lancarEventosDeDecimoTerceiro(listaDeEventosDoFuncionario, controleLancamento, usuarioLogado);
		}

		lancarEventosDeFolhaNormal(listaDeEventosDoFuncionario, controleLancamento, usuarioLogado);

		return listaDeEventosDoFuncionario;
	}

	private void lancarEventosDeDecimoTerceiro(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, UsuarioVO usuarioLogado) throws Exception {
		
		TemplateLancamentoFolhaPagamentoVO template = controleLancamentoFolhapagamentoVO.getTemplateLancamentoFolhaPagamentoVO();
		
		SindicatoVO sindicatoVO = getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(controleLancamentoFolhapagamentoVO.getFuncionarioCargo().getSindicatoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA);
		
		// Caso o sindicato nao seja encontrado retorna e nao e possivel continuar o processamento
		if(!Uteis.isAtributoPreenchido(sindicatoVO))
			return;
		
		if(template.getLancar13Parcela1()) {
			adicionarEventoDeDecimoTerceiroPrimeiraParcela(listaDeEventosDoFuncionario, controleLancamentoFolhapagamentoVO, sindicatoVO);	
		}
		
		boolean segundaParcelaLancada = validarSegundaParcelaDecimoTerceiroLancada(controleLancamentoFolhapagamentoVO);
		if(template.getLancar13Parcela2() && !segundaParcelaLancada) {
				adicionarEventosDaSegundaParcelaDoDecimoTerceiro(listaDeEventosDoFuncionario, controleLancamentoFolhapagamentoVO, sindicatoVO);
		}
	}

	private void adicionarEventosDaSegundaParcelaDoDecimoTerceiro(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, SindicatoVO sindicatoVO) throws Exception {
		
		ControleLancamentoFolhapagamentoVO controleLancamentoPrimeiraParcela = getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarControlePrimeiraParcela13(controleLancamentoFolhapagamentoVO.getFuncionarioCargo(), controleLancamentoFolhapagamentoVO.getAnoCompetencia()); 
		
		if(Uteis.isAtributoPreenchido(controleLancamentoPrimeiraParcela)) {
			adicionarEventoDescontoPrimeiraParcelaDecimoTerceiro(listaDeEventosDoFuncionario, controleLancamentoPrimeiraParcela, sindicatoVO);			
			controleLancamentoFolhapagamentoVO.setSegundaParcela13(true);
		}
		
		adicionarEventosDoDecimoTerceiro(listaDeEventosDoFuncionario, controleLancamentoFolhapagamentoVO);
		
	}

	
	/**
	 * Adiciona os evento padroes do 13 mais as medias informadas no evento e no sindicato
	 * @param listaDeEventosDoFuncionario
	 * @param controleLancamentoFolhapagamentoVO
	 */
	private void adicionarEventosDoDecimoTerceiro(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) {
		
		adicionarEventosDeMediaDo13(controleLancamentoFolhapagamentoVO.getFuncionarioCargo(), controleLancamentoFolhapagamentoVO.getAnoCompetencia(), listaDeEventosDoFuncionario);
		adicionarEventosDe13(listaDeEventosDoFuncionario);
		
	}

	private void adicionarEventosDe13(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) {
		
		List<EventoFolhaPagamentoVO> eventosDo13 = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarEventosDo13();

		if(eventosDo13 == null || eventosDo13.isEmpty()) {
			return;
		}

		for(EventoFolhaPagamentoVO evento : eventosDo13) {
			if(!listaDeEventosDoFuncionario.contains(evento)) {
				listaDeEventosDoFuncionario.add(evento);
			}
		}		
	}

	private void adicionarEventosDeMediaDo13(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos) {
		getFacadeFactory().getSindicatoMedia13InterfaceFacade().consultarEventosDeMediaDe13(funcionarioCargo, anoCompetencia, listaDeEventos);
	}

	/**
	 * Adiciona o evento de desconto da primeira parcela de 13 na lista para calculo caso ele nao esteja na lista
	 * 
	 * @param listaDeEventosDoFuncionario
	 * @param controleLancamentoFolhapagamentoVO
	 * @throws Exception
	 */
	private void adicionarEventoDescontoPrimeiraParcelaDecimoTerceiro(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, SindicatoVO sindicatoVO) throws Exception {

		EventoFolhaPagamentoVO eventoDescontoPrimeiraParcela13 = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(sindicatoVO.getEventoDescontoPrimeiraParcela13().getCodigo(), null, Uteis.NIVELMONTARDADOS_TODOS);
		
		if(Uteis.isAtributoPreenchido(eventoDescontoPrimeiraParcela13) && !listaDeEventosDoFuncionario.contains(eventoDescontoPrimeiraParcela13)) {
			
			eventoDescontoPrimeiraParcela13.setValorTemporario(adicionarValorDeDescontoDaPrimeiraParcela13(controleLancamentoFolhapagamentoVO.getContraCheque(), sindicatoVO.getEventoPrimeiraParcela13()));
			eventoDescontoPrimeiraParcela13.setValorInformado(true);
			
			listaDeEventosDoFuncionario.add(eventoDescontoPrimeiraParcela13);
		}
	}

	private BigDecimal adicionarValorDeDescontoDaPrimeiraParcela13(ContraChequeVO contraCheque, EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		return getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarValorDoEventoDaPrimeiraParcela13(contraCheque, eventoFolhaPagamentoVO);
	}

	/**
	 * Adiciona o evento de primeira parcela do 13 cadastrado no sindicato caso ele ja nao esteja na lista
	 * @param listaDeEventosDoFuncionario
	 * @param controleLancamentoFolhapagamentoVO
	 * @param sindicatoVO
	 * @throws Exception
	 */
	private void adicionarEventoDeDecimoTerceiroPrimeiraParcela(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, SindicatoVO sindicatoVO) throws Exception{

		if(!validarDecimoTerceiroJaLancado(controleLancamentoFolhapagamentoVO)) {
			if (!Uteis.isAtributoPreenchido(sindicatoVO.getEventoPrimeiraParcela13().getCodigo())) {
 				throw new ConsistirException(UteisJSF.internacionalizar("msg_Sindicato_eventoPrimeiraParcela").replace("{0}", sindicatoVO.getParceiroVO().getRazaoSocial()));
			}
			EventoFolhaPagamentoVO eventoDecimoTerceiroPrimeiraParcela = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(sindicatoVO.getEventoPrimeiraParcela13().getCodigo(), null, Uteis.NIVELMONTARDADOS_TODOS);

			if(Uteis.isAtributoPreenchido(eventoDecimoTerceiroPrimeiraParcela) && !listaDeEventosDoFuncionario.contains(eventoDecimoTerceiroPrimeiraParcela)) {
				listaDeEventosDoFuncionario.add(eventoDecimoTerceiroPrimeiraParcela);
				controleLancamentoFolhapagamentoVO.setPrimeiraParcela13(true);
			}
		}
	}

	/**
	 * Retorna true caso ja tenha sido lancado contracheque para a primeira ou segunda parcela do 13
	 * 
	 * @param competenciaFolhaPagamentoVO
	 * @return
	 */
	private boolean validarDecimoTerceiroJaLancado(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) {
		return getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarDecimoTerceiroJaLancado(controleLancamentoFolhapagamentoVO.getFuncionarioCargo(), controleLancamentoFolhapagamentoVO.getAnoCompetencia());
	}
	
	private boolean validarSegundaParcelaDecimoTerceiroLancada(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) {
		return getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().validarDecimoTerceiroSegundaParcelaLancada(controleLancamentoFolhapagamentoVO.getFuncionarioCargo(), controleLancamentoFolhapagamentoVO.getAnoCompetencia());
	}

	private void lancarEventosDeFolhaNormal(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, UsuarioVO usuarioLogado) {

		final TemplateLancamentoFolhaPagamentoVO template = controleLancamentoFolhapagamentoVO.getTemplateLancamentoFolhaPagamentoVO();
		final ContraChequeVO contraChequeVO = controleLancamentoFolhapagamentoVO.getContraCheque();
		final FuncionarioCargoVO funcionarioCargo = controleLancamentoFolhapagamentoVO.getFuncionarioCargo();

		//EVENTOS FIXOS DOS FUNCIONARIOS
		if(template.getLancarEventosFuncionario()) {adicionarEventosFixoDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, false);}

		//EVENTOS DE EMPRESTIMOS
		if(template.getLancarEmprestimos()) {adicionarEventosDeEmprestimo(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, false);}

		//EVENTOS CONTIDOS NO GRUPO DE LANCAMENTO
		if(template.getLancarEventosGrupo()) {	adicionarEventosDoTemplateGrupoDeLancamento(listaDeEventosDoFuncionario, template, contraChequeVO);	}

		//EVENTOS DE VALE TRANSPORTE
		if(template.getLancarValeTransporte()) {adicionarEventosDeValeTransporte(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);}

		//EVENTOS DE SALARIO COMPOSTO
		if(template.getLancarSalarioComposto()) {adicionarEventosDoSalarioCompostoDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);	}

		//EVENTOS DE FOLHA NORMAL
		if(template.getLancarEventosFolhaNormal()) {adicionarEventosDeFolhaNormal(listaDeEventosDoFuncionario);}

		//EVENTOS DE PENSAO
		if(template.getLancarPensao()) {adicionarEventosDePensaoDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, usuarioLogado);	}

		//EVENTOS DE FALTA
		adicionarEventosDeFalta(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);

		//EVENTOS VINCULADOS
		if(!listaDeEventosDoFuncionario.isEmpty()) {adicionarEventosVinculados(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);	}
	}

	private void adicionarEventosDeFolhaNormal(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) {
		getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().adicionarEventosDeFolhaNormal(listaDeEventosDoFuncionario);
	}

	public void adicionarEventosDePensaoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario) {
		getFacadeFactory().getFuncionarioDependenteInterfaceFacade().adicionarEventosDePensaoDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, usuario);
	}

	private void adicionarEventosDeFalta(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario,
			ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		try {			
			getFacadeFactory().getFaltasFuncionarioInterfaceFacade().adicionarEventosDeFaltasFuncionario(contraChequeVO.getCompetenciaFolhaPagamento(), funcionarioCargo, listaDeEventosDoFuncionario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * Lanca os eventos de Rescisao ser calculado na folha do funcionario 
	 * @param listaDeEventosDoFuncionario
	 * @param controleLancamento
	 * @param usuarioLogado
	 */
	private void lancarEventosDeRescisao(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamento) {

		//Consulta dados da rescisao
		RescisaoIndividualVO rescisaoFuncionario = getFacadeFactory().getRescisaoIndividualInterfaceFacade().consultarPorFuncionarioCargoECompetencia(controleLancamento.getFuncionarioCargo(), controleLancamento.getCompetenciaFolhaPagamentoVO());
		
		if(!Uteis.isAtributoPreenchido(rescisaoFuncionario))  
			return;
		
		try {
			SindicatoVO sindicatoVO = getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(controleLancamento.getFuncionarioCargo().getSindicatoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA);
			
			// Caso o sindicato nao seja encontrado retorna e nao e possivel continuar o processamento
			if(!Uteis.isAtributoPreenchido(sindicatoVO))
				return;
			
			if(!validarRescisaoNaoFoiLancada(controleLancamento)) {
				adicionarEventosDeMediaDaRescisao(controleLancamento.getFuncionarioCargo(), controleLancamento.getAnoCompetencia(), listaDeEventosDoFuncionario);
				adicionarEventosDeRescisao(listaDeEventosDoFuncionario);
				
				controleLancamento.setRescisao(Boolean.TRUE);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void adicionarEventosDeMediaDaRescisao(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos) {
		getFacadeFactory().getSindicatoMediaRescisaoInterfaceFacade().consultarEventosDeMediaDeRescisao(funcionarioCargo, anoCompetencia, listaDeEventos, TipoEventoMediaRescisaoEnum.INCIDE_13_PROPORCIONAL);
		getFacadeFactory().getSindicatoMediaRescisaoInterfaceFacade().consultarEventosDeMediaDeRescisao(funcionarioCargo, anoCompetencia, listaDeEventos, TipoEventoMediaRescisaoEnum.INCIDE_FERIAS_PROPORCIONAIS);
		getFacadeFactory().getSindicatoMediaRescisaoInterfaceFacade().consultarEventosDeMediaDeRescisao(funcionarioCargo, anoCompetencia, listaDeEventos, TipoEventoMediaRescisaoEnum.INCIDE_FERIAS_VENCIDAS);
	}
	
	private void adicionarEventosDeRescisao(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) {
		
		List<EventoFolhaPagamentoVO> eventosDeRescisao = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarEventosDeRescisao();

		if(eventosDeRescisao == null || eventosDeRescisao.isEmpty()) {
			return;
		}

		for(EventoFolhaPagamentoVO evento : eventosDeRescisao) {
			if(!listaDeEventosDoFuncionario.contains(evento)) {
				listaDeEventosDoFuncionario.add(evento);
			}
		}		
	}
	
	
	/**
	 * Valida se a rescisao ja foi lancada
	 * @param controleLancamentoFolhapagamentoVO
	 * @return
	 */
	private boolean validarRescisaoNaoFoiLancada(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) {
		return getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().validarRescisaoNaoFoiLancada(controleLancamentoFolhapagamentoVO.getFuncionarioCargo(), controleLancamentoFolhapagamentoVO.getAnoCompetencia());
	}
	
	private void lancarEventosDeFolhaDeFerias(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamento) {

		if(Uteis.isAtributoPreenchido(controleLancamento.getControleMarcacaoFeriasVO())) {

			ReciboFeriasVO recibo = getFacadeFactory().getReciboFeriasInterfaceFacade().consultarPorMarcacao(controleLancamento.getControleMarcacaoFeriasVO().getMarcacaoFerias(), Uteis.NIVELMONTARDADOS_TODOS, null, false);

			boolean inicioFeriasFuncionarioIgualMesCompetencia = validarInicioFeriasFuncionarioIgualMesCompetencia(controleLancamento);

			if (inicioFeriasFuncionarioIgualMesCompetencia) {
				adicionarEventosFerias(listaDeEventosDoFuncionario, controleLancamento, recibo);
				adicionarEventoDescontoAdiantamentoFerias(listaDeEventosDoFuncionario, controleLancamento, recibo);
			}

			if(controleLancamento.getTemplateLancamentoFolhaPagamentoVO().getLancarAdiantamentoFerias()) {
				//Validar que lancamento do adiantamento e no mes anterior
				int qtdMesesPermiteAdiantamento = 1;
				Date dataCompetencia = controleLancamento.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia();
				Date dataInicioGozo = controleLancamento.getControleMarcacaoFeriasVO().getMarcacaoFerias().getDataInicioGozo();
				if(UteisData.obterQuantidadeMesesPeriodo(dataCompetencia, dataInicioGozo) == ++qtdMesesPermiteAdiantamento) {
					adicionarEventoDeAdiantamentoDeFerias(listaDeEventosDoFuncionario, controleLancamento, recibo);	
				}
			}
		}
		
	}

	/**
	 * Adiciona o evento de desconto de adiantamento de ferias casso no estela marcado no 
	 * {@link ControleMarcacaoFeriasVO}.
	 * 
	 * @param listaDeEventosDoFuncionario
	 * @param funcionarioCargo
	 * @param controleMarcacaoFerias
	 * @param recibo
	 */
	private void adicionarEventoDescontoAdiantamentoFerias(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamento, ReciboFeriasVO recibo) {
		if(controleLancamento.getControleMarcacaoFeriasVO().getLancadoAdiantamento()) {
			try {
				EventoFolhaPagamentoVO evento = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultaEventoDeDescontoDoAdiantamentoDoContraCheque(controleLancamento.getFuncionarioCargo(), recibo);
				if (!listaDeEventosDoFuncionario.contains(evento)) {
					listaDeEventosDoFuncionario.add(evento);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Valida que o inicio das ferias do funcionario esta no mes da competencia do contra cheque
	 * 
	 * @param contraChequeVO
	 * @param controleMarcacaoFerias
	 * @param template
	 * @return
	 * @throws Exception 
	 */
	private boolean validarInicioFeriasFuncionarioIgualMesCompetencia(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO) {
		try {
			//Valida que o inicio das ferias do funcionario esta no mes da competencia do contra cheque
			return UteisData.validarSeDataInicioMenorOuIgualQueDataFinalDesconsiderandoDias(controleLancamentoFolhapagamentoVO.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia(), controleLancamentoFolhapagamentoVO.getControleMarcacaoFeriasVO().getMarcacaoFerias().getDataInicioGozo());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Adiciona o evento de Adiantamento de Ferias.
	 * 
	 * @param listaDeEventosDoFuncionario
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 * @param recibo
	 * @param controleMarcacaoFerias
	 */
	private void adicionarEventoDeAdiantamentoDeFerias(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamento, ReciboFeriasVO recibo) {
		try {
			controleLancamento.getContraCheque().setLancadoAdiantamentoFerias(Boolean.TRUE);

			EventoFolhaPagamentoVO evento = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultaEventoDeAdiantamentoDoContraCheque(controleLancamento.getFuncionarioCargo(), recibo);
			
			if(!listaDeEventosDoFuncionario.contains(evento)) {
				listaDeEventosDoFuncionario.add(evento);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adiciona os evento de ferias.
	 * 
	 * @param listaDeEventosDoFuncionario
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 * @param recibo
	 * @param controleMarcacaoFerias
	 */
	private void adicionarEventosFerias(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ControleLancamentoFolhapagamentoVO controleLancamento, ReciboFeriasVO recibo) {
		controleLancamento.getContraCheque().setLancadoReciboFerias(Boolean.TRUE);

		for(EventoFolhaPagamentoVO evento : recibo.getEventosPreenchidosDoReciboDeFeriasSetadosComInformacaoManual(true)) {
			if(evento.getValorTemporario().compareTo(BigDecimal.ZERO) > 0 && (!listaDeEventosDoFuncionario.contains(evento) || evento.getPermiteDuplicarContraCheque())) {
				evento.setValorInformado(true);
				listaDeEventosDoFuncionario.add(evento);
			}
		}
	}

	/**
	 * Adicionar eventos que estao vinculados aos eventos adicionados na lista
	 * tabela: EventoFolhaPagamentoItemVO
	 *  
	 * @param listaDeEventosDoFuncionario
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarEventosVinculados(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		getFacadeFactory().getEventoFolhaPagamentoItemInterfaceFacade().adicionarEventosVinculadosDosEventosDoContraChequeDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void adicionarEventosQueEstaoNoContraChequeDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO) {
		listaDeEventosDoFuncionario.addAll(contraChequeVO.montarDadosEventosPreenchidosDoContraChequeEvento());			
	}

	/**
	 * Realizar recalcudo da folha de pagamento do funcionário vindo do {@link ContraChequeVO}.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarRecalculoContraChequeDoFuncionario(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioLogado, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception {
		ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorReferenciaPorReferencia(ValorFixoEnum.IRRF.name(), new Date());
		List<String> identificadores = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarListaDeIdentificadoresAtivo();

		calculoContraCheque = CalculoContraCheque.inicializarCalculoContraCheque(contraChequeVO.getFuncionarioCargo(), contraChequeVO.getValorDependente(),
				valorReferenciaIRRF, identificadores, contraChequeVO.getCompetenciaFolhaPagamento(), contraChequeVO.getTemplateLancamentoFolhaPagamentoVO().getLancamentoFolhaPagamento());

		realizarCalculoContraChequeDoFuncionario(contraChequeVO, listaDeEventosDoFuncionario, calculoContraCheque, usuarioLogado, engine);

		calculoContraCheque = CalculoContraCheque.inicializarCalculoContraChequeParaRecalculoFolha(calculoContraCheque, contraChequeVO.getFuncionarioCargo(), contraChequeVO.getValorDependente(),
				valorReferenciaIRRF, contraChequeVO.getCompetenciaFolhaPagamento(), contraChequeVO.getTemplateLancamentoFolhaPagamentoVO().getLancamentoFolhaPagamento());
		recalcularContraChequeCalculado(contraChequeVO, listaDeEventosDoFuncionario, calculoContraCheque, usuarioLogado, engine);
	}
	
	/**
	 * Realiza o calculo do contra cheque.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarCalculoContraChequeDoFuncionario(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioLogado, ScriptEngine engine) throws Exception {
		validarDados(contraChequeVO, listaDeEventosDoFuncionario); 

		preencherInformacoesDoFuncionarioCargoNoContraCheque(contraChequeVO);

		List<ContraChequeEventoVO> listaContraChequeEventosCalculados = calcularValorDosEventos(contraChequeVO, listaDeEventosDoFuncionario, calculoContraCheque, engine, Boolean.FALSE);
		listaDeEventosDoFuncionario.sort(Comparator.comparing(EventoFolhaPagamentoVO::getPrioridade).thenComparing(EventoFolhaPagamentoVO::getOrdemCalculo));

		try {
			if (Uteis.isAtributoPreenchido(listaContraChequeEventosCalculados)) {
				contraChequeVO.setContraChequeEventos(listaContraChequeEventosCalculados);

				persistir(contraChequeVO, false, null);
				incluirContraChequeEventosDeImpostos(contraChequeVO, calculoContraCheque, usuarioLogado, listaContraChequeEventosCalculados, engine);

				//realizarAtualizacaoDosValoresDeReCalculoDoContraCheque(contraChequeVO.getCompetenciaFolhaPagamento(), usuarioLogado, contraChequeVO, engine, listaDeEventosDoFuncionario, calculoContraCheque, contraChequeVO.getTemplateLancamentoFolhaPagamentoVO().getLancamentoFolhaPagamento());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(e.getMessage());
		}
	}

	/**
	 * Realiza o Recalculo do Contracheque
	 * 
	 * @param contraChequeVO
	 * @param listaDeEventosDoFuncionario
	 * @param calculoContraCheque
	 * @param usuarioLogado
	 * @param engine
	 * @throws Exception
	 */
	private void recalcularContraChequeCalculado(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioLogado, ScriptEngine engine) throws Exception {
		montarDadosContraChequePorCalculoContraCheque(calculoContraCheque, contraChequeVO);
		realizarAtualizacaoDosValoresDeCalculoDoContraChequeEventos(calculoContraCheque, contraChequeVO.getCompetenciaFolhaPagamento(), usuarioLogado, contraChequeVO, engine, listaDeEventosDoFuncionario);
		montarDadosContraChequePorCalculoContraCheque(calculoContraCheque, contraChequeVO);

		persistir(contraChequeVO, false, usuarioLogado);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDados(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario)
			throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(contraChequeVO.getPeriodo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_periodo"));
		}
		
		if(listaDeEventosDoFuncionario.isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_eventosContracheque"));
		}
	}

	/**
	 * Calcula o valor do evento
	 * Atualiza os valores do calculo 
	 * Popula a lista com os contra cheque evento  
	 * 
	 * @param contraChequeVO
	 * @param listaDeEventosDoFuncionario
	 * @param calculoContraCheque
	 * @param engine - Instância do ScriptEngine para processar a formula
	 * @param recalculo - Se recalculo for true ele zera o valor temporario para processar a formula novamente.
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<ContraChequeEventoVO> calcularValorDosEventos(ContraChequeVO contraChequeVO, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, ScriptEngine engine, boolean recalculo) {
		calculoContraCheque.calcularNumeroDiasTrabalhados();
		calculoContraCheque.calcularNumeroDiasFerias();

		//variavel criada somente para verificar se o evento e INSS, e nao calcular o numero de avos para todo evento 
		Boolean inss = false;
		Boolean avosJaCalculados = false;
		
		List<ContraChequeEventoVO> listaDeContraChequeEvento = new ArrayList<>(); 
		for(EventoFolhaPagamentoVO evento : listaDeEventosDoFuncionario) {
			if (Uteis.isAtributoPreenchido(evento)) {
				try {
					if(Uteis.isAtributoPreenchido(evento.getCategoria()) && evento.getCategoria().equals(CategoriaEventoFolhaEnum.INSS)) {
						inss = true;
						calculoContraCheque.calcularAvosFerias(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia(), null, inss);
						calculoContraCheque.calcularAvos13(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia(), inss);
					} else if(inss || !avosJaCalculados){
						inss = false;
						avosJaCalculados = true;
						calculoContraCheque.calcularAvosFerias(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia(), null, inss);
						calculoContraCheque.calcularAvos13(contraChequeVO.getCompetenciaFolhaPagamento().getDataCompetencia(), inss);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				//TODO Zera o valor temporario para recalcular o imposto de renda.
				if (recalculo) {
					for (ContraChequeEventoVO contraChequeEventoVO : contraChequeVO.getContraChequeEventos()) {
						if (contraChequeEventoVO.getEventoFolhaPagamento().getCodigo().equals(evento.getCodigo())
								&& (evento.getNaturezaEvento().equals(NaturezaEventoFolhaPagamentoEnum.IMPOSTO_DE_RENDA))
								&& !evento.getInformadoManual()) {
							evento.setValorTemporario(BigDecimal.ZERO);
						}
					}
				}

				ContraChequeEventoVO contraChequeCalculado = retornarContraChequeEventoCalculado(contraChequeVO, calculoContraCheque, evento, engine);
				/*System.out.println("Identificador -> " + evento.getIdentificador() 
					+ " Provento ->" +  contraChequeCalculado.getProvento()
					+ " Desconto ->" +  contraChequeCalculado.getDesconto());*/

				//Adiciona na lista somente eventos com valor maior que zero
				if(Uteis.isAtributoPreenchido(contraChequeCalculado.getEventoFolhaPagamento()) && 
						contraChequeCalculado.recuperarValorDoEventoTratado().compareTo(BigDecimal.ZERO) > 0) {
					listaDeContraChequeEvento.add(contraChequeCalculado);
				}
			}
		}
		
		return listaDeContraChequeEvento;
	}

	/**
	 * Inclui eventos de impostos caso nao estejam lancados no contracheque
	 * 
	 * Outros eventos de impostos: INSS
	 * 
	 * @param contraChequeVO
	 * @param calculoContraCheque
	 * @param usuarioVO
	 * @param listaDeContraChequeEvento
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void incluirContraChequeEventosDeImpostos(ContraChequeVO contraChequeVO, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioVO, Collection<ContraChequeEventoVO> listaDeContraChequeEvento, ScriptEngine engine) {
		
		boolean validarIRRFExcedente = validarIRRFExcedente(calculoContraCheque, listaDeContraChequeEvento);  
		
		if(validarIRRFExcedente)
			incluirContraChequeEventosDeIRRF(contraChequeVO, calculoContraCheque, usuarioVO, engine);
		
	}
	
	/**
	 * Inserir eventos de IRRF caso nao exista
	 * 
	 * @param listaDeContraChequeEvento
	 * @param contraChequeVO
	 * @param calculoContraCheque
	 * @param usuarioVO 
	 * @param listaDeContraChequeEvento 
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void incluirContraChequeEventosDeIRRF(ContraChequeVO contraChequeVO, CalculoContraCheque calculoContraCheque, UsuarioVO usuarioVO, ScriptEngine engine) {
		
		//Adiciona evento de IRRF
		EventoFolhaPagamentoVO evento = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarEventoPorCategoriaEAgrupamento(CategoriaEventoFolhaEnum.IRRF, "efp.agrupamentoFolhaNormal");
		calculoContraCheque.setValorIRRFJaLancado(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarValorDoEventoDeIRRFDoContraCheque(contraChequeVO));
		calculoContraCheque.setBaseCalculoIRRF(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarValorDaBaseCalculoIRRFDoContraCheque(contraChequeVO));

		if (Uteis.isAtributoPreenchido(evento)) {
			ContraChequeEventoVO contraChequeCalculado = retornarContraChequeEventoCalculado(contraChequeVO, calculoContraCheque, evento, engine);
	
			//Adiciona na lista somente eventos com valor maior que zero
			if(Uteis.isAtributoPreenchido(contraChequeCalculado.getEventoFolhaPagamento()) && contraChequeCalculado.recuperarValorDoEventoTratado().compareTo(BigDecimal.ZERO) > 0) {
				try {
					contraChequeCalculado.setPeriodo(contraChequeVO.getPeriodo());
					contraChequeCalculado.setContraCheque(contraChequeVO);
					getFacadeFactory().getContraChequeEventoInterfaceFacade().persistir(contraChequeCalculado, false, usuarioVO);	
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * Validar que o periodo calculado incide IRRF e o evento de IRRF nao esta dentro do periodo do ContraChequeEventoVO
	 * true: lista de ContrachequeEventoVO incide IRRF e o evento de IRRF nao esta lancado na lista
	 * false: ou a lista de ContrachequeEventoVO nao incide IRRF ou o evento de IRRF esta lancado na lista
	 * 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private boolean validarIRRFExcedente(CalculoContraCheque calculoContraCheque, Collection<ContraChequeEventoVO> listaDeContraChequeEvento) {
		if(calculoContraCheque.getBaseCalculoIRRF().compareTo(BigDecimal.ZERO) > 0) {
			ContraChequeEventoVO contraChequeEventoVO = listaDeContraChequeEvento.stream().filter(p -> p.getEventoFolhaPagamento().getCategoria() != null && p.getEventoFolhaPagamento().getCategoria().equals(CategoriaEventoFolhaEnum.IRRF)).findFirst().orElse(null);
			//Valida se o evento de IRRF nao existe na lista
			if(contraChequeEventoVO == null || !Uteis.isAtributoPreenchido(contraChequeEventoVO.getEventoFolhaPagamento())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna o evento calculado montado no objeto ContraChequeEventoVO
	 * Caso o valor do calculo do evento seja zerado, retorna 
	 * @param listaDeContraChequeEvento
	 * @param contraChequeVO
	 * @param calculoContraCheque
	 * @param evento
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ContraChequeEventoVO retornarContraChequeEventoCalculado(ContraChequeVO contraChequeVO, CalculoContraCheque calculoContraCheque, EventoFolhaPagamentoVO evento, ScriptEngine engine) {
		getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().calcularEventoFolhaPagamento(evento, contraChequeVO.getFuncionarioCargo(), calculoContraCheque, engine);
		calculoContraCheque.atualizarValoresDeCalculo(evento);
		return getFacadeFactory().getContraChequeEventoInterfaceFacade().montarContraChequeEventoAPartirDoEvento(evento, contraChequeVO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ContraChequeVO consultarPorFuncionarioCargoECompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		String sql = " SELECT * FROM contracheque WHERE funcionarioCargo = ? and competenciaFolhaPagamento = ? ";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, funcionarioCargo.getCodigo(), competenciaFolhaPagamentoVO.getCodigo());

        try {
        	if (rs.next()) {
                return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            }	
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return new ContraChequeVO();
	}

	@Override
	public ContraChequeVO consultarPorFuncionarioCargoCompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		String sql = " SELECT * FROM contracheque WHERE funcionarioCargo = ? and competenciaFolhaPagamento = ? ";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, funcionarioCargo.getCodigo(), competenciaFolhaPagamentoVO.getCodigo());

		if(rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_COMBOBOX);
		} else {
			return new ContraChequeVO();
		}
	}

	/**
	 * Adicionar os eventos de emprestimo do funcionario na lista listaDeEventosDoFuncionario
	 * @param listaDeEventosDoFuncionario
	 * @param funcionarioCargo
	 */
	public void adicionarEventosDeEmprestimo(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias) {
		getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().adicionarEventosDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, reciboDeFerias);
	}
	
	
	/**
	 * Adicionar os eventos fixos do funcionario na lista listaDeEventosDoFuncionario
	 * 
	 * Adicionar os eventos de pensao do funcionario na lista listaDeEventosDoFuncionario
	 * 
	 * @param listaDeEventosDoFuncionario
	 * @param contraChequeVO
	 * @param funcionarioCargo
	 */
	@Override
	public void adicionarEventosFixoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias) {
		getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().adicionarEventosDoFuncionario(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo, reciboDeFerias);
	}
	
	/**
	 * Adicionar os eventos de vale transporte do funcionario na lista listaDeEventosDoFuncionario
	 * @param listaDeEventosDoFuncionario
	 * @param funcionarioCargo
	 */
	private void adicionarEventosDeValeTransporte(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().adicionarEventosDeValeTransporte(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);	
	}

	/**
	 * Adicionar os eventos do grupo da tela de lançamento na lista listaDeEventosDoFuncionario
	 * @param listaDeEventosDoFuncionario
	 * @param funcionarioCargo
	 */
	private void adicionarEventosDoTemplateGrupoDeLancamento(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, TemplateLancamentoFolhaPagamentoVO template, ContraChequeVO contraChequeVO) {
		getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().adicionarEventosDoGrupoLancamentoQueNaoEstaoNoContraCheque(listaDeEventosDoFuncionario, template, contraChequeVO);
	}
	
	/**
	 * Adicionar os eventos de salario composto do funcionario na lista listaDeEventosDoFuncionario
	 * @param listaDeEventosDoFuncionario
	 * @param funcionarioCargo
	 */
	private void adicionarEventosDoSalarioCompostoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		getFacadeFactory().getSalarioCompostoInterfaceFacade().adicionarEventosDoFuncionarioNoContraCheque(listaDeEventosDoFuncionario, contraChequeVO, funcionarioCargo);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarFolhaDePagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado, ScriptEngine engine) throws Exception {
		List<ContraChequeVO> listaContraCheque = getFacadeFactory().getContraChequeInterfaceFacade().consultarContraChequePorCompetenciaEFiltrosDoTemplate(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento());

		getFacadeFactory().getContraChequeEventoInterfaceFacade().cancelarContraCheque(lancamentoFolhaPagamento, usuarioLogado);
		cancelarLancamentoDaMarcacaodeFeriasDosContraChequesCancelados(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento(), usuarioLogado);
		realizarRecalculoListaContraCheque(listaContraCheque, lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), usuarioLogado, engine, lancamentoFolhaPagamento);
		
		getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().cancelarControleLancamentoFolhaPagamento(lancamentoFolhaPagamento, usuarioLogado);
		
		cancelarContaPagar(listaContraCheque, usuarioLogado);
	}

	private void cancelarContaPagar(List<ContraChequeVO> listaContraCheque, UsuarioVO usuarioLogado) throws Exception {
		for (ContraChequeVO contraChequeVO : listaContraCheque) {
			List<ControleLancamentoFolhapagamentoVO> lista = getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarPorContraCheque(contraChequeVO.getCodigo());

			for (ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO : lista) {
				if (Uteis.isAtributoPreenchido(controleLancamentoFolhapagamentoVO.getContaPagarVO())) {
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluirPorContaPagarETipoOrigem(controleLancamentoFolhapagamentoVO.getContaPagarVO().getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name(), usuarioLogado);
					getFacadeFactory().getContaPagarFacade().excluir(controleLancamentoFolhapagamentoVO.getContaPagarVO(), false, usuarioLogado);
					controleLancamentoFolhapagamentoVO.setContaPagarVO(new ContaPagarVO());
					getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().alterar(controleLancamentoFolhapagamentoVO, false, usuarioLogado);
				}
			}
		}

	}

	public void cancelarLancamentoDaMarcacaodeFeriasDosContraChequesCancelados(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO templateFP, UsuarioVO usuarioVO) {
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().cancelarLancamentoDoAdiantamentoDoReciboNoContraCheque(competencia, templateFP, usuarioVO);
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().cancelarLancamentoDoReciboDeFeriasNoContraCheque(competencia, templateFP, usuarioVO);
	}

	/**
	 * Recalcula os valores do contra cheque
	 * @param lancamentoFolhaPagamento
	 * @param usuarioLogado
	 * @throws Exception
	 */
	private void realizarRecalculoListaContraCheque(List<ContraChequeVO> listaContraCheque, CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, UsuarioVO usuarioLogado, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {
		
		if(Uteis.isAtributoPreenchido(listaContraCheque)) {
			for(ContraChequeVO contraCheque : listaContraCheque) {
				realizarAtualizacaoDosValoresDeCalculoDoContraCheque(competenciaFolhaPagamento, usuarioLogado, contraCheque, engine, lancamentoFolhaPagamento);
			}
		}
	}

	public void alterarLancadoAdiantamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {		
		List<Integer> codigosFuncionarios = getFacadeFactory().getContraChequeInterfaceFacade().consultarFuncionarioCargoPorLancamentoFolhaPagamento(lancamentoFolhaPagamento);

		for (Integer codigoFuncionario : codigosFuncionarios) {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().alterarLancadoAdiantamentoPorFuncionarioCargo(codigoFuncionario, Boolean.FALSE);
		}
	}

	/**
	 * Realiza a atualizacao dos valores de calculo do contra cheque
	 * 
	 * @param competenciaFolhaPagamento
	 * @param usuarioLogado
	 * @param contraCheque
	 * @throws Exception
	 */
	public void realizarAtualizacaoDosValoresDeCalculoDoContraCheque(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, UsuarioVO usuarioLogado, ContraChequeVO contraCheque, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {
		CalculoContraCheque calculoContraCheque = realizarSomaDosValoresJaCalculadosDoContraCheque(contraCheque.getFuncionarioCargo(), usuarioLogado, competenciaFolhaPagamento, contraCheque, false, engine, lancamentoFolhaPagamento);

		montarDadosContraChequePorCalculoContraCheque(calculoContraCheque, contraCheque);

		alterar(contraCheque, false, usuarioLogado);
	}
	
	public void realizarAtualizacaoDosValoresDeReCalculoDoContraCheque(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, UsuarioVO usuarioLogado, ContraChequeVO contraCheque, ScriptEngine engine, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, CalculoContraCheque calculoContraCheque, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception {
		calculoContraCheque = realizarSomaDosValoresJaCalculadosDoContraCheque(contraCheque.getFuncionarioCargo(), usuarioLogado, competenciaFolhaPagamento, contraCheque, true, engine, lancamentoFolhaPagamentoVO);


		//TODO PROBLEMA RECALCULANDO DUAS VEZES
		//contraCheque.setContraChequeEventos(calcularValorDosEventos(contraCheque, listaDeEventosDoFuncionario, calculoContraCheque, engine, Boolean.TRUE));
		montarDadosContraChequePorCalculoContraCheque(calculoContraCheque, contraCheque);
		realizarAtualizacaoDosValoresDeCalculoDoContraChequeEventos(calculoContraCheque, competenciaFolhaPagamento, usuarioLogado, contraCheque, engine, listaDeEventosDoFuncionario);

		alterar(contraCheque, false, usuarioLogado);
        getFacadeFactory().getContraChequeEventoInterfaceFacade().persistirTodos(contraCheque, false, usuarioLogado);
	}
	
	/**
	 * Atualizar valores dos eventos .
	 * 
	 * @param calculoContraCheque
	 * @param competenciaFolhaPagamento
	 * @param usuarioLogado
	 * @param contraCheque
	 * @param listaDeEventosDoFuncionario
	 * @param engine
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarAtualizacaoDosValoresDeCalculoDoContraChequeEventos(CalculoContraCheque calculoContraCheque, CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, UsuarioVO usuarioLogado, ContraChequeVO contraCheque, ScriptEngine engine, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) throws Exception {

		contraCheque.setContraChequeEventos(calcularValorDosEventos(contraCheque, listaDeEventosDoFuncionario, calculoContraCheque, engine, Boolean.TRUE));
		
	}

	/**
	 * Monta o objeto {@link ContraChequeVO} com os valores das incidencias do {@link CalculoContraCheque}
	 * 
	 * @param contraChequeVO
	 */
	public void montarDadosContraChequePorCalculoContraCheque(CalculoContraCheque calculoContraCheque, ContraChequeVO contraChequeVO) {
		
		contraChequeVO.setBaseCalculoINSS(calculoContraCheque.getBaseCalculoPrevidencia());
		contraChequeVO.setPrevidenciaPropria(calculoContraCheque.getBaseCalculoPrevidencia());
		
		contraChequeVO.setNumerosDependentes(calculoContraCheque.getNumerosDependentesIrrf());
		contraChequeVO.setValorDependente(calculoContraCheque.getValorDependente());
		
		contraChequeVO.setNumeroDependenteSalarioFamilia(calculoContraCheque.getNumerosDependentesSalFamilia());
		
		contraChequeVO.setFGTS(calculoContraCheque.getFgts());
		contraChequeVO.setDSR(calculoContraCheque.getDsr());
		contraChequeVO.setSalarioFamilia(calculoContraCheque.getSalarioFamilia());
		contraChequeVO.setPlanoSaude(calculoContraCheque.getPlanoSaude());
		
		contraChequeVO.setInformeRendimento(calculoContraCheque.getInformeRendimento());
		contraChequeVO.setRAIS(calculoContraCheque.getRais());
		
		calculoContraCheque.preencherValoresDeIRRF();
		
		contraChequeVO.setBaseCalculoIRRFFerias(calculoContraCheque.getBaseCalculoIRRFFerias());
		contraChequeVO.setBaseCalculoIRRF(calculoContraCheque.getBaseCalculoIRRF());
		contraChequeVO.setFaixa(calculoContraCheque.getPercentualIRRF());
		contraChequeVO.setValorIRRF(calculoContraCheque.getValorIRRF());
		contraChequeVO.setValorIRRFFerias(calculoContraCheque.getValorIRRFFerias());
		contraChequeVO.setDedutivel(calculoContraCheque.getDedutivelIRRF());
		
		contraChequeVO.setTotalProvento(calculoContraCheque.getProvento());
		contraChequeVO.setTotalDesconto(calculoContraCheque.getDesconto());
		contraChequeVO.setTotalReceber(calculoContraCheque.getProvento().subtract(calculoContraCheque.getDesconto()));
	}
	
	/**
	 * Retorna o objeto CalculoContraCheque com o somatorio dos valores do ContrachequeEvento  
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CalculoContraCheque realizarSomaDosValoresJaCalculadosDoContraCheque(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, ContraChequeVO contraCheque, boolean recalculo, ScriptEngine engine, LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {

		CalculoContraCheque calculoContraCheque = CalculoContraCheque.inicializarCalculoContraCheque(funcionarioCargo, competenciaFolhaPagamentoVO, lancamentoFolhaPagamento);
		if(Uteis.isAtributoPreenchido(contraCheque)) {
			contraCheque.setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(contraCheque.getCodigo(), false, usuario));

			realizarSomaDosValoresDaListaDeContraChequeEvento(contraCheque.getContraChequeEventos(), calculoContraCheque, competenciaFolhaPagamentoVO, contraCheque, recalculo, engine);			
		}
		return calculoContraCheque;
	}

	/**
	 * Calcula o valor do CalculoContraCheque pelos eventos do ContraChequeEventoVO sem executar a Formula do Eventos
	 * 
	 * @param listaDeContraChequeEvento
	 * @param calculoContraCheque
	 * @param contraCheque 
	 * @param competenciaFolhaPagamentoVO 
	 */
	private void realizarSomaDosValoresDaListaDeContraChequeEvento(List<ContraChequeEventoVO> listaDeContraChequeEvento, CalculoContraCheque calculoContraCheque, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, ContraChequeVO contraCheque, boolean recalculo, ScriptEngine engine) {
		calculoContraCheque.calcularNumeroDiasTrabalhados();
		calculoContraCheque.calcularNumeroDiasFerias();

		//variavel criada somente para verificar se o evento e INSS, e nao calcular o numero de avos para todo evento 
		Boolean inss = false;
		Boolean avosJaCalculados = false;
		
		EventoFolhaPagamentoVO evento;
		for(ContraChequeEventoVO contraChequeEvento : (List<ContraChequeEventoVO>) listaDeContraChequeEvento) {

			try {
				if(Uteis.isAtributoPreenchido(contraChequeEvento.getEventoFolhaPagamento().getCategoria()) && contraChequeEvento.getEventoFolhaPagamento().getCategoria().equals(CategoriaEventoFolhaEnum.INSS)) {
					inss = true;
					calculoContraCheque.calcularAvosFerias(contraCheque.getCompetenciaFolhaPagamento().getDataCompetencia(), null, inss);
					calculoContraCheque.calcularAvos13(contraCheque.getCompetenciaFolhaPagamento().getDataCompetencia(), inss);
				} else if(inss || !avosJaCalculados){
					inss = false;
					avosJaCalculados = true;
					calculoContraCheque.calcularAvosFerias(contraCheque.getCompetenciaFolhaPagamento().getDataCompetencia(), null, inss);
					calculoContraCheque.calcularAvos13(competenciaFolhaPagamentoVO.getDataCompetencia(), inss);
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}

			evento = contraChequeEvento.getEventoFolhaPagamento();

			//Se for imposto de renda nao atualizar valor temporario
			if ( (contraChequeEvento.getEventoFolhaPagamento().getNaturezaEvento().equals(NaturezaEventoFolhaPagamentoEnum.IMPOSTO_DE_RENDA) 
					|| contraChequeEvento.getEventoFolhaPagamento().getNaturezaEvento().equals(NaturezaEventoFolhaPagamentoEnum.OUTROS)) 
					&& recalculo) {
				contraChequeEvento = retornarContraChequeEventoCalculado(contraCheque, calculoContraCheque, contraChequeEvento.getEventoFolhaPagamento(), engine);
				evento.setValorTemporario(BigDecimal.ZERO);
			} else {
				evento.setValorTemporario(contraChequeEvento.recuperarValorDoEventoTratado());
			}

			evento.setReferencia(contraChequeEvento.getReferencia());
			calculoContraCheque.atualizarValoresDeCalculo(evento);
		}
	}

	@Override
	public List<Integer> consultarIDsPorFuncionarioCargoCompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		String sql = " SELECT codigo FROM contracheque WHERE funcionarioCargo = ? and competenciaFolhaPagamento = ? ";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, funcionarioCargo.getCodigo(), competenciaFolhaPagamentoVO.getCodigo()-1);
		List<Integer> lista = new ArrayList<>();
        while (rs.next()) {
        	lista.add(rs.getInt("codigo"));
        }
		return lista;
	}
	
	@Override
	public List<Integer> consultarFuncionarioCargoPorLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {
		String sql = " SELECT funcionariocargo FROM contracheque WHERE lancamentofolhapagamento = ?";		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, lancamentoFolhaPagamento.getCodigo());
		
		List<Integer> codigos = new ArrayList<>();
        while (rs.next()) {
        	codigos.add(rs.getInt("funcionariocargo"));
        }
		return codigos;
	}
	
	@Override
	public List<ContraChequeVO> consultarContraChequePorCompetenciaEFiltrosDoTemplate(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento, TemplateLancamentoFolhaPagamentoVO templateFP) {
		
		StringBuilder sql = new StringBuilder(" SELECT cc.* FROM contracheque cc ");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = cc.funcionariocargo ");
		sql.append(" WHERE cc.competenciafolhapagamento = ? ");

		if(Uteis.isAtributoPreenchido(competenciaFolhaPagamento.getCodigo())) {
			sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(templateFP));			
		}
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), competenciaFolhaPagamento.getCodigo());

		List<ContraChequeVO> lista = new ArrayList<>();
		
		while(rs.next()) {
			try {
				lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_COMBOBOX));				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return lista;
	}

	/**
	 * Consulta o total de contra cheque para o funcionario e a competencia informado.
	 */
	@Override
	public int consultarTotalContraChequePorFuncionarioECompetencia(FuncionarioCargoVO funcionarioCargoVO, CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(controlelancamentofolhapagamento.codigo) as qtde FROM controlelancamentofolhapagamento ");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = controlelancamentofolhapagamento.contracheque");
		sql.append(" WHERE controlelancamentofolhapagamento.funcionariocargo = ? AND controlelancamentofolhapagamento.competenciafolhapagamento = ? ");
		sql.append(" AND controlelancamentofolhapagamento.rescisao != true AND contracheque .totalreceber > 0");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				funcionarioCargoVO.getCodigo(), competenciaFolhaPagamento.getCodigo());

		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Regra dee negocio que valida se existe contra cheque para o funcionario cargo
	 * na data de competencia informada.
	 */
	@Override
	public void validarSeExisteContraChequeParaFuncionarioCargo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		int quantidadeContraChequeFuncionario = getFacadeFactory().getContraChequeInterfaceFacade().consultarTotalContraChequePorFuncionarioECompetencia(funcionarioCargoVO, competenciaFolhaPagamentoVO); 

		if (quantidadeContraChequeFuncionario > 0) {
			throw new Exception("Existe contracheque lançado para o funcionário," + funcionarioCargoVO.getFuncionarioVO().getPessoa().getNome());
		}
		
	}

	@Override
	public void atualizarContaPagar(Integer codigo, UsuarioVO usuario) throws Exception {
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					final StringBuilder sql = new StringBuilder(" update contracheque set contapagar = null where contapagar = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(codigo, ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_erroAoSalvar"));
		}
	}
}