package negocio.comuns.academico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade Historico. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "historico")
public class HistoricoVO extends SuperVO implements Cloneable {
    public static final long serialVersionUID = 1L;

    //**************************************************
    //**************************************************
    //**************************************************
    // * DADOS BASICOS DO HISTORICOS
    //**************************************************
    //**************************************************
    //**************************************************
    private Integer codigo;
    private String tipoHistorico;
    private String situacao;
    private Double freguencia;
    private Date dataRegistro;
    private Double mediaFinal;
    private UsuarioVO responsavel;
    private Boolean editavel;
    private List listaHistorico;
    private String nomePaiAluno;
    private String nomeMaeAluno;
    private String estabelecimentoEnsinoMedio;
    private String anoConclusaoEnsinoMedio;
    private String mediaFinalTexto;
    private Boolean notificarSolicitacaoReposicao;
    //Atributo usado para ordenar as disciplinas no visualização de notas do aluno(Visão Aluno). AS disciplinas serão ordenadas pela data de programação de aula
    private Date data;
    //Atributo usado para mostrar período da aula e nome do professor que ministrou, na visão do aluno minhas notas (Pós-Graduação)
    private Date dataPrimeiraAula;
    private String nomeProfessor;
    private String titulacaoProfessor;
    private String titulacaoProfessor_Apresentar;
    private String instituicao;
    private String cidade;
    private String estado;
    private CidadeVO cidadeVO;
    private Boolean ocultarDataAula;
    private String dataInicioModulo;
    
    /**
     * ATRIBUTO TRASIENT, UTILIZADO NA TRANSFERENCIA DE MATRIZ CURRICULAR
     * PARA INDICAR QUE NAO DEVEM SER GERADOS HISTÓRICOS AUTOMATICAMENTE
     * PARA AS DISCIPLINAS A SEREM CURSADAS POR EQUIVALENCIA (O QUE É FEITO)
     * AO FINAL DO MÉTODO DE INCLUIR/ALTERAR. ISTO POR QUE SE ESTES HISTÓRICOS
     * FOREM GERADOS DURANTE A TRANSFERENCIA, PODE IMPEDIR NOVOS APROVEITADOS
     * DURANTE O PROCESSAMENTO.
     */
    private Boolean naoGerarHistoricoDisciplinasCursadasPorEquivalencia;

    //**************************************************
    //**************************************************
    //**************************************************
    // * REFERENCIAS A ORIGEM ACADEMICA DO HISTORICO - MATRICULA, MATRICULAPERIODO, PERIODO, GRADEDISCIPLINA, GRUPOOPTATIVAS
    //**************************************************
    //**************************************************
    //**************************************************
    private MatriculaVO matricula;
    private MatriculaPeriodoVO matriculaPeriodo;
    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina;
    private DisciplinaVO disciplina;
    private DisciplinaPreRequisitoVO preRequisito;
    private ConfiguracaoAcademicoVO configuracaoAcademico;
    /**
     * Este corresponde a matriz curricular em que se encontra a matricula do aluno
     */
    private GradeCurricularVO matrizCurricular;
    /**
     * Este corresponde ao periodo letivo em que se encontra a disciplina na matriz curricular, 
     * caso seja uma disciplina equivalente deve ser mantido neste o período da matriz na qual
     * o aluno estudou (ou seja o periodo letivo correspondente a gradeDisciplina fora da grade que
     * o aluno estudou.
     */
    private PeriodoLetivoVO periodoLetivoMatrizCurricular;
    
    /**
     * Este mantem em qual periodo o aluno cursou a disciplina independente de qual periodo letivo é a disciplina, 
     * ou seja se a disciplina for do 3ª periodo e o aluno curso no 2º periodo então será registrado aqui o 2º período
     * Se for uma disciplina equivalente, então este periodo será preenchido que com o períodoLetivo da matriz 
     * do aluno (oficial que ele estuda) conforme regras definida no mapaDeEquivalencia (que diz que deve pegar o primeiro,
     * ou o último, por exemplo).
     */
    private PeriodoLetivoVO periodoLetivoCursada;
    private GradeDisciplinaVO gradeDisciplinaVO;
    
    private TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatricula;
    private Boolean historicoGeradoAPartirTransferenciaMatrizCurricular;
    private String observacaoTransferenciaMatrizCurricular;
    
    /**
     * Indicar se a disciplina é optativa é ainda refere-se a uma optativa
     * de um grupo de optativas. Isto é importante, pois neste caso a mesma
     * não veio da entidade GradeDisciplina, mas sim de GradeCurricularGrupoOptativaDisciplina
     */
    private Boolean disciplinaReferenteAUmGrupoOptativa;
    private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO; 

    
    //**************************************************
    //**************************************************
    //**************************************************
    // * ATRIBUTOS DE CONTROLE SOBRE PROPRIEDADES E SITUAÇÕES DO HISTORICO (DE ORIGEM DE GERAÇÃO E OUTRAS)
    //**************************************************
    //**************************************************
    //**************************************************
    private boolean criadoPorNovaDisciplinaAproveitada = false;
    private Boolean realizouTransferenciaTurno;
    private Integer transferenciaEntradaDisciplinasAproveitadas;
    private Integer disciplinasAproveitadas;
    
    /**
     * Indica que é o histórico de uma disciplina composta, portanto, será cursada por meio
     * de disciplinas de sua composicao. Será TRUE para a disciplina composta
     * e será FALSE para as disciplinas normais e para as disciplinas que
     * fazem parte da composicao.
     */
    private Boolean historicoDisciplinaComposta;
    
    /**
     * Indica que o histórico é de uma disciplina que faz parte de uma composicao.
     * Ou seja, o aluno irá cursá-la com o intuito de ser aprovado em uma disciplina
     * mãe, que define a composicao. Vale ressaltar que tanto uma disciplina normal
     * quanto uma disciplina de um Grupo de Optativa pode ser composta.
     */
    private Boolean historicoDisciplinaFazParteComposicao;
    
    /**
     * Indica a qual a disciplina da composicao este historico se refere. Este atributo
     * será montado somente o atributo disciplinaComposta estiver marcado.
     * É importante destacar que este atributo poderá ser definido com base na composicao
     * de uma disciplina de um grupo de optativa. Ou seja, pode ser uma historico
     * de uma discciplina de uma grupo de optativa, que foi definida para ser
     * estudada por meio de uma composição.
     */
    private GradeDisciplinaCompostaVO gradeDisciplinaComposta;
    
    private Boolean disciplinaIgualGradeAnterior;
    private Boolean permiteMarcarReposicao;
    private String anoHistorico;
    private String semestreHistorico;
    private Integer cargaHorariaAproveitamentoDisciplina;
    /**
     * Carga horaria que de fato o aluno cursou da disciplina. Lembrando,
     * que muitas vezes o aluno pode cursar mais horas (ou menos) que o
     * projeto para a disciplina.
     */
    private Integer cargaHorariaCursada;
    private Integer cargaHorariaDisciplina;
    private Integer creditoDisciplina;
    /**
     * Este marca se a disciplina é optativa
     */
    private Boolean historicoDisciplinaOptativa;
    /**
     * Este marca se a disciplina deve ser considerada como atividade complementar, 
     * porém, a sua carga horária não deve ser contabilizada na integração do 
     * periodo letivo pois a mesma será contabilizada no calculo da atividade complementar
     */
    private Boolean historicoDisciplinaAtividadeComplementar;
    /**
     * Utilizados quando cadastrados apartir da tela de inclusão de disciplina fora da grade
     */
    private Boolean historicoDisciplinaForaGrade;
    
    /**
     * Caso esteja true significa que o usuário está cursando este histórico, em uma grade
     * anterior (da grade atual do aluno) para uma disciplina que existe de forma identifca
     * na grade destino. Mas como o aluno estava cursando a mesma, então não tinhamos como
     * migrar o histórico fazendo o aproveitamento academico do mesmo. Assim, como este boolean
     * true, teremos que ao gravar o histórico como aprovado, iremos automaticamente, aprovar
     * o aluno na grade destino (ou reprovar). Ou seja, o resultado final deste histórico
     * irá ser refletido no histórico da mesma disciplina na gradeDestino. Se true é por que
     * existe na gradeDestino, um histórico equivalente a disciplina deste histórico.
     */
    private Boolean historicoCursandoPorCorrespondenciaAposTransferencia;
    
    private String nomeDisciplina;
    /**
     * Apenas para dizer a origem da disciplina no histórico quando a mesma for aproveitada seja por aproveitamento, concessão de crédito ou concessão de carga horária
     * Este deve ser marcada quando a origem da disciplina vier da tela de Aproveitamento de Disciplina
     */
    private Boolean historicoDisciplinaAproveitada;
    
    /**
     * Quando o aluno estiver estudando uma disciplina equivalente este deve ser marcado como true para determinar a disciplina da matriz curricular, 
     *  ou seja o sistema deverá apresentar este no histórico somente quando as suas equivalencia forem cursadas.
     */
    private Boolean historicoPorEquivalencia;
    
    /**
     * Este deve ser marcado como true quando esta disciplina  for a equivalente da grade do aluno
     * Este deve ser apresentado no boletim acadêmico
     */
    private Boolean historicoEquivalente;
    
    /**
     * Caso esta seja uma disciplina que esteja sendo estuda / cumprida por meio de um
     * mapa de equivalencia, este atributo irá registrar em qual mapa de equivalencia
     * a mesma está envolvida.
     */
    private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina;
    
    /**
     * Caso esta seja uma disciplina que está sendo estudada pelo aluno com intuito de 
     * fechar (ser aprovados em todas as disciplinas que o mapa determina que ele tem que estudar)
     * um MapaEquivalencia, então neste atributo será registrar uma (e somente uma) das disciplinas
     * que o aluno terá que estudar. Haja vista, que cada disciplina que o aluno precisa estudar
     * deve existir um registro em MatriculaPeriodoTurmaDisciplina.
     */
    private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada;
    
    /**
     * Caso esta seja uma disciplina que o aluno esteja cumprindo por meio de um mapa de equivalencia
     * então, neste atributo ficará a referência sobre a qual disciplina da matriz, este histórico
     * refere-se. A disciplina referenciada pelo MapaEquivalenciaDisciplinaMatrizCurricularVO
     * é um das disciplinas que o aluno será/está aprovado por ter cumprido os requesitos do mapa
     * de equivalencia.
     */
    private MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricular;
    
    
    /**
     * Campo que mantém o vinculo com historico equivalente
     * @deprecated na versão 5.0 - passou a ser adotado o mapa de equivalencia N:M
     */
    private HistoricoEquivalenteVO historicoEquivalenteVO;    
    
    /**
     * Campos abaixo defini as faltas do bimestre estes são utilizados para curso de educação infantil, ensino fundamental e ensino médio,
     * Este deve ser alimentado através do registro de aula.
     */
    private Integer faltaPrimeiroBimestre;
    private Integer faltaSegundoBimestre;
    private Integer faltaTerceiroBimestre;
    private Integer faltaQuartoBimestre;
    /**
     * Este é utilizado para todos os niveis educacionais e deve ser registrado o total de faltas que o aluno possui
     */
    private Integer totalFalta;
    private Boolean isentarMediaFinal;
    
    /**
     * Campo que mantém uma observação no histórico, observao que é listada
     * em alguns layout´s de histórico
     */
    private String observacao;
    
    /**
     * Indica que as notas do historico vieram da tela de Criterio Avaliacao Aluno
     * 
     * 
     * 
     */
    private Boolean historicoCriterioAvaliacaoAluno;
    private CriterioAvaliacaoVO criterioAvaliacao;
    
    //**************************************************
    // * ATRIBUTOS REFERENTES AS NOTAS
    //**************************************************

    /**
     * Controla Nota Conceito Importação
     */
    private Boolean utilizaNotaFinalConceito;
    private String notaFinalConceito;
    
    private List<NotaVO> listaNotas;
    private Double nota1;
    private Boolean nota1Lancada;
    private Double nota2;
    private Boolean nota2Lancada;
    private Double nota3;
    private Boolean nota3Lancada;
    private Double nota4;
    private Boolean nota4Lancada;
    private Double nota5;
    private Boolean nota5Lancada;
    private Double nota6;
    private Boolean nota6Lancada;
    private Double nota7;
    private Boolean nota7Lancada;
    private Double nota8;
    private Boolean nota8Lancada;
    private Double nota9;
    private Boolean nota9Lancada;
    private Double nota10;
    private Boolean nota10Lancada;
    private Double nota11;
    private Boolean nota11Lancada;
    private Double nota12;
    private Boolean nota12Lancada;
    private Double nota13;
    private Boolean nota13Lancada;
    private String titulo1;
    private String titulo2;
    private String titulo3;
    private String titulo4;
    private String titulo5;
    private String titulo6;
    private String titulo7;
    private String titulo8;
    private String titulo9;
    private String titulo10;
    private String titulo11;
    private String titulo12;
    private String titulo13;
    private Double nota14;
    private Boolean nota14Lancada;
    private String titulo14;
    private Double nota15;
    private Boolean nota15Lancada;
    private String titulo15;
    private Double nota16;
    private Boolean nota16Lancada;
    private String titulo16;
    private Double nota17;
    private Boolean nota17Lancada;
    private String titulo17;
    private Double nota18;
    private Boolean nota18Lancada;
    private String titulo18;
    private Double nota19;
    private Boolean nota19Lancada;
    private String titulo19;
    private Double nota20;
    private Boolean nota20Lancada;
    private String titulo20;
    private Double nota21;
    private Boolean nota21Lancada;
    private String titulo21;
    private Double nota22;
    private Boolean nota22Lancada;
    private String titulo22;
    private Double nota23;
    private Boolean nota23Lancada;
    private String titulo23;
    private Double nota24;
    private Boolean nota24Lancada;
    private String titulo24;
    private Double nota25;
    private Boolean nota25Lancada;
    private String titulo25;
    private Double nota26;
    private Boolean nota26Lancada;
    private String titulo26;
    private Double nota27;
    private Boolean nota27Lancada;
    private String titulo27;
    private Double nota28;
    private Boolean nota28Lancada;
    private String titulo28;
    private Double nota29;
    private Boolean nota29Lancada;
    private String titulo29;
    private Double nota30;
    private Boolean nota30Lancada;
    private String titulo30;
    //Atributos usados para manutenção de tela
    private String nota1Texto;
    private String nota2Texto;
    private String nota3Texto;
    private String nota4Texto;
    private String nota5Texto;
    private String nota6Texto;
    private String nota7Texto;
    private String nota8Texto;
    private String nota9Texto;
    private String nota10Texto;
    private String nota11Texto;
    private String nota12Texto;
    private String nota13Texto;
    
    private String nota14Texto;
    private String nota15Texto;
    private String nota16Texto;
    private String nota17Texto;
    private String nota18Texto;
    private String nota19Texto;
    private String nota20Texto;
    private String nota21Texto;
    private String nota22Texto;
    private String nota23Texto;
    private String nota24Texto;
    private String nota25Texto;
    private String nota26Texto;
    private String nota27Texto;
    private String nota28Texto;
    private String nota29Texto;
    private String nota30Texto;
    
    /**
     * Atributos que mantem notas de conceito 
     */
    private ConfiguracaoAcademicoNotaConceitoVO nota1Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota2Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota3Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota4Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota5Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota6Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota7Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota8Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota9Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota10Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota11Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota12Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota13Conceito;
    
    private ConfiguracaoAcademicoNotaConceitoVO nota14Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota15Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota16Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota17Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota18Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota19Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota20Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota21Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota22Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota23Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota24Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota25Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota26Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota27Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota28Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota29Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO nota30Conceito;
    private ConfiguracaoAcademicoNotaConceitoVO mediaFinalConceito;
    
    
    /**
     * ATRIBUTOS TRANSIENT´S UTILIZADOS NO MOMENTO DE CALCULAR A MEDIA
     * / SITUACAO FINAL DE UM ALUNO. SÃO VARIAVEIS QUE PODEM SER REFERENCIADAS
     * NA FORMULA DE CALCULO DE CADA NOTA....
     */
    private Integer nrAdvertencias1Bimestre;
    private Integer nrAdvertencias2Bimestre;
    private Integer nrAdvertencias3Bimestre;
    private Integer nrAdvertencias4Bimestre;
    private Integer nrAdvertenciasPeriodoLetivo;
    private Integer nrSuspensoes1Bimestre;
    private Integer nrSuspensoes2Bimestre;
    private Integer nrSuspensoes3Bimestre;
    private Integer nrSuspensoes4Bimestre;
    private Integer nrSuspensoesPeriodoLetivo;
    
    private String erroNotaSubstitutiva;
    
    /**
     * Atributo transiente utilizado no relatório do ensino fundamental e médio
     */
    private Integer totalDiaLetivoAno;
    
    /*
     * Transiente Utilizado Boletim
     */
    private Boolean parteDiversificada;
    /*
     * Atributo Mantém as Notas do aluno para controle de recuperação
     */
    private List<HistoricoNotaVO> historicoNotaVOs;
    private CalendarioLancamentoNotaVO calendarioLancamentoNotaVO;
    
    /**
     * Transient Trancamento
     */
    private Boolean realizarAlteracaoSituacaoHistorico;
    /**
     * Transient Lançamento de nota
     */
    private Boolean historicoAlterado;
    
    /**
     * Transient Lançamento de nota
     */
    private Boolean historicoSalvo;
    
    /**
     * Transient Lançamento de nota
     */
    private Boolean historicoCalculado;
    /**
     * Transient Lançamento de nota
     */
    private String situacaoHistoricoDisciplinaComposta;
    private Double frequenciaTeorica;
    private Double frequenciaPratica;
    private Integer numeroAgrupamentoEquivalenciaDisciplina;
    private Double nota31;
    private Boolean nota31Lancada;
    private String nota31Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota31Conceito;
    
    private Double nota32;
    private Boolean nota32Lancada;
    private String nota32Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota32Conceito;
    
    private Double nota33;
    private Boolean nota33Lancada;
    private String nota33Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota33Conceito;
    
    private Double nota34;
    private Boolean nota34Lancada;
    private String nota34Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota34Conceito;
    
    private Double nota35;
    private Boolean nota35Lancada;
    private String nota35Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota35Conceito;
    
    private Double nota36;
    private Boolean nota36Lancada;
    private String nota36Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota36Conceito;
    
    private Double nota37;
    private Boolean nota37Lancada;
    private String nota37Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota37Conceito;
    
    private Double nota38;
    private Boolean nota38Lancada;
    private String nota38Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota38Conceito;
    
    private Double nota39;
    private Boolean nota39Lancada;
    private String nota39Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota39Conceito;
    
    private Double nota40;
    private Boolean nota40Lancada;
    private String nota40Texto;
    private ConfiguracaoAcademicoNotaConceitoVO nota40Conceito;
    private Boolean desistiuEquivalencia;
    
    /**
     * Usado para histórico aprovados por aproveitamento, onde ao marcado irá apresentar no histórico e no relatório matriz Curricular do Aluno 
     * a situaçãoo "Aprovado" ao inves de "Aprovado Por Aproveitamento"
     */
    private Boolean apresentarAprovadoHistorico;

    private Boolean emRecuperacao;
    
    // Transient 
    // utilizado para validar existencia histórico afim de eliminar duplicidade caso haja muitas transferencias de ida e volta na mesma matriz
    private Boolean verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade;
    private Boolean ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha;
    
    private Date dataInicioAula;
    private Date dataFimAula;
    
    private Date dateDataInicioAula;
    
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota1VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota2VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota3VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota4VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota5VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota6VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota7VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota8VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota9VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota10VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota11VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota12VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota13VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota14VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota15VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota16VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota17VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota18VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota19VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota20VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota21VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota22VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota23VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota24VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota25VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota26VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota27VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota28VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota29VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota30VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota31VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota32VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota33VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota34VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota35VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota36VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota37VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota38VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota39VOs;
    private List<HistoricoNotaParcialVO> historicoNotaParcialNota40VOs;
    
    private List<HistoricoNotaParcialVO> historicoNotaParcialNotaVOs;
    
    //atributo transiente responsavel por definir o tamanho da coluna de notas 
    // em minhas notas na visao do aluno 
    private String widthNotasMinhasNotasVisaoALuno;
    
    //atributo transiente responsavel por definir a carga horaria exigida e cumprida na tela  
    // em minhas notas na visao adm 
    private Integer totalCargaHorariaPeriodoLetivoCumprida;
    private Integer totalCargaHorariaPeriodoLetivoExigida;
    private Integer totalDisciplinasPeriodoLetivo;
    private Integer totalDisciplinasCursadasPeriodoLetivo;
    private String anoBimestreConclusaoDisciplina;
    private Integer bimestre;

