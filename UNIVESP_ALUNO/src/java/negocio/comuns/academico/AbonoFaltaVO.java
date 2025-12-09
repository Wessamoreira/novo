package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Turma. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AbonoFaltaVO extends SuperVO {

    protected Integer codigo;
    protected MatriculaVO matricula;
    protected PessoaVO pessoa;
    protected Date dataInicio;
    protected Date dataFim;
    private String observacao;
    private String justificativa;
    private String tipoAbono;
    private UsuarioVO responsavel;	
//    protected List<DisciplinaAbonoVO> disciplinaAbonoVOs;
    private Boolean abonarFaltaFuturosRegistrosAula;
    private TipoJustificativaFaltaVO tipoJustificativaFaltaVO;
    private String situacaoAula;	
    public static final long serialVersionUID = 1L;


    /**
     * Construtor padrão da classe <code>Turma</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public AbonoFaltaVO() {
        super();
    }

    public static void verificarQtdeFaltasJaAbonada(AbonoFaltaVO obj, List<AbonoFaltaVO> listaAbono) throws ConsistirException {
//        if (obj.isNovoObj()) {
//            List<DisciplinaAbonoVO> listaDiscAbonoAtual = obj.getDisciplinaAbonoVOs();
//            if (listaAbono != null && !listaAbono.isEmpty()) {
//                for (AbonoFaltaVO abonos : listaAbono) {
//                    for (DisciplinaAbonoVO discExistente : abonos.getDisciplinaAbonoVOs()) {
//                        for (DisciplinaAbonoVO discAtual : listaDiscAbonoAtual) {
//                            if ((discExistente.getDisciplina().getCodigo().intValue() == discAtual.getDisciplina().getCodigo().intValue())
//                                    && (discExistente.getFaltaAbonada().booleanValue() && discAtual.getFaltaAbonada().booleanValue())
//                                    && (discExistente.getRegistroAula().getCodigo().intValue() == discAtual.getRegistroAula().getCodigo().intValue())) {
//                                throw new ConsistirException("A aula da disciplina "
//                                        + discAtual.getDisciplina().getNome() + " do dia "
//                                        + discAtual.getRegistroAula().getDataRegistroAula_Apresentar()
//                                        + " já foi abonada.");
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
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
     * @return the matricula
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    /**
     * @param matricula
     *            the matricula to set
     */
    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the pessoa
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    /**
     * @param pessoa
     *            the pessoa to set
     */
    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @return the datainicio
     */
    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    public String getDataInicio_Apresentar() {
        if (dataInicio == null) {
            return "";
        }
        return (Uteis.getData(dataInicio));
    }

    /**
     * @param datainicio
     *            the datainicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the datafim
     */
    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public String getDataFim_Apresentar() {
        if (dataFim == null) {
            return "";
        }
        return (Uteis.getData(dataFim));
    }

    /**
     * @param datafim
     *            the datafim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @return the disciplinaAbonoVOs
     */
   
    
    public String getTipoAbono_Apresentar() {
        if (getTipoAbono().equals("AB")) {
            return "Abono";
        }
        if (getTipoAbono().equals("JU")) {
            return "Justificativa";
        }
        if (getTipoAbono().equals("TT")) {
        	return "Transferência de Turma";
        }
        return "";
    }

    public String getTipoAbono() {
        if (tipoAbono == null) {
            tipoAbono = "";
        }
        return tipoAbono;
    }

    public void setTipoAbono(String tipoAbono) {
        this.tipoAbono = tipoAbono;
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
    
    public String getJustificativa_Apresentar() {
        if (getJustificativa().equals("DO")) {
            return "Licença Médica - Doença";
        }
        if (getJustificativa().equals("GR")) {
            return "Licença Médica - Gravidez";
        }
        if (getJustificativa().equals("OU")) {
            return "Outros";
        }
        return "";
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    /**
     * @return the abonarFaltaFuturosRegistrosAula
     */
    public Boolean getAbonarFaltaFuturosRegistrosAula() {
        if (abonarFaltaFuturosRegistrosAula == null) {
            abonarFaltaFuturosRegistrosAula = Boolean.FALSE;
        }
        return abonarFaltaFuturosRegistrosAula;
    }

    /**
     * @param abonarFaltaFuturosRegistrosAula the abonarFaltaFuturosRegistrosAula to set
     */
    public void setAbonarFaltaFuturosRegistrosAula(Boolean abonarFaltaFuturosRegistrosAula) {
        this.abonarFaltaFuturosRegistrosAula = abonarFaltaFuturosRegistrosAula;
    }
    
    public String getDescricaoAbonoFaltaSemObservacao() {
        String just = getJustificativa_Apresentar();
        if (!just.equals("")) {
            just = "(" + just + ")";
        }
        String desc = "";
        if (this.getTipoAbono().equals("JU")) {
            desc = this.getTipoAbono_Apresentar() + " Período: " +
                      this.getDataInicio_Apresentar() + " - "+ this.getDataFim_Apresentar();
        } else {
            desc = this.getTipoAbono_Apresentar() + " " + just + " Período: " +
                      this.getDataInicio_Apresentar() + " - "+ this.getDataFim_Apresentar();
        }
        return desc;        
    }
    
    public String getDescricaoAbonoFalta() {
        String just = getJustificativa_Apresentar();
        if (!just.equals("")) {
            just = "(" + just + ")";
        }
        String obs = getObservacao();
        if (!obs.equals("")) {
            obs = "(" + obs + ")";
        }
        String desc = "";
        if (this.getTipoAbono().equals("JU")) {
            desc = "Justificado " + obs + " Período: " +
                      this.getDataInicio_Apresentar() + " - "+ this.getDataFim_Apresentar();
        } else {
            desc = "Abonado " + just + " Período: " +
                      this.getDataInicio_Apresentar() + " - "+ this.getDataFim_Apresentar();
        }
        return desc;
    }
	
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public TipoJustificativaFaltaVO getTipoJustificativaFaltaVO() {
		if(tipoJustificativaFaltaVO == null){
			tipoJustificativaFaltaVO = new TipoJustificativaFaltaVO();
		}
		return tipoJustificativaFaltaVO;
	}

	public void setTipoJustificativaFaltaVO(TipoJustificativaFaltaVO tipoJustificativaFaltaVO) {
		this.tipoJustificativaFaltaVO = tipoJustificativaFaltaVO;
	}
	
	public String getSituacaoAula() {
		if (situacaoAula == null) {
			situacaoAula = "";
		}
		return situacaoAula;
	}

	public void setSituacaoAula(String situacaoAula) {
		this.situacaoAula = situacaoAula;
	}

}
