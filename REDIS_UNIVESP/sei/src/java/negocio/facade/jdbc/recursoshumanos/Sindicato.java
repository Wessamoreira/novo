package negocio.facade.jdbc.recursoshumanos;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEntidadeSindicalEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.SindicatoInterfaceFacade;

/*Classe de persistencia que encapsula todas as operacoes de manipulacao dos
* dados da classe <code>EventoFolhaPagamentoVO</code>. Responsavel por implementar
* operacoes como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoVO</code>. Encapsula toda a interacao com o banco de
* dados.
* 
*/
@Service
@Scope
@Lazy
public class Sindicato extends SuperFacade<SindicatoVO> implements SindicatoInterfaceFacade<SindicatoVO> {

	private static final long serialVersionUID = 2469494756862894173L;
	
	protected static String idEntidade;

	public Sindicato() throws Exception {
		super();
		setIdEntidade("Sindicato");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}

		getFacadeFactory().getSindicatoMediaFeriasInterfaceFacade().persistirTodos(obj.getMediaDasFerias(), obj, usuarioVO);
		getFacadeFactory().getSindicatoMedia13InterfaceFacade().persistirTodos(obj.getMedia13(), obj, usuarioVO);
		getFacadeFactory().getSindicatoMediaRescisaoInterfaceFacade().persistirTodos(obj.getSindicatoMediaRescisaoVOs(), obj, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
			
		Sindicato.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		incluir(obj, "sindicato", new AtributoPersistencia()
				.add("parceiro", obj.getParceiroVO())
				.add("tipoentidadesocial", obj.getTipoEntidadeSocial())
				.add("percentualDescontoVT", obj.getPercentualDescontoVT())
				.add("eventoAdiantamentoFerias", obj.getEventoAdiantamentoFerias())
				.add("eventoDescontoAdiantamentoFerias", obj.getEventoDescontoAdiantamentoFerias())
				.add("eventoLancamentoFalta", obj.getEventoLancamentoFalta())
				.add("eventoDSRPerdida", obj.getEventoDSRPerdida())
				.add("validarFaltas", obj.getValidarFaltas())
				.add("considerarFaltasFerias", obj.getConsiderarFaltasFerias())
				.add("eventoDevolucaoFalta", obj.getEventoDevolucaoFalta())
				.add("eventoPrimeiraParcela13", obj.getEventoPrimeiraParcela13())
				.add("eventoDescontoPrimeiraParcela13", obj.getEventoDescontoPrimeiraParcela13())
				, usuarioVO);
		
		obj.setNovoObj(Boolean.TRUE);
			
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		Sindicato.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "sindicato", new AtributoPersistencia()
				.add("parceiro", obj.getParceiroVO())
				.add("tipoentidadesocial", obj.getTipoEntidadeSocial())
				.add("percentualDescontoVT", obj.getPercentualDescontoVT())
				.add("eventoAdiantamentoFerias", obj.getEventoAdiantamentoFerias())
				.add("eventoDescontoAdiantamentoFerias", obj.getEventoDescontoAdiantamentoFerias())
				.add("eventoLancamentoFalta", obj.getEventoLancamentoFalta())
				.add("eventoDSRPerdida", obj.getEventoDSRPerdida())
				.add("validarFaltas", obj.getValidarFaltas())
				.add("considerarFaltasFerias", obj.getConsiderarFaltasFerias())
				.add("eventoDevolucaoFalta", obj.getEventoDevolucaoFalta())
				.add("eventoPrimeiraParcela13", obj.getEventoPrimeiraParcela13())
				.add("eventoDescontoPrimeiraParcela13", obj.getEventoDescontoPrimeiraParcela13())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SindicatoVO sindicato, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		try {
			Sindicato.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			getFacadeFactory().getSindicatoMediaFeriasInterfaceFacade().excluirPorSindicato(sindicato.getCodigo(), false, usuarioVO);
			getFacadeFactory().getSindicatoMedia13InterfaceFacade().excluirPorSindicato(sindicato.getCodigo(), false, usuarioVO);

			StringBuilder sql = new StringBuilder("DELETE FROM sindicato WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { sindicato.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Metodo de consulta paginado
	 */
	@Override
	public void consultarPorFiltro(DataModelo dataModelo) throws Exception {
		
		List<SindicatoVO> objs = new ArrayList<SindicatoVO>(0);
		dataModelo.getListaFiltros().clear();
		
		switch (dataModelo.getCampoConsulta()) {
		case "DESCRICAO":
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarSindicato(dataModelo, "p.nome");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(dataModelo, "p.nome"));
			break;
		default:
			break;
		}
		
		dataModelo.setListaConsulta(objs);
		
	}
	
	
	/**
	 * Consulta os sindicatos paginados com as informações vindas do dataModelo
	 * 
	 * @param dataModelo
	 * @param campoConsulta
	 * @return
	 * @throws Exception
	 */
	private List<SindicatoVO> consultarSindicato(DataModelo dataModelo,  String campoConsulta) throws Exception {
		
		Sindicato.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE 1 = 1");
		
        if (campoConsulta.equals("p.nome")) {
        	sql.append(" AND upper( p.nome ) like(upper(sem_acentos(?))) ");
        }
        sql.append(" order by ").append(campoConsulta).append(" asc ");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());			

		List<SindicatoVO> sindicatos = new ArrayList<>();
		while (tabelaResultado.next()) {
			sindicatos.add(montarDados(tabelaResultado, dataModelo.getNivelMontarDados()));
		}
		return sindicatos;
	}
	
	/**
	 * Consulta paginada do total de eventos da consulta dos filtros informados
	 * @param dataModelo
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	private Integer consultarTotalPorFiltros(DataModelo dataModelo, String campoConsulta) throws Exception {
		Sindicato.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder(getSelectSqlBasico().replace(" * ", " COUNT(s.codigo) as qtde "));

        sql.append(" WHERE 1 = 1");
		
        if (campoConsulta.equals("p.nome")) {
        	sql.append(" AND upper( p.nome ) like(upper(?)) ");
        } 
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	
	/**
	 * Monta o objeto <code>EventoFolhaPagamentoVO<code> consultado do banco de
	 * dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public SindicatoVO montarDados(SqlRowSet tabelaResultado, int montarDados)  throws Exception {

		SindicatoVO obj = new SindicatoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		obj.setParceiroVO(Uteis.montarDadosVO(tabelaResultado.getInt("parceiro"), ParceiroVO.class, p -> getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null)));
		
		if (tabelaResultado.getString("tipoentidadesocial") != null) {
			obj.setTipoEntidadeSocial(TipoEntidadeSindicalEnum.valueOf(tabelaResultado.getString("tipoentidadesocial")));
		} else {
			obj.setTipoEntidadeSocial(TipoEntidadeSindicalEnum.SINDICATO);
		}
		
		obj.setPercentualDescontoVT(tabelaResultado.getBigDecimal("percentualDescontoVT"));
		obj.setValidarFaltas(tabelaResultado.getBoolean("validarFaltas"));
		obj.setConsiderarFaltasFerias(tabelaResultado.getBoolean("considerarFaltasFerias"));

		obj.getEventoAdiantamentoFerias().setCodigo(tabelaResultado.getInt("eventoAdiantamentoFerias"));
		obj.getEventoDescontoAdiantamentoFerias().setCodigo(tabelaResultado.getInt("eventoDescontoAdiantamentoFerias"));
		obj.getEventoLancamentoFalta().setCodigo(tabelaResultado.getInt("eventoLancamentoFalta"));
		obj.getEventoDSRPerdida().setCodigo(tabelaResultado.getInt("eventoDSRPerdida"));
		obj.getEventoDevolucaoFalta().setCodigo(tabelaResultado.getInt("eventoDevolucaoFalta"));
		obj.getEventoPrimeiraParcela13().setCodigo(tabelaResultado.getInt("eventoPrimeiraParcela13"));
		obj.getEventoDescontoPrimeiraParcela13().setCodigo(tabelaResultado.getInt("eventoDescontoPrimeiraParcela13"));
		
		if(montarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA)
			return obj;
		
		obj.setEventoAdiantamentoFerias(Uteis.montarDadosVO(tabelaResultado.getInt("eventoAdiantamentoFerias"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoDescontoAdiantamentoFerias(Uteis.montarDadosVO(tabelaResultado.getInt("eventoDescontoAdiantamentoFerias"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoLancamentoFalta(Uteis.montarDadosVO(tabelaResultado.getInt("eventoLancamentoFalta"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoDSRPerdida(Uteis.montarDadosVO(tabelaResultado.getInt("eventoDSRPerdida"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoDevolucaoFalta(Uteis.montarDadosVO(tabelaResultado.getInt("eventoDevolucaoFalta"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoPrimeiraParcela13(Uteis.montarDadosVO(tabelaResultado.getInt("eventoPrimeiraParcela13"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		obj.setEventoDescontoPrimeiraParcela13(Uteis.montarDadosVO(tabelaResultado.getInt("eventoDescontoPrimeiraParcela13"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));
		
		if(montarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS)
			return obj;
		
		if(montarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setMediaDasFerias(getFacadeFactory().getSindicatoMediaFeriasInterfaceFacade().consultarPorSindicatoVO(obj, false, null));
			obj.setMedia13(getFacadeFactory().getSindicatoMedia13InterfaceFacade().consultarPorSindicatoVO(obj, false, null));
			obj.setSindicatoMediaRescisaoVOs(getFacadeFactory().getSindicatoMediaRescisaoInterfaceFacade().consultarPorSindicatoVO(obj, false, null));
		}

		return obj;
	}

	
	/**
	 * Sql basico para consultad da <code>EventoFolhaPagamentoVO<code>
	 * 
	 * @return
	 */
	public String getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM sindicato s");
		sql.append(" inner JOIN parceiro p ON p.codigo = s.codigo");

		return sql.toString();
	}

	
	/**
	 * Valida os campos obrigatorios do <code>EventoFolhaPagamentoVO<code>
	 * 
	 * @param obj <code>EventoFolhaPagamentoVO</code>
	 * 
	 * @throws ConsistirException
	 * @throws ParseException
	 */
	public void validarDados(SindicatoVO obj) throws ConsistirException {
		try {
			Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getParceiroVO().getCodigo()), "msg_Sindicato_parceiroNaoInformado");
			Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getPercentualDescontoVT()), "msg_Sindicato_valeTransporteNaoInformado");
			Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getEventoAdiantamentoFerias().getCodigo()), "msg_Sindicato_adiantamentoFerias");
			Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getEventoDescontoAdiantamentoFerias().getCodigo()), "msg_Sindicato_descontoAdiantamentoFerias");

			if(obj.getValidarFaltas()) {
				Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getEventoLancamentoFalta().getCodigo()), "msg_Sindicato_lancamentoFalta");
				Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getEventoDSRPerdida().getCodigo()), "msg_Sindicato_dsrPerdido");
				Preconditions.checkState(Uteis.isAtributoPreenchido(obj.getEventoDevolucaoFalta().getCodigo()), "msg_Sindicato_devolucaoFalta");
			}
			
			SindicatoVO sindicato = consultarPorCodigoDoParceiro(obj.getParceiroVO().getCodigo());	
			if(sindicato != null && !sindicato.getCodigo().equals(obj.getCodigo())) {
				throw new ConsistirException("msg_Sindicato_parceiroJaCadastrado");
			}
		} catch (Exception e) {
			throw new ConsistirException(UteisJSF.internacionalizar(e.getMessage()));
		}
	}

	@Override
	public SindicatoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario, int nivelMontarDados) throws Exception {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM sindicato WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (!tabelaResultado.next()) {
			return new SindicatoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public SindicatoVO consultarPorChavePrimaria(Long id) throws Exception {
		return consultarPorChavePrimaria(id.intValue(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		Sindicato.idEntidade = idEntidade;
	}

	public SindicatoVO consultarPorCodigoDoParceiro(Integer codigoParceiro) throws Exception {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM sindicato WHERE parceiro = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoParceiro);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA));
		}
		return new SindicatoVO();
	}

}