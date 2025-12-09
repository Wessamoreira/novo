package negocio.comuns.administrativo;



import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.utilitarias.Uteis;



public class PainelGestorMonitoramentoDetalheCRMVO {

    private Integer quantidadeDia;
    private Integer quantidadeSemana;
    private Integer quantidadeMes;
    private Integer quantidadeMatriculaMes;
    private Integer quantidadeFinalizadoSucessoMes;
    private MesAnoEnum mesAnoEnum;
    private Integer ano;
    private CursoVO curso;
    private FuncionarioVO consultor;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum;
    private Date dataInicio;
    private Date dataTermino;
    private Integer tipoMidiaCaptacao;
    private Integer quantidadeReagendamentoMes;
    
    public Integer getQuantidadeDia() {        
        return quantidadeDia;
    }
    
    public void setQuantidadeDia(Integer quantidadeDia) {
        this.quantidadeDia = quantidadeDia;
    }
    
    public Integer getQuantidadeSemana() {
        return quantidadeSemana;
    }
    
    public void setQuantidadeSemana(Integer quantidadeSemana) {
        this.quantidadeSemana = quantidadeSemana;
    }
    
    public Integer getQuantidadeMes() {
        return quantidadeMes;
    }
    
    public void setQuantidadeMes(Integer quantidadeMes) {
        this.quantidadeMes = quantidadeMes;
    }
    
    public MesAnoEnum getMesAnoEnum() {
        return mesAnoEnum;
    }
    
    public void setMesAnoEnum(MesAnoEnum mesAnoEnum) {
        this.mesAnoEnum = mesAnoEnum;
    }
    
    public Integer getAno() {
        return ano;
    }
    
    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    
    public CursoVO getCurso() {
        if(curso == null){
            curso = new CursoVO();
        }
        return curso;
    }
    
    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }
    
    public FuncionarioVO getConsultor() {
        if(consultor == null){
            consultor = new FuncionarioVO();
        }
        return consultor;
    }
    
    public void setConsultor(FuncionarioVO consultor) {
        this.consultor = consultor;
    }
    
    public PainelGestorTipoMonitoramentoCRMEnum getPainelGestorTipoMonitoramentoCRMEnum() {
        return painelGestorTipoMonitoramentoCRMEnum;
    }
    
    public void setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum) {
        this.painelGestorTipoMonitoramentoCRMEnum = painelGestorTipoMonitoramentoCRMEnum;
    }
    
    
    
    
    public Date getDataInicio() {
        if(dataInicio == null){
            try{
                dataInicio = Uteis.gerarDataInicioMes(Integer.parseInt(getMesAnoEnum().getKey()), getAno());
            }catch (Exception e) {
                dataInicio = new Date();
            }
        }
        return dataInicio;
    }

    
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    
    public Date getDataTermino() {
        if(dataTermino == null){
            try{
                dataTermino = Uteis.gerarDataFimMes(Integer.parseInt(getMesAnoEnum().getKey()), getAno());
            }catch (Exception e) {
                dataTermino = new Date();
            }
        }
        return dataTermino;
    }

    
    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Integer getQuantidadeMatriculaMes() {
		if(quantidadeMatriculaMes == null){
			quantidadeMatriculaMes = 0;
		}
		return quantidadeMatriculaMes;
	}

	public void setQuantidadeMatriculaMes(Integer quantidadeMatriculaMes) {
		this.quantidadeMatriculaMes = quantidadeMatriculaMes;
	}

	public Integer getQuantidadeFinalizadoSucessoMes() {
		if(quantidadeFinalizadoSucessoMes == null){
			quantidadeFinalizadoSucessoMes = 0;
		}
		return quantidadeFinalizadoSucessoMes;
	}

	public void setQuantidadeFinalizadoSucessoMes(Integer quantidadeFinalizadoSucessoMes) {
		this.quantidadeFinalizadoSucessoMes = quantidadeFinalizadoSucessoMes;
	}

	public Integer getTipoMidiaCaptacao() {
		if(tipoMidiaCaptacao == null){
			tipoMidiaCaptacao = 0;
		}
		return tipoMidiaCaptacao;
	}

	public void setTipoMidiaCaptacao(Integer tipoMidiaCaptacao) {
		this.tipoMidiaCaptacao = tipoMidiaCaptacao;
	}

	public Integer getQuantidadeReagendamentoMes() {
		if (quantidadeReagendamentoMes == null) {
			quantidadeReagendamentoMes = 0;
		}
		return quantidadeReagendamentoMes;
	}

	public void setQuantidadeReagendamentoMes(Integer quantidadeReagendamentoMes) {
		this.quantidadeReagendamentoMes = quantidadeReagendamentoMes;
	}    
    
    
}
