package negocio.comuns.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoExpedicaoDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 * Reponsável por manter os dados da entidade ExpedicaoDiploma. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ExpedicaoDiplomaVO extends SuperVO {

    private Integer codigo;
    private Date dataExpedicao;
    private Date dataPublicacaoDiarioOficial;
    private String via;
    private boolean segundaVia;
    private String numeroRegistroDiplomaViaAnterior;
    private String numeroProcessoViaAnterior;
    private String numeroProcesso;	
    private String numeroRegistroDiploma;	
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO matricula;
    private GradeCurricularVO gradeCurricularVO;
    private FuncionarioVO funcionarioPrimarioVO;
    private FuncionarioVO funcionarioSecundarioVO;
    private FuncionarioVO funcionarioTerceiroVO;
    private FuncionarioVO funcionarioQuartoVO;
    private FuncionarioVO funcionarioQuintoVO;
    private CargoVO cargoReitorRegistroDiplomaViaAnterior;    
    private CargoVO cargoSecretariaRegistroDiplomaViaAnterior;   
    private CargoVO cargoFuncionarioPrincipalVO;
    private CargoVO cargoFuncionarioSecundarioVO;
    private CargoVO cargoFuncionarioTerceiroVO;
    private CargoVO cargoFuncionarioQuartoVO;
    private CargoVO cargoFuncionarioQuintoVO; 
    private String tituloFuncionarioPrincipal;
    private String tituloFuncionarioSecundario;
    private String tituloFuncionarioTerceiro;
    private String tituloFuncionarioQuarto;
    private String tituloFuncionarioQuinto;
    private UnidadeEnsinoVO unidadeEnsinoCertificadora;
    private Boolean utilizarUnidadeMatriz;
    private String layoutDiploma;
    private Date dataRegistroDiplomaViaAnterior;
    private String dataRegistroDiplomaViaAnterior_Apresentar;	
    private FuncionarioVO reitorRegistroDiplomaViaAnterior;
    private FuncionarioVO secretariaRegistroDiplomaViaAnterior;
    private String nomeUnidadeEnsinoViaAnterior;
    private String titulacaoMasculinoApresentarDiploma;
    private String titulacaoFemininoApresentarDiploma;
    private String serial;
    private String observacao;

    private List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs; 
    private DocumentoAssinadoVO documentoAssinadoVO;
    private List<String> listaErroGeracaoXML;
    private Boolean informarCamposLivroRegistradora;
	private Boolean gerarXMLDiploma;
    private Integer cargaHorariaTotal;
    private Integer cargaHorariaCursada;
    private String percentualCHIntegralizacaoMatricula;
    private String motivoRejeicaoDiplomaDigital;
    private String codigoValidacaoDiplomaDigital;
    private String situacaoApresentar;
    private Boolean anulado;
    private TextoPadraoDeclaracaoVO textoPadrao;
    private Boolean novaGeracaoRepresentacaoVisualDiplomaDigital;
    private Date dataRegistroDiploma;
    
    //atributos transient 
    private Boolean possuiDiploma ;  
	private String erro;	
	private Boolean possuiErro;	
	private Boolean selecionado;
//	private ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital;
	private CursoCoordenadorVO cursoCoordenadorVO;
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private Boolean emitidoPorDecisaoJudicial;
	private Boolean emitidoPorProcessoTransferenciaAssistida;
	
	private String nomeIesPTA;
	private String cnpjPTA;
	private Boolean possuiCodigoMecPTA;
	private Integer codigoMecPTA;
	private String cepPTA;
	private CidadeVO cidadePTA;
	private String logradouroPTA;
	private String numeroPTA;
	private String complementoPTA;
	private String bairroPTA;
	private TipoAutorizacaoEnum tipoDescredenciamentoPTA;
	private String numeroDescredenciamentoPTA;
	private Date dataDescredenciamentoPTA;
	private String veiculoPublicacaoDescredenciamentoPTA;
	private Date dataPublicacaoDescredenciamentoPTA;
	private Integer secaoPublicacaoDescredenciamentoPTA;
	private Integer paginaPublicacaoDescredenciamentoPTA;
	private Integer numeroDOUDescredenciamentoPTA;
	
	private String nomeJuizDecisaoJudicial;
	private String numeroProcessoDecisaoJudicial;
	private String decisaoJudicial;
	private String informacoesAdicionaisDecisaoJudicial;
//	private List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs;
    
	private VersaoDiplomaDigitalEnum versaoDiploma;
	private String livroRegistradora;
	private String folhaReciboRegistradora;
	
	/**
	 * ATRIBUTO TRASIENTE
	 */
	private Boolean imprimirContrato;
	private String abrirModalVerso;
	private Boolean abrirModalOK;
	private Boolean gerarXMLDiplomaLote;
	private ConsistirException consistirException;
	
	private Date dataCadastro;
	private UsuarioVO responsavelCadastro;
	private Date dataAnulacao;
	private String anotacaoAnulacao;
	private String idDiplomaDigital;
	private String idDadosRegistrosDiplomaDigital;
	private UsuarioVO responsavelAnulacao;
	
	/**
	 * ATRIBUTO TRASIENTE
	 */
	private DocumentoAssinadoVO diplomaDigital;
	private DocumentoAssinadoVO historicoDigital;
	private DocumentoAssinadoVO documentacaoAcademicaDigital;
	private Boolean existeExpedicaoDiplomaSuperior;
	
	/**
	 *ATRIBUTO TRANSIENTE 
	 */
	private Map<String, Object> fieldsPersonalizados;
	private Boolean existeDiplomaDigitalGerado;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ExpedicaoDiploma</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ExpedicaoDiplomaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>ExpedicaoDiplomaVO</code>.
     */
    public static void validarUnicidade(List<ExpedicaoDiplomaVO> lista, ExpedicaoDiplomaVO obj) throws ConsistirException {
        for (ExpedicaoDiplomaVO repetido : lista) {
        }
    }

    public void verificarSegundaVia() {
        if ((!getVia().equals("") && !getVia().equals("1")) && (getMatricula().getCurso().getNivelEducacional().equals("SU"))) {
            setSegundaVia(true);
        } else {
            setSegundaVia(false);
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setVia(getVia().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
        setDataExpedicao(new Date());
        setVia("");
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>ExpedicaoDiploma</code>).
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>ExpedicaoDiploma</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public String getVia() {
        if (via == null) {
            via = "";
        }
        return (via);
    }

    public void setVia(String via) {
        this.via = via;
    }

    public Date getDataPublicacaoDiarioOficial() {
		return dataPublicacaoDiarioOficial;
	}

	public void setDataPublicacaoDiarioOficial(Date dataPublicacaoDiarioOficial) {
		this.dataPublicacaoDiarioOficial = dataPublicacaoDiarioOficial;
	}

	public Date getDataExpedicao() {
        if (dataExpedicao == null) {
            dataExpedicao = new Date();
        }
        return (dataExpedicao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataExpedicao_Apresentar() {
        return (Uteis.getData(dataExpedicao));
    }

    public void setDataExpedicao(Date dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public GradeCurricularVO getGradeCurricularVO() {
        if (gradeCurricularVO == null) {
            gradeCurricularVO = new GradeCurricularVO();
        }
        return gradeCurricularVO;
    }

    public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
        this.gradeCurricularVO = gradeCurricularVO;
    }

    public boolean getSegundaVia() {
        return segundaVia;
    }

    public void setSegundaVia(boolean segundaVia) {
        this.segundaVia = segundaVia;
    }

    public String getNumeroRegistroDiplomaViaAnterior() {
        if (numeroRegistroDiplomaViaAnterior == null) {
            numeroRegistroDiplomaViaAnterior = "";
        }
        return numeroRegistroDiplomaViaAnterior;
    }

    public void setNumeroRegistroDiplomaViaAnterior(String numeroRegistroDiplomaViaAnterior) {
        this.numeroRegistroDiplomaViaAnterior = numeroRegistroDiplomaViaAnterior;
    }

    public String getNumeroProcessoViaAnterior() {
        if (numeroProcessoViaAnterior == null) {
            numeroProcessoViaAnterior = "";
        }
        return numeroProcessoViaAnterior;
    }

    public void setNumeroProcessoViaAnterior(String numeroProcessoViaAnterior) {
        this.numeroProcessoViaAnterior = numeroProcessoViaAnterior;
    }

    /**
     * @return the funcionarioPrimarioVO
     */
    public FuncionarioVO getFuncionarioPrimarioVO() {
        if (funcionarioPrimarioVO == null) {
            funcionarioPrimarioVO = new FuncionarioVO();
        }
        return funcionarioPrimarioVO;
    }

    /**
     * @param funcionarioPrimarioVO the funcionarioPrimarioVO to set
     */
    public void setFuncionarioPrimarioVO(FuncionarioVO funcionarioPrimarioVO) {
        this.funcionarioPrimarioVO = funcionarioPrimarioVO;
    }

    /**
     * @return the funcionarioSecundarioVO
     */
    public FuncionarioVO getFuncionarioSecundarioVO() {
        if (funcionarioSecundarioVO == null) {
            funcionarioSecundarioVO = new FuncionarioVO();
        }
        return funcionarioSecundarioVO;
    }

    /**
     * @param funcionarioSecundarioVO the funcionarioSecundarioVO to set
     */
    public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
        this.funcionarioSecundarioVO = funcionarioSecundarioVO;
    }

    public FuncionarioVO getFuncionarioTerceiroVO() {
        if (funcionarioTerceiroVO == null) {
            funcionarioTerceiroVO = new FuncionarioVO();
        }
        return funcionarioTerceiroVO;
    }

    public void setFuncionarioTerceiroVO(FuncionarioVO funcionarioTerceiroVO) {
        this.funcionarioTerceiroVO = funcionarioTerceiroVO;
    }

	public UnidadeEnsinoVO getUnidadeEnsinoCertificadora() {
		if(unidadeEnsinoCertificadora == null){
			unidadeEnsinoCertificadora = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoCertificadora;
	}

	public void setUnidadeEnsinoCertificadora(UnidadeEnsinoVO unidadeEnsinoCertificadora) {
		this.unidadeEnsinoCertificadora = unidadeEnsinoCertificadora;
	}

	public Boolean getUtilizarUnidadeMatriz() {
		if(utilizarUnidadeMatriz == null){
			utilizarUnidadeMatriz = Boolean.FALSE;
		}
		return utilizarUnidadeMatriz;
	}

	public void setUtilizarUnidadeMatriz(Boolean utilizarUnidadeMatriz) {
		this.utilizarUnidadeMatriz = utilizarUnidadeMatriz;
	}

	public String getLayoutDiploma() {
		if (layoutDiploma == null) {
			layoutDiploma = "";
		}
		return layoutDiploma;
	}

	public void setLayoutDiploma(String layoutDiploma) {
		this.layoutDiploma = layoutDiploma;
	}

	

	public CargoVO getCargoFuncionarioPrincipalVO() {
		if (cargoFuncionarioPrincipalVO == null) {
			cargoFuncionarioPrincipalVO = new CargoVO();
		}
		return cargoFuncionarioPrincipalVO;
	}

	public void setCargoFuncionarioPrincipalVO(CargoVO cargoFuncionarioPrincipalVO) {
		this.cargoFuncionarioPrincipalVO = cargoFuncionarioPrincipalVO;
	}

	public CargoVO getCargoFuncionarioSecundarioVO() {
		if (cargoFuncionarioSecundarioVO == null) {
			cargoFuncionarioSecundarioVO = new CargoVO();
		}
		return cargoFuncionarioSecundarioVO;
	}

	public void setCargoFuncionarioSecundarioVO(CargoVO cargoFuncionarioSecundarioVO) {
		this.cargoFuncionarioSecundarioVO = cargoFuncionarioSecundarioVO;
	}

	public CargoVO getCargoFuncionarioTerceiroVO() {
		if (cargoFuncionarioTerceiroVO == null) {
			cargoFuncionarioTerceiroVO = new CargoVO();
		}
		return cargoFuncionarioTerceiroVO;
	}

	public void setCargoFuncionarioTerceiroVO(CargoVO cargoFuncionarioTerceiroVO) {
		this.cargoFuncionarioTerceiroVO = cargoFuncionarioTerceiroVO;
	}

	public String getTituloFuncionarioPrincipal() {
		if (tituloFuncionarioPrincipal == null) {
			tituloFuncionarioPrincipal = "";
		}
		return tituloFuncionarioPrincipal;
	}

	public void setTituloFuncionarioPrincipal(String tituloFuncionarioPrincipal) {
		this.tituloFuncionarioPrincipal = tituloFuncionarioPrincipal;
	}

	public String getTituloFuncionarioSecundario() {
		if (tituloFuncionarioSecundario == null) {
			tituloFuncionarioSecundario = "";
		}
		return tituloFuncionarioSecundario;
	}

	public void setTituloFuncionarioSecundario(String tituloFuncionarioSecundario) {
		this.tituloFuncionarioSecundario = tituloFuncionarioSecundario;
	}

	public String getTituloFuncionarioTerceiro() {
		if (tituloFuncionarioTerceiro == null) {
			tituloFuncionarioTerceiro = "";
		}
		return tituloFuncionarioTerceiro;
	}

	public void setTituloFuncionarioTerceiro(String tituloFuncionarioTerceiro) {
		this.tituloFuncionarioTerceiro = tituloFuncionarioTerceiro;
	}

	public String getNumeroProcesso() {
		if (numeroProcesso == null) {
			numeroProcesso = "";
		}
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String gerarNumeroProcesso(RequerimentoVO requerimento) {
		if (requerimento.getCodigo().intValue() > 0) {
			String nrProcesso = "";
			nrProcesso = Uteis.getAno(requerimento.getData());
			int mes = Uteis.getMesData(requerimento.getData());
			String mesStr = String.valueOf(mes);
			if (mes < 10) {
				mesStr = "0" + String.valueOf(mes);
			}			
			nrProcesso += mesStr;
			nrProcesso += requerimento.getCodigo().toString();
			setNumeroProcesso(nrProcesso.replaceAll("/", ""));
			return nrProcesso;
		}	
		setNumeroProcesso("");
		return "";
	}    

    /**
     * @return the observacaoComplementarVOs
     */
    public List<ObservacaoComplementarDiplomaVO> getObservacaoComplementarDiplomaVOs() {
        if (observacaoComplementarDiplomaVOs == null) {
            observacaoComplementarDiplomaVOs = new ArrayList<ObservacaoComplementarDiplomaVO>();
        }
        return observacaoComplementarDiplomaVOs;
    }

    /**
     * @param observacaoComplementarVOs the observacaoComplementarVOs to set
     */
    public void setObservacaoComplementarDiplomaVOs(List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs) {
        this.observacaoComplementarDiplomaVOs = observacaoComplementarDiplomaVOs;
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma1() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (i == 0) {
                return obs;
            }
            i++;
            obs.getObservacaoComplementar().getObservacao();
        }
        return new ObservacaoComplementarDiplomaVO();
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma2() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (i == 1) {
                return obs;
            }
            i++;
        }
        return new ObservacaoComplementarDiplomaVO();
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma3() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (i == 2) {
                return obs;
            }
            i++;
        }
        return new ObservacaoComplementarDiplomaVO();
    }

	public String getNumeroRegistroDiploma() {
		if(numeroRegistroDiploma == null ) {
			numeroRegistroDiploma ="";
		}
		return numeroRegistroDiploma;
	}

	public void setNumeroRegistroDiploma(String numeroRegistroDiploma) {
		this.numeroRegistroDiploma = numeroRegistroDiploma;
	}
	

	public Date getDataRegistroDiplomaViaAnterior() {
		if (dataRegistroDiplomaViaAnterior == null) {
			dataRegistroDiplomaViaAnterior = new Date();
		}
		return dataRegistroDiplomaViaAnterior;
	}

	public void setDataRegistroDiplomaViaAnterior(Date dataRegistroDiplomaViaAnterior) {
		this.dataRegistroDiplomaViaAnterior = dataRegistroDiplomaViaAnterior;
	}

	public FuncionarioVO getReitorRegistroDiplomaViaAnterior() {
		if (reitorRegistroDiplomaViaAnterior == null) {
			reitorRegistroDiplomaViaAnterior = new FuncionarioVO();
		}
		return reitorRegistroDiplomaViaAnterior;
	}

	public void setReitorRegistroDiplomaViaAnterior(FuncionarioVO reitorRegistroDiplomaViaAnterior) {
		this.reitorRegistroDiplomaViaAnterior = reitorRegistroDiplomaViaAnterior;
	}

	public FuncionarioVO getSecretariaRegistroDiplomaViaAnterior() {
		if (secretariaRegistroDiplomaViaAnterior == null) {
			secretariaRegistroDiplomaViaAnterior = new FuncionarioVO();
		}
		return secretariaRegistroDiplomaViaAnterior;
	}

	public void setSecretariaRegistroDiplomaViaAnterior(FuncionarioVO secretariaRegistroDiplomaViaAnterior) {
		this.secretariaRegistroDiplomaViaAnterior = secretariaRegistroDiplomaViaAnterior;
	}

	public String getNomeUnidadeEnsinoViaAnterior() {
		if (nomeUnidadeEnsinoViaAnterior == null) {
			nomeUnidadeEnsinoViaAnterior = "";
		}
		return nomeUnidadeEnsinoViaAnterior;
	}

	public void setNomeUnidadeEnsinoViaAnterior(String nomeUnidadeEnsinoViaAnterior) {
		this.nomeUnidadeEnsinoViaAnterior = nomeUnidadeEnsinoViaAnterior;
	}	

	public String getDataRegistroDiplomaViaAnterior_Apresentar() {
		if (dataRegistroDiplomaViaAnterior_Apresentar == null) {
			dataRegistroDiplomaViaAnterior_Apresentar = "";
		}
		dataRegistroDiplomaViaAnterior_Apresentar = (Uteis.getDataCidadeDiaMesPorExtensoEAno("", this.getDataRegistroDiplomaViaAnterior(), true));
		return dataRegistroDiplomaViaAnterior_Apresentar;
	}

	public void setDataRegistroDiplomaViaAnterior_Apresentar(String dataRegistroDiplomaViaAnterior_Apresentar) {
		this.dataRegistroDiplomaViaAnterior_Apresentar = dataRegistroDiplomaViaAnterior_Apresentar;
	}

	public CargoVO getCargoReitorRegistroDiplomaViaAnterior() {
		if (cargoReitorRegistroDiplomaViaAnterior == null) {
			cargoReitorRegistroDiplomaViaAnterior = new CargoVO();
		}
		return cargoReitorRegistroDiplomaViaAnterior;
	}

	public void setCargoReitorRegistroDiplomaViaAnterior(CargoVO cargoReitorRegistroDiplomaViaAnterior) {
		this.cargoReitorRegistroDiplomaViaAnterior = cargoReitorRegistroDiplomaViaAnterior;
	}
	
	public CargoVO getCargoSecretariaRegistroDiplomaViaAnterior() {
		if (cargoSecretariaRegistroDiplomaViaAnterior == null) {
			cargoSecretariaRegistroDiplomaViaAnterior = new CargoVO();
		}
		return cargoSecretariaRegistroDiplomaViaAnterior;
	}

	public void setCargoSecretariaRegistroDiplomaViaAnterior(CargoVO cargoSecretariaRegistroDiplomaViaAnterior) {
		this.cargoSecretariaRegistroDiplomaViaAnterior = cargoSecretariaRegistroDiplomaViaAnterior;
	}

	public String getTitulacaoMasculinoApresentarDiploma() {
		if (titulacaoMasculinoApresentarDiploma == null || titulacaoMasculinoApresentarDiploma.equals("")) {
			titulacaoMasculinoApresentarDiploma = this.getMatricula().getCurso().getTitulacaoMasculinoApresentarDiploma();
		}
		return titulacaoMasculinoApresentarDiploma;
	}

	public void setTitulacaoMasculinoApresentarDiploma(String titulacaoMasculinoApresentarDiploma) {
		this.titulacaoMasculinoApresentarDiploma = titulacaoMasculinoApresentarDiploma;
	}

	public String getTitulacaoFemininoApresentarDiploma() {
		if (titulacaoFemininoApresentarDiploma == null || titulacaoFemininoApresentarDiploma.equals("")) {
			titulacaoFemininoApresentarDiploma = this.getMatricula().getCurso().getTitulacaoFemininoApresentarDiploma();
		}
		return titulacaoFemininoApresentarDiploma;
	}

	public void setTitulacaoFemininoApresentarDiploma(String titulacaoFemininoApresentarDiploma) {
		this.titulacaoFemininoApresentarDiploma = titulacaoFemininoApresentarDiploma;
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

	public String getSerial() {
		if (serial == null) {
			serial = "";
		}
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
		
	public enum MascaraTagNumeroExpedicaoDiploma {
		MATRICULA("MATRICULA", "Matricula", 1), 
		ANO("ANO", "Ano", 2), 
		SEMESTRE("SEMESTRE", "Semestre", 3), 
		NR_LIVRO_EXPEDICAO("NR_LIVRO_EXPEDICAO", "Número Livro Expedição Diploma", 4), 
		ABREV_NOME_CURSO("ABREV_NOME_CURSO", "Abreviatura do Curso", 5),
		NR_PROCESSO("NR_PROCESSO", "Número Processo", 6),
		CODIGO_CURSO("CODIGO_CURSO", "Código do Curso", 7),
		CODIGO_IES("CODIGO_IES", "Código IES", 8),
		CODIGO_EMEC_CURSO("CODIGO_EMEC_CURSO", "Código EMEC do Curso", 9),
		CODIGO_EMEC_IES("CODIGO_EMEC_IES", "Código EMEC da IES", 10),
		NR_LIVRO_REGISTRO_DIPLOMA("NR_LIVRO_REGISTRO_DIPLOMA", "Número do Livro", 11),
		NR_FOLHA_LIVRO("NR_FOLHA_LIVRO", "Número Folha do Livro", 12),
		ANO_ATUAL("ANO_ATUAL", "Ano Atual", 13),
		SEMESTRE_ATUAL("SEMESTRE_ATUAL", "Semestre Atual", 14);

		private String valor;
		private String descricao;
		private int num;

		private MascaraTagNumeroExpedicaoDiploma(String valor, String descricao, int num) {
			this.valor = valor;
			this.descricao = descricao;
			this.num = num;
		}

		public String getValor() {
			return valor;
		}

		public String getDescricao() {
			return descricao;
		}

		public int getNum() {
			return num;
		}
	}

	public List<String> getListaTagMascaraNumeroRegistroExpedicaoDiploma() {
		return Stream.of(MascaraTagNumeroExpedicaoDiploma.values()).map(MascaraTagNumeroExpedicaoDiploma::getValor).collect(Collectors.toList());
	}

	public List<String> getListaTagMascaraNumeroProcessoExpedicaoDiploma() {
		return Stream.of(MascaraTagNumeroExpedicaoDiploma.values()).filter(p -> !p.equals(MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO)).map(MascaraTagNumeroExpedicaoDiploma::getValor).collect(Collectors.toList());
	}

	public Boolean getPossuiDiploma() {
		if (possuiDiploma == null) {
			possuiDiploma = false;
		}
		return possuiDiploma;
	}

	public void setPossuiDiploma(Boolean possuiDiploma) {
		this.possuiDiploma = possuiDiploma;
	}

	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public Boolean getPossuiErro() {
		if (possuiErro == null) {
			possuiErro = Boolean.FALSE;
		}
		return possuiErro;
	}

	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	private Boolean possuiDocumentoAssinadoPendente;

	public Boolean getPossuiDocumentoAssinadoPendente() {
		if (possuiDocumentoAssinadoPendente == null) {
			possuiDocumentoAssinadoPendente = Boolean.FALSE;
		}
		return possuiDocumentoAssinadoPendente;
	}

	public void setPossuiDocumentoAssinadoPendente(Boolean possuiDocumentoAssinadoPendente) {
		this.possuiDocumentoAssinadoPendente = possuiDocumentoAssinadoPendente;
	}

	public FuncionarioVO getFuncionarioQuartoVO() {
		if (funcionarioQuartoVO == null) {
			funcionarioQuartoVO = new FuncionarioVO();
		}
		return funcionarioQuartoVO;
	}

	public void setFuncionarioQuartoVO(FuncionarioVO funcionarioQuartoVO) {
		this.funcionarioQuartoVO = funcionarioQuartoVO;
	}

	public FuncionarioVO getFuncionarioQuintoVO() {
		if (funcionarioQuintoVO == null) {
			funcionarioQuintoVO = new FuncionarioVO();
		}
		return funcionarioQuintoVO;
	}

	public void setFuncionarioQuintoVO(FuncionarioVO funcionarioQuintoVO) {
		this.funcionarioQuintoVO = funcionarioQuintoVO;
	}

	public CargoVO getCargoFuncionarioQuartoVO() {
		if (cargoFuncionarioQuartoVO == null) {
			cargoFuncionarioQuartoVO = new CargoVO();
		}
		return cargoFuncionarioQuartoVO;
	}

	public void setCargoFuncionarioQuartoVO(CargoVO cargoFuncionarioQuartoVO) {
		this.cargoFuncionarioQuartoVO = cargoFuncionarioQuartoVO;
	}

	public CargoVO getCargoFuncionarioQuintoVO() {
		if (cargoFuncionarioQuintoVO == null) {
			cargoFuncionarioQuintoVO = new CargoVO();
		}
		return cargoFuncionarioQuintoVO;
	}

	public void setCargoFuncionarioQuintoVO(CargoVO cargoFuncionarioQuintoVO) {
		this.cargoFuncionarioQuintoVO = cargoFuncionarioQuintoVO;
	}

	public String getTituloFuncionarioQuarto() {
		if (tituloFuncionarioQuarto == null) {
			tituloFuncionarioQuarto = "";
		}
		return tituloFuncionarioQuarto;
	}

	public void setTituloFuncionarioQuarto(String tituloFuncionarioQuarto) {
		this.tituloFuncionarioQuarto = tituloFuncionarioQuarto;
	}

	public String getTituloFuncionarioQuinto() {
		if (tituloFuncionarioQuinto == null) {
			tituloFuncionarioQuinto = "";
		}
		return tituloFuncionarioQuinto;
	}

	public void setTituloFuncionarioQuinto(String tituloFuncionarioQuinto) {
		this.tituloFuncionarioQuinto = tituloFuncionarioQuinto;
	}

	public List<String> getListaErroGeracaoXML() {
		if (listaErroGeracaoXML == null) {
			listaErroGeracaoXML = new ArrayList<String>();
		}
		return listaErroGeracaoXML;
	}

	public void setListaErroGeracaoXML(List<String> listaErroGeracaoXML) {
		this.listaErroGeracaoXML = listaErroGeracaoXML;
	}

	public Boolean getInformarCamposLivroRegistradora() {
		if (informarCamposLivroRegistradora == null) {
			informarCamposLivroRegistradora = false;
		}
		return informarCamposLivroRegistradora;
	}

	public void setInformarCamposLivroRegistradora(Boolean informarCamposLivroRegistradora) {
		this.informarCamposLivroRegistradora = informarCamposLivroRegistradora;
	}

	public Boolean getGerarXMLDiploma() {
		if (gerarXMLDiploma == null) {
			gerarXMLDiploma = false;
		}
		return gerarXMLDiploma;
	}

	public void setGerarXMLDiploma(Boolean gerarXMLDiploma) {
		this.gerarXMLDiploma = gerarXMLDiploma;
	}

	public Integer getCargaHorariaTotal() {
		if (cargaHorariaTotal == null) {
			cargaHorariaTotal = 0;
		}
		return cargaHorariaTotal;
	}

	public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	public Integer getCargaHorariaCursada() {
		if (cargaHorariaCursada == null) {
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public String getPercentualCHIntegralizacaoMatricula() {
		if (percentualCHIntegralizacaoMatricula == null) {
			percentualCHIntegralizacaoMatricula = "0.0";
		}
		return percentualCHIntegralizacaoMatricula;
	}

	public void setPercentualCHIntegralizacaoMatricula(String percentualCHIntegralizacaoMatricula) {
		this.percentualCHIntegralizacaoMatricula = percentualCHIntegralizacaoMatricula;
	}

	public String getMotivoRejeicaoDiplomaDigital() {
		if (motivoRejeicaoDiplomaDigital == null) {
			motivoRejeicaoDiplomaDigital = "";
		}
		return motivoRejeicaoDiplomaDigital;
	}

	public void setMotivoRejeicaoDiplomaDigital(String motivoRejeicaoDiplomaDigital) {
		this.motivoRejeicaoDiplomaDigital = motivoRejeicaoDiplomaDigital;
	}

	public String getCodigoValidacaoDiplomaDigital() {
		return codigoValidacaoDiplomaDigital;
	}

	public void setCodigoValidacaoDiplomaDigital(String codigoValidacaoDiplomaDigital) {
		this.codigoValidacaoDiplomaDigital = codigoValidacaoDiplomaDigital;
	}

	public String getSituacaoApresentar() {
		if (situacaoApresentar == null) {
			if (!getAnulado()) {
				if (Uteis.isAtributoPreenchido(getDiplomaDigital())) {
					if (Uteis.isAtributoPreenchido(getDiplomaDigital().getListaDocumentoAssinadoPessoa())) {
						if (getDiplomaDigital().getListaDocumentoAssinadoPessoa().stream().allMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO))) {
							situacaoApresentar = SituacaoExpedicaoDiplomaEnum.ATIVO.getDescricao();
						} else if (getDiplomaDigital().getListaDocumentoAssinadoPessoa().stream().allMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
							situacaoApresentar = SituacaoExpedicaoDiplomaEnum.PENDENTE.getDescricao();
						} else if (getDiplomaDigital().getListaDocumentoAssinadoPessoa().stream().anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO))) {
							situacaoApresentar = SituacaoExpedicaoDiplomaEnum.ANULADO.getDescricao();
						}
					}
				} else {
					if(getGerarXMLDiploma()) {
						situacaoApresentar = SituacaoExpedicaoDiplomaEnum.AGUARDANDO_GERACAO_XML.getDescricao();
					}else {
						situacaoApresentar = SituacaoExpedicaoDiplomaEnum.EXPEDIDO.getDescricao();
					}
				}
			} else {
				situacaoApresentar = SituacaoExpedicaoDiplomaEnum.ANULADO.getDescricao();
			}
		}
		return situacaoApresentar;
	}

	public void setSituacaoApresentar(String situacaoApresentar) {
		this.situacaoApresentar = situacaoApresentar;
	}

	public Boolean getAnulado() {
		if (anulado == null) {
			anulado = false;
		}
		return anulado;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	public TextoPadraoDeclaracaoVO getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadrao;
	}

	public void setTextoPadrao(TextoPadraoDeclaracaoVO textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public Boolean getNovaGeracaoRepresentacaoVisualDiplomaDigital() {
		if (novaGeracaoRepresentacaoVisualDiplomaDigital == null) {
			novaGeracaoRepresentacaoVisualDiplomaDigital = false;
		}
		return novaGeracaoRepresentacaoVisualDiplomaDigital;
	}

	public void setNovaGeracaoRepresentacaoVisualDiplomaDigital(Boolean novaGeracaoRepresentacaoVisualDiplomaDigital) {
		this.novaGeracaoRepresentacaoVisualDiplomaDigital = novaGeracaoRepresentacaoVisualDiplomaDigital;
	}

	public Date getDataRegistroDiploma() {
		return dataRegistroDiploma;
	}

	public void setDataRegistroDiploma(Date dataRegistroDiploma) {
		this.dataRegistroDiploma = dataRegistroDiploma;
	}

//	public ConfiguracaoDiplomaDigitalVO getConfiguracaoDiplomaDigital() {
//		if (configuracaoDiplomaDigital == null) {
//			configuracaoDiplomaDigital = new ConfiguracaoDiplomaDigitalVO();
//		}
//		return configuracaoDiplomaDigital;
//	}
//
//	public void setConfiguracaoDiplomaDigital(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital) {
//		this.configuracaoDiplomaDigital = configuracaoDiplomaDigital;
//	}

	public CursoCoordenadorVO getCursoCoordenadorVO() {
		if (cursoCoordenadorVO == null) {
			cursoCoordenadorVO = new CursoCoordenadorVO();
		}
		return cursoCoordenadorVO;
	}

	public void setCursoCoordenadorVO(CursoCoordenadorVO cursoCoordenadorVO) {
		this.cursoCoordenadorVO = cursoCoordenadorVO;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}
	
	public Boolean getEmitidoPorDecisaoJudicial() {
		if (emitidoPorDecisaoJudicial == null) {
			emitidoPorDecisaoJudicial = Boolean.FALSE;
		}
		return emitidoPorDecisaoJudicial;
	}

	public void setEmitidoPorDecisaoJudicial(Boolean emitidoPorDecisaoJudicial) {
		this.emitidoPorDecisaoJudicial = emitidoPorDecisaoJudicial;
	}

	public Boolean getEmitidoPorProcessoTransferenciaAssistida() {
		if (emitidoPorProcessoTransferenciaAssistida == null) {
			emitidoPorProcessoTransferenciaAssistida = Boolean.FALSE;
		}
		return emitidoPorProcessoTransferenciaAssistida;
	}

	public void setEmitidoPorProcessoTransferenciaAssistida(Boolean emitidoPorProcessoTransferenciaAssistida) {
		this.emitidoPorProcessoTransferenciaAssistida = emitidoPorProcessoTransferenciaAssistida;
	}

	public String getNomeIesPTA() {
		if (nomeIesPTA == null) {
			nomeIesPTA = Constantes.EMPTY;
		}
		return nomeIesPTA;
	}

	public void setNomeIesPTA(String nomeIesPTA) {
		this.nomeIesPTA = nomeIesPTA;
	}

	public String getCnpjPTA() {
		if (cnpjPTA == null) {
			cnpjPTA = Constantes.EMPTY;
		}
		return cnpjPTA;
	}

	public void setCnpjPTA(String cnpjPTA) {
		this.cnpjPTA = cnpjPTA;
	}

	public Boolean getPossuiCodigoMecPTA() {
		if (possuiCodigoMecPTA == null) {
			possuiCodigoMecPTA = Boolean.FALSE;
		}
		return possuiCodigoMecPTA;
	}

	public void setPossuiCodigoMecPTA(Boolean possuiCodigoMecPTA) {
		this.possuiCodigoMecPTA = possuiCodigoMecPTA;
	}

	public Integer getCodigoMecPTA() {
		if (codigoMecPTA == null) {
			codigoMecPTA = 0;
		}
		return codigoMecPTA;
	}

	public void setCodigoMecPTA(Integer codigoMecPTA) {
		this.codigoMecPTA = codigoMecPTA;
	}

	public String getCepPTA() {
		if (cepPTA == null) {
			cepPTA = Constantes.EMPTY;
		}
		return cepPTA;
	}

	public void setCepPTA(String cepPTA) {
		this.cepPTA = cepPTA;
	}

	public CidadeVO getCidadePTA() {
		if (cidadePTA == null) {
			cidadePTA = new CidadeVO();
		}
		return cidadePTA;
	}

	public void setCidadePTA(CidadeVO cidadePTA) {
		this.cidadePTA = cidadePTA;
	}

	public String getLogradouroPTA() {
		if (logradouroPTA == null) {
			logradouroPTA = Constantes.EMPTY;
		}
		return logradouroPTA;
	}

	public void setLogradouroPTA(String logradouroPTA) {
		this.logradouroPTA = logradouroPTA;
	}

	public String getNumeroPTA() {
		if (numeroPTA == null) {
			numeroPTA = Constantes.EMPTY;
		}
		return numeroPTA;
	}

	public void setNumeroPTA(String numeroPTA) {
		this.numeroPTA = numeroPTA;
	}

	public String getComplementoPTA() {
		if (complementoPTA == null) {
			complementoPTA = Constantes.EMPTY;
		}
		return complementoPTA;
	}

	public void setComplementoPTA(String complementoPTA) {
		this.complementoPTA = complementoPTA;
	}

	public String getBairroPTA() {
		if (bairroPTA == null) {
			bairroPTA = Constantes.EMPTY;
		}
		return bairroPTA;
	}

	public void setBairroPTA(String bairroPTA) {
		this.bairroPTA = bairroPTA;
	}

	public TipoAutorizacaoEnum getTipoDescredenciamentoPTA() {
		if (tipoDescredenciamentoPTA == null) {
			tipoDescredenciamentoPTA = TipoAutorizacaoEnum.PARECER;
		}
		return tipoDescredenciamentoPTA;
	}

	public void setTipoDescredenciamentoPTA(TipoAutorizacaoEnum tipoDescredenciamentoPTA) {
		this.tipoDescredenciamentoPTA = tipoDescredenciamentoPTA;
	}

	public String getNumeroDescredenciamentoPTA() {
		if (numeroDescredenciamentoPTA == null) {
			numeroDescredenciamentoPTA = Constantes.EMPTY;
		}
		return numeroDescredenciamentoPTA;
	}

	public void setNumeroDescredenciamentoPTA(String numeroDescredenciamentoPTA) {
		this.numeroDescredenciamentoPTA = numeroDescredenciamentoPTA;
	}

	public Date getDataDescredenciamentoPTA() {
		return dataDescredenciamentoPTA;
	}

	public void setDataDescredenciamentoPTA(Date dataDescredenciamentoPTA) {
		this.dataDescredenciamentoPTA = dataDescredenciamentoPTA;
	}

	public String getVeiculoPublicacaoDescredenciamentoPTA() {
		if (veiculoPublicacaoDescredenciamentoPTA == null) {
			veiculoPublicacaoDescredenciamentoPTA = Constantes.EMPTY;
		}
		return veiculoPublicacaoDescredenciamentoPTA;
	}

	public void setVeiculoPublicacaoDescredenciamentoPTA(String veiculoPublicacaoDescredenciamentoPTA) {
		this.veiculoPublicacaoDescredenciamentoPTA = veiculoPublicacaoDescredenciamentoPTA;
	}

	public Date getDataPublicacaoDescredenciamentoPTA() {
		return dataPublicacaoDescredenciamentoPTA;
	}

	public void setDataPublicacaoDescredenciamentoPTA(Date dataPublicacaoDescredenciamentoPTA) {
		this.dataPublicacaoDescredenciamentoPTA = dataPublicacaoDescredenciamentoPTA;
	}

	public Integer getSecaoPublicacaoDescredenciamentoPTA() {
		if (secaoPublicacaoDescredenciamentoPTA == null) {
			secaoPublicacaoDescredenciamentoPTA = 0;
		}
		return secaoPublicacaoDescredenciamentoPTA;
	}

	public void setSecaoPublicacaoDescredenciamentoPTA(Integer secaoPublicacaoDescredenciamentoPTA) {
		this.secaoPublicacaoDescredenciamentoPTA = secaoPublicacaoDescredenciamentoPTA;
	}

	public Integer getPaginaPublicacaoDescredenciamentoPTA() {
		if (paginaPublicacaoDescredenciamentoPTA == null) {
			paginaPublicacaoDescredenciamentoPTA = 0;
		}
		return paginaPublicacaoDescredenciamentoPTA;
	}

	public void setPaginaPublicacaoDescredenciamentoPTA(Integer paginaPublicacaoDescredenciamentoPTA) {
		this.paginaPublicacaoDescredenciamentoPTA = paginaPublicacaoDescredenciamentoPTA;
	}

	public Integer getNumeroDOUDescredenciamentoPTA() {
		if (numeroDOUDescredenciamentoPTA == null) {
			numeroDOUDescredenciamentoPTA = 0;
		}
		return numeroDOUDescredenciamentoPTA;
	}

	public void setNumeroDOUDescredenciamentoPTA(Integer numeroDOUDescredenciamentoPTA) {
		this.numeroDOUDescredenciamentoPTA = numeroDOUDescredenciamentoPTA;
	}
	
	public String getNomeJuizDecisaoJudicial() {
		if (nomeJuizDecisaoJudicial == null) {
			nomeJuizDecisaoJudicial = Constantes.EMPTY;
		}
		return nomeJuizDecisaoJudicial;
	}

	public void setNomeJuizDecisaoJudicial(String nomeJuizDecisaoJudicial) {
		this.nomeJuizDecisaoJudicial = nomeJuizDecisaoJudicial;
	}

	public String getNumeroProcessoDecisaoJudicial() {
		if (numeroProcessoDecisaoJudicial == null) {
			numeroProcessoDecisaoJudicial = Constantes.EMPTY;
		}
		return numeroProcessoDecisaoJudicial;
	}

	public void setNumeroProcessoDecisaoJudicial(String numeroProcessoDecisaoJudicial) {
		this.numeroProcessoDecisaoJudicial = numeroProcessoDecisaoJudicial;
	}

	public String getDecisaoJudicial() {
		if (decisaoJudicial == null) {
			decisaoJudicial = Constantes.EMPTY;
		}
		return decisaoJudicial;
	}

	public void setDecisaoJudicial(String decisaoJudicial) {
		this.decisaoJudicial = decisaoJudicial;
	}

	public String getInformacoesAdicionaisDecisaoJudicial() {
		if (informacoesAdicionaisDecisaoJudicial == null) {
			informacoesAdicionaisDecisaoJudicial = Constantes.EMPTY;
		}
		return informacoesAdicionaisDecisaoJudicial;
	}

	public void setInformacoesAdicionaisDecisaoJudicial(String informacoesAdicionaisDecisaoJudicial) {
		this.informacoesAdicionaisDecisaoJudicial = informacoesAdicionaisDecisaoJudicial;
	}
	
//	public List<DeclaracaoAcercaProcessoJudicialVO> getDeclaracaoAcercaProcessoJudicialVOs() {
//		if (declaracaoAcercaProcessoJudicialVOs == null) {
//			declaracaoAcercaProcessoJudicialVOs = new ArrayList<>(0);
//		}
//		return declaracaoAcercaProcessoJudicialVOs;
//	}
//	
//	public void setDeclaracaoAcercaProcessoJudicialVOs(List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs) {
//		this.declaracaoAcercaProcessoJudicialVOs = declaracaoAcercaProcessoJudicialVOs;
//	}
	
	public VersaoDiplomaDigitalEnum getVersaoDiploma() {
		if (versaoDiploma == null) {
			versaoDiploma = VersaoDiplomaDigitalEnum.VERSAO_1_05;
		}
		return versaoDiploma;
	}
	
	public void setVersaoDiploma(VersaoDiplomaDigitalEnum versaoDiploma) {
		this.versaoDiploma = versaoDiploma;
	}

	public String getLivroRegistradora() {
		if(livroRegistradora == null) {
			livroRegistradora = Constantes.EMPTY;
		}
		return livroRegistradora;
	}

	public void setLivroRegistradora(String livroRegistradora) {
		this.livroRegistradora = livroRegistradora;
	}

	public String getFolhaReciboRegistradora() {
		if(folhaReciboRegistradora == null) {
			folhaReciboRegistradora = Constantes.EMPTY;
		}
		return folhaReciboRegistradora;
	}

	public void setFolhaReciboRegistradora(String folhaReciboRegistradora) {
		this.folhaReciboRegistradora = folhaReciboRegistradora;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public String getAbrirModalVerso() {
		if (abrirModalVerso == null) {
			abrirModalVerso = Constantes.EMPTY;
		}
		return abrirModalVerso;
	}

	public void setAbrirModalVerso(String abrirModalVerso) {
		this.abrirModalVerso = abrirModalVerso;
	}

	public Boolean getAbrirModalOK() {
		if (abrirModalOK == null) {
			abrirModalOK = Boolean.FALSE;
		}
		return abrirModalOK;
	}

	public void setAbrirModalOK(Boolean abrirModalOK) {
		this.abrirModalOK = abrirModalOK;
	}
	
	public Boolean getGerarXMLDiplomaLote() {
		if (gerarXMLDiplomaLote == null) {
			gerarXMLDiplomaLote = Boolean.FALSE;
		}
		return gerarXMLDiplomaLote;
	}
	
	public void setGerarXMLDiplomaLote(Boolean gerarXMLDiplomaLote) {
		this.gerarXMLDiplomaLote = gerarXMLDiplomaLote;
	}
	
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}
	
	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public Date getDataAnulacao() {
		return dataAnulacao;
	}

	public void setDataAnulacao(Date dataAnulacao) {
		this.dataAnulacao = dataAnulacao;
	}

//	public TMotivoAnulacao getMotivoAnulacao() {
//		return motivoAnulacao;
//	}
//
//	public void setMotivoAnulacao(TMotivoAnulacao motivoAnulacao) {
//		this.motivoAnulacao = motivoAnulacao;
//	}

	public String getAnotacaoAnulacao() {
		if (anotacaoAnulacao == null) {
			anotacaoAnulacao = Constantes.EMPTY;
		}
		return anotacaoAnulacao;
	}

	public void setAnotacaoAnulacao(String anotacaoAnulacao) {
		this.anotacaoAnulacao = anotacaoAnulacao;
	}
	
	public Map<String, Object> getFieldsPersonalizados() {
		if (fieldsPersonalizados == null) {
			fieldsPersonalizados = new HashMap<>(0);
		}
		return fieldsPersonalizados;
	}
	
	public void setFieldsPersonalizados(Map<String, Object> fieldsPersonalizados) {
		this.fieldsPersonalizados = fieldsPersonalizados;
	}

	public DocumentoAssinadoVO getDiplomaDigital() {
		if (diplomaDigital == null) {
			diplomaDigital = new DocumentoAssinadoVO();
		}
		return diplomaDigital;
	}

	public void setDiplomaDigital(DocumentoAssinadoVO diplomaDigital) {
		this.diplomaDigital = diplomaDigital;
	}

	public DocumentoAssinadoVO getHistoricoDigital() {
		if (historicoDigital == null) {
			historicoDigital = new DocumentoAssinadoVO();
		}
		return historicoDigital;
	}

	public void setHistoricoDigital(DocumentoAssinadoVO historicoDigital) {
		this.historicoDigital = historicoDigital;
	}

	public DocumentoAssinadoVO getDocumentacaoAcademicaDigital() {
		if (documentacaoAcademicaDigital == null) {
			documentacaoAcademicaDigital = new DocumentoAssinadoVO();
		}
		return documentacaoAcademicaDigital;
	}

	public void setDocumentacaoAcademicaDigital(DocumentoAssinadoVO documentacaoAcademicaDigital) {
		this.documentacaoAcademicaDigital = documentacaoAcademicaDigital;
	}
	
	public Boolean getApresentarDiplomaDigital() {
		if (Uteis.isAtributoPreenchido(getDiplomaDigital())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean getApresentarHistoricoDigital() {
		if (Uteis.isAtributoPreenchido(getHistoricoDigital())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean getApresentarDocumentacaoAcademicaDigital() {
		if (Uteis.isAtributoPreenchido(getDocumentacaoAcademicaDigital())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public String getUrlAssinaturaFuncionarioPrincipal(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if (!getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getNome().trim().isEmpty()) {
			return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ASSINATURA.getValue() + "/" + getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}
	
	public String getUrlAssinaturaFuncionarioSecundario(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if (!getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome().trim().isEmpty()) {
			return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ASSINATURA.getValue() + "/" + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}

	public String getUrlAssinaturaFuncionarioTerciario(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if (!getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getNome().trim().isEmpty()) {
			return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ASSINATURA.getValue() + "/" + getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}
	
	public ConsistirException getConsistirException() {
		if (consistirException == null) {
			consistirException = new ConsistirException();
		}
		return consistirException;
	}
	
	public void setConsistirException(ConsistirException consistirException) {
		this.consistirException = consistirException;
	}

	public String getIdDiplomaDigital() {
		if (idDiplomaDigital == null) {
			idDiplomaDigital = Constantes.EMPTY;
		}
		return idDiplomaDigital;
	}
	
	public void setIdDiplomaDigital(String idDiplomaDigital) {
		this.idDiplomaDigital = idDiplomaDigital;
	}
	
	public String getIdDadosRegistrosDiplomaDigital() {
		if (idDadosRegistrosDiplomaDigital == null) {
			idDadosRegistrosDiplomaDigital = Constantes.EMPTY;
		}
		return idDadosRegistrosDiplomaDigital;
	}
	
	public void setIdDadosRegistrosDiplomaDigital(String idDadosRegistrosDiplomaDigital) {
		this.idDadosRegistrosDiplomaDigital = idDadosRegistrosDiplomaDigital;
	}
	
	public UsuarioVO getResponsavelAnulacao() {
		if (responsavelAnulacao == null) {
			responsavelAnulacao = new UsuarioVO();
		}
		return responsavelAnulacao;
	}
	
	public void setResponsavelAnulacao(UsuarioVO responsavelAnulacao) {
		this.responsavelAnulacao = responsavelAnulacao;
	}
	
	public Boolean getExisteExpedicaoDiplomaSuperior() {
		if (existeExpedicaoDiplomaSuperior == null) {
			existeExpedicaoDiplomaSuperior = Boolean.FALSE;
		}
		return existeExpedicaoDiplomaSuperior;
	}
	
	public void setExisteExpedicaoDiplomaSuperior(Boolean existeExpedicaoDiplomaSuperior) {
		this.existeExpedicaoDiplomaSuperior = existeExpedicaoDiplomaSuperior;
	}
	
	public Boolean getExisteDiplomaDigitalGerado() {
		if (existeDiplomaDigitalGerado == null) {
			existeDiplomaDigitalGerado = Boolean.FALSE;
		}
		return existeDiplomaDigitalGerado;
	}
	
	public void setExisteDiplomaDigitalGerado(Boolean existeDiplomaDigitalGerado) {
		this.existeDiplomaDigitalGerado = existeDiplomaDigitalGerado;
	}
}