    /**
     * transient
     * Usado para marcar um historico aprovado por aproveitamento, usando para validar mapa equivalencia
     *
     */
    private Boolean historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo;
    
    
    /**
     * Construtor padrão da classe <code>Historico</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public HistoricoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>HistoricoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public static void validarDados(HistoricoVO obj) throws ConsistirException {
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Histórico) deve ser informado.");
        }
        if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
            throw new ConsistirException("O campo MATRÍCULA (Histórico) deve ser informado.");
        }
        if ((obj.getTipoHistorico() == null) || (obj.getTipoHistorico().equals(""))) {
            throw new ConsistirException("O campo TIPO HISTÓRICO (Histórico) deve ser informado.");
        }
        if ((obj.getSituacao() == null) || (obj.getSituacao().equals(""))) {
            throw new ConsistirException("O campo SITUAÇÃO (Histórico) deve ser informado.");
        }
        if (obj.getMatrizCurricular().getCodigo().equals(0)) {
            throw new ConsistirException("O campo MATRIZ CURRICULAR (Histórico) deve ser informado. Todo histórico deve estar vinculado a uma Matriz Curricular, na qual o aluno está atualmente matriculado.");
        }
        if ((!obj.getHistoricoDisciplinaForaGrade()) && (!obj.getHistoricoDisciplinaFazParteComposicao())) {
            if ((obj.getGradeDisciplinaVO().getCodigo().equals(0)) &&
                (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0))) {
                throw new ConsistirException("Não foi definida uma ORIGEM PARA A DISCIPLINA do Histórico (GradeDisciplina ou GradeCurricularGrupoOptativaDisciplina - Histórico).");
            }
        }
        if (obj.getDisciplinaReferenteAUmGrupoOptativa()) {
            if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
                throw new ConsistirException("Disciplina definida como referente a um GRUPO DE OPTATIVA, porém não foi definido UMA DISCIPLINA DE UM GRUPO DE OPTATIVAS.");
            }
        }
        if (obj.getHistoricoDisciplinaComposta()) {
            if (obj.getHistoricoDisciplinaFazParteComposicao()) {
                throw new ConsistirException("Histórico para uma Disciplina Composta, logo o mesmo não pode ser definido como FAZENDO PARTE DE UMA COMPOSIÇÃO (Esta opção deve ser marcada, somente para as disciplinas que fazem parte da composição.");
            }
        }
        if (obj.getHistoricoDisciplinaFazParteComposicao()) {
             if (obj.getHistoricoDisciplinaComposta()) {
                throw new ConsistirException("Histórico para uma Disciplina que faz Parte de uma Composição, logo o mesmo não pode ser definido como COMPOSTA (Esta opção deve ser marcada, somente para a disciplina COMPOSTA.");
             }
             if (obj.getGradeDisciplinaComposta().getCodigo().equals(0)) {
                throw new ConsistirException("Histórico para uma Disciplina que faz Parte de uma Composição, logo a referência para uma COMPOSIÇÃO (Grade Disciplina Composta) deve ser informada.");
             }
        }
        if (obj.getHistoricoEquivalente()) {
            // DisciplinaEquivale é true quando esta disciplina for uma disciplina de outra MatrizCurricular/curso 
            // que o aluno tem que cursar (dentro de uma mapa de equivalencia - MapaEquivalenciaDisciplinaCursada) 
            // com intuito de ser aprovado em outras disciplinas de sua matriz/curso. 
            // Logo, a mesma deve estar com dentro de uma MapaEquivalencia
            // Este deve ser apresentado no boletim acadêmico. Quando esta for true, significa que haverá
            // outras disciplinas da matriz, que estão com disciplinaPorEquivalencia 
            if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(0)) {
                throw new ConsistirException("Disciplina definida como EQUIVALENTE (disciplina cursada em um mapa de equivalência), porém não foi definido UMA DISCIPLINA A SER CURSADA DO MAPA DE EQUIVALÊNCIA.");
            }
        } else {
            if (obj.getHistoricoPorEquivalencia()) {
                if (obj.getMapaEquivalenciaDisciplinaMatrizCurricular().getCodigo().equals(0)) {
                    throw new ConsistirException("Disciplina definida para ser cumprida por EQUIVALENCIA (disciplina realizada por meio de um mapa de equivalência), porém não foi definido UMA DISCIPLINA DA MATRIZ CURRICULAR CORRESPONDENTE.");
                }
            }
        }
                                
        if (obj.getDataRegistro() == null) {
            throw new ConsistirException("O campo DATA REGISTRO (Histórico) deve ser informado.");
        }
        if ((obj.getMatriculaPeriodo() == null) && (obj.getMatriculaPeriodo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo MATRÍCULA PERÍODO (Histórico) deve ser informado.");
        }
    }

    public static void validarDadosHistoricoTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre) throws ConsistirException {
        if (turmaVO == null || turmaVO.getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo TURMA (Registro de Nota) deve ser informado");
        }
        if (disciplina.intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (Registro de Nota) deve ser informado");
        }
        if (turmaVO.getIntegral()) {
            ano = "";
            semestre = "";
        }

    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setTipoHistorico("NO");
        setSituacao("");
        setFreguencia(0.0);
        setDataRegistro(new Date());
//		setNota1(0.0);
//		setNota2(0.0);
//		setNota3(0.0);
//		setNota4(0.0);
//		setNota5(0.0);
//		setNota6(0.0);
//		setNota7(0.0);
//		setNota8(0.0);
//		setNota9(0.0);
//		setNota10(0.0);
        setEditavel(Boolean.FALSE);
        setNomePaiAluno("");
        setNomeMaeAluno("");
        setEstabelecimentoEnsinoMedio("");
        setAnoConclusaoEnsinoMedio("");
//		setMediaFinal(0.0);
        setTransferenciaEntradaDisciplinasAproveitadas(new Integer(0));
        setDisciplinasAproveitadas(new Integer(0));
    }

    /**
     * Método oficial para determinar se um histórico deve ser tratado como aprovado
     * ou não.
     * @return 
     */
    public Boolean getAprovado() {
        SituacaoHistorico situacaoEnum = SituacaoHistorico.getEnum(getSituacao());
        if (situacaoEnum == null) {
        	return Boolean.FALSE;
        }
        return situacaoEnum.getHistoricoAprovado();
    }

    /**
     * Método oficial para determinar se um histórico deve ser tratado como aprovado
     * ou não.
     * @return 
     */
    public Boolean getReprovado() {
        SituacaoHistorico situacaoEnum = SituacaoHistorico.getEnum(getSituacao());       
        if (situacaoEnum == null) {
        	return Boolean.FALSE;
        }
        return situacaoEnum.getHistoricoReprovado();
    }
    
