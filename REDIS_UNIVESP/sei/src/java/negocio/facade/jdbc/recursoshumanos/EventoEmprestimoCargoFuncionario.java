package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoEmprestimoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.EventoEmprestimoCargoFuncionarioInterfaceFacade;

/**
 * 
 * @author Gilberto Nery
 *
 */
@Scope("singleton")
@Repository
@Lazy
public class EventoEmprestimoCargoFuncionario extends SuperFacade<EventoEmprestimoCargoFuncionarioVO> implements EventoEmprestimoCargoFuncionarioInterfaceFacade<EventoEmprestimoCargoFuncionarioVO> {

	private static final long serialVersionUID = 2089586924070251602L;
	private static String idEntidade = "EventoEmprestimoCargoFuncionario";

	public EventoEmprestimoCargoFuncionario() {
		super();
	}

	@Override
	public void validarDados(EventoEmprestimoCargoFuncionarioVO obj) throws ConsistirException {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO()), "O Funcionário deve ser informado.");
	}

	@Override
	public void persistir(List<EventoEmprestimoCargoFuncionarioVO> listaDeEventoDeEmprestimo, FuncionarioCargoVO funcionarioCargo, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			excluir(funcionarioCargo, true, usuarioVO);
			for(EventoEmprestimoCargoFuncionarioVO obj : listaDeEventoDeEmprestimo) {
				obj.setFuncionarioCargoVO(funcionarioCargo);
				validarDados(obj);
				incluir(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}
	
	@Override
	public void consultarPorEnumCampoConsultaEventoEmprestimoCargoFuncionario(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		List<FuncionarioCargoVO> objs = new ArrayList<>();

		objs = getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario);
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));

		dataModelo.setListaConsulta(objs);
	}

	@Override
	public void excluir(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			getConexao().getJdbcTemplate().update("DELETE FROM eventoemprestimocargofuncionario WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public List<EventoEmprestimoCargoFuncionarioVO> consultarPorCargoFuncionario(FuncionarioCargoVO funcionarioCargo, DataModelo dataModelo) throws Exception {
		List<EventoEmprestimoCargoFuncionarioVO> objs = new ArrayList<>();
		
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(getSQLPadraoConsultaBasica().append(" WHERE funcionariocargo.codigo = ? ").toString(), funcionarioCargo.getCodigo());

		while (tabelaResultado.next()) {
			objs.add(montarDados(tabelaResultado, dataModelo.getNivelMontarDados()));
			
		}
		dataModelo.setListaConsulta(objs);
		return objs;
	}

	@Override
	public void adicionarEventosDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, Boolean reciboDeFerias) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ef.codigo, ef.numeroParcela, ef.valorParcela, ef.valorTotal, ef.dataEmprestimo, ef.inicioDesconto, ef.parcelaPaga, ef.quitado, ef.datapagamento,  ");
		sql.append(" e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento from eventoemprestimocargofuncionario  ef ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = ef.eventoFolhaPagamento and situacao like 'ATIVO' ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		sql.append(" where ef.funcionarioCargo = ? and inicioDesconto <= '").append(UteisData.getDataJDBC(UteisData.getUltimaDataMes(new Date()))).append("'");

		// Caso seja recibo de ferias, valida que nao e a ultima parcela de emprestimo
		if(reciboDeFerias)
			sql.append(" and (parcelapaga + 1) <= numeroparcela");
		else
			sql.append(" and parcelapaga < numeroparcela");

		sql.append(" and ef.quitado = false");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), funcionarioCargo.getCodigo());

		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if(Uteis.isAtributoPreenchido(obj) && !Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
					tratarEventoDeEmprestimo(tabelaResultado, obj, reciboDeFerias);
					if(!listaDeEventosDoFuncionario.contains(obj) || obj.getPermiteDuplicarContraCheque()) {
						listaDeEventosDoFuncionario.add(obj);	
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Preenche as informacoes do emprestimo para ser exibido no evento
	 * @param tabelaResultado
	 * @param obj
	 * @param reciboDeFerias 
	 * @throws Exception
	 */
	private void tratarEventoDeEmprestimo(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj, Boolean reciboDeFerias) throws Exception {
		
		if(Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
			return;
		} else {
			
			EventoEmprestimoCargoFuncionarioVO eventoEmprestimo = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			
			int parcelaASerPaga = 1;
			
			// Se for recibo de ferias, contabiliza 2, porque (normalmente) o recibo de ferias e gerado no mes anterior e so sera contabilizado no mes seguinte
			if(reciboDeFerias && ( (tabelaResultado.getInt("parcelapaga") + 1) != tabelaResultado.getInt("numeroparcela")))
				parcelaASerPaga = 2;
			
			StringBuilder numParcela = new StringBuilder();
			numParcela.append(eventoEmprestimo.getParcelaPaga()+parcelaASerPaga).append("/").append(eventoEmprestimo.getNumeroParcela());

			obj.setReferencia(numParcela.toString());
			obj.setValorTemporario(eventoEmprestimo.getValorParcela());
			obj.setValorInformado(true);
				
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EventoEmprestimoCargoFuncionarioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			EventoEmprestimoCargoFuncionario.incluir(getIdEntidade(), verificarAcesso, usuario);

			incluir(obj, "eventoemprestimocargofuncionario", new AtributoPersistencia()
					.add("eventofolhapagamento", obj.getEventoFolhaPagamentoVO())
					.add("funcionariocargo", obj.getFuncionarioCargoVO())
					.add("numeroParcela", obj.getNumeroParcela())
					.add("valorParcela", obj.getValorParcela())		
					.add("valorTotal", obj.getValorTotal())		
					.add("dataEmprestimo", obj.getDataEmprestimo())		
					.add("inicioDesconto", obj.getInicioDesconto())		
					.add("parcelaPaga", obj.getParcelaPaga())		
					.add("tipoEmprestimo", obj.getTipoEmprestimoVO())
					.add("quitado", obj.getQuitado())
					.add("dataPagamento", obj.getDataPagamento())
					, usuario);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(EventoEmprestimoCargoFuncionarioVO t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
	}

	@Override
	public void excluir(EventoEmprestimoCargoFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), validarAcesso, usuarioVO);
		excluir(obj.getFuncionarioCargoVO(), validarAcesso, usuarioVO);
	}

	@Override
	public EventoEmprestimoCargoFuncionarioVO consultarPorChavePrimaria(Long id) throws Exception {
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(getSQLPadraoConsultaBasica().append(" WHERE codigo = ? ").toString(), id);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( Evento Fixo Funcionario ).");
			}
			return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from eventoemprestimocargofuncionario ef ")
		.append(" inner join eventofolhapagamento evento on evento.codigo = ef.eventofolhapagamento ")
		.append(" inner join funcionariocargo on funcionariocargo.codigo = ef.funcionariocargo and funcionariocargo.utilizarh = true ")
		.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ")
		.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ")
		.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo ");
		return sql;
	}

	@Override
	public EventoEmprestimoCargoFuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		try {

			EventoEmprestimoCargoFuncionarioVO obj = new EventoEmprestimoCargoFuncionarioVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNumeroParcela(tabelaResultado.getInt("numeroParcela"));
			obj.setValorParcela(tabelaResultado.getBigDecimal("valorParcela"));
			obj.setValorTotal(tabelaResultado.getBigDecimal("valorTotal"));
			obj.setDataEmprestimo(tabelaResultado.getDate("dataEmprestimo"));
			obj.setInicioDesconto(tabelaResultado.getDate("inicioDesconto"));
			obj.setParcelaPaga(tabelaResultado.getInt("parcelaPaga"));
			obj.setQuitado(tabelaResultado.getBoolean("quitado"));
			obj.setDataPagamento(tabelaResultado.getDate("dataPagamento"));

			if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS)
				return obj;

			obj.setEventoFolhaPagamentoVO(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
			obj.setFuncionarioCargoVO(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null)));
			obj.setTipoEmprestimoVO(Uteis.montarDadosVO(tabelaResultado.getInt("tipoEmprestimo"), TipoEmprestimoVO.class, p -> getFacadeFactory().getTipoEmprestimoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));

			return obj;
			
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public List<EventoEmprestimoCargoFuncionarioVO> consultarPorCompetenciaEventoEmprestimo(long codigoCompetencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ef.codigo, ef.numeroParcela, ef.valorParcela, ef.valorTotal, ef.dataEmprestimo, ef.inicioDesconto, ef.parcelapaga, ef.quitado, ef.dataPagamento ");
		sql.append(" FROM eventoemprestimocargofuncionario ef");
		sql.append(" INNER JOIN contrachequeevento cce ON cce.eventofolhapagamento = ef.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque cc ON cc.codigo = cce.contracheque");
		sql.append(" WHERE cc.competenciafolhapagamento = ? AND parcelapaga < numeroparcela;");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCompetencia);

		List<EventoEmprestimoCargoFuncionarioVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS));
		}
		return lista;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	@Override
	public void alterarParcelaEmprestimo(EventoEmprestimoCargoFuncionarioVO obj , UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.eventoemprestimocargofuncionario SET parcelapaga=? WHERE codigo = ? ")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getParcelaPaga()+1, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
		
	}

	@Override
	public void alterarSituacaoQuitado(UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				
				StringBuilder sql = new StringBuilder("UPDATE public.eventoemprestimocargofuncionario SET quitado=true WHERE codigo in ")
						.append(" ( select codigo from eventoemprestimocargofuncionario where quitado = false and parcelapaga = numeroparcela) ")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				/*int i = 0;
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);*/
				
				return sqlAlterar;
			}
		});
		
	}
	
}