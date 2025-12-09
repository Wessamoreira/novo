package negocio.comuns.crm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade CompromissoAgendaPessoaHorario. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class CompromissoAgendaPessoaHorarioVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private AgendaPessoaHorarioVO agendaPessoaHorario;
    private String hora;
    private String horaFim;
    private TipoCompromissoEnum tipoCompromisso;
    private CursoVO cursoInteresseProspect;
    private String observacao;
    private String origem;
    private Boolean urgente;
    private Boolean compromissoSelecionado;
//    private Boolean compromissoRealizado;
//    private Boolean compromissoParalizado;
    private TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum;
    private EtapaWorkflowVO etapaWorkflowVO;
    private Date dataCadastro;
    private Date dataCompromisso;
    /**
	* @transient
     * utilizado para saber se o usuario de fato alterou a dataCompromisso. Quando se 
     * altera a dataCompromisso temos que saber se o compromisso foi reagendado ou nao.
     */
    private Date dataCompromissoAnterior;
    /** Grava o histórico de todos os reagendamos realizados no compromisso, haja vista,
     * que esta é uma importante ferramenta para o usuário e um compromisso pode ser 
     * reagendado várias vezes.
     */
    private String historicoReagendamentoCompromisso;
    /**
     * Registra a data inicial do compromisso. De quando o mesmo foi criado.
     * Utilizado para sabermos quando um compromisso já foi reagendado ou não.
     */
	private Date dataInicialCompromisso;
    
    private TipoContatoEnum tipoContato;
    private Boolean realizado;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Prospects </code>.*/
    protected ProspectsVO prospect;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Campanha </code>.*/
    protected CampanhaVO campanha;
    /**
     * Atributo responsável por manter a pré-inscrição que gerou o compromisso
     * é importante este elo, pois podemos ter mais de uma pré para a mesma pessoa
     * Sendo necessário apresentar para o consultor que irá ligar para prospects,
     * possíveis divergencias entre dados de contato informados na pré e registrado 
     * anteriormente para o prospect. Assim o consultor pode confirmar os dados
     * corretos e finais e atualizar o cadastro do mesmo como aluno.
     */
    private PreInscricaoVO preInscricao;
    
    /**
     * Atributos Transientes a serem apresentado na agenda do consultor sinalizando se o prospect possui matrícula
     */
    private Boolean possuiMatricula;
    private List<MatriculaVO> matriculaVOs;
    public String ultimaInteracao;
    private String duvida;
    private Boolean reagendado;
    private Boolean cancelado;

    /**
     * Construtor padrão da classe <code>CompromissoAgendaPessoaHorario</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public CompromissoAgendaPessoaHorarioVO() {
        super();
    }
    
    public CompromissoAgendaPessoaHorarioVO clone() throws CloneNotSupportedException{
    	CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) super.clone();
        obj.setCodigo(0);
        obj.setNovoObj(true);
        obj.getMatriculaVOs().clear();
        for(MatriculaVO mat : this.getMatriculaVOs()){
        	MatriculaVO clone = (MatriculaVO) mat.clone();            
            obj.getMatriculaVOs().add(clone);
        }
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public static void validarDados(CompromissoAgendaPessoaHorarioVO obj) throws ConsistirException, Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }


        if (obj.getDescricao().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_descricao"));
        }
        if (obj.getIsTipoCompromissoContato()) {
            if ((obj.getProspect() == null || (obj.getProspect().getCodigo().intValue() == 0))) {
                throw new Exception(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_prospect"));
            }
        }

        if (obj.getHora().isEmpty()) {
            throw new Exception(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_hora"));
        }

        obj.setHora(Uteis.getFormatoHoraMinuto(obj.getHora()));
        if (Integer.parseInt(obj.getHora().substring(0, 2)) >= 24) {
        	throw new ConsistirException("(Hora) informada invalida, por favor informe um horario valido tanto for informar a (Hora) e mais (Hora Fim)");
        }
        
        if (Integer.parseInt(obj.getHora().substring(3, 5)) >= 60) {
        	throw new ConsistirException("Minuto (Hora) informada invalida");
        }
        
//        obj.setHoraFim(Uteis.getFormatoHoraMinuto(obj.getHoraFim()));
//        if (Integer.parseInt(obj.getHoraFim().substring(0, 2)) >= 24) {
//        	throw new ConsistirException("(Hora Fim) informada invalida");
//        }
//        
//        if (Integer.parseInt(obj.getHoraFim().substring(3, 5)) >= 60) {
//        	throw new ConsistirException("Minuto (Hora Fim) informada invalida");
//        }

        if (obj.getTipoContato() == null || obj.getTipoContato().equals(TipoContatoEnum.NENHUM)) {
            throw new Exception(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_tipoContato"));
        }

        if ((Integer.parseInt(obj.getHora().substring(0, 2)) == Integer.parseInt(Uteis.getHoraAtual().substring(0, 2))
                && Integer.parseInt(obj.getHora().substring(3, 5)) < Integer.parseInt(Uteis.getHoraAtual().substring(3, 5))
                && obj.getAgendaPessoaHorario().getDia() == Uteis.getDiaMesData(new Date())
                && obj.getAgendaPessoaHorario().getMes() == Uteis.getMesDataAtual()
                && obj.getAgendaPessoaHorario().getAno() == Uteis.getAnoData(new Date()))
                || Integer.parseInt(obj.getHora().substring(0, 2)) < Integer.parseInt(Uteis.getHoraAtual().substring(0, 2))
                && obj.getAgendaPessoaHorario().getDia() == Uteis.getDiaMesData(new Date())
                && obj.getAgendaPessoaHorario().getMes() == Uteis.getMesDataAtual()
                && obj.getAgendaPessoaHorario().getAno() == Uteis.getAnoData(new Date())
                || (obj.getAgendaPessoaHorario().getDia() < Uteis.getDiaMesData(new Date())
                && obj.getAgendaPessoaHorario().getMes() == Uteis.getMesDataAtual()
                && obj.getAgendaPessoaHorario().getAno() == Uteis.getAnoData(new Date()))
                || (obj.getAgendaPessoaHorario().getMes() < Uteis.getMesDataAtual()
                && obj.getAgendaPessoaHorario().getAno() == Uteis.getAnoData(new Date()))
                //|| (obj.getAgendaPessoaHorario().getAno() < Uteis.getAnoData(new Date()))
                ) {
            throw new Exception(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_contatoPassado"));
        }

        for (CompromissoAgendaPessoaHorarioVO compromissoExistente : obj.getAgendaPessoaHorario().getListaCompromissoAgendaPessoaHorarioVOs()) {
            if ((obj.getNovoObj() && compromissoExistente.getHora().equals(obj.getHora()) && compromissoExistente.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo()))
                    || (compromissoExistente.getHora().equals(obj.getHora()) && !compromissoExistente.getCodigo().equals(obj.getCodigo()) && compromissoExistente.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo()))) {
                throw new Exception("Já existe um compromisso agendado para essa hora, nesse dia, para esse funcionário.");
            }
        }
        
        if (obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo() == 0) {
            throw new Exception(UteisJSF.internacionalizar("O campo Responsável (Compromisso) deve ser informado."));
        }

    }

    /**
     * Retorna o objeto da classe <code>Campanha</code> relacionado com (<code>CompromissoAgendaPessoaHorario</code>).
     */
    public CampanhaVO getCampanha() {
        if (campanha == null) {
            campanha = new CampanhaVO();
        }
        return (campanha);
    }

    /**
     * Define o objeto da classe <code>Campanha</code> relacionado com (<code>CompromissoAgendaPessoaHorario</code>).
     */
    public void setCampanha(CampanhaVO obj) {
        this.campanha = obj;
    }

    /**
     * Retorna o objeto da classe <code>Prospects</code> relacionado com (<code>CompromissoAgendaPessoaHorario</code>).
     */
    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return (prospect);
    }

    /**
     * Define o objeto da classe <code>Prospects</code> relacionado com (<code>CompromissoAgendaPessoaHorario</code>).
     */
    public void setProspect(ProspectsVO obj) {
        this.prospect = obj;
    }

    public TipoContatoEnum getTipoContato() {
        if (tipoContato == null) {
            tipoContato = TipoContatoEnum.NENHUM;
        }
        return (tipoContato);
    }

    public void setTipoContato(TipoContatoEnum tipoContato) {
        this.tipoContato = tipoContato;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return (dataCadastro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(getDataCadastro()));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataCompromisso_Apresentar() {
        return (Uteis.getData(getDataCompromisso()));
    }

    public Date getDataCompromisso() {
        if (dataCompromisso == null) {
            dataCompromisso = new Date();
        }
        return dataCompromisso;
    }

    public void setDataCompromisso(Date dataCompromisso) {
        this.dataCompromisso = dataCompromisso;
    }

    public Boolean getUrgente() {
        if (urgente == null) {
            urgente = Boolean.FALSE;
        }
        return (urgente);
    }

    public Boolean isUrgente() {
        if (urgente == null) {
            urgente = Boolean.FALSE;
        }
        return (urgente);
    }

    public void setUrgente(Boolean urgente) {
        this.urgente = urgente;
    }

    public String getOrigem() {
        if (origem == null) {
            return "";
        }
        return (origem);
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getObservacao() {
        if (observacao == null) {
            return "";
        }
        return (observacao);
    }

    public String getObservacao_Apresentar() {
        if (getObservacao().length() > 200) {
            return getObservacao().substring(0, 200);
        } else {
            return getObservacao();
        }
    }

    public String getObservacao_ApresentarResumido() {
        if (getObservacao().length() > 30) {
            return getObservacao().substring(0, 30) + "...";
        } else {
            return getObservacao();
        }
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getIsTipoCompromissoTarefa() {
        if (getTipoCompromisso().equals(TipoCompromissoEnum.TAREFA)) {
            return true;
        }
        return false;
    }

    public Boolean getIsTipoCompromissoContato() {
    	if (getTipoCompromisso().equals(TipoCompromissoEnum.CONTATO)  || getTipoCompromisso().equals(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS) || getTipoCompromisso().equals(TipoCompromissoEnum.QUERO_SER_ALUNO)) {
            return true;
        }
        return false;
    }

    public TipoCompromissoEnum getTipoCompromisso() {
        if (tipoCompromisso == null) {
            tipoCompromisso = TipoCompromissoEnum.CONTATO;
        }
        return (tipoCompromisso);
    }
    
    public String getTipoCompromissoEnum_Apresentar() {
    	return UteisJSF.internacionalizar("enum_TipoCompromissoEnum_"+getTipoCompromisso().name());
    }

    public void setTipoCompromisso(TipoCompromissoEnum tipoCompromisso) {
        this.tipoCompromisso = tipoCompromisso;
    }

    public String getHora() {
        if (hora == null) {
            return "";
        }
        return (hora);
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHoraFim() {
        if (horaFim == null) {
            return "";
        }
        return (horaFim);
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public AgendaPessoaHorarioVO getAgendaPessoaHorario() {
        if (agendaPessoaHorario == null) {
            agendaPessoaHorario = new AgendaPessoaHorarioVO();
        }
        return agendaPessoaHorario;
    }

    public void setAgendaPessoaHorario(AgendaPessoaHorarioVO agendaPessoaHorario) {
        this.agendaPessoaHorario = agendaPessoaHorario;
    }

    public String getDescricao() {
        if (descricao == null) {
            return "";
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

    public EtapaWorkflowVO getEtapaWorkflowVO() {
        if (etapaWorkflowVO == null) {
            etapaWorkflowVO = new EtapaWorkflowVO();
        }
        return etapaWorkflowVO;
    }

    public void setEtapaWorkflowVO(EtapaWorkflowVO etapaWorkflowVO) {
        this.etapaWorkflowVO = etapaWorkflowVO;
    }

    public TipoSituacaoCompromissoEnum getTipoSituacaoCompromissoEnum() {
        if (tipoSituacaoCompromissoEnum == null) {
            tipoSituacaoCompromissoEnum = TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO;
        }
        return tipoSituacaoCompromissoEnum;
    }
    public String getTipoSituacaoCompromissoEnum_Apresentar() {
    	
    	return UteisJSF.internacionalizar("enum_TipoSituacaoCompromissoEnum_"+getTipoSituacaoCompromissoEnum().name());
    }

    public void setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum) {
        this.tipoSituacaoCompromissoEnum = tipoSituacaoCompromissoEnum;
    }

    public Boolean getCompromissoSelecionado() {
         if (compromissoSelecionado == null) {
            compromissoSelecionado = Boolean.FALSE;
        }
        return compromissoSelecionado;
    }

    public void setCompromissoSelecionado(Boolean compromissoSelecionado) {
        this.compromissoSelecionado = compromissoSelecionado;
    }

    public Boolean getCompromissoRealizadoComRemarcacao() {
        if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO)) {
            return true;
        }
        return false;
    }

    public Boolean getCompromissoParalizado() {
        if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.PARALIZADO)) {
            return true;
        }
        return false;
    }

    public Boolean getCompromissoRealizado() {
        if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.REALIZADO)) {
            return true;
        }
        return false;
    }
    
    public Boolean getCompromissoRealizadoComInsucessoContato() {
        if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.REALIZADO_COM_INSUCESSO_CONTATO)) {
            return true;
        }
        return false;
    }

    public Boolean getCompromissoAguardandoContato() {
        if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
            return true;
        }
        return false;
    }
    
    public Boolean getPermitirExcluirCompromisso() {
        if (getIsTipoCompromissoTarefa()) {
            // tarefas sempre podem ser excluídas, caso a pessoa tenha permissão para isto
            return true;
        }
        if ((!getCompromissoRealizado()) &&
            (!getCompromissoRealizadoComRemarcacao()) &&
            (!getCompromissoRealizadoComInsucessoContato()) &&
            (!getCompromissoParalizado())) {
            return true;
        }
        return false;
    }
    
    public Boolean getPermitirAlteracaoCompromisso() {
        if (!getCompromissoRealizado() && !getCompromissoRealizadoComRemarcacao() && !getCompromissoRealizadoComInsucessoContato() && !getReagendado()) {
            return true;
        }
        return false;
    }

    public CursoVO getCursoInteresseProspect() {
        if (cursoInteresseProspect == null) {
            cursoInteresseProspect = new CursoVO();
        }
        return cursoInteresseProspect;
    }

    public void setCursoInteresseProspect(CursoVO cursoInteresseProspect) {
        this.cursoInteresseProspect = cursoInteresseProspect;
    }

	public Boolean getRealizado() {
		if (realizado == null) {
			realizado = Boolean.FALSE;
		}
		return realizado;
	}

	public void setRealizado(Boolean realizado) {
		this.realizado = realizado;
	}
        
        public String getImagemTipoContato() {
            if (this.getTipoContato().equals(TipoContatoEnum.EMAIL)) {
                return "fas fa-envelope";
            }
            if (this.getTipoContato().equals(TipoContatoEnum.VISITA)) {
                return "far fa-address-card";
            }
            return "fas fa-phone-volume";
        }

    /**
     * @return the preInscricao
     */
    public PreInscricaoVO getPreInscricao() {
        if (preInscricao == null) {
            preInscricao = new PreInscricaoVO();
        }
        return preInscricao;
    }

    /**
     * @param preInscricao the preInscricao to set
     */
    public void setPreInscricao(PreInscricaoVO preInscricao) {
        this.preInscricao = preInscricao;
    }
    
    public Date getOrdenacao(){
    	try {
			return Uteis.getData(Uteis.getData(getDataCompromisso(), "dd/MM/yyyy")+" "+getHora()+":00", "dd/MM/yyyy hh:mm:ss");
		} catch (ParseException e) {
			return getDataCompromisso();			
		}
    }

	public Boolean getPossuiMatricula() {
		if(possuiMatricula == null){
			possuiMatricula = false;
		}
		return possuiMatricula;
	}

	public void setPossuiMatricula(Boolean possuiMatricula) {
		this.possuiMatricula = possuiMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if(matriculaVOs == null){
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	} 
    
   public String getHistoricoReagendamentoCompromisso() {
    	if (historicoReagendamentoCompromisso == null) {
    		historicoReagendamentoCompromisso = "";
    	}
		return historicoReagendamentoCompromisso;
	}

	public void setHistoricoReagendamentoCompromisso(String historicoReagendamentoCompromisso) {
		this.historicoReagendamentoCompromisso = historicoReagendamentoCompromisso;
	}

	public Date getDataInicialCompromisso() {
		if (dataInicialCompromisso == null) {
			dataInicialCompromisso = new Date();
		}
		return dataInicialCompromisso;
	}
	
	public String getDataInicialCompromisso_Apresentar() {
		if (dataInicialCompromisso == null) {
			return "";
		}
		return Uteis.getData(dataInicialCompromisso);
	}

	public void setDataInicialCompromisso(Date dataInicialCompromisso) {
		this.dataInicialCompromisso = dataInicialCompromisso;
	}

	public Date getDataCompromissoAnterior() {
		return dataCompromissoAnterior;
	}

	public void setDataCompromissoAnterior(Date dataCompromissoAnterior) {
		this.dataCompromissoAnterior = dataCompromissoAnterior;
	}	
	
	/**
	 * Método responsável por adiar um determinado compromisso para uma data futura,
	 * realizando as alterações nos atributos necessários de forma a garantir
	 * que o registro do histórico de alterações seja preservado.
	 * @param dataReagendar
	 */
	public void reagendarCompromissoParaDataFutura(Date dataReagendar, UsuarioVO usuarioLogado) {
		if (Uteis.getData(this.getDataInicialCompromisso()).equals(Uteis.getData(dataReagendar))) {
			// se estou passando por este metodo mas a data do compromisso nao foi alterada,
			// entao nao justifica-se gerar um historico de reagendamento.
			return;
		}
		this.setDataCompromisso(dataReagendar);
	    String dataHoje = Uteis.getData(new Date());
	    String historico = "Compromisso reagendado em " + dataHoje + " para " + Uteis.getData(dataReagendar) + " por " + usuarioLogado.getNome();
	    if (this.getHistoricoReagendamentoCompromisso().equals("")) {
	    	this.setHistoricoReagendamentoCompromisso(historico);
	    } else {
	    	this.setHistoricoReagendamentoCompromisso(this.getHistoricoReagendamentoCompromisso() + " - " + historico);
	    }
	}
 
	public String getUltimaInteracao() {
		if (ultimaInteracao == null) {
			ultimaInteracao = "";
		}
		return ultimaInteracao;
	}

	public void setUltimaInteracao(String ultimaInteracao) {
		this.ultimaInteracao = ultimaInteracao;
	}
	
	public Boolean getApresentarBotaoInscricaoCandidatoProcSeletivo() {
		return getCampanha().getTipoCampanha().equals(TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO);
	}
	
	public String getDuvida() {
		if (duvida == null) {
			duvida = "";
		}
		return duvida;
	}

	public void setDuvida(String duvida) {
		this.duvida = duvida;
	}
	
    public Boolean getCompromissoNaoRealizado() {
        if ((!getCompromissoRealizado()) && (!getCompromissoRealizadoComRemarcacao()) && (!getCompromissoParalizado()) && (!getCancelado())) {
            return true;
        }
        return false;
    }

	public Boolean getReagendado() {
		if (reagendado == null) {
			reagendado = false;
		}
		return reagendado;
	}

	public void setReagendado(Boolean reagendado) {
		this.reagendado = reagendado;
	}
    
	public Boolean getVerificarAgendaEstaAtrasada() {
		if (!Uteis.getData(getDataCompromisso(), "dd/MM/yy").equals(Uteis.getDataAtual())) {
			return true;
		}else {
			return false;
		}
	}

	public Boolean getCancelado() {
		if (getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.CANCELADO)) {
            return true;
        }
        return false;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}
	
	
	
    
	

}