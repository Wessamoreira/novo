/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.CartaoRespostaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscritoSalaRelVO;

/**
 *
 * @author Philippe
 */
@Controller("CartaoRespostaRelControle")
@Scope("viewScope")
@Lazy
public class CartaoRespostaRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8887067667431043948L;
	private ProcSeletivoVO procSeletivoVO;
    private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
    private InscricaoVO inscricaoVO;
    private List<SelectItem> listaSelectItemTipoLayoutImpressao;
    private List<SelectItem> listaSelectItemProcSeletivo;
    private List<SelectItem> listaSelectItemProcSeletivoDataProva;
    private List<InscricaoVO> listaConsultaCandidato;
    private List<SelectItem> listaSelectItemLocalAula;
    private List<SelectItem> listaSelectItemSalaLocalAula;
    private Integer localAula;
    private String campoConsultaCandidato;
    private String layoutImpressao;
    private String valorConsultaCandidato;
    private Boolean buscaCandidatoIndividual;
    private Boolean trazerSomenteCandidatosComInscricaoPaga;
    private List<ProcessoSeletivoInscritoSalaRelVO> processoSeletivoInscritoSalaRelVOs;
    private List<InscricaoVO> inscricaoVOs;
    private Integer numeroCopias;
    private Integer coluna;
    private Integer sala;
    private Integer salaVisualizacao;
    private Boolean visualizarInscricao;
    
    private String campoConsultaProcSeletivo;
    private String valorConsultaProcSeletivo;
    private List<ProcSeletivoVO> listaConsultaProcSeletivo;
    
    
    
    public CartaoRespostaRelControle() throws Exception {
        montarListaSelectItemProcSeletivo();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<CartaoRespostaRelVO> listaObjetos = null;
        try {
            listaObjetos = getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(getTrazerSomenteCandidatosComInscricaoPaga(), 
                    getInscricaoVO().getCandidato().getCodigo(), getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), null, null);
            realizarImpressao(listaObjetos);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //Uteis.liberarListaMemoria(listaObjetos);

        }
    }
    
    private void realizarImpressao(List<CartaoRespostaRelVO> listaObjetos) throws Exception{
        if (!listaObjetos.isEmpty()) {
            if(getLayoutImpressao().equals("CARTAO_RESPOSTA")){
                super.setCaminhoRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().realizarImpressaoCartaoRespostaLC3000(listaObjetos, getNumeroCopias(), getColuna(), false,false, getUsuarioLogado()));
                super.setFazerDownload(true);
            }else  if(getLayoutImpressao().equals("PREENCHER_CARTAO_RESPOSTA")){
                	super.setCaminhoRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().realizarImpressaoCartaoRespostaLC3000(listaObjetos, getNumeroCopias(), getColuna(), true, false, getUsuarioLogado()));
                	super.setFazerDownload(true);
            }else{
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getCartaoRespostaRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getCartaoRespostaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);
//                montarListaSelectItemProcSeletivo();
            }
            setMensagemID("msg_relatorio_ok");
        } else {
            setMensagemID("msg_relatorio_sem_dados");
        }
    }
    
    public void imprimirPDFSala() {
        List<CartaoRespostaRelVO> listaObjetos = null;
        try {
            ProcessoSeletivoInscritoSalaRelVO sala = (ProcessoSeletivoInscritoSalaRelVO) context().getExternalContext().getRequestMap().get("salaItens");
            listaObjetos = getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(getTrazerSomenteCandidatosComInscricaoPaga(), 
                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), sala.getSala().getCodigo(), null);
            realizarImpressao(listaObjetos);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //Uteis.liberarListaMemoria(listaObjetos);

        }
    }
    
    public void imprimirPDFInscricao() {
        List<CartaoRespostaRelVO> listaObjetos = null;
        try {
            InscricaoVO inscricaoVO = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
            listaObjetos = getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(getTrazerSomenteCandidatosComInscricaoPaga(), 
                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), null, inscricaoVO.getCodigo());
            realizarImpressao(listaObjetos);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //Uteis.liberarListaMemoria(listaObjetos);
            
        }
    }
    
    public void imprimirPDFSalaVisualizacao() {
        List<CartaoRespostaRelVO> listaObjetos = null;
        try {            
            listaObjetos = getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(getTrazerSomenteCandidatosComInscricaoPaga(), 
                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSalaVisualizacao(), null);
            realizarImpressao(listaObjetos);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //Uteis.liberarListaMemoria(listaObjetos);
            
        }
    }
    
    public void visualizarCandidatosSala() {
        try {
            ProcessoSeletivoInscritoSalaRelVO sala = (ProcessoSeletivoInscritoSalaRelVO) context().getExternalContext().getRequestMap().get("salaItens");
            setSala(sala.getSala().getCodigo());
            setSalaVisualizacao(sala.getSala().getCodigo());
            setNumeroCopias(1);
            setInscricaoVOs(getFacadeFactory().getInscricaoFacade().consultaRapidaPorSalaProcSeletivoData(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), sala.getSala().getCodigo()));
            setVisualizarInscricao(true);
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void consultarDadosDataProcessoSeletivo() {
        try {
            setVisualizarInscricao(false);
            getProcessoSeletivoInscritoSalaRelVOs().clear();
            if (getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
                setProcessoSeletivoInscritoSalaRelVOs(getFacadeFactory().getInscricaoFacade().consultarQtdeInscritosPorSala(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), true));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public List<ProcessoSeletivoInscritoSalaRelVO> getProcessoSeletivoInscritoSalaRelVOs() {
        if (processoSeletivoInscritoSalaRelVOs == null) {
            processoSeletivoInscritoSalaRelVOs = new ArrayList<ProcessoSeletivoInscritoSalaRelVO>(0);
        }
        return processoSeletivoInscritoSalaRelVOs;
    }

    public void setProcessoSeletivoInscritoSalaRelVOs(List<ProcessoSeletivoInscritoSalaRelVO> processoSeletivoInscritoSalaRelVOs) {
        this.processoSeletivoInscritoSalaRelVOs = processoSeletivoInscritoSalaRelVOs;
    }

    public void consultarCandidatos() throws Exception {
        List<InscricaoVO> resultado = new ArrayList<InscricaoVO>(0);
        try {
            resultado = getFacadeFactory().getCartaoRespostaRelFacade().consultarTodosCandidatos(getValorConsultaCandidato(), getCampoConsultaCandidato(), getItemProcSeletivoDataProvaVO().getCodigo());
//            if (!resultado.isEmpty()) {
                setListaConsultaCandidato(resultado);
//            }
            setMensagemID("msg_dados_consultados");
        }  catch (Exception e) {
            throw e;
        } finally {
            //Uteis.liberarListaMemoria(resultado);
        }
    }
    
    public void consultarProcSeletivos() throws Exception {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaProcSeletivo().equals("descricao")) {
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFim")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicioInternet")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFimInternet")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProcSeletivo(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaCandidatoCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("inscricao", "Número de Inscrição"));
        itens.add(new SelectItem("cpf", "CPF"));
        return itens;
    }
    
    public List<SelectItem> getTipoConsultaProcSeletivoCombo() {
    	 List itens = new ArrayList(0);
         // itens.add(new SelectItem("codigo", "Código"));
         itens.add(new SelectItem("descricao", "Descrição"));
         itens.add(new SelectItem("dataInicio", "Data Início"));
         itens.add(new SelectItem("dataFim", "Data Fim"));
         itens.add(new SelectItem("dataInicioInternet", "Data Início Internet"));
         itens.add(new SelectItem("dataFimInternet", "Data Fim Internet"));
//         itens.add(new SelectItem("valorInscricao", "Valor Inscrição"));
//         itens.add(new SelectItem("dataProva", "Data Prova"));
         return itens;
    }
    
    public void selecionarCandidato() {
        InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
        getInscricaoVO().setCandidato(obj.getCandidato());
        getInscricaoVO().setCodigo(obj.getCodigo());
        setMensagemID("msg_dados_adicionados");
    }
    
    public void selecionarProcSeletivo() {
    	ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
        setProcSeletivoVO(obj);
        montarListaSelectItemProcSeletivoDataProva();
        setMensagemID("msg_dados_adicionados");
    }

    public void limparDadosCandidato() {
        getInscricaoVO().getCandidato().setCodigo(0);
    }
    
    public void limparDadosProcSeletivo() {
    	setProcSeletivoVO(new ProcSeletivoVO());
    }
    
    public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
        List<ProcSeletivoVO> resultadoConsulta = null;
        Iterator<ProcSeletivoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getProcSeletivoFacade().consultarProcessoSeletivoAntesDataProvaPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoVO obj = (ProcSeletivoVO) i.next();                
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));                
            }
            setListaSelectItemProcSeletivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void montarListaSelectItemProcSeletivoDataProva(String prm) throws Exception {
        setVisualizarInscricao(false);
        List<ItemProcSeletivoDataProvaVO> resultadoConsulta = null;
        Iterator<ItemProcSeletivoDataProvaVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
            	ItemProcSeletivoDataProvaVO obj = (ItemProcSeletivoDataProvaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
            }
            setListaSelectItemProcSeletivoDataProva(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ProcSeletivo</code>.
     * Buscando todos os objetos correspondentes a entidade <code>ProcSeletivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemProcSeletivo() {
        try {
            montarListaSelectItemProcSeletivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void montarListaSelectItemProcSeletivoDataProva() {
        try {
        	montarListaSelectItemProcSeletivoDataProva("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void limparCamposConsultaCandidato() {
    	getListaConsultaCandidato().clear();
    	setValorConsultaCandidato(null);
    }
    
    public void limparCamposConsultaProcSeletivo() {
    	try {
    		setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
    		setValorConsultaProcSeletivo(null);
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List<ProcSeletivoVO> consultarProcSeletivoPorDescricao(String descricaoPrm) throws Exception {
        List<ProcSeletivoVO> lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(descricaoPrm,  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public ProcSeletivoVO getProcSeletivoVO() {
        if (procSeletivoVO == null) {
            procSeletivoVO = new ProcSeletivoVO();
        }
        return procSeletivoVO;
    }

    public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
        this.procSeletivoVO = procSeletivoVO;
    }

    public List<SelectItem> getListaSelectItemProcSeletivo() {
        if (listaSelectItemProcSeletivo == null) {
            listaSelectItemProcSeletivo = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemProcSeletivo;
    }

    public void setListaSelectItemProcSeletivo(List<SelectItem> listaSelectItemProcSeletivo) {
        this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
    }

    public List<InscricaoVO> getListaConsultaCandidato() {
        if (listaConsultaCandidato == null) {
            listaConsultaCandidato = new ArrayList<InscricaoVO>(0);
        }
        return listaConsultaCandidato;
    }

    public void setListaConsultaCandidato(List<InscricaoVO> listaConsultaCandidato) {
        this.listaConsultaCandidato = listaConsultaCandidato;
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

    public Boolean getBuscaCandidatoIndividual() {
        if (buscaCandidatoIndividual == null) {
            buscaCandidatoIndividual = Boolean.FALSE;
        }
        return buscaCandidatoIndividual;
    }

    public void setBuscaCandidatoIndividual(Boolean buscaCandidatoIndividual) {
        this.buscaCandidatoIndividual = buscaCandidatoIndividual;
    }

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	public void setItemProcSeletivoDataProvaVO(
			ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	public List<SelectItem> getListaSelectItemProcSeletivoDataProva() {
		if (listaSelectItemProcSeletivoDataProva == null) {
			listaSelectItemProcSeletivoDataProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProcSeletivoDataProva;
	}

	public void setListaSelectItemProcSeletivoDataProva(
			List<SelectItem> listaSelectItemProcSeletivoDataProva) {
		this.listaSelectItemProcSeletivoDataProva = listaSelectItemProcSeletivoDataProva;
	}

	public Boolean getTrazerSomenteCandidatosComInscricaoPaga() {
		if (trazerSomenteCandidatosComInscricaoPaga == null) {
			trazerSomenteCandidatosComInscricaoPaga = Boolean.TRUE;
		}
		return trazerSomenteCandidatosComInscricaoPaga;
	}

	public void setTrazerSomenteCandidatosComInscricaoPaga(
			Boolean trazerSomenteCandidatosComInscricaoPaga) {
		this.trazerSomenteCandidatosComInscricaoPaga = trazerSomenteCandidatosComInscricaoPaga;
	}

    
    public List<SelectItem> getListaSelectItemTipoLayoutImpressao() {
        if(listaSelectItemTipoLayoutImpressao == null){
            listaSelectItemTipoLayoutImpressao = new ArrayList<SelectItem>(0);
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("CODIGO_BARRA", "Etiqueta Código Barra"));
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("CARTAO_RESPOSTA", "Cartão Resposta"));
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("PREENCHER_CARTAO_RESPOSTA", "Preenchimento Cartão Resposta"));
        }
        return listaSelectItemTipoLayoutImpressao;
    }

    
    public void setListaSelectItemTipoLayoutImpressao(List<SelectItem> listaSelectItemTipoLayoutImpressao) {
        this.listaSelectItemTipoLayoutImpressao = listaSelectItemTipoLayoutImpressao;
    }

    
    public String getLayoutImpressao() {
        if(layoutImpressao == null){
            layoutImpressao= "PREENCHER_CARTAO_RESPOSTA";
        }
        return layoutImpressao;
    }

    
    public void setLayoutImpressao(String layoutImpressao) {
        this.layoutImpressao = layoutImpressao;
    }
    
    public Integer getSalaVisualizacao() {
        if(salaVisualizacao == null){
            salaVisualizacao = 0;
        }
        return salaVisualizacao;
    }

    
    public void setSalaVisualizacao(Integer salaVisualizacao) {
        this.salaVisualizacao = salaVisualizacao;
    }
    
    public Integer getNumeroCopias() {
        if(numeroCopias == null){
            numeroCopias = 1;
        }
        return numeroCopias;
    }

    
    public void setNumeroCopias(Integer numeroCopias) {
        this.numeroCopias = numeroCopias;
    }
    
    public List<InscricaoVO> getInscricaoVOs() {
        if (inscricaoVOs == null) {
            inscricaoVOs = new ArrayList<InscricaoVO>(0);
        }
        return inscricaoVOs;
    }

    public void setInscricaoVOs(List<InscricaoVO> inscricaoVOs) {
        this.inscricaoVOs = inscricaoVOs;
    }

    public Boolean getVisualizarInscricao() {
        if (visualizarInscricao == null) {
            visualizarInscricao = false;
        }
        return visualizarInscricao;
    }

    public void setVisualizarInscricao(Boolean visualizarInscricao) {
        this.visualizarInscricao = visualizarInscricao;
    }
    
    public Integer getSala() {
        if(sala == null){
            sala = 0;
        }
        return sala;
    }

    
    public void setSala(Integer sala) {
        this.sala = sala;
    }

	public Integer getColuna() {
		if(coluna == null){
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}
	
	public Integer getLocalAula() {
		if(localAula == null){
			localAula = 0;
		}
		return localAula;
	}

	public void setLocalAula(Integer localAula) {
		this.localAula = localAula;
	}

	public List<SelectItem> getListaSelectItemSalaLocalAula() {
		if(listaSelectItemSalaLocalAula == null){
			listaSelectItemSalaLocalAula = new ArrayList<SelectItem>();
		}
		return listaSelectItemSalaLocalAula;
	}

	public void setListaSelectItemSalaLocalAula(List<SelectItem> listaSelectItemSalaLocalAula) {
		this.listaSelectItemSalaLocalAula = listaSelectItemSalaLocalAula;
	}
	
	public List<SelectItem> getListaSelectItemLocalAula() {
		if(listaSelectItemLocalAula == null){
			listaSelectItemLocalAula = new ArrayList<SelectItem>(0);
			try{
				List<LocalAulaVO> localAulaVOs = getFacadeFactory().getLocalAulaFacade().consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum.ATIVO, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemLocalAula.add(new SelectItem(0, ""));
				for(LocalAulaVO localAulaVO:localAulaVOs){
					listaSelectItemLocalAula.add(new SelectItem(localAulaVO.getCodigo(), localAulaVO.getLocal()));
				}
			}catch(Exception e){
				
			}
		}
		return listaSelectItemLocalAula;
	}

	public void setListaSelectItemLocalAula(List<SelectItem> listaSelectItemLocalAula) {
		this.listaSelectItemLocalAula = listaSelectItemLocalAula;
	}
	
	public void alterarLocalAula(){
    	try{
    		if(getLocalAula() == 0){    			
    			getListaSelectItemSalaLocalAula().clear();
    			
    		}else{
    			List<SalaLocalAulaVO> salaLocalAulaVOs = (getFacadeFactory().getSalaLocalAulaFacade().consultarPorLocalAula(getLocalAula()));
    			getListaSelectItemSalaLocalAula().add(new SelectItem(0, ""));
    			for(SalaLocalAulaVO salaLocalAulaVO: salaLocalAulaVOs){
    				getListaSelectItemSalaLocalAula().add(new SelectItem(salaLocalAulaVO.getCodigo(), salaLocalAulaVO.getSala()));
    			}	
    		}
    		
    	}catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

	public String getCampoConsultaProcSeletivo() {
		if (campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
		if (listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList<ProcSeletivoVO>(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}

	
	
}
