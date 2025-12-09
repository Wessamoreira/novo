package relatorio.negocio.interfaces.academico;

 
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.AniversariantesDoMesRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AniversariantesDoMesRelInterfaceFacade {

	public List<AniversariantesDoMesRelVO> criarObjeto(Integer codUnidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario, boolean aluno, boolean funcionario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

	public String getDesignIReportRelatorio();

	public String getCaminhoBaseRelatorio();
	
	public Integer consultarNumeroDestinatario (Integer codUnidade, TurmaVO turmaVO, String mes, String dia, String diaFim, UsuarioVO usuario, boolean aluno, boolean funcionario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

	public PersonalizacaoMensagemAutomaticaVO carregarDadosMensagemPersonalizada(UsuarioVO usuarioVO) throws Exception;
	
	public String realizarSubstituicaoTagMensagem(AniversariantesDoMesRelVO aniversariantesDoMesRelVO, final String mensagemTemplate);
    
	public String realizarSubstituicaoTagMensagemSms(AniversariantesDoMesRelVO aniversariantesDoMesRelVO, final String mensagemTemplate);
	
	public void executarEnvioComunicadoInternoAniversariante(List<AniversariantesDoMesRelVO> listaObjetos,ComunicacaoInternaVO comunicacaoInternaVO,UsuarioVO usuarioVO,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,Boolean enviarSms,Boolean enviarEmail ) throws Exception;
	
}