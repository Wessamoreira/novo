package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.enumeradores.OrigemFiltroPersonalizadoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FiltroPersonalizadoInterfaceFacade;

@SuppressWarnings({"unchecked","static-access", "rawtypes"})
@Scope("singleton")
@Repository
@Lazy
public class FiltroPersonalizado extends ControleAcesso implements FiltroPersonalizadoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;
	
	public void validarDados(FiltroPersonalizadoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTituloCampo().equals("")) {
			throw new Exception("O campo TÍTULO CAMPO deve ser informado.");
		}
		if (obj.getCampoQuery().equals("")) {
			throw new Exception("O campo QUERY deve ser informado.");
		}
		if (obj.getCodigoOrigem().equals(0)) {
			throw new Exception("O campo CÓDIGO ORIGEM deve ser informado.");
		}
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.TEXTUAL) 
				|| obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.INTEIRO) 
				|| obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.DATA)
				|| obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.BOOLEAN)
				|| (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && !obj.getComboboxManual())
				) {
			obj.getListaFiltroPersonalizadoOpcaoVOs().clear();
		}
		if (obj.getComboboxManual()) {
			obj.setCampoQueryListaCombobox("");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FiltroPersonalizadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			FiltroPersonalizado.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			incluir(obj, "filtroPersonalizado", new AtributoPersistencia()
					.add("tituloCampo", obj.getTituloCampo())
					.add("campoQuery", obj.getCampoQuery())
					.add("campoQueryListaCombobox", obj.getCampoQueryListaCombobox())
					.add("tipoCampoFiltro", obj.getTipoCampoFiltro().toString())
					.add("origem", obj.getOrigem())
					.add("ordem", obj.getOrdem())
					.add("coluna", obj.getColuna())
					.add("comboboxManual", obj.getComboboxManual())
					.add("obrigatorio", obj.getObrigatorio())
					.add("codigoOrigem", obj.getCodigoOrigem()), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().incluirFiltroPersonalizadoOpcoes(obj, obj.getListaFiltroPersonalizadoOpcaoVOs(), usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FiltroPersonalizadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			FiltroPersonalizado.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			alterar(obj, "filtroPersonalizado", new AtributoPersistencia()
					.add("tituloCampo", obj.getTituloCampo())
					.add("campoQuery", obj.getCampoQuery())
					.add("campoQueryListaCombobox", obj.getCampoQueryListaCombobox())
					.add("tipoCampoFiltro", obj.getTipoCampoFiltro().toString())
					.add("origem", obj.getOrigem())
					.add("ordem", obj.getOrdem())
					.add("coluna", obj.getColuna())
					.add("comboboxManual", obj.getComboboxManual())
					.add("obrigatorio", obj.getObrigatorio())
					.add("codigoOrigem", obj.getCodigoOrigem()),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
			getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().alterarFiltroPersonalizadoOpcoes(obj, obj.getListaFiltroPersonalizadoOpcaoVOs(), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirFiltroPersonalizado(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			filtroPersonalizadoVO.setCodigoOrigem(codigoOrigem);
			filtroPersonalizadoVO.setOrigem(origem);
			incluir(filtroPersonalizadoVO, false, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFiltroPersonalizado(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		excluirFiltroPersonalizado(codigoOrigem, origem, listaFiltroPersonalizadoVOs, usuarioVO);
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			filtroPersonalizadoVO.setCodigoOrigem(codigoOrigem);
			filtroPersonalizadoVO.setOrigem(origem);
			if (!Uteis.isAtributoPreenchido(filtroPersonalizadoVO.getCodigo())) {
				incluir(filtroPersonalizadoVO, false, usuarioVO);
			} else {
				alterar(filtroPersonalizadoVO, false, usuarioVO);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFiltroPersonalizado(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from filtroPersonalizado where codigoOrigem = ").append(codigoOrigem).append(" and origem = '").append(origem).append("' ");
		sb.append(" and filtroPersonalizado.codigo not in (");
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			sb.append(filtroPersonalizadoVO.getCodigo()).append(", ");
		}
		sb.append("0)");
		getConexao().getJdbcTemplate().update(sb.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFiltroPersonalizadoPorCodigoOrigemEOrigem(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from filtroPersonalizado where codigoOrigem = ").append(codigoOrigem).append(" and origem = '").append(origem).append("' ");
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	@Override
	public List<FiltroPersonalizadoVO> consultarPorOrigemCodigoOrigem(Integer codigoOrigem, OrigemFiltroPersonalizadoEnum origem, UsuarioVO usuarioVO) {
		StringBuilder sb = getSQLConsultaCompleta();
		sb.append(" where filtroPersonalizado.codigoOrigem = ").append(codigoOrigem);
		sb.append(" and filtroPersonalizado.origem = '").append(origem.toString()).append("' ");
		sb.append(" order by filtroPersonalizado.ordem,  filtroPersonalizado.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs = new java.util.ArrayList<FiltroPersonalizadoVO>(0);
		while (tabelaResultado.next()) {
			FiltroPersonalizadoVO obj = new FiltroPersonalizadoVO();
			montarDadosCompleto(obj, tabelaResultado, usuarioVO);
			listaFiltroPersonalizadoVOs.add(obj);
		}
		return listaFiltroPersonalizadoVOs;
	}
	
	public StringBuilder getSQLConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		sb.append("select filtroPersonalizado.* ");
		sb.append(" from filtroPersonalizado ");
		return sb;
	}
	
	public void montarDadosCompleto(FiltroPersonalizadoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setTituloCampo(dadosSQL.getString("tituloCampo"));
		obj.setCampoQuery(dadosSQL.getString("campoQuery"));
		obj.setCampoQueryListaCombobox(dadosSQL.getString("campoQueryListaCombobox"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.setColuna(dadosSQL.getInt("coluna"));
		obj.setComboboxManual(dadosSQL.getBoolean("comboboxManual"));
		obj.setObrigatorio(dadosSQL.getBoolean("obrigatorio"));
		if (dadosSQL.getString("tipoCampoFiltro") != null) {
			obj.setTipoCampoFiltro(TipoCampoFiltroEnum.valueOf(dadosSQL.getString("tipoCampoFiltro")));
		}
		if (dadosSQL.getString("origem") != null) {
			obj.setOrigem(OrigemFiltroPersonalizadoEnum.valueOf(dadosSQL.getString("origem")));
		}
		obj.setCodigoOrigem(dadosSQL.getInt("codigoOrigem"));
		obj.setListaFiltroPersonalizadoOpcaoVOs(getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().consultarPorFiltroPersonalizado(obj.getCodigo(), usuarioVO));
		
	}
	
	
	@Override
	public void adicionarFiltroPersonalizado(FiltroPersonalizadoVO objAdicionar, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		if (objAdicionar.getTituloCampo().equals("")) {
			throw new Exception("O campo TEXTO deve ser informado.");
		}
		if (objAdicionar.getCampoQuery().equals("") && !objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			throw new Exception("O campo QUERY deve ser informado.");
		}
		validarDadosComboBoxCustomizavelAdicionada(objAdicionar, listaFiltroPersonalizadoVOs, usuarioVO);
		validarDadosCombobox(objAdicionar, usuarioVO);
		int index = 0;
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			if (filtroPersonalizadoVO.getTituloCampo().equals(objAdicionar.getTituloCampo())
					&& filtroPersonalizadoVO.getTipoCampoFiltro().equals(objAdicionar.getTipoCampoFiltro())) {
				if (objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
					inicializarDadosComboBoxCustomizavel(objAdicionar, false, usuarioVO);
				}
				if (objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX)) {
					inicializarDadosCombobox(objAdicionar, usuarioVO);
				}
				listaFiltroPersonalizadoVOs.set(index, objAdicionar);
				return;
			}
			index++;
		}
		if (objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			inicializarDadosComboBoxCustomizavel(objAdicionar, false, usuarioVO);
		}
		if (objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && !objAdicionar.getComboboxManual()) {
			inicializarDadosCombobox(objAdicionar, usuarioVO);
		}
		listaFiltroPersonalizadoVOs.add(objAdicionar);
		realizaReorganizacaoOrdemFiltroPersonalizado(listaFiltroPersonalizadoVOs);
	}
	
	public void realizaReorganizacaoOrdemFiltroPersonalizado(List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs){
    	int index = 1;
    	for(FiltroPersonalizadoVO objExistente: listaFiltroPersonalizadoVOs){
            objExistente.setOrdem(index++);                       
        }
    }
	
	public void validarDadosCombobox(FiltroPersonalizadoVO objAdicionar, UsuarioVO usuarioVO) throws Exception {
		if (!objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX)) {
			return;
		}
		if (!objAdicionar.getComboboxManual() && objAdicionar.getCampoQueryListaCombobox().equals("")) {
			throw new Exception("O campo QUERY LISTA COMBOBOX deve ser informado.");
		}
		if (objAdicionar.getComboboxManual() && objAdicionar.getListaFiltroPersonalizadoOpcaoVOs().isEmpty()) {
			throw new Exception("Deve-se adicionar um Filtro Opção para inluir uma combobox manual.");
		}
		
	}
	
	public void validarDadosComboBoxCustomizavelAdicionada(FiltroPersonalizadoVO objAdicionar, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) throws Exception {
		if (!objAdicionar.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			return;
		}
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL) && filtroPersonalizadoVO.getTituloCampo().trim().equals(objAdicionar.getTituloCampo().trim()) && !filtroPersonalizadoVO.getEditando()) {
				throw new Exception("Já existe um Filtro Personalizado do Tipo COMBOBOX CUSTOMIZÁVEL com esse título, por favor realize a alteração do título para prosseguir.");
			}
		}
	}
	
	@Override
	public void inicializarDadosCombobox(FiltroPersonalizadoVO obj, UsuarioVO usuarioVO) {
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && !obj.getComboboxManual()) {
			obj.setListaSelectItemComboboxVOs(consultarQueryCombobox(obj.getCampoQueryListaCombobox(), usuarioVO));
		} else if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && obj.getComboboxManual()) {
			obj.getListaSelectItemComboboxVOs().clear();
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem("", ""));
			for(FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : obj.getListaFiltroPersonalizadoOpcaoVOs()) {
				itens.add(new SelectItem(filtroPersonalizadoOpcaoVO.getKeyOpcao(), filtroPersonalizadoOpcaoVO.getDescricaoOpcao()));
			}
			obj.setListaSelectItemComboboxVOs(itens);
		}
		
	}
	
	public List<SelectItem> consultarQueryCombobox(String query, UsuarioVO usuarioVO) {
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(query);
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			Integer key = tabelaResultado.getInt("key");
			itens.add(new SelectItem(key.toString(), tabelaResultado.getString("descricao")));
		}
		return itens;
	}
	
	
	@Override
	public void inicializarDadosComboBoxCustomizavel(FiltroPersonalizadoVO obj, Boolean utilizarCodigoComoKey, UsuarioVO  usuarioVO) {
		if (!obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
			return;
		}
		obj.getListaSelectItemComboboxCustomizavelVOs().clear();
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		for(FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : obj.getListaFiltroPersonalizadoOpcaoVOs()) {
			if (utilizarCodigoComoKey) {
				itens.add(new SelectItem(filtroPersonalizadoOpcaoVO.getCodigo().toString(), filtroPersonalizadoOpcaoVO.getDescricaoOpcao()));
			} else {
				itens.add(new SelectItem(filtroPersonalizadoOpcaoVO.getOrdem().toString(), filtroPersonalizadoOpcaoVO.getDescricaoOpcao()));
			}
			
		}
		obj.setListaSelectItemComboboxCustomizavelVOs(itens);
	}
	
	@Override
	public void removerFiltroPersonalizado(FiltroPersonalizadoVO objRemover, List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs, UsuarioVO usuarioVO) {
		int index = 0;
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : listaFiltroPersonalizadoVOs) {
			if (filtroPersonalizadoVO.getTituloCampo().equals(objRemover.getTituloCampo())
					&& filtroPersonalizadoVO.getTipoCampoFiltro().equals(objRemover.getTipoCampoFiltro())
					&& filtroPersonalizadoVO.getCampoQuery().equals(objRemover.getCampoQuery())) {
				listaFiltroPersonalizadoVOs.remove(index);
				break;
			}
			index++;
		}
	}
	
	 @Override
     public void alterarOrdemFiltroOpcao(FiltroPersonalizadoVO filtroPersonalizadoVO, FiltroPersonalizadoOpcaoVO filtroOpcaoVO1, FiltroPersonalizadoOpcaoVO filtroOpcaoVO2) throws Exception {
         int ordem1 = filtroOpcaoVO1.getOrdem();
         filtroOpcaoVO1.setOrdem(filtroOpcaoVO2.getOrdem());
         filtroOpcaoVO2.setOrdem(ordem1);        
         Ordenacao.ordenarLista(filtroPersonalizadoVO.getListaFiltroPersonalizadoOpcaoVOs(), "ordem");
     }
	
	public static String getIdEntidade() {
		return FiltroPersonalizado.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FiltroPersonalizado.idEntidade = idEntidade;
	}


}
