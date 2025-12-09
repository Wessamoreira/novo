package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.script.ScriptEngine;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.ReciboFeriasEventoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.ReciboFeriasInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>ReciboFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ReciboFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({"unchecked", "rawtypes"})
@Service
@Scope
@Lazy
public class ReciboFerias extends SuperFacade<ReciboFeriasVO> implements ReciboFeriasInterfaceFacade<ReciboFeriasVO> {

	private static final long serialVersionUID = -3619664940488584257L;
	
	protected static String idEntidade;

	public ReciboFerias() throws Exception {
		super();
		setIdEntidade("ReciboFerias");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ReciboFerias.idEntidade = idEntidade;
	}

	@Override
	public void validarDados(ReciboFeriasVO t) throws ConsistirException {
		
	}

	@Override
	public void incluir(ReciboFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ReciboFerias.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					final StringBuilder sql = new StringBuilder(" INSERT INTO reciboferias ")
							.append(" ( marcacaoFerias, totalProvento, totalDesconto, totalReceber, tipoRecebimento, ")
							.append(" salario, previdencia, optanteTotal )")
							.append(" VALUES (?, ?, ?, ?, ?, ")
							.append(" ?, ?, ? )")
							.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getMarcacaoFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalProvento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalReceber(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoRecebimento(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOptanteTotal(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

					public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
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
	public void alterar(ReciboFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			ReciboFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				
				final StringBuilder sql = new StringBuilder(" UPDATE reciboferias SET ")
						.append(" marcacaoFerias=?, totalProvento=?, totalDesconto=?, totalReceber=?, tipoRecebimento=?, ")
						.append(" salario=?, previdencia=?, optanteTotal=? ")
						.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

				int i = 0;
				
				Uteis.setValuePreparedStatement(obj.getMarcacaoFerias(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTotalProvento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTotalDesconto(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTotalReceber(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoRecebimento(), ++i, sqlAlterar);

				Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPrevidencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getOptanteTotal(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				
				return sqlAlterar;
			}
		});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(ReciboFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ReciboFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			String sql = "DELETE FROM reciboferias WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ReciboFeriasVO consultarPorChavePrimaria(Long id) throws Exception {
		String sql = " SELECT * FROM reciboferias WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id.intValue());
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception("Dados não encontrados.");
	}
	
	@Override
	public void persistir(ReciboFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
	
		//validarDados(obj);
	
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	
		persistirReciboEvento(obj, usuarioVO);
	}
	
	private void persistirReciboEvento(ReciboFeriasVO obj, UsuarioVO usuario)  {
		try {
			getFacadeFactory().getReciboFeriasEventoInterfaceFacade().persistirTodos(obj, false, usuario);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ReciboFeriasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		ReciboFeriasVO obj = new ReciboFeriasVO();
		
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setTotalProvento(tabelaResultado.getBigDecimal("totalProvento"));
        obj.setTotalDesconto(tabelaResultado.getBigDecimal("totalDesconto"));
        obj.setTotalReceber(tabelaResultado.getBigDecimal("totalReceber"));
        
        if(tabelaResultado.getString("tipoRecebimento") != null) {
        	obj.setTipoRecebimento(TipoRecebimentoEnum.valueOf(tabelaResultado.getString("tipoRecebimento")));        	
        }
        
        obj.setSalario(tabelaResultado.getBigDecimal("salario"));
       	obj.setOptanteTotal(tabelaResultado.getBoolean("optanteTotal"));
       	
       	if(tabelaResultado.getString("previdencia") != null) {
       		obj.setPrevidencia(PrevidenciaEnum.valueOf(tabelaResultado.getString("previdencia")));
       	}
    	
       	if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
       		return obj;
       	}

       	obj.setMarcacaoFerias(Uteis.montarDadosVO(tabelaResultado.getInt("marcacaoFerias"), MarcacaoFeriasVO.class, p -> getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
       	
       	if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
       		obj.setListaReciboEvento(getFacadeFactory().getReciboFeriasEventoInterfaceFacade().consultarPorReciboFerias(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));	
       	}
       	
		return obj;
	}

	@Override
	public ReciboFeriasVO consultarPorMarcacao(MarcacaoFeriasVO marcacao, int nivelmontardadosTodos, UsuarioVO usuario, boolean controlarAcesso) {

		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	        
			StringBuilder sql = new StringBuilder(getSQLBasico());
			sql.append(" where reciboferias.marcacaoFerias = ? ");
	        
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  marcacao.getCodigo());
	        
	        if(tabelaResultado.next()) {
	        	return montarDados(tabelaResultado, nivelmontardadosTodos);
	        } 
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ReciboFeriasVO();
	}

	private String getSQLBasico() {		
		return new StringBuilder(" select * from reciboferias ").toString();
	}

	/**
	 * Calculo o {@link ReciboFeriasVO} pelos {@link EventoFolhaPagamentoVO} agrupado por ferias
	 * @param marcacaoFeriasVO
	 * @param recibo
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void calcularRecibo(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo) throws Exception{

		List<EventoFolhaPagamentoVO> listaDeEventos = adicionarEventosParaCalculoDoRecibo(marcacaoFeriasVO, recibo);

		CalculoContraCheque calculoContraCheque = inicializarCalculoContraCheque(marcacaoFeriasVO);

		ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();

		listaDeEventos.sort(Comparator.comparing(EventoFolhaPagamentoVO::getPrioridade).thenComparing(EventoFolhaPagamentoVO::getOrdemCalculo));
		
		montarDadosReciboFeriasCalculado(marcacaoFeriasVO, recibo, listaDeEventos, calculoContraCheque, engine);

		try {
			this.persistir(recibo, false, null);
			this.recalcularReciboFerias(marcacaoFeriasVO, recibo, engine);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recalcula os eventos pois no momento do calculo as eventos nao estão populados.
	 *  
	 * @param listaDeEventos
	 * @param calculoContraCheque
	 * @param listaDosEventosDoRecibo
	 * @param engine
	 * @param marcacaoFeriasVO
	 * @param recibo
	 * @throws Exception
	 */
	public void recalcularReciboFerias(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo, ScriptEngine engine) throws Exception {
		List<EventoFolhaPagamentoVO> listaDeEventos = adicionarEventosParaCalculoDoRecibo(marcacaoFeriasVO, recibo);

		listaDeEventos.sort(Comparator.comparing(EventoFolhaPagamentoVO::getPrioridade).thenComparing(EventoFolhaPagamentoVO::getOrdemCalculo));

		CalculoContraCheque calculoContraCheque = inicializarCalculoContraCheque(marcacaoFeriasVO);

		recibo.setListaReciboEvento(new ArrayList<>());
		montarDadosReciboFeriasCalculado(marcacaoFeriasVO, recibo, listaDeEventos, calculoContraCheque, engine);

		this.persistir(recibo, false, null);
	}

	/**
	 * Inicializa o {@link CalculoContraCheque} para calculo é recalculo do recibo.
	 * 
	 * @param marcacaoFeriasVO
	 * @return
	 * @throws Exception
	 */
	private CalculoContraCheque inicializarCalculoContraCheque(MarcacaoFeriasVO marcacaoFeriasVO) throws Exception {
		CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
		CalculoContraCheque calculoContraCheque = CalculoContraCheque.inicializarCalculoContraCheque(marcacaoFeriasVO.getFuncionarioCargoVO(), competenciaFolhaPagamentoVO, new LancamentoFolhaPagamentoVO());

		calculoContraCheque.setNumeroDiasFerias(marcacaoFeriasVO.getQtdDias());
		calculoContraCheque.setNumeroDiasAbono(marcacaoFeriasVO.getQtdDiasAbono());

		return calculoContraCheque;
	}

	/**
	 * Monta os dados do {@link ReciboFeriasVO} e {@link ReciboFeriasEventoVO}.
	 * 
	 * @param marcacaoFeriasVO
	 * @param recibo
	 * @param listaEventosFolhaPagamento
	 * @param calculoContraCheque
	 * @param listaDosEventosDoRecibo
	 * @param engine
	 */
	private void montarDadosReciboFeriasCalculado(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo, List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento, 
			CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		List<ReciboFeriasEventoVO> listaDosEventosDoRecibo = new ArrayList<>();

		for(EventoFolhaPagamentoVO eventoFolhaPagamento : listaEventosFolhaPagamento) {
			ReciboFeriasEventoVO reciboFeriasEvento = new ReciboFeriasEventoVO();

			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().calcularEventoFolhaPagamento(eventoFolhaPagamento, marcacaoFeriasVO.getFuncionarioCargoVO(), calculoContraCheque, engine);

			if(eventoFolhaPagamento.getValorTemporario().compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			reciboFeriasEvento.montarDadosInformacoesEventoNoReciboEvento(eventoFolhaPagamento);
			calculoContraCheque.atualizarValoresDeCalculo(eventoFolhaPagamento);
			listaDosEventosDoRecibo.add(reciboFeriasEvento);
		}

		recibo.setMarcacaoFerias(marcacaoFeriasVO);
		recibo.atualizarValoresDoCalculoNoRecibo(calculoContraCheque);
		recibo.setListaReciboEvento(listaDosEventosDoRecibo);
	}

	/**
	 * Adiciona os eventos para calculo do {@link ReciboFeriasVO} ordenando por prioridade do {@link EventoFolhaPagamentoVO}
	 * 
	 * @param marcacaoFeriasVO
	 * @param recibo
	 * @return
	 */
	private List<EventoFolhaPagamentoVO> adicionarEventosParaCalculoDoRecibo(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo) {
		List<EventoFolhaPagamentoVO> listaDeEventos = new ArrayList<>();

		recibo.getListaReciboEvento().stream().forEach(p -> {
			if (p.getInformadoManual()) {
				p.getEvento().setValorTemporario(p.getValorReferencia());
				p.getEvento().setValorInformado(true);
				listaDeEventos.add(p.getEvento());
			}
		});

		adicionarEventosDeMediaParaCalculoDasFerias(marcacaoFeriasVO, listaDeEventos);
		adicionarEventosDeFeriasNoReciboEvento(listaDeEventos);

		montarEventosDaFolhaNormal(listaDeEventos, marcacaoFeriasVO.getFuncionarioCargoVO());

		return listaDeEventos;
	}

	private void montarEventosDaFolhaNormal(List<EventoFolhaPagamentoVO> listaDeEventos, FuncionarioCargoVO funcionarioCargoVO) {
		
		ContraChequeVO contracheque = new ContraChequeVO();
		
		//EVENTOS DE PENSAO
		getFacadeFactory().getContraChequeInterfaceFacade().adicionarEventosDePensaoDoFuncionario(listaDeEventos, contracheque, funcionarioCargoVO, null);

		//EVENTOS FIXOS DOS FUNCIONARIOS
		//getFacadeFactory().getContraChequeInterfaceFacade().adicionarEventosFixoDoFuncionario(listaDeEventos, contracheque, funcionarioCargoVO, true);
		
		//EVENTOS DE EMPRESTIMOS
		getFacadeFactory().getContraChequeInterfaceFacade().adicionarEventosDeEmprestimo(listaDeEventos, contracheque, funcionarioCargoVO, true);

		//EVENTOS VINCULADOS
		getFacadeFactory().getContraChequeInterfaceFacade().adicionarEventosVinculados(listaDeEventos, contracheque, funcionarioCargoVO);
		
	}

	/**
	 * Adicionar os eventos que estao no agrupamento de ferias
	 * 
	 * @param listaDeEventos
	 */
	private void adicionarEventosDeFeriasNoReciboEvento(List<EventoFolhaPagamentoVO> listaDeEventos) {
		
		List<EventoFolhaPagamentoVO> eventosDeFerias = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarEventosDeFerias();

		if(eventosDeFerias == null || eventosDeFerias.isEmpty()) {
			return;
		}

		for(EventoFolhaPagamentoVO evento : eventosDeFerias) {
			if(!listaDeEventos.contains(evento)) {
				listaDeEventos.add(evento);
			}
		}
	}
	
	/**
	 * Adiciona na lista os eventos marcados como media para as ferias, durante o periodo aquisitivo do funcinoario
	 * 
	 * @param marcacao
	 * @throws Exception 
	 */
	private void adicionarEventosDeMediaParaCalculoDasFerias(MarcacaoFeriasVO marcacao, List<EventoFolhaPagamentoVO> listaDeEventos) {
		getFacadeFactory().getSindicatoMediaFeriasInterfaceFacade().consultarEventosDeMediaDeFerias(marcacao, listaDeEventos);
	}

	
	/**
	 * Zera os valores do recibo e excluir todos seus eventos
	 * 
	 */
	@Override
	public void cancelarRecibo(MarcacaoFeriasVO marcacaoFeriasVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception{
		try {
			ReciboFerias.excluir(getIdEntidade(), validarAcesso, usuarioLogado);
		
			ReciboFeriasVO recibo = consultarPorMarcacao(marcacaoFeriasVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado, false);
			
			getFacadeFactory().getReciboFeriasEventoInterfaceFacade().excluirEventosDoRecibo(recibo, false, usuarioLogado);

			final StringBuilder sql = new StringBuilder(" delete from reciboferias WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));

			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { recibo.getCodigo() });
			
		} catch (Exception e) {
			throw e;
		}
	}
	
}