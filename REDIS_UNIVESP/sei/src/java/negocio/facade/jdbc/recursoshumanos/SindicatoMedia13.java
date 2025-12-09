package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
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
import negocio.comuns.recursoshumanos.SindicatoMedia13VO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.interfaces.recursoshumanos.SindicatoMedia13InterfaceFacade;

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
public class SindicatoMedia13 extends SuperFacade<SindicatoMedia13VO> implements SindicatoMedia13InterfaceFacade<SindicatoMedia13VO> {

	private static final long serialVersionUID = -560880500807411349L;
	
	protected static String idEntidade;

	public SindicatoMedia13() throws Exception {
		super();
		setIdEntidade("SindicatoMedia13");
	}

	@Override
	public void persistirTodos(List<SindicatoMedia13VO> sindicatoMedia13VOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception {
		
		excluirTodosQueNaoEstaoNaLista(obj, sindicatoMedia13VOs, false, usuarioVO);

		for (SindicatoMedia13VO sindicatoMediaVO : sindicatoMedia13VOs) {
			sindicatoMediaVO.setSindicatoVO(obj);
			persistir(sindicatoMediaVO, false, usuarioVO);
		}
	}
	
	private void excluirTodosQueNaoEstaoNaLista(SindicatoVO obj, List<SindicatoMedia13VO> sindicatoMedia13VOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		SindicatoMedia13.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<SindicatoMedia13VO> i = sindicatoMedia13VOs.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM sindicatomedia13 WHERE sindicato = ? ");
	    while (i.hasNext()) {
	    	SindicatoMedia13VO objeto = (SindicatoMedia13VO)i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(SindicatoMedia13VO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(SindicatoMedia13VO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SindicatoMedia13.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.sindicatomedia13(grupo, eventomedia, sindicato) VALUES (?, ?, ?)");
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
	public void alterar(SindicatoMedia13VO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMedia13.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.sindicatomedia13 SET grupo=?, eventomedia=?, sindicato=? WHERE codigo = ?");
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
	public void excluir(SindicatoMedia13VO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMedia13.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM sindicatomedia13 WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public SindicatoMedia13VO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(SindicatoMedia13VO obj) throws ConsistirException {
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
	private List<SindicatoMedia13VO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<SindicatoMedia13VO> sindicatoEventoMedias = new ArrayList<>();

        while(tabelaResultado.next()) {
        	sindicatoEventoMedias.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
		return sindicatoEventoMedias;
	}

	@Override
	public SindicatoMedia13VO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		SindicatoMedia13VO obj = new SindicatoMedia13VO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEventoMediaVO(Uteis.montarDadosVO(tabelaResultado.getInt("eventomedia"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventomedia"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setSindicatoVO(Uteis.montarDadosVO(tabelaResultado.getInt("sindicato"), SindicatoVO.class, p -> getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("sindicato"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));		
		obj.setGrupo(tabelaResultado.getString("grupo"));
		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM sindicatomedia13 ");

		return sql.toString();
	}

	@Override
	public List<SindicatoMedia13VO> consultarPorSindicatoVO(SindicatoVO sindicatoVO, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE sindicato = ? order by grupo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), sindicatoVO.getCodigo());
		return (montarDadosLista(tabelaResultado));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		SindicatoMedia13.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorSindicato(Integer codigoSindicato,boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SindicatoMedia13.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder("DELETE FROM sindicatomedia13 WHERE ((sindicato = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { codigoSindicato });
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Consulta os eventos de Media do 13
	 * @param marcacao
	 * @param listaDeEventos
	 */
	public void consultarEventosDeMediaDe13(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos) {
		
		List<SindicatoMedia13VO> listaDeMedia13DoSindicato = consultarEventosDeMediaDoSindicatoDoFuncionario(funcionarioCargo);
		
		if(!listaDeMedia13DoSindicato.isEmpty()) {
			
			// A media e calculado em cima dos meses do inicio e do final ano
			String dataInicial = UteisData.getDataFormatadaPorAnoMes(UteisData.getPrimeiroDataMes(1, anoCompetencia));
			String dataFinal = UteisData.getDataFormatadaPorAnoMes(UteisData.getUltimaDataMes(12, anoCompetencia));
			
			EventoFolhaPagamentoVO eventoDeMediaDoSindicato;
			BigDecimal valorDaMedia = BigDecimal.ZERO;
			for(SindicatoMedia13VO sindicatoMedia : listaDeMedia13DoSindicato) {
				eventoDeMediaDoSindicato = sindicatoMedia.getEventoMediaVO();
				valorDaMedia = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarValorDaMediaDosEventosDoGrupoPorFuncionarioEPeriodo(funcionarioCargo, sindicatoMedia.getGrupo(), 
								TipoEventoMediaEnum.INCIDE_13.getValor(), dataInicial, dataFinal);
				eventoDeMediaDoSindicato.setValorTemporario(valorDaMedia);
				eventoDeMediaDoSindicato.setValorInformado(true);
				listaDeEventos.add(eventoDeMediaDoSindicato);
			}
			
		}
		
	}

	public List<SindicatoMedia13VO> consultarEventosDeMediaDoSindicatoDoFuncionario(FuncionarioCargoVO funcionarioCargoVO) {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE sindicatomedia13.sindicato = ? ");
		sql.append(" order by sindicatomedia13.grupo ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getSindicatoVO().getCodigo());
		
		List<SindicatoMedia13VO> lista = new ArrayList<SindicatoMedia13VO>();
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