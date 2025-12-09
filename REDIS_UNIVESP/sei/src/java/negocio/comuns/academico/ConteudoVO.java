package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.academico.enumeradores.TipoConteudistaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;

public class ConteudoVO extends SuperVO {

    private static final long serialVersionUID = -239277971078202630L;

    private Integer codigo;
    private String descricao;
    private Integer versao;
    private DisciplinaVO disciplina;
    private Date dataCadastro;
    private UsuarioVO responsavelCadastro;
    private Date dataAlteracao;
    private UsuarioVO responsavelAlteracao;
    private TipoConteudistaEnum tipoConteudista;
    private String nomeConteudista;
    private String emailConteudista;
    private String curriculumConteudista;
    private SituacaoConteudoEnum situacaoConteudo;
    private PessoaVO conteudista;
    private Boolean controlarTempo;
    private Boolean controlarPonto;
    private List<UnidadeConteudoVO> unidadeConteudoVOs;
    private String caminhoBaseBackground;
    private String nomeImagemBackground;
    private String corBackground;
    private TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudo;
    private OrigemBackgroundConteudoEnum origemBackgroundConteudo;
    private PessoaVO professor;
    private Boolean usoExclusivoProfessor;
    private GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO;
    private Boolean excluirImagemBackground;
    /**
     * Atributos transient
     */
    private boolean clonarReasOutraDisciplina = false;
	private boolean descartarReasOutraDisciplina = false;
	private boolean clonarQuestaoOnline = false;
	private boolean clonarQuestaoExercicio = false;
    private boolean existeReaComQuestaoParaSerClonado = false;
    private boolean existeAlunoCursandoConteudo = false;
    
    public ConteudoVO clone() throws CloneNotSupportedException {
    	ConteudoVO clone = (ConteudoVO) super.clone();
    	clone.setCodigo(0);
    	clone.setNovoObj(true);
    	clone.setResponsavelCadastro(new UsuarioVO());
    	clone.setDataCadastro(new Date());
    	clone.setVersao(this.getVersao() + 1);
    	clone.setSituacaoConteudo(SituacaoConteudoEnum.EM_ELABORACAO);
    	clone.setUnidadeConteudoVOs(new ArrayList<UnidadeConteudoVO>());
    	for (UnidadeConteudoVO unidadeConteudoVO : this.getUnidadeConteudoVOs()) {
			UnidadeConteudoVO cloneUnidadeConteudoVO = unidadeConteudoVO.clone();
			clone.getUnidadeConteudoVOs().add(cloneUnidadeConteudoVO);
		}   	
		return clone;	
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
        	dataCadastro = new Date();
        }
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

    public Date getDataAlteracao() {
        if (dataAlteracao == null) {
            dataAlteracao = new Date();
        }
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public UsuarioVO getResponsavelAlteracao() {
        if (responsavelAlteracao == null) {
            responsavelAlteracao = new UsuarioVO();
        }
        return responsavelAlteracao;
    }

    public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
        this.responsavelAlteracao = responsavelAlteracao;
    }

    public TipoConteudistaEnum getTipoConteudista() {
        if (tipoConteudista == null) {
            tipoConteudista = TipoConteudistaEnum.EXTERNO;
        }
        return tipoConteudista;
    }

    public void setTipoConteudista(TipoConteudistaEnum tipoConteudista) {
        this.tipoConteudista = tipoConteudista;
    }

    public String getNomeConteudista() {
        if (nomeConteudista == null) {
            nomeConteudista = "";
        }
        return nomeConteudista;
    }

    public void setNomeConteudista(String nomeConteudista) {
        this.nomeConteudista = nomeConteudista;
    }

    public String getEmailConteudista() {
        if (emailConteudista == null) {
            emailConteudista = "";
        }
        return emailConteudista;
    }

    public void setEmailConteudista(String emailConteudista) {
        this.emailConteudista = emailConteudista;
    }

    public String getCurriculumConteudista() {
        if (curriculumConteudista == null) {
            curriculumConteudista = "";
        }
        return curriculumConteudista;
    }

    public void setCurriculumConteudista(String curriculumConteudista) {
        this.curriculumConteudista = curriculumConteudista;
    }

    public SituacaoConteudoEnum getSituacaoConteudo() {
        if (situacaoConteudo == null) {
            situacaoConteudo = SituacaoConteudoEnum.EM_ELABORACAO;
        }
        return situacaoConteudo;
    }
    
    public String getSituacaoConteudo_Apresentar() {
    	return getSituacaoConteudo().getValorApresentar();
    }

    public void setSituacaoConteudo(SituacaoConteudoEnum situacaoConteudo) {
        this.situacaoConteudo = situacaoConteudo;
    }

    public PessoaVO getConteudista() {
        if (conteudista == null) {
            conteudista = new PessoaVO();
        }
        return conteudista;
    }

    public void setConteudista(PessoaVO conteudista) {
        this.conteudista = conteudista;
    }

    public Boolean getControlarTempo() {
        if (controlarTempo == null) {
            controlarTempo = true;
        }
        return controlarTempo;
    }

