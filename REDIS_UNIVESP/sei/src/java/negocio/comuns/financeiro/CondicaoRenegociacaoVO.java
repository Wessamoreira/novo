package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CondicaoRenegociacao. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

public class CondicaoRenegociacaoVO extends SuperVO {
	
    private static final long serialVersionUID = 1L;
	private Date dataCriacao;
    private Integer codigo;
    private String descricao;
    
    private Double descontoEspecifico;
    private Double juroEspecifico;
    private String status;
    private Date dataUltimaAlteracao;
    /** Atributo responsável por manter os objetos da classe <code>ItemCondicaoRenegociacao</code>. */
    private List<ItemCondicaoRenegociacaoVO> itemCondicaoRenegociacaoVOs;
    /** Atributo responsável por manter o objeto 
     * 
     * 
     * relacionado da classe <code>Usuario </code>.*/
    protected UsuarioVO usuarioCriacao;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Usuario </code>.*/
    protected UsuarioVO usuarioUltimaAlteracao;
    /** Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.*/
//    protected UnidadeEnsinoVO unidadeEnsino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Turma </code>.*/
    protected TurmaVO turma;
    /** Atributo responsável por manter o objeto relacionado da classe <code>ContaCorrente </code>.*/
    protected ContaCorrenteVO contaCorrentePadrao;
    /** Atributo responsável por manter o objeto relacionado da classe <code>DescontoProgressivo </code>.*/
    protected DescontoProgressivoVO descontoProgressivo;
    
    private TurnoVO turno;
    
    private PerfilEconomicoVO perfilEconomico;
    
    private GrupoDestinatariosVO grupoDestinatario;
    private Date dataInicioVigencia;
    private Date dataTerminoVigencia;
    private Boolean liberarRenovacaoAposPagamentoPrimeiraParcela;
    private Boolean liberarRenovacaoAposPagamentoTodasParcelas;
    private LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum layoutPadraoTermoReconhecimentoDivida;
    private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
    
    private List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoRenegociacaoUnidadeEnsinoVOs;
    private Boolean utilizarContaCorrenteEspecifica;
    private Boolean permitirPagamentoCartaoCreditoVisaoAluno;
    
    private List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoRenegociacaoFuncionarioVOs;
    
