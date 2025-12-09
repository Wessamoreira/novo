package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

/**
 * Reponsável por manter os dados da entidade PlanoTextoPadrao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class TextoPadraoProcessoSeletivoVO extends TextoPadraoLayoutVO {

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
	private List<ArquivoVO > listaArquivoIreport;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoTextoPadrao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TextoPadraoProcessoSeletivoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoTextoPadraoProcessoSeletivoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TextoPadraoProcessoSeletivoVO obj) throws ConsistirException {
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
        if (obj.getTexto().equals("")) {
            throw new ConsistirException("O campo TEXTO (Texto Padrao) deve ser informado.");
        }
        if (obj.getTipo().equals("")) {
        	throw new ConsistirException("O campo TIPO DECLARAÇÃO (Texto Padrao) deve ser selecionado.");
        }
    }
    
	public void substituirTag(String texto, InscricaoVO inscricao, List<InscricaoVO> lista, TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio) throws Exception {
		MarcadorVO mar = new MarcadorVO();
		TextoPadraoVO t = new TextoPadraoVO();
		PlanoFinanceiroAlunoVO planoFinanceiroVO = new PlanoFinanceiroAlunoVO();
		List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);

		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(getTexto());
		while (m.find()) {
			mar.setTag(m.group());
			if (mar.getTag().contains("_Candidato")) {
				setTexto(t.substituirTagCandidato(getTexto(), mar, inscricao));
			} else if (mar.getTag().contains("_Inscricao")) {
				setTexto(t.substituirTagInscricao(getTexto(), mar, inscricao));
			} else if (mar.getTag().contains("_ProcessoSeletivo")) {
				setTexto(t.substituirTagProcessoSeletivo(getTexto(), mar, inscricao));
			} else if (mar.getTag().contains("_ListaProcessoSeletivo")) {
				setTexto(t.substituirTagListaProcessoSeletivo(getTexto(), mar, lista, tipoRelatorio));
				t.substituirTagComLista(getTexto(), mar.getTag(), lista, "");
			} else if (mar.getTag().contains("_ResultadoProcessoSeletivo")) {
				setTexto(t.substituirTagResultadoProcessoSeletivo(getTexto(), mar, inscricao));
			} else {
				setTexto(t.substituirTagOutras(getTexto(), mar, new ImpressaoContratoVO()));
			}
		}
		getParametrosRel().putAll(t.getParametrosRel());
		planoFinanceiroVO = null;
		planoDescontoVOs = null;
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
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style type='text/css'>");
        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
        sb.append(" th { font-weight: normal; } ");
        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
        sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
        sb.append(" @page { size: A4; margin: 0; } ");
        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
        sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\"><span style=\"font-size: large;\">EXEMPLO</span></p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\"><span style=\"white-space: pre;\"> </span>O aluno(a): [(70){}Nome_Candidato],&nbsp;portador(a)&nbsp;da Carteira de Identidade [(20){}Rg_Candidato]&nbsp;e&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">CPF.: [(14){}Cpf_Candidato],&nbsp; &eacute; aluno(a)&nbsp;desta Institui&ccedil;&atilde;o de Ensino.</p>");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">[(10){}DataAtual_Outras]");
        sb.append("<p style=\"text-align: center;\">&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p style=\"text-align: center;\">______________________ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;_________________________</p>");
        sb.append("<p style=\"text-align: center;\">Diretor(a) &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Secret&aacute;ria Geral</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("<p>&nbsp;</p>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
    
    public void clonar(UsuarioVO usuario) {
        this.setNovoObj(true);
        this.setCodigo(0);
        this.setDescricao(descricao + " - CLONE");
        setDataDefinicao(new Date());
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
			tipo = "CA";
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
	
	public OrientacaoPaginaEnum getOrientacaoDaPaginaEnum() {
		if (orientacaoDaPagina == null) {
			return OrientacaoPaginaEnum.RETRATO;
		}
		return OrientacaoPaginaEnum.getEnum(getOrientacaoDaPagina());
	}

	public void setOrientacaoDaPagina(String orientacaoDaPagina) {
		this.orientacaoDaPagina = orientacaoDaPagina;
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
		if(arquivoIreport == null){
			arquivoIreport = new ArquivoVO();
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
	
	@Override
	public List getObjetos() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ArquivoVO> getListaArquivoIreport() {
		if(listaArquivoIreport == null){
			listaArquivoIreport = new ArrayList<ArquivoVO>();
		}
		return listaArquivoIreport;
	}

	public void setListaArquivoIreport(List<ArquivoVO> listaArquivoIreport) {
		this.listaArquivoIreport = listaArquivoIreport;
	}
}
