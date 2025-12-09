package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.PublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.RestricaoPublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.SituacaoForumEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.FiltroBooleanEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.Uteis;


public class ForumVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3199287413601234270L;
    private Integer codigo;
    private UsuarioVO responsavelCriacao;
    private Date dataCriacao;
    UsuarioVO responsavelInativacao;
    private Date dataInativacao;
    private DisciplinaVO disciplina;
    private TurmaVO turma;
    private ConteudoVO conteudo;
    private SituacaoForumEnum situacaoForum;
    private String tema;
    private CursoVO cursoVO;
    private AreaConhecimentoVO areaConhecimentoVO;
    private PublicoAlvoForumEnum publicoAlvoForumEnum;
    private RestricaoPublicoAlvoForumEnum restricaoPublicoAlvoForumEnum;
    private boolean forumAvaliado;
    private FiltroBooleanEnum filtroForumAvaliado;
    private Integer qtdeAtualizacao;
    private List<ForumPessoaVO> forumPessoaVOs;
    private List<ForumRegistrarNotaVO> forumRegistrarNotaVOs;
    public String ano;
    public String semestre;
	private String tipoPeriodoDisponibilizacao;
    
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public UsuarioVO getResponsavelCriacao() {
        if(responsavelCriacao == null){
            responsavelCriacao = new UsuarioVO();
        }
        return responsavelCriacao;
    }
    
    public void setResponsavelCriacao(UsuarioVO responsavelCriacao) {
        this.responsavelCriacao = responsavelCriacao;
    }
    
    public Date getDataCriacao() {
        if(dataCriacao == null){
            dataCriacao = new Date();
        }
        return dataCriacao;
    }
    
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public UsuarioVO getResponsavelInativacao() {
        return responsavelInativacao;
    }
    
    public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
        this.responsavelInativacao = responsavelInativacao;
    }
    
    public Date getDataInativacao() {
        return dataInativacao;
    }
    
    public void setDataInativacao(Date dataInativacao) {
        this.dataInativacao = dataInativacao;
    }
    
    public DisciplinaVO getDisciplina() {
        if(disciplina == null){
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }
    
    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
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
    
    public ConteudoVO getConteudo() {
        if(conteudo == null){
            conteudo = new ConteudoVO();
        }
        return conteudo;
    }
    
    public void setConteudo(ConteudoVO conteudo) {
        this.conteudo = conteudo;
    }
    
    public SituacaoForumEnum getSituacaoForum() {
        if(situacaoForum == null){
            situacaoForum = SituacaoForumEnum.ATIVO;
        }
        return situacaoForum;
    }
    
    public void setSituacaoForum(SituacaoForumEnum situacaoForum) {
        this.situacaoForum = situacaoForum;
    }

    
    public String getTema() {
        if(tema == null){
            tema ="";
        }
        return tema;
    }

    
    public void setTema(String tema) {
        this.tema = tema;
    }
    
    public Integer getQtdeAtualizacao() {
        return qtdeAtualizacao;
    }

    
    public void setQtdeAtualizacao(Integer qtdeAtualizacao) {
        this.qtdeAtualizacao = qtdeAtualizacao;
    }
    
    public PublicoAlvoForumEnum getPublicoAlvoForumEnum() {
		if (publicoAlvoForumEnum == null) {
			publicoAlvoForumEnum = PublicoAlvoForumEnum.NENHUM;
		}
		return publicoAlvoForumEnum;
	}

	public void setPublicoAlvoForumEnum(PublicoAlvoForumEnum publicoAlvoForumEnum) {
		this.publicoAlvoForumEnum = publicoAlvoForumEnum;
	}

	public RestricaoPublicoAlvoForumEnum getRestricaoPublicoAlvoForumEnum() {
		if (restricaoPublicoAlvoForumEnum == null) {
			restricaoPublicoAlvoForumEnum = RestricaoPublicoAlvoForumEnum.NENHUM;
		}
		return restricaoPublicoAlvoForumEnum;
	}

	public void setRestricaoPublicoAlvoForumEnum(RestricaoPublicoAlvoForumEnum restricaoPublicoAlvoForumEnum) {
		this.restricaoPublicoAlvoForumEnum = restricaoPublicoAlvoForumEnum;
	}

	public boolean isForumAvaliado() {
		return forumAvaliado;
	}

	public void setForumAvaliado(boolean forumAvaliado) {
		this.forumAvaliado = forumAvaliado;
	}

	public FiltroBooleanEnum getFiltroForumAvaliado() {
		return filtroForumAvaliado;
	}

	public void setFiltroForumAvaliado(FiltroBooleanEnum filtroForumAvaliado) {
		this.filtroForumAvaliado = filtroForumAvaliado;
	}

	public AreaConhecimentoVO getAreaConhecimentoVO() {
		if(areaConhecimentoVO == null){
			areaConhecimentoVO = new AreaConhecimentoVO();
		}
		return areaConhecimentoVO;
	}

	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
		this.areaConhecimentoVO = areaConhecimentoVO;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null){
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	
	public List<ForumPessoaVO> getForumPessoaVOs() {
		if(forumPessoaVOs == null){
			forumPessoaVOs = new ArrayList<ForumPessoaVO>();
		}
		return forumPessoaVOs;
	}

	public void setForumPessoaVOs(List<ForumPessoaVO> forumPessoaVOs) {
		this.forumPessoaVOs = forumPessoaVOs;
	}
	
	public List<ForumRegistrarNotaVO> getForumRegistrarNotaVOs() {
		if(forumRegistrarNotaVOs == null){
			forumRegistrarNotaVOs = new ArrayList<ForumRegistrarNotaVO>();
		}
		return forumRegistrarNotaVOs;
	}

	public void setForumRegistrarNotaVOs(List<ForumRegistrarNotaVO> forumRegistraNotaVOs) {
		this.forumRegistrarNotaVOs = forumRegistraNotaVOs;
	}

	public String getCssCampoAreaConhecimento(){
		return Uteis.isAtributoPreenchido(getRestricaoPublicoAlvoForumEnum()) && getRestricaoPublicoAlvoForumEnum().isRestricaoAreaConhecimento() ? "camposObrigatorios":"campos";
	}
	
	public String getCssCampoCurso(){
		return Uteis.isAtributoPreenchido(getRestricaoPublicoAlvoForumEnum()) && getRestricaoPublicoAlvoForumEnum().isRestricaoCurso() ? "camposObrigatorios":"campos";
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	
	public String getTipoPeriodoDisponibilizacao() {
		if (tipoPeriodoDisponibilizacao == null) {
			tipoPeriodoDisponibilizacao = "";
		}
		return tipoPeriodoDisponibilizacao;
	}

	public void setTipoPeriodoDisponibilizacao(String tipoPeriodoDisponibilizacao) {
		this.tipoPeriodoDisponibilizacao = tipoPeriodoDisponibilizacao;
	}
}
