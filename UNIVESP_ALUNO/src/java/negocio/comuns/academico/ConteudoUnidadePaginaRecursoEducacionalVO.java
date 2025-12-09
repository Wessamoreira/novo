package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;

import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;


public class ConteudoUnidadePaginaRecursoEducacionalVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 7035302788147090533L;
    private Integer codigo;
    private TipoRecursoEducacionalEnum tipoRecursoEducacional;    
    private String titulo;    
    private String texto;
    private String descricao;
    private Integer altura;
    private Integer largura;    
    private String caminhoBaseRepositorio;
    private String nomeRealArquivo;
    private String nomeFisicoArquivo;
    private RecursoEducacionalVO recursoEducacional;
    private Boolean manterRecursoDisponivelPagina;
    private ConteudoUnidadePaginaVO conteudoUnidadePagina; 
    private Date dataCadastro;
    private UsuarioVO usuarioCadastro;
    private Date dataAlteracao;
    private UsuarioVO usuarioAlteracao;
    private Integer ordemApresentacao;
    private MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacional;
    private TipoGraficoEnum tipoGrafico;
    private String tituloGrafico;
    private String tituloEixoX;
    private String tituloEixoY;
    private Boolean apresentarLegenda;
    private String valorGrafico;
    private String categoriaGrafico;
    private ForumVO forum;
    private AvaliacaoOnlineVO avaliacaoOnlineVO;
    private ListaExercicioVO listaExercicio;
    /*
     * Atributos de controle
     */
    private Boolean publicarBibliotecaRecursoEducacional;
    private List<ConteudoUnidadePaginaGraficoPizzaVO> conteudoUnidadePaginaGraficoVOs;
    private List<ConteudoUnidadePaginaGraficoCategoriaVO> conteudoUnidadePaginaGraficoCategoriaVOs;
    private List<ConteudoUnidadePaginaGraficoSerieVO> conteudoUnidadePaginaGraficoSerieVOs;
    
    private String caminhoBaseBackground;
    private String nomeImagemBackground;
    private String corBackground;
    private TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudo;
    private OrigemBackgroundConteudoEnum origemBackgroundConteudo;
    /**
     * Transiente
     */
    private Boolean excluirImagemBackground;
    /**
     * Fim Transiente
     */
    private List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs;
    private Boolean autoAvaliacao;
    private Boolean alunoAvaliaAluno;
    private Boolean professorAvaliaAluno;
    private String formulaCalculoNotaFinal;
    private Boolean utilizarNotaConceito;
    private Boolean requerLiberacaoProfessor;
    private Boolean permiteAlunoAvancarConteudoSemLancarNota;
    private Double faixaMinimaNotaAutoAvaliacao;
    private Double faixaMaximaNotaAutoAvaliacao;
    private Double faixaMinimaNotaAlunoAvaliaAluno;
    private Double faixaMaximaNotaAlunoAvaliaAluno;
    private Double faixaMinimaNotaProfessorAvaliaAluno;
    private Double faixaMaximaNotaProfessorAvaliaAluno;
    /**
     * Transiente
     * Utilizado no PBL
     */
    public static final String AUTO_AVAL = "AUTO_AVAL";
    public static final String ALUNO_AVAL = "ALUNO_AVAL";
    public static final String PROF_AVAL = "PROF_AVAL";
    public static final String QTDE_ALU = "QTDE_ALU";
    private GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO;
    private GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO;
    private SituacaoPBLEnum situacaoGestaoEventoConteudoTurma;
    
    private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;

    public ConteudoUnidadePaginaRecursoEducacionalVO clone() throws CloneNotSupportedException {
    	ConteudoUnidadePaginaRecursoEducacionalVO clone = (ConteudoUnidadePaginaRecursoEducacionalVO) super.clone();
    	clone.setNovoObj(true);
        clone.setCodigo(0);
    	clone.setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());    	        
        clone.getRecursoEducacional().setNovoObj(true);
		clone.getRecursoEducacional().setCodigo(0);
    	clone.setUsuarioCadastro(new UsuarioVO());
    	clone.setDataCadastro(new Date()); 	
    	clone.setConteudoUnidadePaginaGraficoVOs(new ArrayList<ConteudoUnidadePaginaGraficoPizzaVO>());
    	clone.setNotaConceitoAvaliacaoPBLVOs(new ArrayList<NotaConceitoAvaliacaoPBLVO>());
    	for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoPizzaVO : this.getConteudoUnidadePaginaGraficoVOs()) {
			ConteudoUnidadePaginaGraficoPizzaVO cloneConteudoUnidadePaginaGraficoPizzaVO = conteudoUnidadePaginaGraficoPizzaVO.clone();
			clone.getConteudoUnidadePaginaGraficoVOs().add(cloneConteudoUnidadePaginaGraficoPizzaVO);
		}
    	clone.setConteudoUnidadePaginaGraficoCategoriaVOs(new ArrayList<ConteudoUnidadePaginaGraficoCategoriaVO>());
    	for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : this.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			ConteudoUnidadePaginaGraficoCategoriaVO cloneConteudoUnidadePaginaGraficoCategoriaVO = conteudoUnidadePaginaGraficoCategoriaVO.clone();
			clone.getConteudoUnidadePaginaGraficoCategoriaVOs().add(cloneConteudoUnidadePaginaGraficoCategoriaVO);
		}
    	clone.setConteudoUnidadePaginaGraficoSerieVOs(new ArrayList<ConteudoUnidadePaginaGraficoSerieVO>());
    	for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : this.getConteudoUnidadePaginaGraficoSerieVOs()) {
			ConteudoUnidadePaginaGraficoSerieVO cloneConteudoUnidadePaginaGraficoSerieVO = conteudoUnidadePaginaGraficoSerieVO.clone();
			clone.getConteudoUnidadePaginaGraficoSerieVOs().add(cloneConteudoUnidadePaginaGraficoSerieVO);
		}
    	if(clone.getTipoRecursoEducacional().isTipoRecursoForum()){
	    	clone.getForum().setNovoObj(true);
	        clone.getForum().setCodigo(0);
    	}
        
    	if(clone.getTipoRecursoEducacional().isTipoRecursoExercicio()){    		
	    	if(clone.getListaExercicio().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)){	    	
	    		clone.setListaExercicio(clone.getListaExercicio().clone());
	    		clone.getListaExercicio().setDescricao(clone.getListaExercicio().getDescricao().replaceAll(" - Clone", ""));
	    		clone.getListaExercicio().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
	    	}else{
	    		clone.getListaExercicio().setNovoObj(true);
	            clone.getListaExercicio().setCodigo(0);
	    	}
    	}
    	if(clone.getTipoRecursoEducacional().isTipoAvaliacaoOnline()){
	        if ( clone.getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
	        	 clone.setAvaliacaoOnlineVO(clone.getAvaliacaoOnlineVO().clone());
	        	 clone.getAvaliacaoOnlineVO().setNome(clone.getAvaliacaoOnlineVO().getNome().replaceAll(" - Clone", ""));
	        	 clone.getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.EM_CONSTRUCAO);
			}else{
				clone.getAvaliacaoOnlineVO().setNovoObj(true);
		        clone.getAvaliacaoOnlineVO().setCodigo(0);	
			}
        }
    	return clone;
    }
    
    public Integer getCodigo() {
        if(codigo ==null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public TipoRecursoEducacionalEnum getTipoRecursoEducacional() {
        return tipoRecursoEducacional;
    }
    
    public void setTipoRecursoEducacional(TipoRecursoEducacionalEnum tipoRecursoEducacional) {
        this.tipoRecursoEducacional = tipoRecursoEducacional;
    }
    
    public String getTitulo() {
        if(titulo == null){
            titulo = "";
        }
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    
    public String getTexto() {
        if(texto == null){
        	
                texto = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><title></title></head><body style=\"background-color:transparent !important;background:transparent !important;\"></body><p></p></html>";
            
        }
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public Integer getAltura() {
        if(altura == null){
            altura = 600;
        }
        return altura;
    }
    
    public void setAltura(Integer altura) {
        this.altura = altura;
    }
    
    public Integer getLargura() {
        if(largura == null){
            largura = 800;
        }
        return largura;
    }
    
    public void setLargura(Integer largura) {
        this.largura = largura;
    }
    
    public String getCaminhoBaseRepositorio() {
        if(caminhoBaseRepositorio ==null){
            caminhoBaseRepositorio = "";
        }
        return caminhoBaseRepositorio;
    }
    
    public void setCaminhoBaseRepositorio(String caminhoBaseRepositorio) {
        this.caminhoBaseRepositorio = caminhoBaseRepositorio;
    }
    
    public String getNomeRealArquivo() {
        if(nomeRealArquivo ==null){
            nomeRealArquivo = "";
        }
        return nomeRealArquivo;
    }
    
    public void setNomeRealArquivo(String nomeRealArquivo) {
        this.nomeRealArquivo = nomeRealArquivo;
    }
    
    public String getNomeFisicoArquivo() {
        if(nomeFisicoArquivo ==null){
            nomeFisicoArquivo = "";
        }
        return nomeFisicoArquivo;
    }
    
    public void setNomeFisicoArquivo(String nomeFisicoArquivo) {
        this.nomeFisicoArquivo = nomeFisicoArquivo;
    }

    
    public RecursoEducacionalVO getRecursoEducacional() {
        if(recursoEducacional ==null){
            recursoEducacional = new RecursoEducacionalVO();
        }
        return recursoEducacional;
    }

    
    public void setRecursoEducacional(RecursoEducacionalVO recursoEducacional) {
        this.recursoEducacional = recursoEducacional;
    }

    
    public Boolean getManterRecursoDisponivelPagina() {
        if(manterRecursoDisponivelPagina ==null){
            manterRecursoDisponivelPagina = true;
        }
        return manterRecursoDisponivelPagina;
    }

    
    public void setManterRecursoDisponivelPagina(Boolean manterRecursoDisponivelPagina) {
        this.manterRecursoDisponivelPagina = manterRecursoDisponivelPagina;
    }

    
    public ConteudoUnidadePaginaVO getConteudoUnidadePagina() {
        if(conteudoUnidadePagina == null){
            conteudoUnidadePagina = new ConteudoUnidadePaginaVO();
        }
        return conteudoUnidadePagina;
    }

    
    public void setConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina) {
        this.conteudoUnidadePagina = conteudoUnidadePagina;
    }

    
    public Date getDataCadastro() {
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        return dataCadastro;
    }

    
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    
    public UsuarioVO getUsuarioCadastro() {
        if(usuarioCadastro == null){
            usuarioCadastro = new UsuarioVO();
        }
        return usuarioCadastro;
    }

    
    public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    
    public Date getDataAlteracao() {
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        return dataAlteracao;
    }

    
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    
    public UsuarioVO getUsuarioAlteracao() {
        if(usuarioAlteracao == null){
            usuarioAlteracao = new UsuarioVO();
        }
        return usuarioAlteracao;
    }

    
    public void setUsuarioAlteracao(UsuarioVO usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    
    public Boolean getPublicarBibliotecaRecursoEducacional() {
        if(publicarBibliotecaRecursoEducacional == null){
            publicarBibliotecaRecursoEducacional = false;
        }
        return publicarBibliotecaRecursoEducacional;
    }

    
    public void setPublicarBibliotecaRecursoEducacional(Boolean publicarBibliotecaRecursoEducacional) {
        this.publicarBibliotecaRecursoEducacional = publicarBibliotecaRecursoEducacional;
    }

    
    public Integer getOrdemApresentacao() {
        if(ordemApresentacao ==null){
            ordemApresentacao = 0;
        }
        return ordemApresentacao;
    }

    
    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    
    public MomentoApresentacaoRecursoEducacionalEnum getMomentoApresentacaoRecursoEducacional() {        
        return momentoApresentacaoRecursoEducacional;
    }

    
    public void setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacional) {
        this.momentoApresentacaoRecursoEducacional = momentoApresentacaoRecursoEducacional;
    }

    
    public String getDescricao() {
        if(descricao== null){
            descricao = "";
        }
        return descricao;
    }

    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public String getTituloGrafico() {
        if(tituloGrafico == null){
            tituloGrafico = "";
        }
        return tituloGrafico;
    }

    
    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }

    
    public String getTituloEixoX() {
        if(tituloEixoX == null){
            tituloEixoX = "";
        }
        return tituloEixoX;
    }

    
    public void setTituloEixoX(String tituloEixoX) {
        this.tituloEixoX = tituloEixoX;
    }

    
    public String getTituloEixoY() {
        if(tituloEixoY == null){
            tituloEixoY = "";
        }
        return tituloEixoY;
    }

    
    public void setTituloEixoY(String tituloEixoY) {
        this.tituloEixoY = tituloEixoY;
    }

    
    public Boolean getApresentarLegenda() {
        if(apresentarLegenda == null){
            apresentarLegenda = true;
        }
        return apresentarLegenda;
    }

    
    public void setApresentarLegenda(Boolean apresentarLegenda) {
        this.apresentarLegenda = apresentarLegenda;
    }

    
    public String getValorGrafico() {
        if(valorGrafico==null){
            valorGrafico = "";
        }
        return valorGrafico;
    }

    
    public void setValorGrafico(String valorGrafico) {
        this.valorGrafico = valorGrafico;
    }

    
    public TipoGraficoEnum getTipoGrafico() {
        if(tipoGrafico == null){
            tipoGrafico = TipoGraficoEnum.PIZZA;
        }
        return tipoGrafico;
    }

    
    public void setTipoGrafico(TipoGraficoEnum tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }
    
    public List<ConteudoUnidadePaginaGraficoPizzaVO> getConteudoUnidadePaginaGraficoVOs() {
        if(conteudoUnidadePaginaGraficoVOs == null){
            conteudoUnidadePaginaGraficoVOs = new ArrayList<ConteudoUnidadePaginaGraficoPizzaVO>(0);
        }
        return conteudoUnidadePaginaGraficoVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoVOs(List<ConteudoUnidadePaginaGraficoPizzaVO> conteudoUnidadePaginaGraficoVOs) {
        
        this.conteudoUnidadePaginaGraficoVOs = conteudoUnidadePaginaGraficoVOs;
    }

    
    public List<ConteudoUnidadePaginaGraficoCategoriaVO> getConteudoUnidadePaginaGraficoCategoriaVOs() {
        if(conteudoUnidadePaginaGraficoCategoriaVOs == null){
            conteudoUnidadePaginaGraficoCategoriaVOs = new ArrayList<ConteudoUnidadePaginaGraficoCategoriaVO>(0);
        }
        return conteudoUnidadePaginaGraficoCategoriaVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoCategoriaVOs(List<ConteudoUnidadePaginaGraficoCategoriaVO> conteudoUnidadePaginaGraficoCategoriaVOs) {
        this.conteudoUnidadePaginaGraficoCategoriaVOs = conteudoUnidadePaginaGraficoCategoriaVOs;
    }

    
    public List<ConteudoUnidadePaginaGraficoSerieVO> getConteudoUnidadePaginaGraficoSerieVOs() {
        if(conteudoUnidadePaginaGraficoSerieVOs == null){
            conteudoUnidadePaginaGraficoSerieVOs = new ArrayList<ConteudoUnidadePaginaGraficoSerieVO>(0);
        }
        return conteudoUnidadePaginaGraficoSerieVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoSerieVOs(List<ConteudoUnidadePaginaGraficoSerieVO> conteudoUnidadePaginaGraficoSerieVOs) {
        this.conteudoUnidadePaginaGraficoSerieVOs = conteudoUnidadePaginaGraficoSerieVOs;
    }

    
    public String getCategoriaGrafico() {
        if(categoriaGrafico == null){
            categoriaGrafico = "";
        }
        return categoriaGrafico;
    }

    
    public void setCategoriaGrafico(String categoriaGrafico) {
        this.categoriaGrafico = categoriaGrafico;
    }
    
    public ForumVO getForum() {
        if(forum == null){
            forum = new ForumVO();
        }
        return forum;
    }

    
    public void setForum(ForumVO forum) {
        this.forum = forum;
    }
    
    public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if(avaliacaoOnlineVO == null){
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}

	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}

    
    public ListaExercicioVO getListaExercicio() {
        if(listaExercicio == null){
            listaExercicio = new ListaExercicioVO();
        }
        return listaExercicio;
    }

    
    public void setListaExercicio(ListaExercicioVO listaExercicio) {
        this.listaExercicio = listaExercicio;
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
    
	public String getCaminhoBaseBackground() {
		if(caminhoBaseBackground == null){
			caminhoBaseBackground = "";
		}
		return caminhoBaseBackground;
	}

	public void setCaminhoBaseBackground(String caminhoBaseBackground) {
		this.caminhoBaseBackground = caminhoBaseBackground;
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
		if(origemBackgroundConteudo == null){
			origemBackgroundConteudo = OrigemBackgroundConteudoEnum.SEM_BACKGROUND;
		}
		return origemBackgroundConteudo;
	}

	public void setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum origemBackgroundConteudo) {
		this.origemBackgroundConteudo = origemBackgroundConteudo;
	}

	public Boolean getExcluirImagemBackground() {
		if(excluirImagemBackground == null){
			excluirImagemBackground = false;
		}
		return excluirImagemBackground;
	}

	public void setExcluirImagemBackground(Boolean excluirImagemBackground) {
		this.excluirImagemBackground = excluirImagemBackground;
	}
    
	public Boolean getExisteImagemBackground(){
		return !getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && (!getCaminhoBaseBackground().trim().isEmpty() || !getCorBackground().trim().isEmpty());
	}
    
	public List<NotaConceitoAvaliacaoPBLVO> getNotaConceitoAvaliacaoPBLVOs() {
		if (notaConceitoAvaliacaoPBLVOs == null) {
			notaConceitoAvaliacaoPBLVOs = new ArrayList<NotaConceitoAvaliacaoPBLVO>();
		}
		return notaConceitoAvaliacaoPBLVOs;
	}

	public void setNotaConceitoAvaliacaoPBLVOs(List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs) {
		this.notaConceitoAvaliacaoPBLVOs = notaConceitoAvaliacaoPBLVOs;
	}

	public Boolean getProfessorAvaliaAluno() {
		if (professorAvaliaAluno == null) {
			professorAvaliaAluno = false;
		}
		return professorAvaliaAluno;
	}

	public void setProfessorAvaliaAluno(Boolean professorAvaliaAluno) {
		this.professorAvaliaAluno = professorAvaliaAluno;
	}

	public String getFormulaCalculoNotaFinal() {
		if (formulaCalculoNotaFinal == null) {
			formulaCalculoNotaFinal = "QTDE_ALU";
		}
		return formulaCalculoNotaFinal;
	}

	public void setFormulaCalculoNotaFinal(String formulaCalculoNotaFinal) {
		this.formulaCalculoNotaFinal = formulaCalculoNotaFinal;
	}

	public Boolean getUtilizarNotaConceito() {
		if (utilizarNotaConceito == null) {
			utilizarNotaConceito = false;
		}
		return utilizarNotaConceito;
	}

	public void setUtilizarNotaConceito(Boolean utilizarNotaConceito) {
		this.utilizarNotaConceito = utilizarNotaConceito;
	}

	public Boolean getRequerLiberacaoProfessor() {
		if (requerLiberacaoProfessor == null) {
			requerLiberacaoProfessor = false;
		}
		return requerLiberacaoProfessor;
	}

	public void setRequerLiberacaoProfessor(Boolean requerLiberacaoProfessor) {
		this.requerLiberacaoProfessor = requerLiberacaoProfessor;
	}

	public Boolean getPermiteAlunoAvancarConteudoSemLancarNota() {
		if (permiteAlunoAvancarConteudoSemLancarNota == null) {
			permiteAlunoAvancarConteudoSemLancarNota = false;
		}
		return permiteAlunoAvancarConteudoSemLancarNota;
	}

	public void setPermiteAlunoAvancarConteudoSemLancarNota(Boolean permiteAlunoAvancarConteudoSemLancarNota) {
		this.permiteAlunoAvancarConteudoSemLancarNota = permiteAlunoAvancarConteudoSemLancarNota;
	}

	public Double getFaixaMinimaNotaAutoAvaliacao() {
		if (faixaMinimaNotaAutoAvaliacao == null) {
			faixaMinimaNotaAutoAvaliacao = 0.0;
		}
		return faixaMinimaNotaAutoAvaliacao;
	}

	public void setFaixaMinimaNotaAutoAvaliacao(Double faixaMinimaNotaAutoAvaliacao) {
		this.faixaMinimaNotaAutoAvaliacao = faixaMinimaNotaAutoAvaliacao;
	}

	public Double getFaixaMinimaNotaAlunoAvaliaAluno() {
		if (faixaMinimaNotaAlunoAvaliaAluno == null) {
			faixaMinimaNotaAlunoAvaliaAluno = 0.0;
		}
		return faixaMinimaNotaAlunoAvaliaAluno;
	}

	public void setFaixaMinimaNotaAlunoAvaliaAluno(Double faixaMinimaNotaAlunoAvaliaAluno) {
		this.faixaMinimaNotaAlunoAvaliaAluno = faixaMinimaNotaAlunoAvaliaAluno;
	}


	public Double getFaixaMinimaNotaProfessorAvaliaAluno() {
		if (faixaMinimaNotaProfessorAvaliaAluno == null) {
			faixaMinimaNotaProfessorAvaliaAluno = 0.0;
		}
		return faixaMinimaNotaProfessorAvaliaAluno;
	}

	public void setFaixaMinimaNotaProfessorAvaliaAluno(Double faixaMinimaNotaProfessorAvaliaAluno) {
		this.faixaMinimaNotaProfessorAvaliaAluno = faixaMinimaNotaProfessorAvaliaAluno;
	}

	public Double getFaixaMaximaNotaProfessorAvaliaAluno() {
		if (faixaMaximaNotaProfessorAvaliaAluno == null) {
			faixaMaximaNotaProfessorAvaliaAluno = 0.0;
		}
		return faixaMaximaNotaProfessorAvaliaAluno;
	}

	public void setFaixaMaximaNotaProfessorAvaliaAluno(Double faixaMaximaNotaProfessorAvaliaAluno) {
		this.faixaMaximaNotaProfessorAvaliaAluno = faixaMaximaNotaProfessorAvaliaAluno;
	}

	public Boolean getAutoAvaliacao() {
		if (autoAvaliacao == null) {
			autoAvaliacao = false;
		}
		return autoAvaliacao;
	}

	public void setAutoAvaliacao(Boolean autoAvaliacao) {
		this.autoAvaliacao = autoAvaliacao;
	}

	public Boolean getAlunoAvaliaAluno() {
		if (alunoAvaliaAluno == null) {
			alunoAvaliaAluno = false;
		}
		return alunoAvaliaAluno;
	}

	public void setAlunoAvaliaAluno(Boolean alunoAvaliaAluno) {
		this.alunoAvaliaAluno = alunoAvaliaAluno;
	}

	public Double getFaixaMaximaNotaAutoAvaliacao() {
		if (faixaMaximaNotaAutoAvaliacao == null) {
			faixaMaximaNotaAutoAvaliacao = 0.0;
		}
		return faixaMaximaNotaAutoAvaliacao;
	}

	public void setFaixaMaximaNotaAutoAvaliacao(Double faixaMaximaNotaAutoAvaliacao) {
		this.faixaMaximaNotaAutoAvaliacao = faixaMaximaNotaAutoAvaliacao;
	}

	public Double getFaixaMaximaNotaAlunoAvaliaAluno() {
		if (faixaMaximaNotaAlunoAvaliaAluno == null) {
			faixaMaximaNotaAlunoAvaliaAluno = 0.0;
		}
		return faixaMaximaNotaAlunoAvaliaAluno;
	}

	public void setFaixaMaximaNotaAlunoAvaliaAluno(Double faixaMaximaNotaAlunoAvaliaAluno) {
		this.faixaMaximaNotaAlunoAvaliaAluno = faixaMaximaNotaAlunoAvaliaAluno;
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
	
	public SituacaoPBLEnum getSituacaoGestaoEventoConteudoTurma() {
		if(situacaoGestaoEventoConteudoTurma == null){
			situacaoGestaoEventoConteudoTurma = SituacaoPBLEnum.PENDENTE;
		}
		return situacaoGestaoEventoConteudoTurma;
	}

	public void setSituacaoGestaoEventoConteudoTurma(SituacaoPBLEnum situacaoGestaoEventoConteudoTurma) {
		this.situacaoGestaoEventoConteudoTurma = situacaoGestaoEventoConteudoTurma;
	}	
	
	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getGestaoEventoConteudoTurmaAvaliacaoPBLVO() {
		if(gestaoEventoConteudoTurmaAvaliacaoPBLVO == null){
			gestaoEventoConteudoTurmaAvaliacaoPBLVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLVO(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLVO = gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}
	
	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = new ArrayList<SelectItem>();
			try {
				listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = UtilSelectItem.getListaSelectItem(getNotaConceitoAvaliacaoPBLVOs().stream().
						filter(t -> t.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO)).collect(Collectors.toList()), "codigo", "conceito");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}
	
	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = new ArrayList<SelectItem>();
			try {
				listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = UtilSelectItem.getListaSelectItem(getNotaConceitoAvaliacaoPBLVOs().stream().
						filter(t -> t.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO)).collect(Collectors.toList()), "codigo", "conceito");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}
	
	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = new ArrayList<SelectItem>();
			try {
				listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = UtilSelectItem.getListaSelectItem(getNotaConceitoAvaliacaoPBLVOs().stream().
						filter(t -> t.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO)).collect(Collectors.toList()), "codigo", "conceito");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(
			List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public String getCssCampoLancamentoNota(){
		return getPermiteAlunoAvancarConteudoSemLancarNota() ? "campos" : "camposObrigatorios";
	}
	
	public boolean equalsCampoSelecaoLista(ConteudoUnidadePaginaRecursoEducacionalVO obj){
		return ((!Uteis.isAtributoPreenchido(getTipoRecursoEducacional()) && !Uteis.isAtributoPreenchido(obj.getTipoRecursoEducacional())) || (getTipoRecursoEducacional().equals(obj.getTipoRecursoEducacional())))
			   && ((!Uteis.isAtributoPreenchido(getMomentoApresentacaoRecursoEducacional()) && !Uteis.isAtributoPreenchido(obj.getMomentoApresentacaoRecursoEducacional()))	|| (getMomentoApresentacaoRecursoEducacional().equals(obj.getMomentoApresentacaoRecursoEducacional())))			   
			   && (getOrdemApresentacao().equals(obj.getOrdemApresentacao()))
			   && (getManterRecursoDisponivelPagina().equals(obj.getManterRecursoDisponivelPagina()))
			   && (getNomeRealArquivo().equals(obj.getNomeRealArquivo()))
			   && (getNomeFisicoArquivo().equals(obj.getNomeFisicoArquivo()))
			   && (getTexto().equals(obj.getTexto()))
			   && (getTitulo().equals(obj.getTitulo()))
			   && (getDescricao().equals(obj.getDescricao()))
			   && (getAltura().equals(obj.getAltura()))
			   && (getLargura().equals(obj.getLargura()))
			   && (getTituloGrafico().equals(obj.getTituloGrafico()))
			   && (getTituloEixoX().equals(obj.getTituloEixoX()))
			   && (getTituloEixoY().equals(obj.getTituloEixoY()))
			   && (getCategoriaGrafico().equals(obj.getCategoriaGrafico()))
			   && (getApresentarLegenda().equals(obj.getApresentarLegenda()))
			   && (getAutoAvaliacao().equals(obj.getAutoAvaliacao()))
			   && (getAlunoAvaliaAluno().equals(obj.getAlunoAvaliaAluno()))
			   && (getProfessorAvaliaAluno().equals(obj.getProfessorAvaliaAluno()))
			   && (getFormulaCalculoNotaFinal().equals(obj.getFormulaCalculoNotaFinal()))
			   && (getUtilizarNotaConceito().equals(obj.getUtilizarNotaConceito()))
			   && (getRequerLiberacaoProfessor().equals(obj.getRequerLiberacaoProfessor()))
			   && (getPermiteAlunoAvancarConteudoSemLancarNota().equals(obj.getPermiteAlunoAvancarConteudoSemLancarNota()))
			   && (getFaixaMaximaNotaAlunoAvaliaAluno().equals(obj.getFaixaMaximaNotaAlunoAvaliaAluno()))
			   && (getFaixaMaximaNotaAutoAvaliacao().equals(obj.getFaixaMaximaNotaAutoAvaliacao()))
			   && (getFaixaMaximaNotaProfessorAvaliaAluno().equals(obj.getFaixaMaximaNotaProfessorAvaliaAluno()))
			   && (getFaixaMinimaNotaAlunoAvaliaAluno().equals(obj.getFaixaMinimaNotaAlunoAvaliaAluno()))
			   && (getFaixaMinimaNotaAutoAvaliacao().equals(obj.getFaixaMinimaNotaAutoAvaliacao()))
			   && (getFaixaMinimaNotaProfessorAvaliaAluno().equals(obj.getFaixaMinimaNotaProfessorAvaliaAluno()))
			   && (getRecursoEducacional().getCodigo().equals(obj.getRecursoEducacional().getCodigo()))
			   && (getForum().getCodigo().equals(obj.getForum().getCodigo()))
			   && (getListaExercicio().getCodigo().equals(obj.getListaExercicio().getCodigo()))
			   && (getAvaliacaoOnlineVO().getCodigo().equals(obj.getAvaliacaoOnlineVO().getCodigo()))
			   ;
	}
	
	private String icone;
	
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getIcone() {
		if(icone == null) {		
			icone = ArquivoHelper.getIcone(getNomeFisicoArquivo());		
		}
		return icone;
	}
	

	private List<String> listaImagensSlide;
	private String conteudoPaginaApresentar;
	
	public String getConteudoPaginaApresentar(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Integer heightDiminuir) {
		if(conteudoPaginaApresentar == null || conteudoPaginaApresentar.trim().isEmpty()) {
			StringBuilder texto = new StringBuilder("");		
			
			if (!getCaminhoBaseRepositorio().isEmpty() && getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FLASH)) {
				texto.append("<object width=\"100%\" style=\"z-index:0;height:calc(100vh - ").append(heightDiminuir).append("px)\" class=\"w-100\" >"); 
				texto.append("<param name=\"movie\" value=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("\">");
				texto.append("<param name=\"wmode\" value=\"transparent\">");
				texto.append("<embed WMODE=\"transparent\" style=\"z-index:0;position:relative;height:calc(100vh - ").append(heightDiminuir).append("px)\" width=\"100%\" type=\"application/x-shockwave-flash\"  class=\"col-md-12\" src=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("\"  pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />");
				texto.append("</object>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.PAGINA_HTML) && !getTexto().isEmpty()) {
				texto.append("<iframe wmode=\"transparent\" style=\"z-index:1;height:calc(100vh - ").append(heightDiminuir).append("px)\"  class=\"w-100\" width=\"100%\" src=\"").append(getTexto()).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM) && !getCaminhoBaseRepositorio().isEmpty()) {
				texto.append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("?UID=").append(Calendar.getInstance().getTime());
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL) && !getTexto().isEmpty()) {
				String urlvideo = getTexto();
				if (urlvideo.endsWith(".pdf") || urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {					
					texto.append(urlvideo);
				} else if (urlvideo.contains("youtube") && urlvideo.indexOf("v=") > 0) {
					if (urlvideo.contains("&")) {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2, urlvideo.indexOf("&"));
					} else {
						urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2);
					}
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn \" style=\"z-index:1;height:calc(100vh - ").append(heightDiminuir).append("px)\"   width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (urlvideo.contains("youtu.be")) {
					urlvideo = urlvideo.substring(urlvideo.lastIndexOf("/") + 1);
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn \" style=\"z-index:1;height:calc(100vh - ").append(heightDiminuir).append("px)\"  width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else if (!urlvideo.contains("http") && !urlvideo.contains("youtu") && !urlvideo.contains("www")) {
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn \" style=\"z-index:1;height:calc(100vh - ").append(heightDiminuir).append("px)\"  width=\"100%\" src=\"http://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				} else {
					texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn \" style=\"z-index:1;height:calc(100vh - ").append(heightDiminuir).append("px)\";  width=\"100%\" src=\"").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" allowfullscreen></iframe>");
				}
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AUDIO) && !getCaminhoBaseRepositorio().isEmpty()) {				
				texto.append("<audio style=\"height: calc(50%);\" controls><source  src=\"").append(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()).append("/").append(getCaminhoBaseRepositorio().replaceAll("\\\\", "/")).append("/").append(getNomeFisicoArquivo()).append("?UID=").append(new Date().getTime()).append("\" type=\"audio/mpeg\" >Formatado de audio não reproduzido por este navegador</audio></object>");
			}else if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && !getCaminhoBaseRepositorio().isEmpty()) {
				for (String arquivo : getListaImagensSlide(configuracaoGeralSistemaVO)) {									
					texto.append("<img src=\"").append(arquivo).append("\" style=\"height:calc(100vh - ").append(heightDiminuir).append("px);\" />");					
				}				
			}
			conteudoPaginaApresentar = texto.toString();
		}
		return conteudoPaginaApresentar;
	}

	public void setConteudoPaginaApresentar(String conteudoPaginaApresentar) {
		this.conteudoPaginaApresentar = conteudoPaginaApresentar;
	}

	public List<String> getListaImagensSlide(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		if(listaImagensSlide == null) {
			listaImagensSlide =  new ArrayList<String>(0);
			if(getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && !getCaminhoBaseRepositorio().toString().isEmpty()) {
				String[] nomeArquivos;
				String[] caminhosArquivos;
				if (!getNomeFisicoArquivo().contains(",")) {
					nomeArquivos = new String[1];
					caminhosArquivos = new String[1];
					nomeArquivos[0] = getNomeFisicoArquivo();
					caminhosArquivos[0] = getCaminhoBaseRepositorio();
				} else {
					nomeArquivos = getNomeFisicoArquivo().split(",");
					caminhosArquivos = getCaminhoBaseRepositorio().split(",");
				}				
				String url = "";
				int x = 0;
				for (String nomeArquivo : nomeArquivos) {
					url = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + caminhosArquivos[x++].replaceAll("\\\\", "/").trim() + "/" + nomeArquivo.trim();	
					listaImagensSlide.add(url);
				}				
			}
		}
		return listaImagensSlide;
	}

	public void setListaImagensSlide(List<String> listaImagensSlide) {
		this.listaImagensSlide = listaImagensSlide;
	}
    
}
