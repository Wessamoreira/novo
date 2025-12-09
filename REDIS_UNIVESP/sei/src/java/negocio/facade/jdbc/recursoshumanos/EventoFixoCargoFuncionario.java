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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFixoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.EventoFixoCargoFuncionarioInterfaceFacade;

/**
 * 
 * @author Gilberto Nery
 *
 */
@SuppressWarnings("unchecked")
@Scope("singleton")
@Repository
@Lazy
public class EventoFixoCargoFuncionario extends ControleAcesso implements EventoFixoCargoFuncionarioInterfaceFacade {

	private static final long serialVersionUID = 7394558315333918982L;
	
	private static String idEntidade = "EventoFixoCargoFuncionario";

	public EventoFixoCargoFuncionario() {
		super();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<EventoFixoCargoFuncionarioVO> listaDeEventoFixos, FuncionarioCargoVO funcionarioCargo, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {

			excluir(funcionarioCargo, true, usuarioVO);
			
			for(EventoFixoCargoFuncionarioVO obj : listaDeEventoFixos) {
				obj.setFuncionarioCargoVO(funcionarioCargo);
				validarDados(obj);
				incluir(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EventoFixoCargoFuncionarioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO eventofixocargofuncionario (eventofolhapagamento, funcionariocargo, lancamentofixo, valor, numerolancamento, ");
			sql.append(" numerototallancamento)  ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ");
			sql.append(" ?) ");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamentoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLancamentoFixo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroLancamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroLancamento(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM eventofixocargofuncionario WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EventoFixoCargoFuncionarioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM eventofixocargofuncionario WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from eventofixocargofuncionario ef ")
		.append(" inner join eventofolhapagamento evento on evento.codigo = ef.eventofolhapagamento ")
		.append(" inner join funcionariocargo on funcionariocargo.codigo = ef.funcionariocargo ")
		.append(" and funcionariocargo.utilizarh = true and funcionariocargo.situacaofuncionario <> 'D' and funcionariocargo.datademissao is null")
		.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ")
		.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ")
		.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo ");
		return sql;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<FuncionarioCargoVO> consultarPorEnumCampoConsultaEventoFixoCargoFuncionario(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		List<FuncionarioCargoVO> objs = new ArrayList<>();

		objs = getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario);
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));

		dataModelo.setListaConsulta(objs);
		return objs;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<EventoFixoCargoFuncionarioVO> consultarPorCargoFuncionario(FuncionarioCargoVO funcionarioCargo, DataModelo dataModelo) throws Exception {
		
		List<EventoFixoCargoFuncionarioVO> objs = new ArrayList<>();
		
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(getSQLPadraoConsultaBasica().append(" WHERE funcionariocargo.codigo = ? ").toString(), funcionarioCargo.getCodigo());

		while (tabelaResultado.next()) {
			objs.add(montarDados(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
			
		}
		dataModelo.setListaConsulta(objs);
		return objs;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public EventoFixoCargoFuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(getSQLPadraoConsultaBasica().append(" WHERE codigo = ? ").toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( Evento Fixo Funcionario ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	public List<EventoFixoCargoFuncionarioVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<EventoFixoCargoFuncionarioVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public EventoFixoCargoFuncionarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {

			EventoFixoCargoFuncionarioVO obj = new EventoFixoCargoFuncionarioVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setLancamentoFixo(dadosSQL.getBoolean("lancamentofixo"));
			obj.setValor(dadosSQL.getBigDecimal("valor"));
			obj.setNumeroLancamento(dadosSQL.getInt("numerolancamento"));
			obj.setNumeroTotalLancamento(dadosSQL.getInt("numerototallancamento"));

			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
				return obj;
			}

			obj.setEventoFolhaPagamentoVO(Uteis.montarDadosVO(dadosSQL.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, usuario, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));

			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
				obj.setFuncionarioCargoVO(Uteis.montarDadosVO(dadosSQL.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
			} 

			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	public List<EventoFolhaPagamentoVO> consultarEventoFixoDoFuncionario(FuncionarioCargoVO funcionarioCargoVO) {
		List<EventoFolhaPagamentoVO> objs = new ArrayList<>();
		
		StringBuilder sqlConsulta = new StringBuilder(" select eventofolhapagamento from eventofixocargofuncionario ").append(" WHERE funcionariocargo = ? ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlConsulta.toString(), funcionarioCargoVO.getCodigo());

		while (tabelaResultado.next()) {
			try {
				objs.add(Uteis.montarDadosVO(tabelaResultado.getInt("evento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_TODOS)));
			} catch (Exception e) {
				throw new StreamSeiException(e.getMessage());
			}
		}
		return objs;		
	}

	private void validarDados(EventoFixoCargoFuncionarioVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO()), "O Funcionário deve ser informado.");
	}

	/**
	 * Operação reponsavel por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return EventoFixoCargoFuncionario.idEntidade;
	}
	
	
	/**
	 * Responsavel por adicionar na lista: listaDeEventosDoFuncionario os eventos fixos do funcionario
	 * e caso o mesma ja esteja no contracheque, traz o ContraChequeEventoVO desse evento 
	 */
	@Override
	public void adicionarEventosDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ef.codigo as codigo, ef.lancamentofixo as lancamentofixo, ef.valor as valor, ef.numerolancamento as numerolancamento, ef.numerototallancamento as numerototallancamento,");
		sql.append(" e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento from eventofixocargofuncionario  ef ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = ef.eventoFolhaPagamento and situacao like 'ATIVO' ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		
		int qtdParcelasRestantes = 0;
		
		if(reciboDeFerias) {
			// Caso a busca dos eventos fixos seja para o Recibo de Ferias, como o recibo (normalmente) e feito no mes anterior, verifica se nao e a ultima parcela do eveno fixo
			qtdParcelasRestantes = 1;
		}

		sql.append(" where ef.funcionarioCargo = ? and ( ef.numerolancamento > ").append(qtdParcelasRestantes).append(" or ef.lancamentofixo = true ) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), funcionarioCargo.getCodigo());

		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if(Uteis.isAtributoPreenchido(obj) && !Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
					tratarEventosFixosCadastradosNoFuncionario(tabelaResultado, obj, reciboDeFerias);
					if(!listaDeEventosDoFuncionario.contains(obj) || obj.getPermiteDuplicarContraCheque()) {
						listaDeEventosDoFuncionario.add(obj);	
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}

	/**
	 * Trata os eventos que foram lancados no grupo
	 * Valores que foram escolhidos manualmente
	 * 
	 * @param tabelaResultado
	 * @param obj
	 * @param reciboDeFerias 
	 */
	private void tratarEventosFixosCadastradosNoFuncionario(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj, Boolean reciboDeFerias) throws Exception {
		
		EventoFixoCargoFuncionarioVO eventoFixo = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null);
System.out.println(eventoFixo.getValor());
		obj.setValorTemporario(eventoFixo.getValor());
		obj.setValorInformado(obj.getValorTemporario().compareTo(BigDecimal.ZERO) > 0);
		
		if(!eventoFixo.getLancamentoFixo()) {
			
			// A parcela contabiliza com ela mesma. 
			int saldoParcela = 1;
			if(reciboDeFerias) {
				// Caso seja recibo de ferias, esta sendo contabilizada a parcela do mes seguinte
				saldoParcela = 2;
			}
			
			Integer qtdParcela = eventoFixo.getNumeroTotalLancamento() - eventoFixo.getNumeroLancamento() + saldoParcela;
			StringBuilder numLancamento = new StringBuilder(qtdParcela.toString()).append("/").append(eventoFixo.getNumeroTotalLancamento());
			obj.setReferencia(numLancamento.toString());
		}
			
	}

	/**
	 * Consulta os eventos fixos do funcionario pela competencia e os que possuem
	 * o numero de lancameno maior que 0.
	 */
	@Override
	public List<EventoFixoCargoFuncionarioVO> consultarPorCompetenciaEventoFixo(long codigoCompetencia, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct ef.codigo, ef.eventofolhapagamento, ef.funcionariocargo, ef.valor, ef.lancamentofixo, ");
		sql.append(" ef.numerolancamento, ef.numerototallancamento FROM eventofixocargofuncionario ef");
		sql.append(" INNER JOIN contrachequeevento cce ON ef.eventofolhapagamento = cce.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque cc ON cc.codigo = cce.contracheque");
		sql.append(" WHERE cc.competenciafolhapagamento = ? AND ef.numerolancamento > 0 and ef.valor > 0");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCompetencia);
		List<EventoFixoCargoFuncionarioVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return lista;
	}

	/**
	 * Consulta os eventos fixos do funcionario pela competencia e os que possuem
	 * o numero de lancameno maior que 0.
	 */
	@Override
	public List<EventoFixoCargoFuncionarioVO> consultarEventoFixoZerado(UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventofixocargofuncionario ef");
		sql.append(" WHERE valor > 0 and lancamentofixo = false and numerolancamento = 0");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<EventoFixoCargoFuncionarioVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return lista;
	}

	/**
	 * Altera o numero de lançamento , diminui o numero de lancamento em 1.
	 */
	@Override
	public void alterarNumeroLancamentoEventoFixo(EventoFixoCargoFuncionarioVO obj, UsuarioVO usuario) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.eventofixocargofuncionario  SET numerolancamento=? WHERE codigo = ? ")
								.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getNumeroLancamento() - 1, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});

	}
}