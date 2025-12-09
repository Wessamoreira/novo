package relatorio.controle.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.ReservaCatalogoRelVO;

@Controller("ReservaCatalogoRelControle")
@Scope("viewScope")
@Lazy
public class ReservaCatalogoRelControle extends SuperControleRelatorio {
	
	private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemUnidadeEnsino;
    private BibliotecaVO bibliotecaVO;
    private CatalogoVO catalogoVO;
    private List listaSelectItemBiblioteca;
    private String campoConsultarCatalogo;
    private String valorConsultarCatalogo;
    private List listaConsultarCatalogo;
	private Date dataInicio;
	private Date dataFim;
	private String tipoRelatorio;
	private Boolean prazoEncerrado;
	private Boolean dentroPrazo;
	private Boolean aguardandoExemplar;
	private Boolean reservaConcluida;
	private Boolean reservaCancelada;
    
    public ReservaCatalogoRelControle() throws Exception {
    	montarListaSelectItemUnidadeEnsino();
    	 setMensagemID("msg_entre_prmrelatorio");
    }
    
    public void imprimirPDF() {
        List<ReservaCatalogoRelVO> listaObjetos = new ArrayList<ReservaCatalogoRelVO>(0);
        try {
        	setFazerDownload(false);
    		getFacadeFactory().getReservaCatalogoRelFacade().validarDados(getBibliotecaVO().getCodigo(), getDataInicio(), getDataFim());
    		listaObjetos = getFacadeFactory().getReservaCatalogoRelFacade().criarObjetoAnalitico(getCatalogoVO().getCodigo(), getBibliotecaVO().getCodigo(), getDataInicio(), getDataFim(), getPrazoEncerrado(), getDentroPrazo(), getAguardandoExemplar(), getReservaConcluida(), getReservaCancelada(), getUsuarioLogado());
    		if (getTipoRelatorio().equals("SI")) {
    			getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getReservaCatalogoRelFacade().designIReportRelatorio());
    		} else {
    			getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getReservaCatalogoRelFacade().designIReportRelatorioAnalitico());
    		}
        	if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getReservaCatalogoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("RESERVAS DE CATÁLOGOS");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getReservaCatalogoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				getSuperParametroRelVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				getSuperParametroRelVO().setPeriodo(Uteis.obterDataFormatoTextoddMMyyyy(getDataInicio()) + " à " + Uteis.obterDataFormatoTextoddMMyyyy(getDataFim()));
				UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
					if (ue.getExisteLogoRelatorio()) {
						String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
						String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
						getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
					} else {
						getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
					}
				
				if (!getCatalogoVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setCatalogo(getCatalogoVO().getTitulo());
				} else {
					getSuperParametroRelVO().setCatalogo("Todos");
				}
				
				realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
        	} else {
        		setMensagemID("msg_relatorio_sem_dados");
        	}
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        	montarListaSelectItemUnidadeEnsino();   
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
    public void limparCampoCatalogo() {
    	setCatalogoVO(new CatalogoVO());
    }
    
    public List getTipoConsultarComboCatalogo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
    
    public void consultarCatalogo() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarCatalogo().equals("codigo")) {
                if (getValorConsultarCatalogo().equals("")) {
                    setValorConsultarCatalogo("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarCatalogo());
                objs = getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getBibliotecaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultarCatalogo().equals("titulo")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(getValorConsultarCatalogo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getBibliotecaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), 0, getUsuarioLogado());
            }
            setListaConsultarCatalogo(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarCatalogo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarCatalogo() throws Exception {
        try {
            CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
            setCatalogoVO(obj);
            Uteis.liberarListaMemoria(this.getListaConsultarCatalogo());
            setValorConsultarCatalogo("");
            setCampoConsultarCatalogo("");
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void montarListaSelectItemBiblioteca() {
        try {
            montarListaSelectItemBiblioteca(getUnidadeEnsinoVO());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemBiblioteca(UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        List<BibliotecaVO> resultadoConsulta = null;
        Iterator<BibliotecaVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
                getUsuarioLogado());
            i = resultadoConsulta.iterator();
            getBibliotecaVO().setCodigo(0);
            List<SelectItem> objs = new ArrayList<SelectItem>(0);            
            while (i.hasNext()) {
                BibliotecaVO obj = (BibliotecaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                if(!Uteis.isAtributoPreenchido(getBibliotecaVO())) {
                	setBibliotecaVO(obj);
                }
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemBiblioteca(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, this.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);            
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
                	setUnidadeEnsinoVO(obj);
                	montarListaSelectItemBiblioteca();
                }
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public List getListaSelectItemTipoRelatorio() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("AN", "Analítico"));
        lista.add(new SelectItem("SI", "Sintético"));
        return lista;
    }
    
    public List getListaSelectItemSituacaoReserva() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("PE", "Prazo Encerrado"));
        lista.add(new SelectItem("DP", "Dentro do Prazo"));
        lista.add(new SelectItem("EE", "Em Espera"));
        lista.add(new SelectItem("CO", "Concluída"));
        return lista;
    }
    
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}
	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}
	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}
	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}
	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}
	public List getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList(0);
		}
		return listaSelectItemBiblioteca;
	}
	public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}
	public String getCampoConsultarCatalogo() {
		if (campoConsultarCatalogo == null) {
			campoConsultarCatalogo = "";
		}
		return campoConsultarCatalogo;
	}
	public void setCampoConsultarCatalogo(String campoConsultarCatalogo) {
		this.campoConsultarCatalogo = campoConsultarCatalogo;
	}
	public String getValorConsultarCatalogo() {
		if (valorConsultarCatalogo == null) {
			valorConsultarCatalogo = "";
		}
		return valorConsultarCatalogo;
	}
	public void setValorConsultarCatalogo(String valorConsultarCatalogo) {
		this.valorConsultarCatalogo = valorConsultarCatalogo;
	}
	public List getListaConsultarCatalogo() {
		if (listaConsultarCatalogo == null) {
			listaConsultarCatalogo = new ArrayList(0);
		}
		return listaConsultarCatalogo;
	}
	public void setListaConsultarCatalogo(List listaConsultarCatalogo) {
		this.listaConsultarCatalogo = listaConsultarCatalogo;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Boolean getPrazoEncerrado() {
		if (prazoEncerrado == null) {
			prazoEncerrado = Boolean.FALSE;
		}
		return prazoEncerrado;
	}

	public void setPrazoEncerrado(Boolean prazoEncerrado) {
		this.prazoEncerrado = prazoEncerrado;
	}

	public Boolean getDentroPrazo() {
		if (dentroPrazo == null) {
			dentroPrazo = Boolean.TRUE;
		}
		return dentroPrazo;
	}

	public void setDentroPrazo(Boolean dentroPrazo) {
		this.dentroPrazo = dentroPrazo;
	}

	public Boolean getAguardandoExemplar() {
		if (aguardandoExemplar == null) {
			aguardandoExemplar = Boolean.TRUE;
		}
		return aguardandoExemplar;
	}

	public void setAguardandoExemplar(Boolean aguardandoExemplar) {
		this.aguardandoExemplar = aguardandoExemplar;
	}

	public Boolean getReservaConcluida() {
		if (reservaConcluida == null) {
			reservaConcluida = Boolean.FALSE;
		}
		return reservaConcluida;
	}

	public void setReservaConcluida(Boolean reservaConcluida) {
		this.reservaConcluida = reservaConcluida;
	}

	public Boolean getReservaCancelada() {
		if(reservaCancelada == null){
			reservaCancelada = false;
		}
		return reservaCancelada;
	}

	public void setReservaCancelada(Boolean reservaCancelada) {
		this.reservaCancelada = reservaCancelada;
	}

	
	
}
