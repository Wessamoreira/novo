package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.MarcacaoFeriasInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>MarcacaoFeriasVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>MarcacaoFeriasVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({"unchecked", "rawtypes"})
@Service
@Scope
@Lazy
public class MarcacaoFerias extends SuperFacade<MarcacaoFeriasVO> implements MarcacaoFeriasInterfaceFacade<MarcacaoFeriasVO> {

	private static final long serialVersionUID = 2347098686848160972L;

	protected static String idEntidade;

	public MarcacaoFerias() throws Exception {
		super();
		setIdEntidade("MarcacaoFerias");
	}

	public MarcacaoFerias(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("MarcacaoFerias");
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		MarcacaoFerias.idEntidade = idEntidade;
	}

	@Override
	public void validarDados(MarcacaoFeriasVO obj) throws ConsistirException {

		if (!Uteis.isAtributoPreenchido(obj.getPeriodoAquisitivoFeriasVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_funcionarioCargo"));
		}

		if(!Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_funcionarioNaoEncontrado"));
		}

		if(obj.getDataFinalGozo().before(obj.getDataInicioGozo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_dataInicioGozoMaioQueDataFinal"));
		}

		if(obj.getQtdDiasAbono()>10) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_qtdDiasAbonoMaiorQueDez"));
		}

		if(!obj.getDataInicioGozo().after(Uteis.obterDataFutura(obj.getDataPagamento(), 2))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_dataPagamentoDeveSerMenorDataInicialGozo"));
		}
	}

	@Override
	public void validarDadosAdiantamento(MarcacaoFeriasVO obj) throws Exception {
		validarDados(obj);

		CompetenciaFolhaPagamentoVO competenciaFolhaPagamento = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
		boolean inicioGozoUmMesAnterior = UteisData.ValidarMesEAnoDatasIguais(obj.getDataInicioGozo(), competenciaFolhaPagamento.getDataCompetencia());
		if (!inicioGozoUmMesAnterior) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_dataInicioGozoForaCompetencia"));
		}
	}

	@Override
	public void validarDadosLancarContraCheque(MarcacaoFeriasVO obj) throws Exception {
		validarDados(obj);

		CompetenciaFolhaPagamentoVO competenciaFolhaPagamento = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
		boolean inicioGozoUmMesAnterior = UteisData.ValidarMesEAnoDatasIguais(obj.getDataInicioGozo(), competenciaFolhaPagamento.getDataCompetencia());
		if (!inicioGozoUmMesAnterior) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_dataInicioGozoForaCompetencia"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirMarcacaoFerias(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		persistir(obj, validarAcesso, usuarioVO);
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().salvarControleMarcacaoFerias(obj, SituacaoMarcacaoFeriasEnum.MARCADA, false, usuarioVO);
	}

