package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;

public class PlanoFinanceiroCursoVO extends SuperVO {

    private Integer codigo;
    // private Integer diaVencimentoParcela;
    /*
     * Transferido para a condição de pagamento - para que cada condição possa seguir suas próprias regras
     * Mantido aqui somente temporariamente até que a versão 4.5.0 seja implementada e utilizada por todos os clientes.
     */
    //private String tipoCalculoParcela; 
    
    /**
     * Atributo utilizado para determinar que as condições de pagamento serão controladas por 
     * valor da disciplina na turma. Item utilizado por instituição de concurso público e/ou preparatórios
     */
    private Boolean controlarValorPorDisciplina;
    
    private UsuarioVO responsavelAutorizacao;
    private TextoPadraoVO textoPadraoContratoMatricula;
    private TextoPadraoVO textoPadraoContratoFiador;
    private String modeloGeracaoParcelas;
    private List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs;
    private String descricao;
    private TextoPadraoVO textoPadraoContratoExtensao;
    private TextoPadraoVO textoPadraoContratoModular;
    private TextoPadraoVO textoPadraoContratoAditivo;
    private TextoPadraoVO textoPadraoContratoInclusaoReposicao;    
    private UnidadeEnsinoVO unidadeEnsino;
    private TurmaVO turma;
    private SituacaoPlanoFinanceiroCursoEnum situacao;
    private Date dataInativacao;
    private UsuarioVO responsavelInativacaoVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrï¿½o da classe <code>PlanoFinanceiroCurso</code>. Cria uma
     * nova instï¿½ncia desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PlanoFinanceiroCursoVO() {
        super();
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por validar os dados de um objeto da classe
     * <code>PlanoFinanceiroCursoVO</code>. Todos os tipos de consistï¿½ncia de
     * dados sï¿½o e devem ser implementadas neste mï¿½todo. Sï¿½o
     * validaï¿½ï¿½es tï¿½picas: verificaï¿½ï¿½o de campos obrigatï¿½rios,
     * verificaï¿½ï¿½o de valores vï¿½lidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistï¿½ncia for encontrada aumaticamente ï¿½
     *                gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PlanoFinanceiroCursoVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Plano Financeiro Curso) deve ser informado.");
        }
        if ((obj.getModeloGeracaoParcelas() == null) || (obj.getModeloGeracaoParcelas().equals(""))) {
            throw new ConsistirException("O campo MODELO GERAÇÃO DE PARCELAS (Plano Financeiro Curso) deve ser informado.");
        }
        if (obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs().isEmpty()) {
        	throw new ConsistirException("Deve ser informado ao menos uma CONDIÇÃO DE PAGAMENTO para o PLANO FINANCEIRO DO CURSO.");
        }		
