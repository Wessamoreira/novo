package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioAberturaRequerimentoVO;
import negocio.comuns.academico.CalendarioAberturaTipoRequerimentoraPrazoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * Classe responsável por implementar a interação entre os componentes JSF da
 * página calendarioAberturaRequerimentoCons.jsp e
 * calendarioAberturaRequerimentoForm.jsp
 * 
 * @see SuperControle
 */
@Controller("CalendarioAberturaRequerimentoControle")
@Scope("viewScope")
@Lazy
public class CalendarioAberturaRequerimentoControle extends SuperControleRelatorio implements Serializable {

    private List listaSelectItemTipoRequerimento;
    private List calendarioAberturaRequerimentoControleVOs;
    private CalendarioAberturaRequerimentoVO calendarioAberturaRequerimentoVO;
    private List listaSelectItemUnidadeEnsino;
    private String situacaoSelecionada;
    private Date dataInicoAlterarGeral;
    private Date dataFimAlterarGeral;

    public CalendarioAberturaRequerimentoControle() throws Exception {
	
    }

    public String gravar() throws Exception {
	try {
	    if (calendarioAberturaRequerimentoVO.isNovoObj().booleanValue()) {
		getFacadeFactory().getCalendarioAberturaRequerimentoFacade().incluir(calendarioAberturaRequerimentoVO, true, getUsuarioLogado());
	    } else {
		getFacadeFactory().getCalendarioAberturaRequerimentoFacade().alterar(calendarioAberturaRequerimentoVO, true, getUsuarioLogado());
	    }
	    setMensagemID("msg_dados_gravados");
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoForm.xhtml");
	} catch (Exception e) {
	    setMensagemDetalhada("msg_erro", e.getMessage());
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoForm.xhtml");
	}
    }

    public String novo() throws Exception {
	removerObjetoMemoria(this);
	montarListaTipoRequerimento();
	montarListaSelectItemUnidadeEnsino("");
	setMensagemID("msg_entre_dados");
	return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoForm.xhtml");
    }

    public String editar() throws Exception {
	CalendarioAberturaRequerimentoVO obj = (CalendarioAberturaRequerimentoVO) context().getExternalContext().getRequestMap().get("calendarioVO");
	getFacadeFactory().getCalendarioAberturaTipoRequerimentoraPrazoFacade().consultarPorCalendarioAberturaRequerimento(obj, getUsuarioLogado());
	obj.setNovoObj(Boolean.FALSE);
	setCalendarioAberturaRequerimentoVO(obj);
	montarListaTipoRequerimento();
	montarListaSelectItemUnidadeEnsino("");
	setMensagemID("msg_dados_editar");
	return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoForm.xhtml");
    }

    public String consultar() throws Exception {
	try {
	    List objs = new ArrayList(0);
	    if (getControleConsulta().getCampoConsulta().equals("codigo")) {
			if (getControleConsulta().getValorConsulta().equals("")) {
			    getControleConsulta().setValorConsulta("0");
			}
			if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
            	throw new Exception("Informe apenas valores numéricos.");
            }
			objs = getFacadeFactory().getCalendarioAberturaRequerimentoFacade().consultarPorCodigo(Integer.parseInt(getControleConsulta().getValorConsulta()), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	    }
	    if (getControleConsulta().getCampoConsulta().equals("descricao")) {
	    	objs = getFacadeFactory().getCalendarioAberturaRequerimentoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	    }
	    setListaConsulta(objs);
	    setMensagemID("msg_dados_consultados");
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoCons.xhtml");
	} catch (Exception e) {
	    setMensagemDetalhada("msg_erro", e.getMessage());
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoCons.xhtml");

	}
    }

    public String excluir() {
	try {
	    getFacadeFactory().getCalendarioAberturaRequerimentoFacade().excluir(calendarioAberturaRequerimentoVO, getUsuarioLogado());
	    novo();
	    setMensagemID("msg_dados_excluidos");
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoForm.xhtml");
	} catch (Exception e) {
	    setMensagemDetalhada("msg_erro", e.getMessage());
	    return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioAberturaRequerimentoCons.xhtml");
	}
    }

