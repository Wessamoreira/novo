package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;

public class DataComemorativaVO extends SuperVO{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2689938174307444905L;
    
    private Integer codigo;
    private Date dataComemorativa;
    private Date dataDiaAntecedencia;
    private TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativa;
    private CargoVO cargo;
    private AreaProfissionalVO areaProfissional;
    private AreaConhecimentoVO areaConhecimento;
    private UnidadeEnsinoVO unidadeEnsino;    
    private DepartamentoVO departamento;    
    
  
    private String assunto;
    private String mensagem;
    private StatusAtivoInativoEnum status;
    private String caminhoImagemPadraoCima;
    private String caminhoImagemPadraoBaixo;
    private String imgCima;
    private String imgBaixo;
    
    private Boolean comunicadoInterno;
    private Boolean bannerComunicado;
    private Boolean mensagemTopo;
    private ArquivoVO arquivoBannerImagem;
    private String bannerMensagem;
    private Integer diasApresentarAntecendencia;
    private String mensagemTopoTexto;
    private ArquivoVO arquivoImagemTopo;
    
    
    

    public String getCaminhoImagemPadraoCima() {
        if (caminhoImagemPadraoCima == null) {
            caminhoImagemPadraoCima = "";
        }
        return caminhoImagemPadraoCima;
    }

    public void setCaminhoImagemPadraoCima(String caminhoImagemPadraoCima) {

        this.caminhoImagemPadraoCima = caminhoImagemPadraoCima;
    }

    public String getCaminhoImagemPadraoBaixo() {
        if (caminhoImagemPadraoBaixo == null) {
            caminhoImagemPadraoBaixo = "";
        }
        return caminhoImagemPadraoBaixo;
    }

    public void setCaminhoImagemPadraoBaixo(String caminhoImagemPadraoBaixo) {
        this.caminhoImagemPadraoBaixo = caminhoImagemPadraoBaixo;
    }

    public String getImgCima() {
        if (imgCima == null) {
            imgCima = "";
        }
        return imgCima;
    }

    public void setImgCima(String imgCima) {
        this.imgCima = imgCima;
    }

    public String getImgBaixo() {
        if (imgBaixo == null) {
            imgBaixo = "";
        }
        return imgBaixo;
    }

    public void setImgBaixo(String imgBaixo) {
        this.imgBaixo = imgBaixo;
    }

    public String getMensagemComLayout(String mensagem) {

        // obeter o ip ou dns;
        // String dominio = UteisEmail.getURLAplicacao("/imagens/email/");
        String dominio = null;
        try {
            dominio = "./resources/imagens/email/";
        } catch (Exception e) {
           // //System.out.println("Comunicacao Erro:" + e.getMessage());
        }
        setCaminhoImagemPadraoCima(dominio + "cima_sei.jpg");
        setCaminhoImagemPadraoBaixo(dominio + "baixo_sei.jpg");

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
        sb.append("<div class=\"Section1\">");
        sb.append("<div>");
        sb.append("<table class=\"MsoNormalTable\" style=\"width: 275px; mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"300\">");
        sb.append("<tbody>");
        sb.append("<tr style=\"height: 59.25pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">");
        sb.append("<td style=\"height: 59.25pt; padding: 0cm;\" colspan=\"3\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1025\" src=\""
            + getCaminhoImagemPadraoCima() + "\" alt=\"" + getImgCima() + "\" width=\"626\" height=\"77\" border=\"0\" /></span></p>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr style=\"mso-yfti-irow:2\">");
        sb.append("<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><texto></span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\">" + mensagem
            + "<span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");

        sb.append("</span></td>");
        sb.append("</tr>");
        sb.append("<tr style=\"height: 43.5pt; mso-yfti-irow: 4; mso-yfti-lastrow: yes;\">");
        sb.append("<td style=\"height: 43.5pt; padding: 0cm;\" colspan=\"3\">");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1028\" src=\""
            + getCaminhoImagemPadraoBaixo() + "\" border=\"0\" alt=\"" + getImgBaixo() + "\" width=\"626\" height=\"59\" /></span></p>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"color: black;\">&nbsp;</span></p>");
        sb.append("</div>");
        sb.append("</body></html>");
        return sb.toString();
    }
    
    
    
    public DataComemorativaVO() {
        super();

    }

    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public Date getDataComemorativa() {
        return dataComemorativa;
    }
    
    public void setDataComemorativa(Date dataComemorativa) {
        this.dataComemorativa = dataComemorativa;
    }
    
    public String getAssunto() {
        if(assunto == null){
            assunto = "";
        }
        return assunto;
    }
    
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    
    public String getMensagem() {
        if(mensagem == null){
            mensagem = getMensagemComLayout("");
        }
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public StatusAtivoInativoEnum getStatus() {
        if(status == null){
            status = StatusAtivoInativoEnum.ATIVO;
        }
        return status;
    }
    
    public void setStatus(StatusAtivoInativoEnum status) {
        this.status = status;
    }
    
    public TipoDestinatarioDataComemorativaEnum getTipoDestinatarioDataComemorativa() {
        if(tipoDestinatarioDataComemorativa == null){
            tipoDestinatarioDataComemorativa = TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL;
        }
        return tipoDestinatarioDataComemorativa;
    }

    
    public void setTipoDestinatarioDataComemorativa(TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativa) {
        this.tipoDestinatarioDataComemorativa = tipoDestinatarioDataComemorativa;
    }

    public CargoVO getCargo() {
        if(cargo == null){
            cargo = new CargoVO();
        }
        return cargo;
    }

    
    public void setCargo(CargoVO cargo) {
        this.cargo = cargo;
    }

    
    public AreaProfissionalVO getAreaProfissional() {
        if(areaProfissional == null){
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    
    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    
    public AreaConhecimentoVO getAreaConhecimento() {
        if(areaConhecimento == null){
            areaConhecimento = new AreaConhecimentoVO();
        }
        return areaConhecimento;
    }

    
    public void setAreaConhecimento(AreaConhecimentoVO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if(unidadeEnsino == null){
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
    
    
    
    public DepartamentoVO getDepartamento() {
        if(departamento == null){
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }
    
    public Boolean getIsTipoAluno(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.ALUNOS);
    }
    
    public Boolean getIsTipoFuncinario(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.FUNCIONARIOS);
    }
    
    public Boolean getIsTipoCoordenador(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.COORDENADORES);
    }
    
    public Boolean getIsTipoProfessor(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.PROFESSORES);
    }

    public Boolean getIsTipoUnidadeEnsino(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO);
    }
    
    public Boolean getIsTipoCargo(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.CARGO);
    }
    
