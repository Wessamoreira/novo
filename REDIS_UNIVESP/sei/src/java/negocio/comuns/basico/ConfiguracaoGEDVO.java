package negocio.comuns.basico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.EntidadeDaoGenerico;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.basico.enumeradores.TipoProvedorAssinaturaEnum;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.dominios.IntegracaoTechCertEnum;
import webservice.nfse.generic.AmbienteEnum;

/**
 * Reponsável por manter os dados da entidade Cidade. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "configuracaoGED")
@EntidadeDaoGenerico(tabela = "configuracaoGED")
public class ConfiguracaoGEDVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private ArquivoVO certificadoDigitalUnidadeEnsinoVO;
	private String senhaCertificadoDigitalUnidadeEnsino;
	private Boolean utilizarAssinaturaUnidadeCertificadora;
	private ArquivoVO certificadoDigitalUnidadeCertificadora;
	private String senhaCertificadoDigitalUnidadeCertificadora;
	private ArquivoVO seloAssinaturaEletronicaVO;
	private TipoProvedorAssinaturaEnum tipoProvedorAssinaturaEnum;
	private AmbienteEnum ambienteProvedorAssinaturaEnum;
	private String usuarioProvedorDeAssinatura;
	private String senhaProvedorDeAssinatura;
	private String tokenProvedorDeAssinatura;	
	private Boolean habilitarIntegracaoCertisign;
	
	private Boolean habilitarIntegracaoImprensaOficial;
	private AmbienteEnum ambienteImprensaOficialEnum;
	private String tokenImprensaOficial;
	private String urlIntegracaoImprensaOficialHomologacao;
	private String urlIntegracaoImprensaOficialProducao;
	
//	private Boolean apresentarSeloDiario;
//	private Boolean apresentarSeloBoletimAcademico;
//	private Boolean apresentarSeloAtaResultadosFinais;
//	private Boolean apresentarSeloEmissaoCertificado;
//	private Boolean apresentarSeloHistorico;
//	private Boolean apresentarSeloExpedicaoDiploma;
//	private Boolean apresentarSeloEmissaoDeclaracao;
//	private Boolean apresentarSeloEmissaoContrato;
//	private Boolean apresentarQrCodeDiario;
//	private Boolean apresentarQrCodeBoletimAcademico;
//	private Boolean apresentarQrCodeAtaResultadosFinais;
//	private Boolean apresentarQrCodeEmissaoCertificado;
//	private Boolean apresentarQrCodeHistorico;
//	private Boolean apresentarQrCodeExpedicaoDiploma;
//	private Boolean apresentarQrCodeEmissaoDeclaracao;
//	private Boolean apresentarQrCodeEmissaoContrato;
//	private Boolean apresentarAssinaturaDigitalizadoFuncionarioDiario;
//	private Boolean apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico;
//	private Boolean apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais;
//	private Boolean apresentarAssinaturaDigitalizadoFuncionarioHistorico;
//	private Boolean apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma;
//	private Boolean apresentarAssinaturaDigitalizadoUnidadeEnsino;
//	private Boolean apresentarAssinaturaDigitalizadoUnidadeCertificadora;
//	private Boolean assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
//
//	private Boolean assinarDocumentacaoAlunoUnidadeEnsino;
//	private Boolean assinarDocumentacaoAlunoUnidadeCertificadora;
//	private Boolean apresentarSeloDocumentoAlunoAssinado;
//	private Boolean apresentarQrCodeDocumentoAlunoAssinado;
//	private Boolean assinarDigitalmenteDocumentacaoAlunoFuncionario;
//	
//	private Boolean assinarDocumentacaoProfessorUnidadeEnsino;
//	private Boolean assinarDocumentacaoProfessorUnidadeCertificadora;
//	private Boolean apresentarSeloDocumentoProfessorAssinado;
//	private Boolean apresentarQrCodeDocumentoProfessorAssinado;	
//	private Boolean assinarDigitalmenteDocumentacaoProfessorFuncionario;
	
	private List<ConfiguracaoGedOrigemVO> configuracaoGedOrigemVOs;
	
	private ConfiguracaoGedOrigemVO configuracaoGedDiarioVO;
	private ConfiguracaoGedOrigemVO configuracaoGedBoletimAcademicoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedHistoricoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedDiplomaVO;
	private ConfiguracaoGedOrigemVO configuracaoGedAtaResultadosFinaisVO;
	private ConfiguracaoGedOrigemVO configuracaoGedCertificadoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedDeclaracaoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedContratoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedDocumentoAlunoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedDocumentoProfessorVO;
	private ConfiguracaoGedOrigemVO configuracaoGedImpostoRendaVO;
	private ConfiguracaoGedOrigemVO configuracaoGedRequerimentoVO;
	private ConfiguracaoGedOrigemVO configuracaoGedUploadInstitucionalVO;
	private ConfiguracaoGedOrigemVO configuracaoGedEstagioVO;
	private ConfiguracaoGedOrigemVO configuracaoGedAtaColacaoGrauVO;	
	private ConfiguracaoGedOrigemVO configuracaoGedPlanoEnsinoVO;	
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
	private Boolean habilitarIntegracaoTechCert;
	private AmbienteEnum ambienteTechCertEnum;
	private String tokenTechCert;
	private String tokenTechCertHomologacao;
	private String apikeyTechCert;
	private String urlIntegracaoTechCertProducao;
	private String urlIntegracaoTechCertHomologacao;
	private TipoProvedorAssinaturaEnum tipoProvedorAssinaturaTechCertEnum;

	public static final long serialVersionUID = 1L;

	public ConfiguracaoGEDVO() {
		super();
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		if (codigo == null) {
			codigo = 0;
		}
		this.codigo = codigo;
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

	public String getSenhaCertificadoDigitalUnidadeEnsino() {
		if (senhaCertificadoDigitalUnidadeEnsino == null) {
			senhaCertificadoDigitalUnidadeEnsino = "";
		}
		return senhaCertificadoDigitalUnidadeEnsino;
	}

	public void setSenhaCertificadoDigitalUnidadeEnsino(String senhaCertificadoDigitalUnidadeEnsino) {
		this.senhaCertificadoDigitalUnidadeEnsino = senhaCertificadoDigitalUnidadeEnsino;
	}

	public Boolean getUtilizarAssinaturaUnidadeCertificadora() {
		if (utilizarAssinaturaUnidadeCertificadora == null) {
			utilizarAssinaturaUnidadeCertificadora = false;
		}
		return utilizarAssinaturaUnidadeCertificadora;
	}

	public void setUtilizarAssinaturaUnidadeCertificadora(Boolean utilizarAssinaturaUnidadeCertificadora) {
		this.utilizarAssinaturaUnidadeCertificadora = utilizarAssinaturaUnidadeCertificadora;
	}

	public ArquivoVO getCertificadoDigitalUnidadeCertificadora() {
		if (certificadoDigitalUnidadeCertificadora == null) {
			certificadoDigitalUnidadeCertificadora = new ArquivoVO();
		}
		return certificadoDigitalUnidadeCertificadora;
	}

	public void setCertificadoDigitalUnidadeCertificadora(ArquivoVO certificadoDigitalUnidadeCertificadora) {
		this.certificadoDigitalUnidadeCertificadora = certificadoDigitalUnidadeCertificadora;
	}

	public ArquivoVO getCertificadoDigitalUnidadeEnsinoVO() {
		if (certificadoDigitalUnidadeEnsinoVO == null) {
			certificadoDigitalUnidadeEnsinoVO = new ArquivoVO();
		}
		return certificadoDigitalUnidadeEnsinoVO;
	}

	public void setCertificadoDigitalUnidadeEnsinoVO(ArquivoVO certificadoDigitalUnidadeEnsinoVO) {
		this.certificadoDigitalUnidadeEnsinoVO = certificadoDigitalUnidadeEnsinoVO;
	}

	public String getSenhaCertificadoDigitalUnidadeCertificadora() {
		if (senhaCertificadoDigitalUnidadeCertificadora == null) {
			senhaCertificadoDigitalUnidadeCertificadora = "";
		}
		return senhaCertificadoDigitalUnidadeCertificadora;
	}

	public void setSenhaCertificadoDigitalUnidadeCertificadora(String senhaCertificadoDigitalUnidadeCertificadora) {
		this.senhaCertificadoDigitalUnidadeCertificadora = senhaCertificadoDigitalUnidadeCertificadora;
	}
	
	public TipoProvedorAssinaturaEnum getTipoProvedorAssinaturaEnum() {
		if (tipoProvedorAssinaturaEnum == null) {
			tipoProvedorAssinaturaEnum = TipoProvedorAssinaturaEnum.ASSINATURA_DIGITAL;
		}
		return tipoProvedorAssinaturaEnum;
	}

	public void setTipoProvedorAssinaturaEnum(TipoProvedorAssinaturaEnum tipoProvedorAssinaturaEnum) {
		this.tipoProvedorAssinaturaEnum = tipoProvedorAssinaturaEnum;
	}

	public AmbienteEnum getAmbienteProvedorAssinaturaEnum() {
		if (ambienteProvedorAssinaturaEnum == null) {
			ambienteProvedorAssinaturaEnum = AmbienteEnum.HOMOLOGACAO;
		}
		return ambienteProvedorAssinaturaEnum;
	}

	public void setAmbienteProvedorAssinaturaEnum(AmbienteEnum ambienteProvedorAssinaturaEnum) {
		this.ambienteProvedorAssinaturaEnum = ambienteProvedorAssinaturaEnum;
	}

	public String getUsuarioProvedorDeAssinatura() {
		if (usuarioProvedorDeAssinatura == null) {
			usuarioProvedorDeAssinatura = "";
		}
		return usuarioProvedorDeAssinatura;
	}

	public void setUsuarioProvedorDeAssinatura(String usuarioProvedorDeAssinatura) {
		this.usuarioProvedorDeAssinatura = usuarioProvedorDeAssinatura;
	}

	public String getSenhaProvedorDeAssinatura() {
		if (senhaProvedorDeAssinatura == null) {
			senhaProvedorDeAssinatura = "";
		}
		return senhaProvedorDeAssinatura;
	}

	public void setSenhaProvedorDeAssinatura(String senhaProvedorDeAssinatura) {
		this.senhaProvedorDeAssinatura = senhaProvedorDeAssinatura;
	}
	
	public String getTokenProvedorDeAssinatura() {
		if (tokenProvedorDeAssinatura == null) {
			tokenProvedorDeAssinatura = "";
		}
		return tokenProvedorDeAssinatura;
	}

	public void setTokenProvedorDeAssinatura(String tokenProvedorDeAssinatura) {
		this.tokenProvedorDeAssinatura = tokenProvedorDeAssinatura;
	}
	
	public Boolean getHabilitarIntegracaoCertisign() {
		if(habilitarIntegracaoCertisign == null) {
			habilitarIntegracaoCertisign = Boolean.FALSE;
		}
		return habilitarIntegracaoCertisign;
	}

	public void setHabilitarIntegracaoCertisign(Boolean habilitarIntegracaoCertisign) {
		this.habilitarIntegracaoCertisign = habilitarIntegracaoCertisign;
	}

	public String getUrlProvedorAssinatura(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		if(provedorDeAssinaturaEnum.isProvedorImprensaOficial()) {
			return  getAmbienteImprensaOficialEnum().isProducao() ? getUrlIntegracaoImprensaOficialProducao() : getUrlIntegracaoImprensaOficialHomologacao();
		}
		return getAmbienteProvedorAssinaturaEnum().isProducao() ? "https://api.portaldeassinaturas.com.br":"https://api-sbx.portaldeassinaturas.com.br";
	}
	
	
	public Boolean getHabilitarIntegracaoImprensaOficial() {
		if(habilitarIntegracaoImprensaOficial == null) {
			habilitarIntegracaoImprensaOficial = Boolean.FALSE;
		}
		return habilitarIntegracaoImprensaOficial;
	}

	public void setHabilitarIntegracaoImprensaOficial(Boolean habilitarIntegracaoImprensaOficial) {
		this.habilitarIntegracaoImprensaOficial = habilitarIntegracaoImprensaOficial;
	}

	public AmbienteEnum getAmbienteImprensaOficialEnum() {
		if(ambienteImprensaOficialEnum == null) {
			ambienteImprensaOficialEnum = AmbienteEnum.HOMOLOGACAO;
		}
		return ambienteImprensaOficialEnum;
	}

	public void setAmbienteImprensaOficialEnum(AmbienteEnum ambienteImprensaOficialEnum) {
		this.ambienteImprensaOficialEnum = ambienteImprensaOficialEnum;
	}

	public String getTokenImprensaOficial() {
		if(tokenImprensaOficial == null) {
			tokenImprensaOficial = "";
		}
		return tokenImprensaOficial;
	}

	public void setTokenImprensaOficial(String tokenImprensaOficial) {
		this.tokenImprensaOficial = tokenImprensaOficial;
	}

	public String getUrlIntegracaoImprensaOficialHomologacao() {
		if(urlIntegracaoImprensaOficialHomologacao == null) {
			urlIntegracaoImprensaOficialHomologacao = "";
		}
		return urlIntegracaoImprensaOficialHomologacao;
	}

	public void setUrlIntegracaoImprensaOficialHomologacao(String urlIntegracaoImprensaOficialHomologacao) {
		this.urlIntegracaoImprensaOficialHomologacao = urlIntegracaoImprensaOficialHomologacao;
	}

	public String getUrlIntegracaoImprensaOficialProducao() {
		if(urlIntegracaoImprensaOficialProducao == null) {
			urlIntegracaoImprensaOficialProducao = "";
		}
		return urlIntegracaoImprensaOficialProducao;
	}

	public void setUrlIntegracaoImprensaOficialProducao(String urlIntegracaoImprensaOficialProducao) {
		this.urlIntegracaoImprensaOficialProducao = urlIntegracaoImprensaOficialProducao;
	}
	
		

//	public Boolean getApresentarSeloDiario() {
//		if (apresentarSeloDiario == null) {
//			apresentarSeloDiario = false;
//		}
//		return apresentarSeloDiario;
//	}
//
//	public void setApresentarSeloDiario(Boolean apresentarSeloDiario) {
//		this.apresentarSeloDiario = apresentarSeloDiario;
//	}
//
//	public Boolean getApresentarSeloBoletimAcademico() {
//		if (apresentarSeloBoletimAcademico == null) {
//			apresentarSeloBoletimAcademico = false;
//		}
//		return apresentarSeloBoletimAcademico;
//	}

//	public void setApresentarSeloBoletimAcademico(Boolean apresentarSeloBoletimAcademico) {
//		this.apresentarSeloBoletimAcademico = apresentarSeloBoletimAcademico;
//	}
//
//	public Boolean getApresentarSeloAtaResultadosFinais() {
//		if (apresentarSeloAtaResultadosFinais == null) {
//			apresentarSeloAtaResultadosFinais = false;
//		}
//		return apresentarSeloAtaResultadosFinais;
//	}
//
//	public void setApresentarSeloAtaResultadosFinais(Boolean apresentarSeloAtaResultadosFinais) {
//		this.apresentarSeloAtaResultadosFinais = apresentarSeloAtaResultadosFinais;
//	}
//
//	public Boolean getApresentarSeloEmissaoCertificado() {
//		if (apresentarSeloEmissaoCertificado == null) {
//			apresentarSeloEmissaoCertificado = false;
//		}
//		return apresentarSeloEmissaoCertificado;
//	}
//
//	public void setApresentarSeloEmissaoCertificado(Boolean apresentarSeloEmissaoCertificado) {
//		this.apresentarSeloEmissaoCertificado = apresentarSeloEmissaoCertificado;
//	}
//
//	public Boolean getApresentarSeloHistorico() {
//		if (apresentarSeloHistorico == null) {
//			apresentarSeloHistorico = false;
//		}
//		return apresentarSeloHistorico;
//	}

//	public void setApresentarSeloHistorico(Boolean apresentarSeloHistorico) {
//		this.apresentarSeloHistorico = apresentarSeloHistorico;
//	}
//
//	public Boolean getApresentarSeloExpedicaoDiploma() {
//		if (apresentarSeloExpedicaoDiploma == null) {
//			apresentarSeloExpedicaoDiploma = false;
//		}
//		return apresentarSeloExpedicaoDiploma;
//	}
//
//	public void setApresentarSeloExpedicaoDiploma(Boolean apresentarSeloExpedicaoDiploma) {
//		this.apresentarSeloExpedicaoDiploma = apresentarSeloExpedicaoDiploma;
//	}	
//
//	public Boolean getApresentarSeloEmissaoDeclaracao() {
//		if (apresentarSeloEmissaoDeclaracao == null) {
//			apresentarSeloEmissaoDeclaracao = false;
//		}
//		return apresentarSeloEmissaoDeclaracao;
//	}
//
//	public void setApresentarSeloEmissaoDeclaracao(Boolean apresentarSeloEmissaoDeclaracao) {
//		this.apresentarSeloEmissaoDeclaracao = apresentarSeloEmissaoDeclaracao;
//	}
//
//	public Boolean getApresentarSeloEmissaoContrato() {
//		if (apresentarSeloEmissaoContrato == null) {
//			apresentarSeloEmissaoContrato = false;
//		}
//		return apresentarSeloEmissaoContrato;
//	}
//
//	public void setApresentarSeloEmissaoContrato(Boolean apresentarSeloEmissaoContrato) {
//		this.apresentarSeloEmissaoContrato = apresentarSeloEmissaoContrato;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoFuncionarioDiario() {
//		if (apresentarAssinaturaDigitalizadoFuncionarioDiario == null) {
//			apresentarAssinaturaDigitalizadoFuncionarioDiario = false;
//		}
//		return apresentarAssinaturaDigitalizadoFuncionarioDiario;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoFuncionarioDiario(Boolean apresentarAssinaturaDigitalizadoFuncionarioDiario) {
//		this.apresentarAssinaturaDigitalizadoFuncionarioDiario = apresentarAssinaturaDigitalizadoFuncionarioDiario;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico() {
//		if (apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico == null) {
//			apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico = false;
//		}
//		return apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico(Boolean apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico) {
//		this.apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico = apresentarAssinaturaDigitalizadoFuncionarioBoletimAcademico;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais() {
//		if (apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais == null) {
//			apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais = false;
//		}
//		return apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais(Boolean apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais) {
//		this.apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais = apresentarAssinaturaDigitalizadoFuncionarioAtaResultadosFinais;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoFuncionarioHistorico() {
//		if (apresentarAssinaturaDigitalizadoFuncionarioHistorico == null) {
//			apresentarAsRequerimentoVO();sinaturaDigitalizadoFuncionarioHistorico = false;
//		}
//		return apresentarAssinaturaDigitalizadoFuncionarioHistorico;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoFuncionarioHistorico(Boolean apresentarAssinaturaDigitalizadoFuncionarioHistorico) {
//		this.apresentarAssinaturaDigitalizadoFuncionarioHistorico = apresentarAssinaturaDigitalizadoFuncionarioHistorico;
//	}


	public ArquivoVO getSeloAssinaturaEletronicaVO() {
		if (seloAssinaturaEletronicaVO == null) {
			seloAssinaturaEletronicaVO = new ArquivoVO();
		}
		return seloAssinaturaEletronicaVO;
	}

	public void setSeloAssinaturaEletronicaVO(ArquivoVO seloAssinaturaEletronicaVO) {
		this.seloAssinaturaEletronicaVO = seloAssinaturaEletronicaVO;
	}

	public Boolean getExisteSelo() {
		return !getSeloAssinaturaEletronicaVO().getNome().trim().isEmpty();
	}

//	public Boolean getApresentarAssinaturaDigitalizadoUnidadeEnsino() {
//		if (apresentarAssinaturaDigitalizadoUnidadeEnsino == null) {
//			apresentarAssinaturaDigitalizadoUnidadeEnsino = false;
//		}
//		return apresentarAssinaturaDigitalizadoUnidadeEnsino;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoUnidadeEnsino(Boolean apresentarAssinaturaDigitalizadoUnidadeEnsino) {
//		this.apresentarAssinaturaDigitalizadoUnidadeEnsino = apresentarAssinaturaDigitalizadoUnidadeEnsino;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoUnidadeCertificadora() {
//		if (apresentarAssinaturaDigitalizadoUnidadeCertificadora == null) {
//			apresentarAssinaturaDigitalizadoUnidadeCertificadora = false;
//		}
//		return apresentarAssinaturaDigitalizadoUnidadeCertificadora;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoUnidadeCertificadora(Boolean apresentarAssinaturaDigitalizadoUnidadeCertificadora) {
//		this.apresentarAssinaturaDigitalizadoUnidadeCertificadora = apresentarAssinaturaDigitalizadoUnidadeCertificadora;
//	}
//	
//	public Boolean getAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada() {
//		if(assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada == null) {
//			assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada = false;
//		}
//		return assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
//	}
//
//	public void setAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada(
//			Boolean assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada) {
//		this.assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada = assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada;
//	}
//
//	public Boolean getApresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma() {
//		if (apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma == null) {
//			apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma = false;
//		}
//		return apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma;
//	}
//
//	public void setApresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma(Boolean apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma) {
//		this.apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma = apresentarAssinaturaDigitalizadoFuncionarioExpedicaoDiploma;
//	}
//
//	public Boolean getAssinarDigitalmenteDocumentosAluno() {
//		return assinarDocumentacaoAlunoUnidadeEnsino || assinarDocumentacaoAlunoUnidadeCertificadora || apresentarSeloDocumentoAlunoAssinado || assinarDigitalmenteDocumentacaoAlunoFuncionario ;
//	}
//
//	public Boolean getAssinarDocumentacaoAlunoUnidadeEnsino() {
//		if(assinarDocumentacaoAlunoUnidadeEnsino == null) {
//			assinarDocumentacaoAlunoUnidadeEnsino = Boolean.TRUE;
//		}
//		return assinarDocumentacaoAlunoUnidadeEnsino;
//	}
//
//	public void setAssinarDocumentacaoAlunoUnidadeEnsino(Boolean assinarDocumentacaoAlunoUnidadeEnsino) {
//		this.assinarDocumentacaoAlunoUnidadeEnsino = assinarDocumentacaoAlunoUnidadeEnsino;
//	}
//
//	public Boolean getAssinarDocumentacaoAlunoUnidadeCertificadora() {
//		if(assinarDocumentacaoAlunoUnidadeCertificadora == null) {
//			assinarDocumentacaoAlunoUnidadeCertificadora = Boolean.TRUE;
//		}
//		return assinarDocumentacaoAlunoUnidadeCertificadora;
//	}
//
//	public void setAssinarDocumentacaoAlunoUnidadeCertificadora(Boolean assinarDocumentacaoAlunoUnidadeCertificadora) {
//		this.assinarDocumentacaoAlunoUnidadeCertificadora = assinarDocumentacaoAlunoUnidadeCertificadora;
//	}
//
//	public Boolean getApresentarSeloDocumentoAlunoAssinado() {
//		if(apresentarSeloDocumentoAlunoAssinado == null) {
//			apresentarSeloDocumentoAlunoAssinado = Boolean.TRUE;
//		}
//		return apresentarSeloDocumentoAlunoAssinado;
//	}
//
//	public void setApresentarSeloDocumentoAlunoAssinado(Boolean apresentarSeloDocumentoAlunoAssinado) {
//		this.apresentarSeloDocumentoAlunoAssinado = apresentarSeloDocumentoAlunoAssinado;
//	}
//
//	public Boolean getAssinarDigitalmenteDocumentacaoAlunoFuncionario() {
//		if(assinarDigitalmenteDocumentacaoAlunoFuncionario == null) {
//			assinarDigitalmenteDocumentacaoAlunoFuncionario = Boolean.TRUE;
//		}
//		return assinarDigitalmenteDocumentacaoAlunoFuncionario;
//	}
//
//	public void setAssinarDigitalmenteDocumentacaoAlunoFuncionario(Boolean assinarDigitalmenteDocumentacaoAlunoFuncionario) {
//		this.assinarDigitalmenteDocumentacaoAlunoFuncionario = assinarDigitalmenteDocumentacaoAlunoFuncionario;
//	}
//	
//	public Boolean getAssinarDocumentacaoProfessorUnidadeEnsino() {
//		if(assinarDocumentacaoProfessorUnidadeEnsino == null) {
//			assinarDocumentacaoProfessorUnidadeEnsino = Boolean.TRUE;
//		}
//		return assinarDocumentacaoProfessorUnidadeEnsino;
//	}
//
//	public void setAssinarDocumentacaoProfessorUnidadeEnsino(Boolean assinarDocumentacaoProfessorUnidadeEnsino) {
//		this.assinarDocumentacaoProfessorUnidadeEnsino = assinarDocumentacaoProfessorUnidadeEnsino;
//	}
//
//	public Boolean getAssinarDocumentacaoProfessorUnidadeCertificadora() {
//		if(assinarDocumentacaoProfessorUnidadeCertificadora == null) {
//			assinarDocumentacaoProfessorUnidadeCertificadora = Boolean.TRUE;
//		}
//		return assinarDocumentacaoProfessorUnidadeCertificadora;
//	}
//
//	public void setAssinarDocumentacaoProfessorUnidadeCertificadora(
//			Boolean assinarDocumentacaoProfessorUnidadeCertificadora) {
//		this.assinarDocumentacaoProfessorUnidadeCertificadora = assinarDocumentacaoProfessorUnidadeCertificadora;
//	}
//
//	public Boolean getApresentarSeloDocumentoProfessorAssinado() {
//		if(apresentarSeloDocumentoProfessorAssinado == null) {
//			apresentarSeloDocumentoProfessorAssinado = Boolean.TRUE;
//		}
//		return apresentarSeloDocumentoProfessorAssinado;
//	}
//
//	public void setApresentarSeloDocumentoProfessorAssinado(Boolean apresentarSeloDocumentoProfessorAssinado) {
//		this.apresentarSeloDocumentoProfessorAssinado = apresentarSeloDocumentoProfessorAssinado;
//	}
//
//	public Boolean getAssinarDigitalmenteDocumentacaoProfessorFuncionario() {
//		if(assinarDigitalmenteDocumentacaoProfessorFuncionario == null) {
//			assinarDigitalmenteDocumentacaoProfessorFuncionario = Boolean.TRUE;
//		}
//		return assinarDigitalmenteDocumentacaoProfessorFuncionario;
//	}
//
//	public void setAssinarDigitalmenteDocumentacaoProfessorFuncionario(Boolean assinarDigitalmenteDocumentacaoProfessorFuncionario) {
//		this.assinarDigitalmenteDocumentacaoProfessorFuncionario = assinarDigitalmenteDocumentacaoProfessorFuncionario;
//	}
//	
//	public Boolean getAssinarDigitalmenteDocumentosProfessor() {
//		return this.assinarDocumentacaoProfessorUnidadeEnsino || this.assinarDocumentacaoProfessorUnidadeCertificadora || this.assinarDigitalmenteDocumentacaoProfessorFuncionario ;
//	}
//	
//	public Boolean getApresentarQrCodeExpedicaoDiploma() {
//		if (apresentarQrCodeExpedicaoDiploma == null) {
//			apresentarQrCodeExpedicaoDiploma = false;
//		}
//		return apresentarQrCodeExpedicaoDiploma;
//	}
//
//	public void setApresentarQrCodeExpedicaoDiploma(Boolean apresentarQrCodeExpedicaoDiploma) {
//		this.apresentarQrCodeExpedicaoDiploma = apresentarQrCodeExpedicaoDiploma;
//	}
//
//	public Boolean getApresentarQrCodeDiario() {
//		if (apresentarQrCodeDiario == null) {
//			apresentarQrCodeDiario = false;
//		}
//		return apresentarQrCodeDiario;
//	}
//
//	public void setApresentarQrCodeDiario(Boolean apresentarQrCodeDiario) {
//		this.apresentarQrCodeDiario = apresentarQrCodeDiario;
//	}
//
//	public Boolean getApresentarQrCodeBoletimAcademico() {
//		if (apresentarQrCodeBoletimAcademico == null) {
//			apresentarQrCodeBoletimAcademico = false;
//		}
//		return apresentarQrCodeBoletimAcademico;
//	}
//
//	public void setApresentarQrCodeBoletimAcademico(Boolean apresentarQrCodeBoletimAcademico) {
//		this.apresentarQrCodeBoletimAcademico = apresentarQrCodeBoletimAcademico;
//	}
//
//	public Boolean getApresentarQrCodeAtaResultadosFinais() {
//		if (apresentarQrCodeAtaResultadosFinais == null) {
//			apresentarQrCodeAtaResultadosFinais = false;
//		}
//		return apresentarQrCodeAtaResultadosFinais;
//	}
//
//	public void setApresentarQrCodeAtaResultadosFinais(Boolean apresentarQrCodeAtaResultadosFinais) {
//		this.apresentarQrCodeAtaResultadosFinais = apresentarQrCodeAtaResultadosFinais;
//	}
//
//	public Boolean getApresentarQrCodeEmissaoCertificado() {
//		if (apresentarQrCodeEmissaoCertificado == null) {
//			apresentarQrCodeEmissaoCertificado = false;
//		}
//		return apresentarQrCodeEmissaoCertificado;
//	}
//
//	public void setApresentarQrCodeEmissaoCertificado(Boolean apresentarQrCodeEmissaoCertificado) {
//		this.apresentarQrCodeEmissaoCertificado = apresentarQrCodeEmissaoCertificado;
//	}
//
//	public Boolean getApresentarQrCodeHistorico() {
//		if (apresentarQrCodeHistorico == null) {
//			apresentarQrCodeHistorico = false;
//		}
//		return apresentarQrCodeHistorico;
//	}
//
//	public void setApresentarQrCodeHistorico(Boolean apresentarQrCodeHistorico) {
//		this.apresentarQrCodeHistorico = apresentarQrCodeHistorico;
//	}
//
//	public Boolean getApresentarQrCodeEmissaoDeclaracao() {
//		if (apresentarQrCodeEmissaoDeclaracao == null) {
//			apresentarQrCodeEmissaoDeclaracao = false;
//		}
//		return apresentarQrCodeEmissaoDeclaracao;
//	}
//
//	public void setApresentarQrCodeEmissaoDeclaracao(Boolean apresentarQrCodeEmissaoDeclaracao) {
//		this.apresentarQrCodeEmissaoDeclaracao = apresentarQrCodeEmissaoDeclaracao;
//	}
//
//	public Boolean getApresentarQrCodeEmissaoContrato() {
//		if (apresentarQrCodeEmissaoContrato == null) {
//			apresentarQrCodeEmissaoContrato = false;
//		}
//		return apresentarQrCodeEmissaoContrato;
//	}
//
//	public void setApresentarQrCodeEmissaoContrato(Boolean apresentarQrCodeEmissaoContrato) {
//		this.apresentarQrCodeEmissaoContrato = apresentarQrCodeEmissaoContrato;
//	}
//
//	public Boolean getApresentarQrCodeDocumentoAlunoAssinado() {
//		if (apresentarQrCodeDocumentoAlunoAssinado == null) {
//			apresentarQrCodeDocumentoAlunoAssinado = false;
//		}
//		return apresentarQrCodeDocumentoAlunoAssinado;
//	}
//
//	public void setApresentarQrCodeDocumentoAlunoAssinado(Boolean apresentarQrCodeDocumentoAlunoAssinado) {
//		this.apresentarQrCodeDocumentoAlunoAssinado = apresentarQrCodeDocumentoAlunoAssinado;
//	}
//
//	public Boolean getApresentarQrCodeDocumentoProfessorAssinado() {
//		if (apresentarQrCodeDocumentoProfessorAssinado == null) {
//			apresentarQrCodeDocumentoProfessorAssinado = false;
//		}
//		return apresentarQrCodeDocumentoProfessorAssinado;
//	}
//
//	public void setApresentarQrCodeDocumentoProfessorAssinado(Boolean apresentarQrCodeDocumentoProfessorAssinado) {
//		this.apresentarQrCodeDocumentoProfessorAssinado = apresentarQrCodeDocumentoProfessorAssinado;
//	}


	public List<ConfiguracaoGedOrigemVO> getConfiguracaoGedOrigemVOs() {
		if(configuracaoGedOrigemVOs == null) {
			configuracaoGedOrigemVOs =  new ArrayList<ConfiguracaoGedOrigemVO>(0);
		}
		return configuracaoGedOrigemVOs;
	}

	public void setConfiguracaoGedOrigemVOs(List<ConfiguracaoGedOrigemVO> configuracaoGedOrigemVOs) {
		this.configuracaoGedOrigemVOs = configuracaoGedOrigemVOs;
	}
	
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedDiarioVO() {
		if(configuracaoGedDiarioVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO))) {
				configuracaoGedDiarioVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)).findFirst().get();
			}else {
				configuracaoGedDiarioVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.DIARIO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedDiarioVO);
			}
		}
		return configuracaoGedDiarioVO;
	}

	public ConfiguracaoGedOrigemVO getConfiguracaoGedBoletimAcademicoVO() {
		if(configuracaoGedBoletimAcademicoVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO))) {
				configuracaoGedBoletimAcademicoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO)).findFirst().get();
			}else {
				configuracaoGedBoletimAcademicoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedBoletimAcademicoVO);
			}			
		}
		return configuracaoGedBoletimAcademicoVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedHistoricoVO() {
		if(configuracaoGedHistoricoVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO))) {
				configuracaoGedHistoricoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)).findFirst().get();
			}else {
				configuracaoGedHistoricoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.HISTORICO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedHistoricoVO);
			}	
		}
		return configuracaoGedHistoricoVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedDiplomaVO() {
		if(configuracaoGedDiplomaVO == null) {			
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA))) {
				configuracaoGedDiplomaVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)).findFirst().get();
			}else {
				configuracaoGedDiplomaVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedDiplomaVO);
			}	
		}
		return configuracaoGedDiplomaVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedAtaResultadosFinaisVO() {
		if(configuracaoGedAtaResultadosFinaisVO == null) {

			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL))) {
				configuracaoGedAtaResultadosFinaisVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL)).findFirst().get();
			}else {
				configuracaoGedAtaResultadosFinaisVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedAtaResultadosFinaisVO);
			}	
		}
		return configuracaoGedAtaResultadosFinaisVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedCertificadoVO() {
		if(configuracaoGedCertificadoVO == null) {


			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO))) {
				configuracaoGedCertificadoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)).findFirst().get();
			}else {
				configuracaoGedCertificadoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedCertificadoVO);
			}	
		}
		return configuracaoGedCertificadoVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedDeclaracaoVO() {
		if(configuracaoGedDeclaracaoVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO))) {
				configuracaoGedDeclaracaoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)).findFirst().get();
			}else {
				configuracaoGedDeclaracaoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedDeclaracaoVO);
			}	
		}
		return configuracaoGedDeclaracaoVO;
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedContratoVO() {
		if(configuracaoGedContratoVO == null) {
			configuracaoGedContratoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO))) {
				configuracaoGedContratoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)).findFirst().get();
			}else {
				configuracaoGedContratoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedContratoVO);
			}
		}
		return configuracaoGedContratoVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedDocumentoAlunoVO() {
		if(configuracaoGedDocumentoAlunoVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO))) {
				configuracaoGedDocumentoAlunoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)).findFirst().get();
			}else {
				configuracaoGedDocumentoAlunoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedDocumentoAlunoVO);
			}
		}
		return configuracaoGedDocumentoAlunoVO;
	}


	public ConfiguracaoGedOrigemVO getConfiguracaoGedDocumentoProfessorVO() {
		if(configuracaoGedDocumentoProfessorVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR))) {
				configuracaoGedDocumentoProfessorVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)).findFirst().get();
			}else {
				configuracaoGedDocumentoProfessorVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedDocumentoProfessorVO);
			}
		}
		return configuracaoGedDocumentoProfessorVO;
	}

	public ConfiguracaoGedOrigemVO getConfiguracaoGedPlanoEnsinoVO() {
		if(configuracaoGedPlanoEnsinoVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO))) {
				configuracaoGedPlanoEnsinoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO)).findFirst().get();
			}else {
				configuracaoGedPlanoEnsinoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedPlanoEnsinoVO);
			}
		}
		return configuracaoGedPlanoEnsinoVO;
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
		switch (tipoOrigemDocumentoAssinadoEnum) {
		case ATA_RESULTADO_FINAL:
			return getConfiguracaoGedAtaResultadosFinaisVO();			
		case BOLETIM_ACADEMICO:
			return getConfiguracaoGedBoletimAcademicoVO();
		case CONTRATO:
			return getConfiguracaoGedContratoVO();
		case DECLARACAO:
			return getConfiguracaoGedDeclaracaoVO();
		case DIARIO:
			return getConfiguracaoGedDiarioVO();
		case DOCUMENTO_ALUNO:
			return getConfiguracaoGedDocumentoAlunoVO();
		case DOCUMENTO_PROFESSOR:
			return getConfiguracaoGedDocumentoProfessorVO();
		case DOCUMENTO_GED:
			return getConfiguracaoGedDocumentoAlunoVO();
		case EMISSAO_CERTIFICADO:
			return getConfiguracaoGedCertificadoVO();
		case DIPLOMA_DIGITAL:
		case DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL:
		case CURRICULO_ESCOLAR_DIGITAL:
		case EXPEDICAO_DIPLOMA:
			return getConfiguracaoGedDiplomaVO();
		case HISTORICO:
		case HISTORICO_DIGITAL:
			return getConfiguracaoGedHistoricoVO();
		case IMPOSTO_RENDA:
			return getConfiguracaoGedImpostoRendaVO();			
		case REQUERIMENTO:
			return getConfiguracaoGedRequerimentoVO();
		case UPLOAD_INSTITUCIONAL:
			return getConfiguracaoGedUploadInstitucionalVO();
		case ESTAGIO:
		case TERMO_ESTAGIO_OBRIGATORIO:
		case TERMO_ESTAGIO_NAO_OBRIGATORIO:
		case TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO:
		case TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO:
			return getConfiguracaoGedEstagioVO();
		case ATA_COLACAO_GRAU:
			return getConfiguracaoGedAtaColacaoGrauVO();
		case PLANO_DE_ENSINO:
			return getConfiguracaoGedPlanoEnsinoVO();
		default:			
			return null;
		}
		
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedImpostoRendaVO() {
		if(configuracaoGedImpostoRendaVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA))) {
				configuracaoGedImpostoRendaVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA)).findFirst().get();
			}else {
				configuracaoGedImpostoRendaVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedImpostoRendaVO);
			}	
		}
		return configuracaoGedImpostoRendaVO;
	}

	public void setConfiguracaoGedImpostoRendaVO(ConfiguracaoGedOrigemVO configuracaoGedImpostoRendaVO) {
		this.configuracaoGedImpostoRendaVO = configuracaoGedImpostoRendaVO;
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedRequerimentoVO() {
		if(configuracaoGedRequerimentoVO == null) {
			configuracaoGedRequerimentoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO))) {
				configuracaoGedRequerimentoVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO)).findFirst().get();
			}else {
				configuracaoGedRequerimentoVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedRequerimentoVO);
			}
		}
		return configuracaoGedRequerimentoVO;
	}

	public void setConfiguracaoGedRequerimentoVO(ConfiguracaoGedOrigemVO configuracaoGedRequerimentoVO) {
		this.configuracaoGedRequerimentoVO = configuracaoGedRequerimentoVO;
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedUploadInstitucionalVO() {
		if(configuracaoGedUploadInstitucionalVO == null) {
			configuracaoGedUploadInstitucionalVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL);
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL))) {
				configuracaoGedUploadInstitucionalVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL)).findFirst().get();
			}else {
				configuracaoGedUploadInstitucionalVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedUploadInstitucionalVO);
			}
		}
		return configuracaoGedUploadInstitucionalVO;
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedEstagioVO() {
		if(configuracaoGedEstagioVO == null) {
			configuracaoGedEstagioVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.ESTAGIO);
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ESTAGIO))) {
				configuracaoGedEstagioVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ESTAGIO)).findFirst().get();
			}else {
				configuracaoGedEstagioVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.ESTAGIO);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedEstagioVO);
			}
		}
		return configuracaoGedEstagioVO;
	}
	
	public void setConfiguracaoGedUploadInstitucionalVO(ConfiguracaoGedOrigemVO configuracaoGedEstagioVO) {
		this.configuracaoGedEstagioVO = configuracaoGedEstagioVO;
	}

	public String getChaveToken(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		if(provedorDeAssinaturaEnum.isProvedorImprensaOficial()) {
			return UteisWebServiceUrl.ACS_Authorization_Token;
		}
		return UteisWebServiceUrl.token;
	}

	public String getValorTokenProvedorAssinatura(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		if(provedorDeAssinaturaEnum.isProvedorImprensaOficial()) {
			return getTokenImprensaOficial();
		}
		return getTokenProvedorDeAssinatura();
	}
	
	public ConfiguracaoGedOrigemVO getConfiguracaoGedAtaColacaoGrauVO() {
		if (configuracaoGedAtaColacaoGrauVO == null) {
			if(getConfiguracaoGedOrigemVOs().stream().anyMatch(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU))) {
				configuracaoGedAtaColacaoGrauVO =  getConfiguracaoGedOrigemVOs().stream().filter(t -> t.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)).findFirst().get();
			}else {
				configuracaoGedAtaColacaoGrauVO =  new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
				getConfiguracaoGedOrigemVOs().add(configuracaoGedAtaColacaoGrauVO);
			}	
		}
		return configuracaoGedAtaColacaoGrauVO;
	}

	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		if (provedorDeAssinaturaEnum == null) {
			provedorDeAssinaturaEnum = ProvedorDeAssinaturaEnum.SEI;
		}
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}

	public Boolean getHabilitarIntegracaoTechCert() {
		if (habilitarIntegracaoTechCert == null) {
			habilitarIntegracaoTechCert = Boolean.FALSE;
		}
		return habilitarIntegracaoTechCert;
	}

	public void setHabilitarIntegracaoTechCert(Boolean habilitarIntegracaoTechCert) {
		this.habilitarIntegracaoTechCert = habilitarIntegracaoTechCert;
	}

	public AmbienteEnum getAmbienteTechCertEnum() {
		if (ambienteTechCertEnum == null) {
			ambienteTechCertEnum = AmbienteEnum.HOMOLOGACAO;
		}
		return ambienteTechCertEnum;
	}

	public void setAmbienteTechCertEnum(AmbienteEnum ambienteTechCertEnum) {
		this.ambienteTechCertEnum = ambienteTechCertEnum;
	}

	public String getTokenTechCert() {
		if (tokenTechCert == null) {
			tokenTechCert =
					getAmbienteTechCertEnum().isProducao()
							? IntegracaoTechCertEnum.TOKEN_PRODUCAO.getValor()
							: IntegracaoTechCertEnum.TOKEN_HOMOLOGACAO.getValor();
		}
		return tokenTechCert;
	}

	public void setTokenTechCert(String tokenTechCert) {
		this.tokenTechCert = tokenTechCert;
	}

	public String getUrlIntegracaoTechCertProducao() {
		if (urlIntegracaoTechCertProducao == null) {
			urlIntegracaoTechCertProducao = "";
		}
		return urlIntegracaoTechCertProducao;
	}

	public void setUrlIntegracaoTechCertProducao(String urlIntegracaoTechCertProducao) {
		this.urlIntegracaoTechCertProducao = urlIntegracaoTechCertProducao;
	}

	public String getUrlIntegracaoTechCertHomologacao() {
		if (urlIntegracaoTechCertHomologacao == null){
			urlIntegracaoTechCertHomologacao = "";
		}
		return urlIntegracaoTechCertHomologacao;
	}

	public void setUrlIntegracaoTechCertHomologacao(String urlIntegracaoTechCertHomologacao) {
		this.urlIntegracaoTechCertHomologacao = urlIntegracaoTechCertHomologacao;
	}

	public TipoProvedorAssinaturaEnum getTipoProvedorAssinaturaTechCertEnum() {
		if (tipoProvedorAssinaturaTechCertEnum == null) {
			tipoProvedorAssinaturaTechCertEnum = TipoProvedorAssinaturaEnum.ASSINATURA_ELETRONICA;
		}
		return tipoProvedorAssinaturaTechCertEnum;
	}

	public void setTipoProvedorAssinaturaTechCertEnum(TipoProvedorAssinaturaEnum tipoProvedorAssinaturaTechCertEnum) {
		this.tipoProvedorAssinaturaTechCertEnum = tipoProvedorAssinaturaTechCertEnum;
	}

	public Boolean getApresentarOpcaoPermitirEditarConfiguracaoIntegracao(){
		return getHabilitarIntegracaoCertisign() || getHabilitarIntegracaoTechCert() || getHabilitarIntegracaoImprensaOficial();
	}

	public String getApikeyTechCert() {
		if (apikeyTechCert == null) {
			apikeyTechCert = "";
		}
		return apikeyTechCert;
	}

	public void setApikeyTechCert(String apikeyTechCert) {
		this.apikeyTechCert = apikeyTechCert;
	}

	public String getTokenTechCertHomologacao() {
		if (tokenTechCertHomologacao == null) {
			tokenTechCertHomologacao = "";
		}
		return tokenTechCertHomologacao;
	}

	public void setTokenTechCertHomologacao(String tokenTechCertHomologacao) {
		this.tokenTechCertHomologacao = tokenTechCertHomologacao;
	}
}