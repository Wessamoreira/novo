package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class LogGradeCurricularVO extends SuperVO {
	
	private Integer codigo;
    private UsuarioVO usuarioResponsavel;
    private Date data;
    private TipoAlteracaoMatrizCurricularEnum operacao;
    private GradeCurricularVO gradeCurricularVO;
    private PeriodoLetivoVO periodoLetivoVO;
    private GradeDisciplinaVO gradeDisciplinaVO;
    private String logAlteracao;
    
    private List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs;
    
    /**
     * Na tabela de logs deve mostrar frequencia, notas, etc somente em algumas situações
     */
    public boolean validarApresentacaoLog() {
    	if(operacao.equals(TipoAlteracaoMatrizCurricularEnum.EXCLUIR_GRUPO_OPTATIVA_DISCIPLINA) || operacao.equals(TipoAlteracaoMatrizCurricularEnum.EXCLUIR_GRADE_DISCIPLINA) || operacao.equals(TipoAlteracaoMatrizCurricularEnum.EXCLUIR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA) 
    			|| operacao.equals(TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA) || operacao.equals(TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA_COMPOSTA) || operacao.equals(TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA)
    			|| operacao.equals(TipoAlteracaoMatrizCurricularEnum.ADICIONAR_PRE_REQUISITO_GRADE_DISCIPLINA) || operacao.equals(TipoAlteracaoMatrizCurricularEnum.DEFINIR_PRE_REQUISITO_GRUPO_DISCIPLINA_OPTATIVA)
    			|| operacao.equals(TipoAlteracaoMatrizCurricularEnum.DEFINIR_PRE_REQUISITO_GRUPO_DISCIPLINA_OPTATIVA)
    	) {
    		return true;
    	}
    	
    	return false;
    }
//    public void setImpactos(JSONArray impactos) {
//    	this.impactos = impactos;
//    }
//    
//    public JSONArray getImpactos() {
//    	if(this.impactos == null) {
//    		return new JSONArray();
//    	}
//    	return this.impactos;
//    }
    
    public String getDataApresentar() {
    	return Uteis.getDataComHoraCompleta(data);
    }
    
    public String getUsuarioApresentar() {
    	return usuarioResponsavel.getCodigo() + " - " + usuarioResponsavel.getNome();
    }
    
    public static final long serialVersionUID = 1L;
       
    public LogGradeCurricularVO() {
        super();
    }
    
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}
	
	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}
	
	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public TipoAlteracaoMatrizCurricularEnum getOperacao() {
		if (operacao == null) {
			operacao = TipoAlteracaoMatrizCurricularEnum.NENHUM;
		}
		return operacao;
	}
	
	public void setOperacao(TipoAlteracaoMatrizCurricularEnum operacao) {
		this.operacao = operacao;
	}
	
	

	public String getLogAlteracao() {
		if (this.logAlteracao == null) {
			logAlteracao = "";
		}
		return logAlteracao;
	}

	public void setLogAlteracao(String logAlteracao) {
		this.logAlteracao = logAlteracao;
	}
	
	/**
	 * Usado quando existe vinculo da disciplina que está sendo alterado em historico. Responsavel por registro todos alunos que cursam a disciplina
	 * @param logAlteracao
	 * @param historicoVOs
	 * @param usuarioVO
	 * @param tipoAlteracaoMatrizCurricularEnum
	 * @param codGradeCurricular
	 */
    public void inicializarDadosLogGradeCurricular(String logAlteracao, List<LogImpactoMatrizCurricularVO> listaLogImpactoMatrizCurricularVOs, UsuarioVO usuarioVO, 
    		TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricularEnum, Integer codGradeCurricular, Integer periodoLetivo, Integer gradeDisciplina) {
    	
    	this.setListaLogImpactoGradeDisciplinaVOs(listaLogImpactoMatrizCurricularVOs);	
    	this.data = new Date();
    	this.usuarioResponsavel = usuarioVO;
    	this.operacao = tipoAlteracaoMatrizCurricularEnum;
    	this.getGradeCurricularVO().setCodigo(codGradeCurricular);
    	this.getPeriodoLetivoVO().setCodigo(periodoLetivo != null ? periodoLetivo : 0);
    	this.getGradeDisciplinaVO().setCodigo(gradeDisciplina != null ? gradeDisciplina : 0);
    	this.logAlteracao = logAlteracao;
    }
    
    
    /**
     * Usado nos casos em que a alteração não os afeta historicos
     * @param logAlteracao
     * @param matriculaVOs
     * @param usuarioVO
     * @param tipoAlteracaoMatrizCurricularEnum
     * @param codGradeCurricular
     */
    public void montarDadosPorMatricula(String logAlteracao, UsuarioVO usuarioVO, 
    		TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricularEnum, Integer codGradeCurricular, Integer codigoPeriodoLetivo, Integer codigoGradeDisciplina) {
    	
//    	this.impactos = matriculaToJSON(matriculaVOs);	
    	this.data = new Date();
    	this.usuarioResponsavel = usuarioVO;
    	this.operacao = tipoAlteracaoMatrizCurricularEnum;
    	this.getGradeCurricularVO().setCodigo(codGradeCurricular);
    	this.getPeriodoLetivoVO().setCodigo(codigoPeriodoLetivo != null ? codigoPeriodoLetivo : 0);
    	this.getGradeDisciplinaVO().setCodigo(codigoGradeDisciplina != null ? codigoGradeDisciplina : 0);
    	this.logAlteracao = logAlteracao;
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
	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}
	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}
	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}
	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}
	public List<LogImpactoMatrizCurricularVO> getListaLogImpactoGradeDisciplinaVOs() {
		if (listaLogImpactoGradeDisciplinaVOs == null) {
			listaLogImpactoGradeDisciplinaVOs = new ArrayList<LogImpactoMatrizCurricularVO>(0);
		}
		return listaLogImpactoGradeDisciplinaVOs;
	}
	public void setListaLogImpactoGradeDisciplinaVOs(List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs) {
		this.listaLogImpactoGradeDisciplinaVOs = listaLogImpactoGradeDisciplinaVOs;
	}
	
}
