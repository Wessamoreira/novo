package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
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
import negocio.comuns.recursoshumanos.SindicatoMediaRescisaoVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaRescisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.SindicatoMediaRescisaoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>SindicatoMediaRescisaoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ESindicatoMediaRescisaoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class SindicatoMediaRescisao  extends SuperFacade<SindicatoMediaRescisaoVO> implements SindicatoMediaRescisaoInterfaceFacade<SindicatoMediaRescisaoVO> {

	
	private static final long serialVersionUID = 2582551299278550588L;

	protected static String idEntidade;

	public SindicatoMediaRescisao() throws Exception {
		super();
		setIdEntidade("SindicatoMediaRescisao");
	}
	
	@Override
	public void persistirTodos(List<SindicatoMediaRescisaoVO> sindicatoMediaRescisaoVOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception {
		excluirTodosQueNaoEstaoNaLista(obj, sindicatoMediaRescisaoVOs, false, usuarioVO);

		for (SindicatoMediaRescisaoVO sindicatoMediaRescisaoVO : sindicatoMediaRescisaoVOs) {
			sindicatoMediaRescisaoVO.setSindicato(obj);
			persistir(sindicatoMediaRescisaoVO, false, usuarioVO);
		}
		
	}

	/**
	 * Exclui todos os sindicatos media Rescisão que foram removidos da lista. 
	 * @param obj
	 * @param sindicatoMediaRescisaoVOs
	 * @param validarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void excluirTodosQueNaoEstaoNaLista(SindicatoVO obj, List<SindicatoMediaRescisaoVO> sindicatoMediaRescisaoVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaRescisao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<SindicatoMediaRescisaoVO> i = sindicatoMediaRescisaoVOs.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM sindicatoMediaRescisao WHERE sindicato = ? ");
	    while (i.hasNext()) {
	    	SindicatoMediaRescisaoVO objeto = i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }

		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(SindicatoMediaRescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(SindicatoMediaRescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaRescisao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "sindicatomediarescisao", new AtributoPersistencia()
				.add("grupo", obj.getGrupo())
				.add("eventomedia",  obj.getEventoFolhaPagamento().getCodigo() == 0 ? null : obj.getEventoFolhaPagamento().getCodigo())
				.add("sindicato", obj.getSindicato().getCodigo() == 0 ? null : obj.getSindicato().getCodigo())
				.add("tipoeventomediarescisao", obj.getTipoEventoMediaRescisao()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	public void alterar(SindicatoMediaRescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaRescisao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "sindicatomediarescisao", new AtributoPersistencia()
				.add("grupo", obj.getGrupo())
				.add("eventomedia",  obj.getEventoFolhaPagamento().getCodigo() == 0 ? null : obj.getEventoFolhaPagamento().getCodigo())
				.add("sindicato", obj.getSindicato().getCodigo() == 0 ? null : obj.getSindicato().getCodigo())
				.add("tipoeventomediarescisao", obj.getTipoEventoMediaRescisao()),
				 new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(SindicatoMediaRescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		SindicatoMediaRescisao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM sindicatomediarescisao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
	}

	@Override
	public SindicatoMediaRescisaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(SindicatoMediaRescisaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoMedia_Grupo"));
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getEventoFolhaPagamento())) {
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
	private List<SindicatoMediaRescisaoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<SindicatoMediaRescisaoVO> sindicatoEventoMedias = new ArrayList<>();

        while(tabelaResultado.next()) {
        	sindicatoEventoMedias.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
		return sindicatoEventoMedias;
	}

	@Override
	public SindicatoMediaRescisaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		SindicatoMediaRescisaoVO obj = new SindicatoMediaRescisaoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventomedia"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventomedia"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setSindicato(Uteis.montarDadosVO(tabelaResultado.getInt("sindicato"), SindicatoVO.class, p -> getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("sindicato"), null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));		
		obj.setGrupo(tabelaResultado.getString("grupo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoeventomediarescisao"))) {
			obj.setTipoEventoMediaRescisao(TipoEventoMediaRescisaoEnum.valueOf(tabelaResultado.getString("tipoeventomediarescisao")));
		}
		return obj;
	}

	@Override
	public List<SindicatoMediaRescisaoVO> consultarPorSindicatoVO(SindicatoVO sindicatoVO, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE sindicato = ? order by grupo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), sindicatoVO.getCodigo());
		return (montarDadosLista(tabelaResultado));
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM sindicatomediarescisao ");

		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SindicatoMediaRescisao.idEntidade = idEntidade;
	}

	private List<SindicatoMediaRescisaoVO> consultarEventosDeMediaDeRescisaoDoSindicatoDoFuncionario(FuncionarioCargoVO funcionarioCargo, TipoEventoMediaRescisaoEnum tipoEventoMediaRescisao) {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE sindicatomediarescisao.sindicato = ? ");
		sql.append(" and sindicatomediarescisao.tipoEventoMediaRescisao = ? ");
		sql.append(" order by sindicatomediarescisao.grupo ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargo.getSindicatoVO().getCodigo(), tipoEventoMediaRescisao.getValor());
		
		List<SindicatoMediaRescisaoVO> lista = new ArrayList<SindicatoMediaRescisaoVO>();
        while (rs.next()) {
        	try {
        		lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSCONSULTA));	
        	}catch (Exception e) {
        		e.printStackTrace();
			}
        }
		return lista;
	}
	
	@Override
	public void consultarEventosDeMediaDeRescisao(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos, TipoEventoMediaRescisaoEnum tipoEventoMedia) {
		
		List<SindicatoMediaRescisaoVO> listaDeMediaRescisaoDoSindicato = consultarEventosDeMediaDeRescisaoDoSindicatoDoFuncionario(funcionarioCargo, tipoEventoMedia);
		
		if(!listaDeMediaRescisaoDoSindicato.isEmpty()) {
			
			// A media e calculado em cima dos meses do inicio e do final ano
			String dataInicial = UteisData.getDataFormatadaPorAnoMes(UteisData.getPrimeiroDataMes(1, anoCompetencia));
			String dataFinal = UteisData.getDataFormatadaPorAnoMes(UteisData.getUltimaDataMes(12, anoCompetencia));
			
			EventoFolhaPagamentoVO eventoDeMediaDoSindicato;
			BigDecimal valorDaMedia = BigDecimal.ZERO;
			for(SindicatoMediaRescisaoVO sindicatoMedia : listaDeMediaRescisaoDoSindicato) {
				eventoDeMediaDoSindicato = sindicatoMedia.getEventoFolhaPagamento();
				valorDaMedia = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarValorDaMediaDosEventosDoGrupoPorFuncionarioEPeriodo(funcionarioCargo, sindicatoMedia.getGrupo(), 
						tipoEventoMedia.getValor(), dataInicial, dataFinal);
				eventoDeMediaDoSindicato.setValorTemporario(valorDaMedia);
				eventoDeMediaDoSindicato.setValorInformado(true);
				listaDeEventos.add(eventoDeMediaDoSindicato);
			}
			
		}
	}

}