	@Override
	public void persistir(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		
		if(!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			MarcacaoFerias.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.marcacaoferias ");
					sql.append(" ( periodoAquisitivoFerias, funcionarioCargo, dataInicioGozo, dataFinalGozo, qtdDias, ")
					.append(" abono, qtdDiasAbono, pagarParcela13, dataPagamento, dataInicioAviso, ")
					.append(" situacaoMarcacao ) VALUES (")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ? ) ")
					.append(" returning codigo ")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getPeriodoAquisitivoFeriasVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioGozo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFinalGozo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdDias(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getAbono(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQtdDiasAbono(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPagarParcela13(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioAviso(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getSituacaoMarcacao(), ++i, sqlInserir);

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
			throw e;
		}
	}

	@Override
	public void alterar(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		MarcacaoFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.marcacaoferias SET ")
				.append(" periodoAquisitivoFerias=?, funcionarioCargo=?, dataInicioGozo=?, dataFinalGozo=?, qtdDias=?, ")
				.append(" abono=?, qtdDiasAbono=?, pagarParcela13=?, dataPagamento=?, dataInicioAviso=?, ") 
				.append(" situacaoMarcacao=? ")
				.append(" WHERE codigo=?")
				.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getPeriodoAquisitivoFeriasVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataInicioGozo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataFinalGozo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQtdDias(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getAbono(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQtdDiasAbono(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPagarParcela13(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataInicioAviso(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getSituacaoMarcacao(), ++i, sqlAlterar);

				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});		
	}

	@Override
	public void excluir(MarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		MarcacaoFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);

		this.cancelarRecibo(obj, validarAcesso, usuarioVO);

		StringBuilder sql = new StringBuilder("DELETE FROM marcacaoferias WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public void excluirMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder str = new StringBuilder("DELETE FROM marcacaoferias WHERE codigo = ? and situacaoMarcacao = ?");
			str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(str.toString(), marcacaoFeriasVO.getCodigo(), situacao.getValor());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MarcacaoFeriasVO consultarPorChavePrimaria(Long id) throws Exception {
        return consultarPorChavePrimaria(id.intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	@Override
	public MarcacaoFeriasVO consultarPorChavePrimaria(Integer id, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where marcacaoferias.codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{id.intValue()});
        
        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, nivelMontarDados));
        }
        return new MarcacaoFeriasVO(); 
	}

	@Override
	public MarcacaoFeriasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {

		MarcacaoFeriasVO obj = new MarcacaoFeriasVO();
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.getPeriodoAquisitivoFeriasVO().setCodigo(tabelaResultado.getInt("periodoAquisitivoFerias"));        
        obj.getFuncionarioCargoVO().setCodigo(tabelaResultado.getInt("funcionarioCargo"));
        obj.setDataInicioGozo(tabelaResultado.getDate("dataInicioGozo"));
        obj.setDataFinalGozo(tabelaResultado.getDate("dataFinalGozo"));
        
        obj.setQtdDias(tabelaResultado.getInt("qtdDias"));
        obj.setAbono(tabelaResultado.getBoolean("abono"));
        obj.setQtdDiasAbono(tabelaResultado.getInt("qtdDiasAbono"));
        obj.setPagarParcela13(tabelaResultado.getBoolean("pagarParcela13"));
        obj.setDataPagamento(tabelaResultado.getDate("dataPagamento"));
        
        obj.setDataInicioAviso(tabelaResultado.getDate("dataInicioAviso"));
        
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacaoMarcacao"))) {
			obj.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.valueOf(tabelaResultado.getString("situacaoMarcacao")));
		}

		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		obj.setPeriodoAquisitivoFeriasVO(Uteis.montarDadosVO(tabelaResultado.getInt("periodoAquisitivoFerias"), PeriodoAquisitivoFeriasVO.class, p -> getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			return obj;
		}
		
        obj.setFuncionarioCargoVO(Uteis.montarDadosVO(tabelaResultado.getInt("funcionarioCargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, nivelMontarDados, null)));

		return obj;
	}

	/**
	 * Montar dados marcação de férias pela marcação de ferias coletiva informada.
	 */
	@Override
	public void montarDadosPorMarcacaoFeriasColetiva(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, FuncionarioCargoVO funcionarioCargoVO, MarcacaoFeriasVO marcacaoFerias) {
		marcacaoFerias.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.MARCADA);
		marcacaoFerias.setFuncionarioCargoVO(funcionarioCargoVO);
		marcacaoFerias.setDataInicioGozo(marcacaoFeriasColetivasVO.getDataInicioGozo());
		marcacaoFerias.setDataFinalGozo(marcacaoFeriasColetivasVO.getDataFinalGozo());
		marcacaoFerias.setQtdDiasAbono(marcacaoFeriasColetivasVO.getQuantidadeDiasAbono());
		marcacaoFerias.setDataPagamento(marcacaoFeriasColetivasVO.getDataPagamento());
		marcacaoFerias.setQtdDias(marcacaoFeriasColetivasVO.getQuantidadeDias());
		marcacaoFerias.setAbono(marcacaoFeriasColetivasVO.getAbono());
		marcacaoFerias.setPagarParcela13(marcacaoFeriasColetivasVO.getPagarPrimeiraParcela13());
		marcacaoFerias.setDataInicioAviso(marcacaoFeriasColetivasVO.getDataInicioAviso());
	}

	/**
	 * Consulta da tela de marcacaoFeriasColetivasCons.xhtml
	 */
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario, String situacaoMarcacao) throws Exception {
		dataModelo.setListaConsulta(consultarMarcacaoDeFeriasPorFuncionarioCargoAtivoRH(dataModelo, situacaoFuncionario, situacaoMarcacao));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalMarcacaoDeFeriasPorFuncionarioCargoAtivoRH(dataModelo, situacaoFuncionario, situacaoMarcacao));
	}

	/**
	 * Consulta o total de marcação de ferias pelo fitro informado para a paginação da tela de consulta.
	 * 
	 * @param dataModelo
	 * @param situacaoFuncionario
	 * @param situacaoMarcacao
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalMarcacaoDeFeriasPorFuncionarioCargoAtivoRH(DataModelo dataModelo, String situacaoFuncionario, String situacaoMarcacao) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder(getSQLBasicoTotalizador());
			sqlStr.append(getFacadeFactory().getFuncionarioCargoFacade().filtroFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, Boolean.FALSE));
			sqlStr.append(filtroPorSituacao(situacaoMarcacao));
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);	
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	/**
	 * Consulta as marcações de ferias pelo fitro informado para a tela de consulta.
	 * 
	 * @param dataModelo
	 * @param situacaoFuncionario
	 * @param situacaoMarcacao
	 * @return
	 * @throws Exception
	 */
	public List<MarcacaoFeriasVO> consultarMarcacaoDeFeriasPorFuncionarioCargoAtivoRH(DataModelo dataModelo, String situacaoFuncionario, String situacaoMarcacao) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(getFacadeFactory().getFuncionarioCargoFacade().filtroFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, Boolean.FALSE));
		sql.append(filtroPorSituacao(situacaoMarcacao));
		sql.append(" order by marcacaoferias.dataInicioGozo, funcionariocargo.matriculaCargo ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados());
    }

	private String filtroPorSituacao(String situacaoMarcacao) {
		if (!situacaoMarcacao.equals("TODOS")) {
			return " and situacaoMarcacao = '".concat(situacaoMarcacao).concat("'");
		}

		return "";
	}

	private String getSQLBasico() {
		return getSQLBasico(false);
	}
	
	private String getSQLBasico(boolean aliasFuncionarioCargo) {
		StringBuilder sql = new StringBuilder(" select * from marcacaoferias ");
		sql.append(getSQLGenerico(aliasFuncionarioCargo));	
		return sql.toString();
	}
	
	private String getSQLBasicoTotalizador() {
		
		StringBuilder sql = new StringBuilder(" select count(marcacaoferias.codigo) AS qtde from marcacaoferias ");
		sql.append(getSQLGenerico(false));
		return sql.toString();
	}
	
	private String getSQLGenerico(boolean aliasFuncionarioCargo) {
		StringBuilder sql = new StringBuilder("");
		if(aliasFuncionarioCargo) {
			sql.append(" inner join funcionariocargo fc on fc.codigo = marcacaoferias.funcionariocargo ");
			sql.append(" inner join funcionario on funcionario.codigo = fc.funcionario ");	
		} else {
			sql.append(" inner join funcionariocargo on funcionariocargo.codigo = marcacaoferias.funcionariocargo ");
			sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
		}
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sql.append(" inner join periodoaquisitivoferias on periodoaquisitivoferias.codigo = marcacaoferias.periodoaquisitivoferias ");
		
		return sql.toString();
	}

    public List<MarcacaoFeriasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
    	
    	List<MarcacaoFeriasVO> vetResultado = new ArrayList<>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    
    /**
     * Consulta se ja existe uma marcacao aberta para o funcionario
     * caso nao exista cria uma marcacao com o seu primeiro periodo aquisitivo aberto
     */
	@Override
	public MarcacaoFeriasVO consultarMarcacaoDiferenteDeFechadaPorFuncionario(String matriculaCargo) throws Exception {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where funcionariocargo.matriculacargo like ? ");
		sql.append(" and marcacaoferias.situacaoMarcacao <> ? ");
		
		List<String> filtros = new ArrayList<>();
		filtros.add(matriculaCargo);
		filtros.add(SituacaoMarcacaoFeriasEnum.FECHADA.getValor());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
        try {
        	if(tabelaResultado.next()) {
        		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);	
        	}
        } catch (Exception e) {
        	e.printStackTrace();
		}
        
        return new MarcacaoFeriasVO();
	}

	/**
	 * Cria MarcacaoFeriasVO com o primeiro periodo aquisitivo aberto
	 * 
	 * @param matriculaCargo
	 * @return
	 */
	@Override
	public MarcacaoFeriasVO inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoAbertoDoFuncionario(String matriculaCargo) {
		PeriodoAquisitivoFeriasVO periodoAquisitivo = getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(matriculaCargo);
		MarcacaoFeriasVO marcacao = new MarcacaoFeriasVO();
		marcacao.setPeriodoAquisitivoFeriasVO(periodoAquisitivo);
		return marcacao;
	}
	
	/**
	 * Cria MarcacaoFeriasVO com o primeiro periodo aquisitivo vencido
	 * 
	 * @param matriculaCargo
	 * @return
	 */
	@Override
	public MarcacaoFeriasVO inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoVencidoDoFuncionario(String matriculaCargo) {
		PeriodoAquisitivoFeriasVO periodoAquisitivo = getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPrimeiroPeriodoAquisitivoVencidoPorFuncionarioCargo(matriculaCargo);
		MarcacaoFeriasVO marcacao = new MarcacaoFeriasVO();
		marcacao.setPeriodoAquisitivoFeriasVO(periodoAquisitivo);
		return marcacao;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calcularRecibo(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		calcularRecibo(null, marcacaoFeriasVO, recibo, validarAcesso, usuarioVO);
	}
	
	@Override
	public void calcularRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(marcacaoFeriasVO);

		marcacaoFeriasVO.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.CALCULADA);
		persistir(marcacaoFeriasVO, true, usuarioVO);	
		
		getFacadeFactory().getReciboFeriasInterfaceFacade().calcularRecibo(marcacaoFeriasVO, recibo);
		
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().salvarControleMarcacaoFerias(marcacaoFeriasColetivasVO, marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum.CALCULADA, false, usuarioVO);
	}

