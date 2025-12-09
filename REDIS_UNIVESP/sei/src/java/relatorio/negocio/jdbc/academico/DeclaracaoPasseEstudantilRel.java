package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoPasseEstudantilVO;
import relatorio.negocio.interfaces.academico.DeclaracaoPasseEstudantilRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoPasseEstudantilRel extends SuperRelatorio implements DeclaracaoPasseEstudantilRelInterfaceFacade {

	/**
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoPasseEstudantilRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns.
	 * academico.MatriculaVO, java.lang.Integer)
	 */
	public DeclaracaoPasseEstudantilVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, String observacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		PessoaVO obj = new PessoaVO();
		obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
		List lista = new ArrayList(0);
		lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO);
		matPeriodo = (MatriculaPeriodoVO) lista.get(0);
		return montarDados(obj, matPeriodo, matricula, observacao);
	}

	/**
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoPasseEstudantilRelInterfaceFacade#montarDados(negocio.comuns.basico.PessoaVO,
	 * negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
	 */
	public DeclaracaoPasseEstudantilVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula, String observacao) throws Exception {
		DeclaracaoPasseEstudantilVO obj = new DeclaracaoPasseEstudantilVO();
		obj.setMatricula(matPeriodo.getMatricula());
		obj.setNome(pessoa.getNome());
		obj.setSexo(pessoa.getSexo_Apresentar());
		obj.setNaturalidade(pessoa.getNaturalidade().getNome());
                if (pessoa.getFiliacaoVOs().size() == 1) {
                    obj.setFiliacao(((FiliacaoVO)pessoa.getFiliacaoVOs().get(0)).getNome());
                } else if (pessoa.getFiliacaoVOs().size() == 2) {
                    obj.setFiliacao(((FiliacaoVO)pessoa.getFiliacaoVOs().get(0)).getNome());
                    obj.setFiliacao( obj.getFiliacao() + " / " + ((FiliacaoVO)pessoa.getFiliacaoVOs().get(1)).getNome());
                }
		obj.setPeriodoLetivo(matPeriodo.getPeridoLetivo().getDescricao());
		obj.setCurso(matricula.getCurso().getNome());
		obj.setTurno(matricula.getTurno().getNome());
		obj.setCpf(pessoa.getCPF());
		obj.setRg(pessoa.getRG());
		obj.setOrgaoExpedidor(pessoa.getOrgaoEmissor());
		obj.setDataExpedicao(pessoa.getDataEmissaoRG_Apresentar());
		obj.setEndereco(pessoa.getEndereco());
		obj.setBairro(pessoa.getSetor());
		obj.setCidade(pessoa.getCidade().getNome());
		obj.setCep(pessoa.getCEP());
		obj.setObservacao(observacao);
		obj.setDataNascimento(pessoa.getDataNasc_Apresentar());
		obj.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false) + ".");
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoPasseEstudantil");
	}
	
	public static void validarDados(DeclaracaoPasseEstudantilVO declaracaoPasseEstudantilVO) throws Exception {
		if (declaracaoPasseEstudantilVO.getNome().equals("")
				|| declaracaoPasseEstudantilVO.getMatricula().equals("")) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
		if(!Uteis.isAtributoPreenchido(declaracaoPasseEstudantilVO.getTextoPadrao())){
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}
	
	@Override
	public String imprimirDeclaracaoPasseEstudantil(DeclaracaoPasseEstudantilVO declaracaoPasseEstudantilVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
		textoStr = textoPadraoDeclaracao.substituirTag(textoStr, "[(250){}Observacoes_Aluno]", declaracaoPasseEstudantilVO.getObservacao(), "", 250);
		if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoStr);
		} else {
			caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
		}
		return caminhoRelatorio;

	}
	
}
