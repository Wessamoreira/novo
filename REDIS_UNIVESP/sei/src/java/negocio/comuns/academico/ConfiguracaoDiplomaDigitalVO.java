package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.CampoPeriodoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.CoordenadorCursoDisciplinaAproveitadaEnum;
import negocio.comuns.academico.enumeradores.FormatoCargaHorariaXmlEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoDiplomaDigital. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @author Felipi Alves
 */
public class ConfiguracaoDiplomaDigitalVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private static final String TEXTO_PADRAO = "TextoPadrao";

	private Integer codigo;
	private Integer horaRelogio;
	private Date dataCadastro;
	private Boolean padrao;
	private Boolean utilizarCoordenadorCursoAtividadeComplementar;
	private Boolean apresentarTextoEnade;
	private Boolean historicoConsiderarAprovado;
	private Boolean historicoConsiderarReprovado;
	private Boolean historicoConsiderarCursando;
	private Boolean historicoConsiderarEvasao;
	private Boolean historicoConsiderarForaGrade;
	private Boolean considerarCargaHorariaForaGrade;
	private Boolean apresentarApenasUltimoHistoricoDisciplina;
	private Boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	private Boolean validarArquivoComprobatoriaIsPDFA;
	private CampoPeriodoDisciplinaEnum utilizarCampoPeriodoDisciplina;
	private CoordenadorCursoDisciplinaAproveitadaEnum coordenadorCursoDisciplinasAproveitadas;
	private VersaoDiplomaDigitalEnum versao;
	private FormatoCargaHorariaXmlEnum formatoCargaHorariaXML;
	private String descricao;
	private String layoutGraduacaoPadrao;
	private String layoutGraduacaoTecnologicaPadrao;
	private String tituloFuncionarioPrimario;
	private String tituloFuncionarioSecundario;
	private String tituloFuncionarioTerceiro;
	private String tituloFuncionarioQuarto;
	private String tituloFuncionarioQuinto;
	private String tipoLayoutHistoricoGraduacao;
	private String tipoLayoutHistoricoGraduacaoTecnologica;
	private UnidadeEnsinoVO unidadeEnsinoPadrao;
	private TextoPadraoDeclaracaoVO textoPadraoGraduacaoPadrao;
	private TextoPadraoDeclaracaoVO textoPadraoGraduacaoTecnologicaPadrao;
	private FuncionarioVO funcionarioPrimario;
	private FuncionarioVO funcionarioTerceiro;
	private FuncionarioVO funcionarioQuarto;
	private FuncionarioVO funcionarioQuinto;
	private FuncionarioVO funcionarioSecundario;
	private CargoVO cargoFuncionarioPrimario;
	private CargoVO cargoFuncionarioSecundario;
	private CargoVO cargoFuncionarioTerceiro;
	private CargoVO cargoFuncionarioQuarto;
	private CargoVO cargoFuncionarioQuinto;
	private ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoGraduacao;
	private ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoGraduacaoTecnologica;

	public void validarDados() throws Exception {
		if (!Uteis.isAtributoPreenchido(getDescricao())) {
			throw new Exception("O campo DESCRIÇÃO deve ser informado.");
		}
		if (getLayoutGraduacaoPadrao().equals(TEXTO_PADRAO)) {
			if (!Uteis.isAtributoPreenchido(getTextoPadraoGraduacaoPadrao())) {
				throw new Exception("O TEXTO PADRÃO (Graduação) deve ser informado.");
			}
		}
		if (getLayoutGraduacaoTecnologicaPadrao().equals(TEXTO_PADRAO)) {
			if (!Uteis.isAtributoPreenchido(getTextoPadraoGraduacaoTecnologicaPadrao())) {
				throw new Exception("O TEXTO PADRÃO (Graduação Tecnológica) deve ser informado.");
			}
		}
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

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = Constantes.EMPTY;
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getPadrao() {
		if (padrao == null) {
			padrao = Boolean.FALSE;
		}
		return padrao;
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoPadrao() {
		if (unidadeEnsinoPadrao == null) {
			unidadeEnsinoPadrao = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoPadrao;
	}

	public void setUnidadeEnsinoPadrao(UnidadeEnsinoVO unidadeEnsinoPadrao) {
		this.unidadeEnsinoPadrao = unidadeEnsinoPadrao;
	}

	public String getLayoutGraduacaoPadrao() {
		if (layoutGraduacaoPadrao == null) {
			layoutGraduacaoPadrao = Constantes.EMPTY;
		}
		return layoutGraduacaoPadrao;
	}

	public void setLayoutGraduacaoPadrao(String layoutGraduacaoPadrao) {
		this.layoutGraduacaoPadrao = layoutGraduacaoPadrao;
	}

	public String getLayoutGraduacaoTecnologicaPadrao() {
		if (layoutGraduacaoTecnologicaPadrao == null) {
			layoutGraduacaoTecnologicaPadrao = Constantes.EMPTY;
		}
		return layoutGraduacaoTecnologicaPadrao;
	}

	public void setLayoutGraduacaoTecnologicaPadrao(String layoutGraduacaoTecnologicaPadrao) {
		this.layoutGraduacaoTecnologicaPadrao = layoutGraduacaoTecnologicaPadrao;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoGraduacaoPadrao() {
		if (textoPadraoGraduacaoPadrao == null) {
			textoPadraoGraduacaoPadrao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoGraduacaoPadrao;
	}

	public void setTextoPadraoGraduacaoPadrao(TextoPadraoDeclaracaoVO textoPadraoGraduacaoPadrao) {
		this.textoPadraoGraduacaoPadrao = textoPadraoGraduacaoPadrao;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoGraduacaoTecnologicaPadrao() {
		if (textoPadraoGraduacaoTecnologicaPadrao == null) {
			textoPadraoGraduacaoTecnologicaPadrao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoGraduacaoTecnologicaPadrao;
	}

	public void setTextoPadraoGraduacaoTecnologicaPadrao(TextoPadraoDeclaracaoVO textoPadraoGraduacaoTecnologicaPadrao) {
		this.textoPadraoGraduacaoTecnologicaPadrao = textoPadraoGraduacaoTecnologicaPadrao;
	}

	public FuncionarioVO getFuncionarioPrimario() {
		if (funcionarioPrimario == null) {
			funcionarioPrimario = new FuncionarioVO();
		}
		return funcionarioPrimario;
	}

	public void setFuncionarioPrimario(FuncionarioVO funcionarioPrimario) {
		this.funcionarioPrimario = funcionarioPrimario;
	}

	public CargoVO getCargoFuncionarioPrimario() {
		if (cargoFuncionarioPrimario == null) {
			cargoFuncionarioPrimario = new CargoVO();
		}
		return cargoFuncionarioPrimario;
	}

	public void setCargoFuncionarioPrimario(CargoVO cargoFuncionarioPrimario) {
		this.cargoFuncionarioPrimario = cargoFuncionarioPrimario;
	}

	public String getTituloFuncionarioPrimario() {
		if (tituloFuncionarioPrimario == null) {
			tituloFuncionarioPrimario = Constantes.EMPTY;
		}
		return tituloFuncionarioPrimario;
	}

	public void setTituloFuncionarioPrimario(String tituloFuncionarioPrimario) {
		this.tituloFuncionarioPrimario = tituloFuncionarioPrimario;
	}

	public FuncionarioVO getFuncionarioSecundario() {
		if (funcionarioSecundario == null) {
			funcionarioSecundario = new FuncionarioVO();
		}
		return funcionarioSecundario;
	}

	public void setFuncionarioSecundario(FuncionarioVO funcionarioSecundario) {
		this.funcionarioSecundario = funcionarioSecundario;
	}

	public CargoVO getCargoFuncionarioSecundario() {
		if (cargoFuncionarioSecundario == null) {
			cargoFuncionarioSecundario = new CargoVO();
		}
		return cargoFuncionarioSecundario;
	}

	public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
		this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
	}

	public String getTituloFuncionarioSecundario() {
		if (tituloFuncionarioSecundario == null) {
			tituloFuncionarioSecundario = Constantes.EMPTY;
		}
		return tituloFuncionarioSecundario;
	}

	public void setTituloFuncionarioSecundario(String tituloFuncionarioSecundario) {
		this.tituloFuncionarioSecundario = tituloFuncionarioSecundario;
	}

	public FuncionarioVO getFuncionarioTerceiro() {
		if (funcionarioTerceiro == null) {
			funcionarioTerceiro = new FuncionarioVO();
		}
		return funcionarioTerceiro;
	}

	public void setFuncionarioTerceiro(FuncionarioVO funcionarioTerceiro) {
		this.funcionarioTerceiro = funcionarioTerceiro;
	}

	public CargoVO getCargoFuncionarioTerceiro() {
		if (cargoFuncionarioTerceiro == null) {
			cargoFuncionarioTerceiro = new CargoVO();
		}
		return cargoFuncionarioTerceiro;
	}

	public void setCargoFuncionarioTerceiro(CargoVO cargoFuncionarioTerceiro) {
		this.cargoFuncionarioTerceiro = cargoFuncionarioTerceiro;
	}

	public String getTituloFuncionarioTerceiro() {
		if (tituloFuncionarioTerceiro == null) {
			tituloFuncionarioTerceiro = Constantes.EMPTY;
		}
		return tituloFuncionarioTerceiro;
	}

	public void setTituloFuncionarioTerceiro(String tituloFuncionarioTerceiro) {
		this.tituloFuncionarioTerceiro = tituloFuncionarioTerceiro;
	}

	public FuncionarioVO getFuncionarioQuarto() {
		if (funcionarioQuarto == null) {
			funcionarioQuarto = new FuncionarioVO();
		}
		return funcionarioQuarto;
	}

	public void setFuncionarioQuarto(FuncionarioVO funcionarioQuarto) {
		this.funcionarioQuarto = funcionarioQuarto;
	}

	public CargoVO getCargoFuncionarioQuarto() {
		if (cargoFuncionarioQuarto == null) {
			cargoFuncionarioQuarto = new CargoVO();
		}
		return cargoFuncionarioQuarto;
	}

	public void setCargoFuncionarioQuarto(CargoVO cargoFuncionarioQuarto) {
		this.cargoFuncionarioQuarto = cargoFuncionarioQuarto;
	}

	public String getTituloFuncionarioQuarto() {
		if (tituloFuncionarioQuarto == null) {
			tituloFuncionarioQuarto = Constantes.EMPTY;
		}
		return tituloFuncionarioQuarto;
	}

	public void setTituloFuncionarioQuarto(String tituloFuncionarioQuarto) {
		this.tituloFuncionarioQuarto = tituloFuncionarioQuarto;
	}

	public FuncionarioVO getFuncionarioQuinto() {
		if (funcionarioQuinto == null) {
			funcionarioQuinto = new FuncionarioVO();
		}
		return funcionarioQuinto;
	}

	public void setFuncionarioQuinto(FuncionarioVO funcionarioQuinto) {
		this.funcionarioQuinto = funcionarioQuinto;
	}

	public CargoVO getCargoFuncionarioQuinto() {
		if (cargoFuncionarioQuinto == null) {
			cargoFuncionarioQuinto = new CargoVO();
		}
		return cargoFuncionarioQuinto;
	}

	public void setCargoFuncionarioQuinto(CargoVO cargoFuncionarioQuinto) {
		this.cargoFuncionarioQuinto = cargoFuncionarioQuinto;
	}

	public String getTituloFuncionarioQuinto() {
		if (tituloFuncionarioQuinto == null) {
			tituloFuncionarioQuinto = Constantes.EMPTY;
		}
		return tituloFuncionarioQuinto;
	}

	public void setTituloFuncionarioQuinto(String tituloFuncionarioQuinto) {
		this.tituloFuncionarioQuinto = tituloFuncionarioQuinto;
	}
	
	public String getTipoLayoutHistoricoGraduacao() {
		if (tipoLayoutHistoricoGraduacao == null) {
			tipoLayoutHistoricoGraduacao = Constantes.EMPTY;
		}
		return tipoLayoutHistoricoGraduacao;
	}
	
	public void setTipoLayoutHistoricoGraduacao(String tipoLayoutHistoricoGraduacao) {
		this.tipoLayoutHistoricoGraduacao = tipoLayoutHistoricoGraduacao;
	}
	
	public String getTipoLayoutHistoricoGraduacaoTecnologica() {
		if (tipoLayoutHistoricoGraduacaoTecnologica == null) {
			tipoLayoutHistoricoGraduacaoTecnologica = Constantes.EMPTY;
		}
		return tipoLayoutHistoricoGraduacaoTecnologica;
	}
	
	public void setTipoLayoutHistoricoGraduacaoTecnologica(String tipoLayoutHistoricoGraduacaoTecnologica) {
		this.tipoLayoutHistoricoGraduacaoTecnologica = tipoLayoutHistoricoGraduacaoTecnologica;
	}

	public CampoPeriodoDisciplinaEnum getUtilizarCampoPeriodoDisciplina() {
		if (utilizarCampoPeriodoDisciplina == null) {
			utilizarCampoPeriodoDisciplina = CampoPeriodoDisciplinaEnum.NUMERO_PERIODO_LETIVO;
		}
		return utilizarCampoPeriodoDisciplina;
	}

	public void setUtilizarCampoPeriodoDisciplina(CampoPeriodoDisciplinaEnum utilizarCampoPeriodoDisciplina) {
		this.utilizarCampoPeriodoDisciplina = utilizarCampoPeriodoDisciplina;
	}

	public CoordenadorCursoDisciplinaAproveitadaEnum getCoordenadorCursoDisciplinasAproveitadas() {
		if (coordenadorCursoDisciplinasAproveitadas == null) {
			coordenadorCursoDisciplinasAproveitadas = CoordenadorCursoDisciplinaAproveitadaEnum.APENAS_APROVEITAMENTO_SEM_PROFESSOR;
		}
		return coordenadorCursoDisciplinasAproveitadas;
	}

	public void setCoordenadorCursoDisciplinasAproveitadas(CoordenadorCursoDisciplinaAproveitadaEnum coordenadorCursoDisciplinasAproveitadas) {
		this.coordenadorCursoDisciplinasAproveitadas = coordenadorCursoDisciplinasAproveitadas;
	}

	public Boolean getUtilizarCoordenadorCursoAtividadeComplementar() {
		if (utilizarCoordenadorCursoAtividadeComplementar == null) {
			utilizarCoordenadorCursoAtividadeComplementar = Boolean.FALSE;
		}
		return utilizarCoordenadorCursoAtividadeComplementar;
	}

	public void setUtilizarCoordenadorCursoAtividadeComplementar(Boolean utilizarCoordenadorCursoAtividadeComplementar) {
		this.utilizarCoordenadorCursoAtividadeComplementar = utilizarCoordenadorCursoAtividadeComplementar;
	}
	
	public Boolean getApresentarTextoEnade() {
		if (apresentarTextoEnade == null) { 
			apresentarTextoEnade = Boolean.FALSE;
		}
		return apresentarTextoEnade;
	}
	
	public void setApresentarTextoEnade(Boolean apresentarTextoEnade) {
		this.apresentarTextoEnade = apresentarTextoEnade;
	}

	public Integer getHoraRelogio() {
		if (horaRelogio == null) {
			horaRelogio = 0;
		}
		return horaRelogio;
	}
	
	public void setHoraRelogio(Integer horaRelogio) {
		this.horaRelogio = horaRelogio;
	}
	
	public VersaoDiplomaDigitalEnum getVersao() {
		if (versao == null) {
			versao = VersaoDiplomaDigitalEnum.VERSAO_1_05;
		}
		return versao;
	}
	
	public void setVersao(VersaoDiplomaDigitalEnum versao) {
		this.versao = versao;
	}

	public Boolean getHistoricoConsiderarAprovado() {
		if (historicoConsiderarAprovado == null) {
			historicoConsiderarAprovado = Boolean.TRUE;
		}
		return historicoConsiderarAprovado;
	}

	public void setHistoricoConsiderarAprovado(Boolean historicoConsiderarAprovado) {
		this.historicoConsiderarAprovado = historicoConsiderarAprovado;
	}

	public Boolean getHistoricoConsiderarReprovado() {
		if (historicoConsiderarReprovado == null) {
			historicoConsiderarReprovado = Boolean.TRUE;
		}
		return historicoConsiderarReprovado;
	}

	public void setHistoricoConsiderarReprovado(Boolean historicoConsiderarReprovado) {
		this.historicoConsiderarReprovado = historicoConsiderarReprovado;
	}

	public Boolean getHistoricoConsiderarCursando() {
		if (historicoConsiderarCursando == null) {
			historicoConsiderarCursando = Boolean.TRUE;
		}
		return historicoConsiderarCursando;
	}

	public void setHistoricoConsiderarCursando(Boolean historicoConsiderarCursando) {
		this.historicoConsiderarCursando = historicoConsiderarCursando;
	}

	public Boolean getHistoricoConsiderarEvasao() {
		if (historicoConsiderarEvasao == null) {
			historicoConsiderarEvasao = Boolean.FALSE;
		}
		return historicoConsiderarEvasao;
	}

	public void setHistoricoConsiderarEvasao(Boolean historicoConsiderarEvasao) {
		this.historicoConsiderarEvasao = historicoConsiderarEvasao;
	}

	public Boolean getHistoricoConsiderarForaGrade() {
		if (historicoConsiderarForaGrade == null) {
			historicoConsiderarForaGrade = Boolean.TRUE;
		}
		return historicoConsiderarForaGrade;
	}

	public void setHistoricoConsiderarForaGrade(Boolean historicoConsiderarForaGrade) {
		this.historicoConsiderarForaGrade = historicoConsiderarForaGrade;
	}

	public Boolean getConsiderarCargaHorariaForaGrade() {
		if (considerarCargaHorariaForaGrade == null) {
			considerarCargaHorariaForaGrade = Boolean.TRUE;
		}
		return considerarCargaHorariaForaGrade;
	}

	public void setConsiderarCargaHorariaForaGrade(Boolean considerarCargaHorariaForaGrade) {
		this.considerarCargaHorariaForaGrade = considerarCargaHorariaForaGrade;
	}
	
	public Boolean getApresentarApenasUltimoHistoricoDisciplina() {
		if (apresentarApenasUltimoHistoricoDisciplina == null) {
			apresentarApenasUltimoHistoricoDisciplina = Boolean.FALSE;
		}
		return apresentarApenasUltimoHistoricoDisciplina;
	}
	
	public void setApresentarApenasUltimoHistoricoDisciplina(Boolean apresentarApenasUltimoHistoricoDisciplina) {
		this.apresentarApenasUltimoHistoricoDisciplina = apresentarApenasUltimoHistoricoDisciplina;
	}
	
	public Boolean getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista() {
		if (considerarCargaHorariaCursadaIgualCargaHorariaPrevista == null) {
			considerarCargaHorariaCursadaIgualCargaHorariaPrevista = Boolean.FALSE;
		}
		return considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	}
	
	public void setConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(Boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista) {
		this.considerarCargaHorariaCursadaIgualCargaHorariaPrevista = considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	}
	
	public FormatoCargaHorariaXmlEnum getFormatoCargaHorariaXML() {
		if (formatoCargaHorariaXML == null) {
			formatoCargaHorariaXML = FormatoCargaHorariaXmlEnum.HORA_AULA;
		}
		return formatoCargaHorariaXML;
	}
	
	public void setFormatoCargaHorariaXML(FormatoCargaHorariaXmlEnum formatoCargaHorariaXML) {
		this.formatoCargaHorariaXML = formatoCargaHorariaXML;
	}

	public ConfiguracaoLayoutHistoricoVO getConfiguracaoLayoutHistoricoGraduacao() {
		if (configuracaoLayoutHistoricoGraduacao == null) {
			configuracaoLayoutHistoricoGraduacao = new ConfiguracaoLayoutHistoricoVO();
		}
		return configuracaoLayoutHistoricoGraduacao;
	}

	public void setConfiguracaoLayoutHistoricoGraduacao(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoGraduacao) {
		this.configuracaoLayoutHistoricoGraduacao = configuracaoLayoutHistoricoGraduacao;
	}

	public ConfiguracaoLayoutHistoricoVO getConfiguracaoLayoutHistoricoGraduacaoTecnologica() {
		if (configuracaoLayoutHistoricoGraduacaoTecnologica == null) {
			configuracaoLayoutHistoricoGraduacaoTecnologica = new ConfiguracaoLayoutHistoricoVO();
		}
		return configuracaoLayoutHistoricoGraduacaoTecnologica;
	}

	public void setConfiguracaoLayoutHistoricoGraduacaoTecnologica(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoGraduacaoTecnologica) {
		this.configuracaoLayoutHistoricoGraduacaoTecnologica = configuracaoLayoutHistoricoGraduacaoTecnologica;
	}

	public boolean isTipoLayoutGraduacaoPersonalizado() {
		return Uteis.isAtributoPreenchido(getTipoLayoutHistoricoGraduacao()) && Uteis.getIsValorNumerico2(getTipoLayoutHistoricoGraduacao());
	}

	public boolean isTipoLayoutGraduacaoTecnologicaPersonalizado() {
		return Uteis.isAtributoPreenchido(getTipoLayoutHistoricoGraduacaoTecnologica()) && Uteis.getIsValorNumerico2(getTipoLayoutHistoricoGraduacaoTecnologica());
	}

	public boolean isNivelMontarDadosTodos() {
		return Uteis.isAtributoPreenchido(getNivelMontarDados()) && getNivelMontarDados().equals(NivelMontarDados.TODOS);
	}
	
	public Boolean getValidarArquivoComprobatoriaIsPDFA() {
		if (validarArquivoComprobatoriaIsPDFA == null) {
			validarArquivoComprobatoriaIsPDFA = Boolean.TRUE;
		}
		return validarArquivoComprobatoriaIsPDFA;
	}
	
	public void setValidarArquivoComprobatoriaIsPDFA(Boolean validarArquivoComprobatoriaIsPDFA) {
		this.validarArquivoComprobatoriaIsPDFA = validarArquivoComprobatoriaIsPDFA;
	}
}