    public void setControlarTempo(Boolean controlarTempo) {
        this.controlarTempo = controlarTempo;
    }

    public Boolean getControlarPonto() {
        if (controlarPonto == null) {
            controlarPonto = true;
        }
        return controlarPonto;
    }

    public void setControlarPonto(Boolean controlarPonto) {
        this.controlarPonto = controlarPonto;
    }

    public Integer getVersao() {
        if (versao == null) {
            versao = 1;
        }
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public List<UnidadeConteudoVO> getUnidadeConteudoVOs() {
        if (unidadeConteudoVOs == null) {
            unidadeConteudoVOs = new ArrayList<UnidadeConteudoVO>(0);
        }
        return unidadeConteudoVOs;
    }

    public void setUnidadeConteudoVOs(List<UnidadeConteudoVO> unidadeConteudoVOs) {
        this.unidadeConteudoVOs = unidadeConteudoVOs;
    }

    public Boolean getIsInativo() {
        return !isNovoObj() && getSituacaoConteudo().equals(SituacaoConteudoEnum.INATIVO);
    }

    public Boolean getIsAtivo() {
        return !isNovoObj() && getSituacaoConteudo().equals(SituacaoConteudoEnum.ATIVO);
    }

    public Boolean getIsEmElaboracao() {
        return !isNovoObj() && getSituacaoConteudo().equals(SituacaoConteudoEnum.EM_ELABORACAO);
    }

    public Boolean getIsConteudistaInterno() {
        return getTipoConteudista().equals(TipoConteudistaEnum.INTERNO);
    }

    public Boolean getIsConteudistaExterno() {
        return getTipoConteudista().equals(TipoConteudistaEnum.EXTERNO);
    }

    public Integer getTempoTotal() {
        Integer tempo = 0;
        for (UnidadeConteudoVO unidadeConteudoVO : getUnidadeConteudoVOs()) {
            tempo += unidadeConteudoVO.getTempo();
        }
        return tempo;
    }
    
    public Integer getPaginaTotal() {
        Integer paginas = 0;
        for (UnidadeConteudoVO unidadeConteudoVO : getUnidadeConteudoVOs()) {
            paginas += unidadeConteudoVO.getPaginas();
        }
        return paginas;
    }
    
    public Double getPontoTotal() {
        Double ponto = 0.0;
        for (UnidadeConteudoVO unidadeConteudoVO : getUnidadeConteudoVOs()) {
            ponto += unidadeConteudoVO.getPonto();
        }
        return ponto;
    }
    
    public Integer getTotalUnidade(){
        return getUnidadeConteudoVOs().size();
    }

	public String getCaminhoBaseBackground() {
		if(caminhoBaseBackground == null){
			caminhoBaseBackground = "";
		}
		return caminhoBaseBackground;
	}

	public void setCaminhoBaseBackground(String caminhoBaseBackground) {
		this.caminhoBaseBackground = caminhoBaseBackground;
	}

	public String getNomeImagemBackground() {
		if(nomeImagemBackground == null){
			nomeImagemBackground = "";
		}
		return nomeImagemBackground;
	}

	public void setNomeImagemBackground(String nomeImagemBackground) {
		this.nomeImagemBackground = nomeImagemBackground;
	}

	public String getCorBackground() {
		if(corBackground == null){
			corBackground = "#FFFFFF";
		}
		return corBackground;
	}

	public void setCorBackground(String corBackground) {
		this.corBackground = corBackground;
	}
	
	public TamanhoImagemBackgroundConteudoEnum getTamanhoImagemBackgroundConteudo() {
		if(tamanhoImagemBackgroundConteudo == null){
			tamanhoImagemBackgroundConteudo = TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO;
		}
		return tamanhoImagemBackgroundConteudo;
	}

	public void setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum) {
		this.tamanhoImagemBackgroundConteudo = tamanhoImagemBackgroundConteudoEnum;
	}

	public OrigemBackgroundConteudoEnum getOrigemBackgroundConteudo() {
            if (origemBackgroundConteudo == null) {
                origemBackgroundConteudo = OrigemBackgroundConteudoEnum.SEM_BACKGROUND;
            }
            return origemBackgroundConteudo;
	}

