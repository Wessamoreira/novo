package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.sad.LegendaGraficoVO;


public class FiltroPainelGestorAcademicoVO extends SuperVO {
    
    private static final long serialVersionUID = 6045315627902507308L;
    private Integer codigo;
    private Boolean preMatriculado;    
    private Boolean matriculado;
    private Boolean calouro;
    private Boolean reingresso;
    private Boolean reabertura;
    private Boolean portadorDiploma;
    private Boolean transferenciaInterna;
    private Boolean transferenciaExterna;
    private Boolean veterano;
    private Boolean possivelFormando;
    private Boolean naoMatriculadoTotal;
    private Boolean naoMatriculadoAdimplente;
    private Boolean naoMatriculadoInadimplente;
    private Boolean rematriculado;
    private Boolean trancado;
    private Boolean cancelado;
    private Boolean abandonoCurso;
    private Boolean formado;
    private Boolean transferenciaSaida;
    private Boolean numeroAlunoTurno;
    private Boolean numeroAlunoSexo;
    private Boolean numeroAlunoTurma;
    private Boolean numeroTurma;
    private Boolean numeroTurmaTurno;
    private Boolean numeroProfessor;
    private Boolean numeroProfessorTurno;
    private Boolean numeroCursoMinistrado;
    private Boolean monitoramentoImpressaoDeclaracao;
    private Boolean monitoramentoProcessoSeletivo;
    private Boolean visualizarAnaliticamente;
    private Boolean filtrarPorAnoSemestre;
    private Boolean jubilado;
    private Boolean naoMatriculadoNaoPreMatriculado;
    
    private UsuarioVO usuario;
    private Boolean apresentarGrafico;
    private String tipoNivelEducacional;
    private PeriodicidadeEnum periodicidadeCurso;
    
    /**
     * Atributos Transientes - Utilizado apenas na apresentação do resultado 
     */        
    private String resultadoGraficoConsultaMonitoramentoAcademico;
    private String resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao;
    private String resultadoGraficoConsultaMonitoramentoAcademicoSexo;
    private String resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno;
    private String resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma;
    private String resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno;
    private String resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno;
    private String resultadoGraficoConsultaMonitoramentoCancelamento;
    private String resultadoGraficoMonitoramentoCalouroPorFormaIngresso;
    private String resultadoGraficoMonitoramentoVeteranoPorFormaIngresso;
    private String resultadoGraficoMonitoramentoCalouroPorCurso;
    private String resultadoGraficoMonitoramentoVeteranoPorCurso;
    private String resultadoGraficoMonitoramentoPreMatriculadoPorCurso;
    private String resultadoGraficoMonitoramentoMatriculaPorCurso;
    private String resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso;
    private String resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso;
    private String resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso;
    private String resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso;
    private String resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso;
    private String resultadoGraficoMonitoramentoMatriculaPorFormaIngresso;
    private String resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso;
    private String resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso;
    private String resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso;
    private String resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso;
    private String resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso;
    private String resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso;
    private String resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso;
    private String resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso;
	
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoSituacaoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoTurmaVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoSexoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoTurnoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoTurmaTurnoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoProfessorTurnoVOs;
    private List<PainelGestorMonitoramentoAcademicoVO> painelGestorDetalheMonitoramentoAcademicoVOs;
    private OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcaoAtual;
    private PainelGestorMonitoramentoProcessoSeletivoVO painelGestorMonitoramentoProcessoSeletivoVO;
    private List<PainelGestorMonitoramentoProcessoSeletivoVO> painelGestorMonitoramentoProcessoSeletivoVOs;
    private Integer codigoBase;
    private String tituloApresentar;
    private String resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida;
    private String resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado;
    
    private List<LegendaGraficoVO> legendaGraficoEvolucaoPorNivelEducacionalVOs;
	
    private String dadosEvolucaoAcademicaCalouroInstituicao;
    private String dadosEvolucaoAcademicaVeteranoNivelAtual;
    private String dadosEvolucaoAcademicaVeteranoNivelAnterior;
    private String dadosEvolucaoAcademicaNaoRenovaramProximoNivel;
    private String dadosEvolucaoAcademicaNaoRenovaramMesmoNivel;
    
    private String categoriaEvolucaoAcademica;
        