    /**
     * Método oficial para determinar se um histórico deve ser tratado como reprovado.
     * implicando que aluno ainda terá que cursar a referida disciplina.
     * @return 
     */
    public Boolean getCursando() {
        SituacaoHistorico situacaoEnum = SituacaoHistorico.getEnum(this.getSituacao());
        if (situacaoEnum.getHistoricoCursando()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Historico</code>).
     */
    @XmlElement(name = "responsavel")
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Historico</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com ( <code>Historico</code>).
     */
    @XmlElement(name = "matricula")
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    public String getAnoSemestreOrdenacao() {
        if (!getAnoHistorico().trim().isEmpty()) {
            return getAnoHistorico() + " / " + getSemestreHistorico() + "/" + getDisciplina().getNome();
        }
        return getMatriculaPeriodo().getAno() + " / " + getMatriculaPeriodo().getSemestre() + "/" + getDisciplina().getNome();
    }

    public String getDisciplinaOrdenacao() {
        return getDisciplina().getNome();
    }

    public Integer getGradeDisciplinaOrdemOrdenacao() {
    	return getGradeDisciplinaVO().getOrdem();
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com ( <code>Historico</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com ( <code>Historico</code>).
     */
    @XmlElement(name = "disciplina")
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return (disciplina);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com ( <code>Historico</code>).
     */
    public void setDisciplina(DisciplinaVO obj) {
        this.disciplina = obj;
    }

    @XmlElement(name = "nota13")
    public Double getNota13() {
        return (nota13);
    }

    public void setNota13(Double nota13) {
//        if (nota13 != null) {
//            if (nota13 > 10.0) {
//                nota13 = 10.0;
//            }
//        }
        this.nota13 = nota13;
    }

    @XmlElement(name = "nota12")
    public Double getNota12() {
        return (nota12);
    }

    public void setNota12(Double nota12) {
//        if (nota12 != null) {
//            if (nota12 > 10.0) {
//                nota12 = 10.0;
//            }
//        }
        this.nota12 = nota12;
    }

    @XmlElement(name = "nota11")
    public Double getNota11() {
        return (nota11);
    }

    public void setNota11(Double nota11) {
//        if (nota11 != null) {
//            if (nota11 > 10.0) {
//                nota11 = 10.0;
//            }
//        }
        this.nota11 = nota11;
    }

    @XmlElement(name = "nota10")
    public Double getNota10() {
        return (nota10);
    }

    public void setNota10(Double nota10) {
//        if (nota10 != null) {
//            if (nota10 > 10.0) {
//                nota10 = 10.0;
//            }
//        }
        this.nota10 = nota10;
    }

    @XmlElement(name = "nota9")
    public Double getNota9() {
        return (nota9);
    }

    public void setNota9(Double nota9) {
//        if (nota9 != null) {
//            if (nota9 > 10.0) {
//                nota9 = 10.0;
//            }
//        }
        this.nota9 = nota9;
    }

    @XmlElement(name = "nota8")
    public Double getNota8() {
        return (nota8);
    }

    public void setNota8(Double nota8) {
//        if (nota8 != null) {
//            if (nota8 > 10.0) {
//                nota8 = 10.0;
//            }
//        }
        this.nota8 = nota8;
    }

    @XmlElement(name = "nota7")
    public Double getNota7() {
        return (nota7);
    }

    public void setNota7(Double nota7) {
//        if (nota7 != null) {
//            if (nota7 > 10.0) {
//                nota7 = 10.0;
//            }
//        }
        this.nota7 = nota7;
    }

    @XmlElement(name = "nota6")
    public Double getNota6() {
        return (nota6);
    }

    public void setNota6(Double nota6) {
//        if (nota6 != null) {
//            if (nota6 > 10.0) {
//                nota6 = 10.0;
//            }
//        }
        this.nota6 = nota6;
    }

    @XmlElement(name = "nota5")
    public Double getNota5() {
        return (nota5);
    }

    public void setNota5(Double nota5) {
//        if (nota5 != null) {
//            if (nota5 > 10.0) {
//                nota5 = 10.0;
//            }
//        }
        this.nota5 = nota5;
    }

    @XmlElement(name = "nota4")
    public Double getNota4() {
        return (nota4);
    }

    public void setNota4(Double nota4) {
//        if (nota4 != null) {
//            if (nota4 > 10.0) {
//                nota4 = 10.0;
//            }
//        }
        this.nota4 = nota4;
    }

    @XmlElement(name = "nota3")
    public Double getNota3() {
        return (nota3);
    }

    public void setNota3(Double nota3) {
//        if (nota3 != null) {
//            if (nota3 > 10.0) {
//                nota3 = 10.0;
//            }
//        }
        this.nota3 = nota3;
    }

    @XmlElement(name = "nota2")
    public Double getNota2() {
        return (nota2);
    }

    public void setNota2(Double nota2) {
//        if (nota2 != null) {
//            if (nota2 > 10.0) {
//                nota2 = 10.0;
//            }
//        }
        this.nota2 = nota2;
    }

    @XmlElement(name = "nota1")
    public Double getNota1() {
        return (nota1);
    }

    public void setNota1(Double nota1) {
//        if (nota1 != null) {
//            if (nota1 > 10.0) {
//                nota1 = 10.0;
//            }
//        }
        this.nota1 = nota1;
    }
    @XmlElement(name = "dataRegistro")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataRegistro() {
        return (dataRegistro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataRegistro_Apresentar() {
        return (Uteis.getData(dataRegistro));
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    @XmlElement(name = "frequencia")
    public Double getFreguencia() {
        if (freguencia == null) {
            freguencia = 0.0;
        }
        return (freguencia);
    }

    public String getFrequencia_Apresentar() {
        return Uteis.getDoubleFormatado(getFreguencia()) + "%";
    }

    public void setFreguencia(Double freguencia) {
        this.freguencia = freguencia;
    }

    @XmlElement(name = "situacao")
    public String getSituacao() {
        if ((situacao == null) || (situacao.equals(""))) {
            situacao = "NC";
        }
        return (situacao);
    }

    @XmlElement(name = "editavel")
    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    @XmlElement(name = "matriculaPeriodoTurmaDisciplina")
    public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplina() {
        if (matriculaPeriodoTurmaDisciplina == null) {
            matriculaPeriodoTurmaDisciplina = new MatriculaPeriodoTurmaDisciplinaVO();
        }
        return matriculaPeriodoTurmaDisciplina;
    }

    public void setMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina) {
        this.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    @XmlElement(name = "situacaoApresentar")
    public String getSituacao_Apresentar() {
    	if(getSituacao().equals("AA") && getApresentarAprovadoHistorico()){
    		return SituacaoHistorico.getDescricao("AP");
    	}
        return SituacaoHistorico.getDescricao(situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @XmlElement(name = "tipoHistorico")
    public String getTipoHistorico() {
        if (tipoHistorico == null) {
            tipoHistorico = TipoHistorico.NORMAL.getValor();
        }
        return (tipoHistorico);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getTipoHistorico_Apresentar() {
        return TipoHistorico.getDescricao(getTipoHistorico());
    }

    public void setTipoHistorico(String tipoHistorico) {
        this.tipoHistorico = tipoHistorico;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
    	if (codigo == null) {
    		codigo = 0;
    	}
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    @XmlElement(name = "matriculaPeriodo")
    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public String getOrdenacao() {
        return Uteis.removerAcentuacao(getMatricula().getAluno().getNome());
    }

    public Integer getPeriodoLetivoOrdenacao() {
        return getMatriculaPeriodo().getPeridoLetivo().getPeriodoLetivo();
    }

    public JRDataSource getListaHistoricoAluno() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaHistorico().toArray());
        return jr;
    }

    /**
     * @return the listaHistorico
     */
    @XmlElement(name = "listaHistorico")
    public List<HistoricoVO> getListaHistorico() {
        if (listaHistorico == null) {
            listaHistorico = new ArrayList<HistoricoVO>();
        }
        return listaHistorico;
    }

    /**
     * @param listaHistorico
     *            the listaHistorico to set
     */
    public void setListaHistorico(List listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

    /**
     * @return the nomePaiAluno
     */
    @XmlElement(name = "nomePaiAluno")
    public String getNomePaiAluno() {
        return nomePaiAluno;
    }

    /**
     * @param nomePaiAluno
     *            the nomePaiAluno to set
     */
    public void setNomePaiAluno(String nomePaiAluno) {
        this.nomePaiAluno = nomePaiAluno;
    }

    /**
     * @return the nomeMaeAluno
     */
    @XmlElement(name = "nomeMaeAluno")
    public String getNomeMaeAluno() {
        return nomeMaeAluno;
    }

    /**
     * @param nomeMaeAluno
     *            the nomeMaeAluno to set
     */
    public void setNomeMaeAluno(String nomeMaeAluno) {
        this.nomeMaeAluno = nomeMaeAluno;
    }

    /**
     * @return the estabelecimentoEnsinoMedio
     */
    @XmlElement(name = "estabelecimentoEnsinoMedio")
    public String getEstabelecimentoEnsinoMedio() {
        return estabelecimentoEnsinoMedio;
    }

    /**
     * @param estabelecimentoEnsinoMedio
     *            the estabelecimentoEnsinoMedio to set
     */
    public void setEstabelecimentoEnsinoMedio(String estabelecimentoEnsinoMedio) {
        this.estabelecimentoEnsinoMedio = estabelecimentoEnsinoMedio;
    }

    /**
     * @return the anoConclusaoEnsinoMedio
     */
    @XmlElement(name = "anoConclusaoEnsinoMedio")
    public String getAnoConclusaoEnsinoMedio() {
        return anoConclusaoEnsinoMedio;
    }

    /**
     * @param anoConclusaoEnsinoMedio
     *            the anoConclusaoEnsinoMedio to set
     */
    public void setAnoConclusaoEnsinoMedio(String anoConclusaoEnsinoMedio) {
        this.anoConclusaoEnsinoMedio = anoConclusaoEnsinoMedio;
    }

    @XmlElement(name = "mediaFinal")
    public Double getMediaFinal() {
        return (mediaFinal);
    }

    @XmlElement(name = "mediaFinalApresentar")
    public String getMediaFinal_Apresentar() {
        if (getMediaFinalConceito().getCodigo() > 0) {
            return getMediaFinalConceito().getConceitoNota();
        }else if(getUtilizaNotaFinalConceito()){
        	return getNotaFinalConceito();
        }        
        return Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(getMediaFinal(), getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
    }

    public void setMediaFinal(Double mediaFinal) {
    	this.mediaFinal = mediaFinal;
    }

    @XmlElement(name = "transferenciaEntradaDisciplinasAproveitadas")
    public Integer getTransferenciaEntradaDisciplinasAproveitadas() {
        return transferenciaEntradaDisciplinasAproveitadas;
    }

    public void setTransferenciaEntradaDisciplinasAproveitadas(Integer transferenciaEntradaDisciplinasAproveitadas) {
        this.transferenciaEntradaDisciplinasAproveitadas = transferenciaEntradaDisciplinasAproveitadas;
    }

    @XmlElement(name = "disciplinasAproveitadas")
    public Integer getDisciplinasAproveitadas() {
        return disciplinasAproveitadas;
    }

    public void setDisciplinasAproveitadas(Integer disciplinasAproveitadas) {
        this.disciplinasAproveitadas = disciplinasAproveitadas;
    }

    /**
     * @return the configuracaoAcademico
     */
    @XmlElement(name = "configuracaoAcademico")
    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        if (configuracaoAcademico == null) {
            configuracaoAcademico = new ConfiguracaoAcademicoVO();
        }
        return configuracaoAcademico;
    }

    /**
     * @param configuracaoAcademico
     *            the configuracaoAcademico to set
     */
    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }

    @XmlElement(name = "notas")
    public List<NotaVO> getListaNotas() {
        if (listaNotas == null) {
            listaNotas = new ArrayList<NotaVO>(0);
        }
        return listaNotas;
    }

    public void setListaNotas(List<NotaVO> listaNotas) {
        this.listaNotas = listaNotas;
    }

    public JRDataSource getNotaVO() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaNotas().toArray());
        return jr;
    }

    /**
     * @return Lista de notas
     */
    public List<NotaVO> montaNotas(boolean apenasNotaTipoMedia, int quantidadeCasasDecimaisPermitirAposVirgula) {
        NotaVO nota;
        ConfiguracaoAcademicoNotaConceitoVO conceito = null;
        ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = null;
        getListaNotas().clear();
        for (int i = 1; i <= 40; i++) {
        	configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "configuracaoAcademicaNota"+i+"VO");
            if (configuracaoAcademicaNotaVO.getCodigo() > 0 &&
            		configuracaoAcademicaNotaVO.getApresentarNotaBoletim()
            		&& (apenasNotaTipoMedia == false 
            		|| (apenasNotaTipoMedia == true && configuracaoAcademicaNotaVO.getUtilizarComoMediaFinal()))) {
                nota = new NotaVO();
                nota.setTitulo(configuracaoAcademicaNotaVO.getTitulo());
                conceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(this, "nota" + i + "Conceito");
                if (conceito != null && !conceito.getConceitoNota().trim().isEmpty()) {
                    nota.setValorTexto(conceito.getConceitoNota());
                }
                nota.setValor(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(((Double) UtilReflexao.invocarMetodoGet(this, "nota" + i)), quantidadeCasasDecimaisPermitirAposVirgula));
                UtilReflexao.invocarMetodo(this, "setTitulo" + i, (String) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "tituloNotaApresentar" + i));
                getListaNotas().add(nota);
            } else {
                UtilReflexao.invocarMetodoSetParametroNull(this, "nota" + i);
            }
        }
        return getListaNotas();
    }
    
    public List<NotaVO> montaNotas(List<NotaVO> listaNotas) {
        NotaVO nota;
        for (int i = 1; i <= 40; i++) {
            if ((Boolean) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "utilizarNota" + i)) {
                nota = new NotaVO();
                nota.setTitulo((String) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "tituloNotaApresentar" + i));
                listaNotas.add(nota);
            }
        }
        return listaNotas;
    }

    @XmlElement(name = "nota1Lancada")
    public Boolean getNota1Lancada() {
        if (nota1Lancada == null) {
            nota1Lancada = false;
        }
        return nota1Lancada;
    }

    public void setNota1Lancada(Boolean nota1Lancada) {
        this.nota1Lancada = nota1Lancada;
    }

    @XmlElement(name = "nota2Lancada")
    public Boolean getNota2Lancada() {
        if (nota2Lancada == null) {
            nota2Lancada = false;
        }
        return nota2Lancada;
    }

    public void setNota2Lancada(Boolean nota2Lancada) {
        this.nota2Lancada = nota2Lancada;
    }

    @XmlElement(name = "nota3Lancada")
    public Boolean getNota3Lancada() {
        if (nota3Lancada == null) {
            nota3Lancada = false;
        }
        return nota3Lancada;
    }

    public void setNota3Lancada(Boolean nota3Lancada) {
        this.nota3Lancada = nota3Lancada;
    }

    @XmlElement(name = "nota4Lancada")
    public Boolean getNota4Lancada() {
        if (nota4Lancada == null) {
            nota4Lancada = false;
        }
        return nota4Lancada;
    }

    public void setNota4Lancada(Boolean nota4Lancada) {
        this.nota4Lancada = nota4Lancada;
    }

    @XmlElement(name = "nota5Lancada")
    public Boolean getNota5Lancada() {
        if (nota5Lancada == null) {
            nota5Lancada = false;
        }
        return nota5Lancada;
    }

    public void setNota5Lancada(Boolean nota5Lancada) {
        this.nota5Lancada = nota5Lancada;
    }

    @XmlElement(name = "nota6Lancada")
    public Boolean getNota6Lancada() {
        if (nota6Lancada == null) {
            nota6Lancada = false;
        }
        return nota6Lancada;
    }

    public void setNota6Lancada(Boolean nota6Lancada) {
        this.nota6Lancada = nota6Lancada;
    }

    @XmlElement(name = "nota7Lancada")
    public Boolean getNota7Lancada() {
        if (nota7Lancada == null) {
            nota7Lancada = false;
        }
        return nota7Lancada;
    }

    public void setNota7Lancada(Boolean nota7Lancada) {
        this.nota7Lancada = nota7Lancada;
    }

    @XmlElement(name = "nota8Lancada")
    public Boolean getNota8Lancada() {
        if (nota8Lancada == null) {
            nota8Lancada = false;
        }
        return nota8Lancada;
    }

    public void setNota8Lancada(Boolean nota8Lancada) {
        this.nota8Lancada = nota8Lancada;
    }

    @XmlElement(name = "nota9Lancada")
    public Boolean getNota9Lancada() {
        if (nota9Lancada == null) {
            nota9Lancada = false;
        }
        return nota9Lancada;
    }

    public void setNota9Lancada(Boolean nota9Lancada) {
        this.nota9Lancada = nota9Lancada;
    }

    @XmlElement(name = "nota10Lancada")
    public Boolean getNota10Lancada() {
        if (nota10Lancada == null) {
            nota10Lancada = false;
        }
        return nota10Lancada;
    }

    public void setNota10Lancada(Boolean nota10Lancada) {
        this.nota10Lancada = nota10Lancada;
    }

    @XmlElement(name = "nota11Lancada")
    public Boolean getNota11Lancada() {
        if (nota11Lancada == null) {
            nota11Lancada = false;
        }
        return nota11Lancada;
    }

    public void setNota11Lancada(Boolean nota11Lancada) {
        this.nota11Lancada = nota11Lancada;
    }

    @XmlElement(name = "nota12Lancada")
    public Boolean getNota12Lancada() {
        if (nota12Lancada == null) {
            nota12Lancada = false;
        }
        return nota12Lancada;
    }

    public void setNota12Lancada(Boolean nota12Lancada) {
        this.nota12Lancada = nota12Lancada;
    }

    @XmlElement(name = "nota13Lancada")
    public Boolean getNota13Lancada() {
        if (nota13Lancada == null) {
            nota13Lancada = false;
        }
        return nota13Lancada;
    }

    public void setNota13Lancada(Boolean nota13Lancada) {
        this.nota13Lancada = nota13Lancada;
    }

    @XmlElement(name = "criadoPorNovaDisciplinaAproveitada")
    public boolean getCriadoPorNovaDisciplinaAproveitada() {
        return criadoPorNovaDisciplinaAproveitada;
    }

    public void setCriadoPorNovaDisciplinaAproveitada(boolean criadoPorNovaDisciplinaAproveitada) {
        this.criadoPorNovaDisciplinaAproveitada = criadoPorNovaDisciplinaAproveitada;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HistoricoVO other = (HistoricoVO) obj;
        if (this.nota1 != other.nota1 && (this.nota1 == null || !this.nota1.equals(other.nota1))) {
            return false;
        }
        if (this.nota2 != other.nota2 && (this.nota2 == null || !this.nota2.equals(other.nota2))) {
            return false;
        }
        if (this.nota3 != other.nota3 && (this.nota3 == null || !this.nota3.equals(other.nota3))) {
            return false;
        }
        if (this.nota4 != other.nota4 && (this.nota4 == null || !this.nota4.equals(other.nota4))) {
            return false;
        }
        if (this.nota5 != other.nota5 && (this.nota5 == null || !this.nota5.equals(other.nota5))) {
            return false;
        }
        if (this.nota6 != other.nota6 && (this.nota6 == null || !this.nota6.equals(other.nota6))) {
            return false;
        }
        if (this.nota7 != other.nota7 && (this.nota7 == null || !this.nota7.equals(other.nota7))) {
            return false;
        }
        if (this.nota8 != other.nota8 && (this.nota8 == null || !this.nota8.equals(other.nota8))) {
            return false;
        }
        if (this.nota9 != other.nota9 && (this.nota9 == null || !this.nota9.equals(other.nota9))) {
            return false;
        }
        if (this.nota10 != other.nota10 && (this.nota10 == null || !this.nota10.equals(other.nota10))) {
            return false;
        }
        if (this.nota11 != other.nota11 && (this.nota11 == null || !this.nota11.equals(other.nota11))) {
            return false;
        }
        if (this.nota12 != other.nota12 && (this.nota12 == null || !this.nota12.equals(other.nota12))) {
            return false;
        }
        if (this.nota13 != other.nota13 && (this.nota13 == null || !this.nota13.equals(other.nota13))) {
            return false;
        }
        if (this.nota14 != other.nota14 && (this.nota14 == null || !this.nota14.equals(other.nota14))) {
            return false;
        }
        if (this.nota15 != other.nota15 && (this.nota15 == null || !this.nota15.equals(other.nota15))) {
            return false;
        }        
        if (this.nota16 != other.nota16 && (this.nota16 == null || !this.nota16.equals(other.nota16))) {
            return false;
        }        
        if (this.nota17 != other.nota17 && (this.nota17 == null || !this.nota17.equals(other.nota17))) {
            return false;
        }        
        if (this.nota18 != other.nota18 && (this.nota18 == null || !this.nota18.equals(other.nota18))) {
            return false;
        }        
        if (this.nota19 != other.nota19 && (this.nota19 == null || !this.nota19.equals(other.nota19))) {
            return false;
        }        
        if (this.nota20 != other.nota20 && (this.nota20 == null || !this.nota20.equals(other.nota20))) {
            return false;
        }        
        if (this.nota21 != other.nota21 && (this.nota21 == null || !this.nota21.equals(other.nota21))) {
            return false;
        }        
        if (this.nota22 != other.nota22 && (this.nota22 == null || !this.nota22.equals(other.nota22))) {
            return false;
        }        
        if (this.nota23 != other.nota23 && (this.nota23 == null || !this.nota23.equals(other.nota23))) {
            return false;
        }        
        if (this.nota24 != other.nota24 && (this.nota24 == null || !this.nota24.equals(other.nota24))) {
            return false;
        }        
        if (this.nota25 != other.nota25 && (this.nota25 == null || !this.nota25.equals(other.nota25))) {
            return false;
        }        
        if (this.nota26 != other.nota26 && (this.nota26 == null || !this.nota26.equals(other.nota26))) {
            return false;
        }        
        if (this.nota27 != other.nota27 && (this.nota27 == null || !this.nota27.equals(other.nota27))) {
            return false;
        }    
        if (this.nota28 != other.nota28 && (this.nota28 == null || !this.nota28.equals(other.nota28))) {
            return false;
        }    
        if (this.nota29 != other.nota29 && (this.nota29 == null || !this.nota29.equals(other.nota29))) {
            return false;
        }        
        if (this.nota30 != other.nota30 && (this.nota30 == null || !this.nota30.equals(other.nota30))) {
            return false;
        }   
        if (this.nota31 != other.nota31 && (this.nota31 == null || !this.nota31.equals(other.nota31))) {
            return false;
        }
        if (this.nota32 != other.nota32 && (this.nota32 == null || !this.nota32.equals(other.nota32))) {
            return false;
        }
        if (this.nota33 != other.nota33 && (this.nota33 == null || !this.nota33.equals(other.nota33))) {
            return false;
        }
        if (this.nota34 != other.nota34 && (this.nota34 == null || !this.nota34.equals(other.nota34))) {
            return false;
        }
        if (this.nota35 != other.nota35 && (this.nota35 == null || !this.nota35.equals(other.nota35))) {
            return false;
        }
        if (this.nota36 != other.nota36 && (this.nota36 == null || !this.nota36.equals(other.nota36))) {
            return false;
        }
        if (this.nota37 != other.nota37 && (this.nota37 == null || !this.nota37.equals(other.nota37))) {
            return false;
        }
        if (this.nota38 != other.nota38 && (this.nota38 == null || !this.nota38.equals(other.nota38))) {
            return false;
        }
        if (this.nota39 != other.nota39 && (this.nota39 == null || !this.nota39.equals(other.nota39))) {
            return false;
        }
        if (this.nota40 != other.nota40 && (this.nota40 == null || !this.nota40.equals(other.nota40))) {
            return false;
        }
        if (this.nota31 != other.nota31 && (this.nota31 == null || !this.nota31.equals(other.nota31))) {
            return false;
        }
        if (this.nota32 != other.nota32 && (this.nota32 == null || !this.nota32.equals(other.nota32))) {
            return false;
        }
        if (this.nota33 != other.nota33 && (this.nota33 == null || !this.nota33.equals(other.nota33))) {
            return false;
        }
        if (this.nota34 != other.nota34 && (this.nota34 == null || !this.nota34.equals(other.nota34))) {
            return false;
        }
        if (this.nota35 != other.nota35 && (this.nota35 == null || !this.nota35.equals(other.nota35))) {
            return false;
        }
        if (this.nota36 != other.nota36 && (this.nota36 == null || !this.nota36.equals(other.nota36))) {
            return false;
        }
        if (this.nota37 != other.nota37 && (this.nota37 == null || !this.nota37.equals(other.nota37))) {
            return false;
        }
        if (this.nota38 != other.nota38 && (this.nota38 == null || !this.nota38.equals(other.nota38))) {
            return false;
        }
        if (this.nota39 != other.nota39 && (this.nota39 == null || !this.nota39.equals(other.nota39))) {
            return false;
        }
        if (this.nota40 != other.nota40 && (this.nota40 == null || !this.nota40.equals(other.nota40))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.nota1 != null ? this.nota1.hashCode() : 0);
        hash = 59 * hash + (this.nota2 != null ? this.nota2.hashCode() : 0);
        hash = 59 * hash + (this.nota3 != null ? this.nota3.hashCode() : 0);
        hash = 59 * hash + (this.nota4 != null ? this.nota4.hashCode() : 0);
        hash = 59 * hash + (this.nota5 != null ? this.nota5.hashCode() : 0);
        hash = 59 * hash + (this.nota6 != null ? this.nota6.hashCode() : 0);
        hash = 59 * hash + (this.nota7 != null ? this.nota7.hashCode() : 0);
        hash = 59 * hash + (this.nota8 != null ? this.nota8.hashCode() : 0);
        hash = 59 * hash + (this.nota9 != null ? this.nota9.hashCode() : 0);
        hash = 59 * hash + (this.nota10 != null ? this.nota10.hashCode() : 0);
        hash = 59 * hash + (this.nota11 != null ? this.nota11.hashCode() : 0);
        hash = 59 * hash + (this.nota12 != null ? this.nota12.hashCode() : 0);
        hash = 59 * hash + (this.nota13 != null ? this.nota13.hashCode() : 0);
        hash = 59 * hash + (this.nota14 != null ? this.nota14.hashCode() : 0);
        hash = 59 * hash + (this.nota15 != null ? this.nota15.hashCode() : 0);
        hash = 59 * hash + (this.nota16 != null ? this.nota16.hashCode() : 0);
        hash = 59 * hash + (this.nota17 != null ? this.nota17.hashCode() : 0);
        hash = 59 * hash + (this.nota18 != null ? this.nota18.hashCode() : 0);
        hash = 59 * hash + (this.nota19 != null ? this.nota19.hashCode() : 0);
        hash = 59 * hash + (this.nota20 != null ? this.nota20.hashCode() : 0);
        hash = 59 * hash + (this.nota21 != null ? this.nota21.hashCode() : 0);
        hash = 59 * hash + (this.nota22 != null ? this.nota22.hashCode() : 0);
        hash = 59 * hash + (this.nota23 != null ? this.nota23.hashCode() : 0);
        hash = 59 * hash + (this.nota24 != null ? this.nota24.hashCode() : 0);
        hash = 59 * hash + (this.nota25 != null ? this.nota25.hashCode() : 0);
        hash = 59 * hash + (this.nota26 != null ? this.nota26.hashCode() : 0);
        hash = 59 * hash + (this.nota27 != null ? this.nota27.hashCode() : 0);
        hash = 59 * hash + (this.nota28 != null ? this.nota28.hashCode() : 0);
        hash = 59 * hash + (this.nota29 != null ? this.nota29.hashCode() : 0);
        hash = 59 * hash + (this.nota30 != null ? this.nota30.hashCode() : 0);
        hash = 59 * hash + (this.nota31 != null ? this.nota31.hashCode() : 0);
        hash = 59 * hash + (this.nota32 != null ? this.nota32.hashCode() : 0);
        hash = 59 * hash + (this.nota33 != null ? this.nota33.hashCode() : 0);
        hash = 59 * hash + (this.nota34 != null ? this.nota34.hashCode() : 0);
        hash = 59 * hash + (this.nota35 != null ? this.nota35.hashCode() : 0);
        hash = 59 * hash + (this.nota36 != null ? this.nota36.hashCode() : 0);
        hash = 59 * hash + (this.nota37 != null ? this.nota37.hashCode() : 0);
        hash = 59 * hash + (this.nota38 != null ? this.nota38.hashCode() : 0);
        hash = 59 * hash + (this.nota39 != null ? this.nota39.hashCode() : 0);
        hash = 59 * hash + (this.nota40 != null ? this.nota40.hashCode() : 0);
        return hash;
    }

    public String getSituacaoMatriculaOuSituacaoFinanceiraMatriculaPeriodo() {
        if (!getMatricula().getSituacao().equals("AT")) {
            if (getRealizouTransferenciaTurno()) {
                return getMatricula().getSituacao_Apresentar() + " - Realizou T. Turno";
            }
            return getMatricula().getSituacao_Apresentar();
        } else if (getMatriculaPeriodo().getSituacao().equals("PF")) {
            if (getRealizouTransferenciaTurno()) {
                return getMatriculaPeriodo().getSituacao_Apresentar() + " - Realizou T. Turno";
            }
            return getMatriculaPeriodo().getSituacao_Apresentar();
        }
        if (getRealizouTransferenciaTurno()) {
            return getMatricula().getSituacao_Apresentar() + " - Realizou T. Turno";
        }
        return getMatricula().getSituacao_Apresentar();
    }

    public boolean getIsProfessorNaoPodeAlterarRegistro() {
        if (!getMatricula().getSituacao().equals("AT") || getSituacao().equals("AA")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the data
     */
    public Date getData() {

        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    @XmlElement(name = "dataPrimeiraAula")
    public Date getDataPrimeiraAula() {
        return dataPrimeiraAula;
    }

    public void setDataPrimeiraAula(Date dataPrimeiraAula) {
        this.dataPrimeiraAula = dataPrimeiraAula;
    }

    @XmlElement(name = "nomeProfessor")
    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getPeriodoAula_Apresentar() {
    	if (this.getOcultarDataAula()) {
    		return "EaD";
    	}
        if (getDataPrimeiraAula() != null && getData() != null) {
            return "De  " + Uteis.getData(getDataPrimeiraAula()) + "  a  " + Uteis.getData(getData());
        }
        return "";
    }

    @XmlElement(name = "titulo1")
    public String getTitulo1() {
        if (titulo1 == null) {
            titulo1 = "";
        }
        return titulo1;
    }

    public void setTitulo1(String titulo1) {
        this.titulo1 = titulo1;
    }

    @XmlElement(name = "titulo2")
    public String getTitulo2() {
        if (titulo2 == null) {
            titulo2 = "";
        }
        return titulo2;
    }

    public void setTitulo2(String titulo2) {
        this.titulo2 = titulo2;
    }

    @XmlElement(name = "titulo3")
    public String getTitulo3() {
        if (titulo3 == null) {
            titulo3 = "";
        }
        return titulo3;
    }

    public void setTitulo3(String titulo3) {
        this.titulo3 = titulo3;
    }

    @XmlElement(name = "titulo4")
    public String getTitulo4() {
        if (titulo4 == null) {
            titulo4 = "";
        }
        return titulo4;
    }

    public void setTitulo4(String titulo4) {
        this.titulo4 = titulo4;
    }

    @XmlElement(name = "titulo5")
    public String getTitulo5() {
        if (titulo5 == null) {
            titulo5 = "";
        }
        return titulo5;
    }

    public void setTitulo5(String titulo5) {
        this.titulo5 = titulo5;
    }

    @XmlElement(name = "titulo6")
    public String getTitulo6() {
        if (titulo6 == null) {
            titulo6 = "";
        }
        return titulo6;
    }

    public void setTitulo6(String titulo6) {
        this.titulo6 = titulo6;
    }

    @XmlElement(name = "titulo7")
    public String getTitulo7() {
        if (titulo7 == null) {
            titulo7 = "";
        }
        return titulo7;
    }

    public void setTitulo7(String titulo7) {
        this.titulo7 = titulo7;
    }

    @XmlElement(name = "titulo8")
    public String getTitulo8() {
        if (titulo8 == null) {
            titulo8 = "";
        }
        return titulo8;
    }

    public void setTitulo8(String titulo8) {
        this.titulo8 = titulo8;
    }

    @XmlElement(name = "titulo9")
    public String getTitulo9() {
        if (titulo9 == null) {
            titulo9 = "";
        }
        return titulo9;
    }

    public void setTitulo9(String titulo9) {
        this.titulo9 = titulo9;
    }

    @XmlElement(name = "titulo10")
    public String getTitulo10() {
        if (titulo10 == null) {
            titulo10 = "";
        }
        return titulo10;
    }

    public void setTitulo10(String titulo10) {
        this.titulo10 = titulo10;
    }

    @XmlElement(name = "titulo11")
    public String getTitulo11() {
        if (titulo11 == null) {
            titulo11 = "";
        }
        return titulo11;
    }

    public void setTitulo11(String titulo11) {
        this.titulo11 = titulo11;
    }

    @XmlElement(name = "titulo12")
    public String getTitulo12() {
        if (titulo12 == null) {
            titulo12 = "";
        }
        return titulo12;
    }

    public void setTitulo12(String titulo12) {
        this.titulo12 = titulo12;
    }

    @XmlElement(name = "titulo13")
    public String getTitulo13() {
        if (titulo13 == null) {
            titulo13 = "";
        }
        return titulo13;
    }

    public void setTitulo13(String titulo13) {
        this.titulo13 = titulo13;
    }

    @XmlElement(name = "disciplinaIgualGradeAnterior")
    public Boolean getDisciplinaIgualGradeAnterior() {
        if (disciplinaIgualGradeAnterior == null) {
            disciplinaIgualGradeAnterior = Boolean.FALSE;
        }
        return disciplinaIgualGradeAnterior;
    }

    public void setDisciplinaIgualGradeAnterior(Boolean disciplinaIgualGradeAnterior) {
        this.disciplinaIgualGradeAnterior = disciplinaIgualGradeAnterior;
    }

    @XmlElement(name = "instituicao")
    public String getInstituicao() {
        if (instituicao == null) {
            instituicao = "";
        }
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    @XmlElement(name = "nota1Texto")
    public String getNota1Texto() {
        if (nota1Texto == null) {
            nota1Texto = "";
        }
        return nota1Texto;
    }

    public void setNota1Texto(String nota1Texto) {
        this.nota1Texto = nota1Texto;
    }

    @XmlElement(name = "nota2Texto")
    public String getNota2Texto() {
        if (nota2Texto == null) {
            nota2Texto = "";
        }
        return nota2Texto;
    }

    public void setNota2Texto(String nota2Texto) {
        this.nota2Texto = nota2Texto;
    }

    @XmlElement(name = "nota3Texto")
    public String getNota3Texto() {
        if (nota3Texto == null) {
            nota3Texto = "";
        }
        return nota3Texto;
    }

    public void setNota3Texto(String nota3Texto) {
        this.nota3Texto = nota3Texto;
    }

    @XmlElement(name = "nota4Texto")
    public String getNota4Texto() {
        if (nota4Texto == null) {
            nota4Texto = "";
        }
        return nota4Texto;
    }

    public void setNota4Texto(String nota4Texto) {
        this.nota4Texto = nota4Texto;
    }

    @XmlElement(name = "nota5Texto")
    public String getNota5Texto() {
        if (nota5Texto == null) {
            nota5Texto = "";
        }
        return nota5Texto;
    }

    public void setNota5Texto(String nota5Texto) {
        this.nota5Texto = nota5Texto;
    }

    @XmlElement(name = "nota6Texto")
    public String getNota6Texto() {
        if (nota6Texto == null) {
            nota6Texto = "";
        }
        return nota6Texto;
    }

    public void setNota6Texto(String nota6Texto) {
        this.nota6Texto = nota6Texto;
    }

    @XmlElement(name = "nota7Texto")
    public String getNota7Texto() {
        if (nota7Texto == null) {
            nota7Texto = "";
        }
        return nota7Texto;
    }

    public void setNota7Texto(String nota7Texto) {
        this.nota7Texto = nota7Texto;
    }

    @XmlElement(name = "nota8Texto")
    public String getNota8Texto() {
        if (nota8Texto == null) {
            nota8Texto = "";
        }
        return nota8Texto;
    }

    public void setNota8Texto(String nota8Texto) {
        this.nota8Texto = nota8Texto;
    }

    @XmlElement(name = "nota9Texto")
    public String getNota9Texto() {
        if (nota9Texto == null) {
            nota9Texto = "";
        }
        return nota9Texto;
    }

    public void setNota9Texto(String nota9Texto) {
        this.nota9Texto = nota9Texto;
    }

    @XmlElement(name = "nota10Texto")
    public String getNota10Texto() {
        if (nota10Texto == null) {
            nota10Texto = "";
        }
        return nota10Texto;
    }

    public void setNota10Texto(String nota10Texto) {
        this.nota10Texto = nota10Texto;
    }

    @XmlElement(name = "nota11Texto")
    public String getNota11Texto() {
        if (nota11Texto == null) {
            nota11Texto = "";
        }
        return nota11Texto;
    }

    public void setNota11Texto(String nota11Texto) {
        this.nota11Texto = nota11Texto;
    }

    @XmlElement(name = "nota12Texto")
    public String getNota12Texto() {
        if (nota12Texto == null) {
            nota12Texto = "";
        }
        return nota12Texto;
    }

    public void setNota12Texto(String nota12Texto) {
        this.nota12Texto = nota12Texto;
    }

    @XmlElement(name = "nota13Texto")
    public String getNota13Texto() {
        if (nota13Texto == null) {
            nota13Texto = "";
        }
        return nota13Texto;
    }

    public void setNota13Texto(String nota13Texto) {
        this.nota13Texto = nota13Texto;
    }

    @XmlElement(name = "mediaFinalTexto")
    public String getMediaFinalTexto() {
        if (mediaFinalTexto == null) {
            mediaFinalTexto = "";
        }
        return mediaFinalTexto;
    }

    public void setMediaFinalTexto(String mediaFinalTexto) {
        this.mediaFinalTexto = mediaFinalTexto;
    }

    public Boolean getApresentarNota1Historico() {
        return (getConfiguracaoAcademico().getApresentarTextoSemNotaCampoNuloHistorico() && getNota1() != null);
    }

    public Boolean getApresentarNota2Historico() {
        return !getConfiguracaoAcademico().getApresentarTextoSemNotaCampoNuloHistorico()
                || (getConfiguracaoAcademico().getApresentarTextoSemNotaCampoNuloHistorico() && getNota2() != null);
    }

    public Boolean getApresentarNota3Historico() {
        return !getConfiguracaoAcademico().getApresentarTextoSemNotaCampoNuloHistorico()
                || (getConfiguracaoAcademico().getApresentarTextoSemNotaCampoNuloHistorico() && getNota3() != null);
    }

    @XmlElement(name = "realizouTransferenciaTurno")
    public Boolean getRealizouTransferenciaTurno() {
        if (realizouTransferenciaTurno == null) {
            realizouTransferenciaTurno = Boolean.FALSE;
        }
        return realizouTransferenciaTurno;
    }

    public void setRealizouTransferenciaTurno(Boolean realizouTransferenciaTurno) {
        this.realizouTransferenciaTurno = realizouTransferenciaTurno;
    }

    /**
     * @return the titulacaoProfessor
     * foi incluido esta validação porque existia titulaçao com nome null na base 
     * titulacaoProfessor.equals("null")
     */
    @XmlElement(name = "titulacaoProfessor")
    public String getTitulacaoProfessor() {
        if (titulacaoProfessor == null  ) {
            titulacaoProfessor = "";
        }
        return titulacaoProfessor;
    }

    /**
     * @param titulacaoProfessor the titulacaoProfessor to set
     */
    public void setTitulacaoProfessor(String titulacaoProfessor) {
        this.titulacaoProfessor = titulacaoProfessor;
    }

    @XmlElement(name = "nota1Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota1Conceito() {
        if (nota1Conceito == null) {
            nota1Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota1Conceito;
    }

    public void setNota1Conceito(ConfiguracaoAcademicoNotaConceitoVO nota1Conceito) {
        this.nota1Conceito = nota1Conceito;
    }
    @XmlElement(name = "nota2Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota2Conceito() {
        if (nota2Conceito == null) {
            nota2Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota2Conceito;
    }

    public void setNota2Conceito(ConfiguracaoAcademicoNotaConceitoVO nota2Conceito) {
        this.nota2Conceito = nota2Conceito;
    }
    @XmlElement(name = "nota3Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota3Conceito() {
        if (nota3Conceito == null) {
            nota3Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota3Conceito;
    }

    public void setNota3Conceito(ConfiguracaoAcademicoNotaConceitoVO nota3Conceito) {
        this.nota3Conceito = nota3Conceito;
    }
    @XmlElement(name = "nota4Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota4Conceito() {
        if (nota4Conceito == null) {
            nota4Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota4Conceito;
    }

    public void setNota4Conceito(ConfiguracaoAcademicoNotaConceitoVO nota4Conceito) {
        this.nota4Conceito = nota4Conceito;
    }
    @XmlElement(name = "nota5Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota5Conceito() {
        if (nota5Conceito == null) {
            nota5Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota5Conceito;
    }

    public void setNota5Conceito(ConfiguracaoAcademicoNotaConceitoVO nota5Conceito) {
        this.nota5Conceito = nota5Conceito;
    }
    @XmlElement(name = "nota6Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota6Conceito() {
        if (nota6Conceito == null) {
            nota6Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota6Conceito;
    }

    public void setNota6Conceito(ConfiguracaoAcademicoNotaConceitoVO nota6Conceito) {
        this.nota6Conceito = nota6Conceito;
    }
    @XmlElement(name = "nota7Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota7Conceito() {
        if (nota7Conceito == null) {
            nota7Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota7Conceito;
    }

    public void setNota7Conceito(ConfiguracaoAcademicoNotaConceitoVO nota7Conceito) {
        this.nota7Conceito = nota7Conceito;
    }
    @XmlElement(name = "nota8Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota8Conceito() {
        if (nota8Conceito == null) {
            nota8Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota8Conceito;
    }

    public void setNota8Conceito(ConfiguracaoAcademicoNotaConceitoVO nota8Conceito) {
        this.nota8Conceito = nota8Conceito;
    }
    @XmlElement(name = "nota9Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota9Conceito() {
        if (nota9Conceito == null) {
            nota9Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota9Conceito;
    }

    public void setNota9Conceito(ConfiguracaoAcademicoNotaConceitoVO nota9Conceito) {
        this.nota9Conceito = nota9Conceito;
    }
    @XmlElement(name = "nota10Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota10Conceito() {
        if (nota10Conceito == null) {
            nota10Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota10Conceito;
    }

    public void setNota10Conceito(ConfiguracaoAcademicoNotaConceitoVO nota10Conceito) {
        this.nota10Conceito = nota10Conceito;
    }
    @XmlElement(name = "nota11Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota11Conceito() {
        if (nota11Conceito == null) {
            nota11Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota11Conceito;
    }

    public void setNota11Conceito(ConfiguracaoAcademicoNotaConceitoVO nota11Conceito) {
        this.nota11Conceito = nota11Conceito;
    }
    @XmlElement(name = "nota12Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota12Conceito() {
        if (nota12Conceito == null) {
            nota12Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota12Conceito;
    }

    public void setNota12Conceito(ConfiguracaoAcademicoNotaConceitoVO nota12Conceito) {
        this.nota12Conceito = nota12Conceito;
    }
    @XmlElement(name = "nota13Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota13Conceito() {
        if (nota13Conceito == null) {
            nota13Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota13Conceito;
    }

    public void setNota13Conceito(ConfiguracaoAcademicoNotaConceitoVO nota13Conceito) {
        this.nota13Conceito = nota13Conceito;
    }
    
    @XmlElement(name = "mediaFinalConceito")
    public ConfiguracaoAcademicoNotaConceitoVO getMediaFinalConceito() {
        if (mediaFinalConceito == null) {
            mediaFinalConceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return mediaFinalConceito;
    }

    public void setMediaFinalConceito(ConfiguracaoAcademicoNotaConceitoVO mediaFinalConceito) {
        this.mediaFinalConceito = mediaFinalConceito;
    }

    /**
     * @return the notificarSolicitacaoReposicao
     */
    @XmlElement(name = "notificarSolicitacaoReposicao")
    public Boolean getNotificarSolicitacaoReposicao() {
        if (notificarSolicitacaoReposicao == null) {
            notificarSolicitacaoReposicao = Boolean.FALSE;
        }
        return notificarSolicitacaoReposicao;
    }

    /**
     * @param notificarSolicitacaoReposicao the notificarSolicitacaoReposicao to set
     */
    public void setNotificarSolicitacaoReposicao(Boolean notificarSolicitacaoReposicao) {
        this.notificarSolicitacaoReposicao = notificarSolicitacaoReposicao;
    }

    /**
     * @return the permiteMarcarReposicao
     */
    @XmlElement(name = "permiteMarcarReposicao")
    public Boolean getPermiteMarcarReposicao() {
        if (permiteMarcarReposicao == null) {
            permiteMarcarReposicao = Boolean.TRUE;
        }
        return permiteMarcarReposicao;
    }

    /**
     * @param permiteMarcarReposicao the permiteMarcarReposicao to set
     */
    public void setPermiteMarcarReposicao(Boolean permiteMarcarReposicao) {
        this.permiteMarcarReposicao = permiteMarcarReposicao;
    }
    
    public Integer getAnoHistoricoInteiro() {
        String anoStr = getAnoHistorico();
        if (anoStr.equals("")) {
            anoStr = "0";
        }
        return Integer.parseInt(anoStr);
    }

    @XmlElement(name = "anoHistorico")
    public String getAnoHistorico() {
        if (anoHistorico == null) {
            anoHistorico = "";
        }
        return anoHistorico;
    }

    public void setAnoHistorico(String anoHistorico) {
        this.anoHistorico = anoHistorico;
    }
    
    public Integer getSemestreHistoricoInteiro() {
        String semestreStr = getSemestreHistorico();
        if (semestreStr.equals("")) {
            semestreStr = "0";
        }
        return Integer.parseInt(semestreStr);
    }

    @XmlElement(name = "semestreHistorico")
    public String getSemestreHistorico() {
        if (semestreHistorico == null) {
            semestreHistorico = "";
        }
        return semestreHistorico;
    }

    public void setSemestreHistorico(String semestreHistorico) {
        this.semestreHistorico = semestreHistorico;
    }

//    public Boolean getInclusaoDisciplinaForaGrade() {
//        if (inclusaoDisciplinaForaGrade == null) {
//            inclusaoDisciplinaForaGrade = Boolean.FALSE;
//        }
//        return inclusaoDisciplinaForaGrade;
//    }
//
//    public void setInclusaoDisciplinaForaGrade(Boolean inclusaoDisciplinaForaGrade) {
//        this.inclusaoDisciplinaForaGrade = inclusaoDisciplinaForaGrade;
//    }

    @XmlElement(name = "cidade")
    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @XmlElement(name = "estado")
    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlElement(name = "gradeDisciplinaVO")
    public GradeDisciplinaVO getGradeDisciplinaVO() {
        if (gradeDisciplinaVO == null) {
            gradeDisciplinaVO = new GradeDisciplinaVO();
        }
        return gradeDisciplinaVO;
    }

    public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
        this.gradeDisciplinaVO = gradeDisciplinaVO;
    }

    @XmlElement(name = "cidadeVO")
    public CidadeVO getCidadeVO() {
        if (cidadeVO == null) {
            cidadeVO = new CidadeVO();
        }
        return cidadeVO;
    }

    public void setCidadeVO(CidadeVO cidadeVO) {
        this.cidadeVO = cidadeVO;
    }

    @XmlElement(name = "cargaHorariaAproveitamentoDisciplina")
    public Integer getCargaHorariaAproveitamentoDisciplina() {
        if (cargaHorariaAproveitamentoDisciplina == null) {
            cargaHorariaAproveitamentoDisciplina = 0;
        }
        return cargaHorariaAproveitamentoDisciplina;
    }

    public void setCargaHorariaAproveitamentoDisciplina(Integer cargaHorariaAproveitamentoDisciplina) {
        this.cargaHorariaAproveitamentoDisciplina = cargaHorariaAproveitamentoDisciplina;
    }

    //NOTA 14 *********************
    @XmlElement(name = "nota14")
    public Double getNota14() {
        return (nota14);
    }

    public void setNota14(Double nota14) {
        this.nota14 = nota14;
    }
    @XmlElement(name = "titulo14")
    public String getTitulo14() {
        if (titulo14 == null) {
            titulo14 = "";
        }
        return titulo14;
    }
    public void setTitulo14(String titulo14) {
        this.titulo14 = titulo14;
    }
    @XmlElement(name = "nota14Lancada")
    public Boolean getNota14Lancada() {
        if (nota14Lancada == null) {
            nota14Lancada = false;
        }
        return nota14Lancada;
    }
    public void setNota14Lancada(Boolean nota14Lancada) {
        this.nota14Lancada = nota14Lancada;
    }    
    @XmlElement(name = "nota14Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota14Conceito() {
        if (nota14Conceito == null) {
            nota14Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota14Conceito;
    }
    public void setNota14Conceito(ConfiguracaoAcademicoNotaConceitoVO nota14Conceito) {
        this.nota14Conceito = nota14Conceito;
    }    
    @XmlElement(name = "nota14Texto")
    public String getNota14Texto() {
        if (nota14Texto == null) {
            nota14Texto = "";
        }
        return nota14Texto;
    }
    public void setNota14Texto(String nota14Texto) {
        this.nota14Texto = nota14Texto;
    }    
    //***************
    
    //NOTA 15 *********************
    @XmlElement(name = "nota15")
    public Double getNota15() {
        return (nota15);
    }

    public void setNota15(Double nota15) {
        this.nota15 = nota15;
    }
    @XmlElement(name = "titulo15")
    public String getTitulo15() {
        if (titulo15 == null) {
            titulo15 = "";
        }
        return titulo15;
    }
    public void setTitulo15(String titulo15) {
        this.titulo15 = titulo15;
    }
    @XmlElement(name = "nota15Lancada")
    public Boolean getNota15Lancada() {
        if (nota15Lancada == null) {
            nota15Lancada = false;
        }
        return nota15Lancada;
    }
    public void setNota15Lancada(Boolean nota15Lancada) {
        this.nota15Lancada = nota15Lancada;
    }    
    @XmlElement(name = "nota15Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota15Conceito() {
        if (nota15Conceito == null) {
            nota15Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota15Conceito;
    }
    public void setNota15Conceito(ConfiguracaoAcademicoNotaConceitoVO nota15Conceito) {
        this.nota15Conceito = nota15Conceito;
    }
    @XmlElement(name = "nota15Texto")
    public String getNota15Texto() {
        if (nota15Texto == null) {
            nota15Texto = "";
        }
        return nota15Texto;
    }
    public void setNota15Texto(String nota15Texto) {
        this.nota15Texto = nota15Texto;
    }    
    //***************

    //NOTA 16 *********************
    @XmlElement(name = "nota16")
    public Double getNota16() {
        return (nota16);
    }

    public void setNota16(Double nota16) {
        this.nota16 = nota16;
    }
    @XmlElement(name = "titulo16")
    public String getTitulo16() {
        if (titulo16 == null) {
            titulo16 = "";
        }
        return titulo16;
    }
    public void setTitulo16(String titulo16) {
        this.titulo16 = titulo16;
    }
    @XmlElement(name = "nota16Lancada")
    public Boolean getNota16Lancada() {
        if (nota16Lancada == null) {
            nota16Lancada = false;
        }
        return nota16Lancada;
    }
    public void setNota16Lancada(Boolean nota16Lancada) {
        this.nota16Lancada = nota16Lancada;
    }    
    @XmlElement(name = "nota16Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota16Conceito() {
        if (nota16Conceito == null) {
            nota16Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota16Conceito;
    }
    public void setNota16Conceito(ConfiguracaoAcademicoNotaConceitoVO nota16Conceito) {
        this.nota16Conceito = nota16Conceito;
    }        
    @XmlElement(name = "nota16Texto")
    public String getNota16Texto() {
        if (nota16Texto == null) {
            nota16Texto = "";
        }
        return nota16Texto;
    }
    public void setNota16Texto(String nota16Texto) {
        this.nota16Texto = nota16Texto;
    }    
    //***************

    //NOTA 17 *********************
    @XmlElement(name = "nota17")
    public Double getNota17() {
        return (nota17);
    }

    public void setNota17(Double nota17) {
        this.nota17 = nota17;
    }
    @XmlElement(name = "titulo17")
    public String getTitulo17() {
        if (titulo17 == null) {
            titulo17 = "";
        }
        return titulo17;
    }
    public void setTitulo17(String titulo17) {
        this.titulo17 = titulo17;
    }
    @XmlElement(name = "nota17Lancada")
    public Boolean getNota17Lancada() {
        if (nota17Lancada == null) {
            nota17Lancada = false;
        }
        return nota17Lancada;
    }
    public void setNota17Lancada(Boolean nota17Lancada) {
        this.nota17Lancada = nota17Lancada;
    }    
    @XmlElement(name = "nota17Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota17Conceito() {
        if (nota17Conceito == null) {
            nota17Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota17Conceito;
    }
    public void setNota17Conceito(ConfiguracaoAcademicoNotaConceitoVO nota17Conceito) {
        this.nota17Conceito = nota17Conceito;
    }        
    @XmlElement(name = "nota17Texto")
    public String getNota17Texto() {
        if (nota17Texto == null) {
            nota17Texto = "";
        }
        return nota17Texto;
    }
    public void setNota17Texto(String nota17Texto) {
        this.nota17Texto = nota17Texto;
    }    
    //***************

    //NOTA 18 *********************
    @XmlElement(name = "nota18")
    public Double getNota18() {
        return (nota18);
    }

    public void setNota18(Double nota18) {
        this.nota18 = nota18;
    }
    @XmlElement(name = "titulo18")
    public String getTitulo18() {
        if (titulo18 == null) {
            titulo18 = "";
        }
        return titulo18;
    }
    public void setTitulo18(String titulo18) {
        this.titulo18 = titulo18;
    }
    @XmlElement(name = "nota18Lancada")
    public Boolean getNota18Lancada() {
        if (nota18Lancada == null) {
            nota18Lancada = false;
        }
        return nota18Lancada;
    }
    public void setNota18Lancada(Boolean nota18Lancada) {
        this.nota18Lancada = nota18Lancada;
    }    
    @XmlElement(name = "nota18Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota18Conceito() {
        if (nota18Conceito == null) {
            nota18Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota18Conceito;
    }
    public void setNota18Conceito(ConfiguracaoAcademicoNotaConceitoVO nota18Conceito) {
        this.nota18Conceito = nota18Conceito;
    }        
    @XmlElement(name = "nota18Texto")
    public String getNota18Texto() {
        if (nota18Texto == null) {
            nota18Texto = "";
        }
        return nota18Texto;
    }
    public void setNota18Texto(String nota18Texto) {
        this.nota18Texto = nota18Texto;
    }    
    //***************

    //NOTA 19 *********************
    @XmlElement(name = "nota19")
    public Double getNota19() {
        return (nota19);
    }

    public void setNota19(Double nota19) {
        this.nota19 = nota19;
    }
    @XmlElement(name = "titulo19")
    public String getTitulo19() {
        if (titulo19 == null) {
            titulo19 = "";
        }
        return titulo19;
    }
    public void setTitulo19(String titulo19) {
        this.titulo19 = titulo19;
    }
    @XmlElement(name = "nota19Lancada")
    public Boolean getNota19Lancada() {
        if (nota19Lancada == null) {
            nota19Lancada = false;
        }
        return nota19Lancada;
    }
    public void setNota19Lancada(Boolean nota19Lancada) {
        this.nota19Lancada = nota19Lancada;
    }    
    @XmlElement(name = "nota19Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota19Conceito() {
        if (nota19Conceito == null) {
            nota19Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota19Conceito;
    }
    public void setNota19Conceito(ConfiguracaoAcademicoNotaConceitoVO nota19Conceito) {
        this.nota19Conceito = nota19Conceito;
    }        
    @XmlElement(name = "nota19Texto")
    public String getNota19Texto() {
        if (nota19Texto == null) {
            nota19Texto = "";
        }
        return nota19Texto;
    }
    public void setNota19Texto(String nota19Texto) {
        this.nota19Texto = nota19Texto;
    }    
    //***************

    //NOTA 20 *********************
    @XmlElement(name = "nota20")
    public Double getNota20() {
        return (nota20);
    }

    public void setNota20(Double nota20) {
        this.nota20 = nota20;
    }
    @XmlElement(name = "titulo20")
    public String getTitulo20() {
        if (titulo20 == null) {
            titulo20 = "";
        }
        return titulo20;
    }
    public void setTitulo20(String titulo20) {
        this.titulo20 = titulo20;
    }
    @XmlElement(name = "nota20Lancada")
    public Boolean getNota20Lancada() {
        if (nota20Lancada == null) {
            nota20Lancada = false;
        }
        return nota20Lancada;
    }
    public void setNota20Lancada(Boolean nota20Lancada) {
        this.nota20Lancada = nota20Lancada;
    }    
    @XmlElement(name = "nota20Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota20Conceito() {
        if (nota20Conceito == null) {
            nota20Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota20Conceito;
    }
    public void setNota20Conceito(ConfiguracaoAcademicoNotaConceitoVO nota20Conceito) {
        this.nota20Conceito = nota20Conceito;
    }        
    @XmlElement(name = "nota20Texto")
    public String getNota20Texto() {
        if (nota20Texto == null) {
            nota20Texto = "";
        }
        return nota20Texto;
    }
    public void setNota20Texto(String nota20Texto) {
        this.nota20Texto = nota20Texto;
    }    
    //***************

    //NOTA 21 *********************
    @XmlElement(name = "nota21")
    public Double getNota21() {
        return (nota21);
    }

    public void setNota21(Double nota21) {
        this.nota21 = nota21;
    }
    @XmlElement(name = "titulo21")
    public String getTitulo21() {
        if (titulo21 == null) {
            titulo21 = "";
        }
        return titulo21;
    }
    public void setTitulo21(String titulo21) {
        this.titulo21 = titulo21;
    }
    @XmlElement(name = "nota21Lancada")
    public Boolean getNota21Lancada() {
        if (nota21Lancada == null) {
            nota21Lancada = false;
        }
        return nota21Lancada;
    }
    public void setNota21Lancada(Boolean nota21Lancada) {
        this.nota21Lancada = nota21Lancada;
    }    
    @XmlElement(name = "nota21Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota21Conceito() {
        if (nota21Conceito == null) {
            nota21Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota21Conceito;
    }
    public void setNota21Conceito(ConfiguracaoAcademicoNotaConceitoVO nota21Conceito) {
        this.nota21Conceito = nota21Conceito;
    }        
    @XmlElement(name = "nota21Texto")
    public String getNota21Texto() {
        if (nota21Texto == null) {
            nota21Texto = "";
        }
        return nota21Texto;
    }
    public void setNota21Texto(String nota21Texto) {
        this.nota21Texto = nota21Texto;
    }    
    //***************

    //NOTA 22 *********************
    @XmlElement(name = "nota22")
    public Double getNota22() {
        return (nota22);
    }

    public void setNota22(Double nota22) {
        this.nota22 = nota22;
    }
    @XmlElement(name = "titulo22")
    public String getTitulo22() {
        if (titulo22 == null) {
            titulo22 = "";
        }
        return titulo22;
    }
    public void setTitulo22(String titulo22) {
        this.titulo22 = titulo22;
    }
    @XmlElement(name = "nota22Lancada")
    public Boolean getNota22Lancada() {
        if (nota22Lancada == null) {
            nota22Lancada = false;
        }
        return nota22Lancada;
    }
    public void setNota22Lancada(Boolean nota22Lancada) {
        this.nota22Lancada = nota22Lancada;
    }    
    @XmlElement(name = "nota22Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota22Conceito() {
        if (nota22Conceito == null) {
            nota22Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota22Conceito;
    }
    public void setNota22Conceito(ConfiguracaoAcademicoNotaConceitoVO nota22Conceito) {
        this.nota22Conceito = nota22Conceito;
    }        
    @XmlElement(name = "nota22Texto")
    public String getNota22Texto() {
        if (nota22Texto == null) {
            nota22Texto = "";
        }
        return nota22Texto;
    }
    public void setNota22Texto(String nota22Texto) {
        this.nota22Texto = nota22Texto;
    }    
    //***************

    //NOTA 23 *********************
    @XmlElement(name = "nota23")
    public Double getNota23() {
        return (nota23);
    }

    public void setNota23(Double nota23) {
        this.nota23 = nota23;
    }
    @XmlElement(name = "titulo23")
    public String getTitulo23() {
        if (titulo23 == null) {
            titulo23 = "";
        }
        return titulo23;
    }
    public void setTitulo23(String titulo23) {
        this.titulo23 = titulo23;
    }
    @XmlElement(name = "nota23Lancada")
    public Boolean getNota23Lancada() {
        if (nota23Lancada == null) {
            nota23Lancada = false;
        }
        return nota23Lancada;
    }
    public void setNota23Lancada(Boolean nota23Lancada) {
        this.nota23Lancada = nota23Lancada;
    }    
    @XmlElement(name = "nota23Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota23Conceito() {
        if (nota23Conceito == null) {
            nota23Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota23Conceito;
    }
    public void setNota23Conceito(ConfiguracaoAcademicoNotaConceitoVO nota23Conceito) {
        this.nota23Conceito = nota23Conceito;
    }        
    @XmlElement(name = "nota23Texto")
    public String getNota23Texto() {
        if (nota23Texto == null) {
            nota23Texto = "";
        }
        return nota23Texto;
    }
    public void setNota23Texto(String nota23Texto) {
        this.nota23Texto = nota23Texto;
    }    
    //***************

    //NOTA 24 *********************
    @XmlElement(name = "nota24")
    public Double getNota24() {
        return (nota24);
    }

    public void setNota24(Double nota24) {
        this.nota24 = nota24;
    }
    @XmlElement(name = "titulo24")
    public String getTitulo24() {
        if (titulo24 == null) {
            titulo24 = "";
        }
        return titulo24;
    }
    public void setTitulo24(String titulo24) {
        this.titulo24 = titulo24;
    }
    @XmlElement(name = "nota24Lancada")
    public Boolean getNota24Lancada() {
        if (nota24Lancada == null) {
            nota24Lancada = false;
        }
        return nota24Lancada;
    }
    public void setNota24Lancada(Boolean nota24Lancada) {
        this.nota24Lancada = nota24Lancada;
    }    
    @XmlElement(name = "nota24Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota24Conceito() {
        if (nota24Conceito == null) {
            nota24Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota24Conceito;
    }
    public void setNota24Conceito(ConfiguracaoAcademicoNotaConceitoVO nota24Conceito) {
        this.nota24Conceito = nota24Conceito;
    }        
    @XmlElement(name = "nota24Texto")
    public String getNota24Texto() {
        if (nota24Texto == null) {
            nota24Texto = "";
        }
        return nota24Texto;
    }
    public void setNota24Texto(String nota24Texto) {
        this.nota24Texto = nota24Texto;
    }    
    
    //***************

    //NOTA 25 *********************
    @XmlElement(name = "nota25")
    public Double getNota25() {
        return (nota25);
    }

    public void setNota25(Double nota25) {
        this.nota25 = nota25;
    }
    @XmlElement(name = "titulo25")
    public String getTitulo25() {
        if (titulo25 == null) {
            titulo25 = "";
        }
        return titulo25;
    }
    public void setTitulo25(String titulo25) {
        this.titulo25 = titulo25;
    }
    @XmlElement(name = "nota25Lancada")
    public Boolean getNota25Lancada() {
        if (nota25Lancada == null) {
            nota25Lancada = false;
        }
        return nota25Lancada;
    }
    public void setNota25Lancada(Boolean nota25Lancada) {
        this.nota25Lancada = nota25Lancada;
    }    
    @XmlElement(name = "nota25Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota25Conceito() {
        if (nota25Conceito == null) {
            nota25Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota25Conceito;
    }
    public void setNota25Conceito(ConfiguracaoAcademicoNotaConceitoVO nota25Conceito) {
        this.nota25Conceito = nota25Conceito;
    }            
    @XmlElement(name = "nota25Texto")
    public String getNota25Texto() {
        if (nota25Texto == null) {
            nota25Texto = "";
        }
        return nota25Texto;
    }
    public void setNota25Texto(String nota25Texto) {
        this.nota25Texto = nota25Texto;
    }        
    //***************

    //NOTA 26 *********************
    @XmlElement(name = "nota26")
    public Double getNota26() {
        return (nota26);
    }

    public void setNota26(Double nota26) {
        this.nota26 = nota26;
    }
    @XmlElement(name = "titulo26")
    public String getTitulo26() {
        if (titulo26 == null) {
            titulo26 = "";
        }
        return titulo26;
    }
    public void setTitulo26(String titulo26) {
        this.titulo26 = titulo26;
    }
    @XmlElement(name = "nota26Lancada")
    public Boolean getNota26Lancada() {
        if (nota26Lancada == null) {
            nota26Lancada = false;
        }
        return nota26Lancada;
    }
    public void setNota26Lancada(Boolean nota26Lancada) {
        this.nota26Lancada = nota26Lancada;
    }    
    @XmlElement(name = "nota26Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota26Conceito() {
        if (nota26Conceito == null) {
            nota26Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota26Conceito;
    }
    public void setNota26Conceito(ConfiguracaoAcademicoNotaConceitoVO nota26Conceito) {
        this.nota26Conceito = nota26Conceito;
    }            
    @XmlElement(name = "nota26Texto")
    public String getNota26Texto() {
        if (nota26Texto == null) {
            nota26Texto = "";
        }
        return nota26Texto;
    }
    public void setNota26Texto(String nota26Texto) {
        this.nota26Texto = nota26Texto;
    }        
    //***************

    //NOTA 27 *********************
    @XmlElement(name = "nota27")
    public Double getNota27() {
        return (nota27);
    }

    public void setNota27(Double nota27) {
        this.nota27 = nota27;
    }
    @XmlElement(name = "titulo27")
    public String getTitulo27() {
        if (titulo27 == null) {
            titulo27 = "";
        }
        return titulo27;
    }
    public void setTitulo27(String titulo27) {
        this.titulo27 = titulo27;
    }
    @XmlElement(name = "nota27Lancada")
    public Boolean getNota27Lancada() {
        if (nota27Lancada == null) {
            nota27Lancada = false;
        }
        return nota27Lancada;
    }
    public void setNota27Lancada(Boolean nota27Lancada) {
        this.nota27Lancada = nota27Lancada;
    }    
    @XmlElement(name = "nota27Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota27Conceito() {
        if (nota27Conceito == null) {
            nota27Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota27Conceito;
    }
    public void setNota27Conceito(ConfiguracaoAcademicoNotaConceitoVO nota27Conceito) {
        this.nota27Conceito = nota27Conceito;
    }            
    @XmlElement(name = "nota27Texto")
    public String getNota27Texto() {
        if (nota27Texto == null) {
            nota27Texto = "";
        }
        return nota27Texto;
    }
    public void setNota27Texto(String nota27Texto) {
        this.nota27Texto = nota27Texto;
    }        
    //***************

    //NOTA 28 *********************
    @XmlElement(name = "nota28")
    public Double getNota28() {
        return (nota28);
    }

    public void setNota28(Double nota28) {
        this.nota28 = nota28;
    }
    @XmlElement(name = "titulo28")
    public String getTitulo28() {
        if (titulo28 == null) {
            titulo28 = "";
        }
        return titulo28;
    }
    public void setTitulo28(String titulo28) {
        this.titulo28 = titulo28;
    }
    @XmlElement(name = "nota28Lancada")
    public Boolean getNota28Lancada() {
        if (nota28Lancada == null) {
            nota28Lancada = false;
        }
        return nota28Lancada;
    }
    public void setNota28Lancada(Boolean nota28Lancada) {
        this.nota28Lancada = nota28Lancada;
    }    
    @XmlElement(name = "nota28Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota28Conceito() {
        if (nota28Conceito == null) {
            nota28Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota28Conceito;
    }
    public void setNota28Conceito(ConfiguracaoAcademicoNotaConceitoVO nota28Conceito) {
        this.nota28Conceito = nota28Conceito;
    }            
    @XmlElement(name = "nota28Texto")
    public String getNota28Texto() {
        if (nota28Texto == null) {
            nota28Texto = "";
        }
        return nota28Texto;
    }
    public void setNota28Texto(String nota28Texto) {
        this.nota28Texto = nota28Texto;
    }        
    //***************

    //NOTA 29 *********************
    @XmlElement(name = "nota29")
    public Double getNota29() {
        return (nota29);
    }

    public void setNota29(Double nota29) {
        this.nota29 = nota29;
    }
    @XmlElement(name = "titulo29")
    public String getTitulo29() {
        if (titulo29 == null) {
            titulo29 = "";
        }
        return titulo29;
    }
    public void setTitulo29(String titulo29) {
        this.titulo29 = titulo29;
    }
    @XmlElement(name = "nota29Lancada")
    public Boolean getNota29Lancada() {
        if (nota29Lancada == null) {
            nota29Lancada = false;
        }
        return nota29Lancada;
    }
    public void setNota29Lancada(Boolean nota29Lancada) {
        this.nota29Lancada = nota29Lancada;
    }    
    @XmlElement(name = "nota29Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota29Conceito() {
        if (nota29Conceito == null) {
            nota29Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota29Conceito;
    }
    public void setNota29Conceito(ConfiguracaoAcademicoNotaConceitoVO nota29Conceito) {
        this.nota29Conceito = nota29Conceito;
    }            
    @XmlElement(name = "nota29Texto")
    public String getNota29Texto() {
        if (nota29Texto == null) {
            nota29Texto = "";
        }
        return nota29Texto;
    }
    public void setNota29Texto(String nota29Texto) {
        this.nota29Texto = nota29Texto;
    }
    //***************

    //NOTA 30 *********************
    @XmlElement(name = "nota30")
    public Double getNota30() {
        return (nota30);
    }

    public void setNota30(Double nota30) {
        this.nota30 = nota30;
    }
    @XmlElement(name = "titulo30")
    public String getTitulo30() {
        if (titulo30 == null) {
            titulo30 = "";
        }
        return titulo30;
    }
    public void setTitulo30(String titulo30) {
        this.titulo30 = titulo30;
    }
    @XmlElement(name = "nota30Lancada")
    public Boolean getNota30Lancada() {
        if (nota30Lancada == null) {
            nota30Lancada = false;
        }
        return nota30Lancada;
    }
    public void setNota30Lancada(Boolean nota30Lancada) {
        this.nota30Lancada = nota30Lancada;
    }    
    @XmlElement(name = "nota30Conceito")
    public ConfiguracaoAcademicoNotaConceitoVO getNota30Conceito() {
        if (nota30Conceito == null) {
            nota30Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
        }
        return nota30Conceito;
    }
    public void setNota30Conceito(ConfiguracaoAcademicoNotaConceitoVO nota30Conceito) {
        this.nota30Conceito = nota30Conceito;
    }            
    @XmlElement(name = "nota30Texto")
    public String getNota30Texto() {
        if (nota30Texto == null) {
            nota30Texto = "";
        }
        return nota30Texto;
    }
    public void setNota30Texto(String nota30Texto) {
        this.nota30Texto = nota30Texto;
    }
    //***************

    @XmlElement(name = "cargaHorariaCursada")
	public Integer getCargaHorariaCursada() {
		if(cargaHorariaCursada == null){
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	@XmlElement(name = "isentarMediaFinal")
	public Boolean getIsentarMediaFinal() {
		if (isentarMediaFinal == null) {
			isentarMediaFinal = Boolean.FALSE;
		}
		return isentarMediaFinal;
	}

	public void setIsentarMediaFinal(Boolean isentarMediaFinal) {
		this.isentarMediaFinal = isentarMediaFinal;
	}

	@XmlElement(name = "cargaHorariaDisciplina")
	public Integer getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = 0;
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	@XmlElement(name = "historicoDisciplinaOptativa")
	public Boolean getHistoricoDisciplinaOptativa() {
		if (historicoDisciplinaOptativa == null) {
			historicoDisciplinaOptativa = false;
		}
		return historicoDisciplinaOptativa;
	}

	public void setHistoricoDisciplinaOptativa(Boolean historicoDisciplinaOptativa) {
		this.historicoDisciplinaOptativa = historicoDisciplinaOptativa;
	}

	@XmlElement(name = "historicoDisciplinaForaGrade")
	public Boolean getHistoricoDisciplinaForaGrade() {
		if (historicoDisciplinaForaGrade == null) {
			historicoDisciplinaForaGrade = false;
		}
		return historicoDisciplinaForaGrade;
	}

	public void setHistoricoDisciplinaForaGrade(Boolean historicoDisciplinaForaGrade) {
		this.historicoDisciplinaForaGrade = historicoDisciplinaForaGrade;
	}

	@XmlElement(name = "nomeDisciplina")
	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}
	

	@XmlElement(name = "historicoDisciplinaAproveitada")
	public Boolean getHistoricoDisciplinaAproveitada() {
		if (historicoDisciplinaAproveitada == null) {
			historicoDisciplinaAproveitada = false;
		}
		return historicoDisciplinaAproveitada;
	}

	public void setHistoricoDisciplinaAproveitada(Boolean historicoDisciplinaAproveitada) {
		this.historicoDisciplinaAproveitada = historicoDisciplinaAproveitada;
	}

	@XmlElement(name = "historicoPorEquivalencia")
	public Boolean getHistoricoPorEquivalencia() {
		if (historicoPorEquivalencia == null) {
			historicoPorEquivalencia = false;
		}
		return historicoPorEquivalencia;
	}

	public void setHistoricoPorEquivalencia(Boolean historicoPorEquivalencia) {
		this.historicoPorEquivalencia = historicoPorEquivalencia;
	}

	@XmlElement(name = "historicoEquivalente")
	public Boolean getHistoricoEquivalente() {
		if (historicoEquivalente == null) {
			historicoEquivalente = false;
		}
		return historicoEquivalente;
	}

	public void setHistoricoEquivalente(Boolean historicoEquivalente) {
		this.historicoEquivalente = historicoEquivalente;
	}

	@XmlElement(name = "faltaPrimeiroBimestre")
	public Integer getFaltaPrimeiroBimestre() {
		if (faltaPrimeiroBimestre == null) {
			faltaPrimeiroBimestre = 0;
		}
		return faltaPrimeiroBimestre;
	}

	public void setFaltaPrimeiroBimestre(Integer faltaPrimeiroBimestre) {
		this.faltaPrimeiroBimestre = faltaPrimeiroBimestre;
	}

	@XmlElement(name = "faltaSegundoBimestre")
	public Integer getFaltaSegundoBimestre() {
		if (faltaSegundoBimestre == null) {
			faltaSegundoBimestre = 0;
		}
		return faltaSegundoBimestre;
	}

	public void setFaltaSegundoBimestre(Integer faltaSegundoBimestre) {
		this.faltaSegundoBimestre = faltaSegundoBimestre;
	}

	@XmlElement(name = "faltaTerceiroBimestre")
	public Integer getFaltaTerceiroBimestre() {
		if (faltaTerceiroBimestre == null) {
			faltaTerceiroBimestre = 0;
		}
		return faltaTerceiroBimestre;
	}

	public void setFaltaTerceiroBimestre(Integer faltaTerceiroBimestre) {
		this.faltaTerceiroBimestre = faltaTerceiroBimestre;
	}

	@XmlElement(name = "faltaQuartoBimestre")
	public Integer getFaltaQuartoBimestre() {
		if (faltaQuartoBimestre == null) {
			faltaQuartoBimestre = 0;
		}
		return faltaQuartoBimestre;
	}

	public void setFaltaQuartoBimestre(Integer faltaQuartoBimestre) {
		this.faltaQuartoBimestre = faltaQuartoBimestre;
	}

	 @XmlElement(name = "totalFaltas")
	public Integer getTotalFalta() {
		if (totalFalta == null) {
			totalFalta = 0;
		}
		return totalFalta;
	}

	public void setTotalFalta(Integer totalFalta) {
		this.totalFalta = totalFalta;
	}

	@XmlElement(name = "periodoLetivoMatrizCurricular")
	public PeriodoLetivoVO getPeriodoLetivoMatrizCurricular() {
		if (periodoLetivoMatrizCurricular == null) {
			periodoLetivoMatrizCurricular = new PeriodoLetivoVO();
		}
		return periodoLetivoMatrizCurricular;
	}

	public void setPeriodoLetivoMatrizCurricular(PeriodoLetivoVO periodoLetivoMatrizCurricular) {
		this.periodoLetivoMatrizCurricular = periodoLetivoMatrizCurricular;
	}

	@XmlElement(name = "periodoLetivoCursada")
	public PeriodoLetivoVO getPeriodoLetivoCursada() {
		if (periodoLetivoCursada == null) {
			periodoLetivoCursada = new PeriodoLetivoVO();
		}
		return periodoLetivoCursada;
	}

	public void setPeriodoLetivoCursada(PeriodoLetivoVO periodoLetivoCursada) {
		this.periodoLetivoCursada = periodoLetivoCursada;
	}
	
	@XmlElement(name = "matrizCurricular")
	public GradeCurricularVO getMatrizCurricular() {
		if (matrizCurricular == null) {
			matrizCurricular = new GradeCurricularVO();
		}
		return matrizCurricular;
	}

	public void setMatrizCurricular(GradeCurricularVO matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}

	@XmlElement(name = "utilizaNotaFinalConceito")
	public Boolean getUtilizaNotaFinalConceito() {
		if (utilizaNotaFinalConceito == null) {
			utilizaNotaFinalConceito = false;
		}
		return utilizaNotaFinalConceito;
	}

	public void setUtilizaNotaFinalConceito(Boolean utilizaNotaFinalConceito) {
		this.utilizaNotaFinalConceito = utilizaNotaFinalConceito;
	}

	@XmlElement(name = "notaFinalConceito")
	public String getNotaFinalConceito() {
		if (notaFinalConceito == null) {
			notaFinalConceito = "";
		}
		return notaFinalConceito;
	}

	public void setNotaFinalConceito(String notaFinalConceito) {
		this.notaFinalConceito = notaFinalConceito;
	}

	@XmlElement(name = "creditoDisciplina")
	public Integer getCreditoDisciplina() {
		if (creditoDisciplina == null) {
			creditoDisciplina = 0;
		}
		return creditoDisciplina;
	}

	public void setCreditoDisciplina(Integer creditoDisciplina) {
		this.creditoDisciplina = creditoDisciplina;
	}
	
	/**
         * Método responsável por determinar que um aluno reprovou por nota
         * e nao por falta/abandono. Isto é importante para o controle do SEI
         * que determina se o aluno pode ou nao fazer uma disciplina em regime
         * especial (ver configuracao academica do sistema). Caso aluno
         * tenha sido reprovado por falta, existe a possibilidade do mesmo
         * cursá-la em regime especial (sem assistir a aulas presenciais), fazendo
         * somente atividades supervisionadas por um professor.
         * @return boolean
         */
        public Boolean getAlunoReprovadoPorNotaENaoPorFaltaOuAbandono() {
            SituacaoHistorico situacaoEnum = SituacaoHistorico.getEnum(getSituacao());
            if ((situacaoEnum.equals(SituacaoHistorico.REPROVADO)) ||
                (situacaoEnum.equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO))) { // esta situacao só fica registrada para alunos reprovados por falta
                return Boolean.TRUE;
            }
            return Boolean.FALSE; 
        }

    /**
     * @return the gradeCurricularGrupoOptativaDisciplinaVO
     */
    @XmlElement(name = "gradeCurricularGrupoOptativaDisciplinaVO")
    public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
        if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
            gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
        }
        return gradeCurricularGrupoOptativaDisciplinaVO;
    }

    /**
     * @param gradeCurricularGrupoOptativaDisciplinaVO the gradeCurricularGrupoOptativaDisciplinaVO to set
     */
    public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
        this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
    }

    /**
     * @return the gradeDisciplinaComposta
     */
    
    @XmlElement(name = "gradeDisciplinaComposta")
    public GradeDisciplinaCompostaVO getGradeDisciplinaComposta() {
        if (gradeDisciplinaComposta == null) {
            gradeDisciplinaComposta = new GradeDisciplinaCompostaVO();
        }
        return gradeDisciplinaComposta;
    }

    /**
     * @param gradeDisciplinaComposta the gradeDisciplinaComposta to set
     */
    public void setGradeDisciplinaComposta(GradeDisciplinaCompostaVO gradeDisciplinaComposta) {
        this.gradeDisciplinaComposta = gradeDisciplinaComposta;
    }
    
    @Override
    public String toString() {
        return "[Disciplina: " + this.getDisciplina().getCodigo() + "-" + this.getDisciplina().getNome() + " => " + this.getAnoHistorico() + "-" + this.getSemestreHistorico() + " Média Final: " + this.getMediaFinal_Apresentar() + 
                " Situação: " + this.getSituacao_Apresentar() + " Grade: " + this.getMatrizCurricular().getCodigo();
    }

    @XmlElement(name = "historicoDisciplinaAtividadeComplementar")
	public Boolean getHistoricoDisciplinaAtividadeComplementar() {
		if (historicoDisciplinaAtividadeComplementar == null) {
			historicoDisciplinaAtividadeComplementar = false;
		}
		return historicoDisciplinaAtividadeComplementar;
	}

	public void setHistoricoDisciplinaAtividadeComplementar(Boolean historicoDisciplinaAtividadeComplementar) {
		this.historicoDisciplinaAtividadeComplementar = historicoDisciplinaAtividadeComplementar;
	}

	@XmlElement(name = "observacao")
	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@XmlElement(name = "totalDiaLetivoAno")
	public Integer getTotalDiaLetivoAno() {
		if (totalDiaLetivoAno == null) {
			totalDiaLetivoAno = 0;
		}
		return totalDiaLetivoAno;
	}

	public void setTotalDiaLetivoAno(Integer totalDiaLetivoAno) {
		this.totalDiaLetivoAno = totalDiaLetivoAno;
	}

        /**
        * Campo que mantém o vinculo com historico equivalente
        * @deprecated na versão 5.0 - passou a ser adotado o mapa de equivalencia N:M
        */
	public HistoricoEquivalenteVO getHistoricoEquivalenteVO() {
		if (historicoEquivalenteVO == null) {
			historicoEquivalenteVO = new HistoricoEquivalenteVO();
		}
		return historicoEquivalenteVO;
	}

        /**
        * Campo que mantém o vinculo com historico equivalente
        * @deprecated na versão 5.0 - passou a ser adotado o mapa de equivalencia N:M
        */
	public void setHistoricoEquivalenteVO(HistoricoEquivalenteVO historicoEquivalenteVO) {
		this.historicoEquivalenteVO = historicoEquivalenteVO;
	}


    /**
     * @return the mapaEquivalenciaDisciplinaCursada
     */
	@XmlElement(name = "mapaEquivalenciaDisciplinaCursada")
    public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursada() {
        if (mapaEquivalenciaDisciplinaCursada == null) {
            mapaEquivalenciaDisciplinaCursada = new MapaEquivalenciaDisciplinaCursadaVO();
        }
        return mapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @param mapaEquivalenciaDisciplinaCursada the mapaEquivalenciaDisciplinaCursada to set
     */
    public void setMapaEquivalenciaDisciplinaCursada(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada) {
        this.mapaEquivalenciaDisciplinaCursada = mapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @return the mapaEquivalenciaDisciplinaMatrizCurricular
     */
    @XmlElement(name = "mapaEquivalenciaDisciplinaMatrizCurricular")
    public MapaEquivalenciaDisciplinaMatrizCurricularVO getMapaEquivalenciaDisciplinaMatrizCurricular() {
        if (mapaEquivalenciaDisciplinaMatrizCurricular == null) {
            mapaEquivalenciaDisciplinaMatrizCurricular = new MapaEquivalenciaDisciplinaMatrizCurricularVO();
        }
        return mapaEquivalenciaDisciplinaMatrizCurricular;
    }

    /**
     * @param mapaEquivalenciaDisciplinaMatrizCurricular the mapaEquivalenciaDisciplinaMatrizCurricular to set
     */
    public void setMapaEquivalenciaDisciplinaMatrizCurricular(MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricular) {
        this.mapaEquivalenciaDisciplinaMatrizCurricular = mapaEquivalenciaDisciplinaMatrizCurricular;
    }

    /**
     * @return the disciplinaReferenteAUmGrupoOptativa
     */
    @XmlElement(name = "disciplinaReferenteAUmGrupoOptativa")
    public Boolean getDisciplinaReferenteAUmGrupoOptativa() {
        if (disciplinaReferenteAUmGrupoOptativa == null) {
            disciplinaReferenteAUmGrupoOptativa = Boolean.FALSE;
        }
        return disciplinaReferenteAUmGrupoOptativa;
    }

    /**
     * @param disciplinaReferenteAUmGrupoOptativa the disciplinaReferenteAUmGrupoOptativa to set
     */
    public void setDisciplinaReferenteAUmGrupoOptativa(Boolean disciplinaReferenteAUmGrupoOptativa) {
        this.disciplinaReferenteAUmGrupoOptativa = disciplinaReferenteAUmGrupoOptativa;
    }

    /**
     * @return the historicoDisciplinaComposta
     */
    @XmlElement(name = "historicoDisciplinaComposta")
    public Boolean getHistoricoDisciplinaComposta() {
        if (historicoDisciplinaComposta == null) {
            historicoDisciplinaComposta = Boolean.FALSE;
        }
        return historicoDisciplinaComposta;
    }

    /**
     * @param historicoDisciplinaComposta the historicoDisciplinaComposta to set
     */
    public void setHistoricoDisciplinaComposta(Boolean historicoDisciplinaComposta) {
        this.historicoDisciplinaComposta = historicoDisciplinaComposta;
    }

    /**
     * @return the historicoDisciplinaFazParteComposicao
     */
    @XmlElement(name = "historicoDisciplinaFazParteComposicao")
    public Boolean getHistoricoDisciplinaFazParteComposicao() {
        if (historicoDisciplinaFazParteComposicao == null) {
            historicoDisciplinaFazParteComposicao = Boolean.FALSE;
        }
        return historicoDisciplinaFazParteComposicao;
    }

    /**
     * @param historicoDisciplinaFazParteComposicao the historicoDisciplinaFazParteComposicao to set
     */
    public void setHistoricoDisciplinaFazParteComposicao(Boolean historicoDisciplinaFazParteComposicao) {
        this.historicoDisciplinaFazParteComposicao = historicoDisciplinaFazParteComposicao;
    }

    /**
     * @return the mapaEquivalenciaDisciplina
     */
    @XmlElement(name = "mapaEquivalenciaDisciplina")
    public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplina() {
        if (mapaEquivalenciaDisciplina == null) {
            mapaEquivalenciaDisciplina = new MapaEquivalenciaDisciplinaVO();
        }
        return mapaEquivalenciaDisciplina;
    }

    /**
     * @param mapaEquivalenciaDisciplina the mapaEquivalenciaDisciplina to set
     */
    public void setMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina) {
        this.mapaEquivalenciaDisciplina = mapaEquivalenciaDisciplina;
    }

    @XmlElement(name = "historicoCriterioAvaliacaoAluno")
	public Boolean getHistoricoCriterioAvaliacaoAluno() {
		if (historicoCriterioAvaliacaoAluno == null) {
			historicoCriterioAvaliacaoAluno = false;
		}
		return historicoCriterioAvaliacaoAluno;
	}

	public void setHistoricoCriterioAvaliacaoAluno(Boolean historicoCriterioAvaliacaoAluno) {
		this.historicoCriterioAvaliacaoAluno = historicoCriterioAvaliacaoAluno;
	}

	@XmlElement(name = "criterioAvaliacao")
	public CriterioAvaliacaoVO getCriterioAvaliacao() {
		if (criterioAvaliacao == null) {
			criterioAvaliacao = new CriterioAvaliacaoVO();
		}
		return criterioAvaliacao;
	}

	public void setCriterioAvaliacao(CriterioAvaliacaoVO criterioAvaliacao) {
		this.criterioAvaliacao = criterioAvaliacao;
	}
    
    public Boolean getCursandoPorEquivalencia() {
        if (this.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getTrancado() {
        if (this.getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return the transferenciaMatrizCurricularMatricula
     */
    @XmlElement(name = "transferenciaMatrizCurricularMatricula")
    public TransferenciaMatrizCurricularMatriculaVO getTransferenciaMatrizCurricularMatricula() {
        if (transferenciaMatrizCurricularMatricula == null) {
            transferenciaMatrizCurricularMatricula = new TransferenciaMatrizCurricularMatriculaVO();
        }
        return transferenciaMatrizCurricularMatricula;
    }

    /**
     * @param transferenciaMatrizCurricularMatricula the transferenciaMatrizCurricularMatricula to set
     */
    public void setTransferenciaMatrizCurricularMatricula(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatricula) {
        this.transferenciaMatrizCurricularMatricula = transferenciaMatrizCurricularMatricula;
    }

    /**
     * @return the historicoGeradoAPartirTransferenciaMatrizCurricular
     */
    @XmlElement(name = "historicoGeradoAPartirTransferenciaMatrizCurricular")
    public Boolean getHistoricoGeradoAPartirTransferenciaMatrizCurricular() {
        if (historicoGeradoAPartirTransferenciaMatrizCurricular == null) {
            historicoGeradoAPartirTransferenciaMatrizCurricular = Boolean.FALSE;
        }
        return historicoGeradoAPartirTransferenciaMatrizCurricular;
    }

    /**
     * @param historicoGeradoAPartirTransferenciaMatrizCurricular the historicoGeradoAPartirTransferenciaMatrizCurricular to set
     */
    public void setHistoricoGeradoAPartirTransferenciaMatrizCurricular(Boolean historicoGeradoAPartirTransferenciaMatrizCurricular) {
        this.historicoGeradoAPartirTransferenciaMatrizCurricular = historicoGeradoAPartirTransferenciaMatrizCurricular;
    }

    /**
     * @return the observacaoTransferenciaMatrizCurricular
     */
    @XmlElement(name = "observacaoTransferenciaMatrizCurricular")
    public String getObservacaoTransferenciaMatrizCurricular() {
        if (observacaoTransferenciaMatrizCurricular == null) {
            observacaoTransferenciaMatrizCurricular = "";
        }
        return observacaoTransferenciaMatrizCurricular;
    }

    /**
     * @param observacaoTransferenciaMatrizCurricular the observacaoTransferenciaMatrizCurricular to set
     */
    public void setObservacaoTransferenciaMatrizCurricular(String observacaoTransferenciaMatrizCurricular) {
        this.observacaoTransferenciaMatrizCurricular = observacaoTransferenciaMatrizCurricular;
    }

    public void adicionarObservacaoTransferenciaMatrizCurricular(Date data, String responsavel, String descricao, Boolean subordinado) {
        String base = this.getObservacaoTransferenciaMatrizCurricular();
        String quebraLinha = "/n";
        if (base.equals("")) {
            quebraLinha = "";
        }
        String dataResponsavel = "    -> ";
        if (!subordinado) {
            dataResponsavel = Uteis.getDataComHora(data) + " - " + responsavel + " : ";
        }
        base = base + quebraLinha + dataResponsavel + descricao;
        this.setObservacaoTransferenciaMatrizCurricular(base);
    }

    /**
     * @return the naoGerarHistoricoDisciplinasCursadasPorEquivalencia
     */
    @XmlElement(name = "naoGerarHistoricoDisciplinasCursadasPorEquivalencia")
    public Boolean getNaoGerarHistoricoDisciplinasCursadasPorEquivalencia() {
        if (naoGerarHistoricoDisciplinasCursadasPorEquivalencia == null) {
            naoGerarHistoricoDisciplinasCursadasPorEquivalencia = Boolean.FALSE;
        }
        return naoGerarHistoricoDisciplinasCursadasPorEquivalencia;
    }

    /**
     * @param naoGerarHistoricoDisciplinasCursadasPorEquivalencia the naoGerarHistoricoDisciplinasCursadasPorEquivalencia to set
     */
    public void setNaoGerarHistoricoDisciplinasCursadasPorEquivalencia(Boolean naoGerarHistoricoDisciplinasCursadasPorEquivalencia) {
        this.naoGerarHistoricoDisciplinasCursadasPorEquivalencia = naoGerarHistoricoDisciplinasCursadasPorEquivalencia;
    }

    /**
     * @return the historicoCursandoPorCorrespondenciaAposTransferencia
     */
    @XmlElement(name = "historicoCursandoPorCorrespondenciaAposTransferencia")
    public Boolean getHistoricoCursandoPorCorrespondenciaAposTransferencia() {
        if (historicoCursandoPorCorrespondenciaAposTransferencia == null) {
            historicoCursandoPorCorrespondenciaAposTransferencia = Boolean.FALSE;
        }
        return historicoCursandoPorCorrespondenciaAposTransferencia;
    }

    /**
     * @param historicoCursandoPorCorrespondenciaAposTransferencia the historicoCursandoPorCorrespondenciaAposTransferencia to set
     */
    public void setHistoricoCursandoPorCorrespondenciaAposTransferencia(Boolean historicoCursandoPorCorrespondenciaAposTransferencia) {
        this.historicoCursandoPorCorrespondenciaAposTransferencia = historicoCursandoPorCorrespondenciaAposTransferencia;
    }
    
    @XmlElement(name = "historicoNotaVOs")
	public List<HistoricoNotaVO> getHistoricoNotaVOs() {
		if (historicoNotaVOs == null) {
			historicoNotaVOs = new ArrayList<HistoricoNotaVO>(0);
		}
		return historicoNotaVOs;
	}

	public void setHistoricoNotaVOs(List<HistoricoNotaVO> historicoNotaVOs) {
		this.historicoNotaVOs = historicoNotaVOs;
	}

	@XmlElement(name = "parteDiversificada")
	public Boolean getParteDiversificada() {
		if (parteDiversificada == null) {
			parteDiversificada = false;
		}
		return parteDiversificada;
	}

	public void setParteDiversificada(Boolean parteDiversificada) {
		this.parteDiversificada = parteDiversificada;
	}

    /**
     * @return the nrAdvertencias1Bimestre
     */
    public Integer getNrAdvertencias1Bimestre() {
        if (nrAdvertencias1Bimestre == null) {
            nrAdvertencias1Bimestre = 0;
        }
        return nrAdvertencias1Bimestre;
    }

    /**
     * @param nrAdvertencias1Bimestre the nrAdvertencias1Bimestre to set
     */
    public void setNrAdvertencias1Bimestre(Integer nrAdvertencias1Bimestre) {
        this.nrAdvertencias1Bimestre = nrAdvertencias1Bimestre;
    }

    /**
     * @return the nrAdvertencias2Bimestre
     */
    public Integer getNrAdvertencias2Bimestre() {
        if (nrAdvertencias2Bimestre == null) {
            nrAdvertencias2Bimestre = 0;
        }
        return nrAdvertencias2Bimestre;
    }

    /**
     * @param nrAdvertencias2Bimestre the nrAdvertencias2Bimestre to set
     */
    public void setNrAdvertencias2Bimestre(Integer nrAdvertencias2Bimestre) {
        this.nrAdvertencias2Bimestre = nrAdvertencias2Bimestre;
    }

    /**
     * @return the nrAdvertencias3Bimestre
     */
    public Integer getNrAdvertencias3Bimestre() {
        if (nrAdvertencias3Bimestre == null) {
            nrAdvertencias3Bimestre = 0;
        }
        return nrAdvertencias3Bimestre;
    }

    /**
     * @param nrAdvertencias3Bimestre the nrAdvertencias3Bimestre to set
     */
    public void setNrAdvertencias3Bimestre(Integer nrAdvertencias3Bimestre) {
        this.nrAdvertencias3Bimestre = nrAdvertencias3Bimestre;
    }

    /**
     * @return the nrAdvertencias4Bimestre
     */
    public Integer getNrAdvertencias4Bimestre() {
        if (nrAdvertencias4Bimestre == null) {
            nrAdvertencias4Bimestre = 0;
        }
        return nrAdvertencias4Bimestre;
    }

    /**
     * @param nrAdvertencias4Bimestre the nrAdvertencias4Bimestre to set
     */
    public void setNrAdvertencias4Bimestre(Integer nrAdvertencias4Bimestre) {
        this.nrAdvertencias4Bimestre = nrAdvertencias4Bimestre;
    }

    /**
     * @return the nrAdvertenciasPeriodoLetivo
     */
    public Integer getNrAdvertenciasPeriodoLetivo() {
        if (nrAdvertenciasPeriodoLetivo == null) {
            nrAdvertenciasPeriodoLetivo = 0;
        }
        return nrAdvertenciasPeriodoLetivo;
    }

    /**
     * @param nrAdvertenciasPeriodoLetivo the nrAdvertenciasPeriodoLetivo to set
     */
    public void setNrAdvertenciasPeriodoLetivo(Integer nrAdvertenciasPeriodoLetivo) {
        this.nrAdvertenciasPeriodoLetivo = nrAdvertenciasPeriodoLetivo;
    }

    /**
     * @return the erroNotaSubstitutiva
     */
    public String getErroNotaSubstitutiva() {
        if (erroNotaSubstitutiva == null) {
            erroNotaSubstitutiva = "";
        }
        return erroNotaSubstitutiva;
    }

    /**
     * @param erroNotaSubstitutiva the erroNotaSubstitutiva to set
     */
    public void setErroNotaSubstitutiva(String erroNotaSubstitutiva) {
        this.erroNotaSubstitutiva = erroNotaSubstitutiva;
    }
    
	public CalendarioLancamentoNotaVO getCalendarioLancamentoNotaVO() {
		if (calendarioLancamentoNotaVO == null) {
			calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
		}
		return calendarioLancamentoNotaVO;
	}

	public void setCalendarioLancamentoNotaVO(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO) {
		this.calendarioLancamentoNotaVO = calendarioLancamentoNotaVO;
	}

    /**
     * @return the nrSuspensoes1Bimestre
     */
    public Integer getNrSuspensoes1Bimestre() {
        if (nrSuspensoes1Bimestre == null) {
            nrSuspensoes1Bimestre = 0;
        }
        return nrSuspensoes1Bimestre;
    }

    /**
     * @param nrSuspensoes1Bimestre the nrSuspensoes1Bimestre to set
     */
    public void setNrSuspensoes1Bimestre(Integer nrSuspensoes1Bimestre) {
        this.nrSuspensoes1Bimestre = nrSuspensoes1Bimestre;
    }

    /**
     * @return the nrSuspensoes2Bimestre
     */
    public Integer getNrSuspensoes2Bimestre() {
        if (nrSuspensoes2Bimestre == null) {
            nrSuspensoes2Bimestre = 0;
        }
        return nrSuspensoes2Bimestre;
    }

    /**
     * @param nrSuspensoes2Bimestre the nrSuspensoes2Bimestre to set
     */
    public void setNrSuspensoes2Bimestre(Integer nrSuspensoes2Bimestre) {
        this.nrSuspensoes2Bimestre = nrSuspensoes2Bimestre;
    }

    /**
     * @return the nrSuspensoes3Bimestre
     */
    public Integer getNrSuspensoes3Bimestre() {
        if (nrSuspensoes3Bimestre == null) {
            nrSuspensoes3Bimestre = 0;
        }
        return nrSuspensoes3Bimestre;
    }

    /**
     * @param nrSuspensoes3Bimestre the nrSuspensoes3Bimestre to set
     */
    public void setNrSuspensoes3Bimestre(Integer nrSuspensoes3Bimestre) {
        this.nrSuspensoes3Bimestre = nrSuspensoes3Bimestre;
    }

    /**
     * @return the nrSuspensoes4Bimestre
     */
    public Integer getNrSuspensoes4Bimestre() {
        if (nrSuspensoes4Bimestre == null) {
            nrSuspensoes4Bimestre = 0;
        }
        return nrSuspensoes4Bimestre;
    }

    /**
     * @param nrSuspensoes4Bimestre the nrSuspensoes4Bimestre to set
     */
    public void setNrSuspensoes4Bimestre(Integer nrSuspensoes4Bimestre) {
        this.nrSuspensoes4Bimestre = nrSuspensoes4Bimestre;
    }

    /**
     * @return the nrSuspensoesPeriodoLetivo
     */
    public Integer getNrSuspensoesPeriodoLetivo() {
        if (nrSuspensoesPeriodoLetivo == null) {
            nrSuspensoesPeriodoLetivo = 0;
        }        
        return nrSuspensoesPeriodoLetivo;
    }

    /**
     * @param nrSuspensoesPeriodoLetivo the nrSuspensoesPeriodoLetivo to set
     */
    public void setNrSuspensoesPeriodoLetivo(Integer nrSuspensoesPeriodoLetivo) {
        this.nrSuspensoesPeriodoLetivo = nrSuspensoesPeriodoLetivo;
    }

    @XmlElement(name = "ocultarDataAula")
	public Boolean getOcultarDataAula() {
		if (ocultarDataAula == null) {
			ocultarDataAula = Boolean.FALSE;
		}
		return ocultarDataAula;
	}

	public void setOcultarDataAula(Boolean ocultarDataAula) {
		this.ocultarDataAula = ocultarDataAula;
	}


	/**
	 * @return the realizarAlteracaoSituacaoHistorico
	 */
	@XmlElement(name = "realizarAlteracaoSituacaoHistorico")
	public Boolean getRealizarAlteracaoSituacaoHistorico() {
		if (realizarAlteracaoSituacaoHistorico == null) {
			realizarAlteracaoSituacaoHistorico = false;
		}
		return realizarAlteracaoSituacaoHistorico;
	}

	/**
	 * @param realizarAlteracaoSituacaoHistorico the realizarAlteracaoSituacaoHistorico to set
	 */
	public void setRealizarAlteracaoSituacaoHistorico(Boolean realizarAlteracaoSituacaoHistorico) {
		this.realizarAlteracaoSituacaoHistorico = realizarAlteracaoSituacaoHistorico;
	}
	
	@XmlElement(name = "historicoAlterado")
	public Boolean getHistoricoAlterado() {
		if (historicoAlterado == null) {
			historicoAlterado = Boolean.FALSE;
		}
		return historicoAlterado;
	}

	public void setHistoricoAlterado(Boolean historicoAlterado) {
		this.historicoAlterado = historicoAlterado;
	}

	/**
	 * @return the historicoSalvo
	 */
	@XmlElement(name = "historicoSalvo")
	public Boolean getHistoricoSalvo() {
		if (historicoSalvo == null) {
			historicoSalvo = Boolean.FALSE;
		}
		return historicoSalvo;
	}

	/**
	 * @param historicoSalvo the historicoSalvo to set
	 */
	public void setHistoricoSalvo(Boolean historicoSalvo) {
		this.historicoSalvo = historicoSalvo;
	}

	/**
	 * @return the historicoCalculado
	 */
	@XmlElement(name = "historicoCalculado")
	public Boolean getHistoricoCalculado() {
		if (historicoCalculado == null) {
			historicoCalculado = Boolean.FALSE;
		}
		return historicoCalculado;
	}

	/**
	 * @param historicoCalculado the historicoCalculado to set
	 */
	public void setHistoricoCalculado(Boolean historicoCalculado) {
		this.historicoCalculado = historicoCalculado;
	}
	
	public Boolean getApresentarBotaoGravarLancamentoNota() {
		return getHistoricoSalvo();
	}
	
	public Boolean getApresentarBotaoCalcularLancamentoNota() {
		return getHistoricoCalculado();
	}

	@XmlElement(name = "frequenciaTeorica")
	public Double getFrequenciaTeorica() {
		if (frequenciaTeorica == null) {
			frequenciaTeorica = 0.0;
		}
		return frequenciaTeorica;
	}

	public void setFrequenciaTeorica(Double frequenciaTeorica) {
		this.frequenciaTeorica = frequenciaTeorica;
	}

	@XmlElement(name = "frequenciaPratica")
	public Double getFrequenciaPratica() {
		if (frequenciaPratica == null) {
			frequenciaPratica = 0.0; 
		}
		return frequenciaPratica;
	}

	public void setFrequenciaPratica(Double frequenciaPratica) {
		this.frequenciaPratica = frequenciaPratica;
	}
	
	public Boolean getApresentarSituacaoAplicandoRegraConfiguracaoAcademica() {
		if (!getHistoricoDisciplinaFazParteComposicao()) {
			return true;
		}
		if (getHistoricoDisciplinaFazParteComposicao() && !getConfiguracaoAcademico().getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao()) {
			return true;
		}
		return false;
	}
	
	@XmlElement(name = "situacaoHistoricoDisciplinaComposta")
	public String getSituacaoHistoricoDisciplinaComposta() {
		if (situacaoHistoricoDisciplinaComposta == null) {
			situacaoHistoricoDisciplinaComposta = SituacaoHistorico.CURSANDO.getValor();
		}
		return situacaoHistoricoDisciplinaComposta;
	}

	public void setSituacaoHistoricoDisciplinaComposta(String situacaoHistoricoDisciplinaComposta) {
		this.situacaoHistoricoDisciplinaComposta = situacaoHistoricoDisciplinaComposta;
	}

	public String getAnoSemestreApresentar() {
		return getAnoHistorico()+(getSemestreHistorico().trim().isEmpty()?"":"/"+getSemestreHistorico());
	}
	

	/**
	 * @return the numeroAgrupamentoEquivalenciaDisciplina
	 */
	@XmlElement(name = "numeroAgrupamentoEquivalenciaDisciplina")
	public Integer getNumeroAgrupamentoEquivalenciaDisciplina() {
		if (numeroAgrupamentoEquivalenciaDisciplina == null) {
			numeroAgrupamentoEquivalenciaDisciplina = 1;
		}
		return numeroAgrupamentoEquivalenciaDisciplina;
	}

	/**
	 * @param numeroAgrupamentoEquivalenciaDisciplina the numeroAgrupamentoEquivalenciaDisciplina to set
	 */
	public void setNumeroAgrupamentoEquivalenciaDisciplina(Integer numeroAgrupamentoEquivalenciaDisciplina) {
		this.numeroAgrupamentoEquivalenciaDisciplina = numeroAgrupamentoEquivalenciaDisciplina;
	}

	@XmlElement(name = "apresentarAprovadoHistorico")
	public Boolean getApresentarAprovadoHistorico() {
		if(apresentarAprovadoHistorico == null){
			apresentarAprovadoHistorico = false;
		}
		return apresentarAprovadoHistorico;
	}

	public void setApresentarAprovadoHistorico(Boolean apresentarAprovadoHistorico) {
		this.apresentarAprovadoHistorico = apresentarAprovadoHistorico;
	}
	@XmlElement(name = "nota31")
	public Double getNota31() {
		return nota31;
	}

	public void setNota31(Double nota31) {
		this.nota31 = nota31;
	}
	@XmlElement(name = "nota31Lancada")
	public Boolean getNota31Lancada() {
		if (nota31Lancada == null) {
			nota31Lancada = false;
		}
		return nota31Lancada;
	}

	public void setNota31Lancada(Boolean nota31Lancada) {
		this.nota31Lancada = nota31Lancada;
	}

	@XmlElement(name = "nota31Texto")
	public String getNota31Texto() {
		if (nota31Texto == null) {
			nota31Texto = "";
		}
		return nota31Texto;
	}

	public void setNota31Texto(String nota31Texto) {
		this.nota31Texto = nota31Texto;
	}
	@XmlElement(name = "nota31Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota31Conceito() {
		if (nota31Conceito == null) {
			nota31Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota31Conceito;
	}

	public void setNota31Conceito(ConfiguracaoAcademicoNotaConceitoVO nota31Conceito) {
		this.nota31Conceito = nota31Conceito;
	}
	@XmlElement(name = "nota32")
	public Double getNota32() {
		return nota32;
	}

	public void setNota32(Double nota32) {
		this.nota32 = nota32;
	}
	@XmlElement(name = "nota32Lancada")
	public Boolean getNota32Lancada() {
		if (nota32Lancada == null) {
			nota32Lancada = false;
		}
		return nota32Lancada;
	}

	public void setNota32Lancada(Boolean nota32Lancada) {
		this.nota32Lancada = nota32Lancada;
	}
	
	@XmlElement(name = "nota32Texto")
	public String getNota32Texto() {
		if (nota32Texto == null) {
			nota32Texto = "";
		}
		return nota32Texto;
	}

	public void setNota32Texto(String nota32Texto) {
		this.nota32Texto = nota32Texto;
	}

	@XmlElement(name = "nota32Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota32Conceito() {
		if (nota32Conceito == null) {
			nota32Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota32Conceito;
	}

	public void setNota32Conceito(ConfiguracaoAcademicoNotaConceitoVO nota32Conceito) {
		this.nota32Conceito = nota32Conceito;
	}
	@XmlElement(name = "nota33")
	public Double getNota33() {
		return nota33;
	}

	public void setNota33(Double nota33) {
		this.nota33 = nota33;
	}
	@XmlElement(name = "nota33Lancada")
	public Boolean getNota33Lancada() {
		if (nota33Lancada == null) {
			nota33Lancada = false;
		}
		return nota33Lancada;
	}

	public void setNota33Lancada(Boolean nota33Lancada) {
		this.nota33Lancada = nota33Lancada;
	}

	@XmlElement(name = "nota33Texto")
	public String getNota33Texto() {
		if (nota33Texto == null) {
			nota33Texto = "";
		}
		return nota33Texto;
	}

	public void setNota33Texto(String nota33Texto) {
		this.nota33Texto = nota33Texto;
	}
	
	@XmlElement(name = "nota33Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota33Conceito() {
		if (nota33Conceito == null) {
			nota33Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota33Conceito;
	}

	public void setNota33Conceito(ConfiguracaoAcademicoNotaConceitoVO nota33Conceito) {
		this.nota33Conceito = nota33Conceito;
	}
	@XmlElement(name = "nota34")
	public Double getNota34() {
		return nota34;
	}

	public void setNota34(Double nota34) {
		this.nota34 = nota34;
	}
	@XmlElement(name = "nota34Lancada")
	public Boolean getNota34Lancada() {
		if (nota34Lancada == null) {
			nota34Lancada = false;
		}
		return nota34Lancada;
	}

	public void setNota34Lancada(Boolean nota34Lancada) {
		this.nota34Lancada = nota34Lancada;
	}

	@XmlElement(name = "nota34Texto")
	public String getNota34Texto() {
		if (nota34Texto == null) {
			nota34Texto = "";
		}
		return nota34Texto;
	}

	public void setNota34Texto(String nota34Texto) {
		this.nota34Texto = nota34Texto;
	}

	@XmlElement(name = "nota34Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota34Conceito() {
		if (nota34Conceito == null) {
			nota34Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota34Conceito;
	}

	public void setNota34Conceito(ConfiguracaoAcademicoNotaConceitoVO nota34Conceito) {
		this.nota34Conceito = nota34Conceito;
	}
	@XmlElement(name = "nota35")
	public Double getNota35() {
		return nota35;
	}

	public void setNota35(Double nota35) {
		this.nota35 = nota35;
	}
	@XmlElement(name = "nota35Lancada")
	public Boolean getNota35Lancada() {
		if (nota35Lancada == null) {
			nota35Lancada = false;
		}
		return nota35Lancada;
	}

	public void setNota35Lancada(Boolean nota35Lancada) {
		this.nota35Lancada = nota35Lancada;
	}

	@XmlElement(name = "nota35Texto")
	public String getNota35Texto() {
		if (nota35Texto == null) {
			nota35Texto = "";
		}
		return nota35Texto;
	}

	public void setNota35Texto(String nota35Texto) {
		this.nota35Texto = nota35Texto;
	}

	@XmlElement(name = "nota35Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota35Conceito() {
		if (nota35Conceito == null) {
			nota35Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota35Conceito;
	}

	public void setNota35Conceito(ConfiguracaoAcademicoNotaConceitoVO nota35Conceito) {
		this.nota35Conceito = nota35Conceito;
	}
	@XmlElement(name = "nota36")
	public Double getNota36() {
		return nota36;
	}

	public void setNota36(Double nota36) {
		this.nota36 = nota36;
	}
	@XmlElement(name = "nota36Lancada")
	public Boolean getNota36Lancada() {
		if (nota36Lancada == null) {
			nota36Lancada = false;
		}
		return nota36Lancada;
	}

	public void setNota36Lancada(Boolean nota36Lancada) {
		this.nota36Lancada = nota36Lancada;
	}
	@XmlElement(name = "nota36Texto")
	public String getNota36Texto() {
		if (nota36Texto == null) {
			nota36Texto = "";
		}
		return nota36Texto;
	}

	public void setNota36Texto(String nota36Texto) {
		this.nota36Texto = nota36Texto;
	}

	@XmlElement(name = "nota36Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota36Conceito() {
		if (nota36Conceito == null) {
			nota36Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota36Conceito;
	}

	public void setNota36Conceito(ConfiguracaoAcademicoNotaConceitoVO nota36Conceito) {
		this.nota36Conceito = nota36Conceito;
	}
	@XmlElement(name = "nota37")
	public Double getNota37() {
		return nota37;
	}

	public void setNota37(Double nota37) {
		this.nota37 = nota37;
	}
	@XmlElement(name = "nota37Lancada")
	public Boolean getNota37Lancada() {
		if (nota37Lancada == null) {
			nota37Lancada = false;
		}
		return nota37Lancada;
	}

	public void setNota37Lancada(Boolean nota37Lancada) {
		this.nota37Lancada = nota37Lancada;
	}

	@XmlElement(name = "nota37Texto")
	public String getNota37Texto() {
		if (nota37Texto == null) {
			nota37Texto = "";
		}
		return nota37Texto;
	}

	public void setNota37Texto(String nota37Texto) {
		this.nota37Texto = nota37Texto;
	}

	@XmlElement(name = "nota37Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota37Conceito() {
		if (nota37Conceito == null) {
			nota37Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota37Conceito;
	}

	public void setNota37Conceito(ConfiguracaoAcademicoNotaConceitoVO nota37Conceito) {
		this.nota37Conceito = nota37Conceito;
	}
	@XmlElement(name = "nota38")
	public Double getNota38() {
		return nota38;
	}

	public void setNota38(Double nota38) {
		this.nota38 = nota38;
	}
	@XmlElement(name = "nota38Lancada")
	public Boolean getNota38Lancada() {
		if (nota38Lancada == null) {
			nota38Lancada = false;
		}
		return nota38Lancada;
	}

	public void setNota38Lancada(Boolean nota38Lancada) {
		this.nota38Lancada = nota38Lancada;
	}
	@XmlElement(name = "nota38Texto")
	public String getNota38Texto() {
		if (nota38Texto == null) {
			nota38Texto = "";
		}
		return nota38Texto;
	}

	public void setNota38Texto(String nota38Texto) {
		this.nota38Texto = nota38Texto;
	}

	@XmlElement(name = "nota38Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota38Conceito() {
		if (nota38Conceito == null) {
			nota38Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota38Conceito;
	}

	public void setNota38Conceito(ConfiguracaoAcademicoNotaConceitoVO nota38Conceito) {
		this.nota38Conceito = nota38Conceito;
	}
	@XmlElement(name = "nota39")
	public Double getNota39() {
		return nota39;
	}

	public void setNota39(Double nota39) {
		this.nota39 = nota39;
	}
	@XmlElement(name = "nota39Lancada")
	public Boolean getNota39Lancada() {
		if (nota39Lancada == null) {
			nota39Lancada = false;
		}
		return nota39Lancada;
	}

	public void setNota39Lancada(Boolean nota39Lancada) {
		this.nota39Lancada = nota39Lancada;
	}

	@XmlElement(name = "nota39Texto")
	public String getNota39Texto() {
		if (nota39Texto == null) {
			nota39Texto = "";
		}
		return nota39Texto;
	}

	public void setNota39Texto(String nota39Texto) {
		this.nota39Texto = nota39Texto;
	}

	@XmlElement(name = "nota39Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota39Conceito() {
		if (nota39Conceito == null) {
			nota39Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota39Conceito;
	}

	public void setNota39Conceito(ConfiguracaoAcademicoNotaConceitoVO nota39Conceito) {
		this.nota39Conceito = nota39Conceito;
	}
	@XmlElement(name = "nota40")
	public Double getNota40() {
		return nota40;
	}

	public void setNota40(Double nota40) {
		this.nota40 = nota40;
	}
	@XmlElement(name = "nota40Lancada")
	public Boolean getNota40Lancada() {
		if (nota40Lancada == null) {
			nota40Lancada = false;
		}
		return nota40Lancada;
	}

	public void setNota40Lancada(Boolean nota40Lancada) {
		this.nota40Lancada = nota40Lancada;
	}

	@XmlElement(name = "nota40Texto")
	public String getNota40Texto() {
		if (nota40Texto == null) {
			nota40Texto = "";
		}
		return nota40Texto;
	}

	public void setNota40Texto(String nota40Texto) {
		this.nota40Texto = nota40Texto;
	}

	@XmlElement(name = "nota40Conceito")
	public ConfiguracaoAcademicoNotaConceitoVO getNota40Conceito() {
		if (nota40Conceito == null) {
			nota40Conceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return nota40Conceito;
	}

	public void setNota40Conceito(ConfiguracaoAcademicoNotaConceitoVO nota40Conceito) {
		this.nota40Conceito = nota40Conceito;
	}
	
	public String getDescricaoTipoDisciplinaHistorico() {
		if (this.getHistoricoDisciplinaForaGrade()) {
			return "Disciplina Fora Grade";
		} else {
			if (this.getHistoricoDisciplinaFazParteComposicao()) {
				return "Disciplina Parte de Uma Composição";	
			} else {
				if (this.getHistoricoDisciplinaComposta()) {
					return "Disciplina Composta";	
				} else {
					if (this.getDisciplinaReferenteAUmGrupoOptativa()) {
						return "Disciplina Referente Grupo Optativa";	
					} else {
						return "Regular (Normal)";
					}
				}
			}
		}
	}

	public void definirMediaFinalNotaConceito(){
		if(Uteis.isAtributoPreenchido(getMediaFinalConceito().getCodigo())){
			for(ConfiguracaoAcademicoNotaConceitoVO conceitoVO: getConfiguracaoAcademico().getConfiguracaoAcademicaMediaFinalConceito()){
				if(conceitoVO.getCodigo().equals(getMediaFinalConceito().getCodigo())){
					setMediaFinal(conceitoVO.getFaixaNota2());
					setNotaFinalConceito(conceitoVO.getAbreviaturaConceitoNota());
				}
			}
		}else{
			setMediaFinal(null);
			setNotaFinalConceito("");
		}
		
	}


	@XmlElement(name = "emRecuperacao")
	public Boolean getEmRecuperacao() {
		if(emRecuperacao == null){
			emRecuperacao = false;
		}
		return emRecuperacao;
	}

	public void setEmRecuperacao(Boolean emRecuperacao) {
		this.emRecuperacao = emRecuperacao;
	}
	
	private static final String EM_RECUPERACAO = " (Em Recuperação)";
	private static final String SEM_RECUPERACAO = "";
	
	public String getEmRecuperacaoApresentar(){
	  if((getSituacao().equals("RE") || getSituacao().equals("CS")) && getEmRecuperacao()){
		  return HistoricoVO.EM_RECUPERACAO;
	  }
	  return HistoricoVO.SEM_RECUPERACAO;
	}  	/**
     * @return Lista de notas
     */
    public List<NotaVO> montaListaNotas(boolean apenasNotaTipoMedia, List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs, boolean permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica) {
        NotaVO nota;
        ConfiguracaoAcademicoNotaConceitoVO conceito = null;
        getListaNotas().clear();
        Date dataAtual = new Date();
        for (int i = 1; i <= 40; i++) {
        	Boolean utilizaNota = (Boolean) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "utilizarNota"+i);
        	Boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "apresentarNota"+i);
        	Boolean utilizarComoMediaFinal = (Boolean) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "nota"+i+"MediaFinal");        	
        	boolean permiteApresentarNotaBoletim = ((ConfiguracaoAcademicaNotaVO)UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "configuracaoAcademicaNota"+i+"VO")).getApresentarNotaBoletim();
        	
        	if ((permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica || permiteApresentarNotaBoletim) 
        			&& utilizaNota && apresentarNota 
            		&& (!apenasNotaTipoMedia || (apenasNotaTipoMedia && utilizarComoMediaFinal))
            		&& ((Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO()) && (
            				 (Date) UtilReflexao.invocarMetodoGet(getCalendarioLancamentoNotaVO(), "dataLiberacaoAlunoNota"+i) == null
            				 || ((Date) UtilReflexao.invocarMetodoGet(getCalendarioLancamentoNotaVO(), "dataLiberacaoAlunoNota"+i)).compareTo(dataAtual) <= 0            				
            				)) 
            				|| !Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO())) ) {
                nota = new NotaVO();
                nota.setTitulo((String) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "tituloNotaApresentar"+i));
                if(turmaDisciplinaNotaTituloVOs != null && !turmaDisciplinaNotaTituloVOs.isEmpty()){
            	for(TurmaDisciplinaNotaTituloVO tituloVO:turmaDisciplinaNotaTituloVOs){
					if(tituloVO.getNota().getNumeroNota().intValue() == i){
						if(!tituloVO.getTitulo().trim().isEmpty()){
							nota.setTitulo(tituloVO.getTitulo());
						}
						break;
					}
				}
                }
                conceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(this, "nota" + i + "Conceito");
                if (conceito != null && !conceito.getConceitoNota().trim().isEmpty()) {
                    nota.setValorTexto(conceito.getConceitoNota());
                }else{
                	if((Double) UtilReflexao.invocarMetodoGet(this, "nota" + i) != null){
                		nota.setValorTexto(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula((Double) UtilReflexao.invocarMetodoGet(this, "nota" + i), getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
                	}else{
                		nota.setValorTexto("");
                	}
                }
                nota.setValor((Double) UtilReflexao.invocarMetodoGet(this, "nota" + i));
                UtilReflexao.invocarMetodo(this, "setTitulo" + i, (String) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademico(), "tituloNotaApresentar" + i));
                getListaNotas().add(nota);
            }
        }
        if(!getMediaFinal_Apresentar().trim().isEmpty() && (!Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO()) 
        		|| (Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO()) 
        		&& getCalendarioLancamentoNotaVO().getApresentarCalculoMediaFinalVisaoAluno()
        		))){
            nota = new NotaVO();
            nota.setTitulo("Média Final");
            if (getConfiguracaoAcademico().getOcultarMediaFinalDisciplinaCasoReprovado()) {
				if (!getSituacao().equals(SituacaoHistorico.REPROVADO.getValor()) && !getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) && !getSituacao().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor())) {
					nota.setValorTexto(getMediaFinal_Apresentar());
				} 
			} else {
				nota.setValorTexto(getMediaFinal_Apresentar());
			}
            getListaNotas().add(nota);
        }
        return getListaNotas();
    }
	
	@XmlElement(name = "anoSemestreMobile")
    public String getAnoSemestreMobile() {
    	if (!getAnoHistorico().trim().isEmpty() && !getSemestreHistorico().trim().isEmpty()) {
    		return getAnoHistorico().substring(2, 4) + "/" + getSemestreHistorico();
    	} else if (!getAnoHistorico().trim().isEmpty()) {
    		return getAnoHistorico();
    	} else {
    		if(getDataPrimeiraAula() != null) {
    			String ano = UteisData.getAnoDataString(getDataPrimeiraAula()).substring(2, 4);
    			Integer mes = UteisData.getMesData(getDataPrimeiraAula());
    			return mes.toString() + "/" + ano;
    		}else {
    			return Uteis.getAno(getDataRegistro());
    		}
    	}
    }
	
	public String getOrdemComposicao(){
		return getGradeDisciplinaComposta().getOrdem() >= 10 ? getGradeDisciplinaComposta().getOrdem()+getDisciplina().getNome() : "0"+getGradeDisciplinaComposta().getOrdem()+getDisciplina().getNome();
	}
	
	@XmlElement(name = "verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade")
	public Boolean getVerificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade() {
		if (verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade == null) {
			verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade = false;
		}
		return verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade;
	}

	public void setVerificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade(Boolean verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade) {
		this.verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade = verificarExistenciaCorrespondeciaHistoricoPorEquivalenciaParaExclusaoEliminandoDuplicidade;
	}
	
	
	private String notasDescritasAluno;

	@XmlElement(name = "notasDescritasAluno")
	public String getNotasDescritasAluno() {
		if(notasDescritasAluno == null){			
			notasDescritasAluno = "";
		}
		return notasDescritasAluno;
	}

	public void setNotasDescritasAluno(String notasDescritasAluno) {
		this.notasDescritasAluno = notasDescritasAluno;
	}

	public Boolean getOcultarFrequenciaFalta(){
		return getHistoricoDisciplinaComposta() && getConfiguracaoAcademico().getOcultarFrequenciaDisciplinaComposta();
	}
	
	@XmlElement(name = "freguencia")
    public String getFreguenciaMobile() {
		if(getOcultarFrequenciaFalta()){
			return "";
		}
        return getFreguencia().toString();
    }
	
	
	public Boolean getControlaRecuperacaoDisciplinaComposta(){
		return (getHistoricoDisciplinaComposta() && Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getControlarRecuperacaoPelaDisciplinaPrincipal() : getGradeCurricularGrupoOptativaDisciplinaVO().getControlarRecuperacaoPelaDisciplinaPrincipal());
	}
	
	public String getFormulaCalculoNotaRecuperacaoDisciplinaComposta(){		
		return getHistoricoDisciplinaComposta()  && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getFormulaCalculoNotaRecuperacao() : getGradeCurricularGrupoOptativaDisciplinaVO().getFormulaCalculoNotaRecuperacao()) : "";
	}
	
	public String getFormulaCondicaoNotaRecuperadaDisciplinaComposta(){		
		return getHistoricoDisciplinaComposta()  && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getFormulaCalculoNotaRecuperada() : getGradeCurricularGrupoOptativaDisciplinaVO().getFormulaCalculoNotaRecuperada()) : "";
	}
	
	public String getVariavelNotaRecuperacao(){		
		return getHistoricoDisciplinaComposta() && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getVariavelNotaRecuperacao() : getGradeCurricularGrupoOptativaDisciplinaVO().getVariavelNotaRecuperacao()) : "";
	}
	
	public String getVariavelNotaCondicaoUso(){		
		return getHistoricoDisciplinaComposta() && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getVariavelNotaCondicaoUsoRecuperacao() : getGradeCurricularGrupoOptativaDisciplinaVO().getVariavelNotaCondicaoUsoRecuperacao()) : "";
	}
	
	public String getVariavelNotaCondicaoRecuperacao(){		
		return getHistoricoDisciplinaComposta() && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getVariavelNotaFormulaCalculoNotaRecuperada() : getGradeCurricularGrupoOptativaDisciplinaVO().getVariavelNotaFormulaCalculoNotaRecuperada()) : "";
	}
	
	public String getFormulaCondicaoUsoRecuperacao(){		
		return getHistoricoDisciplinaComposta() && getControlaRecuperacaoDisciplinaComposta() ? (Uteis.isAtributoPreenchido(getGradeDisciplinaVO()) ? getGradeDisciplinaVO().getCondicaoUsoRecuperacao() : getGradeCurricularGrupoOptativaDisciplinaVO().getCondicaoUsoRecuperacao()) : "";
	}
	
	public Boolean getBloquearLancamentoNotaRecuperacao(int nrNota){
		if(!getVariavelNotaRecuperacao().trim().isEmpty() && !getFormulaCalculoNotaRecuperacaoDisciplinaComposta().trim().isEmpty()){
			TipoNotaConceitoEnum tipoNota = getConfiguracaoAcademico().getNumeroNotaPorVariavel(getVariavelNotaRecuperacao());
			return tipoNota != null && tipoNota.getNumeroNota().equals(nrNota);
		}
		return false;
	}
	
	public int getNumeroNotaRecuperacao(){
		if(!getVariavelNotaRecuperacao().trim().isEmpty() && !getFormulaCalculoNotaRecuperacaoDisciplinaComposta().trim().isEmpty()){
			TipoNotaConceitoEnum tipoNota = getConfiguracaoAcademico().getNumeroNotaPorVariavel(getVariavelNotaRecuperacao());
			return tipoNota != null ? tipoNota.getNumeroNota() : 0;
		}
		return 0;
	}

	public String getTitulacaoProfessor_Apresentar() {
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.FUNDAMENTAL.getValor()) || getTitulacaoProfessor().equals("IN")) {
			return NivelFormacaoAcademica.FUNDAMENTAL.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.ESPECIALIZACAO.getValor())) {
			return NivelFormacaoAcademica.ESPECIALIZACAO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.MESTRADO.getValor())) {
			return NivelFormacaoAcademica.MESTRADO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.POS_GRADUACAO.getValor())) {
			return NivelFormacaoAcademica.POS_GRADUACAO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.DOUTORADO.getValor())) {
			return NivelFormacaoAcademica.DOUTORADO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.MEDIO.getValor()) || getTitulacaoProfessor().equals("ME")) {
			return NivelFormacaoAcademica.MEDIO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.TECNICO.getValor()) || getTitulacaoProfessor().equals("PR")) {
			return NivelFormacaoAcademica.TECNICO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.POS_DOUTORADO.getValor())) {
			return NivelFormacaoAcademica.POS_DOUTORADO.getDescricao();
		}
		if (getTitulacaoProfessor().equalsIgnoreCase(NivelFormacaoAcademica.GRADUACAO.getValor()) || getTitulacaoProfessor().equals("SU") ) {
			return NivelFormacaoAcademica.GRADUACAO.getDescricao();
		}
    	return NivelFormacaoAcademica.getDescricao(getTitulacaoProfessor());
	}
	
	private List<HistoricoVO> historicoDisciplinaFilhaComposicaoVOs;

	@XmlElement(name = "historicoDisciplinaFilhaComposicaoVOs")
	public List<HistoricoVO> getHistoricoDisciplinaFilhaComposicaoVOs() {
		if (historicoDisciplinaFilhaComposicaoVOs == null) {
			historicoDisciplinaFilhaComposicaoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoDisciplinaFilhaComposicaoVOs;
	}

	public void setHistoricoDisciplinaFilhaComposicaoVOs(List<HistoricoVO> historicoDisciplinaFilhaComposicaoVOs) {
		this.historicoDisciplinaFilhaComposicaoVOs = historicoDisciplinaFilhaComposicaoVOs;
	}
	
	private StringBuilder logCalculoNota;

	public StringBuilder getLogCalculoNota() {
		if (logCalculoNota == null) {
			logCalculoNota = new StringBuilder("");
		}
		return logCalculoNota;
	}

	public void setLogCalculoNota(StringBuilder logCalculoNota) {
		this.logCalculoNota = logCalculoNota;
	}
	
	public Boolean getIsApresentarLog() {
		return getLogCalculoNota().length() > 0;
	}
	
	public Boolean getDesistiuEquivalencia() {
		if (desistiuEquivalencia == null) {
			desistiuEquivalencia = false;
		}
		return desistiuEquivalencia;
	}

	public void setDesistiuEquivalencia(Boolean desistiuEquivalencia) {
		this.desistiuEquivalencia = desistiuEquivalencia;
	}
	
	public Boolean getOcultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha() {
		if (ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha == null) {
			ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha = false;
		}
		return ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha;
	}

	public void setOcultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha(Boolean ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha) {
		this.ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha = ocultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha;
	}

	public Date getDataInicioAula() {
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	public Date getDataFimAula() {
		return dataFimAula;
	}

	public void setDataFimAula(Date dataFimAula) {
		this.dataFimAula = dataFimAula;
	}	
	
	private List<HistoricoNotaVO> historicoNotaMobileVOs;

	/**
	 * @return the historicoNotaMobileVOs
	 */
	@XmlElement(name = "historicoNotaMobileVOs")
	public List<HistoricoNotaVO> getHistoricoNotaMobileVOs() {
		if(historicoNotaMobileVOs == null){
			historicoNotaMobileVOs = new ArrayList<HistoricoNotaVO>(0);
		}
		return historicoNotaMobileVOs;
	}
	
	public String getOrdenacaoMinhasNotasAplicativo(){
		return getAnoHistorico()+getSemestreHistorico()+getDisciplina().getNome();
	}

	public DisciplinaPreRequisitoVO getPreRequisito() {
		if (preRequisito == null) {
			preRequisito = new DisciplinaPreRequisitoVO();
        }
		return preRequisito;
	}

	public void setPreRequisito(DisciplinaPreRequisitoVO preRequisito) {
		this.preRequisito = preRequisito;
	}
	
	

	public Date getDateDataInicioAula() {
		return dateDataInicioAula;
	}

	public void setDateDataInicioAula(Date dateDataInicioAula) {
		this.dateDataInicioAula = dateDataInicioAula;
	}
	
	public Date getDataInicioAula_formatoDate() throws ParseException {
		if (Uteis.isAtributoPreenchido(getDataInicioAula())) {
			return getDataInicioAula();
		}
		return null;
	}
	
	public String getDataInicioModulo() {
		if(dataInicioModulo == null) {
			dataInicioModulo = "";
		}
		return dataInicioModulo;
	}

	public void setDataInicioModulo(String dataInicioModulo) {
		this.dataInicioModulo = dataInicioModulo;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getObterConfiguracaoNotaConceito(int nrNota) {
		switch (nrNota) {
		case 1:
			return getNota1Conceito();			
		case 2:
			return getNota2Conceito();			
		case 3:
			return getNota3Conceito();			
		case 4:
			return getNota4Conceito();			
		case 5:
			return getNota5Conceito();			
		case 6:
			return getNota6Conceito();			
		case 7:
			return getNota7Conceito();			
		case 8:
			return getNota8Conceito();			
		case 9:
			return getNota9Conceito();			
		case 10:
			return getNota10Conceito();			
		case 11:
			return getNota11Conceito();			
		case 12:
			return getNota12Conceito();			
		case 13:
			return getNota13Conceito();			
		case 14:
			return getNota14Conceito();			
		case 15:
			return getNota15Conceito();			
		case 16:
			return getNota16Conceito();			
		case 17:
			return getNota17Conceito();			
		case 18:
			return getNota18Conceito();			
		case 19:
			return getNota19Conceito();			
		case 20:
			return getNota20Conceito();			
		case 21:
			return getNota21Conceito();			
		case 22:
			return getNota22Conceito();			
		case 23:
			return getNota23Conceito();			
		case 24:
			return getNota24Conceito();			
		case 25:
			return getNota25Conceito();			
		case 26:
			return getNota26Conceito();			
		case 27:
			return getNota27Conceito();			
		case 28:
			return getNota28Conceito();			
		case 29:
			return getNota29Conceito();			
		case 30:
			return getNota30Conceito();			
		case 31:
			return getNota31Conceito();			
		case 32:
			return getNota32Conceito();			
		case 33:
			return getNota33Conceito();			
		case 34:
			return getNota34Conceito();			
		case 35:
			return getNota35Conceito();			
		case 36:
			return getNota36Conceito();			
		case 37:
			return getNota37Conceito();			
		case 38:
			return getNota38Conceito();			
		case 39:
			return getNota39Conceito();			
		case 40:
			return getNota40Conceito();			
		default:
			return new ConfiguracaoAcademicoNotaConceitoVO();
		}
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota1VOs() {
		if(historicoNotaParcialNota1VOs == null) {
			historicoNotaParcialNota1VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota1VOs;
	}

	public void setHistoricoNotaParcialNota1VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota1VOs) {
		this.historicoNotaParcialNota1VOs = historicoNotaParcialNota1VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota2VOs() {
		if(historicoNotaParcialNota2VOs == null) {
			historicoNotaParcialNota2VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota2VOs;
	}

	public void setHistoricoNotaParcialNota2VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota2VOs) {
		this.historicoNotaParcialNota2VOs = historicoNotaParcialNota2VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota3VOs() {
		if(historicoNotaParcialNota3VOs == null) {
			historicoNotaParcialNota3VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota3VOs;
	}

	public void setHistoricoNotaParcialNota3VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota3VOs) {
		this.historicoNotaParcialNota3VOs = historicoNotaParcialNota3VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota4VOs() {
		if(historicoNotaParcialNota4VOs == null) {
			historicoNotaParcialNota4VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota4VOs;
	}

	public void setHistoricoNotaParcialNota4VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota4VOs) {
		this.historicoNotaParcialNota4VOs = historicoNotaParcialNota4VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota5VOs() {
		if(historicoNotaParcialNota5VOs == null) {
			historicoNotaParcialNota5VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota5VOs;
	}

	public void setHistoricoNotaParcialNota5VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota5VOs) {
		this.historicoNotaParcialNota5VOs = historicoNotaParcialNota5VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota6VOs() {
		if(historicoNotaParcialNota6VOs == null) {
			historicoNotaParcialNota6VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota6VOs;
	}

	public void setHistoricoNotaParcialNota6VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota6VOs) {
		this.historicoNotaParcialNota6VOs = historicoNotaParcialNota6VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota7VOs() {
		if(historicoNotaParcialNota7VOs == null) {
			historicoNotaParcialNota7VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota7VOs;
	}

	public void setHistoricoNotaParcialNota7VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota7VOs) {
		this.historicoNotaParcialNota7VOs = historicoNotaParcialNota7VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota8VOs() {
		if(historicoNotaParcialNota8VOs == null) {
			historicoNotaParcialNota8VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota8VOs;
	}

	public void setHistoricoNotaParcialNota8VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota8VOs) {
		this.historicoNotaParcialNota8VOs = historicoNotaParcialNota8VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota9VOs() {
		if(historicoNotaParcialNota9VOs == null) {
			historicoNotaParcialNota9VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota9VOs;
	}

	public void setHistoricoNotaParcialNota9VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota9VOs) {
		this.historicoNotaParcialNota9VOs = historicoNotaParcialNota9VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota10VOs() {
		if(historicoNotaParcialNota10VOs == null) {
			historicoNotaParcialNota10VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota10VOs;
	}

	public void setHistoricoNotaParcialNota10VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota10VOs) {
		this.historicoNotaParcialNota10VOs = historicoNotaParcialNota10VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota11VOs() {
		if(historicoNotaParcialNota11VOs == null) {
			historicoNotaParcialNota11VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota11VOs;
	}

	public void setHistoricoNotaParcialNota11VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota11VOs) {
		this.historicoNotaParcialNota11VOs = historicoNotaParcialNota11VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota12VOs() {
		if(historicoNotaParcialNota12VOs == null) {
			historicoNotaParcialNota12VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota12VOs;
	}

	public void setHistoricoNotaParcialNota12VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota12VOs) {
		this.historicoNotaParcialNota12VOs = historicoNotaParcialNota12VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota13VOs() {
		if(historicoNotaParcialNota13VOs == null) {
			historicoNotaParcialNota13VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota13VOs;
	}

	public void setHistoricoNotaParcialNota13VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota13VOs) {
		this.historicoNotaParcialNota13VOs = historicoNotaParcialNota13VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota14VOs() {
		if(historicoNotaParcialNota14VOs == null) {
			historicoNotaParcialNota14VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota14VOs;
	}

	public void setHistoricoNotaParcialNota14VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota14VOs) {
		this.historicoNotaParcialNota14VOs = historicoNotaParcialNota14VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota15VOs() {
		if(historicoNotaParcialNota15VOs == null) {
			historicoNotaParcialNota15VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota15VOs;
	}

	public void setHistoricoNotaParcialNota15VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota15VOs) {
		this.historicoNotaParcialNota15VOs = historicoNotaParcialNota15VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota16VOs() {
		if(historicoNotaParcialNota16VOs == null) {
			historicoNotaParcialNota16VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota16VOs;
	}

	public void setHistoricoNotaParcialNota16VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota16VOs) {
		this.historicoNotaParcialNota16VOs = historicoNotaParcialNota16VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota17VOs() {
		if(historicoNotaParcialNota17VOs == null) {
			historicoNotaParcialNota17VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota17VOs;
	}

	public void setHistoricoNotaParcialNota17VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota17VOs) {
		this.historicoNotaParcialNota17VOs = historicoNotaParcialNota17VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota18VOs() {
		if(historicoNotaParcialNota18VOs == null) {
			historicoNotaParcialNota18VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota18VOs;
	}

	public void setHistoricoNotaParcialNota18VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota18VOs) {
		this.historicoNotaParcialNota18VOs = historicoNotaParcialNota18VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota19VOs() {
		if(historicoNotaParcialNota19VOs == null) {
			historicoNotaParcialNota19VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota19VOs;
	}

	public void setHistoricoNotaParcialNota19VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota19VOs) {
		this.historicoNotaParcialNota19VOs = historicoNotaParcialNota19VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota20VOs() {
		if(historicoNotaParcialNota20VOs == null) {
			historicoNotaParcialNota20VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota20VOs;
	}

	public void setHistoricoNotaParcialNota20VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota20VOs) {
		this.historicoNotaParcialNota20VOs = historicoNotaParcialNota20VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota21VOs() {
		if(historicoNotaParcialNota21VOs == null) {
			historicoNotaParcialNota21VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota21VOs;
	}

	public void setHistoricoNotaParcialNota21VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota21VOs) {
		this.historicoNotaParcialNota21VOs = historicoNotaParcialNota21VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota22VOs() {
		if(historicoNotaParcialNota22VOs == null) {
			historicoNotaParcialNota22VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota22VOs;
	}

	public void setHistoricoNotaParcialNota22VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota22VOs) {
		this.historicoNotaParcialNota22VOs = historicoNotaParcialNota22VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota23VOs() {
		if(historicoNotaParcialNota23VOs == null) {
			historicoNotaParcialNota23VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota23VOs;
	}

	public void setHistoricoNotaParcialNota23VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota23VOs) {
		this.historicoNotaParcialNota23VOs = historicoNotaParcialNota23VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota24VOs() {
		if(historicoNotaParcialNota24VOs == null) {
			historicoNotaParcialNota24VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota24VOs;
	}

	public void setHistoricoNotaParcialNota24VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota24VOs) {
		this.historicoNotaParcialNota24VOs = historicoNotaParcialNota24VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota25VOs() {
		if(historicoNotaParcialNota25VOs == null) {
			historicoNotaParcialNota25VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota25VOs;
	}

	public void setHistoricoNotaParcialNota25VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota25VOs) {
		this.historicoNotaParcialNota25VOs = historicoNotaParcialNota25VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota26VOs() {
		if(historicoNotaParcialNota26VOs == null) {
			historicoNotaParcialNota26VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota26VOs;
	}

	public void setHistoricoNotaParcialNota26VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota26VOs) {
		this.historicoNotaParcialNota26VOs = historicoNotaParcialNota26VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota27VOs() {
		if(historicoNotaParcialNota27VOs == null) {
			historicoNotaParcialNota27VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota27VOs;
	}

	public void setHistoricoNotaParcialNota27VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota27VOs) {
		this.historicoNotaParcialNota27VOs = historicoNotaParcialNota27VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota28VOs() {
		if(historicoNotaParcialNota28VOs == null) {
			historicoNotaParcialNota28VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota28VOs;
	}

	public void setHistoricoNotaParcialNota28VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota28VOs) {
		this.historicoNotaParcialNota28VOs = historicoNotaParcialNota28VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota29VOs() {
		if(historicoNotaParcialNota29VOs == null) {
			historicoNotaParcialNota29VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota29VOs;
	}

	public void setHistoricoNotaParcialNota29VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota29VOs) {
		this.historicoNotaParcialNota29VOs = historicoNotaParcialNota29VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota30VOs() {
		if(historicoNotaParcialNota30VOs == null) {
			historicoNotaParcialNota30VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota30VOs;
	}

	public void setHistoricoNotaParcialNota30VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota30VOs) {
		this.historicoNotaParcialNota30VOs = historicoNotaParcialNota30VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota31VOs() {
		if(historicoNotaParcialNota31VOs == null) {
			historicoNotaParcialNota31VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota31VOs;
	}

	public void setHistoricoNotaParcialNota31VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota31VOs) {
		this.historicoNotaParcialNota31VOs = historicoNotaParcialNota31VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota32VOs() {
		if(historicoNotaParcialNota32VOs == null) {
			historicoNotaParcialNota32VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota32VOs;
	}

	public void setHistoricoNotaParcialNota32VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota32VOs) {
		this.historicoNotaParcialNota32VOs = historicoNotaParcialNota32VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota33VOs() {
		if(historicoNotaParcialNota33VOs == null) {
			historicoNotaParcialNota33VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota33VOs;
	}

	public void setHistoricoNotaParcialNota33VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota33VOs) {
		this.historicoNotaParcialNota33VOs = historicoNotaParcialNota33VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota34VOs() {
		if(historicoNotaParcialNota34VOs == null) {
			historicoNotaParcialNota34VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota34VOs;
	}

	public void setHistoricoNotaParcialNota34VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota34VOs) {
		this.historicoNotaParcialNota34VOs = historicoNotaParcialNota34VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota35VOs() {
		if(historicoNotaParcialNota35VOs == null) {
			historicoNotaParcialNota35VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota35VOs;
	}

	public void setHistoricoNotaParcialNota35VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota35VOs) {
		this.historicoNotaParcialNota35VOs = historicoNotaParcialNota35VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota36VOs() {
		if(historicoNotaParcialNota36VOs == null) {
			historicoNotaParcialNota36VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota36VOs;
	}

	public void setHistoricoNotaParcialNota36VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota36VOs) {
		this.historicoNotaParcialNota36VOs = historicoNotaParcialNota36VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota37VOs() {
		if(historicoNotaParcialNota37VOs == null) {
			historicoNotaParcialNota37VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota37VOs;
	}

	public void setHistoricoNotaParcialNota37VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota37VOs) {
		this.historicoNotaParcialNota37VOs = historicoNotaParcialNota37VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota38VOs() {
		if(historicoNotaParcialNota38VOs == null) {
			historicoNotaParcialNota38VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota38VOs;
	}

	public void setHistoricoNotaParcialNota38VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota38VOs) {
		this.historicoNotaParcialNota38VOs = historicoNotaParcialNota38VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota39VOs() {
		if(historicoNotaParcialNota39VOs == null) {
			historicoNotaParcialNota39VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota39VOs;
	}

	public void setHistoricoNotaParcialNota39VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota39VOs) {
		this.historicoNotaParcialNota39VOs = historicoNotaParcialNota39VOs;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNota40VOs() {
		if(historicoNotaParcialNota40VOs == null) {
			historicoNotaParcialNota40VOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNota40VOs;
	}

	public void setHistoricoNotaParcialNota40VOs(List<HistoricoNotaParcialVO> historicoNotaParcialNota40VOs) {
		this.historicoNotaParcialNota40VOs = historicoNotaParcialNota40VOs;
	}
	

	public List<HistoricoNotaParcialVO> getObterListaNotaParcialPorTipoNota(String tipoNota) {
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_1.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota1VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_2.name())) {			
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota2VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_3.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota3VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_4.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota4VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_5.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota5VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_6.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota6VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_7.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota7VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_8.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota8VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_9.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota9VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_10.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota10VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_11.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota11VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_12.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota12VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_13.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota13VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_14.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota14VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_15.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota15VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_16.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota16VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_17.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota17VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_18.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota18VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_19.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota19VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_20.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota20VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_21.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota21VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_22.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota22VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_23.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota23VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_24.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota24VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_25.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota25VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_26.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota26VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_27.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota27VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_28.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota28VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_29.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota29VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_30.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota30VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_31.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota31VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_32.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota32VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_33.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota33VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_34.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota34VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_35.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota35VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_36.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota36VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_37.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota37VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_38.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota38VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_39.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota39VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		if (tipoNota.equals(TipoNotaConceitoEnum.NOTA_40.name())) {
			setHistoricoNotaParcialNotaVOs(getHistoricoNotaParcialNota40VOs());
			return getHistoricoNotaParcialNotaVOs();
		}
		return null;
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialNotaVOs() {
		if (historicoNotaParcialNotaVOs == null) {
			historicoNotaParcialNotaVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialNotaVOs;
	}

	public void setHistoricoNotaParcialNotaVOs(List<HistoricoNotaParcialVO> historicoNotaParcialNotaVOs) {
		this.historicoNotaParcialNotaVOs = historicoNotaParcialNotaVOs;
	}

	public String getWidthNotasMinhasNotasVisaoALuno() {
		if(widthNotasMinhasNotasVisaoALuno == null ) {
			widthNotasMinhasNotasVisaoALuno = "" ;
		}
		return widthNotasMinhasNotasVisaoALuno;
	}

	public void setWidthNotasMinhasNotasVisaoALuno(String widthNotasMinhasNotasVisaoALuno) {
		this.widthNotasMinhasNotasVisaoALuno = widthNotasMinhasNotasVisaoALuno;
	}

	public Integer getTotalCargaHorariaPeriodoLetivoCumprida() {
		if(totalCargaHorariaPeriodoLetivoCumprida == null ) {
			totalCargaHorariaPeriodoLetivoCumprida =0;
		}
		return totalCargaHorariaPeriodoLetivoCumprida;
	}

	public void setTotalCargaHorariaPeriodoLetivoCumprida(Integer totalCargaHorariaPeriodoLetivoCumprida) {
		this.totalCargaHorariaPeriodoLetivoCumprida = totalCargaHorariaPeriodoLetivoCumprida;
	}

	public Integer getTotalCargaHorariaPeriodoLetivoExigida() {
		if(totalCargaHorariaPeriodoLetivoExigida == null ) {
			totalCargaHorariaPeriodoLetivoExigida =0 ;
		}
		return totalCargaHorariaPeriodoLetivoExigida;
	}

	public void setTotalCargaHorariaPeriodoLetivoExigida(Integer totalCargaHorariaPeriodoLetivoExigida) {
		this.totalCargaHorariaPeriodoLetivoExigida = totalCargaHorariaPeriodoLetivoExigida;
	}
	
	
    public Integer getTotalDisciplinasPeriodoLetivo() {
    	if(totalDisciplinasPeriodoLetivo == null ) {
    		totalDisciplinasPeriodoLetivo = 0;
    	}
		return totalDisciplinasPeriodoLetivo;
	}

	public void setTotalDisciplinasPeriodoLetivo(Integer totalDisciplinasPeriodoLetivo) {
		this.totalDisciplinasPeriodoLetivo = totalDisciplinasPeriodoLetivo;
	}

	public Integer getTotalDisciplinasCursadasPeriodoLetivo() {
		if(totalDisciplinasCursadasPeriodoLetivo == null ){
			totalDisciplinasCursadasPeriodoLetivo = 0;
		}
		return totalDisciplinasCursadasPeriodoLetivo;
	}

	public void setTotalDisciplinasCursadasPeriodoLetivo(Integer totalDisciplinasCursadasPeriodoLetivo) {
		this.totalDisciplinasCursadasPeriodoLetivo = totalDisciplinasCursadasPeriodoLetivo;
	}

	
	@ExcluirJsonAnnotation
	private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO;


	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		if(periodoLetivoAtivoUnidadeEnsinoCursoVO == null) {
			periodoLetivoAtivoUnidadeEnsinoCursoVO =  new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		return periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoVO = periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}
	
	public String getAnoBimestreConclusaoDisciplina() {
		if (anoBimestreConclusaoDisciplina == null) {
			anoBimestreConclusaoDisciplina = Constantes.EMPTY;
		}
		return anoBimestreConclusaoDisciplina;
	}
	
	public void setAnoBimestreConclusaoDisciplina(String anoBimestreConclusaoDisciplina) {
		this.anoBimestreConclusaoDisciplina = anoBimestreConclusaoDisciplina;
	}
	public String getChaveDisciplinaMaeComposicao() {
		return getGradeDisciplinaVO().getCodigo() + "-" + getMatrizCurricular().getCodigo() + "-" + getAnoHistorico() + "-" + getSemestreHistorico() + "-" + getDataRegistro_Apresentar();
	}

	public String getChaveDisciplinaMaeComposicaoOptativa() {
		return getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo() + "-" + getMatrizCurricular().getCodigo() + "-" + getAnoHistorico() + "-" + getSemestreHistorico() + "-" + getDataRegistro_Apresentar();
	}
	
	public String getChaveDisciplinaFilhaComposicao() {
		return getGradeDisciplinaComposta().getGradeDisciplina().getCodigo() + "-" + getMatrizCurricular().getCodigo() + "-" + getAnoHistorico() + "-" + getSemestreHistorico() + "-" + getDataRegistro_Apresentar();
	}

	public String getChaveDisciplinaFilhaComposicaoOptativa() {
		return getGradeDisciplinaComposta().getGradeCurricularGrupoOptativaDisciplina().getCodigo() + "-" + getMatrizCurricular().getCodigo() + "-" + getAnoHistorico() + "-" + getSemestreHistorico() + "-" + getDataRegistro_Apresentar();
	}

	public Integer getBimestre() {
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}

    public Boolean getHistoricoAproveitamentoEquivalenciaMatriculaProcessoSeletivo() {
        if(historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo == null ){
            historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo =  Boolean.FALSE;
        }
        return historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo;
    }

    public void setHistoricoAproveitamentoEquivalenciaMatriculaProcessoSeletivo(Boolean historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo) {
        this.historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo = historicoAproveitamentoEquivalenciaMatriculaProcessoSeletivo;
    }
	
}
