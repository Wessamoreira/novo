package negocio.comuns.administrativo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
//import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.MaterialUnidadeEnsinoVO;
//import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoDiferenciadaEscolaEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoZonaEscolaEnum;
import negocio.comuns.administrativo.enumeradores.UnidadeVinculadaEscolaEducacaoBasicaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.*;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

import java.util.*;

/**
 * Reponsável por manter os dados da entidade UnidadeEnsino. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see EmpresaVO
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.PossuiEndereco;



/**
 * Reponsvel por manter os dados da entidade UnidadeEnsino. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os mtodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memria os dados desta entidade.
 * 
 * @see SuperVO
 * @see EmpresaVO
 */
@XmlRootElement(name = "unidadeEnsino")
public class UnidadeEnsinoVO extends EmpresaVO implements PossuiEndereco {

	public static final long serialVersionUID = 1L;
	
	private Boolean matriz;
	// Atributo usado no momento que esta fazendo uma cotao para universidade
	// no e persistido no banco
	// usado somente para ter o controle de quais universidade vai fazer
	// cotao.
	private String nomeExpedicaoDiploma;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String leiCriacao1;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String leiCriacao2;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean escolhidaParaFazerCotacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean permitirVisualizacaoLogin;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private ConfiguracoesVO configuracoes;
	/**
	 * Atributo responsvel por manter os objetos da classe
	 * <code>UnidadeEnsinoCurso</code>.
	 */
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;

	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private List<UnidadeEnsinoCursoCentroResultadoVO> unidadeEnsinoCentroResultado;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> unidadeEnsinoNivelEducacionalCentroResultado;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> unidadeEnsinoTipoRequerimentoCentroResultado;
	private String abreviatura;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String ano;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDocumento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoIES;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoIESMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String credenciamentoPortaria;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoDO;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private FuncionarioVO diretorGeral;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private PessoaVO responsavelCobrancaUnidade;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TurmaVO turma;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String mantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean desativada;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean usarConfiguracaoPadrao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean apresentarTelaProcessoSeletivo;
	//private ContaCorrenteVO contaCorrentePadraoVO;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformada;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private List<MaterialUnidadeEnsinoVO> materialUnidadeEnsinoVOs;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoIndex;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoIndex;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaIndex;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoRelatorio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoRelatorio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaRelatorio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean filtrarUnidadeEnsino;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private FuncionarioVO coordenadorTCC;

	// CAMPO DESTINADO APENAS PARA PROCESSUS PARA O HISTORICO DO ALUNO
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String credenciamento;

	

	
	//CAMPO DESTINADO PARA O CENSO
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String dependenciaAdministrativa;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String localizacaoZonaEscola;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String localizacaoDiferenciadaEscola;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String categoriaEscolaPrivada;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String conveniadaPoderPublico;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String localFuncionamentoDaEscola;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String formaOcupacaoPredio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean predioCompartilhado;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada1;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada2;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada3;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada4;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada5;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoEscolaCompartilhada6;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String aguaConsumida;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String abastecimentoAgua;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String abastecimentoEnergia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String esgotoSanitario;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String destinoLixo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String unidadeVinculadaEscolaEducacaoBasica;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoEscolaSede;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean forneceAguaPotavelConsumoHumano;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tratamentoLixo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaDiretoria;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaProfessores;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaSecretaria;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean laboratorioInformatica;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean laboratorioCiencias;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean recursosMultifuncionais;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean quadraEsportesCoberta;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean quadraEsportesDescoberta;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean cozinha;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean biblioteca;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaLeitura;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean parqueInfantil;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean bercario;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroForaPredio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroDentroPredio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroEducacaoInfantil;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroDeficiencia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean viasDeficiencia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroChuveiro;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean refeitorio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean despensa;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean almoxarifado;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean auditorio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean patioCoberto;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean patioDescoberto;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean alojamentoAluno;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean alojamentoProfessor;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean areaVerde;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean lavanderia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean nenhumaDependencia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean banheiroExclusivoFuncionarios;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean piscina;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaRepousoAluno; 
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaArtes;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaMusica;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaDanca;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean salaMultiuso;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean terreirao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean viveiroAnimais;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean corrimaoGuardaCorpos;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean elevador;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean pisosTateis;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean portasVaoLivreMinimoOitentaCentimetros;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean rampas;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean sinalizacaoSonora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean sinalizacaoTatil;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean sinalizacaoVisual;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean nenhumRecursoAcessibilidade;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroSalasAulaUtilizadasEscolaDentroPredioEscolar;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean antenaParabolica;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean computadores;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean copiadora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean impressora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean impressoraMultifuncional;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean scanner;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetUsoAdiministrativo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetUsoProcessoEnsinoAprendizagem;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetUsoAlunos;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetComunidade;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean naoPossuiAcessoInternet;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetComputadoresEscola;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean acessoInternetDispositivosPessoais;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean redeLocalCabo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean redeLocalWireless;	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean naoExisteRedeLocal;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean alimentacaoEscolarAlunos;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean educacaoEscolarIndigena;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean linguaIndigena;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean linguaPortuguesa;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoLinguaIndigena1;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoLinguaIndigena2;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer codigoLinguaIndigena3;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroSalasAulaExistente;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroSalasDentroForaPredio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeTelevisao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeVideoCassete;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeDVD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeAntenaParabolica;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeCopiadora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeRetroprojetor;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeImpressora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeAparelhoSom;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeProjetorMultimidia;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeFax;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeMaquinaFotograficaFilmadora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeComputadores;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeComputadoresAdministrativos;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer quantidadeComputadoresAlunos;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean computadoresAcessoInternet;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean internetBandaLarga;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoTributacaoMunicipio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoPaginaInicial;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaInicial;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoPaginaInicial;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaAplicativo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoAplicativo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoAplicativo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoLogoUnidadeEnsinoUsarAplicativo;
	/***
	 * logos para email
	 */
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoEmailCima;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoEmailCima;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaEmailCima;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoEmailBaixo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoEmailBaixo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaEmailBaixo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String caminhoBaseLogoMunicipio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String nomeArquivoLogoMunicipio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean logoInformadaMunicipio;

	/**
	 * Construtor padro da classe <code>UnidadeEnsino</code>. Cria uma nova
	 * instncia desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */

	// CAMPO DESTINADO PARA SELECIONAR AS INSTITUIES ONDE SE DESEJA ADICIONAR
	// O CURSO EM QUESTO ( TELA DE CURSO CURSO.FORM)
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean selecionarAdicionarCursoInstituicao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean apresentarHomePreInscricao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String codigoIntegracaoContabil;
	
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoProcessoSeletivo;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoMatricula;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoMensalidade;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoMaterialDidatico;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoBiblioteca;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoRequerimento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoNegociacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer contaCorrentePadraoDevolucaoCheque;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private String informacoesAdicionaisEndereco;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private String cnpjMantenedora;
    private String unidadeCertificadora;
    private String  cnpjUnidadeCertificadora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Integer codigoIESUnidadeCertificadora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private ConfiguracaoGEDVO configuracaoGEDVO;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private ConfiguracaoMobileVO configuracaoMobileVO;
	
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private String tipoChancela;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Double porcentagemChancela;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Double valorFixoChancela;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private Boolean valorPorAluno;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private FuncionarioVO responsavelNotificacaoAlteracaoCronogramaAula;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private FuncionarioVO orientadorPadraoEstagio;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private String observacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
    private FuncionarioVO operadorResponsavel;
//	@ExcluirJsonAnnotation @JsonIgnore
//	@Expose(deserialize = false, serialize = false)
    private transient ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoEnum;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOUCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean informarDadosRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean utilizarEnderecoUnidadeEnsinoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String cepRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private CidadeVO cidadeRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String complementoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String bairroRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String enderecoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean utilizarCredenciamentoUnidadeEnsino;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoDORegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroPublicacaoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean utilizarMantenedoraUnidadeEnsino;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String mantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String cnpjMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String cepMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String enderecoMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private CidadeVO cidadeMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String complementoMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String bairroMantenedoraRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean utilizarEnderecoUnidadeEnsinoMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String cepMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String enderecoMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private CidadeVO cidadeMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String complementoMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String bairroMantenedora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOURecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoCredenciamentoRegistradora;
	private String numeroRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOURenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String credenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoDOEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String credenciamentoPortariaEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOUCredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOURecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataPublicacaoRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String veiculoPublicacaoRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer secaoPublicacaoRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer paginaPublicacaoRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Integer numeroDOURenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private TipoAutorizacaoCursoEnum tipoAutorizacaoRenovacaoRecredenciamentoEAD;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean credenciamentoEmTramitacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloCredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean recredenciamentoEmTramitacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean renovacaoRecredenciamentoEmTramitacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloRenovacaoRecredenciamento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean credenciamentoEadEmTramitacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoCredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoCredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroCredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloCredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean recredenciamentoEmTramitacaoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean renovacaoRecredenciamentoEmTramitacaoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoRenovacaoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoRenovacaoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroRenovacaoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloRenovacaoRecredenciamentoEad;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Boolean credenciamentoRegistradoraEmTramitacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String numeroProcessoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private String tipoProcessoCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataCadastroCredenciamentoRegistradora;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private Date dataProtocoloCredenciamentoRegistradora;
	@Expose(deserialize = false, serialize = false)
	private String numeroCredenciamento;
	@Expose(deserialize = false, serialize = false)
	private Integer numeroVagaOfertada;
    
	public UnidadeEnsinoVO() {
		super();
	}
	
	public UnidadeEnsinoVO(Integer codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public String getNome() {
		return super.getNome();
	}

	public Boolean getDesativada() {
		if (desativada == null) {
			desativada = false;
		}
		return desativada;
	}

	public void setDesativada(Boolean desativada) {
		this.desativada = desativada;
	}

	/**
	 * Operao responsvel por validar os dados de um objeto da classe
	 * <code>UnidadeEnsinoVO</code>. Todos os tipos de consistncia de dados so
	 * e devem ser implementadas neste mtodo. So validaes tpicas:
	 * verificao de campos obrigatrios, verificao de valores vlidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistncia for encontrada aumaticamente 
	 *                gerada uma exceo descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(UnidadeEnsinoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Dados Bsicos) deve ser informado.");
		}
		if (obj.getAbreviatura().equals("")) {
			throw new ConsistirException("O campo ABREVIATURA(Dados Bsicos) deve ser informado.");
		}
		if (obj.getTipoEmpresa().equals("JU")) {
			if (obj.getCNPJ().equals("")) {
				throw new ConsistirException("O campo CNPJ (Dados Bsicos) deve ser informado.");
			}
		}
		if (obj.getCEP().equals("")) {
			throw new ConsistirException("O campo CEP (Dados Bsicos) deve ser informado.");
		}
		if (obj.getEndereco().equals("")) {
			throw new ConsistirException("O campo ENDEREO (Dados Bsicos) deve ser informado.");
		}
		if (obj.getSetor().equals("")) {
			throw new ConsistirException("O campo BAIRRO/SETOR (Dados Bsicos) deve ser informado.");
		}
		if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CIDADE (Dados Bsicos) deve ser informado.");
		}

		if (obj.getTelComercial1().equals("")) {
			throw new ConsistirException("O campo TELEFONE (Dados Bsicos) deve ser informado.");
		}
		if (obj.getConfiguracoes().getCodigo() == 0) {
			throw new ConsistirException("O campo Configurao (Dados Bsicos) deve ser informado ");
		}

		if (obj.getTipoEmpresa().equals("FI")) {
			if (obj.getRG().equals("")) {
				throw new ConsistirException("O campo RG (Dados Bsicos) deve ser informado.");
			}
			if (obj.getCPF().equals("")) {
				throw new ConsistirException("O campo CPF (Dados Bsicos) deve ser informado.");
			}
		}
		if (obj.getDiretorGeral() == null) {
			throw new ConsistirException("O campo DIRETOR GERAL deve ser informado.");
		}
		if(obj.getLocalizacaoZonaEscola().equals(LocalizacaoZonaEscolaEnum.URBANA.getValor()) && obj.getLocalizacaoDiferenciadaEscola().equals(LocalizacaoDiferenciadaEscolaEnum.AREA_ASSENTAMENTO.getValor())) {
			throw new ConsistirException("O campo Localizao Diferenciada da Escola (Dados Censo) no pode ser rea de Assentamento quando o campo Localizao Zona da escola for Urbana.");
		}
		if(obj.getUnidadeVinculadaEscolaEducacaoBasica().equals(UnidadeVinculadaEscolaEducacaoBasicaEnum.UNIDADE_VINCULADA_ESCOLA_EDUCACAO_BASICA.getValor())) {
			throw new ConsistirException("O campo Cdigo Escola Sede (Dados Bsicos) deve ser preenchido.");
		}
		if(obj.getUnidadeVinculadaEscolaEducacaoBasica().equals(UnidadeVinculadaEscolaEducacaoBasicaEnum.UNIDADE_OFERTANTE_EDUCACAO_SUPERIOR.getValor())) {
			throw new ConsistirException("O campo Cdigo da IES Sede (Dados Bsicos) deve ser preenchido.");
		}
		validarDadosCredenciamento(obj);
		validarDadosRecredenciamento(obj);
		validarDadosRenovacaoRecredenciamento(obj);
		validarDadosRegistradora(obj);
		validarDadosMantenedora(obj);
		/*
		 * if (obj.getConfiguracoes().getCodigo().intValue() == 0) { throw new
		 * ConsistirException(
		 * "Nenhuma configurao definida, selecione uma configurao ou marque a opo Configurao Padro."
		 * ); }
		 */
	}

