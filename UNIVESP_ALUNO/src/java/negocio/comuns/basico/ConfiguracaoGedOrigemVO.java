package negocio.comuns.basico;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;


@XmlRootElement(name = "ConfiguracaoGedOrigemVO")
public class ConfiguracaoGedOrigemVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3618102318903635803L;
	@XmlTransient
	@ExcluirJsonAnnotation
	private ConfiguracaoGEDVO configuracaoGEDVO;
	
	private Integer codigo;
	
	private TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado;

	private Boolean assinarDocumento  = false;
	
	private Boolean apresentarAssinaturaDigitalizadoFuncionario  = false;
	
	private Boolean apresentarSelo = false;
	private int alturaSelo = 35;
	private int larguraSelo = 35;
	private int posicaoXSelo = 10;
	private int posicaoYSelo = 10;
	private boolean ultimaPaginaSelo = true;
	
	private Boolean apresentarQrCode = false;
	private int alturaQrCode = 50;
	private int larguraQrCode = 50;
	private int posicaoXQrCode = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 520 : 57;
	private int posicaoYQrCode = 10;
	private boolean ultimaPaginaQrCode = true;
	
	private Boolean assinaturaUnidadeEnsino = true;
	private Boolean apresentarAssinaturaUnidadeEnsino = true;
	private int alturaAssinaturaUnidadeEnsino = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 80 : 100;
	private int larguraAssinaturaUnidadeEnsino = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 250 : 150;
	private int posicaoXAssinaturaUnidadeEnsino = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 630 : 440;
	private int posicaoYAssinaturaUnidadeEnsino = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 520 : 10;
	private boolean ultimaPaginaAssinaturaUnidadeEnsino = true;
	
	private Boolean apresentarAssinaturaFuncionario1 = false;
	private int alturaAssinaturaFuncionario1 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 60 : 100;
	private int larguraAssinaturaFuncionario1 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 220 :100;
	private int posicaoXAssinaturaFuncionario1 =  getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 240: 110;
	private int posicaoYAssinaturaFuncionario1 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 15: 10;
	private boolean ultimaPaginaAssinaturaFuncionario1 = true;
	
	private Boolean apresentarAssinaturaFuncionario2 = false;
	private int alturaAssinaturaFuncionario2 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 60 : 100;;
	private int larguraAssinaturaFuncionario2 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 220 :100;
	private int posicaoXAssinaturaFuncionario2 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 600 :220;
	private int posicaoYAssinaturaFuncionario2 = getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) ? 15: 10;
	private boolean ultimaPaginaAssinaturaFuncionario2 = true;
	
	private Boolean apresentarAssinaturaFuncionario3 = false;
	private int alturaAssinaturaFuncionario3 = 100;
	private int larguraAssinaturaFuncionario3 = 100;
	private int posicaoXAssinaturaFuncionario3 = 330;
	private int posicaoYAssinaturaFuncionario3 = 10;
	private boolean ultimaPaginaAssinaturaFuncionario3 = true;
	
					
	private Boolean apresentarAssinaturaAluno = false;
	private int alturaAssinaturaAluno = 100;
	private int larguraAssinaturaAluno = 100;
	private int posicaoXAssinaturaAluno = 330;
	private int posicaoYAssinaturaAluno = 10;
	private boolean ultimaPaginaAssinaturaAluno = true;
	
	
	private Boolean assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
	private Boolean assinarDocumentoFuncionarioResponsavel;
	private Boolean permitirAlunoAssinarDigitalmente;
	
	private ProvedorDeAssinaturaEnum provedorAssinaturaPadraoEnum;
	
	

	public ConfiguracaoGedOrigemVO() {
		super();		
	}
	
	public ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado) {
		super();
		this.tipoOrigemDocumentoAssinado = tipoOrigemDocumentoAssinado;
	}

	@XmlTransient
	public ConfiguracaoGEDVO getConfiguracaoGEDVO() {
		if(configuracaoGEDVO == null) {
			configuracaoGEDVO =  new ConfiguracaoGEDVO();
		}
		return configuracaoGEDVO;
	}

	public void setConfiguracaoGEDVO(ConfiguracaoGEDVO configuracaoGEDVO) {
		this.configuracaoGEDVO = configuracaoGEDVO;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "tipoOrigemDocumentoAssinado")
	public TipoOrigemDocumentoAssinadoEnum getTipoOrigemDocumentoAssinado() {
		if(tipoOrigemDocumentoAssinado == null) {
			tipoOrigemDocumentoAssinado =  TipoOrigemDocumentoAssinadoEnum.NENHUM;
		}
		return tipoOrigemDocumentoAssinado;
	}

	public void setTipoOrigemDocumentoAssinado(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado) {
		this.tipoOrigemDocumentoAssinado = tipoOrigemDocumentoAssinado;
	}
	
	@XmlElement(name = "assinarDocumento")
	public Boolean getAssinarDocumento() {
		return assinarDocumento;
	}

	public void setAssinarDocumento(Boolean assinarDocumento) {
		this.assinarDocumento = assinarDocumento;
	}

	@XmlElement(name = "apresentarAssinaturaDigitalizadoFuncionario")
	public Boolean getApresentarAssinaturaDigitalizadoFuncionario() {
		return apresentarAssinaturaDigitalizadoFuncionario;
	}

	public void setApresentarAssinaturaDigitalizadoFuncionario(Boolean apresentarAssinaturaDigitalizadoFuncionario) {
		this.apresentarAssinaturaDigitalizadoFuncionario = apresentarAssinaturaDigitalizadoFuncionario;
	}

	@XmlElement(name = "apresentarSelo")
	public Boolean getApresentarSelo() {
		return apresentarSelo;
	}

	public void setApresentarSelo(Boolean apresentarSelo) {
		this.apresentarSelo = apresentarSelo;
	}

	@XmlElement(name = "alturaSelo")
	public int getAlturaSelo() {
		return alturaSelo;
	}

	public void setAlturaSelo(int alturaSelo) {
		this.alturaSelo = alturaSelo;
	}

	@XmlElement(name = "larguraSelo")
	public int getLarguraSelo() {
		return larguraSelo;
	}

	public void setLarguraSelo(int larguraSelo) {
		this.larguraSelo = larguraSelo;
	}

	@XmlElement(name = "posicaoXSelo")
	public int getPosicaoXSelo() {
		return posicaoXSelo;
	}

	public void setPosicaoXSelo(int posicaoXSelo) {
		this.posicaoXSelo = posicaoXSelo;
	}

	@XmlElement(name = "posicaoYSelo")
	public int getPosicaoYSelo() {
		return posicaoYSelo;
	}

	public void setPosicaoYSelo(int posicaoYSelo) {
		this.posicaoYSelo = posicaoYSelo;
	}

	@XmlElement(name = "ultimaPaginaSelo")
	public boolean isUltimaPaginaSelo() {
		return ultimaPaginaSelo;
	}

	public void setUltimaPaginaSelo(boolean ultimaPaginaSelo) {
		this.ultimaPaginaSelo = ultimaPaginaSelo;
	}

	@XmlElement(name = "apresentarQrCode")
	public Boolean getApresentarQrCode() {
		return apresentarQrCode;
	}

	public void setApresentarQrCode(Boolean apresentarQrCode) {
		this.apresentarQrCode = apresentarQrCode;
	}

	@XmlElement(name = "alturaQrCode")
	public int getAlturaQrCode() {
		return alturaQrCode;
	}

	public void setAlturaQrCode(int alturaQrCode) {
		this.alturaQrCode = alturaQrCode;
	}

	@XmlElement(name = "larguraQrCode")
	public int getLarguraQrCode() {
		return larguraQrCode;
	}

	public void setLarguraQrCode(int larguraQrCode) {
		this.larguraQrCode = larguraQrCode;
	}

	@XmlElement(name = "posicaoXQrCode")
	public int getPosicaoXQrCode() {
		return posicaoXQrCode;
	}

	public void setPosicaoXQrCode(int posicaoXQrCode) {
		this.posicaoXQrCode = posicaoXQrCode;
	}

	@XmlElement(name = "posicaoYQrCode")
	public int getPosicaoYQrCode() {
		return posicaoYQrCode;
	}

	public void setPosicaoYQrCode(int posicaoYQrCode) {
		this.posicaoYQrCode = posicaoYQrCode;
	}

	@XmlElement(name = "ultimaPaginaQrCode")
	public boolean isUltimaPaginaQrCode() {
		return ultimaPaginaQrCode;
	}

	public void setUltimaPaginaQrCode(boolean ultimaPaginaQrCode) {
		this.ultimaPaginaQrCode = ultimaPaginaQrCode;
	}

	@XmlElement(name = "assinaturaUnidadeEnsino")
	public Boolean getAssinaturaUnidadeEnsino() {
		return assinaturaUnidadeEnsino;
	}

	public void setAssinaturaUnidadeEnsino(Boolean assinaturaUnidadeEnsino) {
		this.assinaturaUnidadeEnsino = assinaturaUnidadeEnsino;
	}

	@XmlElement(name = "apresentarAssinaturaUnidadeEnsino")
	public Boolean getApresentarAssinaturaUnidadeEnsino() {
		return apresentarAssinaturaUnidadeEnsino;
	}

	public void setApresentarAssinaturaUnidadeEnsino(Boolean apresentarAssinaturaUnidadeEnsino) {
		this.apresentarAssinaturaUnidadeEnsino = apresentarAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "alturaAssinaturaUnidadeEnsino")
	public int getAlturaAssinaturaUnidadeEnsino() {
		return alturaAssinaturaUnidadeEnsino;
	}

	public void setAlturaAssinaturaUnidadeEnsino(int alturaAssinaturaUnidadeEnsino) {
		this.alturaAssinaturaUnidadeEnsino = alturaAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "larguraAssinaturaUnidadeEnsino")
	public int getLarguraAssinaturaUnidadeEnsino() {
		return larguraAssinaturaUnidadeEnsino;
	}

	public void setLarguraAssinaturaUnidadeEnsino(int larguraAssinaturaUnidadeEnsino) {
		this.larguraAssinaturaUnidadeEnsino = larguraAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "posicaoXAssinaturaUnidadeEnsino")
	public int getPosicaoXAssinaturaUnidadeEnsino() {
		return posicaoXAssinaturaUnidadeEnsino;
	}

	public void setPosicaoXAssinaturaUnidadeEnsino(int posicaoXAssinaturaUnidadeEnsino) {
		this.posicaoXAssinaturaUnidadeEnsino = posicaoXAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "posicaoYAssinaturaUnidadeEnsino")
	public int getPosicaoYAssinaturaUnidadeEnsino() {
		return posicaoYAssinaturaUnidadeEnsino;
	}

	public void setPosicaoYAssinaturaUnidadeEnsino(int posicaoYAssinaturaUnidadeEnsino) {
		this.posicaoYAssinaturaUnidadeEnsino = posicaoYAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "ultimaPaginaAssinaturaUnidadeEnsino")
	public boolean isUltimaPaginaAssinaturaUnidadeEnsino() {
		return ultimaPaginaAssinaturaUnidadeEnsino;
	}

	public void setUltimaPaginaAssinaturaUnidadeEnsino(boolean ultimaPaginaAssinaturaUnidadeEnsino) {
		this.ultimaPaginaAssinaturaUnidadeEnsino = ultimaPaginaAssinaturaUnidadeEnsino;
	}

	@XmlElement(name = "apresentarAssinaturaFuncionario1")
	public Boolean getApresentarAssinaturaFuncionario1() {
		return apresentarAssinaturaFuncionario1;
	}

	public void setApresentarAssinaturaFuncionario1(Boolean apresentarAssinaturaFuncionario1) {
		this.apresentarAssinaturaFuncionario1 = apresentarAssinaturaFuncionario1;
	}

	@XmlElement(name = "alturaAssinaturaFuncionario1")
	public int getAlturaAssinaturaFuncionario1() {
		return alturaAssinaturaFuncionario1;
	}

	public void setAlturaAssinaturaFuncionario1(int alturaAssinaturaFuncionario1) {
		this.alturaAssinaturaFuncionario1 = alturaAssinaturaFuncionario1;
	}

	@XmlElement(name = "larguraAssinaturaFuncionario1")
	public int getLarguraAssinaturaFuncionario1() {
		return larguraAssinaturaFuncionario1;
	}

	public void setLarguraAssinaturaFuncionario1(int larguraAssinaturaFuncionario1) {
		this.larguraAssinaturaFuncionario1 = larguraAssinaturaFuncionario1;
	}

	@XmlElement(name = "posicaoXAssinaturaFuncionario1")
	public int getPosicaoXAssinaturaFuncionario1() {
		return posicaoXAssinaturaFuncionario1;
	}

	public void setPosicaoXAssinaturaFuncionario1(int posicaoXAssinaturaFuncionario1) {
		this.posicaoXAssinaturaFuncionario1 = posicaoXAssinaturaFuncionario1;
	}

	@XmlElement(name = "posicaoYAssinaturaFuncionario1")
	public int getPosicaoYAssinaturaFuncionario1() {
		return posicaoYAssinaturaFuncionario1;
	}

	public void setPosicaoYAssinaturaFuncionario1(int posicaoYAssinaturaFuncionario1) {
		this.posicaoYAssinaturaFuncionario1 = posicaoYAssinaturaFuncionario1;
	}

	@XmlElement(name = "ultimaPaginaAssinaturaFuncionario1")
	public boolean isUltimaPaginaAssinaturaFuncionario1() {
		return ultimaPaginaAssinaturaFuncionario1;
	}

	public void setUltimaPaginaAssinaturaFuncionario1(boolean ultimaPaginaAssinaturaFuncionario1) {
		this.ultimaPaginaAssinaturaFuncionario1 = ultimaPaginaAssinaturaFuncionario1;
	}

	@XmlElement(name = "apresentarAssinaturaFuncionario2")
	public Boolean getApresentarAssinaturaFuncionario2() {
		return apresentarAssinaturaFuncionario2;
	}

	public void setApresentarAssinaturaFuncionario2(Boolean apresentarAssinaturaFuncionario2) {
		this.apresentarAssinaturaFuncionario2 = apresentarAssinaturaFuncionario2;
	}

	@XmlElement(name = "alturaAssinaturaFuncionario2")
	public int getAlturaAssinaturaFuncionario2() {
		return alturaAssinaturaFuncionario2;
	}

	public void setAlturaAssinaturaFuncionario2(int alturaAssinaturaFuncionario2) {
		this.alturaAssinaturaFuncionario2 = alturaAssinaturaFuncionario2;
	}

	@XmlElement(name = "larguraAssinaturaFuncionario2")
	public int getLarguraAssinaturaFuncionario2() {
		return larguraAssinaturaFuncionario2;
	}

	public void setLarguraAssinaturaFuncionario2(int larguraAssinaturaFuncionario2) {
		this.larguraAssinaturaFuncionario2 = larguraAssinaturaFuncionario2;
	}

	@XmlElement(name = "posicaoXAssinaturaFuncionario2")
	public int getPosicaoXAssinaturaFuncionario2() {
		return posicaoXAssinaturaFuncionario2;
	}

	public void setPosicaoXAssinaturaFuncionario2(int posicaoXAssinaturaFuncionario2) {
		this.posicaoXAssinaturaFuncionario2 = posicaoXAssinaturaFuncionario2;
	}

	@XmlElement(name = "posicaoYAssinaturaFuncionario2")
	public int getPosicaoYAssinaturaFuncionario2() {
		return posicaoYAssinaturaFuncionario2;
	}

	public void setPosicaoYAssinaturaFuncionario2(int posicaoYAssinaturaFuncionario2) {
		this.posicaoYAssinaturaFuncionario2 = posicaoYAssinaturaFuncionario2;
	}

	@XmlElement(name = "ultimaPaginaAssinaturaFuncionario2")
	public boolean isUltimaPaginaAssinaturaFuncionario2() {
		return ultimaPaginaAssinaturaFuncionario2;
	}

	public void setUltimaPaginaAssinaturaFuncionario2(boolean ultimaPaginaAssinaturaFuncionario2) {
		this.ultimaPaginaAssinaturaFuncionario2 = ultimaPaginaAssinaturaFuncionario2;
	}

	@XmlElement(name = "apresentarAssinaturaFuncionario3")
	public Boolean getApresentarAssinaturaFuncionario3() {
		return apresentarAssinaturaFuncionario3;
	}

	public void setApresentarAssinaturaFuncionario3(Boolean apresentarAssinaturaFuncionario3) {
		this.apresentarAssinaturaFuncionario3 = apresentarAssinaturaFuncionario3;
	}

	@XmlElement(name = "alturaAssinaturaFuncionario3")
	public int getAlturaAssinaturaFuncionario3() {
		return alturaAssinaturaFuncionario3;
	}

	public void setAlturaAssinaturaFuncionario3(int alturaAssinaturaFuncionario3) {
		this.alturaAssinaturaFuncionario3 = alturaAssinaturaFuncionario3;
	}

	@XmlElement(name = "larguraAssinaturaFuncionario3")
	public int getLarguraAssinaturaFuncionario3() {
		return larguraAssinaturaFuncionario3;
	}

	public void setLarguraAssinaturaFuncionario3(int larguraAssinaturaFuncionario3) {
		this.larguraAssinaturaFuncionario3 = larguraAssinaturaFuncionario3;
	}
	
	@XmlElement(name = "posicaoXAssinaturaFuncionario3")
	public int getPosicaoXAssinaturaFuncionario3() {
		return posicaoXAssinaturaFuncionario3;
	}

	public void setPosicaoXAssinaturaFuncionario3(int posicaoXAssinaturaFuncionario3) {
		this.posicaoXAssinaturaFuncionario3 = posicaoXAssinaturaFuncionario3;
	}

	@XmlElement(name = "posicaoYAssinaturaFuncionario3")
	public int getPosicaoYAssinaturaFuncionario3() {
		return posicaoYAssinaturaFuncionario3;
	}

	public void setPosicaoYAssinaturaFuncionario3(int posicaoYAssinaturaFuncionario3) {
		this.posicaoYAssinaturaFuncionario3 = posicaoYAssinaturaFuncionario3;
	}

	@XmlElement(name = "ultimaPaginaAssinaturaFuncionario3")
	public boolean isUltimaPaginaAssinaturaFuncionario3() {
		return ultimaPaginaAssinaturaFuncionario3;
	}

	public void setUltimaPaginaAssinaturaFuncionario3(boolean ultimaPaginaAssinaturaFuncionario3) {
		this.ultimaPaginaAssinaturaFuncionario3 = ultimaPaginaAssinaturaFuncionario3;
	}

	@XmlElement(name = "assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada")
	public Boolean getAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada() {
		return assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
	}

	public void setAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada(
			Boolean assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada) {
		this.assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada = assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
	}

	@XmlElement(name = "assinarDocumentoFuncionarioResponsavel")
	public Boolean getAssinarDocumentoFuncionarioResponsavel() {
		if(assinarDocumentoFuncionarioResponsavel == null) {
			assinarDocumentoFuncionarioResponsavel = false;
		}
		return assinarDocumentoFuncionarioResponsavel;
	}

	public void setAssinarDocumentoFuncionarioResponsavel(Boolean assinarDocumentoFuncionarioResponsavel) {
		this.assinarDocumentoFuncionarioResponsavel = assinarDocumentoFuncionarioResponsavel;
	}
	
	@XmlElement(name = "apresentarAssinaturaAluno")
	public Boolean getApresentarAssinaturaAluno() {
		return apresentarAssinaturaAluno;
	}

	public void setApresentarAssinaturaAluno(Boolean apresentarAssinaturaAluno) {
		this.apresentarAssinaturaAluno = apresentarAssinaturaAluno;
	}
	
	@XmlElement(name = "alturaAssinaturaAluno")
	public int getAlturaAssinaturaAluno() {
		return alturaAssinaturaAluno;
	}

	public void setAlturaAssinaturaAluno(int alturaAssinaturaAluno) {
		this.alturaAssinaturaAluno = alturaAssinaturaAluno;
	}
	@XmlElement(name = "larguraAssinaturaAluno")
	public int getLarguraAssinaturaAluno() {
		return larguraAssinaturaAluno;
	}

	public void setLarguraAssinaturaAluno(int larguraAssinaturaAluno) {
		this.larguraAssinaturaAluno = larguraAssinaturaAluno;
	}
	@XmlElement(name = "posicaoXAssinaturaAluno")
	public int getPosicaoXAssinaturaAluno() {
		return posicaoXAssinaturaAluno;
	}

	public void setPosicaoXAssinaturaAluno(int posicaoXAssinaturaAluno) {
		this.posicaoXAssinaturaAluno = posicaoXAssinaturaAluno;
	}
	@XmlElement(name = "posicaoYAssinaturaAluno")
	public int getPosicaoYAssinaturaAluno() {
		return posicaoYAssinaturaAluno;
	}

	public void setPosicaoYAssinaturaAluno(int posicaoYAssinaturaAluno) {
		this.posicaoYAssinaturaAluno = posicaoYAssinaturaAluno;
	}
	@XmlElement(name = "ultimaPaginaAssinaturaAluno")
	public boolean isUltimaPaginaAssinaturaAluno() {
		return ultimaPaginaAssinaturaAluno;
	}

	public void setUltimaPaginaAssinaturaAluno(boolean ultimaPaginaAssinaturaAluno) {
		this.ultimaPaginaAssinaturaAluno = ultimaPaginaAssinaturaAluno;
	}
	
	

	public ProvedorDeAssinaturaEnum getProvedorAssinaturaPadraoEnum() {
		if(provedorAssinaturaPadraoEnum == null) {
			provedorAssinaturaPadraoEnum = ProvedorDeAssinaturaEnum.SEI;
		}
		return provedorAssinaturaPadraoEnum;
	}

	public void setProvedorAssinaturaPadraoEnum(ProvedorDeAssinaturaEnum provedorAssinaturaPadraoEnum) {
		this.provedorAssinaturaPadraoEnum = provedorAssinaturaPadraoEnum;
	}

	public Boolean getPermitiMarcarOpcaoApresentarAssinaturaDigitalizadaFuncionario() {
		return !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)
		&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED);
	}
	

	public Boolean getPermitiApresentarQrCode() {
		return !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA)
				&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
				&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)
				&& !getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED);
	}
	
	public Boolean getApresentarOpcaoAssinaturaFuncionario1() {
		return getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)						
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)						
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO)
		;
	}
	
	public Boolean getApresentarOpcaoAssinaturaFuncionario2() {
		return getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
	}
	
	public Boolean getApresentarOpcaoAssinaturaFuncionario3() {
		return
		getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)		
		|| getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)
		;
	}
	
	public Boolean getApresentarOpcaoAssinaturaAluno() {
		return getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
	}
	
	public Boolean getApresentarOpcaoPermitirAlunoAssinarDigitalmente() {
		return getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
	}
	
	public Boolean getPermitirAlunoAssinarDigitalmente() {
		if (permitirAlunoAssinarDigitalmente == null) {
			permitirAlunoAssinarDigitalmente = false;
		}
		return permitirAlunoAssinarDigitalmente;
	}
	
	public void setPermitirAlunoAssinarDigitalmente(Boolean permitirAlunoAssinarDigitalmente) {
		this.permitirAlunoAssinarDigitalmente = permitirAlunoAssinarDigitalmente;
	}
	
}
