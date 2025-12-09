/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutComprovanteEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("ComprovanteInscricaoRelControle")
@Scope("viewScope")
public class ComprovanteInscricaoRelControle extends SuperControleRelatorio {

    private InscricaoVO inscricaoVO;
    private List listaSelectItemProcessoSeletivo;
    private String campoConsultaCandidato;
    private String valorConsultaCandidato;
    private List listaConsultaCandidato;
    private List<InscricaoVO> listaInscricoes;
    private String layout;
    private Boolean declararCursoEscolaPublica;

    public ComprovanteInscricaoRelControle() {
        montarListaSelectItemProcessoSeletivo();
    }

    public void imprimirPDF() {
        String design = null;
        String titulo = null;
        List listaObjetos = new ArrayList(0);
        getListaInscricoes().clear();
        try {
            design = getFacadeFactory().getComprovanteInscricaoRelFacade().designIReportRelatorio(getInscricaoVO().getProcSeletivo().getTipoLayoutComprovante().name());
            titulo = "INSCRIÇÃO PARA O PROCESSO SELETIVO";
            getFacadeFactory().getComprovanteInscricaoRelFacade().validarDadosPesquisa(getInscricaoVO());
            getInscricaoVO().setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getInscricaoVO().getProcSeletivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            if (getInscricaoVO().getCodigo() != 0) {
                getListaInscricoes().add(getInscricaoVO());
                listaObjetos = getFacadeFactory().getComprovanteInscricaoRelFacade().preencherDadosComprovanteInscricao(getListaInscricoes(), getUsuarioLogado());
            } else {
                getFacadeFactory().getComprovanteInscricaoRelFacade().validarDadosPesquisa(getInscricaoVO());
                setListaInscricoes(getFacadeFactory().getInscricaoFacade().consultarPorCodigoProcSeletivoCodigoAlunoComprovanteInscricao(getInscricaoVO().getProcSeletivo().getCodigo(), getInscricaoVO().getCandidato().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                listaObjetos = getFacadeFactory().getComprovanteInscricaoRelFacade().preencherDadosComprovanteInscricao(getListaInscricoes(), getUsuarioLogado());
            }
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovanteInscricaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().adicionarParametro("declararCursoEscolaPublica", getDeclararCursoEscolaPublica());
                getSuperParametroRelVO().adicionarParametro("apresentarCampoDataProva", !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("PS"));
                if (!getInscricaoVO().getUnidadeEnsino().getCodigo().equals(0)) {
                	getInscricaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getInscricaoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                	if (!getInscricaoVO().getUnidadeEnsino().getCaminhoBaseLogoRelatorio().equals("") && !getInscricaoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio().equals("")) {
                		getSuperParametroRelVO().adicionarParametro("logoPadraoUnidadeEnsino",getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getInscricaoVO().getUnidadeEnsino().getCaminhoBaseLogoRelatorio() + File.separator + getInscricaoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio());
                	} else { 
                		//getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getInscricaoVO().getUnidadeEnsino().getCaminhoBaseLogoRelatorio() + File.separator + getInscricaoVO().getUnidadeEnsino().getNomeArquivoLogoRelatorio());
                		getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getInscricaoVO().getUnidadeEnsino());
                	}
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemProcessoSeletivo();
            } else { 
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void limparCandidato() throws Exception {
        try {
            getInscricaoVO().setCandidato(null);
        } catch (Exception e) {
        }
    }

    public void montarListaSelectItemProcessoSeletivo() {
        try {
            montarListaSelectItemProcessoSeletivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemProcessoSeletivo(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(prm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            Ordenacao.ordenarListaDecrescente(resultadoConsulta, "dataInicio");
            
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            setListaSelectItemProcessoSeletivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void consultarCandidato() {
        try {
            List objs = new ArrayList(0);
            getFacadeFactory().getComprovanteInscricaoRelFacade().validarDadosPesquisa(getInscricaoVO());
            if (getCampoConsultaCandidato().equals("nome")) {
//                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaCandidato(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            	objs = getFacadeFactory().getPessoaFacade().consultarPorNomeEProcessoSeletivo(getValorConsultaCandidato(), getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCandidato().equals("CPF")) {
//                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaCandidato(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPFEProcessoSeletivo(getValorConsultaCandidato(), getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCandidato().equals("RG")) {
//                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaCandidato(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                objs = getFacadeFactory().getPessoaFacade().consultarPorRGEProcessoSeletivo(getValorConsultaCandidato(), getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }

            setListaConsultaCandidato(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCandidato(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCandidato() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
        getInscricaoVO().setCandidato(obj);
        valorConsultaCandidato = "";
        campoConsultaCandidato = "";
        getListaConsultaCandidato().clear();
    }

    public List getTipoConsultaComboCandidato() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    public InscricaoVO getInscricaoVO() {
        if (inscricaoVO == null) {
            inscricaoVO = new InscricaoVO();
        }
        return inscricaoVO;
    }

    public void setInscricaoVO(InscricaoVO inscricaoVO) {
        this.inscricaoVO = inscricaoVO;
    }

    public List getListaSelectItemProcessoSeletivo() {
        if (listaSelectItemProcessoSeletivo == null) {
            listaSelectItemProcessoSeletivo = new ArrayList(0);
        }
        return listaSelectItemProcessoSeletivo;
    }

    public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
        this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
    }

    public List<InscricaoVO> getListaInscricoes() {
        if (listaInscricoes == null) {
            listaInscricoes = new ArrayList<InscricaoVO>(0);
        }
        return listaInscricoes;
    }

    public void setListaInscricoes(List listaInscricoes) {
        this.listaInscricoes = listaInscricoes;
    }

    public String getCampoConsultaCandidato() {
        if (campoConsultaCandidato == null) {
            campoConsultaCandidato = "";
        }
        return campoConsultaCandidato;
    }

    public void setCampoConsultaCandidato(String campoConsultaCandidato) {
        this.campoConsultaCandidato = campoConsultaCandidato;
    }

    public String getValorConsultaCandidato() {
        if (valorConsultaCandidato == null) {
            valorConsultaCandidato = "";
        }
        return valorConsultaCandidato;
    }

    public void setValorConsultaCandidato(String valorConsultaCandidato) {
        this.valorConsultaCandidato = valorConsultaCandidato;
    }

    public List getListaConsultaCandidato() {
        if (listaConsultaCandidato == null) {
            listaConsultaCandidato = new ArrayList(0);
        }
        return listaConsultaCandidato;
    }

    public void setListaConsultaCandidato(List listaConsultaCandidato) {
        this.listaConsultaCandidato = listaConsultaCandidato;
    }

	public String getLayout() {
		if (layout == null) {
			layout = "";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Boolean getDeclararCursoEscolaPublica() {
		if (declararCursoEscolaPublica == null) {
			declararCursoEscolaPublica = Boolean.FALSE;
		}
		return declararCursoEscolaPublica;
	}

	public void setDeclararCursoEscolaPublica(Boolean declararCursoEscolaPublica) {
		this.declararCursoEscolaPublica = declararCursoEscolaPublica;
	}
	
	public List<SelectItem> getListaSelectItemTipoLayout() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		for (TipoLayoutComprovanteEnum tipoLayoutComprovanteEnum : TipoLayoutComprovanteEnum.values()) {
			objs.add(new SelectItem(tipoLayoutComprovanteEnum.name(), tipoLayoutComprovanteEnum.getValorApresentar()));
		}
		return objs;
	}
	
	public boolean getIsMostrarDeclararCursoEscolaPublica() {
		return !getInscricaoVO().getProcSeletivo().getTipoLayoutComprovante().equals(TipoLayoutComprovanteEnum.LAYOUT_2);
	}
	
}
