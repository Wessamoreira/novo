package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ParceiroUnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ParceiroInterfaceFacade {
	

    public ParceiroVO novo() throws Exception;
    void persistir(ParceiroVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception;
    public void excluir(ParceiroVO obj, UsuarioVO usuario) throws Exception;
    public ParceiroVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorNomeDuploPercent(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorRazaoSocial(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorRG(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCPF(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public ParceiroVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public ParceiroVO consultarPorCNPJUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Boolean consultarExisteVagaAtivaCadastrada(Integer codigoParceiro, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCNPJ(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorTipoParceiro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorRGBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCPFBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCodigoBancoCurriculumTrue(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorNomeBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorCNPJBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarPorRazaoSocialBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public Integer consultarQuantidadeParceiroCadastrados() throws Exception;
    public String consultarDataUltimoCadastro() throws Exception;
    public void unificarParceiro(final Integer codParceiroManter, final Integer codParceiroRemover, UsuarioVO usuario) throws Exception;
    public ParceiroVO consultarPorMatriculaAluno(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Integer consultarQuantidadeParceiroInativados() throws Exception;
    public List<ParceiroVO> consultarParceiroInativoBancoCurriculumFalse(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ParceiroVO> consultarParceiroBancoCurriculumTrueComVaga(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    Boolean validarSeParceiroUtilizarFinanciamentoProprioPorCodigoConvenio(Integer convenio) throws Exception;
    Boolean realizarVerificacaoDebitoFinanceiroAoIncluirConvenioMatricula(Integer parceiro) throws Exception;
    void atualizarValorIsentarJuroMulta(ParceiroVO parceiro, UsuarioVO usuario) throws Exception;
	void adicionarParceiroUnidadeEnsinoContaCorrenteVO(ParceiroVO parceiro, ParceiroUnidadeEnsinoContaCorrenteVO puecc, UsuarioVO usuario) throws Exception;
	void removerParceiroUnidadeEnsinoContaCorrenteVO(ParceiroVO parceiro, ParceiroUnidadeEnsinoContaCorrenteVO puecc) throws Exception;
	public List<ParceiroVO> consultarPorTipoSindicato(String valorConsultaParceiro, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuarioLogado) throws Exception;
		
}