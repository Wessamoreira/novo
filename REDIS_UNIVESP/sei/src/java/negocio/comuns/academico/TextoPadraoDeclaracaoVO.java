package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

/**
 * Reponsável por manter os dados da entidade PlanoTextoPadrao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class TextoPadraoDeclaracaoVO extends TextoPadraoLayoutVO {

    private Integer codigo;
    private String descricao;
    private UnidadeEnsinoVO unidadeEnsino;
    private Date dataDefinicao;
    private String texto;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario</code>.
     */
    private UsuarioVO responsavelDefinicao;
    private Boolean controlarDocumentacaoPendente;
    private Boolean validarTodosDocumentosCurso;
    private Boolean validarApenasDocumentoSuspensao;
    private String tipo;
    private String orientacaoDaPagina;
    private String margemDireita;
    private String margemEsquerda;
    private String margemSuperior;
    private String margemInferior;
    private TipoDesigneTextoEnum tipoDesigneTextoEnum;
	private ArquivoVO arquivoIreport;
	private Boolean assinarDigitalmenteTextoPadrao = false;
	private String corAssinaturaDigitalmente;
	private AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum;
	private Float larguraAssinatura;
	private Float alturaAssinatura;
	private HashMap<String, Object> parametrosRel;
    private String nivelEducacional;
    private List<TextoPadraoDeclaracaoFuncionarioVO> textoPadraoDeclaracaofuncionarioVOs;
    private List objetos;
    private List<ArquivoVO > listaArquivoIreport;
    
    private Boolean modeloPadraoVisaoAluno;
	
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoTextoPadrao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TextoPadraoDeclaracaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoTextoPadraoDeclaracaoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TextoPadraoDeclaracaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO ( Texto Padrao) deve ser informado.");
        }
        if (obj.getDataDefinicao() == null) {
            throw new ConsistirException("O campo DATA DEFINIÇÃO (Texto Padrao) deve ser informado.");
        }
        if ((obj.getResponsavelDefinicao() == null) || (obj.getResponsavelDefinicao().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL DEFINIÇÃO ( Texto Padrao) deve ser informado.");
        }
        if (obj.getTexto().equals("") && (!obj.getTipo().equals("CO") && !obj.getTipo().equals("DQ"))) {
            throw new ConsistirException("O campo TEXTO (Texto Padrao) deve ser informado.");
        }
        if (obj.getTipo().equals("")) {
        	throw new ConsistirException("O campo TIPO DECLARAÇÃO (Texto Padrao) deve ser selecionado.");
        }
        if (obj.getTipoDesigneTextoEnum().isPdf() && obj.getListaArquivoIreport().isEmpty()) {
        	throw new ConsistirException("O arquivo do Ireport (Texto Padrao) deve ser adicionado.");
        }
        if (obj.getTipoDesigneTextoEnum().isPdf() &&! obj.possuiArquivoIreportPrincipal()) {
        	throw new ConsistirException("O arquivo do Ireport (Texto Padrao) Principal deve ser adicionado.");
        }
    }
    
	public void substituirTag(ImpressaoContratoVO impressaoContrato, UsuarioVO usuario) throws Exception {
		MarcadorVO mar = new MarcadorVO();
		TextoPadraoVO t = new TextoPadraoVO();
		PlanoFinanceiroAlunoVO planoFinanceiroVO = new PlanoFinanceiroAlunoVO();
		List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
		DadosComerciaisVO dcAluno = impressaoContrato.getMatriculaVO().getAluno().getDadosComerciaisVOs().stream()
			.filter(p -> p.getEmpregoAtual())
			.findFirst()
			.orElse(impressaoContrato.getMatriculaVO().getAluno().getDadosComerciaisVOs().stream().findFirst().orElse(null));
		
		DadosComerciaisVO dcProfessor = impressaoContrato.getProfessor().getPessoa().getDadosComerciaisVOs().stream()
			.filter(p -> p.getEmpregoAtual())
			.findFirst()
			.orElse(impressaoContrato.getProfessor().getPessoa().getDadosComerciaisVOs().stream().findFirst().orElse(null));
		
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			mar.setTag(m.group());
			if (mar.getTag().contains("_Aluno")) {
				setTexto(t.substituirTagAluno(getTexto(), impressaoContrato, mar, dcAluno));
			} else if (mar.getTag().contains("_Professor")) {
				setTexto(t.substituirTagProfessor(getTexto(), impressaoContrato, mar, dcProfessor));
			} else if (mar.getTag().contains("_UnidadeEnsino")) {
				setTexto(t.substituirTagUnidadeEnsino(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Matricula") || mar.getTag().contains("_matricula")) {
				setTexto(t.substituirTagMatricula(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Curso")) {
				setTexto(t.substituirTagCurso(getTexto(), impressaoContrato, mar, planoFinanceiroVO, planoDescontoVOs, usuario));
			} else if (mar.getTag().contains("_Disciplina")) {
				setTexto(t.substituirTagDisciplina(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Estagio")) {
				setTexto(t.substituirTagEstagio(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_InscProcSeletivo")) {
				setTexto(t.substituirTagInscProcSeletivo(getTexto(), impressaoContrato, mar));
			} else if (mar.getTag().contains("_Outras")) {
				setTexto(t.substituirTagOutras(getTexto(), mar, impressaoContrato));
			} else if (mar.getTag().contains("_ContaReceber") && Uteis.isAtributoPreenchido(impressaoContrato.getNegociacaoContaReceberVO())) {
				setTexto(t.substituirTagNegociacaoContaReceber(texto, impressaoContrato.getNegociacaoContaReceberVO(), mar));
			} else if (mar.getTag().contains("_ProgramacaoFormaturaAluno")) {
				setTexto(t.substituirTagProgramacaoFormatura(getTexto(), impressaoContrato, mar));
			} else {
				setTexto(t.substituirTagAluno(getTexto(), impressaoContrato, mar, dcAluno));
				setTexto(t.substituirTagProfessor(getTexto(), impressaoContrato, mar, dcProfessor));
				setTexto(t.substituirTagUnidadeEnsino(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagMatricula(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagCurso(getTexto(), impressaoContrato, mar, planoFinanceiroVO, planoDescontoVOs, usuario));
				setTexto(t.substituirTagDisciplina(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagOutras(getTexto(), mar, impressaoContrato));
				setTexto(t.substituirTagProgramacaoFormatura(getTexto(), impressaoContrato, mar));
				setTexto(t.substituirTagRequerimento(getTexto(), impressaoContrato, mar));
			}
		}
		getParametrosRel().putAll(t.getParametrosRel());
		planoFinanceiroVO = null;
		planoDescontoVOs = null;
	}
	
	public void substituirTag(FuncionarioVO funcionarioVO, UsuarioVO usuario, SuperParametroRelVO parametro) throws Exception {
		MarcadorVO mar = new MarcadorVO();
		TextoPadraoVO t = new TextoPadraoVO();
		
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			mar.setTag(m.group());
			 if (mar.getTag().contains("_Outras")) {
					setTexto(t.substituirTagOutras(getTexto(), mar, funcionarioVO));
				}
			
			}
			if(t.getParametrosRel().size() > 0) {
				parametro.getParametros().putAll(t.getParametrosRel());
			}
	}
		
    public String substituirTag(String texto, String tag, String valor, String textoTag, int tamanhoOcupacaoTag) {
        // tag = Uteis.trocarAcentuacaoPorAcentuacaoHTML(tag);
        int tam = valor.length();
        if (tamanhoOcupacaoTag == 0) {
			tamanhoOcupacaoTag = tam;
		}
		if (tamanhoOcupacaoTag < tam) {
			valor = valor.substring(0, tamanhoOcupacaoTag);
		}
        texto = texto.replace(tag, textoTag + valor);
        if(getParametrosRel() != null){
			getParametrosRel().put(obterTagSemTextoSemTamanho(tag), valor);	
		}
        return texto;
    }
    
    public String obterTagSemTextoSemTamanho(String tag) {
		int posicao1 = tag.indexOf(")");
		int posicao2 = tag.indexOf("}");
		if (posicao2 != -1) {
			return tag.substring(posicao2 + 1, tag.length() - 1);
		} else if (posicao1 != -1) {
			return tag.substring(posicao1 + 1, tag.length() - 1);
		} else {
			return "";
		}
	}

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setDescricao(getDescricao().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setDataDefinicao(new Date());
        setTexto(iniciarDeclaracaoPadrao());
    }

    public String iniciarDeclaracaoPadrao() {
        StringBuilder sb = new StringBuilder();
//        sb.append("<html>");
//        sb.append("<head>");
//        sb.append("<style type='text/css'>");
//        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
//        sb.append(" th { font-weight: normal; } ");
//        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
//        sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
//        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
//        sb.append(" @page { size: A4; margin: 0; } ");
//        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
//        sb.append("</style>");
//        sb.append("</head>");
//        sb.append("<body>");
//        sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
//        sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'>");
//        sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\"><span style=\"font-size: large;\">EXEMPLO DECLARA&Ccedil;&Atilde;O</span></p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\"><span style=\"white-space: pre;\"> </span>Declaramos, para os devidos fins, que [(70){}Nome_Aluno],&nbsp;portador(a)&nbsp;da Carteira de Identidade [(20){}Rg_Aluno]&nbsp;e&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\">CPF.: [(14){}Cpf_Aluno],&nbsp;Matr&iacute;cula n&ordm; [(20){}Matricula_Matricula], &eacute; aluno(a)&nbsp;desta Institui&ccedil;&atilde;o de Ensino,&nbsp;matriculado(a) no Curso de&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\">[(70){}Nome_Curso],&nbsp;e vem frequentando as aulas normalmente.</p>");
//        sb.append("<p style=\"text-align: center;\">Por ser verdade, firmamos a presente.</p>");
//        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\">[(10){}DataAtual_Outras]");
//        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p style=\"text-align: center;\">______________________ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;_________________________</p>");
//        sb.append("<p style=\"text-align: center;\">Diretor(a) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Secret&aacute;ria Geral</p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("<p>&nbsp;</p>");
//        sb.append("</div>");
//        sb.append("</div>");
//        sb.append("</body>");
//        sb.append("</html>");
        return sb.toString();
    }
    
    public void clonar(UsuarioVO usuario) {
        this.setNovoObj(true);
        this.setCodigo(0);
        this.setDescricao(descricao + " - CLONE");
        setDataDefinicao(new Date());
        if(Uteis.isAtributoPreenchido(this.getListaArquivoIreport())) {
        	this.setListaArquivoIreport(new ArrayList<ArquivoVO>());
        	this.setTexto("");
        }
        setResponsavelDefinicao(usuario);
    }

    public UsuarioVO getResponsavelDefinicao() {
        if (responsavelDefinicao == null) {
            responsavelDefinicao = new UsuarioVO();
        }
        return responsavelDefinicao;
    }

    public void setResponsavelDefinicao(UsuarioVO responsavelDefinicao) {
        this.responsavelDefinicao = responsavelDefinicao;
    }

    /**
     * Retorna o objeto da classe <code>Colaborador</code> relacionado com (
     * <code>PlanoTextoPadrao</code>).
     */
    public String getTexto() {
        if (texto == null) {
            texto = "";
        }
        return (texto);
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getDataDefinicao() {
        if (dataDefinicao == null) {
            dataDefinicao = new Date();
        }
        return (dataDefinicao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataDefinicao_Apresentar() {
        return (Uteis.getData(getDataDefinicao()));
    }

    public void setDataDefinicao(Date dataDefinicao) {
        this.dataDefinicao = dataDefinicao;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public Boolean getControlarDocumentacaoPendente() {
        if (controlarDocumentacaoPendente == null) {
            controlarDocumentacaoPendente = Boolean.FALSE;
        }
        return controlarDocumentacaoPendente;
    }

    public void setControlarDocumentacaoPendente(Boolean controlarDocumentacaoPendente) {
        this.controlarDocumentacaoPendente = controlarDocumentacaoPendente;
    }

    public Boolean getValidarApenasDocumentoSuspensao() {
        if (validarApenasDocumentoSuspensao == null || !getControlarDocumentacaoPendente()) {
            validarApenasDocumentoSuspensao = Boolean.FALSE;
        }
        return validarApenasDocumentoSuspensao;
    }

    public void setValidarApenasDocumentoSuspensao(Boolean validarApenasDocumentoSuspensao) {
        this.validarApenasDocumentoSuspensao = validarApenasDocumentoSuspensao;
    }

    public Boolean getValidarTodosDocumentosCurso() {
        if (validarTodosDocumentosCurso == null || !getControlarDocumentacaoPendente()) {
            validarTodosDocumentosCurso = Boolean.FALSE;
        }
        return validarTodosDocumentosCurso;
    }

    public void setValidarTodosDocumentosCurso(Boolean validarTodosDocumentosCurso) {
        this.validarTodosDocumentosCurso = validarTodosDocumentosCurso;
    }

    /**
     * @return the unidadeEnsino
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

	public String getTipo() {
		if(tipo == null) {
			tipo = "OT";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOrientacaoDaPagina() {
		if (orientacaoDaPagina == null) {
			orientacaoDaPagina = "";
		}
		return orientacaoDaPagina;
	}

	public void setOrientacaoDaPagina(String orientacaoDaPagina) {
		this.orientacaoDaPagina = orientacaoDaPagina;
	}
	
	public OrientacaoPaginaEnum getOrientacaoDaPaginaEnum() {
		if (orientacaoDaPagina == null) {
			return OrientacaoPaginaEnum.RETRATO;
		}
		return OrientacaoPaginaEnum.getEnum(getOrientacaoDaPagina());
	}

	public String getMargemDireita() {
		if (margemDireita == null) {
			margemDireita = "1";
		}
		return margemDireita;
	}

	public void setMargemDireita(String margemDireita) {
		this.margemDireita = margemDireita;
	}

	public String getMargemEsquerda() {
		if (margemEsquerda == null) {
			margemEsquerda = "1";
		}
		return margemEsquerda;
	}

	public void setMargemEsquerda(String margemEsquerda) {
		this.margemEsquerda = margemEsquerda;
	}

	public String getMargemInferior() {
		if (margemInferior == null) {
			margemInferior = "1";
		}
		return margemInferior;
	}

	public void setMargemInferior(String margemInferior) {
		this.margemInferior = margemInferior;
	}

	public String getMargemSuperior() {
		if (margemSuperior == null) {
			margemSuperior = "1";
		}
		return margemSuperior;
	}

	public void setMargemSuperior(String margemSuperior) {
		this.margemSuperior = margemSuperior;
	}
	
	public TipoDesigneTextoEnum getTipoDesigneTextoEnum() {
		if(tipoDesigneTextoEnum == null){
			tipoDesigneTextoEnum = TipoDesigneTextoEnum.HTML;
		}
		return tipoDesigneTextoEnum;
	}

	public void setTipoDesigneTextoEnum(TipoDesigneTextoEnum tipoDesigneTextoEnum) {
		this.tipoDesigneTextoEnum = tipoDesigneTextoEnum;
	}

	public ArquivoVO getArquivoIreport() {
		if(!getListaArquivoIreport().isEmpty()){
			arquivoIreport = getListaArquivoIreport().stream().filter(arquivo -> arquivo.getArquivoIreportPrincipal()).findFirst().orElse(new ArquivoVO());
		}else if(arquivoIreport == null) {
			arquivoIreport =  new ArquivoVO();
		}
		return arquivoIreport;
	}

	public void setArquivoIreport(ArquivoVO arquivoIreport) {
		this.arquivoIreport = arquivoIreport;
	}
	
	public Boolean getAssinarDigitalmenteTextoPadrao() {
		return assinarDigitalmenteTextoPadrao;
	}

	public void setAssinarDigitalmenteTextoPadrao(Boolean assinarDigitalmenteTextoPadrao) {
		this.assinarDigitalmenteTextoPadrao = assinarDigitalmenteTextoPadrao;
	}
	
	public AlinhamentoAssinaturaDigitalEnum getAlinhamentoAssinaturaDigitalEnum() {
		if(alinhamentoAssinaturaDigitalEnum == null){
			alinhamentoAssinaturaDigitalEnum = AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA;
		}
		return alinhamentoAssinaturaDigitalEnum;
	}
	
	public void setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum) {
		this.alinhamentoAssinaturaDigitalEnum = alinhamentoAssinaturaDigitalEnum;
	}

	public HashMap getParametrosRel() {
		if(parametrosRel == null){
			parametrosRel = new HashMap<String, Object>();
		}
		return parametrosRel;
	}

	public void setParametrosRel(HashMap<String, Object> parametrosRel) {
		this.parametrosRel = parametrosRel;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
	public void adicionarObjTextoPadraoDeclaracaoFuncionarioVOs(TextoPadraoDeclaracaoFuncionarioVO obj) throws Exception {
        int index = 0;
        Iterator i = getTextoPadraoDeclaracaofuncionarioVOs().iterator();
        while (i.hasNext()) {
        	TextoPadraoDeclaracaoFuncionarioVO objExistente = (TextoPadraoDeclaracaoFuncionarioVO) i.next();
            if (objExistente.getFuncionario().getCodigo().equals(obj.getFuncionario().getCodigo())) {
                getTextoPadraoDeclaracaofuncionarioVOs().set(index, obj);
                return;
            }
            index++;
        }
        getTextoPadraoDeclaracaofuncionarioVOs().add(obj);
    }

    public void excluirObjTextoPadraoDeclaracaoFuncionarioVOs(Integer funcionario) throws Exception {
        int index = 0;
        Iterator i = getTextoPadraoDeclaracaofuncionarioVOs().iterator();
        while (i.hasNext()) {
        	TextoPadraoDeclaracaoFuncionarioVO objExistente = (TextoPadraoDeclaracaoFuncionarioVO) i.next();
            if (objExistente.getFuncionario().getCodigo().equals(funcionario)) {
                getTextoPadraoDeclaracaofuncionarioVOs().remove(index);
                return;
            }
            index++;
        }
    }

	public List<TextoPadraoDeclaracaoFuncionarioVO> getTextoPadraoDeclaracaofuncionarioVOs() {
		if (textoPadraoDeclaracaofuncionarioVOs == null) {
			textoPadraoDeclaracaofuncionarioVOs = new ArrayList<TextoPadraoDeclaracaoFuncionarioVO>();
		}
		return textoPadraoDeclaracaofuncionarioVOs;
	}

	public void setTextoPadraoDeclaracaofuncionarioVOs(List<TextoPadraoDeclaracaoFuncionarioVO> textoPadraoDeclaracaofuncionarioVOs) {
		this.textoPadraoDeclaracaofuncionarioVOs = textoPadraoDeclaracaofuncionarioVOs;
	}
	
	public String getCorAssinaturaDigitalmente() {
		if (corAssinaturaDigitalmente == null) {
			corAssinaturaDigitalmente = "#000000";
		}
		return corAssinaturaDigitalmente;
	}

	public void setCorAssinaturaDigitalmente(String corAssinaturaDigitalmente) {
		this.corAssinaturaDigitalmente = corAssinaturaDigitalmente;
	}
	
	public Float getLarguraAssinatura() {
		if (larguraAssinatura == null) {
			larguraAssinatura = 200f;
		}
		return larguraAssinatura;
	}

	public void setLarguraAssinatura(Float larguraAssinatura) {
		this.larguraAssinatura = larguraAssinatura;
	}

	public Float getAlturaAssinatura() {
		if (alturaAssinatura == null) {
			alturaAssinatura = 40f;
		}
		return alturaAssinatura;
	}

	public void setAlturaAssinatura(Float alturaAssinatura) {
		this.alturaAssinatura = alturaAssinatura;
	}
	
	public List getObjetos() {
		if (objetos == null) {
			objetos = new ArrayList();
		}
		return objetos;
	}

	public void setObjetos(List objetos) {
		this.objetos = objetos;
	}

	public Boolean getModeloPadraoVisaoAluno() {
		if(modeloPadraoVisaoAluno == null) {
			modeloPadraoVisaoAluno = false;
		}
		return modeloPadraoVisaoAluno;
	}

	public void setModeloPadraoVisaoAluno(Boolean modeloPadraoVisaoAluno) {
		this.modeloPadraoVisaoAluno = modeloPadraoVisaoAluno;
	}

	public List<ArquivoVO> getListaArquivoIreport() {
		if(listaArquivoIreport == null) {
			listaArquivoIreport = new ArrayList<ArquivoVO>();
		}
		return listaArquivoIreport;
	}

	public void setListaArquivoIreport(List<ArquivoVO> listaArquivoIreport) {
		this.listaArquivoIreport = listaArquivoIreport;
	}
	
	public Boolean possuiArquivoIreportPrincipal() {
		if(!getListaArquivoIreport().isEmpty()) {
			return getListaArquivoIreport().stream().filter(arquivo -> arquivo.getArquivoIreportPrincipal()).findFirst().isPresent();
		}
		return false;
	}
	
	
	
}
