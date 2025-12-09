package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.IdentificacaoEstudantilVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;

public interface IdentificacaoEstudantilInterfaceFacade {

    public String designIReportRelatorioLayout1();

    public String designIReportRelatorioLayout2();

    public String designIReportRelatorioLayout3();
    
    String caminhoBaseRelatorio();

	List<IdentificacaoEstudantilVO> consultarDadosIdentificacaoEstudantilAluno(Integer unidadeEnsino, Integer requerimento, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, Boolean apenasRequerimentoPago, String matricula, Boolean utilizarFotoPerfilAluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	List<IdentificacaoEstudantilVO> consultarDadosIdentificacaoEstudantilProfessor(Integer unidadeEnsino,Integer curso, String ano, String semestre, String matricula,Boolean utilizarFotoAluno,UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	void realizarMotagemIdentificacaoPDF(LayoutEtiquetaVO layoutEtiqueta, List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs ,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminho) throws Exception;
}
