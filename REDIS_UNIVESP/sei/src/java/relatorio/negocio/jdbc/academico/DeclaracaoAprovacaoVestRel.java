package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.Date;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoAprovacaoVestVO;
import relatorio.negocio.interfaces.academico.DeclaracaoAprovacaoVestRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoAprovacaoVestRel extends SuperRelatorio implements DeclaracaoAprovacaoVestRelInterfaceFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoAprovacaoVestRelInterfaceFacade#consultarPorInscricaoCandidato(negocio
	 * .comuns.processosel.InscricaoVO, java.lang.Integer)
	 */
	public DeclaracaoAprovacaoVestVO consultarPorInscricaoCandidato(InscricaoVO inscricao, Integer nivelMontarDados) throws Exception {
		PessoaVO obj = new PessoaVO();
		obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(inscricao.getCandidato().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		return montarDados(obj, inscricao);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DeclaracaoAprovacaoVestRelInterfaceFacade#montarDados(negocio.comuns.basico.
	 * PessoaVO, negocio.comuns.processosel.InscricaoVO)
	 */
	public DeclaracaoAprovacaoVestVO montarDados(PessoaVO pessoa, InscricaoVO inscricao) throws Exception {
		DeclaracaoAprovacaoVestVO obj = new DeclaracaoAprovacaoVestVO();
		obj.setNome(pessoa.getNome());
		obj.setRg(pessoa.getRG());
		obj.setCpf(pessoa.getCPF());
		obj.setAnoMat(String.valueOf(Uteis.getAnoData(inscricao.getData())));
		obj.setCurso(inscricao.getCursoOpcao1().getCurso().getNome());
		obj.setInscricao(inscricao.getCodigo());
		obj.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(inscricao.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("DeclaracaoAprovacaoVestRel");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}
	
	public static void validarDados(DeclaracaoAprovacaoVestVO vo) throws Exception{
		if (vo.getInscricao() == null || vo.getInscricao().intValue() == 0
				|| vo.getNome().equals("")) {
			throw new Exception("A inscrição deve ser informada para criação do relatório.");
		}
	}
}
