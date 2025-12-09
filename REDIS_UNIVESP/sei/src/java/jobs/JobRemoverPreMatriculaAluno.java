package jobs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobRemoverPreMatriculaAluno extends SuperFacadeJDBC implements Runnable, Serializable {

	private static final long serialVersionUID = 1L;

	public void run() {
		try {
//			System.out.print("Iniciou Processamento EXCLUSÃO PRÉ-MATRÍCULA");
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
//			System.out.print("Usuário Responsável: " + usuarioVO.toString());
//				System.out.println("Quantidade de dias para remover Pré-Matrícula: " + configuracaoGeralSistemaVO.getDiasParaRemoverPreMatricula());
				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, null);
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
//				System.out.println("Configuração Financeira: " + configuracaoFinanceiroVO.getCodigo());
				try {
//					System.out.print("Realizando consulta de Pré-Matrículas a serem excluídas");
					List<MatriculaPeriodoVO> matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaPreMatriculaParaExclusao();
//					System.out.print("Total de Pré-Matrículas a serem excluídas: " + matriculaPeriodoVOs.size());
					for (Iterator<MatriculaPeriodoVO> iterator = matriculaPeriodoVOs.iterator(); iterator.hasNext();) {
						MatriculaPeriodoVO matriculaPeriodoVO = iterator.next();
						try {
							getFacadeFactory().getMatriculaFacade().excluirPreMatriculaERegistrosRelacionados(matriculaPeriodoVO, "REMOÇÃO AUTOMÁTICA (JOB) => Pré-Matrícula não confirmada!", configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
//							System.out.println("Pré-Matrícula excluída: " + matriculaPeriodoVO.getCodigo() + " Matrícula: " + matriculaPeriodoVO.getMatricula() + " Aluno: " + matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
						} catch (Exception e) {
							System.out.println("Problema excluir Pré-Matrícula:" + matriculaPeriodoVO.getCodigo() + "Erro:" + e.getMessage());
						}
					}
				} catch (Exception e) {
					System.out.print("Erro REMOVER PRE-MATRICULA (2) => " + e.getMessage());
				}
		} catch (Exception e) {
			System.out.print("Erro REMOVER PRE-MATRICULA (3) => " + e.getMessage());
		}
	}
}
