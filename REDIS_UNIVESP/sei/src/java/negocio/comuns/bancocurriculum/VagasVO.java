package negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;

public class VagasVO extends SuperVO {

    /**
     *
     */
    private static final long serialVersionUID = -2976502272373351535L;
    private Integer codigo;
    private ParceiroVO parceiro;
    private String cargo;
    private AreaProfissionalVO areaProfissional;
    private Integer numeroVagas;
    private String superiorImediato;
    private EstadoVO estado;
    private CidadeVO cidade;
    private String horarioTrabalho;
    private String salario;
    private String beneficios;
    private String escolaridadeRequerida;
    private String conhecimentoEspecifico;
    private String principalAtividadeCargo;
    private Boolean necessitaVeiculo;
    private String tipoVeiculo;
    private String caracteristicaPessoalImprescindivel;
    private Boolean declaracaoValidade;
    private Boolean CLT;
    private Boolean estagio;
    private Boolean horista;
    private Boolean mensalista;
    private Boolean temporario;
    private Boolean outro;
    private Boolean mudanca;
    private Boolean transferencia;
    private Boolean viagem;
    private Boolean encerrar;
    private String situacao;
    private Date dataAlteracao;
    private Date dataCadastro;
    private Date dataEncerramento;
    private Date dataAtivacao;
    private Date dataExpiracao;
    private Boolean ingles;
    private Boolean espanhol;
    private Boolean frances;
    private String inglesNivel;
    private String espanholNivel;
    private String francesNivel;
    private String outrosIdiomas;
    private String outrosIdiomasNivel;
    private Boolean windows;
    private Boolean word;
    private Boolean excel;
    private Boolean access;
    private Boolean powerPoint;
    private Boolean internet;
    private Boolean sap;
    private Boolean corelDraw;
    private Boolean autoCad;
    private Boolean photoshop;
    private Boolean microsiga;
    private String outrosSoftwares;
    private String motivoEncerramento;
    private String alunoContratado;
    // atributo não persirtido utilizado
    private Integer quantidadeCandidatos;
    private Boolean empresaSigilosaParaVaga;
    // atributo não persirtido utilizado
    // apenas pra controle de tela
    private Boolean notificarAluno;
    // atributo não persirtido
    private List<AreaProfissionalVO> listaAreaProfissional;
    private List<VagaQuestaoVO> vagaQuestaoVOs;
    private List<VagaContatoVO> vagaContatoVOs;
    private List<VagaEstadoVO> vagaEstadoVOs;
    private List<VagaAreaVO> vagaAreaVOs;
	private Integer qtdDiasExpiracaoVaga;