	@Override
	public void cancelarRecibo(MarcacaoFeriasVO marcacaoFeriasVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		try {
			
			StringBuilder sql = new StringBuilder("UPDATE marcacaoferias SET situacaoMarcacao = ? WHERE codigo = ?");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, SituacaoMarcacaoFeriasEnum.MARCADA.getValor());
					sqlAlterar.setInt(2, marcacaoFeriasVO.getCodigo());
					return sqlAlterar;
				}
			});
			
			getFacadeFactory().getReciboFeriasInterfaceFacade().cancelarRecibo(marcacaoFeriasVO, false, usuarioLogado);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * Centralizado o lancamento do adiantamento da folha de ferias na tela de Lancamento Folha de Pagamento
	 * @param marcacaoFeriasVO
	 * @param reciboFeriasVO
	 * @param lancamentoFolhaPagamentoVO
	 * @throws Exception
	 */
	@Deprecated
	public void lancarAdiantamendoNaFolha(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception {
		alterarSituacaoMarcacao(marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum.CALCULADA, true, false);
		
		ContraChequeVO contraChequeVO = montarContraChequePelaMarcacaoDeFerias(marcacaoFeriasVO, reciboFeriasVO, lancamentoFolhaPagamentoVO);
		
		contraChequeVO.setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(contraChequeVO.getCodigo(), false, null));
		
		CalculoContraCheque calculoContraCheque = montarCalculoDoContraChequeDaMarcacao(marcacaoFeriasVO);
		
		//Ajustar a chamada do metodo
		List<EventoFolhaPagamentoVO> listaDeEventos = new ArrayList<>();
		listaDeEventos.add(consultaEventoDeAdiantamentoDoContraCheque(marcacaoFeriasVO.getFuncionarioCargoVO(), reciboFeriasVO));
		
		if(contraChequeVO.montarDadosEventosPreenchidosDoContraChequeEvento().contains(listaDeEventos.get(0))) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}
		
		listaDeEventos.addAll(contraChequeVO.montarDadosEventosPreenchidosDoContraChequeEvento());

		ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();
		getFacadeFactory().getContraChequeInterfaceFacade().realizarCalculoContraChequeDoFuncionario(contraChequeVO, listaDeEventos, calculoContraCheque, null, engine);
	}

	/**
	 * Atualiza a situacao da Marcacao de Ferias
	 * 
	 * @param marcacaoFeriasVO
	 * @param situacao
	 * @param lancadoAdiantamento
	 * @param lancadoDescontoAdiantamento
	 */
	@Override
	public void alterarSituacaoMarcacao(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, Boolean lancadoAdiantamento, Boolean lancadoDescontoAdiantamento) {
		try {
			
			StringBuilder sql = new StringBuilder("UPDATE marcacaoferias SET ");
			sql.append(" situacaoMarcacao = ? ");
			
			if(lancadoAdiantamento != null)
				sql.append(" , lancadoAdiantamento = ? ");
			
			if(lancadoDescontoAdiantamento != null)
				sql.append(" , lancadoDescontoAdiantamento = ? ");
			
			sql.append(" WHERE codigo = ?");
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					
					Uteis.setValuePreparedStatement(situacao.getValor(), ++i, sqlAlterar);
					
					if(lancadoAdiantamento != null)
						Uteis.setValuePreparedStatement(lancadoAdiantamento, ++i, sqlAlterar);
					
					if(lancadoDescontoAdiantamento != null)
						Uteis.setValuePreparedStatement(lancadoDescontoAdiantamento, ++i, sqlAlterar);
					
					
					//codigo
					Uteis.setValuePreparedStatement(marcacaoFeriasVO.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void alterarSituacaoFerias(MarcacaoFeriasVO marcacaoFerias, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("UPDATE marcacaoferias SET situacaoMarcacao = ? WHERE codigo = ? ");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;

					Uteis.setValuePreparedStatement(marcacaoFerias.getSituacaoMarcacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(marcacaoFerias.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Altera a situação de lançado adiantamento para 'Não' pelo
	 * funcionario cargo informado.
	 * 
	 * @param codigoFuncionario - Código do {@link FuncionarioCargoVO }
	 */
	@Override
	public void alterarLancadoAdiantamentoPorFuncionarioCargo(Integer codigoFuncionario, boolean lancadoAdiantamento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("update marcacaoferias set lancadoadiantamento = ? where funcionariocargo = ? AND situacaomarcacao = 'CALCULADA' ");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;

					Uteis.setValuePreparedStatement(lancadoAdiantamento, ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(codigoFuncionario, ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	private ContraChequeVO montarContraChequePelaMarcacaoDeFerias(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) {
		ContraChequeVO contraChequeVO;
		try {
			contraChequeVO = getFacadeFactory().getContraChequeInterfaceFacade().consultarPorFuncionarioCargoECompetencia(marcacaoFeriasVO.getFuncionarioCargoVO(), lancamentoFolhaPagamentoVO.getCompetenciaFolhaPagamentoVO());
		} catch (Exception e) {
			e.printStackTrace();
			contraChequeVO = new ContraChequeVO();
		}
		
		//contraChequeVO.setLancamentoFolhaPagamento(lancamentoFolhaPagamentoVO);
		contraChequeVO.setCompetenciaFolhaPagamento(lancamentoFolhaPagamentoVO.getCompetenciaFolhaPagamentoVO());
		contraChequeVO.setPeriodo(lancamentoFolhaPagamentoVO.getPeriodo());
		contraChequeVO.setFuncionarioCargo(marcacaoFeriasVO.getFuncionarioCargoVO());
		
		return contraChequeVO;
	}

	@Override
	public EventoFolhaPagamentoVO consultaEventoDeAdiantamentoDoContraCheque(FuncionarioCargoVO funcionarioCargoVO, ReciboFeriasVO reciboFeriasVO) throws Exception {
		
		SindicatoVO sindicato = getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(funcionarioCargoVO.getSindicatoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS);

		if(!Uteis.isAtributoPreenchido(sindicato.getEventoAdiantamentoFerias())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_eventoDeAdiantamentoNaoCadastrado"));
		}
		
		EventoFolhaPagamentoVO evento = sindicato.getEventoAdiantamentoFerias();
		evento.setValorTemporario(reciboFeriasVO.getTotalReceber());
		evento.setValorInformado(Boolean.TRUE);
		
		return evento;
		
	}
	
	@Override
	public EventoFolhaPagamentoVO consultaEventoDeDescontoDoAdiantamentoDoContraCheque(FuncionarioCargoVO funcionarioCargoVO, ReciboFeriasVO reciboFeriasVO) throws Exception {
		
		SindicatoVO sindicato = getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(funcionarioCargoVO.getSindicatoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS);

		if(!Uteis.isAtributoPreenchido(sindicato.getEventoDescontoAdiantamentoFerias())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_eventoDeDescontoDeAdiantamentoNaoCadastrado"));
		}
		
		EventoFolhaPagamentoVO evento = sindicato.getEventoDescontoAdiantamentoFerias();
		evento.setValorTemporario(reciboFeriasVO.getTotalReceber());
		evento.setValorInformado(Boolean.TRUE);
		
		return evento;
		
	}
	
	private CalculoContraCheque montarCalculoDoContraChequeDaMarcacao(MarcacaoFeriasVO marcacaoFeriasVO) throws Exception {
		
		CalculoContraCheque calculoContraCheque = new CalculoContraCheque(marcacaoFeriasVO.getFuncionarioCargoVO());
		
		calculoContraCheque.setNumeroDiasAbono(marcacaoFeriasVO.getQtdDiasAbono());
		calculoContraCheque.setNumeroDiasFerias(marcacaoFeriasVO.getQtdDias());
		
		return calculoContraCheque;
	}

	/**
	 * Centralizado o lancamento do recibo de feiras para a tela de Lancamento Folha de Pagamento
	 * 
	 * @param marcacaoFeriasVO
	 * @param reciboFeriasVO
	 * @param lancamentoFolhaPagamentoVO
	 * @throws Exception
	 */
	@Deprecated
	public void lancarEventosDoReciboNoContraCheque(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO reciboFeriasVO, LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO) throws Exception {
		
		alterarSituacaoMarcacao(marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum.CALCULADA, true, false);
		
		ContraChequeVO contraChequeVO = montarContraChequePelaMarcacaoDeFerias(marcacaoFeriasVO, reciboFeriasVO, lancamentoFolhaPagamentoVO);
		
		List<EventoFolhaPagamentoVO> listaDeEventos = (List<EventoFolhaPagamentoVO>) reciboFeriasVO.getEventosPreenchidosDoReciboDeFeriasSetadosComInformacaoManual(true);
		CalculoContraCheque calculoContraCheque = montarCalculoDoContraChequeDaMarcacao(marcacaoFeriasVO);
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("rhino");
		getFacadeFactory().getContraChequeInterfaceFacade().realizarCalculoContraChequeDoFuncionario(contraChequeVO, listaDeEventos, calculoContraCheque, null, engine);
		
	}

	@Override
	public MarcacaoFeriasVO consultarMarcacaoPorFuncionarioESituacao(String matriculaCargo, SituacaoMarcacaoFeriasEnum situacao)  {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where matriculacargo like ? ");
		sql.append(" and marcacaoferias.situacaoMarcacao = ? ");
		
		List<String> filtros = new ArrayList<>();
		filtros.add(matriculaCargo);
		filtros.add(situacao.getValor());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
        try {
        	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);        	
        } catch (Exception e) {
        	return new MarcacaoFeriasVO();
		}
	}

	
	@Override
	public List<Integer> consultarFuncionariosMarcacaoFerias() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT funcionariocargo FROM marcacaoferias marcacao");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = marcacao.funcionariocargo");
		sql.append(" WHERE datainiciogozo >= ? AND fc.situacaofuncionario like 'ATIVO'");
		sql.append(" AND marcacao.situacaomarcacao = 'CALCULADA'");

		List<Integer> funcionariosCargos = new ArrayList<>();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Date());
		
        while (tabelaResultado.next()) {
        	funcionariosCargos.add(tabelaResultado.getInt("funcionariocargo"));
        }
        return funcionariosCargos;
	}

	@Override
	public List<Integer> consultarFuncionariosMarcacaoRetornoFerias(Date dataCompetencia) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT funcionariocargo FROM marcacaoferias marcacao");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = marcacao.funcionariocargo");
		sql.append(" WHERE datafinalgozo < ? AND fc.situacaofuncionario like 'FERIAS'");

		List<Integer> funcionariosCargos = new ArrayList<>();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataCompetencia);
		
        while (tabelaResultado.next()) {
        	funcionariosCargos.add(tabelaResultado.getInt("funcionariocargo"));
        }
        return funcionariosCargos;	
	}

	@Override
	public MarcacaoFeriasVO consultarMarcacaoPorFuncionarioSituacaoEDataFerias(String matriculaCargo, SituacaoMarcacaoFeriasEnum situacao, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where fc.matriculacargo like ? ");
		sql.append(" and marcacaoferias.situacaoMarcacao = ? ");
		sql.append(" and ( marcacaoferias.datainiciogozo between ? and ? ");
		sql.append(" or ( marcacaoferias.datafinalgozo between ? and ? ");
		
		List<Object> filtros = new ArrayList<>();
		filtros.add(matriculaCargo);
		filtros.add(situacao.getValor());
		filtros.add(UteisData.getPrimeiroDataMes(competenciaFolhaPagamentoVO.getDataCompetencia()));
		filtros.add(UteisData.getUltimaDataMes(competenciaFolhaPagamentoVO.getDataCompetencia()));
		filtros.add(UteisData.getPrimeiroDataMes(competenciaFolhaPagamentoVO.getDataCompetencia()));
		filtros.add(UteisData.getUltimaDataMes(competenciaFolhaPagamentoVO.getDataCompetencia()));
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
        try {
        	if(tabelaResultado.next()) {
        		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);        		
        	}
        } catch (Exception e) {
        	e.printStackTrace();
		}
        
        return new MarcacaoFeriasVO();
	}

	
	/**
	 * Consulta a lista de MarcacaoFeriasVO dos funcionarios selecionados pelos 
	 */
	@Override
	public List<MarcacaoFeriasVO> consultarListaDeMarcacaoDeFeriasPorFiltroDosFuncionarioESituacao(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, 
			SituacaoMarcacaoFeriasEnum situacao) {
		
		StringBuilder sql = new StringBuilder(getSQLBasico(true));
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(templateLancamentoFolhaPagamentoVO));
		sql.append(filtroPorSituacao(situacao.getValor()));
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<MarcacaoFeriasVO> listaMarcacao = new ArrayList<>();
		
		while (tabelaResultado.next()) {
            try {
				listaMarcacao.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
		return listaMarcacao;
	}

	/**
	 * Finaliza as ferias e salva o historico no controle
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void finalizarMarcacaoDeFerias(MarcacaoFeriasVO marcacaoFeriasVO, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		finalizarMarcacaoDeFerias(null, marcacaoFeriasVO, encerrarPeriodoAquisitivo, dataFechamento, validarAcesso, usuarioVO);
	}
	
	/**
	 * Finaliza as ferias e salva o historico no controle
	 */
	@Override
	public void finalizarMarcacaoDeFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		ControleMarcacaoFeriasVO controle = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFerias(marcacaoFeriasVO);
		
		if(marcacaoFeriasVO.getSituacaoMarcacao().equals(SituacaoMarcacaoFeriasEnum.CALCULADA) && controle.getLancadoReciboNoContraCheque()) {
			finalizarFerias(marcacaoFeriasVO, encerrarPeriodoAquisitivo, dataFechamento, validarAcesso, usuarioVO);
			getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().salvarControleMarcacaoFerias(controle, marcacaoFeriasColetivasVO, marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum.FECHADA, validarAcesso, usuarioVO);
		} else if(!controle.getLancadoReciboNoContraCheque()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_FechadaSemLancaRecibo"));
		}
	}
	
	private void finalizarFerias(MarcacaoFeriasVO marcacaoFeriasVO, boolean encerrarPeriodoAquisitivo, Date dataFechamento, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		//Salva as ferias como Fechada
		marcacaoFeriasVO.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.FECHADA);
		alterarSituacaoFerias(marcacaoFeriasVO, validarAcesso, usuarioVO);
		
		//Encerra o periodo aquisitivo
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().encerrarPeriodoAquisitivo(marcacaoFeriasVO.getPeriodoAquisitivoFeriasVO(), encerrarPeriodoAquisitivo, dataFechamento, validarAcesso, usuarioVO);
		
		//Retorna o funcionario para ativo
		getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(marcacaoFeriasVO.getFuncionarioCargoVO().getCodigo(), SituacaoFuncionarioEnum.ATIVO.getValor(), usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processaSituacaoFuncionarioFerias() throws Exception {
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		UsuarioVO usuarioVO = new UsuarioVO();

		if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo())) {
			usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
		}

		CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(false);
		if(competenciaFolhaPagamentoVO != null) {
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().validarFeriasAtivas(competenciaFolhaPagamentoVO, usuarioVO);
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().validarFuncionarioQueRetornouDeFerias(competenciaFolhaPagamentoVO, usuarioVO);
		}
	}

	@Override
	public MarcacaoFeriasVO consultarMarcacaoPorPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM marcacaoferias WHERE periodoaquisitivoferias = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), periodoAquisitivoFeriasVO.getCodigo());

        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX));
        }
        return new MarcacaoFeriasVO(); 
	}
}