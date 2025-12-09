package controle.basico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.DataComemorativaVO;
import negocio.comuns.basico.TipoDestinatarioDataComemorativaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("DataComemorativaControle")
@Scope("viewScope")
@Lazy
public class DataComemorativaControle extends SuperControle {

    /**
     * 
     */
    private static final long serialVersionUID = -662568552022578241L;

    private DataComemorativaVO dataComemorativa;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemDepartamento;
    private List<SelectItem> listaSelectItemCargo;
    private List<SelectItem> listaSelectItemAreaConhecimento;
    private List<SelectItem> listaSelectItemAreaProfissional;
    private List<SelectItem> listaSelectItemTipoDestinatrioDataComemorativa;
    private List<SelectItem> listaSelectItemStatusDataComemorativa;
    /**
     * Atributos utilizados na tela de consulta;
     */
    private TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativaEnum;
    private StatusAtivoInativoEnum status;
    private Integer unidadeEnsino;
    private Integer departamento;
    private Integer cargo;
    private Integer areaConhecimento;
    private Integer areaProfissional;
    private String assunto;
    private Date dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
    private Date dataFim = Uteis.getDataUltimoDiaMes(new Date());
    
    
    

    public DataComemorativaControle() {
        super();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    public void persistir() {
        try {
            getFacadeFactory().getDataComemorativaFacade().persistir(getDataComemorativa(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarAtivacao() {
        try {
            getFacadeFactory().getDataComemorativaFacade().realizarAtivacao(getDataComemorativa(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (Exception e) {
            getDataComemorativa().setStatus(StatusAtivoInativoEnum.INATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    public void realizarInativacao() {
        try {
            getFacadeFactory().getDataComemorativaFacade().realizarInativacao(getDataComemorativa(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
        } catch (Exception e) {
            getDataComemorativa().setStatus(StatusAtivoInativoEnum.ATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
	public String editar() {
		try {
			DataComemorativaVO data = (DataComemorativaVO) context().getExternalContext().getRequestMap().get("item");
			setDataComemorativa(getFacadeFactory().getDataComemorativaFacade().consultarPorChavePrimaria(data.getCodigo(), false, getUsuarioLogado()));
			inicializarDadosImagem();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("dataComemorativaForm");
	}

    public void inicializarDadosImagem() throws Exception {
        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getDataComemorativa().getArquivoImagemTopo(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
        setCaminhoFotoUsuario2(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getDataComemorativa().getArquivoBannerImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
    }

    public String novo() {
        setDataComemorativa(null);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("dataComemorativaForm");
    }
    
    public String consultar() {
        try {
            Uteis.liberarListaMemoria(getListaConsulta());
            setListaConsulta(getFacadeFactory().getDataComemorativaFacade().consultar(getDataInicio(), getDataFim(), assunto, tipoDestinatarioDataComemorativaEnum, status, unidadeEnsino, departamento, cargo, areaConhecimento, areaProfissional, true, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("dataComemorativaCons");
    }

    public void upLoadImagem(FileUploadEvent uploadEvent) {
        try {
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getDataComemorativa().getArquivoImagemTopo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM, getUsuarioLogado());
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getDataComemorativa().getArquivoImagemTopo(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
            setExibirBotao(Boolean.TRUE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public String getShowFotoCrop() {
        try {
            if (getDataComemorativa().getArquivoImagemTopo().getNome() == null) {
                return "";
            }
            return getCaminhoFotoUsuario();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getTagImageComFotoPadrao();
        }
    }

    public void renderizarUpload() {
        setExibirUpload(false);
    }

    public void cancelar() {
        setExibirUpload(true);
        setExibirBotao(false);
    }

    public void upLoadImagem2(FileUploadEvent uploadEvent) {
        try {
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getDataComemorativa().getArquivoBannerImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM, getUsuarioLogado());
            setCaminhoFotoUsuario2(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getDataComemorativa().getArquivoBannerImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
            setExibirBotao(Boolean.TRUE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public String getShowFotoCrop2() {
        try {
            if (getDataComemorativa().getArquivoBannerImagem().getNome() == null) {
                return "";
            }
            return getCaminhoFotoUsuario2();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getTagImageComFotoPadrao();
        }
    }

    public void renderizarUpload2() {
        setExibirUpload(false);
    }

    public void cancelar2() {
        setExibirUpload(true);
        setExibirBotao(false);
    }

    public void montarListaSelectItens() {
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemDepartamento);
        Uteis.liberarListaMemoria(listaSelectItemCargo);
        Uteis.liberarListaMemoria(listaSelectItemAreaConhecimento);
        Uteis.liberarListaMemoria(listaSelectItemAreaProfissional);
        Uteis.liberarListaMemoria(listaSelectItemTipoDestinatrioDataComemorativa);
        Uteis.liberarListaMemoria(listaSelectItemStatusDataComemorativa);
        listaSelectItemUnidadeEnsino = null;
        listaSelectItemDepartamento = null;
        listaSelectItemCargo = null;
        listaSelectItemAreaConhecimento = null;
        listaSelectItemAreaProfissional = null;
        listaSelectItemTipoDestinatrioDataComemorativa = null;
        listaSelectItemStatusDataComemorativa = null;
        getListaSelectItemAreaConhecimento();
        getListaSelectItemUnidadeEnsino();
        getListaSelectItemAreaProfissional();
        getListaSelectItemCargo();
        getListaSelectItemDepartamento();
        getListaSelectItemStatusDataComemorativa();
        getListaSelectItemTipoDestinatrioDataComemorativa();
    }

    public DataComemorativaVO getDataComemorativa() {
        if(dataComemorativa ==null){
            dataComemorativa = new DataComemorativaVO();
        }
        return dataComemorativa;
    }

    public void setDataComemorativa(DataComemorativaVO dataComemorativa) {
        this.dataComemorativa = dataComemorativa;
    }

    public TipoDestinatarioDataComemorativaEnum getTipoDestinatarioDataComemorativaEnum() {
        return tipoDestinatarioDataComemorativaEnum;
    }

    public void setTipoDestinatarioDataComemorativaEnum(TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativaEnum) {
        this.tipoDestinatarioDataComemorativaEnum = tipoDestinatarioDataComemorativaEnum;
    }

    public StatusAtivoInativoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAtivoInativoEnum status) {
        this.status = status;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public Integer getCargo() {
        return cargo;
    }

    public void setCargo(Integer cargo) {
        this.cargo = cargo;
    }

    public Integer getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(Integer areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public Integer getAreaProfissional() {
        return areaProfissional;
    }

    public void setAreaProfissional(Integer areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if(listaSelectItemUnidadeEnsino == null){            
            listaSelectItemUnidadeEnsino= new ArrayList<SelectItem>(0);
            try{
                List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());                
                for(UnidadeEnsinoVO unidadeEnsinoVO:unidadeEnsinoVOs){
                    listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));                    
                }                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemUnidadeEnsino = null;
            }
        }
        return listaSelectItemUnidadeEnsino;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemDepartamento() {
        if(listaSelectItemDepartamento == null){            
            listaSelectItemDepartamento= new ArrayList<SelectItem>(0);
            try{
                List<DepartamentoVO> departamentoVOs = getFacadeFactory().getDepartamentoFacade().consultarPorNome("",  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());                
                for(DepartamentoVO departamentoVO:departamentoVOs){
                    listaSelectItemDepartamento.add(new SelectItem(departamentoVO.getCodigo(), departamentoVO.getNome()));                    
                }                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemDepartamento = null;
            }
        }
        return listaSelectItemDepartamento;
    }

    public List<SelectItem> getListaSelectItemCargo() {
        if(listaSelectItemCargo == null){            
            listaSelectItemCargo= new ArrayList<SelectItem>(0);
            try{
                List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultaRapidaPorNome("",  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());                
                for(CargoVO cargoVO:cargoVOs){
                    listaSelectItemCargo.add(new SelectItem(cargoVO.getCodigo(), cargoVO.getNome()));                    
                }                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemCargo = null;
            }
        }
        return listaSelectItemCargo;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemAreaConhecimento() {
        if(listaSelectItemAreaConhecimento == null){            
            listaSelectItemAreaConhecimento= new ArrayList<SelectItem>(0);
            try{
                List<AreaConhecimentoVO> areaConhecimentoVOs = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome("", false, getUsuarioLogado());
                
                for(AreaConhecimentoVO areaConhecimentoVO:areaConhecimentoVOs){
                    listaSelectItemAreaConhecimento.add(new SelectItem(areaConhecimentoVO.getCodigo(), areaConhecimentoVO.getNome()));
                }
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemAreaConhecimento = null;
            }
        }
        return listaSelectItemAreaConhecimento;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemAreaProfissional() {
        if(listaSelectItemAreaProfissional == null){            
            listaSelectItemAreaProfissional= new ArrayList<SelectItem>(0);
            try{
                List<AreaProfissionalVO> areaProfissionalVOs = getFacadeFactory().getAreaProfissionalFacade().consultarPorSituacaoAreaProfissional("AT",  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());                
                for(AreaProfissionalVO areaProfissionalVO:areaProfissionalVOs){
                    listaSelectItemAreaProfissional.add(new SelectItem(areaProfissionalVO.getCodigo(), areaProfissionalVO.getDescricaoAreaProfissional()));                    
                }
                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemAreaProfissional = null;
            }
        }
        return listaSelectItemAreaProfissional;
    }

    public List<SelectItem> getListaSelectItemTipoDestinatrioDataComemorativa() {
        if(listaSelectItemTipoDestinatrioDataComemorativa == null){            
            listaSelectItemTipoDestinatrioDataComemorativa= new ArrayList<SelectItem>(0);
            try{
                listaSelectItemTipoDestinatrioDataComemorativa.add(new SelectItem(null, ""));
                                
               for(TipoDestinatarioDataComemorativaEnum tipo:TipoDestinatarioDataComemorativaEnum.values()){
                    listaSelectItemTipoDestinatrioDataComemorativa.add(new SelectItem(tipo, tipo.getValorApresentar()));                    
                }
                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemTipoDestinatrioDataComemorativa = null;
            }
        }
        return listaSelectItemTipoDestinatrioDataComemorativa;
    }

    public List<SelectItem> getListaSelectItemStatusDataComemorativa() {
        if(listaSelectItemStatusDataComemorativa == null){            
            listaSelectItemStatusDataComemorativa= new ArrayList<SelectItem>(0);
            try{
                listaSelectItemStatusDataComemorativa.add(new SelectItem(null, ""));
                                
               for(StatusAtivoInativoEnum status:StatusAtivoInativoEnum.values()){
                   listaSelectItemStatusDataComemorativa.add(new SelectItem(status, status.getValorApresentar()));                    
                }
                
            }catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemStatusDataComemorativa = null;
            }
        }
        return listaSelectItemStatusDataComemorativa;
    }
    
    public String inicializarConsulta() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("dataComemorativaCons");
	}
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }
    
    public Boolean getIsTipoUnidadeEnsino(){
        return getTipoDestinatarioDataComemorativaEnum() != null && getTipoDestinatarioDataComemorativaEnum().equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO);
    }
    
    public Boolean getIsTipoCargo(){
        return getTipoDestinatarioDataComemorativaEnum() != null && getTipoDestinatarioDataComemorativaEnum().equals(TipoDestinatarioDataComemorativaEnum.CARGO);
    }
    
    public Boolean getIsTipoDepartamento(){
        return getTipoDestinatarioDataComemorativaEnum() != null && getTipoDestinatarioDataComemorativaEnum().equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO);
    }
    
    public Boolean getIsTipoAreaConhecimento(){
        return getTipoDestinatarioDataComemorativaEnum() != null && getTipoDestinatarioDataComemorativaEnum().equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO);
    }
    
    public Boolean getIsTipoAreaProfissional(){
        return getTipoDestinatarioDataComemorativaEnum() != null && getTipoDestinatarioDataComemorativaEnum().equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL);
    }

}