    public VagasVO() {
        super();
    }

    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    public String getCargo() {
        if (cargo == null) {
            cargo = "";
        }
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public AreaProfissionalVO getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    public String getCargo_Apresentar() {
        if (getCargo().length() > 35) {
            return getCargo().substring(0, 35) + "...";
        }
        return getCargo();
    }

    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public Integer getNumeroVagas() {
        if (numeroVagas == null) {
            numeroVagas = 0;
        }
        return numeroVagas;
    }

    public void setNumeroVagas(Integer numeroVagas) {
        this.numeroVagas = numeroVagas;
    }

    public String getSuperiorImediato() {
        if (superiorImediato == null) {
            superiorImediato = "";
        }
        return superiorImediato;
    }

    public void setSuperiorImediato(String superiorImediato) {
        this.superiorImediato = superiorImediato;
    }

    public String getHorarioTrabalho() {
        if (horarioTrabalho == null) {
            horarioTrabalho = "";
        }
        return horarioTrabalho;
    }

    public void setHorarioTrabalho(String horarioTrabalho) {
        this.horarioTrabalho = horarioTrabalho;
    }

    public String getBeneficios() {
        if (beneficios == null) {
            beneficios = "";
        }
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getEscolaridadeRequerida() {
        if (escolaridadeRequerida == null) {
            escolaridadeRequerida = "";
        }
        return escolaridadeRequerida;
    }

    public void setEscolaridadeRequerida(String escolaridadeRequerida) {
        this.escolaridadeRequerida = escolaridadeRequerida;
    }

    public String getConhecimentoEspecifico() {
        if (conhecimentoEspecifico == null) {
            conhecimentoEspecifico = "";
        }
        return conhecimentoEspecifico;
    }

    public void setConhecimentoEspecifico(String conhecimentoEspecifico) {
        this.conhecimentoEspecifico = conhecimentoEspecifico;
    }

    public String getPrincipalAtividadeCargo() {
        if (principalAtividadeCargo == null) {
            principalAtividadeCargo = "";
        }
        return principalAtividadeCargo;
    }

    public void setPrincipalAtividadeCargo(String principalAtividadeCargo) {
        this.principalAtividadeCargo = principalAtividadeCargo;
    }

    public Boolean getNecessitaVeiculo() {
        if (necessitaVeiculo == null) {
            necessitaVeiculo = Boolean.FALSE;
        }
        return necessitaVeiculo;
    }

    public void setNecessitaVeiculo(Boolean necessitaVeiculo) {
        this.necessitaVeiculo = necessitaVeiculo;
    }

    public String getCaracteristicaPessoalImprescindivel() {
        if (caracteristicaPessoalImprescindivel == null) {
            caracteristicaPessoalImprescindivel = "";
        }
        return caracteristicaPessoalImprescindivel;
    }

    public void setCaracteristicaPessoalImprescindivel(String caracteristicaPessoalImprescindivel) {
        this.caracteristicaPessoalImprescindivel = caracteristicaPessoalImprescindivel;
    }

    public Boolean getDeclaracaoValidade() {
        if (declaracaoValidade == null) {
            declaracaoValidade = Boolean.FALSE;
        }
        return declaracaoValidade;
    }

    public void setDeclaracaoValidade(Boolean declaracaoValidade) {
        this.declaracaoValidade = declaracaoValidade;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipoVeiculo() {
        if (tipoVeiculo == null) {
            tipoVeiculo = "";
        }
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Boolean getCLT() {
        if (CLT == null) {
            CLT = Boolean.FALSE;
        }
        return CLT;
    }

    public Boolean getEstagio() {
        if (estagio == null) {
            estagio = Boolean.FALSE;
        }
        return estagio;
    }

    public Boolean getHorista() {
        if (horista == null) {
            horista = Boolean.FALSE;
        }
        return horista;
    }

    public Boolean getMensalista() {
        if (mensalista == null) {
            mensalista = Boolean.FALSE;
        }
        return mensalista;
    }

    public Boolean getTemporario() {
        if (temporario == null) {
            temporario = Boolean.FALSE;
        }
        return temporario;
    }

    public Boolean getOutro() {
        if (outro == null) {
            outro = Boolean.FALSE;
        }
        return outro;
    }

    public void setCLT(Boolean CLT) {
        this.CLT = CLT;
    }

    public void setEstagio(Boolean estagio) {
        this.estagio = estagio;
    }

    public void setHorista(Boolean horista) {
        this.horista = horista;
    }

    public void setMensalista(Boolean mensalista) {
        this.mensalista = mensalista;
    }

    public void setTemporario(Boolean temporario) {
        this.temporario = temporario;
    }

    public void setOutro(Boolean outro) {
        this.outro = outro;
    }

    public Boolean getMudanca() {
        if (mudanca == null) {
            mudanca = Boolean.FALSE;
        }
        return mudanca;
    }

    public void setMudanca(Boolean mudanca) {
        this.mudanca = mudanca;
    }

    public Boolean getTransferencia() {
        if (transferencia == null) {
            transferencia = Boolean.FALSE;
        }
        return transferencia;
    }

    public void setTransferencia(Boolean transferencia) {
        this.transferencia = transferencia;
    }

    public Boolean getViagem() {
        if (viagem == null) {
            viagem = Boolean.FALSE;
        }
        return viagem;
    }

    public void setViagem(Boolean viagem) {
        this.viagem = viagem;
    }

    public String getSalario() {
        if (salario == null) {
            salario = "";
        }
        return salario;
    }

    public String getSalario_Apresentar() {
        if (getSalario().equals("À Combinar")) {
            salario = "À combinar";
        } else if (getSalario().equals("Até R$999")) {
            salario = "Até R$999,00";
        } else if (getSalario().equals("R$1000 à R$1999")) {
            salario = "De R$ 1.000,00 até R$ 1.999,00";
        } else if (getSalario().equals("R$2000 à R$2999")) {
            salario = "De R$ 2.000,00 até R$ 2.999,00";
        } else if (getSalario().equals("R$3000 à R$3999")) {
            salario = "De R$ 3.000,00 até R$ 3.999,00";
        } else if (getSalario().equals("R$4000 à R$4999")) {
            salario = "De R$ 4.000,00 até R$ 4.999,00";
        } else if (getSalario().equals("R$5000 à R$5999")) {
            salario = "De R$ 5.000,00 até R$ 5.999,00";
        } else if (getSalario().equals("acima de R$6000")) {
            salario = "Acima de R$ 6.000,00";
        }
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getSituacao() {
        if (situacao == null) {
			situacao = "AT";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao_Apresentar() {
        if (this.getSituacao().equals("CA")) {
            return "Cancelado";
        }
        if (this.getSituacao().equals("AT")) {
            return "Aberta";
        }
        if (this.getSituacao().equals("EX")) {
            return "Expirada";
        }
        if (this.getSituacao().equals("EN")) {
            return "Encerrada";
        }
        return "Em Análise";
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public Date getDataAtivacao() {
        return dataAtivacao;
    }

    public String getDataAtivacao_Apresentar() {
        return Uteis.getData(getDataAtivacao());
    }

    public String getDataAlteracao_Apresentar() {
        return Uteis.getData(getDataAlteracao());
    }

    public String getDataCadastro_Apresentar() {
        return Uteis.getData(getDataCadastro());
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public Boolean getNotificarAluno() {
        return notificarAluno;
    }

    public void setNotificarAluno(Boolean notificarAluno) {
        this.notificarAluno = notificarAluno;
    }

    public List<AreaProfissionalVO> getListaAreaProfissional() {
        if (listaAreaProfissional == null) {
            listaAreaProfissional = new ArrayList<AreaProfissionalVO>(0);
        }
        return listaAreaProfissional;
    }

    public void setListaAreaProfissional(List<AreaProfissionalVO> listaAreaProfissional) {
        this.listaAreaProfissional = listaAreaProfissional;
    }

    public Boolean getEspanhol() {
        if (espanhol == null) {
            espanhol = false;
        }
        return espanhol;
    }

    public void setEspanhol(Boolean espanhol) {
        this.espanhol = espanhol;
    }

    public String getEspanholNivel() {
        if (espanholNivel == null) {
            espanholNivel = "";
        }
        return espanholNivel;
    }

    public void setEspanholNivel(String espanholNivel) {
        this.espanholNivel = espanholNivel;
    }

    public Boolean getFrances() {
        if (frances == null) {
            frances = false;
        }
        return frances;
    }

    public void setFrances(Boolean frances) {
        this.frances = frances;
    }

    public String getFrancesNivel() {
        if (francesNivel == null) {
            francesNivel = "";
        }
        return francesNivel;
    }

    public void setFrancesNivel(String francesNivel) {
        this.francesNivel = francesNivel;
    }

    public Boolean getIngles() {
        if (ingles == null) {
            ingles = false;
        }
        return ingles;
    }

    public void setIngles(Boolean ingles) {
        this.ingles = ingles;
    }

    public String getInglesNivel() {
        if (inglesNivel == null) {
            inglesNivel = "";
        }
        return inglesNivel;
    }

    public String getNivel_Apresentar(String nivel) {
        if (nivel.equals("inicial")) {
            return "Inicial";
        }
        if (nivel.equals("intermediario")) {
            return "Intermediário";
        }
        if (nivel.equals("avancado")) {
            return "Avançado";
        }
        return "";
    }

    public void setInglesNivel(String inglesNivel) {
        this.inglesNivel = inglesNivel;
    }

    public Boolean getAccess() {
        if (access == null) {
            access = false;
        }
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public Boolean getAutoCad() {
        if (autoCad == null) {
            autoCad = false;
        }
        return autoCad;
    }

    public void setAutoCad(Boolean autoCad) {
        this.autoCad = autoCad;
    }

    public Boolean getCorelDraw() {
        if (corelDraw == null) {
            corelDraw = false;
        }
        return corelDraw;
    }

    public void setCorelDraw(Boolean corelDraw) {
        this.corelDraw = corelDraw;
    }

    public Boolean getExcel() {
        if (excel == null) {
            excel = false;
        }
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public Boolean getInternet() {
        if (internet == null) {
            internet = false;
        }
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Boolean getMicrosiga() {
        if (microsiga == null) {
            microsiga = false;
        }
        return microsiga;
    }

    public void setMicrosiga(Boolean microsiga) {
        this.microsiga = microsiga;
    }

    public Boolean getPhotoshop() {
        if (photoshop == null) {
            photoshop = false;
        }
        return photoshop;
    }

    public void setPhotoshop(Boolean photoshop) {
        this.photoshop = photoshop;
    }

    public Boolean getPowerPoint() {
        if (powerPoint == null) {
            powerPoint = false;
        }
        return powerPoint;
    }

    public void setPowerPoint(Boolean powerPoint) {
        this.powerPoint = powerPoint;
    }

    public Boolean getSap() {
        if (sap == null) {
            sap = false;
        }
        return sap;
    }

    public void setSap(Boolean sap) {
        this.sap = sap;
    }

    public Boolean getWindows() {
        if (windows == null) {
            windows = false;
        }
        return windows;
    }

    public void setWindows(Boolean windows) {
        this.windows = windows;
    }

    public Boolean getWord() {
        if (word == null) {
            word = false;
        }
        return word;
    }

    public void setWord(Boolean word) {
        this.word = word;
    }

    public String getOutrosSoftwares() {
        if (outrosSoftwares == null) {
            outrosSoftwares = "";
        }
        return outrosSoftwares;
    }

    public void setOutrosSoftwares(String outrosSoftwares) {
        this.outrosSoftwares = outrosSoftwares;
    }

    public String getAlunoContratado() {
        if (alunoContratado == null) {
            alunoContratado = "";
        }
        return alunoContratado;
    }

    public void setAlunoContratado(String alunoContratado) {
        this.alunoContratado = alunoContratado;
    }

    public String getMotivoEncerramento() {
        if (motivoEncerramento == null) {
            motivoEncerramento = "";
        }
        return motivoEncerramento;
    }

    public void setMotivoEncerramento(String motivoEncerramento) {
        this.motivoEncerramento = motivoEncerramento;
    }

    public EstadoVO getEstado() {
        if (estado == null) {
            estado = new EstadoVO();
        }
        return estado;
    }

    public void setEstado(EstadoVO estado) {
        this.estado = estado;
    }

    public String getCidadeEstado_Apresentar() {
        return getCidade().getNome() + " - " + getCidade().getEstado().getSigla();
    }

    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    public Integer getQuantidadeCandidatos() {
        if (quantidadeCandidatos == null) {
            quantidadeCandidatos = 0;
        }
        return quantidadeCandidatos;
    }

    public void setQuantidadeCandidatos(Integer quantidadeCandidatos) {
        this.quantidadeCandidatos = quantidadeCandidatos;
    }

    public Boolean getEmpresaSigilosaParaVaga() {
        if (empresaSigilosaParaVaga == null) {
            empresaSigilosaParaVaga = Boolean.FALSE;
        }
        if (getParceiro().getCodigo().intValue() != 0 && getCodigo().intValue() == 0) {
            empresaSigilosaParaVaga = getParceiro().getEmpresaSigilosaParaVaga().booleanValue();
        }
        return empresaSigilosaParaVaga;
    }

    public void setEmpresaSigilosaParaVaga(Boolean empresaSigilosaParaVaga) {
        this.empresaSigilosaParaVaga = empresaSigilosaParaVaga;
    }

    /**
     * @return the encerrar
     */
    public Boolean getEncerrar() {
        return encerrar;
    }

    /**
     * @param encerrar the encerrar to set
     */
    public void setEncerrar(Boolean encerrar) {
        this.encerrar = encerrar;
    }

    public boolean getAprensetarModalAluno() {
        if (this.getMotivoEncerramento().equals("alunoInstituicaoContratado")) {
            return true;
        }
        return false;
    }

    public List<VagaQuestaoVO> getVagaQuestaoVOs() {
        if (vagaQuestaoVOs == null) {
            vagaQuestaoVOs = new ArrayList<VagaQuestaoVO>(0);
        }
        return vagaQuestaoVOs;
    }

    public void setVagaQuestaoVOs(List<VagaQuestaoVO> vagaQuestaoVOs) {
        this.vagaQuestaoVOs = vagaQuestaoVOs;
    }

    public List<VagaContatoVO> getVagaContatoVOs() {
        if (vagaContatoVOs == null) {
            vagaContatoVOs = new ArrayList<VagaContatoVO>(0);
        }
        return vagaContatoVOs;
    }

    public void setVagaContatoVOs(List<VagaContatoVO> vagaContatoVOs) {
        this.vagaContatoVOs = vagaContatoVOs;
    }

    public List<VagaEstadoVO> getVagaEstadoVOs() {
        if (vagaEstadoVOs == null) {
            vagaEstadoVOs = new ArrayList<VagaEstadoVO>(0);
        }
        return vagaEstadoVOs;
    }

    public void setVagaEstadoVOs(List<VagaEstadoVO> vagaEstadoVOs) {
        this.vagaEstadoVOs = vagaEstadoVOs;
    }

    public List<VagaAreaVO> getVagaAreaVOs() {
        if (vagaAreaVOs == null) {
            vagaAreaVOs = new ArrayList<VagaAreaVO>(0);
        }
        return vagaAreaVOs;
    }

    public void setVagaAreaVOs(List<VagaAreaVO> vagaAreaVOs) {
        this.vagaAreaVOs = vagaAreaVOs;
    }

    public String getOutrosIdiomas() {
        if (outrosIdiomas == null) {
            outrosIdiomas = "";
        }
        return outrosIdiomas;
    }

    public void setOutrosIdiomas(String outrosIdiomas) {
        this.outrosIdiomas = outrosIdiomas;
    }

    public String getOutrosIdiomasNivel() {
        if (outrosIdiomasNivel == null) {
            outrosIdiomasNivel = "";
        }
        return outrosIdiomasNivel;
    }

    public void setOutrosIdiomasNivel(String outrosIdiomasNivel) {
        this.outrosIdiomasNivel = outrosIdiomasNivel;
    }

    /**
     * @return the dataAlteracao
     */
    public Date getDataAlteracao() {
        if (dataAlteracao == null) {
            dataAlteracao = new Date();
        }
        return dataAlteracao;
    }

    /**
     * @param dataAlteracao the dataAlteracao to set
     */
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    /**
     * @return the dataAlteracao
     */
    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return dataCadastro;
    }

    /**
     * @param dataAlteracao the dataAlteracao to set
     */
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEstadosVaga() {
        String valorRetornar = "";
        int cont = 1;
        Iterator i = this.getVagaEstadoVOs().iterator();
        while (i.hasNext()) {
            VagaEstadoVO ve = (VagaEstadoVO)i.next();
            if (ve.getSelecionado()) {
                valorRetornar += ve.getEstado().getSigla();
            }
            if (cont < this.getVagaEstadoVOs().size()) {
                valorRetornar += " | ";
            }
            cont++;
        }
        return valorRetornar;
    }

    public String getEstadosVagaReduzido() {
        String valorRetornar = "";
        int cont = 1;
        Iterator i = this.getVagaEstadoVOs().iterator();
        while (i.hasNext()) {
            VagaEstadoVO ve = (VagaEstadoVO)i.next();
            if (ve.getSelecionado() && cont < 4) {
                valorRetornar += ve.getEstado().getSigla();
            }
            if (cont < 4 && cont < this.getVagaEstadoVOs().size()) {
                valorRetornar += " | ";
            } else if (cont == 4) {
                valorRetornar += "...";
            }
            cont++;
        }
        return valorRetornar;
    }

    public String getAreasVaga() {
        String valorRetornar = "";
        int cont = 1;
        Iterator i = this.getVagaAreaVOs().iterator();
        while (i.hasNext()) {
            VagaAreaVO ve = (VagaAreaVO)i.next();
            if (ve.getSelecionado()) {
                valorRetornar += ve.getAreaProfissional().getDescricaoAreaProfissional();
            }
            if (cont < this.getVagaAreaVOs().size()) {
                valorRetornar += " | ";
            }
            cont++;
        }
        return valorRetornar;
    }

	public Integer getQtdDiasExpiracaoVaga() {
		if (qtdDiasExpiracaoVaga == null) {
			qtdDiasExpiracaoVaga = 0;
		}
		return qtdDiasExpiracaoVaga;
	}

	public void setQtdDiasExpiracaoVaga(Integer qtdDiasExpiracaoVaga) {
		this.qtdDiasExpiracaoVaga = qtdDiasExpiracaoVaga;
	}
}
