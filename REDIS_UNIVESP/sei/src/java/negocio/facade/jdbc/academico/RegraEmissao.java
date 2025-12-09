package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.recursoshumanos.Rescisao;
import negocio.interfaces.academico.RegraEmissaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegraEmissao extends SuperFacade<RegraEmissaoVO> implements RegraEmissaoInterfaceFacade<RegraEmissaoVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6396714329006506614L;
	private static final String REGRAEMISSAO = "RegraEmissao";

	protected static String idEntidade;
	
	public RegraEmissao() {
		super();
		setIdEntidade(REGRAEMISSAO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(RegraEmissaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		//validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
		getFacadeFactory().getRegraEmissaoUnidadeEnsinoInterfaceFacade().persistir(obj.getRegraEmissaoUnidadeEnsinoVOs(), validarAcesso, usuarioVO);
	}
	

	@Override
	public void persistir(List<RegraEmissaoVO> lista, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		for(RegraEmissaoVO obj : lista) {
			if(obj.getCodigo() == 0) {
				incluir(obj, validarAcesso, usuarioVO);
			}else {
				alterar(obj, validarAcesso, usuarioVO);
			}
	    	getFacadeFactory().getRegraEmissaoUnidadeEnsinoInterfaceFacade().persistir(obj.getRegraEmissaoUnidadeEnsinoVOs(), validarAcesso, usuarioVO);	 
		}
	}

	@Override
	public void incluir(RegraEmissaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, REGRAEMISSAO,
				new AtributoPersistencia()
						.add("nivelEducacional", obj.getNivelEducacional())
						.add("validarMatrizCurricularIntegralizado", obj.getValidarMatrizCurricularIntegralizado())
						.add("validarNotaTCC", obj.getValidarNotaTCC())
						.add("validarDocumentosEntregues",obj.getValidarDocumentosEntregues())
						.add("notaTCC",obj.getNotaTCC())
						.add("tipoContrato", obj.getTipoContrato()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}
	
	
	@Override
	public void alterar(RegraEmissaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, REGRAEMISSAO, 
				new AtributoPersistencia()
					.add("nivelEducacional", obj.getNivelEducacional())
					.add("validarMatrizCurricularIntegralizado", obj.getValidarMatrizCurricularIntegralizado())
					.add("validarNotaTCC", obj.getValidarNotaTCC())
					.add("validarDocumentosEntregues", obj.getValidarDocumentosEntregues())
					.add("notaTCC",obj.getNotaTCC())
					.add("tipoContrato", obj.getTipoContrato()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(RegraEmissaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getFacadeFactory().getRegraEmissaoUnidadeEnsinoInterfaceFacade().excluirRegraEmissaoUnidadeEnsino(obj.getRegraEmissaoUnidadeEnsinoVOs(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM RegraEmissao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public RegraEmissaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append("Select *from RegraEmissao WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	@Override
	public List<RegraEmissaoVO> consultarTodasRegrasEmissao(UsuarioVO usuario) throws Exception {
		List<RegraEmissaoVO> objetos = new ArrayList<>();
		StringBuilder sql = new StringBuilder();	
		sql.append("Select *from regraemissao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			RegraEmissaoVO novoObj = new RegraEmissaoVO();
			novoObj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Override
	public RegraEmissaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		RegraEmissaoVO obj = new RegraEmissaoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
		obj.setValidarMatrizCurricularIntegralizado(tabelaResultado.getBoolean("validarMatrizCurricularIntegralizado"));
		obj.setValidarNotaTCC(tabelaResultado.getBoolean("validarNotaTCC"));
		obj.setValidarDocumentosEntregues(tabelaResultado.getBoolean("tipoContrato"));
		obj.setNotaTCC(tabelaResultado.getDouble("notaTCC"));
		obj.setRegraEmissaoUnidadeEnsinoVOs(new ArrayList<RegraEmissaoUnidadeEnsinoVO>());
		return obj;
	}
	
	public RegraEmissaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegraEmissaoVO obj = new RegraEmissaoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
		obj.setValidarMatrizCurricularIntegralizado(tabelaResultado.getBoolean("validarMatrizCurricularIntegralizado"));
		obj.setValidarNotaTCC(tabelaResultado.getBoolean("validarNotaTCC"));
		obj.setValidarDocumentosEntregues(tabelaResultado.getBoolean("validarDocumentosEntregues"));
		obj.setNotaTCC(tabelaResultado.getDouble("notaTCC"));
		obj.setTipoContrato(tabelaResultado.getString("tipoContrato"));
		obj.setRegraEmissaoUnidadeEnsinoVOs(getFacadeFactory().getRegraEmissaoUnidadeEnsinoInterfaceFacade().consultarRegraEmissaoUnidadePorRegraEmissao(obj, false, usuario));
		return obj;
	}
	
	@Override
	public List<RegraEmissaoVO> adicionarRegraEmissaoVOs(List<RegraEmissaoVO> lista, RegraEmissaoVO obj) throws Exception {
		validarDados(obj);
		
		if(lista != null) {
			if(!obj.getNivelEducacional().equals("PO")) {
				int cont = 0;
				for(RegraEmissaoVO regraEmissao : lista) {
					if(regraEmissao.getNivelEducacional().equals(obj.getNivelEducacional())) {
						lista.set(cont, obj);
						return lista;
					}
					cont ++;
				}
			}else {
				int cont = 0;
				for(RegraEmissaoVO regraEmissao : lista) {
					if(regraEmissao.getNivelEducacional().equals(obj.getNivelEducacional()) && regraEmissao.getTipoContrato().equals(obj.getTipoContrato())) {
						lista.set(cont, obj);
						return lista;
					}
					cont ++;
				}
			}
		}
		lista.add(obj);
		return lista;
	}
	

	@Override
	public void adicionarUnidadeEnsino(RegraEmissaoUnidadeEnsinoVO regraUnidadeEnsino, RegraEmissaoVO re) throws Exception {
		/*
		 * if(revs != null) { for(RegraEmissaoVO obj: revs) {
		 * if(obj.getNivelEducacional().equals(re.getNivelEducacional())){ return; } } }
		 */
		re.getRegraEmissaoUnidadeEnsinoVOs().add(regraUnidadeEnsino);
	}
	
	@Override
	public void removerRegraEmissao(RegraEmissaoUnidadeEnsinoVO objRegraEmissaoUnidadeEnsino, RegraEmissaoVO obj) throws Exception {
		Iterator<RegraEmissaoUnidadeEnsinoVO> listaRegraEmissaoUnidade = obj.getRegraEmissaoUnidadeEnsinoVOs().iterator();
		
		while(listaRegraEmissaoUnidade.hasNext()) {
			RegraEmissaoUnidadeEnsinoVO regraUnidadeExistente = listaRegraEmissaoUnidade.next();
			if(regraUnidadeExistente.getUnidadeEnsinoVO().getCodigo().equals(objRegraEmissaoUnidadeEnsino.getUnidadeEnsinoVO().getCodigo())) {
				listaRegraEmissaoUnidade.remove();
			}
		}
	}
	
	
	
	@Override
	public void validarDados(RegraEmissaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getRegraEmissaoUnidadeEnsinoVOs())) {
			throw new ConsistirException(UteisJSF.internacionalizar("Selecione pelo menos uma unidade de ensino!"));
		}

		/*
		 * if (!Uteis.isAtributoPreenchido(obj.getValidarNotaTCC())) { throw new
		 * ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_tipoAfastamento")
		 * ); }
		 * 
		 * if (!Uteis.isAtributoPreenchido(obj.getValidarDocumentosEntregues())) { throw
		 * new ConsistirException(UteisJSF.internacionalizar(
		 * "msg_Rescisao_motivoAfastamento")); }
		 * 
		 * if (!Uteis.isAtributoPreenchido(obj.getTipoContrato())) { throw new
		 * ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_dataInicio")); }
		 */
	}


	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		RegraEmissao.idEntidade = idEntidade;
	}
}
