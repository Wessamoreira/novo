package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoTransferenciaRelVO;
import relatorio.negocio.interfaces.academico.DeclaracaoTransferenciaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoTransferenciaRel extends SuperRelatorio implements DeclaracaoTransferenciaRelInterfaceFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoTransferenciaRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns
	 * .academico.MatriculaVO, java.lang.Integer)
	 */
	public DeclaracaoTransferenciaRelVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List lista = new ArrayList(0);
		PessoaVO obj = new PessoaVO();
		MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
		
		obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		matPeriodo = (MatriculaPeriodoVO) lista.get(0);
		return montarDados(obj, matPeriodo, matricula);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoTransferenciaRelInterfaceFacade#montarDados(negocio.comuns.basico.
	 * PessoaVO, negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
	 */
	public DeclaracaoTransferenciaRelVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception {
		DeclaracaoTransferenciaRelVO obj = new DeclaracaoTransferenciaRelVO();
		obj.setMatricula(matPeriodo.getMatricula());
		obj.setNome(pessoa.getNome());
		obj.setCpf(pessoa.getCPF());
		obj.setCurso(matricula.getCurso().getNome());
		obj.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		obj.setDataNasc(pessoa.getDataNasc_Apresentar());
		obj.setNaturalidade(pessoa.getNaturalidade().getNome());
		obj.setNomeDiretor("");
		obj.setPeriodoLetivo(matPeriodo.getPeridoLetivo().getDescricao());
		obj.setRg(pessoa.getRG() + " (" + pessoa.getOrgaoEmissor() 
				+  (pessoa.getEstadoEmissaoRG_Apresentar().equals("") ? "" : "-" ) + pessoa.getEstadoEmissaoRG_Apresentar() + ")");
		obj.setUf(pessoa.getNaturalidade().getEstado().getSigla());
		obj.setUnidadeEnsino(matricula.getUnidadeEnsino().getNome());
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioGuia() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeGuia() + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoTransferenciaRel");
	}

	public static String getIdEntidadeGuia() {
		return ("GuiaTransferenciaRel");
	}
	
	public static void validarDados(DeclaracaoTransferenciaRelVO declara) throws Exception {
		if (declara.getMatricula().equals("") || declara.getNome().equals("")) {
			throw new Exception("A matricula deve ser informada para geração do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(declara.getTextoPadraoDeclaracao())) {
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}
	
	@Override
	public String imprimirDeclaracaoTransferencia(DeclaracaoTransferenciaRelVO declaracaoTransferenciaRelVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		try {
			String caminhoRelatorio = "";
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
			if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoStr);
			} else {
				caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
			}
			return caminhoRelatorio;
		} catch (Exception e) {
			throw e;
		}
    }
	
}
