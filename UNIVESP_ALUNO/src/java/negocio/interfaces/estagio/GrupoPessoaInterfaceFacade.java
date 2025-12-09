package negocio.interfaces.estagio;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.primefaces.event.FileUploadEvent;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;

public interface GrupoPessoaInterfaceFacade {

	void persistir(GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(GrupoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, GrupoPessoaVO obj) throws Exception;

	GrupoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void adicionarGrupoPessoaItemVO(GrupoPessoaVO obj, GrupoPessoaItemVO gpi, UsuarioVO usuario);

	void removerGrupoPessoaItemVO(GrupoPessoaVO obj, GrupoPessoaItemVO gpi, UsuarioVO usuario);

	File realizarExportacaoLayouGrupoPessoa(GrupoPessoaVO obj,  UsuarioVO usuario) throws Exception;
	
	void upLoadArquivoImportado(InputStream uploadEvent, List<GrupoPessoaVO> listaGrupoPessoa, UsuarioVO usuario) throws Exception;

	void realizarImportacaoGrupoPessoa(List<GrupoPessoaVO> listaGrupoPessoa, UsuarioVO usuario) throws Exception;

	List<GrupoPessoaVO> consultaGrupoPessoaCombobox(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	GrupoPessoaVO consultarPorCurso(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<GrupoPessoaVO> consultarGrupoPessoaAtivoAgrupandoFacilitadorQtdeEstagiosParaRedistribuicaoFacilitadoresPorSituacaoEstagio( List<String> situacoesEstagio,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarRedistribuicaoGrupoFacilitadores(GrupoPessoaVO objGrupoPessoaVO, boolean controlarAcesso, UsuarioVO usuarioLogado);

}