    public void montarListaTipoRequerimento() throws Exception {
	List resultadoConsulta = null;
	Iterator i = null;
	try {
	    if (getCalendarioAberturaRequerimentoVO().getNovoObj()) {
		resultadoConsulta = consultarTipoRequerimento();
	    } else {
		resultadoConsulta = consultarTipoRequerimentoSemVinculoPrazoAberturaRequerimento();
	    }
	    i = resultadoConsulta.iterator();
	    List objs = new ArrayList(0);
	    objs.add(new SelectItem(0, ""));
	    while (i.hasNext()) {
		TipoRequerimentoVO obj = (TipoRequerimentoVO) i.next();
		CalendarioAberturaTipoRequerimentoraPrazoVO aberturaTipoRequerimentoraPrazoVO = new CalendarioAberturaTipoRequerimentoraPrazoVO();
		aberturaTipoRequerimentoraPrazoVO.setTipoRequerimentoVO(obj);
		getCalendarioAberturaRequerimentoVO().getCalendarioAberturaTipoRequerimentoraPrazoVOs().add(aberturaTipoRequerimentoraPrazoVO);
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    Uteis.liberarListaMemoria(resultadoConsulta);
	    i = null;
		}
    }

    public List consultarTipoRequerimento() throws Exception {
	List lista = new ArrayList(0);
	lista = getFacadeFactory().getTipoRequerimentoFacade().consultarPorSituacao("AT", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
	return lista;
    }
    
    public List consultarTipoRequerimentoSemVinculoPrazoAberturaRequerimento() throws Exception {
	List lista = new ArrayList(0);
	lista = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoPorSituacaoESemVinculoCalendarioAbertura("AT", getCalendarioAberturaRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
	return lista;
}

    public List getListaSelectItemTipoRequerimento() {
	if (listaSelectItemTipoRequerimento == null) {
	    listaSelectItemTipoRequerimento = new ArrayList<TipoRequerimentoVO>(0);
	}
	return listaSelectItemTipoRequerimento;
    }

    public void setListaSelectItemTipoRequerimento(List listaSelectItemTipoRequerimento) {
	this.listaSelectItemTipoRequerimento = listaSelectItemTipoRequerimento;
    }

    public List getCalendarioAberturaRequerimentoControleVOs() {
	if (calendarioAberturaRequerimentoControleVOs == null) {
	    calendarioAberturaRequerimentoControleVOs = new ArrayList<CalendarioAberturaRequerimentoVO>(0);
	}
	return calendarioAberturaRequerimentoControleVOs;
    }

    public void setCalendarioAberturaRequerimentoControleVOs(List calendarioAberturaRequerimentoControleVOs) {
	this.calendarioAberturaRequerimentoControleVOs = calendarioAberturaRequerimentoControleVOs;
    }


	public CalendarioAberturaRequerimentoVO getCalendarioAberturaRequerimentoVO() {
		if (calendarioAberturaRequerimentoVO == null) {
			calendarioAberturaRequerimentoVO = new CalendarioAberturaRequerimentoVO();
		}
		return calendarioAberturaRequerimentoVO;
	}

	public void setCalendarioAberturaRequerimentoVO(CalendarioAberturaRequerimentoVO calendarioAberturaRequerimentoVO) {
		this.calendarioAberturaRequerimentoVO = calendarioAberturaRequerimentoVO;
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}
	
	public void alterarDataInicioFimGeral() throws Exception{
		if(!Uteis.isAtributoPreenchido(getDataInicoAlterarGeral())){
			throw new ConsistirException("O Campo DATA INICIO deve ser preenchido para o processamento");
		}
		if(!Uteis.isAtributoPreenchido(getDataFimAlterarGeral())){
			throw new ConsistirException("O Campo DATA FIM deve ser preenchido para o processamento");
		}
		List<CalendarioAberturaTipoRequerimentoraPrazoVO> listaAlterar = new ArrayList<CalendarioAberturaTipoRequerimentoraPrazoVO>();
		for (CalendarioAberturaTipoRequerimentoraPrazoVO obj : getCalendarioAberturaRequerimentoVO().getCalendarioAberturaTipoRequerimentoraPrazoVOs()) {
			if(obj.getSituacao().equals(getSituacaoSelecionada())){
				obj.setDataInicio(getDataInicoAlterarGeral());
				obj.setDataFim(getDataFimAlterarGeral());
				listaAlterar.add(obj);
			}
		}
		if(listaAlterar.size() > 0){
			getFacadeFactory().getCalendarioAberturaTipoRequerimentoraPrazoFacade().alterar(getCalendarioAberturaRequerimentoVO().getCodigo(), listaAlterar, getUsuarioLogado());
		}
	}
	
	public List getTipoSituacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("Aguardando Inicio", "Aguardando Inicio"));
		itens.add(new SelectItem("Vencido", "Vencido"));
		itens.add(new SelectItem("No Prazo", "No Prazo"));
		return itens;
	}

	public String getSituacaoSelecionada() {
		if(situacaoSelecionada == null){
			situacaoSelecionada = "";
		}
		return situacaoSelecionada;
	}

	public void setSituacaoSelecionada(String situacaoSelecionada) {
		this.situacaoSelecionada = situacaoSelecionada;
	}

	public Date getDataInicoAlterarGeral() {
		return dataInicoAlterarGeral;
	}

	public void setDataInicoAlterarGeral(Date dataInicoAlterarGeral) {
		this.dataInicoAlterarGeral = dataInicoAlterarGeral;
	}

	public Date getDataFimAlterarGeral() {
		return dataFimAlterarGeral;
	}

	public void setDataFimAlterarGeral(Date dataFimAlterarGeral) {
		this.dataFimAlterarGeral = dataFimAlterarGeral;
	}
	
}
