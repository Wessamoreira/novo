/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package negocio.interfaces.administrativo;

import java.util.List;
import java.util.Map;

import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.ModuloTemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * 
 * @author Mauro
 */
public interface PersonalizacaoMensagemAutomaticaInterfaceFacade {

    public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void alterar(PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuarioVO) throws Exception;

    void incluir(PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuarioVO) throws Exception;

    void executarGeracaoMensagemPadrao(Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<PersonalizacaoMensagemAutomaticaVO> consultarPorParamentrosTemplate(String assunto, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, ModuloTemplateMensagemAutomaticaEnum moduloTemplateMensagemAutomaticaEnum, String nomeCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	PersonalizacaoMensagemAutomaticaVO executarGeracaoMensagemPadraoTemplateEspecifico(
			TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, Boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
	
	PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados,	Integer unidadeEnsino, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;
	
    public PersonalizacaoMensagemAutomaticaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
	
	void inicializarDadosUnidadeEnsinoSelecionadaEdicao(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO);

	Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, List<Integer> unidadeEnsinos, UsuarioVO usuarioVO) throws Exception;

	PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(
			TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso,
			Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	
	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, Integer codigoCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void excluirPorCursoTampleteMensagemAutomatica(PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuario) throws Exception;

	PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO) throws Exception;

	public PersonalizacaoMensagemAutomaticaVO consultarMensagemAutomaticaDeferimentoRequerimento(Integer codigoTipoRequerimento, UsuarioVO usuario) throws Exception;

	public PersonalizacaoMensagemAutomaticaVO consultarMensagemAutomaticaIndeferimentoRequerimento(Integer codigoTipoRequerimento, UsuarioVO usuario) throws Exception;

	public void persistir(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomatica, UsuarioVO usuario) throws Exception;

	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplatePorTipoRequerimento(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, Integer tipoRequerimento, UsuarioVO usuarioVO) throws Exception;

	public void excluirPorTipoRequerimento(Integer tipoRequerimento, UsuarioVO usuario); 
}