    public Boolean getIsTipoDepartamento(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO);
    }
    
    public Boolean getIsTipoAreaConhecimento(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO);
    }
    
    public Boolean getIsTipoAreaProfissional(){
        return getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL);
    }
    
    public Boolean getIsAtivo(){
        return !getNovoObj() && getStatus().equals(StatusAtivoInativoEnum.ATIVO);
    }
    
    public Boolean getIsInativo(){
        return !getNovoObj() && getStatus().equals(StatusAtivoInativoEnum.INATIVO);
    }

    /**
     * @return the comunicadoInterno
     */
    public Boolean getComunicadoInterno() {
        if (comunicadoInterno == null) {
            comunicadoInterno = Boolean.FALSE;
        }
        return comunicadoInterno;
    }

    /**
     * @param comunicadoInterno the comunicadoInterno to set
     */
    public void setComunicadoInterno(Boolean comunicadoInterno) {
        this.comunicadoInterno = comunicadoInterno;
    }

    /**
     * @return the bannerComunicado
     */
    public Boolean getBannerComunicado() {
        if (bannerComunicado == null) {
            bannerComunicado = Boolean.FALSE;
        }
        return bannerComunicado;
    }

    /**
     * @param bannerComunicado the bannerComunicado to set
     */
    public void setBannerComunicado(Boolean bannerComunicado) {
        this.bannerComunicado = bannerComunicado;
    }

    /**
     * @return the mensagemTopo
     */
    public Boolean getMensagemTopo() {
        if (mensagemTopo == null) {
            mensagemTopo = Boolean.FALSE;
        }
        return mensagemTopo;
    }

    /**
     * @param mensagemTopo the mensagemTopo to set
     */
    public void setMensagemTopo(Boolean mensagemTopo) {
        this.mensagemTopo = mensagemTopo;
    }

    /**
     * @return the arquivoBannerImagem
     */
    public ArquivoVO getArquivoBannerImagem() {
        if (arquivoBannerImagem == null) {
            arquivoBannerImagem = new ArquivoVO();
        }
        return arquivoBannerImagem;
    }

    /**
     * @param arquivoBannerImagem the arquivoBannerImagem to set
     */
    public void setArquivoBannerImagem(ArquivoVO arquivoBannerImagem) {
        this.arquivoBannerImagem = arquivoBannerImagem;
    }

    /**
     * @return the bannerMensagem
     */
    public String getBannerMensagem() {
        if (bannerMensagem == null) {
            bannerMensagem = "";
        }
        return bannerMensagem;
    }

    /**
     * @param bannerMensagem the bannerMensagem to set
     */
    public void setBannerMensagem(String bannerMensagem) {
        this.bannerMensagem = bannerMensagem;
    }

    /**
     * @return the diasApresentarAntecendencia
     */
    public Integer getDiasApresentarAntecendencia() {
        if (diasApresentarAntecendencia == null) {
            diasApresentarAntecendencia = 0;
        }
        return diasApresentarAntecendencia;
    }

    /**
     * @param diasApresentarAntecendencia the diasApresentarAntecendencia to set
     */
    public void setDiasApresentarAntecendencia(Integer diasApresentarAntecendencia) {
        this.diasApresentarAntecendencia = diasApresentarAntecendencia;
    }

    /**
     * @return the mensagemTopoTexto
     */
    public String getMensagemTopoTexto() {
        if (mensagemTopoTexto == null) {
            mensagemTopoTexto = "";
        }
        return mensagemTopoTexto;
    }

    /**
     * @param mensagemTopoTexto the mensagemTopoTexto to set
     */
    public void setMensagemTopoTexto(String mensagemTopoTexto) {
        this.mensagemTopoTexto = mensagemTopoTexto;
    }

    /**
     * @return the arquivoImagemTopo
     */
    public ArquivoVO getArquivoImagemTopo() {
        if (arquivoImagemTopo == null) {
            arquivoImagemTopo = new ArquivoVO();
        }
        return arquivoImagemTopo;
    }

    /**
     * @param arquivoImagemTopo the arquivoImagemTopo to set
     */
    public void setArquivoImagemTopo(ArquivoVO arquivoImagemTopo) {
        this.arquivoImagemTopo = arquivoImagemTopo;
    }

    /**
     * @return the dataDiaAntecedencia
     */
    public Date getDataDiaAntecedencia() {
        return dataDiaAntecedencia;
    }

    /**
     * @param dataDiaAntecedencia the dataDiaAntecedencia to set
     */
    public void setDataDiaAntecedencia(Date dataDiaAntecedencia) {
        this.dataDiaAntecedencia = dataDiaAntecedencia;
    }
    
    
    
    
    
    
}