package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.faces.model.SelectItem;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.RegraCalculoDisciplinaCompostaEnum;
import negocio.comuns.academico.enumeradores.TipoCalculoCargaHorariaFrequencia;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.TipoUsoConfiguracaoAcademicoEnum;
import negocio.comuns.academico.enumeradores.VariaveisNotaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.enumeradores.PoliticaNotaSubstitutivaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoAcademico. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "configuracaoAcademicoVO")
public class ConfiguracaoAcademicoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private String formulaCalculoMediaFinal;
	/**
	 * Determina o percentual mínimo da carga horária que uma disciplina a ser
	 * aproveitada deverá ter com relação a carga horária da disciplina da
	 * matriz curricular. Por exemplo, se a disciplina do aproveitamento tem
	 * 40horas e disciplinas da matriz curricular do aluno tem 80h, este
	 * percentual está definido para 75%, então sistema bloqueará o
	 * aproveitamento.
	 */
	private Integer percMinimoCargaHorariaDisciplinaParaAproveitamento;

	private Boolean controlarAvancoPeriodoPorCreditoOuCH;
	private String tipoControleAvancoPeriodoPorCreditoOuCH;
	private Integer percCumprirPeriodoAnteriorRenovarProximoPerLetivo;
	private Integer percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo;

	private Boolean controlarInclusaoDisciplinaPorNrMaxCreditoOuCH;
	private String tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;
	private Boolean acumularCreditosOuCHPeriodosAnterioresNaoCumpridos;

	private Boolean permitirInclusaoDiscipDependenciaComChoqueHorario;
	private Integer qtdPermitirInclusaoDiscipDependenciaComChoqueHorario;
	private Boolean permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta;

	private Double nota1;
	private Boolean utilizarNota1;
	private Boolean utilizarNota1PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota1ConceitoVOs;
	private String tituloNota1;
	private String tituloNotaApresentar1;
	private String formulaCalculoNota1;
	private String formulaUsoNota1;
	private Boolean nota1MediaFinal;
	private BimestreEnum bimestreNota1;
	private Double faixaNota1Menor;
	private Double faixaNota1Maior;
	private Double nota2;
	private Boolean utilizarNota2;
	private Boolean utilizarNota2PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota2ConceitoVOs;
	private String tituloNota2;
	private String tituloNotaApresentar2;
	private String formulaCalculoNota2;
	private String formulaUsoNota2;
	private Boolean nota2MediaFinal;
	private BimestreEnum bimestreNota2;
	private Double faixaNota2Menor;
	private Double faixaNota2Maior;
	private Double nota3;
	private Boolean utilizarNota3;
	private Boolean utilizarNota3PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota3ConceitoVOs;
	private String tituloNota3;
	private String tituloNotaApresentar3;
	private String formulaCalculoNota3;
	private String formulaUsoNota3;
	private Boolean nota3MediaFinal;
	private BimestreEnum bimestreNota3;
	private Double faixaNota3Menor;
	private Double faixaNota3Maior;
	private Double nota4;
	private Boolean utilizarNota4;
	private Boolean utilizarNota4PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota4ConceitoVOs;
	private String tituloNota4;
	private String tituloNotaApresentar4;
	private String formulaCalculoNota4;
	private String formulaUsoNota4;
	private Boolean nota4MediaFinal;
	private BimestreEnum bimestreNota4;
	private Double faixaNota4Menor;
	private Double faixaNota4Maior;
	private Double nota5;
	private Boolean utilizarNota5;
	private Boolean utilizarNota5PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota5ConceitoVOs;
	private String tituloNota5;
	private String tituloNotaApresentar5;
	private String formulaCalculoNota5;
	private String formulaUsoNota5;
	private Boolean nota5MediaFinal;
	private BimestreEnum bimestreNota5;
	private Double faixaNota5Menor;
	private Double faixaNota5Maior;
	private Double nota6;
	private Boolean utilizarNota6;
	private Boolean utilizarNota6PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota6ConceitoVOs;
	private String tituloNota6;
	private String tituloNotaApresentar6;
	private String formulaCalculoNota6;
	private String formulaUsoNota6;
	private Boolean nota6MediaFinal;
	private BimestreEnum bimestreNota6;
	private Double faixaNota6Menor;
	private Double faixaNota6Maior;
	private Double nota7;
	private Boolean utilizarNota7;
	private Boolean utilizarNota7PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota7ConceitoVOs;
	private String tituloNota7;
	private String tituloNotaApresentar7;
	private String formulaCalculoNota7;
	private String formulaUsoNota7;
	private Boolean nota7MediaFinal;
	private BimestreEnum bimestreNota7;
	private Double faixaNota7Menor;
	private Double faixaNota7Maior;
	private Double nota8;
	private Boolean utilizarNota8;
	private Boolean utilizarNota8PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota8ConceitoVOs;
	private String tituloNota8;
	private String tituloNotaApresentar8;
	private String formulaCalculoNota8;
	private String formulaUsoNota8;
	private Boolean nota8MediaFinal;
	private BimestreEnum bimestreNota8;
	private Double faixaNota8Menor;
	private Double faixaNota8Maior;
	private Double nota9;
	private Boolean utilizarNota9;
	private Boolean utilizarNota9PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota9ConceitoVOs;
	private String tituloNota9;
	private String tituloNotaApresentar9;
	private String formulaCalculoNota9;
	private String formulaUsoNota9;
	private Boolean nota9MediaFinal;
	private BimestreEnum bimestreNota9;
	private Double faixaNota9Menor;
	private Double faixaNota9Maior;
	private Double nota10;
	private Boolean utilizarNota10;
	private Boolean utilizarNota10PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota10ConceitoVOs;
	private String tituloNota10;
	private String tituloNotaApresentar10;
	private String formulaCalculoNota10;
	private String formulaUsoNota10;
	private Boolean nota10MediaFinal;
	private BimestreEnum bimestreNota10;
	private Double faixaNota10Menor;
	private Double faixaNota10Maior;
	private Double nota11;
	private Boolean utilizarNota11;
	private String tituloNota11;
	private String tituloNotaApresentar11;
	private String formulaCalculoNota11;
	private String formulaUsoNota11;
	private Boolean nota11MediaFinal;
	private Boolean utilizarNota11PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota11ConceitoVOs;
	private BimestreEnum bimestreNota11;
	private Double faixaNota11Menor;
	private Double faixaNota11Maior;
	private Double nota12;
	private Boolean utilizarNota12;
	private String tituloNota12;
	private String tituloNotaApresentar12;
	private String formulaCalculoNota12;
	private String formulaUsoNota12;
	private Boolean nota12MediaFinal;
	private Boolean utilizarNota12PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota12ConceitoVOs;
	private BimestreEnum bimestreNota12;
	private Double faixaNota12Menor;
	private Double faixaNota12Maior;
	private Double nota13;
	private Boolean utilizarNota13;
	private String tituloNota13;
	private String tituloNotaApresentar13;
	private String formulaCalculoNota13;
	private String formulaUsoNota13;
	private Boolean nota13MediaFinal;
	private Boolean utilizarNota13PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota13ConceitoVOs;
	private BimestreEnum bimestreNota13;
	private Double faixaNota13Menor;
	private Double faixaNota13Maior;
	private Double nota14;
	private Boolean utilizarNota14;
	private String tituloNota14;
	private String tituloNotaApresentar14;
	private String formulaCalculoNota14;
	private String formulaUsoNota14;
	private Boolean nota14MediaFinal;
	private Boolean utilizarNota14PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota14ConceitoVOs;
	private BimestreEnum bimestreNota14;
	private Double faixaNota14Menor;
	private Double faixaNota14Maior;
	private Boolean utilizarComoSubstitutiva14;
	private Boolean apresentarNota14;
	private String politicaSubstitutiva14;
	private Double nota15;
	private Boolean utilizarNota15;
	private String tituloNota15;
	private String tituloNotaApresentar15;
	private String formulaCalculoNota15;
	private String formulaUsoNota15;
	private Boolean nota15MediaFinal;
	private Boolean utilizarNota15PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota15ConceitoVOs;
	private BimestreEnum bimestreNota15;
	private Double faixaNota15Menor;
	private Double faixaNota15Maior;
	private Boolean utilizarComoSubstitutiva15;
	private Boolean apresentarNota15;
	private String politicaSubstitutiva15;
	private Double nota16;
	private Boolean utilizarNota16;
	private String tituloNota16;
	private String tituloNotaApresentar16;
	private String formulaCalculoNota16;
	private String formulaUsoNota16;
	private Boolean nota16MediaFinal;
	private Boolean utilizarNota16PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota16ConceitoVOs;
	private BimestreEnum bimestreNota16;
	private Double faixaNota16Menor;
	private Double faixaNota16Maior;
	private Boolean utilizarComoSubstitutiva16;
	private Boolean apresentarNota16;
	private String politicaSubstitutiva16;
	private Double nota17;
	private Boolean utilizarNota17;
	private String tituloNota17;
	private String tituloNotaApresentar17;
	private String formulaCalculoNota17;
	private String formulaUsoNota17;
	private Boolean nota17MediaFinal;
	private Boolean utilizarNota17PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota17ConceitoVOs;
	private BimestreEnum bimestreNota17;
	private Double faixaNota17Menor;
	private Double faixaNota17Maior;
	private Boolean utilizarComoSubstitutiva17;
	private Boolean apresentarNota17;
	private String politicaSubstitutiva17;
	private Double nota18;
	private Boolean utilizarNota18;
	private String tituloNota18;
	private String tituloNotaApresentar18;
	private String formulaCalculoNota18;
	private String formulaUsoNota18;
	private Boolean nota18MediaFinal;
	private Boolean utilizarNota18PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota18ConceitoVOs;
	private BimestreEnum bimestreNota18;
	private Double faixaNota18Menor;
	private Double faixaNota18Maior;
	private Boolean utilizarComoSubstitutiva18;
	private Boolean apresentarNota18;
	private String politicaSubstitutiva18;
	private Double nota19;
	private Boolean utilizarNota19;
	private String tituloNota19;
	private String tituloNotaApresentar19;
	private String formulaCalculoNota19;
	private String formulaUsoNota19;
	private Boolean nota19MediaFinal;
	private Boolean utilizarNota19PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota19ConceitoVOs;
	private BimestreEnum bimestreNota19;
	private Double faixaNota19Menor;
	private Double faixaNota19Maior;
	private Boolean utilizarComoSubstitutiva19;
	private Boolean apresentarNota19;
	private String politicaSubstitutiva19;
	private Double nota20;
	private Boolean utilizarNota20;
	private String tituloNota20;
	private String tituloNotaApresentar20;
	private String formulaCalculoNota20;
	private String formulaUsoNota20;
	private Boolean nota20MediaFinal;
	private Boolean utilizarNota20PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota20ConceitoVOs;
	private BimestreEnum bimestreNota20;
	private Double faixaNota20Menor;
	private Double faixaNota20Maior;
	private Boolean utilizarComoSubstitutiva20;
	private Boolean apresentarNota20;
	private String politicaSubstitutiva20;
	private Double nota21;
	private Boolean utilizarNota21;
	private String tituloNota21;
	private String tituloNotaApresentar21;
	private String formulaCalculoNota21;
	private String formulaUsoNota21;
	private Boolean nota21MediaFinal;
	private Boolean utilizarNota21PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota21ConceitoVOs;
	private BimestreEnum bimestreNota21;
	private Double faixaNota21Menor;
	private Double faixaNota21Maior;
	private Boolean utilizarComoSubstitutiva21;
	private Boolean apresentarNota21;
	private String politicaSubstitutiva21;
	private Double nota22;
	private Boolean utilizarNota22;
	private String tituloNota22;
	private String tituloNotaApresentar22;
	private String formulaCalculoNota22;
	private String formulaUsoNota22;
	private Boolean nota22MediaFinal;
	private Boolean utilizarNota22PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota22ConceitoVOs;
	private BimestreEnum bimestreNota22;
	private Double faixaNota22Menor;
	private Double faixaNota22Maior;
	private Boolean utilizarComoSubstitutiva22;
	private Boolean apresentarNota22;
	private String politicaSubstitutiva22;
	private Double nota23;
	private Boolean utilizarNota23;
	private String tituloNota23;
	private String tituloNotaApresentar23;
	private String formulaCalculoNota23;
	private String formulaUsoNota23;
	private Boolean nota23MediaFinal;
	private Boolean utilizarNota23PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota23ConceitoVOs;
	private BimestreEnum bimestreNota23;
	private Double faixaNota23Menor;
	private Double faixaNota23Maior;
	private Boolean utilizarComoSubstitutiva23;
	private Boolean apresentarNota23;
	private String politicaSubstitutiva23;
	private Double nota24;
	private Boolean utilizarNota24;
	private String tituloNota24;
	private String tituloNotaApresentar24;
	private String formulaCalculoNota24;
	private String formulaUsoNota24;
	private Boolean nota24MediaFinal;
	private Boolean utilizarNota24PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota24ConceitoVOs;
	private BimestreEnum bimestreNota24;
	private Double faixaNota24Menor;
	private Double faixaNota24Maior;
	private Boolean utilizarComoSubstitutiva24;
	private Boolean apresentarNota24;
	private String politicaSubstitutiva24;
	private Double nota25;
	private Boolean utilizarNota25;
	private String tituloNota25;
	private String tituloNotaApresentar25;
	private String formulaCalculoNota25;
	private String formulaUsoNota25;
	private Boolean nota25MediaFinal;
	private Boolean utilizarNota25PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota25ConceitoVOs;
	private BimestreEnum bimestreNota25;
	private Double faixaNota25Menor;
	private Double faixaNota25Maior;
	private Boolean utilizarComoSubstitutiva25;
	private Boolean apresentarNota25;
	private String politicaSubstitutiva25;
	private Double nota26;
	private Boolean utilizarNota26;
	private String tituloNota26;
	private String tituloNotaApresentar26;
	private String formulaCalculoNota26;
	private String formulaUsoNota26;
	private Boolean nota26MediaFinal;
	private Boolean utilizarNota26PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota26ConceitoVOs;
	private BimestreEnum bimestreNota26;
	private Double faixaNota26Menor;
	private Double faixaNota26Maior;
	private Boolean utilizarComoSubstitutiva26;
	private Boolean apresentarNota26;
	private String politicaSubstitutiva26;
	private Double nota27;
	private Boolean utilizarNota27;
	private String tituloNota27;
	private String tituloNotaApresentar27;
	private String formulaCalculoNota27;
	private String formulaUsoNota27;
	private Boolean nota27MediaFinal;
	private Boolean utilizarNota27PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota27ConceitoVOs;
	private BimestreEnum bimestreNota27;
	private Double faixaNota27Menor;
	private Double faixaNota27Maior;
	private Boolean utilizarComoSubstitutiva27;
	private Boolean apresentarNota27;
	private String politicaSubstitutiva27;
	private Double nota28;
	private Boolean utilizarNota28;
	private String tituloNota28;
	private String tituloNotaApresentar28;
	private String formulaCalculoNota28;
	private String formulaUsoNota28;
	private Boolean nota28MediaFinal;
	private Boolean utilizarNota28PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota28ConceitoVOs;
	private BimestreEnum bimestreNota28;
	private Double faixaNota28Menor;
	private Double faixaNota28Maior;
	private Boolean utilizarComoSubstitutiva28;
	private Boolean apresentarNota28;
	private String politicaSubstitutiva28;
	private Double nota29;
	private Boolean utilizarNota29;
	private String tituloNota29;
	private String tituloNotaApresentar29;
	private String formulaCalculoNota29;
	private String formulaUsoNota29;
	private Boolean nota29MediaFinal;
	private Boolean utilizarNota29PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota29ConceitoVOs;
	private BimestreEnum bimestreNota29;
	private Double faixaNota29Menor;
	private Double faixaNota29Maior;
	private Boolean utilizarComoSubstitutiva29;
	private Boolean apresentarNota29;
	private String politicaSubstitutiva29;
	private Double nota30;
	private Boolean utilizarNota30;
	private String tituloNota30;
	private String tituloNotaApresentar30;
	private String formulaCalculoNota30;
	private String formulaUsoNota30;
	private Boolean nota30MediaFinal;
	private Boolean utilizarNota30PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota30ConceitoVOs;
	private BimestreEnum bimestreNota30;
	private Double faixaNota30Menor;
	private Double faixaNota30Maior;
	private Boolean utilizarComoSubstitutiva30;
	private Boolean apresentarNota30;
	private String politicaSubstitutiva30;
	
	
	private Double nota31;
	private Boolean utilizarNota31;
	private String tituloNota31;
	private String tituloNotaApresentar31;
	private String formulaCalculoNota31;
	private String formulaUsoNota31;
	private Boolean nota31MediaFinal;
	private Boolean utilizarNota31PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota31ConceitoVOs;
	private BimestreEnum bimestreNota31;
	private Double faixaNota31Menor;
	private Double faixaNota31Maior;
	private Boolean utilizarComoSubstitutiva31;
	private Boolean apresentarNota31;
	private String politicaSubstitutiva31;
	
	private Double nota32;
	private Boolean utilizarNota32;
	private String tituloNota32;
	private String tituloNotaApresentar32;
	private String formulaCalculoNota32;
	private String formulaUsoNota32;
	private Boolean nota32MediaFinal;
	private Boolean utilizarNota32PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota32ConceitoVOs;
	private BimestreEnum bimestreNota32;
	private Double faixaNota32Menor;
	private Double faixaNota32Maior;
	private Boolean utilizarComoSubstitutiva32;
	private Boolean apresentarNota32;
	private String politicaSubstitutiva32;
	
	private Double nota33;
	private Boolean utilizarNota33;
	private String tituloNota33;
	private String tituloNotaApresentar33;
	private String formulaCalculoNota33;
	private String formulaUsoNota33;
	private Boolean nota33MediaFinal;
	private Boolean utilizarNota33PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota33ConceitoVOs;
	private BimestreEnum bimestreNota33;
	private Double faixaNota33Menor;
	private Double faixaNota33Maior;
	private Boolean utilizarComoSubstitutiva33;
	private Boolean apresentarNota33;
	private String politicaSubstitutiva33;
	
	private Double nota34;
	private Boolean utilizarNota34;
	private String tituloNota34;
	private String tituloNotaApresentar34;
	private String formulaCalculoNota34;
	private String formulaUsoNota34;
	private Boolean nota34MediaFinal;
	private Boolean utilizarNota34PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota34ConceitoVOs;
	private BimestreEnum bimestreNota34;
	private Double faixaNota34Menor;
	private Double faixaNota34Maior;
	private Boolean utilizarComoSubstitutiva34;
	private Boolean apresentarNota34;
	private String politicaSubstitutiva34;
	
	private Double nota35;
	private Boolean utilizarNota35;
	private String tituloNota35;
	private String tituloNotaApresentar35;
	private String formulaCalculoNota35;
	private String formulaUsoNota35;
	private Boolean nota35MediaFinal;
	private Boolean utilizarNota35PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota35ConceitoVOs;
	private BimestreEnum bimestreNota35;
	private Double faixaNota35Menor;
	private Double faixaNota35Maior;
	private Boolean utilizarComoSubstitutiva35;
	private Boolean apresentarNota35;
	private String politicaSubstitutiva35;
	
	private Double nota36;
	private Boolean utilizarNota36;
	private String tituloNota36;
	private String tituloNotaApresentar36;
	private String formulaCalculoNota36;
	private String formulaUsoNota36;
	private Boolean nota36MediaFinal;
	private Boolean utilizarNota36PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota36ConceitoVOs;
	private BimestreEnum bimestreNota36;
	private Double faixaNota36Menor;
	private Double faixaNota36Maior;
	private Boolean utilizarComoSubstitutiva36;
	private Boolean apresentarNota36;
	private String politicaSubstitutiva36;
	
	private Double nota37;
	private Boolean utilizarNota37;
	private String tituloNota37;
	private String tituloNotaApresentar37;
	private String formulaCalculoNota37;
	private String formulaUsoNota37;
	private Boolean nota37MediaFinal;
	private Boolean utilizarNota37PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota37ConceitoVOs;
	private BimestreEnum bimestreNota37;
	private Double faixaNota37Menor;
	private Double faixaNota37Maior;
	private Boolean utilizarComoSubstitutiva37;
	private Boolean apresentarNota37;
	private String politicaSubstitutiva37;
	
	private Double nota38;
	private Boolean utilizarNota38;
	private String tituloNota38;
	private String tituloNotaApresentar38;
	private String formulaCalculoNota38;
	private String formulaUsoNota38;
	private Boolean nota38MediaFinal;
	private Boolean utilizarNota38PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota38ConceitoVOs;
	private BimestreEnum bimestreNota38;
	private Double faixaNota38Menor;
	private Double faixaNota38Maior;
	private Boolean utilizarComoSubstitutiva38;
	private Boolean apresentarNota38;
	private String politicaSubstitutiva38;
	
	private Double nota39;
	private Boolean utilizarNota39;
	private String tituloNota39;
	private String tituloNotaApresentar39;
	private String formulaCalculoNota39;
	private String formulaUsoNota39;
	private Boolean nota39MediaFinal;
	private Boolean utilizarNota39PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota39ConceitoVOs;
	private BimestreEnum bimestreNota39;
	private Double faixaNota39Menor;
	private Double faixaNota39Maior;
	private Boolean utilizarComoSubstitutiva39;
	private Boolean apresentarNota39;
	private String politicaSubstitutiva39;
	
	private Double nota40;
	private Boolean utilizarNota40;
	private String tituloNota40;
	private String tituloNotaApresentar40;
	private String formulaCalculoNota40;
	private String formulaUsoNota40;
	private Boolean nota40MediaFinal;
	private Boolean utilizarNota40PorConceito;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota40ConceitoVOs;
	private BimestreEnum bimestreNota40;
	private Double faixaNota40Menor;
	private Double faixaNota40Maior;
	private Boolean utilizarComoSubstitutiva40;
	private Boolean apresentarNota40;
	private String politicaSubstitutiva40;
	
	
	
	
	
	private Double percentualFrequenciaAprovacao;
	private String mascaraPadraoGeracaoMatricula;
	private Integer numeroDisciplinaConsiderarReprovadoPeriodoLetivo;
	private Boolean permiteEvoluirPeriodoLetivoCasoReprovado;
	private Boolean reprovadoMatricularDisciplinaPeriodoLetivo;
	private ConfiguracoesVO configuracoesVO;
	private Integer diasMaximoReativacaoMatricula;
	private Boolean renovacaoMatriculaSequencial;
	private Boolean obrigarAceiteAlunoTermoParaEditarRenovacao;
	private Boolean permiteCursarDisciplinaEPreRequisito;
	private Boolean liberarPreRequisitoDisciplinaConcomitancia;
	private Boolean apresentarNota1;
	private Boolean apresentarNota2;
	private Boolean apresentarNota3;
	private Boolean apresentarNota4;
	private Boolean apresentarNota5;
	private Boolean apresentarNota6;
	private Boolean apresentarNota7;
	private Boolean apresentarNota8;
	private Boolean apresentarNota9;
	private Boolean apresentarNota10;
	private Boolean apresentarNota11;
	private Boolean apresentarNota12;
	private Boolean apresentarNota13;
	private Boolean utilizarComoSubstitutiva1;
	private Boolean utilizarComoSubstitutiva2;
	private Boolean utilizarComoSubstitutiva3;
	private Boolean utilizarComoSubstitutiva4;
	private Boolean utilizarComoSubstitutiva5;
	private Boolean utilizarComoSubstitutiva6;
	private Boolean utilizarComoSubstitutiva7;
	private Boolean utilizarComoSubstitutiva8;
	private Boolean utilizarComoSubstitutiva9;
	private Boolean utilizarComoSubstitutiva10;
	private Boolean utilizarComoSubstitutiva11;
	private Boolean utilizarComoSubstitutiva12;
	private Boolean utilizarComoSubstitutiva13;
	private String politicaSubstitutiva1;
	private String politicaSubstitutiva2;
	private String politicaSubstitutiva3;
	private String politicaSubstitutiva4;
	private String politicaSubstitutiva5;
	private String politicaSubstitutiva6;
	private String politicaSubstitutiva7;
	private String politicaSubstitutiva8;
	private String politicaSubstitutiva9;
	private String politicaSubstitutiva10;
	private String politicaSubstitutiva11;
	private String politicaSubstitutiva12;
	private String politicaSubstitutiva13;
	private Boolean notasDeCincoEmCincoDecimos;
	private Boolean notasDeCincoEmCincoDecimosApenasMedia;
	/**
	 * Habilida o arredondamento para mais: EX: se a media for 6.51 até 6.99
	 * alterar para 7 EX: se a media for 6.01 até 6.49 alterar para 6.5
	 */
	private Boolean utilizarArredondamentoMediaParaMais;
	private Boolean limitarQtdeDiasMaxDownload;
	private Integer qtdeMaxDiasDownload;
	private Boolean enviarMensagemNotaAbaixoMedia;
	private Boolean apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico;
	private Double pesoMediaNotaMeritoAcademico;
	private Double pesoMediaFrequenciaMeritoAcademico;
	private Boolean usarSituacaoAprovadoAproveitamentoTransferenciaGrade;
	private Boolean apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno;
	private Boolean considerarCampoNuloNotaZerada;
	private Boolean apresentarTextoSemNotaCampoNuloHistorico;
	private TipoCalculoCargaHorariaFrequencia tipoCalculoCargaHorariaFrequencia;
	private Boolean permiteRegistrarAulaFutura;
	public static final long serialVersionUID = 1L;

	private Integer quantidadeCasasDecimaisPermitirAposVirgula;

	// Boolean responsável para alterar o relatório de Histórico do Aluno
	private Boolean apresentarSiglaConcessaoCredito;
	private String tipoApresentarFrequenciaVisaoAluno;
	private String regraArredondamentoNota1;
	private String regraArredondamentoNota2;
	private String regraArredondamentoNota3;
	private String regraArredondamentoNota4;
	private String regraArredondamentoNota5;
	private String regraArredondamentoNota6;
	private String regraArredondamentoNota7;
	private String regraArredondamentoNota8;
	private String regraArredondamentoNota9;
	private String regraArredondamentoNota10;
	private String regraArredondamentoNota11;
	private String regraArredondamentoNota12;
	private String regraArredondamentoNota13;
	private String regraArredondamentoNota14;
	private String regraArredondamentoNota15;
	private String regraArredondamentoNota16;
	private String regraArredondamentoNota17;
	private String regraArredondamentoNota18;
	private String regraArredondamentoNota19;
	private String regraArredondamentoNota20;
	private String regraArredondamentoNota21;
	private String regraArredondamentoNota22;
	private String regraArredondamentoNota23;
	private String regraArredondamentoNota24;
	private String regraArredondamentoNota25;
	private String regraArredondamentoNota26;
	private String regraArredondamentoNota27;
	private String regraArredondamentoNota28;
	private String regraArredondamentoNota29;
	private String regraArredondamentoNota30;
	private String regraArredondamentoNota31;
	private String regraArredondamentoNota32;
	private String regraArredondamentoNota33;
	private String regraArredondamentoNota34;
	private String regraArredondamentoNota35;
	private String regraArredondamentoNota36;
	private String regraArredondamentoNota37;
	private String regraArredondamentoNota38;
	private String regraArredondamentoNota39;
	private String regraArredondamentoNota40;
	
	
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota1VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota2VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota3VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota4VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota5VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota6VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota7VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota8VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota9VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota10VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota11VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota12VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota13VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota14VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota15VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota16VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota17VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota18VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota19VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota20VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota21VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota22VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota23VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota24VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota25VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota26VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota27VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota28VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota29VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota30VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota31VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota32VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota33VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota34VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota35VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota36VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota37VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota38VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota39VO;
	private ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota40VO;
	private Boolean apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico;
	private Boolean obrigaInformarFormaIngressoMatricula;
	private Boolean obrigaInformarOrigemFormaIngressoMatricula;
	private Boolean bloquearRegistroAulaAnteriorDataMatricula;
	private Boolean ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao;
	private String mascaraNumeroProcessoExpedicaoDiploma;
	private String mascaraNumeroRegistroDiploma;
	/**
	 * Quando true o sistema não irá controlar para as disciplinas que fazem parte da composição se a mesma está em recuperação e também se está aprovada, pois
	 * a situação de histórico das filhas dependerá da situação da disciplina principal, ou seja se aprovar na disciplina principal está aprovadao nas filhas o mesmo 
	 * vale para o controle de recuperação.  
	 */
	private Boolean situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal;
	private Boolean calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes;	
	//E-MAIL PARA GRUPO DE DESTINATARIOS
	//COM ALUNOS EM ALTA PERFOMACE
//	private GrupoDestinataxriosVO grupoDestinatario;
//	private Double mediaNotaAluno;
//	private Double mediaFrequenciaAluno;
	/**
	 * Quando marcado será habilitado o campo numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina
	 */
	private Boolean habilitarControleInclusaoDisciplinaPeriodoFuturo;
	/**
	 * Com este campo informado, o sistema irá controlar na tela de matricula e matricula on-line quais disciplinas
	 * que o aluno poderá incluir após o periodo letivo em que se encontra, 
	 * ex: se o aluno estiver no 3º Periodo e neste campo o valor for 2 então só poderá incluir disciplinas do 4º e 5º Período  
	 * Obs: Existe uma funcionalidade no perfil de acesso da matrícula para liberar inclusão de outros periodos.
	 * Obs: Este campo só deve ser considerado se o campo  habilitarControleInclusaoDisciplinaPeriodoFuturo estiver marcado.
	 */
	private Integer  numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina;
	/**
	 * Quando marcado e o aluno não possuir disciplina de periodos letivos anteriores 
	 * sem cursar ou reprovado o mesmo não poderá realizar inclusão de disciplinas
	 * de periodos letivo futuros. 
	 * Obs Existe uma funcionalidade no perfil de acesso da matrícula para liberar esta inclusão
	 */
	private Boolean bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
	/**
	 * Quando marcado e o aluno não possuir disciplina de periodos letivos anteriores 
	 * sem cursar ou reprovado o mesmo não poderá realizar exclusão de disciplinas
	 * de periodos letivo em que está realizando a renovação. 
	 * Obs Existe uma funcionalidade no perfil de acesso da matrícula para liberar esta exclusão
	 */
	private Boolean bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
	/**
	 * Quando marcado e o aluno possuir disciplina de periodos letivos anteriores 
	 * sem cursar ou reprovado o mesmo será obrigado a incluir as disciplinas de depedência
	 * até o limite definido no campo porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia, 
	 * onde ser definido:
	 *  - 50% e o aluno deve 4 disciplina então terá que incluir no mínimo 2.
	 *  - 50% e o aluno deve 2 disciplina então terá que incluir no mínimo 1, ou seja, 
	 *    no caso de quantidade quebrada sempre arredondar para menos.
	 * Obs Existe uma funcionalidade no perfil de acesso da matrícula para liberar esta exclusão
	 */
	private Boolean habilitarControleInclusaoObrigatoriaDisciplinaDependencia;
	/**
	 * Este campo só é utilizado quando o campo habilitarControleInclusaoObrigatoriaDisciplinaDependencia estiver marcado, 
	 * ele define o percentual mínimo de disciplinas de dependencia que o aluno deverá incluir no ato da renovação.
	 * Obs.: Existe uma funcionalidade no perfil de acesso da matrícula para liberar a renovação com o quantidade de disciplinas abaixo do mínimo
	 */
	private Integer porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia;
	/**
	 * Com este campo marcado o sistema deverá no ato da renovação on-line quando o mesmo clicar para conculir a renovação,
	 * apresentar um informativo para o aluno que as disciplinas x, y e z não possuem vaga, por isto serão removidas automaticamente da renovação.
	 * 
	 */
	private Boolean removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline;
	/**
	 * Com este campo marcado o sistema irá definir no ato da renovação definir uma turma prática e teórica para disciplina em questão de forma que
	 * busque turmas que ainda possuem vaga (se possível).  
	 */
	private Boolean habilitarDistribuicaoTurmaPraticaTeoricaRenovacao;
	
	private boolean habilitarDistribuicaoDisciplinaDependenciaAutomatica = false;
	private boolean alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo = false;
	private boolean habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares = false;
	/**
	 * Com este campo marcado o sistema irá definir no ato da renovação definir uma turma prática e teórica considerando as que possuem aulas programas
	 * 
	 */	
	private Boolean distribuirTurmaPraticaTeoricaComAulaProgramada;
	private Boolean removerDisciplinaTurmaPraticaTeoricaComChoqueHorario;
	private Boolean considerarRegularAlunoDependenciaOptativa;
	private Boolean considerarPortadoDiplomaTransEntradaAlunoIrregular;
	
	/**
	 * Campos @Transient utilizados apenas como parametro para relatório.
	 */
	private boolean filtrarNota1;
	private boolean filtrarNota2;
	private boolean filtrarNota3;
	private boolean filtrarNota4;
	private boolean filtrarNota5;
	private boolean filtrarNota6;
	private boolean filtrarNota7;
	private boolean filtrarNota8;
	private boolean filtrarNota9;
	private boolean filtrarNota10;
	private boolean filtrarNota11;
	private boolean filtrarNota12;
	private boolean filtrarNota13;
	private boolean filtrarNota14;
	private boolean filtrarNota15;
	private boolean filtrarNota16;
	private boolean filtrarNota17;
	private boolean filtrarNota18;
	private boolean filtrarNota19;
	private boolean filtrarNota20;
	private boolean filtrarNota21;
	private boolean filtrarNota22;
	private boolean filtrarNota23;
	private boolean filtrarNota24;
	private boolean filtrarNota25;
	private boolean filtrarNota26;
	private boolean filtrarNota27;
	private boolean filtrarNota28;
	private boolean filtrarNota29;
	private boolean filtrarNota30;
	private boolean filtrarNota31;
	private boolean filtrarNota32;
	private boolean filtrarNota33;
	private boolean filtrarNota34;
	private boolean filtrarNota35;
	private boolean filtrarNota36;
	private boolean filtrarNota37;
	private boolean filtrarNota38;
	private boolean filtrarNota39;
	private boolean filtrarNota40;
	
	private Boolean matricularApenasDisciplinaAulaProgramada;
	
	private Integer quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula;
	private Boolean incluirAutomaticamenteDisciplinaGrupoOptativa;
	private Boolean permitirAlunoRegularIncluirDisciplinaGrupoOptativa;
	private Boolean bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa;
	
	private TipoUsoConfiguracaoAcademicoEnum tipoUsoConfiguracaoAcademico;
	private RegraCalculoDisciplinaCompostaEnum regraCalculoDisciplinaComposta;	
	
	private Integer quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula;
	
	/**
	 * Campo transient usado para guarda o mapa de notas 
	 */
	private HashMap<String, ConfiguracaoAcademicaNotaVO>mapaConfigNotas;
	
	private Boolean considerarDisciplinasReprovadasPeriodosLetivosAnteriores;

	private Boolean criarDigitoMascaraMatricula;
	private String formulaCriarDigitoMascaraMatricula;
	private Boolean reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha;
	private Boolean ocultarFrequenciaDisciplinaComposta;
	private Boolean ocultarMediaFinalDisciplinaCasoReprovado;
	private Boolean validarChoqueHorarioOutraMatriculaAluno;
	private List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaUtilizarVOs;
	private Boolean ocultarBotaoCalcularMedia;
	
    private Boolean validarDadosEnadeCensoMatricularAluno;
    private String formulaCoeficienteRendimento;
    private Integer casasDecimaisCoeficienteRendimento;
    
    private Boolean permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual;
    private Boolean registrarComoFaltaAulasRealizadasAposDataMatricula;
    
    private Boolean permitirAproveitamentoDisciplinasOptativas;
	/**	
	Construtor padrão da classe <code>ConfiguracaoAcademico</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	
	public ConfiguracaoAcademicoVO() {
		super();
		inicializarDados();
	}

	
	public ConfiguracaoAcademicoVO(String padrao) {
		super();
		if (padrao.equals("P1")) {
			inicializarDadosDefault1();
		} else if (padrao.equals("P2")) {
			inicializarDadosDefault2();
		} else if (padrao.equals("P3")) {
			inicializarDadosDefault3();
		} else {
			inicializarDados();
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ConfiguracaoAcademicoVO obj, HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs) throws ConsistirException, Exception {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Configurações Gerais Acadêmico) deve ser informado.");
		}
		if (!obj.getIsPossuiControleDisciplinaReprovacao()) {
			obj.setPermiteEvoluirPeriodoLetivoCasoReprovado(false);
			obj.setReprovadoMatricularDisciplinaPeriodoLetivo(false);
		}else if(obj.getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() > 0) {
			obj.setReprovadoMatricularDisciplinaPeriodoLetivo(true);
		}
		
		if(obj.getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && obj.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() <= 0){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAcademico_porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia"));
		}

		// Valida Notas Conflitantes
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(obj, "utilizarNota" + y)) {
				String nota1 = (String) UtilReflexao.invocarMetodoGet(obj, "tituloNota" + y);
				UtilReflexao.invocarMetodo(obj, "setTituloNota" + y, nota1.trim());
				for (VariaveisNotaEnum variavelNota : VariaveisNotaEnum.values()) {
                    if(nota1.contains(variavelNota.getValor())){
                    	throw new ConsistirException("A VARIÁVEL DA NOTA " + y + " esta em conflito com a VARIÁVEL RESERVADA " + variavelNota + ", ambas não podem ter a mesma sequência de caracteres.(" + obj.getNome() + ")");
                    }
                }
				if(nota1.contains("MAIOR") || "MAIOR".contains(nota1)){
					throw new ConsistirException("A VARIÁVEL DA NOTA " + y + " esta em conflito com a FUNÇÃO RESERVADA - MAIOR, ambas não podem ter a mesma sequência de caracteres.("+obj.getNome()+")");
				}
				for (int x = 1; x <= 40; x++) {
					if (x != y && (Boolean) UtilReflexao.invocarMetodoGet(obj, "utilizarNota" + x)) {
						String nota2 = (String) UtilReflexao.invocarMetodoGet(obj, "tituloNota" + x);
						if (nota2.contains(nota1) || nota1.contains(nota2)) {
							throw new ConsistirException("A VARIÁVEL DA NOTA " + x + " esta em conflito com a VARIÁVEL DA NOTA " + y + ", ambas não podem ter a mesma sequência de caracteres.(" + obj.getNome() + ")");
						}
					}
				}
			}
		}

		if (!obj.getFormulaCalculoMediaFinal().trim().isEmpty()) {			
			validarFormulaCalcula(obj, historicoVO, historicoFilhaComposicaoVOs, 0, true, obj.getFormulaCalculoMediaFinal(),"A FORMULA DE CÁLCULO APROVAÇÃO está incorreta ("+obj.getNome()+") " );
		}
		if(obj.getUsarFormulaCalculoFrequencia()) {
			validarFormulaCalcula(obj, historicoVO, historicoFilhaComposicaoVOs, 0, false, obj.getFormulaCalculoFrequencia(), "A FÓRMULA DE CÁLCULO DA FREQUÊNCIA está incorreta ("+obj.getNome()+") ");
		}
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(obj, "utilizarNota" + i)) {
				if (((String) UtilReflexao.invocarMetodoGet(obj, "tituloNota" + i)).trim().isEmpty()) {
					throw new ConsistirException("O campo VARIAVEL (Nota " + i + ") deve ser informado. (" + obj.getNome() + ")");
				}
				if (((String) UtilReflexao.invocarMetodoGet(obj, "tituloNotaApresentar" + i)).trim().isEmpty()) {
					throw new ConsistirException("O campo TÍTULO (Nota " + i + ") deve ser informado. (" + obj.getNome() + ")");
				}

			} else {
				UtilReflexao.invocarMetodoSetParametroNull(obj, "tituloNota" + i);
				UtilReflexao.invocarMetodoSetParametroNull(obj, "tituloNotaApresentar" + i);
				UtilReflexao.invocarMetodoSetParametroNull(obj, "formulaCalculoNota" + i);
				UtilReflexao.invocarMetodoSetParametroNull(obj, "formulaUsoNota" + i);
				UtilReflexao.invocarMetodoSetParametroNull(obj, "bimestreNota" + i);
				UtilReflexao.invocarMetodoSetParametroNull(obj, "utilizarNota" + i + "PorConceito");
				UtilReflexao.invocarMetodoSetParametroNull(obj, "nota" + i + "MediaFinal");
				UtilReflexao.invocarMetodoSetParametroNull(obj, "configuracaoAcademicoNota" + i + "ConceitoVOs");
			}
		}

		// Valida se a formula de CALCULO da nota é válida
		for (int i = 1; i <= 40; i++) {
			String formula = (String) UtilReflexao.invocarMetodoGet(obj, "formulaCalculoNota" + i);
			if (!formula.trim().isEmpty()) {				
				if(formula.contains(",")){					
					UtilReflexao.invocarMetodo(obj, "setFormulaCalculoNota" + i, formula);				
				}				
				validarFormulaCalcula(obj, historicoVO, historicoFilhaComposicaoVOs, i, false, formula, "A FORMULA DE CÁLCULO informada na nota " + i + " está incorreta.(" + obj.getNome() + ")");								
			}
		}

		// Valida se a condição de uso da nota é válida
		for (int i = 1; i <= 40; i++) {
			String formula = (String) UtilReflexao.invocarMetodoGet(obj, "formulaUsoNota" + i);
			if (!formula.trim().isEmpty()) {
				if(formula.contains(",")){					
					UtilReflexao.invocarMetodo(obj, "setFormulaUsoNota" + i, formula);				
				}
				validarFormulaCalcula(obj, historicoVO, historicoFilhaComposicaoVOs, i, true, formula, "A CONDIÇÃO DE USO informada na nota " + i + " está incorreta.(" + obj.getNome() + ")");				
			}
		}

		List<Integer> listaNotasQueSaoMedias = new ArrayList<Integer>();
		int j = 0;
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(obj, "nota" + i + "MediaFinal")) {
				j = j + 1;
				listaNotasQueSaoMedias.add(i);
			}
		}
		if (j > 1) {
			boolean controlador = false;
			for (Integer integer : listaNotasQueSaoMedias) {
				if (!(Boolean) UtilReflexao.invocarMetodoGet(obj, "formulaUsoNota" + integer.intValue()).equals("")) {
					controlador = true;
					break;
				} else {
					continue;
				}
			}
			if (!controlador) {
				throw new ConsistirException("Pelo menos uma das NOTAS QUE SÃO MÉDIAS FINAIS deve ter uma CONDIÇÃO DE USO cadastrada (" + obj.getNome() + ").");
			}
		}

		if ((obj.getConfiguracoesVO() == null || obj.getConfiguracoesVO().getCodigo().intValue() == 0) && !obj.isNovoObj()) {
			throw new ConsistirException("Esta configuração não pode ser salva, (CONFIGURAÇÕES - " + obj.getNome() + ") ainda não foi salvo");
		}
		if (obj.getMascaraPadraoGeracaoMatricula() != null) {
			if (obj.getMascaraPadraoGeracaoMatricula().toUpperCase().indexOf("I") == -1) {
				throw new ConsistirException("O campo MASCARA PADRÃO (Configurações Gerais Acadêmico - " + obj.getNome() + ") deve conter 'I' para criar incremento na matrícula.");
			}
			if (obj.getMascaraPadraoGeracaoMatricula().length() > 20) {
				throw new ConsistirException("O campo MASCARA PADRÃO (Configurações Gerais Acadêmico - " + obj.getNome() + ") não pode ultrapassar 20 caracteres.");
			}
		}
		
		if (obj.getCriarDigitoMascaraMatricula() && Uteis.isAtributoPreenchido(obj.getFormulaCriarDigitoMascaraMatricula())) {
			if (obj.getFormulaCriarDigitoMascaraMatricula().toUpperCase().contains("B")){
				throw new ConsistirException("O campo FÓRMULA DÍGITO MÁSCARA MATRÍCULA (Configurações Gerais Acadêmico) não pode conter a letra B, que corresponde a sigla do curso.");
			}
			if (!obj.getFormulaCriarDigitoMascaraMatricula().toUpperCase().contains("/")){
				throw new ConsistirException("O campo FÓRMULA DÍGITO MÁSCARA MATRÍCULA (Configurações Gerais Acadêmico) deve conter uma barra inclinada (/) para o funcionamento da fórmula.");
			}
		}

		// validando nota conceito
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(obj, "utilizarNota" + i + "PorConceito")) {
				List informadoConfiguracaoNotaConceito = (List) UtilReflexao.invocarMetodoGet(obj, "configuracaoAcademicoNota" + i + "ConceitoVOs");
				if (informadoConfiguracaoNotaConceito.isEmpty()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAcademico_informarNotaConceito" + i) + "(" + obj.getNome() + ")");
				}
			} else {
				UtilReflexao.invocarMetodoSetParametroNull(obj, "configuracaoAcademicoNota" + i + "ConceitoVOs");
			}
		}

		if (!obj.getNota1MediaFinal() && !obj.getNota2MediaFinal() && !obj.getNota3MediaFinal() && !obj.getNota4MediaFinal() && !obj.getNota5MediaFinal() && !obj.getNota6MediaFinal() && !obj.getNota7MediaFinal() && !obj.getNota8MediaFinal() && !obj.getNota9MediaFinal() && !obj.getNota10MediaFinal() && !obj.getNota11MediaFinal() && !obj.getNota12MediaFinal() && !obj.getNota11MediaFinal() && !obj.getNota12MediaFinal() && !obj.getNota13MediaFinal() && !obj.getNota14MediaFinal() && !obj.getNota15MediaFinal() && !obj.getNota16MediaFinal() && !obj.getNota17MediaFinal() && !obj.getNota18MediaFinal() && !obj.getNota19MediaFinal() && !obj.getNota20MediaFinal() && !obj.getNota21MediaFinal() && !obj.getNota22MediaFinal() && !obj.getNota23MediaFinal() && !obj.getNota24MediaFinal() && !obj.getNota25MediaFinal() && !obj.getNota26MediaFinal() && !obj.getNota27MediaFinal() && !obj.getNota28MediaFinal() && !obj.getNota29MediaFinal() && !obj.getNota30MediaFinal() && !obj.getNota31MediaFinal() && !obj.getNota32MediaFinal() && !obj.getNota33MediaFinal() && !obj.getNota34MediaFinal() && !obj.getNota35MediaFinal() && !obj.getNota36MediaFinal() && !obj.getNota37MediaFinal() && !obj.getNota38MediaFinal() && !obj.getNota39MediaFinal() && !obj.getNota40MediaFinal()) {
			throw new ConsistirException("Pelo menos UMA das NOTAS (Configurações Gerais Acadêmico - " + obj.getNome() + ") deve ser definida como MÉDIA FINAL");
		}

		// validando forma calculo para notas que é média final
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(obj, "nota" + i + "MediaFinal")) {
				String formulaCalculo = (String) UtilReflexao.invocarMetodoGet(obj, "formulaCalculoNota" + i);
				if (formulaCalculo.equals("")) {
					throw new ConsistirException("Deve ser definida a FORMULA DE CÁLCULO (Configurações Gerais Acadêmico - " + obj.getNome() + ") da nota " + i);
				}
			}
		}
		
		if (Uteis.isAtributoPreenchido(obj.getFormulaCoeficienteRendimento())){
			if (!validarFormulaCoeficienteRendimento(obj.getFormulaCoeficienteRendimento())) {
				throw new Exception("A FÓRMULA COEFICIENTE DE RENDIMENTO está inválida.");								
			}
		}

	}
	
	public static void validarFormulaCalcula(ConfiguracaoAcademicoVO configuracaoAcademicoVO, HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, int numeroNota, boolean formulaUso, String formula, String msgErro) throws ConsistirException, Exception {		
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		if(formula.contains("=!")){
			throw new ConsistirException("A formula " + formula + "possui os símbolos =!, que devem ser invertidos (!=)");
		}		
		formula = realizarSubstituicaoFuncaoComposicao(historicoVO, historicoFilhaComposicaoVOs, formula, numeroNota, formulaUso);
		for (VariaveisNotaEnum variavelNota : VariaveisNotaEnum.values()) {
			if(variavelNota.getTipoValor().equals(TipoCampoEnum.DOUBLE)){
				formula = formula.replaceAll(variavelNota.getValor(), "0.0");
			}else if(variavelNota.getTipoValor().equals(TipoCampoEnum.INTEIRO)){
					formula = formula.replaceAll(variavelNota.getValor(), "0");			
			}else if(variavelNota.getTipoValor().equals(TipoCampoEnum.BOOLEAN)){
				formula = formula.replaceAll(variavelNota.getValor(), "false");
			}
        }
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + y)) {
				String nota = (String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNota" + y);
				formula = formula.replaceAll(nota, "0.0");
			}
		}
		formula = formula.replaceAll(" e ", " && ");
		formula = formula.replaceAll(" E ", " && ");
		formula = formula.replaceAll(" ou ", " || ");
		formula = formula.replaceAll(" OU ", " || ");
		formula = formula.replaceAll("=", "==");
		formula = formula.replaceAll("====", "==");
		formula = formula.replaceAll(">==", ">=");
		formula = formula.replaceAll("<==", "<=");
		formula = formula.replaceAll("!==", "!=");
		
		formula = validarUsoFuncaoMaior(formula, 0, configuracaoAcademicoVO.getNome());
		Object result;
		try {
			result = engine.eval(formula);
			if (!formulaUso  && result != null && !(result instanceof Number)) {
				try {
					Double.valueOf(result.toString());
				} catch (NumberFormatException e) {
					throw new ConsistirException(msgErro);								
				}
			}else if (formulaUso  && !(result instanceof Boolean)) {
				throw new ConsistirException(msgErro);				
			}				
		} catch (ScriptException e) {
			throw new ConsistirException(msgErro);
		}
	}

	public void realizarUpperCaseDados() {
		this.mascaraPadraoGeracaoMatricula = mascaraPadraoGeracaoMatricula.toUpperCase();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
		setFormulaCalculoMediaFinal("");
		setNota1(0.0);
		setUtilizarNota1(Boolean.FALSE);
		setTituloNota1("");
		setFormulaCalculoNota1("");
		setFormulaUsoNota1("");
		setNota2(0.0);
		setUtilizarNota2(Boolean.FALSE);
		setTituloNota2("");
		setFormulaCalculoNota2("");
		setFormulaUsoNota2("");
		setNota3(0.0);
		setUtilizarNota3(Boolean.FALSE);
		setTituloNota3("");
		setFormulaCalculoNota3("");
		setFormulaUsoNota3("");
		setNota4(0.0);
		setUtilizarNota4(Boolean.FALSE);
		setTituloNota4("");
		setFormulaCalculoNota4("");
		setFormulaUsoNota4("");
		setNota5(0.0);
		setUtilizarNota5(Boolean.FALSE);
		setTituloNota5("");
		setFormulaCalculoNota5("");
		setFormulaUsoNota5("");
		setNota6(0.0);
		setUtilizarNota6(Boolean.FALSE);
		setTituloNota6("");
		setFormulaCalculoNota6("");
		setFormulaUsoNota6("");
		setNota7(0.0);
		setUtilizarNota7(Boolean.FALSE);
		setTituloNota7("");
		setFormulaCalculoNota7("");
		setFormulaUsoNota7("");
		setNota8(0.0);
		setUtilizarNota8(Boolean.FALSE);
		setTituloNota8("");
		setFormulaCalculoNota8("");
		setFormulaUsoNota8("");
		setNota9(0.0);
		setUtilizarNota9(Boolean.FALSE);
		setTituloNota9("");
		setFormulaCalculoNota9("");
		setFormulaUsoNota9("");
		setNota10(0.0);
		setUtilizarNota10(Boolean.FALSE);
		setTituloNota10("");
		setFormulaCalculoNota10("");
		setFormulaUsoNota10("");
		setNota11(0.0);
		setUtilizarNota11(Boolean.FALSE);
		setTituloNota11("");
		setFormulaCalculoNota11("");
		setFormulaUsoNota11("");
		setNota12(0.0);
		setUtilizarNota12(Boolean.FALSE);
		setTituloNota12("");
		setFormulaCalculoNota12("");
		setFormulaUsoNota12("");
		setNota13(0.0);
		setUtilizarNota13(Boolean.FALSE);
		setTituloNota13("");
		setFormulaCalculoNota13("");
		setFormulaUsoNota13("");
		// **************** NOTA 14
		setNota14(0.0);
		setUtilizarNota14(Boolean.FALSE);
		setTituloNota14("");
		setFormulaCalculoNota14("");
		setFormulaUsoNota14("");
		// ************************
		// **************** NOTA 15
		setNota15(0.0);
		setUtilizarNota15(Boolean.FALSE);
		setTituloNota15("");
		setFormulaCalculoNota15("");
		setFormulaUsoNota15("");
		// ************************
		// **************** NOTA 16
		setNota16(0.0);
		setUtilizarNota16(Boolean.FALSE);
		setTituloNota16("");
		setFormulaCalculoNota16("");
		setFormulaUsoNota16("");
		// ************************
		// **************** NOTA 17
		setNota17(0.0);
		setUtilizarNota17(Boolean.FALSE);
		setTituloNota17("");
		setFormulaCalculoNota17("");
		setFormulaUsoNota17("");
		// ************************
		// **************** NOTA 18
		setNota18(0.0);
		setUtilizarNota18(Boolean.FALSE);
		setTituloNota18("");
		setFormulaCalculoNota18("");
		setFormulaUsoNota18("");
		// ************************
		// **************** NOTA 19
		setNota19(0.0);
		setUtilizarNota19(Boolean.FALSE);
		setTituloNota19("");
		setFormulaCalculoNota19("");
		setFormulaUsoNota19("");
		// ************************
		// **************** NOTA 20
		setNota20(0.0);
		setUtilizarNota20(Boolean.FALSE);
		setTituloNota20("");
		setFormulaCalculoNota20("");
		setFormulaUsoNota20("");
		// ************************
		// **************** NOTA 21
		setNota21(0.0);
		setUtilizarNota21(Boolean.FALSE);
		setTituloNota21("");
		setFormulaCalculoNota21("");
		setFormulaUsoNota21("");
		// ************************
		// **************** NOTA 22
		setNota22(0.0);
		setUtilizarNota22(Boolean.FALSE);
		setTituloNota22("");
		setFormulaCalculoNota22("");
		setFormulaUsoNota22("");
		// ************************
		// **************** NOTA 23
		setNota23(0.0);
		setUtilizarNota23(Boolean.FALSE);
		setTituloNota23("");
		setFormulaCalculoNota23("");
		setFormulaUsoNota23("");
		// ************************
		// **************** NOTA 24
		setNota24(0.0);
		setUtilizarNota24(Boolean.FALSE);
		setTituloNota24("");
		setFormulaCalculoNota24("");
		setFormulaUsoNota24("");
		// ************************
		// **************** NOTA 25
		setNota25(0.0);
		setUtilizarNota25(Boolean.FALSE);
		setTituloNota25("");
		setFormulaCalculoNota25("");
		setFormulaUsoNota25("");
		// ************************
		// **************** NOTA 26
		setNota26(0.0);
		setUtilizarNota26(Boolean.FALSE);
		setTituloNota26("");
		setFormulaCalculoNota26("");
		setFormulaUsoNota26("");
		// ************************
		// **************** NOTA 27
		setNota27(0.0);
		setUtilizarNota27(Boolean.FALSE);
		setTituloNota27("");
		setFormulaCalculoNota27("");
		setFormulaUsoNota27("");
		// ************************
		// **************** NOTA 28
		setNota28(0.0);
		setUtilizarNota28(Boolean.FALSE);
		setTituloNota28("");
		setFormulaCalculoNota28("");
		setFormulaUsoNota28("");
		// ************************
		// **************** NOTA 29
		setNota29(0.0);
		setUtilizarNota29(Boolean.FALSE);
		setTituloNota29("");
		setFormulaCalculoNota29("");
		setFormulaUsoNota29("");
		// ************************
		// **************** NOTA 30
		setNota30(0.0);
		setUtilizarNota30(Boolean.FALSE);
		setTituloNota30("");
		setFormulaCalculoNota30("");
		setFormulaUsoNota30("");
		// ************************
		// **************** NOTA 31
		setNota31(0.0);
		setUtilizarNota31(Boolean.FALSE);
		setTituloNota31("");
		setFormulaCalculoNota31("");
		setFormulaUsoNota31("");
		// ************************
		// **************** NOTA 32
		setNota32(0.0);
		setUtilizarNota32(Boolean.FALSE);
		setTituloNota32("");
		setFormulaCalculoNota32("");
		setFormulaUsoNota32("");
		// ************************
		// **************** NOTA 33
		setNota33(0.0);
		setUtilizarNota33(Boolean.FALSE);
		setTituloNota33("");
		setFormulaCalculoNota33("");
		setFormulaUsoNota33("");
		// ************************
		// **************** NOTA 34
		setNota34(0.0);
		setUtilizarNota34(Boolean.FALSE);
		setTituloNota34("");
		setFormulaCalculoNota34("");
		setFormulaUsoNota34("");
		// ************************
		// **************** NOTA 35
		setNota35(0.0);
		setUtilizarNota35(Boolean.FALSE);
		setTituloNota35("");
		setFormulaCalculoNota35("");
		setFormulaUsoNota35("");
		// ************************
		// **************** NOTA 36
		setNota36(0.0);
		setUtilizarNota36(Boolean.FALSE);
		setTituloNota36("");
		setFormulaCalculoNota36("");
		setFormulaUsoNota36("");
		// ************************
		// **************** NOTA 37
		setNota37(0.0);
		setUtilizarNota37(Boolean.FALSE);
		setTituloNota37("");
		setFormulaCalculoNota37("");
		setFormulaUsoNota37("");
		// ************************
		// **************** NOTA 38
		setNota38(0.0);
		setUtilizarNota38(Boolean.FALSE);
		setTituloNota38("");
		setFormulaCalculoNota38("");
		setFormulaUsoNota38("");
		// ************************
		// **************** NOTA 39
		setNota39(0.0);
		setUtilizarNota39(Boolean.FALSE);
		setTituloNota39("");
		setFormulaCalculoNota39("");
		setFormulaUsoNota39("");
		// ************************
		// **************** NOTA 40
		setNota40(0.0);
		setUtilizarNota40(Boolean.FALSE);
		setTituloNota40("");
		setFormulaCalculoNota40("");
		setFormulaUsoNota40("");


		setPercentualFrequenciaAprovacao(0.0);
		setMascaraPadraoGeracaoMatricula("AASUUUCCCCIIIII");
	}

	public void inicializarDadosDefault1() {
		inicializarDados();
		setNome("Configuração Acadêmico Default 1");
		setFormulaCalculoMediaFinal("((ME>=7) ou (MF>=10))");
		setNota1(0.0);
		setUtilizarNota1(Boolean.TRUE);
		setTituloNota1("V1");
		setFormulaCalculoNota1("");
		setFormulaUsoNota1("");
		setNota2(0.0);
		setUtilizarNota2(Boolean.TRUE);
		setTituloNota2("VT");
		setFormulaCalculoNota2("");
		setFormulaUsoNota2("");
		setNota3(0.0);
		setUtilizarNota3(Boolean.TRUE);
		setTituloNota3("V2");
		setFormulaCalculoNota3("");
		setFormulaUsoNota3("");
		setNota4(0.0);
		setUtilizarNota4(Boolean.TRUE);
		setNota4MediaFinal(true);
		setTituloNota4("ME");
		setFormulaCalculoNota4("((V1+VT+V2)/3)");
		setFormulaUsoNota4("");
		setNota5(0.0);
		setUtilizarNota5(Boolean.TRUE);
		setTituloNota5("VS");
		setFormulaCalculoNota5("");
		setFormulaUsoNota5("((ME>=4)e(ME<7))");
		setNota6(0.0);
		setUtilizarNota6(Boolean.TRUE);
		setTituloNota6("MF");
		setNota6MediaFinal(true);
		setFormulaCalculoNota6("(ME+VS)");
		setFormulaUsoNota6("((ME>=4)e(ME<7))");
		setPercentualFrequenciaAprovacao(75.0);
		setMascaraPadraoGeracaoMatricula("AASUUUCCCCIIIII");
	}

	public void inicializarDadosDefault2() {
		inicializarDados();
		setNome("Configuração Acadêmico Default 2");
		setFormulaCalculoMediaFinal("((ME>=5) ou (MF>=5))");
		setNota1(0.0);
		setUtilizarNota1(Boolean.TRUE);
		setTituloNota1("V1");
		setFormulaCalculoNota1("");
		setFormulaUsoNota1("");
		setNota2(0.0);
		setUtilizarNota2(Boolean.TRUE);
		setTituloNota2("VT");
		setFormulaCalculoNota2("");
		setFormulaUsoNota2("");
		setNota3(0.0);
		setUtilizarNota3(Boolean.TRUE);
		setTituloNota3("V2");
		setFormulaCalculoNota3("");
		setFormulaUsoNota3("");
		setNota4(0.0);
		setUtilizarNota4(Boolean.TRUE);
		setTituloNota4("ME");
		setFormulaCalculoNota4("((V1+VT+V2)/3)");
		setFormulaUsoNota4("");
		setNota4MediaFinal(true);
		setNota5(0.0);
		setUtilizarNota5(Boolean.TRUE);
		setTituloNota5("VS");
		setFormulaCalculoNota5("");
		setFormulaUsoNota5("(ME<5)");
		setNota6(0.0);
		setUtilizarNota6(Boolean.TRUE);
		setTituloNota6("MF");
		setFormulaCalculoNota6("(ME+VS)");
		setFormulaUsoNota6("(ME<5)");
		setNota6MediaFinal(true);
		setPercentualFrequenciaAprovacao(75.0);
		setMascaraPadraoGeracaoMatricula("AASUUUCCCCIIIII");
	}

	public void inicializarDadosDefault3() {
		inicializarDados();
		setNome("Configuração Acadêmico Default 3");
		setFormulaCalculoMediaFinal("((ME>=5) ou (VS>=5))");
		setNota1(0.0);
		setUtilizarNota1(Boolean.TRUE);
		setTituloNota1("V1");
		setFormulaCalculoNota1("");
		setFormulaUsoNota1("");
		setNota2(0.0);
		setUtilizarNota2(Boolean.TRUE);
		setTituloNota2("V2");
		setFormulaCalculoNota2("");
		setFormulaUsoNota2("");
		setNota3(0.0);
		setUtilizarNota3(Boolean.TRUE);
		setTituloNota3("ME");
		setFormulaCalculoNota3("");
		setFormulaUsoNota3("((V1+V2)/2)");
		setNota3MediaFinal(true);
		setNota4(0.0);
		setUtilizarNota4(Boolean.TRUE);
		setTituloNota4("VS");
		setFormulaCalculoNota4("");
		setFormulaUsoNota4("(ME<6)");
		setNota5(0.0);
		setUtilizarNota5(Boolean.TRUE);
		setTituloNota5("MF");
		setFormulaCalculoNota5("(ME+VS)");
		setFormulaUsoNota5("(ME<5)");
		setNota5MediaFinal(true);
		setPercentualFrequenciaAprovacao(75.0);
		setMascaraPadraoGeracaoMatricula("AASUUUCCCCIIIII");
	}

	public String substituirSimbolosFormulaCalculoMediaFinal(HistoricoVO historicoVO, String formulaCalculoMedia, List<String> listaNotas) {
		for (int i = 1; i <= 40; i++) {
			String tituloNota = null;
			Double valorNota = null;
			try {
				tituloNota = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i);
				valorNota = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
				if (!tituloNota.equals("")) {
					if (formulaCalculoMedia.contains(tituloNota)) {
						if (valorNota == null && !historicoVO.getConfiguracaoAcademico().getConsiderarCampoNuloNotaZerada()) {
							formulaCalculoMedia = formulaCalculoMedia.replaceAll("\\b" + tituloNota + "\\b", "null");
						}else if (valorNota == null && historicoVO.getConfiguracaoAcademico().getConsiderarCampoNuloNotaZerada()) {
							formulaCalculoMedia = formulaCalculoMedia.replaceAll("\\b" + tituloNota + "\\b", String.valueOf(0));
						}else{
							formulaCalculoMedia = formulaCalculoMedia.replaceAll("\\b" + tituloNota + "\\b", String.valueOf(valorNota));						
						}
					}
				}
			} finally {
				if (tituloNota != null) {
					tituloNota = null;
				}
				if (valorNota != null) {
					valorNota = null;
				}
			}
		}
		formulaCalculoMedia = substituirVariaveisNotaFormaCalculoNota(historicoVO, formulaCalculoMedia);
		return formulaCalculoMedia;
	}

	public String mapearMenorNotaAlunoDasNotasDefinidasParaSubstituicao(HistoricoVO historicoVO, String listaNotasSubstituir, String formulaUsoNotaSubstitutiva) {
		if (!listaNotasSubstituir.equals("")) {
			String tituloMenorNota = "";
			Double valorMenorNota = 999999999.0;
			if (!listaNotasSubstituir.endsWith(";")) {
				listaNotasSubstituir = listaNotasSubstituir + ";";
			}
			for (int i = 1; i <= 40; i++) {
				String tituloNota = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i);
				if (tituloNota == null) {
					tituloNota = "";
				}
				if ((listaNotasSubstituir.contains(tituloNota + ";")) && (!tituloNota.equals(""))) { // o
																										// ponto
																										// e
																										// virgula
																										// é
																										// necessário
																										// para
																										// que
																										// ao
																										// verificarmos
																										// o
																										// contains,
																										// a
																										// nota1
																										// não
																										// seja
																										// processada
																										// como
																										// a
																										// nota10,
																										// pois
																										// isto
																										// temos
																										// que
																										// testa
																										// contais('nota1;')
					Double valorNotaSubstituir = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
					// if ((valorNotaSubstituir == null) || (valorNotaSubstituir
					// <= (new Double(0.0)))) {
					if (valorNotaSubstituir == null) {
						// Se esta nota é nulla, então vamos iniciá-la com zero
						// para fins de ser
						// processada como zero.
						valorNotaSubstituir = 0.0;
					}
					if (valorNotaSubstituir <= valorMenorNota) {
						// No caso colocamos menor ou igual, pois caso tenham
						// mais de uma
						// nota menor (ou seja, duas notas que valores iguais e
						// menores,
						// então a última nota deve ser substituída ao invés da
						// primeira.
						valorMenorNota = valorNotaSubstituir;
						tituloMenorNota = tituloNota;
					}
				}
			}
			return tituloMenorNota;
		}
		return "";
	}
        
        /**
         * Método responsável por verificar se uma nota substitutiva deve ser validada e possui
         * inconsistencia com relação a faixa de nota a ser substituida. 
         * Isto ocorrerá caso a nota SEJA SUBSTITUTIVA e a faixa de valores dela, estiver zerada.
         * Para este caso específico o SEI deverá verificar se o valor da nota substitutiva obdece na verdade a faixa
         * de notas da nota que será substituída. Para isto toda a regra de substituiçõa precisará
         * ser aplicada para saber qual nota será substituída e poder validar a faixa da mesma sobre
         * o valor a ser substituído.
         * @param historicoVO 
         */
	public boolean verificarFaixaNotaSubstitutivaValidaComRelacaoNotaASerSubstituida(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs) throws Exception {
        // como estamos querendo validar se uma nota substitutiva pode ser aplicada para uma
        // determinada nota, teremos que verificar se a nota substitutiva foi aplicada para alguma
        // nota e caso sim, se houve erro nesta aplicacao. O que pode ser controlado pelo atributo
        // erroNotaSubstitutiva do objeto historicoVO.
        for (int i = 1; i <= 40; i++) {
            historicoVO.setErroNotaSubstitutiva("");
            verificarExisteEDeveSerAplicadaNotaSubstitutiva(historicoVO, historicoFilhaComposicaoVOs, i);
            if (!historicoVO.getErroNotaSubstitutiva().equals("")) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Este método deve varrer todas as notas e verificar se existe nota marcada
	 * como utilizarComoSubstitutiva e que deve ser utilizada para substituir a
	 * nota em questão.
	 * 
	 * @param historicoVO
	 * @param formulaCalculoNota
	 * @param indice
	 * @return
	 */
	public Double verificarExisteEDeveSerAplicadaNotaSubstitutiva(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, int indice) throws Exception {
		String tituloNotaSubstituir = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + indice);
		Double valorNotaSubstituir = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + indice);
                Double faixaNotaMenorSubstitur = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + indice + "Menor");
                Double faixaNotaMaiorSubstitur = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + indice + "Maior");
                
		for (int i = 1; i <= 40; i++) {
                        String tituloNotaSubstitutiva = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i);
			Boolean utilizarComoSubstitutiva = (Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarComoSubstitutiva" + i);
			Double valorNotaSubstitutiva = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
			if ((utilizarComoSubstitutiva) && (valorNotaSubstitutiva != null)) {
                                Double faixaNotaMenorSubstitutiva = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + i + "Menor");
                                Double faixaNotaMaiorSubstitutiva = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + i + "Maior");
                                if (faixaNotaMenorSubstitutiva == null) {
                                    faixaNotaMenorSubstitutiva = 0.0;
                                }
                                if (faixaNotaMaiorSubstitutiva == null) {
                                    faixaNotaMaiorSubstitutiva = 0.0;
                                }
                                Boolean utilizarFaixaNotaSubstitur = Boolean.FALSE;
                                if ((faixaNotaMenorSubstitutiva.equals(0.0) && faixaNotaMaiorSubstitutiva.equals(0.0))) {
                                    // Este controle é utilizado para verificar que quando a faixa de notas
                                    // da substitutiva nao for informada (ambas forem nulas ou igual 0.0), entao 
                                    // o sistema irá validar o valor da nota a ser utilizada como substitutiva com relação 
                                    // a faixa de notas da nota que está sendo substituída. Por exemplo, se o professor
                                    // informou uma nota substituiva de valor 8, que será utilizar para substitir uma nota
                                    // cuja a faixa de valores vai de 0 a 7. entao o sistema deverá gerar uma exceção.
                                    utilizarFaixaNotaSubstitur = Boolean.TRUE;
                                }                        

                                String listaNotasSubstituir = (String) UtilReflexao.invocarMetodoGet(this, "formulaCalculoNota" + i); // utilizamos
																														// este
																														// campo
																														// para
																														// manter
																														// lista
																														// de
																														// notas
																														// a
																														// serem
																														// substituídas
				if (listaNotasSubstituir.contains(tituloNotaSubstituir)) {
					// Se entrar aqui é por que encontramos uma nota
					// substitutiva para a nota que estamos processando.
					// Agora temos que verificar se a formula de uso da mesma é
					// atendida para procedermos com a substituição.
					String formulaUsoNotaSubstitutiva = (String) UtilReflexao.invocarMetodoGet(this, "formulaUsoNota" + i);
					if (!formulaUsoNotaSubstitutiva.equals("")) {
						String formulaUsoNotaComValores = substituirSimbolosNaFormulaUsoNota(historicoVO, historicoFilhaComposicaoVOs, formulaUsoNotaSubstitutiva, i);
						try {
							if (!verificarSituacaoAluno(formulaUsoNotaComValores)) {
								continue;
							}
						} catch (NumberFormatException e) {
							e.getMessage();
						}
					}
					// Se chegarmos aqui é por que a formulaDeUso foi atendida,
					// ou esta vazia - o que indica que a mesma deve ser
					// utilizada
					// String tituloNota =
					// (String)UtilReflexao.invocarMetodoGet(this, "tituloNota"
					// + i);
					String politicaSubstituicao = (String) UtilReflexao.invocarMetodoGet(this, "politicaSubstitutiva" + i);
					if (PoliticaNotaSubstitutivaEnum.valueOf(politicaSubstituicao) == PoliticaNotaSubstitutivaEnum.SUBSTITUIR_MENOR_NOTA) {
						if (valorNotaSubstituir == null) {
							// se estiver nulo, assumo como zero para efeitos de
							// calculo da
							// menor nota
							// lembrando que o tratamento de nota nulo deve ser
							// tratada como
							// zerada, já foi realizado
							// na rotina principal que chama esta.
							valorNotaSubstituir = new Double(0.0);
						}
						if (valorNotaSubstitutiva > valorNotaSubstituir) {
							// Se entrarmos aqui é por que a nota substitutiva é
							// superior a nota do aluno em avaliação neste
							// momento
							// Logo, resta sabermos se esta é a menor nota
							// dentre as que vão ser substituídas. Isto é
							// necessário pois
							// esta política de substituição prega, que a nota
							// substitutiva deve substituir aquela que for
							// menor, dentre
							// as definidas para substituição. Assim, temos que
							// ver se perante as demais notas que podem ser
							// substituídas
							// esta de fato é a menor.
							String tituloMenorNota = mapearMenorNotaAlunoDasNotasDefinidasParaSubstituicao(historicoVO, listaNotasSubstituir, formulaUsoNotaSubstitutiva);
							if (tituloMenorNota.equals(tituloNotaSubstituir)) {
                                                                if (utilizarFaixaNotaSubstitur) {
                                                                    if ((valorNotaSubstitutiva < faixaNotaMenorSubstitur) ||
                                                                        (valorNotaSubstitutiva > faixaNotaMaiorSubstitur)) {
                                                                        //UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota" + i);
                                                                        historicoVO.setErroNotaSubstitutiva("Valor informado para substitutiva " + tituloNotaSubstitutiva+ " está fora do intervalo "
                                                                                + "permitido para a nota a ser substituída " + tituloNotaSubstituir + ". Intervalo permitido para esta nota: " + faixaNotaMenorSubstitur + "-" + faixaNotaMaiorSubstitur);
                                                                        return 0.0;
                                                                    }    
                                                                }
								return valorNotaSubstitutiva;
							}
						}
						continue;
					}
					if (PoliticaNotaSubstitutivaEnum.valueOf(politicaSubstituicao) == PoliticaNotaSubstitutivaEnum.SUBSTITUIR_NOTAS_EM_BRANCO) {
						if (valorNotaSubstituir == null) {
							return valorNotaSubstitutiva;
						}
					}
				}
			}
		}
		return null;
	}

        /**
         * Método responsável por substituir na formula de calculo da nota, possiveis 
         * variaveis que são 
         * @param historicoVO
         * @param formulaCalculoNota
         * @return
         * @throws Exception 
         */
	public String substituirVariaveisNotaFormaCalculoNota(HistoricoVO historicoVO, String formulaCalculoNota)  {
            for (VariaveisNotaEnum variavel : VariaveisNotaEnum.values()) {
                if (formulaCalculoNota.contains(variavel.getValor())) {
                    if (variavel.equals(VariaveisNotaEnum.ADVERB1)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrAdvertencias1Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.ADVERB2)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrAdvertencias2Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.ADVERB3)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrAdvertencias3Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.ADVERB4)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrAdvertencias4Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.ADVERPER)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrAdvertenciasPeriodoLetivo()));
                    }
                    
                    
                    if (variavel.equals(VariaveisNotaEnum.SUSPENB1)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrSuspensoes1Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.SUSPENB2)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrSuspensoes2Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.SUSPENB3)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrSuspensoes3Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.SUSPENB4)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrSuspensoes4Bimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.SUSPENPER)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getNrSuspensoesPeriodoLetivo()));
                    }
                    
                    if (variavel.equals(VariaveisNotaEnum.FALTASB1)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getFaltaPrimeiroBimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.FALTASB2)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getFaltaSegundoBimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.FALTASB3)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getFaltaTerceiroBimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.FALTASB4)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getFaltaQuartoBimestre()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.FALTASPER)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getTotalFalta()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.FREQUENCIA)) {
                    	formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getFreguencia()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.COMPGERAL)) {
                    	formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", historicoVO.getHistoricoDisciplinaFazParteComposicao()?"true":"false");
                    }
                    if (variavel.equals(VariaveisNotaEnum.COMPFORMULA)) {
                    	formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", historicoVO.getHistoricoDisciplinaFazParteComposicao() && historicoVO.getGradeDisciplinaComposta().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO)?"true":"false");
                    }
                    if (variavel.equals(VariaveisNotaEnum.COMPMEDIA)) {
                    	formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", historicoVO.getHistoricoDisciplinaFazParteComposicao() && historicoVO.getGradeDisciplinaComposta().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.MEDIA)?"true":"false");
                    }
                    if (variavel.equals(VariaveisNotaEnum.COMPSOMA)) {
                    	formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", historicoVO.getHistoricoDisciplinaFazParteComposicao() && historicoVO.getGradeDisciplinaComposta().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.SOMATORIO)?"true":"false");
                    }
                    if (variavel.equals(VariaveisNotaEnum.NR_CH)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getCargaHorariaDisciplina()));
                    }
                    if (variavel.equals(VariaveisNotaEnum.NR_CRED)) {
                        formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + variavel.getValor() + "\\b", String.valueOf(historicoVO.getCreditoDisciplina()));
                    }
                }
            }
            return formulaCalculoNota;
        }
        
	public String substituirSimbolosFormaCalculoNota(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, String formulaCalculoNota, int indice, String tituloNota) throws Exception {    
		formulaCalculoNota = realizarSubstituicaoFuncaoComposicao(historicoVO, historicoFilhaComposicaoVOs, formulaCalculoNota, indice, false);
		formulaCalculoNota = substituirVariaveisPorValor(historicoVO, historicoFilhaComposicaoVOs, formulaCalculoNota);
		historicoVO.getLogCalculoNota().append("NOTA ").append(indice).append(" (").append(tituloNota).append("): ").append("FORMULA DE CALCULO SUBSTITUIDA - ").append(formulaCalculoNota).append("\n");
		realizarCalculoNotaFinal(historicoVO, formulaCalculoNota, indice, tituloNota);
		return formulaCalculoNota;
	}
	
	public String substituirVariaveisPorValor(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, String formulaCalculoNota) throws Exception{
       formulaCalculoNota = substituirVariaveisNotaFormaCalculoNota(historicoVO, formulaCalculoNota);
       for (int i = 1; i <= 40; i++) {
    	   	String tituloNota = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i);
			if (!tituloNota.equals("")) {
				if (formulaCalculoNota.contains(tituloNota)) {
					Double valorNotaLancado = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
					Double notaSubstitutiva = verificarExisteEDeveSerAplicadaNotaSubstitutiva(historicoVO, historicoFilhaComposicaoVOs, i);
					if (notaSubstitutiva != null) {
						// Se entrar aqui é por que foi encontrada uma nota
						// substitutiva para esta nota (indice)
						// que deve ser aplicada. Portanto, o valor a ser
						// utilizado no cálculo não é o valor da nota
						// lançada para esta nota, mas sim o valor da
						// substitutiva.
						valorNotaLancado = notaSubstitutiva;
					}
					// if (valorNotaLancado == null) {
					// UtilReflexao.invocarMetodoSetParametroNull(historicoVO,
					// "nota" + indice);
					// if (!getConsiderarCampoNuloNotaZerada() ||
					// (getConsiderarCampoNuloNotaZerada() &&
					// !verificarUsoNota(historicoVO, i))) {
					// return formulaCalculoNota;
					// } else {
					// formulaCalculoNota = formulaCalculoNota.replaceAll("\\b"
					// + tituloNota + "\\b", String.valueOf(0.0));
					// }
					// }
					if (valorNotaLancado == null && getConsiderarCampoNuloNotaZerada() && verificarUsoNota(historicoVO, historicoFilhaComposicaoVOs, i)) {
						valorNotaLancado = 0.0;
					}
					// UtilReflexao.invocarMetodo(this, "setFormulaCalculoNota"
					// + indice, formulaCalculoNota.replace((String)
					// UtilReflexao.invocarMetodoGet(this, "tituloNota" + i),
					// String.valueOf(UtilReflexao.invocarMetodoGet(historicoVO,
					// "nota" + i))));
					formulaCalculoNota = formulaCalculoNota.replaceAll("\\b" + tituloNota + "\\b", String.valueOf(valorNotaLancado));
					// formulaCalculoNota = (String)
					// UtilReflexao.invocarMetodoGet(this, "formulaCalculoNota"
					// + indice);
				}
			}
		}
		
		return formulaCalculoNota;
	}
	
	public void realizarCalculoNotaFinal(HistoricoVO historicoVO, String formulaCalculoNota, int indice, String tituloNota) throws Exception{
		Double notaFinal = verificarAprovacaoAluno(formulaCalculoNota);
		Double mediaFinal = realizarCalculoArredondamentoMediaFinal(notaFinal, indice);
		ConfiguracaoAcademicoNotaConceitoVO mediaFinalConceito = null;
		Object configuracaoAcademicaNotaVO = UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "configuracaoAcademicaNota"+indice+"VO");
		if (mediaFinal != null && (Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indice + "PorConceito")) {
			List<ConfiguracaoAcademicoNotaConceitoVO> listaConceitoUtilizar = (List<ConfiguracaoAcademicoNotaConceitoVO>) UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicoNota" + indice + "ConceitoVOs");
			for (ConfiguracaoAcademicoNotaConceitoVO obj : listaConceitoUtilizar) {
				if (mediaFinal >= obj.getFaixaNota1() && mediaFinal <= obj.getFaixaNota2()) {
					UtilReflexao.invocarMetodo(historicoVO, "setNota" + indice + "Conceito", obj.clone());
					historicoVO.getLogCalculoNota().append("NOTA ").append(indice).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE CALCULO NOTA CONCEITO - ").append(obj.getAbreviaturaConceitoNota()).append("\n");
					if(configuracaoAcademicaNotaVO != null && configuracaoAcademicaNotaVO instanceof ConfiguracaoAcademicaNotaVO) {
						((ConfiguracaoAcademicaNotaVO)configuracaoAcademicaNotaVO).setNotaConceitoSelecionado(obj);
					}
					mediaFinalConceito = obj.clone();
					break;
				}
			}
		}
		if(mediaFinal != null){
			historicoVO.getLogCalculoNota().append("NOTA ").append(indice).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE CALCULO - ").append(mediaFinal).append("\n");
			UtilReflexao.invocarMetodo(historicoVO, "setNota" + indice, mediaFinal);			
		}else{
			historicoVO.getLogCalculoNota().append("NOTA ").append(indice).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE CALCULO - ").append("\n");
			UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota" + indice);
		}
		if(configuracaoAcademicaNotaVO != null && configuracaoAcademicaNotaVO instanceof ConfiguracaoAcademicaNotaVO) {
			((ConfiguracaoAcademicaNotaVO)configuracaoAcademicaNotaVO).setNotaDigitada(mediaFinal);
		}
		if ((Boolean) UtilReflexao.invocarMetodoGet(this, "nota" + indice + "MediaFinal")) {
			historicoVO.setMediaFinal(mediaFinal);
			if(mediaFinal != null) {
				historicoVO.getLogCalculoNota().append("MEDIA FINAL ").append(": ").append(mediaFinal).append("\n");
			}else {
				historicoVO.getLogCalculoNota().append("MEDIA FINAL ").append(": ").append("\n");
			}
			if (mediaFinalConceito != null) {
				historicoVO.setMediaFinalConceito(mediaFinalConceito);
				historicoVO.setUtilizaNotaFinalConceito(true);
				historicoVO.setNotaFinalConceito(mediaFinalConceito.getAbreviaturaConceitoNota());
				historicoVO.getLogCalculoNota().append("MEDIA FINAL CONCEITO").append(": ").append(mediaFinalConceito.getAbreviaturaConceitoNota()).append("\n");
			}

		}
	}

	public Boolean verificarUsoNota(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, int indice) throws Exception{
		String formulaUsoNota = null;
		if (!UtilReflexao.invocarMetodoGet(this, "formulaUsoNota" + indice).equals("")) {
			formulaUsoNota = "";
			formulaUsoNota = substituirSimbolosNaFormulaUsoNota(historicoVO, historicoFilhaComposicaoVOs, (String) UtilReflexao.invocarMetodoGet(this, "formulaUsoNota" + indice), indice);
			try {
				if (!verificarSituacaoAluno(formulaUsoNota)) {
					return Boolean.FALSE;
				}
			} catch (NumberFormatException e) {
				e.getMessage();
			}
		}
		return Boolean.TRUE;
	}

	public Double realizarCalculoArredondamentoMediaFinal(Double notaFinal, int numeroNota) throws Exception {
		if(notaFinal == null){
			return notaFinal;
		}		
		/**
		 * Adicionada regra para arredondamento de casas decimais de nota, tal
		 * regra será adicionada na configuração academica de cada nota seguindo
		 * o padrão VALOR INICIO-VALOR FIM:VALOR SUBSTITUIR; Ex.: 00-24:00, todo
		 * valor decimal de nota que estiver entre 00 e 24 será arredondado para
		 * 00.
		 */
		if(getQuantidadeCasasDecimaisPermitirAposVirgula() > 4){
			notaFinal = Uteis.arredondarDecimal(notaFinal, getQuantidadeCasasDecimaisPermitirAposVirgula());
		}else{
			notaFinal = Uteis.arrendondarForcando4CadasDecimais(notaFinal);
		}
		String regraArredondamentoNota = (String) UtilReflexao.invocarMetodoGet(this, "regraArredondamentoNota" + numeroNota);
		if (!getNotasDeCincoEmCincoDecimosApenasMedia() && !getUtilizarArredondamentoMediaParaMais() && !regraArredondamentoNota.trim().isEmpty()) {
			return this.executarArredondamentoNota(regraArredondamentoNota, notaFinal);
		}
		if (getUtilizarArredondamentoMediaParaMais()) {
			if (notaFinal != null) {
				return Uteis.arredondarMultiploDeCincoParaCima(notaFinal);
			} else {
				return null;
			}
		}
//		Double nota = null;
		Double mediaFinal = null;
		if (notaFinal != null) {
//			nota = Uteis.arredondar(notaFinal * 10, 0, 0);
//			mediaFinal = nota / 10;
			mediaFinal = notaFinal;
		}
		if (mediaFinal != null && (getNotasDeCincoEmCincoDecimos() || getNotasDeCincoEmCincoDecimosApenasMedia())) {
			mediaFinal = (Math.round(2 * mediaFinal) / 2.0);
		}
		if (mediaFinal != null && mediaFinal > (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + numeroNota + "Maior")) {
			mediaFinal = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + numeroNota + "Maior");
		}
		if (mediaFinal != null && mediaFinal < (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + numeroNota + "Menor")) {
			mediaFinal = (Double) UtilReflexao.invocarMetodoGet(this, "faixaNota" + numeroNota + "Menor");
		}
		return Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(mediaFinal, getQuantidadeCasasDecimaisPermitirAposVirgula());
	}

	public String substituirSimbolosNaFormulaUsoNota(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, String formulaUsoNota, int indice) throws Exception {
		formulaUsoNota = realizarSubstituicaoFuncaoComposicao(historicoVO, historicoFilhaComposicaoVOs, formulaUsoNota, indice, true);
		formulaUsoNota = substituirVariaveisNotaFormaCalculoNota(historicoVO, formulaUsoNota);
		for (int i = 1; i <= 40; i++) {
			if (!UtilReflexao.invocarMetodoGet(this, "tituloNota" + i).equals("")) {
				if (formulaUsoNota.contains((String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i))) {
					if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i) == null && getConsiderarCampoNuloNotaZerada() && i != indice && verificarUsoNota(historicoVO, historicoFilhaComposicaoVOs, i)) {
						formulaUsoNota = formulaUsoNota.replace((String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i), "0.0");
					} else {
						// UtilReflexao.invocarMetodo(this, "setFormulaUsoNota"
						// +
						// indice, formulaUsoNota.replace((String)
						// UtilReflexao.invocarMetodoGet(this, "tituloNota" +
						// i),
						// String.valueOf(UtilReflexao.invocarMetodoGet(historicoVO,
						// "nota" + i))));
						
						if (historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula().equals(0)) {
							Double nota = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
							formulaUsoNota = formulaUsoNota.replace((String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i), nota == null ? "null" : nota.toString());
						} else {
							formulaUsoNota = formulaUsoNota.replace((String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + i), 
									Uteis.getValorTruncadoDeDoubleParaString((Double)UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
						}
						
						// formulaUsoNota = (String)
						// UtilReflexao.invocarMetodoGet(this, "formulaUsoNota"
						// +
						// indice);
					}
				}
			}
		}
		return formulaUsoNota;
	}

	/**
	 * Método que verifica as notas que são utilizadas pela configuração do
	 * acadêmico, e, usando reflexão, cria uma Lista de Strings com o nome dos
	 * métodos correspondentes com as notas utilizadas.
	 * 
	 * @return listaNotasUtilizadas
	 */
	public List<String> verificarNotasQueSaoUtilizadas(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs) throws Exception {
		List<String> listaNotasUtilizadas = new ArrayList<String>();
		/*
		 * Este foi adicionado porque se as notas eram apagadas e a média já tinha sido calculado então a mesma não apagava mantendo assim a média errada
		 */
		historicoVO.setMediaFinal(null);
		for (int i = 1; i <= 40; i++) {
			Boolean utilizarNota = null;
			Boolean utilizarComoSubstitutiva = null;
			String formulaUsoNota = null;
			String formulaCalculoNota = null;
			String tituloNota = null;
			Boolean retorno = null;
			String nota = null;
			Object notaLancada = null;
			try {
				utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + i);
				utilizarComoSubstitutiva = (Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarComoSubstitutiva" + i);
				formulaUsoNota = (String) UtilReflexao.invocarMetodoGet(this, "formulaUsoNota" + i);
				formulaCalculoNota = (String) UtilReflexao.invocarMetodoGet(this, "formulaCalculoNota" + i);								
				if ((utilizarNota) && (!utilizarComoSubstitutiva)) {
					tituloNota = (String) UtilReflexao.invocarMetodoGet(this, "tituloNotaApresentar" + i);
					// Devemos processar aqui somente as notas que forem ser
					// utilizadas e que não forem do tipo
					// substitutiva. Pois se for uma nota de substituição a mesma é
					// processada separadamente
					// no momento correto de substituir a nota no calculo da mesma,
					// pois neste momento sabemos se
					// o valor a ser utilizado no calculado deve ser o da nota
					// substitutiva
					if(historicoVO.getLogCalculoNota().length() > 1) {
						historicoVO.getLogCalculoNota().append("\n");
						historicoVO.getLogCalculoNota().append("--------------------------------------------------------------------------------------------------------");
						historicoVO.getLogCalculoNota().append("\n");
					}
					if (formulaCalculoNota.equals("")) {
						if (!formulaUsoNota.equals("")) {
							historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE USO - ").append(formulaUsoNota).append("\n");
							formulaUsoNota = substituirSimbolosNaFormulaUsoNota(historicoVO, historicoFilhaComposicaoVOs, formulaUsoNota, i);
							historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE USO SUBSTITUIDA - ").append(formulaUsoNota).append("\n");						
							try {
								retorno = verificarSituacaoAluno(formulaUsoNota);
								historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE USO - ").append(retorno.toString()).append("\n");
								if (!retorno) {
									continue;
								}
							} catch (NumberFormatException e) {
								historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE USO - ").append(e.getMessage()).append("\n");
								e.getMessage();
							}
						}
						if (!formulaUsoNota.equals("") && !historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA)) {
							continue;
						}
						nota = "";
						nota = "nota" + i + "Lancada";
						notaLancada = UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i);
						if(notaLancada == null) {
							historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("\n");
						}else {
							historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append(notaLancada).append("\n");
						}
						listaNotasUtilizadas.add(nota);
						continue;
					}
					if (formulaUsoNota.equals("")) {
						historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE CALCULO - ").append(formulaCalculoNota).append("\n");
						substituirSimbolosFormaCalculoNota(historicoVO, historicoFilhaComposicaoVOs, formulaCalculoNota, i, tituloNota);
					} else {
						historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE USO - ").append(formulaUsoNota).append("\n");
						formulaUsoNota = substituirSimbolosNaFormulaUsoNota(historicoVO, historicoFilhaComposicaoVOs, formulaUsoNota, i);
						historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE USO SUBSTITUIDA - ").append(formulaUsoNota).append("\n");
						retorno = obterSituacaoFormula(formulaUsoNota);
						historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("RESULTADO FORMULA DE USO - ").append(retorno.toString()).append("\n");
						if (retorno) {
							historicoVO.getLogCalculoNota().append("NOTA ").append(i).append(" (").append(tituloNota).append("): ").append("FORMULA DE CALCULO - ").append(formulaCalculoNota).append("\n");
							substituirSimbolosFormaCalculoNota(historicoVO, historicoFilhaComposicaoVOs, formulaCalculoNota, i, tituloNota);
						}
					}
					// Double nota = Uteis.arrendondarForcando2CadasDecimais(new
					// Double(verificarAprovacaoAluno(formulaCalculoNota)));
					// if (nota != null) {
					// UtilReflexao.invocarMetodo(historicoVO, "setNota" + i, nota);
					// UtilReflexao.invocarMetodo(historicoVO, "setNota" + i +
					// "Lancada", true);
					// }
				}
			} finally {
				if (utilizarNota != null) {
					utilizarNota = null;
				}
				if (utilizarComoSubstitutiva != null) {
					utilizarComoSubstitutiva = null;
				}
				if (formulaUsoNota != null) {
					formulaUsoNota = null;
				}
				if (formulaCalculoNota != null) {
					formulaCalculoNota = null;
				}
				if (tituloNota != null) {
					tituloNota = null;
				}
				if (retorno != null) {
					retorno = null;
				}
				if (nota != null) {
					nota = null;
				}
				if (notaLancada != null) {
					notaLancada = null;
				}
			}
		}
		return listaNotasUtilizadas;
	}

	public Boolean verificarHistoricoProvenienteImportacao(HistoricoVO historicoVO) {
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + i + "Lancada")) {
				return false;
			}
		}
		if (historicoVO.getMediaFinal() != null && historicoVO.getMediaFinal() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Método que sobrecarrega o método logo abaixo, para pegar a lista de
	 * strings com os métodos que serão utilizados de acordo com a configuração
	 * acadêmica.
	 * 
	 * @param historicoVO
	 * @param verificarNotasUsadas
	 * @return
	 * @throws Exception
	 */
	public Boolean substituirVariaveisFormulaPorValores(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, Boolean realizarCalculoNotas) throws Exception {
		List<String> listaNotas = null;
		String formulaCalculoMedia = null;
		try {
			historicoVO.setLogCalculoNota(new StringBuilder(""));
			if(realizarCalculoNotas){
				listaNotas = verificarNotasQueSaoUtilizadas(historicoVO, historicoFilhaComposicaoVOs);
				for (String nota : listaNotas) {
					if ((Boolean) UtilReflexao.invocarMetodoGet(historicoVO, nota)) {
						continue;
					}
					// } else {
					// if (!getConsiderarCampoNuloNotaZerada()) {
					// throw new FechamentoPeriodoLetivoException();
					// }
					// }
				}
			}
			
			// return substituirVariaveisFormulaPorValores(historicoVO);
			formulaCalculoMedia = this.getFormulaCalculoMediaFinal().trim();
			// formulaCalculoMedia =
			// alterarVariaveisFormulaCalculoMediaFinalPorValor(historicoVO,
			// formulaCalculoMedia, historicoVO.getNota1(), historicoVO.getNota2(),
			// historicoVO.getNota3(), historicoVO.getNota4(),
			// historicoVO.getNota5(), historicoVO.getNota6(),
			// historicoVO.getNota7(), historicoVO.getNota8(),
			// historicoVO.getNota9(), historicoVO.getNota10());
			formulaCalculoMedia = substituirSimbolosFormulaCalculoMediaFinal(historicoVO, formulaCalculoMedia, null);
			// historicoVO.setMediaFinal(verificarAprovacaoAluno(formulaCalculoMedia));
			if(historicoVO.getConfiguracaoAcademico().getUsarFormulaCalculoFrequencia() && !historicoVO.getConfiguracaoAcademico().getFormulaCalculoFrequencia().trim().isEmpty()) {
				historicoVO.getLogCalculoNota().append("FREQUENCIA: FORMULA CALCULO - ").append(historicoVO.getConfiguracaoAcademico().getFormulaCalculoFrequencia()).append("\n");			
				String formula = substituirSimbolosFormulaCalculoMediaFinal(historicoVO, historicoVO.getConfiguracaoAcademico().getFormulaCalculoFrequencia(), null);
				historicoVO.getLogCalculoNota().append("FREQUENCIA: FORMULA CALCULO SUBSTITUIDA - ").append(formula).append("\n");
				historicoVO.setFreguencia(Uteis.arrendondarForcando2CadasDecimais(Uteis.realizarCalculoFormula(formula)));
				historicoVO.getLogCalculoNota().append("FREQUENCIA: RESULTADO FORMULA CALCULO - ").append(historicoVO.getFreguencia()).append("\n");
				if (historicoVO.getFreguencia() < historicoVO.getConfiguracaoAcademico().getPercentualFrequenciaAprovacao()) {
					historicoVO.setSituacao("RF");
				}else if(historicoVO.getSituacao().equals("RF")) {
					historicoVO.setSituacao("CS");
				}
			}
			return (verificarSituacaoAluno(formulaCalculoMedia));
			// verificarHistoricoProvenienteImportacao(historicoVO);		
		} finally {
			if (formulaCalculoMedia == null) {
				formulaCalculoMedia = null;
			}
			if (listaNotas != null) {
				listaNotas.clear();
				listaNotas = null;
			}
		}
	}

	public boolean substituirVariaveisFormulaPorValores(HistoricoVO historicoVO) throws Exception {
		if (this.getUtilizarNota1().booleanValue()) {
			String formulaNota1 = "";
			String formulaUso1 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota1PorConceito() && getFormulaCalculoNota1().equals("") && historicoVO.getNota1Conceito().getCodigo() > 0) {
				historicoVO.setNota1(historicoVO.getNota1Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota1().equals("")) {
				formulaUso1 = alterarVariaveisFormulaPorValor(getFormulaUsoNota1(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso1)) {
					if (historicoVO.getNota1() == null && getFormulaCalculoNota1().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota1() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota1().equals("")) {
							formulaNota1 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota1(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota1(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota1))));

						}
					}
				}
			}
			if (!getFormulaCalculoNota1().equals("")) {
				formulaNota1 = alterarVariaveisFormulaPorValor(getFormulaUsoNota1(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota1(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota1))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota1PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota1ConceitoVOs()) {
						if (historicoVO.getNota1() >= conf.getFaixaNota1() && historicoVO.getNota1() <= conf.getFaixaNota2()) {
							historicoVO.setNota1Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota2().booleanValue()) {
			String formulaNota2 = "";
			String formulaUso2 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota2PorConceito() && getFormulaCalculoNota2().equals("") && historicoVO.getNota2Conceito().getCodigo() > 0) {
				historicoVO.setNota2(historicoVO.getNota2Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota2().equals("")) {
				formulaUso2 = alterarVariaveisFormulaPorValor(getFormulaUsoNota2(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso2)) {
					if (historicoVO.getNota2() == null && getFormulaCalculoNota2().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota2() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota2().equals("")) {
							formulaNota2 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota2(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota2(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota2))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota2().equals("")) {
				formulaNota2 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota2(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota2(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota2))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota2PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota2ConceitoVOs()) {
						if (historicoVO.getNota2() >= conf.getFaixaNota1() && historicoVO.getNota2() <= conf.getFaixaNota2()) {
							historicoVO.setNota2Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota3().booleanValue()) {
			String formulaNota3 = "";
			String formulaUso3 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota3PorConceito() && getFormulaCalculoNota3().equals("") && historicoVO.getNota3Conceito().getCodigo() > 0) {
				historicoVO.setNota3(historicoVO.getNota3Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota3().equals("")) {
				formulaUso3 = alterarVariaveisFormulaPorValor(getFormulaUsoNota3(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso3)) {
					if (historicoVO.getNota3() == null && getFormulaCalculoNota3().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota3() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota3().equals("")) {
							formulaNota3 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota3(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota3(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota3))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota3().equals("")) {
				formulaNota3 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota3(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota3(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota3))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota3PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota3ConceitoVOs()) {
						if (historicoVO.getNota3() >= conf.getFaixaNota1() && historicoVO.getNota3() <= conf.getFaixaNota2()) {
							historicoVO.setNota3Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota4().booleanValue()) {
			String formulaNota4 = "";
			String formulaUso4 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota4PorConceito() && getFormulaCalculoNota4().equals("") && historicoVO.getNota4Conceito().getCodigo() > 0) {
				historicoVO.setNota4(historicoVO.getNota4Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota4().equals("")) {
				formulaUso4 = alterarVariaveisFormulaPorValor(getFormulaUsoNota4(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso4)) {
					if (historicoVO.getNota4() == null && getFormulaCalculoNota4().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota4() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota4().equals("")) {
							formulaNota4 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota4(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota4(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota4))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota4().equals("")) {
				formulaNota4 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota4(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota4(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota4))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota4PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota4ConceitoVOs()) {
						if (historicoVO.getNota4() >= conf.getFaixaNota1() && historicoVO.getNota4() <= conf.getFaixaNota2()) {
							historicoVO.setNota4Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota5().booleanValue()) {
			String formulaNota5 = "";
			String formulaUso5 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota5PorConceito() && getFormulaCalculoNota5().equals("") && historicoVO.getNota5Conceito().getCodigo() > 0) {
				historicoVO.setNota5(historicoVO.getNota5Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota5().equals("")) {
				formulaUso5 = alterarVariaveisFormulaPorValor(getFormulaUsoNota5(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso5)) {
					if (historicoVO.getNota5() == null && getFormulaCalculoNota5().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota5() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota5().equals("")) {
							formulaNota5 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota5(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota5(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota5))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota5().equals("")) {
				formulaNota5 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota5(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota5(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota5))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota5PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota5ConceitoVOs()) {
						if (historicoVO.getNota5() >= conf.getFaixaNota1() && historicoVO.getNota5() <= conf.getFaixaNota2()) {
							historicoVO.setNota5Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota6().booleanValue()) {
			String formulaNota6 = "";
			String formulaUso6 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota6PorConceito() && getFormulaCalculoNota6().equals("") && historicoVO.getNota6Conceito().getCodigo() > 0) {
				historicoVO.setNota6(historicoVO.getNota6Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota6().equals("")) {
				formulaUso6 = alterarVariaveisFormulaPorValor(getFormulaUsoNota6(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso6)) {
					if (historicoVO.getNota6() == null && getFormulaCalculoNota6().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota6() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota6().equals("")) {
							formulaNota6 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota6(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota6(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota6))));
						}
					}
				}
			} else {
				if (!getFormulaCalculoNota6().equals("")) {
					formulaNota6 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota6(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
					historicoVO.setNota6(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota6))));
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota6PorConceito()) {
						for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota6ConceitoVOs()) {
							if (historicoVO.getNota6() >= conf.getFaixaNota1() && historicoVO.getNota6() <= conf.getFaixaNota2()) {
								historicoVO.setNota6Conceito(conf);
								break;
							}
						}
					}
				}
			}
		}
		if (this.getUtilizarNota7().booleanValue()) {
			String formulaNota7 = "";
			String formulaUso7 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota7PorConceito() && getFormulaCalculoNota7().equals("") && historicoVO.getNota7Conceito().getCodigo() > 0) {
				historicoVO.setNota7(historicoVO.getNota7Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota7().equals("")) {
				formulaUso7 = alterarVariaveisFormulaPorValor(getFormulaUsoNota7(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso7)) {
					if (historicoVO.getNota7() == null && getFormulaCalculoNota7().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota7() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota7().equals("")) {
							formulaNota7 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota7(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota7(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota7))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota7().equals("")) {
				formulaNota7 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota7(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota7(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota7))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota7PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota7ConceitoVOs()) {
						if (historicoVO.getNota7() >= conf.getFaixaNota1() && historicoVO.getNota7() <= conf.getFaixaNota2()) {
							historicoVO.setNota7Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota8().booleanValue()) {
			String formulaNota8 = "";
			String formulaUso8 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota8PorConceito() && getFormulaCalculoNota8().equals("") && historicoVO.getNota8Conceito().getCodigo() > 0) {
				historicoVO.setNota8(historicoVO.getNota8Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota8().equals("")) {
				formulaUso8 = alterarVariaveisFormulaPorValor(getFormulaUsoNota8(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso8)) {
					if (historicoVO.getNota8() == null && getFormulaCalculoNota8().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota8() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota8().equals("")) {
							formulaNota8 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota8(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota8(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota8))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota8().equals("")) {
				formulaNota8 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota8(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota8(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota8))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota8PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota8ConceitoVOs()) {
						if (historicoVO.getNota8() >= conf.getFaixaNota1() && historicoVO.getNota8() <= conf.getFaixaNota2()) {
							historicoVO.setNota8Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota9().booleanValue()) {
			String formulaNota9 = "";
			String formulaUso9 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota9PorConceito() && getFormulaCalculoNota9().equals("") && historicoVO.getNota9Conceito().getCodigo() > 0) {
				historicoVO.setNota9(historicoVO.getNota9Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota9().equals("")) {
				formulaUso9 = alterarVariaveisFormulaPorValor(getFormulaUsoNota9(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso9)) {
					if (historicoVO.getNota9() == null && getFormulaCalculoNota9().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota9() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota9().equals("")) {
							formulaNota9 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota9(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota9(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota9))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota9().equals("")) {
				formulaNota9 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota9(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota9(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota9))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota9PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota9ConceitoVOs()) {
						if (historicoVO.getNota9() >= conf.getFaixaNota1() && historicoVO.getNota9() <= conf.getFaixaNota2()) {
							historicoVO.setNota9Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota10().booleanValue()) {
			String formulaNota10 = "";
			String formulaUso10 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota10PorConceito() && getFormulaCalculoNota10().equals("") && historicoVO.getNota10Conceito().getCodigo() > 0) {
				historicoVO.setNota10(historicoVO.getNota10Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota10().equals("")) {
				formulaUso10 = alterarVariaveisFormulaPorValor(getFormulaUsoNota10(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso10)) {
					if (historicoVO.getNota10() == null && getFormulaCalculoNota10().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota10() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota10().equals("")) {
							formulaNota10 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota10(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota10(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota10))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota10().equals("")) {
				formulaNota10 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota10(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota10(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota10))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota10PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota10ConceitoVOs()) {
						if (historicoVO.getNota10() >= conf.getFaixaNota1() && historicoVO.getNota10() <= conf.getFaixaNota2()) {
							historicoVO.setNota10Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota11().booleanValue()) {
			String formulaNota11 = "";
			String formulaUso11 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota11PorConceito() && getFormulaCalculoNota11().equals("") && historicoVO.getNota11Conceito().getCodigo() > 0) {
				historicoVO.setNota11(historicoVO.getNota11Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota11().equals("")) {
				formulaUso11 = alterarVariaveisFormulaPorValor(getFormulaUsoNota11(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso11)) {
					if (historicoVO.getNota11() == null && getFormulaCalculoNota11().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota11() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota11().equals("")) {
							formulaNota11 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota11(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota11(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota11))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota11().equals("")) {
				formulaNota11 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota11(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota11(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota11))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota11PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota11ConceitoVOs()) {
						if (historicoVO.getNota11() >= conf.getFaixaNota1() && historicoVO.getNota11() <= conf.getFaixaNota2()) {
							historicoVO.setNota11Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota12().booleanValue()) {
			String formulaNota12 = "";
			String formulaUso12 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota12PorConceito() && getFormulaCalculoNota12().equals("") && historicoVO.getNota12Conceito().getCodigo() > 0) {
				historicoVO.setNota12(historicoVO.getNota12Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota12().equals("")) {
				formulaUso12 = alterarVariaveisFormulaPorValor(getFormulaUsoNota12(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso12)) {
					if (historicoVO.getNota12() == null && getFormulaCalculoNota12().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota12() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota12().equals("")) {
							formulaNota12 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota12(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota12(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota12))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota12().equals("")) {
				formulaNota12 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota12(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota12(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota12))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota12PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota12ConceitoVOs()) {
						if (historicoVO.getNota12() >= conf.getFaixaNota1() && historicoVO.getNota12() <= conf.getFaixaNota2()) {
							historicoVO.setNota12Conceito(conf);
							break;
						}
					}
				}
			}
		}
		if (this.getUtilizarNota13().booleanValue()) {
			String formulaNota13 = "";
			String formulaUso13 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota13PorConceito() && getFormulaCalculoNota13().equals("") && historicoVO.getNota13Conceito().getCodigo() > 0) {
				historicoVO.setNota13(historicoVO.getNota13Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota13().equals("")) {
				formulaUso13 = alterarVariaveisFormulaPorValor(getFormulaUsoNota13(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso13)) {
					if (historicoVO.getNota13() == null && getFormulaCalculoNota13().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota13() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota13().equals("")) {
							formulaNota13 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota13(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota13(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota13))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota13().equals("")) {
				formulaNota13 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota13(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota13(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota13))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota13PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota13ConceitoVOs()) {
						if (historicoVO.getNota13() >= conf.getFaixaNota1() && historicoVO.getNota13() <= conf.getFaixaNota2()) {
							historicoVO.setNota13Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 14
		if (this.getUtilizarNota14().booleanValue()) {
			String formulaNota14 = "";
			String formulaUso14 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota14PorConceito() && getFormulaCalculoNota14().equals("") && historicoVO.getNota14Conceito().getCodigo() > 0) {
				historicoVO.setNota14(historicoVO.getNota14Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota14().equals("")) {
				formulaUso14 = alterarVariaveisFormulaPorValor(getFormulaUsoNota14(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso14)) {
					if (historicoVO.getNota14() == null && getFormulaCalculoNota14().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota14() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota14().equals("")) {
							formulaNota14 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota14(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota14(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota14))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota14().equals("")) {
				formulaNota14 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota14(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota14(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota14))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota14PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota14ConceitoVOs()) {
						if (historicoVO.getNota14() >= conf.getFaixaNota1() && historicoVO.getNota14() <= conf.getFaixaNota2()) {
							historicoVO.setNota14Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 15
		if (this.getUtilizarNota15().booleanValue()) {
			String formulaNota15 = "";
			String formulaUso15 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota15PorConceito() && getFormulaCalculoNota15().equals("") && historicoVO.getNota15Conceito().getCodigo() > 0) {
				historicoVO.setNota15(historicoVO.getNota15Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota15().equals("")) {
				formulaUso15 = alterarVariaveisFormulaPorValor(getFormulaUsoNota15(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso15)) {
					if (historicoVO.getNota15() == null && getFormulaCalculoNota15().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota15() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota15().equals("")) {
							formulaNota15 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota15(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota15(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota15))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota15().equals("")) {
				formulaNota15 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota15(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota15(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota15))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota15PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota15ConceitoVOs()) {
						if (historicoVO.getNota15() >= conf.getFaixaNota1() && historicoVO.getNota15() <= conf.getFaixaNota2()) {
							historicoVO.setNota15Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 16
		if (this.getUtilizarNota16().booleanValue()) {
			String formulaNota16 = "";
			String formulaUso16 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota16PorConceito() && getFormulaCalculoNota16().equals("") && historicoVO.getNota16Conceito().getCodigo() > 0) {
				historicoVO.setNota16(historicoVO.getNota16Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota16().equals("")) {
				formulaUso16 = alterarVariaveisFormulaPorValor(getFormulaUsoNota16(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso16)) {
					if (historicoVO.getNota16() == null && getFormulaCalculoNota16().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota16() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota16().equals("")) {
							formulaNota16 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota16(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota16(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota16))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota16().equals("")) {
				formulaNota16 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota16(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota16(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota16))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota16PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota16ConceitoVOs()) {
						if (historicoVO.getNota16() >= conf.getFaixaNota1() && historicoVO.getNota16() <= conf.getFaixaNota2()) {
							historicoVO.setNota16Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 17
		if (this.getUtilizarNota17().booleanValue()) {
			String formulaNota17 = "";
			String formulaUso17 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota17PorConceito() && getFormulaCalculoNota17().equals("") && historicoVO.getNota17Conceito().getCodigo() > 0) {
				historicoVO.setNota17(historicoVO.getNota17Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota17().equals("")) {
				formulaUso17 = alterarVariaveisFormulaPorValor(getFormulaUsoNota17(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso17)) {
					if (historicoVO.getNota17() == null && getFormulaCalculoNota17().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota17() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota17().equals("")) {
							formulaNota17 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota17(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota17(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota17))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota17().equals("")) {
				formulaNota17 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota17(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota17(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota17))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota17PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota17ConceitoVOs()) {
						if (historicoVO.getNota17() >= conf.getFaixaNota1() && historicoVO.getNota17() <= conf.getFaixaNota2()) {
							historicoVO.setNota17Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 18
		if (this.getUtilizarNota18().booleanValue()) {
			String formulaNota18 = "";
			String formulaUso18 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota18PorConceito() && getFormulaCalculoNota18().equals("") && historicoVO.getNota18Conceito().getCodigo() > 0) {
				historicoVO.setNota18(historicoVO.getNota18Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota18().equals("")) {
				formulaUso18 = alterarVariaveisFormulaPorValor(getFormulaUsoNota18(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso18)) {
					if (historicoVO.getNota18() == null && getFormulaCalculoNota18().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota18() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota18().equals("")) {
							formulaNota18 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota18(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota18(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota18))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota18().equals("")) {
				formulaNota18 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota18(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota18(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota18))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota18PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota18ConceitoVOs()) {
						if (historicoVO.getNota18() >= conf.getFaixaNota1() && historicoVO.getNota18() <= conf.getFaixaNota2()) {
							historicoVO.setNota18Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 19
		if (this.getUtilizarNota19().booleanValue()) {
			String formulaNota19 = "";
			String formulaUso19 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota19PorConceito() && getFormulaCalculoNota19().equals("") && historicoVO.getNota19Conceito().getCodigo() > 0) {
				historicoVO.setNota19(historicoVO.getNota19Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota19().equals("")) {
				formulaUso19 = alterarVariaveisFormulaPorValor(getFormulaUsoNota19(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso19)) {
					if (historicoVO.getNota19() == null && getFormulaCalculoNota19().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota19() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota19().equals("")) {
							formulaNota19 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota19(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota19(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota19))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota19().equals("")) {
				formulaNota19 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota19(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota19(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota19))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota19PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota19ConceitoVOs()) {
						if (historicoVO.getNota19() >= conf.getFaixaNota1() && historicoVO.getNota19() <= conf.getFaixaNota2()) {
							historicoVO.setNota19Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 20
		if (this.getUtilizarNota20().booleanValue()) {
			String formulaNota20 = "";
			String formulaUso20 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota20PorConceito() && getFormulaCalculoNota20().equals("") && historicoVO.getNota20Conceito().getCodigo() > 0) {
				historicoVO.setNota20(historicoVO.getNota20Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota20().equals("")) {
				formulaUso20 = alterarVariaveisFormulaPorValor(getFormulaUsoNota20(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso20)) {
					if (historicoVO.getNota20() == null && getFormulaCalculoNota20().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota20() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota20().equals("")) {
							formulaNota20 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota20(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota20(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota20))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota20().equals("")) {
				formulaNota20 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota20(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota20(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota20))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota20PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota20ConceitoVOs()) {
						if (historicoVO.getNota20() >= conf.getFaixaNota1() && historicoVO.getNota20() <= conf.getFaixaNota2()) {
							historicoVO.setNota20Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 21
		if (this.getUtilizarNota21().booleanValue()) {
			String formulaNota21 = "";
			String formulaUso21 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota21PorConceito() && getFormulaCalculoNota21().equals("") && historicoVO.getNota21Conceito().getCodigo() > 0) {
				historicoVO.setNota21(historicoVO.getNota21Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota21().equals("")) {
				formulaUso21 = alterarVariaveisFormulaPorValor(getFormulaUsoNota21(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso21)) {
					if (historicoVO.getNota21() == null && getFormulaCalculoNota21().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota21() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota21().equals("")) {
							formulaNota21 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota21(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota21(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota21))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota21().equals("")) {
				formulaNota21 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota21(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota21(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota21))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota21PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota21ConceitoVOs()) {
						if (historicoVO.getNota21() >= conf.getFaixaNota1() && historicoVO.getNota21() <= conf.getFaixaNota2()) {
							historicoVO.setNota21Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 22
		if (this.getUtilizarNota22().booleanValue()) {
			String formulaNota22 = "";
			String formulaUso22 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota22PorConceito() && getFormulaCalculoNota22().equals("") && historicoVO.getNota22Conceito().getCodigo() > 0) {
				historicoVO.setNota22(historicoVO.getNota22Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota22().equals("")) {
				formulaUso22 = alterarVariaveisFormulaPorValor(getFormulaUsoNota22(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso22)) {
					if (historicoVO.getNota22() == null && getFormulaCalculoNota22().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota22() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota22().equals("")) {
							formulaNota22 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota22(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota22(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota22))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota22().equals("")) {
				formulaNota22 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota22(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota22(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota22))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota22PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota22ConceitoVOs()) {
						if (historicoVO.getNota22() >= conf.getFaixaNota1() && historicoVO.getNota22() <= conf.getFaixaNota2()) {
							historicoVO.setNota22Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 23
		if (this.getUtilizarNota23().booleanValue()) {
			String formulaNota23 = "";
			String formulaUso23 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota23PorConceito() && getFormulaCalculoNota23().equals("") && historicoVO.getNota23Conceito().getCodigo() > 0) {
				historicoVO.setNota23(historicoVO.getNota23Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota23().equals("")) {
				formulaUso23 = alterarVariaveisFormulaPorValor(getFormulaUsoNota23(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso23)) {
					if (historicoVO.getNota23() == null && getFormulaCalculoNota23().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota23() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota23().equals("")) {
							formulaNota23 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota23(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota23(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota23))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota23().equals("")) {
				formulaNota23 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota23(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota23(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota23))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota23PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota23ConceitoVOs()) {
						if (historicoVO.getNota23() >= conf.getFaixaNota1() && historicoVO.getNota23() <= conf.getFaixaNota2()) {
							historicoVO.setNota23Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 24
		if (this.getUtilizarNota24().booleanValue()) {
			String formulaNota24 = "";
			String formulaUso24 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota24PorConceito() && getFormulaCalculoNota24().equals("") && historicoVO.getNota24Conceito().getCodigo() > 0) {
				historicoVO.setNota24(historicoVO.getNota24Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota24().equals("")) {
				formulaUso24 = alterarVariaveisFormulaPorValor(getFormulaUsoNota24(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso24)) {
					if (historicoVO.getNota24() == null && getFormulaCalculoNota24().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota24() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota24().equals("")) {
							formulaNota24 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota24(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota24(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota24))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota24().equals("")) {
				formulaNota24 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota24(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota24(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota24))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota24PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota24ConceitoVOs()) {
						if (historicoVO.getNota24() >= conf.getFaixaNota1() && historicoVO.getNota24() <= conf.getFaixaNota2()) {
							historicoVO.setNota24Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 25
		if (this.getUtilizarNota25().booleanValue()) {
			String formulaNota25 = "";
			String formulaUso25 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota25PorConceito() && getFormulaCalculoNota25().equals("") && historicoVO.getNota25Conceito().getCodigo() > 0) {
				historicoVO.setNota25(historicoVO.getNota25Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota25().equals("")) {
				formulaUso25 = alterarVariaveisFormulaPorValor(getFormulaUsoNota25(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso25)) {
					if (historicoVO.getNota25() == null && getFormulaCalculoNota25().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota25() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota25().equals("")) {
							formulaNota25 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota25(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota25(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota25))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota25().equals("")) {
				formulaNota25 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota25(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota25(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota25))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota25PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota25ConceitoVOs()) {
						if (historicoVO.getNota25() >= conf.getFaixaNota1() && historicoVO.getNota25() <= conf.getFaixaNota2()) {
							historicoVO.setNota25Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 26
		if (this.getUtilizarNota26().booleanValue()) {
			String formulaNota26 = "";
			String formulaUso26 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota26PorConceito() && getFormulaCalculoNota26().equals("") && historicoVO.getNota26Conceito().getCodigo() > 0) {
				historicoVO.setNota26(historicoVO.getNota26Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota26().equals("")) {
				formulaUso26 = alterarVariaveisFormulaPorValor(getFormulaUsoNota26(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso26)) {
					if (historicoVO.getNota26() == null && getFormulaCalculoNota26().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota26() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota26().equals("")) {
							formulaNota26 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota26(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota26(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota26))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota26().equals("")) {
				formulaNota26 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota26(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota26(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota26))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota26PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota26ConceitoVOs()) {
						if (historicoVO.getNota26() >= conf.getFaixaNota1() && historicoVO.getNota26() <= conf.getFaixaNota2()) {
							historicoVO.setNota26Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 27
		if (this.getUtilizarNota27().booleanValue()) {
			String formulaNota27 = "";
			String formulaUso27 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota27PorConceito() && getFormulaCalculoNota27().equals("") && historicoVO.getNota27Conceito().getCodigo() > 0) {
				historicoVO.setNota27(historicoVO.getNota27Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota27().equals("")) {
				formulaUso27 = alterarVariaveisFormulaPorValor(getFormulaUsoNota27(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso27)) {
					if (historicoVO.getNota27() == null && getFormulaCalculoNota27().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota27() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota27().equals("")) {
							formulaNota27 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota27(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota27(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota27))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota27().equals("")) {
				formulaNota27 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota27(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota27(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota27))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota27PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota27ConceitoVOs()) {
						if (historicoVO.getNota27() >= conf.getFaixaNota1() && historicoVO.getNota27() <= conf.getFaixaNota2()) {
							historicoVO.setNota27Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 28
		if (this.getUtilizarNota28().booleanValue()) {
			String formulaNota28 = "";
			String formulaUso28 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota28PorConceito() && getFormulaCalculoNota28().equals("") && historicoVO.getNota28Conceito().getCodigo() > 0) {
				historicoVO.setNota28(historicoVO.getNota28Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota28().equals("")) {
				formulaUso28 = alterarVariaveisFormulaPorValor(getFormulaUsoNota28(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso28)) {
					if (historicoVO.getNota28() == null && getFormulaCalculoNota28().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota28() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota28().equals("")) {
							formulaNota28 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota28(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota28(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota28))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota28().equals("")) {
				formulaNota28 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota28(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota28(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota28))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota28PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota28ConceitoVOs()) {
						if (historicoVO.getNota28() >= conf.getFaixaNota1() && historicoVO.getNota28() <= conf.getFaixaNota2()) {
							historicoVO.setNota28Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 29
		if (this.getUtilizarNota29().booleanValue()) {
			String formulaNota29 = "";
			String formulaUso29 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota29PorConceito() && getFormulaCalculoNota29().equals("") && historicoVO.getNota29Conceito().getCodigo() > 0) {
				historicoVO.setNota29(historicoVO.getNota29Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota29().equals("")) {
				formulaUso29 = alterarVariaveisFormulaPorValor(getFormulaUsoNota29(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso29)) {
					if (historicoVO.getNota29() == null && getFormulaCalculoNota29().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota29() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota29().equals("")) {
							formulaNota29 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota29(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota29(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota29))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota29().equals("")) {
				formulaNota29 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota29(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota29(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota29))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota29PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota29ConceitoVOs()) {
						if (historicoVO.getNota29() >= conf.getFaixaNota1() && historicoVO.getNota29() <= conf.getFaixaNota2()) {
							historicoVO.setNota29Conceito(conf);
							break;
						}
					}
				}
			}
		}

		// ********* NOTA 30
		if (this.getUtilizarNota30().booleanValue()) {
			String formulaNota30 = "";
			String formulaUso30 = "";
			if (historicoVO.getConfiguracaoAcademico().getUtilizarNota30PorConceito() && getFormulaCalculoNota30().equals("") && historicoVO.getNota30Conceito().getCodigo() > 0) {
				historicoVO.setNota30(historicoVO.getNota30Conceito().getFaixaNota2());
			}
			if (!getFormulaUsoNota30().equals("")) {
				formulaUso30 = alterarVariaveisFormulaPorValor(getFormulaUsoNota30(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				if (verificarSituacaoAluno(formulaUso30)) {
					if (historicoVO.getNota30() == null && getFormulaCalculoNota30().equals("")) {
						throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota30() + " deve ser informado.");
					} else {
						if (!getFormulaCalculoNota30().equals("")) {
							formulaNota30 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota30(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
							historicoVO.setNota30(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota30))));
						}
					}
				}
			}
			if (!getFormulaCalculoNota30().equals("")) {
				formulaNota30 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota30(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
				historicoVO.setNota30(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota30))));
				if (historicoVO.getConfiguracaoAcademico().getUtilizarNota30PorConceito()) {
					for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota30ConceitoVOs()) {
						if (historicoVO.getNota30() >= conf.getFaixaNota1() && historicoVO.getNota30() <= conf.getFaixaNota2()) {
							historicoVO.setNota30Conceito(conf);
							break;
						}
					}
				}
			}
		}
		
		// ********* NOTA 31
				if (this.getUtilizarNota31().booleanValue()) {
					String formulaNota31 = "";
					String formulaUso31 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota31PorConceito() && getFormulaCalculoNota31().equals("") && historicoVO.getNota31Conceito().getCodigo() > 0) {
						historicoVO.setNota31(historicoVO.getNota31Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota31().equals("")) {
						formulaUso31 = alterarVariaveisFormulaPorValor(getFormulaUsoNota31(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso31)) {
							if (historicoVO.getNota31() == null && getFormulaCalculoNota31().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota31() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota31().equals("")) {
									formulaNota31 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota31(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota31(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota31))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota31().equals("")) {
						formulaNota31 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota31(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota31(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota31))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota31PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota31ConceitoVOs()) {
								if (historicoVO.getNota31() >= conf.getFaixaNota1() && historicoVO.getNota31() <= conf.getFaixaNota2()) {
									historicoVO.setNota31Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 32
				if (this.getUtilizarNota32().booleanValue()) {
					String formulaNota32 = "";
					String formulaUso32 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota32PorConceito() && getFormulaCalculoNota32().equals("") && historicoVO.getNota32Conceito().getCodigo() > 0) {
						historicoVO.setNota32(historicoVO.getNota32Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota32().equals("")) {
						formulaUso32 = alterarVariaveisFormulaPorValor(getFormulaUsoNota32(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso32)) {
							if (historicoVO.getNota32() == null && getFormulaCalculoNota32().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota32() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota32().equals("")) {
									formulaNota32 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota32(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota32(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota32))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota32().equals("")) {
						formulaNota32 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota32(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota32(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota32))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota32PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota32ConceitoVOs()) {
								if (historicoVO.getNota32() >= conf.getFaixaNota1() && historicoVO.getNota32() <= conf.getFaixaNota2()) {
									historicoVO.setNota32Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 33
				if (this.getUtilizarNota33().booleanValue()) {
					String formulaNota33 = "";
					String formulaUso33 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota33PorConceito() && getFormulaCalculoNota33().equals("") && historicoVO.getNota33Conceito().getCodigo() > 0) {
						historicoVO.setNota33(historicoVO.getNota33Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota33().equals("")) {
						formulaUso33 = alterarVariaveisFormulaPorValor(getFormulaUsoNota33(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso33)) {
							if (historicoVO.getNota33() == null && getFormulaCalculoNota33().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota33() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota33().equals("")) {
									formulaNota33 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota33(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota33(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota33))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota33().equals("")) {
						formulaNota33 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota33(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota33(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota33))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota33PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota33ConceitoVOs()) {
								if (historicoVO.getNota33() >= conf.getFaixaNota1() && historicoVO.getNota33() <= conf.getFaixaNota2()) {
									historicoVO.setNota33Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 34
				if (this.getUtilizarNota34().booleanValue()) {
					String formulaNota34 = "";
					String formulaUso34 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota34PorConceito() && getFormulaCalculoNota34().equals("") && historicoVO.getNota34Conceito().getCodigo() > 0) {
						historicoVO.setNota34(historicoVO.getNota34Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota34().equals("")) {
						formulaUso34 = alterarVariaveisFormulaPorValor(getFormulaUsoNota34(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso34)) {
							if (historicoVO.getNota34() == null && getFormulaCalculoNota34().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota34() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota34().equals("")) {
									formulaNota34 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota34(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota34(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota34))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota34().equals("")) {
						formulaNota34 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota34(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota34(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota34))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota34PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota34ConceitoVOs()) {
								if (historicoVO.getNota34() >= conf.getFaixaNota1() && historicoVO.getNota34() <= conf.getFaixaNota2()) {
									historicoVO.setNota34Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 35
				if (this.getUtilizarNota35().booleanValue()) {
					String formulaNota35 = "";
					String formulaUso35 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota35PorConceito() && getFormulaCalculoNota35().equals("") && historicoVO.getNota35Conceito().getCodigo() > 0) {
						historicoVO.setNota35(historicoVO.getNota35Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota35().equals("")) {
						formulaUso35 = alterarVariaveisFormulaPorValor(getFormulaUsoNota35(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso35)) {
							if (historicoVO.getNota35() == null && getFormulaCalculoNota35().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota35() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota35().equals("")) {
									formulaNota35 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota35(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota35(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota35))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota35().equals("")) {
						formulaNota35 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota35(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota35(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota35))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota35PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota35ConceitoVOs()) {
								if (historicoVO.getNota35() >= conf.getFaixaNota1() && historicoVO.getNota35() <= conf.getFaixaNota2()) {
									historicoVO.setNota35Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 36
				if (this.getUtilizarNota36().booleanValue()) {
					String formulaNota36 = "";
					String formulaUso36 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota36PorConceito() && getFormulaCalculoNota36().equals("") && historicoVO.getNota36Conceito().getCodigo() > 0) {
						historicoVO.setNota36(historicoVO.getNota36Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota36().equals("")) {
						formulaUso36 = alterarVariaveisFormulaPorValor(getFormulaUsoNota36(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso36)) {
							if (historicoVO.getNota36() == null && getFormulaCalculoNota36().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota36() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota36().equals("")) {
									formulaNota36 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota36(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota36(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota36))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota36().equals("")) {
						formulaNota36 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota36(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota36(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota36))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota36PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota36ConceitoVOs()) {
								if (historicoVO.getNota36() >= conf.getFaixaNota1() && historicoVO.getNota36() <= conf.getFaixaNota2()) {
									historicoVO.setNota36Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 37
				if (this.getUtilizarNota37().booleanValue()) {
					String formulaNota37 = "";
					String formulaUso37 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota37PorConceito() && getFormulaCalculoNota37().equals("") && historicoVO.getNota37Conceito().getCodigo() > 0) {
						historicoVO.setNota37(historicoVO.getNota37Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota37().equals("")) {
						formulaUso37 = alterarVariaveisFormulaPorValor(getFormulaUsoNota37(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso37)) {
							if (historicoVO.getNota37() == null && getFormulaCalculoNota37().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota37() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota37().equals("")) {
									formulaNota37 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota37(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota37(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota37))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota37().equals("")) {
						formulaNota37 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota37(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota37(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota37))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota37PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota37ConceitoVOs()) {
								if (historicoVO.getNota37() >= conf.getFaixaNota1() && historicoVO.getNota37() <= conf.getFaixaNota2()) {
									historicoVO.setNota37Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 38
				if (this.getUtilizarNota38().booleanValue()) {
					String formulaNota38 = "";
					String formulaUso38 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota38PorConceito() && getFormulaCalculoNota38().equals("") && historicoVO.getNota38Conceito().getCodigo() > 0) {
						historicoVO.setNota38(historicoVO.getNota38Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota38().equals("")) {
						formulaUso38 = alterarVariaveisFormulaPorValor(getFormulaUsoNota38(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso38)) {
							if (historicoVO.getNota38() == null && getFormulaCalculoNota38().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota38() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota38().equals("")) {
									formulaNota38 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota38(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota38(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota38))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota38().equals("")) {
						formulaNota38 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota38(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota38(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota38))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota38PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota38ConceitoVOs()) {
								if (historicoVO.getNota38() >= conf.getFaixaNota1() && historicoVO.getNota38() <= conf.getFaixaNota2()) {
									historicoVO.setNota38Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 39
				if (this.getUtilizarNota39().booleanValue()) {
					String formulaNota39 = "";
					String formulaUso39 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota39PorConceito() && getFormulaCalculoNota39().equals("") && historicoVO.getNota39Conceito().getCodigo() > 0) {
						historicoVO.setNota39(historicoVO.getNota39Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota39().equals("")) {
						formulaUso39 = alterarVariaveisFormulaPorValor(getFormulaUsoNota39(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso39)) {
							if (historicoVO.getNota39() == null && getFormulaCalculoNota39().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota39() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota39().equals("")) {
									formulaNota39 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota39(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota39(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota39))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota39().equals("")) {
						formulaNota39 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota39(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota39(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota39))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota39PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota39ConceitoVOs()) {
								if (historicoVO.getNota39() >= conf.getFaixaNota1() && historicoVO.getNota39() <= conf.getFaixaNota2()) {
									historicoVO.setNota39Conceito(conf);
									break;
								}
							}
						}
					}
				}
				// ********* NOTA 40
				if (this.getUtilizarNota40().booleanValue()) {
					String formulaNota40 = "";
					String formulaUso40 = "";
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota40PorConceito() && getFormulaCalculoNota40().equals("") && historicoVO.getNota40Conceito().getCodigo() > 0) {
						historicoVO.setNota40(historicoVO.getNota40Conceito().getFaixaNota2());
					}
					if (!getFormulaUsoNota40().equals("")) {
						formulaUso40 = alterarVariaveisFormulaPorValor(getFormulaUsoNota40(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						if (verificarSituacaoAluno(formulaUso40)) {
							if (historicoVO.getNota40() == null && getFormulaCalculoNota40().equals("")) {
								throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota40() + " deve ser informado.");
							} else {
								if (!getFormulaCalculoNota40().equals("")) {
									formulaNota40 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota40(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
									historicoVO.setNota40(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota40))));
								}
							}
						}
					}
					if (!getFormulaCalculoNota40().equals("")) {
						formulaNota40 = alterarVariaveisFormulaPorValor(getFormulaCalculoNota40(), historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
						historicoVO.setNota40(Uteis.arrendondarForcando2CadasDecimais(new Double(verificarAprovacaoAluno(formulaNota40))));
						if (historicoVO.getConfiguracaoAcademico().getUtilizarNota40PorConceito()) {
							for (ConfiguracaoAcademicoNotaConceitoVO conf : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota40ConceitoVOs()) {
								if (historicoVO.getNota40() >= conf.getFaixaNota1() && historicoVO.getNota40() <= conf.getFaixaNota2()) {
									historicoVO.setNota40Conceito(conf);
									break;
								}
							}
						}
					}
				}
				

		// .......(ME > 7) ou (MF > 5)
		String formulaCalculoMedia = this.getFormulaCalculoMediaFinal().trim();
		formulaCalculoMedia = alterarVariaveisFormulaCalculoMediaFinalPorValor(historicoVO, formulaCalculoMedia, historicoVO.getNota1(), historicoVO.getNota2(), historicoVO.getNota3(), historicoVO.getNota4(), historicoVO.getNota5(), historicoVO.getNota6(), historicoVO.getNota7(), historicoVO.getNota8(), historicoVO.getNota9(), historicoVO.getNota10(), historicoVO.getNota11(), historicoVO.getNota12(), historicoVO.getNota13(), historicoVO.getNota14(), historicoVO.getNota15(), historicoVO.getNota16(), historicoVO.getNota17(), historicoVO.getNota18(), historicoVO.getNota19(), historicoVO.getNota20(), historicoVO.getNota21(), historicoVO.getNota22(), historicoVO.getNota23(), historicoVO.getNota24(), historicoVO.getNota25(), historicoVO.getNota26(), historicoVO.getNota27(), historicoVO.getNota28(), historicoVO.getNota29(), historicoVO.getNota30(), historicoVO.getNota31(), historicoVO.getNota32(), historicoVO.getNota33(), historicoVO.getNota34(), historicoVO.getNota35(), historicoVO.getNota36(), historicoVO.getNota37(), historicoVO.getNota38(), historicoVO.getNota39(), historicoVO.getNota40());
		return (verificarSituacaoAluno(formulaCalculoMedia));
	}

	public boolean verificarSituacaoAluno(String formulaCalculoMediaFinal) {
		String formula = null;
		ScriptEngineManager factory = null;
		ScriptEngine engine = null;
		Object result = null;
		Integer cont = null;
		try {
			formula = formulaCalculoMediaFinal.trim();
			
			// Criado para substituir o modelo de calculo anterior este realizar
			// calculos mais complexos
			factory = new ScriptEngineManager();
			// create a JavaScript engine
			engine = factory.getEngineByName("JavaScript");
			// evaluate JavaScript code from String
			try {
				formula = formula.replaceAll(" e ", " && ");
				formula = formula.replaceAll(" E ", " && ");
				formula = formula.replaceAll(" ou ", " || ");
				formula = formula.replaceAll(" OU ", " || ");
				formula = formula.replaceAll("=", "==");
				formula = formula.replaceAll("====", "==");
				formula = formula.replaceAll(">==", ">=");
				formula = formula.replaceAll("<==", "<=");
				formula = formula.replaceAll("!==", "!=");			
				formula = realizarSubstituicaoFuncaoMaior(formula);
				result = engine.eval(formula);
				if (result instanceof Boolean) {
					return (Boolean) result;
				}
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			formula = formulaCalculoMediaFinal.trim();
			cont = 0;
			boolean valorFim = false;
			while (cont != 1) {
				if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1)) {
					valorFim = obterSituacaoFormula(formula);
					cont = 1;
				} else {
					int ini = formula.lastIndexOf('(');
					int fim = formula.indexOf(')', ini);
					String novaFormula = formula.substring(ini + 1, fim);
					valorFim = obterSituacaoFormula(novaFormula);
					String parteFormulaSubstituir = formula.substring(ini, fim + 1);
					formula = formula.replace(parteFormulaSubstituir, String.valueOf(valorFim));
					if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1) && (formula.indexOf('+') == -1) && (formula.indexOf('-') == -1) && (formula.indexOf('/') == -1) && (formula.indexOf('*') == -1) && (formula.indexOf(" ou ") == -1) && (formula.indexOf(" e ") == -1) && (formula.indexOf(">=") == -1) && (formula.indexOf(">=") == -1) && (formula.indexOf('>') == -1) && (formula.indexOf("<=") == -1) && (formula.indexOf("=<") == -1) && (formula.indexOf('<') == -1)) {
						cont = 1;
					}
				}
			}
			return valorFim;
		} finally {
			if (formula != null) {
				formula = null;
			}
			if (factory != null) {
				factory = null;
			}
			if (engine != null) {
				engine = null;
			}
			if (result != null) {
				result = null;
			}
			if (cont != null) {
				cont = null;
			}
		}
	}

	public boolean obterSituacaoFormula(String formula) {
		String formula2 = formula;
		// Criado para substituir o modelo de calculo anterior este realizar
		// calculos mais complexos
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");		
		// evaluate JavaScript code from String
		try {
			formula2 = formula2.replaceAll(" e ", " && ");
			formula2 = formula2.replaceAll(" E ", " && ");
			formula2 = formula2.replaceAll(" ou ", " || ");
			formula2 = formula2.replaceAll(" OU ", " || ");
			formula2 = formula2.replaceAll("=", "==");
			formula2 = formula2.replaceAll("====", "==");
			formula2 = formula2.replaceAll(">==", ">=");
			formula2 = formula2.replaceAll("<==", "<=");
			formula2 = formula2.replaceAll("!==", "!=");			
			formula2 = realizarSubstituicaoFuncaoMaior(formula2);			
			Object result = engine.eval(formula2);
			if (result instanceof Boolean) {
				return (Boolean) result;
			}
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return true;
	}

	public String alterarVariaveisFormulaPorValor(String formula, Double nota1, Double nota2, Double nota3, Double nota4, Double nota5, Double nota6, Double nota7, Double nota8, Double nota9, Double nota10, Double nota11, Double nota12, Double nota13, Double nota14, Double nota15, Double nota16, Double nota17, Double nota18, Double nota19, Double nota20, Double nota21, Double nota22, Double nota23, Double nota24, Double nota25, Double nota26, Double nota27, Double nota28, Double nota29, Double nota30, Double nota31, Double nota32, Double nota33, Double nota34, Double nota35, Double nota36, Double nota37, Double nota38, Double nota39, Double nota40) throws Exception {
		int cont = 0;
		while (cont != -1) {
			if ((!getTituloNota1().equals("")) && (formula.indexOf(getTituloNota1()) != -1)) {
				if (nota1 == null || nota1 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota1() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota1(), nota1.toString());
				cont = formula.indexOf(getTituloNota1());
			}
			if ((!getTituloNota2().equals("")) && (formula.indexOf(getTituloNota2()) != -1)) {
				if (nota2 == null || nota2 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota2() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota2(), nota2.toString());
				cont = formula.indexOf(getTituloNota2());
			}
			if ((!getTituloNota3().equals("")) && (formula.indexOf(getTituloNota3()) != -1)) {
				if (nota3 == null || nota3 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota3() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota3(), nota3.toString());
				cont = formula.indexOf(getTituloNota3());
			}
			if ((!getTituloNota4().equals("")) && (formula.indexOf(getTituloNota4()) != -1)) {
				if (nota4 == null || nota4 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota4() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota4(), nota4.toString());
				cont = formula.indexOf(getTituloNota4());
			}
			if ((!getTituloNota5().equals("")) && (formula.indexOf(getTituloNota5()) != -1)) {
				if (nota5 == null || nota5 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota5() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota5(), nota5.toString());
				cont = formula.indexOf(getTituloNota5());
			}
			if ((!getTituloNota6().equals("")) && (formula.indexOf(getTituloNota6()) != -1)) {
				if (nota6 == null || nota6 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota6() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota6(), nota6.toString());
				cont = formula.indexOf(getTituloNota6());
			}
			if ((!getTituloNota7().equals("")) && (formula.indexOf(getTituloNota7()) != -1)) {
				if (nota7 == null || nota7 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota7() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota7(), nota7.toString());
				cont = formula.indexOf(getTituloNota7());
			}
			if ((!getTituloNota8().equals("")) && (formula.indexOf(getTituloNota8()) != -1)) {
				if (nota8 == null || nota8 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota8() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota8(), nota8.toString());
				cont = formula.indexOf(getTituloNota8());
			}
			if ((!getTituloNota9().equals("")) && (formula.indexOf(getTituloNota9()) != -1)) {
				if (nota9 == null || nota9 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota9() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota9(), nota9.toString());
				cont = formula.indexOf(getTituloNota9());
			}
			if ((!getTituloNota10().equals("")) && (formula.indexOf(getTituloNota10()) != -1)) {
				if (nota10 == null || nota10 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota10() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota10(), nota10.toString());
				cont = formula.indexOf(getTituloNota10());
			}
			if ((!getTituloNota11().equals("")) && (formula.indexOf(getTituloNota11()) != -1)) {
				if (nota11 == null || nota11 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota11() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota11(), nota11.toString());
				cont = formula.indexOf(getTituloNota11());
			}
			if ((!getTituloNota12().equals("")) && (formula.indexOf(getTituloNota12()) != -1)) {
				if (nota12 == null || nota12 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota12() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota12(), nota12.toString());
				cont = formula.indexOf(getTituloNota12());
			}
			if ((!getTituloNota13().equals("")) && (formula.indexOf(getTituloNota13()) != -1)) {
				if (nota13 == null || nota13 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota13() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota13(), nota13.toString());
				cont = formula.indexOf(getTituloNota13());
			}
			if ((!getTituloNota14().equals("")) && (formula.indexOf(getTituloNota14()) != -1)) {
				if (nota14 == null || nota14 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota14() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota14(), nota14.toString());
				cont = formula.indexOf(getTituloNota14());
			}
			if ((!getTituloNota15().equals("")) && (formula.indexOf(getTituloNota15()) != -1)) {
				if (nota15 == null || nota15 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota15() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota15(), nota15.toString());
				cont = formula.indexOf(getTituloNota15());
			}
			if ((!getTituloNota16().equals("")) && (formula.indexOf(getTituloNota16()) != -1)) {
				if (nota16 == null || nota16 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota16() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota16(), nota16.toString());
				cont = formula.indexOf(getTituloNota16());
			}
			if ((!getTituloNota17().equals("")) && (formula.indexOf(getTituloNota17()) != -1)) {
				if (nota17 == null || nota17 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota17() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota17(), nota17.toString());
				cont = formula.indexOf(getTituloNota17());
			}
			if ((!getTituloNota18().equals("")) && (formula.indexOf(getTituloNota18()) != -1)) {
				if (nota18 == null || nota18 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota18() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota18(), nota18.toString());
				cont = formula.indexOf(getTituloNota18());
			}
			if ((!getTituloNota19().equals("")) && (formula.indexOf(getTituloNota19()) != -1)) {
				if (nota19 == null || nota19 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota19() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota19(), nota19.toString());
				cont = formula.indexOf(getTituloNota19());
			}
			if ((!getTituloNota20().equals("")) && (formula.indexOf(getTituloNota20()) != -1)) {
				if (nota20 == null || nota20 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota20() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota20(), nota20.toString());
				cont = formula.indexOf(getTituloNota20());
			}
			if ((!getTituloNota21().equals("")) && (formula.indexOf(getTituloNota21()) != -1)) {
				if (nota21 == null || nota21 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota21() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota21(), nota21.toString());
				cont = formula.indexOf(getTituloNota21());
			}
			if ((!getTituloNota22().equals("")) && (formula.indexOf(getTituloNota22()) != -1)) {
				if (nota22 == null || nota22 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota22() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota22(), nota22.toString());
				cont = formula.indexOf(getTituloNota22());
			}
			if ((!getTituloNota23().equals("")) && (formula.indexOf(getTituloNota23()) != -1)) {
				if (nota23 == null || nota23 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota23() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota23(), nota23.toString());
				cont = formula.indexOf(getTituloNota23());
			}
			if ((!getTituloNota24().equals("")) && (formula.indexOf(getTituloNota24()) != -1)) {
				if (nota24 == null || nota24 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota24() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota24(), nota24.toString());
				cont = formula.indexOf(getTituloNota24());
			}
			if ((!getTituloNota25().equals("")) && (formula.indexOf(getTituloNota25()) != -1)) {
				if (nota25 == null || nota25 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota25() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota25(), nota25.toString());
				cont = formula.indexOf(getTituloNota25());
			}
			if ((!getTituloNota26().equals("")) && (formula.indexOf(getTituloNota26()) != -1)) {
				if (nota26 == null || nota26 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota26() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota26(), nota26.toString());
				cont = formula.indexOf(getTituloNota26());
			}
			if ((!getTituloNota27().equals("")) && (formula.indexOf(getTituloNota27()) != -1)) {
				if (nota27 == null || nota27 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota27() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota27(), nota27.toString());
				cont = formula.indexOf(getTituloNota27());
			}
			if ((!getTituloNota28().equals("")) && (formula.indexOf(getTituloNota28()) != -1)) {
				if (nota28 == null || nota28 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota28() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota28(), nota28.toString());
				cont = formula.indexOf(getTituloNota28());
			}
			if ((!getTituloNota29().equals("")) && (formula.indexOf(getTituloNota29()) != -1)) {
				if (nota29 == null || nota29 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota29() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota29(), nota29.toString());
				cont = formula.indexOf(getTituloNota29());
			}
			if ((!getTituloNota30().equals("")) && (formula.indexOf(getTituloNota30()) != -1)) {
				if (nota30 == null || nota30 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota30() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota30(), nota30.toString());
				cont = formula.indexOf(getTituloNota30());
			}
			
			if ((!getTituloNota31().equals("")) && (formula.indexOf(getTituloNota31()) != -1)) {
				if (nota31 == null || nota31 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota31() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota31(), nota31.toString());
				cont = formula.indexOf(getTituloNota31());
			}
			if ((!getTituloNota32().equals("")) && (formula.indexOf(getTituloNota32()) != -1)) {
				if (nota32 == null || nota32 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota32() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota32(), nota32.toString());
				cont = formula.indexOf(getTituloNota32());
			}
			if ((!getTituloNota33().equals("")) && (formula.indexOf(getTituloNota33()) != -1)) {
				if (nota33 == null || nota33 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota33() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota33(), nota33.toString());
				cont = formula.indexOf(getTituloNota33());
			}
			if ((!getTituloNota34().equals("")) && (formula.indexOf(getTituloNota34()) != -1)) {
				if (nota34 == null || nota34 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota34() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota34(), nota34.toString());
				cont = formula.indexOf(getTituloNota34());
			}
			if ((!getTituloNota35().equals("")) && (formula.indexOf(getTituloNota35()) != -1)) {
				if (nota35 == null || nota35 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota35() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota35(), nota35.toString());
				cont = formula.indexOf(getTituloNota35());
			}
			if ((!getTituloNota36().equals("")) && (formula.indexOf(getTituloNota36()) != -1)) {
				if (nota36 == null || nota36 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota36() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota36(), nota36.toString());
				cont = formula.indexOf(getTituloNota36());
			}
			if ((!getTituloNota37().equals("")) && (formula.indexOf(getTituloNota37()) != -1)) {
				if (nota37 == null || nota37 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota37() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota37(), nota37.toString());
				cont = formula.indexOf(getTituloNota37());
			}
			if ((!getTituloNota38().equals("")) && (formula.indexOf(getTituloNota38()) != -1)) {
				if (nota38 == null || nota38 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota38() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota38(), nota38.toString());
				cont = formula.indexOf(getTituloNota38());
			}
			if ((!getTituloNota39().equals("")) && (formula.indexOf(getTituloNota39()) != -1)) {
				if (nota39 == null || nota39 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota39() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota39(), nota39.toString());
				cont = formula.indexOf(getTituloNota39());
			}
			if ((!getTituloNota40().equals("")) && (formula.indexOf(getTituloNota40()) != -1)) {
				if (nota40 == null || nota40 < 0) {
					throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloNota40() + " deve ser informado.");
				}
				formula = formula.replaceFirst(getTituloNota40(), nota40.toString());
				cont = formula.indexOf(getTituloNota40());
			}
		}
		return formula;
	}

	public String alterarVariaveisFormulaCalculoMediaFinalPorValor(HistoricoVO historicoVO, String formula, Double nota1, Double nota2, Double nota3, Double nota4, Double nota5, Double nota6, Double nota7, Double nota8, Double nota9, Double nota10, Double nota11, Double nota12, Double nota13, Double nota14, Double nota15, Double nota16, Double nota17, Double nota18, Double nota19, Double nota20, Double nota21, Double nota22, Double nota23, Double nota24, Double nota25, Double nota26, Double nota27, Double nota28, Double nota29, Double nota30, Double nota31, Double nota32, Double nota33, Double nota34, Double nota35, Double nota36, Double nota37, Double nota38, Double nota39, Double nota40) throws Exception {
		int cont = 0;
		while (cont != -1) {
			if ((!getTituloNota1().equals("")) && (formula.indexOf(getTituloNota1()) != -1)) {
				formula = formula.replaceFirst(getTituloNota1(), nota1.toString());
				if (getNota1MediaFinal() && nota1.doubleValue() > 0) {
					historicoVO.setMediaFinalConceito(historicoVO.getNota1Conceito());
					historicoVO.setMediaFinal(nota1);
				} else {
					if (getNota1MediaFinal()) {
						historicoVO.setMediaFinalConceito(historicoVO.getNota1Conceito());
						historicoVO.setMediaFinal(0.0);
					}
				}
				cont = formula.indexOf(getTituloNota1());
			}
			if ((!getTituloNota2().equals("")) && (formula.indexOf(getTituloNota2()) != -1)) {
				formula = formula.replaceFirst(getTituloNota2(), nota2.toString());
				if (getNota2MediaFinal() && nota2.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota2);
					historicoVO.setMediaFinalConceito(historicoVO.getNota2Conceito());
				} else {
					if (getNota2MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota2Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota2());
			}
			if ((!getTituloNota3().equals("")) && (formula.indexOf(getTituloNota3()) != -1)) {
				formula = formula.replaceFirst(getTituloNota3(), nota3.toString());
				if (getNota3MediaFinal() && nota3.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota3);
					historicoVO.setMediaFinalConceito(historicoVO.getNota3Conceito());
				} else {
					if (getNota3MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota3Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota3());
			}
			if ((!getTituloNota4().equals("")) && (formula.indexOf(getTituloNota4()) != -1)) {
				formula = formula.replaceFirst(getTituloNota4(), nota4.toString());
				if (getNota4MediaFinal() && nota4.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota4);
					historicoVO.setMediaFinalConceito(historicoVO.getNota4Conceito());
				} else {
					if (getNota4MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota4Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota4());
			}
			if ((!getTituloNota5().equals("")) && (formula.indexOf(getTituloNota5()) != -1)) {
				formula = formula.replaceFirst(getTituloNota5(), nota5.toString());
				if (getNota5MediaFinal() && nota5.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota5);
					historicoVO.setMediaFinalConceito(historicoVO.getNota5Conceito());
				} else {
					if (getNota5MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota5Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota5());
			}
			if ((!getTituloNota6().equals("")) && (formula.indexOf(getTituloNota6()) != -1)) {
				formula = formula.replaceFirst(getTituloNota6(), nota6.toString());
				if (getNota6MediaFinal() && nota6.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota6);
					historicoVO.setMediaFinalConceito(historicoVO.getNota6Conceito());
				} else {
					if (getNota6MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota6Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota6());
			}
			if ((!getTituloNota7().equals("")) && (formula.indexOf(getTituloNota7()) != -1)) {
				formula = formula.replaceFirst(getTituloNota7(), nota7.toString());
				if (getNota7MediaFinal() && nota7.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota7);
					historicoVO.setMediaFinalConceito(historicoVO.getNota7Conceito());
				} else {
					if (getNota7MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota7Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota7());
			}
			if ((!getTituloNota8().equals("")) && (formula.indexOf(getTituloNota8()) != -1)) {
				formula = formula.replaceFirst(getTituloNota8(), nota8.toString());
				if (getNota8MediaFinal() && nota8.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota8);
					historicoVO.setMediaFinalConceito(historicoVO.getNota8Conceito());
				} else {
					if (getNota8MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota8Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota8());
			}
			if ((!getTituloNota9().equals("")) && (formula.indexOf(getTituloNota9()) != -1)) {
				formula = formula.replaceFirst(getTituloNota9(), nota9.toString());
				if (getNota9MediaFinal() && nota9.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota9);
					historicoVO.setMediaFinalConceito(historicoVO.getNota9Conceito());
				} else {
					if (getNota9MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota9Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota9());
			}
			if ((!getTituloNota10().equals("")) && (formula.indexOf(getTituloNota10()) != -1)) {
				formula = formula.replaceFirst(getTituloNota10(), nota10.toString());
				if (getNota10MediaFinal() && nota10.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota10);
					historicoVO.setMediaFinalConceito(historicoVO.getNota10Conceito());
				} else {
					if (getNota10MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota10Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota10());
			}
			if ((!getTituloNota11().equals("")) && (formula.indexOf(getTituloNota11()) != -1)) {
				formula = formula.replaceFirst(getTituloNota11(), nota11.toString());
				if (getNota11MediaFinal() && nota11.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota11);
					historicoVO.setMediaFinalConceito(historicoVO.getNota11Conceito());
				} else {
					if (getNota11MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota11Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota11());
			}
			if ((!getTituloNota12().equals("")) && (formula.indexOf(getTituloNota12()) != -1)) {
				formula = formula.replaceFirst(getTituloNota12(), nota12.toString());
				if (getNota12MediaFinal() && nota12.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota12);
					historicoVO.setMediaFinalConceito(historicoVO.getNota12Conceito());
				} else {
					if (getNota12MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota12Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota12());
			}
			if ((!getTituloNota13().equals("")) && (formula.indexOf(getTituloNota13()) != -1)) {
				formula = formula.replaceFirst(getTituloNota13(), nota13.toString());
				if (getNota13MediaFinal() && nota13.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota13);
					historicoVO.setMediaFinalConceito(historicoVO.getNota13Conceito());
				} else {
					if (getNota13MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota13Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota13());
			}

			if ((!getTituloNota14().equals("")) && (formula.indexOf(getTituloNota14()) != -1)) {
				formula = formula.replaceFirst(getTituloNota14(), nota14.toString());
				if (getNota14MediaFinal() && nota14.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota14);
					historicoVO.setMediaFinalConceito(historicoVO.getNota14Conceito());
				} else {
					if (getNota14MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota14Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota14());
			}

			if ((!getTituloNota15().equals("")) && (formula.indexOf(getTituloNota15()) != -1)) {
				formula = formula.replaceFirst(getTituloNota15(), nota15.toString());
				if (getNota15MediaFinal() && nota15.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota15);
					historicoVO.setMediaFinalConceito(historicoVO.getNota15Conceito());
				} else {
					if (getNota15MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota15Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota15());
			}

			if ((!getTituloNota16().equals("")) && (formula.indexOf(getTituloNota16()) != -1)) {
				formula = formula.replaceFirst(getTituloNota16(), nota16.toString());
				if (getNota16MediaFinal() && nota16.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota16);
					historicoVO.setMediaFinalConceito(historicoVO.getNota16Conceito());
				} else {
					if (getNota16MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota16Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota16());
			}

			if ((!getTituloNota17().equals("")) && (formula.indexOf(getTituloNota17()) != -1)) {
				formula = formula.replaceFirst(getTituloNota17(), nota17.toString());
				if (getNota17MediaFinal() && nota17.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota17);
					historicoVO.setMediaFinalConceito(historicoVO.getNota17Conceito());
				} else {
					if (getNota17MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota17Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota17());
			}

			if ((!getTituloNota18().equals("")) && (formula.indexOf(getTituloNota18()) != -1)) {
				formula = formula.replaceFirst(getTituloNota18(), nota18.toString());
				if (getNota18MediaFinal() && nota18.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota18);
					historicoVO.setMediaFinalConceito(historicoVO.getNota18Conceito());
				} else {
					if (getNota18MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota18Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota18());
			}

			if ((!getTituloNota19().equals("")) && (formula.indexOf(getTituloNota19()) != -1)) {
				formula = formula.replaceFirst(getTituloNota19(), nota19.toString());
				if (getNota19MediaFinal() && nota19.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota19);
					historicoVO.setMediaFinalConceito(historicoVO.getNota19Conceito());
				} else {
					if (getNota19MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota19Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota19());
			}

			if ((!getTituloNota20().equals("")) && (formula.indexOf(getTituloNota20()) != -1)) {
				formula = formula.replaceFirst(getTituloNota20(), nota20.toString());
				if (getNota20MediaFinal() && nota20.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota20);
					historicoVO.setMediaFinalConceito(historicoVO.getNota20Conceito());
				} else {
					if (getNota20MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota20Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota20());
			}

			if ((!getTituloNota21().equals("")) && (formula.indexOf(getTituloNota21()) != -1)) {
				formula = formula.replaceFirst(getTituloNota21(), nota21.toString());
				if (getNota21MediaFinal() && nota21.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota21);
					historicoVO.setMediaFinalConceito(historicoVO.getNota21Conceito());
				} else {
					if (getNota21MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota21Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota21());
			}

			if ((!getTituloNota22().equals("")) && (formula.indexOf(getTituloNota22()) != -1)) {
				formula = formula.replaceFirst(getTituloNota22(), nota22.toString());
				if (getNota22MediaFinal() && nota22.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota22);
					historicoVO.setMediaFinalConceito(historicoVO.getNota22Conceito());
				} else {
					if (getNota22MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota22Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota22());
			}

			if ((!getTituloNota23().equals("")) && (formula.indexOf(getTituloNota23()) != -1)) {
				formula = formula.replaceFirst(getTituloNota23(), nota23.toString());
				if (getNota23MediaFinal() && nota23.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota23);
					historicoVO.setMediaFinalConceito(historicoVO.getNota23Conceito());
				} else {
					if (getNota23MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota23Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota23());
			}

			if ((!getTituloNota24().equals("")) && (formula.indexOf(getTituloNota24()) != -1)) {
				formula = formula.replaceFirst(getTituloNota24(), nota24.toString());
				if (getNota24MediaFinal() && nota24.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota24);
					historicoVO.setMediaFinalConceito(historicoVO.getNota24Conceito());
				} else {
					if (getNota24MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota24Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota24());
			}

			if ((!getTituloNota25().equals("")) && (formula.indexOf(getTituloNota25()) != -1)) {
				formula = formula.replaceFirst(getTituloNota25(), nota25.toString());
				if (getNota25MediaFinal() && nota25.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota25);
					historicoVO.setMediaFinalConceito(historicoVO.getNota25Conceito());
				} else {
					if (getNota25MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota25Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota25());
			}

			if ((!getTituloNota26().equals("")) && (formula.indexOf(getTituloNota26()) != -1)) {
				formula = formula.replaceFirst(getTituloNota26(), nota26.toString());
				if (getNota26MediaFinal() && nota26.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota26);
					historicoVO.setMediaFinalConceito(historicoVO.getNota26Conceito());
				} else {
					if (getNota26MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota26Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota26());
			}

			if ((!getTituloNota27().equals("")) && (formula.indexOf(getTituloNota27()) != -1)) {
				formula = formula.replaceFirst(getTituloNota27(), nota27.toString());
				if (getNota27MediaFinal() && nota27.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota27);
					historicoVO.setMediaFinalConceito(historicoVO.getNota27Conceito());
				} else {
					if (getNota27MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota27Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota27());
			}

			if ((!getTituloNota28().equals("")) && (formula.indexOf(getTituloNota28()) != -1)) {
				formula = formula.replaceFirst(getTituloNota28(), nota28.toString());
				if (getNota28MediaFinal() && nota28.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota28);
					historicoVO.setMediaFinalConceito(historicoVO.getNota28Conceito());
				} else {
					if (getNota28MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota28Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota28());
			}

			if ((!getTituloNota29().equals("")) && (formula.indexOf(getTituloNota29()) != -1)) {
				formula = formula.replaceFirst(getTituloNota29(), nota29.toString());
				if (getNota29MediaFinal() && nota29.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota29);
					historicoVO.setMediaFinalConceito(historicoVO.getNota29Conceito());
				} else {
					if (getNota29MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota29Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota29());
			}

			if ((!getTituloNota30().equals("")) && (formula.indexOf(getTituloNota30()) != -1)) {
				formula = formula.replaceFirst(getTituloNota30(), nota30.toString());
				if (getNota30MediaFinal() && nota30.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota30);
					historicoVO.setMediaFinalConceito(historicoVO.getNota30Conceito());
				} else {
					if (getNota30MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota30Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota30());
			}
			
			if ((!getTituloNota31().equals("")) && (formula.indexOf(getTituloNota31()) != -1)) {
				formula = formula.replaceFirst(getTituloNota31(), nota31.toString());
				if (getNota31MediaFinal() && nota31.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota31);
					historicoVO.setMediaFinalConceito(historicoVO.getNota31Conceito());
				} else {
					if (getNota31MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota31Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota31());
			}
			
			if ((!getTituloNota32().equals("")) && (formula.indexOf(getTituloNota32()) != -1)) {
				formula = formula.replaceFirst(getTituloNota32(), nota32.toString());
				if (getNota32MediaFinal() && nota32.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota32);
					historicoVO.setMediaFinalConceito(historicoVO.getNota32Conceito());
				} else {
					if (getNota32MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota32Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota32());
			}
			
			if ((!getTituloNota33().equals("")) && (formula.indexOf(getTituloNota33()) != -1)) {
				formula = formula.replaceFirst(getTituloNota33(), nota33.toString());
				if (getNota33MediaFinal() && nota33.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota33);
					historicoVO.setMediaFinalConceito(historicoVO.getNota33Conceito());
				} else {
					if (getNota33MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota33Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota33());
			}
			
			if ((!getTituloNota34().equals("")) && (formula.indexOf(getTituloNota34()) != -1)) {
				formula = formula.replaceFirst(getTituloNota34(), nota34.toString());
				if (getNota34MediaFinal() && nota34.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota34);
					historicoVO.setMediaFinalConceito(historicoVO.getNota34Conceito());
				} else {
					if (getNota34MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota34Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota34());
			}
			
			if ((!getTituloNota35().equals("")) && (formula.indexOf(getTituloNota35()) != -1)) {
				formula = formula.replaceFirst(getTituloNota35(), nota35.toString());
				if (getNota35MediaFinal() && nota35.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota35);
					historicoVO.setMediaFinalConceito(historicoVO.getNota35Conceito());
				} else {
					if (getNota35MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota35Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota35());
			}
			
			if ((!getTituloNota36().equals("")) && (formula.indexOf(getTituloNota36()) != -1)) {
				formula = formula.replaceFirst(getTituloNota36(), nota36.toString());
				if (getNota36MediaFinal() && nota36.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota36);
					historicoVO.setMediaFinalConceito(historicoVO.getNota36Conceito());
				} else {
					if (getNota36MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota36Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota36());
			}
			
			if ((!getTituloNota37().equals("")) && (formula.indexOf(getTituloNota37()) != -1)) {
				formula = formula.replaceFirst(getTituloNota37(), nota37.toString());
				if (getNota37MediaFinal() && nota37.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota37);
					historicoVO.setMediaFinalConceito(historicoVO.getNota37Conceito());
				} else {
					if (getNota37MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota37Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota37());
			}
			
			if ((!getTituloNota38().equals("")) && (formula.indexOf(getTituloNota38()) != -1)) {
				formula = formula.replaceFirst(getTituloNota38(), nota38.toString());
				if (getNota38MediaFinal() && nota38.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota38);
					historicoVO.setMediaFinalConceito(historicoVO.getNota38Conceito());
				} else {
					if (getNota38MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota38Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota38());
			}
			
			if ((!getTituloNota39().equals("")) && (formula.indexOf(getTituloNota39()) != -1)) {
				formula = formula.replaceFirst(getTituloNota39(), nota39.toString());
				if (getNota39MediaFinal() && nota39.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota39);
					historicoVO.setMediaFinalConceito(historicoVO.getNota39Conceito());
				} else {
					if (getNota39MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota39Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota39());
			}
			
			if ((!getTituloNota40().equals("")) && (formula.indexOf(getTituloNota40()) != -1)) {
				formula = formula.replaceFirst(getTituloNota40(), nota40.toString());
				if (getNota40MediaFinal() && nota40.doubleValue() > 0) {
					historicoVO.setMediaFinal(nota40);
					historicoVO.setMediaFinalConceito(historicoVO.getNota40Conceito());
				} else {
					if (getNota40MediaFinal()) {
						historicoVO.setMediaFinal(0.0);
						historicoVO.setMediaFinalConceito(historicoVO.getNota40Conceito());
					}
				}
				cont = formula.indexOf(getTituloNota40());
			}

		}
		return formula;
	}

	public Double verificarAprovacaoAluno(String formulaCalculoMediaFinal) {
		formulaCalculoMediaFinal = realizarSubstituicaoFuncaoMaior(formulaCalculoMediaFinal);
		return Uteis.realizarCalculoFormula(formulaCalculoMediaFinal);
		// String formula = formulaCalculoMediaFinal.trim();
		// int cont = 0;
		// Double valorFim = 0.0;
		// while (cont != 1) {
		// if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1)) {
		// valorFim = obterResultadoFormula(formula);
		// cont = 1;
		// } else {
		// int ini = formula.lastIndexOf('(');
		// int fim = formula.indexOf(')', ini);
		// String novaFormula = formula.substring(ini + 1, fim);
		// valorFim =
		// Uteis.arrendondarForcando2CadasDecimais(obterResultadoFormula(novaFormula));
		// String parteFormulaSubstituir = formula.substring(ini, fim + 1);
		// formula = formula.replace(parteFormulaSubstituir,
		// String.valueOf(valorFim));
		// if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1) &&
		// (formula.indexOf('+') == -1) && (formula.indexOf('-') == -1) &&
		// (formula.indexOf('/') == -1) && (formula.indexOf('*') == -1)) {
		// cont = 1;
		// }
		// }
		// }
		//
		// return valorFim;
	}

	public Double obterResultadoFormula(String formula) {
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		try {
			formula = formula.replaceAll(" e ", " && ");
			formula = formula.replaceAll(" E ", " && ");
			formula = formula.replaceAll(" ou ", " || ");
			formula = formula.replaceAll(" OU ", " || ");
			formula = formula.replaceAll("=", "==");
			formula = formula.replaceAll("====", "==");
			formula = formula.replaceAll(">==", ">=");
			formula = formula.replaceAll("<==", "<=");
			formula = formula.replaceAll("!==", "!=");
			
			formula = realizarSubstituicaoFuncaoMaior(formula);
			Object result = engine.eval(formula);
			if (result instanceof Number) {
				return Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(new Double(((Number)result).doubleValue()), getQuantidadeCasasDecimaisPermitirAposVirgula());
			} else if(result != null) {
				try {
					Double valor = Double.valueOf(result.toString());
					return Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(valor, getQuantidadeCasasDecimaisPermitirAposVirgula());
				} catch (NumberFormatException e) {
				}
			}
		} catch (ScriptException e) {		
			e.printStackTrace();
		}
		return null;
		
	}

	public Double getPercentualFrequenciaAprovacao() {
		return (percentualFrequenciaAprovacao);
	}

	public void setPercentualFrequenciaAprovacao(Double percentualFrequenciaAprovacao) {
		this.percentualFrequenciaAprovacao = percentualFrequenciaAprovacao;
	}

	public String getFormulaUsoNota10() {
		if (formulaUsoNota10 == null) {
			formulaUsoNota10 = "";
		}
		return (formulaUsoNota10);
	}

	public void setFormulaUsoNota10(String formulaUsoNota10) {
		this.formulaUsoNota10 = formulaUsoNota10;
	}

	public String getFormulaCalculoNota10() {
		if (formulaCalculoNota10 == null) {
			formulaCalculoNota10 = "";
		}
		return (formulaCalculoNota10);
	}

	public void setFormulaCalculoNota10(String formulaCalculoNota10) {
		this.formulaCalculoNota10 = formulaCalculoNota10;
	}

	public String getTituloNota10() {
		if (tituloNota10 == null) {
			tituloNota10 = "";
		}
		return (tituloNota10);
	}

	public void setTituloNota10(String tituloNota10) {
		this.tituloNota10 = tituloNota10;
	}

	public Boolean getUtilizarNota10() {
		if (utilizarNota10 == null) {
			utilizarNota10 = false;
		}
		return (utilizarNota10);
	}

	public Boolean isUtilizarNota10() {
		if (utilizarNota10 == null) {
			utilizarNota10 = false;
		}
		return (utilizarNota10);
	}

	public void setUtilizarNota10(Boolean utilizarNota10) {
		this.utilizarNota10 = utilizarNota10;
	}

	public Double getNota10() {
		return (nota10);
	}

	public void setNota10(Double nota10) {
		this.nota10 = nota10;
	}

	public String getFormulaUsoNota9() {
		if (formulaUsoNota9 == null) {
			formulaUsoNota9 = "";
		}
		return (formulaUsoNota9);
	}

	public void setFormulaUsoNota9(String formulaUsoNota9) {
		this.formulaUsoNota9 = formulaUsoNota9;
	}

	public String getFormulaCalculoNota9() {
		if (formulaCalculoNota9 == null) {
			formulaCalculoNota9 = "";
		}
		return (formulaCalculoNota9);
	}

	public void setFormulaCalculoNota9(String formulaCalculoNota9) {
		this.formulaCalculoNota9 = formulaCalculoNota9;
	}

	public String getTituloNota9() {
		if (tituloNota9 == null) {
			tituloNota9 = "";
		}
		return (tituloNota9);
	}

	public void setTituloNota9(String tituloNota9) {
		this.tituloNota9 = tituloNota9;
	}

	public Boolean getUtilizarNota9() {
		if (utilizarNota9 == null) {
			utilizarNota9 = false;
		}
		return (utilizarNota9);
	}

	public Boolean isUtilizarNota9() {
		if (utilizarNota9 == null) {
			utilizarNota9 = false;
		}
		return (utilizarNota9);
	}

	public void setUtilizarNota9(Boolean utilizarNota9) {
		this.utilizarNota9 = utilizarNota9;
	}

	public Double getNota9() {
		return (nota9);
	}

	public void setNota9(Double nota9) {
		this.nota9 = nota9;
	}

	public String getFormulaUsoNota8() {
		if (formulaUsoNota8 == null) {
			formulaUsoNota8 = "";
		}
		return (formulaUsoNota8);
	}

	public void setFormulaUsoNota8(String formulaUsoNota8) {
		this.formulaUsoNota8 = formulaUsoNota8;
	}

	public String getFormulaCalculoNota8() {
		if (formulaCalculoNota8 == null) {
			formulaCalculoNota8 = "";
		}
		return (formulaCalculoNota8);
	}

	public void setFormulaCalculoNota8(String formulaCalculoNota8) {
		this.formulaCalculoNota8 = formulaCalculoNota8;
	}

	public String getTituloNota8() {
		if (tituloNota8 == null) {
			tituloNota8 = "";
		}
		return (tituloNota8);
	}

	public void setTituloNota8(String tituloNota8) {
		this.tituloNota8 = tituloNota8;
	}

	public Boolean getUtilizarNota8() {
		if (utilizarNota8 == null) {
			utilizarNota8 = false;
		}
		return (utilizarNota8);
	}

	public Boolean isUtilizarNota8() {
		if (utilizarNota8 == null) {
			utilizarNota8 = false;
		}
		return (utilizarNota8);
	}

	public void setUtilizarNota8(Boolean utilizarNota8) {
		this.utilizarNota8 = utilizarNota8;
	}

	public Double getNota8() {
		return (nota8);
	}

	public void setNota8(Double nota8) {
		this.nota8 = nota8;
	}

	public String getFormulaUsoNota7() {
		if (formulaUsoNota7 == null) {
			formulaUsoNota7 = "";
		}
		return (formulaUsoNota7);
	}

	public void setFormulaUsoNota7(String formulaUsoNota7) {
		this.formulaUsoNota7 = formulaUsoNota7;
	}

	public String getFormulaCalculoNota7() {
		if (formulaCalculoNota7 == null) {
			formulaCalculoNota7 = "";
		}
		return (formulaCalculoNota7);
	}

	public void setFormulaCalculoNota7(String formulaCalculoNota7) {
		this.formulaCalculoNota7 = formulaCalculoNota7;
	}

	public String getTituloNota7() {
		if (tituloNota7 == null) {
			tituloNota7 = "";
		}
		return (tituloNota7);
	}

	public void setTituloNota7(String tituloNota7) {
		this.tituloNota7 = tituloNota7;
	}

	public Boolean getUtilizarNota7() {
		if (utilizarNota7 == null) {
			utilizarNota7 = false;
		}
		return (utilizarNota7);
	}

	public Boolean isUtilizarNota7() {
		if (utilizarNota7 == null) {
			utilizarNota7 = false;
		}
		return (utilizarNota7);
	}

	public void setUtilizarNota7(Boolean utilizarNota7) {
		this.utilizarNota7 = utilizarNota7;
	}

	public Double getNota7() {
		return (nota7);
	}

	public void setNota7(Double nota7) {
		this.nota7 = nota7;
	}

	public String getFormulaUsoNota6() {
		if (formulaUsoNota6 == null) {
			formulaUsoNota6 = "";
		}
		return (formulaUsoNota6);
	}

	public void setFormulaUsoNota6(String formulaUsoNota6) {
		this.formulaUsoNota6 = formulaUsoNota6;
	}

	public String getFormulaCalculoNota6() {
		if (formulaCalculoNota6 == null) {
			formulaCalculoNota6 = "";
		}
		return (formulaCalculoNota6);
	}

	public void setFormulaCalculoNota6(String formulaCalculoNota6) {
		this.formulaCalculoNota6 = formulaCalculoNota6;
	}

	public String getTituloNota6() {
		if (tituloNota6 == null) {
			tituloNota6 = "";
		}
		return (tituloNota6);
	}

	public void setTituloNota6(String tituloNota6) {
		this.tituloNota6 = tituloNota6;
	}

	public Boolean getUtilizarNota6() {
		if (utilizarNota6 == null) {
			utilizarNota6 = false;
		}
		return (utilizarNota6);
	}

	public Boolean isUtilizarNota6() {
		return (utilizarNota6);
	}

	public void setUtilizarNota6(Boolean utilizarNota6) {
		this.utilizarNota6 = utilizarNota6;
	}

	public Double getNota6() {
		return (nota6);
	}

	public void setNota6(Double nota6) {
		this.nota6 = nota6;
	}

	public String getFormulaUsoNota5() {
		if (formulaUsoNota5 == null) {
			formulaUsoNota5 = "";
		}
		return (formulaUsoNota5);
	}

	public void setFormulaUsoNota5(String formulaUsoNota5) {
		this.formulaUsoNota5 = formulaUsoNota5;
	}

	public String getFormulaCalculoNota5() {
		if (formulaCalculoNota5 == null) {
			formulaCalculoNota5 = "";
		}
		return (formulaCalculoNota5);
	}

	public void setFormulaCalculoNota5(String formulaCalculoNota5) {
		this.formulaCalculoNota5 = formulaCalculoNota5;
	}

	public String getTituloNota5() {
		if (tituloNota5 == null) {
			tituloNota5 = "";
		}
		return (tituloNota5);
	}

	public void setTituloNota5(String tituloNota5) {
		this.tituloNota5 = tituloNota5;
	}

	public Boolean getUtilizarNota5() {
		if (utilizarNota5 == null) {
			utilizarNota5 = false;
		}
		return (utilizarNota5);
	}

	public Boolean isUtilizarNota5() {
		if (utilizarNota5 == null) {
			utilizarNota5 = false;
		}
		return (utilizarNota5);
	}

	public void setUtilizarNota5(Boolean utilizarNota5) {
		this.utilizarNota5 = utilizarNota5;
	}

	public Double getNota5() {
		return (nota5);
	}

	public void setNota5(Double nota5) {
		this.nota5 = nota5;
	}

	public String getFormulaUsoNota4() {
		if (formulaUsoNota4 == null) {
			formulaUsoNota4 = "";
		}
		return (formulaUsoNota4);
	}

	public void setFormulaUsoNota4(String formulaUsoNota4) {
		this.formulaUsoNota4 = formulaUsoNota4;
	}

	public String getFormulaCalculoNota4() {
		if (formulaCalculoNota4 == null) {
			formulaCalculoNota4 = "";
		}
		return (formulaCalculoNota4);
	}

	public void setFormulaCalculoNota4(String formulaCalculoNota4) {
		this.formulaCalculoNota4 = formulaCalculoNota4;
	}

	public String getTituloNota4() {
		if (tituloNota4 == null) {
			tituloNota4 = "";
		}
		return (tituloNota4);
	}

	public void setTituloNota4(String tituloNota4) {
		this.tituloNota4 = tituloNota4;
	}

	public Boolean getUtilizarNota4() {
		if (utilizarNota4 == null) {
			utilizarNota4 = false;
		}
		return (utilizarNota4);
	}

	public Boolean isUtilizarNota4() {
		if (utilizarNota4 == null) {
			utilizarNota4 = false;
		}
		return (utilizarNota4);
	}

	public void setUtilizarNota4(Boolean utilizarNota4) {
		this.utilizarNota4 = utilizarNota4;
	}

	public Double getNota4() {
		return (nota4);
	}

	public void setNota4(Double nota4) {
		this.nota4 = nota4;
	}

	public String getFormulaUsoNota3() {
		if (formulaUsoNota3 == null) {
			formulaUsoNota3 = "";
		}
		return (formulaUsoNota3);
	}

	public void setFormulaUsoNota3(String formulaUsoNota3) {
		this.formulaUsoNota3 = formulaUsoNota3;
	}

	public String getFormulaCalculoNota3() {
		if (formulaCalculoNota3 == null) {
			formulaCalculoNota3 = "";
		}
		return (formulaCalculoNota3);
	}

	public void setFormulaCalculoNota3(String formulaCalculoNota3) {
		this.formulaCalculoNota3 = formulaCalculoNota3;
	}

	public String getTituloNota3() {
		if (tituloNota3 == null) {
			tituloNota3 = "";
		}
		return (tituloNota3);
	}

	public void setTituloNota3(String tituloNota3) {
		this.tituloNota3 = tituloNota3;
	}

	public Boolean getUtilizarNota3() {
		if (utilizarNota3 == null) {
			utilizarNota3 = false;
		}
		return (utilizarNota3);
	}

	public Boolean isUtilizarNota3() {
		if (utilizarNota3 == null) {
			utilizarNota3 = false;
		}
		return (utilizarNota3);
	}

	public void setUtilizarNota3(Boolean utilizarNota3) {
		this.utilizarNota3 = utilizarNota3;
	}

	public Double getNota3() {
		return (nota3);
	}

	public void setNota3(Double nota3) {
		this.nota3 = nota3;
	}

	public String getFormulaUsoNota2() {
		if (formulaUsoNota2 == null) {
			formulaUsoNota2 = "";
		}
		return (formulaUsoNota2);
	}

	public void setFormulaUsoNota2(String formulaUsoNota2) {
		this.formulaUsoNota2 = formulaUsoNota2;
	}

	public String getFormulaCalculoNota2() {
		if (formulaCalculoNota2 == null) {
			formulaCalculoNota2 = "";
		}
		return (formulaCalculoNota2);
	}

	public void setFormulaCalculoNota2(String formulaCalculoNota2) {
		this.formulaCalculoNota2 = formulaCalculoNota2;
	}

	public String getTituloNota2() {
		if (tituloNota2 == null) {
			tituloNota2 = "";
		}
		return (tituloNota2);
	}

	public void setTituloNota2(String tituloNota2) {
		this.tituloNota2 = tituloNota2;
	}

	public Boolean getUtilizarNota2() {
		if (utilizarNota2 == null) {
			utilizarNota2 = false;
		}
		return (utilizarNota2);
	}

	public Boolean isUtilizarNota2() {
		return (utilizarNota2);
	}

	public void setUtilizarNota2(Boolean utilizarNota2) {
		this.utilizarNota2 = utilizarNota2;
	}

	public Double getNota2() {
		return (nota2);
	}

	public void setNota2(Double nota2) {
		this.nota2 = nota2;
	}

	public String getFormulaUsoNota1() {
		if (formulaUsoNota1 == null) {
			formulaUsoNota1 = "";
		}
		return (formulaUsoNota1);
	}

	public void setFormulaUsoNota1(String formulaUsoNota1) {
		this.formulaUsoNota1 = formulaUsoNota1;
	}

	public String getFormulaCalculoNota1() {
		if (formulaCalculoNota1 == null) {
			formulaCalculoNota1 = "";
		}
		return (formulaCalculoNota1);
	}

	public void setFormulaCalculoNota1(String formulaCalculoNota1) {
		this.formulaCalculoNota1 = formulaCalculoNota1;
	}

	public String getTituloNota1() {
		if (tituloNota1 == null) {
			tituloNota1 = "";
		}
		return (tituloNota1);
	}

	public void setTituloNota1(String tituloNota1) {
		this.tituloNota1 = tituloNota1;
	}

	public Boolean getUtilizarNota1() {
		if (utilizarNota1 == null) {
			utilizarNota1 = Boolean.FALSE;
		}
		return (utilizarNota1);
	}

	public Boolean isUtilizarNota1() {
		if (utilizarNota1 == null) {
			utilizarNota1 = Boolean.FALSE;
		}
		return (utilizarNota1);
	}

	public void setUtilizarNota1(Boolean utilizarNota1) {
		this.utilizarNota1 = utilizarNota1;
	}

	public Double getNota1() {
		return (nota1);
	}

	public void setNota1(Double nota1) {
		this.nota1 = nota1;
	}

	public String getFormulaCalculoMediaFinal() {
		if (formulaCalculoMediaFinal == null) {
			formulaCalculoMediaFinal = "";
		}
		return (formulaCalculoMediaFinal);
	}

	public void setFormulaCalculoMediaFinal(String formulaCalculoMediaFinal) {
		this.formulaCalculoMediaFinal = formulaCalculoMediaFinal;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public String getMascaraPadraoGeracaoMatricula() {
		if (mascaraPadraoGeracaoMatricula == null) {
			mascaraPadraoGeracaoMatricula = "";
		}
		return mascaraPadraoGeracaoMatricula;
	}

	public void setMascaraPadraoGeracaoMatricula(String mascaraPadraoGeracaoMatricula) {
		this.mascaraPadraoGeracaoMatricula = mascaraPadraoGeracaoMatricula;
	}

	public Boolean getNota1MediaFinal() {
		if (nota1MediaFinal == null) {
			nota1MediaFinal = false;
		}
		return nota1MediaFinal;
	}

	public void setNota1MediaFinal(Boolean nota1MediaFinal) {
		this.nota1MediaFinal = nota1MediaFinal;
	}

	public Boolean getNota10MediaFinal() {
		if (nota10MediaFinal == null) {
			nota10MediaFinal = false;
		}
		return nota10MediaFinal;
	}

	public void setNota10MediaFinal(Boolean nota10MediaFinal) {
		this.nota10MediaFinal = nota10MediaFinal;
	}

	public Boolean getNota2MediaFinal() {
		if (nota2MediaFinal == null) {
			nota2MediaFinal = false;
		}
		return nota2MediaFinal;
	}

	public void setNota2MediaFinal(Boolean nota2MediaFinal) {
		this.nota2MediaFinal = nota2MediaFinal;
	}

	public Boolean getNota3MediaFinal() {
		if (nota3MediaFinal == null) {
			nota3MediaFinal = false;
		}
		return nota3MediaFinal;
	}

	public void setNota3MediaFinal(Boolean nota3MediaFinal) {
		this.nota3MediaFinal = nota3MediaFinal;
	}

	public Boolean getNota4MediaFinal() {
		if (nota4MediaFinal == null) {
			nota4MediaFinal = false;
		}
		return nota4MediaFinal;
	}

	public void setNota4MediaFinal(Boolean nota4MediaFinal) {
		this.nota4MediaFinal = nota4MediaFinal;
	}

	public Boolean getNota5MediaFinal() {
		if (nota5MediaFinal == null) {
			nota5MediaFinal = false;
		}
		return nota5MediaFinal;
	}

	public void setNota5MediaFinal(Boolean nota5MediaFinal) {
		this.nota5MediaFinal = nota5MediaFinal;
	}

	public Boolean getNota6MediaFinal() {
		if (nota6MediaFinal == null) {
			nota6MediaFinal = false;
		}
		return nota6MediaFinal;
	}

	public void setNota6MediaFinal(Boolean nota6MediaFinal) {
		this.nota6MediaFinal = nota6MediaFinal;
	}

	public Boolean getNota7MediaFinal() {
		if (nota7MediaFinal == null) {
			nota7MediaFinal = false;
		}
		return nota7MediaFinal;
	}

	public void setNota7MediaFinal(Boolean nota7MediaFinal) {
		this.nota7MediaFinal = nota7MediaFinal;
	}

	public Boolean getNota8MediaFinal() {
		if (nota8MediaFinal == null) {
			nota8MediaFinal = false;
		}
		return nota8MediaFinal;
	}

	public void setNota8MediaFinal(Boolean nota8MediaFinal) {
		this.nota8MediaFinal = nota8MediaFinal;
	}

	public Boolean getNota9MediaFinal() {
		if (nota9MediaFinal == null) {
			nota9MediaFinal = false;
		}
		return nota9MediaFinal;
	}

	public void setNota9MediaFinal(Boolean nota9MediaFinal) {
		this.nota9MediaFinal = nota9MediaFinal;
	}

	/**
	 * @return the configuracoesVO
	 */
	public ConfiguracoesVO getConfiguracoesVO() {
		if (configuracoesVO == null) {
			configuracoesVO = new ConfiguracoesVO();
		}
		return configuracoesVO;
	}

	/**
	 * @param configuracoesVO
	 *            the configuracoesVO to set
	 */
	public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
		this.configuracoesVO = configuracoesVO;
	}

	public Integer getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() {
		if (numeroDisciplinaConsiderarReprovadoPeriodoLetivo == null) {
			numeroDisciplinaConsiderarReprovadoPeriodoLetivo = 0;
		}
		return numeroDisciplinaConsiderarReprovadoPeriodoLetivo;
	}

	public void setNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(Integer numeroDisciplinaConsiderarReprovadoPeriodoLetivo) {
		this.numeroDisciplinaConsiderarReprovadoPeriodoLetivo = numeroDisciplinaConsiderarReprovadoPeriodoLetivo;
	}

	public Boolean getReprovadoMatricularDisciplinaPeriodoLetivo() {
		if (reprovadoMatricularDisciplinaPeriodoLetivo == null) {
			reprovadoMatricularDisciplinaPeriodoLetivo = false;
		}
		return reprovadoMatricularDisciplinaPeriodoLetivo;
	}

	public void setReprovadoMatricularDisciplinaPeriodoLetivo(Boolean reprovadoMatricularDisciplinaPeriodoLetivo) {
		this.reprovadoMatricularDisciplinaPeriodoLetivo = reprovadoMatricularDisciplinaPeriodoLetivo;
	}

	public Boolean getIsPossuiControleDisciplinaReprovacao() {
		return getReprovadoMatricularDisciplinaPeriodoLetivo() || getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() > 0;
	}

	public Boolean getPermiteEvoluirPeriodoLetivoCasoReprovado() {
		if (permiteEvoluirPeriodoLetivoCasoReprovado == null) {
			permiteEvoluirPeriodoLetivoCasoReprovado = false;
		}
		return permiteEvoluirPeriodoLetivoCasoReprovado;
	}

	public void setPermiteEvoluirPeriodoLetivoCasoReprovado(Boolean permiteEvoluirPeriodoLetivoCasoReprovado) {
		this.permiteEvoluirPeriodoLetivoCasoReprovado = permiteEvoluirPeriodoLetivoCasoReprovado;
	}

	public Integer getDiasMaximoReativacaoMatricula() {
		if (diasMaximoReativacaoMatricula == null) {
			diasMaximoReativacaoMatricula = 0;
		}
		return diasMaximoReativacaoMatricula;
	}

	public void setDiasMaximoReativacaoMatricula(Integer diasMaximoReativacaoMatricula) {
		this.diasMaximoReativacaoMatricula = diasMaximoReativacaoMatricula;
	}
	
	
	public Boolean getObrigarAceiteAlunoTermoParaEditarRenovacao() {
		if (obrigarAceiteAlunoTermoParaEditarRenovacao == null) {
			obrigarAceiteAlunoTermoParaEditarRenovacao = false;
		}
		return obrigarAceiteAlunoTermoParaEditarRenovacao;
	}

	public void setObrigarAceiteAlunoTermoParaEditarRenovacao(Boolean obrigarAceiteAlunoTermoParaEditarRenovacao) {
		this.obrigarAceiteAlunoTermoParaEditarRenovacao = obrigarAceiteAlunoTermoParaEditarRenovacao;
	}	

	public Boolean getRenovacaoMatriculaSequencial() {
		if (renovacaoMatriculaSequencial == null) {
			renovacaoMatriculaSequencial = false;
		}
		return renovacaoMatriculaSequencial;
	}

	public void setRenovacaoMatriculaSequencial(Boolean renovacaoMatriculaSequencial) {
		this.renovacaoMatriculaSequencial = renovacaoMatriculaSequencial;
	}

	public Boolean getPermiteCursarDisciplinaEPreRequisito() {
		if (permiteCursarDisciplinaEPreRequisito == null) {
			permiteCursarDisciplinaEPreRequisito = false;
		}
		return permiteCursarDisciplinaEPreRequisito;
	}

	public void setPermiteCursarDisciplinaEPreRequisito(Boolean permiteCursarDisciplinaEPreRequisito) {
		this.permiteCursarDisciplinaEPreRequisito = permiteCursarDisciplinaEPreRequisito;
	}

	/**
	 * @return the apresentarNota
	 */
	public Boolean getApresentarNota1() {
		if (apresentarNota1 == null) {
			apresentarNota1 = Boolean.TRUE;
		}
		return apresentarNota1;
	}

	/**
	 * @param apresentarNota
	 *            the apresentarNota to set
	 */
	public void setApresentarNota1(Boolean apresentarNota1) {
		this.apresentarNota1 = apresentarNota1;
	}

	/**
	 * @return the apresentarNota2
	 */
	public Boolean getApresentarNota2() {
		if (apresentarNota2 == null) {
			apresentarNota2 = Boolean.TRUE;
		}
		return apresentarNota2;
	}

	/**
	 * @param apresentarNota2
	 *            the apresentarNota2 to set
	 */
	public void setApresentarNota2(Boolean apresentarNota2) {
		this.apresentarNota2 = apresentarNota2;
	}

	/**
	 * @return the apresentarNota3
	 */
	public Boolean getApresentarNota3() {
		if (apresentarNota3 == null) {
			apresentarNota3 = Boolean.TRUE;
		}
		return apresentarNota3;
	}

	/**
	 * @param apresentarNota3
	 *            the apresentarNota3 to set
	 */
	public void setApresentarNota3(Boolean apresentarNota3) {
		this.apresentarNota3 = apresentarNota3;
	}

	/**
	 * @return the apresentarNota4
	 */
	public Boolean getApresentarNota4() {
		if (apresentarNota4 == null) {
			apresentarNota4 = Boolean.TRUE;
		}
		return apresentarNota4;
	}

	/**
	 * @param apresentarNota4
	 *            the apresentarNota4 to set
	 */
	public void setApresentarNota4(Boolean apresentarNota4) {
		this.apresentarNota4 = apresentarNota4;
	}

	/**
	 * @return the apresentarNota5
	 */
	public Boolean getApresentarNota5() {
		if (apresentarNota5 == null) {
			apresentarNota5 = Boolean.TRUE;
		}
		return apresentarNota5;
	}

	/**
	 * @param apresentarNota5
	 *            the apresentarNota5 to set
	 */
	public void setApresentarNota5(Boolean apresentarNota5) {
		this.apresentarNota5 = apresentarNota5;
	}

	/**
	 * @return the apresentarNota6
	 */
	public Boolean getApresentarNota6() {
		if (apresentarNota6 == null) {
			apresentarNota6 = Boolean.TRUE;
		}
		return apresentarNota6;
	}

	/**
	 * @param apresentarNota6
	 *            the apresentarNota6 to set
	 */
	public void setApresentarNota6(Boolean apresentarNota6) {
		this.apresentarNota6 = apresentarNota6;
	}

	/**
	 * @return the apresentarNota7
	 */
	public Boolean getApresentarNota7() {
		if (apresentarNota7 == null) {
			apresentarNota7 = Boolean.TRUE;
		}
		return apresentarNota7;
	}

	/**
	 * @param apresentarNota7
	 *            the apresentarNota7 to set
	 */
	public void setApresentarNota7(Boolean apresentarNota7) {
		this.apresentarNota7 = apresentarNota7;
	}

	/**
	 * @return the apresentarNota8
	 */
	public Boolean getApresentarNota8() {
		if (apresentarNota8 == null) {
			apresentarNota8 = Boolean.TRUE;
		}
		return apresentarNota8;
	}

	/**
	 * @param apresentarNota8
	 *            the apresentarNota8 to set
	 */
	public void setApresentarNota8(Boolean apresentarNota8) {
		this.apresentarNota8 = apresentarNota8;
	}

	/**
	 * @return the apresentarNota9
	 */
	public Boolean getApresentarNota9() {
		if (apresentarNota9 == null) {
			apresentarNota9 = Boolean.TRUE;
		}
		return apresentarNota9;
	}

	/**
	 * @param apresentarNota9
	 *            the apresentarNota9 to set
	 */
	public void setApresentarNota9(Boolean apresentarNota9) {
		this.apresentarNota9 = apresentarNota9;
	}

	/**
	 * @return the apresentarNota10
	 */
	public Boolean getApresentarNota10() {
		if (apresentarNota10 == null) {
			apresentarNota10 = Boolean.TRUE;
		}
		return apresentarNota10;
	}

	/**
	 * @param apresentarNota10
	 *            the apresentarNota10 to set
	 */
	public void setApresentarNota10(Boolean apresentarNota10) {
		this.apresentarNota10 = apresentarNota10;
	}

	public Boolean getApresentarNota11() {
		if (apresentarNota11 == null) {
			apresentarNota11 = Boolean.TRUE;
		}
		return apresentarNota11;
	}

	public void setApresentarNota11(Boolean apresentarNota11) {
		this.apresentarNota11 = apresentarNota11;
	}

	public Boolean getApresentarNota12() {
		if (apresentarNota12 == null) {
			apresentarNota12 = Boolean.TRUE;
		}
		return apresentarNota12;
	}

	public void setApresentarNota12(Boolean apresentarNota12) {
		this.apresentarNota12 = apresentarNota12;
	}

	public Boolean getApresentarNota13() {
		if (apresentarNota13 == null) {
			apresentarNota13 = Boolean.TRUE;
		}
		return apresentarNota13;
	}

	public void setApresentarNota13(Boolean apresentarNota13) {
		this.apresentarNota13 = apresentarNota13;
	}

	public void setNotasDeCincoEmCincoDecimos(Boolean notasDeCincoEmCincoDecimos) {
		this.notasDeCincoEmCincoDecimos = notasDeCincoEmCincoDecimos;
	}

	public Boolean getNotasDeCincoEmCincoDecimos() {
		if (notasDeCincoEmCincoDecimos == null) {
			notasDeCincoEmCincoDecimos = Boolean.FALSE;
		}
		return notasDeCincoEmCincoDecimos;
	}

	public Boolean getLimitarQtdeDiasMaxDownload() {
		if (limitarQtdeDiasMaxDownload == null) {
			limitarQtdeDiasMaxDownload = false;
		}
		return limitarQtdeDiasMaxDownload;
	}

	public void setLimitarQtdeDiasMaxDownload(Boolean limitarQtdeDiasMaxDownload) {
		this.limitarQtdeDiasMaxDownload = limitarQtdeDiasMaxDownload;
	}

	public Integer getQtdeMaxDiasDownload() {
		if (qtdeMaxDiasDownload == null) {
			qtdeMaxDiasDownload = 0;
		}
		return qtdeMaxDiasDownload;
	}

	public void setQtdeMaxDiasDownload(Integer qtdeMaxDiasDownload) {
		this.qtdeMaxDiasDownload = qtdeMaxDiasDownload;
	}

	public Boolean getNotasDeCincoEmCincoDecimosApenasMedia() {
		if (notasDeCincoEmCincoDecimosApenasMedia == null) {
			notasDeCincoEmCincoDecimosApenasMedia = Boolean.FALSE;
		}
		return notasDeCincoEmCincoDecimosApenasMedia;
	}

	public void setNotasDeCincoEmCincoDecimosApenasMedia(Boolean notasDeCincoEmCincoDecimosApenasMedia) {
		this.notasDeCincoEmCincoDecimosApenasMedia = notasDeCincoEmCincoDecimosApenasMedia;
	}

	public Double getNota11() {
		return nota11;
	}

	public void setNota11(Double nota11) {
		this.nota11 = nota11;
	}

	public Boolean getUtilizarNota11() {
		return utilizarNota11;
	}

	public Boolean isUtilizarNota11() {
		return (utilizarNota11);
	}

	public void setUtilizarNota11(Boolean utilizarNota11) {
		this.utilizarNota11 = utilizarNota11;
	}

	public String getTituloNota11() {
		if (tituloNota11 == null) {
			tituloNota11 = "";
		}
		return tituloNota11;
	}

	public void setTituloNota11(String tituloNota11) {
		this.tituloNota11 = tituloNota11;
	}

	public String getFormulaCalculoNota11() {
		if (formulaCalculoNota11 == null) {
			formulaCalculoNota11 = "";
		}
		return formulaCalculoNota11;
	}

	public void setFormulaCalculoNota11(String formulaCalculoNota11) {
		this.formulaCalculoNota11 = formulaCalculoNota11;
	}

	public String getFormulaUsoNota11() {
		if (formulaUsoNota11 == null) {
			formulaUsoNota11 = "";
		}
		return formulaUsoNota11;
	}

	public void setFormulaUsoNota11(String formulaUsoNota11) {
		this.formulaUsoNota11 = formulaUsoNota11;
	}

	public Boolean getNota11MediaFinal() {
		if (nota11MediaFinal == null) {
			nota11MediaFinal = false;
		}
		return nota11MediaFinal;
	}

	public void setNota11MediaFinal(Boolean nota11MediaFinal) {
		this.nota11MediaFinal = nota11MediaFinal;
	}

	public Double getNota12() {
		return nota12;
	}

	public void setNota12(Double nota12) {
		this.nota12 = nota12;
	}

	public Boolean getUtilizarNota12() {
		return utilizarNota12;
	}

	public Boolean isUtilizarNota12() {
		return (utilizarNota12);
	}

	public void setUtilizarNota12(Boolean utilizarNota12) {
		this.utilizarNota12 = utilizarNota12;
	}

	public String getTituloNota12() {
		if (tituloNota12 == null) {
			tituloNota12 = "";
		}
		return tituloNota12;
	}

	public void setTituloNota12(String tituloNota12) {
		this.tituloNota12 = tituloNota12;
	}

	public String getFormulaCalculoNota12() {
		if (formulaCalculoNota12 == null) {
			formulaCalculoNota12 = "";
		}
		return formulaCalculoNota12;
	}

	public void setFormulaCalculoNota12(String formulaCalculoNota12) {
		this.formulaCalculoNota12 = formulaCalculoNota12;
	}

	public String getFormulaUsoNota12() {
		if (formulaUsoNota12 == null) {
			formulaUsoNota12 = "";
		}
		return formulaUsoNota12;
	}

	public void setFormulaUsoNota12(String formulaUsoNota12) {
		this.formulaUsoNota12 = formulaUsoNota12;
	}

	public Boolean getNota12MediaFinal() {
		if (nota12MediaFinal == null) {
			nota12MediaFinal = false;
		}
		return nota12MediaFinal;
	}

	public void setNota12MediaFinal(Boolean nota12MediaFinal) {
		this.nota12MediaFinal = nota12MediaFinal;
	}

	public Double getNota13() {
		return nota13;
	}

	public void setNota13(Double nota13) {
		this.nota13 = nota13;
	}

	public Boolean getUtilizarNota13() {
		return utilizarNota13;
	}

	public Boolean isUtilizarNota13() {
		return (utilizarNota13);
	}

	public void setUtilizarNota13(Boolean utilizarNota13) {
		this.utilizarNota13 = utilizarNota13;
	}

	public String getTituloNota13() {
		if (tituloNota13 == null) {
			tituloNota13 = "";
		}
		return tituloNota13;
	}

	public void setTituloNota13(String tituloNota13) {
		this.tituloNota13 = tituloNota13;
	}

	public String getFormulaCalculoNota13() {
		if (formulaCalculoNota13 == null) {
			formulaCalculoNota13 = "";
		}
		return formulaCalculoNota13;
	}

	public void setFormulaCalculoNota13(String formulaCalculoNota13) {
		this.formulaCalculoNota13 = formulaCalculoNota13;
	}

	public String getFormulaUsoNota13() {
		if (formulaUsoNota13 == null) {
			formulaUsoNota13 = "";
		}
		return formulaUsoNota13;
	}

	public void setFormulaUsoNota13(String formulaUsoNota13) {
		this.formulaUsoNota13 = formulaUsoNota13;
	}

	public Boolean getNota13MediaFinal() {
		if (nota13MediaFinal == null) {
			nota13MediaFinal = false;
		}
		return nota13MediaFinal;
	}

	public void setNota13MediaFinal(Boolean nota13MediaFinal) {
		this.nota13MediaFinal = nota13MediaFinal;
	}

	public Boolean getEnviarMensagemNotaAbaixoMedia() {
		if (enviarMensagemNotaAbaixoMedia == null) {
			enviarMensagemNotaAbaixoMedia = Boolean.FALSE;
		}
		return enviarMensagemNotaAbaixoMedia;
	}

	public void setEnviarMensagemNotaAbaixoMedia(Boolean enviarMensagemNotaAbaixoMedia) {
		this.enviarMensagemNotaAbaixoMedia = enviarMensagemNotaAbaixoMedia;
	}

	public Boolean getApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico() {
		if (apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico == null) {
			apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico = Boolean.FALSE;
		}
		return apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico;
	}

	public void setApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico(Boolean apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico) {
		this.apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico = apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico;
	}

	public Double getPesoMediaNotaMeritoAcademico() {
		if (pesoMediaNotaMeritoAcademico == null) {
			pesoMediaNotaMeritoAcademico = 0.00;
		}
		return pesoMediaNotaMeritoAcademico;
	}

	public void setPesoMediaNotaMeritoAcademico(Double pesoMediaNotaMeritoAcademico) {
		this.pesoMediaNotaMeritoAcademico = pesoMediaNotaMeritoAcademico;
	}

	public Double getPesoMediaFrequenciaMeritoAcademico() {
		if (pesoMediaFrequenciaMeritoAcademico == null) {
			pesoMediaFrequenciaMeritoAcademico = 0.00;
		}
		return pesoMediaFrequenciaMeritoAcademico;
	}

	public void setPesoMediaFrequenciaMeritoAcademico(Double pesoMediaFrequenciaMeritoAcademico) {
		this.pesoMediaFrequenciaMeritoAcademico = pesoMediaFrequenciaMeritoAcademico;
	}

	public Boolean getUsarSituacaoAprovadoAproveitamentoTransferenciaGrade() {
		if (usarSituacaoAprovadoAproveitamentoTransferenciaGrade == null) {
			usarSituacaoAprovadoAproveitamentoTransferenciaGrade = Boolean.FALSE;
		}
		return usarSituacaoAprovadoAproveitamentoTransferenciaGrade;
	}

	public void setUsarSituacaoAprovadoAproveitamentoTransferenciaGrade(Boolean usarSituacaoAprovadoAproveitamentoTransferenciaGrade) {
		this.usarSituacaoAprovadoAproveitamentoTransferenciaGrade = usarSituacaoAprovadoAproveitamentoTransferenciaGrade;
	}


	public Boolean getConsiderarCampoNuloNotaZerada() {
		if (considerarCampoNuloNotaZerada == null) {
			considerarCampoNuloNotaZerada = Boolean.FALSE;
		}
		return considerarCampoNuloNotaZerada;
	}

	public void setConsiderarCampoNuloNotaZerada(Boolean considerarCampoNuloNotaZerada) {
		this.considerarCampoNuloNotaZerada = considerarCampoNuloNotaZerada;
	}

	public Boolean getLiberarPreRequisitoDisciplinaConcomitancia() {
		if (liberarPreRequisitoDisciplinaConcomitancia == null) {
			liberarPreRequisitoDisciplinaConcomitancia = Boolean.FALSE;
		}
		return liberarPreRequisitoDisciplinaConcomitancia;
	}

	public void setLiberarPreRequisitoDisciplinaConcomitancia(Boolean liberarPreRequisitoDisciplinaConcomitancia) {
		this.liberarPreRequisitoDisciplinaConcomitancia = liberarPreRequisitoDisciplinaConcomitancia;
	}

	public Boolean getApresentarTextoSemNotaCampoNuloHistorico() {
		if (apresentarTextoSemNotaCampoNuloHistorico == null) {
			apresentarTextoSemNotaCampoNuloHistorico = Boolean.FALSE;
		}
		return apresentarTextoSemNotaCampoNuloHistorico;
	}

	public void setApresentarTextoSemNotaCampoNuloHistorico(Boolean apresentarTextoSemNotaCampoNuloHistorico) {
		this.apresentarTextoSemNotaCampoNuloHistorico = apresentarTextoSemNotaCampoNuloHistorico;
	}

	public Boolean getUtilizarNota1PorConceito() {
		if (utilizarNota1PorConceito == null) {
			utilizarNota1PorConceito = false;
		}
		return utilizarNota1PorConceito;
	}

	public void setUtilizarNota1PorConceito(Boolean utilizarNota1PorConceito) {
		this.utilizarNota1PorConceito = utilizarNota1PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota1ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota1ConceitoVOs() {
		if (configuracaoAcademicoNota1ConceitoVOs == null) {
			configuracaoAcademicoNota1ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota1ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota1ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota1ConceitoVOs) {
		this.configuracaoAcademicoNota1ConceitoVOs = configuracaoAcademicoNota1ConceitoVOs;
	}

	public Boolean getUtilizarNota2PorConceito() {
		if (utilizarNota2PorConceito == null) {
			utilizarNota2PorConceito = false;
		}
		return utilizarNota2PorConceito;
	}

	public void setUtilizarNota2PorConceito(Boolean utilizarNota2PorConceito) {
		this.utilizarNota2PorConceito = utilizarNota2PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota2ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota2ConceitoVOs() {
		if (configuracaoAcademicoNota2ConceitoVOs == null) {
			configuracaoAcademicoNota2ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota2ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota2ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota2ConceitoVOs) {
		this.configuracaoAcademicoNota2ConceitoVOs = configuracaoAcademicoNota2ConceitoVOs;
	}

	public Boolean getUtilizarNota3PorConceito() {
		if (utilizarNota3PorConceito == null) {
			utilizarNota3PorConceito = false;
		}
		return utilizarNota3PorConceito;
	}

	public void setUtilizarNota3PorConceito(Boolean utilizarNota3PorConceito) {
		this.utilizarNota3PorConceito = utilizarNota3PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota3ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota3ConceitoVOs() {
		if (configuracaoAcademicoNota3ConceitoVOs == null) {
			configuracaoAcademicoNota3ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota3ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota3ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota3ConceitoVOs) {
		this.configuracaoAcademicoNota3ConceitoVOs = configuracaoAcademicoNota3ConceitoVOs;
	}

	public Boolean getUtilizarNota4PorConceito() {
		if (utilizarNota4PorConceito == null) {
			utilizarNota4PorConceito = false;
		}
		return utilizarNota4PorConceito;
	}

	public void setUtilizarNota4PorConceito(Boolean utilizarNota4PorConceito) {
		this.utilizarNota4PorConceito = utilizarNota4PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota4ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota4ConceitoVOs() {
		if (configuracaoAcademicoNota4ConceitoVOs == null) {
			configuracaoAcademicoNota4ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota4ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota4ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota4ConceitoVOs) {
		this.configuracaoAcademicoNota4ConceitoVOs = configuracaoAcademicoNota4ConceitoVOs;
	}

	public Boolean getUtilizarNota5PorConceito() {
		if (utilizarNota5PorConceito == null) {
			utilizarNota5PorConceito = false;
		}
		return utilizarNota5PorConceito;
	}

	public void setUtilizarNota5PorConceito(Boolean utilizarNota5PorConceito) {
		this.utilizarNota5PorConceito = utilizarNota5PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota5ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota5ConceitoVOs() {
		if (configuracaoAcademicoNota5ConceitoVOs == null) {
			configuracaoAcademicoNota5ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota5ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota5ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota5ConceitoVOs) {
		this.configuracaoAcademicoNota5ConceitoVOs = configuracaoAcademicoNota5ConceitoVOs;
	}

	public Boolean getUtilizarNota6PorConceito() {
		if (utilizarNota6PorConceito == null) {
			utilizarNota6PorConceito = false;
		}
		return utilizarNota6PorConceito;
	}

	public void setUtilizarNota6PorConceito(Boolean utilizarNota6PorConceito) {
		this.utilizarNota6PorConceito = utilizarNota6PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota6ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota6ConceitoVOs() {
		if (configuracaoAcademicoNota6ConceitoVOs == null) {
			configuracaoAcademicoNota6ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota6ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota6ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota6ConceitoVOs) {
		this.configuracaoAcademicoNota6ConceitoVOs = configuracaoAcademicoNota6ConceitoVOs;
	}

	public Boolean getUtilizarNota7PorConceito() {
		if (utilizarNota7PorConceito == null) {
			utilizarNota7PorConceito = false;
		}
		return utilizarNota7PorConceito;
	}

	public void setUtilizarNota7PorConceito(Boolean utilizarNota7PorConceito) {
		this.utilizarNota7PorConceito = utilizarNota7PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota7ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota7ConceitoVOs() {
		if (configuracaoAcademicoNota7ConceitoVOs == null) {
			configuracaoAcademicoNota7ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota7ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota7ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota7ConceitoVOs) {
		this.configuracaoAcademicoNota7ConceitoVOs = configuracaoAcademicoNota7ConceitoVOs;
	}

	public Boolean getUtilizarNota8PorConceito() {
		if (utilizarNota8PorConceito == null) {
			utilizarNota8PorConceito = false;
		}
		return utilizarNota8PorConceito;
	}

	public void setUtilizarNota8PorConceito(Boolean utilizarNota8PorConceito) {
		this.utilizarNota8PorConceito = utilizarNota8PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota8ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota8ConceitoVOs() {
		if (configuracaoAcademicoNota8ConceitoVOs == null) {
			configuracaoAcademicoNota8ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota8ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota8ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota8ConceitoVOs) {
		this.configuracaoAcademicoNota8ConceitoVOs = configuracaoAcademicoNota8ConceitoVOs;
	}

	public Boolean getUtilizarNota9PorConceito() {
		if (utilizarNota9PorConceito == null) {
			utilizarNota9PorConceito = false;
		}
		return utilizarNota9PorConceito;
	}

	public void setUtilizarNota9PorConceito(Boolean utilizarNota9PorConceito) {
		this.utilizarNota9PorConceito = utilizarNota9PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota9ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota9ConceitoVOs() {
		if (configuracaoAcademicoNota9ConceitoVOs == null) {
			configuracaoAcademicoNota9ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota9ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota9ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota9ConceitoVOs) {
		this.configuracaoAcademicoNota9ConceitoVOs = configuracaoAcademicoNota9ConceitoVOs;
	}

	public Boolean getUtilizarNota10PorConceito() {
		if (utilizarNota10PorConceito == null) {
			utilizarNota10PorConceito = false;
		}
		return utilizarNota10PorConceito;
	}

	public void setUtilizarNota10PorConceito(Boolean utilizarNota10PorConceito) {
		this.utilizarNota10PorConceito = utilizarNota10PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota10ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota10ConceitoVOs() {
		if (configuracaoAcademicoNota10ConceitoVOs == null) {
			configuracaoAcademicoNota10ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota10ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota10ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota10ConceitoVOs) {
		this.configuracaoAcademicoNota10ConceitoVOs = configuracaoAcademicoNota10ConceitoVOs;
	}

	public Boolean getUtilizarNota11PorConceito() {
		if (utilizarNota11PorConceito == null) {
			utilizarNota11PorConceito = false;
		}
		return utilizarNota11PorConceito;
	}

	public void setUtilizarNota11PorConceito(Boolean utilizarNota11PorConceito) {
		this.utilizarNota11PorConceito = utilizarNota11PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota11ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota11ConceitoVOs() {
		if (configuracaoAcademicoNota11ConceitoVOs == null) {
			configuracaoAcademicoNota11ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota11ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota11ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota11ConceitoVOs) {
		this.configuracaoAcademicoNota11ConceitoVOs = configuracaoAcademicoNota11ConceitoVOs;
	}

	public Boolean getUtilizarNota12PorConceito() {
		if (utilizarNota12PorConceito == null) {
			utilizarNota12PorConceito = false;
		}
		return utilizarNota12PorConceito;
	}

	public void setUtilizarNota12PorConceito(Boolean utilizarNota12PorConceito) {
		this.utilizarNota12PorConceito = utilizarNota12PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota12ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota12ConceitoVOs() {
		if (configuracaoAcademicoNota12ConceitoVOs == null) {
			configuracaoAcademicoNota12ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota12ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota12ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota12ConceitoVOs) {
		this.configuracaoAcademicoNota12ConceitoVOs = configuracaoAcademicoNota12ConceitoVOs;
	}

	public Boolean getUtilizarNota13PorConceito() {
		if (utilizarNota13PorConceito == null) {
			utilizarNota13PorConceito = false;
		}
		return utilizarNota13PorConceito;
	}

	public void setUtilizarNota13PorConceito(Boolean utilizarNota13PorConceito) {
		this.utilizarNota13PorConceito = utilizarNota13PorConceito;
	}

	@XmlElement(name = "configuracaoAcademicoNota13ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota13ConceitoVOs() {
		if (configuracaoAcademicoNota13ConceitoVOs == null) {
			configuracaoAcademicoNota13ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota13ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota13ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota13ConceitoVOs) {
		this.configuracaoAcademicoNota13ConceitoVOs = configuracaoAcademicoNota13ConceitoVOs;
	}

	public Boolean getNota1TipoLancamento() {
		if (!getUtilizarNota1()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva1()) || (getFormulaCalculoNota1().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota2TipoLancamento() {
		if (!getUtilizarNota2()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva2()) || (getFormulaCalculoNota2().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota3TipoLancamento() {
		if (!getUtilizarNota3()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva3()) || (getFormulaCalculoNota3().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota4TipoLancamento() {
		if (!getUtilizarNota4()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva4()) || (getFormulaCalculoNota4().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota5TipoLancamento() {
		if (!getUtilizarNota5()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva5()) || (getFormulaCalculoNota5().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota6TipoLancamento() {
		if (!getUtilizarNota6()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva6()) || (getFormulaCalculoNota6().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota7TipoLancamento() {
		if (!getUtilizarNota7()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva7()) || (getFormulaCalculoNota7().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota8TipoLancamento() {
		if (!getUtilizarNota8()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva8()) || (getFormulaCalculoNota8().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota9TipoLancamento() {
		if (!getUtilizarNota9()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva9()) || (getFormulaCalculoNota9().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota10TipoLancamento() {
		if (!getUtilizarNota10()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva10()) || (getFormulaCalculoNota10().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota11TipoLancamento() {
		if (!getUtilizarNota11()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva11()) || (getFormulaCalculoNota11().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota12TipoLancamento() {
		if (!getUtilizarNota12()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva12()) || (getFormulaCalculoNota12().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota13TipoLancamento() {
		if (!getUtilizarNota13()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva13()) || (getFormulaCalculoNota13().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota14TipoLancamento() {
		if (!getUtilizarNota14()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva14()) || (getFormulaCalculoNota14().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota15TipoLancamento() {
		if (!getUtilizarNota15()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva15()) || (getFormulaCalculoNota15().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota16TipoLancamento() {
		if (!getUtilizarNota16()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva16()) || (getFormulaCalculoNota16().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota17TipoLancamento() {
		if (!getUtilizarNota17()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva17()) || (getFormulaCalculoNota17().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota18TipoLancamento() {
		if (!getUtilizarNota18()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva18()) || (getFormulaCalculoNota18().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota19TipoLancamento() {
		if (!getUtilizarNota19()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva19()) || (getFormulaCalculoNota19().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota20TipoLancamento() {
		if (!getUtilizarNota20()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva20()) || (getFormulaCalculoNota20().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota21TipoLancamento() {
		if (!getUtilizarNota21()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva21()) || (getFormulaCalculoNota21().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota22TipoLancamento() {
		if (!getUtilizarNota22()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva22()) || (getFormulaCalculoNota22().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota23TipoLancamento() {
		if (!getUtilizarNota23()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva23()) || (getFormulaCalculoNota23().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota24TipoLancamento() {
		if (!getUtilizarNota24()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva24()) || (getFormulaCalculoNota24().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota25TipoLancamento() {
		if (!getUtilizarNota25()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva25()) || (getFormulaCalculoNota25().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota26TipoLancamento() {
		if (!getUtilizarNota26()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva26()) || (getFormulaCalculoNota26().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota27TipoLancamento() {
		if (!getUtilizarNota27()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva27()) || (getFormulaCalculoNota27().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota28TipoLancamento() {
		if (!getUtilizarNota28()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva28()) || (getFormulaCalculoNota28().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota29TipoLancamento() {
		if (!getUtilizarNota29()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva29()) || (getFormulaCalculoNota29().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	public Boolean getNota30TipoLancamento() {
		if (!getUtilizarNota30()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva30()) || (getFormulaCalculoNota30().trim().isEmpty())) {
			return true;
		}
		return false;
	}

	/**
	 * @return the utilizarComoSubstitutiva1
	 */
	public Boolean getUtilizarComoSubstitutiva1() {
		if (utilizarComoSubstitutiva1 == null) {
			utilizarComoSubstitutiva1 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva1;
	}

	/**
	 * @param utilizarComoSubstitutiva1
	 *            the utilizarComoSubstitutiva1 to set
	 */
	public void setUtilizarComoSubstitutiva1(Boolean utilizarComoSubstitutiva1) {
		this.utilizarComoSubstitutiva1 = utilizarComoSubstitutiva1;
	}

	/**
	 * @return the utilizarComoSubstitutiva2
	 */
	public Boolean getUtilizarComoSubstitutiva2() {
		if (utilizarComoSubstitutiva2 == null) {
			utilizarComoSubstitutiva2 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva2;
	}

	/**
	 * @param utilizarComoSubstitutiva2
	 *            the utilizarComoSubstitutiva2 to set
	 */
	public void setUtilizarComoSubstitutiva2(Boolean utilizarComoSubstitutiva2) {
		this.utilizarComoSubstitutiva2 = utilizarComoSubstitutiva2;
	}

	/**
	 * @return the utilizarComoSubstitutiva3
	 */
	public Boolean getUtilizarComoSubstitutiva3() {
		if (utilizarComoSubstitutiva3 == null) {
			utilizarComoSubstitutiva3 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva3;
	}

	/**
	 * @param utilizarComoSubstitutiva3
	 *            the utilizarComoSubstitutiva3 to set
	 */
	public void setUtilizarComoSubstitutiva3(Boolean utilizarComoSubstitutiva3) {
		this.utilizarComoSubstitutiva3 = utilizarComoSubstitutiva3;
	}

	/**
	 * @return the utilizarComoSubstitutiva4
	 */
	public Boolean getUtilizarComoSubstitutiva4() {
		if (utilizarComoSubstitutiva4 == null) {
			utilizarComoSubstitutiva4 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva4;
	}

	/**
	 * @param utilizarComoSubstitutiva4
	 *            the utilizarComoSubstitutiva4 to set
	 */
	public void setUtilizarComoSubstitutiva4(Boolean utilizarComoSubstitutiva4) {
		this.utilizarComoSubstitutiva4 = utilizarComoSubstitutiva4;
	}

	/**
	 * @return the utilizarComoSubstitutiva5
	 */
	public Boolean getUtilizarComoSubstitutiva5() {
		if (utilizarComoSubstitutiva5 == null) {
			utilizarComoSubstitutiva5 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva5;
	}

	/**
	 * @param utilizarComoSubstitutiva5
	 *            the utilizarComoSubstitutiva5 to set
	 */
	public void setUtilizarComoSubstitutiva5(Boolean utilizarComoSubstitutiva5) {
		this.utilizarComoSubstitutiva5 = utilizarComoSubstitutiva5;
	}

	/**
	 * @return the utilizarComoSubstitutiva6
	 */
	public Boolean getUtilizarComoSubstitutiva6() {
		if (utilizarComoSubstitutiva6 == null) {
			utilizarComoSubstitutiva6 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva6;
	}

	/**
	 * @param utilizarComoSubstitutiva6
	 *            the utilizarComoSubstitutiva6 to set
	 */
	public void setUtilizarComoSubstitutiva6(Boolean utilizarComoSubstitutiva6) {
		this.utilizarComoSubstitutiva6 = utilizarComoSubstitutiva6;
	}

	/**
	 * @return the utilizarComoSubstitutiva7
	 */
	public Boolean getUtilizarComoSubstitutiva7() {
		if (utilizarComoSubstitutiva7 == null) {
			utilizarComoSubstitutiva7 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva7;
	}

	/**
	 * @param utilizarComoSubstitutiva7
	 *            the utilizarComoSubstitutiva7 to set
	 */
	public void setUtilizarComoSubstitutiva7(Boolean utilizarComoSubstitutiva7) {
		this.utilizarComoSubstitutiva7 = utilizarComoSubstitutiva7;
	}

	/**
	 * @return the utilizarComoSubstitutiva8
	 */
	public Boolean getUtilizarComoSubstitutiva8() {
		if (utilizarComoSubstitutiva8 == null) {
			utilizarComoSubstitutiva8 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva8;
	}

	/**
	 * @param utilizarComoSubstitutiva8
	 *            the utilizarComoSubstitutiva8 to set
	 */
	public void setUtilizarComoSubstitutiva8(Boolean utilizarComoSubstitutiva8) {
		this.utilizarComoSubstitutiva8 = utilizarComoSubstitutiva8;
	}

	/**
	 * @return the utilizarComoSubstitutiva9
	 */
	public Boolean getUtilizarComoSubstitutiva9() {
		if (utilizarComoSubstitutiva9 == null) {
			utilizarComoSubstitutiva9 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva9;
	}

	/**
	 * @param utilizarComoSubstitutiva9
	 *            the utilizarComoSubstitutiva9 to set
	 */
	public void setUtilizarComoSubstitutiva9(Boolean utilizarComoSubstitutiva9) {
		this.utilizarComoSubstitutiva9 = utilizarComoSubstitutiva9;
	}

	/**
	 * @return the utilizarComoSubstitutiva10
	 */
	public Boolean getUtilizarComoSubstitutiva10() {
		if (utilizarComoSubstitutiva10 == null) {
			utilizarComoSubstitutiva10 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva10;
	}

	/**
	 * @param utilizarComoSubstitutiva10
	 *            the utilizarComoSubstitutiva10 to set
	 */
	public void setUtilizarComoSubstitutiva10(Boolean utilizarComoSubstitutiva10) {
		this.utilizarComoSubstitutiva10 = utilizarComoSubstitutiva10;
	}

	/**
	 * @return the utilizarComoSubstitutiva11
	 */
	public Boolean getUtilizarComoSubstitutiva11() {
		if (utilizarComoSubstitutiva11 == null) {
			utilizarComoSubstitutiva11 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva11;
	}

	/**
	 * @param utilizarComoSubstitutiva11
	 *            the utilizarComoSubstitutiva11 to set
	 */
	public void setUtilizarComoSubstitutiva11(Boolean utilizarComoSubstitutiva11) {
		this.utilizarComoSubstitutiva11 = utilizarComoSubstitutiva11;
	}

	/**
	 * @return the utilizarComoSubstitutiva12
	 */
	public Boolean getUtilizarComoSubstitutiva12() {
		if (utilizarComoSubstitutiva12 == null) {
			utilizarComoSubstitutiva12 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva12;
	}

	/**
	 * @param utilizarComoSubstitutiva12
	 *            the utilizarComoSubstitutiva12 to set
	 */
	public void setUtilizarComoSubstitutiva12(Boolean utilizarComoSubstitutiva12) {
		this.utilizarComoSubstitutiva12 = utilizarComoSubstitutiva12;
	}

	/**
	 * @return the utilizarComoSubstitutiva13
	 */
	public Boolean getUtilizarComoSubstitutiva13() {
		if (utilizarComoSubstitutiva13 == null) {
			utilizarComoSubstitutiva13 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva13;
	}

	/**
	 * @param utilizarComoSubstitutiva13
	 *            the utilizarComoSubstitutiva13 to set
	 */
	public void setUtilizarComoSubstitutiva13(Boolean utilizarComoSubstitutiva13) {
		this.utilizarComoSubstitutiva13 = utilizarComoSubstitutiva13;
	}

	/**
	 * @return the politicaSubstitutiva1
	 */
	public String getPoliticaSubstitutiva1() {
		if (politicaSubstitutiva1 == null) {
			politicaSubstitutiva1 = "";
		}
		return politicaSubstitutiva1;
	}

	/**
	 * @param politicaSubstitutiva1
	 *            the politicaSubstitutiva1 to set
	 */
	public void setPoliticaSubstitutiva1(String politicaSubstitutiva1) {
		this.politicaSubstitutiva1 = politicaSubstitutiva1;
	}

	/**
	 * @return the politicaSubstitutiva2
	 */
	public String getPoliticaSubstitutiva2() {
		if (politicaSubstitutiva2 == null) {
			politicaSubstitutiva2 = "";
		}
		return politicaSubstitutiva2;
	}

	/**
	 * @param politicaSubstitutiva2
	 *            the politicaSubstitutiva2 to set
	 */
	public void setPoliticaSubstitutiva2(String politicaSubstitutiva2) {
		this.politicaSubstitutiva2 = politicaSubstitutiva2;
	}

	/**
	 * @return the politicaSubstitutiva3
	 */
	public String getPoliticaSubstitutiva3() {
		if (politicaSubstitutiva3 == null) {
			politicaSubstitutiva3 = "";
		}
		return politicaSubstitutiva3;
	}

	/**
	 * @param politicaSubstitutiva3
	 *            the politicaSubstitutiva3 to set
	 */
	public void setPoliticaSubstitutiva3(String politicaSubstitutiva3) {
		this.politicaSubstitutiva3 = politicaSubstitutiva3;
	}

	/**
	 * @return the politicaSubstitutiva4
	 */
	public String getPoliticaSubstitutiva4() {
		if (politicaSubstitutiva4 == null) {
			politicaSubstitutiva4 = "";
		}
		return politicaSubstitutiva4;
	}

	/**
	 * @param politicaSubstitutiva4
	 *            the politicaSubstitutiva4 to set
	 */
	public void setPoliticaSubstitutiva4(String politicaSubstitutiva4) {
		this.politicaSubstitutiva4 = politicaSubstitutiva4;
	}

	/**
	 * @return the politicaSubstitutiva5
	 */
	public String getPoliticaSubstitutiva5() {
		if (politicaSubstitutiva5 == null) {
			politicaSubstitutiva5 = "";
		}
		return politicaSubstitutiva5;
	}

	/**
	 * @param politicaSubstitutiva5
	 *            the politicaSubstitutiva5 to set
	 */
	public void setPoliticaSubstitutiva5(String politicaSubstitutiva5) {
		this.politicaSubstitutiva5 = politicaSubstitutiva5;
	}

	/**
	 * @return the politicaSubstitutiva6
	 */
	public String getPoliticaSubstitutiva6() {
		if (politicaSubstitutiva6 == null) {
			politicaSubstitutiva6 = "";
		}
		return politicaSubstitutiva6;
	}

	/**
	 * @param politicaSubstitutiva6
	 *            the politicaSubstitutiva6 to set
	 */
	public void setPoliticaSubstitutiva6(String politicaSubstitutiva6) {
		this.politicaSubstitutiva6 = politicaSubstitutiva6;
	}

	/**
	 * @return the politicaSubstitutiva7
	 */
	public String getPoliticaSubstitutiva7() {
		if (politicaSubstitutiva7 == null) {
			politicaSubstitutiva7 = "";
		}
		return politicaSubstitutiva7;
	}

	/**
	 * @param politicaSubstitutiva7
	 *            the politicaSubstitutiva7 to set
	 */
	public void setPoliticaSubstitutiva7(String politicaSubstitutiva7) {
		this.politicaSubstitutiva7 = politicaSubstitutiva7;
	}

	/**
	 * @return the politicaSubstitutiva8
	 */
	public String getPoliticaSubstitutiva8() {
		if (politicaSubstitutiva8 == null) {
			politicaSubstitutiva8 = "";
		}
		return politicaSubstitutiva8;
	}

	/**
	 * @param politicaSubstitutiva8
	 *            the politicaSubstitutiva8 to set
	 */
	public void setPoliticaSubstitutiva8(String politicaSubstitutiva8) {
		this.politicaSubstitutiva8 = politicaSubstitutiva8;
	}

	/**
	 * @return the politicaSubstitutiva9
	 */
	public String getPoliticaSubstitutiva9() {
		if (politicaSubstitutiva9 == null) {
			politicaSubstitutiva9 = "";
		}
		return politicaSubstitutiva9;
	}

	/**
	 * @param politicaSubstitutiva9
	 *            the politicaSubstitutiva9 to set
	 */
	public void setPoliticaSubstitutiva9(String politicaSubstitutiva9) {
		this.politicaSubstitutiva9 = politicaSubstitutiva9;
	}

	/**
	 * @return the politicaSubstitutiva10
	 */
	public String getPoliticaSubstitutiva10() {
		if (politicaSubstitutiva10 == null) {
			politicaSubstitutiva10 = "";
		}
		return politicaSubstitutiva10;
	}

	/**
	 * @param politicaSubstitutiva10
	 *            the politicaSubstitutiva10 to set
	 */
	public void setPoliticaSubstitutiva10(String politicaSubstitutiva10) {
		this.politicaSubstitutiva10 = politicaSubstitutiva10;
	}

	/**
	 * @return the politicaSubstitutiva11
	 */
	public String getPoliticaSubstitutiva11() {
		if (politicaSubstitutiva11 == null) {
			politicaSubstitutiva11 = "";
		}
		return politicaSubstitutiva11;
	}

	/**
	 * @param politicaSubstitutiva11
	 *            the politicaSubstitutiva11 to set
	 */
	public void setPoliticaSubstitutiva11(String politicaSubstitutiva11) {
		this.politicaSubstitutiva11 = politicaSubstitutiva11;
	}

	/**
	 * @return the politicaSubstitutiva12
	 */
	public String getPoliticaSubstitutiva12() {
		if (politicaSubstitutiva12 == null) {
			politicaSubstitutiva12 = "";
		}
		return politicaSubstitutiva12;
	}

	/**
	 * @param politicaSubstitutiva12
	 *            the politicaSubstitutiva12 to set
	 */
	public void setPoliticaSubstitutiva12(String politicaSubstitutiva12) {
		this.politicaSubstitutiva12 = politicaSubstitutiva12;
	}

	/**
	 * @return the politicaSubstitutiva13
	 */
	public String getPoliticaSubstitutiva13() {
		if (politicaSubstitutiva13 == null) {
			politicaSubstitutiva13 = "";
		}
		return politicaSubstitutiva13;
	}

	/**
	 * @param politicaSubstitutiva13
	 *            the politicaSubstitutiva13 to set
	 */
	public void setPoliticaSubstitutiva13(String politicaSubstitutiva13) {
		this.politicaSubstitutiva13 = politicaSubstitutiva13;
	}

	public Boolean getUtilizarArredondamentoMediaParaMais() {
		if (utilizarArredondamentoMediaParaMais == null) {
			utilizarArredondamentoMediaParaMais = false;
		}
		return utilizarArredondamentoMediaParaMais;
	}

	public void setUtilizarArredondamentoMediaParaMais(Boolean utilizarArredondamentoMediaParaMais) {
		this.utilizarArredondamentoMediaParaMais = utilizarArredondamentoMediaParaMais;
	}

	public TipoCalculoCargaHorariaFrequencia getTipoCalculoCargaHorariaFrequencia() {
		if (tipoCalculoCargaHorariaFrequencia == null) {
			tipoCalculoCargaHorariaFrequencia = TipoCalculoCargaHorariaFrequencia.CARGA_HORARIA_REGISTRO_AULA;
		}
		return tipoCalculoCargaHorariaFrequencia;
	}

	public void setTipoCalculoCargaHorariaFrequencia(TipoCalculoCargaHorariaFrequencia tipoCalculoCargaHorariaFrequencia) {
		this.tipoCalculoCargaHorariaFrequencia = tipoCalculoCargaHorariaFrequencia;
	}

	public Boolean getPermiteRegistrarAulaFutura() {
		if (permiteRegistrarAulaFutura == null) {
			permiteRegistrarAulaFutura = Boolean.FALSE;
		}
		return permiteRegistrarAulaFutura;
	}

	public void setPermiteRegistrarAulaFutura(Boolean permiteRegistrarAulaFutura) {
		this.permiteRegistrarAulaFutura = permiteRegistrarAulaFutura;
	}

	public BimestreEnum getBimestreNota1() {
		if (bimestreNota1 == null) {
			bimestreNota1 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota1;
	}

	public void setBimestreNota1(BimestreEnum bimestreNota1) {
		this.bimestreNota1 = bimestreNota1;
	}

	public BimestreEnum getBimestreNota2() {
		if (bimestreNota2 == null) {
			bimestreNota2 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota2;
	}

	public void setBimestreNota2(BimestreEnum bimestreNota2) {
		this.bimestreNota2 = bimestreNota2;
	}

	public BimestreEnum getBimestreNota3() {
		if (bimestreNota3 == null) {
			bimestreNota3 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota3;
	}

	public void setBimestreNota3(BimestreEnum bimestreNota3) {
		this.bimestreNota3 = bimestreNota3;
	}

	public BimestreEnum getBimestreNota4() {
		if (bimestreNota4 == null) {
			bimestreNota4 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota4;
	}

	public void setBimestreNota4(BimestreEnum bimestreNota4) {
		this.bimestreNota4 = bimestreNota4;
	}

	public BimestreEnum getBimestreNota5() {
		if (bimestreNota5 == null) {
			bimestreNota5 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota5;
	}

	public void setBimestreNota5(BimestreEnum bimestreNota5) {
		this.bimestreNota5 = bimestreNota5;
	}

	public BimestreEnum getBimestreNota6() {
		if (bimestreNota6 == null) {
			bimestreNota6 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota6;
	}

	public void setBimestreNota6(BimestreEnum bimestreNota6) {
		this.bimestreNota6 = bimestreNota6;
	}

	public BimestreEnum getBimestreNota7() {
		if (bimestreNota7 == null) {
			bimestreNota7 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota7;
	}

	public void setBimestreNota7(BimestreEnum bimestreNota7) {
		this.bimestreNota7 = bimestreNota7;
	}

	public BimestreEnum getBimestreNota8() {
		if (bimestreNota8 == null) {
			bimestreNota8 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota8;
	}

	public void setBimestreNota8(BimestreEnum bimestreNota8) {
		this.bimestreNota8 = bimestreNota8;
	}

	public BimestreEnum getBimestreNota9() {
		if (bimestreNota9 == null) {
			bimestreNota9 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota9;
	}

	public void setBimestreNota9(BimestreEnum bimestreNota9) {
		this.bimestreNota9 = bimestreNota9;
	}

	public BimestreEnum getBimestreNota10() {
		if (bimestreNota10 == null) {
			bimestreNota10 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota10;
	}

	public void setBimestreNota10(BimestreEnum bimestreNota10) {
		this.bimestreNota10 = bimestreNota10;
	}

	public BimestreEnum getBimestreNota11() {
		if (bimestreNota11 == null) {
			bimestreNota11 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota11;
	}

	public void setBimestreNota11(BimestreEnum bimestreNota11) {
		this.bimestreNota11 = bimestreNota11;
	}

	public BimestreEnum getBimestreNota12() {
		if (bimestreNota12 == null) {
			bimestreNota12 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota12;
	}

	public void setBimestreNota12(BimestreEnum bimestreNota12) {
		this.bimestreNota12 = bimestreNota12;
	}

	public BimestreEnum getBimestreNota13() {
		if (bimestreNota13 == null) {
			bimestreNota13 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota13;
	}

	public void setBimestreNota13(BimestreEnum bimestreNota13) {
		this.bimestreNota13 = bimestreNota13;
	}

	// NOTA 14 **************************************************************
	public BimestreEnum getBimestreNota14() {
		if (bimestreNota14 == null) {
			bimestreNota14 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota14;
	}

	public void setBimestreNota14(BimestreEnum bimestreNota14) {
		this.bimestreNota14 = bimestreNota14;
	}

	public String getFormulaUsoNota14() {
		if (formulaUsoNota14 == null) {
			formulaUsoNota14 = "";
		}
		return (formulaUsoNota14);
	}

	public void setFormulaUsoNota14(String formulaUsoNota14) {
		this.formulaUsoNota14 = formulaUsoNota14;
	}

	public String getFormulaCalculoNota14() {
		if (formulaCalculoNota14 == null) {
			formulaCalculoNota14 = "";
		}
		return (formulaCalculoNota14);
	}

	public void setFormulaCalculoNota14(String formulaCalculoNota14) {
		this.formulaCalculoNota14 = formulaCalculoNota14;
	}

	public String getTituloNota14() {
		if (tituloNota14 == null) {
			tituloNota14 = "";
		}
		return (tituloNota14);
	}

	public void setTituloNota14(String tituloNota14) {
		this.tituloNota14 = tituloNota14;
	}

	public Boolean getUtilizarNota14() {
		if (utilizarNota14 == null) {
			utilizarNota14 = Boolean.FALSE;
		}
		return (utilizarNota14);
	}

	public Boolean isUtilizarNota14() {
		return (utilizarNota14);
	}

	public void setUtilizarNota14(Boolean utilizarNota14) {
		this.utilizarNota14 = utilizarNota14;
	}

	public Double getNota14() {
		return (nota14);
	}

	public void setNota14(Double nota14) {
		this.nota14 = nota14;
	}

	public Boolean getNota14MediaFinal() {
		if (nota14MediaFinal == null) {
			nota14MediaFinal = false;
		}
		return nota14MediaFinal;
	}

	public void setNota14MediaFinal(Boolean nota14MediaFinal) {
		this.nota14MediaFinal = nota14MediaFinal;
	}

	public Boolean getUtilizarNota14PorConceito() {
		if (utilizarNota14PorConceito == null) {
			utilizarNota14PorConceito = false;
		}
		return utilizarNota14PorConceito;
	}

	public void setUtilizarNota14PorConceito(Boolean utilizarNota14PorConceito) {
		this.utilizarNota14PorConceito = utilizarNota14PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva14() {
		if (utilizarComoSubstitutiva14 == null) {
			utilizarComoSubstitutiva14 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva14;
	}

	public void setUtilizarComoSubstitutiva14(Boolean utilizarComoSubstitutiva14) {
		this.utilizarComoSubstitutiva14 = utilizarComoSubstitutiva14;
	}

	public Boolean getApresentarNota14() {
		if (apresentarNota14 == null) {
			apresentarNota14 = Boolean.TRUE;
		}
		return apresentarNota14;
	}

	public void setApresentarNota14(Boolean apresentarNota14) {
		this.apresentarNota14 = apresentarNota14;
	}

	public String getPoliticaSubstitutiva14() {
		if (politicaSubstitutiva14 == null) {
			politicaSubstitutiva14 = "";
		}
		return politicaSubstitutiva14;
	}

	public void setPoliticaSubstitutiva14(String politicaSubstitutiva14) {
		this.politicaSubstitutiva14 = politicaSubstitutiva14;
	}

	@XmlElement(name = "configuracaoAcademicoNota14ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota14ConceitoVOs() {
		if (configuracaoAcademicoNota14ConceitoVOs == null) {
			configuracaoAcademicoNota14ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota14ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota14ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota14ConceitoVOs) {
		this.configuracaoAcademicoNota14ConceitoVOs = configuracaoAcademicoNota14ConceitoVOs;
	}

	// **************************************************************

	// NOTA 15 **************************************************************
	public BimestreEnum getBimestreNota15() {
		if (bimestreNota15 == null) {
			bimestreNota15 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota15;
	}

	public void setBimestreNota15(BimestreEnum bimestreNota15) {
		this.bimestreNota15 = bimestreNota15;
	}

	public String getFormulaUsoNota15() {
		if (formulaUsoNota15 == null) {
			formulaUsoNota15 = "";
		}
		return (formulaUsoNota15);
	}

	public void setFormulaUsoNota15(String formulaUsoNota15) {
		this.formulaUsoNota15 = formulaUsoNota15;
	}

	public String getFormulaCalculoNota15() {
		if (formulaCalculoNota15 == null) {
			formulaCalculoNota15 = "";
		}
		return (formulaCalculoNota15);
	}

	public void setFormulaCalculoNota15(String formulaCalculoNota15) {
		this.formulaCalculoNota15 = formulaCalculoNota15;
	}

	public String getTituloNota15() {
		if (tituloNota15 == null) {
			tituloNota15 = "";
		}
		return (tituloNota15);
	}

	public void setTituloNota15(String tituloNota15) {
		this.tituloNota15 = tituloNota15;
	}

	public Boolean getUtilizarNota15() {
		if (utilizarNota15 == null) {
			utilizarNota15 = Boolean.FALSE;
		}
		return (utilizarNota15);
	}

	public Boolean isUtilizarNota15() {
		return (utilizarNota15);
	}

	public void setUtilizarNota15(Boolean utilizarNota15) {
		this.utilizarNota15 = utilizarNota15;
	}

	public Double getNota15() {
		return (nota15);
	}

	public void setNota15(Double nota15) {
		this.nota15 = nota15;
	}

	public Boolean getNota15MediaFinal() {
		if (nota15MediaFinal == null) {
			nota15MediaFinal = false;
		}
		return nota15MediaFinal;
	}

	public void setNota15MediaFinal(Boolean nota15MediaFinal) {
		this.nota15MediaFinal = nota15MediaFinal;
	}

	public Boolean getUtilizarNota15PorConceito() {
		if (utilizarNota15PorConceito == null) {
			utilizarNota15PorConceito = false;
		}
		return utilizarNota15PorConceito;
	}

	public void setUtilizarNota15PorConceito(Boolean utilizarNota15PorConceito) {
		this.utilizarNota15PorConceito = utilizarNota15PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva15() {
		if (utilizarComoSubstitutiva15 == null) {
			utilizarComoSubstitutiva15 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva15;
	}

	public void setUtilizarComoSubstitutiva15(Boolean utilizarComoSubstitutiva15) {
		this.utilizarComoSubstitutiva15 = utilizarComoSubstitutiva15;
	}

	public Boolean getApresentarNota15() {
		if (apresentarNota15 == null) {
			apresentarNota15 = Boolean.TRUE;
		}
		return apresentarNota15;
	}

	public void setApresentarNota15(Boolean apresentarNota15) {
		this.apresentarNota15 = apresentarNota15;
	}

	public String getPoliticaSubstitutiva15() {
		if (politicaSubstitutiva15 == null) {
			politicaSubstitutiva15 = "";
		}
		return politicaSubstitutiva15;
	}

	public void setPoliticaSubstitutiva15(String politicaSubstitutiva15) {
		this.politicaSubstitutiva15 = politicaSubstitutiva15;
	}

	@XmlElement(name = "configuracaoAcademicoNota15ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota15ConceitoVOs() {
		if (configuracaoAcademicoNota15ConceitoVOs == null) {
			configuracaoAcademicoNota15ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota15ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota15ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota15ConceitoVOs) {
		this.configuracaoAcademicoNota15ConceitoVOs = configuracaoAcademicoNota15ConceitoVOs;
	}

	// **************************************************************

	// NOTA 16 **************************************************************
	public BimestreEnum getBimestreNota16() {
		if (bimestreNota16 == null) {
			bimestreNota16 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota16;
	}

	public void setBimestreNota16(BimestreEnum bimestreNota16) {
		this.bimestreNota16 = bimestreNota16;
	}

	public String getFormulaUsoNota16() {
		if (formulaUsoNota16 == null) {
			formulaUsoNota16 = "";
		}
		return (formulaUsoNota16);
	}

	public void setFormulaUsoNota16(String formulaUsoNota16) {
		this.formulaUsoNota16 = formulaUsoNota16;
	}

	public String getFormulaCalculoNota16() {
		if (formulaCalculoNota16 == null) {
			formulaCalculoNota16 = "";
		}
		return (formulaCalculoNota16);
	}

	public void setFormulaCalculoNota16(String formulaCalculoNota16) {
		this.formulaCalculoNota16 = formulaCalculoNota16;
	}

	public String getTituloNota16() {
		if (tituloNota16 == null) {
			tituloNota16 = "";
		}
		return (tituloNota16);
	}

	public void setTituloNota16(String tituloNota16) {
		this.tituloNota16 = tituloNota16;
	}

	public Boolean getUtilizarNota16() {
		if (utilizarNota16 == null) {
			utilizarNota16 = Boolean.FALSE;
		}
		return (utilizarNota16);
	}

	public Boolean isUtilizarNota16() {
		return (utilizarNota16);
	}

	public void setUtilizarNota16(Boolean utilizarNota16) {
		this.utilizarNota16 = utilizarNota16;
	}

	public Double getNota16() {
		return (nota16);
	}

	public void setNota16(Double nota16) {
		this.nota16 = nota16;
	}

	public Boolean getNota16MediaFinal() {
		if (nota16MediaFinal == null) {
			nota16MediaFinal = false;
		}
		return nota16MediaFinal;
	}

	public void setNota16MediaFinal(Boolean nota16MediaFinal) {
		this.nota16MediaFinal = nota16MediaFinal;
	}

	public Boolean getUtilizarNota16PorConceito() {
		if (utilizarNota16PorConceito == null) {
			utilizarNota16PorConceito = false;
		}
		return utilizarNota16PorConceito;
	}

	public void setUtilizarNota16PorConceito(Boolean utilizarNota16PorConceito) {
		this.utilizarNota16PorConceito = utilizarNota16PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva16() {
		if (utilizarComoSubstitutiva16 == null) {
			utilizarComoSubstitutiva16 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva16;
	}

	public void setUtilizarComoSubstitutiva16(Boolean utilizarComoSubstitutiva16) {
		this.utilizarComoSubstitutiva16 = utilizarComoSubstitutiva16;
	}

	public Boolean getApresentarNota16() {
		if (apresentarNota16 == null) {
			apresentarNota16 = Boolean.TRUE;
		}
		return apresentarNota16;
	}

	public void setApresentarNota16(Boolean apresentarNota16) {
		this.apresentarNota16 = apresentarNota16;
	}

	public String getPoliticaSubstitutiva16() {
		if (politicaSubstitutiva16 == null) {
			politicaSubstitutiva16 = "";
		}
		return politicaSubstitutiva16;
	}

	public void setPoliticaSubstitutiva16(String politicaSubstitutiva16) {
		this.politicaSubstitutiva16 = politicaSubstitutiva16;
	}

	@XmlElement(name = "configuracaoAcademicoNota16ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota16ConceitoVOs() {
		if (configuracaoAcademicoNota16ConceitoVOs == null) {
			configuracaoAcademicoNota16ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota16ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota16ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota16ConceitoVOs) {
		this.configuracaoAcademicoNota16ConceitoVOs = configuracaoAcademicoNota16ConceitoVOs;
	}

	// **************************************************************

	// NOTA 17 **************************************************************
	public BimestreEnum getBimestreNota17() {
		if (bimestreNota17 == null) {
			bimestreNota17 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota17;
	}

	public void setBimestreNota17(BimestreEnum bimestreNota17) {
		this.bimestreNota17 = bimestreNota17;
	}

	public String getFormulaUsoNota17() {
		if (formulaUsoNota17 == null) {
			formulaUsoNota17 = "";
		}
		return (formulaUsoNota17);
	}

	public void setFormulaUsoNota17(String formulaUsoNota17) {
		this.formulaUsoNota17 = formulaUsoNota17;
	}

	public String getFormulaCalculoNota17() {
		if (formulaCalculoNota17 == null) {
			formulaCalculoNota17 = "";
		}
		return (formulaCalculoNota17);
	}

	public void setFormulaCalculoNota17(String formulaCalculoNota17) {
		this.formulaCalculoNota17 = formulaCalculoNota17;
	}

	public String getTituloNota17() {
		if (tituloNota17 == null) {
			tituloNota17 = "";
		}
		return (tituloNota17);
	}

	public void setTituloNota17(String tituloNota17) {
		this.tituloNota17 = tituloNota17;
	}

	public Boolean getUtilizarNota17() {
		if (utilizarNota17 == null) {
			utilizarNota17 = Boolean.FALSE;
		}
		return (utilizarNota17);
	}

	public Boolean isUtilizarNota17() {
		return (utilizarNota17);
	}

	public void setUtilizarNota17(Boolean utilizarNota17) {
		this.utilizarNota17 = utilizarNota17;
	}

	public Double getNota17() {
		return (nota17);
	}

	public void setNota17(Double nota17) {
		this.nota17 = nota17;
	}

	public Boolean getNota17MediaFinal() {
		if (nota17MediaFinal == null) {
			nota17MediaFinal = false;
		}
		return nota17MediaFinal;
	}

	public void setNota17MediaFinal(Boolean nota17MediaFinal) {
		this.nota17MediaFinal = nota17MediaFinal;
	}

	public Boolean getUtilizarNota17PorConceito() {
		if (utilizarNota17PorConceito == null) {
			utilizarNota17PorConceito = false;
		}
		return utilizarNota17PorConceito;
	}

	public void setUtilizarNota17PorConceito(Boolean utilizarNota17PorConceito) {
		this.utilizarNota17PorConceito = utilizarNota17PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva17() {
		if (utilizarComoSubstitutiva17 == null) {
			utilizarComoSubstitutiva17 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva17;
	}

	public void setUtilizarComoSubstitutiva17(Boolean utilizarComoSubstitutiva17) {
		this.utilizarComoSubstitutiva17 = utilizarComoSubstitutiva17;
	}

	public Boolean getApresentarNota17() {
		if (apresentarNota17 == null) {
			apresentarNota17 = Boolean.TRUE;
		}
		return apresentarNota17;
	}

	public void setApresentarNota17(Boolean apresentarNota17) {
		this.apresentarNota17 = apresentarNota17;
	}

	public String getPoliticaSubstitutiva17() {
		if (politicaSubstitutiva17 == null) {
			politicaSubstitutiva17 = "";
		}
		return politicaSubstitutiva17;
	}

	public void setPoliticaSubstitutiva17(String politicaSubstitutiva17) {
		this.politicaSubstitutiva17 = politicaSubstitutiva17;
	}

	@XmlElement(name = "configuracaoAcademicoNota17ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota17ConceitoVOs() {
		if (configuracaoAcademicoNota17ConceitoVOs == null) {
			configuracaoAcademicoNota17ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota17ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota17ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota17ConceitoVOs) {
		this.configuracaoAcademicoNota17ConceitoVOs = configuracaoAcademicoNota17ConceitoVOs;
	}

	// **************************************************************

	// NOTA 18 **************************************************************
	public BimestreEnum getBimestreNota18() {
		if (bimestreNota18 == null) {
			bimestreNota18 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota18;
	}

	public void setBimestreNota18(BimestreEnum bimestreNota18) {
		this.bimestreNota18 = bimestreNota18;
	}

	public String getFormulaUsoNota18() {
		if (formulaUsoNota18 == null) {
			formulaUsoNota18 = "";
		}
		return (formulaUsoNota18);
	}

	public void setFormulaUsoNota18(String formulaUsoNota18) {
		this.formulaUsoNota18 = formulaUsoNota18;
	}

	public String getFormulaCalculoNota18() {
		if (formulaCalculoNota18 == null) {
			formulaCalculoNota18 = "";
		}
		return (formulaCalculoNota18);
	}

	public void setFormulaCalculoNota18(String formulaCalculoNota18) {
		this.formulaCalculoNota18 = formulaCalculoNota18;
	}

	public String getTituloNota18() {
		if (tituloNota18 == null) {
			tituloNota18 = "";
		}
		return (tituloNota18);
	}

	public void setTituloNota18(String tituloNota18) {
		this.tituloNota18 = tituloNota18;
	}

	public Boolean getUtilizarNota18() {
		if (utilizarNota18 == null) {
			utilizarNota18 = Boolean.FALSE;
		}
		return (utilizarNota18);
	}

	public Boolean isUtilizarNota18() {
		return (utilizarNota18);
	}

	public void setUtilizarNota18(Boolean utilizarNota18) {
		this.utilizarNota18 = utilizarNota18;
	}

	public Double getNota18() {
		return (nota18);
	}

	public void setNota18(Double nota18) {
		this.nota18 = nota18;
	}

	public Boolean getNota18MediaFinal() {
		if (nota18MediaFinal == null) {
			nota18MediaFinal = false;
		}
		return nota18MediaFinal;
	}

	public void setNota18MediaFinal(Boolean nota18MediaFinal) {
		this.nota18MediaFinal = nota18MediaFinal;
	}

	public Boolean getUtilizarNota18PorConceito() {
		if (utilizarNota18PorConceito == null) {
			utilizarNota18PorConceito = false;
		}
		return utilizarNota18PorConceito;
	}

	public void setUtilizarNota18PorConceito(Boolean utilizarNota18PorConceito) {
		this.utilizarNota18PorConceito = utilizarNota18PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva18() {
		if (utilizarComoSubstitutiva18 == null) {
			utilizarComoSubstitutiva18 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva18;
	}

	public void setUtilizarComoSubstitutiva18(Boolean utilizarComoSubstitutiva18) {
		this.utilizarComoSubstitutiva18 = utilizarComoSubstitutiva18;
	}

	public Boolean getApresentarNota18() {
		if (apresentarNota18 == null) {
			apresentarNota18 = Boolean.TRUE;
		}
		return apresentarNota18;
	}

	public void setApresentarNota18(Boolean apresentarNota18) {
		this.apresentarNota18 = apresentarNota18;
	}

	public String getPoliticaSubstitutiva18() {
		if (politicaSubstitutiva18 == null) {
			politicaSubstitutiva18 = "";
		}
		return politicaSubstitutiva18;
	}

	public void setPoliticaSubstitutiva18(String politicaSubstitutiva18) {
		this.politicaSubstitutiva18 = politicaSubstitutiva18;
	}

	@XmlElement(name = "configuracaoAcademicoNota18ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota18ConceitoVOs() {
		if (configuracaoAcademicoNota18ConceitoVOs == null) {
			configuracaoAcademicoNota18ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota18ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota18ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota18ConceitoVOs) {
		this.configuracaoAcademicoNota18ConceitoVOs = configuracaoAcademicoNota18ConceitoVOs;
	}

	// **************************************************************

	// NOTA 19 **************************************************************
	public BimestreEnum getBimestreNota19() {
		if (bimestreNota19 == null) {
			bimestreNota19 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota19;
	}

	public void setBimestreNota19(BimestreEnum bimestreNota19) {
		this.bimestreNota19 = bimestreNota19;
	}

	public String getFormulaUsoNota19() {
		if (formulaUsoNota19 == null) {
			formulaUsoNota19 = "";
		}
		return (formulaUsoNota19);
	}

	public void setFormulaUsoNota19(String formulaUsoNota19) {
		this.formulaUsoNota19 = formulaUsoNota19;
	}

	public String getFormulaCalculoNota19() {
		if (formulaCalculoNota19 == null) {
			formulaCalculoNota19 = "";
		}
		return (formulaCalculoNota19);
	}

	public void setFormulaCalculoNota19(String formulaCalculoNota19) {
		this.formulaCalculoNota19 = formulaCalculoNota19;
	}

	public String getTituloNota19() {
		if (tituloNota19 == null) {
			tituloNota19 = "";
		}
		return (tituloNota19);
	}

	public void setTituloNota19(String tituloNota19) {
		this.tituloNota19 = tituloNota19;
	}

	public Boolean getUtilizarNota19() {
		if (utilizarNota19 == null) {
			utilizarNota19 = Boolean.FALSE;
		}
		return (utilizarNota19);
	}

	public Boolean isUtilizarNota19() {
		return (utilizarNota19);
	}

	public void setUtilizarNota19(Boolean utilizarNota19) {
		this.utilizarNota19 = utilizarNota19;
	}

	public Double getNota19() {
		return (nota19);
	}

	public void setNota19(Double nota19) {
		this.nota19 = nota19;
	}

	public Boolean getNota19MediaFinal() {
		if (nota19MediaFinal == null) {
			nota19MediaFinal = false;
		}
		return nota19MediaFinal;
	}

	public void setNota19MediaFinal(Boolean nota19MediaFinal) {
		this.nota19MediaFinal = nota19MediaFinal;
	}

	public Boolean getUtilizarNota19PorConceito() {
		if (utilizarNota19PorConceito == null) {
			utilizarNota19PorConceito = false;
		}
		return utilizarNota19PorConceito;
	}

	public void setUtilizarNota19PorConceito(Boolean utilizarNota19PorConceito) {
		this.utilizarNota19PorConceito = utilizarNota19PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva19() {
		if (utilizarComoSubstitutiva19 == null) {
			utilizarComoSubstitutiva19 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva19;
	}

	public void setUtilizarComoSubstitutiva19(Boolean utilizarComoSubstitutiva19) {
		this.utilizarComoSubstitutiva19 = utilizarComoSubstitutiva19;
	}

	public Boolean getApresentarNota19() {
		if (apresentarNota19 == null) {
			apresentarNota19 = Boolean.TRUE;
		}
		return apresentarNota19;
	}

	public void setApresentarNota19(Boolean apresentarNota19) {
		this.apresentarNota19 = apresentarNota19;
	}

	public String getPoliticaSubstitutiva19() {
		if (politicaSubstitutiva19 == null) {
			politicaSubstitutiva19 = "";
		}
		return politicaSubstitutiva19;
	}

	public void setPoliticaSubstitutiva19(String politicaSubstitutiva19) {
		this.politicaSubstitutiva19 = politicaSubstitutiva19;
	}

	@XmlElement(name = "configuracaoAcademicoNota19ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota19ConceitoVOs() {
		if (configuracaoAcademicoNota19ConceitoVOs == null) {
			configuracaoAcademicoNota19ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota19ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota19ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota19ConceitoVOs) {
		this.configuracaoAcademicoNota19ConceitoVOs = configuracaoAcademicoNota19ConceitoVOs;
	}

	// **************************************************************

	// NOTA 20 **************************************************************
	public BimestreEnum getBimestreNota20() {
		if (bimestreNota20 == null) {
			bimestreNota20 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota20;
	}

	public void setBimestreNota20(BimestreEnum bimestreNota20) {
		this.bimestreNota20 = bimestreNota20;
	}

	public String getFormulaUsoNota20() {
		if (formulaUsoNota20 == null) {
			formulaUsoNota20 = "";
		}
		return (formulaUsoNota20);
	}

	public void setFormulaUsoNota20(String formulaUsoNota20) {
		this.formulaUsoNota20 = formulaUsoNota20;
	}

	public String getFormulaCalculoNota20() {
		if (formulaCalculoNota20 == null) {
			formulaCalculoNota20 = "";
		}
		return (formulaCalculoNota20);
	}

	public void setFormulaCalculoNota20(String formulaCalculoNota20) {
		this.formulaCalculoNota20 = formulaCalculoNota20;
	}

	public String getTituloNota20() {
		if (tituloNota20 == null) {
			tituloNota20 = "";
		}
		return (tituloNota20);
	}

	public void setTituloNota20(String tituloNota20) {
		this.tituloNota20 = tituloNota20;
	}

	public Boolean getUtilizarNota20() {
		if (utilizarNota20 == null) {
			utilizarNota20 = Boolean.FALSE;
		}
		return (utilizarNota20);
	}

	public Boolean isUtilizarNota20() {
		return (utilizarNota20);
	}

	public void setUtilizarNota20(Boolean utilizarNota20) {
		this.utilizarNota20 = utilizarNota20;
	}

	public Double getNota20() {
		return (nota20);
	}

	public void setNota20(Double nota20) {
		this.nota20 = nota20;
	}

	public Boolean getNota20MediaFinal() {
		if (nota20MediaFinal == null) {
			nota20MediaFinal = false;
		}
		return nota20MediaFinal;
	}

	public void setNota20MediaFinal(Boolean nota20MediaFinal) {
		this.nota20MediaFinal = nota20MediaFinal;
	}

	public Boolean getUtilizarNota20PorConceito() {
		if (utilizarNota20PorConceito == null) {
			utilizarNota20PorConceito = false;
		}
		return utilizarNota20PorConceito;
	}

	public void setUtilizarNota20PorConceito(Boolean utilizarNota20PorConceito) {
		this.utilizarNota20PorConceito = utilizarNota20PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva20() {
		if (utilizarComoSubstitutiva20 == null) {
			utilizarComoSubstitutiva20 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva20;
	}

	public void setUtilizarComoSubstitutiva20(Boolean utilizarComoSubstitutiva20) {
		this.utilizarComoSubstitutiva20 = utilizarComoSubstitutiva20;
	}

	public Boolean getApresentarNota20() {
		if (apresentarNota20 == null) {
			apresentarNota20 = Boolean.TRUE;
		}
		return apresentarNota20;
	}

	public void setApresentarNota20(Boolean apresentarNota20) {
		this.apresentarNota20 = apresentarNota20;
	}

	public String getPoliticaSubstitutiva20() {
		if (politicaSubstitutiva20 == null) {
			politicaSubstitutiva20 = "";
		}
		return politicaSubstitutiva20;
	}

	public void setPoliticaSubstitutiva20(String politicaSubstitutiva20) {
		this.politicaSubstitutiva20 = politicaSubstitutiva20;
	}

	@XmlElement(name = "configuracaoAcademicoNota20ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota20ConceitoVOs() {
		if (configuracaoAcademicoNota20ConceitoVOs == null) {
			configuracaoAcademicoNota20ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota20ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota20ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota20ConceitoVOs) {
		this.configuracaoAcademicoNota20ConceitoVOs = configuracaoAcademicoNota20ConceitoVOs;
	}

	// **************************************************************

	// NOTA 21 **************************************************************
	public BimestreEnum getBimestreNota21() {
		if (bimestreNota21 == null) {
			bimestreNota21 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota21;
	}

	public void setBimestreNota21(BimestreEnum bimestreNota21) {
		this.bimestreNota21 = bimestreNota21;
	}

	public String getFormulaUsoNota21() {
		if (formulaUsoNota21 == null) {
			formulaUsoNota21 = "";
		}
		return (formulaUsoNota21);
	}

	public void setFormulaUsoNota21(String formulaUsoNota21) {
		this.formulaUsoNota21 = formulaUsoNota21;
	}

	public String getFormulaCalculoNota21() {
		if (formulaCalculoNota21 == null) {
			formulaCalculoNota21 = "";
		}
		return (formulaCalculoNota21);
	}

	public void setFormulaCalculoNota21(String formulaCalculoNota21) {
		this.formulaCalculoNota21 = formulaCalculoNota21;
	}

	public String getTituloNota21() {
		if (tituloNota21 == null) {
			tituloNota21 = "";
		}
		return (tituloNota21);
	}

	public void setTituloNota21(String tituloNota21) {
		this.tituloNota21 = tituloNota21;
	}

	public Boolean getUtilizarNota21() {
		if (utilizarNota21 == null) {
			utilizarNota21 = Boolean.FALSE;
		}
		return (utilizarNota21);
	}

	public Boolean isUtilizarNota21() {
		return (utilizarNota21);
	}

	public void setUtilizarNota21(Boolean utilizarNota21) {
		this.utilizarNota21 = utilizarNota21;
	}

	public Double getNota21() {
		return (nota21);
	}

	public void setNota21(Double nota21) {
		this.nota21 = nota21;
	}

	public Boolean getNota21MediaFinal() {
		if (nota21MediaFinal == null) {
			nota21MediaFinal = false;
		}
		return nota21MediaFinal;
	}

	public void setNota21MediaFinal(Boolean nota21MediaFinal) {
		this.nota21MediaFinal = nota21MediaFinal;
	}

	public Boolean getUtilizarNota21PorConceito() {
		if (utilizarNota21PorConceito == null) {
			utilizarNota21PorConceito = false;
		}
		return utilizarNota21PorConceito;
	}

	public void setUtilizarNota21PorConceito(Boolean utilizarNota21PorConceito) {
		this.utilizarNota21PorConceito = utilizarNota21PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva21() {
		if (utilizarComoSubstitutiva21 == null) {
			utilizarComoSubstitutiva21 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva21;
	}

	public void setUtilizarComoSubstitutiva21(Boolean utilizarComoSubstitutiva21) {
		this.utilizarComoSubstitutiva21 = utilizarComoSubstitutiva21;
	}

	public Boolean getApresentarNota21() {
		if (apresentarNota21 == null) {
			apresentarNota21 = Boolean.TRUE;
		}
		return apresentarNota21;
	}

	public void setApresentarNota21(Boolean apresentarNota21) {
		this.apresentarNota21 = apresentarNota21;
	}

	public String getPoliticaSubstitutiva21() {
		if (politicaSubstitutiva21 == null) {
			politicaSubstitutiva21 = "";
		}
		return politicaSubstitutiva21;
	}

	public void setPoliticaSubstitutiva21(String politicaSubstitutiva21) {
		this.politicaSubstitutiva21 = politicaSubstitutiva21;
	}

	@XmlElement(name = "configuracaoAcademicoNota21ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota21ConceitoVOs() {
		if (configuracaoAcademicoNota21ConceitoVOs == null) {
			configuracaoAcademicoNota21ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota21ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota21ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota21ConceitoVOs) {
		this.configuracaoAcademicoNota21ConceitoVOs = configuracaoAcademicoNota21ConceitoVOs;
	}

	// **************************************************************

	// NOTA 22 **************************************************************
	public BimestreEnum getBimestreNota22() {
		if (bimestreNota22 == null) {
			bimestreNota22 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota22;
	}

	public void setBimestreNota22(BimestreEnum bimestreNota22) {
		this.bimestreNota22 = bimestreNota22;
	}

	public String getFormulaUsoNota22() {
		if (formulaUsoNota22 == null) {
			formulaUsoNota22 = "";
		}
		return (formulaUsoNota22);
	}

	public void setFormulaUsoNota22(String formulaUsoNota22) {
		this.formulaUsoNota22 = formulaUsoNota22;
	}

	public String getFormulaCalculoNota22() {
		if (formulaCalculoNota22 == null) {
			formulaCalculoNota22 = "";
		}
		return (formulaCalculoNota22);
	}

	public void setFormulaCalculoNota22(String formulaCalculoNota22) {
		this.formulaCalculoNota22 = formulaCalculoNota22;
	}

	public String getTituloNota22() {
		if (tituloNota22 == null) {
			tituloNota22 = "";
		}
		return (tituloNota22);
	}

	public void setTituloNota22(String tituloNota22) {
		this.tituloNota22 = tituloNota22;
	}

	public Boolean getUtilizarNota22() {
		if (utilizarNota22 == null) {
			utilizarNota22 = Boolean.FALSE;
		}
		return (utilizarNota22);
	}

	public Boolean isUtilizarNota22() {
		return (utilizarNota22);
	}

	public void setUtilizarNota22(Boolean utilizarNota22) {
		this.utilizarNota22 = utilizarNota22;
	}

	public Double getNota22() {
		return (nota22);
	}

	public void setNota22(Double nota22) {
		this.nota22 = nota22;
	}

	public Boolean getNota22MediaFinal() {
		if (nota22MediaFinal == null) {
			nota22MediaFinal = false;
		}
		return nota22MediaFinal;
	}

	public void setNota22MediaFinal(Boolean nota22MediaFinal) {
		this.nota22MediaFinal = nota22MediaFinal;
	}

	public Boolean getUtilizarNota22PorConceito() {
		if (utilizarNota22PorConceito == null) {
			utilizarNota22PorConceito = false;
		}
		return utilizarNota22PorConceito;
	}

	public void setUtilizarNota22PorConceito(Boolean utilizarNota22PorConceito) {
		this.utilizarNota22PorConceito = utilizarNota22PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva22() {
		if (utilizarComoSubstitutiva22 == null) {
			utilizarComoSubstitutiva22 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva22;
	}

	public void setUtilizarComoSubstitutiva22(Boolean utilizarComoSubstitutiva22) {
		this.utilizarComoSubstitutiva22 = utilizarComoSubstitutiva22;
	}

	public Boolean getApresentarNota22() {
		if (apresentarNota22 == null) {
			apresentarNota22 = Boolean.TRUE;
		}
		return apresentarNota22;
	}

	public void setApresentarNota22(Boolean apresentarNota22) {
		this.apresentarNota22 = apresentarNota22;
	}

	public String getPoliticaSubstitutiva22() {
		if (politicaSubstitutiva22 == null) {
			politicaSubstitutiva22 = "";
		}
		return politicaSubstitutiva22;
	}

	public void setPoliticaSubstitutiva22(String politicaSubstitutiva22) {
		this.politicaSubstitutiva22 = politicaSubstitutiva22;
	}

	@XmlElement(name = "configuracaoAcademicoNota22ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota22ConceitoVOs() {
		if (configuracaoAcademicoNota22ConceitoVOs == null) {
			configuracaoAcademicoNota22ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota22ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota22ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota22ConceitoVOs) {
		this.configuracaoAcademicoNota22ConceitoVOs = configuracaoAcademicoNota22ConceitoVOs;
	}

	// **************************************************************

	// NOTA 23 **************************************************************
	public BimestreEnum getBimestreNota23() {
		if (bimestreNota23 == null) {
			bimestreNota23 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota23;
	}

	public void setBimestreNota23(BimestreEnum bimestreNota23) {
		this.bimestreNota23 = bimestreNota23;
	}

	public String getFormulaUsoNota23() {
		if (formulaUsoNota23 == null) {
			formulaUsoNota23 = "";
		}
		return (formulaUsoNota23);
	}

	public void setFormulaUsoNota23(String formulaUsoNota23) {
		this.formulaUsoNota23 = formulaUsoNota23;
	}

	public String getFormulaCalculoNota23() {
		if (formulaCalculoNota23 == null) {
			formulaCalculoNota23 = "";
		}
		return (formulaCalculoNota23);
	}

	public void setFormulaCalculoNota23(String formulaCalculoNota23) {
		this.formulaCalculoNota23 = formulaCalculoNota23;
	}

	public String getTituloNota23() {
		if (tituloNota23 == null) {
			tituloNota23 = "";
		}
		return (tituloNota23);
	}

	public void setTituloNota23(String tituloNota23) {
		this.tituloNota23 = tituloNota23;
	}

	public Boolean getUtilizarNota23() {
		if (utilizarNota23 == null) {
			utilizarNota23 = Boolean.FALSE;
		}
		return (utilizarNota23);
	}

	public Boolean isUtilizarNota23() {
		return (utilizarNota23);
	}

	public void setUtilizarNota23(Boolean utilizarNota23) {
		this.utilizarNota23 = utilizarNota23;
	}

	public Double getNota23() {
		return (nota23);
	}

	public void setNota23(Double nota23) {
		this.nota23 = nota23;
	}

	public Boolean getNota23MediaFinal() {
		if (nota23MediaFinal == null) {
			nota23MediaFinal = false;
		}
		return nota23MediaFinal;
	}

	public void setNota23MediaFinal(Boolean nota23MediaFinal) {
		this.nota23MediaFinal = nota23MediaFinal;
	}

	public Boolean getUtilizarNota23PorConceito() {
		if (utilizarNota23PorConceito == null) {
			utilizarNota23PorConceito = false;
		}
		return utilizarNota23PorConceito;
	}

	public void setUtilizarNota23PorConceito(Boolean utilizarNota23PorConceito) {
		this.utilizarNota23PorConceito = utilizarNota23PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva23() {
		if (utilizarComoSubstitutiva23 == null) {
			utilizarComoSubstitutiva23 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva23;
	}

	public void setUtilizarComoSubstitutiva23(Boolean utilizarComoSubstitutiva23) {
		this.utilizarComoSubstitutiva23 = utilizarComoSubstitutiva23;
	}

	public Boolean getApresentarNota23() {
		if (apresentarNota23 == null) {
			apresentarNota23 = Boolean.TRUE;
		}
		return apresentarNota23;
	}

	public void setApresentarNota23(Boolean apresentarNota23) {
		this.apresentarNota23 = apresentarNota23;
	}

	public String getPoliticaSubstitutiva23() {
		if (politicaSubstitutiva23 == null) {
			politicaSubstitutiva23 = "";
		}
		return politicaSubstitutiva23;
	}

	public void setPoliticaSubstitutiva23(String politicaSubstitutiva23) {
		this.politicaSubstitutiva23 = politicaSubstitutiva23;
	}

	@XmlElement(name = "configuracaoAcademicoNota23ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota23ConceitoVOs() {
		if (configuracaoAcademicoNota23ConceitoVOs == null) {
			configuracaoAcademicoNota23ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota23ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota23ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota23ConceitoVOs) {
		this.configuracaoAcademicoNota23ConceitoVOs = configuracaoAcademicoNota23ConceitoVOs;
	}

	// **************************************************************

	// NOTA 24 **************************************************************
	public BimestreEnum getBimestreNota24() {
		if (bimestreNota24 == null) {
			bimestreNota24 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota24;
	}

	public void setBimestreNota24(BimestreEnum bimestreNota24) {
		this.bimestreNota24 = bimestreNota24;
	}

	public String getFormulaUsoNota24() {
		if (formulaUsoNota24 == null) {
			formulaUsoNota24 = "";
		}
		return (formulaUsoNota24);
	}

	public void setFormulaUsoNota24(String formulaUsoNota24) {
		this.formulaUsoNota24 = formulaUsoNota24;
	}

	public String getFormulaCalculoNota24() {
		if (formulaCalculoNota24 == null) {
			formulaCalculoNota24 = "";
		}
		return (formulaCalculoNota24);
	}

	public void setFormulaCalculoNota24(String formulaCalculoNota24) {
		this.formulaCalculoNota24 = formulaCalculoNota24;
	}

	public String getTituloNota24() {
		if (tituloNota24 == null) {
			tituloNota24 = "";
		}
		return (tituloNota24);
	}

	public void setTituloNota24(String tituloNota24) {
		this.tituloNota24 = tituloNota24;
	}

	public Boolean getUtilizarNota24() {
		if (utilizarNota24 == null) {
			utilizarNota24 = Boolean.FALSE;
		}
		return (utilizarNota24);
	}

	public Boolean isUtilizarNota24() {
		return (utilizarNota24);
	}

	public void setUtilizarNota24(Boolean utilizarNota24) {
		this.utilizarNota24 = utilizarNota24;
	}

	public Double getNota24() {
		return (nota24);
	}

	public void setNota24(Double nota24) {
		this.nota24 = nota24;
	}

	public Boolean getNota24MediaFinal() {
		if (nota24MediaFinal == null) {
			nota24MediaFinal = false;
		}
		return nota24MediaFinal;
	}

	public void setNota24MediaFinal(Boolean nota24MediaFinal) {
		this.nota24MediaFinal = nota24MediaFinal;
	}

	public Boolean getUtilizarNota24PorConceito() {
		if (utilizarNota24PorConceito == null) {
			utilizarNota24PorConceito = false;
		}
		return utilizarNota24PorConceito;
	}

	public void setUtilizarNota24PorConceito(Boolean utilizarNota24PorConceito) {
		this.utilizarNota24PorConceito = utilizarNota24PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva24() {
		if (utilizarComoSubstitutiva24 == null) {
			utilizarComoSubstitutiva24 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva24;
	}

	public void setUtilizarComoSubstitutiva24(Boolean utilizarComoSubstitutiva24) {
		this.utilizarComoSubstitutiva24 = utilizarComoSubstitutiva24;
	}

	public Boolean getApresentarNota24() {
		if (apresentarNota24 == null) {
			apresentarNota24 = Boolean.TRUE;
		}
		return apresentarNota24;
	}

	public void setApresentarNota24(Boolean apresentarNota24) {
		this.apresentarNota24 = apresentarNota24;
	}

	public String getPoliticaSubstitutiva24() {
		if (politicaSubstitutiva24 == null) {
			politicaSubstitutiva24 = "";
		}
		return politicaSubstitutiva24;
	}

	public void setPoliticaSubstitutiva24(String politicaSubstitutiva24) {
		this.politicaSubstitutiva24 = politicaSubstitutiva24;
	}

	@XmlElement(name = "configuracaoAcademicoNota24ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota24ConceitoVOs() {
		if (configuracaoAcademicoNota24ConceitoVOs == null) {
			configuracaoAcademicoNota24ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota24ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota24ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota24ConceitoVOs) {
		this.configuracaoAcademicoNota24ConceitoVOs = configuracaoAcademicoNota24ConceitoVOs;
	}

	// **************************************************************

	// NOTA 25 **************************************************************
	public BimestreEnum getBimestreNota25() {
		if (bimestreNota25 == null) {
			bimestreNota25 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota25;
	}

	public void setBimestreNota25(BimestreEnum bimestreNota25) {
		this.bimestreNota25 = bimestreNota25;
	}

	public String getFormulaUsoNota25() {
		if (formulaUsoNota25 == null) {
			formulaUsoNota25 = "";
		}
		return (formulaUsoNota25);
	}

	public void setFormulaUsoNota25(String formulaUsoNota25) {
		this.formulaUsoNota25 = formulaUsoNota25;
	}

	public String getFormulaCalculoNota25() {
		if (formulaCalculoNota25 == null) {
			formulaCalculoNota25 = "";
		}
		return (formulaCalculoNota25);
	}

	public void setFormulaCalculoNota25(String formulaCalculoNota25) {
		this.formulaCalculoNota25 = formulaCalculoNota25;
	}

	public String getTituloNota25() {
		if (tituloNota25 == null) {
			tituloNota25 = "";
		}
		return (tituloNota25);
	}

	public void setTituloNota25(String tituloNota25) {
		this.tituloNota25 = tituloNota25;
	}

	public Boolean getUtilizarNota25() {
		if (utilizarNota25 == null) {
			utilizarNota25 = Boolean.FALSE;
		}
		return (utilizarNota25);
	}

	public Boolean isUtilizarNota25() {
		return (utilizarNota25);
	}

	public void setUtilizarNota25(Boolean utilizarNota25) {
		this.utilizarNota25 = utilizarNota25;
	}

	public Double getNota25() {
		return (nota25);
	}

	public void setNota25(Double nota25) {
		this.nota25 = nota25;
	}

	public Boolean getNota25MediaFinal() {
		if (nota25MediaFinal == null) {
			nota25MediaFinal = false;
		}
		return nota25MediaFinal;
	}

	public void setNota25MediaFinal(Boolean nota25MediaFinal) {
		this.nota25MediaFinal = nota25MediaFinal;
	}

	public Boolean getUtilizarNota25PorConceito() {
		if (utilizarNota25PorConceito == null) {
			utilizarNota25PorConceito = false;
		}
		return utilizarNota25PorConceito;
	}

	public void setUtilizarNota25PorConceito(Boolean utilizarNota25PorConceito) {
		this.utilizarNota25PorConceito = utilizarNota25PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva25() {
		if (utilizarComoSubstitutiva25 == null) {
			utilizarComoSubstitutiva25 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva25;
	}

	public void setUtilizarComoSubstitutiva25(Boolean utilizarComoSubstitutiva25) {
		this.utilizarComoSubstitutiva25 = utilizarComoSubstitutiva25;
	}

	public Boolean getApresentarNota25() {
		if (apresentarNota25 == null) {
			apresentarNota25 = Boolean.TRUE;
		}
		return apresentarNota25;
	}

	public void setApresentarNota25(Boolean apresentarNota25) {
		this.apresentarNota25 = apresentarNota25;
	}

	public String getPoliticaSubstitutiva25() {
		if (politicaSubstitutiva25 == null) {
			politicaSubstitutiva25 = "";
		}
		return politicaSubstitutiva25;
	}

	public void setPoliticaSubstitutiva25(String politicaSubstitutiva25) {
		this.politicaSubstitutiva25 = politicaSubstitutiva25;
	}

	@XmlElement(name = "configuracaoAcademicoNota25ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota25ConceitoVOs() {
		if (configuracaoAcademicoNota25ConceitoVOs == null) {
			configuracaoAcademicoNota25ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota25ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota25ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota25ConceitoVOs) {
		this.configuracaoAcademicoNota25ConceitoVOs = configuracaoAcademicoNota25ConceitoVOs;
	}

	// **************************************************************

	// NOTA 26 **************************************************************
	public BimestreEnum getBimestreNota26() {
		if (bimestreNota26 == null) {
			bimestreNota26 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota26;
	}

	public void setBimestreNota26(BimestreEnum bimestreNota26) {
		this.bimestreNota26 = bimestreNota26;
	}

	public String getFormulaUsoNota26() {
		if (formulaUsoNota26 == null) {
			formulaUsoNota26 = "";
		}
		return (formulaUsoNota26);
	}

	public void setFormulaUsoNota26(String formulaUsoNota26) {
		this.formulaUsoNota26 = formulaUsoNota26;
	}

	public String getFormulaCalculoNota26() {
		if (formulaCalculoNota26 == null) {
			formulaCalculoNota26 = "";
		}
		return (formulaCalculoNota26);
	}

	public void setFormulaCalculoNota26(String formulaCalculoNota26) {
		this.formulaCalculoNota26 = formulaCalculoNota26;
	}

	public String getTituloNota26() {
		if (tituloNota26 == null) {
			tituloNota26 = "";
		}
		return (tituloNota26);
	}

	public void setTituloNota26(String tituloNota26) {
		this.tituloNota26 = tituloNota26;
	}

	public Boolean getUtilizarNota26() {
		if (utilizarNota26 == null) {
			utilizarNota26 = Boolean.FALSE;
		}
		return (utilizarNota26);
	}

	public Boolean isUtilizarNota26() {
		return (utilizarNota26);
	}

	public void setUtilizarNota26(Boolean utilizarNota26) {
		this.utilizarNota26 = utilizarNota26;
	}

	public Double getNota26() {
		return (nota26);
	}

	public void setNota26(Double nota26) {
		this.nota26 = nota26;
	}

	public Boolean getNota26MediaFinal() {
		if (nota26MediaFinal == null) {
			nota26MediaFinal = false;
		}
		return nota26MediaFinal;
	}

	public void setNota26MediaFinal(Boolean nota26MediaFinal) {
		this.nota26MediaFinal = nota26MediaFinal;
	}

	public Boolean getUtilizarNota26PorConceito() {
		if (utilizarNota26PorConceito == null) {
			utilizarNota26PorConceito = false;
		}
		return utilizarNota26PorConceito;
	}

	public void setUtilizarNota26PorConceito(Boolean utilizarNota26PorConceito) {
		this.utilizarNota26PorConceito = utilizarNota26PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva26() {
		if (utilizarComoSubstitutiva26 == null) {
			utilizarComoSubstitutiva26 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva26;
	}

	public void setUtilizarComoSubstitutiva26(Boolean utilizarComoSubstitutiva26) {
		this.utilizarComoSubstitutiva26 = utilizarComoSubstitutiva26;
	}

	public Boolean getApresentarNota26() {
		if (apresentarNota26 == null) {
			apresentarNota26 = Boolean.TRUE;
		}
		return apresentarNota26;
	}

	public void setApresentarNota26(Boolean apresentarNota26) {
		this.apresentarNota26 = apresentarNota26;
	}

	public String getPoliticaSubstitutiva26() {
		if (politicaSubstitutiva26 == null) {
			politicaSubstitutiva26 = "";
		}
		return politicaSubstitutiva26;
	}

	public void setPoliticaSubstitutiva26(String politicaSubstitutiva26) {
		this.politicaSubstitutiva26 = politicaSubstitutiva26;
	}

	@XmlElement(name = "configuracaoAcademicoNota26ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota26ConceitoVOs() {
		if (configuracaoAcademicoNota26ConceitoVOs == null) {
			configuracaoAcademicoNota26ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota26ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota26ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota26ConceitoVOs) {
		this.configuracaoAcademicoNota26ConceitoVOs = configuracaoAcademicoNota26ConceitoVOs;
	}

	// **************************************************************

	// NOTA 27 **************************************************************
	public BimestreEnum getBimestreNota27() {
		if (bimestreNota27 == null) {
			bimestreNota27 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota27;
	}

	public void setBimestreNota27(BimestreEnum bimestreNota27) {
		this.bimestreNota27 = bimestreNota27;
	}

	public String getFormulaUsoNota27() {
		if (formulaUsoNota27 == null) {
			formulaUsoNota27 = "";
		}
		return (formulaUsoNota27);
	}

	public void setFormulaUsoNota27(String formulaUsoNota27) {
		this.formulaUsoNota27 = formulaUsoNota27;
	}

	public String getFormulaCalculoNota27() {
		if (formulaCalculoNota27 == null) {
			formulaCalculoNota27 = "";
		}
		return (formulaCalculoNota27);
	}

	public void setFormulaCalculoNota27(String formulaCalculoNota27) {
		this.formulaCalculoNota27 = formulaCalculoNota27;
	}

	public String getTituloNota27() {
		if (tituloNota27 == null) {
			tituloNota27 = "";
		}
		return (tituloNota27);
	}

	public void setTituloNota27(String tituloNota27) {
		this.tituloNota27 = tituloNota27;
	}

	public Boolean getUtilizarNota27() {
		if (utilizarNota27 == null) {
			utilizarNota27 = Boolean.FALSE;
		}
		return (utilizarNota27);
	}

	public Boolean isUtilizarNota27() {
		return (utilizarNota27);
	}

	public void setUtilizarNota27(Boolean utilizarNota27) {
		this.utilizarNota27 = utilizarNota27;
	}

	public Double getNota27() {
		return (nota27);
	}

	public void setNota27(Double nota27) {
		this.nota27 = nota27;
	}

	public Boolean getNota27MediaFinal() {
		if (nota27MediaFinal == null) {
			nota27MediaFinal = false;
		}
		return nota27MediaFinal;
	}

	public void setNota27MediaFinal(Boolean nota27MediaFinal) {
		this.nota27MediaFinal = nota27MediaFinal;
	}

	public Boolean getUtilizarNota27PorConceito() {
		if (utilizarNota27PorConceito == null) {
			utilizarNota27PorConceito = false;
		}
		return utilizarNota27PorConceito;
	}

	public void setUtilizarNota27PorConceito(Boolean utilizarNota27PorConceito) {
		this.utilizarNota27PorConceito = utilizarNota27PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva27() {
		if (utilizarComoSubstitutiva27 == null) {
			utilizarComoSubstitutiva27 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva27;
	}

	public void setUtilizarComoSubstitutiva27(Boolean utilizarComoSubstitutiva27) {
		this.utilizarComoSubstitutiva27 = utilizarComoSubstitutiva27;
	}

	public Boolean getApresentarNota27() {
		if (apresentarNota27 == null) {
			apresentarNota27 = Boolean.TRUE;
		}
		return apresentarNota27;
	}

	public void setApresentarNota27(Boolean apresentarNota27) {
		this.apresentarNota27 = apresentarNota27;
	}

	public String getPoliticaSubstitutiva27() {
		if (politicaSubstitutiva27 == null) {
			politicaSubstitutiva27 = "";
		}
		return politicaSubstitutiva27;
	}

	public void setPoliticaSubstitutiva27(String politicaSubstitutiva27) {
		this.politicaSubstitutiva27 = politicaSubstitutiva27;
	}

	@XmlElement(name = "configuracaoAcademicoNota27ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota27ConceitoVOs() {
		if (configuracaoAcademicoNota27ConceitoVOs == null) {
			configuracaoAcademicoNota27ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota27ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota27ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota27ConceitoVOs) {
		this.configuracaoAcademicoNota27ConceitoVOs = configuracaoAcademicoNota27ConceitoVOs;
	}

	// **************************************************************

	// NOTA 28 **************************************************************
	public BimestreEnum getBimestreNota28() {
		if (bimestreNota28 == null) {
			bimestreNota28 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota28;
	}

	public void setBimestreNota28(BimestreEnum bimestreNota28) {
		this.bimestreNota28 = bimestreNota28;
	}

	public String getFormulaUsoNota28() {
		if (formulaUsoNota28 == null) {
			formulaUsoNota28 = "";
		}
		return (formulaUsoNota28);
	}

	public void setFormulaUsoNota28(String formulaUsoNota28) {
		this.formulaUsoNota28 = formulaUsoNota28;
	}

	public String getFormulaCalculoNota28() {
		if (formulaCalculoNota28 == null) {
			formulaCalculoNota28 = "";
		}
		return (formulaCalculoNota28);
	}

	public void setFormulaCalculoNota28(String formulaCalculoNota28) {
		this.formulaCalculoNota28 = formulaCalculoNota28;
	}

	public String getTituloNota28() {
		if (tituloNota28 == null) {
			tituloNota28 = "";
		}
		return (tituloNota28);
	}

	public void setTituloNota28(String tituloNota28) {
		this.tituloNota28 = tituloNota28;
	}

	public Boolean getUtilizarNota28() {
		if (utilizarNota28 == null) {
			utilizarNota28 = Boolean.FALSE;
		}
		return (utilizarNota28);
	}

	public Boolean isUtilizarNota28() {
		return (utilizarNota28);
	}

	public void setUtilizarNota28(Boolean utilizarNota28) {
		this.utilizarNota28 = utilizarNota28;
	}

	public Double getNota28() {
		return (nota28);
	}

	public void setNota28(Double nota28) {
		this.nota28 = nota28;
	}

	public Boolean getNota28MediaFinal() {
		if (nota28MediaFinal == null) {
			nota28MediaFinal = false;
		}
		return nota28MediaFinal;
	}

	public void setNota28MediaFinal(Boolean nota28MediaFinal) {
		this.nota28MediaFinal = nota28MediaFinal;
	}

	public Boolean getUtilizarNota28PorConceito() {
		if (utilizarNota28PorConceito == null) {
			utilizarNota28PorConceito = false;
		}
		return utilizarNota28PorConceito;
	}

	public void setUtilizarNota28PorConceito(Boolean utilizarNota28PorConceito) {
		this.utilizarNota28PorConceito = utilizarNota28PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva28() {
		if (utilizarComoSubstitutiva28 == null) {
			utilizarComoSubstitutiva28 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva28;
	}

	public void setUtilizarComoSubstitutiva28(Boolean utilizarComoSubstitutiva28) {
		this.utilizarComoSubstitutiva28 = utilizarComoSubstitutiva28;
	}

	public Boolean getApresentarNota28() {
		if (apresentarNota28 == null) {
			apresentarNota28 = Boolean.TRUE;
		}
		return apresentarNota28;
	}

	public void setApresentarNota28(Boolean apresentarNota28) {
		this.apresentarNota28 = apresentarNota28;
	}

	public String getPoliticaSubstitutiva28() {
		if (politicaSubstitutiva28 == null) {
			politicaSubstitutiva28 = "";
		}
		return politicaSubstitutiva28;
	}

	public void setPoliticaSubstitutiva28(String politicaSubstitutiva28) {
		this.politicaSubstitutiva28 = politicaSubstitutiva28;
	}

	@XmlElement(name = "configuracaoAcademicoNota28ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota28ConceitoVOs() {
		if (configuracaoAcademicoNota28ConceitoVOs == null) {
			configuracaoAcademicoNota28ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota28ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota28ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota28ConceitoVOs) {
		this.configuracaoAcademicoNota28ConceitoVOs = configuracaoAcademicoNota28ConceitoVOs;
	}

	// **************************************************************

	// NOTA 29 **************************************************************
	public BimestreEnum getBimestreNota29() {
		if (bimestreNota29 == null) {
			bimestreNota29 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota29;
	}

	public void setBimestreNota29(BimestreEnum bimestreNota29) {
		this.bimestreNota29 = bimestreNota29;
	}

	public String getFormulaUsoNota29() {
		if (formulaUsoNota29 == null) {
			formulaUsoNota29 = "";
		}
		return (formulaUsoNota29);
	}

	public void setFormulaUsoNota29(String formulaUsoNota29) {
		this.formulaUsoNota29 = formulaUsoNota29;
	}

	public String getFormulaCalculoNota29() {
		if (formulaCalculoNota29 == null) {
			formulaCalculoNota29 = "";
		}
		return (formulaCalculoNota29);
	}

	public void setFormulaCalculoNota29(String formulaCalculoNota29) {
		this.formulaCalculoNota29 = formulaCalculoNota29;
	}

	public String getTituloNota29() {
		if (tituloNota29 == null) {
			tituloNota29 = "";
		}
		return (tituloNota29);
	}

	public void setTituloNota29(String tituloNota29) {
		this.tituloNota29 = tituloNota29;
	}

	public Boolean getUtilizarNota29() {
		if (utilizarNota29 == null) {
			utilizarNota29 = Boolean.FALSE;
		}
		return (utilizarNota29);
	}

	public Boolean isUtilizarNota29() {
		return (utilizarNota29);
	}

	public void setUtilizarNota29(Boolean utilizarNota29) {
		this.utilizarNota29 = utilizarNota29;
	}

	public Double getNota29() {
		return (nota29);
	}

	public void setNota29(Double nota29) {
		this.nota29 = nota29;
	}

	public Boolean getNota29MediaFinal() {
		if (nota29MediaFinal == null) {
			nota29MediaFinal = false;
		}
		return nota29MediaFinal;
	}

	public void setNota29MediaFinal(Boolean nota29MediaFinal) {
		this.nota29MediaFinal = nota29MediaFinal;
	}

	public Boolean getUtilizarNota29PorConceito() {
		if (utilizarNota29PorConceito == null) {
			utilizarNota29PorConceito = false;
		}
		return utilizarNota29PorConceito;
	}

	public void setUtilizarNota29PorConceito(Boolean utilizarNota29PorConceito) {
		this.utilizarNota29PorConceito = utilizarNota29PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva29() {
		if (utilizarComoSubstitutiva29 == null) {
			utilizarComoSubstitutiva29 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva29;
	}

	public void setUtilizarComoSubstitutiva29(Boolean utilizarComoSubstitutiva29) {
		this.utilizarComoSubstitutiva29 = utilizarComoSubstitutiva29;
	}

	public Boolean getApresentarNota29() {
		if (apresentarNota29 == null) {
			apresentarNota29 = Boolean.TRUE;
		}
		return apresentarNota29;
	}

	public void setApresentarNota29(Boolean apresentarNota29) {
		this.apresentarNota29 = apresentarNota29;
	}

	public String getPoliticaSubstitutiva29() {
		if (politicaSubstitutiva29 == null) {
			politicaSubstitutiva29 = "";
		}
		return politicaSubstitutiva29;
	}

	public void setPoliticaSubstitutiva29(String politicaSubstitutiva29) {
		this.politicaSubstitutiva29 = politicaSubstitutiva29;
	}

	@XmlElement(name = "configuracaoAcademicoNota29ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota29ConceitoVOs() {
		if (configuracaoAcademicoNota29ConceitoVOs == null) {
			configuracaoAcademicoNota29ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota29ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota29ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota29ConceitoVOs) {
		this.configuracaoAcademicoNota29ConceitoVOs = configuracaoAcademicoNota29ConceitoVOs;
	}

	// **************************************************************

	// NOTA 30 **************************************************************
	public BimestreEnum getBimestreNota30() {
		if (bimestreNota30 == null) {
			bimestreNota30 = BimestreEnum.NAO_CONTROLA;
		}
		return bimestreNota30;
	}

	public void setBimestreNota30(BimestreEnum bimestreNota30) {
		this.bimestreNota30 = bimestreNota30;
	}

	public String getFormulaUsoNota30() {
		if (formulaUsoNota30 == null) {
			formulaUsoNota30 = "";
		}
		return (formulaUsoNota30);
	}

	public void setFormulaUsoNota30(String formulaUsoNota30) {
		this.formulaUsoNota30 = formulaUsoNota30;
	}

	public String getFormulaCalculoNota30() {
		if (formulaCalculoNota30 == null) {
			formulaCalculoNota30 = "";
		}
		return (formulaCalculoNota30);
	}

	public void setFormulaCalculoNota30(String formulaCalculoNota30) {
		this.formulaCalculoNota30 = formulaCalculoNota30;
	}

	public String getTituloNota30() {
		if (tituloNota30 == null) {
			tituloNota30 = "";
		}
		return (tituloNota30);
	}

	public void setTituloNota30(String tituloNota30) {
		this.tituloNota30 = tituloNota30;
	}

	public Boolean getUtilizarNota30() {
		if (utilizarNota30 == null) {
			utilizarNota30 = Boolean.FALSE;
		}
		return (utilizarNota30);
	}

	public Boolean isUtilizarNota30() {
		return (utilizarNota30);
	}

	public void setUtilizarNota30(Boolean utilizarNota30) {
		this.utilizarNota30 = utilizarNota30;
	}

	public Double getNota30() {
		return (nota30);
	}

	public void setNota30(Double nota30) {
		this.nota30 = nota30;
	}

	public Boolean getNota30MediaFinal() {
		if (nota30MediaFinal == null) {
			nota30MediaFinal = false;
		}
		return nota30MediaFinal;
	}

	public void setNota30MediaFinal(Boolean nota30MediaFinal) {
		this.nota30MediaFinal = nota30MediaFinal;
	}

	public Boolean getUtilizarNota30PorConceito() {
		if (utilizarNota30PorConceito == null) {
			utilizarNota30PorConceito = false;
		}
		return utilizarNota30PorConceito;
	}

	public void setUtilizarNota30PorConceito(Boolean utilizarNota30PorConceito) {
		this.utilizarNota30PorConceito = utilizarNota30PorConceito;
	}

	public Boolean getUtilizarComoSubstitutiva30() {
		if (utilizarComoSubstitutiva30 == null) {
			utilizarComoSubstitutiva30 = Boolean.FALSE;
		}
		return utilizarComoSubstitutiva30;
	}

	public void setUtilizarComoSubstitutiva30(Boolean utilizarComoSubstitutiva30) {
		this.utilizarComoSubstitutiva30 = utilizarComoSubstitutiva30;
	}

	public Boolean getApresentarNota30() {
		if (apresentarNota30 == null) {
			apresentarNota30 = Boolean.TRUE;
		}
		return apresentarNota30;
	}

	public void setApresentarNota30(Boolean apresentarNota30) {
		this.apresentarNota30 = apresentarNota30;
	}

	public String getPoliticaSubstitutiva30() {
		if (politicaSubstitutiva30 == null) {
			politicaSubstitutiva30 = "";
		}
		return politicaSubstitutiva30;
	}

	public void setPoliticaSubstitutiva30(String politicaSubstitutiva30) {
		this.politicaSubstitutiva30 = politicaSubstitutiva30;
	}

	@XmlElement(name = "configuracaoAcademicoNota30ConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota30ConceitoVOs() {
		if (configuracaoAcademicoNota30ConceitoVOs == null) {
			configuracaoAcademicoNota30ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNota30ConceitoVOs;
	}

	public void setConfiguracaoAcademicoNota30ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota30ConceitoVOs) {
		this.configuracaoAcademicoNota30ConceitoVOs = configuracaoAcademicoNota30ConceitoVOs;
	}

	public String getTituloNotaApresentar1() {
		if (tituloNotaApresentar1 == null) {
			tituloNotaApresentar1 = "";
		}
		return tituloNotaApresentar1;
	}

	public void setTituloNotaApresentar1(String tituloNotaApresentar1) {
		this.tituloNotaApresentar1 = tituloNotaApresentar1;
	}

	public String getTituloNotaApresentar2() {
		if (tituloNotaApresentar2 == null) {
			tituloNotaApresentar2 = "";
		}
		return tituloNotaApresentar2;
	}

	public void setTituloNotaApresentar2(String tituloNotaApresentar2) {
		this.tituloNotaApresentar2 = tituloNotaApresentar2;
	}

	public String getTituloNotaApresentar3() {
		if (tituloNotaApresentar3 == null) {
			tituloNotaApresentar3 = "";
		}
		return tituloNotaApresentar3;
	}

	public void setTituloNotaApresentar3(String tituloNotaApresentar3) {
		this.tituloNotaApresentar3 = tituloNotaApresentar3;
	}

	public String getTituloNotaApresentar4() {
		if (tituloNotaApresentar4 == null) {
			tituloNotaApresentar4 = "";
		}
		return tituloNotaApresentar4;
	}

	public void setTituloNotaApresentar4(String tituloNotaApresentar4) {
		this.tituloNotaApresentar4 = tituloNotaApresentar4;
	}

	public String getTituloNotaApresentar5() {
		if (tituloNotaApresentar5 == null) {
			tituloNotaApresentar5 = "";
		}
		return tituloNotaApresentar5;
	}

	public void setTituloNotaApresentar5(String tituloNotaApresentar5) {
		this.tituloNotaApresentar5 = tituloNotaApresentar5;
	}

	public String getTituloNotaApresentar6() {
		if (tituloNotaApresentar6 == null) {
			tituloNotaApresentar6 = "";
		}
		return tituloNotaApresentar6;
	}

	public void setTituloNotaApresentar6(String tituloNotaApresentar6) {
		this.tituloNotaApresentar6 = tituloNotaApresentar6;
	}

	public String getTituloNotaApresentar7() {
		if (tituloNotaApresentar7 == null) {
			tituloNotaApresentar7 = "";
		}
		return tituloNotaApresentar7;
	}

	public void setTituloNotaApresentar7(String tituloNotaApresentar7) {
		this.tituloNotaApresentar7 = tituloNotaApresentar7;
	}

	public String getTituloNotaApresentar8() {
		if (tituloNotaApresentar8 == null) {
			tituloNotaApresentar8 = "";
		}
		return tituloNotaApresentar8;
	}

	public void setTituloNotaApresentar8(String tituloNotaApresentar8) {
		this.tituloNotaApresentar8 = tituloNotaApresentar8;
	}

	public String getTituloNotaApresentar9() {
		if (tituloNotaApresentar9 == null) {
			tituloNotaApresentar9 = "";
		}
		return tituloNotaApresentar9;
	}

	public void setTituloNotaApresentar9(String tituloNotaApresentar9) {
		this.tituloNotaApresentar9 = tituloNotaApresentar9;
	}

	public String getTituloNotaApresentar10() {
		if (tituloNotaApresentar10 == null) {
			tituloNotaApresentar10 = "";
		}
		return tituloNotaApresentar10;
	}

	public void setTituloNotaApresentar10(String tituloNotaApresentar10) {
		this.tituloNotaApresentar10 = tituloNotaApresentar10;
	}

	public String getTituloNotaApresentar11() {
		if (tituloNotaApresentar11 == null) {
			tituloNotaApresentar11 = "";
		}
		return tituloNotaApresentar11;
	}

	public void setTituloNotaApresentar11(String tituloNotaApresentar11) {
		this.tituloNotaApresentar11 = tituloNotaApresentar11;
	}

	public String getTituloNotaApresentar12() {
		if (tituloNotaApresentar12 == null) {
			tituloNotaApresentar12 = "";
		}
		return tituloNotaApresentar12;
	}

	public void setTituloNotaApresentar12(String tituloNotaApresentar12) {
		this.tituloNotaApresentar12 = tituloNotaApresentar12;
	}

	public String getTituloNotaApresentar13() {
		if (tituloNotaApresentar13 == null) {
			tituloNotaApresentar13 = "";
		}
		return tituloNotaApresentar13;
	}

	public void setTituloNotaApresentar13(String tituloNotaApresentar13) {
		this.tituloNotaApresentar13 = tituloNotaApresentar13;
	}

	public String getTituloNotaApresentar14() {
		if (tituloNotaApresentar14 == null) {
			tituloNotaApresentar14 = "";
		}
		return tituloNotaApresentar14;
	}

	public void setTituloNotaApresentar14(String tituloNotaApresentar14) {
		this.tituloNotaApresentar14 = tituloNotaApresentar14;
	}

	public String getTituloNotaApresentar15() {
		if (tituloNotaApresentar15 == null) {
			tituloNotaApresentar15 = "";
		}
		return tituloNotaApresentar15;
	}

	public void setTituloNotaApresentar15(String tituloNotaApresentar15) {
		this.tituloNotaApresentar15 = tituloNotaApresentar15;
	}

	public String getTituloNotaApresentar16() {
		if (tituloNotaApresentar16 == null) {
			tituloNotaApresentar16 = "";
		}
		return tituloNotaApresentar16;
	}

	public void setTituloNotaApresentar16(String tituloNotaApresentar16) {
		this.tituloNotaApresentar16 = tituloNotaApresentar16;
	}

	public String getTituloNotaApresentar17() {
		if (tituloNotaApresentar17 == null) {
			tituloNotaApresentar17 = "";
		}
		return tituloNotaApresentar17;
	}

	public void setTituloNotaApresentar17(String tituloNotaApresentar17) {
		this.tituloNotaApresentar17 = tituloNotaApresentar17;
	}

	public String getTituloNotaApresentar18() {
		if (tituloNotaApresentar18 == null) {
			tituloNotaApresentar18 = "";
		}
		return tituloNotaApresentar18;
	}

	public void setTituloNotaApresentar18(String tituloNotaApresentar18) {
		this.tituloNotaApresentar18 = tituloNotaApresentar18;
	}

	public String getTituloNotaApresentar19() {
		if (tituloNotaApresentar19 == null) {
			tituloNotaApresentar19 = "";
		}
		return tituloNotaApresentar19;
	}

	public void setTituloNotaApresentar19(String tituloNotaApresentar19) {
		this.tituloNotaApresentar19 = tituloNotaApresentar19;
	}

	public String getTituloNotaApresentar20() {
		if (tituloNotaApresentar20 == null) {
			tituloNotaApresentar20 = "";
		}
		return tituloNotaApresentar20;
	}

	public void setTituloNotaApresentar20(String tituloNotaApresentar20) {
		this.tituloNotaApresentar20 = tituloNotaApresentar20;
	}

	public String getTituloNotaApresentar21() {
		if (tituloNotaApresentar21 == null) {
			tituloNotaApresentar21 = "";
		}
		return tituloNotaApresentar21;
	}

	public void setTituloNotaApresentar21(String tituloNotaApresentar21) {
		this.tituloNotaApresentar21 = tituloNotaApresentar21;
	}

	public String getTituloNotaApresentar22() {
		if (tituloNotaApresentar22 == null) {
			tituloNotaApresentar22 = "";
		}
		return tituloNotaApresentar22;
	}

	public void setTituloNotaApresentar22(String tituloNotaApresentar22) {
		this.tituloNotaApresentar22 = tituloNotaApresentar22;
	}

	public String getTituloNotaApresentar23() {
		if (tituloNotaApresentar23 == null) {
			tituloNotaApresentar23 = "";
		}
		return tituloNotaApresentar23;
	}

	public void setTituloNotaApresentar23(String tituloNotaApresentar23) {
		this.tituloNotaApresentar23 = tituloNotaApresentar23;
	}

	public String getTituloNotaApresentar24() {
		if (tituloNotaApresentar24 == null) {
			tituloNotaApresentar24 = "";
		}
		return tituloNotaApresentar24;
	}

	public void setTituloNotaApresentar24(String tituloNotaApresentar24) {
		this.tituloNotaApresentar24 = tituloNotaApresentar24;
	}

	public String getTituloNotaApresentar25() {
		if (tituloNotaApresentar25 == null) {
			tituloNotaApresentar25 = "";
		}
		return tituloNotaApresentar25;
	}

	public void setTituloNotaApresentar25(String tituloNotaApresentar25) {
		this.tituloNotaApresentar25 = tituloNotaApresentar25;
	}

	public String getTituloNotaApresentar26() {
		if (tituloNotaApresentar26 == null) {
			tituloNotaApresentar26 = "";
		}
		return tituloNotaApresentar26;
	}

	public void setTituloNotaApresentar26(String tituloNotaApresentar26) {
		this.tituloNotaApresentar26 = tituloNotaApresentar26;
	}

	public String getTituloNotaApresentar27() {
		if (tituloNotaApresentar27 == null) {
			tituloNotaApresentar27 = "";
		}
		return tituloNotaApresentar27;
	}

	public void setTituloNotaApresentar27(String tituloNotaApresentar27) {
		this.tituloNotaApresentar27 = tituloNotaApresentar27;
	}

	public String getTituloNotaApresentar28() {
		if (tituloNotaApresentar28 == null) {
			tituloNotaApresentar28 = "";
		}
		return tituloNotaApresentar28;
	}

	public void setTituloNotaApresentar28(String tituloNotaApresentar28) {
		this.tituloNotaApresentar28 = tituloNotaApresentar28;
	}

	public String getTituloNotaApresentar29() {
		if (tituloNotaApresentar29 == null) {
			tituloNotaApresentar29 = "";
		}
		return tituloNotaApresentar29;
	}

	public void setTituloNotaApresentar29(String tituloNotaApresentar29) {
		this.tituloNotaApresentar29 = tituloNotaApresentar29;
	}

	public String getTituloNotaApresentar30() {
		if (tituloNotaApresentar30 == null) {
			tituloNotaApresentar30 = "";
		}
		return tituloNotaApresentar30;
	}

	public void setTituloNotaApresentar30(String tituloNotaApresentar30) {
		this.tituloNotaApresentar30 = tituloNotaApresentar30;
	}

	/**
	 * @return the controlarAvancoPeriodoPorCreditoOuCH
	 */
	public Boolean getControlarAvancoPeriodoPorCreditoOuCH() {
		if (controlarAvancoPeriodoPorCreditoOuCH == null) {
			controlarAvancoPeriodoPorCreditoOuCH = Boolean.FALSE;
		}
		return controlarAvancoPeriodoPorCreditoOuCH;
	}

	/**
	 * @param controlarAvancoPeriodoPorCreditoOuCH
	 *            the controlarAvancoPeriodoPorCreditoOuCH to set
	 */
	public void setControlarAvancoPeriodoPorCreditoOuCH(Boolean controlarAvancoPeriodoPorCreditoOuCH) {
		this.controlarAvancoPeriodoPorCreditoOuCH = controlarAvancoPeriodoPorCreditoOuCH;
	}

	public String getTipoControleAvancoPeriodoPorCreditoOuCH_Apresentar() {
		if (getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CR")) {
			return "Créditos Disciplinas Período Letivo";
		}
		if (getTipoControleAvancoPeriodoPorCreditoOuCH().equals("CH")) {
			return "Carga Horária Disciplinas Período Letivo";
		}
		if (getTipoControleAvancoPeriodoPorCreditoOuCH().equals("QT")) {
			return "Quantidade Disciplinas Pendentes";
		}
		return "";
	}

	/**
	 * @return the tipoControleAvancoPeriodoPorCreditoOuCH
	 */
	public String getTipoControleAvancoPeriodoPorCreditoOuCH() {
		if (tipoControleAvancoPeriodoPorCreditoOuCH == null) {
			tipoControleAvancoPeriodoPorCreditoOuCH = "CR";
		}
		return tipoControleAvancoPeriodoPorCreditoOuCH;
	}

	/**
	 * @param tipoControleAvancoPeriodoPorCreditoOuCH
	 *            the tipoControleAvancoPeriodoPorCreditoOuCH to set
	 */
	public void setTipoControleAvancoPeriodoPorCreditoOuCH(String tipoControleAvancoPeriodoPorCreditoOuCH) {
		this.tipoControleAvancoPeriodoPorCreditoOuCH = tipoControleAvancoPeriodoPorCreditoOuCH;
	}

	/**
	 * @return the percCumprirPeriodoAnteriorRenovarProximoPerLetivo
	 */
	public Integer getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo() {
		if (percCumprirPeriodoAnteriorRenovarProximoPerLetivo == null) {
			percCumprirPeriodoAnteriorRenovarProximoPerLetivo = 0;
		}
		return percCumprirPeriodoAnteriorRenovarProximoPerLetivo;
	}

	/**
	 * @param percCumprirPeriodoAnteriorRenovarProximoPerLetivo
	 *            the percCumprirPeriodoAnteriorRenovarProximoPerLetivo to set
	 */
	public void setPercCumprirPeriodoAnteriorRenovarProximoPerLetivo(Integer percCumprirPeriodoAnteriorRenovarProximoPerLetivo) {
		this.percCumprirPeriodoAnteriorRenovarProximoPerLetivo = percCumprirPeriodoAnteriorRenovarProximoPerLetivo;
	}

	/**
	 * @return the percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo
	 */
	public Integer getPercCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo() {
		if (percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo == null) {
			percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo = 0;
		}
		return percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo;
	}

	/**
	 * @param percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo
	 *            the percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo to set
	 */
	public void setPercCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo(Integer percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo) {
		this.percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo = percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo;
	}

	/**
	 * @return the controlarInclusaoDisciplinaPorNrMaxCreditoOuCH
	 */
	public Boolean getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH() {
		if (controlarInclusaoDisciplinaPorNrMaxCreditoOuCH == null) {
			controlarInclusaoDisciplinaPorNrMaxCreditoOuCH = Boolean.FALSE;
		}
		return controlarInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	/**
	 * @param controlarInclusaoDisciplinaPorNrMaxCreditoOuCH
	 *            the controlarInclusaoDisciplinaPorNrMaxCreditoOuCH to set
	 */
	public void setControlarInclusaoDisciplinaPorNrMaxCreditoOuCH(Boolean controlarInclusaoDisciplinaPorNrMaxCreditoOuCH) {
		this.controlarInclusaoDisciplinaPorNrMaxCreditoOuCH = controlarInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	public String getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH_Apresentar() {
		if (getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CR")) {
			return "Crédito";
		}
		if (getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH().equals("CH")) {
			return "Carga Horária";
		}
		return "";
	}

	/**
	 * @return the tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH
	 */
	public String getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH() {
		if (tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH == null) {
			tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH = "CR";
		}
		return tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	/**
	 * @param tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH
	 *            the tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH to set
	 */
	public void setTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH(String tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH) {
		this.tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH = tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH;
	}

	/**
	 * @return the acumularCreditosOuCHPeriodosAnterioresNaoCumpridos
	 */
	public Boolean getAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos() {
		if (acumularCreditosOuCHPeriodosAnterioresNaoCumpridos == null) {
			acumularCreditosOuCHPeriodosAnterioresNaoCumpridos = Boolean.TRUE;
		}
		return acumularCreditosOuCHPeriodosAnterioresNaoCumpridos;
	}

	/**
	 * @param acumularCreditosOuCHPeriodosAnterioresNaoCumpridos
	 *            the acumularCreditosOuCHPeriodosAnterioresNaoCumpridos to set
	 */
	public void setAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos(Boolean acumularCreditosOuCHPeriodosAnterioresNaoCumpridos) {
		this.acumularCreditosOuCHPeriodosAnterioresNaoCumpridos = acumularCreditosOuCHPeriodosAnterioresNaoCumpridos;
	}

	/**
	 * @return the permitirInclusaoDiscipDependenciaComChoqueHorario
	 */
	public Boolean getPermitirInclusaoDiscipDependenciaComChoqueHorario() {
		if (permitirInclusaoDiscipDependenciaComChoqueHorario == null) {
			permitirInclusaoDiscipDependenciaComChoqueHorario = Boolean.FALSE;
		}
		return permitirInclusaoDiscipDependenciaComChoqueHorario;
	}

	/**
	 * @param permitirInclusaoDiscipDependenciaComChoqueHorario
	 *            the permitirInclusaoDiscipDependenciaComChoqueHorario to set
	 */
	public void setPermitirInclusaoDiscipDependenciaComChoqueHorario(Boolean permitirInclusaoDiscipDependenciaComChoqueHorario) {
		this.permitirInclusaoDiscipDependenciaComChoqueHorario = permitirInclusaoDiscipDependenciaComChoqueHorario;
	}

	/**
	 * @return the qtdPermitirInclusaoDiscipDependenciaComChoqueHorario
	 */
	public Integer getQtdPermitirInclusaoDiscipDependenciaComChoqueHorario() {
		if (qtdPermitirInclusaoDiscipDependenciaComChoqueHorario == null) {
			qtdPermitirInclusaoDiscipDependenciaComChoqueHorario = 0;
		}
		return qtdPermitirInclusaoDiscipDependenciaComChoqueHorario;
	}

	/**
	 * @param qtdPermitirInclusaoDiscipDependenciaComChoqueHorario
	 *            the qtdPermitirInclusaoDiscipDependenciaComChoqueHorario to
	 *            set
	 */
	public void setQtdPermitirInclusaoDiscipDependenciaComChoqueHorario(Integer qtdPermitirInclusaoDiscipDependenciaComChoqueHorario) {
		this.qtdPermitirInclusaoDiscipDependenciaComChoqueHorario = qtdPermitirInclusaoDiscipDependenciaComChoqueHorario;
	}

	/**
	 * @return the permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta
	 */
	public Boolean getPermitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta() {
		if (permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta == null) {
			permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta = Boolean.FALSE;
		}
		return permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta;
	}

	/**
	 * @param permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta
	 *            the permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta to
	 *            set
	 */
	public void setPermitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta(Boolean permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta) {
		this.permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta = permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta;
	}

	public Boolean getApresentarSiglaConcessaoCredito() {
		if (apresentarSiglaConcessaoCredito == null) {
			apresentarSiglaConcessaoCredito = Boolean.FALSE;
		}
		return apresentarSiglaConcessaoCredito;
	}

	public void setApresentarSiglaConcessaoCredito(Boolean apresentarSiglaConcessaoCredito) {
		this.apresentarSiglaConcessaoCredito = apresentarSiglaConcessaoCredito;
	}

	public Double getFaixaNota1Menor() {
		if (faixaNota1Menor == null) {
			faixaNota1Menor = 0.0;
		}
		return faixaNota1Menor;
	}

	public void setFaixaNota1Menor(Double faixaNota1Menor) {
		this.faixaNota1Menor = faixaNota1Menor;
	}

	public Double getFaixaNota1Maior() {
		if (faixaNota1Maior == null) {
			faixaNota1Maior = 10.0;
		}
		return faixaNota1Maior;
	}

	public void setFaixaNota1Maior(Double faixaNota1Maior) {
		this.faixaNota1Maior = faixaNota1Maior;
	}

	public Double getFaixaNota2Menor() {
		if (faixaNota2Menor == null) {
			faixaNota2Menor = 0.0;
		}
		return faixaNota2Menor;
	}

	public void setFaixaNota2Menor(Double faixaNota2Menor) {
		this.faixaNota2Menor = faixaNota2Menor;
	}

	public Double getFaixaNota2Maior() {
		if (faixaNota2Maior == null) {
			faixaNota2Maior = 10.0;
		}
		return faixaNota2Maior;
	}

	public void setFaixaNota2Maior(Double faixaNota2Maior) {
		this.faixaNota2Maior = faixaNota2Maior;
	}

	public Double getFaixaNota3Menor() {
		if (faixaNota3Menor == null) {
			faixaNota3Menor = 0.0;
		}
		return faixaNota3Menor;
	}

	public void setFaixaNota3Menor(Double faixaNota3Menor) {
		this.faixaNota3Menor = faixaNota3Menor;
	}

	public Double getFaixaNota3Maior() {
		if (faixaNota3Maior == null) {
			faixaNota3Maior = 10.0;
		}
		return faixaNota3Maior;
	}

	public void setFaixaNota3Maior(Double faixaNota3Maior) {
		this.faixaNota3Maior = faixaNota3Maior;
	}

	public Double getFaixaNota4Menor() {
		if (faixaNota4Menor == null) {
			faixaNota4Menor = 0.0;
		}
		return faixaNota4Menor;
	}

	public void setFaixaNota4Menor(Double faixaNota4Menor) {
		this.faixaNota4Menor = faixaNota4Menor;
	}

	public Double getFaixaNota4Maior() {
		if (faixaNota4Maior == null) {
			faixaNota4Maior = 10.0;
		}
		return faixaNota4Maior;
	}

	public void setFaixaNota4Maior(Double faixaNota4Maior) {
		this.faixaNota4Maior = faixaNota4Maior;
	}

	public Double getFaixaNota5Menor() {
		if (faixaNota5Menor == null) {
			faixaNota5Menor = 0.0;
		}
		return faixaNota5Menor;
	}

	public void setFaixaNota5Menor(Double faixaNota5Menor) {
		this.faixaNota5Menor = faixaNota5Menor;
	}

	public Double getFaixaNota5Maior() {
		if (faixaNota5Maior == null) {
			faixaNota5Maior = 10.0;
		}
		return faixaNota5Maior;
	}

	public void setFaixaNota5Maior(Double faixaNota5Maior) {
		this.faixaNota5Maior = faixaNota5Maior;
	}

	public Double getFaixaNota6Menor() {
		if (faixaNota6Menor == null) {
			faixaNota6Menor = 0.0;
		}
		return faixaNota6Menor;
	}

	public void setFaixaNota6Menor(Double faixaNota6Menor) {
		this.faixaNota6Menor = faixaNota6Menor;
	}

	public Double getFaixaNota6Maior() {
		if (faixaNota6Maior == null) {
			faixaNota6Maior = 10.0;
		}
		return faixaNota6Maior;
	}

	public void setFaixaNota6Maior(Double faixaNota6Maior) {
		this.faixaNota6Maior = faixaNota6Maior;
	}

	public Double getFaixaNota7Menor() {
		if (faixaNota7Menor == null) {
			faixaNota7Menor = 0.0;
		}
		return faixaNota7Menor;
	}

	public void setFaixaNota7Menor(Double faixaNota7Menor) {
		this.faixaNota7Menor = faixaNota7Menor;
	}

	public Double getFaixaNota7Maior() {
		if (faixaNota7Maior == null) {
			faixaNota7Maior = 10.0;
		}
		return faixaNota7Maior;
	}

	public void setFaixaNota7Maior(Double faixaNota7Maior) {
		this.faixaNota7Maior = faixaNota7Maior;
	}

	public Double getFaixaNota8Menor() {
		if (faixaNota8Menor == null) {
			faixaNota8Menor = 0.0;
		}
		return faixaNota8Menor;
	}

	public void setFaixaNota8Menor(Double faixaNota8Menor) {
		this.faixaNota8Menor = faixaNota8Menor;
	}

	public Double getFaixaNota8Maior() {
		if (faixaNota8Maior == null) {
			faixaNota8Maior = 10.0;
		}
		return faixaNota8Maior;
	}

	public void setFaixaNota8Maior(Double faixaNota8Maior) {
		this.faixaNota8Maior = faixaNota8Maior;
	}

	public Double getFaixaNota9Menor() {
		if (faixaNota9Menor == null) {
			faixaNota9Menor = 0.0;
		}
		return faixaNota9Menor;
	}

	public void setFaixaNota9Menor(Double faixaNota9Menor) {
		this.faixaNota9Menor = faixaNota9Menor;
	}

	public Double getFaixaNota9Maior() {
		if (faixaNota9Maior == null) {
			faixaNota9Maior = 10.0;
		}
		return faixaNota9Maior;
	}

	public void setFaixaNota9Maior(Double faixaNota9Maior) {
		this.faixaNota9Maior = faixaNota9Maior;
	}

	public Double getFaixaNota10Menor() {
		if (faixaNota10Menor == null) {
			faixaNota10Menor = 0.0;
		}
		return faixaNota10Menor;
	}

	public void setFaixaNota10Menor(Double faixaNota10Menor) {
		this.faixaNota10Menor = faixaNota10Menor;
	}

	public Double getFaixaNota10Maior() {
		if (faixaNota10Maior == null) {
			faixaNota10Maior = 10.0;
		}
		return faixaNota10Maior;
	}

	public void setFaixaNota10Maior(Double faixaNota10Maior) {
		this.faixaNota10Maior = faixaNota10Maior;
	}

	public Double getFaixaNota11Menor() {
		if (faixaNota11Menor == null) {
			faixaNota11Menor = 0.0;
		}
		return faixaNota11Menor;
	}

	public void setFaixaNota11Menor(Double faixaNota11Menor) {
		this.faixaNota11Menor = faixaNota11Menor;
	}

	public Double getFaixaNota11Maior() {
		if (faixaNota11Maior == null) {
			faixaNota11Maior = 10.0;
		}
		return faixaNota11Maior;
	}

	public void setFaixaNota11Maior(Double faixaNota11Maior) {
		this.faixaNota11Maior = faixaNota11Maior;
	}

	public Double getFaixaNota12Menor() {
		if (faixaNota12Menor == null) {
			faixaNota12Menor = 0.0;
		}
		return faixaNota12Menor;
	}

	public void setFaixaNota12Menor(Double faixaNota12Menor) {
		this.faixaNota12Menor = faixaNota12Menor;
	}

	public Double getFaixaNota12Maior() {
		if (faixaNota12Maior == null) {
			faixaNota12Maior = 10.0;
		}
		return faixaNota12Maior;
	}

	public void setFaixaNota12Maior(Double faixaNota12Maior) {
		this.faixaNota12Maior = faixaNota12Maior;
	}

	public Double getFaixaNota13Menor() {
		if (faixaNota13Menor == null) {
			faixaNota13Menor = 0.0;
		}
		return faixaNota13Menor;
	}

	public void setFaixaNota13Menor(Double faixaNota13Menor) {
		this.faixaNota13Menor = faixaNota13Menor;
	}

	public Double getFaixaNota13Maior() {
		if (faixaNota13Maior == null) {
			faixaNota13Maior = 10.0;
		}
		return faixaNota13Maior;
	}

	public void setFaixaNota13Maior(Double faixaNota13Maior) {
		this.faixaNota13Maior = faixaNota13Maior;
	}

	public Double getFaixaNota14Menor() {
		if (faixaNota14Menor == null) {
			faixaNota14Menor = 0.0;
		}
		return faixaNota14Menor;
	}

	public void setFaixaNota14Menor(Double faixaNota14Menor) {
		this.faixaNota14Menor = faixaNota14Menor;
	}

	public Double getFaixaNota14Maior() {
		if (faixaNota14Maior == null) {
			faixaNota14Maior = 10.0;
		}
		return faixaNota14Maior;
	}

	public void setFaixaNota14Maior(Double faixaNota14Maior) {
		this.faixaNota14Maior = faixaNota14Maior;
	}

	public Double getFaixaNota15Menor() {
		if (faixaNota15Menor == null) {
			faixaNota15Menor = 0.0;
		}
		return faixaNota15Menor;
	}

	public void setFaixaNota15Menor(Double faixaNota15Menor) {
		this.faixaNota15Menor = faixaNota15Menor;
	}

	public Double getFaixaNota15Maior() {
		if (faixaNota15Maior == null) {
			faixaNota15Maior = 10.0;
		}
		return faixaNota15Maior;
	}

	public void setFaixaNota15Maior(Double faixaNota15Maior) {
		this.faixaNota15Maior = faixaNota15Maior;
	}

	public Double getFaixaNota16Menor() {
		if (faixaNota16Menor == null) {
			faixaNota16Menor = 0.0;
		}
		return faixaNota16Menor;
	}

	public void setFaixaNota16Menor(Double faixaNota16Menor) {
		this.faixaNota16Menor = faixaNota16Menor;
	}

	public Double getFaixaNota16Maior() {
		if (faixaNota16Maior == null) {
			faixaNota16Maior = 10.0;
		}
		return faixaNota16Maior;
	}

	public void setFaixaNota16Maior(Double faixaNota16Maior) {
		this.faixaNota16Maior = faixaNota16Maior;
	}

	public Double getFaixaNota17Menor() {
		if (faixaNota17Menor == null) {
			faixaNota17Menor = 0.0;
		}
		return faixaNota17Menor;
	}

	public void setFaixaNota17Menor(Double faixaNota17Menor) {
		this.faixaNota17Menor = faixaNota17Menor;
	}

	public Double getFaixaNota17Maior() {
		if (faixaNota17Maior == null) {
			faixaNota17Maior = 10.0;
		}
		return faixaNota17Maior;
	}

	public void setFaixaNota17Maior(Double faixaNota17Maior) {
		this.faixaNota17Maior = faixaNota17Maior;
	}

	public Double getFaixaNota18Menor() {
		if (faixaNota18Menor == null) {
			faixaNota18Menor = 0.0;
		}
		return faixaNota18Menor;
	}

	public void setFaixaNota18Menor(Double faixaNota18Menor) {
		this.faixaNota18Menor = faixaNota18Menor;
	}

	public Double getFaixaNota18Maior() {
		if (faixaNota18Maior == null) {
			faixaNota18Maior = 10.0;
		}
		return faixaNota18Maior;
	}

	public void setFaixaNota18Maior(Double faixaNota18Maior) {
		this.faixaNota18Maior = faixaNota18Maior;
	}

	public Double getFaixaNota19Menor() {
		if (faixaNota19Menor == null) {
			faixaNota19Menor = 0.0;
		}
		return faixaNota19Menor;
	}

	public void setFaixaNota19Menor(Double faixaNota19Menor) {
		this.faixaNota19Menor = faixaNota19Menor;
	}

	public Double getFaixaNota19Maior() {
		if (faixaNota19Maior == null) {
			faixaNota19Maior = 10.0;
		}
		return faixaNota19Maior;
	}

	public void setFaixaNota19Maior(Double faixaNota19Maior) {
		this.faixaNota19Maior = faixaNota19Maior;
	}

	public Double getFaixaNota20Menor() {
		if (faixaNota20Menor == null) {
			faixaNota20Menor = 0.0;
		}
		return faixaNota20Menor;
	}

	public void setFaixaNota20Menor(Double faixaNota20Menor) {
		this.faixaNota20Menor = faixaNota20Menor;
	}

	public Double getFaixaNota20Maior() {
		if (faixaNota20Maior == null) {
			faixaNota20Maior = 10.0;
		}
		return faixaNota20Maior;
	}

	public void setFaixaNota20Maior(Double faixaNota20Maior) {
		this.faixaNota20Maior = faixaNota20Maior;
	}

	public Double getFaixaNota21Menor() {
		if (faixaNota21Menor == null) {
			faixaNota21Menor = 0.0;
		}
		return faixaNota21Menor;
	}

	public void setFaixaNota21Menor(Double faixaNota21Menor) {
		this.faixaNota21Menor = faixaNota21Menor;
	}

	public Double getFaixaNota21Maior() {
		if (faixaNota21Maior == null) {
			faixaNota21Maior = 10.0;
		}
		return faixaNota21Maior;
	}

	public void setFaixaNota21Maior(Double faixaNota21Maior) {
		this.faixaNota21Maior = faixaNota21Maior;
	}

	public Double getFaixaNota22Menor() {
		if (faixaNota22Menor == null) {
			faixaNota22Menor = 0.0;
		}
		return faixaNota22Menor;
	}

	public void setFaixaNota22Menor(Double faixaNota22Menor) {
		this.faixaNota22Menor = faixaNota22Menor;
	}

	public Double getFaixaNota22Maior() {
		if (faixaNota22Maior == null) {
			faixaNota22Maior = 10.0;
		}
		return faixaNota22Maior;
	}

	public void setFaixaNota22Maior(Double faixaNota22Maior) {
		this.faixaNota22Maior = faixaNota22Maior;
	}

	public Double getFaixaNota23Menor() {
		if (faixaNota23Menor == null) {
			faixaNota23Menor = 0.0;
		}
		return faixaNota23Menor;
	}

	public void setFaixaNota23Menor(Double faixaNota23Menor) {
		this.faixaNota23Menor = faixaNota23Menor;
	}

	public Double getFaixaNota23Maior() {
		if (faixaNota23Maior == null) {
			faixaNota23Maior = 10.0;
		}
		return faixaNota23Maior;
	}

	public void setFaixaNota23Maior(Double faixaNota23Maior) {
		this.faixaNota23Maior = faixaNota23Maior;
	}

	public Double getFaixaNota24Menor() {
		if (faixaNota24Menor == null) {
			faixaNota24Menor = 0.0;
		}
		return faixaNota24Menor;
	}

	public void setFaixaNota24Menor(Double faixaNota24Menor) {
		this.faixaNota24Menor = faixaNota24Menor;
	}

	public Double getFaixaNota24Maior() {
		if (faixaNota24Maior == null) {
			faixaNota24Maior = 10.0;
		}
		return faixaNota24Maior;
	}

	public void setFaixaNota24Maior(Double faixaNota24Maior) {
		this.faixaNota24Maior = faixaNota24Maior;
	}

	public Double getFaixaNota25Menor() {
		if (faixaNota25Menor == null) {
			faixaNota25Menor = 0.0;
		}
		return faixaNota25Menor;
	}

	public void setFaixaNota25Menor(Double faixaNota25Menor) {
		this.faixaNota25Menor = faixaNota25Menor;
	}

	public Double getFaixaNota25Maior() {
		if (faixaNota25Maior == null) {
			faixaNota25Maior = 10.0;
		}
		return faixaNota25Maior;
	}

	public void setFaixaNota25Maior(Double faixaNota25Maior) {
		this.faixaNota25Maior = faixaNota25Maior;
	}

	public Double getFaixaNota26Menor() {
		if (faixaNota26Menor == null) {
			faixaNota26Menor = 0.0;
		}
		return faixaNota26Menor;
	}

	public void setFaixaNota26Menor(Double faixaNota26Menor) {
		this.faixaNota26Menor = faixaNota26Menor;
	}

	public Double getFaixaNota26Maior() {
		if (faixaNota26Maior == null) {
			faixaNota26Maior = 10.0;
		}
		return faixaNota26Maior;
	}

	public void setFaixaNota26Maior(Double faixaNota26Maior) {
		this.faixaNota26Maior = faixaNota26Maior;
	}

	public Double getFaixaNota27Menor() {
		if (faixaNota27Menor == null) {
			faixaNota27Menor = 0.0;
		}
		return faixaNota27Menor;
	}

	public void setFaixaNota27Menor(Double faixaNota27Menor) {
		this.faixaNota27Menor = faixaNota27Menor;
	}

	public Double getFaixaNota27Maior() {
		if (faixaNota27Maior == null) {
			faixaNota27Maior = 10.0;
		}
		return faixaNota27Maior;
	}

	public void setFaixaNota27Maior(Double faixaNota27Maior) {
		this.faixaNota27Maior = faixaNota27Maior;
	}

	public Double getFaixaNota28Menor() {
		if (faixaNota28Menor == null) {
			faixaNota28Menor = 0.0;
		}
		return faixaNota28Menor;
	}

	public void setFaixaNota28Menor(Double faixaNota28Menor) {
		this.faixaNota28Menor = faixaNota28Menor;
	}

	public Double getFaixaNota28Maior() {
		if (faixaNota28Maior == null) {
			faixaNota28Maior = 10.0;
		}
		return faixaNota28Maior;
	}

	public void setFaixaNota28Maior(Double faixaNota28Maior) {
		this.faixaNota28Maior = faixaNota28Maior;
	}

	public Double getFaixaNota29Menor() {
		if (faixaNota29Menor == null) {
			faixaNota29Menor = 0.0;
		}
		return faixaNota29Menor;
	}

	public void setFaixaNota29Menor(Double faixaNota29Menor) {
		this.faixaNota29Menor = faixaNota29Menor;
	}

	public Double getFaixaNota29Maior() {
		if (faixaNota29Maior == null) {
			faixaNota29Maior = 10.0;
		}
		return faixaNota29Maior;
	}

	public void setFaixaNota29Maior(Double faixaNota29Maior) {
		this.faixaNota29Maior = faixaNota29Maior;
	}

	public Double getFaixaNota30Menor() {
		if (faixaNota30Menor == null) {
			faixaNota30Menor = 0.0;
		}
		return faixaNota30Menor;
	}

	public void setFaixaNota30Menor(Double faixaNota30Menor) {
		this.faixaNota30Menor = faixaNota30Menor;
	}

	public Double getFaixaNota30Maior() {
		if (faixaNota30Maior == null) {
			faixaNota30Maior = 10.0;
		}
		return faixaNota30Maior;
	}

	public void setFaixaNota30Maior(Double faixaNota30Maior) {
		this.faixaNota30Maior = faixaNota30Maior;
	}

	@XmlElement(name = "quantidadeCasasDecimaisPermitirAposVirgula")
	public Integer getQuantidadeCasasDecimaisPermitirAposVirgula() {
		if (quantidadeCasasDecimaisPermitirAposVirgula == null) {
			quantidadeCasasDecimaisPermitirAposVirgula = 1;
		}
		return quantidadeCasasDecimaisPermitirAposVirgula;
	}

	public void setQuantidadeCasasDecimaisPermitirAposVirgula(Integer quantidadeCasasDecimaisPermitirAposVirgula) {
		this.quantidadeCasasDecimaisPermitirAposVirgula = quantidadeCasasDecimaisPermitirAposVirgula;
	}

	/**
	 * @return the percMinimoCargaHorariaDisciplinaParaAproveitamento
	 */
	public Integer getPercMinimoCargaHorariaDisciplinaParaAproveitamento() {
		if (percMinimoCargaHorariaDisciplinaParaAproveitamento == null) {
			percMinimoCargaHorariaDisciplinaParaAproveitamento = 0;
		}
		return percMinimoCargaHorariaDisciplinaParaAproveitamento;
	}

	/**
	 * @param percMinimoCargaHorariaDisciplinaParaAproveitamento
	 *            the percMinimoCargaHorariaDisciplinaParaAproveitamento to set
	 */
	public void setPercMinimoCargaHorariaDisciplinaParaAproveitamento(Integer percMinimoCargaHorariaDisciplinaParaAproveitamento) {
		this.percMinimoCargaHorariaDisciplinaParaAproveitamento = percMinimoCargaHorariaDisciplinaParaAproveitamento;
	}

	@XmlElement(name = "tipoApresentarFrequenciaVisaoAluno")
	public String getTipoApresentarFrequenciaVisaoAluno() {
		if (tipoApresentarFrequenciaVisaoAluno == null) {
			tipoApresentarFrequenciaVisaoAluno = "PO";
		}
		return tipoApresentarFrequenciaVisaoAluno;
	}

	public void setTipoApresentarFrequenciaVisaoAluno(String tipoApresentarFrequenciaVisaoAluno) {
		this.tipoApresentarFrequenciaVisaoAluno = tipoApresentarFrequenciaVisaoAluno;
	}

	@XmlElement(name = "regraArredondamentoNota1")
	public String getRegraArredondamentoNota1() {
		if (regraArredondamentoNota1 == null) {
			regraArredondamentoNota1 = "";
		}
		return regraArredondamentoNota1;
	}

	public void setRegraArredondamentoNota1(String regraArredondamentoNota1) {
		this.regraArredondamentoNota1 = regraArredondamentoNota1;
	}

	@XmlElement(name = "regraArredondamentoNota2")
	public String getRegraArredondamentoNota2() {
		if (regraArredondamentoNota2 == null) {
			regraArredondamentoNota2 = "";
		}
		return regraArredondamentoNota2;
	}

	public void setRegraArredondamentoNota2(String regraArredondamentoNota2) {
		this.regraArredondamentoNota2 = regraArredondamentoNota2;
	}

	@XmlElement(name = "regraArredondamentoNota3")
	public String getRegraArredondamentoNota3() {
		if (regraArredondamentoNota3 == null) {
			regraArredondamentoNota3 = "";
		}
		return regraArredondamentoNota3;
	}

	public void setRegraArredondamentoNota3(String regraArredondamentoNota3) {
		this.regraArredondamentoNota3 = regraArredondamentoNota3;
	}

	@XmlElement(name = "regraArredondamentoNota4")
	public String getRegraArredondamentoNota4() {
		if (regraArredondamentoNota4 == null) {
			regraArredondamentoNota4 = "";
		}
		return regraArredondamentoNota4;
	}

	public void setRegraArredondamentoNota4(String regraArredondamentoNota4) {
		this.regraArredondamentoNota4 = regraArredondamentoNota4;
	}

	@XmlElement(name = "regraArredondamentoNota5")
	public String getRegraArredondamentoNota5() {
		if (regraArredondamentoNota5 == null) {
			regraArredondamentoNota5 = "";
		}
		return regraArredondamentoNota5;
	}

	public void setRegraArredondamentoNota5(String regraArredondamentoNota5) {
		this.regraArredondamentoNota5 = regraArredondamentoNota5;
	}

	@XmlElement(name = "regraArredondamentoNota7")
	public String getRegraArredondamentoNota6() {
		if (regraArredondamentoNota6 == null) {
			regraArredondamentoNota6 = "";
		}
		return regraArredondamentoNota6;
	}

	public void setRegraArredondamentoNota6(String regraArredondamentoNota6) {
		this.regraArredondamentoNota6 = regraArredondamentoNota6;
	}

	public String getRegraArredondamentoNota7() {
		if (regraArredondamentoNota7 == null) {
			regraArredondamentoNota7 = "";
		}
		return regraArredondamentoNota7;
	}

	public void setRegraArredondamentoNota7(String regraArredondamentoNota7) {
		this.regraArredondamentoNota7 = regraArredondamentoNota7;
	}

	@XmlElement(name = "regraArredondamentoNota8")
	public String getRegraArredondamentoNota8() {
		if (regraArredondamentoNota8 == null) {
			regraArredondamentoNota8 = "";
		}
		return regraArredondamentoNota8;
	}

	public void setRegraArredondamentoNota8(String regraArredondamentoNota8) {
		this.regraArredondamentoNota8 = regraArredondamentoNota8;
	}

	@XmlElement(name = "regraArredondamentoNota9")
	public String getRegraArredondamentoNota9() {
		if (regraArredondamentoNota9 == null) {
			regraArredondamentoNota9 = "";
		}
		return regraArredondamentoNota9;
	}

	public void setRegraArredondamentoNota9(String regraArredondamentoNota9) {
		this.regraArredondamentoNota9 = regraArredondamentoNota9;
	}

	@XmlElement(name = "regraArredondamentoNota10")
	public String getRegraArredondamentoNota10() {
		if (regraArredondamentoNota10 == null) {
			regraArredondamentoNota10 = "";
		}
		return regraArredondamentoNota10;
	}

	public void setRegraArredondamentoNota10(String regraArredondamentoNota10) {
		this.regraArredondamentoNota10 = regraArredondamentoNota10;
	}

	@XmlElement(name = "regraArredondamentoNota11")
	public String getRegraArredondamentoNota11() {
		if (regraArredondamentoNota11 == null) {
			regraArredondamentoNota11 = "";
		}
		return regraArredondamentoNota11;
	}

	public void setRegraArredondamentoNota11(String regraArredondamentoNota11) {
		this.regraArredondamentoNota11 = regraArredondamentoNota11;
	}

	@XmlElement(name = "regraArredondamentoNota12")
	public String getRegraArredondamentoNota12() {
		if (regraArredondamentoNota12 == null) {
			regraArredondamentoNota12 = "";
		}
		return regraArredondamentoNota12;
	}

	public void setRegraArredondamentoNota12(String regraArredondamentoNota12) {
		this.regraArredondamentoNota12 = regraArredondamentoNota12;
	}

	@XmlElement(name = "regraArredondamentoNota13")
	public String getRegraArredondamentoNota13() {
		if (regraArredondamentoNota13 == null) {
			regraArredondamentoNota13 = "";
		}
		return regraArredondamentoNota13;
	}

	public void setRegraArredondamentoNota13(String regraArredondamentoNota13) {
		this.regraArredondamentoNota13 = regraArredondamentoNota13;
	}

	
	public String getRegraArredondamentoNota14() {
		if (regraArredondamentoNota14 == null) {
			regraArredondamentoNota14 = "";
		}
		return regraArredondamentoNota14;
	}

	public void setRegraArredondamentoNota14(String regraArredondamentoNota14) {
		this.regraArredondamentoNota14 = regraArredondamentoNota14;
	}

	public String getRegraArredondamentoNota15() {
		if (regraArredondamentoNota15 == null) {
			regraArredondamentoNota15 = "";
		}
		return regraArredondamentoNota15;
	}

	public void setRegraArredondamentoNota15(String regraArredondamentoNota15) {
		this.regraArredondamentoNota15 = regraArredondamentoNota15;
	}

	public String getRegraArredondamentoNota16() {
		if (regraArredondamentoNota16 == null) {
			regraArredondamentoNota16 = "";
		}
		return regraArredondamentoNota16;
	}

	public void setRegraArredondamentoNota16(String regraArredondamentoNota16) {
		this.regraArredondamentoNota16 = regraArredondamentoNota16;
	}

	public String getRegraArredondamentoNota17() {
		if (regraArredondamentoNota17 == null) {
			regraArredondamentoNota17 = "";
		}
		return regraArredondamentoNota17;
	}

	public void setRegraArredondamentoNota17(String regraArredondamentoNota17) {
		this.regraArredondamentoNota17 = regraArredondamentoNota17;
	}

	public String getRegraArredondamentoNota18() {
		if (regraArredondamentoNota18 == null) {
			regraArredondamentoNota18 = "";
		}
		return regraArredondamentoNota18;
	}

	public void setRegraArredondamentoNota18(String regraArredondamentoNota18) {
		this.regraArredondamentoNota18 = regraArredondamentoNota18;
	}

	public String getRegraArredondamentoNota19() {
		if (regraArredondamentoNota19 == null) {
			regraArredondamentoNota19 = "";
		}
		return regraArredondamentoNota19;
	}

	public void setRegraArredondamentoNota19(String regraArredondamentoNota19) {
		this.regraArredondamentoNota19 = regraArredondamentoNota19;
	}

	public String getRegraArredondamentoNota20() {
		if (regraArredondamentoNota20 == null) {
			regraArredondamentoNota20 = "";
		}
		return regraArredondamentoNota20;
	}

	public void setRegraArredondamentoNota20(String regraArredondamentoNota20) {
		this.regraArredondamentoNota20 = regraArredondamentoNota20;
	}

	public String getRegraArredondamentoNota21() {
		if (regraArredondamentoNota21 == null) {
			regraArredondamentoNota21 = "";
		}
		return regraArredondamentoNota21;
	}

	public void setRegraArredondamentoNota21(String regraArredondamentoNota21) {
		this.regraArredondamentoNota21 = regraArredondamentoNota21;
	}

	public String getRegraArredondamentoNota22() {
		if (regraArredondamentoNota22 == null) {
			regraArredondamentoNota22 = "";
		}
		return regraArredondamentoNota22;
	}

	public void setRegraArredondamentoNota22(String regraArredondamentoNota22) {
		this.regraArredondamentoNota22 = regraArredondamentoNota22;
	}

	public String getRegraArredondamentoNota23() {
		if (regraArredondamentoNota23 == null) {
			regraArredondamentoNota23 = "";
		}
		return regraArredondamentoNota23;
	}

	public void setRegraArredondamentoNota23(String regraArredondamentoNota23) {
		this.regraArredondamentoNota23 = regraArredondamentoNota23;
	}

	public String getRegraArredondamentoNota24() {
		if (regraArredondamentoNota24 == null) {
			regraArredondamentoNota24 = "";
		}
		return regraArredondamentoNota24;
	}

	public void setRegraArredondamentoNota24(String regraArredondamentoNota24) {
		this.regraArredondamentoNota24 = regraArredondamentoNota24;
	}

	public String getRegraArredondamentoNota25() {
		if (regraArredondamentoNota25 == null) {
			regraArredondamentoNota25 = "";
		}
		return regraArredondamentoNota25;
	}

	public void setRegraArredondamentoNota25(String regraArredondamentoNota25) {
		this.regraArredondamentoNota25 = regraArredondamentoNota25;
	}

	public String getRegraArredondamentoNota26() {
		if (regraArredondamentoNota26 == null) {
			regraArredondamentoNota26 = "";
		}
		return regraArredondamentoNota26;
	}

	public void setRegraArredondamentoNota26(String regraArredondamentoNota26) {
		this.regraArredondamentoNota26 = regraArredondamentoNota26;
	}

	public String getRegraArredondamentoNota27() {
		if (regraArredondamentoNota27 == null) {
			regraArredondamentoNota27 = "";
		}
		return regraArredondamentoNota27;
	}

	public void setRegraArredondamentoNota27(String regraArredondamentoNota27) {
		this.regraArredondamentoNota27 = regraArredondamentoNota27;
	}

	public String getRegraArredondamentoNota28() {
		if (regraArredondamentoNota28 == null) {
			regraArredondamentoNota28 = "";
		}
		return regraArredondamentoNota28;
	}

	public void setRegraArredondamentoNota28(String regraArredondamentoNota28) {
		this.regraArredondamentoNota28 = regraArredondamentoNota28;
	}

	public String getRegraArredondamentoNota29() {
		if (regraArredondamentoNota29 == null) {
			regraArredondamentoNota29 = "";
		}
		return regraArredondamentoNota29;
	}

	public void setRegraArredondamentoNota29(String regraArredondamentoNota29) {
		this.regraArredondamentoNota29 = regraArredondamentoNota29;
	}

	public String getRegraArredondamentoNota30() {
		if (regraArredondamentoNota30 == null) {
			regraArredondamentoNota30 = "";
		}
		return regraArredondamentoNota30;
	}

	public void setRegraArredondamentoNota30(String regraArredondamentoNota30) {
		this.regraArredondamentoNota30 = regraArredondamentoNota30;
	}

	/**
	 * Método responsável por executar o arredondamento de nota conforme
	 * configurado na Configuração Acadêmica de cada nota respeitando o padrão
	 * VALOR INICIO-VALOR FIM:VALOR SUBSTITUIR; Ex.: 00-24:00;, todo valor
	 * decimal de nota que estiver entre 00 e 24 será arredondado para 00.
	 */
	public Double executarArredondamentoNota(String regraArredondamentoNota, Double nota) {
		try {
			if (nota != null) {
				if (regraArredondamentoNota.isEmpty()) {
					return nota;
				}
				int posicao = 0;
				String parteRegra = "";
				while (posicao < regraArredondamentoNota.length()) {
					parteRegra = regraArredondamentoNota.substring(posicao, posicao + regraArredondamentoNota.indexOf(";")).replace(";", "");
					nota = obterArredondamentoNota(parteRegra, nota);
					posicao += parteRegra.length() + 1;
				}
			}
			return nota;
		} catch (Exception e) {
			return nota;
		}
	}

	public Double obterArredondamentoNota(String regraArredondamentoNota, Double nota) {
		Integer formulaEsquerda = Integer.parseInt(regraArredondamentoNota.substring(0, regraArredondamentoNota.indexOf("-")));
		Integer formulaDireita = Integer.parseInt(regraArredondamentoNota.substring(regraArredondamentoNota.indexOf("-") + 1, regraArredondamentoNota.indexOf(":")));
		String valorSubstituir = regraArredondamentoNota.substring(regraArredondamentoNota.indexOf(':') + 1, regraArredondamentoNota.length());
		Integer inteiro = Integer.parseInt(String.valueOf(nota).substring(0, String.valueOf(nota).indexOf(".")));
		String decimos = String.valueOf(nota).substring(String.valueOf(nota).indexOf(".") + 1, String.valueOf(nota).length());
		if (decimos.length() == 1) {
			decimos = decimos + "0";
		} else if (decimos.length() > 1) {
			decimos = decimos.substring(0, 2);
		}
		if (Integer.parseInt(decimos) >= formulaEsquerda && (Integer.parseInt(decimos) <= formulaDireita || valorSubstituir.length() == 1)) {
			if (valorSubstituir.length() == 1) {
				return Double.parseDouble(String.valueOf(inteiro + Integer.parseInt(valorSubstituir)));
			} else {
				return Double.parseDouble(inteiro + "." + valorSubstituir);
			}
		}
		return nota;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota1VO() {
		if (configuracaoAcademicaNota1VO == null) {
			configuracaoAcademicaNota1VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_1);
		}
		return configuracaoAcademicaNota1VO;
	}

	public void setConfiguracaoAcademicaNota1VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota1VO) {
		this.configuracaoAcademicaNota1VO = configuracaoAcademicaNota1VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota2VO() {
		if (configuracaoAcademicaNota2VO == null) {
			configuracaoAcademicaNota2VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_2);
		}
		return configuracaoAcademicaNota2VO;
	}

	public void setConfiguracaoAcademicaNota2VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota2VO) {
		this.configuracaoAcademicaNota2VO = configuracaoAcademicaNota2VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota3VO() {
		if (configuracaoAcademicaNota3VO == null) {
			configuracaoAcademicaNota3VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_3);
		}
		return configuracaoAcademicaNota3VO;
	}

	public void setConfiguracaoAcademicaNota3VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota3VO) {
		this.configuracaoAcademicaNota3VO = configuracaoAcademicaNota3VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota4VO() {
		if (configuracaoAcademicaNota4VO == null) {
			configuracaoAcademicaNota4VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_4);
		}
		return configuracaoAcademicaNota4VO;
	}

	public void setConfiguracaoAcademicaNota4VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota4VO) {
		this.configuracaoAcademicaNota4VO = configuracaoAcademicaNota4VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota5VO() {
		if (configuracaoAcademicaNota5VO == null) {
			configuracaoAcademicaNota5VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_5);
		}
		return configuracaoAcademicaNota5VO;
	}

	public void setConfiguracaoAcademicaNota5VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota5VO) {
		this.configuracaoAcademicaNota5VO = configuracaoAcademicaNota5VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota6VO() {
		if (configuracaoAcademicaNota6VO == null) {
			configuracaoAcademicaNota6VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_6);
		}
		return configuracaoAcademicaNota6VO;
	}

	public void setConfiguracaoAcademicaNota6VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota6VO) {
		this.configuracaoAcademicaNota6VO = configuracaoAcademicaNota6VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota7VO() {
		if (configuracaoAcademicaNota7VO == null) {
			configuracaoAcademicaNota7VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_7);
		}
		return configuracaoAcademicaNota7VO;
	}

	public void setConfiguracaoAcademicaNota7VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota7VO) {
		this.configuracaoAcademicaNota7VO = configuracaoAcademicaNota7VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota8VO() {
		if (configuracaoAcademicaNota8VO == null) {
			configuracaoAcademicaNota8VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_8);
		}
		return configuracaoAcademicaNota8VO;
	}

	public void setConfiguracaoAcademicaNota8VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota8VO) {
		this.configuracaoAcademicaNota8VO = configuracaoAcademicaNota8VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota9VO() {
		if (configuracaoAcademicaNota9VO == null) {
			configuracaoAcademicaNota9VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_9);
		}
		return configuracaoAcademicaNota9VO;
	}

	public void setConfiguracaoAcademicaNota9VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota9VO) {
		this.configuracaoAcademicaNota9VO = configuracaoAcademicaNota9VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota10VO() {
		if (configuracaoAcademicaNota10VO == null) {
			configuracaoAcademicaNota10VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_10);
		}
		return configuracaoAcademicaNota10VO;
	}

	public void setConfiguracaoAcademicaNota10VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota10VO) {
		this.configuracaoAcademicaNota10VO = configuracaoAcademicaNota10VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota11VO() {
		if (configuracaoAcademicaNota11VO == null) {
			configuracaoAcademicaNota11VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_11);
		}
		return configuracaoAcademicaNota11VO;
	}

	public void setConfiguracaoAcademicaNota11VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota11VO) {
		this.configuracaoAcademicaNota11VO = configuracaoAcademicaNota11VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota12VO() {
		if (configuracaoAcademicaNota12VO == null) {
			configuracaoAcademicaNota12VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_12);
		}
		return configuracaoAcademicaNota12VO;
	}

	public void setConfiguracaoAcademicaNota12VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota12VO) {
		this.configuracaoAcademicaNota12VO = configuracaoAcademicaNota12VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota13VO() {
		if (configuracaoAcademicaNota13VO == null) {
			configuracaoAcademicaNota13VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_13);
		}
		return configuracaoAcademicaNota13VO;
	}

	public void setConfiguracaoAcademicaNota13VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota13VO) {
		this.configuracaoAcademicaNota13VO = configuracaoAcademicaNota13VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota14VO() {
		if (configuracaoAcademicaNota14VO == null) {
			configuracaoAcademicaNota14VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_14);
		}
		return configuracaoAcademicaNota14VO;
	}

	public void setConfiguracaoAcademicaNota14VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota14VO) {
		this.configuracaoAcademicaNota14VO = configuracaoAcademicaNota14VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota15VO() {
		if (configuracaoAcademicaNota15VO == null) {
			configuracaoAcademicaNota15VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_15);
		}
		return configuracaoAcademicaNota15VO;
	}

	public void setConfiguracaoAcademicaNota15VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota15VO) {
		this.configuracaoAcademicaNota15VO = configuracaoAcademicaNota15VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota16VO() {
		if (configuracaoAcademicaNota16VO == null) {
			configuracaoAcademicaNota16VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_16);
		}
		return configuracaoAcademicaNota16VO;
	}

	public void setConfiguracaoAcademicaNota16VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota16VO) {
		this.configuracaoAcademicaNota16VO = configuracaoAcademicaNota16VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota17VO() {
		if (configuracaoAcademicaNota17VO == null) {
			configuracaoAcademicaNota17VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_17);
		}
		return configuracaoAcademicaNota17VO;
	}

	public void setConfiguracaoAcademicaNota17VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota17VO) {
		this.configuracaoAcademicaNota17VO = configuracaoAcademicaNota17VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota18VO() {
		if (configuracaoAcademicaNota18VO == null) {
			configuracaoAcademicaNota18VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_18);
		}
		return configuracaoAcademicaNota18VO;
	}

	public void setConfiguracaoAcademicaNota18VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota18VO) {
		this.configuracaoAcademicaNota18VO = configuracaoAcademicaNota18VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota19VO() {
		if (configuracaoAcademicaNota19VO == null) {
			configuracaoAcademicaNota19VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_19);
		}
		return configuracaoAcademicaNota19VO;
	}

	public void setConfiguracaoAcademicaNota19VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota19VO) {
		this.configuracaoAcademicaNota19VO = configuracaoAcademicaNota19VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota20VO() {
		if (configuracaoAcademicaNota20VO == null) {
			configuracaoAcademicaNota20VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_20);
		}
		return configuracaoAcademicaNota20VO;
	}

	public void setConfiguracaoAcademicaNota20VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota20VO) {
		this.configuracaoAcademicaNota20VO = configuracaoAcademicaNota20VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota21VO() {
		if (configuracaoAcademicaNota21VO == null) {
			configuracaoAcademicaNota21VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_21);
		}
		return configuracaoAcademicaNota21VO;
	}

	public void setConfiguracaoAcademicaNota21VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota21VO) {
		this.configuracaoAcademicaNota21VO = configuracaoAcademicaNota21VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota22VO() {
		if (configuracaoAcademicaNota22VO == null) {
			configuracaoAcademicaNota22VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_22);
		}
		return configuracaoAcademicaNota22VO;
	}

	public void setConfiguracaoAcademicaNota22VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota22VO) {
		this.configuracaoAcademicaNota22VO = configuracaoAcademicaNota22VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota23VO() {
		if (configuracaoAcademicaNota23VO == null) {
			configuracaoAcademicaNota23VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_23);
		}
		return configuracaoAcademicaNota23VO;
	}

	public void setConfiguracaoAcademicaNota23VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota23VO) {
		this.configuracaoAcademicaNota23VO = configuracaoAcademicaNota23VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota24VO() {
		if (configuracaoAcademicaNota24VO == null) {
			configuracaoAcademicaNota24VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_24);
		}
		return configuracaoAcademicaNota24VO;
	}

	public void setConfiguracaoAcademicaNota24VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota24VO) {
		this.configuracaoAcademicaNota24VO = configuracaoAcademicaNota24VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota25VO() {
		if (configuracaoAcademicaNota25VO == null) {
			configuracaoAcademicaNota25VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_25);
		}
		return configuracaoAcademicaNota25VO;
	}

	public void setConfiguracaoAcademicaNota25VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota25VO) {
		this.configuracaoAcademicaNota25VO = configuracaoAcademicaNota25VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota26VO() {
		if (configuracaoAcademicaNota26VO == null) {
			configuracaoAcademicaNota26VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_26);
		}
		return configuracaoAcademicaNota26VO;
	}

	public void setConfiguracaoAcademicaNota26VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota26VO) {
		this.configuracaoAcademicaNota26VO = configuracaoAcademicaNota26VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota27VO() {
		if (configuracaoAcademicaNota27VO == null) {
			configuracaoAcademicaNota27VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_27);
		}
		return configuracaoAcademicaNota27VO;
	}

	public void setConfiguracaoAcademicaNota27VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota27VO) {
		this.configuracaoAcademicaNota27VO = configuracaoAcademicaNota27VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota28VO() {
		if (configuracaoAcademicaNota28VO == null) {
			configuracaoAcademicaNota28VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_28);
		}
		return configuracaoAcademicaNota28VO;
	}

	public void setConfiguracaoAcademicaNota28VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota28VO) {
		this.configuracaoAcademicaNota28VO = configuracaoAcademicaNota28VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota29VO() {
		if (configuracaoAcademicaNota29VO == null) {
			configuracaoAcademicaNota29VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_29);
		}
		return configuracaoAcademicaNota29VO;
	}

	public void setConfiguracaoAcademicaNota29VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota29VO) {
		this.configuracaoAcademicaNota29VO = configuracaoAcademicaNota29VO;
	}

	public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota30VO() {
		if (configuracaoAcademicaNota30VO == null) {
			configuracaoAcademicaNota30VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_30);
		}
		return configuracaoAcademicaNota30VO;
	}

	public void setConfiguracaoAcademicaNota30VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota30VO) {
		this.configuracaoAcademicaNota30VO = configuracaoAcademicaNota30VO;
	}
	
	public TipoNotaConceitoEnum getNumeroNotaPorVariavel(String variavel){
		for (int y = 1; y <= 40; y++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + y)) {
				String nota1 = (String) UtilReflexao.invocarMetodoGet(this, "tituloNota" + y);
				if(nota1.equals(variavel)){
					return TipoNotaConceitoEnum.getTipoNota(y);
				}
			}
		}
		return null;
	}


	public Boolean getApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico() {
		if (apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico == null) {
			apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico = false;
		}
		return apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico;
	}


	public void setApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico(Boolean apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico) {
		this.apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico = apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico;
	}
	
	/**
	 * @author Victor Hugo 02/02/2015
	 */
	private Boolean utilizarApoioEADParaDisciplinasModalidadePresencial;

	public Boolean getUtilizarApoioEADParaDisciplinasModalidadePresencial() {
		if(utilizarApoioEADParaDisciplinasModalidadePresencial == null) {
			utilizarApoioEADParaDisciplinasModalidadePresencial = false;
		}
		return utilizarApoioEADParaDisciplinasModalidadePresencial;
	}


	public void setUtilizarApoioEADParaDisciplinasModalidadePresencial(Boolean utilizarApoioEADParaDisciplinasModalidadePresencial) {
		this.utilizarApoioEADParaDisciplinasModalidadePresencial = utilizarApoioEADParaDisciplinasModalidadePresencial;
	}
	public String realizarSubstituicaoFuncaoMaior(String formulaCalculo) {
		Integer posInicioMaior = null;
		String formulaMaior = null;
		String[] parametros = null;
		String[] notas = null;
		Integer qtdeNotas = null;
		String acao = null;
		List<NotaVO> notasVOs = null;
		StringBuilder formulaFinal = null;
		try {
			if(formulaCalculo != null && formulaCalculo.contains("MAIOR")){
				posInicioMaior = formulaCalculo.indexOf("MAIOR");		
				formulaMaior = formulaCalculo.substring(posInicioMaior, formulaCalculo.length());
				formulaMaior = formulaMaior.substring(0, formulaMaior.indexOf(")")+1);
				parametros = formulaMaior.replaceAll("MAIOR", "").replace("(", "").replace(")", "").trim().split(",");			
				notas = parametros[0].split(";");
				if(notas.length == 0 || parametros.length < 3){
					formulaCalculo = formulaCalculo.replace(formulaMaior, "(0)");				
					return formulaCalculo;
				}
				qtdeNotas = Integer.valueOf(parametros[1]);
				acao = parametros[2];
				notasVOs = new ArrayList<NotaVO>(0);
				for(String nota: notas){
					NotaVO notaVO = new NotaVO();
					notaVO.setValorTexto(nota);
					notaVO.setValor(nota == null || nota.trim().isEmpty() || nota.equalsIgnoreCase("null")?null:Double.valueOf(nota.trim()));
					notasVOs.add(notaVO);
				}
				Ordenacao.ordenarListaDecrescente(notasVOs, "valor");
				formulaFinal = new StringBuilder(""); 
				for(int x = 0; x<qtdeNotas;x++){
					if(!formulaFinal.toString().isEmpty()){				
						formulaFinal.append(acao);
					}else if(formulaFinal.toString().isEmpty()){
						formulaFinal.append("(");
					}			
					formulaFinal.append(notasVOs.get(x).getValorTexto());
					
				}
				formulaFinal.append(")");
				
				formulaCalculo = formulaCalculo.replace(formulaMaior, formulaFinal.toString());
			}
			return formulaCalculo;
		} finally {
			if (posInicioMaior != null) {
				posInicioMaior = null;
			}
			if (formulaMaior != null) {
				formulaMaior = null;
			}
			if (parametros != null) {
				parametros = null;
			}
			if (notas != null) {
				notas = null;
			}
			if (qtdeNotas != null) {
				qtdeNotas = null;
			}
			if (acao != null) {
				acao = null;
			}
			if (formulaFinal != null) {
				formulaFinal = null;
			}
			if (notasVOs != null) {
				notasVOs.clear();
				notasVOs = null;
			}
		}
	}
	
	public static String validarUsoFuncaoMaior(String formulaCalculo, int numeroNota, String nomeConfiguracao) throws ConsistirException {
		if(formulaCalculo.contains("MAIOR")){
			int posInicioMaior = formulaCalculo.indexOf("MAIOR");		
			String formulaMaior = formulaCalculo.substring(posInicioMaior, formulaCalculo.length());
			formulaMaior = formulaMaior.substring(0, formulaMaior.indexOf(")")+1);
			String[] parametros = formulaMaior.replaceAll("MAIOR", "").replace("(", "").replace(")", "").trim().split(",");
			if(parametros.length != 3){
				throw new ConsistirException("Função MAIOR utilizada na NOTA "+numeroNota+" da configuração "+nomeConfiguracao+" deve possuir 3 parametros ex: MAIOR(NOTA_1;NOTA_2;NOTA_3;NOTA_4, NUMERO_NOTAS_CONSIDERAR, OPERACAO_EXECUTAR)");
			}
			String[] notas = parametros[0].split(";");			
			Integer qtdeNotas = Integer.valueOf(parametros[1]);
			String acao = parametros[2];
			if(notas.length < qtdeNotas){
				throw new ConsistirException("A quantidade de notas informadas na função MAIOR utilizada na NOTA "+numeroNota+" da configuração "+nomeConfiguracao+" deve ser maior ou igual ao segundo parâmetro da função.");
			}			
			if(!acao.trim().equals("+") && !acao.trim().equals("/") && !acao.trim().equals("*") && !acao.trim().equals("-")){
				throw new ConsistirException("O terceiro parâmetro da função MAIOR utilizada na NOTA "+numeroNota+" da configuração "+nomeConfiguracao+" deve possuir um dos seguintes caracteres +, -, *, /.");
			}
			List<NotaVO> notasVOs = new ArrayList<NotaVO>(0);
			for(String nota: notas){
				NotaVO notaVO = new NotaVO();
				notaVO.setValorTexto(nota);
				notaVO.setValor(nota == null || nota.trim().isEmpty() || nota.equalsIgnoreCase("null")?null:Double.valueOf(nota.trim()));
				notasVOs.add(notaVO);
			}
			Ordenacao.ordenarListaDecrescente(notasVOs, "valor");
			StringBuilder formulaFinal = new StringBuilder(""); 
			for(int x = 0; x<qtdeNotas;x++){
				if(!formulaFinal.toString().isEmpty() && notasVOs.get(x).getValor() != null){				
					formulaFinal.append(acao);
				}else if(formulaFinal.toString().isEmpty() && notasVOs.get(x).getValor() != null){
					formulaFinal.append("(");
				}			
				if(notasVOs.get(x).getValor() != null){
					formulaFinal.append(notasVOs.get(x).getValor().toString());
				}
			}
			formulaFinal.append(")");
			
			formulaCalculo = formulaCalculo.replace(formulaMaior, formulaFinal.toString());
		}	
		return formulaCalculo;
	}
	
	public Boolean getObrigaInformarFormaIngressoMatricula() {
		if (obrigaInformarFormaIngressoMatricula == null) {
			obrigaInformarFormaIngressoMatricula = Boolean.FALSE;
		}
		return obrigaInformarFormaIngressoMatricula;
	}

	public void setObrigaInformarFormaIngressoMatricula(Boolean obrigaInformarFormaIngressoMatricula) {
		this.obrigaInformarFormaIngressoMatricula = obrigaInformarFormaIngressoMatricula;
	}

	public Boolean getObrigaInformarOrigemFormaIngressoMatricula() {
		if (obrigaInformarOrigemFormaIngressoMatricula == null) {
			obrigaInformarOrigemFormaIngressoMatricula = Boolean.FALSE;
		}
		return obrigaInformarOrigemFormaIngressoMatricula;
	}

	public void setObrigaInformarOrigemFormaIngressoMatricula(Boolean obrigaInformarOrigemFormaIngressoMatricula) {
		this.obrigaInformarOrigemFormaIngressoMatricula = obrigaInformarOrigemFormaIngressoMatricula;
	}	

	public Boolean getBloquearRegistroAulaAnteriorDataMatricula() {
		if (bloquearRegistroAulaAnteriorDataMatricula == null) {
			bloquearRegistroAulaAnteriorDataMatricula = Boolean.FALSE;
		}
		return bloquearRegistroAulaAnteriorDataMatricula;
	}

	public void setBloquearRegistroAulaAnteriorDataMatricula(Boolean bloquearRegistroAulaAnteriorDataMatricula) {
		this.bloquearRegistroAulaAnteriorDataMatricula = bloquearRegistroAulaAnteriorDataMatricula;
	}
	
	public Boolean getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao() {
		if (ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao == null) {
			ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao = false;
		}
		return ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao;
	}


	public void setOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao(Boolean ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao) {
		this.ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao = ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao;
	}


	/**
	 * @return the situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal
	 */
	public Boolean getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal() {
		if (situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal == null) {
			situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal = false;
		}
		return situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal;
	}


	/**
	 * @param situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal the situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal to set
	 */
	public void setSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal(Boolean situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal) {
		this.situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal = situacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal;
	}
	


	/**
	 * @return the calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes
	 */
	public Boolean getCalcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes() {
		if (calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes == null) {
			calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes = false;
		}
		return calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes;
	}


	/**
	 * @param calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes the calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes to set
	 */
	public void setCalcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes(Boolean calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes) {
		this.calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes = calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes;
	}
	
	/**
	 * @return the habilitarControleInclusaoDisciplinaPeriodoFuturo
	 */
	public Boolean getHabilitarControleInclusaoDisciplinaPeriodoFuturo() {
		if (habilitarControleInclusaoDisciplinaPeriodoFuturo == null) {
			habilitarControleInclusaoDisciplinaPeriodoFuturo = false;
		}
		return habilitarControleInclusaoDisciplinaPeriodoFuturo;
	}


	/**
	 * @param habilitarControleInclusaoDisciplinaPeriodoFuturo the habilitarControleInclusaoDisciplinaPeriodoFuturo to set
	 */
	public void setHabilitarControleInclusaoDisciplinaPeriodoFuturo(Boolean habilitarControleInclusaoDisciplinaPeriodoFuturo) {
		this.habilitarControleInclusaoDisciplinaPeriodoFuturo = habilitarControleInclusaoDisciplinaPeriodoFuturo;
	}


	/**
	 * @return the numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina
	 */
	public Integer getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina() {
		if (numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina == null) {
			numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina = 0;
		}
		return numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina;
	}


	/**
	 * @param numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina the numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina to set
	 */
	public void setNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina(Integer numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina) {
		this.numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina = numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina;
	}


	/**
	 * @return the bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular
	 */
	public Boolean getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() {
		if (bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular == null) {
			bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular =false;
		}
		return bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
	}


	/**
	 * @param bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular the bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular to set
	 */
	public void setBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular) {
		this.bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
	}


	/**
	 * @return the bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular
	 */
	public Boolean getBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular() {
		if (bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular == null) {
			bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = false;
		}
		return bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
	}


	/**
	 * @param bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular the bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular to set
	 */
	public void setBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular) {
		this.bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
	}


	/**
	 * @return the habilitarControleInclusaoObrigatoriaDisciplinaDependencia
	 */
	public Boolean getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() {
		if (habilitarControleInclusaoObrigatoriaDisciplinaDependencia == null) {
			habilitarControleInclusaoObrigatoriaDisciplinaDependencia = false;
		}
		return habilitarControleInclusaoObrigatoriaDisciplinaDependencia;
	}


	/**
	 * @param habilitarControleInclusaoObrigatoriaDisciplinaDependencia the habilitarControleInclusaoObrigatoriaDisciplinaDependencia to set
	 */
	public void setHabilitarControleInclusaoObrigatoriaDisciplinaDependencia(Boolean habilitarControleInclusaoObrigatoriaDisciplinaDependencia) {
		this.habilitarControleInclusaoObrigatoriaDisciplinaDependencia = habilitarControleInclusaoObrigatoriaDisciplinaDependencia;
	}


	/**
	 * @return the porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia
	 */
	public Integer getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia() {
		if (porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia == null) {
			porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia = 0;
		}
		return porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia;
	}


	/**
	 * @param porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia the porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia to set
	 */
	public void setPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia(Integer porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia) {
		this.porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia = porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia;
	}


	/**
	 * @return the removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline
	 */
	public Boolean getRemoverAutomaticamenteDisciplinaSemVagaRenovacaoOnline() {
		if (removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline == null) {
			removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline = false;
		}
		return removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline;
	}
	

	/**
	 * @param removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline the removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline to set
	 */
	public void setRemoverAutomaticamenteDisciplinaSemVagaRenovacaoOnline(Boolean removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline) {
		this.removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline = removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline;
	}


	/**
	 * @return the habilitarDistribuicaoTurmaPraticaTeoricaRenovacao
	 */
	public Boolean getHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao() {
		if (habilitarDistribuicaoTurmaPraticaTeoricaRenovacao == null) {
			habilitarDistribuicaoTurmaPraticaTeoricaRenovacao = false;
		}
		return habilitarDistribuicaoTurmaPraticaTeoricaRenovacao;
	}


	/**
	 * @param habilitarDistribuicaoTurmaPraticaTeoricaRenovacao the habilitarDistribuicaoTurmaPraticaTeoricaRenovacao to set
	 */
	public void setHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao(Boolean habilitarDistribuicaoTurmaPraticaTeoricaRenovacao) {
		this.habilitarDistribuicaoTurmaPraticaTeoricaRenovacao = habilitarDistribuicaoTurmaPraticaTeoricaRenovacao;
	}

	public boolean isHabilitarDistribuicaoDisciplinaDependenciaAutomatica() {
		return habilitarDistribuicaoDisciplinaDependenciaAutomatica;
	}


	public void setHabilitarDistribuicaoDisciplinaDependenciaAutomatica(boolean habilitarDistribuicaoDisciplinaDependenciaAutomatica) {
		this.habilitarDistribuicaoDisciplinaDependenciaAutomatica = habilitarDistribuicaoDisciplinaDependenciaAutomatica;
	}

	public boolean isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares() {
		return habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares;
	}

	public void setHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares(boolean habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares) {
		this.habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares = habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares;
	}


	public boolean isAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo() {
		return alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo;
	}

	public void setAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo(boolean alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo) {
		this.alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo = alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo;
	}

	/**
	 * @return the distribuirTurmaPraticaTeoricaComAulaProgramada
	 */
	public Boolean getDistribuirTurmaPraticaTeoricaComAulaProgramada() {
		if (distribuirTurmaPraticaTeoricaComAulaProgramada == null) {
			distribuirTurmaPraticaTeoricaComAulaProgramada = false;
		}
		return distribuirTurmaPraticaTeoricaComAulaProgramada;
	}


	/**
	 * @param distribuirTurmaPraticaTeoricaComAulaProgramada the distribuirTurmaPraticaTeoricaComAulaProgramada to set
	 */
	public void setDistribuirTurmaPraticaTeoricaComAulaProgramada(Boolean distribuirTurmaPraticaTeoricaComAulaProgramada) {
		this.distribuirTurmaPraticaTeoricaComAulaProgramada = distribuirTurmaPraticaTeoricaComAulaProgramada;
	}


	/**
	 * @return the removerDisciplinaTurmaPraticaTeoricaComChoqueHorario
	 */
	public Boolean getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario() {
		if (removerDisciplinaTurmaPraticaTeoricaComChoqueHorario == null) {
			removerDisciplinaTurmaPraticaTeoricaComChoqueHorario = false;
		}
		return removerDisciplinaTurmaPraticaTeoricaComChoqueHorario;
	}


	/**
	 * @param removerDisciplinaTurmaPraticaTeoricaComChoqueHorario the removerDisciplinaTurmaPraticaTeoricaComChoqueHorario to set
	 */
	public void setRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(Boolean removerDisciplinaTurmaPraticaTeoricaComChoqueHorario) {
		this.removerDisciplinaTurmaPraticaTeoricaComChoqueHorario = removerDisciplinaTurmaPraticaTeoricaComChoqueHorario;
	}


	/**
	 * @return the considerarRegularAlunoDependenciaOptativa
	 */
	public Boolean getConsiderarRegularAlunoDependenciaOptativa() {
		if (considerarRegularAlunoDependenciaOptativa == null) {
			considerarRegularAlunoDependenciaOptativa = false;
		}
		return considerarRegularAlunoDependenciaOptativa;
	}


	/**
	 * @param considerarRegularAlunoDependenciaOptativa the considerarRegularAlunoDependenciaOptativa to set
	 */
	public void setConsiderarRegularAlunoDependenciaOptativa(Boolean considerarRegularAlunoDependenciaOptativa) {
		this.considerarRegularAlunoDependenciaOptativa = considerarRegularAlunoDependenciaOptativa;
	}
   
	public Boolean getApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno() {
		if(apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno == null){
			apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno = Boolean.TRUE;
		}
		return apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno;
	}


	public void setApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno(Boolean apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno) {
		this.apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno = apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno;
	}

	public boolean isFiltrarNota1() {
		return filtrarNota1;
	}

	public void setFiltrarNota1(boolean filtrarNota1) {
		this.filtrarNota1 = filtrarNota1;
	}

	public boolean isFiltrarNota2() {
		return filtrarNota2;
	}

	public void setFiltrarNota2(boolean filtrarNota2) {
		this.filtrarNota2 = filtrarNota2;
	}

	public boolean isFiltrarNota3() {
		return filtrarNota3;
	}

	public void setFiltrarNota3(boolean filtrarNota3) {
		this.filtrarNota3 = filtrarNota3;
	}

	public boolean isFiltrarNota4() {
		return filtrarNota4;
	}

	public void setFiltrarNota4(boolean filtrarNota4) {
		this.filtrarNota4 = filtrarNota4;
	}

	public boolean isFiltrarNota5() {
		return filtrarNota5;
	}

	public void setFiltrarNota5(boolean filtrarNota5) {
		this.filtrarNota5 = filtrarNota5;
	}

	public boolean isFiltrarNota6() {
		return filtrarNota6;
	}

	public void setFiltrarNota6(boolean filtrarNota6) {
		this.filtrarNota6 = filtrarNota6;
	}

	public boolean isFiltrarNota7() {
		return filtrarNota7;
	}

	public void setFiltrarNota7(boolean filtrarNota7) {
		this.filtrarNota7 = filtrarNota7;
	}

	public boolean isFiltrarNota8() {
		return filtrarNota8;
	}

	public void setFiltrarNota8(boolean filtrarNota8) {
		this.filtrarNota8 = filtrarNota8;
	}

	public boolean isFiltrarNota9() {
		return filtrarNota9;
	}

	public void setFiltrarNota9(boolean filtrarNota9) {
		this.filtrarNota9 = filtrarNota9;
	}

	public boolean isFiltrarNota10() {
		return filtrarNota10;
	}

	public void setFiltrarNota10(boolean filtrarNota10) {
		this.filtrarNota10 = filtrarNota10;
	}

	public boolean isFiltrarNota11() {
		return filtrarNota11;
	}

	public void setFiltrarNota11(boolean filtrarNota11) {
		this.filtrarNota11 = filtrarNota11;
	}

	public boolean isFiltrarNota12() {
		return filtrarNota12;
	}

	public void setFiltrarNota12(boolean filtrarNota12) {
		this.filtrarNota12 = filtrarNota12;
	}

	public boolean isFiltrarNota13() {
		return filtrarNota13;
	}

	public void setFiltrarNota13(boolean filtrarNota13) {
		this.filtrarNota13 = filtrarNota13;
	}

	public boolean isFiltrarNota14() {
		return filtrarNota14;
	}

	public void setFiltrarNota14(boolean filtrarNota14) {
		this.filtrarNota14 = filtrarNota14;
	}

	public boolean isFiltrarNota15() {
		return filtrarNota15;
	}

	public void setFiltrarNota15(boolean filtrarNota15) {
		this.filtrarNota15 = filtrarNota15;
	}

	public boolean isFiltrarNota16() {
		return filtrarNota16;
	}

	public void setFiltrarNota16(boolean filtrarNota16) {
		this.filtrarNota16 = filtrarNota16;
	}

	public boolean isFiltrarNota17() {
		return filtrarNota17;
	}

	public void setFiltrarNota17(boolean filtrarNota17) {
		this.filtrarNota17 = filtrarNota17;
	}

	public boolean isFiltrarNota18() {
		return filtrarNota18;
	}

	public void setFiltrarNota18(boolean filtrarNota18) {
		this.filtrarNota18 = filtrarNota18;
	}

	public boolean isFiltrarNota19() {
		return filtrarNota19;
	}

	public void setFiltrarNota19(boolean filtrarNota19) {
		this.filtrarNota19 = filtrarNota19;
	}

	public boolean isFiltrarNota20() {
		return filtrarNota20;
	}

	public void setFiltrarNota20(boolean filtrarNota20) {
		this.filtrarNota20 = filtrarNota20;
	}

	public boolean isFiltrarNota21() {
		return filtrarNota21;
	}

	public void setFiltrarNota21(boolean filtrarNota21) {
		this.filtrarNota21 = filtrarNota21;
	}

	public boolean isFiltrarNota22() {
		return filtrarNota22;
	}

	public void setFiltrarNota22(boolean filtrarNota22) {
		this.filtrarNota22 = filtrarNota22;
	}

	public boolean isFiltrarNota23() {
		return filtrarNota23;
	}

	public void setFiltrarNota23(boolean filtrarNota23) {
		this.filtrarNota23 = filtrarNota23;
	}

	public boolean isFiltrarNota24() {
		return filtrarNota24;
	}

	public void setFiltrarNota24(boolean filtrarNota24) {
		this.filtrarNota24 = filtrarNota24;
	}

	public boolean isFiltrarNota25() {
		return filtrarNota25;
	}

	public void setFiltrarNota25(boolean filtrarNota25) {
		this.filtrarNota25 = filtrarNota25;
	}

	public boolean isFiltrarNota26() {
		return filtrarNota26;
	}

	public void setFiltrarNota26(boolean filtrarNota26) {
		this.filtrarNota26 = filtrarNota26;
	}

	public boolean isFiltrarNota27() {
		return filtrarNota27;
	}

	public void setFiltrarNota27(boolean filtrarNota27) {
		this.filtrarNota27 = filtrarNota27;
	}

	public boolean isFiltrarNota28() {
		return filtrarNota28;
	}

	public void setFiltrarNota28(boolean filtrarNota28) {
		this.filtrarNota28 = filtrarNota28;
	}

	public boolean isFiltrarNota29() {
		return filtrarNota29;
	}

	public void setFiltrarNota29(boolean filtrarNota29) {
		this.filtrarNota29 = filtrarNota29;
	}

	public boolean isFiltrarNota30() {
		return filtrarNota30;
	}

	public void setFiltrarNota30(boolean filtrarNota30) {
		this.filtrarNota30 = filtrarNota30;
	}
	
	public void realizarMarcarTodasNotas(){
		realizarSelecionarTodosNotas(true);
	}
	
	public void realizarDesmarcarTodasNotas(){
		realizarSelecionarTodosNotas(false);
	}
	
	public void realizarSelecionarTodosNotas(boolean selecionado){
		setFiltrarNota1(selecionado);
		setFiltrarNota2(selecionado);
		setFiltrarNota3(selecionado);
		setFiltrarNota4(selecionado);
		setFiltrarNota5(selecionado);
		setFiltrarNota6(selecionado);
		setFiltrarNota7(selecionado);
		setFiltrarNota8(selecionado);
		setFiltrarNota9(selecionado);
		setFiltrarNota10(selecionado);
		setFiltrarNota11(selecionado);
		setFiltrarNota12(selecionado);
		setFiltrarNota13(selecionado);
		setFiltrarNota14(selecionado);
		setFiltrarNota15(selecionado);
		setFiltrarNota16(selecionado);
		setFiltrarNota17(selecionado);
		setFiltrarNota18(selecionado);
		setFiltrarNota19(selecionado);
		setFiltrarNota20(selecionado);
		setFiltrarNota21(selecionado);
		setFiltrarNota22(selecionado);
		setFiltrarNota23(selecionado);
		setFiltrarNota24(selecionado);
		setFiltrarNota25(selecionado);
		setFiltrarNota26(selecionado);
		setFiltrarNota27(selecionado);
		setFiltrarNota28(selecionado);
		setFiltrarNota29(selecionado);
		setFiltrarNota30(selecionado);
		setFiltrarNota31(selecionado);
		setFiltrarNota32(selecionado);
		setFiltrarNota33(selecionado);
		setFiltrarNota34(selecionado);
		setFiltrarNota35(selecionado);
		setFiltrarNota36(selecionado);
		setFiltrarNota37(selecionado);
		setFiltrarNota38(selecionado);
		setFiltrarNota39(selecionado);
		setFiltrarNota40(selecionado);
	}


	/**
	 * @return the considerarPortadoDiplomaTransEntradaAlunoIrregular
	 */
	public Boolean getConsiderarPortadoDiplomaTransEntradaAlunoIrregular() {
		if (considerarPortadoDiplomaTransEntradaAlunoIrregular == null) {
			considerarPortadoDiplomaTransEntradaAlunoIrregular = false;
		}
		return considerarPortadoDiplomaTransEntradaAlunoIrregular;
	}


	/**
	 * @param considerarPortadoDiplomaTransEntradaAlunoIrregular the considerarPortadoDiplomaTransEntradaAlunoIrregular to set
	 */
	public void setConsiderarPortadoDiplomaTransEntradaAlunoIrregular(Boolean considerarPortadoDiplomaTransEntradaAlunoIrregular) {
		this.considerarPortadoDiplomaTransEntradaAlunoIrregular = considerarPortadoDiplomaTransEntradaAlunoIrregular;
	}


	/**
	 * @return the matricularApenasDisciplinaAulaProgramada
	 */
	public Boolean getMatricularApenasDisciplinaAulaProgramada() {
		if (matricularApenasDisciplinaAulaProgramada == null) {
			matricularApenasDisciplinaAulaProgramada = false;
		}
		return matricularApenasDisciplinaAulaProgramada;
	}


	/**
	 * @param matricularApenasDisciplinaAulaProgramada the matricularApenasDisciplinaAulaProgramada to set
	 */
	public void setMatricularApenasDisciplinaAulaProgramada(Boolean matricularApenasDisciplinaAulaProgramada) {
		this.matricularApenasDisciplinaAulaProgramada = matricularApenasDisciplinaAulaProgramada;
	}


	public Integer getQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula() {
		if (quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula == null) {
			quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula = 0;
		}
		return quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula;
	}


	public void setQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula(Integer quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula) {
		this.quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula = quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula;
	}


	public Boolean getIncluirAutomaticamenteDisciplinaGrupoOptativa() {
		if(incluirAutomaticamenteDisciplinaGrupoOptativa == null){
			incluirAutomaticamenteDisciplinaGrupoOptativa = true;
		}
		return incluirAutomaticamenteDisciplinaGrupoOptativa;
	}


	public void setIncluirAutomaticamenteDisciplinaGrupoOptativa(Boolean incluirAutomaticamenteDisciplinaGrupoOptativa) {
		this.incluirAutomaticamenteDisciplinaGrupoOptativa = incluirAutomaticamenteDisciplinaGrupoOptativa;
	}


	public Boolean getPermitirAlunoRegularIncluirDisciplinaGrupoOptativa() {
		if(permitirAlunoRegularIncluirDisciplinaGrupoOptativa == null){
			permitirAlunoRegularIncluirDisciplinaGrupoOptativa = false;
		}
		return permitirAlunoRegularIncluirDisciplinaGrupoOptativa;
	}


	public void setPermitirAlunoRegularIncluirDisciplinaGrupoOptativa(
			Boolean permitirAlunoRegularIncluirDisciplinaGrupoOptativa) {
		this.permitirAlunoRegularIncluirDisciplinaGrupoOptativa = permitirAlunoRegularIncluirDisciplinaGrupoOptativa;
	}


	public Boolean getBloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa() {
		if(bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa == null){
			bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa = false;
		}
		return bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa;
	}


	public void setBloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa(
			Boolean bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa) {
		this.bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa = bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa;
	}
	
	// NOTA 31 **************************************************************
		public BimestreEnum getBimestreNota31() {
			if (bimestreNota31 == null) {
				bimestreNota31 = BimestreEnum.NAO_CONTROLA;
			}
			return bimestreNota31;
		}

		public void setBimestreNota31(BimestreEnum bimestreNota31) {
			this.bimestreNota31 = bimestreNota31;
		}

		public String getFormulaUsoNota31() {
			if (formulaUsoNota31 == null) {
				formulaUsoNota31 = "";
			}
			return (formulaUsoNota31);
		}

		public void setFormulaUsoNota31(String formulaUsoNota31) {
			this.formulaUsoNota31 = formulaUsoNota31;
		}

		public String getFormulaCalculoNota31() {
			if (formulaCalculoNota31 == null) {
				formulaCalculoNota31 = "";
			}
			return (formulaCalculoNota31);
		}

		public void setFormulaCalculoNota31(String formulaCalculoNota31) {
			this.formulaCalculoNota31 = formulaCalculoNota31;
		}

		public String getTituloNota31() {
			if (tituloNota31 == null) {
				tituloNota31 = "";
			}
			return (tituloNota31);
		}

		public void setTituloNota31(String tituloNota31) {
			this.tituloNota31 = tituloNota31;
		}

		public Boolean getUtilizarNota31() {
			if (utilizarNota31 == null) {
				utilizarNota31 = Boolean.FALSE;
			}
			return (utilizarNota31);
		}

		public Boolean isUtilizarNota31() {
			return (utilizarNota31);
		}

		public void setUtilizarNota31(Boolean utilizarNota31) {
			this.utilizarNota31 = utilizarNota31;
		}

		public Double getNota31() {
			return (nota31);
		}

		public void setNota31(Double nota31) {
			this.nota31 = nota31;
		}

		public Boolean getNota31MediaFinal() {
			if (nota31MediaFinal == null) {
				nota31MediaFinal = false;
			}
			return nota31MediaFinal;
		}

		public void setNota31MediaFinal(Boolean nota31MediaFinal) {
			this.nota31MediaFinal = nota31MediaFinal;
		}

		public Boolean getUtilizarNota31PorConceito() {
			if (utilizarNota31PorConceito == null) {
				utilizarNota31PorConceito = false;
			}
			return utilizarNota31PorConceito;
		}

		public void setUtilizarNota31PorConceito(Boolean utilizarNota31PorConceito) {
			this.utilizarNota31PorConceito = utilizarNota31PorConceito;
		}

		public Boolean getUtilizarComoSubstitutiva31() {
			if (utilizarComoSubstitutiva31 == null) {
				utilizarComoSubstitutiva31 = Boolean.FALSE;
			}
			return utilizarComoSubstitutiva31;
		}

		public void setUtilizarComoSubstitutiva31(Boolean utilizarComoSubstitutiva31) {
			this.utilizarComoSubstitutiva31 = utilizarComoSubstitutiva31;
		}

		public Boolean getApresentarNota31() {
			if (apresentarNota31 == null) {
				apresentarNota31 = Boolean.TRUE;
			}
			return apresentarNota31;
		}

		public void setApresentarNota31(Boolean apresentarNota31) {
			this.apresentarNota31 = apresentarNota31;
		}

		public String getPoliticaSubstitutiva31() {
			if (politicaSubstitutiva31 == null) {
				politicaSubstitutiva31 = "";
			}
			return politicaSubstitutiva31;
		}

		public void setPoliticaSubstitutiva31(String politicaSubstitutiva31) {
			this.politicaSubstitutiva31 = politicaSubstitutiva31;
		}

		@XmlElement(name = "configuracaoAcademicoNota31ConceitoVOs")
		public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota31ConceitoVOs() {
			if (configuracaoAcademicoNota31ConceitoVOs == null) {
				configuracaoAcademicoNota31ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
			}
			return configuracaoAcademicoNota31ConceitoVOs;
		}

		public void setConfiguracaoAcademicoNota31ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota31ConceitoVOs) {
			this.configuracaoAcademicoNota31ConceitoVOs = configuracaoAcademicoNota31ConceitoVOs;
		}
		
		public String getTituloNotaApresentar31() {
			if (tituloNotaApresentar31 == null) {
				tituloNotaApresentar31 = "";
			}
			return tituloNotaApresentar31;
		}

		public void setTituloNotaApresentar31(String tituloNotaApresentar31) {
			this.tituloNotaApresentar31 = tituloNotaApresentar31;
		}

		public Double getFaixaNota31Menor() {
			if (faixaNota31Menor == null) {
				faixaNota31Menor = 0.0;
			}
			return faixaNota31Menor;
		}

		public void setFaixaNota31Menor(Double faixaNota31Menor) {
			this.faixaNota31Menor = faixaNota31Menor;
		}

		public Double getFaixaNota31Maior() {
			if (faixaNota31Maior == null) {
				faixaNota31Maior = 10.0;
			}
			return faixaNota31Maior;
		}

		public void setFaixaNota31Maior(Double faixaNota31Maior) {
			this.faixaNota31Maior = faixaNota31Maior;
		}

		public String getRegraArredondamentoNota31() {
			if (regraArredondamentoNota31 == null) {
				regraArredondamentoNota31 = "";
			}
			return regraArredondamentoNota31;
		}

		public void setRegraArredondamentoNota31(String regraArredondamentoNota31) {
			this.regraArredondamentoNota31 = regraArredondamentoNota31;
		}

		public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota31VO() {
			if (configuracaoAcademicaNota31VO == null) {
				configuracaoAcademicaNota31VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_31);
			}
			return configuracaoAcademicaNota31VO;
		}

		public void setConfiguracaoAcademicaNota31VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota31VO) {
			this.configuracaoAcademicaNota31VO = configuracaoAcademicaNota31VO;
		}

		public boolean isFiltrarNota31() {
			return filtrarNota31;
		}

		public void setFiltrarNota31(boolean filtrarNota31) {
			this.filtrarNota31 = filtrarNota31;
		}

		
		// NOTA 32 **************************************************************
				public BimestreEnum getBimestreNota32() {
					if (bimestreNota32 == null) {
						bimestreNota32 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota32;
				}

				public void setBimestreNota32(BimestreEnum bimestreNota32) {
					this.bimestreNota32 = bimestreNota32;
				}

				public String getFormulaUsoNota32() {
					if (formulaUsoNota32 == null) {
						formulaUsoNota32 = "";
					}
					return (formulaUsoNota32);
				}

				public void setFormulaUsoNota32(String formulaUsoNota32) {
					this.formulaUsoNota32 = formulaUsoNota32;
				}

				public String getFormulaCalculoNota32() {
					if (formulaCalculoNota32 == null) {
						formulaCalculoNota32 = "";
					}
					return (formulaCalculoNota32);
				}

				public void setFormulaCalculoNota32(String formulaCalculoNota32) {
					this.formulaCalculoNota32 = formulaCalculoNota32;
				}

				public String getTituloNota32() {
					if (tituloNota32 == null) {
						tituloNota32 = "";
					}
					return (tituloNota32);
				}

				public void setTituloNota32(String tituloNota32) {
					this.tituloNota32 = tituloNota32;
				}

				public Boolean getUtilizarNota32() {
					if (utilizarNota32 == null) {
						utilizarNota32 = Boolean.FALSE;
					}
					return (utilizarNota32);
				}

				public Boolean isUtilizarNota32() {
					return (utilizarNota32);
				}

				public void setUtilizarNota32(Boolean utilizarNota32) {
					this.utilizarNota32 = utilizarNota32;
				}

				public Double getNota32() {
					return (nota32);
				}

				public void setNota32(Double nota32) {
					this.nota32 = nota32;
				}

				public Boolean getNota32MediaFinal() {
					if (nota32MediaFinal == null) {
						nota32MediaFinal = false;
					}
					return nota32MediaFinal;
				}

				public void setNota32MediaFinal(Boolean nota32MediaFinal) {
					this.nota32MediaFinal = nota32MediaFinal;
				}

				public Boolean getUtilizarNota32PorConceito() {
					if (utilizarNota32PorConceito == null) {
						utilizarNota32PorConceito = false;
					}
					return utilizarNota32PorConceito;
				}

				public void setUtilizarNota32PorConceito(Boolean utilizarNota32PorConceito) {
					this.utilizarNota32PorConceito = utilizarNota32PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva32() {
					if (utilizarComoSubstitutiva32 == null) {
						utilizarComoSubstitutiva32 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva32;
				}

				public void setUtilizarComoSubstitutiva32(Boolean utilizarComoSubstitutiva32) {
					this.utilizarComoSubstitutiva32 = utilizarComoSubstitutiva32;
				}

				public Boolean getApresentarNota32() {
					if (apresentarNota32 == null) {
						apresentarNota32 = Boolean.TRUE;
					}
					return apresentarNota32;
				}

				public void setApresentarNota32(Boolean apresentarNota32) {
					this.apresentarNota32 = apresentarNota32;
				}

				public String getPoliticaSubstitutiva32() {
					if (politicaSubstitutiva32 == null) {
						politicaSubstitutiva32 = "";
					}
					return politicaSubstitutiva32;
				}

				public void setPoliticaSubstitutiva32(String politicaSubstitutiva32) {
					this.politicaSubstitutiva32 = politicaSubstitutiva32;
				}

				@XmlElement(name = "configuracaoAcademicoNota32ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota32ConceitoVOs() {
					if (configuracaoAcademicoNota32ConceitoVOs == null) {
						configuracaoAcademicoNota32ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota32ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota32ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota32ConceitoVOs) {
					this.configuracaoAcademicoNota32ConceitoVOs = configuracaoAcademicoNota32ConceitoVOs;
				}
				
				public String getTituloNotaApresentar32() {
					if (tituloNotaApresentar32 == null) {
						tituloNotaApresentar32 = "";
					}
					return tituloNotaApresentar32;
				}

				public void setTituloNotaApresentar32(String tituloNotaApresentar32) {
					this.tituloNotaApresentar32 = tituloNotaApresentar32;
				}

				public Double getFaixaNota32Menor() {
					if (faixaNota32Menor == null) {
						faixaNota32Menor = 0.0;
					}
					return faixaNota32Menor;
				}

				public void setFaixaNota32Menor(Double faixaNota32Menor) {
					this.faixaNota32Menor = faixaNota32Menor;
				}

				public Double getFaixaNota32Maior() {
					if (faixaNota32Maior == null) {
						faixaNota32Maior = 10.0;
					}
					return faixaNota32Maior;
				}

				public void setFaixaNota32Maior(Double faixaNota32Maior) {
					this.faixaNota32Maior = faixaNota32Maior;
				}

				public String getRegraArredondamentoNota32() {
					if (regraArredondamentoNota32 == null) {
						regraArredondamentoNota32 = "";
					}
					return regraArredondamentoNota32;
				}

				public void setRegraArredondamentoNota32(String regraArredondamentoNota32) {
					this.regraArredondamentoNota32 = regraArredondamentoNota32;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota32VO() {
					if (configuracaoAcademicaNota32VO == null) {
						configuracaoAcademicaNota32VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_32);
					}
					return configuracaoAcademicaNota32VO;
				}

				public void setConfiguracaoAcademicaNota32VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota32VO) {
					this.configuracaoAcademicaNota32VO = configuracaoAcademicaNota32VO;
				}

				public boolean isFiltrarNota32() {
					return filtrarNota32;
				}

				public void setFiltrarNota32(boolean filtrarNota32) {
					this.filtrarNota32 = filtrarNota32;
				}

				
				// NOTA 33 **************************************************************
				public BimestreEnum getBimestreNota33() {
					if (bimestreNota33 == null) {
						bimestreNota33 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota33;
				}

				public void setBimestreNota33(BimestreEnum bimestreNota33) {
					this.bimestreNota33 = bimestreNota33;
				}

				public String getFormulaUsoNota33() {
					if (formulaUsoNota33 == null) {
						formulaUsoNota33 = "";
					}
					return (formulaUsoNota33);
				}

				public void setFormulaUsoNota33(String formulaUsoNota33) {
					this.formulaUsoNota33 = formulaUsoNota33;
				}

				public String getFormulaCalculoNota33() {
					if (formulaCalculoNota33 == null) {
						formulaCalculoNota33 = "";
					}
					return (formulaCalculoNota33);
				}

				public void setFormulaCalculoNota33(String formulaCalculoNota33) {
					this.formulaCalculoNota33 = formulaCalculoNota33;
				}

				public String getTituloNota33() {
					if (tituloNota33 == null) {
						tituloNota33 = "";
					}
					return (tituloNota33);
				}

				public void setTituloNota33(String tituloNota33) {
					this.tituloNota33 = tituloNota33;
				}

				public Boolean getUtilizarNota33() {
					if (utilizarNota33 == null) {
						utilizarNota33 = Boolean.FALSE;
					}
					return (utilizarNota33);
				}

				public Boolean isUtilizarNota33() {
					return (utilizarNota33);
				}

				public void setUtilizarNota33(Boolean utilizarNota33) {
					this.utilizarNota33 = utilizarNota33;
				}

				public Double getNota33() {
					return (nota33);
				}

				public void setNota33(Double nota33) {
					this.nota33 = nota33;
				}

				public Boolean getNota33MediaFinal() {
					if (nota33MediaFinal == null) {
						nota33MediaFinal = false;
					}
					return nota33MediaFinal;
				}

				public void setNota33MediaFinal(Boolean nota33MediaFinal) {
					this.nota33MediaFinal = nota33MediaFinal;
				}

				public Boolean getUtilizarNota33PorConceito() {
					if (utilizarNota33PorConceito == null) {
						utilizarNota33PorConceito = false;
					}
					return utilizarNota33PorConceito;
				}

				public void setUtilizarNota33PorConceito(Boolean utilizarNota33PorConceito) {
					this.utilizarNota33PorConceito = utilizarNota33PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva33() {
					if (utilizarComoSubstitutiva33 == null) {
						utilizarComoSubstitutiva33 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva33;
				}

				public void setUtilizarComoSubstitutiva33(Boolean utilizarComoSubstitutiva33) {
					this.utilizarComoSubstitutiva33 = utilizarComoSubstitutiva33;
				}

				public Boolean getApresentarNota33() {
					if (apresentarNota33 == null) {
						apresentarNota33 = Boolean.TRUE;
					}
					return apresentarNota33;
				}

				public void setApresentarNota33(Boolean apresentarNota33) {
					this.apresentarNota33 = apresentarNota33;
				}

				public String getPoliticaSubstitutiva33() {
					if (politicaSubstitutiva33 == null) {
						politicaSubstitutiva33 = "";
					}
					return politicaSubstitutiva33;
				}

				public void setPoliticaSubstitutiva33(String politicaSubstitutiva33) {
					this.politicaSubstitutiva33 = politicaSubstitutiva33;
				}

				@XmlElement(name = "configuracaoAcademicoNota33ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota33ConceitoVOs() {
					if (configuracaoAcademicoNota33ConceitoVOs == null) {
						configuracaoAcademicoNota33ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota33ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota33ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota33ConceitoVOs) {
					this.configuracaoAcademicoNota33ConceitoVOs = configuracaoAcademicoNota33ConceitoVOs;
				}
				
				public String getTituloNotaApresentar33() {
					if (tituloNotaApresentar33 == null) {
						tituloNotaApresentar33 = "";
					}
					return tituloNotaApresentar33;
				}

				public void setTituloNotaApresentar33(String tituloNotaApresentar33) {
					this.tituloNotaApresentar33 = tituloNotaApresentar33;
				}

				public Double getFaixaNota33Menor() {
					if (faixaNota33Menor == null) {
						faixaNota33Menor = 0.0;
					}
					return faixaNota33Menor;
				}

				public void setFaixaNota33Menor(Double faixaNota33Menor) {
					this.faixaNota33Menor = faixaNota33Menor;
				}

				public Double getFaixaNota33Maior() {
					if (faixaNota33Maior == null) {
						faixaNota33Maior = 10.0;
					}
					return faixaNota33Maior;
				}

				public void setFaixaNota33Maior(Double faixaNota33Maior) {
					this.faixaNota33Maior = faixaNota33Maior;
				}

				public String getRegraArredondamentoNota33() {
					if (regraArredondamentoNota33 == null) {
						regraArredondamentoNota33 = "";
					}
					return regraArredondamentoNota33;
				}

				public void setRegraArredondamentoNota33(String regraArredondamentoNota33) {
					this.regraArredondamentoNota33 = regraArredondamentoNota33;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota33VO() {
					if (configuracaoAcademicaNota33VO == null) {
						configuracaoAcademicaNota33VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_33);
					}
					return configuracaoAcademicaNota33VO;
				}

				public void setConfiguracaoAcademicaNota33VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota33VO) {
					this.configuracaoAcademicaNota33VO = configuracaoAcademicaNota33VO;
				}

				public boolean isFiltrarNota33() {
					return filtrarNota33;
				}

				public void setFiltrarNota33(boolean filtrarNota33) {
					this.filtrarNota33 = filtrarNota33;
				}

				
				// NOTA 34 **************************************************************
				public BimestreEnum getBimestreNota34() {
					if (bimestreNota34 == null) {
						bimestreNota34 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota34;
				}

				public void setBimestreNota34(BimestreEnum bimestreNota34) {
					this.bimestreNota34 = bimestreNota34;
				}

				public String getFormulaUsoNota34() {
					if (formulaUsoNota34 == null) {
						formulaUsoNota34 = "";
					}
					return (formulaUsoNota34);
				}

				public void setFormulaUsoNota34(String formulaUsoNota34) {
					this.formulaUsoNota34 = formulaUsoNota34;
				}

				public String getFormulaCalculoNota34() {
					if (formulaCalculoNota34 == null) {
						formulaCalculoNota34 = "";
					}
					return (formulaCalculoNota34);
				}

				public void setFormulaCalculoNota34(String formulaCalculoNota34) {
					this.formulaCalculoNota34 = formulaCalculoNota34;
				}

				public String getTituloNota34() {
					if (tituloNota34 == null) {
						tituloNota34 = "";
					}
					return (tituloNota34);
				}

				public void setTituloNota34(String tituloNota34) {
					this.tituloNota34 = tituloNota34;
				}

				public Boolean getUtilizarNota34() {
					if (utilizarNota34 == null) {
						utilizarNota34 = Boolean.FALSE;
					}
					return (utilizarNota34);
				}

				public Boolean isUtilizarNota34() {
					return (utilizarNota34);
				}

				public void setUtilizarNota34(Boolean utilizarNota34) {
					this.utilizarNota34 = utilizarNota34;
				}

				public Double getNota34() {
					return (nota34);
				}

				public void setNota34(Double nota34) {
					this.nota34 = nota34;
				}

				public Boolean getNota34MediaFinal() {
					if (nota34MediaFinal == null) {
						nota34MediaFinal = false;
					}
					return nota34MediaFinal;
				}

				public void setNota34MediaFinal(Boolean nota34MediaFinal) {
					this.nota34MediaFinal = nota34MediaFinal;
				}

				public Boolean getUtilizarNota34PorConceito() {
					if (utilizarNota34PorConceito == null) {
						utilizarNota34PorConceito = false;
					}
					return utilizarNota34PorConceito;
				}

				public void setUtilizarNota34PorConceito(Boolean utilizarNota34PorConceito) {
					this.utilizarNota34PorConceito = utilizarNota34PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva34() {
					if (utilizarComoSubstitutiva34 == null) {
						utilizarComoSubstitutiva34 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva34;
				}

				public void setUtilizarComoSubstitutiva34(Boolean utilizarComoSubstitutiva34) {
					this.utilizarComoSubstitutiva34 = utilizarComoSubstitutiva34;
				}

				public Boolean getApresentarNota34() {
					if (apresentarNota34 == null) {
						apresentarNota34 = Boolean.TRUE;
					}
					return apresentarNota34;
				}

				public void setApresentarNota34(Boolean apresentarNota34) {
					this.apresentarNota34 = apresentarNota34;
				}

				public String getPoliticaSubstitutiva34() {
					if (politicaSubstitutiva34 == null) {
						politicaSubstitutiva34 = "";
					}
					return politicaSubstitutiva34;
				}

				public void setPoliticaSubstitutiva34(String politicaSubstitutiva34) {
					this.politicaSubstitutiva34 = politicaSubstitutiva34;
				}

				@XmlElement(name = "configuracaoAcademicoNota34ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota34ConceitoVOs() {
					if (configuracaoAcademicoNota34ConceitoVOs == null) {
						configuracaoAcademicoNota34ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota34ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota34ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota34ConceitoVOs) {
					this.configuracaoAcademicoNota34ConceitoVOs = configuracaoAcademicoNota34ConceitoVOs;
				}
				
				public String getTituloNotaApresentar34() {
					if (tituloNotaApresentar34 == null) {
						tituloNotaApresentar34 = "";
					}
					return tituloNotaApresentar34;
				}

				public void setTituloNotaApresentar34(String tituloNotaApresentar34) {
					this.tituloNotaApresentar34 = tituloNotaApresentar34;
				}

				public Double getFaixaNota34Menor() {
					if (faixaNota34Menor == null) {
						faixaNota34Menor = 0.0;
					}
					return faixaNota34Menor;
				}

				public void setFaixaNota34Menor(Double faixaNota34Menor) {
					this.faixaNota34Menor = faixaNota34Menor;
				}

				public Double getFaixaNota34Maior() {
					if (faixaNota34Maior == null) {
						faixaNota34Maior = 10.0;
					}
					return faixaNota34Maior;
				}

				public void setFaixaNota34Maior(Double faixaNota34Maior) {
					this.faixaNota34Maior = faixaNota34Maior;
				}

				public String getRegraArredondamentoNota34() {
					if (regraArredondamentoNota34 == null) {
						regraArredondamentoNota34 = "";
					}
					return regraArredondamentoNota34;
				}

				public void setRegraArredondamentoNota34(String regraArredondamentoNota34) {
					this.regraArredondamentoNota34 = regraArredondamentoNota34;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota34VO() {
					if (configuracaoAcademicaNota34VO == null) {
						configuracaoAcademicaNota34VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_34);
					}
					return configuracaoAcademicaNota34VO;
				}

				public void setConfiguracaoAcademicaNota34VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota34VO) {
					this.configuracaoAcademicaNota34VO = configuracaoAcademicaNota34VO;
				}

				public boolean isFiltrarNota34() {
					return filtrarNota34;
				}

				public void setFiltrarNota34(boolean filtrarNota34) {
					this.filtrarNota34 = filtrarNota34;
				}

				
				// NOTA 35 **************************************************************
				public BimestreEnum getBimestreNota35() {
					if (bimestreNota35 == null) {
						bimestreNota35 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota35;
				}

				public void setBimestreNota35(BimestreEnum bimestreNota35) {
					this.bimestreNota35 = bimestreNota35;
				}

				public String getFormulaUsoNota35() {
					if (formulaUsoNota35 == null) {
						formulaUsoNota35 = "";
					}
					return (formulaUsoNota35);
				}

				public void setFormulaUsoNota35(String formulaUsoNota35) {
					this.formulaUsoNota35 = formulaUsoNota35;
				}

				public String getFormulaCalculoNota35() {
					if (formulaCalculoNota35 == null) {
						formulaCalculoNota35 = "";
					}
					return (formulaCalculoNota35);
				}

				public void setFormulaCalculoNota35(String formulaCalculoNota35) {
					this.formulaCalculoNota35 = formulaCalculoNota35;
				}

				public String getTituloNota35() {
					if (tituloNota35 == null) {
						tituloNota35 = "";
					}
					return (tituloNota35);
				}

				public void setTituloNota35(String tituloNota35) {
					this.tituloNota35 = tituloNota35;
				}

				public Boolean getUtilizarNota35() {
					if (utilizarNota35 == null) {
						utilizarNota35 = Boolean.FALSE;
					}
					return (utilizarNota35);
				}

				public Boolean isUtilizarNota35() {
					return (utilizarNota35);
				}

				public void setUtilizarNota35(Boolean utilizarNota35) {
					this.utilizarNota35 = utilizarNota35;
				}

				public Double getNota35() {
					return (nota35);
				}

				public void setNota35(Double nota35) {
					this.nota35 = nota35;
				}

				public Boolean getNota35MediaFinal() {
					if (nota35MediaFinal == null) {
						nota35MediaFinal = false;
					}
					return nota35MediaFinal;
				}

				public void setNota35MediaFinal(Boolean nota35MediaFinal) {
					this.nota35MediaFinal = nota35MediaFinal;
				}

				public Boolean getUtilizarNota35PorConceito() {
					if (utilizarNota35PorConceito == null) {
						utilizarNota35PorConceito = false;
					}
					return utilizarNota35PorConceito;
				}

				public void setUtilizarNota35PorConceito(Boolean utilizarNota35PorConceito) {
					this.utilizarNota35PorConceito = utilizarNota35PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva35() {
					if (utilizarComoSubstitutiva35 == null) {
						utilizarComoSubstitutiva35 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva35;
				}

				public void setUtilizarComoSubstitutiva35(Boolean utilizarComoSubstitutiva35) {
					this.utilizarComoSubstitutiva35 = utilizarComoSubstitutiva35;
				}

				public Boolean getApresentarNota35() {
					if (apresentarNota35 == null) {
						apresentarNota35 = Boolean.TRUE;
					}
					return apresentarNota35;
				}

				public void setApresentarNota35(Boolean apresentarNota35) {
					this.apresentarNota35 = apresentarNota35;
				}

				public String getPoliticaSubstitutiva35() {
					if (politicaSubstitutiva35 == null) {
						politicaSubstitutiva35 = "";
					}
					return politicaSubstitutiva35;
				}

				public void setPoliticaSubstitutiva35(String politicaSubstitutiva35) {
					this.politicaSubstitutiva35 = politicaSubstitutiva35;
				}

				@XmlElement(name = "configuracaoAcademicoNota35ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota35ConceitoVOs() {
					if (configuracaoAcademicoNota35ConceitoVOs == null) {
						configuracaoAcademicoNota35ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota35ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota35ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota35ConceitoVOs) {
					this.configuracaoAcademicoNota35ConceitoVOs = configuracaoAcademicoNota35ConceitoVOs;
				}
				
				public String getTituloNotaApresentar35() {
					if (tituloNotaApresentar35 == null) {
						tituloNotaApresentar35 = "";
					}
					return tituloNotaApresentar35;
				}

				public void setTituloNotaApresentar35(String tituloNotaApresentar35) {
					this.tituloNotaApresentar35 = tituloNotaApresentar35;
				}

				public Double getFaixaNota35Menor() {
					if (faixaNota35Menor == null) {
						faixaNota35Menor = 0.0;
					}
					return faixaNota35Menor;
				}

				public void setFaixaNota35Menor(Double faixaNota35Menor) {
					this.faixaNota35Menor = faixaNota35Menor;
				}

				public Double getFaixaNota35Maior() {
					if (faixaNota35Maior == null) {
						faixaNota35Maior = 10.0;
					}
					return faixaNota35Maior;
				}

				public void setFaixaNota35Maior(Double faixaNota35Maior) {
					this.faixaNota35Maior = faixaNota35Maior;
				}

				public String getRegraArredondamentoNota35() {
					if (regraArredondamentoNota35 == null) {
						regraArredondamentoNota35 = "";
					}
					return regraArredondamentoNota35;
				}

				public void setRegraArredondamentoNota35(String regraArredondamentoNota35) {
					this.regraArredondamentoNota35 = regraArredondamentoNota35;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota35VO() {
					if (configuracaoAcademicaNota35VO == null) {
						configuracaoAcademicaNota35VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_35);
					}
					return configuracaoAcademicaNota35VO;
				}

				public void setConfiguracaoAcademicaNota35VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota35VO) {
					this.configuracaoAcademicaNota35VO = configuracaoAcademicaNota35VO;
				}

				public boolean isFiltrarNota35() {
					return filtrarNota35;
				}

				public void setFiltrarNota35(boolean filtrarNota35) {
					this.filtrarNota35 = filtrarNota35;
				}

				
				// NOTA 36 **************************************************************
				public BimestreEnum getBimestreNota36() {
					if (bimestreNota36 == null) {
						bimestreNota36 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota36;
				}

				public void setBimestreNota36(BimestreEnum bimestreNota36) {
					this.bimestreNota36 = bimestreNota36;
				}

				public String getFormulaUsoNota36() {
					if (formulaUsoNota36 == null) {
						formulaUsoNota36 = "";
					}
					return (formulaUsoNota36);
				}

				public void setFormulaUsoNota36(String formulaUsoNota36) {
					this.formulaUsoNota36 = formulaUsoNota36;
				}

				public String getFormulaCalculoNota36() {
					if (formulaCalculoNota36 == null) {
						formulaCalculoNota36 = "";
					}
					return (formulaCalculoNota36);
				}

				public void setFormulaCalculoNota36(String formulaCalculoNota36) {
					this.formulaCalculoNota36 = formulaCalculoNota36;
				}

				public String getTituloNota36() {
					if (tituloNota36 == null) {
						tituloNota36 = "";
					}
					return (tituloNota36);
				}

				public void setTituloNota36(String tituloNota36) {
					this.tituloNota36 = tituloNota36;
				}

				public Boolean getUtilizarNota36() {
					if (utilizarNota36 == null) {
						utilizarNota36 = Boolean.FALSE;
					}
					return (utilizarNota36);
				}

				public Boolean isUtilizarNota36() {
					return (utilizarNota36);
				}

				public void setUtilizarNota36(Boolean utilizarNota36) {
					this.utilizarNota36 = utilizarNota36;
				}

				public Double getNota36() {
					return (nota36);
				}

				public void setNota36(Double nota36) {
					this.nota36 = nota36;
				}

				public Boolean getNota36MediaFinal() {
					if (nota36MediaFinal == null) {
						nota36MediaFinal = false;
					}
					return nota36MediaFinal;
				}

				public void setNota36MediaFinal(Boolean nota36MediaFinal) {
					this.nota36MediaFinal = nota36MediaFinal;
				}

				public Boolean getUtilizarNota36PorConceito() {
					if (utilizarNota36PorConceito == null) {
						utilizarNota36PorConceito = false;
					}
					return utilizarNota36PorConceito;
				}

				public void setUtilizarNota36PorConceito(Boolean utilizarNota36PorConceito) {
					this.utilizarNota36PorConceito = utilizarNota36PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva36() {
					if (utilizarComoSubstitutiva36 == null) {
						utilizarComoSubstitutiva36 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva36;
				}

				public void setUtilizarComoSubstitutiva36(Boolean utilizarComoSubstitutiva36) {
					this.utilizarComoSubstitutiva36 = utilizarComoSubstitutiva36;
				}

				public Boolean getApresentarNota36() {
					if (apresentarNota36 == null) {
						apresentarNota36 = Boolean.TRUE;
					}
					return apresentarNota36;
				}

				public void setApresentarNota36(Boolean apresentarNota36) {
					this.apresentarNota36 = apresentarNota36;
				}

				public String getPoliticaSubstitutiva36() {
					if (politicaSubstitutiva36 == null) {
						politicaSubstitutiva36 = "";
					}
					return politicaSubstitutiva36;
				}

				public void setPoliticaSubstitutiva36(String politicaSubstitutiva36) {
					this.politicaSubstitutiva36 = politicaSubstitutiva36;
				}

				@XmlElement(name = "configuracaoAcademicoNota36ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota36ConceitoVOs() {
					if (configuracaoAcademicoNota36ConceitoVOs == null) {
						configuracaoAcademicoNota36ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota36ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota36ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota36ConceitoVOs) {
					this.configuracaoAcademicoNota36ConceitoVOs = configuracaoAcademicoNota36ConceitoVOs;
				}
				
				public String getTituloNotaApresentar36() {
					if (tituloNotaApresentar36 == null) {
						tituloNotaApresentar36 = "";
					}
					return tituloNotaApresentar36;
				}

				public void setTituloNotaApresentar36(String tituloNotaApresentar36) {
					this.tituloNotaApresentar36 = tituloNotaApresentar36;
				}

				public Double getFaixaNota36Menor() {
					if (faixaNota36Menor == null) {
						faixaNota36Menor = 0.0;
					}
					return faixaNota36Menor;
				}

				public void setFaixaNota36Menor(Double faixaNota36Menor) {
					this.faixaNota36Menor = faixaNota36Menor;
				}

				public Double getFaixaNota36Maior() {
					if (faixaNota36Maior == null) {
						faixaNota36Maior = 10.0;
					}
					return faixaNota36Maior;
				}

				public void setFaixaNota36Maior(Double faixaNota36Maior) {
					this.faixaNota36Maior = faixaNota36Maior;
				}

				public String getRegraArredondamentoNota36() {
					if (regraArredondamentoNota36 == null) {
						regraArredondamentoNota36 = "";
					}
					return regraArredondamentoNota36;
				}

				public void setRegraArredondamentoNota36(String regraArredondamentoNota36) {
					this.regraArredondamentoNota36 = regraArredondamentoNota36;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota36VO() {
					if (configuracaoAcademicaNota36VO == null) {
						configuracaoAcademicaNota36VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_36);
					}
					return configuracaoAcademicaNota36VO;
				}

				public void setConfiguracaoAcademicaNota36VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota36VO) {
					this.configuracaoAcademicaNota36VO = configuracaoAcademicaNota36VO;
				}

				public boolean isFiltrarNota36() {
					return filtrarNota36;
				}

				public void setFiltrarNota36(boolean filtrarNota36) {
					this.filtrarNota36 = filtrarNota36;
				}

				
				// NOTA 37 **************************************************************
				public BimestreEnum getBimestreNota37() {
					if (bimestreNota37 == null) {
						bimestreNota37 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota37;
				}

				public void setBimestreNota37(BimestreEnum bimestreNota37) {
					this.bimestreNota37 = bimestreNota37;
				}

				public String getFormulaUsoNota37() {
					if (formulaUsoNota37 == null) {
						formulaUsoNota37 = "";
					}
					return (formulaUsoNota37);
				}

				public void setFormulaUsoNota37(String formulaUsoNota37) {
					this.formulaUsoNota37 = formulaUsoNota37;
				}

				public String getFormulaCalculoNota37() {
					if (formulaCalculoNota37 == null) {
						formulaCalculoNota37 = "";
					}
					return (formulaCalculoNota37);
				}

				public void setFormulaCalculoNota37(String formulaCalculoNota37) {
					this.formulaCalculoNota37 = formulaCalculoNota37;
				}

				public String getTituloNota37() {
					if (tituloNota37 == null) {
						tituloNota37 = "";
					}
					return (tituloNota37);
				}

				public void setTituloNota37(String tituloNota37) {
					this.tituloNota37 = tituloNota37;
				}

				public Boolean getUtilizarNota37() {
					if (utilizarNota37 == null) {
						utilizarNota37 = Boolean.FALSE;
					}
					return (utilizarNota37);
				}

				public Boolean isUtilizarNota37() {
					return (utilizarNota37);
				}

				public void setUtilizarNota37(Boolean utilizarNota37) {
					this.utilizarNota37 = utilizarNota37;
				}

				public Double getNota37() {
					return (nota37);
				}

				public void setNota37(Double nota37) {
					this.nota37 = nota37;
				}

				public Boolean getNota37MediaFinal() {
					if (nota37MediaFinal == null) {
						nota37MediaFinal = false;
					}
					return nota37MediaFinal;
				}

				public void setNota37MediaFinal(Boolean nota37MediaFinal) {
					this.nota37MediaFinal = nota37MediaFinal;
				}

				public Boolean getUtilizarNota37PorConceito() {
					if (utilizarNota37PorConceito == null) {
						utilizarNota37PorConceito = false;
					}
					return utilizarNota37PorConceito;
				}

				public void setUtilizarNota37PorConceito(Boolean utilizarNota37PorConceito) {
					this.utilizarNota37PorConceito = utilizarNota37PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva37() {
					if (utilizarComoSubstitutiva37 == null) {
						utilizarComoSubstitutiva37 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva37;
				}

				public void setUtilizarComoSubstitutiva37(Boolean utilizarComoSubstitutiva37) {
					this.utilizarComoSubstitutiva37 = utilizarComoSubstitutiva37;
				}

				public Boolean getApresentarNota37() {
					if (apresentarNota37 == null) {
						apresentarNota37 = Boolean.TRUE;
					}
					return apresentarNota37;
				}

				public void setApresentarNota37(Boolean apresentarNota37) {
					this.apresentarNota37 = apresentarNota37;
				}

				public String getPoliticaSubstitutiva37() {
					if (politicaSubstitutiva37 == null) {
						politicaSubstitutiva37 = "";
					}
					return politicaSubstitutiva37;
				}

				public void setPoliticaSubstitutiva37(String politicaSubstitutiva37) {
					this.politicaSubstitutiva37 = politicaSubstitutiva37;
				}

				@XmlElement(name = "configuracaoAcademicoNota37ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota37ConceitoVOs() {
					if (configuracaoAcademicoNota37ConceitoVOs == null) {
						configuracaoAcademicoNota37ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota37ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota37ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota37ConceitoVOs) {
					this.configuracaoAcademicoNota37ConceitoVOs = configuracaoAcademicoNota37ConceitoVOs;
				}
				
				public String getTituloNotaApresentar37() {
					if (tituloNotaApresentar37 == null) {
						tituloNotaApresentar37 = "";
					}
					return tituloNotaApresentar37;
				}

				public void setTituloNotaApresentar37(String tituloNotaApresentar37) {
					this.tituloNotaApresentar37 = tituloNotaApresentar37;
				}

				public Double getFaixaNota37Menor() {
					if (faixaNota37Menor == null) {
						faixaNota37Menor = 0.0;
					}
					return faixaNota37Menor;
				}

				public void setFaixaNota37Menor(Double faixaNota37Menor) {
					this.faixaNota37Menor = faixaNota37Menor;
				}

				public Double getFaixaNota37Maior() {
					if (faixaNota37Maior == null) {
						faixaNota37Maior = 10.0;
					}
					return faixaNota37Maior;
				}

				public void setFaixaNota37Maior(Double faixaNota37Maior) {
					this.faixaNota37Maior = faixaNota37Maior;
				}

				public String getRegraArredondamentoNota37() {
					if (regraArredondamentoNota37 == null) {
						regraArredondamentoNota37 = "";
					}
					return regraArredondamentoNota37;
				}

				public void setRegraArredondamentoNota37(String regraArredondamentoNota37) {
					this.regraArredondamentoNota37 = regraArredondamentoNota37;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota37VO() {
					if (configuracaoAcademicaNota37VO == null) {
						configuracaoAcademicaNota37VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_37);
					}
					return configuracaoAcademicaNota37VO;
				}

				public void setConfiguracaoAcademicaNota37VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota37VO) {
					this.configuracaoAcademicaNota37VO = configuracaoAcademicaNota37VO;
				}

				public boolean isFiltrarNota37() {
					return filtrarNota37;
				}

				public void setFiltrarNota37(boolean filtrarNota37) {
					this.filtrarNota37 = filtrarNota37;
				}

				
				// NOTA 38 **************************************************************
				public BimestreEnum getBimestreNota38() {
					if (bimestreNota38 == null) {
						bimestreNota38 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota38;
				}

				public void setBimestreNota38(BimestreEnum bimestreNota38) {
					this.bimestreNota38 = bimestreNota38;
				}

				public String getFormulaUsoNota38() {
					if (formulaUsoNota38 == null) {
						formulaUsoNota38 = "";
					}
					return (formulaUsoNota38);
				}

				public void setFormulaUsoNota38(String formulaUsoNota38) {
					this.formulaUsoNota38 = formulaUsoNota38;
				}

				public String getFormulaCalculoNota38() {
					if (formulaCalculoNota38 == null) {
						formulaCalculoNota38 = "";
					}
					return (formulaCalculoNota38);
				}

				public void setFormulaCalculoNota38(String formulaCalculoNota38) {
					this.formulaCalculoNota38 = formulaCalculoNota38;
				}

				public String getTituloNota38() {
					if (tituloNota38 == null) {
						tituloNota38 = "";
					}
					return (tituloNota38);
				}

				public void setTituloNota38(String tituloNota38) {
					this.tituloNota38 = tituloNota38;
				}

				public Boolean getUtilizarNota38() {
					if (utilizarNota38 == null) {
						utilizarNota38 = Boolean.FALSE;
					}
					return (utilizarNota38);
				}

				public Boolean isUtilizarNota38() {
					return (utilizarNota38);
				}

				public void setUtilizarNota38(Boolean utilizarNota38) {
					this.utilizarNota38 = utilizarNota38;
				}

				public Double getNota38() {
					return (nota38);
				}

				public void setNota38(Double nota38) {
					this.nota38 = nota38;
				}

				public Boolean getNota38MediaFinal() {
					if (nota38MediaFinal == null) {
						nota38MediaFinal = false;
					}
					return nota38MediaFinal;
				}

				public void setNota38MediaFinal(Boolean nota38MediaFinal) {
					this.nota38MediaFinal = nota38MediaFinal;
				}

				public Boolean getUtilizarNota38PorConceito() {
					if (utilizarNota38PorConceito == null) {
						utilizarNota38PorConceito = false;
					}
					return utilizarNota38PorConceito;
				}

				public void setUtilizarNota38PorConceito(Boolean utilizarNota38PorConceito) {
					this.utilizarNota38PorConceito = utilizarNota38PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva38() {
					if (utilizarComoSubstitutiva38 == null) {
						utilizarComoSubstitutiva38 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva38;
				}

				public void setUtilizarComoSubstitutiva38(Boolean utilizarComoSubstitutiva38) {
					this.utilizarComoSubstitutiva38 = utilizarComoSubstitutiva38;
				}

				public Boolean getApresentarNota38() {
					if (apresentarNota38 == null) {
						apresentarNota38 = Boolean.TRUE;
					}
					return apresentarNota38;
				}

				public void setApresentarNota38(Boolean apresentarNota38) {
					this.apresentarNota38 = apresentarNota38;
				}

				public String getPoliticaSubstitutiva38() {
					if (politicaSubstitutiva38 == null) {
						politicaSubstitutiva38 = "";
					}
					return politicaSubstitutiva38;
				}

				public void setPoliticaSubstitutiva38(String politicaSubstitutiva38) {
					this.politicaSubstitutiva38 = politicaSubstitutiva38;
				}

				@XmlElement(name = "configuracaoAcademicoNota38ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota38ConceitoVOs() {
					if (configuracaoAcademicoNota38ConceitoVOs == null) {
						configuracaoAcademicoNota38ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota38ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota38ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota38ConceitoVOs) {
					this.configuracaoAcademicoNota38ConceitoVOs = configuracaoAcademicoNota38ConceitoVOs;
				}
				
				public String getTituloNotaApresentar38() {
					if (tituloNotaApresentar38 == null) {
						tituloNotaApresentar38 = "";
					}
					return tituloNotaApresentar38;
				}

				public void setTituloNotaApresentar38(String tituloNotaApresentar38) {
					this.tituloNotaApresentar38 = tituloNotaApresentar38;
				}

				public Double getFaixaNota38Menor() {
					if (faixaNota38Menor == null) {
						faixaNota38Menor = 0.0;
					}
					return faixaNota38Menor;
				}

				public void setFaixaNota38Menor(Double faixaNota38Menor) {
					this.faixaNota38Menor = faixaNota38Menor;
				}

				public Double getFaixaNota38Maior() {
					if (faixaNota38Maior == null) {
						faixaNota38Maior = 10.0;
					}
					return faixaNota38Maior;
				}

				public void setFaixaNota38Maior(Double faixaNota38Maior) {
					this.faixaNota38Maior = faixaNota38Maior;
				}

				public String getRegraArredondamentoNota38() {
					if (regraArredondamentoNota38 == null) {
						regraArredondamentoNota38 = "";
					}
					return regraArredondamentoNota38;
				}

				public void setRegraArredondamentoNota38(String regraArredondamentoNota38) {
					this.regraArredondamentoNota38 = regraArredondamentoNota38;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota38VO() {
					if (configuracaoAcademicaNota38VO == null) {
						configuracaoAcademicaNota38VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_38);
					}
					return configuracaoAcademicaNota38VO;
				}

				public void setConfiguracaoAcademicaNota38VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota38VO) {
					this.configuracaoAcademicaNota38VO = configuracaoAcademicaNota38VO;
				}

				public boolean isFiltrarNota38() {
					return filtrarNota38;
				}

				public void setFiltrarNota38(boolean filtrarNota38) {
					this.filtrarNota38 = filtrarNota38;
				}

				
				// NOTA 39 **************************************************************
				public BimestreEnum getBimestreNota39() {
					if (bimestreNota39 == null) {
						bimestreNota39 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota39;
				}

				public void setBimestreNota39(BimestreEnum bimestreNota39) {
					this.bimestreNota39 = bimestreNota39;
				}

				public String getFormulaUsoNota39() {
					if (formulaUsoNota39 == null) {
						formulaUsoNota39 = "";
					}
					return (formulaUsoNota39);
				}

				public void setFormulaUsoNota39(String formulaUsoNota39) {
					this.formulaUsoNota39 = formulaUsoNota39;
				}

				public String getFormulaCalculoNota39() {
					if (formulaCalculoNota39 == null) {
						formulaCalculoNota39 = "";
					}
					return (formulaCalculoNota39);
				}

				public void setFormulaCalculoNota39(String formulaCalculoNota39) {
					this.formulaCalculoNota39 = formulaCalculoNota39;
				}

				public String getTituloNota39() {
					if (tituloNota39 == null) {
						tituloNota39 = "";
					}
					return (tituloNota39);
				}

				public void setTituloNota39(String tituloNota39) {
					this.tituloNota39 = tituloNota39;
				}

				public Boolean getUtilizarNota39() {
					if (utilizarNota39 == null) {
						utilizarNota39 = Boolean.FALSE;
					}
					return (utilizarNota39);
				}

				public Boolean isUtilizarNota39() {
					return (utilizarNota39);
				}

				public void setUtilizarNota39(Boolean utilizarNota39) {
					this.utilizarNota39 = utilizarNota39;
				}

				public Double getNota39() {
					return (nota39);
				}

				public void setNota39(Double nota39) {
					this.nota39 = nota39;
				}

				public Boolean getNota39MediaFinal() {
					if (nota39MediaFinal == null) {
						nota39MediaFinal = false;
					}
					return nota39MediaFinal;
				}

				public void setNota39MediaFinal(Boolean nota39MediaFinal) {
					this.nota39MediaFinal = nota39MediaFinal;
				}

				public Boolean getUtilizarNota39PorConceito() {
					if (utilizarNota39PorConceito == null) {
						utilizarNota39PorConceito = false;
					}
					return utilizarNota39PorConceito;
				}

				public void setUtilizarNota39PorConceito(Boolean utilizarNota39PorConceito) {
					this.utilizarNota39PorConceito = utilizarNota39PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva39() {
					if (utilizarComoSubstitutiva39 == null) {
						utilizarComoSubstitutiva39 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva39;
				}

				public void setUtilizarComoSubstitutiva39(Boolean utilizarComoSubstitutiva39) {
					this.utilizarComoSubstitutiva39 = utilizarComoSubstitutiva39;
				}

				public Boolean getApresentarNota39() {
					if (apresentarNota39 == null) {
						apresentarNota39 = Boolean.TRUE;
					}
					return apresentarNota39;
				}

				public void setApresentarNota39(Boolean apresentarNota39) {
					this.apresentarNota39 = apresentarNota39;
				}

				public String getPoliticaSubstitutiva39() {
					if (politicaSubstitutiva39 == null) {
						politicaSubstitutiva39 = "";
					}
					return politicaSubstitutiva39;
				}

				public void setPoliticaSubstitutiva39(String politicaSubstitutiva39) {
					this.politicaSubstitutiva39 = politicaSubstitutiva39;
				}

				@XmlElement(name = "configuracaoAcademicoNota39ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota39ConceitoVOs() {
					if (configuracaoAcademicoNota39ConceitoVOs == null) {
						configuracaoAcademicoNota39ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota39ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota39ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota39ConceitoVOs) {
					this.configuracaoAcademicoNota39ConceitoVOs = configuracaoAcademicoNota39ConceitoVOs;
				}
				
				public String getTituloNotaApresentar39() {
					if (tituloNotaApresentar39 == null) {
						tituloNotaApresentar39 = "";
					}
					return tituloNotaApresentar39;
				}

				public void setTituloNotaApresentar39(String tituloNotaApresentar39) {
					this.tituloNotaApresentar39 = tituloNotaApresentar39;
				}

				public Double getFaixaNota39Menor() {
					if (faixaNota39Menor == null) {
						faixaNota39Menor = 0.0;
					}
					return faixaNota39Menor;
				}

				public void setFaixaNota39Menor(Double faixaNota39Menor) {
					this.faixaNota39Menor = faixaNota39Menor;
				}

				public Double getFaixaNota39Maior() {
					if (faixaNota39Maior == null) {
						faixaNota39Maior = 10.0;
					}
					return faixaNota39Maior;
				}

				public void setFaixaNota39Maior(Double faixaNota39Maior) {
					this.faixaNota39Maior = faixaNota39Maior;
				}

				public String getRegraArredondamentoNota39() {
					if (regraArredondamentoNota39 == null) {
						regraArredondamentoNota39 = "";
					}
					return regraArredondamentoNota39;
				}

				public void setRegraArredondamentoNota39(String regraArredondamentoNota39) {
					this.regraArredondamentoNota39 = regraArredondamentoNota39;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota39VO() {
					if (configuracaoAcademicaNota39VO == null) {
						configuracaoAcademicaNota39VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_39);
					}
					return configuracaoAcademicaNota39VO;
				}

				public void setConfiguracaoAcademicaNota39VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota39VO) {
					this.configuracaoAcademicaNota39VO = configuracaoAcademicaNota39VO;
				}

				public boolean isFiltrarNota39() {
					return filtrarNota39;
				}

				public void setFiltrarNota39(boolean filtrarNota39) {
					this.filtrarNota39 = filtrarNota39;
				}

				
				// NOTA 40 **************************************************************
				public BimestreEnum getBimestreNota40() {
					if (bimestreNota40 == null) {
						bimestreNota40 = BimestreEnum.NAO_CONTROLA;
					}
					return bimestreNota40;
				}

				public void setBimestreNota40(BimestreEnum bimestreNota40) {
					this.bimestreNota40 = bimestreNota40;
				}

				public String getFormulaUsoNota40() {
					if (formulaUsoNota40 == null) {
						formulaUsoNota40 = "";
					}
					return (formulaUsoNota40);
				}

				public void setFormulaUsoNota40(String formulaUsoNota40) {
					this.formulaUsoNota40 = formulaUsoNota40;
				}

				public String getFormulaCalculoNota40() {
					if (formulaCalculoNota40 == null) {
						formulaCalculoNota40 = "";
					}
					return (formulaCalculoNota40);
				}

				public void setFormulaCalculoNota40(String formulaCalculoNota40) {
					this.formulaCalculoNota40 = formulaCalculoNota40;
				}

				public String getTituloNota40() {
					if (tituloNota40 == null) {
						tituloNota40 = "";
					}
					return (tituloNota40);
				}

				public void setTituloNota40(String tituloNota40) {
					this.tituloNota40 = tituloNota40;
				}

				public Boolean getUtilizarNota40() {
					if (utilizarNota40 == null) {
						utilizarNota40 = Boolean.FALSE;
					}
					return (utilizarNota40);
				}

				public Boolean isUtilizarNota40() {
					return (utilizarNota40);
				}

				public void setUtilizarNota40(Boolean utilizarNota40) {
					this.utilizarNota40 = utilizarNota40;
				}

				public Double getNota40() {
					return (nota40);
				}

				public void setNota40(Double nota40) {
					this.nota40 = nota40;
				}

				public Boolean getNota40MediaFinal() {
					if (nota40MediaFinal == null) {
						nota40MediaFinal = false;
					}
					return nota40MediaFinal;
				}

				public void setNota40MediaFinal(Boolean nota40MediaFinal) {
					this.nota40MediaFinal = nota40MediaFinal;
				}

				public Boolean getUtilizarNota40PorConceito() {
					if (utilizarNota40PorConceito == null) {
						utilizarNota40PorConceito = false;
					}
					return utilizarNota40PorConceito;
				}

				public void setUtilizarNota40PorConceito(Boolean utilizarNota40PorConceito) {
					this.utilizarNota40PorConceito = utilizarNota40PorConceito;
				}

				public Boolean getUtilizarComoSubstitutiva40() {
					if (utilizarComoSubstitutiva40 == null) {
						utilizarComoSubstitutiva40 = Boolean.FALSE;
					}
					return utilizarComoSubstitutiva40;
				}

				public void setUtilizarComoSubstitutiva40(Boolean utilizarComoSubstitutiva40) {
					this.utilizarComoSubstitutiva40 = utilizarComoSubstitutiva40;
				}

				public Boolean getApresentarNota40() {
					if (apresentarNota40 == null) {
						apresentarNota40 = Boolean.TRUE;
					}
					return apresentarNota40;
				}

				public void setApresentarNota40(Boolean apresentarNota40) {
					this.apresentarNota40 = apresentarNota40;
				}

				public String getPoliticaSubstitutiva40() {
					if (politicaSubstitutiva40 == null) {
						politicaSubstitutiva40 = "";
					}
					return politicaSubstitutiva40;
				}

				public void setPoliticaSubstitutiva40(String politicaSubstitutiva40) {
					this.politicaSubstitutiva40 = politicaSubstitutiva40;
				}

				@XmlElement(name = "configuracaoAcademicoNota40ConceitoVOs")
				public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNota40ConceitoVOs() {
					if (configuracaoAcademicoNota40ConceitoVOs == null) {
						configuracaoAcademicoNota40ConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
					}
					return configuracaoAcademicoNota40ConceitoVOs;
				}

				public void setConfiguracaoAcademicoNota40ConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNota40ConceitoVOs) {
					this.configuracaoAcademicoNota40ConceitoVOs = configuracaoAcademicoNota40ConceitoVOs;
				}
				
				public String getTituloNotaApresentar40() {
					if (tituloNotaApresentar40 == null) {
						tituloNotaApresentar40 = "";
					}
					return tituloNotaApresentar40;
				}

				public void setTituloNotaApresentar40(String tituloNotaApresentar40) {
					this.tituloNotaApresentar40 = tituloNotaApresentar40;
				}

				public Double getFaixaNota40Menor() {
					if (faixaNota40Menor == null) {
						faixaNota40Menor = 0.0;
					}
					return faixaNota40Menor;
				}

				public void setFaixaNota40Menor(Double faixaNota40Menor) {
					this.faixaNota40Menor = faixaNota40Menor;
				}

				public Double getFaixaNota40Maior() {
					if (faixaNota40Maior == null) {
						faixaNota40Maior = 10.0;
					}
					return faixaNota40Maior;
				}

				public void setFaixaNota40Maior(Double faixaNota40Maior) {
					this.faixaNota40Maior = faixaNota40Maior;
				}

				public String getRegraArredondamentoNota40() {
					if (regraArredondamentoNota40 == null) {
						regraArredondamentoNota40 = "";
					}
					return regraArredondamentoNota40;
				}

				public void setRegraArredondamentoNota40(String regraArredondamentoNota40) {
					this.regraArredondamentoNota40 = regraArredondamentoNota40;
				}

				public ConfiguracaoAcademicaNotaVO getConfiguracaoAcademicaNota40VO() {
					if (configuracaoAcademicaNota40VO == null) {
						configuracaoAcademicaNota40VO = new ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum.NOTA_40);
					}
					return configuracaoAcademicaNota40VO;
				}

				public void setConfiguracaoAcademicaNota40VO(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota40VO) {
					this.configuracaoAcademicaNota40VO = configuracaoAcademicaNota40VO;
				}

				public boolean isFiltrarNota40() {
					return filtrarNota40;
				}

				public void setFiltrarNota40(boolean filtrarNota40) {
					this.filtrarNota40 = filtrarNota40;
				}

				public Boolean getNota31TipoLancamento() {
					if (!getUtilizarNota31()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva31()) || (getFormulaCalculoNota31().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota32TipoLancamento() {
					if (!getUtilizarNota32()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva32()) || (getFormulaCalculoNota32().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota33TipoLancamento() {
					if (!getUtilizarNota33()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva33()) || (getFormulaCalculoNota33().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota34TipoLancamento() {
					if (!getUtilizarNota34()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva34()) || (getFormulaCalculoNota34().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota35TipoLancamento() {
					if (!getUtilizarNota35()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva35()) || (getFormulaCalculoNota35().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota36TipoLancamento() {
					if (!getUtilizarNota36()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva36()) || (getFormulaCalculoNota36().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota37TipoLancamento() {
					if (!getUtilizarNota37()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva37()) || (getFormulaCalculoNota37().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota38TipoLancamento() {
					if (!getUtilizarNota38()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva38()) || (getFormulaCalculoNota38().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota39TipoLancamento() {
					if (!getUtilizarNota39()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva39()) || (getFormulaCalculoNota39().trim().isEmpty())) {
						return true;
					}
					return false;
				}

				public Boolean getNota40TipoLancamento() {
					if (!getUtilizarNota40()) {
						return false;
					}
					if ((getUtilizarComoSubstitutiva40()) || (getFormulaCalculoNota40().trim().isEmpty())) {
						return true;
					}
					return false;
				}


	public ConfiguracaoAcademicoNotaConceitoVO obterConceitoAPartirMediaFinal(Double mediaFinal) throws Exception {
		int indice = obterIndiceNotaReferenteMediaFinal();
		
		ConfiguracaoAcademicoNotaConceitoVO mediaFinalConceito = null;
		if (mediaFinal != null && (Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indice + "PorConceito")) {
			List<ConfiguracaoAcademicoNotaConceitoVO> listaConceitoUtilizar = (List<ConfiguracaoAcademicoNotaConceitoVO>) UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicoNota" + indice + "ConceitoVOs");
			for (ConfiguracaoAcademicoNotaConceitoVO obj : listaConceitoUtilizar) {
				if (mediaFinal >= obj.getFaixaNota1() && mediaFinal <= obj.getFaixaNota2()) {
					//UtilReflexao.invocarMetodo(historicoVO, "setNota" + indice + "Conceito", obj.clone());
					mediaFinalConceito = obj.clone();
					break;
				}
			}
			return mediaFinalConceito;
		}
		return null;
	}

	public Boolean getUtilizaConceitoMediaFinal() {
		int indiceMediaFinal = obterIndiceNotaReferenteMediaFinal();
		if (indiceMediaFinal > 0) {
			Boolean notaMediaFinalUtilizaConceito = (Boolean)UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indiceMediaFinal + "PorConceito");
			return notaMediaFinalUtilizaConceito;
		}
		return Boolean.FALSE;
	}

	public int obterIndiceNotaReferenteMediaFinal() {
		int indiceVerificar = 1;
		while (indiceVerificar <= 40) {
			try {
				if ((Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indiceVerificar) 
						&& (Boolean)UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indiceVerificar + "PorConceito")
						&& (Boolean)UtilReflexao.invocarMetodoGet(this, "nota" + indiceVerificar + "MediaFinal")) {										
						return indiceVerificar;					
				}
			} catch (Exception e) {
			}
			indiceVerificar = indiceVerificar + 1; 
		}
		return 0;
	}


	public HashMap<String, ConfiguracaoAcademicaNotaVO> getMapaConfigNotas() {
		if(mapaConfigNotas == null){
			mapaConfigNotas = new HashMap<String, ConfiguracaoAcademicaNotaVO>();
		}
		return mapaConfigNotas;
	}


	public void setMapaConfigNotas(HashMap<String, ConfiguracaoAcademicaNotaVO> mapaConfigNotas) {
		this.mapaConfigNotas = mapaConfigNotas;
	}
	
 
	public List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicaMediaFinalConceito;

	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicaMediaFinalConceito() {
		if (configuracaoAcademicaMediaFinalConceito == null) {
			configuracaoAcademicaMediaFinalConceito = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
			int indiceVerificar = 1;
			while (indiceVerificar <= 40) {
				try {
					if ((Boolean) UtilReflexao.invocarMetodoGet(this, "utilizarNota" + indiceVerificar)
							&& (Boolean) UtilReflexao.invocarMetodoGet(this,
									"utilizarNota" + indiceVerificar + "PorConceito")
							&& (Boolean) UtilReflexao.invocarMetodoGet(this, "nota" + indiceVerificar + "MediaFinal")) {
						configuracaoAcademicaMediaFinalConceito = (List<ConfiguracaoAcademicoNotaConceitoVO>) UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicoNota" + indiceVerificar + "ConceitoVOs");
					}
				} catch (Exception e) {
				}
				indiceVerificar = indiceVerificar + 1;
			}
		}
		return configuracaoAcademicaMediaFinalConceito;
	}
	
	
	public List<SelectItem> configuracaoAcademicaMediaFinalConceitoSelectItem;

	public List<SelectItem> getConfiguracaoAcademicaMediaFinalConceitoSelectItem() {
		if(configuracaoAcademicaMediaFinalConceitoSelectItem == null){
			configuracaoAcademicaMediaFinalConceitoSelectItem = new ArrayList<SelectItem>(0);
			configuracaoAcademicaMediaFinalConceitoSelectItem.add(new SelectItem(0, ""));
			for(ConfiguracaoAcademicoNotaConceitoVO conceitoVO: getConfiguracaoAcademicaMediaFinalConceito()){
				configuracaoAcademicaMediaFinalConceitoSelectItem.add(new SelectItem(conceitoVO.getCodigo(), conceitoVO.getConceitoNota()));	
			}
		}
		return configuracaoAcademicaMediaFinalConceitoSelectItem;
	}


	public void setConfiguracaoAcademicaMediaFinalConceitoSelectItem(
			List<SelectItem> configuracaoAcademicaMediaFinalConceitoSelectItem) {
		this.configuracaoAcademicaMediaFinalConceitoSelectItem = configuracaoAcademicaMediaFinalConceitoSelectItem;
	}


	public Boolean getConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores() {
		if (considerarDisciplinasReprovadasPeriodosLetivosAnteriores == null) {
			considerarDisciplinasReprovadasPeriodosLetivosAnteriores = false;
		}
		return considerarDisciplinasReprovadasPeriodosLetivosAnteriores;
	}


	public void setConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores(Boolean considerarDisciplinasReprovadasPeriodosLetivosAnteriores) {
		this.considerarDisciplinasReprovadasPeriodosLetivosAnteriores = considerarDisciplinasReprovadasPeriodosLetivosAnteriores;
	}


	public Boolean getCriarDigitoMascaraMatricula() {
		if (criarDigitoMascaraMatricula == null) {
			criarDigitoMascaraMatricula = Boolean.FALSE;
		}
		return criarDigitoMascaraMatricula;
	}


	public void setCriarDigitoMascaraMatricula(Boolean criarDigitoMascaraMatricula) {
		this.criarDigitoMascaraMatricula = criarDigitoMascaraMatricula;
	}


	public String getFormulaCriarDigitoMascaraMatricula() {
		if (formulaCriarDigitoMascaraMatricula == null) {
			formulaCriarDigitoMascaraMatricula = "";
		}
		return formulaCriarDigitoMascaraMatricula;
	}


	public void setFormulaCriarDigitoMascaraMatricula(String formulaCriarDigitoMascaraMatricula) {
		this.formulaCriarDigitoMascaraMatricula = formulaCriarDigitoMascaraMatricula;
	}
	


	public Boolean getOcultarFrequenciaDisciplinaComposta() {
		if(ocultarFrequenciaDisciplinaComposta == null){
			ocultarFrequenciaDisciplinaComposta = false;
		}
		return ocultarFrequenciaDisciplinaComposta;
	}


	public void setOcultarFrequenciaDisciplinaComposta(Boolean ocultarFrequenciaDisciplinaComposta) {
		this.ocultarFrequenciaDisciplinaComposta = ocultarFrequenciaDisciplinaComposta;
	}


	public Boolean getReprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha() {
		if(reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha == null){
			reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha = false;
		}
		return reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha;
	}


	public void setReprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha(
			Boolean reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha) {
		this.reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha = reprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha;
	}
	
	public boolean getBloquearNotaComposta(Integer nrNota){
		if(getDisciplinaComposta() && getQtdeDisciplinaFilhaComposicao() > 0){
			return (getDisciplinaCompostaConfiAcadDiferente() && nrNota <= getQtdeDisciplinaFilhaComposicao()) 
					|| !getDisciplinaCompostaConfiAcadDiferente()
					|| (getNumeroNotaRecuperacao() > 0 && getNumeroNotaRecuperacao() == nrNota);
		}
		return false;
	}
	
	public boolean bloquearNotaComposta(Integer nrNota){
		return getBloquearNotaComposta(nrNota);
	}
	
	
	private Boolean disciplinaComposta;
	private Boolean disciplinaCompostaConfiAcadDiferente;
	private Integer qtdeDisciplinaFilhaComposicao;
	private int numeroNotaRecuperacao;
	
	public int getNumeroNotaRecuperacao() {
		return numeroNotaRecuperacao;
	}


	public void setNumeroNotaRecuperacao(int numeroNotaRecuperacao) {
		this.numeroNotaRecuperacao = numeroNotaRecuperacao;
	}


	public Boolean getDisciplinaComposta() {
		if(disciplinaComposta == null){
			disciplinaComposta = false;
		}
		return disciplinaComposta;
	}

	public void setDisciplinaComposta(Boolean disciplinaComposta) {
		this.disciplinaComposta = disciplinaComposta;
	}

	public Integer getQtdeDisciplinaFilhaComposicao() {
		if(qtdeDisciplinaFilhaComposicao == null){
			qtdeDisciplinaFilhaComposicao = 0;
		}
		return qtdeDisciplinaFilhaComposicao;
	}

	public void setQtdeDisciplinaFilhaComposicao(Integer qtdeDisciplinaFilhaComposicao) {
		this.qtdeDisciplinaFilhaComposicao = qtdeDisciplinaFilhaComposicao;
	}

	public Boolean getDisciplinaCompostaConfiAcadDiferente() {
		if(disciplinaCompostaConfiAcadDiferente == null){
			disciplinaCompostaConfiAcadDiferente = false;
		}
		return disciplinaCompostaConfiAcadDiferente;
	}

	public void setDisciplinaCompostaConfiAcadDiferente(Boolean disciplinaCompostaConfiAcadDiferente) {
		this.disciplinaCompostaConfiAcadDiferente = disciplinaCompostaConfiAcadDiferente;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ConfiguracaoAcademicoVO clone()  throws CloneNotSupportedException {
		ConfiguracaoAcademicoVO clone = (ConfiguracaoAcademicoVO)super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);		
		clone.setConfiguracaoAcademicaNota1VO(this.getConfiguracaoAcademicaNota1VO().clone());
		clone.setConfiguracaoAcademicaNota2VO(this.getConfiguracaoAcademicaNota2VO().clone());
		clone.setConfiguracaoAcademicaNota3VO(this.getConfiguracaoAcademicaNota3VO().clone());
		clone.setConfiguracaoAcademicaNota4VO(this.getConfiguracaoAcademicaNota4VO().clone());
		clone.setConfiguracaoAcademicaNota5VO(this.getConfiguracaoAcademicaNota5VO().clone());
		clone.setConfiguracaoAcademicaNota6VO(this.getConfiguracaoAcademicaNota6VO().clone());
		clone.setConfiguracaoAcademicaNota7VO(this.getConfiguracaoAcademicaNota7VO().clone());
		clone.setConfiguracaoAcademicaNota8VO(this.getConfiguracaoAcademicaNota8VO().clone());
		clone.setConfiguracaoAcademicaNota9VO(this.getConfiguracaoAcademicaNota9VO().clone());
		clone.setConfiguracaoAcademicaNota10VO(this.getConfiguracaoAcademicaNota10VO().clone());
		clone.setConfiguracaoAcademicaNota11VO(this.getConfiguracaoAcademicaNota11VO().clone());
		clone.setConfiguracaoAcademicaNota12VO(this.getConfiguracaoAcademicaNota12VO().clone());
		clone.setConfiguracaoAcademicaNota13VO(this.getConfiguracaoAcademicaNota13VO().clone());
		clone.setConfiguracaoAcademicaNota14VO(this.getConfiguracaoAcademicaNota14VO().clone());
		clone.setConfiguracaoAcademicaNota15VO(this.getConfiguracaoAcademicaNota15VO().clone());
		clone.setConfiguracaoAcademicaNota16VO(this.getConfiguracaoAcademicaNota16VO().clone());
		clone.setConfiguracaoAcademicaNota17VO(this.getConfiguracaoAcademicaNota17VO().clone());
		clone.setConfiguracaoAcademicaNota18VO(this.getConfiguracaoAcademicaNota18VO().clone());
		clone.setConfiguracaoAcademicaNota19VO(this.getConfiguracaoAcademicaNota19VO().clone());
		clone.setConfiguracaoAcademicaNota20VO(this.getConfiguracaoAcademicaNota20VO().clone());
		clone.setConfiguracaoAcademicaNota21VO(this.getConfiguracaoAcademicaNota21VO().clone());
		clone.setConfiguracaoAcademicaNota22VO(this.getConfiguracaoAcademicaNota22VO().clone());
		clone.setConfiguracaoAcademicaNota23VO(this.getConfiguracaoAcademicaNota23VO().clone());
		clone.setConfiguracaoAcademicaNota24VO(this.getConfiguracaoAcademicaNota24VO().clone());
		clone.setConfiguracaoAcademicaNota25VO(this.getConfiguracaoAcademicaNota25VO().clone());
		clone.setConfiguracaoAcademicaNota26VO(this.getConfiguracaoAcademicaNota26VO().clone());
		clone.setConfiguracaoAcademicaNota27VO(this.getConfiguracaoAcademicaNota27VO().clone());
		clone.setConfiguracaoAcademicaNota28VO(this.getConfiguracaoAcademicaNota28VO().clone());
		clone.setConfiguracaoAcademicaNota29VO(this.getConfiguracaoAcademicaNota29VO().clone());
		clone.setConfiguracaoAcademicaNota30VO(this.getConfiguracaoAcademicaNota30VO().clone());
		clone.setConfiguracaoAcademicaNota31VO(this.getConfiguracaoAcademicaNota31VO().clone());
		clone.setConfiguracaoAcademicaNota32VO(this.getConfiguracaoAcademicaNota32VO().clone());
		clone.setConfiguracaoAcademicaNota33VO(this.getConfiguracaoAcademicaNota33VO().clone());
		clone.setConfiguracaoAcademicaNota34VO(this.getConfiguracaoAcademicaNota34VO().clone());
		clone.setConfiguracaoAcademicaNota35VO(this.getConfiguracaoAcademicaNota35VO().clone());
		clone.setConfiguracaoAcademicaNota36VO(this.getConfiguracaoAcademicaNota36VO().clone());
		clone.setConfiguracaoAcademicaNota37VO(this.getConfiguracaoAcademicaNota37VO().clone());
		clone.setConfiguracaoAcademicaNota38VO(this.getConfiguracaoAcademicaNota38VO().clone());
		clone.setConfiguracaoAcademicaNota39VO(this.getConfiguracaoAcademicaNota39VO().clone());
		clone.setConfiguracaoAcademicaNota40VO(this.getConfiguracaoAcademicaNota40VO().clone());
		clone.setConfiguracaoAcademicoNota1ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota2ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota3ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota4ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota5ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota6ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota7ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota8ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota9ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota10ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota11ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota12ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota13ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota14ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota15ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota16ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota17ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota18ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota19ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota20ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota21ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota22ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota23ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota24ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota25ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota26ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota27ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota28ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota29ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota30ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota31ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota32ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota33ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota34ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota35ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota36ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota37ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota38ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota39ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setConfiguracaoAcademicoNota40ConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		for(int x = 1; x <= 40; x++){
			List<ConfiguracaoAcademicoNotaConceitoVO> listaNotaConceito = ((List<ConfiguracaoAcademicoNotaConceitoVO>)UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicoNota"+x+"ConceitoVOs"));
			if( listaNotaConceito != null){				
				for(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO: listaNotaConceito){
					ConfiguracaoAcademicoNotaConceitoVO cloneConceito = configuracaoAcademicoNotaConceitoVO.clone();
					cloneConceito.setConfiguracaoAcademico(0);
					cloneConceito.setCodigo(0);
					cloneConceito.setNovoObj(true);					
					((List<ConfiguracaoAcademicoNotaConceitoVO>)UtilReflexao.invocarMetodoGet(clone, "configuracaoAcademicoNota"+x+"ConceitoVOs")).add(cloneConceito);
				}
				
			}
			
		}
		return clone;
	}
	
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo1;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo2;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo3;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo4;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo5;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo6;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo7;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo8;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo9;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo10;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo11;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo12;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo13;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo14;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo15;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo16;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo17;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo18;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo19;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo20;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo21;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo22;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo23;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo24;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo25;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo26;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo27;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo28;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo29;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo30;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo31;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo32;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo33;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo34;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo35;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo36;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo37;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo38;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo39;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo40;	
	
	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo1() {
		if(turmaDisciplinaNotaTitulo1 == null){
			turmaDisciplinaNotaTitulo1 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo1;
	}


	public void setTurmaDisciplinaNotaTitulo1(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo1) {
		this.turmaDisciplinaNotaTitulo1 = turmaDisciplinaNotaTitulo1;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo2() {
		if(turmaDisciplinaNotaTitulo2 == null){
			turmaDisciplinaNotaTitulo2 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo2;
	}


	public void setTurmaDisciplinaNotaTitulo2(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo2) {
		this.turmaDisciplinaNotaTitulo2 = turmaDisciplinaNotaTitulo2;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo3() {
		if(turmaDisciplinaNotaTitulo3 == null){
			turmaDisciplinaNotaTitulo3 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo3;
	}


	public void setTurmaDisciplinaNotaTitulo3(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo3) {
		this.turmaDisciplinaNotaTitulo3 = turmaDisciplinaNotaTitulo3;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo4() {
		if(turmaDisciplinaNotaTitulo4 == null){
			turmaDisciplinaNotaTitulo4 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo4;
	}


	public void setTurmaDisciplinaNotaTitulo4(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo4) {
		this.turmaDisciplinaNotaTitulo4 = turmaDisciplinaNotaTitulo4;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo5() {
		if(turmaDisciplinaNotaTitulo5 == null){
			turmaDisciplinaNotaTitulo5 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo5;
	}


	public void setTurmaDisciplinaNotaTitulo5(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo5) {
		this.turmaDisciplinaNotaTitulo5 = turmaDisciplinaNotaTitulo5;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo6() {
		if(turmaDisciplinaNotaTitulo6 == null){
			turmaDisciplinaNotaTitulo6 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo6;
	}


	public void setTurmaDisciplinaNotaTitulo6(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo6) {
		this.turmaDisciplinaNotaTitulo6 = turmaDisciplinaNotaTitulo6;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo7() {
		if(turmaDisciplinaNotaTitulo7 == null){
			turmaDisciplinaNotaTitulo7 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo7;
	}


	public void setTurmaDisciplinaNotaTitulo7(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo7) {
		this.turmaDisciplinaNotaTitulo7 = turmaDisciplinaNotaTitulo7;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo8() {
		if(turmaDisciplinaNotaTitulo8 == null){
			turmaDisciplinaNotaTitulo8 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo8;
	}


	public void setTurmaDisciplinaNotaTitulo8(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo8) {
		this.turmaDisciplinaNotaTitulo8 = turmaDisciplinaNotaTitulo8;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo9() {
		if(turmaDisciplinaNotaTitulo9 == null){
			turmaDisciplinaNotaTitulo9 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo9;
	}


	public void setTurmaDisciplinaNotaTitulo9(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo9) {
		this.turmaDisciplinaNotaTitulo9 = turmaDisciplinaNotaTitulo9;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo10() {
		if(turmaDisciplinaNotaTitulo10 == null){
			turmaDisciplinaNotaTitulo10 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo10;
	}


	public void setTurmaDisciplinaNotaTitulo10(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo10) {
		this.turmaDisciplinaNotaTitulo10 = turmaDisciplinaNotaTitulo10;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo11() {
		if(turmaDisciplinaNotaTitulo11 == null){
			turmaDisciplinaNotaTitulo11 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo11;
	}


	public void setTurmaDisciplinaNotaTitulo11(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo11) {
		this.turmaDisciplinaNotaTitulo11 = turmaDisciplinaNotaTitulo11;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo12() {
		if(turmaDisciplinaNotaTitulo12 == null){
			turmaDisciplinaNotaTitulo12 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo12;
	}


	public void setTurmaDisciplinaNotaTitulo12(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo12) {
		this.turmaDisciplinaNotaTitulo12 = turmaDisciplinaNotaTitulo12;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo13() {
		if(turmaDisciplinaNotaTitulo13 == null){
			turmaDisciplinaNotaTitulo13 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo13;
	}


	public void setTurmaDisciplinaNotaTitulo13(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo13) {
		this.turmaDisciplinaNotaTitulo13 = turmaDisciplinaNotaTitulo13;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo14() {
		if(turmaDisciplinaNotaTitulo14 == null){
			turmaDisciplinaNotaTitulo14 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo14;
	}


	public void setTurmaDisciplinaNotaTitulo14(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo14) {
		this.turmaDisciplinaNotaTitulo14 = turmaDisciplinaNotaTitulo14;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo15() {
		if(turmaDisciplinaNotaTitulo15 == null){
			turmaDisciplinaNotaTitulo15 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo15;
	}


	public void setTurmaDisciplinaNotaTitulo15(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo15) {
		this.turmaDisciplinaNotaTitulo15 = turmaDisciplinaNotaTitulo15;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo16() {
		if(turmaDisciplinaNotaTitulo16 == null){
			turmaDisciplinaNotaTitulo16 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo16;
	}


	public void setTurmaDisciplinaNotaTitulo16(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo16) {
		this.turmaDisciplinaNotaTitulo16 = turmaDisciplinaNotaTitulo16;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo17() {
		if(turmaDisciplinaNotaTitulo17 == null){
			turmaDisciplinaNotaTitulo17 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo17;
	}


	public void setTurmaDisciplinaNotaTitulo17(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo17) {
		this.turmaDisciplinaNotaTitulo17 = turmaDisciplinaNotaTitulo17;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo18() {
		if(turmaDisciplinaNotaTitulo18 == null){
			turmaDisciplinaNotaTitulo18 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo18;
	}


	public void setTurmaDisciplinaNotaTitulo18(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo18) {
		this.turmaDisciplinaNotaTitulo18 = turmaDisciplinaNotaTitulo18;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo19() {
		if(turmaDisciplinaNotaTitulo19 == null){
			turmaDisciplinaNotaTitulo19 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo19;
	}


	public void setTurmaDisciplinaNotaTitulo19(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo19) {
		this.turmaDisciplinaNotaTitulo19 = turmaDisciplinaNotaTitulo19;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo20() {
		if(turmaDisciplinaNotaTitulo20 == null){
			turmaDisciplinaNotaTitulo20 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo20;
	}


	public void setTurmaDisciplinaNotaTitulo20(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo20) {
		this.turmaDisciplinaNotaTitulo20 = turmaDisciplinaNotaTitulo20;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo21() {
		if(turmaDisciplinaNotaTitulo21 == null){
			turmaDisciplinaNotaTitulo21 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo21;
	}


	public void setTurmaDisciplinaNotaTitulo21(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo21) {
		this.turmaDisciplinaNotaTitulo21 = turmaDisciplinaNotaTitulo21;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo22() {
		if(turmaDisciplinaNotaTitulo22 == null){
			turmaDisciplinaNotaTitulo22 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo22;
	}


	public void setTurmaDisciplinaNotaTitulo22(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo22) {
		this.turmaDisciplinaNotaTitulo22 = turmaDisciplinaNotaTitulo22;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo23() {
		if(turmaDisciplinaNotaTitulo23 == null){
			turmaDisciplinaNotaTitulo23 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo23;
	}


	public void setTurmaDisciplinaNotaTitulo23(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo23) {
		this.turmaDisciplinaNotaTitulo23 = turmaDisciplinaNotaTitulo23;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo24() {
		if(turmaDisciplinaNotaTitulo24 == null){
			turmaDisciplinaNotaTitulo24 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo24;
	}


	public void setTurmaDisciplinaNotaTitulo24(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo24) {
		this.turmaDisciplinaNotaTitulo24 = turmaDisciplinaNotaTitulo24;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo25() {
		if(turmaDisciplinaNotaTitulo25 == null){
			turmaDisciplinaNotaTitulo25 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo25;
	}


	public void setTurmaDisciplinaNotaTitulo25(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo25) {
		this.turmaDisciplinaNotaTitulo25 = turmaDisciplinaNotaTitulo25;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo26() {
		if(turmaDisciplinaNotaTitulo26 == null){
			turmaDisciplinaNotaTitulo26 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo26;
	}


	public void setTurmaDisciplinaNotaTitulo26(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo26) {
		this.turmaDisciplinaNotaTitulo26 = turmaDisciplinaNotaTitulo26;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo27() {
		if(turmaDisciplinaNotaTitulo27 == null){
			turmaDisciplinaNotaTitulo27 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo27;
	}


	public void setTurmaDisciplinaNotaTitulo27(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo27) {
		this.turmaDisciplinaNotaTitulo27 = turmaDisciplinaNotaTitulo27;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo28() {
		if(turmaDisciplinaNotaTitulo28 == null){
			turmaDisciplinaNotaTitulo28 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo28;
	}


	public void setTurmaDisciplinaNotaTitulo28(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo28) {
		this.turmaDisciplinaNotaTitulo28 = turmaDisciplinaNotaTitulo28;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo29() {
		if(turmaDisciplinaNotaTitulo29 == null){
			turmaDisciplinaNotaTitulo29 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo29;
	}


	public void setTurmaDisciplinaNotaTitulo29(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo29) {
		this.turmaDisciplinaNotaTitulo29 = turmaDisciplinaNotaTitulo29;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo30() {
		if(turmaDisciplinaNotaTitulo30 == null){
			turmaDisciplinaNotaTitulo30 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo30;
	}


	public void setTurmaDisciplinaNotaTitulo30(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo30) {
		this.turmaDisciplinaNotaTitulo30 = turmaDisciplinaNotaTitulo30;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo31() {
		if(turmaDisciplinaNotaTitulo31 == null){
			turmaDisciplinaNotaTitulo31 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo31;
	}


	public void setTurmaDisciplinaNotaTitulo31(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo31) {
		this.turmaDisciplinaNotaTitulo31 = turmaDisciplinaNotaTitulo31;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo32() {
		if(turmaDisciplinaNotaTitulo32 == null){
			turmaDisciplinaNotaTitulo32 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo32;
	}


	public void setTurmaDisciplinaNotaTitulo32(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo32) {
		this.turmaDisciplinaNotaTitulo32 = turmaDisciplinaNotaTitulo32;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo33() {
		if(turmaDisciplinaNotaTitulo33 == null){
			turmaDisciplinaNotaTitulo33 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo33;
	}


	public void setTurmaDisciplinaNotaTitulo33(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo33) {
		this.turmaDisciplinaNotaTitulo33 = turmaDisciplinaNotaTitulo33;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo34() {
		if(turmaDisciplinaNotaTitulo34 == null){
			turmaDisciplinaNotaTitulo34 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo34;
	}


	public void setTurmaDisciplinaNotaTitulo34(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo34) {
		this.turmaDisciplinaNotaTitulo34 = turmaDisciplinaNotaTitulo34;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo35() {
		if(turmaDisciplinaNotaTitulo35 == null){
			turmaDisciplinaNotaTitulo35 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo35;
	}


	public void setTurmaDisciplinaNotaTitulo35(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo35) {
		this.turmaDisciplinaNotaTitulo35 = turmaDisciplinaNotaTitulo35;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo36() {
		if(turmaDisciplinaNotaTitulo36 == null){
			turmaDisciplinaNotaTitulo36 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo36;
	}


	public void setTurmaDisciplinaNotaTitulo36(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo36) {
		this.turmaDisciplinaNotaTitulo36 = turmaDisciplinaNotaTitulo36;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo37() {
		if(turmaDisciplinaNotaTitulo37 == null){
			turmaDisciplinaNotaTitulo37 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo37;
	}


	public void setTurmaDisciplinaNotaTitulo37(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo37) {
		this.turmaDisciplinaNotaTitulo37 = turmaDisciplinaNotaTitulo37;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo38() {
		if(turmaDisciplinaNotaTitulo38 == null){
			turmaDisciplinaNotaTitulo38 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo38;
	}


	public void setTurmaDisciplinaNotaTitulo38(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo38) {
		this.turmaDisciplinaNotaTitulo38 = turmaDisciplinaNotaTitulo38;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo39() {
		if(turmaDisciplinaNotaTitulo39 == null){
			turmaDisciplinaNotaTitulo39 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo39;
	}


	public void setTurmaDisciplinaNotaTitulo39(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo39) {
		this.turmaDisciplinaNotaTitulo39 = turmaDisciplinaNotaTitulo39;
	}


	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTitulo40() {
		if(turmaDisciplinaNotaTitulo40 == null){
			turmaDisciplinaNotaTitulo40 = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTitulo40;
	}


	public void setTurmaDisciplinaNotaTitulo40(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo40) {
		this.turmaDisciplinaNotaTitulo40 = turmaDisciplinaNotaTitulo40;
	}

	public static Boolean realizarValidacaoConfiguracaoAcademicaTipoComposta(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {		
		int maiorComposicao = 0;
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + i)) {
				String formulaCalculo = (String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "formulaCalculoNota" + i);
				if(formulaCalculo.contains("COMPOSICAO")) {
					List<String> funcoes = realizarSeparacaoEValidacaoFormulaComposicao(formulaCalculo, i, false);
					for (String funcao : funcoes) {
						Integer nrComposicao = realizarObtencaoPrimeiroParametroFuncaoComposicao(funcao, i, false);						
						if(nrComposicao > maiorComposicao) {
							maiorComposicao = nrComposicao;
						}
					}
				}				
				String formulaUsoCalculo = (String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "formulaUsoNota" + i);
				if(formulaUsoCalculo.contains("COMPOSICAO")) {
					List<String> funcoes = realizarSeparacaoEValidacaoFormulaComposicao(formulaCalculo, i, false);
					for (String funcao : funcoes) {
						Integer nrComposicao = realizarObtencaoPrimeiroParametroFuncaoComposicao(funcao, i, false);						
						if(nrComposicao > maiorComposicao) {
							maiorComposicao = nrComposicao;
						}
					}
				}				
			}
		}
		if(configuracaoAcademicoVO.getQtdeDisciplinaFilhaComposicao().equals(0)) {
			configuracaoAcademicoVO.setQtdeDisciplinaFilhaComposicao(maiorComposicao);
		}
		return maiorComposicao > 0 || configuracaoAcademicoVO.getTipoUsoConfiguracaoAcademico().equals(TipoUsoConfiguracaoAcademicoEnum.COMPOSTA);
	}
		
	
	public static String realizarSubstituicaoFuncaoComposicao(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, String formulaCalculo, int numeroNota, boolean formulaUso) throws ConsistirException, Exception{		
		if(formulaCalculo.contains("COMPOSICAO[") && Uteis.isAtributoPreenchido(historicoFilhaComposicaoVOs)){
			formulaCalculo = formulaCalculo.replace(" ", "");
			List<String> funcoes = realizarSeparacaoEValidacaoFormulaComposicao(formulaCalculo, numeroNota, formulaUso);
			for (String funcao : funcoes) {
				Integer nrComposicao = realizarObtencaoPrimeiroParametroFuncaoComposicao(funcao, numeroNota, formulaUso);
				String formula = realizarObtencaoSegundoParametroFuncaoComposicao(funcao, numeroNota);
				String formulaSubstituida = realizarSubstituicaoVariaveisPorValorFuncaoComposicao(historicoVO, historicoFilhaComposicaoVOs, nrComposicao, formula, numeroNota, formulaUso);
				formulaCalculo = formulaCalculo.replace(funcao, formulaSubstituida);
			}
		}else if(formulaCalculo.contains("COMPOSICAO[") && !Uteis.isAtributoPreenchido(historicoFilhaComposicaoVOs)) {
			throw new Exception("A configuração "+historicoVO.getConfiguracaoAcademico().getNome()+" usa a função COMPOSICAO(NUMERO_DISCIPLINA_FILHA, [FORMULA_MATEMATICA]), portanto não foi possível encontrar as disciplinas da composição para realizar o calculo da formula, realize o teste da configuração para facilitar a identificação de alguma inconsistência.");
		}	
		return formulaCalculo;
	}
	
	private static List<String> realizarSeparacaoEValidacaoFormulaComposicao(String formulaCalculo, int numeroNota, boolean formulaUso) throws ConsistirException, Exception{
		List<String> funcoes = new ArrayList<String>(0);
		formulaCalculo = formulaCalculo.replace(" ", "");
		while(formulaCalculo.contains("COMPOSICAO[")) {			
			String funcao = formulaCalculo.substring(formulaCalculo.indexOf("COMPOSICAO["), formulaCalculo.length()).trim();
			if(funcao.indexOf("]") < 0){
				throw new ConsistirException("A função COMPOSIÇÂO "+(formulaUso?" na condição de uso ":" na fórmula de calculo ")+" (NOTA "+numeroNota+") deve possui a seguinte sintaxe COMPOSICAO[NUMERO_DISCIPLINA_FILHA, FORMULA_MATEMATICA].");
			}
			funcao = funcao.substring(0, funcao.indexOf("]")+1);			
			if(funcao.contains("COMPOSICAO[")) {				
				if(funcao.contains(",") && funcao.split(",").length == 2) {
					formulaCalculo = formulaCalculo.replace(funcao, "");
					funcoes.add(funcao);
				}else {
					throw new ConsistirException("A função COMPOSIÇÂO "+(formulaUso?" na condição de uso ":" na fórmula de calculo ")+" (NOTA "+numeroNota+") deve possui a seguinte sintaxe COMPOSICAO[NUMERO_DISCIPLINA_FILHA, FORMULA_MATEMATICA].");
				}
			}
		}	
		return funcoes;
	}
	
	private static Integer realizarObtencaoPrimeiroParametroFuncaoComposicao(String funcao, int numeroNota, boolean formulaUso) throws ConsistirException, Exception{	
		String primeiro = funcao.substring(funcao.indexOf("COMPOSICAO[")+11, funcao.indexOf(",")).trim();
		if(!Uteis.getIsValorNumerico(primeiro)) {
			throw new ConsistirException("A função COMPOSIÇÂO "+(formulaUso?" na condição de uso ":" na fórmula de calculo ")+" ("+funcao+") (NOTA "+numeroNota+") deve possui como primeiro parâmetro um valor numérico inteiro.");
		}
		return Integer.valueOf(primeiro);
	}
	
	private static String realizarObtencaoSegundoParametroFuncaoComposicao(String funcao, int numeroNota) throws ConsistirException{
		String segundo = funcao.substring(funcao.indexOf(",")+1, funcao.indexOf("]")).trim();		
		return segundo;
	}
	
	private static String realizarSubstituicaoVariaveisPorValorFuncaoComposicao(HistoricoVO historicoVO, List<HistoricoVO> historicoFilhaComposicaoVOs, Integer numeroComposicao, String funcao, int numeroNota, boolean formulaUso) throws ConsistirException, Exception{
		if(historicoFilhaComposicaoVOs.size() < numeroComposicao) {
			throw new ConsistirException("A configuração acadêmica "+historicoVO.getConfiguracaoAcademico().getNome()+" está usando a função COMPOSICAO "+(formulaUso?" na condição de uso ":" na fórmula de calculo ")+" na nota "+numeroNota+", porém não foi possível encontrar a disciplina composta de número "+numeroComposicao+".");
		}
		HistoricoVO historicoFilhaComposicao = historicoFilhaComposicaoVOs.get(numeroComposicao-1);
		return historicoFilhaComposicao.getConfiguracaoAcademico().substituirVariaveisPorValor(historicoFilhaComposicao, null, funcao);			
	}


	public TipoUsoConfiguracaoAcademicoEnum getTipoUsoConfiguracaoAcademico() {
		if (tipoUsoConfiguracaoAcademico == null) {
			tipoUsoConfiguracaoAcademico = TipoUsoConfiguracaoAcademicoEnum.GERAL;
		}
		return tipoUsoConfiguracaoAcademico;
	}


	public void setTipoUsoConfiguracaoAcademico(TipoUsoConfiguracaoAcademicoEnum tipoUsoConfiguracaoAcademico) {
		this.tipoUsoConfiguracaoAcademico = tipoUsoConfiguracaoAcademico;
	}


	public RegraCalculoDisciplinaCompostaEnum getRegraCalculoDisciplinaComposta() {
		if (regraCalculoDisciplinaComposta == null) {
			regraCalculoDisciplinaComposta = RegraCalculoDisciplinaCompostaEnum.MEDIA_FILHA_COMPOSICAO;
		}
		return regraCalculoDisciplinaComposta;
	}


	public void setRegraCalculoDisciplinaComposta(RegraCalculoDisciplinaCompostaEnum regraCalculoDisciplinaComposta) {
		this.regraCalculoDisciplinaComposta = regraCalculoDisciplinaComposta;
	}

	private List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs;
	public List<ConfiguracaoAcademicaNotaVO> getConfiguracaoAcademicaNotaVOs() {
		if (configuracaoAcademicaNotaVOs == null) {
			configuracaoAcademicaNotaVOs = new ArrayList<ConfiguracaoAcademicaNotaVO>(0);
			for(int i = 1; i <= 40; i++) {
				ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO)UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicaNota"+i+"VO");
				if(configuracaoAcademicaNotaVO.getUtilizarNota()) {
					configuracaoAcademicaNotaVOs.add(configuracaoAcademicaNotaVO);
				}
			}
			
		}
		return configuracaoAcademicaNotaVOs;
	}


	public void setConfiguracaoAcademicaNotaVOs(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs) {
		this.configuracaoAcademicaNotaVOs = configuracaoAcademicaNotaVOs;
	}
	
	@XmlElement(name = "configuracaoAcademicaNotaUtilizarVOs")
	public List<ConfiguracaoAcademicaNotaVO> getConfiguracaoAcademicaNotaUtilizarVOs() {
		if (configuracaoAcademicaNotaUtilizarVOs == null) {
			configuracaoAcademicaNotaUtilizarVOs = new ArrayList<ConfiguracaoAcademicaNotaVO>(0);
			for(int i = 1; i <= 40; i++) {
				ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO)UtilReflexao.invocarMetodoGet(this, "configuracaoAcademicaNota"+i+"VO");
				if(configuracaoAcademicaNotaVO.getUtilizarNota() && configuracaoAcademicaNotaVO.getApresentarNota()) {
					configuracaoAcademicaNotaUtilizarVOs.add(configuracaoAcademicaNotaVO);
				}
			}
			
		}
		return configuracaoAcademicaNotaUtilizarVOs;
	}


	public void setConfiguracaoAcademicaNotaUtilizarVOs(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaUtilizarVOs) {
		this.configuracaoAcademicaNotaUtilizarVOs = configuracaoAcademicaNotaUtilizarVOs;
	}
	
	public Integer getQtdeNotaUtilizar() {
		return getConfiguracaoAcademicaNotaUtilizarVOs().size();
	}
	
	public Integer getQtdePorLinhaNotaUtilizar() {
		return getConfiguracaoAcademicaNotaUtilizarVOs().size() > 10 ? 10 : getConfiguracaoAcademicaNotaUtilizarVOs().size();
	}
	
	/**
	 * @return the ocultarBotaoCalcularMedia
	 */
	@XmlElement(name = "ocultarBotaoCalcularMedia")
	public Boolean getOcultarBotaoCalcularMedia() {
		if (ocultarBotaoCalcularMedia == null) {
			ocultarBotaoCalcularMedia = false;
		}
		return ocultarBotaoCalcularMedia;
	}


	/**
	 * @param ocultarBotaoCalcularMedia the ocultarBotaoCalcularMedia to set
	 */
	public void setOcultarBotaoCalcularMedia(Boolean ocultarBotaoCalcularMedia) {
		this.ocultarBotaoCalcularMedia = ocultarBotaoCalcularMedia;
	}

	public Boolean getValidarChoqueHorarioOutraMatriculaAluno() {
		if (validarChoqueHorarioOutraMatriculaAluno == null) {
			validarChoqueHorarioOutraMatriculaAluno = false;
		}
		return validarChoqueHorarioOutraMatriculaAluno;
	}

	public void setValidarChoqueHorarioOutraMatriculaAluno(Boolean validarChoqueHorarioOutraMatriculaAluno) {
		this.validarChoqueHorarioOutraMatriculaAluno = validarChoqueHorarioOutraMatriculaAluno;
	}

	public Boolean getValidarDadosEnadeCensoMatricularAluno() {
		if (validarDadosEnadeCensoMatricularAluno == null) {
			validarDadosEnadeCensoMatricularAluno = false;
		}
		return validarDadosEnadeCensoMatricularAluno;
	}

	public void setValidarDadosEnadeCensoMatricularAluno(Boolean validarDadosEnadeCensoMatricularAluno) {
		this.validarDadosEnadeCensoMatricularAluno = validarDadosEnadeCensoMatricularAluno;
	}
	
	private Boolean usarFormulaCalculoFrequencia;
	private String formulaCalculoFrequencia;
	
	public Boolean getUsarFormulaCalculoFrequencia() {
		if(usarFormulaCalculoFrequencia == null){
			usarFormulaCalculoFrequencia = false;
		}
		return usarFormulaCalculoFrequencia;
	}


	public void setUsarFormulaCalculoFrequencia(Boolean usarFormulaCalculoFrequencia) {
		this.usarFormulaCalculoFrequencia = usarFormulaCalculoFrequencia;
	}


	public String getFormulaCalculoFrequencia() {
		if(formulaCalculoFrequencia == null){
			formulaCalculoFrequencia = "";
		}
		return formulaCalculoFrequencia;
	}


	public void setFormulaCalculoFrequencia(String formulaCalculoFrequencia) {
		this.formulaCalculoFrequencia = formulaCalculoFrequencia;
	}

	public Boolean getOcultarMediaFinalDisciplinaCasoReprovado() {
		if (ocultarMediaFinalDisciplinaCasoReprovado == null) {
			ocultarMediaFinalDisciplinaCasoReprovado = false;
		}
		return ocultarMediaFinalDisciplinaCasoReprovado;
	}


	public void setOcultarMediaFinalDisciplinaCasoReprovado(Boolean ocultarMediaFinalDisciplinaCasoReprovado) {
		this.ocultarMediaFinalDisciplinaCasoReprovado = ocultarMediaFinalDisciplinaCasoReprovado;
	}

	public String getFormulaCoeficienteRendimento() {
		if (formulaCoeficienteRendimento == null) {
			formulaCoeficienteRendimento = "";
		}
		return formulaCoeficienteRendimento;
	}


	public void setFormulaCoeficienteRendimento(String formulaCoeficienteRendimento) {
		this.formulaCoeficienteRendimento = formulaCoeficienteRendimento;
	}
	
	public static Boolean validarFormulaCoeficienteRendimento(String formula) {
		List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>();
		HistoricoVO historicoVO1 = new HistoricoVO();
		historicoVO1.setMediaFinal(5.0);
		historicoVO1.getGradeDisciplinaVO().setNrCreditos(1);
		historicoVO1.setCargaHorariaDisciplina(50);
		historicoVO1.setSituacao("AP");
		HistoricoVO historicoVO2 = new HistoricoVO();
		historicoVO2.setMediaFinal(1.0);
		historicoVO2.getGradeDisciplinaVO().setNrCreditos(6);
		historicoVO2.setCargaHorariaDisciplina(10);
		historicoVO2.setSituacao("CA");
		HistoricoVO historicoVO3 = new HistoricoVO();
		historicoVO3.setMediaFinal(9.5);
		historicoVO3.getGradeDisciplinaVO().setNrCreditos(20);
		historicoVO3.setCargaHorariaDisciplina(5);
		historicoVO3.setSituacao("CS");
		historicoVOs.add(historicoVO1);
		historicoVOs.add(historicoVO2);
		historicoVOs.add(historicoVO3);
		try {
			Double coeficienteRendimento = realizarCalculoCoeficienteRendimento(formula, historicoVOs);
			if (coeficienteRendimento <= 0.0) {
				return false;
			} else {
			return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Double realizarCalculoCoeficienteRendimento(String formulaBase, List<HistoricoVO> historicoVOs) throws Exception{
		formulaBase = formulaBase.replace(" ", "");
		String[] formulaSomas = formulaBase.split("SOMA\\[");
		for(String soma: formulaSomas) {
			if (Uteis.isAtributoPreenchido(soma)) {
				Double valorSomado = 0.0;
			for(HistoricoVO historicoVO: historicoVOs) {
				valorSomado += substituirValoresCoeficienteRendimento(soma.substring(0, soma.lastIndexOf("]")), historicoVO);
			}
			String operador = soma.contains("]") ? soma.substring(soma.lastIndexOf("]") +1 ) : "";
				formulaBase = formulaBase.replace("SOMA[", "");
				if (!operador.isEmpty()) {
					if (operador.equals("/") || operador.equals("+") || operador.equals("-")|| operador.equals("*")) {
						formulaBase = formulaBase.replace(soma, valorSomado.toString() + operador);		
					}
				} else {
					formulaBase = formulaBase.replace(soma, valorSomado.toString());
					
				}
			}
		}		
		return Uteis.arrendondarForcando2CadasDecimais(Uteis.realizarCalculoFormula(formulaBase));
	}
	
	public static Double substituirValoresCoeficienteRendimento(String formulaCoeficienteRendimento, HistoricoVO historicoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(formulaCoeficienteRendimento)) {
			if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO().getNrCreditos())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CR",	historicoVO.getGradeDisciplinaVO().getNrCreditos().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CR",	"0");
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getCargaHorariaDisciplina())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CH",	historicoVO.getCargaHorariaDisciplina().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CH",	"0");
			}			
			formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_DIS",	"1");			
			if (Uteis.isAtributoPreenchido(historicoVO.getMediaFinal())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("MEDIA_FINAL", historicoVO.getMediaFinal().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("MEDIA_FINAL", "0.0");
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getSituacao())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("SIT_HIS", historicoVO.getSituacao());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("SIT_HIS", "");
			}
		}
		return Uteis.realizarCalculoFormula(formulaCoeficienteRendimento);
	}
	


	public String getMascaraNumeroProcessoExpedicaoDiploma() {
		if(mascaraNumeroProcessoExpedicaoDiploma == null ) {
			mascaraNumeroProcessoExpedicaoDiploma = "";
		}
		return mascaraNumeroProcessoExpedicaoDiploma;
	}


	public void setMascaraNumeroProcessoExpedicaoDiploma(String mascaraNumeroProcessoExpedicaoDiploma) {
		this.mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma;
	}	
	
	public Integer getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula() {
		if (quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula == null) {
			quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula = 0;
		}
		return quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula;
	}

	public void setQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula(
			Integer quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula) {
		this.quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula = quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula;
	}
	
	public Integer getCasasDecimaisCoeficienteRendimento() {
		if (casasDecimaisCoeficienteRendimento == null) {
			casasDecimaisCoeficienteRendimento = 0;
		}
		return casasDecimaisCoeficienteRendimento;
	}


	public void setCasasDecimaisCoeficienteRendimento(Integer casasDecimaisCoeficienteRendimento) {
		this.casasDecimaisCoeficienteRendimento = casasDecimaisCoeficienteRendimento;
	}
	public Boolean getPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual() {
		if (permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual == null) {
			permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual = true;
		}
		return permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual;
	}

	public void setPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual(Boolean permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual) {
		this.permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual = permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual;
	}


	public Boolean getRegistrarComoFaltaAulasRealizadasAposDataMatricula() {
		if (registrarComoFaltaAulasRealizadasAposDataMatricula == null) {
			registrarComoFaltaAulasRealizadasAposDataMatricula = false;
		}
		return registrarComoFaltaAulasRealizadasAposDataMatricula;
	}


	public void setRegistrarComoFaltaAulasRealizadasAposDataMatricula(
			Boolean registrarComoFaltaAulasRealizadasAposDataMatricula) {
		this.registrarComoFaltaAulasRealizadasAposDataMatricula = registrarComoFaltaAulasRealizadasAposDataMatricula;
	}
	
	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTituloVO(Integer nrNota) {
		return (TurmaDisciplinaNotaTituloVO)UtilReflexao.invocarMetodoGet(this, "turmaDisciplinaNotaTitulo"+nrNota);		
	}


	public String getMascaraNumeroRegistroDiploma() {
		if(mascaraNumeroRegistroDiploma == null ) {
			mascaraNumeroRegistroDiploma ="";
		}
		return mascaraNumeroRegistroDiploma;
	}


	public void setMascaraNumeroRegistroDiploma(String mascaraNumeroRegistroDiploma) {
		this.mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma;
	}	
	
	public Boolean getPermitirAproveitamentoDisciplinasOptativas() {
		if (permitirAproveitamentoDisciplinasOptativas == null) {
			permitirAproveitamentoDisciplinasOptativas = Boolean.TRUE;
		}
		return permitirAproveitamentoDisciplinasOptativas;
	}

	public void setPermitirAproveitamentoDisciplinasOptativas(Boolean permitirAproveitamentoDisciplinasOptativas) {
		this.permitirAproveitamentoDisciplinasOptativas = permitirAproveitamentoDisciplinasOptativas;
	}
}
