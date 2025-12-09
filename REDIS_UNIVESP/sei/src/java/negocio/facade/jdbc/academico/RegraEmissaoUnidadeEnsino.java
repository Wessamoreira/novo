package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.recursoshumanos.Rescisao;
import negocio.interfaces.academico.RegraEmissaoUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegraEmissaoUnidadeEnsino extends SuperFacade<RegraEmissaoUnidadeEnsinoVO> implements RegraEmissaoUnidadeEnsinoInterfaceFacade<RegraEmissaoUnidadeEnsinoVO> {

	

	private static final long serialVersionUID = 718471023634236305L;

	private static final String REGRAEMISSAOUNIDADEENSINO = "RegraEmissaoUnidadeEnsino";

	protected static String idEntidade;

	public RegraEmissaoUnidadeEnsino() {
		super();
		setIdEntidade(REGRAEMISSAOUNIDADEENSINO);
	}
	
	@Override
	public void persistir(List<RegraEmissaoUnidadeEnsinoVO> lista, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		for(RegraEmissaoUnidadeEnsinoVO obj : lista) {
			if(obj.getCodigo() == 0) {
				incluir(obj, validarAcesso, usuarioVO);
			}else {
				alterar(obj, validarAcesso, usuarioVO);
			}
		}
	}
	
	
	@Override
	public void incluir(RegraEmissaoUnidadeEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, REGRAEMISSAOUNIDADEENSINO,
				new AtributoPersistencia()
						.add("regraEmissao", obj.getRegraEmissaoVO().getCodigo())
						.add("unidadeEnsino", obj.getUnidadeEnsinoVO().getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);

	}
	
	@Override
	public void alterar(RegraEmissaoUnidadeEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		
		alterar(obj, REGRAEMISSAOUNIDADEENSINO, 
				new AtributoPersistencia()
					.add("regraEmissao", obj.getRegraEmissaoVO().getCodigo())
					.add("unidadeEnsino", obj.getUnidadeEnsinoVO().getCodigo()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}
	


	@Override
	public void excluir(RegraEmissaoUnidadeEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		RegraEmissaoUnidadeEnsino.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM RegraEmissaoUnidadeEnsino WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}
	
	@Override
	public RegraEmissaoUnidadeEnsinoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("Select *from RegraEmissaoUnidadeEnsino WHERE codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	@Override
	public List<RegraEmissaoUnidadeEnsinoVO> consultarRegraEmissaoUnidadePorRegraEmissao(RegraEmissaoVO regraEmissaoVO, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		RegraEmissaoUnidadeEnsino.consultar(getIdEntidade());
		List<RegraEmissaoUnidadeEnsinoVO> objetos = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" Select  regraemissaounidadeensino.codigo, unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\"from regraemissaounidadeensino ");
		sql.append(" INNER JOIN unidadeensino  on regraemissaounidadeensino.unidadeensino = unidadeensino.codigo ");
		sql.append(" WHERE  regraemissaounidadeensino.regraemissao = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),regraEmissaoVO.getCodigo());
		while (tabelaResultado.next()) {
			RegraEmissaoUnidadeEnsinoVO novoObj = new RegraEmissaoUnidadeEnsinoVO();
			novoObj = montarDados(tabelaResultado,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, regraEmissaoVO);
			objetos.add(novoObj);
		}
		return objetos;
	}
	
	@Override
	public void excluirRegraEmissaoUnidadeEnsino(List<RegraEmissaoUnidadeEnsinoVO> emissaoUnidadeEnsinoVOs, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		for(RegraEmissaoUnidadeEnsinoVO regraEmissaoUnidadeEnsinoVO : emissaoUnidadeEnsinoVOs ) {
			if(regraEmissaoUnidadeEnsinoVO.getCodigo() != 0) {
				RegraEmissaoUnidadeEnsino.excluir(getIdEntidade());
				StringBuilder sql = new StringBuilder("DELETE FROM regraemissaounidadeensino WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sql.toString(), regraEmissaoUnidadeEnsinoVO.getCodigo());
			}
		}
	}
	
	
	public RegraEmissaoUnidadeEnsinoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, RegraEmissaoVO regraEmissao) throws Exception {
		RegraEmissaoUnidadeEnsinoVO obj = new RegraEmissaoUnidadeEnsinoVO();
		UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		
		unidadeEnsinoVO.setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
		unidadeEnsinoVO.setNome(tabelaResultado.getString("unidadeensino.nome"));
		
		obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
		obj.setRegraEmissaoVO(regraEmissao);
		
		return obj;
	}
	
	@Override
	public void validarDados(RegraEmissaoUnidadeEnsinoVO obj) throws ConsistirException {
		
	}
	
	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		RegraEmissaoUnidadeEnsino.idEntidade = idEntidade;
	}
	
	@Override
	public List<RegraEmissaoUnidadeEnsinoVO> consultarRegraEmissaoUnidadeEnsinoPorRegraEmissaoCodigo(Integer codigo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public RegraEmissaoUnidadeEnsinoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