	public void setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum origemBackgroundConteudo) {
		this.origemBackgroundConteudo = origemBackgroundConteudo;
	}
	
	public Boolean getExisteImagemBackground(){
		return !getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && (!getCaminhoBaseBackground().trim().isEmpty() || !getCorBackground().trim().isEmpty());
	}
	
	/**
	 * @author Victor Hugo 11/12/2014
	 */
	private Double percentAtingido;
	private Double pontosAtingidos;
	private Double percentEstudado;
	private Double percentARealizar;
	private Double percentAtrasado;

	public Double getPercentAtingido() {
		if (percentAtingido == null) {
			percentAtingido = 0.0;
		}
		return percentAtingido;
	}

	public void setPercentAtingido(Double percentAtingido) {
		this.percentAtingido = percentAtingido;
	}

	public Double getPontosAtingidos() {
		if (pontosAtingidos == null) {
			pontosAtingidos = 0.0;
		}
		return pontosAtingidos;
	}

	public void setPontosAtingidos(Double pontosAtingidos) {
		this.pontosAtingidos = pontosAtingidos;
	}

	public Double getPercentEstudado() {
		if (percentEstudado == null) {
			percentEstudado = 0.0;
		}
		return percentEstudado;
	}

	public void setPercentEstudado(Double percentEstudado) {
		this.percentEstudado = percentEstudado;
	}

	public Double getPercentARealizar() {
		if (percentARealizar == null) {
			percentARealizar = 0.0;
		}
		return percentARealizar;
	}

	public void setPercentARealizar(Double percentARealizar) {
		this.percentARealizar = percentARealizar;
	}

	public Double getPercentAtrasado() {
		if (percentAtrasado == null) {
			percentAtrasado = 0.0;
		}
		return percentAtrasado;
	}

	public void setPercentAtrasado(Double percentAtrasado) {
		this.percentAtrasado = percentAtrasado;
	}
	
	/**
	 * @author Victor Hugo 09/01/2015
	 * 
	 */
	private Integer qtdeExercicioDificil;
	private Integer qtdeExercicioFacil;
	private Integer qtdeExercicioMedio;


	public Integer getQtdeExercicioDificil() {
		if (qtdeExercicioDificil == null) {
			qtdeExercicioDificil = 0;
		}
		return qtdeExercicioDificil;
	}

	public void setQtdeExercicioDificil(Integer qtdeExercicioDificil) {
		this.qtdeExercicioDificil = qtdeExercicioDificil;
	}

	public Integer getQtdeExercicioFacil() {
		if (qtdeExercicioFacil == null) {
			qtdeExercicioFacil = 0;
		}
		return qtdeExercicioFacil;
	}

	public void setQtdeExercicioFacil(Integer qtdeExercicioFacil) {
		this.qtdeExercicioFacil = qtdeExercicioFacil;
	}

	public Integer getQtdeExercicioMedio() {
		if (qtdeExercicioMedio == null) {
			qtdeExercicioMedio = 0;
		}
		return qtdeExercicioMedio;
	}

	public void setQtdeExercicioMedio(Integer qtdeExercicioMedio) {
		this.qtdeExercicioMedio = qtdeExercicioMedio;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}
	
	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}
	
	public Boolean getUsoExclusivoProfessor() {
		if(usoExclusivoProfessor == null) {
			usoExclusivoProfessor = false;
		}
		return usoExclusivoProfessor;
	}
	
	public void setUsoExclusivoProfessor(Boolean usoExclusivoProfessor) {
		this.usoExclusivoProfessor = usoExclusivoProfessor;
	}
	
	public GestaoEventoConteudoTurmaVO getGestaoEventoConteudoTurmaVO() {
		if (gestaoEventoConteudoTurmaVO == null) {
			gestaoEventoConteudoTurmaVO = new GestaoEventoConteudoTurmaVO();
		}
		return gestaoEventoConteudoTurmaVO;
	}


	public void setGestaoEventoConteudoTurmaVO(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) {
		this.gestaoEventoConteudoTurmaVO = gestaoEventoConteudoTurmaVO;
	}
	
	public boolean isExisteReaComQuestaoParaSerClonado() {
		return existeReaComQuestaoParaSerClonado;
	}


	public void setExisteReaComQuestaoParaSerClonado(boolean existeReaComQuestaoParaSerClonado) {
		this.existeReaComQuestaoParaSerClonado = existeReaComQuestaoParaSerClonado;
	}
	
	public boolean isClonarReasOutraDisciplina() {
		return clonarReasOutraDisciplina;
	}

	public void setClonarReasOutraDisciplina(boolean clonarReasOutraDisciplina) {
		this.clonarReasOutraDisciplina = clonarReasOutraDisciplina;
	}

	public boolean isDescartarReasOutraDisciplina() {
		return descartarReasOutraDisciplina;
	}

	public void setDescartarReasOutraDisciplina(boolean descartarReasOutraDisciplina) {
		this.descartarReasOutraDisciplina = descartarReasOutraDisciplina;
	}


	public boolean isClonarQuestaoOnline() {
		return clonarQuestaoOnline;
	}


	public void setClonarQuestaoOnline(boolean clonarQuestaoOnline) {
		this.clonarQuestaoOnline = clonarQuestaoOnline;
	}


	public boolean isClonarQuestaoExercicio() {
		return clonarQuestaoExercicio;
	}


	public void setClonarQuestaoExercicio(boolean clonarQuestaoExercicio) {
		this.clonarQuestaoExercicio = clonarQuestaoExercicio;
	}
	
	public boolean isExisteAlunoCursandoConteudo() {
		return existeAlunoCursandoConteudo;
	}


	public void setExisteAlunoCursandoConteudo(boolean existeAlunoCursandoConteudo) {
		this.existeAlunoCursandoConteudo = existeAlunoCursandoConteudo;
	}
	
	public String getDescricaoCombobox() {
		return getCodigo()+" - "+getDescricao();
	}
}