    public Boolean getPreMatriculado() {
        if(preMatriculado == null){
            preMatriculado = true;
        }
        return preMatriculado;
    }

    
    public void setPreMatriculado(Boolean preMatriculado) {
        this.preMatriculado = preMatriculado;
    }

    
    public Boolean getMatriculado() {
        if(matriculado == null){
            matriculado = true;
        }
        return matriculado;
    }

    
    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    
    public Boolean getCalouro() {
        if(calouro == null){
            calouro = true;
        }
        return calouro;
    }

    
    public void setCalouro(Boolean calouro) {
        this.calouro = calouro;
    }

    
    public Boolean getReingresso() {
        if(reingresso == null){
            reingresso = true;
        }
        return reingresso;
    }

    
    public void setReingresso(Boolean reingresso) {
        this.reingresso = reingresso;
    }

    
    public Boolean getReabertura() {
        if(reabertura == null){
            reabertura = true;
        }
        return reabertura;
    }

    
    public void setReabertura(Boolean reabertura) {
        this.reabertura = reabertura;
    }

    
    public Boolean getPortadorDiploma() {
        if(portadorDiploma == null){
            portadorDiploma = true;
        }
        return portadorDiploma;
    }

    
    public void setPortadorDiploma(Boolean portadorDiploma) {
        this.portadorDiploma = portadorDiploma;
    }

    
    public Boolean getTransferenciaInterna() {
        if(transferenciaInterna == null){
            transferenciaInterna = true;
        }
        return transferenciaInterna;
    }

    
    public void setTransferenciaInterna(Boolean transferenciaInterna) {
        this.transferenciaInterna = transferenciaInterna;
    }

    
    public Boolean getTransferenciaExterna() {
        if(transferenciaExterna == null){
            transferenciaExterna = true;
        }
        return transferenciaExterna;
    }

    
    public void setTransferenciaExterna(Boolean transferenciaExterna) {
        this.transferenciaExterna = transferenciaExterna;
    }

    
    public Boolean getVeterano() {
        if(veterano == null){
            veterano = true;
        }
        return veterano;
    }

    
    public void setVeterano(Boolean veterano) {
        this.veterano = veterano;
    }

    
    public Boolean getPossivelFormando() {
        if(possivelFormando == null){
            possivelFormando = true;
        }
        return possivelFormando;
    }

    
    public void setPossivelFormando(Boolean possivelFormando) {
        this.possivelFormando = possivelFormando;
    }

    
    public Boolean getNaoMatriculadoTotal() {
        if(naoMatriculadoTotal == null){
            naoMatriculadoTotal = true;
        }
        return naoMatriculadoTotal;
    }

    
    public void setNaoMatriculadoTotal(Boolean naoMatriculadoTotal) {
        this.naoMatriculadoTotal = naoMatriculadoTotal;
    }

    
    public Boolean getNaoMatriculadoAdimplente() {
        if(naoMatriculadoAdimplente == null){
            naoMatriculadoAdimplente = true;
        }
        return naoMatriculadoAdimplente;
    }

    
    public void setNaoMatriculadoAdimplente(Boolean naoMatriculadoAdimplente) {
        this.naoMatriculadoAdimplente = naoMatriculadoAdimplente;
    }

    
    public Boolean getNaoMatriculadoInadimplente() {
        if(naoMatriculadoInadimplente == null){
            naoMatriculadoInadimplente = true;
        }
        return naoMatriculadoInadimplente;
    }

    
    public void setNaoMatriculadoInadimplente(Boolean naoMatriculadoInadimplente) {
        this.naoMatriculadoInadimplente = naoMatriculadoInadimplente;
    }

    
    public Boolean getRematriculado() {
        if(rematriculado == null){
            rematriculado = true;
        }
        return rematriculado;
    }

    
    public void setRematriculado(Boolean rematriculado) {
        this.rematriculado = rematriculado;
    }

    
    public Boolean getTrancado() {
        if(trancado == null){
            trancado = true;
        }
        return trancado;
    }

    
    public void setTrancado(Boolean trancado) {
        this.trancado = trancado;
    }

    
    public Boolean getCancelado() {
        if(cancelado == null){
            cancelado = true;
        }
        return cancelado;
    }

    
    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    
    public Boolean getTransferenciaSaida() {
        if(transferenciaSaida == null){
            transferenciaSaida = true;
        }
        return transferenciaSaida;
    }

    
    public void setTransferenciaSaida(Boolean transferenciaSaida) {
        this.transferenciaSaida = transferenciaSaida;
    }

    
    public Boolean getNumeroAlunoTurno() {
        if(numeroAlunoTurno == null){
            numeroAlunoTurno = true;
        }
        return numeroAlunoTurno;
    }

    
    public void setNumeroAlunoTurno(Boolean numeroAlunoTurno) {
        this.numeroAlunoTurno = numeroAlunoTurno;
    }

    
    public Boolean getNumeroAlunoSexo() {
        if(numeroAlunoSexo == null){
            numeroAlunoSexo = true;
        }
        return numeroAlunoSexo;
    }

    
    public void setNumeroAlunoSexo(Boolean numeroAlunoSexo) {
        this.numeroAlunoSexo = numeroAlunoSexo;
    }

    
    public Boolean getNumeroAlunoTurma() {
        if(numeroAlunoTurma == null){
            numeroAlunoTurma = true;
        }
        return numeroAlunoTurma;
    }

    
    public void setNumeroAlunoTurma(Boolean numeroAlunoTurma) {
        this.numeroAlunoTurma = numeroAlunoTurma;
    }

    
    public Boolean getNumeroTurma() {
        if(numeroTurma == null){
            numeroTurma = true;
        }
        return numeroTurma;
    }

    
    public void setNumeroTurma(Boolean numeroTurma) {
        this.numeroTurma = numeroTurma;
    }

    
    public Boolean getNumeroTurmaTurno() {
        if(numeroTurmaTurno == null){
            numeroTurmaTurno = true;
        }
        return numeroTurmaTurno;
    }

    
    public void setNumeroTurmaTurno(Boolean numeroTurmaTurno) {
        this.numeroTurmaTurno = numeroTurmaTurno;
    }

    
    public Boolean getNumeroProfessor() {
        if(numeroProfessor == null){
            numeroProfessor = true;
        }
        return numeroProfessor;
    }

    
    public void setNumeroProfessor(Boolean numeroProfessor) {
        this.numeroProfessor = numeroProfessor;
    }

    
    public Boolean getNumeroProfessorTurno() {
        if(numeroProfessorTurno == null){
            numeroProfessorTurno = true;
        }
        return numeroProfessorTurno;
    }

    
    public void setNumeroProfessorTurno(Boolean numeroProfessorTurno) {
        this.numeroProfessorTurno = numeroProfessorTurno;
    }

    
    public Boolean getNumeroCursoMinistrado() {
        if(numeroCursoMinistrado == null){
            numeroCursoMinistrado = true;
        }
        return numeroCursoMinistrado;
    }

    
    public void setNumeroCursoMinistrado(Boolean numeroCursoMinistrado) {
        this.numeroCursoMinistrado = numeroCursoMinistrado;
    }

    
    public Boolean getVisualizarAnaliticamente() {
        if(visualizarAnaliticamente == null){
            visualizarAnaliticamente = true;
        }
        return visualizarAnaliticamente;
    }

    
    public void setVisualizarAnaliticamente(Boolean visualizarAnaliticamente) {
        this.visualizarAnaliticamente = visualizarAnaliticamente;
    }


    
    public UsuarioVO getUsuario() {
        if(usuario == null){
            usuario = new UsuarioVO();
        }
        return usuario;
    }


    
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
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


    
    public String getResultadoGraficoConsultaMonitoramentoAcademico() {
        if(resultadoGraficoConsultaMonitoramentoAcademico == null){
            resultadoGraficoConsultaMonitoramentoAcademico ="";
        }
        return resultadoGraficoConsultaMonitoramentoAcademico;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademico(String resultadoGraficoConsultaMonitoramentoAcademico) {
        this.resultadoGraficoConsultaMonitoramentoAcademico = resultadoGraficoConsultaMonitoramentoAcademico;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoAcademicoSexo() {
        if(resultadoGraficoConsultaMonitoramentoAcademicoSexo == null){
            resultadoGraficoConsultaMonitoramentoAcademicoSexo = "";
        }
        return resultadoGraficoConsultaMonitoramentoAcademicoSexo;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademicoSexo(String resultadoGraficoConsultaMonitoramentoAcademicoSexo) {
        this.resultadoGraficoConsultaMonitoramentoAcademicoSexo = resultadoGraficoConsultaMonitoramentoAcademicoSexo;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno() {
        if(resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno == null){
            resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno = "";
        }
        return resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno(String resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno) {
        this.resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno = resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma() {
        if(resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma == null){
            resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma = "";
        }
        return resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma(String resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma) {
        this.resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma = resultadoGraficoConsultaMonitoramentoAcademicoAlunoTurma;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno() {
        if(resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno == null){
            resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno = "";
        }
        return resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno(String resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno) {
        this.resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno = resultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno() {
        if(resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno == null){
            resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno = "";
        }
        return resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno(String resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno) {
        this.resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno = resultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoVOs() {
        if(painelGestorMonitoramentoAcademicoVOs == null){
            painelGestorMonitoramentoAcademicoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>();
        }
        return painelGestorMonitoramentoAcademicoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoVOs) {
        this.painelGestorMonitoramentoAcademicoVOs = painelGestorMonitoramentoAcademicoVOs;
    }


    
    public Boolean getApresentarGrafico() {
        if(apresentarGrafico == null){
            apresentarGrafico = true;
        }
        return apresentarGrafico;
    }


    
    public void setApresentarGrafico(Boolean apresentarGrafico) {
        this.apresentarGrafico = apresentarGrafico;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorDetalheMonitoramentoAcademicoVOs() {
        if(painelGestorDetalheMonitoramentoAcademicoVOs == null){
            painelGestorDetalheMonitoramentoAcademicoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorDetalheMonitoramentoAcademicoVOs;
    }


    
    public void setPainelGestorDetalheMonitoramentoAcademicoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorDetalheMonitoramentoAcademicoVOs) {
        this.painelGestorDetalheMonitoramentoAcademicoVOs = painelGestorDetalheMonitoramentoAcademicoVOs;
    }


    
    public OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum getOpcaoAtual() {
        return opcaoAtual;
    }


    
    public void setOpcaoAtual(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcaoAtual) {
        this.opcaoAtual = opcaoAtual;
    }


    
    public Integer getCodigoBase() {
        if(codigoBase == null){
            codigoBase = 0;
        }
        return codigoBase;
    }


    
    public void setCodigoBase(Integer codigoBase) {
        this.codigoBase = codigoBase;
    }


    
    public String getTituloApresentar() {
        return tituloApresentar;
    }


    
    public void setTituloApresentar(String tituloApresentar) {
        this.tituloApresentar = tituloApresentar;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoAlunoTurmaVOs() {
        if(painelGestorMonitoramentoAcademicoAlunoTurmaVOs == null){
            painelGestorMonitoramentoAcademicoAlunoTurmaVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoAlunoTurmaVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoAlunoTurmaVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoTurmaVOs) {
        this.painelGestorMonitoramentoAcademicoAlunoTurmaVOs = painelGestorMonitoramentoAcademicoAlunoTurmaVOs;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoAlunoSexoVOs() {
        if(painelGestorMonitoramentoAcademicoAlunoSexoVOs == null){
            painelGestorMonitoramentoAcademicoAlunoSexoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoAlunoSexoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoAlunoSexoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoSexoVOs) {
        this.painelGestorMonitoramentoAcademicoAlunoSexoVOs = painelGestorMonitoramentoAcademicoAlunoSexoVOs;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoAlunoTurnoVOs() {
        if(painelGestorMonitoramentoAcademicoAlunoTurnoVOs == null){
            painelGestorMonitoramentoAcademicoAlunoTurnoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoAlunoTurnoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoAlunoTurnoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoAlunoTurnoVOs) {
        this.painelGestorMonitoramentoAcademicoAlunoTurnoVOs = painelGestorMonitoramentoAcademicoAlunoTurnoVOs;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoTurmaTurnoVOs() {
        if(painelGestorMonitoramentoAcademicoTurmaTurnoVOs == null){
            painelGestorMonitoramentoAcademicoTurmaTurnoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoTurmaTurnoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoTurmaTurnoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoTurmaTurnoVOs) {
        this.painelGestorMonitoramentoAcademicoTurmaTurnoVOs = painelGestorMonitoramentoAcademicoTurmaTurnoVOs;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoProfessorTurnoVOs() {
        if(painelGestorMonitoramentoAcademicoProfessorTurnoVOs == null){
            painelGestorMonitoramentoAcademicoProfessorTurnoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoProfessorTurnoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoProfessorTurnoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoProfessorTurnoVOs) {
        this.painelGestorMonitoramentoAcademicoProfessorTurnoVOs = painelGestorMonitoramentoAcademicoProfessorTurnoVOs;
    }


    
    public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoSituacaoVOs() {
        if(painelGestorMonitoramentoAcademicoSituacaoVOs == null){
            painelGestorMonitoramentoAcademicoSituacaoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
        }
        return painelGestorMonitoramentoAcademicoSituacaoVOs;
    }


    
    public void setPainelGestorMonitoramentoAcademicoSituacaoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoSituacaoVOs) {
        this.painelGestorMonitoramentoAcademicoSituacaoVOs = painelGestorMonitoramentoAcademicoSituacaoVOs;
    }


    
    public String getResultadoGraficoConsultaMonitoramentoCancelamento() {
        if(resultadoGraficoConsultaMonitoramentoCancelamento == null){
            resultadoGraficoConsultaMonitoramentoCancelamento = "";
        }
        return resultadoGraficoConsultaMonitoramentoCancelamento;
    }


    
    public void setResultadoGraficoConsultaMonitoramentoCancelamento(String resultadoGraficoConsultaMonitoramentoCancelamento) {
        this.resultadoGraficoConsultaMonitoramentoCancelamento = resultadoGraficoConsultaMonitoramentoCancelamento;
    }


	public Boolean getMonitoramentoImpressaoDeclaracao() {
		if(monitoramentoImpressaoDeclaracao== null){
			monitoramentoImpressaoDeclaracao = true;
		}
		return monitoramentoImpressaoDeclaracao;
	}


	public void setMonitoramentoImpressaoDeclaracao(Boolean monitoramentoImpressaoDeclaracao) {
		this.monitoramentoImpressaoDeclaracao = monitoramentoImpressaoDeclaracao;
	}


	public String getResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao() {
		if(resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao == null){
			resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao = "";
		}
		return resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao;
	}


	public void setResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao(String resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao) {
		this.resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao = resultadoGraficoConsultaMonitoramentoImpressaoDeclaracao;
	}


	public List<PainelGestorMonitoramentoAcademicoVO> getPainelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs() {
		if(painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs == null){
			painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs = new ArrayList<PainelGestorMonitoramentoAcademicoVO>(0);
		}
		return painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs;
	}


	public void setPainelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs(List<PainelGestorMonitoramentoAcademicoVO> painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs) {
		this.painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs = painelGestorMonitoramentoAcademicoImpressaoDeclaracaoVOs;
	}

	public Boolean getAbandonoCurso() {
		if(abandonoCurso == null){
			abandonoCurso = true;
		}
		return abandonoCurso;
	}


	public void setAbandonoCurso(Boolean abandonoCurso) {
		this.abandonoCurso = abandonoCurso;
	}


	public Boolean getFormado() {
		if(formado == null){
			formado = true;
		}
		return formado;
	}


	public void setFormado(Boolean formado) {
		this.formado = formado;
	}


	public Boolean getMonitoramentoProcessoSeletivo() {
		if (monitoramentoProcessoSeletivo == null) {
			monitoramentoProcessoSeletivo = true;
		}
		return monitoramentoProcessoSeletivo;
	}


	public void setMonitoramentoProcessoSeletivo(Boolean monitoramentoProcessoSeletivo) {
		this.monitoramentoProcessoSeletivo = monitoramentoProcessoSeletivo;
	}


	public PainelGestorMonitoramentoProcessoSeletivoVO getPainelGestorMonitoramentoProcessoSeletivoVO() {
		if (painelGestorMonitoramentoProcessoSeletivoVO == null) {
			painelGestorMonitoramentoProcessoSeletivoVO = new PainelGestorMonitoramentoProcessoSeletivoVO();
		}
		return painelGestorMonitoramentoProcessoSeletivoVO;
	}


	public void setPainelGestorMonitoramentoProcessoSeletivoVO(PainelGestorMonitoramentoProcessoSeletivoVO painelGestorMonitoramentoProcessoSeletivoVO) {
		this.painelGestorMonitoramentoProcessoSeletivoVO = painelGestorMonitoramentoProcessoSeletivoVO;
	}


	public List<PainelGestorMonitoramentoProcessoSeletivoVO> getPainelGestorMonitoramentoProcessoSeletivoVOs() {
		if (painelGestorMonitoramentoProcessoSeletivoVOs == null) {
			painelGestorMonitoramentoProcessoSeletivoVOs = new ArrayList<PainelGestorMonitoramentoProcessoSeletivoVO>(0);
		}
		return painelGestorMonitoramentoProcessoSeletivoVOs;
	}


	public void setPainelGestorMonitoramentoProcessoSeletivoVOs(List<PainelGestorMonitoramentoProcessoSeletivoVO> painelGestorMonitoramentoProcessoSeletivoVOs) {
		this.painelGestorMonitoramentoProcessoSeletivoVOs = painelGestorMonitoramentoProcessoSeletivoVOs;
	}


	public String getTipoNivelEducacional() {
		if (tipoNivelEducacional == null) {
			tipoNivelEducacional = "";
		}
		return tipoNivelEducacional;
	}


	public void setTipoNivelEducacional(String tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}
    
    /**
     * @return the resultadoGraficoMonitoramentoCalouroPorFormaIngresso
     */
    public String getResultadoGraficoMonitoramentoCalouroPorFormaIngresso() {
        if (resultadoGraficoMonitoramentoCalouroPorFormaIngresso == null) {
            resultadoGraficoMonitoramentoCalouroPorFormaIngresso = "";
        }
        return resultadoGraficoMonitoramentoCalouroPorFormaIngresso;
    }

    /**
     * @param resultadoGraficoMonitoramentoCalouroPorFormaIngresso the resultadoGraficoMonitoramentoCalouroPorFormaIngresso to set
     */
    public void setResultadoGraficoMonitoramentoCalouroPorFormaIngresso(String resultadoGraficoMonitoramentoCalouroPorFormaIngresso) {
        this.resultadoGraficoMonitoramentoCalouroPorFormaIngresso = resultadoGraficoMonitoramentoCalouroPorFormaIngresso;
    }

    /**
     * @return the resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso
     */
    public String getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso() {
        if (resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso == null) {
            resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso = "";
        }
        return resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso;
    }

    /**
     * @param resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso the resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso to set
     */
    public void setResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso(String resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso) {
        this.resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso = resultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso;
    }

    /**
     * @return the resultadoGraficoMonitoramentoCalouroPorCurso
     */
    public String getResultadoGraficoMonitoramentoCalouroPorCurso() {
        if (resultadoGraficoMonitoramentoCalouroPorCurso == null) { 
            resultadoGraficoMonitoramentoCalouroPorCurso = "";
        }
        return resultadoGraficoMonitoramentoCalouroPorCurso;
    }

    /**
     * @param resultadoGraficoMonitoramentoCalouroPorCurso the resultadoGraficoMonitoramentoCalouroPorCurso to set
     */
    public void setResultadoGraficoMonitoramentoCalouroPorCurso(String resultadoGraficoMonitoramentoCalouroPorCurso) {
        this.resultadoGraficoMonitoramentoCalouroPorCurso = resultadoGraficoMonitoramentoCalouroPorCurso;
    }

    public String getResultadoGraficoMonitoramentoVeteranoPorCurso() {
		if (resultadoGraficoMonitoramentoVeteranoPorCurso == null) {
			resultadoGraficoMonitoramentoVeteranoPorCurso = "";
		}
		return resultadoGraficoMonitoramentoVeteranoPorCurso;
	}


	public void setResultadoGraficoMonitoramentoVeteranoPorCurso(String resultadoGraficoMonitoramentoVeteranoPorCurso) {
		this.resultadoGraficoMonitoramentoVeteranoPorCurso = resultadoGraficoMonitoramentoVeteranoPorCurso;
	}


	public String getResultadoGraficoMonitoramentoVeteranoPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoVeteranoPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoVeteranoPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoVeteranoPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoVeteranoPorFormaIngresso(String resultadoGraficoMonitoramentoVeteranoPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoVeteranoPorFormaIngresso = resultadoGraficoMonitoramentoVeteranoPorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoPreMatriculadoPorCurso() {
		if (resultadoGraficoMonitoramentoPreMatriculadoPorCurso == null) {
			resultadoGraficoMonitoramentoPreMatriculadoPorCurso = "";
		}
		return resultadoGraficoMonitoramentoPreMatriculadoPorCurso;
	}


	public void setResultadoGraficoMonitoramentoPreMatriculadoPorCurso(String resultadoGraficoMonitoramentoPreMatriculadoPorCurso) {
		this.resultadoGraficoMonitoramentoPreMatriculadoPorCurso = resultadoGraficoMonitoramentoPreMatriculadoPorCurso;
	}


	public String getResultadoGraficoMonitoramentoMatriculaPorCurso() {
		if (resultadoGraficoMonitoramentoMatriculaPorCurso == null) {
			resultadoGraficoMonitoramentoMatriculaPorCurso = "";
		}
		return resultadoGraficoMonitoramentoMatriculaPorCurso;
	}


	public void setResultadoGraficoMonitoramentoMatriculaPorCurso(String resultadoGraficoMonitoramentoMatriculaPorCurso) {
		this.resultadoGraficoMonitoramentoMatriculaPorCurso = resultadoGraficoMonitoramentoMatriculaPorCurso;
	}


	public String getResultadoGraficoMonitoramentoMatriculaPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoMatriculaPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoMatriculaPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoMatriculaPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoMatriculaPorFormaIngresso(String resultadoGraficoMonitoramentoMatriculaPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoMatriculaPorFormaIngresso = resultadoGraficoMonitoramentoMatriculaPorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso(String resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso = resultadoGraficoMonitoramentoPreMatriculadoCalouroPorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso() {
		if (resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso == null) {
			resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso = "";
		}
		return resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso;
	}


	public void setResultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso(String resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso) {
		this.resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso = resultadoGraficoMonitoramentoPreMatriculadoCalouroPorCurso;
	}


	public String getResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso(String resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso = resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso() {
		if (resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso == null) {
			resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso = "";
		}
		return resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso;
	}


	public void setResultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso(String resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso) {
		this.resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso = resultadoGraficoMonitoramentoPreMatriculadoVeteranoPorCurso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso(String resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso = resultadoGraficoMonitoramentoNaoRenovadosTotalPorCurso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso(String resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso = resultadoGraficoMonitoramentoNaoRenovadosTotalPorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso(String resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso = resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorCurso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso(String resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso = resultadoGraficoMonitoramentoNaoRenovadosAdimplentePorFormaIngresso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso(String resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso = resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorCurso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso(String resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso = resultadoGraficoMonitoramentoNaoRenovadosInadimplentePorFormaIngresso;
	}


	public String getResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida() {
		if (resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida == null) {
			resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida = "";
		}
		return resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida;
	}


	public void setResultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida(String resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida) {
		this.resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida = resultadoGraficoConsultaMonitoramentoAcademicoEvasaoSaida;
	}


	public String getResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado() {
		if (resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado == null) {
			resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado = "";
		}
		return resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado;
	}


	public void setResultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado(String resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado) {
		this.resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado = resultadoGraficoConsultaMonitoramentoAcademicoNaoRenovado;
	}


	public Boolean getNaoMatriculadoNaoPreMatriculado() {
		if (naoMatriculadoNaoPreMatriculado == null) {
			naoMatriculadoNaoPreMatriculado = true;
		}
		return naoMatriculadoNaoPreMatriculado;
	}


	public void setNaoMatriculadoNaoPreMatriculado(Boolean naoMatriculadoNaoPreMatriculado) {
		this.naoMatriculadoNaoPreMatriculado = naoMatriculadoNaoPreMatriculado;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso(String resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso = resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorCurso;
	}


	public String getResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso() {
		if (resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso == null) {
			resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso = "";
		}
		return resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso;
	}


	public void setResultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso(String resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso) {
		this.resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso = resultadoGraficoMonitoramentoNaoRenovadosNaoPreMatriculadoPorFormaIngresso;
	}
	
	public PeriodicidadeEnum getPeriodicidadeCurso() {
		if (periodicidadeCurso == null) {
			periodicidadeCurso = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidadeCurso;
	}


	public void setPeriodicidadeCurso(PeriodicidadeEnum periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}

	public List<LegendaGraficoVO> getLegendaGraficoEvolucaoPorNivelEducacionalVOs() {
		if (legendaGraficoEvolucaoPorNivelEducacionalVOs == null) {
			legendaGraficoEvolucaoPorNivelEducacionalVOs = new ArrayList<LegendaGraficoVO>(0);
		}
		return legendaGraficoEvolucaoPorNivelEducacionalVOs;
	}

	public void setLegendaGraficoEvolucaoPorNivelEducacionalVOs(List<LegendaGraficoVO> legendaGraficoEvolucaoPorNivelEducacionalVOs) {
		this.legendaGraficoEvolucaoPorNivelEducacionalVOs = legendaGraficoEvolucaoPorNivelEducacionalVOs;
	}


	public String getDadosEvolucaoAcademicaCalouroInstituicao() {
		if (dadosEvolucaoAcademicaCalouroInstituicao == null) {
			dadosEvolucaoAcademicaCalouroInstituicao = "";
		}
		return dadosEvolucaoAcademicaCalouroInstituicao;
	}


	public void setDadosEvolucaoAcademicaCalouroInstituicao(String dadosEvolucaoAcademicaCalouroInstituicao) {
		this.dadosEvolucaoAcademicaCalouroInstituicao = dadosEvolucaoAcademicaCalouroInstituicao;
	}


	public String getDadosEvolucaoAcademicaVeteranoNivelAtual() {
		if (dadosEvolucaoAcademicaVeteranoNivelAtual == null) {
			dadosEvolucaoAcademicaVeteranoNivelAtual = "";
		}
		return dadosEvolucaoAcademicaVeteranoNivelAtual;
	}


	public void setDadosEvolucaoAcademicaVeteranoNivelAtual(String dadosEvolucaoAcademicaVeteranoNivelAtual) {
		this.dadosEvolucaoAcademicaVeteranoNivelAtual = dadosEvolucaoAcademicaVeteranoNivelAtual;
	}


	public String getDadosEvolucaoAcademicaVeteranoNivelAnterior() {
		if (dadosEvolucaoAcademicaVeteranoNivelAnterior == null) {
			dadosEvolucaoAcademicaVeteranoNivelAnterior = "";
		}
		return dadosEvolucaoAcademicaVeteranoNivelAnterior;
	}


	public void setDadosEvolucaoAcademicaVeteranoNivelAnterior(String dadosEvolucaoAcademicaVeteranoNivelAnterior) {
		this.dadosEvolucaoAcademicaVeteranoNivelAnterior = dadosEvolucaoAcademicaVeteranoNivelAnterior;
	}


	public String getDadosEvolucaoAcademicaNaoRenovaramProximoNivel() {
		if (dadosEvolucaoAcademicaNaoRenovaramProximoNivel == null) {
			dadosEvolucaoAcademicaNaoRenovaramProximoNivel = "";
		}
		return dadosEvolucaoAcademicaNaoRenovaramProximoNivel;
	}


	public void setDadosEvolucaoAcademicaNaoRenovaramProximoNivel(String dadosEvolucaoAcademicaNaoRenovaramProximoNivel) {
		this.dadosEvolucaoAcademicaNaoRenovaramProximoNivel = dadosEvolucaoAcademicaNaoRenovaramProximoNivel;
	}


	public String getDadosEvolucaoAcademicaNaoRenovaramMesmoNivel() {
		if (dadosEvolucaoAcademicaNaoRenovaramMesmoNivel == null) {
			dadosEvolucaoAcademicaNaoRenovaramMesmoNivel = "";
		}
		return dadosEvolucaoAcademicaNaoRenovaramMesmoNivel;
	}


	public void setDadosEvolucaoAcademicaNaoRenovaramMesmoNivel(String dadosEvolucaoAcademicaNaoRenovaramMesmoNivel) {
		this.dadosEvolucaoAcademicaNaoRenovaramMesmoNivel = dadosEvolucaoAcademicaNaoRenovaramMesmoNivel;
	}
	
	public String getCategoriaEvolucaoAcademica() {
		if (categoriaEvolucaoAcademica == null) {
			categoriaEvolucaoAcademica = "";
		}
		return categoriaEvolucaoAcademica;
	}


	public void setCategoriaEvolucaoAcademica(String categoriaEvolucaoAcademica) {
		this.categoriaEvolucaoAcademica = categoriaEvolucaoAcademica;
	}

	public Boolean getJubilado() {
		if(jubilado == null) {
			jubilado = false;
		}
		return jubilado;
	}

	public void setJubilado(Boolean jubilado) {
		this.jubilado = jubilado;
	}
	
	
}