	/**
	 * Operao responsvel por adicionar um novo objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code> ao List
	 * <code>unidadeEnsinoCursoVOs</code>. Utiliza o atributo padro de consulta
	 * da classe <code>UnidadeEnsinoCurso</code> - getCurso().getCodigo() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que ser
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjUnidadeEnsinoCursoVOs(UnidadeEnsinoCursoVO objEditar, UnidadeEnsinoCursoVO obj) throws Exception {
		UnidadeEnsinoCursoVO.validarDados(obj);
		Iterator i = getUnidadeEnsinoCursoVOs().iterator();
		int index = 0;
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) i.next();
			if ((objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) && (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) && (objExistente.getUnidadeEnsino().equals(obj.getUnidadeEnsino()))) {
				if ((objEditar.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) && (objEditar.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) && (objEditar.getUnidadeEnsino().equals(obj.getUnidadeEnsino()))) {
					getUnidadeEnsinoCursoVOs().set(index, obj);
					return;
				}
				// excluirObjUnidadeEnsinoCursoVOs(obj.getCurso().getCodigo(),
				// obj.getTurno().getCodigo());
				throw new Exception("J existe um registro deste curso para o turno (" + objExistente.getTurno().getNome() + ").");
			}
			index++;
		}
		index = 0;
		if (!objEditar.getCurso().getCodigo().equals(0)) {
			Iterator j = getUnidadeEnsinoCursoVOs().iterator();
			while (j.hasNext()) {
				UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) j.next();
				if ((objExistente.getCurso().getCodigo().equals(objEditar.getCurso().getCodigo())) && (objExistente.getTurno().getCodigo().equals(objEditar.getTurno().getCodigo())) && (objExistente.getUnidadeEnsino().equals(objEditar.getUnidadeEnsino()))) {
					getUnidadeEnsinoCursoVOs().set(index, obj);
					return;
				}
				index++;
			}
		}
		getUnidadeEnsinoCursoVOs().add(obj);
	}

	/**
	 * Operao responsvel por excluir um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code> no List
	 * <code>unidadeEnsinoCursoVOs</code>. Utiliza o atributo padro de consulta
	 * da classe <code>UnidadeEnsinoCurso</code> - getCurso().getCodigo() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param curso
	 *            Parmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjUnidadeEnsinoCursoVOs(Integer curso, Integer turno) throws Exception {
		int index = 0;
		Iterator i = getUnidadeEnsinoCursoVOs().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) i.next();
			if ((objExistente.getCurso().getCodigo().equals(curso)) && (objExistente.getTurno().getCodigo().equals(turno))) {
				getUnidadeEnsinoCursoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operao responsvel por consultar um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code> no List
	 * <code>unidadeEnsinoCursoVOs</code>. Utiliza o atributo padro de consulta
	 * da classe <code>UnidadeEnsinoCurso</code> - getCurso().getCodigo() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param curso
	 *            Parmetro para localizar o objeto do List.
	 */
	public UnidadeEnsinoCursoVO consultarObjUnidadeEnsinoCursoVO(Integer curso, Integer turno) throws Exception {
		Iterator i = getUnidadeEnsinoCursoVOs().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) i.next();
			if ((objExistente.getCurso().getCodigo().equals(curso)) && (objExistente.getTurno().getCodigo().equals(turno))) {
				return objExistente;
			}
		}
		return null;
	}

	public UnidadeEnsinoCursoVO consultarObjUnidadeEnsinoCursoVO(Integer curso) throws Exception {
		Iterator i = getUnidadeEnsinoCursoVOs().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) i.next();
			if (objExistente.getCurso().getCodigo().equals(curso)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Retorna Atributo responsvel por manter os objetos da classe
	 * <code>UnidadeEnsinoCurso</code>.
	 */
	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return (unidadeEnsinoCursoVOs);
	}

	/**
	 * Define Atributo responsvel por manter os objetos da classe
	 * <code>UnidadeEnsinoCurso</code>.
	 */
	public void setUnidadeEnsinoCursoVOs(List unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

	public Boolean getMatriz() {
		if (matriz == null) {
			matriz = false;
		}
		return (matriz);
	}

	public Boolean isMatriz() {
		if (matriz == null) {
			matriz = Boolean.FALSE;
		}
		return (matriz);
	}

	public void setMatriz(Boolean matriz) {
		this.matriz = matriz;
	}

	public String getUnidadeMatriz() {
		if (getMatriz()) {
			return "Sim";
		}
		return "";
	}

	public Boolean getEscolhidaParaFazerCotacao() {
		if (escolhidaParaFazerCotacao == null) {
			escolhidaParaFazerCotacao = false;
		}
		return escolhidaParaFazerCotacao;
	}

	public void setEscolhidaParaFazerCotacao(Boolean escolhidaParaFazerCotacao) {
		this.escolhidaParaFazerCotacao = escolhidaParaFazerCotacao;
	}
	
	

	public FuncionarioVO getOrientadorPadraoEstagio() {
		if (orientadorPadraoEstagio == null) {
			orientadorPadraoEstagio = new FuncionarioVO();
		}
		return orientadorPadraoEstagio;
	}

	public void setOrientadorPadraoEstagio(FuncionarioVO orientadorPadraoEstagio) {
		this.orientadorPadraoEstagio = orientadorPadraoEstagio;
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

	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		if (abreviatura == null) {
			abreviatura = "";
		}
		return abreviatura;
	}

	/**
	 * @param abreviatura
	 *            the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * @return the configuracoes
	 */
	public ConfiguracoesVO getConfiguracoes() {
		if (configuracoes == null) {
			configuracoes = new ConfiguracoesVO();
		}
		return configuracoes;
	}

	/**
	 * @param configuracoes
	 *            the configuracoes to set
	 */
	public void setConfiguracoes(ConfiguracoesVO configuracoes) {
		this.configuracoes = configuracoes;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the numeroDocumento
	 */
	public Integer getNumeroDocumento() {
		if (numeroDocumento == null) {
			numeroDocumento = 1;
		}
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(Integer numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Integer getCodigoIES() {
		if (codigoIES == null) {
			codigoIES = 0;
		}
		return codigoIES;
	}

	public void setCodigoIES(Integer codigoIES) {
		this.codigoIES = codigoIES;
	}

	/**
	 * @return the credenciamentoPortaria
	 */
	public String getCredenciamentoPortaria() {
		if (credenciamentoPortaria == null) {
			credenciamentoPortaria = "";
		}
		return credenciamentoPortaria;
	}

	/**
	 * @param credenciamentoPortaria
	 *            the credenciamentoPortaria to set
	 */
	public void setCredenciamentoPortaria(String credenciamentoPortaria) {
		this.credenciamentoPortaria = credenciamentoPortaria;
	}

	/**
	 * @return the dataPublicacaoDO
	 */
	public Date getDataPublicacaoDO() {
		return dataPublicacaoDO;
	}

	/**
	 * @param dataPublicacaoDO
	 *            the dataPublicacaoDO to set
	 */
	public void setDataPublicacaoDO(Date dataPublicacaoDO) {
		this.dataPublicacaoDO = dataPublicacaoDO;
	}

	public FuncionarioVO getDiretorGeral() {
		if (diretorGeral == null) {
			diretorGeral = new FuncionarioVO();
		}
		return diretorGeral;
	}

	public void setDiretorGeral(FuncionarioVO diretorGeral) {
		this.diretorGeral = diretorGeral;
	}

	public String getMantenedora() {
		if (mantenedora == null) {
			mantenedora = "";
		}
		return mantenedora;
	}

	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

	/**
	 * @return the permitirVisualizacaoLogin
	 */
	public Boolean getPermitirVisualizacaoLogin() {
		if (permitirVisualizacaoLogin == null) {
			return false;
		}
		return permitirVisualizacaoLogin;
	}

	/**
	 * @param permitirVisualizacaoLogin
	 *            the permitirVisualizacaoLogin to set
	 */
	public void setPermitirVisualizacaoLogin(Boolean permitirVisualizacaoLogin) {
		this.permitirVisualizacaoLogin = permitirVisualizacaoLogin;
	}

	public Boolean getUsarConfiguracaoPadrao() {
		if (usarConfiguracaoPadrao == null) {
			usarConfiguracaoPadrao = Boolean.FALSE;
		}
		return usarConfiguracaoPadrao;
	}

	public void setUsarConfiguracaoPadrao(Boolean usarConfiguracaoPadrao) {
		this.usarConfiguracaoPadrao = usarConfiguracaoPadrao;
	}

	public Boolean getApresentarTelaProcessoSeletivo() {
		if (apresentarTelaProcessoSeletivo == null) {
			apresentarTelaProcessoSeletivo = Boolean.FALSE;
		}
		return apresentarTelaProcessoSeletivo;
	}

	public void setApresentarTelaProcessoSeletivo(Boolean apresentarTelaProcessoSeletivo) {
		this.apresentarTelaProcessoSeletivo = apresentarTelaProcessoSeletivo;
	}

	/**
	 * @return the responsavelCobrancaUnidade
	 */
	public PessoaVO getResponsavelCobrancaUnidade() {
		if (responsavelCobrancaUnidade == null) {
			responsavelCobrancaUnidade = new PessoaVO();
		}
		return responsavelCobrancaUnidade;
	}

	/**
	 * @param responsavelCobrancaUnidade
	 *            the responsavelCobrancaUnidade to set
	 */
	public void setResponsavelCobrancaUnidade(PessoaVO responsavelCobrancaUnidade) {
		this.responsavelCobrancaUnidade = responsavelCobrancaUnidade;
	}

//	public ContaCorrenteVO getContaCorrentePadraoVO() {
//		if (contaCorrentePadraoVO == null) {
//			contaCorrentePadraoVO = new ContaCorrenteVO();
//		}
//		return contaCorrentePadraoVO;
//	}
//
//	public void setContaCorrentePadraoVO(ContaCorrenteVO contaCorrentePadraoVO) {
//		this.contaCorrentePadraoVO = contaCorrentePadraoVO;
//	}

	public String getCaminhoBaseLogo() {
		if (caminhoBaseLogo == null) {
			caminhoBaseLogo = "";
		}
		return caminhoBaseLogo;
	}

	public void setCaminhoBaseLogo(String caminhoBaseLogo) {
		this.caminhoBaseLogo = caminhoBaseLogo;
	}

	public String getNomeArquivoLogo() {
		if (nomeArquivoLogo == null) {
			nomeArquivoLogo = "";
		}
		return nomeArquivoLogo;
	}

	public void setNomeArquivoLogo(String nomeArquivoLogo) {
		this.nomeArquivoLogo = nomeArquivoLogo;
	}

	public Boolean getLogoInformada() {
		if (logoInformada == null) {
			logoInformada = false;
		}
		return logoInformada;
	}

	public void setLogoInformada(Boolean logoInformada) {
		this.logoInformada = logoInformada;
	}

	public Boolean getExisteLogo() {
		return !getNomeArquivoLogo().trim().isEmpty();
	}

	public Boolean getExisteLogoIndex() {
		return !getNomeArquivoLogoIndex().trim().isEmpty();
	}

	public Boolean getExisteLogoRelatorio() {
		return !getNomeArquivoLogoRelatorio().trim().isEmpty();
	}

	public String getCaminhoBaseLogoIndex() {
		if (caminhoBaseLogoIndex == null) {
			caminhoBaseLogoIndex = "";
		}
		return caminhoBaseLogoIndex;
	}

	public void setCaminhoBaseLogoIndex(String caminhoBaseLogoIndex) {
		this.caminhoBaseLogoIndex = caminhoBaseLogoIndex;
	}

	public String getNomeArquivoLogoIndex() {
		if (nomeArquivoLogoIndex == null) {
			nomeArquivoLogoIndex = "";
		}
		return nomeArquivoLogoIndex;
	}

	public void setNomeArquivoLogoIndex(String nomeArquivoLogoIndex) {
		this.nomeArquivoLogoIndex = nomeArquivoLogoIndex;
	}

	public Boolean getLogoInformadaIndex() {
		if (logoInformadaIndex == null) {
			logoInformadaIndex = false;
		}
		return logoInformadaIndex;
	}

	public void setLogoInformadaIndex(Boolean logoInformadaIndex) {
		this.logoInformadaIndex = logoInformadaIndex;
	}

	public List<MaterialUnidadeEnsinoVO> getMaterialUnidadeEnsinoVOs() {
		if (materialUnidadeEnsinoVOs == null) {
			materialUnidadeEnsinoVOs = new ArrayList<MaterialUnidadeEnsinoVO>(0);
		}
		return materialUnidadeEnsinoVOs;
	}

	public void setMaterialUnidadeEnsinoVOs(List materialUnidadeEnsinoVOs) {
		this.materialUnidadeEnsinoVOs = materialUnidadeEnsinoVOs;
	}

	public Boolean getFiltrarUnidadeEnsino() {
		if (filtrarUnidadeEnsino == null) {
			filtrarUnidadeEnsino = false;
		}
		return filtrarUnidadeEnsino;
	}

	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	public TurmaVO getTurma() {
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public String getCaminhoBaseLogoRelatorio() {
		if (caminhoBaseLogoRelatorio == null) {
			caminhoBaseLogoRelatorio = "";
		}
		return caminhoBaseLogoRelatorio;
	}

	public void setCaminhoBaseLogoRelatorio(String caminhoBaseLogoRelatorio) {
		this.caminhoBaseLogoRelatorio = caminhoBaseLogoRelatorio;
	}

	public String getNomeArquivoLogoRelatorio() {
		if (nomeArquivoLogoRelatorio == null) {
			nomeArquivoLogoRelatorio = "";
		}
		return nomeArquivoLogoRelatorio;
	}

	public void setNomeArquivoLogoRelatorio(String nomeArquivoLogoRelatorio) {
		this.nomeArquivoLogoRelatorio = nomeArquivoLogoRelatorio;
	}

	public Boolean getLogoInformadaRelatorio() {
		if (logoInformadaRelatorio == null) {
			logoInformadaRelatorio = false;
		}
		return logoInformadaRelatorio;
	}

	public void setLogoInformadaRelatorio(Boolean logoInformadaRelatorio) {
		this.logoInformadaRelatorio = logoInformadaRelatorio;
	}

	public FuncionarioVO getCoordenadorTCC() {
		if (coordenadorTCC == null) {
			coordenadorTCC = new FuncionarioVO();
		}
		return coordenadorTCC;
	}

	public void setCoordenadorTCC(FuncionarioVO coordenadorTCC) {
		this.coordenadorTCC = coordenadorTCC;
	}

	public String getCredenciamento() {
		if (credenciamento == null) {
			credenciamento = "";
		}
		return credenciamento;
	}

	public void setCredenciamento(String credenciamento) {
		this.credenciamento = credenciamento;
	}

	

	public Boolean getSelecionarAdicionarCursoInstituicao() {
		if (selecionarAdicionarCursoInstituicao == null) {
			selecionarAdicionarCursoInstituicao = Boolean.FALSE;
		}
		return selecionarAdicionarCursoInstituicao;
	}

	public void setSelecionarAdicionarCursoInstituicao(Boolean selecionarAdicionarCursoInstituicao) {
		this.selecionarAdicionarCursoInstituicao = selecionarAdicionarCursoInstituicao;
	}

	public String getNomeExpedicaoDiploma() {
		if (nomeExpedicaoDiploma == null) {
			nomeExpedicaoDiploma = "";
		}
		return nomeExpedicaoDiploma;
	}

	public void setNomeExpedicaoDiploma(String nomeExpedicaoDiploma) {
		this.nomeExpedicaoDiploma = nomeExpedicaoDiploma;
	}

	public String getDependenciaAdministrativa() {
		if (dependenciaAdministrativa == null) {
			dependenciaAdministrativa = "PR";
		}
		return dependenciaAdministrativa;
	}

	public void setDependenciaAdministrativa(String dependenciaAdministrativa) {
		this.dependenciaAdministrativa = dependenciaAdministrativa;
	}

	public String getLocalizacaoZonaEscola() {
		if (localizacaoZonaEscola == null) {
			localizacaoZonaEscola = "UR";
		}
		return localizacaoZonaEscola;
	}

	public void setLocalizacaoZonaEscola(String localizacaoZonaEscola) {
		this.localizacaoZonaEscola = localizacaoZonaEscola;
	}

	public String getCategoriaEscolaPrivada() {
		if (categoriaEscolaPrivada == null) {
			categoriaEscolaPrivada = "";
		}
		return categoriaEscolaPrivada;
	}

	public void setCategoriaEscolaPrivada(String categoriaEscolaPrivada) {
		this.categoriaEscolaPrivada = categoriaEscolaPrivada;
	}

	public String getConveniadaPoderPublico() {
		if (conveniadaPoderPublico == null) {
			conveniadaPoderPublico = "";
		}
		return conveniadaPoderPublico;
	}

	public void setConveniadaPoderPublico(String conveniadaPoderPublico) {
		this.conveniadaPoderPublico = conveniadaPoderPublico;
	}

	public String getLocalFuncionamentoDaEscola() {
		if (localFuncionamentoDaEscola == null) {
			localFuncionamentoDaEscola = "";
		}
		return localFuncionamentoDaEscola;
	}

	public void setLocalFuncionamentoDaEscola(String localFuncionamentoDaEscola) {
		this.localFuncionamentoDaEscola = localFuncionamentoDaEscola;
	}

	public String getFormaOcupacaoPredio() {
		if (formaOcupacaoPredio == null) {
			formaOcupacaoPredio = "";
		}
		return formaOcupacaoPredio;
	}

	public void setFormaOcupacaoPredio(String formaOcupacaoPredio) {
		this.formaOcupacaoPredio = formaOcupacaoPredio;
	}

	public Boolean getPredioCompartilhado() {
		if (predioCompartilhado == null) {
			predioCompartilhado = Boolean.FALSE;
		}
		return predioCompartilhado;
	}

	public void setPredioCompartilhado(Boolean predioCompartilhado) {
		this.predioCompartilhado = predioCompartilhado;
	}

	public String getCodigoEscolaCompartilhada1() {
		if (codigoEscolaCompartilhada1 == null) {
			codigoEscolaCompartilhada1 = "";
		}
		return codigoEscolaCompartilhada1;
	}

	public void setCodigoEscolaCompartilhada1(String codigoEscolaCompartilhada1) {
		this.codigoEscolaCompartilhada1 = codigoEscolaCompartilhada1;
	}

	public String getCodigoEscolaCompartilhada2() {
		if (codigoEscolaCompartilhada2 == null) {
			codigoEscolaCompartilhada2 = "";
		}
		return codigoEscolaCompartilhada2;
	}

	public void setCodigoEscolaCompartilhada2(String codigoEscolaCompartilhada2) {
		this.codigoEscolaCompartilhada2 = codigoEscolaCompartilhada2;
	}

	public String getCodigoEscolaCompartilhada3() {
		if (codigoEscolaCompartilhada3 == null) {
			codigoEscolaCompartilhada3 = "";
		}
		return codigoEscolaCompartilhada3;
	}

	public void setCodigoEscolaCompartilhada3(String codigoEscolaCompartilhada3) {
		this.codigoEscolaCompartilhada3 = codigoEscolaCompartilhada3;
	}

	public String getCodigoEscolaCompartilhada4() {
		if (codigoEscolaCompartilhada4 == null) {
			codigoEscolaCompartilhada4 = "";
		}
		return codigoEscolaCompartilhada4;
	}

	public void setCodigoEscolaCompartilhada4(String codigoEscolaCompartilhada4) {
		this.codigoEscolaCompartilhada4 = codigoEscolaCompartilhada4;
	}

	public String getCodigoEscolaCompartilhada5() {
		if (codigoEscolaCompartilhada5 == null) {
			codigoEscolaCompartilhada5 = "";
		}
		return codigoEscolaCompartilhada5;
	}

	public void setCodigoEscolaCompartilhada5(String codigoEscolaCompartilhada5) {
		this.codigoEscolaCompartilhada5 = codigoEscolaCompartilhada5;
	}

	public String getCodigoEscolaCompartilhada6() {
		if(codigoEscolaCompartilhada6 == null){
			codigoEscolaCompartilhada6 = "";
		}
		return codigoEscolaCompartilhada6;
	}

	public void setCodigoEscolaCompartilhada6(String codigoEscolaCompartilhada6) {
		this.codigoEscolaCompartilhada6 = codigoEscolaCompartilhada6;
	}

	public String getAguaConsumida() {
		if(aguaConsumida == null){
			aguaConsumida = "";
		}
		return aguaConsumida;
	}

	public void setAguaConsumida(String aguaConsumida) {
		this.aguaConsumida = aguaConsumida;
	}

	public String getAbastecimentoAgua() {
		if(abastecimentoAgua == null){
			abastecimentoAgua = "";
		}
		return abastecimentoAgua;
	}

	public void setAbastecimentoAgua(String abastecimentoAgua) {
		this.abastecimentoAgua = abastecimentoAgua;
	}

	public String getAbastecimentoEnergia() {
		if(abastecimentoEnergia == null){
			abastecimentoEnergia = "";
		}
		return abastecimentoEnergia;
	}

	public void setAbastecimentoEnergia(String abastecimentoEnergia) {
		this.abastecimentoEnergia = abastecimentoEnergia;
	}

	public String getEsgotoSanitario() {
		if(esgotoSanitario == null){
			esgotoSanitario = "";
		}
		return esgotoSanitario;
	}

	public void setEsgotoSanitario(String esgotoSanitario) {
		this.esgotoSanitario = esgotoSanitario;
	}

	public String getDestinoLixo() {
		if(destinoLixo == null){
			destinoLixo = "";
		}
		return destinoLixo;
	}

	public void setDestinoLixo(String destinoLixo) {
		this.destinoLixo = destinoLixo;
	}

	public Boolean getSalaDiretoria() {
		if(salaDiretoria == null){
			salaDiretoria = Boolean.FALSE;
		}
		return salaDiretoria;
	}

	public void setSalaDiretoria(Boolean salaDiretoria) {
		this.salaDiretoria = salaDiretoria;
	}

	public Boolean getSalaProfessores() {
		if(salaProfessores == null){
			salaProfessores = Boolean.FALSE;
		}
		return salaProfessores;
	}

	public void setSalaProfessores(Boolean salaProfessores) {
		this.salaProfessores = salaProfessores;
	}

	public Boolean getSalaSecretaria() {
		if(salaSecretaria == null){
			salaSecretaria = Boolean.FALSE;
		}
		return salaSecretaria;
	}

	public void setSalaSecretaria(Boolean salaSecretaria) {
		this.salaSecretaria = salaSecretaria;
	}

	public Boolean getLaboratorioInformatica() {
		if(laboratorioInformatica == null){
			laboratorioInformatica = Boolean.FALSE;
		}
		return laboratorioInformatica;
	}

	public void setLaboratorioInformatica(Boolean laboratorioInformatica) {
		this.laboratorioInformatica = laboratorioInformatica;
	}

	public Boolean getLaboratorioCiencias() {
		if(laboratorioCiencias == null){
			laboratorioCiencias = Boolean.FALSE;
		}
		return laboratorioCiencias;
	}

	public void setLaboratorioCiencias(Boolean laboratorioCiencias) {
		this.laboratorioCiencias = laboratorioCiencias;
	}

	public Boolean getRecursosMultifuncionais() {
		if(recursosMultifuncionais == null){
			recursosMultifuncionais = Boolean.FALSE;
		}
		return recursosMultifuncionais;
	}

	public void setRecursosMultifuncionais(Boolean recursosMultifuncionais) {
		this.recursosMultifuncionais = recursosMultifuncionais;
	}

	public Boolean getQuadraEsportesCoberta() {
		if(quadraEsportesCoberta == null){
			quadraEsportesCoberta = Boolean.FALSE;
		}
		return quadraEsportesCoberta;
	}

	public void setQuadraEsportesCoberta(Boolean quadraEsportesCoberta) {
		this.quadraEsportesCoberta = quadraEsportesCoberta;
	}

	public Boolean getQuadraEsportesDescoberta() {
		if(quadraEsportesDescoberta == null){
			quadraEsportesDescoberta = Boolean.FALSE;
		}
		return quadraEsportesDescoberta;
	}

	public void setQuadraEsportesDescoberta(Boolean quadraEsportesDescoberta) {
		this.quadraEsportesDescoberta = quadraEsportesDescoberta;
	}

	public Boolean getCozinha() {
		if(cozinha == null){
			cozinha = Boolean.FALSE;
		}
		return cozinha;
	}

	public void setCozinha(Boolean cozinha) {
		this.cozinha = cozinha;
	}

	public Boolean getBiblioteca() {
		if(biblioteca == null){
			biblioteca = Boolean.FALSE;
		}
		return biblioteca;
	}

	public void setBiblioteca(Boolean biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Boolean getSalaLeitura() {
		if(salaLeitura == null){
			salaLeitura = Boolean.FALSE;
		}
		return salaLeitura;
	}

	public void setSalaLeitura(Boolean salaLeitura) {
		this.salaLeitura = salaLeitura;
	}

	public Boolean getParqueInfantil() {
		if(parqueInfantil == null){
			parqueInfantil = Boolean.FALSE;
		}
		return parqueInfantil;
	}

	public void setParqueInfantil(Boolean parqueInfantil) {
		this.parqueInfantil = parqueInfantil;
	}

	public Boolean getBercario() {
		if(bercario == null){
			bercario = Boolean.FALSE;
		}
		return bercario;
	}

	public void setBercario(Boolean bercario) {
		this.bercario = bercario;
	}

	public Boolean getBanheiroForaPredio() {
		if(banheiroForaPredio == null){
			banheiroForaPredio = Boolean.FALSE;
		}
		return banheiroForaPredio;
	}

	public void setBanheiroForaPredio(Boolean banheiroForaPredio) {
		this.banheiroForaPredio = banheiroForaPredio;
	}

	public Boolean getBanheiroDentroPredio() {
		if(banheiroDentroPredio == null){
			banheiroDentroPredio = Boolean.FALSE;
		}
		return banheiroDentroPredio;
	}

	public void setBanheiroDentroPredio(Boolean banheiroDentroPredio) {
		this.banheiroDentroPredio = banheiroDentroPredio;
	}

	public Boolean getBanheiroEducacaoInfantil() {
		if(banheiroEducacaoInfantil == null){
			banheiroEducacaoInfantil = Boolean.FALSE;
		}
		return banheiroEducacaoInfantil;
	}

	public void setBanheiroEducacaoInfantil(Boolean banheiroEducacaoInfantil) {
		this.banheiroEducacaoInfantil = banheiroEducacaoInfantil;
	}

	public Boolean getBanheiroDeficiencia() {
		if(banheiroDeficiencia == null){
			banheiroDeficiencia = Boolean.FALSE;
		}
		return banheiroDeficiencia;
	}

	public void setBanheiroDeficiencia(Boolean banheiroDeficiencia) {
		this.banheiroDeficiencia = banheiroDeficiencia;
	}

	public Boolean getViasDeficiencia() {
		if(viasDeficiencia == null){
			viasDeficiencia = Boolean.FALSE;
		}
		return viasDeficiencia;
	}

	public void setViasDeficiencia(Boolean viasDeficiencia) {
		this.viasDeficiencia = viasDeficiencia;
	}

	public Boolean getBanheiroChuveiro() {
		if(banheiroChuveiro == null){
			banheiroChuveiro = Boolean.FALSE;
		}
		return banheiroChuveiro;
	}

	public void setBanheiroChuveiro(Boolean banheiroChuveiro) {
		this.banheiroChuveiro = banheiroChuveiro;
	}

	public Boolean getRefeitorio() {
		if(refeitorio == null){
			refeitorio = Boolean.FALSE;
		}
		return refeitorio;
	}

	public void setRefeitorio(Boolean refeitorio) {
		this.refeitorio = refeitorio;
	}

	public Boolean getDespensa() {
		if(despensa == null){
			despensa = Boolean.FALSE;
		}
		return despensa;
	}

	public void setDespensa(Boolean despensa) {
		this.despensa = despensa;
	}

	public Boolean getAlmoxarifado() {
		if(almoxarifado == null){
			almoxarifado = Boolean.FALSE;
		}
		return almoxarifado;
	}

	public void setAlmoxarifado(Boolean almoxarifado) {
		this.almoxarifado = almoxarifado;
	}

	public Boolean getAuditorio() {
		if(auditorio == null){
			auditorio = Boolean.FALSE;
		}
		return auditorio;
	}

	public void setAuditorio(Boolean auditorio) {
		this.auditorio = auditorio;
	}

	public Boolean getPatioCoberto() {
		if(patioCoberto == null){
			patioCoberto = Boolean.FALSE;
		}
		return patioCoberto;
	}

	public void setPatioCoberto(Boolean patioCoberto) {
		this.patioCoberto = patioCoberto;
	}

	public Boolean getPatioDescoberto() {
		if(patioDescoberto == null){
			patioDescoberto = Boolean.FALSE;
		}
		return patioDescoberto;
	}

	public void setPatioDescoberto(Boolean patioDescoberto) {
		this.patioDescoberto = patioDescoberto;
	}

	public Boolean getAlojamentoAluno() {
		if(alojamentoAluno == null){
			alojamentoAluno = Boolean.FALSE;
		}
		return alojamentoAluno;
	}

	public void setAlojamentoAluno(Boolean alojamentoAluno) {
		this.alojamentoAluno = alojamentoAluno;
	}

	public Boolean getAlojamentoProfessor() {
		if(alojamentoProfessor == null){
			alojamentoProfessor = Boolean.FALSE;
		}
		return alojamentoProfessor;
	}

	public void setAlojamentoProfessor(Boolean alojamentoProfessor) {
		this.alojamentoProfessor = alojamentoProfessor;
	}

	public Boolean getAreaVerde() {
		if(areaVerde == null){
			areaVerde = Boolean.FALSE;
		}
		return areaVerde;
	}

	public void setAreaVerde(Boolean areaVerde) {
		this.areaVerde = areaVerde;
	}

	public Boolean getLavanderia() {
		if(lavanderia == null){
			lavanderia = Boolean.FALSE;
		}
		return lavanderia;
	}

	public void setLavanderia(Boolean lavanderia) {
		this.lavanderia = lavanderia;
	}

	public Boolean getNenhumaDependencia() {
		if(nenhumaDependencia == null){
			nenhumaDependencia = Boolean.FALSE;
		}
		return nenhumaDependencia;
	}

	public void setNenhumaDependencia(Boolean nenhumaDependencia) {
		this.nenhumaDependencia = nenhumaDependencia;
	}

	public String getNumeroSalasAulaExistente() {
		if(numeroSalasAulaExistente == null){
			numeroSalasAulaExistente = "0000";
		}
		return numeroSalasAulaExistente;
	}

	public void setNumeroSalasAulaExistente(String numeroSalasAulaExistente) {
		this.numeroSalasAulaExistente = numeroSalasAulaExistente;
	}

	public String getNumeroSalasDentroForaPredio() {
		if(numeroSalasDentroForaPredio == null){
			numeroSalasDentroForaPredio = "0000";
		}
		return numeroSalasDentroForaPredio;
	}

	public void setNumeroSalasDentroForaPredio(String numeroSalasDentroForaPredio) {
		this.numeroSalasDentroForaPredio = numeroSalasDentroForaPredio;
	}

	public Integer getQuantidadeTelevisao() {
		return quantidadeTelevisao;
	}

	public void setQuantidadeTelevisao(Integer quantidadeTelevisao) {
		this.quantidadeTelevisao = quantidadeTelevisao;
	}

	public Integer getQuantidadeVideoCassete() {
		return quantidadeVideoCassete;
	}

	public void setQuantidadeVideoCassete(Integer quantidadeVideoCassete) {
		this.quantidadeVideoCassete = quantidadeVideoCassete;
	}

	public Integer getQuantidadeDVD() {
		return quantidadeDVD;
	}

	public void setQuantidadeDVD(Integer quantidadeDVD) {
		this.quantidadeDVD = quantidadeDVD;
	}

	public Integer getQuantidadeAntenaParabolica() {
		return quantidadeAntenaParabolica;
	}

	public void setQuantidadeAntenaParabolica(Integer quantidadeAntenaParabolica) {
		this.quantidadeAntenaParabolica = quantidadeAntenaParabolica;
	}

	public Integer getQuantidadeCopiadora() {
		return quantidadeCopiadora;
	}

	public void setQuantidadeCopiadora(Integer quantidadeCopiadora) {
		this.quantidadeCopiadora = quantidadeCopiadora;
	}

	public Integer getQuantidadeRetroprojetor() {
		return quantidadeRetroprojetor;
	}

	public void setQuantidadeRetroprojetor(Integer quantidadeRetroprojetor) {
		this.quantidadeRetroprojetor = quantidadeRetroprojetor;
	}

	public Integer getQuantidadeImpressora() {
		return quantidadeImpressora;
	}

	public void setQuantidadeImpressora(Integer quantidadeImpressora) {
		this.quantidadeImpressora = quantidadeImpressora;
	}

	public Integer getQuantidadeAparelhoSom() {
		return quantidadeAparelhoSom;
	}

	public void setQuantidadeAparelhoSom(Integer quantidadeAparelhoSom) {
		this.quantidadeAparelhoSom = quantidadeAparelhoSom;
	}

	public Integer getQuantidadeProjetorMultimidia() {
		return quantidadeProjetorMultimidia;
	}

	public void setQuantidadeProjetorMultimidia(Integer quantidadeProjetorMultimidia) {
		this.quantidadeProjetorMultimidia = quantidadeProjetorMultimidia;
	}

	public Integer getQuantidadeFax() {
		return quantidadeFax;
	}

	public void setQuantidadeFax(Integer quantidadeFax) {
		this.quantidadeFax = quantidadeFax;
	}

	public Integer getQuantidadeMaquinaFotograficaFilmadora() {
		return quantidadeMaquinaFotograficaFilmadora;
	}

	public void setQuantidadeMaquinaFotograficaFilmadora(Integer quantidadeMaquinaFotograficaFilmadora) {
		this.quantidadeMaquinaFotograficaFilmadora = quantidadeMaquinaFotograficaFilmadora;
	}

	public Integer getQuantidadeComputadores() {
		return quantidadeComputadores;
	}

	public void setQuantidadeComputadores(Integer quantidadeComputadores) {
		this.quantidadeComputadores = quantidadeComputadores;
	}

	public Integer getQuantidadeComputadoresAdministrativos() {
		return quantidadeComputadoresAdministrativos;
	}

	public void setQuantidadeComputadoresAdministrativos(Integer quantidadeComputadoresAdministrativos) {
		this.quantidadeComputadoresAdministrativos = quantidadeComputadoresAdministrativos;
	}

	public Integer getQuantidadeComputadoresAlunos() {
		return quantidadeComputadoresAlunos;
	}

	public void setQuantidadeComputadoresAlunos(Integer quantidadeComputadoresAlunos) {
		this.quantidadeComputadoresAlunos = quantidadeComputadoresAlunos;
	}

	public Boolean getComputadoresAcessoInternet() {
		if(computadoresAcessoInternet == null){
			computadoresAcessoInternet = Boolean.FALSE;
		}
		return computadoresAcessoInternet;
	}

	public void setComputadoresAcessoInternet(Boolean computadoresAcessoInternet) {
		this.computadoresAcessoInternet = computadoresAcessoInternet;
	}

	public Boolean getInternetBandaLarga() {
		if(internetBandaLarga == null){
			internetBandaLarga = Boolean.FALSE;
		}
		return internetBandaLarga;
	}

	public void setInternetBandaLarga(Boolean internetBandaLarga) {
		this.internetBandaLarga = internetBandaLarga;
	}

	public String getCodigoTributacaoMunicipio() {
		if (codigoTributacaoMunicipio == null) {
			codigoTributacaoMunicipio = "";
		}
		return codigoTributacaoMunicipio;
	}

	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		this.codigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 13/08/2015 5.0.3.5
	 */
	private String codigoOrgaoRegionalEnsino;
	private String codigoDistritoCenso;

	public String getCodigoOrgaoRegionalEnsino() {
		if(codigoOrgaoRegionalEnsino == null) {
			codigoOrgaoRegionalEnsino = "";
		}
		return codigoOrgaoRegionalEnsino;
	}

	public void setCodigoOrgaoRegionalEnsino(String codigoOrgaoRegionalEnsino) {
		this.codigoOrgaoRegionalEnsino = codigoOrgaoRegionalEnsino;
	}

	public String getCodigoDistritoCenso() {
		if(codigoDistritoCenso == null) {
			codigoDistritoCenso = "";
		}
		return codigoDistritoCenso;
	}

	public void setCodigoDistritoCenso(String codigoDistritoCenso) {
		this.codigoDistritoCenso = codigoDistritoCenso;
	}

	public Boolean getApresentarHomePreInscricao() {
		if (apresentarHomePreInscricao == null) {
			apresentarHomePreInscricao = true;
		}
		return apresentarHomePreInscricao;
	}

	public void setApresentarHomePreInscricao(Boolean apresentarHomePreInscricao) {
		this.apresentarHomePreInscricao = apresentarHomePreInscricao;
	}

	public String getLeiCriacao1() {
		if (leiCriacao1 == null) {
			leiCriacao1 = "";
		}
		return leiCriacao1;
	}

	public void setLeiCriacao1(String leiCriacao1) {
		this.leiCriacao1 = leiCriacao1;
	}

	public String getLeiCriacao2() {
		if (leiCriacao2 == null) {
			leiCriacao2 = "";
		}
		return leiCriacao2;
	}

	public void setLeiCriacao2(String leiCriacao2) {
		this.leiCriacao2 = leiCriacao2;
	}
	
	public Boolean getApresentarCodigoInscricaoOVG () {
		if (this.getCidade().getEstado().getSigla().equalsIgnoreCase("GO")) {
			return true;
		} else {
			return false;
		}
	}

	public String getNomeArquivoLogoPaginaInicial() {
		if (nomeArquivoLogoPaginaInicial == null) {
			nomeArquivoLogoPaginaInicial = "";
		}
		return nomeArquivoLogoPaginaInicial;
	}

	public void setNomeArquivoLogoPaginaInicial(String nomeArquivoLogoPaginaInicial) {
		this.nomeArquivoLogoPaginaInicial = nomeArquivoLogoPaginaInicial;
	}

	public Boolean getLogoInformadaInicial() {
		if (logoInformadaInicial == null) {
			logoInformadaInicial = false;
		}
		return logoInformadaInicial;
	}

	public void setLogoInformadaInicial(Boolean logoInformadaInicial) {
		this.logoInformadaInicial = logoInformadaInicial;
	}

	public String getCaminhoBaseLogoPaginaInicial() {
		if (caminhoBaseLogoPaginaInicial == null) {
			caminhoBaseLogoPaginaInicial = "";
		}
		return caminhoBaseLogoPaginaInicial;
	}

	public void setCaminhoBaseLogoPaginaInicial(String caminhoBaseLogoPaginaInicial) {
		this.caminhoBaseLogoPaginaInicial = caminhoBaseLogoPaginaInicial;
	}
	
	public Boolean getExisteLogoPaginaInicial() {
		return !getNomeArquivoLogoPaginaInicial().trim().isEmpty();
	}
	
	public String getCaminhoBaseLogoEmailCima() {
		if (caminhoBaseLogoEmailCima == null) {
			caminhoBaseLogoEmailCima = "";
		}
		return caminhoBaseLogoEmailCima;
	}

	public void setCaminhoBaseLogoEmailCima(String caminhoBaseLogoEmailCima) {
		this.caminhoBaseLogoEmailCima = caminhoBaseLogoEmailCima;
	}

	public String getNomeArquivoLogoEmailCima() {
		if (nomeArquivoLogoEmailCima == null) {
			nomeArquivoLogoEmailCima = "";
		}
		return nomeArquivoLogoEmailCima;
	}

	public void setNomeArquivoLogoEmailCima(String nomeArquivoLogoEmailCima) {
		this.nomeArquivoLogoEmailCima = nomeArquivoLogoEmailCima;
	}

	public Boolean getLogoInformadaEmailCima() {
		if (logoInformadaEmailCima == null) {
			logoInformadaEmailCima = false;
		}
		return logoInformadaEmailCima;
	}

	public void setLogoInformadaEmailCima(Boolean logoInformadaEmailCima) {
		this.logoInformadaEmailCima = logoInformadaEmailCima;
	}
	
	public Boolean getExisteLogoEmailCima() {
		return !getNomeArquivoLogoEmailCima().trim().isEmpty();
	}

	public String getCaminhoBaseLogoEmailBaixo() {
		if (caminhoBaseLogoEmailBaixo == null) {
			caminhoBaseLogoEmailBaixo = "";
		}
		return caminhoBaseLogoEmailBaixo;
	}

	public void setCaminhoBaseLogoEmailBaixo(String caminhoBaseLogoEmailBaixo) {
		this.caminhoBaseLogoEmailBaixo = caminhoBaseLogoEmailBaixo;
	}

	public String getNomeArquivoLogoEmailBaixo() {
		if (nomeArquivoLogoEmailBaixo == null) {
			nomeArquivoLogoEmailBaixo = "";
		}
		return nomeArquivoLogoEmailBaixo;
	}

	public void setNomeArquivoLogoEmailBaixo(String nomeArquivoLogoEmailBaixo) {
		this.nomeArquivoLogoEmailBaixo = nomeArquivoLogoEmailBaixo;
	}

	public Boolean getLogoInformadaEmailBaixo() {
		if (logoInformadaEmailBaixo == null) {
			logoInformadaEmailBaixo = false;
		}
		return logoInformadaEmailBaixo;
	}

	public void setLogoInformadaEmailBaixo(Boolean logoInformadaEmailBaixo) {
		this.logoInformadaEmailBaixo = logoInformadaEmailBaixo;
	}
	
	public Boolean getExisteLogoEmailBaixo() {
		return !getNomeArquivoLogoEmailBaixo().trim().isEmpty();
	}
	
	public String getCaminhoBaseLogoMunicipio() {
		if (caminhoBaseLogoMunicipio == null) {
			caminhoBaseLogoMunicipio = "";
		}
		return caminhoBaseLogoMunicipio;
	}

	public void setCaminhoBaseLogoMunicipio(String caminhoBaseLogoMunicipio) {
		this.caminhoBaseLogoMunicipio = caminhoBaseLogoMunicipio;
	}

	public String getNomeArquivoLogoMunicipio() {
		if (nomeArquivoLogoMunicipio == null) {
			nomeArquivoLogoMunicipio = "";
		}
		return nomeArquivoLogoMunicipio;
	}

	public void setNomeArquivoLogoMunicipio(String nomeArquivoLogoMunicipio) {
		this.nomeArquivoLogoMunicipio = nomeArquivoLogoMunicipio;
	}

	public Boolean getLogoInformadaMunicipio() {
		if (logoInformadaMunicipio == null) {
			logoInformadaMunicipio = false;
		}
		return logoInformadaMunicipio;
	}

	public void setLogoInformadaMunicipio(Boolean logoInformadaMunicipio) {
		this.logoInformadaMunicipio = logoInformadaMunicipio;
	}
	
	public Boolean getExisteLogoMunicipio() {
		return !getNomeArquivoLogoMunicipio().trim().isEmpty();
	}
	
	public Boolean getExisteLogoAplicativo() {
		return !getNomeArquivoLogoAplicativo().trim().isEmpty();
	}

	public Boolean getLogoInformadaAplicativo() {
		if(logoInformadaAplicativo == null){
			logoInformadaAplicativo = false;
		}
		return logoInformadaAplicativo;
	}

	public void setLogoInformadaAplicativo(Boolean logoInformadaAplicativo) {
		this.logoInformadaAplicativo = logoInformadaAplicativo;
	}


	public String getCaminhoBaseLogoAplicativo() {
		if(caminhoBaseLogoAplicativo == null){
			caminhoBaseLogoAplicativo = "";
		}
		return caminhoBaseLogoAplicativo;
	}

	public void setCaminhoBaseLogoAplicativo(String caminhoBaseLogoAplicativo) {
		this.caminhoBaseLogoAplicativo = caminhoBaseLogoAplicativo;
	}

	public String getNomeArquivoLogoAplicativo() {
		if(nomeArquivoLogoAplicativo == null){
			nomeArquivoLogoAplicativo = "";
		}
		return nomeArquivoLogoAplicativo;
	}

	public void setNomeArquivoLogoAplicativo(String nomeArquivoLogoAplicativo) {
		this.nomeArquivoLogoAplicativo = nomeArquivoLogoAplicativo;
	}

	public String getCaminhoLogoUnidadeEnsinoUsarAplicativo() {
		if(caminhoLogoUnidadeEnsinoUsarAplicativo == null){
			caminhoLogoUnidadeEnsinoUsarAplicativo = "";
		}
		return caminhoLogoUnidadeEnsinoUsarAplicativo;
	}

	public void setCaminhoLogoUnidadeEnsinoUsarAplicativo(String caminhoLogoUnidadeEnsinoUsarAplicativo) {
		this.caminhoLogoUnidadeEnsinoUsarAplicativo = caminhoLogoUnidadeEnsinoUsarAplicativo;
	}
	
	

	public List<UnidadeEnsinoCursoCentroResultadoVO> getUnidadeEnsinoCursoCentroResultado() {
		unidadeEnsinoCentroResultado = Optional.ofNullable(unidadeEnsinoCentroResultado).orElse(new ArrayList<>());
		return unidadeEnsinoCentroResultado;
	}

	public void setUnidadeEnsinoCursoCentroResultado(List<UnidadeEnsinoCursoCentroResultadoVO> unidadeEnsinoCentroResultado) {
		this.unidadeEnsinoCentroResultado = unidadeEnsinoCentroResultado;
	}
	

	

	public List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> getUnidadeEnsinoNivelEducacionalCentroResultado() {
		unidadeEnsinoNivelEducacionalCentroResultado = Optional.ofNullable(unidadeEnsinoNivelEducacionalCentroResultado).orElse(new ArrayList<>());
		return unidadeEnsinoNivelEducacionalCentroResultado;
	}

	public void setUnidadeEnsinoNivelEducacionalCentroResultado(List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> unidadeEnsinoNivelEducacionalCentroResultado) {
		this.unidadeEnsinoNivelEducacionalCentroResultado = unidadeEnsinoNivelEducacionalCentroResultado;
	}

	public List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> getUnidadeEnsinoTipoRequerimentoCentroResultado() {
		unidadeEnsinoTipoRequerimentoCentroResultado = Optional.ofNullable(unidadeEnsinoTipoRequerimentoCentroResultado).orElse(new ArrayList<>());
		return unidadeEnsinoTipoRequerimentoCentroResultado;
	}

	public void setUnidadeEnsinoTipoRequerimentoCentroResultado(List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> unidadeEnsinoTipoRequerimentoCentroResultado) {
		this.unidadeEnsinoTipoRequerimentoCentroResultado = unidadeEnsinoTipoRequerimentoCentroResultado;
	}

	public String getCodigoIntegracaoContabil() {
		if(codigoIntegracaoContabil == null){
			codigoIntegracaoContabil = "";
		}
		return codigoIntegracaoContabil;
	}

	public void setCodigoIntegracaoContabil(String codigoIntegracaoContabil) {
		this.codigoIntegracaoContabil = codigoIntegracaoContabil;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnidadeEnsinoVO other = (UnidadeEnsinoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	


    public Integer getContaCorrentePadraoBiblioteca() {
        if (contaCorrentePadraoBiblioteca == null) {
            contaCorrentePadraoBiblioteca = 0;
        }
        return contaCorrentePadraoBiblioteca;
    }

    public void setContaCorrentePadraoBiblioteca(Integer contaCorrentePadraoBiblioteca) {
        this.contaCorrentePadraoBiblioteca = contaCorrentePadraoBiblioteca;
    }

    public Integer getContaCorrentePadraoMatricula() {
        if (contaCorrentePadraoMatricula == null) {
            contaCorrentePadraoMatricula = 0;
        }
        return contaCorrentePadraoMatricula;
    }

    public void setContaCorrentePadraoMatricula(Integer contaCorrentePadraoMatricula) {
        this.contaCorrentePadraoMatricula = contaCorrentePadraoMatricula;
    }

    public Integer getContaCorrentePadraoMensalidade() {
        if (contaCorrentePadraoMensalidade == null) {
            contaCorrentePadraoMensalidade = 0;
        }
        return contaCorrentePadraoMensalidade;
    }

    public void setContaCorrentePadraoMensalidade(Integer contaCorrentePadraoMensalidade) {
        this.contaCorrentePadraoMensalidade = contaCorrentePadraoMensalidade;
    }

    public Integer getContaCorrentePadraoProcessoSeletivo() {
        if (contaCorrentePadraoProcessoSeletivo == null) {
            contaCorrentePadraoProcessoSeletivo = 0;
        }
        return contaCorrentePadraoProcessoSeletivo;
    }

    public void setContaCorrentePadraoProcessoSeletivo(Integer contaCorrentePadraoProcessoSeletivo) {
        this.contaCorrentePadraoProcessoSeletivo = contaCorrentePadraoProcessoSeletivo;
    }

    public Integer getContaCorrentePadraoRequerimento() {
        if (contaCorrentePadraoRequerimento == null) {
            contaCorrentePadraoRequerimento = 0;
        }
        return contaCorrentePadraoRequerimento;
    }

    public void setContaCorrentePadraoRequerimento(Integer contaCorrentePadraoRequerimento) {
        this.contaCorrentePadraoRequerimento = contaCorrentePadraoRequerimento;
    }

    public Integer getContaCorrentePadraoNegociacao() {
        if (contaCorrentePadraoNegociacao == null) {
            contaCorrentePadraoNegociacao = 0;
        }
        return contaCorrentePadraoNegociacao;
    }

    public void setContaCorrentePadraoNegociacao(Integer contaCorrentePadraoNegociacao) {
        this.contaCorrentePadraoNegociacao = contaCorrentePadraoNegociacao;
    }

	public Integer getContaCorrentePadraoMaterialDidatico() {
		if (contaCorrentePadraoMaterialDidatico == null) {
			contaCorrentePadraoMaterialDidatico = 0;
		}
		return contaCorrentePadraoMaterialDidatico;
	}

	public void setContaCorrentePadraoMaterialDidatico(Integer contaCorrentePadraoMaterialDidatico) {
		this.contaCorrentePadraoMaterialDidatico = contaCorrentePadraoMaterialDidatico;
	}

	public Integer getContaCorrentePadraoDevolucaoCheque() {
		if (contaCorrentePadraoDevolucaoCheque == null) {
			contaCorrentePadraoDevolucaoCheque = 0;
		}
		return contaCorrentePadraoDevolucaoCheque;
	}

	public void setContaCorrentePadraoDevolucaoCheque(Integer contaCorrentePadraoDevolucaoCheque) {
		this.contaCorrentePadraoDevolucaoCheque = contaCorrentePadraoDevolucaoCheque;
	}

	public String getInformacoesAdicionaisEndereco() {
		if (informacoesAdicionaisEndereco == null) {
			informacoesAdicionaisEndereco = "";
		}
		return informacoesAdicionaisEndereco;
	}

	public void setInformacoesAdicionaisEndereco(String informacoesAdicionaisEndereco) {
		this.informacoesAdicionaisEndereco = informacoesAdicionaisEndereco;
	}
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}
	
	public Integer getCodigoIESMantenedora() {
		if (codigoIESMantenedora == null) {
			codigoIESMantenedora = 0;
		}
		return codigoIESMantenedora;
	}

	public void setCodigoIESMantenedora(Integer codigoIESMantenedora) {
		this.codigoIESMantenedora = codigoIESMantenedora;
	}	
	public String getNome_CNPJ() {
		return getNome() + " - " + getCNPJ();
	}
	
	public String getCnpjMantenedora() {
		if (cnpjMantenedora == null) {
			cnpjMantenedora = "";
		}
		return cnpjMantenedora;
	}

	public void setCnpjMantenedora(String cnpjMantenedora) {
		this.cnpjMantenedora = cnpjMantenedora;
	}

	public String getUnidadeCertificadora() {
		if (unidadeCertificadora == null) {
			unidadeCertificadora = "";
		}
		return unidadeCertificadora;
	}

	public void setUnidadeCertificadora(String unidadeCertificadora) {
		this.unidadeCertificadora = unidadeCertificadora;
	}

	public String getCnpjUnidadeCertificadora() {
		if (cnpjUnidadeCertificadora == null) {
			cnpjUnidadeCertificadora = "";
		}
		return cnpjUnidadeCertificadora;
	}

	public void setCnpjUnidadeCertificadora(String cnpjUnidadeCertificadora) {
		this.cnpjUnidadeCertificadora = cnpjUnidadeCertificadora;
	}

	public Integer getCodigoIESUnidadeCertificadora() {
		if (codigoIESUnidadeCertificadora == null) {
			codigoIESUnidadeCertificadora = 0;
		}
		return codigoIESUnidadeCertificadora;
	}

	public void setCodigoIESUnidadeCertificadora(Integer codigoIESUnidadeCertificadora) {
		this.codigoIESUnidadeCertificadora = codigoIESUnidadeCertificadora;
	}	
	
	
	public ConfiguracaoGEDVO getConfiguracaoGEDVO() {
		if (configuracaoGEDVO == null) {
			configuracaoGEDVO = new ConfiguracaoGEDVO();
		}
		return configuracaoGEDVO;
	}
	
	public void setConfiguracaoGEDVO(ConfiguracaoGEDVO configuracaoGEDVO) {
		this.configuracaoGEDVO = configuracaoGEDVO;
	}
	
	public ConfiguracaoMobileVO getConfiguracaoMobileVO() {
		if (configuracaoMobileVO == null) {
			configuracaoMobileVO = new ConfiguracaoMobileVO();
		}
		return configuracaoMobileVO;
	}

	public void setConfiguracaoMobileVO(ConfiguracaoMobileVO configuracaoMobileVO) {
		this.configuracaoMobileVO = configuracaoMobileVO;
	}

	public String getLocalizacaoDiferenciadaEscola() {
		if (localizacaoDiferenciadaEscola == null) {
			localizacaoDiferenciadaEscola = "ND";
		}
		return localizacaoDiferenciadaEscola;
	}

	public void setLocalizacaoDiferenciadaEscola(String localizacaoDiferenciadaEscola) {
		this.localizacaoDiferenciadaEscola = localizacaoDiferenciadaEscola;
}

	public String getUnidadeVinculadaEscolaEducacaoBasica() {
		if(unidadeVinculadaEscolaEducacaoBasica == null) {
			unidadeVinculadaEscolaEducacaoBasica = "";
		}
		return unidadeVinculadaEscolaEducacaoBasica;
	}

	public void setUnidadeVinculadaEscolaEducacaoBasica(
			String unidadeVinculadaEscolaEducacaoBasica) {
		this.unidadeVinculadaEscolaEducacaoBasica = unidadeVinculadaEscolaEducacaoBasica;
	}

	public Integer getCodigoEscolaSede() {
		return codigoEscolaSede;
	}

	public void setCodigoEscolaSede(Integer codigoEscolaSede) {
		this.codigoEscolaSede = codigoEscolaSede;
	}

	public Boolean getForneceAguaPotavelConsumoHumano() {
		if(forneceAguaPotavelConsumoHumano == null) {
			forneceAguaPotavelConsumoHumano = Boolean.TRUE;
		}
		return forneceAguaPotavelConsumoHumano;
	}

	public void setForneceAguaPotavelConsumoHumano(Boolean forneceAguaPotavelConsumoHumano) {
		this.forneceAguaPotavelConsumoHumano = forneceAguaPotavelConsumoHumano;
	}

	public String getTratamentoLixo() {
		if(tratamentoLixo == null) {
			tratamentoLixo = "";
		}
		return tratamentoLixo;
	}

	public void setTratamentoLixo(String tratamentoLixo) {
		this.tratamentoLixo = tratamentoLixo;
	}

	public Boolean getBanheiroExclusivoFuncionarios() {
		if(banheiroExclusivoFuncionarios == null) {
			banheiroExclusivoFuncionarios = Boolean.FALSE;
		}
		return banheiroExclusivoFuncionarios;
	}

	public void setBanheiroExclusivoFuncionarios(Boolean banheiroExclusivoFuncionarios) {
		this.banheiroExclusivoFuncionarios = banheiroExclusivoFuncionarios;
	}

	public Boolean getPiscina() {
		if(piscina == null) {
			piscina = Boolean.FALSE;
		}
		return piscina;
	}

	public void setPiscina(Boolean piscina) {
		this.piscina = piscina;
	}

	public Boolean getSalaRepousoAluno() {
		if(salaRepousoAluno == null) {
			salaRepousoAluno = Boolean.FALSE;
		}
		return salaRepousoAluno;
	}

	public void setSalaRepousoAluno(Boolean salaRepousoAluno) {
		this.salaRepousoAluno = salaRepousoAluno;
	}

	public Boolean getSalaArtes() {
		if(salaArtes == null) {
			salaArtes = Boolean.FALSE;
		}
		return salaArtes;
	}

	public void setSalaArtes(Boolean salaArtes) {
		this.salaArtes = salaArtes;
	}

	public Boolean getSalaMusica() {
		if(salaMusica == null) {
			salaMusica = Boolean.FALSE;
		}
		return salaMusica;
	}

	public void setSalaMusica(Boolean salaMusica) {
		this.salaMusica = salaMusica;
	}

	public Boolean getSalaDanca() {
		if(salaDanca == null) {
			salaDanca = Boolean.FALSE;
		}
		return salaDanca;
	}

	public void setSalaDanca(Boolean salaDanca) {
		this.salaDanca = salaDanca;
	}

	public Boolean getSalaMultiuso() {
		if(salaMultiuso == null) {
			salaMultiuso = Boolean.FALSE;
		}
		return salaMultiuso;
	}

	public void setSalaMultiuso(Boolean salaMultiuso) {
		this.salaMultiuso = salaMultiuso;
	}

	public Boolean getTerreirao() {
		if(terreirao == null) {
			terreirao = Boolean.FALSE;
		}
		return terreirao;
	}

	public void setTerreirao(Boolean terreirao) {
		this.terreirao = terreirao;
	}

	public Boolean getViveiroAnimais() {
		if(viveiroAnimais == null) {
			viveiroAnimais = Boolean.FALSE;
		}
		return viveiroAnimais;
	}

	public void setViveiroAnimais(Boolean viveiroAnimais) {
		this.viveiroAnimais = viveiroAnimais;
	}

	public Boolean getCorrimaoGuardaCorpos() {
		if(corrimaoGuardaCorpos == null) {
			corrimaoGuardaCorpos = Boolean.FALSE;
		}
		return corrimaoGuardaCorpos;
	}

	public void setCorrimaoGuardaCorpos(Boolean corrimaoGuardaCorpos) {
		this.corrimaoGuardaCorpos = corrimaoGuardaCorpos;
	}

	public Boolean getElevador() {
		if(elevador == null) {
			elevador = Boolean.FALSE;
		}
		return elevador;
	}

	public void setElevador(Boolean elevador) {
		this.elevador = elevador;
	}

	public Boolean getPisosTateis() {
		if(pisosTateis == null) {
			pisosTateis = Boolean.FALSE;
		}
		return pisosTateis;
	}

	public void setPisosTateis(Boolean pisosTateis) {
		this.pisosTateis = pisosTateis;
	}

	public Boolean getPortasVaoLivreMinimoOitentaCentimetros() {
		if(portasVaoLivreMinimoOitentaCentimetros == null) {
			portasVaoLivreMinimoOitentaCentimetros = Boolean.FALSE;
		}
		return portasVaoLivreMinimoOitentaCentimetros;
	}

	public void setPortasVaoLivreMinimoOitentaCentimetros(Boolean portasVaoLivreMinimoOitentaCentimetros) {
		this.portasVaoLivreMinimoOitentaCentimetros = portasVaoLivreMinimoOitentaCentimetros;
	}

	public Boolean getRampas() {
		if(rampas == null) {
			rampas = Boolean.FALSE;
		}
		return rampas;
	}

	public void setRampas(Boolean rampas) {
		this.rampas = rampas;
	}

	public Boolean getSinalizacaoSonora() {
		if(sinalizacaoSonora == null) {
			sinalizacaoSonora = Boolean.FALSE;
		}
		return sinalizacaoSonora;
	}

	public void setSinalizacaoSonora(Boolean sinalizacaoSonora) {
		this.sinalizacaoSonora = sinalizacaoSonora;
	}

	public Boolean getSinalizacaoTatil() {
		if(sinalizacaoTatil == null) {
			sinalizacaoTatil = Boolean.FALSE;
		}
		return sinalizacaoTatil;
	}

	public void setSinalizacaoTatil(Boolean sinalizacaoTatil) {
		this.sinalizacaoTatil = sinalizacaoTatil;
	}

	public Boolean getSinalizacaoVisual() {
		if(sinalizacaoVisual == null) {
			sinalizacaoVisual = Boolean.FALSE;
		}
		return sinalizacaoVisual;
	}

	public void setSinalizacaoVisual(Boolean sinalizacaoVisual) {
		this.sinalizacaoVisual = sinalizacaoVisual;
	}

	public Boolean getNenhumRecursoAcessibilidade() {
		if(nenhumRecursoAcessibilidade == null) {
			nenhumRecursoAcessibilidade = Boolean.FALSE;
		}
		return nenhumRecursoAcessibilidade;
	}

	public void setNenhumRecursoAcessibilidade(Boolean nenhumRecursoAcessibilidade) {
		this.nenhumRecursoAcessibilidade = nenhumRecursoAcessibilidade;
	}

	public Integer getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar() {
		return numeroSalasAulaUtilizadasEscolaDentroPredioEscolar;
	}

	public void setNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar(
			Integer numeroSalasAulaUtilizadasEscolaDentroPredioEscolar) {
		this.numeroSalasAulaUtilizadasEscolaDentroPredioEscolar = numeroSalasAulaUtilizadasEscolaDentroPredioEscolar;
	}

	public Boolean getAntenaParabolica() {
		if(antenaParabolica == null) {
			antenaParabolica = Boolean.FALSE;
		}
		return antenaParabolica;
	}

	public void setAntenaParabolica(Boolean antenaParabolica) {
		this.antenaParabolica = antenaParabolica;
	}

	public Boolean getComputadores() {
		if(computadores == null) {
			computadores = Boolean.FALSE;
		}
		return computadores;
	}

	public void setComputadores(Boolean computadores) {
		this.computadores = computadores;
	}

	public Boolean getCopiadora() {
		if(copiadora == null) {
			copiadora = Boolean.FALSE;
		}
		return copiadora;
	}

	public void setCopiadora(Boolean copiadora) {
		this.copiadora = copiadora;
	}

	public Boolean getImpressora() {
		if(impressora == null) {
			impressora = Boolean.FALSE;
		}
		return impressora;
	}

	public void setImpressora(Boolean impressora) {
		this.impressora = impressora;
	}

	public Boolean getImpressoraMultifuncional() {
		if(impressoraMultifuncional == null) {
			impressoraMultifuncional = Boolean.FALSE;
		}
		return impressoraMultifuncional;
	}

	public void setImpressoraMultifuncional(Boolean impressoraMultifuncional) {
		this.impressoraMultifuncional = impressoraMultifuncional;
	}

	public Boolean getScanner() {
		if(scanner == null) {
			scanner = Boolean.FALSE;
		}
		return scanner;
	}

	public void setScanner(Boolean scanner) {
		this.scanner = scanner;
	}

	public Boolean getAcessoInternetUsoAdiministrativo() {
		if(acessoInternetUsoAdiministrativo == null) {
			acessoInternetUsoAdiministrativo = Boolean.FALSE;
		}
		return acessoInternetUsoAdiministrativo;
	}

	public void setAcessoInternetUsoAdiministrativo(Boolean acessoInternetUsoAdiministrativo) {
		this.acessoInternetUsoAdiministrativo = acessoInternetUsoAdiministrativo;
	}

	public Boolean getAcessoInternetUsoProcessoEnsinoAprendizagem() {
		if(acessoInternetUsoProcessoEnsinoAprendizagem == null) {
			acessoInternetUsoProcessoEnsinoAprendizagem = Boolean.FALSE;
		}
		return acessoInternetUsoProcessoEnsinoAprendizagem;
	}

	public void setAcessoInternetUsoProcessoEnsinoAprendizagem(Boolean acessoInternetUsoProcessoEnsinoAprendizagem) {
		this.acessoInternetUsoProcessoEnsinoAprendizagem = acessoInternetUsoProcessoEnsinoAprendizagem;
	}

	public Boolean getAcessoInternetUsoAlunos() {
		if(acessoInternetUsoAlunos == null) {
			acessoInternetUsoAlunos = Boolean.FALSE;
		}
		return acessoInternetUsoAlunos;
	}

	public void setAcessoInternetUsoAlunos(Boolean acessoInternetUsoAlunos) {
		this.acessoInternetUsoAlunos = acessoInternetUsoAlunos;
	}

	public Boolean getAcessoInternetComunidade() {
		if(acessoInternetComunidade == null) {
			acessoInternetComunidade = Boolean.FALSE;
		}
		return acessoInternetComunidade;
	}

	public void setAcessoInternetComunidade(Boolean acessoInternetComunidade) {
		this.acessoInternetComunidade = acessoInternetComunidade;
	}

	public Boolean getNaoPossuiAcessoInternet() {
		if(naoPossuiAcessoInternet == null) {
			naoPossuiAcessoInternet = Boolean.FALSE;
		}
		return naoPossuiAcessoInternet;
	}

	public void setNaoPossuiAcessoInternet(Boolean naoPossuiAcessoInternet) {
		this.naoPossuiAcessoInternet = naoPossuiAcessoInternet;
	}

	public Boolean getAcessoInternetComputadoresEscola() {
		if(acessoInternetComputadoresEscola == null) {
			acessoInternetComputadoresEscola = Boolean.FALSE;
		}
		return acessoInternetComputadoresEscola;
	}

	public void setAcessoInternetComputadoresEscola(Boolean acessoInternetComputadoresEscola) {
		this.acessoInternetComputadoresEscola = acessoInternetComputadoresEscola;
	}

	public Boolean getAcessoInternetDispositivosPessoais() {
		if(acessoInternetDispositivosPessoais == null) {
			acessoInternetDispositivosPessoais = Boolean.FALSE;
		}
		return acessoInternetDispositivosPessoais;
	}

	public void setAcessoInternetDispositivosPessoais(Boolean acessoInternetDispositivosPessoais) {
		this.acessoInternetDispositivosPessoais = acessoInternetDispositivosPessoais;
	}

	public Boolean getRedeLocalCabo() {
		if(redeLocalCabo == null) {
			redeLocalCabo = Boolean.FALSE;
		}
		return redeLocalCabo;
	}

	public void setRedeLocalCabo(Boolean redeLocalCabo) {
		this.redeLocalCabo = redeLocalCabo;
	}

	public Boolean getRedeLocalWireless() {
		if(redeLocalWireless == null) {
			redeLocalWireless = Boolean.FALSE;
		}
		return redeLocalWireless;
	}

	public void setRedeLocalWireless(Boolean redeLocalWireless) {
		this.redeLocalWireless = redeLocalWireless;
	}

	public Boolean getNaoExisteRedeLocal() {
		if(naoExisteRedeLocal == null) {
			naoExisteRedeLocal = Boolean.FALSE;
		}
		return naoExisteRedeLocal;
	}

	public void setNaoExisteRedeLocal(Boolean naoExisteRedeLocal) {
		this.naoExisteRedeLocal = naoExisteRedeLocal;
	}

	public Boolean getAlimentacaoEscolarAlunos() {
		if(alimentacaoEscolarAlunos == null) {
			alimentacaoEscolarAlunos = Boolean.FALSE;
		}
		return alimentacaoEscolarAlunos;
	}

	public void setAlimentacaoEscolarAlunos(Boolean alimentacaoEscolarAlunos) {
		this.alimentacaoEscolarAlunos = alimentacaoEscolarAlunos;
	}

	public Boolean getEducacaoEscolarIndigena() {
		if(educacaoEscolarIndigena == null) {
			educacaoEscolarIndigena = Boolean.FALSE;
		}
		return educacaoEscolarIndigena;
	}

	public void setEducacaoEscolarIndigena(Boolean educacaoEscolarIndigena) {
		this.educacaoEscolarIndigena = educacaoEscolarIndigena;
	}

	public Boolean getLinguaIndigena() {
		if(linguaIndigena == null) {
			linguaIndigena = Boolean.FALSE;
		}
		return linguaIndigena;
	}

	public void setLinguaIndigena(Boolean linguaIndigena) {
		this.linguaIndigena = linguaIndigena;
	}

	public Boolean getLinguaPortuguesa() {
		if(linguaPortuguesa == null) {
			linguaPortuguesa = Boolean.FALSE;
		}
		return linguaPortuguesa;
	}

	public void setLinguaPortuguesa(Boolean linguaPortuguesa) {
		this.linguaPortuguesa = linguaPortuguesa;
	}

	public Integer getCodigoLinguaIndigena1() {
		return codigoLinguaIndigena1;
	}

	public void setCodigoLinguaIndigena1(Integer codigoLinguaIndigena1) {
		this.codigoLinguaIndigena1 = codigoLinguaIndigena1;
	}

	public Integer getCodigoLinguaIndigena2() {
		return codigoLinguaIndigena2;
	}

	public void setCodigoLinguaIndigena2(Integer codigoLinguaIndigena2) {
		this.codigoLinguaIndigena2 = codigoLinguaIndigena2;
	}

	public Integer getCodigoLinguaIndigena3() {
		return codigoLinguaIndigena3;
	}

	public void setCodigoLinguaIndigena3(Integer codigoLinguaIndigena3) {
		this.codigoLinguaIndigena3 = codigoLinguaIndigena3;
	}

	

	public String getTipoChancela() {
		if(tipoChancela == null) {
			tipoChancela = "";
		}
		return tipoChancela;
	}

	public void setTipoChancela(String tipoChancela) {
		this.tipoChancela = tipoChancela;
	}

	public Double getPorcentagemChancela() {
		 if (porcentagemChancela == null) {
	            porcentagemChancela = 0.0;
	       }
		return porcentagemChancela;
	}

	public void setPorcentagemChancela(Double porcentagemChancela) {
		this.porcentagemChancela = porcentagemChancela;
	}

	public Double getValorFixoChancela() {
		 if (valorFixoChancela == null) {
	            valorFixoChancela = 0.0;
	       }
		return valorFixoChancela;
	}

	public void setValorFixoChancela(Double valorFixoChancela) {
		this.valorFixoChancela = valorFixoChancela;
	}

	public Boolean getValorPorAluno() {
		return valorPorAluno;
	}

	public void setValorPorAluno(Boolean valorPorAluno) {
		this.valorPorAluno = valorPorAluno;
	}

	public FuncionarioVO getResponsavelNotificacaoAlteracaoCronogramaAula() {
		if(responsavelNotificacaoAlteracaoCronogramaAula == null) {
			responsavelNotificacaoAlteracaoCronogramaAula = new FuncionarioVO();
		}
		return responsavelNotificacaoAlteracaoCronogramaAula;
	}

	public void setResponsavelNotificacaoAlteracaoCronogramaAula(FuncionarioVO responsavelNotificacaoAlteracaoCronogramaAula) {
		this.responsavelNotificacaoAlteracaoCronogramaAula = responsavelNotificacaoAlteracaoCronogramaAula;
	}

	public FuncionarioVO getOperadorResponsavel() {
		if(operadorResponsavel == null ) {
			operadorResponsavel = new FuncionarioVO();
		}
		return operadorResponsavel;
	}

	public void setOperadorResponsavel(FuncionarioVO operadorResponsavel) {
		this.operadorResponsavel = operadorResponsavel;
	}
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}
	
	
	public TipoAutorizacaoCursoEnum getTipoAutorizacaoEnum() {
		return tipoAutorizacaoEnum;
	}
	
	public void setTipoAutorizacaoEnum(TipoAutorizacaoCursoEnum tipoAutorizacaoEnum) {
		this.tipoAutorizacaoEnum = tipoAutorizacaoEnum;
	}

	public Date getDataCredenciamento() {
		return dataCredenciamento;
	}

	public void setDataCredenciamento(Date dataCredenciamento) {
		this.dataCredenciamento = dataCredenciamento;
	}

	public String getVeiculoPublicacaoCredenciamento() {
		if (veiculoPublicacaoCredenciamento == null) {
			veiculoPublicacaoCredenciamento = Constantes.EMPTY;
		}
		return veiculoPublicacaoCredenciamento;
	}

	public void setVeiculoPublicacaoCredenciamento(String veiculoPublicacaoCredenciamento) {
		this.veiculoPublicacaoCredenciamento = veiculoPublicacaoCredenciamento;
	}

	public Integer getSecaoPublicacaoCredenciamento() {
		if (secaoPublicacaoCredenciamento == null) {
			secaoPublicacaoCredenciamento = 0;
		}
		return secaoPublicacaoCredenciamento;
	}

	public void setSecaoPublicacaoCredenciamento(Integer secaoPublicacaoCredenciamento) {
		this.secaoPublicacaoCredenciamento = secaoPublicacaoCredenciamento;
	}

	public Integer getPaginaPublicacaoCredenciamento() {
		if (paginaPublicacaoCredenciamento == null) {
			paginaPublicacaoCredenciamento = 0;
		}
		return paginaPublicacaoCredenciamento;
	}

	public void setPaginaPublicacaoCredenciamento(Integer paginaPublicacaoCredenciamento) {
		this.paginaPublicacaoCredenciamento = paginaPublicacaoCredenciamento;
	}

	public Integer getNumeroDOUCredenciamento() {
		if (numeroDOUCredenciamento == null) {
			numeroDOUCredenciamento = 0;
		}
		return numeroDOUCredenciamento;
	}

	public void setNumeroDOUCredenciamento(Integer numeroDOUCredenciamento) {
		this.numeroDOUCredenciamento = numeroDOUCredenciamento;
	}
	
	public Boolean getInformarDadosRegistradora() {
		if (informarDadosRegistradora == null) {
			informarDadosRegistradora = Boolean.TRUE;
		}
		return informarDadosRegistradora;
	}
	
	public void setInformarDadosRegistradora(Boolean informarDadosRegistradora) {
		this.informarDadosRegistradora = informarDadosRegistradora;
	}
	
	public Boolean getUtilizarEnderecoUnidadeEnsinoRegistradora() {
		if (utilizarEnderecoUnidadeEnsinoRegistradora == null) {
			utilizarEnderecoUnidadeEnsinoRegistradora = Boolean.FALSE;
		}
		return utilizarEnderecoUnidadeEnsinoRegistradora;
	}
	
	public void setUtilizarEnderecoUnidadeEnsinoRegistradora(Boolean utilizarEnderecoUnidadeEnsinoRegistradora) {
		this.utilizarEnderecoUnidadeEnsinoRegistradora = utilizarEnderecoUnidadeEnsinoRegistradora;
	}
	
	public String getCepRegistradora() {
		if (cepRegistradora == null) {
			cepRegistradora = Constantes.EMPTY;
		}
		return cepRegistradora;
	}
	
	public void setCepRegistradora(String cepRegistradora) {
		this.cepRegistradora = cepRegistradora;
	}
	
	public CidadeVO getCidadeRegistradora() {
		if (cidadeRegistradora == null) {
			cidadeRegistradora = new CidadeVO();
		}
		return cidadeRegistradora;
	}
	
	public void setCidadeRegistradora(CidadeVO cidadeRegistradora) {
		this.cidadeRegistradora = cidadeRegistradora;
	}
	
	public String getComplementoRegistradora() {
		if (complementoRegistradora == null) {
			complementoRegistradora = Constantes.EMPTY;
		}
		return complementoRegistradora;
	}
	
	public void setComplementoRegistradora(String complementoRegistradora) {
		this.complementoRegistradora = complementoRegistradora;
	}
	
	public String getBairroRegistradora() {
		if (bairroRegistradora == null) {
			bairroRegistradora = Constantes.EMPTY;
		}
		return bairroRegistradora;
	}
	
	public void setBairroRegistradora(String bairroRegistradora) {
		this.bairroRegistradora = bairroRegistradora;
	}
	
	public String getEnderecoRegistradora() {
		if (enderecoRegistradora == null) {
			enderecoRegistradora = Constantes.EMPTY;
		}
		return enderecoRegistradora;
	}
	
	public void setEnderecoRegistradora(String enderecoRegistradora) {
		this.enderecoRegistradora = enderecoRegistradora;
	}
	
	public String getNumeroRegistradora() {
		if (numeroRegistradora == null) {
			numeroRegistradora = Constantes.EMPTY;
		}
		return numeroRegistradora;
	}
	
	public void setNumeroRegistradora(String numeroRegistradora) {
		this.numeroRegistradora = numeroRegistradora;
	}
	
	public Boolean getUtilizarCredenciamentoUnidadeEnsino() {
		if (utilizarCredenciamentoUnidadeEnsino == null) {
			utilizarCredenciamentoUnidadeEnsino = Boolean.FALSE;
		}
		return utilizarCredenciamentoUnidadeEnsino;
	}
	
	public void setUtilizarCredenciamentoUnidadeEnsino(Boolean utilizarCredenciamentoUnidadeEnsino) {
		this.utilizarCredenciamentoUnidadeEnsino = utilizarCredenciamentoUnidadeEnsino;
	}

	public String getNumeroCredenciamentoRegistradora() {
		if (numeroCredenciamentoRegistradora == null) {
			numeroCredenciamentoRegistradora = Constantes.EMPTY;
		}
		return numeroCredenciamentoRegistradora;
	}

	public void setNumeroCredenciamentoRegistradora(String numeroCredenciamentoRegistradora) {
		this.numeroCredenciamentoRegistradora = numeroCredenciamentoRegistradora;
	}

	public Date getDataCredenciamentoRegistradora() {
		return dataCredenciamentoRegistradora;
	}

	public void setDataCredenciamentoRegistradora(Date dataCredenciamentoRegistradora) {
		this.dataCredenciamentoRegistradora = dataCredenciamentoRegistradora;
	}

	public Date getDataPublicacaoDORegistradora() {
		return dataPublicacaoDORegistradora;
	}

	public void setDataPublicacaoDORegistradora(Date dataPublicacaoDORegistradora) {
		this.dataPublicacaoDORegistradora = dataPublicacaoDORegistradora;
	}

	public String getVeiculoPublicacaoCredenciamentoRegistradora() {
		if (veiculoPublicacaoCredenciamentoRegistradora == null) {
			veiculoPublicacaoCredenciamentoRegistradora = Constantes.EMPTY;
		}
		return veiculoPublicacaoCredenciamentoRegistradora;
	}

	public void setVeiculoPublicacaoCredenciamentoRegistradora(String veiculoPublicacaoCredenciamentoRegistradora) {
		this.veiculoPublicacaoCredenciamentoRegistradora = veiculoPublicacaoCredenciamentoRegistradora;
	}

	public Integer getSecaoPublicacaoCredenciamentoRegistradora() {
		if (secaoPublicacaoCredenciamentoRegistradora == null) {
			secaoPublicacaoCredenciamentoRegistradora = 0;
		}
		return secaoPublicacaoCredenciamentoRegistradora;
	}

	public void setSecaoPublicacaoCredenciamentoRegistradora(Integer secaoPublicacaoCredenciamentoRegistradora) {
		this.secaoPublicacaoCredenciamentoRegistradora = secaoPublicacaoCredenciamentoRegistradora;
	}

	public Integer getPaginaPublicacaoCredenciamentoRegistradora() {
		if (paginaPublicacaoCredenciamentoRegistradora == null) {
			paginaPublicacaoCredenciamentoRegistradora = 0;
		}
		return paginaPublicacaoCredenciamentoRegistradora;
	}

	public void setPaginaPublicacaoCredenciamentoRegistradora(Integer paginaPublicacaoCredenciamentoRegistradora) {
		this.paginaPublicacaoCredenciamentoRegistradora = paginaPublicacaoCredenciamentoRegistradora;
	}

	public Integer getNumeroPublicacaoCredenciamentoRegistradora() {
		if (numeroPublicacaoCredenciamentoRegistradora == null) {
			numeroPublicacaoCredenciamentoRegistradora = 0;
		}
		return numeroPublicacaoCredenciamentoRegistradora;
	}

	public void setNumeroPublicacaoCredenciamentoRegistradora(Integer numeroPublicacaoCredenciamentoRegistradora) {
		this.numeroPublicacaoCredenciamentoRegistradora = numeroPublicacaoCredenciamentoRegistradora;
	}

	public String getMantenedoraRegistradora() {
		if (mantenedoraRegistradora == null) {
			mantenedoraRegistradora = Constantes.EMPTY;
		}
		return mantenedoraRegistradora;
	}

	public void setMantenedoraRegistradora(String mantenedoraRegistradora) {
		this.mantenedoraRegistradora = mantenedoraRegistradora;
	}

	public String getCnpjMantenedoraRegistradora() {
		if (cnpjMantenedoraRegistradora == null) {
			cnpjMantenedoraRegistradora = Constantes.EMPTY;
		}
		return cnpjMantenedoraRegistradora;
	}

	public void setCnpjMantenedoraRegistradora(String cnpjMantenedoraRegistradora) {
		this.cnpjMantenedoraRegistradora = cnpjMantenedoraRegistradora;
	}

	public String getCepMantenedoraRegistradora() {
		if (cepMantenedoraRegistradora == null) {
			cepMantenedoraRegistradora = Constantes.EMPTY;
		}
		return cepMantenedoraRegistradora;
	}

	public void setCepMantenedoraRegistradora(String cepMantenedoraRegistradora) {
		this.cepMantenedoraRegistradora = cepMantenedoraRegistradora;
	}

	public String getEnderecoMantenedoraRegistradora() {
		if (enderecoMantenedoraRegistradora == null) {
			enderecoMantenedoraRegistradora = Constantes.EMPTY;
		}
		return enderecoMantenedoraRegistradora;
	}

	public void setEnderecoMantenedoraRegistradora(String enderecoMantenedoraRegistradora) {
		this.enderecoMantenedoraRegistradora = enderecoMantenedoraRegistradora;
	}

	public String getNumeroMantenedoraRegistradora() {
		if (numeroMantenedoraRegistradora == null) {
			numeroMantenedoraRegistradora = Constantes.EMPTY;
		}
		return numeroMantenedoraRegistradora;
	}

	public void setNumeroMantenedoraRegistradora(String numeroMantenedoraRegistradora) {
		this.numeroMantenedoraRegistradora = numeroMantenedoraRegistradora;
	}

	public CidadeVO getCidadeMantenedoraRegistradora() {
		if (cidadeMantenedoraRegistradora == null) {
			cidadeMantenedoraRegistradora = new CidadeVO();
		}
		return cidadeMantenedoraRegistradora;
	}

	public void setCidadeMantenedoraRegistradora(CidadeVO cidadeMantenedoraRegistradora) {
		this.cidadeMantenedoraRegistradora = cidadeMantenedoraRegistradora;
	}

	public String getComplementoMantenedoraRegistradora() {
		if (complementoMantenedoraRegistradora == null) {
			complementoMantenedoraRegistradora = Constantes.EMPTY;
		}
		return complementoMantenedoraRegistradora;
	}

	public void setComplementoMantenedoraRegistradora(String complementoMantenedoraRegistradora) {
		this.complementoMantenedoraRegistradora = complementoMantenedoraRegistradora;
	}

	public String getBairroMantenedoraRegistradora() {
		if (bairroMantenedoraRegistradora == null) {
			bairroMantenedoraRegistradora = Constantes.EMPTY;
		}
		return bairroMantenedoraRegistradora;
	}

	public void setBairroMantenedoraRegistradora(String bairroMantenedoraRegistradora) {
		this.bairroMantenedoraRegistradora = bairroMantenedoraRegistradora;
	}
	
	public Boolean getUtilizarMantenedoraUnidadeEnsino() {
		if (utilizarMantenedoraUnidadeEnsino == null) {
			utilizarMantenedoraUnidadeEnsino = Boolean.FALSE;
		}
		return utilizarMantenedoraUnidadeEnsino;
	}
	
	public void setUtilizarMantenedoraUnidadeEnsino(Boolean utilizarMantenedoraUnidadeEnsino) {
		this.utilizarMantenedoraUnidadeEnsino = utilizarMantenedoraUnidadeEnsino;
	}
	
	public Boolean getUtilizarEnderecoUnidadeEnsinoMantenedora() {
		if (utilizarEnderecoUnidadeEnsinoMantenedora == null) {
			utilizarEnderecoUnidadeEnsinoMantenedora = Boolean.FALSE;
		}
		return utilizarEnderecoUnidadeEnsinoMantenedora;
	}
	
	public void setUtilizarEnderecoUnidadeEnsinoMantenedora(Boolean utilizarEnderecoUnidadeEnsinoMantenedora) {
		this.utilizarEnderecoUnidadeEnsinoMantenedora = utilizarEnderecoUnidadeEnsinoMantenedora;
	}

	public String getCepMantenedora() {
		if (cepMantenedora == null) {
			cepMantenedora = Constantes.EMPTY;
		}
		return cepMantenedora;
	}

	public void setCepMantenedora(String cepMantenedora) {
		this.cepMantenedora = cepMantenedora;
	}

	public String getEnderecoMantenedora() {
		if (enderecoMantenedora == null) {
			enderecoMantenedora = Constantes.EMPTY;
		}
		return enderecoMantenedora;
	}

	public void setEnderecoMantenedora(String enderecoMantenedora) {
		this.enderecoMantenedora = enderecoMantenedora;
	}

	public String getNumeroMantenedora() {
		if (numeroMantenedora == null) {
			numeroMantenedora = Constantes.EMPTY;
		}
		return numeroMantenedora;
	}

	public void setNumeroMantenedora(String numeroMantenedora) {
		this.numeroMantenedora = numeroMantenedora;
	}

	public CidadeVO getCidadeMantenedora() {
		if (cidadeMantenedora == null) {
			cidadeMantenedora = new CidadeVO();
		}
		return cidadeMantenedora;
	}

	public void setCidadeMantenedora(CidadeVO cidadeMantenedora) {
		this.cidadeMantenedora = cidadeMantenedora;
	}

	public String getComplementoMantenedora() {
		if (complementoMantenedora == null) {
			complementoMantenedora = Constantes.EMPTY;
		}
		return complementoMantenedora;
	}

	public void setComplementoMantenedora(String complementoMantenedora) {
		this.complementoMantenedora = complementoMantenedora;
	}

	public String getBairroMantenedora() {
		if (bairroMantenedora == null) {
			bairroMantenedora = Constantes.EMPTY;
		}
		return bairroMantenedora;
	}

	public void setBairroMantenedora(String bairroMantenedora) {
		this.bairroMantenedora = bairroMantenedora;
	}

	public String getNumeroRecredenciamento() {
		if (numeroRecredenciamento == null) {
			numeroRecredenciamento = Constantes.EMPTY;
		}
		return numeroRecredenciamento;
	}

	public void setNumeroRecredenciamento(String numeroRecredenciamento) {
		this.numeroRecredenciamento = numeroRecredenciamento;
	}

	public Date getDataRecredenciamento() {
		return dataRecredenciamento;
	}

	public void setDataRecredenciamento(Date dataRecredenciamento) {
		this.dataRecredenciamento = dataRecredenciamento;
	}

	public Date getDataPublicacaoRecredenciamento() {
		return dataPublicacaoRecredenciamento;
	}

	public void setDataPublicacaoRecredenciamento(Date dataPublicacaoRecredenciamento) {
		this.dataPublicacaoRecredenciamento = dataPublicacaoRecredenciamento;
	}

	public String getVeiculoPublicacaoRecredenciamento() {
		if (veiculoPublicacaoRecredenciamento == null) {
			veiculoPublicacaoRecredenciamento = Constantes.EMPTY;
		}
		return veiculoPublicacaoRecredenciamento;
	}

	public void setVeiculoPublicacaoRecredenciamento(String veiculoPublicacaoRecredenciamento) {
		this.veiculoPublicacaoRecredenciamento = veiculoPublicacaoRecredenciamento;
	}

	public Integer getSecaoPublicacaoRecredenciamento() {
		if (secaoPublicacaoRecredenciamento == null) {
			secaoPublicacaoRecredenciamento = 0;
		}
		return secaoPublicacaoRecredenciamento;
	}

	public void setSecaoPublicacaoRecredenciamento(Integer secaoPublicacaoRecredenciamento) {
		this.secaoPublicacaoRecredenciamento = secaoPublicacaoRecredenciamento;
	}

	public Integer getPaginaPublicacaoRecredenciamento() {
		if (paginaPublicacaoRecredenciamento == null) {
			paginaPublicacaoRecredenciamento = 0;
		}
		return paginaPublicacaoRecredenciamento;
	}

	public void setPaginaPublicacaoRecredenciamento(Integer paginaPublicacaoRecredenciamento) {
		this.paginaPublicacaoRecredenciamento = paginaPublicacaoRecredenciamento;
	}

	public Integer getNumeroDOURecredenciamento() {
		if (numeroDOURecredenciamento == null) {
			numeroDOURecredenciamento = 0;
		}
		return numeroDOURecredenciamento;
	}

	public void setNumeroDOURecredenciamento(Integer numeroDOURecredenciamento) {
		this.numeroDOURecredenciamento = numeroDOURecredenciamento;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoRecredenciamento() {
		return tipoAutorizacaoRecredenciamento;
	}

	public void setTipoAutorizacaoRecredenciamento(TipoAutorizacaoCursoEnum tipoAutorizacaoRecredenciamento) {
		this.tipoAutorizacaoRecredenciamento = tipoAutorizacaoRecredenciamento;
	}
	
	public TipoAutorizacaoCursoEnum getTipoAutorizacaoCredenciamentoRegistradora() {
		return tipoAutorizacaoCredenciamentoRegistradora;
	}
	
	public void setTipoAutorizacaoCredenciamentoRegistradora(TipoAutorizacaoCursoEnum tipoAutorizacaoCredenciamentoRegistradora) {
		this.tipoAutorizacaoCredenciamentoRegistradora = tipoAutorizacaoCredenciamentoRegistradora;
	}
	
	public String getNumeroRenovacaoRecredenciamento() {
		if (numeroRenovacaoRecredenciamento == null) {
			numeroRenovacaoRecredenciamento = Constantes.EMPTY;
		}
		return numeroRenovacaoRecredenciamento;
	}

	public void setNumeroRenovacaoRecredenciamento(String numeroRenovacaoRecredenciamento) {
		this.numeroRenovacaoRecredenciamento = numeroRenovacaoRecredenciamento;
	}

	public Date getDataRenovacaoRecredenciamento() {
		return dataRenovacaoRecredenciamento;
	}

	public void setDataRenovacaoRecredenciamento(Date dataRenovacaoRecredenciamento) {
		this.dataRenovacaoRecredenciamento = dataRenovacaoRecredenciamento;
	}

	public Date getDataPublicacaoRenovacaoRecredenciamento() {
		return dataPublicacaoRenovacaoRecredenciamento;
	}

	public void setDataPublicacaoRenovacaoRecredenciamento(Date dataPublicacaoRenovacaoRecredenciamento) {
		this.dataPublicacaoRenovacaoRecredenciamento = dataPublicacaoRenovacaoRecredenciamento;
	}

	public String getVeiculoPublicacaoRenovacaoRecredenciamento() {
		if (veiculoPublicacaoRenovacaoRecredenciamento == null) {
			veiculoPublicacaoRenovacaoRecredenciamento = Constantes.EMPTY;
		}
		return veiculoPublicacaoRenovacaoRecredenciamento;
	}

	public void setVeiculoPublicacaoRenovacaoRecredenciamento(String veiculoPublicacaoRenovacaoRecredenciamento) {
		this.veiculoPublicacaoRenovacaoRecredenciamento = veiculoPublicacaoRenovacaoRecredenciamento;
	}

	public Integer getSecaoPublicacaoRenovacaoRecredenciamento() {
		if (secaoPublicacaoRenovacaoRecredenciamento == null) {
			secaoPublicacaoRenovacaoRecredenciamento = 0;
		}
		return secaoPublicacaoRenovacaoRecredenciamento;
	}

	public void setSecaoPublicacaoRenovacaoRecredenciamento(Integer secaoPublicacaoRenovacaoRecredenciamento) {
		this.secaoPublicacaoRenovacaoRecredenciamento = secaoPublicacaoRenovacaoRecredenciamento;
	}

	public Integer getPaginaPublicacaoRenovacaoRecredenciamento() {
		if (paginaPublicacaoRenovacaoRecredenciamento == null) {
			paginaPublicacaoRenovacaoRecredenciamento = 0;
		}
		return paginaPublicacaoRenovacaoRecredenciamento;
	}

	public void setPaginaPublicacaoRenovacaoRecredenciamento(Integer paginaPublicacaoRenovacaoRecredenciamento) {
		this.paginaPublicacaoRenovacaoRecredenciamento = paginaPublicacaoRenovacaoRecredenciamento;
	}

	public Integer getNumeroDOURenovacaoRecredenciamento() {
		if (numeroDOURenovacaoRecredenciamento == null) {
			numeroDOURenovacaoRecredenciamento = 0;
		}
		return numeroDOURenovacaoRecredenciamento;
	}

	public void setNumeroDOURenovacaoRecredenciamento(Integer numeroDOURenovacaoRecredenciamento) {
		this.numeroDOURenovacaoRecredenciamento = numeroDOURenovacaoRecredenciamento;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoRenovacaoRecredenciamento() {
		return tipoAutorizacaoRenovacaoRecredenciamento;
	}

	public void setTipoAutorizacaoRenovacaoRecredenciamento(TipoAutorizacaoCursoEnum tipoAutorizacaoRenovacaoRecredenciamento) {
		this.tipoAutorizacaoRenovacaoRecredenciamento = tipoAutorizacaoRenovacaoRecredenciamento;
	}
	
	public static void validarDadosCredenciamento(UnidadeEnsinoVO obj) {
		if (obj.getCredenciamentoEmTramitacao()) {
			obj.setNumeroCredenciamento(null);
			obj.setCredenciamento(null);
			obj.setDataCredenciamento(null);
			obj.setDataPublicacaoDO(null);
			obj.setCredenciamentoPortaria(null);
			obj.setVeiculoPublicacaoCredenciamento(null);
			obj.setSecaoPublicacaoCredenciamento(null);
			obj.setPaginaPublicacaoCredenciamento(null);
			obj.setNumeroDOUCredenciamento(null);
			obj.setTipoAutorizacaoEnum(null);
		} else {
			obj.setNumeroProcessoCredenciamento(null);
			obj.setTipoProcessoCredenciamento(null);
			obj.setDataCadastroCredenciamento(null);
			obj.setDataProtocoloCredenciamento(null);
		}
		if (obj.getCredenciamentoEadEmTramitacao()) {
			obj.setNumeroCredenciamentoEAD(null);
			obj.setCredenciamentoEAD(null);
			obj.setDataCredenciamentoEAD(null);
			obj.setDataPublicacaoDOEAD(null);
			obj.setCredenciamentoPortariaEAD(null);
			obj.setVeiculoPublicacaoCredenciamentoEAD(null);
			obj.setSecaoPublicacaoCredenciamentoEAD(null);
			obj.setPaginaPublicacaoCredenciamentoEAD(null);
			obj.setNumeroDOUCredenciamentoEAD(null);
			obj.setTipoAutorizacaoEAD(null);
		} else {
			obj.setNumeroProcessoCredenciamentoEad(null);
			obj.setTipoProcessoCredenciamentoEad(null);
			obj.setDataCadastroCredenciamentoEad(null);
			obj.setDataProtocoloCredenciamentoEad(null);
		}
	}
	
	public static void validarDadosRecredenciamento(UnidadeEnsinoVO obj) {
		if (obj.getRecredenciamentoEmTramitacao()) {
			obj.setNumeroRecredenciamento(null);
			obj.setDataRecredenciamento(null);
			obj.setDataPublicacaoRecredenciamento(null);
			obj.setVeiculoPublicacaoRecredenciamento(null);
			obj.setSecaoPublicacaoRecredenciamento(null);
			obj.setPaginaPublicacaoRecredenciamento(null);
			obj.setNumeroDOURecredenciamento(null);
			obj.setTipoAutorizacaoRecredenciamento(null);
		} else {
			obj.setNumeroProcessoRecredenciamento(null);
			obj.setTipoProcessoRecredenciamento(null);
			obj.setDataCadastroRecredenciamento(null);
			obj.setDataProtocoloRecredenciamento(null);
		}
		if (obj.getRecredenciamentoEmTramitacaoEad()) {
			obj.setNumeroRecredenciamentoEAD(null);
			obj.setDataRecredenciamentoEAD(null);
			obj.setDataPublicacaoRecredenciamentoEAD(null);
			obj.setVeiculoPublicacaoRecredenciamentoEAD(null);
			obj.setSecaoPublicacaoRecredenciamentoEAD(null);
			obj.setPaginaPublicacaoRecredenciamentoEAD(null);
			obj.setNumeroDOURecredenciamentoEAD(null);
			obj.setTipoAutorizacaoRecredenciamentoEAD(null);
		} else {
			obj.setNumeroProcessoRecredenciamentoEad(null);
			obj.setTipoProcessoRecredenciamentoEad(null);
			obj.setDataCadastroRecredenciamentoEad(null);
			obj.setDataProtocoloRecredenciamentoEad(null);
		}
	}
	
	public static void validarDadosRenovacaoRecredenciamento(UnidadeEnsinoVO obj) {
		if (obj.getRenovacaoRecredenciamentoEmTramitacao()) {
			obj.setNumeroRenovacaoRecredenciamento(null);
			obj.setDataRenovacaoRecredenciamento(null);
			obj.setDataPublicacaoRenovacaoRecredenciamento(null);
			obj.setVeiculoPublicacaoRenovacaoRecredenciamento(null);
			obj.setSecaoPublicacaoRenovacaoRecredenciamento(null);
			obj.setPaginaPublicacaoRenovacaoRecredenciamento(null);
			obj.setNumeroDOURenovacaoRecredenciamento(null);
			obj.setTipoAutorizacaoRenovacaoRecredenciamento(null);
		} else {
			obj.setNumeroProcessoRenovacaoRecredenciamento(null);
			obj.setTipoProcessoRenovacaoRecredenciamento(null);
			obj.setDataCadastroRenovacaoRecredenciamento(null);
			obj.setDataProtocoloRenovacaoRecredenciamento(null);
		}
		if (obj.getRenovacaoRecredenciamentoEmTramitacaoEad()) {
			obj.setNumeroRenovacaoRecredenciamentoEAD(null);
			obj.setDataRenovacaoRecredenciamentoEAD(null);
			obj.setDataPublicacaoRenovacaoRecredenciamentoEAD(null);
			obj.setVeiculoPublicacaoRenovacaoRecredenciamentoEAD(null);
			obj.setSecaoPublicacaoRenovacaoRecredenciamentoEAD(null);
			obj.setPaginaPublicacaoRenovacaoRecredenciamentoEAD(null);
			obj.setNumeroDOURenovacaoRecredenciamentoEAD(null);
			obj.setTipoAutorizacaoRenovacaoRecredenciamentoEAD(null);
		} else {
			obj.setNumeroProcessoRenovacaoRecredenciamentoEad(null);
			obj.setTipoProcessoRenovacaoRecredenciamentoEad(null);
			obj.setDataCadastroRenovacaoRecredenciamentoEad(null);
			obj.setDataProtocoloRenovacaoRecredenciamentoEad(null);
		}
	}
	
	public static void validarDadosMantenedora(UnidadeEnsinoVO obj) {
		if (obj.getUtilizarEnderecoUnidadeEnsinoMantenedora()) {
			obj.setCepMantenedora(null);
			obj.setEnderecoMantenedora(null);
			obj.setNumeroMantenedora(null);
			obj.setCidadeMantenedora(null);
			obj.setComplementoMantenedora(null);
			obj.setBairroMantenedora(null);
		}
	}
	
	public static void validarDadosRegistradora(UnidadeEnsinoVO obj) {
		if (!obj.getInformarDadosRegistradora()) {
			obj.setUnidadeCertificadora(null);
			obj.setCnpjUnidadeCertificadora(null);
			obj.setCodigoIESUnidadeCertificadora(null);
			obj.setCepRegistradora(null);
			obj.setCidadeRegistradora(null);
			obj.setComplementoRegistradora(null);
			obj.setBairroRegistradora(null);
			obj.setEnderecoRegistradora(null);
			obj.setNumeroRegistradora(null);
			obj.setUtilizarCredenciamentoUnidadeEnsino(Boolean.FALSE);
			obj.setNumeroCredenciamentoRegistradora(null);
			obj.setDataCredenciamentoRegistradora(null);
			obj.setDataPublicacaoDORegistradora(null);
			obj.setVeiculoPublicacaoCredenciamentoRegistradora(null);
			obj.setSecaoPublicacaoCredenciamentoRegistradora(null);
			obj.setPaginaPublicacaoCredenciamentoRegistradora(null);
			obj.setNumeroPublicacaoCredenciamentoRegistradora(null);
			obj.setUtilizarMantenedoraUnidadeEnsino(Boolean.FALSE);
			obj.setMantenedoraRegistradora(null);
			obj.setCnpjMantenedoraRegistradora(null);
			obj.setCepMantenedoraRegistradora(null);
			obj.setEnderecoMantenedoraRegistradora(null);
			obj.setNumeroMantenedoraRegistradora(null);
			obj.setCidadeMantenedoraRegistradora(null);
			obj.setComplementoMantenedoraRegistradora(null);
			obj.setBairroMantenedoraRegistradora(null);
		} else { 
			if (obj.getUtilizarEnderecoUnidadeEnsinoRegistradora()) {
				obj.setCepRegistradora(null);
				obj.setCidadeRegistradora(null);
				obj.setComplementoRegistradora(null);
				obj.setBairroRegistradora(null);
				obj.setEnderecoRegistradora(null);
				obj.setNumeroRegistradora(null);
			}
			if (obj.getUtilizarCredenciamentoUnidadeEnsino()) {
				obj.setNumeroCredenciamentoRegistradora(null);
				obj.setDataCredenciamentoRegistradora(null);
				obj.setDataPublicacaoDORegistradora(null);
				obj.setVeiculoPublicacaoCredenciamentoRegistradora(null);
				obj.setSecaoPublicacaoCredenciamentoRegistradora(null);
				obj.setPaginaPublicacaoCredenciamentoRegistradora(null);
				obj.setNumeroPublicacaoCredenciamentoRegistradora(null);
			}
			if (obj.getUtilizarMantenedoraUnidadeEnsino()) {
				obj.setMantenedoraRegistradora(null);
				obj.setCnpjMantenedoraRegistradora(null);
				obj.setCepMantenedoraRegistradora(null);
				obj.setEnderecoMantenedoraRegistradora(null);
				obj.setNumeroMantenedoraRegistradora(null);
				obj.setCidadeMantenedoraRegistradora(null);
				obj.setComplementoMantenedoraRegistradora(null);
				obj.setBairroMantenedoraRegistradora(null);
			}
		}
	}
	
	public String getNumeroCredenciamentoEAD() {
		if (numeroCredenciamentoEAD == null) {
			numeroCredenciamentoEAD = Constantes.EMPTY;
		}
		return numeroCredenciamentoEAD;
	}

	public void setNumeroCredenciamentoEAD(String numeroCredenciamentoEAD) {
		this.numeroCredenciamentoEAD = numeroCredenciamentoEAD;
	}

	public String getCredenciamentoEAD() {
		if (credenciamentoEAD == null) {
			credenciamentoEAD = Constantes.EMPTY;
		}
		return credenciamentoEAD;
	}

	public void setCredenciamentoEAD(String credenciamentoEAD) {
		this.credenciamentoEAD = credenciamentoEAD;
	}

	public Date getDataCredenciamentoEAD() {
		return dataCredenciamentoEAD;
	}

	public void setDataCredenciamentoEAD(Date dataCredenciamentoEAD) {
		this.dataCredenciamentoEAD = dataCredenciamentoEAD;
	}

	public Date getDataPublicacaoDOEAD() {
		return dataPublicacaoDOEAD;
	}

	public void setDataPublicacaoDOEAD(Date dataPublicacaoDOEAD) {
		this.dataPublicacaoDOEAD = dataPublicacaoDOEAD;
	}

	public String getCredenciamentoPortariaEAD() {
		if (credenciamentoPortariaEAD == null) {
			credenciamentoPortariaEAD = Constantes.EMPTY;
		}
		return credenciamentoPortariaEAD;
	}

	public void setCredenciamentoPortariaEAD(String credenciamentoPortariaEAD) {
		this.credenciamentoPortariaEAD = credenciamentoPortariaEAD;
	}

	public String getVeiculoPublicacaoCredenciamentoEAD() {
		if (veiculoPublicacaoCredenciamentoEAD == null) {
			veiculoPublicacaoCredenciamentoEAD = Constantes.EMPTY;
		}
		return veiculoPublicacaoCredenciamentoEAD;
	}

	public void setVeiculoPublicacaoCredenciamentoEAD(String veiculoPublicacaoCredenciamentoEAD) {
		this.veiculoPublicacaoCredenciamentoEAD = veiculoPublicacaoCredenciamentoEAD;
	}

	public Integer getSecaoPublicacaoCredenciamentoEAD() {
		if (secaoPublicacaoCredenciamentoEAD == null) {
			secaoPublicacaoCredenciamentoEAD = 0;
		}
		return secaoPublicacaoCredenciamentoEAD;
	}

	public void setSecaoPublicacaoCredenciamentoEAD(Integer secaoPublicacaoCredenciamentoEAD) {
		this.secaoPublicacaoCredenciamentoEAD = secaoPublicacaoCredenciamentoEAD;
	}

	public Integer getPaginaPublicacaoCredenciamentoEAD() {
		if (paginaPublicacaoCredenciamentoEAD == null) {
			paginaPublicacaoCredenciamentoEAD = 0;
		}
		return paginaPublicacaoCredenciamentoEAD;
	}

	public void setPaginaPublicacaoCredenciamentoEAD(Integer paginaPublicacaoCredenciamentoEAD) {
		this.paginaPublicacaoCredenciamentoEAD = paginaPublicacaoCredenciamentoEAD;
	}

	public Integer getNumeroDOUCredenciamentoEAD() {
		if (numeroDOUCredenciamentoEAD == null) {
			numeroDOUCredenciamentoEAD = 0;
		}
		return numeroDOUCredenciamentoEAD;
	}

	public void setNumeroDOUCredenciamentoEAD(Integer numeroDOUCredenciamentoEAD) {
		this.numeroDOUCredenciamentoEAD = numeroDOUCredenciamentoEAD;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoEAD() {
		return tipoAutorizacaoEAD;
	}

	public void setTipoAutorizacaoEAD(TipoAutorizacaoCursoEnum tipoAutorizacaoEAD) {
		this.tipoAutorizacaoEAD = tipoAutorizacaoEAD;
	}

	public String getNumeroRecredenciamentoEAD() {
		if (numeroRecredenciamentoEAD == null) {
			numeroRecredenciamentoEAD = Constantes.EMPTY;
		}
		return numeroRecredenciamentoEAD;
	}

	public void setNumeroRecredenciamentoEAD(String numeroRecredenciamentoEAD) {
		this.numeroRecredenciamentoEAD = numeroRecredenciamentoEAD;
	}

	public Date getDataRecredenciamentoEAD() {
		return dataRecredenciamentoEAD;
	}

	public void setDataRecredenciamentoEAD(Date dataRecredenciamentoEAD) {
		this.dataRecredenciamentoEAD = dataRecredenciamentoEAD;
	}

	public Date getDataPublicacaoRecredenciamentoEAD() {
		return dataPublicacaoRecredenciamentoEAD;
	}

	public void setDataPublicacaoRecredenciamentoEAD(Date dataPublicacaoRecredenciamentoEAD) {
		this.dataPublicacaoRecredenciamentoEAD = dataPublicacaoRecredenciamentoEAD;
	}

	public String getVeiculoPublicacaoRecredenciamentoEAD() {
		if (veiculoPublicacaoRecredenciamentoEAD == null) {
			veiculoPublicacaoRecredenciamentoEAD = Constantes.EMPTY;
		}
		return veiculoPublicacaoRecredenciamentoEAD;
	}

	public void setVeiculoPublicacaoRecredenciamentoEAD(String veiculoPublicacaoRecredenciamentoEAD) {
		this.veiculoPublicacaoRecredenciamentoEAD = veiculoPublicacaoRecredenciamentoEAD;
	}

	public Integer getSecaoPublicacaoRecredenciamentoEAD() {
		if (secaoPublicacaoRecredenciamentoEAD == null) {
			secaoPublicacaoRecredenciamentoEAD = 0;
		}
		return secaoPublicacaoRecredenciamentoEAD;
	}

	public void setSecaoPublicacaoRecredenciamentoEAD(Integer secaoPublicacaoRecredenciamentoEAD) {
		this.secaoPublicacaoRecredenciamentoEAD = secaoPublicacaoRecredenciamentoEAD;
	}

	public Integer getPaginaPublicacaoRecredenciamentoEAD() {
		if (paginaPublicacaoRecredenciamentoEAD == null) {
			paginaPublicacaoRecredenciamentoEAD = 0;
		}
		return paginaPublicacaoRecredenciamentoEAD;
	}

	public void setPaginaPublicacaoRecredenciamentoEAD(Integer paginaPublicacaoRecredenciamentoEAD) {
		this.paginaPublicacaoRecredenciamentoEAD = paginaPublicacaoRecredenciamentoEAD;
	}

	public Integer getNumeroDOURecredenciamentoEAD() {
		if (numeroDOURecredenciamentoEAD == null) {
			numeroDOURecredenciamentoEAD = 0;
		}
		return numeroDOURecredenciamentoEAD;
	}

	public void setNumeroDOURecredenciamentoEAD(Integer numeroDOURecredenciamentoEAD) {
		this.numeroDOURecredenciamentoEAD = numeroDOURecredenciamentoEAD;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoRecredenciamentoEAD() {
		return tipoAutorizacaoRecredenciamentoEAD;
	}

	public void setTipoAutorizacaoRecredenciamentoEAD(TipoAutorizacaoCursoEnum tipoAutorizacaoRecredenciamentoEAD) {
		this.tipoAutorizacaoRecredenciamentoEAD = tipoAutorizacaoRecredenciamentoEAD;
	}

	public String getNumeroRenovacaoRecredenciamentoEAD() {
		if (numeroRenovacaoRecredenciamentoEAD == null) {
			numeroRenovacaoRecredenciamentoEAD = Constantes.EMPTY;
		}
		return numeroRenovacaoRecredenciamentoEAD;
	}

	public void setNumeroRenovacaoRecredenciamentoEAD(String numeroRenovacaoRecredenciamentoEAD) {
		this.numeroRenovacaoRecredenciamentoEAD = numeroRenovacaoRecredenciamentoEAD;
	}

	public Date getDataRenovacaoRecredenciamentoEAD() {
		return dataRenovacaoRecredenciamentoEAD;
	}

	public void setDataRenovacaoRecredenciamentoEAD(Date dataRenovacaoRecredenciamentoEAD) {
		this.dataRenovacaoRecredenciamentoEAD = dataRenovacaoRecredenciamentoEAD;
	}

	public Date getDataPublicacaoRenovacaoRecredenciamentoEAD() {
		return dataPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public void setDataPublicacaoRenovacaoRecredenciamentoEAD(Date dataPublicacaoRenovacaoRecredenciamentoEAD) {
		this.dataPublicacaoRenovacaoRecredenciamentoEAD = dataPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public String getVeiculoPublicacaoRenovacaoRecredenciamentoEAD() {
		if (veiculoPublicacaoRenovacaoRecredenciamentoEAD == null) {
			veiculoPublicacaoRenovacaoRecredenciamentoEAD = Constantes.EMPTY;
		}
		return veiculoPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public void setVeiculoPublicacaoRenovacaoRecredenciamentoEAD(String veiculoPublicacaoRenovacaoRecredenciamentoEAD) {
		this.veiculoPublicacaoRenovacaoRecredenciamentoEAD = veiculoPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public Integer getSecaoPublicacaoRenovacaoRecredenciamentoEAD() {
		if (secaoPublicacaoRenovacaoRecredenciamentoEAD == null) {
			secaoPublicacaoRenovacaoRecredenciamentoEAD = 0;
		}
		return secaoPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public void setSecaoPublicacaoRenovacaoRecredenciamentoEAD(Integer secaoPublicacaoRenovacaoRecredenciamentoEAD) {
		this.secaoPublicacaoRenovacaoRecredenciamentoEAD = secaoPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public Integer getPaginaPublicacaoRenovacaoRecredenciamentoEAD() {
		if (paginaPublicacaoRenovacaoRecredenciamentoEAD == null) {
			paginaPublicacaoRenovacaoRecredenciamentoEAD = 0;
		}
		return paginaPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public void setPaginaPublicacaoRenovacaoRecredenciamentoEAD(Integer paginaPublicacaoRenovacaoRecredenciamentoEAD) {
		this.paginaPublicacaoRenovacaoRecredenciamentoEAD = paginaPublicacaoRenovacaoRecredenciamentoEAD;
	}

	public Integer getNumeroDOURenovacaoRecredenciamentoEAD() {
		if (numeroDOURenovacaoRecredenciamentoEAD == null) {
			numeroDOURenovacaoRecredenciamentoEAD = 0;
		}
		return numeroDOURenovacaoRecredenciamentoEAD;
	}

	public void setNumeroDOURenovacaoRecredenciamentoEAD(Integer numeroDOURenovacaoRecredenciamentoEAD) {
		this.numeroDOURenovacaoRecredenciamentoEAD = numeroDOURenovacaoRecredenciamentoEAD;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoRenovacaoRecredenciamentoEAD() {
		return tipoAutorizacaoRenovacaoRecredenciamentoEAD;
	}

	public void setTipoAutorizacaoRenovacaoRecredenciamentoEAD(TipoAutorizacaoCursoEnum tipoAutorizacaoRenovacaoRecredenciamentoEAD) {
		this.tipoAutorizacaoRenovacaoRecredenciamentoEAD = tipoAutorizacaoRenovacaoRecredenciamentoEAD;
	}
	
	public Boolean getCredenciamentoEmTramitacao() {
		if (credenciamentoEmTramitacao == null) {
			credenciamentoEmTramitacao = Boolean.FALSE;
		}
		return credenciamentoEmTramitacao;
	}

	public void setCredenciamentoEmTramitacao(Boolean credenciamentoEmTramitacao) {
		this.credenciamentoEmTramitacao = credenciamentoEmTramitacao;
	}

	public String getNumeroProcessoCredenciamento() {
		if (numeroProcessoCredenciamento == null) {
			numeroProcessoCredenciamento = Constantes.EMPTY;
		}
		return numeroProcessoCredenciamento;
	}

	public void setNumeroProcessoCredenciamento(String numeroProcessoCredenciamento) {
		this.numeroProcessoCredenciamento = numeroProcessoCredenciamento;
	}

	public String getTipoProcessoCredenciamento() {
		if (tipoProcessoCredenciamento == null) {
			tipoProcessoCredenciamento = Constantes.EMPTY;
		}
		return tipoProcessoCredenciamento;
	}

	public void setTipoProcessoCredenciamento(String tipoProcessoCredenciamento) {
		this.tipoProcessoCredenciamento = tipoProcessoCredenciamento;
	}

	public Date getDataCadastroCredenciamento() {
		return dataCadastroCredenciamento;
	}

	public void setDataCadastroCredenciamento(Date dataCadastroCredenciamento) {
		this.dataCadastroCredenciamento = dataCadastroCredenciamento;
	}

	public Date getDataProtocoloCredenciamento() {
		return dataProtocoloCredenciamento;
	}

	public void setDataProtocoloCredenciamento(Date dataProtocoloCredenciamento) {
		this.dataProtocoloCredenciamento = dataProtocoloCredenciamento;
	}

	public Boolean getRecredenciamentoEmTramitacao() {
		if (recredenciamentoEmTramitacao == null) {
			recredenciamentoEmTramitacao = Boolean.FALSE;
		}
		return recredenciamentoEmTramitacao;
	}

	public void setRecredenciamentoEmTramitacao(Boolean recredenciamentoEmTramitacao) {
		this.recredenciamentoEmTramitacao = recredenciamentoEmTramitacao;
	}

	public String getNumeroProcessoRecredenciamento() {
		if (numeroProcessoRecredenciamento == null) {
			numeroProcessoRecredenciamento = Constantes.EMPTY;
		}
		return numeroProcessoRecredenciamento;
	}

	public void setNumeroProcessoRecredenciamento(String numeroProcessoRecredenciamento) {
		this.numeroProcessoRecredenciamento = numeroProcessoRecredenciamento;
	}

	public String getTipoProcessoRecredenciamento() {
		if (tipoProcessoRecredenciamento == null) {
			tipoProcessoRecredenciamento = Constantes.EMPTY;
		}
		return tipoProcessoRecredenciamento;
	}

	public void setTipoProcessoRecredenciamento(String tipoProcessoRecredenciamento) {
		this.tipoProcessoRecredenciamento = tipoProcessoRecredenciamento;
	}

	public Date getDataCadastroRecredenciamento() {
		return dataCadastroRecredenciamento;
	}

	public void setDataCadastroRecredenciamento(Date dataCadastroRecredenciamento) {
		this.dataCadastroRecredenciamento = dataCadastroRecredenciamento;
	}

	public Date getDataProtocoloRecredenciamento() {
		return dataProtocoloRecredenciamento;
	}

	public void setDataProtocoloRecredenciamento(Date dataProtocoloRecredenciamento) {
		this.dataProtocoloRecredenciamento = dataProtocoloRecredenciamento;
	}

	public Boolean getRenovacaoRecredenciamentoEmTramitacao() {
		if (renovacaoRecredenciamentoEmTramitacao == null) {
			renovacaoRecredenciamentoEmTramitacao = Boolean.FALSE;
		}
		return renovacaoRecredenciamentoEmTramitacao;
	}

	public void setRenovacaoRecredenciamentoEmTramitacao(Boolean renovacaoRecredenciamentoEmTramitacao) {
		this.renovacaoRecredenciamentoEmTramitacao = renovacaoRecredenciamentoEmTramitacao;
	}

	public String getNumeroProcessoRenovacaoRecredenciamento() {
		if (numeroProcessoRenovacaoRecredenciamento == null) {
			numeroProcessoRenovacaoRecredenciamento = Constantes.EMPTY;
		}
		return numeroProcessoRenovacaoRecredenciamento;
	}

	public void setNumeroProcessoRenovacaoRecredenciamento(String numeroProcessoRenovacaoRecredenciamento) {
		this.numeroProcessoRenovacaoRecredenciamento = numeroProcessoRenovacaoRecredenciamento;
	}

	public String getTipoProcessoRenovacaoRecredenciamento() {
		if (tipoProcessoRenovacaoRecredenciamento == null) {
			tipoProcessoRenovacaoRecredenciamento = Constantes.EMPTY;
		}
		return tipoProcessoRenovacaoRecredenciamento;
	}

	public void setTipoProcessoRenovacaoRecredenciamento(String tipoProcessoRenovacaoRecredenciamento) {
		this.tipoProcessoRenovacaoRecredenciamento = tipoProcessoRenovacaoRecredenciamento;
	}

	public Date getDataCadastroRenovacaoRecredenciamento() {
		return dataCadastroRenovacaoRecredenciamento;
	}

	public void setDataCadastroRenovacaoRecredenciamento(Date dataCadastroRenovacaoRecredenciamento) {
		this.dataCadastroRenovacaoRecredenciamento = dataCadastroRenovacaoRecredenciamento;
	}

	public Date getDataProtocoloRenovacaoRecredenciamento() {
		return dataProtocoloRenovacaoRecredenciamento;
	}

	public void setDataProtocoloRenovacaoRecredenciamento(Date dataProtocoloRenovacaoRecredenciamento) {
		this.dataProtocoloRenovacaoRecredenciamento = dataProtocoloRenovacaoRecredenciamento;
	}

	public Boolean getCredenciamentoEadEmTramitacao() {
		if (credenciamentoEadEmTramitacao == null) {
			credenciamentoEadEmTramitacao = Boolean.FALSE;
		}
		return credenciamentoEadEmTramitacao;
	}

	public void setCredenciamentoEadEmTramitacao(Boolean credenciamentoEadEmTramitacao) {
		this.credenciamentoEadEmTramitacao = credenciamentoEadEmTramitacao;
	}

	public String getNumeroProcessoCredenciamentoEad() {
		if (numeroProcessoCredenciamentoEad == null) {
			numeroProcessoCredenciamentoEad = Constantes.EMPTY;
		}
		return numeroProcessoCredenciamentoEad;
	}

	public void setNumeroProcessoCredenciamentoEad(String numeroProcessoCredenciamentoEad) {
		this.numeroProcessoCredenciamentoEad = numeroProcessoCredenciamentoEad;
	}

	public String getTipoProcessoCredenciamentoEad() {
		if (tipoProcessoCredenciamentoEad == null) {
			tipoProcessoCredenciamentoEad = Constantes.EMPTY;
		}
		return tipoProcessoCredenciamentoEad;
	}

	public void setTipoProcessoCredenciamentoEad(String tipoProcessoCredenciamentoEad) {
		this.tipoProcessoCredenciamentoEad = tipoProcessoCredenciamentoEad;
	}

	public Date getDataCadastroCredenciamentoEad() {
		return dataCadastroCredenciamentoEad;
	}

	public void setDataCadastroCredenciamentoEad(Date dataCadastroCredenciamentoEad) {
		this.dataCadastroCredenciamentoEad = dataCadastroCredenciamentoEad;
	}

	public Date getDataProtocoloCredenciamentoEad() {
		return dataProtocoloCredenciamentoEad;
	}

	public void setDataProtocoloCredenciamentoEad(Date dataProtocoloCredenciamentoEad) {
		this.dataProtocoloCredenciamentoEad = dataProtocoloCredenciamentoEad;
	}

	public Boolean getRecredenciamentoEmTramitacaoEad() {
		if (recredenciamentoEmTramitacaoEad == null) {
			recredenciamentoEmTramitacaoEad = Boolean.FALSE;
		}
		return recredenciamentoEmTramitacaoEad;
	}

	public void setRecredenciamentoEmTramitacaoEad(Boolean recredenciamentoEmTramitacaoEad) {
		this.recredenciamentoEmTramitacaoEad = recredenciamentoEmTramitacaoEad;
	}

	public String getNumeroProcessoRecredenciamentoEad() {
		if (numeroProcessoRecredenciamentoEad == null) {
			numeroProcessoRecredenciamentoEad = Constantes.EMPTY;
		}
		return numeroProcessoRecredenciamentoEad;
	}

	public void setNumeroProcessoRecredenciamentoEad(String numeroProcessoRecredenciamentoEad) {
		this.numeroProcessoRecredenciamentoEad = numeroProcessoRecredenciamentoEad;
	}

	public String getTipoProcessoRecredenciamentoEad() {
		if (tipoProcessoRecredenciamentoEad == null) {
			tipoProcessoRecredenciamentoEad = Constantes.EMPTY;
		}
		return tipoProcessoRecredenciamentoEad;
	}

	public void setTipoProcessoRecredenciamentoEad(String tipoProcessoRecredenciamentoEad) {
		this.tipoProcessoRecredenciamentoEad = tipoProcessoRecredenciamentoEad;
	}

	public Date getDataCadastroRecredenciamentoEad() {
		return dataCadastroRecredenciamentoEad;
	}

	public void setDataCadastroRecredenciamentoEad(Date dataCadastroRecredenciamentoEad) {
		this.dataCadastroRecredenciamentoEad = dataCadastroRecredenciamentoEad;
	}

	public Date getDataProtocoloRecredenciamentoEad() {
		return dataProtocoloRecredenciamentoEad;
	}

	public void setDataProtocoloRecredenciamentoEad(Date dataProtocoloRecredenciamentoEad) {
		this.dataProtocoloRecredenciamentoEad = dataProtocoloRecredenciamentoEad;
	}

	public Boolean getRenovacaoRecredenciamentoEmTramitacaoEad() {
		if (renovacaoRecredenciamentoEmTramitacaoEad == null) {
			renovacaoRecredenciamentoEmTramitacaoEad = Boolean.FALSE;
		}
		return renovacaoRecredenciamentoEmTramitacaoEad;
	}

	public void setRenovacaoRecredenciamentoEmTramitacaoEad(Boolean renovacaoRecredenciamentoEmTramitacaoEad) {
		this.renovacaoRecredenciamentoEmTramitacaoEad = renovacaoRecredenciamentoEmTramitacaoEad;
	}

	public String getNumeroProcessoRenovacaoRecredenciamentoEad() {
		if (numeroProcessoRenovacaoRecredenciamentoEad == null) {
			numeroProcessoRenovacaoRecredenciamentoEad = Constantes.EMPTY;
		}
		return numeroProcessoRenovacaoRecredenciamentoEad;
	}

	public void setNumeroProcessoRenovacaoRecredenciamentoEad(String numeroProcessoRenovacaoRecredenciamentoEad) {
		this.numeroProcessoRenovacaoRecredenciamentoEad = numeroProcessoRenovacaoRecredenciamentoEad;
	}

	public String getTipoProcessoRenovacaoRecredenciamentoEad() {
		if (tipoProcessoRenovacaoRecredenciamentoEad == null) {
			tipoProcessoRenovacaoRecredenciamentoEad = Constantes.EMPTY;
		}
		return tipoProcessoRenovacaoRecredenciamentoEad;
	}

	public void setTipoProcessoRenovacaoRecredenciamentoEad(String tipoProcessoRenovacaoRecredenciamentoEad) {
		this.tipoProcessoRenovacaoRecredenciamentoEad = tipoProcessoRenovacaoRecredenciamentoEad;
	}

	public Date getDataCadastroRenovacaoRecredenciamentoEad() {
		return dataCadastroRenovacaoRecredenciamentoEad;
	}

	public void setDataCadastroRenovacaoRecredenciamentoEad(Date dataCadastroRenovacaoRecredenciamentoEad) {
		this.dataCadastroRenovacaoRecredenciamentoEad = dataCadastroRenovacaoRecredenciamentoEad;
	}

	public Date getDataProtocoloRenovacaoRecredenciamentoEad() {
		return dataProtocoloRenovacaoRecredenciamentoEad;
	}

	public void setDataProtocoloRenovacaoRecredenciamentoEad(Date dataProtocoloRenovacaoRecredenciamentoEad) {
		this.dataProtocoloRenovacaoRecredenciamentoEad = dataProtocoloRenovacaoRecredenciamentoEad;
	}

	public Boolean getCredenciamentoRegistradoraEmTramitacao() {
		if (credenciamentoRegistradoraEmTramitacao == null) {
			credenciamentoRegistradoraEmTramitacao = Boolean.FALSE;
		}
		return credenciamentoRegistradoraEmTramitacao;
	}

	public void setCredenciamentoRegistradoraEmTramitacao(Boolean credenciamentoRegistradoraEmTramitacao) {
		this.credenciamentoRegistradoraEmTramitacao = credenciamentoRegistradoraEmTramitacao;
	}

	public String getNumeroProcessoCredenciamentoRegistradora() {
		if (numeroProcessoCredenciamentoRegistradora == null) {
			numeroProcessoCredenciamentoRegistradora = Constantes.EMPTY;
		}
		return numeroProcessoCredenciamentoRegistradora;
	}

	public void setNumeroProcessoCredenciamentoRegistradora(String numeroProcessoCredenciamentoRegistradora) {
		this.numeroProcessoCredenciamentoRegistradora = numeroProcessoCredenciamentoRegistradora;
	}

	public String getTipoProcessoCredenciamentoRegistradora() {
		if (tipoProcessoCredenciamentoRegistradora == null) {
			tipoProcessoCredenciamentoRegistradora = Constantes.EMPTY;
		}
		return tipoProcessoCredenciamentoRegistradora;
	}

	public void setTipoProcessoCredenciamentoRegistradora(String tipoProcessoCredenciamentoRegistradora) {
		this.tipoProcessoCredenciamentoRegistradora = tipoProcessoCredenciamentoRegistradora;
	}

	public Date getDataCadastroCredenciamentoRegistradora() {
		return dataCadastroCredenciamentoRegistradora;
	}

	public void setDataCadastroCredenciamentoRegistradora(Date dataCadastroCredenciamentoRegistradora) {
		this.dataCadastroCredenciamentoRegistradora = dataCadastroCredenciamentoRegistradora;
	}

	public Date getDataProtocoloCredenciamentoRegistradora() {
		return dataProtocoloCredenciamentoRegistradora;
	}

	public void setDataProtocoloCredenciamentoRegistradora(Date dataProtocoloCredenciamentoRegistradora) {
		this.dataProtocoloCredenciamentoRegistradora = dataProtocoloCredenciamentoRegistradora;
	}
	
	public String getNumeroCredenciamento() {
		if (numeroCredenciamento == null) {
			numeroCredenciamento = Constantes.EMPTY;
		}
		return numeroCredenciamento;
	}
	
	public void setNumeroCredenciamento(String numeroCredenciamento) {
		this.numeroCredenciamento = numeroCredenciamento;
	}
	public Integer getNumeroVagaOfertada() {
		if(numeroVagaOfertada == null) {
			numeroVagaOfertada = 0;
		}
		return numeroVagaOfertada;
	}
	
	

	public void setNumeroVagaOfertada(Integer numeroVagaOfertada) {
		this.numeroVagaOfertada = numeroVagaOfertada;
	}

}
