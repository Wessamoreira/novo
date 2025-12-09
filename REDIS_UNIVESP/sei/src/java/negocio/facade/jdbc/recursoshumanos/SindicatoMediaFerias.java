package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoMediaVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoMediaFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.interfaces.recursoshumanos.SindicatoMediaFeriasInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>EventoFolhaPagamentoMediaVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoMediaVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SindicatoMediaFerias extends SuperFacade<SindicatoMediaFeriasVO> implements SindicatoMediaFeriasInterfaceFacade<SindicatoMediaFeriasVO> {

	private static final long serialVersionUID = -3996179376156513483L;
	
	protected static String idEntidade;

	public SindicatoMediaFerias() throws Exception {
		super();
		setIdEntidade("SindicatoMediaFerias");
	}

	@Override
	public void persistirTodos(List<SindicatoMediaFeriasVO> sindicatoMediaFeriasVOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception {
		
		excluirTodosQueNaoEstaoNaLista(obj, sindicatoMediaFeriasVOs, false, usuarioVO);

		for (SindicatoMediaFeriasVO sindicatoMediaVO : sindicatoMediaFeriasVOs) {
			sindicatoMediaVO.setSindicatoVO(obj);
			persistir(sindicatoMediaVO, false, usuarioVO);
		}
	}
	
	private void excluirTodosQueNaoEstaoNaLista(SindicatoVO obj, List<SindicatoMediaFeriasVO> sindicatoMediaFeriasVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		SindicatoMediaFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<SindicatoMediaFeriasVO> i = sindicatoMediaFeriasVOs.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM sindicatomediaferias WHERE sindicato = ? ");
	    while (i.hasNext()) {
	    	SindicatoMediaFeriasVO objeto = (SindicatoMediaFeriasVO)i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(SindicatoMediaFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(SindicatoMediaFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SindicatoMediaFerias.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.sindicatomediaferias(grupo, eventomedia, sindicato) VALUES (?, ?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoMediaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSindicatoVO(), ++i, sqlInserir);

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
	public void alterar(SindicatoMediaFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.sindicatomediaferias SET grupo=?, eventomedia=?, sindicato=? WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEventoMediaVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSindicatoVO(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(SindicatoMediaFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM sindicatomediaferias WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public SindicatoMediaFeriasVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(SindicatoMediaFeriasVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoMedia_Grupo"));
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getEventoMediaVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoMedia_TipoMedia"));
		}
	}

	/**
	 * Monta a lista de {@link EventoFolhaPagamentoMediaVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<SindicatoMediaFeriasVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<SindicatoMediaFeriasVO> sindicatoEventoMedias = new ArrayList<>();

        while(tabelaResultado.next()) {
        	sindicatoEventoMedias.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
		return sindicatoEventoMedias;
	}

	@Override
	public SindicatoMediaFeriasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		SindicatoMediaFeriasVO obj = new SindicatoMediaFeriasVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEventoMediaVO(Uteis.montarDadosVO(tabelaResultado.getInt("eventomedia"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventomedia"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setSindicatoVO(Uteis.montarDadosVO(tabelaResultado.getInt("sindicato"), SindicatoVO.class, p -> getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("sindicato"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));		
		obj.setGrupo(tabelaResultado.getString("grupo"));
		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM sindicatomediaferias ");

		return sql.toString();
	}

	@Override
	public List<SindicatoMediaFeriasVO> consultarPorSindicatoVO(SindicatoVO sindicatoVO, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE sindicato = ? order by grupo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), sindicatoVO.getCodigo());
		return (montarDadosLista(tabelaResultado));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		SindicatoMediaFerias.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorSindicato(Integer codigoSindicato,boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SindicatoMediaFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder("DELETE FROM sindicatomediaferias WHERE ((sindicato = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { codigoSindicato });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consultarEventosDeMediaDeFerias(MarcacaoFeriasVO marcacao, List<EventoFolhaPagamentoVO> listaDeEventos) {

		List<SindicatoMediaFeriasVO> listaDeMediaDoSindicato = consultarEventosDeMediaDoSindicatoDoFuncionario(marcacao.getFuncionarioCargoVO());

		int diaQuinze = 15;
		if(!listaDeMediaDoSindicato.isEmpty()) {

			int numeroMesInicialProgredir = 0, numeroMesFinalProgredir = 0;
			if (UteisData.getDiaMesData(marcacao.getPeriodoAquisitivoFeriasVO().getInicioPeriodo()) > diaQuinze) {
				numeroMesInicialProgredir = 1;
			}

			if (UteisData.getDiaMesData(marcacao.getPeriodoAquisitivoFeriasVO().getFinalPeriodo()) < diaQuinze) {
				numeroMesFinalProgredir = -1;
			}

			// A media e calculado em cima dos meses do periodo aquisitivo
			//Desconsiderar o ultimo mes do periodo aquisitivo para nao considerar 13 meses
			String dataInicial = UteisData.getDataFormatadaPorAnoMes(UteisData.obterDataFuturaAdicionandoMes(marcacao.getPeriodoAquisitivoFeriasVO().getInicioPeriodo(), numeroMesInicialProgredir));
			String dataFinal = UteisData.getDataFormatadaPorAnoMes(UteisData.obterDataFuturaAdicionandoMes(marcacao.getPeriodoAquisitivoFeriasVO().getFinalPeriodo(), numeroMesFinalProgredir));
			
			EventoFolhaPagamentoVO eventoDeMediaDoSindicato;
			BigDecimal valorDaMedia = BigDecimal.ZERO;
			for(SindicatoMediaFeriasVO sindicatoMedia : listaDeMediaDoSindicato) {
				eventoDeMediaDoSindicato = sindicatoMedia.getEventoMediaVO();
				valorDaMedia = getFacadeFactory().getContraChequeEventoInterfaceFacade().
						consultarValorDaMediaDosEventosDoGrupoPorFuncionarioEPeriodo(marcacao.getFuncionarioCargoVO(), sindicatoMedia.getGrupo(),TipoEventoMediaEnum.INCIDE_FERIAS.getValor(), dataInicial, dataFinal);
				if (marcacao.getAbono()) {
					BigDecimal resultado = valorDaMedia.divide( new BigDecimal("30") , 6, RoundingMode.HALF_EVEN).multiply(new BigDecimal(marcacao.getQtdDias().toString()));
					valorDaMedia = resultado.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
				eventoDeMediaDoSindicato.setValorTemporario(valorDaMedia);
				eventoDeMediaDoSindicato.setValorInformado(false);
				listaDeEventos.add(eventoDeMediaDoSindicato);
			}
			
		}
		
	}

	public List<SindicatoMediaFeriasVO> consultarEventosDeMediaDoSindicatoDoFuncionario(FuncionarioCargoVO funcionarioCargoVO) {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE sindicatomediaferias.sindicato = ? ");
		sql.append(" order by sindicatomediaferias.grupo ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getSindicatoVO().getCodigo());
		
		List<SindicatoMediaFeriasVO> lista = new ArrayList<SindicatoMediaFeriasVO>();
        while (rs.next()) {
        	try {
        		lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSCONSULTA));	
        	}catch (Exception e) {
        		e.printStackTrace();
			}
        }
		return lista;
	}
}