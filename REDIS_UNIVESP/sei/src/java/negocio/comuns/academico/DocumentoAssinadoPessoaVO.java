package negocio.comuns.academico;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

@XmlRootElement(name = "DocumentoAssinadoPessoa")
public class DocumentoAssinadoPessoaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2489853056648380231L;
	@XmlElement(name = "codigo")
	private Integer codigo;
	@ExcluirJsonAnnotation
	private DocumentoAssinadoVO documentoAssinadoVO;
	@XmlElement(name = "dataSolicitacao")
	private Date dataSolicitacao;
	@XmlElement(name = "dataAssinatura")
	private Date dataAssinatura;
	@XmlElement(name = "pessoaVO")
	private PessoaVO pessoaVO;
	@XmlElement(name = "tipoPessoa")
	private TipoPessoa tipoPessoa;
	@XmlElement(name = "situacaoDocumentoAssinadoPessoaEnum")
	private SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum;
	@XmlElement(name = "motivoRejeicao")
	private String motivoRejeicao;
	@XmlElement(name = "dataRejeicao")
	private Date dataRejeicao;
	
	@XmlElement(name = "ordemAssinatura")
	private Integer ordemAssinatura;
	@XmlElement(name = "cargo")
	private String cargo;
	@XmlElement(name = "titulo")
	private String titulo;
	
	private String codigoAssinatura;
	private String acaoAssinatura;
	private String jsonAssinatura;
	
	@XmlElement(name = "urlAssinatura")
	private String urlAssinatura;
	
	private String nomePessoa;
	private String emailPessoa;
	private String cpfPessoa;
	private DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum;
	
	/**
	 * Transient
	 */
	private TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum;
	private String dataApresentar;
	
	private Boolean assinarDocumento;
	private Boolean tipoPessoaConcedente;
	
	private String dispositivoAssinatura;
    private String ipAssinatura;
    
    private String latitude;
    private String longitude;
	
    @XmlElement(name = "assinarPorCNPJ")
	private Boolean assinarPorCNPJ;
    @XmlElement(name = "nome")
	private String nome;
    @XmlElement(name = "nomeAssinanteApresentar")
	private String nomeAssinanteApresentar;
    @XmlElement(name = "provedorAssinatura")
	private String provedorAssinatura;
	private String urlProvedorDeAssinatura;

	public DocumentoAssinadoPessoaVO() {
		super();
	}
	
	public DocumentoAssinadoPessoaVO(Date dataSolicitacao, TipoPessoa tipoPessoa,  SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura, String nomePessoa, String emailPessoa, String cpfPessoa, TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum) {
		super();
		this.nomePessoa = nomePessoa;		
		this.emailPessoa = emailPessoa;
		this.cpfPessoa = cpfPessoa;
		this.tipoAssinaturaDocumentoEnum = tipoAssinaturaDocumentoEnum;
		inicializarDataSituacao(dataSolicitacao, tipoPessoa, situacaoDocumentoAssinadoPessoaEnum, documentoAssinadoVO, ordemAssinatura);		
	}	
	
	public DocumentoAssinadoPessoaVO(Date dataSolicitacao, PessoaVO pessoaVO, TipoPessoa tipoPessoa,  SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, 
			DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura, String cargo, String titulo , Boolean assinarDocumento, TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum) {
		super();
		this.pessoaVO = pessoaVO;
		this.titulo = titulo;		
		this.cargo = cargo;
		this.assinarDocumento = assinarDocumento;
		this.tipoAssinaturaDocumentoEnum = tipoAssinaturaDocumentoEnum;
		inicializarDataSituacao(dataSolicitacao, tipoPessoa, situacaoDocumentoAssinadoPessoaEnum, documentoAssinadoVO, ordemAssinatura);
	}
	
	public DocumentoAssinadoPessoaVO(Date dataSolicitacao, PessoaVO pessoaVO, TipoPessoa tipoPessoa,  SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, 
			DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura, String cargo, String titulo , Boolean assinarDocumento, TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum, Boolean assinarPorCnpj) {
		super();
		this.pessoaVO = pessoaVO;
		this.titulo = titulo;		
		this.cargo = cargo;
		this.assinarDocumento = assinarDocumento;
		this.tipoAssinaturaDocumentoEnum = tipoAssinaturaDocumentoEnum;
		this.assinarPorCNPJ = assinarPorCnpj;
		inicializarDataSituacao(dataSolicitacao, tipoPessoa, situacaoDocumentoAssinadoPessoaEnum, documentoAssinadoVO, ordemAssinatura);
	}
	
	private void inicializarDataSituacao(Date dataSolicitacao,TipoPessoa tipoPessoa,  SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum,DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura) {
		this.dataSolicitacao = dataSolicitacao;
		this.tipoPessoa = tipoPessoa;
		this.documentoAssinadoVO = documentoAssinadoVO;
		this.situacaoDocumentoAssinadoPessoaEnum = situacaoDocumentoAssinadoPessoaEnum;
		this.ordemAssinatura = ordemAssinatura;				
		if(situacaoDocumentoAssinadoPessoaEnum.isAssinado()){
			this.dataAssinatura = new Date();	
		}else if(situacaoDocumentoAssinadoPessoaEnum.isRejeitado()){
			this.dataRejeicao = new Date();	
		}
	}
	
	

	public TipoAssinaturaDocumentoEnum getTipoAssinaturaDocumentoEnum() {
		if (tipoAssinaturaDocumentoEnum == null) {
			tipoAssinaturaDocumentoEnum = TipoAssinaturaDocumentoEnum.NENHUM;
		}
		return tipoAssinaturaDocumentoEnum;
	}

	public void setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum) {
		this.tipoAssinaturaDocumentoEnum = tipoAssinaturaDocumentoEnum;
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

	public Date getDataSolicitacao() {
		if (dataSolicitacao == null) {
			dataSolicitacao = new Date();
		}
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public TipoPessoa getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = TipoPessoa.FUNCIONARIO;
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public SituacaoDocumentoAssinadoPessoaEnum getSituacaoDocumentoAssinadoPessoaEnum() {
		if (situacaoDocumentoAssinadoPessoaEnum == null) {
			situacaoDocumentoAssinadoPessoaEnum = SituacaoDocumentoAssinadoPessoaEnum.PENDENTE;
		}
		return situacaoDocumentoAssinadoPessoaEnum;
	}

	public void setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) {
		this.situacaoDocumentoAssinadoPessoaEnum = situacaoDocumentoAssinadoPessoaEnum;
	}

	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = "";
		}
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}

	public Date getDataRejeicao() {
		return dataRejeicao;
	}

	public void setDataRejeicao(Date dataRejeicao) {
		this.dataRejeicao = dataRejeicao;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	@XmlElement(name = "dataApresentar")
	public String getDataApresentar() {
		switch (getSituacaoDocumentoAssinadoPessoaEnum()) {
		case ASSINADO:
			dataApresentar = Uteis.getData(getDataAssinatura(), "dd/MM/yyyy HH:mm:ss");
			break;
		case PENDENTE:
			dataApresentar = Uteis.getData(getDataSolicitacao(), "dd/MM/yyyy HH:mm:ss");
			break;
		case REJEITADO:
			dataApresentar = Uteis.getData(getDataRejeicao(), "dd/MM/yyyy HH:mm:ss");
			break;
		default:
			dataApresentar = "";
		}
		return dataApresentar;
	}
	
	public void setDataApresentar(String dataApresentar) {
		this.dataApresentar = dataApresentar;
	}


	@Override
	public String toString() {
		return "DocumentoAssinadoPessoaVO [codigo=" + codigo + "]";
	}

	public Integer getOrdemAssinatura() {
		if(ordemAssinatura == null) {
			ordemAssinatura = 0;
		}
		return ordemAssinatura;
	}

	public void setOrdemAssinatura(Integer ordemAssinatura) {
		this.ordemAssinatura = ordemAssinatura;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCodigoAssinatura() {
		return codigoAssinatura;
	}

	public void setCodigoAssinatura(String codigoAssinatura) {
		this.codigoAssinatura = codigoAssinatura;
	}

	public String getAcaoAssinatura() {		
		return acaoAssinatura;
	}

	public void setAcaoAssinatura(String acaoAssinatura) {
		this.acaoAssinatura = acaoAssinatura;
	}

	public String getUrlAssinatura() {		
		return urlAssinatura;
	}

	public void setUrlAssinatura(String urlAssinatura) {
		this.urlAssinatura = urlAssinatura;
	}

	public String getJsonAssinatura() {
		if (jsonAssinatura == null) {
			jsonAssinatura = "";
		}
		return jsonAssinatura;
	}

	public void setJsonAssinatura(String jsonAssinatura) {
		this.jsonAssinatura = jsonAssinatura;
	}

	public String getNomePessoa() {
		if (nomePessoa == null) {
			nomePessoa = "";
		}
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getEmailPessoa() {
		if (emailPessoa == null) {
			emailPessoa = "";
		}
		return emailPessoa;
	}

	public void setEmailPessoa(String emailPessoa) {
		this.emailPessoa = emailPessoa;
	}

	public String getCpfPessoa() {
		if (cpfPessoa == null) {
			cpfPessoa = "";
		}
		return cpfPessoa;
	}

	public void setCpfPessoa(String cpfPessoa) {
		this.cpfPessoa = cpfPessoa;
	}


	public Boolean getAssinarDocumento() {
		if (assinarDocumento == null) {
			assinarDocumento = false;
		}
		return assinarDocumento;
	}

	public void setAssinarDocumento(Boolean assinarDocumento) {
		this.assinarDocumento = assinarDocumento;
	}
	
	
	public String getIpAssinatura() {
		if (ipAssinatura == null) {
			ipAssinatura = "";
		}
		return ipAssinatura;
	}

	public void setIpAssinatura(String ipAssinatura) {
		this.ipAssinatura = ipAssinatura;
	}

	public String getDispositivoAssinatura() {
		if (dispositivoAssinatura == null) {
			dispositivoAssinatura = "";
		}
		return dispositivoAssinatura;
	}

	public String getNomeApresentar() {
		return Uteis.isAtributoPreenchido(getNomePessoa()) ? getNomePessoa() : getPessoaVO().getNome();
	}



	public void setDispositivoAssinatura(String dispositivoAssinatura) {
		this.dispositivoAssinatura = dispositivoAssinatura;
	}

	public void getDadosComplementaresAssinaturaDigital() throws UnknownHostException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
        	HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        	setIpAssinatura(request.getRemoteAddr());
        	setDispositivoAssinatura(request.getHeader("user-agent"));
        }
	 }

	public String getLatitude() {
		if (latitude == null) {
			latitude = "";
		}
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	
	
	public String getLongitude() {
		if (longitude == null) {
			longitude = "";
		}
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public Boolean getTipoPessoaConcedente() {
		if (tipoPessoaConcedente == null) {
			tipoPessoaConcedente = false;
		}
		return tipoPessoaConcedente;
	}
	
	public void setTipoPessoaConcedente(Boolean tipoPessoaConcedente) {
		this.tipoPessoaConcedente = tipoPessoaConcedente;
	}

	public DocumentoAssinadoOrigemEnum getDocumentoAssinadoOrigemEnum() {
		if (documentoAssinadoOrigemEnum == null) {
			documentoAssinadoOrigemEnum = DocumentoAssinadoOrigemEnum.NENHUM;
		}
		return documentoAssinadoOrigemEnum;
	}

	public void setDocumentoAssinadoOrigemEnum(DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) {
		this.documentoAssinadoOrigemEnum = documentoAssinadoOrigemEnum;
	}
	
	public Boolean getAssinarPorCNPJ() {
		if (assinarPorCNPJ == null) {
			assinarPorCNPJ = false;
		}
		return assinarPorCNPJ;
	}

	public void setAssinarPorCNPJ(Boolean assinarPorCNPJ) {
		this.assinarPorCNPJ = assinarPorCNPJ;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNomeAssinanteApresentar() {
		if (nomeAssinanteApresentar == null) {
			nomeAssinanteApresentar = obterNomeAssinanteApresentar();
		}
		return nomeAssinanteApresentar;
	}
	
	public void setNomeAssinanteApresentar(String nomeAssinanteApresentar) {
		this.nomeAssinanteApresentar = nomeAssinanteApresentar;
	}
	
	public String obterNomeAssinanteApresentar() {
		Optional<DocumentoAssinadoPessoaVO> possuiSegundaAssinatura = getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().filter(dap -> dap.getOrdemAssinatura().equals(2)).findAny();
		if (getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			switch (getOrdemAssinatura()) {
			case 1:
				String ordemAssinatura1 = getOrdemAssinatura() + ". " + getPessoaVO().getNome();
				return ordemAssinatura1;
			case 2:
				String ordemAssinatura2 = getOrdemAssinatura() + ". " + getPessoaVO().getNome();
				return ordemAssinatura2;
			case 3:
				String ordemAssinatura3 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getPessoaVO().getNome() + " (" + getDocumentoAssinadoVO().getUnidadeEnsinoVO().getNome() + ")" : (getOrdemAssinatura() - 1) + ". " + getPessoaVO().getNome() + " (" + getDocumentoAssinadoVO().getUnidadeEnsinoVO().getNome() + ")";
				return ordemAssinatura3;
			case 4:
				if (Uteis.isAtributoPreenchido(getPessoaVO())) {
					String ordemAssinatura4 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getPessoaVO().getNome() : (getOrdemAssinatura() - 1) + ". " + getPessoaVO().getNome();
					return ordemAssinatura4;
				} else {
					String ordemAssinatura4 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getNome() : (getOrdemAssinatura() - 1) + ". " + getNome();
					return ordemAssinatura4;
				}
			case 5:
				if (getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) {
					String ordemAssinatura4 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getPessoaVO().getNome() + " (" + getDocumentoAssinadoVO().getUnidadeEnsinoVO().getNome() + ")" : (getOrdemAssinatura() - 2) + ". " + getPessoaVO().getNome() + " (" + getDocumentoAssinadoVO().getUnidadeEnsinoVO().getNome() + ")";
					return ordemAssinatura4;
				} else {
					String registradora = Uteis.isAtributoPreenchido(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getUnidadeCertificadora()) ? getDocumentoAssinadoVO().getUnidadeEnsinoVO().getUnidadeCertificadora() : "Registradora";
					if (Uteis.isAtributoPreenchido(getPessoaVO())) {
						String ordemAssinatura5 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getPessoaVO().getNome() + " (" + getDocumentoAssinadoVO().getUnidadeEnsinoVO().getUnidadeCertificadora() + ")" : (getOrdemAssinatura() - 1) + ". " + getPessoaVO().getNome() + " (" + registradora + ")";
						return ordemAssinatura5;
					} else {
						String ordemAssinatura5 = possuiSegundaAssinatura.isPresent() ? getOrdemAssinatura() + ". " + getNome() : (getOrdemAssinatura() - 1) + ". " + getNome() + " (" + registradora + ")";
						return ordemAssinatura5;
					}
				}
			default:
				return Constantes.EMPTY;
			}
		} else {
			return getOrdemAssinatura() + ". " + getPessoaVO().getNome();
		}
	}
	
	public String getProvedorAssinatura() {
		if (provedorAssinatura == null) {
			provedorAssinatura = "";
		}
		return provedorAssinatura;
	}

	public void setProvedorAssinatura(String provedorAssinatura) {
		this.provedorAssinatura = provedorAssinatura;
	}

	public String getUrlProvedorDeAssinatura() {
		if (urlProvedorDeAssinatura == null) {
			urlProvedorDeAssinatura = "";
		}
		return urlProvedorDeAssinatura;
	}

	public void setUrlProvedorDeAssinatura(String urlProvedorDeAssinatura) {
		this.urlProvedorDeAssinatura = urlProvedorDeAssinatura;
	}
}
