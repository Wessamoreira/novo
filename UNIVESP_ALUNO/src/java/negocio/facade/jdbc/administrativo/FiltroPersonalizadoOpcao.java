package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FiltroPersonalizadoOpcaoInterfaceFacade;

@SuppressWarnings({"unchecked","static-access", "rawtypes"})
@Scope("singleton")
@Repository
@Lazy
public class FiltroPersonalizadoOpcao extends ControleAcesso implements FiltroPersonalizadoOpcaoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;
	
	public void validarDados(FiltroPersonalizadoOpcaoVO obj, UsuarioVO usuarioVO) throws Exception {
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FiltroPersonalizadoOpcaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			incluir(obj, "filtroPersonalizadoOpcao", new AtributoPersistencia()
					.add("filtroPersonalizado", obj.getFiltroPersonalizadoVO().getCodigo())
					.add("descricaoOpcao", obj.getDescricaoOpcao())
					.add("keyOpcao", obj.getKeyOpcao())
					.add("selecionado", obj.getSelecionado())
					.add("campoQuery", obj.getCampoQuery())
					.add("ordem", obj.getOrdem()), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FiltroPersonalizadoOpcaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			alterar(obj, "filtroPersonalizadoOpcao", new AtributoPersistencia()
					.add("filtroPersonalizado", obj.getFiltroPersonalizadoVO().getCodigo())
					.add("descricaoOpcao", obj.getDescricaoOpcao())
					.add("keyOpcao", obj.getKeyOpcao())
					.add("selecionado", obj.getSelecionado())
					.add("campoQuery", obj.getCampoQuery())
					.add("ordem", obj.getOrdem()),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void incluirFiltroPersonalizadoOpcoes(FiltroPersonalizadoVO obj, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : listaFiltroPersonalizadoVOs) {
			filtroPersonalizadoOpcaoVO.setFiltroPersonalizadoVO(obj);
			incluir(filtroPersonalizadoOpcaoVO, false, usuarioVO);
		}
	}
	
	@Override
	public void alterarFiltroPersonalizadoOpcoes(FiltroPersonalizadoVO obj, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		excluirFiltroPersonalizadoOpcoes(obj, listaFiltroPersonalizadoVOs, usuarioVO);
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : listaFiltroPersonalizadoVOs) {
			filtroPersonalizadoOpcaoVO.setFiltroPersonalizadoVO(obj);
			if (!Uteis.isAtributoPreenchido(filtroPersonalizadoOpcaoVO.getCodigo())) {
				incluir(filtroPersonalizadoOpcaoVO, false, usuarioVO);
			} else {
				alterar(filtroPersonalizadoOpcaoVO, false, usuarioVO);
			}
		}
	}
	
	public void excluirFiltroPersonalizadoOpcoes(FiltroPersonalizadoVO obj, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from filtroPersonalizadoOpcao where filtroPersonalizado = ").append(obj.getCodigo());
		sb.append(" and filtroPersonalizadoOpcao.codigo not in (");
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcoesVO : listaFiltroPersonalizadoVOs) {
			sb.append(filtroPersonalizadoOpcoesVO.getCodigo()).append(", ");
		}
		sb.append("0)");
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	@Override
	public void excluir(FiltroPersonalizadoOpcaoVO obj, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from filtroPersonalizadoOpcao where codigo = ? ");
		getConexao().getJdbcTemplate().update(sb.toString(), new Object[] {obj.getCodigo()});
	}
	
	public StringBuilder getSQLConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		return sb;
	}
	
	@Override
	public List<FiltroPersonalizadoOpcaoVO> consultarPorFiltroPersonalizado(Integer filtroPersonalizado, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from filtroPersonalizadoOpcao where filtroPersonalizado = ? ");
		List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcoesVOs = new ArrayList<FiltroPersonalizadoOpcaoVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] {filtroPersonalizado});
		while (tabelaResultado.next()) {
			FiltroPersonalizadoOpcaoVO obj = new FiltroPersonalizadoOpcaoVO();
			montarDadosCompleto(obj, tabelaResultado, usuarioVO);
			listaFiltroPersonalizadoOpcoesVOs.add(obj);
		}
		return listaFiltroPersonalizadoOpcoesVOs;
	}
	
	public void montarDadosCompleto(FiltroPersonalizadoOpcaoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getFiltroPersonalizadoVO().setCodigo(dadosSQL.getInt("filtroPersonalizado"));
		obj.setDescricaoOpcao(dadosSQL.getString("descricaoOpcao"));
		obj.setKeyOpcao(dadosSQL.getString("keyOpcao"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.setSelecionado(dadosSQL.getBoolean("selecionado"));
		obj.setCampoQuery(dadosSQL.getString("campoQuery"));
	}
	
	@Override
	public void adicionarFiltroPersonalizadoOpcao(FiltroPersonalizadoOpcaoVO objAdicionar, FiltroPersonalizadoVO filtroPersonalizadoVO, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs, UsuarioVO usuarioVO) throws Exception {
		if (objAdicionar.getDescricaoOpcao().equals("")) {
			throw new Exception("O campo DESCRIÇÃO OPÇÃO deve ser informado");
		}
		if (objAdicionar.getKeyOpcao().equals("") && !filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			throw new Exception("O campo KEY OPÇÃO deve ser informado");
		}
		if (objAdicionar.getCampoQuery().equals("") && filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			throw new Exception("O campo QUERY deve ser informado");
		}
		if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.SIMPLES_ESCOLHA)) {
			validarDadosSimplesEscolhaSelecionado(objAdicionar, listaFiltroPersonalizadoOpcaoVOs, usuarioVO);
		}
		int index = 0;
		objAdicionar.setFiltroPersonalizadoVO(filtroPersonalizadoVO);
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : listaFiltroPersonalizadoOpcaoVOs) {
			if (filtroPersonalizadoOpcaoVO.getDescricaoOpcao().equals(objAdicionar.getDescricaoOpcao()) 
					&& filtroPersonalizadoOpcaoVO.getKeyOpcao().equals(objAdicionar.getKeyOpcao())) {
				listaFiltroPersonalizadoOpcaoVOs.set(index, objAdicionar);
				return;
			}
			index++;
		}
		listaFiltroPersonalizadoOpcaoVOs.add(objAdicionar);
		realizaReorganizacaoOrdemFiltroOpcao(listaFiltroPersonalizadoOpcaoVOs);
	}
	
	public void validarDadosSimplesEscolhaSelecionado(FiltroPersonalizadoOpcaoVO objAdicionar, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs, UsuarioVO usuarioVO) throws Exception {
		if (!objAdicionar.getSelecionado()) {
			return;
		}
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : listaFiltroPersonalizadoOpcaoVOs) {
			if (filtroPersonalizadoOpcaoVO.getSelecionado() && !objAdicionar.getKeyOpcao().equals(filtroPersonalizadoOpcaoVO.getKeyOpcao())) {
				objAdicionar.setSelecionado(false);
				throw new Exception("A opção "+filtroPersonalizadoOpcaoVO.getDescricaoOpcao()+" já se encontra selecionada. Não é possível selecionar duas opções para o tipo campo Simples Escolha.");
			}
		}
	}
	
	
	
	@Override
	public void removerFiltroPersonalizadoOpcao(FiltroPersonalizadoOpcaoVO objRemover, List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs, UsuarioVO usuarioVO) {
		int index = 0;
		for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : listaFiltroPersonalizadoOpcaoVOs) {
			if (filtroPersonalizadoOpcaoVO.getDescricaoOpcao().equals(objRemover.getDescricaoOpcao())) {
				listaFiltroPersonalizadoOpcaoVOs.remove(index);
				break;
			}
			index++;
		}
		
	}
	
	public void realizaReorganizacaoOrdemFiltroOpcao(List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs){
    	int index = 1;
    	for(FiltroPersonalizadoOpcaoVO objExistente: listaFiltroPersonalizadoOpcaoVOs){
            objExistente.setOrdem(index++);                       
        }
    }
	
	public static String getIdEntidade() {
		return FiltroPersonalizadoOpcao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FiltroPersonalizadoOpcao.idEntidade = idEntidade;
	}
	
	@Override
	public String consultarCampoQueryPorCodigo(Integer codigo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT campoQuery from filtroPersonalizadoOpcao where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("campoQuery");
		}
		return "";
	}
	
	
}
