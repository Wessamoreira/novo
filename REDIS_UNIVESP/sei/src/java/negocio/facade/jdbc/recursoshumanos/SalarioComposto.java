package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.SalarioCompostoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.SalarioCompostoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>SalarioCompostoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>SalarioCompostoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class SalarioComposto extends SuperFacade<SalarioCompostoVO> implements SalarioCompostoInterfaceFacade<SalarioCompostoVO> {

	private static final long serialVersionUID = 4011234606222123574L;

	protected static String idEntidade;

	public SalarioComposto() throws Exception {
		super();
		setIdEntidade("SalarioComposto");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(List<SalarioCompostoVO> listaSalarioComposto, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		BigDecimal jornada = listaSalarioComposto.stream().map(p -> BigDecimal.valueOf(p.getJornada())).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (!BigDecimal.valueOf(funcionarioCargo.getJornada()).equals(jornada)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SalarioComposto_jornadaTrabalhoDiferentes")
					.replace("{0}", jornada.toString()).replace("{1}", funcionarioCargo.getJornada().toString()));
		}

		excluirPorFuncionarioCargo(funcionarioCargo, false, usuarioVO);

		for (SalarioCompostoVO salarioCompostoVO : listaSalarioComposto) {
			salarioCompostoVO.setCodigo(0);
			salarioCompostoVO.setFuncionarioCargo(funcionarioCargo);
			persistir(salarioCompostoVO, validarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(List<SalarioCompostoVO> listaSalarioComposto, HistoricoSalarialVO historicoSalarialVO,
			FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		this.persistirTodos(listaSalarioComposto, funcionarioCargoVO, validarAcesso, usuarioVO);
		
		//Gera o Historico Salarial.
		getFacadeFactory().getHistoricoSalarialInterfaceFacade().persistir(historicoSalarialVO, Boolean.FALSE, usuarioVO);
	}

	@Override
	public void persistir(SalarioCompostoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(SalarioCompostoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SalarioComposto.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.salariocomposto(funcionariocargo, eventofolhapagamento, jornada, valorhora, valormensal) VALUES (?, ?, ?, ?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getJornada(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorHora(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorMensal(), ++i, sqlInserir);

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
	public void alterar(SalarioCompostoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SalarioComposto.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE public.salariocomposto SET funcionariocargo=?, eventofolhapagamento=?, jornada=?, valorhora=?, valormensal=? WHERE codigo=?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getJornada(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValorHora(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValorMensal(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(SalarioCompostoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SalarioComposto.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM salariocomposto WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM salariocomposto WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public SalarioCompostoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE salariocomposto.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT codigo, funcionariocargo, eventofolhapagamento, jornada, valorhora, valormensal FROM public.salariocomposto");
		return sql;
	}

	@Override
	public SalarioCompostoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		SalarioCompostoVO obj = new SalarioCompostoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setJornada(tabelaResultado.getInt("jornada"));
		obj.setValorHora(tabelaResultado.getBigDecimal("valorhora"));
		obj.setValorMensal(tabelaResultado.getBigDecimal("valormensal"));
		
		if(Uteis.NIVELMONTARDADOS_DADOSMINIMOS == nivelMontarDados)
			return obj;
		
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null)));
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
		
		return obj;
	}

	public SalarioCompostoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		SalarioCompostoVO obj = new SalarioCompostoVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO)));
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, usuarioVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
		obj.setJornada(tabelaResultado.getInt("jornada"));
		obj.setValorHora(tabelaResultado.getBigDecimal("valorhora"));
		obj.setValorMensal(tabelaResultado.getBigDecimal("valormensal"));
		return obj;
	}

	@Override
	public void validarDados(SalarioCompostoVO obj) throws ConsistirException {
		
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		List<FuncionarioCargoVO> objs = new ArrayList<>();

		objs = getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario);
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));

		dataModelo.setListaConsulta(objs);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String where) {
		try {
			StringBuilder sqlStr = new StringBuilder(getFacadeFactory().getFuncionarioCargoFacade().getSQLTotalizadorFuncionarioCargo().append(where));
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public List<SalarioCompostoVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE salariocomposto.funcionariocargo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());
		
		List<SalarioCompostoVO> lista = new ArrayList<>();
		while(rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		}
		return lista;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SalarioComposto.idEntidade = idEntidade;
	}

	@Override
	public void adicionarEventosDoFuncionarioNoContraCheque(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sc.codigo as codigo, sc.jornada as jornada, sc.valorhora as valorhora, sc.valormensal as valormensal, ");
		sql.append(" e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento from salariocomposto sc ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = sc.eventoFolhaPagamento ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		sql.append(" where sc.funcionarioCargo = ? ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), funcionarioCargo.getCodigo());

		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(
						tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if(Uteis.isAtributoPreenchido(obj) && !Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
					tratarEventosSalarioComposto(tabelaResultado, obj);
					listaDeEventosDoFuncionario.add(obj);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Preenche as informacoes do salario composto para ser exibido no evento
	 * @param tabelaResultado
	 * @param obj
	 * @throws Exception
	 */
	private void tratarEventosSalarioComposto(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj) throws Exception {
		
		if(Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
			return;
		} else {
			SalarioCompostoVO salarioComposto = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			
			obj.setReferencia(salarioComposto.getJornada().toString());
			obj.setValorTemporario(salarioComposto.getValorMensal());
			obj.setValorInformado(true);
			
		}
	}
	
	/**
	 * Realiza a soma dos valores da propriedade valorMensal da lista de salario composto passada como parametro
	 * @param listaSalarioComposto
	 * @return
	 */
	@Override
	public BigDecimal realizarSomaDoValorMensalDoSalarioComposto(List<SalarioCompostoVO> listaSalarioComposto) {
		return new BigDecimal(listaSalarioComposto.stream().mapToDouble(p -> p.getValorMensal().doubleValue()).sum());
		
	}
	
	/**
	 * Realiza a soma dos valores da propriedade jornada da lista de salario composto passada como parametro
	 * @param listaSalarioComposto
	 * @return
	 */
	@Override
	public Integer realizarSomaDasJornadasDoSalarioComposto(List<SalarioCompostoVO> listaSalarioComposto) {
		return listaSalarioComposto.stream().mapToInt(p -> p.getJornada()).sum();
	}
}