    /**
     * Construtor padrão da classe <code>CondicaoRenegociacao</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public CondicaoRenegociacaoVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>DescontoProgressivo</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public DescontoProgressivoVO getDescontoProgressivo() {
        if (descontoProgressivo == null) {
            descontoProgressivo = new DescontoProgressivoVO();
        }
        return (descontoProgressivo);
    }
     
    /**
     * Define o objeto da classe <code>DescontoProgressivo</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setDescontoProgressivo( DescontoProgressivoVO obj) {
        this.descontoProgressivo = obj;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public ContaCorrenteVO getContaCorrentePadrao() {
        if (contaCorrentePadrao == null) {
            contaCorrentePadrao = new ContaCorrenteVO();
        }
        return (contaCorrentePadrao);
    }
     
    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setContaCorrentePadrao( ContaCorrenteVO obj) {
        this.contaCorrentePadrao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turma</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return (turma);
    }
     
    /**
     * Define o objeto da classe <code>Turma</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setTurma( TurmaVO obj) {
        this.turma = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }
     
    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setCurso( CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public UsuarioVO getUsuarioUltimaAlteracao() {
        if (usuarioUltimaAlteracao == null) {
            usuarioUltimaAlteracao = new UsuarioVO();
        }
        return (usuarioUltimaAlteracao);
    }
     
    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setUsuarioUltimaAlteracao( UsuarioVO obj) {
        this.usuarioUltimaAlteracao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public UsuarioVO getUsuarioCriacao() {
        if (usuarioCriacao == null) {
            usuarioCriacao = new UsuarioVO();
        }
        return (usuarioCriacao);
    }
     
    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (<code>CondicaoRenegociacao</code>).
    */
    public void setUsuarioCriacao( UsuarioVO obj) {
        this.usuarioCriacao = obj;
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>ItemCondicaoRenegociacao</code>. */
    public List<ItemCondicaoRenegociacaoVO> getItemCondicaoRenegociacaoVOs() {
        if (itemCondicaoRenegociacaoVOs == null) {
            itemCondicaoRenegociacaoVOs = new ArrayList<ItemCondicaoRenegociacaoVO>(0);
        }
        return (itemCondicaoRenegociacaoVOs);
    }
     
    /** Define Atributo responsável por manter os objetos da classe <code>ItemCondicaoRenegociacao</code>. */
    public void setItemCondicaoRenegociacaoVOs( List<ItemCondicaoRenegociacaoVO> itemCondicaoRenegociacaoVOs ) {
        this.itemCondicaoRenegociacaoVOs = itemCondicaoRenegociacaoVOs;
    }

    public String getStatus() {
        if (status == null) {
            status = "AT";
        }
        return status;
    }
     
    public void setStatus( String status ) {
        this.status = status;
    }

    public Double getJuroEspecifico() {
        if (juroEspecifico == null) {
            juroEspecifico = 0.0;
        }
        return (juroEspecifico);
    }
     
    public void setJuroEspecifico( Double juroEspecifico ) {
        this.juroEspecifico = juroEspecifico;
    }

    public Double getDescontoEspecifico() {
        if (descontoEspecifico == null) {
            descontoEspecifico = 0.0;
        }
        return (descontoEspecifico);
    }
     
    public void setDescontoEspecifico( Double descontoEspecifico ) {
        this.descontoEspecifico = descontoEspecifico;
    }

    

    public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
    	return turno;
	}




	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}




	public String getDescricao() {
        if (descricao == null) {
            return "";
        }
        return (descricao);
    }
     
    public void setDescricao( String descricao ) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }
     
    public void setCodigo( Integer codigo ) {
        this.codigo = codigo;
    }

    public Date getDataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = new Date();
        }
        return (dataCriacao);
    }
     
    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
    */
    public String getDataCriacao_Apresentar() {
        return (Uteis.getData(dataCriacao));
    }
     
    public void setDataCriacao( Date dataCriacao ) {
        this.dataCriacao = dataCriacao;
    }




	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}




	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	
	public boolean getIsAtivo(){
	    return !getNovoObj() && getStatus().equals("AT");
	}
	
	public boolean getIsInativo(){
        return !getNovoObj() && getStatus().equals("IN");
    }




    
    public PerfilEconomicoVO getPerfilEconomico() {
        if(perfilEconomico == null){
            perfilEconomico = new PerfilEconomicoVO();
        }
        return perfilEconomico;
    }




    
    public void setPerfilEconomico(PerfilEconomicoVO perfilEconomicoVO) {
        this.perfilEconomico = perfilEconomicoVO;
    }




    
    public Date getDataInicioVigencia() {
        return dataInicioVigencia;
    }




    
    public void setDataInicioVigencia(Date dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }




    
    public Date getDataTerminoVigencia() {        
        return dataTerminoVigencia;
    }




    
    public void setDataTerminoVigencia(Date dataTerminoVigencia) {
        this.dataTerminoVigencia = dataTerminoVigencia;
    }
	
    
    public GrupoDestinatariosVO getGrupoDestinatario() {
        if(grupoDestinatario == null){
            grupoDestinatario = new GrupoDestinatariosVO();
        }
        return grupoDestinatario;
    }

    
    public void setGrupoDestinatario(GrupoDestinatariosVO grupoDestinatariosVO) {
        this.grupoDestinatario = grupoDestinatariosVO;
    }
    
    public Boolean getLiberarRenovacaoAposPagamentoPrimeiraParcela() {
        if(liberarRenovacaoAposPagamentoPrimeiraParcela == null){
            liberarRenovacaoAposPagamentoPrimeiraParcela = false;
        }
        return liberarRenovacaoAposPagamentoPrimeiraParcela;
    }

    
    public void setLiberarRenovacaoAposPagamentoPrimeiraParcela(Boolean liberarRenovacaoAposPagamentoPrimeiraParcela) {
        this.liberarRenovacaoAposPagamentoPrimeiraParcela = liberarRenovacaoAposPagamentoPrimeiraParcela;
    }

    
    public Boolean getLiberarRenovacaoAposPagamentoTodasParcelas() {
        if(liberarRenovacaoAposPagamentoTodasParcelas == null){
            liberarRenovacaoAposPagamentoTodasParcelas = false;
        }        
        return liberarRenovacaoAposPagamentoTodasParcelas;
    }

    
    public void setLiberarRenovacaoAposPagamentoTodasParcelas(Boolean liberarRenovacaoAposPagamentoTodasParcelas) {
        this.liberarRenovacaoAposPagamentoTodasParcelas = liberarRenovacaoAposPagamentoTodasParcelas;
    }

	public LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum getLayoutPadraoTermoReconhecimentoDivida() {
		return layoutPadraoTermoReconhecimentoDivida;
	}

	public void setLayoutPadraoTermoReconhecimentoDivida(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum layoutPadraoTermoReconhecimentoDivida) {
		this.layoutPadraoTermoReconhecimentoDivida = layoutPadraoTermoReconhecimentoDivida;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
		if (textoPadraoDeclaracaoVO == null) {
			textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracaoVO;
	}

	public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
		this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
	}
	
	public List<CondicaoRenegociacaoUnidadeEnsinoVO> getListaCondicaoRenegociacaoUnidadeEnsinoVOs() {
		if (listaCondicaoRenegociacaoUnidadeEnsinoVOs == null) {
			listaCondicaoRenegociacaoUnidadeEnsinoVOs = new ArrayList<CondicaoRenegociacaoUnidadeEnsinoVO>(0);
		}
		return listaCondicaoRenegociacaoUnidadeEnsinoVOs;
	}

	public void setListaCondicaoRenegociacaoUnidadeEnsinoVOs(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoRenegociacaoUnidadeEnsinoVOs) {
		this.listaCondicaoRenegociacaoUnidadeEnsinoVOs = listaCondicaoRenegociacaoUnidadeEnsinoVOs;
	}

	public Boolean getUtilizarContaCorrenteEspecifica() {
		if (utilizarContaCorrenteEspecifica == null) {
			utilizarContaCorrenteEspecifica = false;
		}
		return utilizarContaCorrenteEspecifica;
	}

	public void setUtilizarContaCorrenteEspecifica(Boolean utilizarContaCorrenteEspecifica) {
		this.utilizarContaCorrenteEspecifica = utilizarContaCorrenteEspecifica;
	}

	public List<CondicaoRenegociacaoFuncionarioVO> getListaCondicaoRenegociacaoFuncionarioVOs() {
		if (listaCondicaoRenegociacaoFuncionarioVOs == null) {
			listaCondicaoRenegociacaoFuncionarioVOs = new ArrayList<CondicaoRenegociacaoFuncionarioVO>(0);
		}
		return listaCondicaoRenegociacaoFuncionarioVOs;
	}

	public void setListaCondicaoRenegociacaoFuncionarioVOs(List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoRenegociacaoFuncionarioVOs) {
		this.listaCondicaoRenegociacaoFuncionarioVOs = listaCondicaoRenegociacaoFuncionarioVOs;
	}
	
	public Boolean getUsuarioPossuiPermissaoParaRealizarNegociacao(Integer pessoa) {
		if (getListaCondicaoRenegociacaoFuncionarioVOs().isEmpty()) {
			return true;
		}
		boolean possuiPermissao = false;
		for (CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO : getListaCondicaoRenegociacaoFuncionarioVOs()) {
			if (condicaoRenegociacaoFuncionarioVO.getFuncionarioVO().getPessoa().getCodigo().equals(pessoa)) {
				possuiPermissao = true;
				break;
			}
		}
		return possuiPermissao;
	}
	
	public String status_Apresentar;
	public String getStatus_Apresentar() {
		if(status_Apresentar == null) {
		if(getStatus().equals("AT")) {
			if(Uteis.isAtributoPreenchido(getDataInicioVigencia()) && !Uteis.getData(getDataInicioVigencia()).equals(Uteis.getData(new Date())) && getDataInicioVigencia().compareTo(new Date()) > 0) {
				status_Apresentar = "Ativo Fora Prazo Vigência";
			}else if(Uteis.isAtributoPreenchido(getDataInicioVigencia()) && Uteis.isAtributoPreenchido(getDataTerminoVigencia()) && (Uteis.getData(getDataInicioVigencia()).equals(Uteis.getData(new Date())) || getDataInicioVigencia().compareTo(new Date()) <= 0) &&
					(Uteis.getData(getDataTerminoVigencia()).equals(Uteis.getData(new Date())) || getDataTerminoVigencia().compareTo(new Date()) >= 0)) {
				status_Apresentar = "Ativo Dentro Prazo Vigência";
			}else if(Uteis.isAtributoPreenchido(getDataTerminoVigencia()) && !Uteis.getData(getDataTerminoVigencia()).equals(Uteis.getData(new Date())) && getDataTerminoVigencia().compareTo(new Date()) < 0) {
				status_Apresentar = "Ativo Prazo Encerrado";
			}else {
				status_Apresentar = "Ativo Sem Prazo Vigência";
			}
		}else {
		status_Apresentar =  "Inativo";
		}
		}
		return status_Apresentar;
	}

	public Boolean getPermitirPagamentoCartaoCreditoVisaoAluno() {
		if(permitirPagamentoCartaoCreditoVisaoAluno == null) {
			permitirPagamentoCartaoCreditoVisaoAluno = true;
		}
		return permitirPagamentoCartaoCreditoVisaoAluno;
	}

	public void setPermitirPagamentoCartaoCreditoVisaoAluno(Boolean permitirPagamentoCartaoCreditoVisaoAluno) {
		this.permitirPagamentoCartaoCreditoVisaoAluno = permitirPagamentoCartaoCreditoVisaoAluno;
	}
}