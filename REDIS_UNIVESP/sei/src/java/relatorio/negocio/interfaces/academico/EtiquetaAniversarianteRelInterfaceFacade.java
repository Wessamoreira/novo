package relatorio.negocio.interfaces.academico;

 
 
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
 

import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EtiquetaAniversarianteRelInterfaceFacade {

	public String realizarImpressaoEtiqueta(Integer unidadeEnsino,TurmaVO turmaVO,String mes, String dia, String diaFim, boolean aluno, boolean funcionario,LayoutEtiquetaVO layoutEtiqueta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;
	

}
