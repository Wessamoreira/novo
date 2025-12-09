package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoBibliotecaEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Emprestimo
	 *
	 */
	EMPRESTIMO("Emprestimo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Emprestimo_titulo"),
					UteisJSF.internacionalizar("per_Emprestimo_ajuda"), new String[] { "homeBibliotecaExterna.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Emprestimo_titulo"),
					UteisJSF.internacionalizar("per_Emprestimo_ajuda"), new String[] { "homeBibliotecaExterna.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_BibliotecaVisaoAluno_titulo"),
					UteisJSF.internacionalizar("per_BibliotecaVisaoAluno_ajuda"),
					new String[] { "emprestimosAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Emprestimo_titulo"),
					UteisJSF.internacionalizar("per_Emprestimo_ajuda"), new String[] { "emprestimosAluno.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Emprestimo_titulo"),
					UteisJSF.internacionalizar("per_Emprestimo_ajuda"),
					new String[] { "emprestimoCons.xhtml", "emprestimoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_EMPRESTIMO_DEVOLUCAO),
	PERMITE_ISENTAR_MULTA_BIBLIOTECA("PermiteIsentarMultaBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteIsentarMultaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermiteIsentarMultaBiblioteca_ajuda"),
					new String[] { "compraCons.xhtml", "compraForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoBibliotecaEnum.EMPRESTIMO,
			PerfilAcessoSubModuloEnum.BIBLIOTECA_EMPRESTIMO_DEVOLUCAO),
	/**
	 * Emprestimo Rel
	 *
	 */
	EMPRESTIMO_REL("EmprestimoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_EmprestimoRel_titulo"),
			UteisJSF.internacionalizar("per_EmprestimoRel_ajuda"), new String[] { "emprestimoBibliotecaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Reserva Catalogo Rel
	 *
	 */
	RESERVA_CATALOGO_REL("ReservaCatalogoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ReservaCatalogoRel_titulo"),
			UteisJSF.internacionalizar("per_ReservaCatalogoRel_ajuda"), new String[] { "reservaCatalogoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Acervo Rel
	 *
	 */
	ACERVO_REL("AcervoRel",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AcervoRel_titulo"),
							UteisJSF.internacionalizar("per_AcervoRel_ajuda"), new String[] { "acervoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Exemplares Por Curso Rel
	 *
	 */
	EXEMPLARES_POR_CURSO_REL("ExemplaresPorCursoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExemplaresPorCursoRel_titulo"),
					UteisJSF.internacionalizar("per_ExemplaresPorCursoRel_ajuda"),
					new String[] { "exemplaresPorCursoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Exemplares Rel
	 *
	 */
	EXEMPLARES_REL("ExemplaresRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExemplaresRel_titulo"),
					UteisJSF.internacionalizar("per_ExemplaresRel_ajuda"), new String[] { "exemplaresRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Situacao Exemplares Rel
	 *
	 */
	SITUACAO_EXEMPLARES_REL("SituacaoExemplaresRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SituacaoExemplaresRel_titulo"),
					UteisJSF.internacionalizar("per_SituacaoExemplaresRel_ajuda"),
					new String[] { "situacaoExemplaresRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Etiqueta Livro Rel
	 *
	 */
	ETIQUETA_LIVRO_REL("EtiquetaLivroRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_EtiquetaLivroRel_titulo"),
			UteisJSF.internacionalizar("per_EtiquetaLivroRel_ajuda"), new String[] { "etiquetaLivroRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_RELATORIOS_BIBLIOTECA),
	/**
	 * Busca Catalogo
	 *
	 */
	BUSCA_CATALOGO("BuscaCatalogo", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_BuscaCatalogo_titulo"),
			UteisJSF.internacionalizar("per_BuscaCatalogo_ajuda"), new String[] { "homeBibliotecaExterna.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_CONSULTA_ACERVO),

	/**
	 * Minha Biblioteca
	 *
	 */
	MINHA_BIBLIOTECA("MinhaBiblioteca", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MinhaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_MinhaBiblioteca_ajuda"), new String[] {}),
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_MinhaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_MinhaBiblioteca_ajuda"), new String[] {}),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_MinhaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_MinhaBiblioteca_ajuda"), new String[] {}),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_MinhaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_MinhaBiblioteca_ajuda"), new String[] {}) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_CONSULTA_ACERVO),

	BIBLIOTECA_LEXMAGISTER("BibliotecaLexMagister",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BibliotecaLexMagister_titulo"),
					UteisJSF.internacionalizar("per_BibliotecaLexMagister_ajuda"), new String[] {}),
					new PermissaoVisao(
							TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_BibliotecaLexMagister_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaLexMagister_ajuda"), new String[] {}),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_BibliotecaLexMagister_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaLexMagister_ajuda"), new String[] {}),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_BibliotecaLexMagister_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaLexMagister_ajuda"), new String[] {}) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_CONSULTA_ACERVO),

	BIBLIOTECA_BV_PEARSON("BibliotecaBvPearson",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BibliotecaBvPearson_titulo"),
					UteisJSF.internacionalizar("per_BibliotecaBvPearson_ajuda"), new String[] {}),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_ajuda"), new String[] {}),
					new PermissaoVisao(TipoVisaoEnum.PROFESSOR,
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_ajuda"), new String[] {}),
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_titulo"),
							UteisJSF.internacionalizar("per_BibliotecaBvPearson_ajuda"), new String[] {}) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_CONSULTA_ACERVO),

	/**
	 * Assinatura Periodico
	 *
	 */
	ASSINATURA_PERIODICO("AssinaturaPeriodico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AssinaturaPeriodico_titulo"),
					UteisJSF.internacionalizar("per_AssinaturaPeriodico_ajuda"),
					new String[] { "assinaturaPeriodicoCons.xhtml", "assinaturaPeriodicoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	PERIODICO_PERMITE_TRANSFRENCIA_EXEMPLAR_ENTRE_BIBLIOTECAS("Periodico_PermiteTransfrenciaExemplarEntreBibliotecas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PeriodicoPermiteTransfrenciaExemplarEntreBibliotecas_titulo"),
					UteisJSF.internacionalizar("per_PeriodicoPermiteTransfrenciaExemplarEntreBibliotecas_ajuda"),
					new String[] { "assinaturaPeriodicoCons.xhtml", "assinaturaPeriodicoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoBibliotecaEnum.ASSINATURA_PERIODICO,
			PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	PERMITE_ACESSAR_PERIODICO_OUTRAS_UNIDADES("Catalogo_AcessarPeriodicosOutrasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AcessarPeriodicoOutrasUnidades_titulo"),
					UteisJSF.internacionalizar("per_AcessarPeriodicoOutrasUnidades_ajuda"),
					new String[] { "catalogoCons.xhtml", "catalogoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoBibliotecaEnum.ASSINATURA_PERIODICO,
			PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	/**
	 * Catalogo
	 *
	 */
	CATALOGO("Catalogo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Catalogo_titulo"), UteisJSF.internacionalizar("per_Catalogo_ajuda"),
					new String[] { "catalogoCons.xhtml", "catalogoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	CATALOGO_PERMITE_TRANSFRENCIA_EXEMPLAR_ENTRE_BIBLIOTECAS("Catalogo_PermiteTransfrenciaExemplarEntreBibliotecas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CatalogoPermiteTransfrenciaExemplarEntreBibliotecas_titulo"),
					UteisJSF.internacionalizar("per_CatalogoPermiteTransfrenciaExemplarEntreBibliotecas_ajuda"),
					new String[] { "catalogoCons.xhtml", "catalogoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoBibliotecaEnum.CATALOGO,
			PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	PERMITE_ACESSAR_CATALOGO_OUTRAS_UNIDADES("Catalogo_AcessarCatalogoOutrasUnidades",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AcessarCatalogoOutrasUnidades_titulo"),
					UteisJSF.internacionalizar("per_AcessarCatalogoOutrasUnidades_ajuda"),
					new String[] { "catalogoCons.xhtml", "catalogoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoBibliotecaEnum.CATALOGO,
			PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	/**
	 * Autor
	 *
	 */
	AUTOR("Autor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Autor_titulo"), UteisJSF.internacionalizar("per_Autor_ajuda"),
					new String[] { "autorCons.xhtml", "autorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Classificacao Bibliografica
	 *
	 */
	CLASSIFICACAO_BIBLIOGRAFICA("ClassificacaoBibliografica",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ClassificacaoBibliografica_titulo"),
					UteisJSF.internacionalizar("per_ClassificacaoBibliografica_ajuda"),
					new String[] { "classificacaoBibliograficaCons.xhtml", "classificacaoBibliograficaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Registro Entrada Acervo
	 *
	 */
	REGISTRO_ENTRADA_ACERVO("RegistroEntradaAcervo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroEntradaAcervo_titulo"),
					UteisJSF.internacionalizar("per_RegistroEntradaAcervo_ajuda"),
					new String[] { "registroEntradaAcervoCons.xhtml", "registroEntradaAcervoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Tipo Catalogo
	 *
	 */
	TIPO_CATALOGO("TipoCatalogo", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_TipoCatalogo_titulo"), UteisJSF.internacionalizar("per_TipoCatalogo_ajuda"),
			new String[] { "tipoCatalogoCons.xhtml", "tipoCatalogoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Registro Saida Acervo
	 *
	 */
	REGISTRO_SAIDA_ACERVO("RegistroSaidaAcervo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroSaidaAcervo_titulo"),
					UteisJSF.internacionalizar("per_RegistroSaidaAcervo_ajuda"),
					new String[] { "registroSaidaAcervoCons.xhtml", "registroSaidaAcervoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Configuracao Biblioteca
	 *
	 */
	CONFIGURACAO_BIBLIOTECA("ConfiguracaoBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoBiblioteca_ajuda"),
					new String[] { "configuracaoBibliotecaCons.xhtml", "configuracaoBibliotecaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Secao
	 *
	 */
	SECAO("Secao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Secao_titulo"), UteisJSF.internacionalizar("per_Secao_ajuda"),
					new String[] { "secaoCons.xhtml", "secaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Editora
	 *
	 */
	EDITORA("Editora",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Editora_titulo"), UteisJSF.internacionalizar("per_Editora_ajuda"),
					new String[] { "editoraCons.xhtml", "editoraForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Editora
	 *
	 */
	MOTIVO_EMPRESTIMO_POR_HORA("MotivoEmprestimoPorHora",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MotivoEmprestimoPorHora_titulo"),
					UteisJSF.internacionalizar("per_MotivoEmprestimoPorHora_ajuda"),
					new String[] { "motivoEmprestimoPorHoraCons.xhtml", "motivoEmprestimoPorHoraForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),
	/**
	 * Bloqueio Biblioteca
	 *
	 */
	BLOQUEIO_BIBLIOTECA("BloqueioBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BloqueioBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_BloqueioBiblioteca_ajuda"),
					new String[] { "bloqueioBibliotecaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_OPCOES),
	/**
	 * Biblioteca
	 *
	 */
	BIBLIOTECA("Biblioteca", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Biblioteca_titulo"),
					UteisJSF.internacionalizar("per_Biblioteca_ajuda"), new String[] { "homeBibliotecaExterna.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Biblioteca_titulo"),
					UteisJSF.internacionalizar("per_Biblioteca_ajuda"), new String[] { "homeBibliotecaExterna.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Biblioteca_titulo"),
					UteisJSF.internacionalizar("per_Biblioteca_ajuda"),
					new String[] { "bibliotecaCons.xhtml", "bibliotecaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA),

	/**
	 * Biblioteca
	 *
	 */
	PAINEL_GESTOR_EMPRESTIMO_BIBLIOTECA("PainelGestorEmprestimoBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorEmprestimoBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorEmprestimoBiblioteca_ajuda"),
					new String[] { "painelEmprestimos.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.BIBLIOTECA_BIBLIOTECA);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoBibliotecaEnum(String valor, PermissaoVisao[] permissaoVisao,
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.valor = valor;
		this.permissaoVisao = permissaoVisao;
		this.tipoPerfilAcesso = tipoPerfilAcesso;
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	private String valor;
	private PermissaoVisao[] permissaoVisao;
	private TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso;
	private PerfilAcessoSubModuloEnum perfilAcessoSubModulo;
	private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum;

	/**
	 * @return the tipoPerfilAcesso
	 */
	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso() {
		if (tipoPerfilAcesso == null) {
			tipoPerfilAcesso = TipoPerfilAcessoPermissaoEnum.ENTIDADE;
		}
		return tipoPerfilAcesso;
	}

	/**
	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
	 */
	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) {
		this.tipoPerfilAcesso = tipoPerfilAcesso;
	}

	/**
	 * @return the perfilAcessoSubModulo
	 */
	public PerfilAcessoSubModuloEnum getPerfilAcessoSubModulo() {
		if (perfilAcessoSubModulo == null) {
			perfilAcessoSubModulo = PerfilAcessoSubModuloEnum.TODOS;
		}
		return perfilAcessoSubModulo;
	}

	/**
	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
	 */
	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the permissaoSuperiorEnum
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() {
		return permissaoSuperiorEnum;
	}

	/**
	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
	 */
	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) {
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
	}

	/**
	 * @return the PermissaoVisao
	 */
	public PermissaoVisao[] getPermissaoVisao() {
		if (permissaoVisao == null) {
			permissaoVisao = new PermissaoVisao[0];
		}
		return permissaoVisao;
	}

	/**
	 * @param PermissaoVisao the PermissaoVisao to set
	 */
	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) {
		this.permissaoVisao = PermissaoVisao;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) {
		for (PermissaoVisao permissaoVisao : getPermissaoVisao()) {
			if (permissaoVisao.equals(tipoVisaoEnum)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
		}
		return null;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			for (PermissaoVisao permissaoVisao2 : getPermissaoVisao()) {
				if (permissaoVisao2.equals(tipoVisaoEnum)) {
					return permissaoVisao2;
				}
			}
		}
		return null;
	}

	public Boolean getIsApresentarApenasPermissaoTotal() {
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
				&& getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
	}

	public String descricaoModulo;

	@Override
	public String getDescricaoModulo() {
		if (descricaoModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();
			} else {
				descricaoModulo = "";
			}
		}
		return descricaoModulo;
	}

	public String descricaoSubModulo;

	@Override
	public String getDescricaoSubModulo() {
		if (descricaoSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();

			} else {
				descricaoSubModulo = "";
			}
		}
		return descricaoSubModulo;
	}

	public String descricaoModuloSubModulo;

	@Override
	public String getDescricaoModuloSubModulo() {
		if (descricaoModuloSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()
						+ " - " + getPerfilAcessoSubModulo().getDescricao();
			} else {
				descricaoModuloSubModulo = "";
			}
		}
		return descricaoModuloSubModulo;
	}

}