//        if (obj.getTipoCalculoParcela().equals("")) {
//            throw new ConsistirException("O campo FORMA CÁLCULO PARCELAS (Plano Financeiro Curso) deve ser informado.");
//        }
        // if ((obj.getDiaVencimentoParcela() == null) ||
        // (obj.getDiaVencimentoParcela() <= 0) ||
        // (obj.getDiaVencimentoParcela() > 31)) {
        // throw new
        // ConsistirException("O campo DIA PADRÃO VENCIMENTO PARCELAS (Plano Financeiro Curso) deve ser informado.");
        // }
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = new Integer(0);
        }
        return codigo;
    }

    /**
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }


    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PlanoFinanceiroCurso</code>).
     */
    public UsuarioVO getResponsavelAutorizacao() {
        if (responsavelAutorizacao == null) {
            responsavelAutorizacao = new UsuarioVO();
        }
        return (responsavelAutorizacao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PlanoFinanceiroCurso</code>).
     */
    public void setResponsavelAutorizacao(UsuarioVO obj) {
        this.responsavelAutorizacao = obj;
    }
    
//    public String getTipoCalculoParcela() {
//        if (tipoCalculoParcela == null) {
//            tipoCalculoParcela = "";
//        }
//        return tipoCalculoParcela;
//    }
//
//    public void setTipoCalculoParcela(String tipoCalculoParcela) {
//        this.tipoCalculoParcela = tipoCalculoParcela;
//    }

    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> getCondicaoPagamentoPlanoFinanceiroCursoVOs() {
        if (condicaoPagamentoPlanoFinanceiroCursoVOs == null) {
            condicaoPagamentoPlanoFinanceiroCursoVOs = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
        }
        return condicaoPagamentoPlanoFinanceiroCursoVOs;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCursoVOs
     *            the condicaoPagamentoPlanoFinanceiroCursoVOs to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCursoVOs(List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs) {
        this.condicaoPagamentoPlanoFinanceiroCursoVOs = condicaoPagamentoPlanoFinanceiroCursoVOs;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    /**
     * @param descricao
     *            the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the modeloGeracaoParcelas
     */
    public String getModeloGeracaoParcelas() {
        if (modeloGeracaoParcelas == null) {
            modeloGeracaoParcelas = "AM";
        }
        return modeloGeracaoParcelas;
    }

    /**
     * @param modeloGeracaoParcelas
     *            the modeloGeracaoParcelas to set
     */
    public void setModeloGeracaoParcelas(String modeloGeracaoParcelas) {
        this.modeloGeracaoParcelas = modeloGeracaoParcelas;
    }

    public void adicionarCondicaoPagamentoPlanoFinanceiroCursoVOs(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) throws ConsistirException {
        int index = 0;
        for (CondicaoPagamentoPlanoFinanceiroCursoVO objExistente : getCondicaoPagamentoPlanoFinanceiroCursoVOs()) {
        	 if (objExistente.getDescricao().equals(condicaoPagamentoPlanoFinanceiroCursoVO.getDescricao()) &&
             		objExistente.getCategoria().equals(condicaoPagamentoPlanoFinanceiroCursoVO.getCategoria())) {
            	//	throw new ConsistirException(UteisJSF.internacionalizar("prt_PlanoFinanceiroCurso_condicoesDePagamento_jaCadastradoComMesmoNomeECategoria"));
             	getCondicaoPagamentoPlanoFinanceiroCursoVOs().set(index, condicaoPagamentoPlanoFinanceiroCursoVO);
             	Ordenacao.ordenarLista(getCondicaoPagamentoPlanoFinanceiroCursoVOs(), "categoria");
             	return;
             }
             index++;
		}
        getCondicaoPagamentoPlanoFinanceiroCursoVOs().add(condicaoPagamentoPlanoFinanceiroCursoVO);
        Ordenacao.ordenarLista(getCondicaoPagamentoPlanoFinanceiroCursoVOs(), "categoria");
    }

    /**
     * @return the textoPadrao
     */
    public TextoPadraoVO getTextoPadraoContratoMatricula() {
        if (textoPadraoContratoMatricula == null) {
            textoPadraoContratoMatricula = new TextoPadraoVO();
        }
        return textoPadraoContratoMatricula;
    }

    /**
     * @param textoPadrao
     *            the textoPadrao to set
     */
    public void setTextoPadraoContratoMatricula(TextoPadraoVO textoPadraoContratoMatricula) {
        this.textoPadraoContratoMatricula = textoPadraoContratoMatricula;
    }

    /**
     * @return the textoPadraoFiador
     */
    public TextoPadraoVO getTextoPadraoContratoFiador() {
        if (textoPadraoContratoFiador == null) {
            textoPadraoContratoFiador = new TextoPadraoVO();
        }
        return textoPadraoContratoFiador;
    }

    /**
     * @param textoPadraoFiador the textoPadraoFiador to set
     */
    public void setTextoPadraoContratoFiador(TextoPadraoVO textoPadraoContratoFiador) {
        this.textoPadraoContratoFiador = textoPadraoContratoFiador;
    }

    public void setTextoPadraoContratoExtensao(TextoPadraoVO textoPadraoContratoExtensao) {
        this.textoPadraoContratoExtensao = textoPadraoContratoExtensao;
    }

    public TextoPadraoVO getTextoPadraoContratoExtensao() {
        if (textoPadraoContratoExtensao == null) {
            textoPadraoContratoExtensao = new TextoPadraoVO();
        }
        return textoPadraoContratoExtensao;
    }

    public TextoPadraoVO getTextoPadraoContratoModular() {
        if (textoPadraoContratoModular == null) {
            textoPadraoContratoModular = new TextoPadraoVO();
        }
        return textoPadraoContratoModular;
    }

    public void setTextoPadraoContratoModular(TextoPadraoVO textoPadraoContratoModular) {
        this.textoPadraoContratoModular = textoPadraoContratoModular;
    }

    public TextoPadraoVO getTextoPadraoContratoAditivo() {
        if (textoPadraoContratoAditivo == null) {
            textoPadraoContratoAditivo = new TextoPadraoVO();
        }
        return textoPadraoContratoAditivo;
    }

    public void setTextoPadraoContratoAditivo(TextoPadraoVO textoPadraoContratoAditivo) {
        this.textoPadraoContratoAditivo = textoPadraoContratoAditivo;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    
    public TurmaVO getTurma() {
        if(turma == null){
            turma = new TurmaVO();
        }
        return turma;
    }

    
    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }
    
    public Boolean getIsValorPorDisciplina(){
        return getControlarValorPorDisciplina();
    }

    /**
     * @return the controlarValorPorDisciplina
     */
    public Boolean getControlarValorPorDisciplina() {
        if (controlarValorPorDisciplina == null) {
            controlarValorPorDisciplina = Boolean.FALSE;
        }
        return controlarValorPorDisciplina;
    }

    /**
     * @param controlarValorPorDisciplina the controlarValorPorDisciplina to set
     */
    public void setControlarValorPorDisciplina(Boolean controlarValorPorDisciplina) {
        this.controlarValorPorDisciplina = controlarValorPorDisciplina;
    }



	public TextoPadraoVO getTextoPadraoContratoInclusaoReposicao() {
		if (textoPadraoContratoInclusaoReposicao == null) {
			textoPadraoContratoInclusaoReposicao = new TextoPadraoVO();
		}
		return textoPadraoContratoInclusaoReposicao;
	}

	public void setTextoPadraoContratoInclusaoReposicao(TextoPadraoVO textoPadraoContratoInclusaoReposicao) {
		this.textoPadraoContratoInclusaoReposicao = textoPadraoContratoInclusaoReposicao;
	}

	public SituacaoPlanoFinanceiroCursoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoPlanoFinanceiroCursoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoPlanoFinanceiroCursoEnum situacao) {
		this.situacao = situacao;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public UsuarioVO getResponsavelInativacaoVO() {
		if (responsavelInativacaoVO == null) {
			responsavelInativacaoVO = new UsuarioVO();
		}
		return responsavelInativacaoVO;
	}

	public void setResponsavelInativacaoVO(UsuarioVO responsavelInativacaoVO) {
		this.responsavelInativacaoVO = responsavelInativacaoVO;
	}
	
	public Boolean getApresentarBotaoAtivarPlanoFinanceiroCurso() {
		return getSituacao().equals(SituacaoPlanoFinanceiroCursoEnum.EM_CONSTRUCAO) && !getCodigo().equals(0);
	}
	
	public Boolean getApresentarBotaoInativarPlanoFinanceiroCurso() {
		return getSituacao().equals(SituacaoPlanoFinanceiroCursoEnum.ATIVO);
	}

	public String getSituacao_Apresentar() {
		return SituacaoPlanoFinanceiroCursoEnum.getDescricao(getSituacao());
	}
}
