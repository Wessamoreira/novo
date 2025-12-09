package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

/**
 * @see SuperVO
 * @author Felipi Alves
 */
public class GestaoXmlGradeCurricularVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private List<GestaoXmlGradeCurricularVO> listaGestaoXmlGradeCurricularErro;
	private List<UnidadeEnsinoVO> unidadeEnsinoFiltrarVOs;
	private List<CursoVO> cursoFiltrarVOs;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private ConsistirException consistirException;
	private DataModelo controleConsultaOtimizado;
	private GradeCurricularVO gradeCurricularVO;
	private FuncionarioVO funcionarioSecundario;
	private FuncionarioVO funcionarioPrimario;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CargoVO cargoSecundario;
	private CargoVO cargoPrimario;
	private CursoVO cursoVO;
	private String tituloFuncionarioSecundario;
	private String tituloFuncionarioPrimario;
	private String situacaoGradeFiltrar;
	private String situacaoXmlFiltrar;
	private String situacaoDocumento;
	private String motivoRejeicao;
	private String codigoValidacao;
	private Boolean coordenadorCursoAssinateEcpfLote;
	private Boolean selecionado;

	public List<GestaoXmlGradeCurricularVO> getListaGestaoXmlGradeCurricularErro() {
		if (listaGestaoXmlGradeCurricularErro == null) {
			listaGestaoXmlGradeCurricularErro = new ArrayList<>(0);
		}
		return listaGestaoXmlGradeCurricularErro;
	}
	
	public void setListaGestaoXmlGradeCurricularErro(List<GestaoXmlGradeCurricularVO> listaGestaoXmlGradeCurricularErro) {
		this.listaGestaoXmlGradeCurricularErro = listaGestaoXmlGradeCurricularErro;
	}
	
	public List<UnidadeEnsinoVO> getUnidadeEnsinoFiltrarVOs() {
		if (unidadeEnsinoFiltrarVOs == null) {
			unidadeEnsinoFiltrarVOs = new ArrayList<>(0);
		}
		return unidadeEnsinoFiltrarVOs;
	}

	public void setUnidadeEnsinoFiltrarVOs(List<UnidadeEnsinoVO> unidadeEnsinoFiltrarVOs) {
		this.unidadeEnsinoFiltrarVOs = unidadeEnsinoFiltrarVOs;
	}

	public List<CursoVO> getCursoFiltrarVOs() {
		if (cursoFiltrarVOs == null) {
			cursoFiltrarVOs = new ArrayList<>(0);
		}
		return cursoFiltrarVOs;
	}

	public void setCursoFiltrarVOs(List<CursoVO> cursoFiltrarVOs) {
		this.cursoFiltrarVOs = cursoFiltrarVOs;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}
	
	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	public ConsistirException getConsistirException() {
		if (consistirException == null) {
			consistirException = new ConsistirException();
		}
		return consistirException;
	}
	
	public void setConsistirException(ConsistirException consistirException) {
		this.consistirException = consistirException;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	
	public CargoVO getCargoPrimario() {
		if (cargoPrimario == null) {
			cargoPrimario = new CargoVO();
		}
		return cargoPrimario;
	}
	
	public void setCargoPrimario(CargoVO cargoPrimario) {
		this.cargoPrimario = cargoPrimario;
	}
	
	public CargoVO getCargoSecundario() {
		if (cargoSecundario == null) {
			cargoSecundario = new CargoVO();
		}
		return cargoSecundario;
	}
	
	public void setCargoSecundario(CargoVO cargoSecundario) {
		this.cargoSecundario = cargoSecundario;
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
	
	public FuncionarioVO getFuncionarioSecundario() {
		if (funcionarioSecundario == null) {
			funcionarioSecundario = new FuncionarioVO();
		}
		return funcionarioSecundario;
	}
	
	public void setFuncionarioSecundario(FuncionarioVO funcionarioSecundario) {
		this.funcionarioSecundario = funcionarioSecundario;
	}

	public DataModelo getControleConsultaOtimizado() {
		if (controleConsultaOtimizado == null) {
			controleConsultaOtimizado = new DataModelo();
		}
		return controleConsultaOtimizado;
	}

	public void setControleConsultaOtimizado(DataModelo controleConsultaOtimizado) {
		this.controleConsultaOtimizado = controleConsultaOtimizado;
	}

	public String getSituacaoGradeFiltrar() {
		if (situacaoGradeFiltrar == null) {
			situacaoGradeFiltrar = Constantes.EMPTY;
		}
		return situacaoGradeFiltrar;
	}

	public void setSituacaoGradeFiltrar(String situacaoGradeFiltrar) {
		this.situacaoGradeFiltrar = situacaoGradeFiltrar;
	}

	public String getSituacaoXmlFiltrar() {
		if (situacaoXmlFiltrar == null) {
			situacaoXmlFiltrar = Constantes.EMPTY;
		}
		return situacaoXmlFiltrar;
	}

	public void setSituacaoXmlFiltrar(String situacaoXmlFiltrar) {
		this.situacaoXmlFiltrar = situacaoXmlFiltrar;
	}
	
	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = Constantes.EMPTY;
		}
		return motivoRejeicao;
	}
	
	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
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
	
	public String getSituacaoDocumento() {
		if (situacaoDocumento == null) {
			situacaoDocumento = Constantes.EMPTY;
		}
		return situacaoDocumento;
	}
	
	public void setSituacaoDocumento(String situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
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
	
	public String getCodigoValidacao() {
		if (codigoValidacao == null) {
			codigoValidacao = Constantes.EMPTY;
		}
		return codigoValidacao;
	}
	
	public void setCodigoValidacao(String codigoValidacao) {
		this.codigoValidacao = codigoValidacao;
	}
	
	public Boolean getCoordenadorCursoAssinateEcpfLote() {
		if (coordenadorCursoAssinateEcpfLote == null) {
			coordenadorCursoAssinateEcpfLote = Boolean.TRUE;
		}
		return coordenadorCursoAssinateEcpfLote;
	}
	
	public void setCoordenadorCursoAssinateEcpfLote(Boolean coordenadorCursoAssinateEcpfLote) {
		this.coordenadorCursoAssinateEcpfLote = coordenadorCursoAssinateEcpfLote;
	}
	
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}
	
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public void limparDados() {
		setConsistirException(new ConsistirException());
		setMotivoRejeicao(Constantes.EMPTY);
		setListaGestaoXmlGradeCurricularErro(new ArrayList<>(0));
	}
	
	public void validarDados() throws Exception {
		if (!Uteis.isAtributoPreenchido(getCursoVO())) {
			throw new Exception("O curso deve ser informado para a geração de um currículo escolar digital.");
		}
		if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
			throw new Exception("A unidade de ensino deve ser informada para a geração de um currículo escolar digital.");
		}
		if (!Uteis.isAtributoPreenchido(getGradeCurricularVO())) {
			throw new Exception("Uma matriz curricular deve ser informada para a geração de um currículo escolar digital.");
		}
		if (!Uteis.isAtributoPreenchido(getFuncionarioPrimario())) {
			throw new Exception("Deve ser informado um funcionário assinante de E-CPF para a geração do xml de currículo escolar digital.");
		}
		if (!Uteis.isAtributoPreenchido(getCargoPrimario())) {
			throw new Exception("O cargo do funcionário E-CPF deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(getFuncionarioSecundario())) {
			throw new Exception("Deve ser informado um funcionário assinante de E-CNPJ para a geração do xml de currículo escolar digital.");
		}
		if (!Uteis.isAtributoPreenchido(getCargoSecundario())) {
			throw new Exception("O cargo do funcionário E-CNPJ deve ser informado.");
		}
	}

	public void validarDadosConsulta(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs) throws Exception {
		List<UnidadeEnsinoVO> listaUnidadeEnsino = Uteis.isAtributoPreenchido(unidadeEnsinoVOs) ? unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>();
		List<CursoVO> listaCurso = Uteis.isAtributoPreenchido(cursoVOs) ? cursoVOs.stream().filter(CursoVO::getFiltrarCursoVO).collect(Collectors.toList()) : new ArrayList<>();
		getUnidadeEnsinoFiltrarVOs().clear();
		getCursoFiltrarVOs().clear();
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			setUnidadeEnsinoFiltrarVOs(listaUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(listaCurso)) {
			setCursoFiltrarVOs(listaCurso);
		}
	}

}
