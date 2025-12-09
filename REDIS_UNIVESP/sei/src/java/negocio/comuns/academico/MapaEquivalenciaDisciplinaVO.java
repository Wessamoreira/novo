package negocio.comuns.academico;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoRegraAnoSemestreEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraCargaHorariaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraFrequenciaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraNotaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.TipoRelacionamentoDisciplinaEquivalenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "mapaEquivalenciaDisciplinaVO")
public class MapaEquivalenciaDisciplinaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 610270425789120601L;
	private Integer codigo;
	private Integer sequencia;
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricular;
	private String observacao;
	private TipoRegraNotaEquivalenciaEnum tipoRegraNotaEquivalencia;
	/**
	 * Utilizado apenas para as regras das notas de formula de calculo
	 */	
	private String formulaCalculoNota;
	
	
	private TipoRegraCargaHorariaEquivalenciaEnum tipoRegraCargaHorariaEquivalencia;
	private TipoRegraFrequenciaEquivalenciaEnum tipoRegraFrequenciaEquivalencia;
	private TipoRegraAnoSemestreEquivalenciaEnum tipoRegraAnoSemestreEquivalencia;
	private TipoRegraPeriodoLetivoEnum tipoRegraPeriodoLetivo;
	private TipoRelacionamentoDisciplinaEquivalenciaEnum tipoRelacionamentoDisciplinaEquivalencia;
	private List<MapaEquivalenciaDisciplinaCursadaVO> mapaEquivalenciaDisciplinaCursadaVOs;
	private List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaMatrizCurricularVOs;
	private StatusAtivoInativoEnum situacao;
	private Date dataInativacao;
	private UsuarioVO usuarioInativacao;
	private Date dataCadastro;
	private UsuarioVO usuarioCadastro;
	private Boolean equivalenciaPorIsencao;
	/**
	 * Atributos transientes 
	 * 
	 */
	private String disciplinaMatrizCurricular;
	private String disciplinaEquivalente;
	
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaMatrizCurricular() {
		if(mapaEquivalenciaMatrizCurricular == null){
			mapaEquivalenciaMatrizCurricular = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaMatrizCurricular;
	}
	public void setMapaEquivalenciaMatrizCurricular(MapaEquivalenciaMatrizCurricularVO mapaEquivalencia) {
		this.mapaEquivalenciaMatrizCurricular = mapaEquivalencia;
	}
	public String getObservacao() {
		if(observacao == null){
			observacao = "";
		}
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public TipoRegraNotaEquivalenciaEnum getTipoRegraNotaEquivalencia() {
		if(tipoRegraNotaEquivalencia == null){
			tipoRegraNotaEquivalencia = TipoRegraNotaEquivalenciaEnum.MEDIA_NOTA;
		}
		return tipoRegraNotaEquivalencia;
	}
	public void setTipoRegraNotaEquivalencia(TipoRegraNotaEquivalenciaEnum tipoRegraNotaEquivalencia) {
		this.tipoRegraNotaEquivalencia = tipoRegraNotaEquivalencia;
	}
	public String getFormulaCalculoNota() {
		if(formulaCalculoNota == null){
			formulaCalculoNota = "";
		}
		return formulaCalculoNota;
	}
	public void setFormulaCalculoNota(String formulaCalculoNota) {
		this.formulaCalculoNota = formulaCalculoNota;
	}
	public TipoRegraCargaHorariaEquivalenciaEnum getTipoRegraCargaHorariaEquivalencia() {
		if(tipoRegraCargaHorariaEquivalencia == null){
			tipoRegraCargaHorariaEquivalencia = TipoRegraCargaHorariaEquivalenciaEnum.CARGA_HORARIA_GRADE;
		}
		return tipoRegraCargaHorariaEquivalencia;
	}
	public void setTipoRegraCargaHorariaEquivalencia(TipoRegraCargaHorariaEquivalenciaEnum tipoRegraCargaHorariaEquivalencia) {
		this.tipoRegraCargaHorariaEquivalencia = tipoRegraCargaHorariaEquivalencia;
	}
	public TipoRegraFrequenciaEquivalenciaEnum getTipoRegraFrequenciaEquivalencia() {
		if(tipoRegraFrequenciaEquivalencia == null){
			tipoRegraFrequenciaEquivalencia = TipoRegraFrequenciaEquivalenciaEnum.MAIOR_FREQUENCIA;
		}
		return tipoRegraFrequenciaEquivalencia;
	}
	public void setTipoRegraFrequenciaEquivalencia(TipoRegraFrequenciaEquivalenciaEnum tipoRegraFrequenciaEquivalencia) {
		this.tipoRegraFrequenciaEquivalencia = tipoRegraFrequenciaEquivalencia;
	}
	public TipoRegraAnoSemestreEquivalenciaEnum getTipoRegraAnoSemestreEquivalencia() {
		if(tipoRegraAnoSemestreEquivalencia == null){
			tipoRegraAnoSemestreEquivalencia = TipoRegraAnoSemestreEquivalenciaEnum.MAIOR_ANO_SEMESTRE;
		}
		return tipoRegraAnoSemestreEquivalencia;
	}
	public void setTipoRegraAnoSemestreEquivalencia(TipoRegraAnoSemestreEquivalenciaEnum tipoRegraAnoSemestreEquivalencia) {
		this.tipoRegraAnoSemestreEquivalencia = tipoRegraAnoSemestreEquivalencia;
	}
	public TipoRelacionamentoDisciplinaEquivalenciaEnum getTipoRelacionamentoDisciplinaEquivalencia() {
		if(tipoRelacionamentoDisciplinaEquivalencia == null){
			tipoRelacionamentoDisciplinaEquivalencia = TipoRelacionamentoDisciplinaEquivalenciaEnum.RELACIONAMENTO_E;
		}
		return tipoRelacionamentoDisciplinaEquivalencia;
	}
	public void setTipoRelacionamentoDisciplinaEquivalencia(TipoRelacionamentoDisciplinaEquivalenciaEnum tipoRelacionamentoDisciplinaEquivalencia) {
		this.tipoRelacionamentoDisciplinaEquivalencia = tipoRelacionamentoDisciplinaEquivalencia;
	}
	
	@XmlElement(name = "mapaEquivalenciaDisciplinaCursadaVOs")
	public List<MapaEquivalenciaDisciplinaCursadaVO> getMapaEquivalenciaDisciplinaCursadaVOs() {
		if(mapaEquivalenciaDisciplinaCursadaVOs == null){
			mapaEquivalenciaDisciplinaCursadaVOs = new ArrayList<MapaEquivalenciaDisciplinaCursadaVO>(0);
		}
		return mapaEquivalenciaDisciplinaCursadaVOs;
	}
	public void setMapaEquivalenciaDisciplinaCursadaVOs(List<MapaEquivalenciaDisciplinaCursadaVO> mapaEquivalenciaDisciplinaCursadaVOs) {
		this.mapaEquivalenciaDisciplinaCursadaVOs = mapaEquivalenciaDisciplinaCursadaVOs;
	}
	public List<MapaEquivalenciaDisciplinaMatrizCurricularVO> getMapaEquivalenciaDisciplinaMatrizCurricularVOs() {
		if(mapaEquivalenciaDisciplinaMatrizCurricularVOs == null){
			mapaEquivalenciaDisciplinaMatrizCurricularVOs = new ArrayList<MapaEquivalenciaDisciplinaMatrizCurricularVO>(0);
		}
		return mapaEquivalenciaDisciplinaMatrizCurricularVOs;
	}
	public void setMapaEquivalenciaDisciplinaMatrizCurricularVOs(List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaGradeVOs) {
		this.mapaEquivalenciaDisciplinaMatrizCurricularVOs = mapaEquivalenciaDisciplinaGradeVOs;
	}
	public Integer getSequencia() {
		if(sequencia == null){
			sequencia = 0;
		}
		return sequencia;
	}
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
        
    public String getDisciplinaMatrizCurricular() {
        if (disciplinaMatrizCurricular == null) {
            disciplinaMatrizCurricular = "";
        }
        if (disciplinaMatrizCurricular.equals("")) {
            disciplinaMatrizCurricular = "";
            for (MapaEquivalenciaDisciplinaMatrizCurricularVO obj : getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
                if (!disciplinaMatrizCurricular.trim().isEmpty()) {
                    disciplinaMatrizCurricular += ", ";
                }
                disciplinaMatrizCurricular += obj.getDisciplinaVO().getCodigo() + " - " + obj.getDisciplinaVO().getNome();
            }
        }
        return disciplinaMatrizCurricular;
    }
	
	public void setDisciplinaMatrizCurricular(String disciplinaMatrizCurricular) {
		this.disciplinaMatrizCurricular = disciplinaMatrizCurricular;
	}
	
	public String getDisciplinaEquivalente() {
		if(disciplinaEquivalente == null || disciplinaEquivalente.equals("")){
			disciplinaEquivalente = "";
			for(MapaEquivalenciaDisciplinaCursadaVO obj: getMapaEquivalenciaDisciplinaCursadaVOs()){
				if(!disciplinaEquivalente.trim().isEmpty()){
					disciplinaEquivalente += ", ";
				}
				disciplinaEquivalente += obj.getDisciplinaVO().getCodigo()+" - "+obj.getDisciplinaVO().getNome();
			}
		}
		return disciplinaEquivalente;
	}
	
	public void setDisciplinaEquivalente(String disciplinaEquivalente) {
		this.disciplinaEquivalente = disciplinaEquivalente;
	}
	
	public TipoRegraPeriodoLetivoEnum getTipoRegraPeriodoLetivo() {
		if(tipoRegraPeriodoLetivo == null){
			tipoRegraPeriodoLetivo = TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ATUAL;
		}
		return tipoRegraPeriodoLetivo;
	}
	
	public void setTipoRegraPeriodoLetivo(TipoRegraPeriodoLetivoEnum tipoRegraPeriodoLetivo) {
		this.tipoRegraPeriodoLetivo = tipoRegraPeriodoLetivo;
	}
        
        public MapaEquivalenciaDisciplinaMatrizCurricularVO consultarObjMapaEquivalenciaDisciplinaMatrizCurricularVOPorCodigoDisciplina(Integer codigoDisciplina) {
            for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaMatriz : this.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
                if (mapaMatriz.getDisciplinaVO().getCodigo().equals(codigoDisciplina)) {
                    return mapaMatriz;
                }
            }
            return null;
        }

    /**
     * @deprecated utilizar método similar que fornecer carga horaria
     */
    public MapaEquivalenciaDisciplinaCursadaVO consultarObjMapaEquivalenciaDisciplinaCursadaVOPorCodigo(Integer codigoMapaEquivalenciaDisciplinaCursar) {
        for (MapaEquivalenciaDisciplinaCursadaVO mapa : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if (mapa.getCodigo().equals(codigoMapaEquivalenciaDisciplinaCursar)) {
                return mapa;
            }
        }
        return null;
    }

    /**
     * @return the mapaCumpridoPeloAluno
     */
    public Boolean getMapaCumpridoPeloAluno() {
        boolean mapaCumpridoPeloAluno = true;
        for (MapaEquivalenciaDisciplinaCursadaVO cursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if (!cursada.getDisciplinaAprovada() && !cursada.getHistorico().getReprovado()) {
                // se ao menos uma disciplina nao está como aprovada, significa que o mapa
                // nao está fechado.
                return false;
            }
        }
        return mapaCumpridoPeloAluno;
    }
    
    /**
     * Método retorna a carga horária a ser assumida para cada disciplinas da matriz
     * que foi cursada por meio outros disciplinas equivalentes. Trabalha olhando 
     * para o atributo histórico, que deve estar montado (carregado) quando este for
     * chamado. Isto por que no objeto histórico já existem informações sobre carga
     * horário e créditos devidamente montados. Haja vista, que estas informações podem
     * vir de diferentes fontes (gradedisciplina, gradecurriculargrupooptativadisciplina, gradecomposta)
     * @param disciplinaMatrizBaseObtencao
     * @return 
     */
    public Integer getCargaHorariaMapaEquivalenciaCumpridoPeloAluno(
            MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao) {
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.CARGA_HORARIA_GRADE)) {
            return disciplinaMatrizBaseObtencao.getHistorico().getCargaHorariaDisciplina();
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.MAIOR_CARGA_HORARIA)) {
            Integer maiorCH = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if (disciplinaCursada.getHistorico().getCargaHorariaDisciplina().compareTo(maiorCH) >= 0) {
                    maiorCH = disciplinaCursada.getHistorico().getCargaHorariaDisciplina();
                }
            }
            return maiorCH;
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.MEDIA_CARGA_HORARIA)) {
            Integer somaCH = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                somaCH = somaCH + disciplinaCursada.getHistorico().getCargaHorariaDisciplina();
            }
            Double mediaFinalCH = Uteis.arrendondarForcando2CadasDecimais(somaCH / this.getMapaEquivalenciaDisciplinaCursadaVOs().size());
            return mediaFinalCH.intValue();
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.SOMAR_CARGA_HORARIA)) {
            Integer somaCH = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                somaCH = somaCH + disciplinaCursada.getHistorico().getCargaHorariaDisciplina();
            }
            return somaCH;
        }
        return 0;
    }
    
    public Integer getCreditosMapaEquivalenciaCumpridoPeloAluno(
            MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao) {
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.CARGA_HORARIA_GRADE)) {
            return disciplinaMatrizBaseObtencao.getHistorico().getCreditoDisciplina();
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.MAIOR_CARGA_HORARIA)) {
            Integer maiorCredito = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if (disciplinaCursada.getHistorico().getCreditoDisciplina().compareTo(maiorCredito) >= 0) {
                    maiorCredito = disciplinaCursada.getHistorico().getCreditoDisciplina();
                }
            }
            return maiorCredito;
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.MEDIA_CARGA_HORARIA)) {
            Integer somaCr = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                somaCr =+ disciplinaCursada.getHistorico().getCreditoDisciplina();
            }
            Double mediaFinalCr = Uteis.arrendondarForcando2CadasDecimais(somaCr / this.getMapaEquivalenciaDisciplinaCursadaVOs().size());
            return mediaFinalCr.intValue();
        }
        if (this.getTipoRegraCargaHorariaEquivalencia().equals(TipoRegraCargaHorariaEquivalenciaEnum.SOMAR_CARGA_HORARIA)) {
            Integer somaCr = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                somaCr =+ disciplinaCursada.getHistorico().getCreditoDisciplina();
            }
            return somaCr;
        }        
        return 0;
    }
    
    public Double getFrequenciaMapaEquivalenciaCumpridoPeloAluno(MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao) {
        if (this.getTipoRegraFrequenciaEquivalencia().equals(TipoRegraFrequenciaEquivalenciaEnum.MAIOR_FREQUENCIA)) {
            Double maior = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if (disciplinaCursada.getHistorico().getFreguencia().compareTo(maior) >= 0) {
                    maior = disciplinaCursada.getHistorico().getFreguencia();
                }
            }
            return maior;
            
        }
        if (this.getTipoRegraFrequenciaEquivalencia().equals(TipoRegraFrequenciaEquivalenciaEnum.MEDIA_FREQUENCIA)) {
            Double soma = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                soma =+ disciplinaCursada.getHistorico().getFreguencia();
            }
            return Uteis.arrendondarForcando2CadasDecimais(soma / this.getMapaEquivalenciaDisciplinaCursadaVOs().size());
        }
        if (this.getTipoRegraFrequenciaEquivalencia().equals(TipoRegraFrequenciaEquivalenciaEnum.SOMAR_FREQUENCIA)) {
            Double soma = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                soma =+ disciplinaCursada.getHistorico().getFreguencia();
            }
            return soma;
        }
        if (this.getTipoRegraFrequenciaEquivalencia().equals(TipoRegraFrequenciaEquivalenciaEnum.FREQUENCIA_PROPORCIONAL)) {
            Integer somaFaltas = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                somaFaltas =+ disciplinaCursada.getHistorico().getTotalFalta();
            }
            Integer cargaHoraiaBase = disciplinaMatrizBaseObtencao.getHistorico().getCargaHorariaDisciplina();
            Double percentualFaltasProporcional = Uteis.arrendondarForcando2CadasDecimais((somaFaltas * 100) / cargaHoraiaBase);
            return 100 - percentualFaltasProporcional;
        }
        return 0.0;
    }

    public Double getMediaFinalMapaEquivalenciaCumpridoPeloAluno(MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao) throws Exception {
        if (this.getTipoRegraNotaEquivalencia().equals(TipoRegraNotaEquivalenciaEnum.MAIOR_NOTA)) {
            Double maior = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Double mediaProcessar = disciplinaCursada.getHistorico().getMediaFinal();
                if (mediaProcessar == null) {
                    mediaProcessar = 0.0;
                }
                if (mediaProcessar.compareTo(maior) >= 0) {
                    maior = mediaProcessar;
                }
            }
            return maior;
        }
        if (this.getTipoRegraNotaEquivalencia().equals(TipoRegraNotaEquivalenciaEnum.MEDIA_NOTA)) {
            Double soma = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Double mediaProcessar = disciplinaCursada.getHistorico().getMediaFinal();
                if (mediaProcessar == null) {
                    mediaProcessar = 0.0;
                }
                soma += mediaProcessar;
            }
            return Uteis.arrendondarForcando2CadasDecimais(soma / this.getMapaEquivalenciaDisciplinaCursadaVOs().size());
        }
        if (this.getTipoRegraNotaEquivalencia().equals(TipoRegraNotaEquivalenciaEnum.SOMAR_NOTA)) {
            Double soma = 0.0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Double mediaProcessar = disciplinaCursada.getHistorico().getMediaFinal();
                if (mediaProcessar == null) {
                    mediaProcessar = 0.0;
                }
                soma =+ mediaProcessar;
            }
            return soma;
        }
        if (this.getTipoRegraNotaEquivalencia().equals(TipoRegraNotaEquivalenciaEnum.FORMULA_CALCULO)) {
            String formulaCalculoMediaFinal = this.getFormulaCalculoNota();
            for (MapaEquivalenciaDisciplinaCursadaVO mapaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                String variavelNota = mapaCursada.getVariavelNota();
                formulaCalculoMediaFinal = formulaCalculoMediaFinal.replace(formulaCalculoMediaFinal, variavelNota);
            }
            Double mediaFinal = Uteis.realizarCalculoFormula(formulaCalculoMediaFinal);
            if (mediaFinal == null) {
                mediaFinal = 0.0;
            }
            return Uteis.arrendondarForcando2CadasDecimais(mediaFinal);
        }
        return 0.0;
    }
    
    public PeriodoLetivoVO getPeriodoLetivoCursadoMapaEquivalenciaCumpridoPeloAluno(MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao) {
        if (this.getTipoRegraPeriodoLetivo().equals(TipoRegraPeriodoLetivoEnum.PERIODO_MATRIZ_CURRICULAR)) {
            return disciplinaMatrizBaseObtencao.getHistorico().getPeriodoLetivoMatrizCurricular();
        }
        if (this.getTipoRegraPeriodoLetivo().equals(TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ATUAL)) { 
            Integer maiorAno = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                String anoMaisRecente = disciplinaCursada.getHistorico().getAnoHistorico();
                if (anoMaisRecente.equals("")) {
                    anoMaisRecente = "0";
                }
                Integer anoHistorico = Integer.parseInt(anoMaisRecente);
                if (anoHistorico.compareTo(maiorAno) >= 0) {
                    maiorAno = anoHistorico;
                }
            }
            // uma vez definido o maior ano, temos que avaliar o maior semestre deste ano
            Integer semestreMaior = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                String anoMaisRecente = disciplinaCursada.getHistorico().getAnoHistorico();
                if (anoMaisRecente.equals("")) {
                    anoMaisRecente = "0";
                }
                Integer anoHistorico = Integer.parseInt(anoMaisRecente);
                if (anoHistorico.equals(maiorAno)) {
                    // se estamos em um histórico do ano maior, temos que avaliar se o semestre
                    // deste historico também é o maior e registrá-lo para retornar 
                    String semestreMaisRecente = disciplinaCursada.getHistorico().getSemestreHistorico();
                    if (semestreMaisRecente.equals("")) {
                        semestreMaisRecente = "0";
                    }
                    Integer semestreHistorico = Integer.parseInt(semestreMaisRecente);
                    if (semestreHistorico.compareTo(semestreMaior) >= 0) {
                        semestreMaior = semestreHistorico;
                    }
                }
            }
            //uma vez definido o ano/semestre maior (ou seja, mais atual), iremos obter o período letivo 
            // do mesmo para retorná-lo
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                String anoHistoricoStr = disciplinaCursada.getHistorico().getAnoHistorico();
                if (anoHistoricoStr.equals("")) {
                    anoHistoricoStr = "0";
                }
                String semestreHistorioStr = disciplinaCursada.getHistorico().getSemestreHistorico();
                if (semestreHistorioStr.equals("")) {
                    semestreHistorioStr = "0";
                }
                Integer anoHistorico = Integer.parseInt(anoHistoricoStr);
                Integer semestreHistorico = Integer.parseInt(semestreHistorioStr);
                if ((anoHistorico.equals(maiorAno)) &&
                    (semestreHistorico.equals(semestreMaior))) {
                    // se entrarmos aqui é por que encontramos o historico mais atual,
                    // assim basta retornar o periodoLetivoCursado do mesmo
                    return disciplinaCursada.getHistorico().getPeriodoLetivoCursada();
                }
            }
        }
        if (this.getTipoRegraPeriodoLetivo().equals(TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ANTIGO)) {
            
            Integer menorAno = 9999999;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                if (disciplinaCursada.getHistorico().getAnoHistorico().equals("")) {
                    // nao ha ano registro para o historico, logo vamos assumir o mesmo
                    // como mais antigo
                    return disciplinaCursada.getHistorico().getPeriodoLetivoCursada();
                }
                Integer anoHistorico = Integer.parseInt(disciplinaCursada.getHistorico().getAnoHistorico());
                if (anoHistorico.compareTo(menorAno) <= 0) {
                    menorAno = anoHistorico;
                }
            }
            // uma vez definido o menor ano, temos que avaliar o menor semestre deste ano
            Integer semestreMenor = 9999;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = Integer.parseInt(disciplinaCursada.getHistorico().getAnoHistorico());
                if (anoHistorico.equals(menorAno)) {
                    // se estamos em um histórico do ano menor, temos que avaliar se o semestre
                    // deste historico também é o menor e registrá-lo para retornar 
                    if (!disciplinaCursada.getHistorico().getSemestreHistorico().equals("")) {
                        Integer semestreHistorico = Integer.parseInt(disciplinaCursada.getHistorico().getSemestreHistorico());
                        if (semestreHistorico.compareTo(semestreMenor) <= 0) {
                            semestreMenor = semestreHistorico;
                        }
                    } else {
                        semestreMenor = 0;
                    }
                }
            }
            //uma vez definido o ano/semestre menor (ou seja, mais antigo), iremos obter o período letivo 
            // do mesmo para retorná-lo
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                String anoHistoricoStr = disciplinaCursada.getHistorico().getAnoHistorico();
                if (anoHistoricoStr.equals("")) {
                    anoHistoricoStr = "0";
                }
                String semestreHistoricoStr = disciplinaCursada.getHistorico().getSemestreHistorico();
                if (semestreHistoricoStr.equals("")) {
                    semestreHistoricoStr = "0";
                }
                Integer anoHistorico = Integer.parseInt(anoHistoricoStr);
                Integer semestreHistorico = Integer.parseInt(semestreHistoricoStr);
                if ((anoHistorico.equals(menorAno)) &&
                    (semestreHistorico.equals(semestreMenor))) {
                    // se entrarmos aqui é por que encontramos o historico mais antigo,
                    // assim basta retornar o periodoLetivoCursado do mesmo
                    return disciplinaCursada.getHistorico().getPeriodoLetivoCursada();
                }
            }
        }
        return null;
    }
    
    public String getAnoMapaEquivalenciaCumpridoPeloAluno(
            MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao,
            String anoResolucaoMapaStr) {
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.ANO_SEMESTRE_ATUAL)) {
            return anoResolucaoMapaStr;
        }
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.MAIOR_ANO_SEMESTRE)) {
            Integer maior = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.compareTo(maior) >= 0) {
                    maior = anoHistorico;
                }
            }            
            return maior == 0 ? "" : String.valueOf(maior);
        }
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.MENOR_ANO_SEMESTRE)) {
            Integer menor = 99999999;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.compareTo(menor) <= 0) {
                    menor = anoHistorico;
                }
            }
            return String.valueOf(menor);
        }
        return "";
    }
    
    public String getSemestreMapaEquivalenciaCumpridoPeloAluno(
            MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatrizBaseObtencao,
            String semestreResolucaoMapa) {
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.ANO_SEMESTRE_ATUAL)) {
            return semestreResolucaoMapa;
        }
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.MAIOR_ANO_SEMESTRE)) {
            Integer maior = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.compareTo(maior) >= 0) {
                    maior = anoHistorico;
                }
            }
            // uma vez definido o maior ano, temos que avaliar o maior semestre deste ano
            Integer semestreMaior = 0;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.equals(maior)) {
                    // se estamos em um histórico do ano maior, temos que avaliar se o semestre
                    // deste historico também é o maior e registrá-lo para retornar 
                    Integer semestreHistorico = disciplinaCursada.getHistorico().getSemestreHistoricoInteiro();
                    if (semestreHistorico.compareTo(semestreMaior) >= 0) {
                        semestreMaior = semestreHistorico;
                    }
                }
            }
            return semestreMaior == 0 ? "" : String.valueOf(semestreMaior);
        }
        if (this.getTipoRegraAnoSemestreEquivalencia().equals(TipoRegraAnoSemestreEquivalenciaEnum.MENOR_ANO_SEMESTRE)) {
            Integer menor = 99999999;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.compareTo(menor) <= 0) {
                    menor = anoHistorico;
                }
            }
            // uma vez definido o menor ano, temos que avaliar o menor semestre deste ano
            Integer semestreMenor = 999;
            for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
                Integer anoHistorico = disciplinaCursada.getHistorico().getAnoHistoricoInteiro();
                if (anoHistorico.equals(menor)) {
                    // se estamos em um histórico do ano menor, temos que avaliar se o semestre
                    // deste historico também é o menor e registrá-lo para retornar 
                    Integer semestreHistorico = disciplinaCursada.getHistorico().getSemestreHistoricoInteiro();
                    if (semestreHistorico.compareTo(semestreMenor) <= 0) {
                        semestreMenor = semestreHistorico;
                    }
                }
            }
            return semestreMenor == 999 ? "" : String.valueOf(semestreMenor); 
        }
        return "";
    }
	public StatusAtivoInativoEnum getSituacao() {
		if (situacao == null) {
			situacao = StatusAtivoInativoEnum.ATIVO;
		}
		return situacao;
	}
	public void setSituacao(StatusAtivoInativoEnum situacao) {
		this.situacao = situacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MapaEquivalenciaDisciplinaVO){
			return this.toString().equals(obj.toString());
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		Ordenacao.ordenarLista(getMapaEquivalenciaDisciplinaCursadaVOs(), "ordenacao");
		for(MapaEquivalenciaDisciplinaCursadaVO cursada: getMapaEquivalenciaDisciplinaCursadaVOs()){
			str.append(cursada.getOrdenacao());
		}
		Ordenacao.ordenarLista(getMapaEquivalenciaDisciplinaMatrizCurricularVOs(), "ordenacao");
		for(MapaEquivalenciaDisciplinaMatrizCurricularVO matriz: getMapaEquivalenciaDisciplinaMatrizCurricularVOs()){
			str.append(matriz.getOrdenacao());
		}		
		return str.toString();
	}
    public MapaEquivalenciaDisciplinaCursadaVO consultarObjMapaEquivalenciaDisciplinaCursadaVOPorDisciplinaECargaHoraria(
            Integer codigoDisciplinaCursar, Integer cargaHoraria) {
        for (MapaEquivalenciaDisciplinaCursadaVO mapa : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if ((mapa.getDisciplinaVO().getCodigo().equals(codigoDisciplinaCursar)) &&
                (mapa.getCargaHoraria().equals(cargaHoraria)))     {
                return mapa;
            }
        }
        return null;
    }
	public Boolean getEquivalenciaPorIsencao() {
		if (equivalenciaPorIsencao == null) {
			equivalenciaPorIsencao = false;
		}
		return equivalenciaPorIsencao;
	}
	
	public void setEquivalenciaPorIsencao(Boolean equivalenciaPorIsencao) {
		this.equivalenciaPorIsencao = equivalenciaPorIsencao;
	}
	
	public boolean getIsAtivo(){
		return getSituacao().equals(StatusAtivoInativoEnum.ATIVO);
	}
	
	public boolean getIsInativo(){
		return getSituacao().equals(StatusAtivoInativoEnum.INATIVO);
	}
	
	public Date getDataInativacao() {		
		return dataInativacao;
	}
	
	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}
	
	public UsuarioVO getUsuarioInativacao() {
		if (usuarioInativacao == null) {
			usuarioInativacao = new UsuarioVO();
		}
		return usuarioInativacao;
	}
	public void setUsuarioInativacao(UsuarioVO usuarioInativacao) {
		this.usuarioInativacao = usuarioInativacao;
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
	public UsuarioVO getUsuarioCadastro() {
		if (usuarioCadastro == null) {
			usuarioCadastro = new UsuarioVO();
		}
		return usuarioCadastro;
	}
	public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}
	
	
    /**
     * Determina se dois mapas são equivalenciates, comparando suas disciplinas (código da disciplnia)
     * e cargas horárias. Tanto a serem cursadas como a serem aprovadas por equivalencia.
     * @return 
     */
    public boolean equivalente(MapaEquivalenciaDisciplinaVO mapaVerificar) {
        if (this.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size() != mapaVerificar.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size()) {
            return false;
        }
        if (this.getMapaEquivalenciaDisciplinaCursadaVOs().size() != mapaVerificar.getMapaEquivalenciaDisciplinaCursadaVOs().size()) {
            return false;
        }
        for (MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaMatriz : this.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
            MapaEquivalenciaDisciplinaMatrizCurricularVO disciplinaEquivalenteMatrizEncontrada = mapaVerificar.consultarObjMapaEquivalenciaDisciplinaMatrizCurricularVOPorCodigoDisciplina(disciplinaMatriz.getDisciplinaVO().getCodigo());
            if (disciplinaEquivalenteMatrizEncontrada == null) {
                // se entrarmos aqui é por que no mapaVerificar nao existe uma disciplina da Matriz Equivalente,
                // considerando o mesmo código de disciplina existente neste objeto (neste mapa)
                return false;
            }
        }
        
        for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            MapaEquivalenciaDisciplinaCursadaVO disciplinaEquivalenteEncontrada = mapaVerificar.consultarObjMapaEquivalenciaDisciplinaCursadaVOPorDisciplinaECargaHoraria(disciplinaCursada.getDisciplinaVO().getCodigo(), disciplinaCursada.getCargaHoraria());
            if (disciplinaEquivalenteEncontrada == null) {
                // se entrarmos aqui é por que no mapaVerificar nao existe uma disciplina Equivalente,
                // considerando o mesmo código de disciplina e carga horária existente neste objeto (neste mapa)
                return false;
            }
        }
        return true;
    }
    
    /**
     * Método responsável por verificar em uma lista de mapas, se algum deles é compátivel com este mapa de equivalencia (this).
     * Este tipo de recurso é útil na transfernecia de matriz curricular, por exemplo, onde precisa-se obter um mapa equivalencia
     * em uma nova matriz curricular.
     * @param listaMapasVerificar
     * @return 
     */
    public MapaEquivalenciaDisciplinaVO obterMapaEquivalenteDisciplina(List<MapaEquivalenciaDisciplinaVO> listaMapasVerificar) {
        for (MapaEquivalenciaDisciplinaVO mapaVerificar : listaMapasVerificar) {
            if (this.equivalente(mapaVerificar)) {
                return mapaVerificar;
            }
        }
        return null; 
    }
    
	public List<Integer> getMapaEquivalenciaCodigoDisciplinaMatrizCurricularVOs() {
		return getMapaEquivalenciaDisciplinaMatrizCurricularVOs().stream()
				.map(MapaEquivalenciaDisciplinaMatrizCurricularVO::getDisciplinaVO).filter(Uteis::isAtributoPreenchido)
				.map(DisciplinaVO::getCodigo).distinct().collect(toList());
	}
	
	// Metodo responsavel para verificar se existe alguma disciplina reprovada na regra de equivalencia 1:N
	public Boolean getVerificarExistenciaDiscilinaReprovada() {
        boolean disciplinaReprovada = false;
        for (MapaEquivalenciaDisciplinaCursadaVO cursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if (!cursada.getDisciplinaAprovada() && cursada.getHistorico().getReprovado()) {
                return true;
            }
        }
        return disciplinaReprovada;
    }
	
	public Boolean getVerificarDiscilinaEquivalenteReprovada() {
        boolean disciplinaReprovada = false;
        int totalDisciplinaReprovada = 0;
        for (MapaEquivalenciaDisciplinaCursadaVO cursada : this.getMapaEquivalenciaDisciplinaCursadaVOs()) {
            if (!cursada.getDisciplinaAprovada() && cursada.getHistorico().getReprovado()) {
               totalDisciplinaReprovada++;
            }
        }
        if (Uteis.isAtributoPreenchido(this.getMapaEquivalenciaDisciplinaCursadaVOs()) && this.getMapaEquivalenciaDisciplinaCursadaVOs().size() == totalDisciplinaReprovada) {
        	return true;
		}
        return disciplinaReprovada;
    }
}
