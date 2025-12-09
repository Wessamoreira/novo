package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import relatorio.negocio.comuns.academico.ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO;

public interface ProfessorAnaliticoRelInterfaceFacade {
   
    public List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO> criarObjeto(
            AreaConhecimentoVO areaConhecimentoVO, DisciplinaVO disciplinaInteresseVO, String situacaoProfessor, 
            String escolaridade, PessoaVO professorVO, UsuarioVO usuarioVO, String ordemRelatorio, Boolean imprimirDadosComplementares) throws Exception;
}
