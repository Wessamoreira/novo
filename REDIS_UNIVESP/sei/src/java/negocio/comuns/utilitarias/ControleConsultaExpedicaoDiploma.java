package negocio.comuns.utilitarias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoEnum;
import negocio.comuns.academico.enumeradores.TipoHorarioEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.diplomaDigital.versao1_05.TMotivoAnulacao;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * 
 * @author Felipi Alves
 *
 */

/**
 * 
 * <div> ao realizar a chamada da consulta SEMPRE verificar os nomes fields para
 * adicionar na consulta </div>
 *
 */

@SuppressWarnings("serial")
public class ControleConsultaExpedicaoDiploma extends SuperControleRelatorio implements Serializable {

	public ControleConsultaExpedicaoDiploma() {
		setTipoData("dataRegistro");
		getControleConsultaOtimizado().setLimitePorPagina(10);
	}

	/**
	 * fields de consulta
	 */
	public Object expedicaoDiploma_codigo;
	public Object expedicaoDiploma_gradecurricular;
	public Object expedicaoDiploma_funcionarioprimario;
	public Object expedicaoDiploma_funcionariosecundario;
	public Object expedicaoDiploma_unidadeensinocertificadora;
	public Object expedicaoDiploma_funcionarioterceiro;
	public Object expedicaoDiploma_cargofuncionarioprincipal;
	public Object expedicaoDiploma_cargofuncionariosecundario;
	public Object expedicaoDiploma_cargofuncionarioterceiro;
	public Object expedicaoDiploma_reitorregistrodiplomaviaanterior;
	public Object expedicaoDiploma_secretariaregistrodiplomaviaanterior;
	public Object expedicaoDiploma_cargoreitorregistrodiplomaviaanterior;
	public Object expedicaoDiploma_nrlivroregistradora;
	public Object expedicaoDiploma_nrfolhareciboregistradora;
	public Object expedicaoDiploma_numeroregistroregistradora;
	public Object expedicaoDiploma_funcionarioquarto;
	public Object expedicaoDiploma_funcionarioquinto;
	public Object expedicaoDiploma_cargofuncionarioquarto;
	public Object expedicaoDiploma_cargofuncionarioquinto;
	public Object expedicaoDiploma_textopadrao;
	public Object expedicaoDiploma_codigomecpta;
	public Object expedicaoDiploma_cidadepta;
	public Object expedicaoDiploma_secaopublicacaodescredenciamentopta;
	public Object expedicaoDiploma_paginapublicacaodescredenciamentopta;
	public Object expedicaoDiploma_numerodoudescredenciamentopta;
	public Object expedicaoDiploma_responsavelcadastro;
	public Object expedicaoDiploma_utilizarunidadematriz;
	public Object expedicaoDiploma_informarcamposlivroregistradora;
	public Object expedicaoDiploma_gerarxmldiploma;
	public Object expedicaoDiploma_anulado;
	public Object expedicaoDiploma_emitidoporprocessotransferenciaassistida;
	public Object expedicaoDiploma_emitidopordecisaojudicial;
	public Object expedicaoDiploma_matricula;
	public Object expedicaoDiploma_via;
	public Object expedicaoDiploma_numeroprocessoviaanterior;
	public Object expedicaoDiploma_numeroregistrodiplomaviaanterior;
	public Object expedicaoDiploma_layoutdiploma;
	public Object expedicaoDiploma_titulofuncionarioprincipal;
	public Object expedicaoDiploma_titulofuncionariosecundario;
	public Object expedicaoDiploma_titulofuncionarioterceiro;
	public Object expedicaoDiploma_numeroprocesso;
	public Object expedicaoDiploma_titulacaomasculinoapresentardiploma;
	public Object expedicaoDiploma_titulacaofemininoapresentardiploma;
	public Object expedicaoDiploma_serial;
	public Object expedicaoDiploma_observacao;
	public Object expedicaoDiploma_titulofuncionarioquarto;
	public Object expedicaoDiploma_titulofuncionarioquinto;
	public Object expedicaoDiploma_numeroregistrodiploma;
	public Object expedicaoDiploma_codigovalidacaodiplomadigital;
	public Object expedicaoDiploma_nomeiespta;
	public Object expedicaoDiploma_cnpjpta;
	public Object expedicaoDiploma_ceppta;
	public Object expedicaoDiploma_logradouropta;
	public Object expedicaoDiploma_numeropta;
	public Object expedicaoDiploma_complementopta;
	public Object expedicaoDiploma_bairropta;
	public Object expedicaoDiploma_tipodescredenciamentopta;
	public Object expedicaoDiploma_numerodescredenciamentopta;
	public Object expedicaoDiploma_veiculopublicacaodescredenciamentopta;
	public Object expedicaoDiploma_nomejuizdecisaojudicial;
	public Object expedicaoDiploma_numeroprocessodecisaojudicial;
	public Object expedicaoDiploma_decisaojudicial;
	public Object expedicaoDiploma_informacoesadicionaisdecisaojudicial;
	public Object expedicaoDiploma_versaodiploma;
	public Object expedicaoDiploma_motivoanulacao;
	public Object expedicaoDiploma_dataexpedicao;
	public Object expedicaoDiploma_dataregistrodiplomaviaanterior;
	public Object expedicaoDiploma_datapublicacaodiariooficial;
	public Object expedicaoDiploma_dataregistrodiploma;
	public Object expedicaoDiploma_datadescredenciamentopta;
	public Object expedicaoDiploma_datapublicacaodescredenciamentopta;
	public Object expedicaoDiploma_dataanulacao;
	public Object expedicaoDiploma_datacadastro;
	public Object expedicaoDiploma_anotacaoanulacao;
	public Object expedicaoDiploma_responsavelanulacao;
	/**
	 * textopadrao_
	 */
	public Object textopadrao_codigo;
	public Object textopadrao_descricao;
	/**
	 * reitorregistrodiplomaviaanterior_
	 */
	public Object reitorregistrodiplomaviaanterior_codigo;
	public Object reitorregistrodiplomaviaanterior_matricula;
	public Object reitorregistrodiplomaviaanterior_pessoa;
	public Object reitorregistrodiplomaviaanterior_escolaridade;
	/**
	 * reitorregistrodiplomaviaanterior_pessoa
	 */
	public Object reitorregistrodiplomaviaanterior_pessoa_codigo;
	public Object reitorregistrodiplomaviaanterior_pessoa_cpf;
	public Object reitorregistrodiplomaviaanterior_pessoa_nome;
	/**
	 * cargoreitorregistrodiplomaviaanterior_
	 */
	public Object cargoreitorregistrodiplomaviaanterior_codigo;
	public Object cargoreitorregistrodiplomaviaanterior_nome;
	/**
	 * secretariaregistrodiplomaviaanterior_
	 */
	public Object secretariaregistrodiplomaviaanterior_codigo;
	public Object secretariaregistrodiplomaviaanterior_matricula;
	public Object secretariaregistrodiplomaviaanterior_pessoa;
	public Object secretariaregistrodiplomaviaanterior_escolaridade;
	/**
	 * reitorregistrodiplomaviaanterior_pessoa
	 */
	public Object secretariaregistrodiplomaviaanterior_pessoa_codigo;
	public Object secretariaregistrodiplomaviaanterior_pessoa_cpf;
	public Object secretariaregistrodiplomaviaanterior_pessoa_nome;
	/**
	 * unidadecertificadora_
	 */
	public Object unidadecertificadora_codigo;
	public Object unidadecertificadora_nome;
	public Object unidadecertificadora_cnpj;
	/**
	 * funcionarioprimario_
	 */
	public Object funcionarioprimario_matricula;
	public Object funcionarioprimario_pessoa;
	public Object funcionarioprimario_codigo;
	public Object funcionarioprimario_escolaridade;
	public Object funcionarioprimario_arquivoassinatura;
	/**
	 * funcionariosecundario_
	 */
	public Object funcionariosecundario_matricula;
	public Object funcionariosecundario_pessoa;
	public Object funcionariosecundario_codigo;
	public Object funcionariosecundario_escolaridade;
	public Object funcionariosecundario_arquivoassinatura;
	/**
	 * funcionarioterceiro_
	 */
	public Object funcionarioterceiro_matricula;
	public Object funcionarioterceiro_pessoa;
	public Object funcionarioterceiro_codigo;
	public Object funcionarioterceiro_escolaridade;
	public Object funcionarioterceiro_arquivoassinatura;
	/**
	 * funcionarioquarto_
	 */
	public Object funcionarioquarto_matricula;
	public Object funcionarioquarto_pessoa;
	public Object funcionarioquarto_codigo;
	public Object funcionarioquarto_escolaridade;
	public Object funcionarioquarto_arquivoassinatura;
	/**
	 * funcionarioquinto_
	 */
	public Object funcionarioquinto_matricula;
	public Object funcionarioquinto_pessoa;
	public Object funcionarioquinto_codigo;
	public Object funcionarioquinto_escolaridade;
	public Object funcionarioquinto_arquivoassinatura;
	/**
	 * funcionarioprimario_pessoa
	 */
	public Object funcionarioprimario_pessoa_codigo;
	public Object funcionarioprimario_pessoa_cpf;
	public Object funcionarioprimario_pessoa_nome;
	public Object funcionarioprimario_pessoa_tipoassinaturadocumentoenum;
	/**
	 * funcionariosecundario_pessoa
	 */
	public Object funcionariosecundario_pessoa_codigo;
	public Object funcionariosecundario_pessoa_cpf;
	public Object funcionariosecundario_pessoa_nome;
	public Object funcionariosecundario_pessoa_tipoassinaturadocumentoenum;
	/**
	 * funcionarioterceiro_pessoa
	 */
	public Object funcionarioterceiro_pessoa_codigo;
	public Object funcionarioterceiro_pessoa_cpf;
	public Object funcionarioterceiro_pessoa_nome;
	public Object funcionarioterceiro_pessoa_tipoassinaturadocumentoenum;
	/**
	 * funcionarioquarto_pessoa
	 */
	public Object funcionarioquarto_pessoa_codigo;
	public Object funcionarioquarto_pessoa_cpf;
	public Object funcionarioquarto_pessoa_nome;
	public Object funcionarioquarto_pessoa_tipoassinaturadocumentoenum;
	/**
	 * funcionarioquinto_pessoa
	 */
	public Object funcionarioquinto_pessoa_codigo;
	public Object funcionarioquinto_pessoa_cpf;
	public Object funcionarioquinto_pessoa_nome;
	public Object funcionarioquinto_pessoa_tipoassinaturadocumentoenum;
	/**
	 * funcionarioprimario_arquivoassinatura
	 */
	public Object funcionarioprimario_arquivoassinatura_codigo;
	public Object funcionarioprimario_arquivoassinatura_nome;
	public Object funcionarioprimario_arquivoassinatura_descricao;
	public Object funcionarioprimario_arquivoassinatura_dataupload;
	public Object funcionarioprimario_arquivoassinatura_pastabasearquivo;
	public Object funcionarioprimario_arquivoassinatura_servidorarquivoonline;
	/**
	 * funcionariosecundario_arquivoassinatura
	 */
	public Object funcionariosecundario_arquivoassinatura_codigo;
	public Object funcionariosecundario_arquivoassinatura_nome;
	public Object funcionariosecundario_arquivoassinatura_descricao;
	public Object funcionariosecundario_arquivoassinatura_dataupload;
	public Object funcionariosecundario_arquivoassinatura_pastabasearquivo;
	public Object funcionariosecundario_arquivoassinatura_servidorarquivoonline;
	/**
	 * funcionarioterceiro_arquivoassinatura
	 */
	public Object funcionarioterceiro_arquivoassinatura_codigo;
	public Object funcionarioterceiro_arquivoassinatura_nome;
	public Object funcionarioterceiro_arquivoassinatura_descricao;
	public Object funcionarioterceiro_arquivoassinatura_dataupload;
	public Object funcionarioterceiro_arquivoassinatura_pastabasearquivo;
	public Object funcionarioterceiro_arquivoassinatura_servidorarquivoonline;
	/**
	 * funcionarioquarto_arquivoassinatura
	 */
	public Object funcionarioquarto_arquivoassinatura_codigo;
	public Object funcionarioquarto_arquivoassinatura_nome;
	public Object funcionarioquarto_arquivoassinatura_descricao;
	public Object funcionarioquarto_arquivoassinatura_dataupload;
	public Object funcionarioquarto_arquivoassinatura_pastabasearquivo;
	public Object funcionarioquarto_arquivoassinatura_servidorarquivoonline;
	/**
	 * funcionarioquinto_arquivoassinatura
	 */
	public Object funcionarioquinto_arquivoassinatura_codigo;
	public Object funcionarioquinto_arquivoassinatura_nome;
	public Object funcionarioquinto_arquivoassinatura_descricao;
	public Object funcionarioquinto_arquivoassinatura_dataupload;
	public Object funcionarioquinto_arquivoassinatura_pastabasearquivo;
	public Object funcionarioquinto_arquivoassinatura_servidorarquivoonline;
	/**
	 * cargofuncionarioprincipal_
	 */
	public Object cargofuncionarioprincipal_codigo;
	public Object cargofuncionarioprincipal_nome;
	/**
	 * cargofuncionariosecundario_
	 */
	public Object cargofuncionariosecundario_codigo;
	public Object cargofuncionariosecundario_nome;
	/**
	 * cargofuncionarioterceiro_
	 */
	public Object cargofuncionarioterceiro_codigo;
	public Object cargofuncionarioterceiro_nome;
	/**
	 * cargofuncionarioquarto_
	 */
	public Object cargofuncionarioquarto_codigo;
	public Object cargofuncionarioquarto_nome;
	/**
	 * cargofuncionarioquinto_
	 */
	public Object cargofuncionarioquinto_codigo;
	public Object cargofuncionarioquinto_nome;
	/**
	 * responsavelcadastro_
	 */
	public Object responsavelcadastro_codigo;
	public Object responsavelcadastro_nome;
	public Object responsavelcadastro_pessoa;
	/**
	 * cidadepta_
	 */
	public Object cidadepta_codigo;
	public Object cidadepta_nome;
	public Object cidadepta_codigoibge;
	public Object cidadepta_estado;
	/**
	 * cidadepta_estado
	 */
	public Object cidadepta_estado_codigo;
	public Object cidadepta_estado_nome;
	public Object cidadepta_estado_codigoibge;
	public Object cidadepta_estado_sigla;
	public Object cidadepta_estado_paiz;
	
	
	public Object responsavelanulacao_codigo;
	public Object responsavelanulacao_nome;
	public Object responsavelanulacao_pessoa;
	
	public Object responsavelanulacao_pessoa_codigo;
	public Object responsavelanulacao_pessoa_nome;
	
	/**
	 * matricula_
	 */
	public Object matricula_turno;
	public Object matricula_usuario;
	public Object matricula_inscricao;
	public Object matricula_situacaofinanceira;
	public Object matricula_situacao;
	public Object matricula_data;
	public Object matricula_curso;
	public Object matricula_unidadeensino;
	public Object matricula_aluno;
	public Object matricula_matricula;
	public Object matricula_tranferenciaentrada;
	public Object matricula_formaingresso;
	public Object matricula_atividadecomplementar;
	public Object matricula_anoingresso;
	public Object matricula_semestreingresso;
	public Object matricula_anoconclusao;
	public Object matricula_semestreconclusao;
	public Object matricula_disciplinasprocseletivo;
	public Object matricula_fezenade;
	public Object matricula_dataenade;
	public Object matricula_notaenade;
	public Object matricula_enade;
	public Object matricula_alunoabandonoucurso;
	public Object matricula_observacaocomplementar;
	public Object matricula_localarmazenamentodocumentosmatricula;
	public Object matricula_datainiciocurso;
	public Object matricula_dataconclusaocurso;
	public Object matricula_formacaoacademica;
	public Object matricula_autorizacaocurso;
	public Object matricula_tipomatricula;
	public Object matricula_matriculasuspensa;
	public Object matricula_databasesuspensao;
	public Object matricula_datacolacaograu;
	public Object matricula_mesingresso;
	public Object matricula_datacadastro;
	public Object matricula_gradecurricularatual;
	public Object matricula_observacaodiploma;
	public Object matricula_classificacaoingresso;
	public Object matricula_semestreanoingressocenso;
	public Object matricula_renovacaoreconhecimento;
	public Object matricula_nomemonografia;
	public Object matricula_notamonografia;
	public Object matricula_orientadormonografia;
	public Object matricula_cargahorariamonografia;
	public Object matricula_titulacaoorientadormonografia;
	public Object matricula_cargahorariamatrizcurricular;
	public Object matricula_areaconcentracaomestrado;
	public Object matricula_datarealizacaoprovamestrado;
	public Object matricula_dataexamequalificacaomestrado;
	public Object matricula_notaenem;
	public Object matricula_matriculaserasa;
	public Object matricula_consultor;
	public Object matricula_tipotrabalhoconclusaocurso;
	public Object matricula_naoenviarmensagemcobranca;
	public Object matricula_alunoconcluiudisciplinasregulares;
	public Object matricula_qtddiasadiarbloqueio;
	public Object matricula_canceladofinanceiro;
	public Object matricula_dataemissaohistorico;
	public Object matricula_codigofinanceiromatricula;
	public Object matricula_bloqueioporsolicitacaoliberacaomatricula;
	public Object matricula_dataprocessoseletivo;
	public Object matricula_totalpontoprocseletivo;
	public Object matricula_permitirinclusaoexclusaodisciplinasrenovacao;
	public Object matricula_localprocessoseletivo;
	public Object matricula_horascomplementares;
	/**
	 * matricula_aluno_
	 */
	public Object matricula_aluno_tituloeleitoral;
	public Object matricula_aluno_orgaoemissor;
	public Object matricula_aluno_estadoemissaorg;
	public Object matricula_aluno_dataemissaorg;
	public Object matricula_aluno_rg;
	public Object matricula_aluno_cpf;
	public Object matricula_aluno_nacionalidade;
	public Object matricula_aluno_naturalidade;
	public Object matricula_aluno_datanasc;
	public Object matricula_aluno_email;
	public Object matricula_aluno_celular;
	public Object matricula_aluno_telefonerecado;
	public Object matricula_aluno_telefoneres;
	public Object matricula_aluno_telefonecomer;
	public Object matricula_aluno_estadocivil;
	public Object matricula_aluno_sexo;
	public Object matricula_aluno_cidade;
	public Object matricula_aluno_complemento;
	public Object matricula_aluno_cep;
	public Object matricula_aluno_numero;
	public Object matricula_aluno_setor;
	public Object matricula_aluno_endereco;
	public Object matricula_aluno_nome;
	public Object matricula_aluno_codigo;
	public Object matricula_aluno_aluno;
	public Object matricula_aluno_professor;
	public Object matricula_aluno_funcionario;
	public Object matricula_aluno_candidato;
	public Object matricula_aluno_ativo;
	public Object matricula_aluno_arquivoimagem;
	public Object matricula_aluno_coordenador;
	public Object matricula_aluno_nomebatismo;
	public Object matricula_aluno_utilizardocumentoestrangeiro;
	public Object matricula_aluno_tipodocumentoestrangeiro;
	public Object matricula_aluno_numerodocumentoestrangeiro;
	/**
	 * matricula_aluno_nacionalidade_
	 */
	public Object matricula_aluno_nacionalidade_codigo;
	public Object matricula_aluno_nacionalidade_nome;
	public Object matricula_aluno_nacionalidade_nacionalidade;
	/**
	 * matricula_aluno_naturalidade_
	 */
	public Object matricula_aluno_naturalidade_codigo;
	public Object matricula_aluno_naturalidade_nome;
	public Object matricula_aluno_naturalidade_codigoibge;
	public Object matricula_aluno_naturalidade_estado;
	/**
	 * matricula_aluno_naturalidade_estado
	 */
	public Object matricula_aluno_naturalidade_estado_codigo;
	public Object matricula_aluno_naturalidade_estado_nome;
	public Object matricula_aluno_naturalidade_estado_codigoibge;
	public Object matricula_aluno_naturalidade_estado_sigla;
	public Object matricula_aluno_naturalidade_estado_paiz;
	/**
	 * matricula_autorizacaocurso_
	 */
	public Object matricula_autorizacaocurso_codigo;
	public Object matricula_autorizacaocurso_nome;
	public Object matricula_autorizacaocurso_data;
	public Object matricula_autorizacaocurso_curso;
	public Object matricula_autorizacaocurso_tipoautorizacaocursoenum;
	public Object matricula_autorizacaocurso_numero;
	public Object matricula_autorizacaocurso_datacredenciamento;
	public Object matricula_autorizacaocurso_veiculopublicacao;
	public Object matricula_autorizacaocurso_secaopublicacao;
	public Object matricula_autorizacaocurso_paginapublicacao;
	public Object matricula_autorizacaocurso_numerodou;
	public Object matricula_autorizacaocurso_emtramitacao;
	public Object matricula_autorizacaocurso_datacadastro;
	public Object matricula_autorizacaocurso_dataprotocolo;
	/**
	 * matricula_renovacaoReconhecimento_
	 */
	public Object matricula_renovacaoreconhecimento_codigo;
	public Object matricula_renovacaoreconhecimento_nome;
	public Object matricula_renovacaoreconhecimento_data;
	public Object matricula_renovacaoreconhecimento_curso;
	public Object matricula_renovacaoreconhecimento_tipoautorizacaocursoenum;
	public Object matricula_renovacaoreconhecimento_numero;
	public Object matricula_renovacaoreconhecimento_datacredenciamento;
	public Object matricula_renovacaoreconhecimento_veiculopublicacao;
	public Object matricula_renovacaoreconhecimento_secaopublicacao;
	public Object matricula_renovacaoreconhecimento_paginapublicacao;
	public Object matricula_renovacaoreconhecimento_numerodou;
	public Object matricula_renovacaoreconhecimento_emtramitacao;
	public Object matricula_renovacaoreconhecimento_datacadastro;
	public Object matricula_renovacaoreconhecimento_dataprotocolo;
	/**
	 * matricula_unidadeEnsino_
	 */
	public Object matricula_unidadeensino_matriz;
	public Object matricula_unidadeensino_email;
	public Object matricula_unidadeensino_cnpj;
	public Object matricula_unidadeensino_tipoempresa;
	public Object matricula_unidadeensino_cep;
	public Object matricula_unidadeensino_cidade;
	public Object matricula_unidadeensino_complemento;
	public Object matricula_unidadeensino_numero;
	public Object matricula_unidadeensino_setor;
	public Object matricula_unidadeensino_endereco;
	public Object matricula_unidadeensino_razaosocial;
	public Object matricula_unidadeensino_nome;
	public Object matricula_unidadeensino_codigo;
	public Object matricula_unidadeensino_abreviatura;
	public Object matricula_unidadeensino_configuracoes;
	public Object matricula_unidadeensino_codigoies;
	public Object matricula_unidadeensino_credenciamentoportaria;
	public Object matricula_unidadeensino_datapublicacaodo;
	public Object matricula_unidadeensino_mantenedora;
	public Object matricula_unidadeensino_desativada;
	public Object matricula_unidadeensino_credenciamento;
	public Object matricula_unidadeensino_nomeexpedicaodiploma;
	public Object matricula_unidadeensino_codigoiesmantenedora;
	public Object matricula_unidadeensino_cnpjmantenedora;
	public Object matricula_unidadeensino_unidadecertificadora;
	public Object matricula_unidadeensino_cnpjunidadecertificadora;
	public Object matricula_unidadeensino_codigoiesunidadecertificadora;
	public Object matricula_unidadeensino_configuracaoged;
	public Object matricula_unidadeensino_configuracaoaparenciasistema;
	public Object matricula_unidadeensino_tipoautorizacaoenum;
	public Object matricula_unidadeensino_numerocredenciamento;
	public Object matricula_unidadeensino_datacredenciamento;
	public Object matricula_unidadeensino_veiculopublicacaocredenciamento;
	public Object matricula_unidadeensino_secaopublicacaocredenciamento;
	public Object matricula_unidadeensino_paginapublicacaocredenciamento;
	public Object matricula_unidadeensino_numerodoucredenciamento;
	public Object matricula_unidadeensino_informardadosregistradora;
	public Object mat_uni_utilizarenderecounidadeensinoregistradora;
	public Object matricula_unidadeensino_cepregistradora;
	public Object matricula_unidadeensino_cidaderegistradora;
	public Object matricula_unidadeensino_complementoregistradora;
	public Object matricula_unidadeensino_bairroregistradora;
	public Object matricula_unidadeensino_enderecoregistradora;
	public Object matricula_unidadeensino_numeroregistradora;
	public Object matricula_unidadeensino_utilizarcredenciamentounidadeensino;
	public Object matricula_unidadeensino_numerocredenciamentoregistradora;
	public Object matricula_unidadeensino_datacredenciamentoregistradora;
	public Object matricula_unidadeensino_datapublicacaodoregistradora;
	public Object mat_uni_veiculopublicacaocredenciamentoregistradora;
	public Object mat_uni_secaopublicacaocredenciamentoregistradora;
	public Object mat_uni_paginapublicacaocredenciamentoregistradora;
	public Object mat_uni_numeropublicacaocredenciamentoregistradora;
	public Object matricula_unidadeensino_utilizarmantenedoraunidadeensino;
	public Object matricula_unidadeensino_mantenedoraregistradora;
	public Object matricula_unidadeensino_cnpjmantenedoraregistradora;
	public Object matricula_unidadeensino_cepmantenedoraregistradora;
	public Object matricula_unidadeensino_enderecomantenedoraregistradora;
	public Object matricula_unidadeensino_numeromantenedoraregistradora;
	public Object matricula_unidadeensino_cidademantenedoraregistradora;
	public Object matricula_unidadeensino_complementomantenedoraregistradora;
	public Object matricula_unidadeensino_bairromantenedoraregistradora;
	public Object mat_uni_utilizarenderecounidadeensinomantenedora;
	public Object matricula_unidadeensino_cepmantenedora;
	public Object matricula_unidadeensino_enderecomantenedora;
	public Object matricula_unidadeensino_numeromantenedora;
	public Object matricula_unidadeensino_cidademantenedora;
	public Object matricula_unidadeensino_complementomantenedora;
	public Object matricula_unidadeensino_bairromantenedora;
	public Object matricula_unidadeensino_numerorecredenciamento;
	public Object matricula_unidadeensino_datarecredenciamento;
	public Object matricula_unidadeensino_datapublicacaorecredenciamento;
	public Object matricula_unidadeensino_veiculopublicacaorecredenciamento;
	public Object matricula_unidadeensino_secaopublicacaorecredenciamento;
	public Object matricula_unidadeensino_paginapublicacaorecredenciamento;
	public Object matricula_unidadeensino_numerodourecredenciamento;
	public Object matricula_unidadeensino_tipoautorizacaorecredenciamento;
	public Object matricula_unidadeensino_numerorenovacaorecredenciamento;
	public Object matricula_unidadeensino_datarenovacaorecredenciamento;
	public Object mat_uni_datapublicacaorenovacaorecredenciamento;
	public Object mat_uni_veiculopublicacaorenovacaorecredenciamento;
	public Object mat_uni_secaopublicacaorenovacaorecredenciamento;
	public Object mat_uni_paginapublicacaorenovacaorecredenciamento;
	public Object matricula_unidadeensino_numerodourenovacaorecredenciamento;
	public Object mat_uni_tipoautorizacaorenovacaorecredenciamento;
	public Object mat_uni_tipoautorizacaocredenciamentoregistradora;
	public Object matricula_unidadeensino_configuracaodiplomadigital;
	public Object matricula_unidadeensino_numerocredenciamentoead;
	public Object matricula_unidadeensino_credenciamentoead;
	public Object matricula_unidadeensino_datacredenciamentoead;
	public Object matricula_unidadeensino_datapublicacaodoead;
	public Object matricula_unidadeensino_credenciamentoportariaead;
	public Object matricula_unidadeensino_veiculopublicacaocredenciamentoead;
	public Object matricula_unidadeensino_secaopublicacaocredenciamentoead;
	public Object matricula_unidadeensino_paginapublicacaocredenciamentoead;
	public Object matricula_unidadeensino_numerodoucredenciamentoead;
	public Object matricula_unidadeensino_tipoautorizacaoead;
	public Object matricula_unidadeensino_numerorecredenciamentoead;
	public Object matricula_unidadeensino_datarecredenciamentoead;
	public Object matricula_unidadeensino_datapublicacaorecredenciamentoead;
	public Object matricula_unidadeensino_veiculopublicacaorecredenciamentoead;
	public Object matricula_unidadeensino_secaopublicacaorecredenciamentoead;
	public Object matricula_unidadeensino_paginapublicacaorecredenciamentoead;
	public Object matricula_unidadeensino_numerodourecredenciamentoead;
	public Object matricula_unidadeensino_tipoautorizacaorecredenciamentoead;
	public Object matricula_unidadeensino_numerorenovacaorecredenciamentoead;
	public Object matricula_unidadeensino_datarenovacaorecredenciamentoead;
	public Object mat_uni_datapublicacaorenovacaorecredenciamentoead;
	public Object mat_uni_veiculopublicacaorenovacaorecredenciamentoead;
	public Object mat_uni_secaopublicacaorenovacaorecredenciamentoead;
	public Object mat_uni_paginapublicacaorenovacaorecredenciamentoead;
	public Object matricula_unidadeensino_numerodourenovacaorecredenciamentoead;
	public Object mat_uni_tipoautorizacaorenovacaorecredenciamentoead;
	/**
	 * matricula_turno_
	 */
	public Object matricula_turno_codigo;
	public Object matricula_turno_duracaoaula;
	public Object matricula_turno_nraulas;
	public Object matricula_turno_nome;
	public Object matricula_turno_tipohorario;
	/**
	 * matricula_curso_
	 */
	public Object matricula_curso_configuracaoacademico;
	public Object matricula_curso_areaconhecimento;
	public Object matricula_curso_periodicidade;
	public Object matricula_curso_datacriacao;
	public Object matricula_curso_nrregistrointerno;
	public Object matricula_curso_datapublicacaodo;
	public Object matricula_curso_nrperiodoletivo;
	public Object matricula_curso_niveleducacional;
	public Object matricula_curso_publicoalvo;
	public Object matricula_curso_descricao;
	public Object matricula_curso_objetivos;
	public Object matricula_curso_nome;
	public Object matricula_curso_codigo;
	public Object matricula_curso_titulo;
	public Object matricula_curso_abreviatura;
	public Object matricula_curso_titulacaodoformando;
	public Object matricula_curso_titulacaodoformandofeminino;
	public Object matricula_curso_nomedocumentacao;
	public Object matricula_curso_titulacaomasculinoapresentardiploma;
	public Object matricula_curso_titulacaofemininoapresentardiploma;
	public Object matricula_curso_modalidadecurso;
	public Object matricula_curso_possuicodigoemec;
	public Object matricula_curso_codigoemec;
	public Object matricula_curso_numeroprocessoemec;
	public Object matricula_curso_tipoprocessoemec;
	public Object matricula_curso_datacadastroemec;
	public Object matricula_curso_dataprotocoloemec;
	public Object matricula_curso_tipoautorizacaocursoenum;
	public Object matricula_curso_numeroautorizacao;
	public Object matricula_curso_datacredenciamento;
	public Object matricula_curso_veiculopublicacao;
	public Object matricula_curso_secaopublicacao;
	public Object matricula_curso_paginapublicacao;
	public Object matricula_curso_numerodou;
	public Object matricula_curso_autorizacaoresolucaoemtramitacao;
	public Object matricula_curso_numeroprocessoautorizacaoresolucao;
	public Object matricula_curso_tipoprocessoautorizacaoresolucao;
	public Object matricula_curso_datacadastroautorizacaoresolucao;
	public Object matricula_curso_dataprotocoloautorizacaoresolucao;
	public Object matricula_curso_habilitacao;
	public Object matricula_curso_datahabilitacao;
	/**
	 * matricula_gradecurricular_
	 */
	public Object matricula_gradecurricular_curso;
	public Object matricula_gradecurricular_situacao;
	public Object matricula_gradecurricular_datacadastro;
	public Object matricula_gradecurricular_nome;
	public Object matricula_gradecurricular_codigo;
	public Object matricula_gradecurricular_cargahoraria;
	public Object matricula_gradecurricular_creditos;
	public Object mat_gra_totalcargahorariaatividadecomplementar;
	public Object matricula_gradecurricular_totalcargahorariaestagio;
	/**
	 * filtros de consulta
	 */
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private MatriculaVO matriculaVO;
	private DataModelo controleConsultaOtimizado;
	private CursoVO cursoVO;
	private Boolean pendenteAssinatura;
	private Boolean valido;
	private Boolean anulado;
	private String codigoVerificacao;
	private String numeroRegistro;
	private String numeroProcesso;
	private String tipoData;
	private String via;
	private Date dataPeriodoInicio;
	private Date dataPeriodoFim;
	private Date dataPeriodoAnulacaoInicio;
	private Date dataPeriodoAnulacaoFim;
	private List<SelectItem> tipoConsultaComboLimitePagina;
	private List<SelectItem> tipoConsultaComboTipoData;

	/**
	 * 
	 * filtros de consulta curso
	 */
	private TipoNivelEducacional tipoNivelEducacionalCurso;
	private DataModelo controleConsultaCurso;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> tipoConsultaComboNivelEducacional;
	/**
	 * 
	 * filtros de consulta matricula
	 */
	private DataModelo controleConsultaMatricula;
	private List<SelectItem> tipoConsultaComboMatricula;

	public void montarDadosExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_codigo)) {
			expedicaoDiploma.setCodigo((Integer) expedicaoDiploma_codigo);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_matricula)) {
			expedicaoDiploma.getMatricula().setMatricula((String) expedicaoDiploma_matricula);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_via)) {
			expedicaoDiploma.setVia((String) expedicaoDiploma_via);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_dataexpedicao)) {
			expedicaoDiploma.setDataExpedicao((Date) expedicaoDiploma_dataexpedicao);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_gradecurricular)) {
			expedicaoDiploma.getGradeCurricularVO().setCodigo((Integer) expedicaoDiploma_gradecurricular);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeroprocessoviaanterior)) {
			expedicaoDiploma.setNumeroProcessoViaAnterior((String) expedicaoDiploma_numeroprocessoviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeroregistrodiplomaviaanterior)) {
			expedicaoDiploma.setNumeroRegistroDiplomaViaAnterior((String) expedicaoDiploma_numeroregistrodiplomaviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioprimario)) {
			expedicaoDiploma.getFuncionarioPrimarioVO().setCodigo((Integer) expedicaoDiploma_funcionarioprimario);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionariosecundario)) {
			expedicaoDiploma.getFuncionarioSecundarioVO().setCodigo((Integer) expedicaoDiploma_funcionariosecundario);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_unidadeensinocertificadora)) {
			expedicaoDiploma.getUnidadeEnsinoCertificadora().setCodigo((Integer) expedicaoDiploma_unidadeensinocertificadora);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioterceiro)) {
			expedicaoDiploma.getFuncionarioTerceiroVO().setCodigo((Integer) expedicaoDiploma_funcionarioterceiro);
		}
		if (expedicaoDiploma_utilizarunidadematriz != null) {
			expedicaoDiploma.setUtilizarUnidadeMatriz((Boolean) expedicaoDiploma_utilizarunidadematriz);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_layoutdiploma)) {
			expedicaoDiploma.setLayoutDiploma((String) expedicaoDiploma_layoutdiploma);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioprincipal)) {
			expedicaoDiploma.getCargoFuncionarioPrincipalVO().setCodigo((Integer) expedicaoDiploma_cargofuncionarioprincipal);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionariosecundario)) {
			expedicaoDiploma.getCargoFuncionarioSecundarioVO().setCodigo((Integer) expedicaoDiploma_cargofuncionariosecundario);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioterceiro)) {
			expedicaoDiploma.getCargoFuncionarioTerceiroVO().setCodigo((Integer) expedicaoDiploma_cargofuncionarioterceiro);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulofuncionarioprincipal)) {
			expedicaoDiploma.setTituloFuncionarioPrincipal((String) expedicaoDiploma_titulofuncionarioprincipal);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulofuncionariosecundario)) {
			expedicaoDiploma.setTituloFuncionarioSecundario((String) expedicaoDiploma_titulofuncionariosecundario);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulofuncionarioterceiro)) {
			expedicaoDiploma.setTituloFuncionarioTerceiro((String) expedicaoDiploma_titulofuncionarioterceiro);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeroprocesso)) {
			expedicaoDiploma.setNumeroProcesso((String) expedicaoDiploma_numeroprocesso);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_reitorregistrodiplomaviaanterior)) {
			expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().setCodigo((Integer) expedicaoDiploma_reitorregistrodiplomaviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_secretariaregistrodiplomaviaanterior)) {
			expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().setCodigo((Integer) expedicaoDiploma_secretariaregistrodiplomaviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_secretariaregistrodiplomaviaanterior)) {
			expedicaoDiploma.setDataRegistroDiplomaViaAnterior((Date) expedicaoDiploma_dataregistrodiplomaviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargoreitorregistrodiplomaviaanterior)) {
			expedicaoDiploma.getCargoReitorRegistroDiplomaViaAnterior().setCodigo((Integer) expedicaoDiploma_cargoreitorregistrodiplomaviaanterior);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulacaomasculinoapresentardiploma)) {
			expedicaoDiploma.setTitulacaoMasculinoApresentarDiploma((String) expedicaoDiploma_titulacaomasculinoapresentardiploma);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulacaofemininoapresentardiploma)) {
			expedicaoDiploma.setTitulacaoFemininoApresentarDiploma((String) expedicaoDiploma_titulacaofemininoapresentardiploma);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_datapublicacaodiariooficial)) {
			expedicaoDiploma.setDataPublicacaoDiarioOficial((Date) expedicaoDiploma_datapublicacaodiariooficial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_serial)) {
			expedicaoDiploma.setSerial((String) expedicaoDiploma_serial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_observacao)) {
			expedicaoDiploma.setObservacao((String) expedicaoDiploma_observacao);
		}
		if (expedicaoDiploma_informarcamposlivroregistradora != null) {
			expedicaoDiploma.setInformarCamposLivroRegistradora((Boolean) expedicaoDiploma_informarcamposlivroregistradora);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_nrlivroregistradora)) {
			expedicaoDiploma.setLivroRegistradora((String) expedicaoDiploma_nrlivroregistradora);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_nrfolhareciboregistradora)) {
			expedicaoDiploma.setFolhaReciboRegistradora((String) expedicaoDiploma_nrfolhareciboregistradora);
		}
		if (expedicaoDiploma_gerarxmldiploma != null) {
			expedicaoDiploma.setGerarXMLDiploma((Boolean) expedicaoDiploma_gerarxmldiploma);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioquarto)) {
			expedicaoDiploma.getFuncionarioQuartoVO().setCodigo((Integer) expedicaoDiploma_funcionarioquarto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioquinto)) {
			expedicaoDiploma.getFuncionarioQuintoVO().setCodigo((Integer) expedicaoDiploma_funcionarioquinto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioquarto)) {
			expedicaoDiploma.getCargoFuncionarioQuartoVO().setCodigo((Integer) expedicaoDiploma_cargofuncionarioquarto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioquinto)) {
			expedicaoDiploma.getCargoFuncionarioQuintoVO().setCodigo((Integer) expedicaoDiploma_cargofuncionarioquinto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulofuncionarioquarto)) {
			expedicaoDiploma.setTituloFuncionarioQuarto((String) expedicaoDiploma_titulofuncionarioquarto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_titulofuncionarioquinto)) {
			expedicaoDiploma.setTituloFuncionarioQuinto((String) expedicaoDiploma_titulofuncionarioquinto);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeroregistrodiploma)) {
			expedicaoDiploma.setNumeroRegistroDiploma((String) expedicaoDiploma_numeroregistrodiploma);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_codigovalidacaodiplomadigital)) {
			expedicaoDiploma.setCodigoValidacaoDiplomaDigital((String) expedicaoDiploma_codigovalidacaodiplomadigital);
		}
		if (expedicaoDiploma_anulado != null) {
			expedicaoDiploma.setAnulado((Boolean) expedicaoDiploma_anulado);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_textopadrao)) {
			expedicaoDiploma.getTextoPadrao().setCodigo((Integer) expedicaoDiploma_textopadrao);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_dataregistrodiploma)) {
			expedicaoDiploma.setDataRegistroDiploma((Date) expedicaoDiploma_dataregistrodiploma);
		}
		if (expedicaoDiploma_emitidoporprocessotransferenciaassistida != null) {
			expedicaoDiploma.setEmitidoPorProcessoTransferenciaAssistida((Boolean) expedicaoDiploma_emitidoporprocessotransferenciaassistida);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_nomeiespta)) {
			expedicaoDiploma.setNomeIesPTA((String) expedicaoDiploma_nomeiespta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cnpjpta)) {
			expedicaoDiploma.setCnpjPTA((String) expedicaoDiploma_cnpjpta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_codigomecpta)) {
			expedicaoDiploma.setCodigoMecPTA((Integer) expedicaoDiploma_codigomecpta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_ceppta)) {
			expedicaoDiploma.setCepPTA((String) expedicaoDiploma_ceppta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_cidadepta)) {
			expedicaoDiploma.getCidadePTA().setCodigo((Integer) expedicaoDiploma_cidadepta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_logradouropta)) {
			expedicaoDiploma.setLogradouroPTA((String) expedicaoDiploma_logradouropta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeropta)) {
			expedicaoDiploma.setNumeroPTA((String) expedicaoDiploma_numeropta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_complementopta)) {
			expedicaoDiploma.setComplementoPTA((String) expedicaoDiploma_complementopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_bairropta)) {
			expedicaoDiploma.setBairroPTA((String) expedicaoDiploma_bairropta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_tipodescredenciamentopta)) {
			expedicaoDiploma.setTipoDescredenciamentoPTA(TipoAutorizacaoEnum.valueOf((String) expedicaoDiploma_tipodescredenciamentopta));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numerodescredenciamentopta)) {
			expedicaoDiploma.setNumeroDescredenciamentoPTA((String) expedicaoDiploma_numerodescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_datadescredenciamentopta)) {
			expedicaoDiploma.setDataDescredenciamentoPTA((Date) expedicaoDiploma_datadescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_datapublicacaodescredenciamentopta)) {
			expedicaoDiploma.setDataPublicacaoDescredenciamentoPTA((Date) expedicaoDiploma_datapublicacaodescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_veiculopublicacaodescredenciamentopta)) {
			expedicaoDiploma.setVeiculoPublicacaoDescredenciamentoPTA((String) expedicaoDiploma_veiculopublicacaodescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_secaopublicacaodescredenciamentopta)) {
			expedicaoDiploma.setSecaoPublicacaoDescredenciamentoPTA((Integer) expedicaoDiploma_secaopublicacaodescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_paginapublicacaodescredenciamentopta)) {
			expedicaoDiploma.setPaginaPublicacaoDescredenciamentoPTA((Integer) expedicaoDiploma_paginapublicacaodescredenciamentopta);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numerodoudescredenciamentopta)) {
			expedicaoDiploma.setNumeroDOUDescredenciamentoPTA((Integer) expedicaoDiploma_numerodoudescredenciamentopta);
		}
		if (expedicaoDiploma_emitidopordecisaojudicial != null) {
			expedicaoDiploma.setEmitidoPorDecisaoJudicial((Boolean) expedicaoDiploma_emitidopordecisaojudicial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_nomejuizdecisaojudicial)) {
			expedicaoDiploma.setNomeJuizDecisaoJudicial((String) expedicaoDiploma_nomejuizdecisaojudicial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_numeroprocessodecisaojudicial)) {
			expedicaoDiploma.setNumeroProcessoDecisaoJudicial((String) expedicaoDiploma_numeroprocessodecisaojudicial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_decisaojudicial)) {
			expedicaoDiploma.setDecisaoJudicial((String) expedicaoDiploma_decisaojudicial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_informacoesadicionaisdecisaojudicial)) {
			expedicaoDiploma.setInformacoesAdicionaisDecisaoJudicial((String) expedicaoDiploma_informacoesadicionaisdecisaojudicial);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_versaodiploma)) {
			expedicaoDiploma.setVersaoDiploma(VersaoDiplomaDigitalEnum.getEnum((String) expedicaoDiploma_versaodiploma));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_dataanulacao)) {
			expedicaoDiploma.setDataAnulacao((Date) expedicaoDiploma_dataanulacao);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_motivoanulacao)) {
			expedicaoDiploma.setMotivoAnulacao(TMotivoAnulacao.valueOf((String) expedicaoDiploma_motivoanulacao));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_responsavelcadastro)) {
			expedicaoDiploma.getResponsavelCadastro().setCodigo((Integer) expedicaoDiploma_responsavelcadastro);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_datacadastro)) {
			expedicaoDiploma.setDataCadastro((Date) expedicaoDiploma_datacadastro);
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiploma_anotacaoanulacao)) {
			expedicaoDiploma.setAnotacaoAnulacao((String) expedicaoDiploma_anotacaoanulacao);
		}
		/**
		 * montagem dados do texto padrao da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_textopadrao) || Uteis.isAtributoPreenchido((Integer) textopadrao_codigo)) {
			expedicaoDiploma.getTextoPadrao().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_textopadrao) ? expedicaoDiploma_textopadrao : textopadrao_codigo));
			if (Uteis.isAtributoPreenchido(textopadrao_descricao)) {
				expedicaoDiploma.getTextoPadrao().setDescricao((String) textopadrao_descricao);
			}
		}
		/**
		 * montagem dados da unidade ensino certificadora da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) unidadecertificadora_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_unidadeensinocertificadora)) {
			expedicaoDiploma.getUnidadeEnsinoCertificadora().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_unidadeensinocertificadora) ? expedicaoDiploma_unidadeensinocertificadora : unidadecertificadora_codigo));
			if (Uteis.isAtributoPreenchido(unidadecertificadora_nome)) {
				expedicaoDiploma.getUnidadeEnsinoCertificadora().setNome((String) unidadecertificadora_nome);
			}
			if (Uteis.isAtributoPreenchido(unidadecertificadora_cnpj)) {
				expedicaoDiploma.getUnidadeEnsinoCertificadora().setCNPJ((String) unidadecertificadora_cnpj);
			}
		}
		/**
		 * montagem dados do funcionario primario da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) funcionarioprimario_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_funcionarioprimario)) {
			expedicaoDiploma.getFuncionarioPrimarioVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioprimario) ? expedicaoDiploma_funcionarioprimario : funcionarioprimario_codigo));
			if (Uteis.isAtributoPreenchido(funcionarioprimario_matricula)) {
				expedicaoDiploma.getFuncionarioPrimarioVO().setMatricula((String) funcionarioprimario_matricula);
			}
			if (Uteis.isAtributoPreenchido(funcionarioprimario_pessoa)) {
				expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setCodigo((Integer) funcionarioprimario_pessoa);
			}
			if (Uteis.isAtributoPreenchido(funcionarioprimario_escolaridade)) {
				expedicaoDiploma.getFuncionarioPrimarioVO().setEscolaridade((String) funcionarioprimario_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioprimario_pessoa_codigo) || Uteis.isAtributoPreenchido((Integer) funcionarioprimario_pessoa)) {
				expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioprimario_pessoa) ? funcionarioprimario_pessoa : funcionarioprimario_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioprimario_pessoa_cpf)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setCPF((String) funcionarioprimario_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_pessoa_nome)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setNome((String) funcionarioprimario_pessoa_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_pessoa_tipoassinaturadocumentoenum)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf((String) funcionarioprimario_pessoa_tipoassinaturadocumentoenum));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioprimario_arquivoassinatura) || Uteis.isAtributoPreenchido((Integer) funcionarioprimario_arquivoassinatura_codigo)) {
				expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura) ? funcionarioprimario_arquivoassinatura : funcionarioprimario_arquivoassinatura_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura_nome)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setNome((String) funcionarioprimario_arquivoassinatura_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura_descricao)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setDescricao((String) funcionarioprimario_arquivoassinatura_descricao);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura_dataupload)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setDataUpload((Date) funcionarioprimario_arquivoassinatura_dataupload);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura_pastabasearquivo)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setPastaBaseArquivo((String) funcionarioprimario_arquivoassinatura_pastabasearquivo);
				}
				if (Uteis.isAtributoPreenchido(funcionarioprimario_arquivoassinatura_servidorarquivoonline)) {
					expedicaoDiploma.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf((String) funcionarioprimario_arquivoassinatura_servidorarquivoonline));
				}
			}
		}
		/**
		 * montagem dados do funcionario secundario da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) funcionariosecundario_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_funcionariosecundario)) {
			expedicaoDiploma.getFuncionarioSecundarioVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionariosecundario) ? expedicaoDiploma_funcionariosecundario : funcionariosecundario_codigo));
			if (Uteis.isAtributoPreenchido(funcionariosecundario_matricula)) {
				expedicaoDiploma.getFuncionarioSecundarioVO().setMatricula((String) funcionariosecundario_matricula);
			}
			if (Uteis.isAtributoPreenchido(funcionariosecundario_pessoa)) {
				expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setCodigo((Integer) funcionariosecundario_pessoa);
			}
			if (Uteis.isAtributoPreenchido(funcionariosecundario_escolaridade)) {
				expedicaoDiploma.getFuncionarioSecundarioVO().setEscolaridade((String) funcionariosecundario_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionariosecundario_pessoa_codigo) || Uteis.isAtributoPreenchido((Integer) funcionariosecundario_pessoa)) {
				expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionariosecundario_pessoa) ? funcionariosecundario_pessoa : funcionariosecundario_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(funcionariosecundario_pessoa_cpf)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setCPF((String) funcionariosecundario_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_pessoa_nome)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setNome((String) funcionariosecundario_pessoa_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_pessoa_tipoassinaturadocumentoenum)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf((String) funcionariosecundario_pessoa_tipoassinaturadocumentoenum));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionariosecundario_arquivoassinatura) || Uteis.isAtributoPreenchido((Integer) funcionariosecundario_arquivoassinatura_codigo)) {
				expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura) ? funcionariosecundario_arquivoassinatura : funcionariosecundario_arquivoassinatura_codigo));
				if (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura_nome)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setNome((String) funcionariosecundario_arquivoassinatura_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura_descricao)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setDescricao((String) funcionariosecundario_arquivoassinatura_descricao);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura_dataupload)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setDataUpload((Date) funcionariosecundario_arquivoassinatura_dataupload);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura_pastabasearquivo)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setPastaBaseArquivo((String) funcionariosecundario_arquivoassinatura_pastabasearquivo);
				}
				if (Uteis.isAtributoPreenchido(funcionariosecundario_arquivoassinatura_servidorarquivoonline)) {
					expedicaoDiploma.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf((String) funcionariosecundario_arquivoassinatura_servidorarquivoonline));
				}
			}
		}
		/**
		 * montagem dados do funcionario terceiro da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) funcionarioterceiro_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_funcionarioterceiro)) {
			expedicaoDiploma.getFuncionarioTerceiroVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioterceiro) ? expedicaoDiploma_funcionarioterceiro : funcionarioterceiro_codigo));
			if (Uteis.isAtributoPreenchido(funcionarioterceiro_matricula)) {
				expedicaoDiploma.getFuncionarioTerceiroVO().setMatricula((String) funcionarioterceiro_matricula);
			}
			if (Uteis.isAtributoPreenchido(funcionarioterceiro_pessoa)) {
				expedicaoDiploma.getFuncionarioTerceiroVO().getPessoa().setCodigo((Integer) funcionarioterceiro_pessoa);
			}
			if (Uteis.isAtributoPreenchido(funcionarioterceiro_escolaridade)) {
				expedicaoDiploma.getFuncionarioTerceiroVO().setEscolaridade((String) funcionarioterceiro_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioterceiro_pessoa_codigo) || Uteis.isAtributoPreenchido((Integer) funcionarioterceiro_pessoa)) {
				expedicaoDiploma.getFuncionarioTerceiroVO().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioterceiro_pessoa) ? funcionarioterceiro_pessoa : funcionarioterceiro_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_pessoa_cpf)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getPessoa().setCPF((String) funcionarioterceiro_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_pessoa_nome)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getPessoa().setNome((String) funcionarioterceiro_pessoa_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_pessoa_tipoassinaturadocumentoenum)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf((String) funcionarioterceiro_pessoa_tipoassinaturadocumentoenum));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioterceiro_arquivoassinatura) || Uteis.isAtributoPreenchido((Integer) funcionarioterceiro_arquivoassinatura_codigo)) {
				expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura) ? funcionarioterceiro_arquivoassinatura : funcionarioterceiro_arquivoassinatura_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura_nome)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setNome((String) funcionarioterceiro_arquivoassinatura_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura_descricao)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setDescricao((String) funcionarioterceiro_arquivoassinatura_descricao);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura_dataupload)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setDataUpload((Date) funcionarioterceiro_arquivoassinatura_dataupload);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura_pastabasearquivo)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setPastaBaseArquivo((String) funcionarioterceiro_arquivoassinatura_pastabasearquivo);
				}
				if (Uteis.isAtributoPreenchido(funcionarioterceiro_arquivoassinatura_servidorarquivoonline)) {
					expedicaoDiploma.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf((String) funcionarioterceiro_arquivoassinatura_servidorarquivoonline));
				}
			}
		}
		/**
		 * montagem dados do funcionario quarto da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) funcionarioquarto_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_funcionarioquarto)) {
			expedicaoDiploma.getFuncionarioQuartoVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioquarto) ? expedicaoDiploma_funcionarioquarto : funcionarioquarto_codigo));
			if (Uteis.isAtributoPreenchido(funcionarioquarto_matricula)) {
				expedicaoDiploma.getFuncionarioQuartoVO().setMatricula((String) funcionarioquarto_matricula);
			}
			if (Uteis.isAtributoPreenchido(funcionarioquarto_pessoa)) {
				expedicaoDiploma.getFuncionarioQuartoVO().getPessoa().setCodigo((Integer) funcionarioquarto_pessoa);
			}
			if (Uteis.isAtributoPreenchido(funcionarioquarto_escolaridade)) {
				expedicaoDiploma.getFuncionarioQuartoVO().setEscolaridade((String) funcionarioquarto_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioquarto_pessoa_codigo) || Uteis.isAtributoPreenchido((Integer) funcionarioquarto_pessoa)) {
				expedicaoDiploma.getFuncionarioQuartoVO().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioquarto_pessoa) ? funcionarioquarto_pessoa : funcionarioquarto_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioquarto_pessoa_cpf)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getPessoa().setCPF((String) funcionarioquarto_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_pessoa_nome)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getPessoa().setNome((String) funcionarioquarto_pessoa_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_pessoa_tipoassinaturadocumentoenum)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf((String) funcionarioquarto_pessoa_tipoassinaturadocumentoenum));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioquarto_arquivoassinatura) || Uteis.isAtributoPreenchido((Integer) funcionarioquarto_arquivoassinatura_codigo)) {
				expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura) ? funcionarioquarto_arquivoassinatura : funcionarioquarto_arquivoassinatura_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura_nome)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setNome((String) funcionarioquarto_arquivoassinatura_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura_descricao)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setDescricao((String) funcionarioquarto_arquivoassinatura_descricao);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura_dataupload)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setDataUpload((Date) funcionarioquarto_arquivoassinatura_dataupload);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura_pastabasearquivo)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setPastaBaseArquivo((String) funcionarioquarto_arquivoassinatura_pastabasearquivo);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquarto_arquivoassinatura_servidorarquivoonline)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf((String) funcionarioquarto_arquivoassinatura_servidorarquivoonline));
				}
			}
		}
		/**
		 * montagem dados do funcionario quinto da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) funcionarioquinto_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_funcionarioquinto)) {
			expedicaoDiploma.getFuncionarioQuintoVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_funcionarioquinto) ? expedicaoDiploma_funcionarioquinto : funcionarioquinto_codigo));
			if (Uteis.isAtributoPreenchido(funcionarioquinto_matricula)) {
				expedicaoDiploma.getFuncionarioQuintoVO().setMatricula((String) funcionarioquinto_matricula);
			}
			if (Uteis.isAtributoPreenchido(funcionarioquinto_pessoa)) {
				expedicaoDiploma.getFuncionarioQuintoVO().getPessoa().setCodigo((Integer) funcionarioquinto_pessoa);
			}
			if (Uteis.isAtributoPreenchido(funcionarioquinto_escolaridade)) {
				expedicaoDiploma.getFuncionarioQuintoVO().setEscolaridade((String) funcionarioquinto_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioquinto_pessoa_codigo) || Uteis.isAtributoPreenchido((Integer) funcionarioquinto_pessoa)) {
				expedicaoDiploma.getFuncionarioQuintoVO().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioquinto_pessoa) ? funcionarioquinto_pessoa : funcionarioquinto_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioquinto_pessoa_cpf)) {
					expedicaoDiploma.getFuncionarioQuintoVO().getPessoa().setCPF((String) funcionarioquinto_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_pessoa_nome)) {
					expedicaoDiploma.getFuncionarioQuintoVO().getPessoa().setNome((String) funcionarioquinto_pessoa_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_pessoa_tipoassinaturadocumentoenum)) {
					expedicaoDiploma.getFuncionarioQuintoVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf((String) funcionarioquinto_pessoa_tipoassinaturadocumentoenum));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) funcionarioquinto_arquivoassinatura) || Uteis.isAtributoPreenchido((Integer) funcionarioquinto_arquivoassinatura_codigo)) {
				expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura) ? funcionarioquinto_arquivoassinatura : funcionarioquinto_arquivoassinatura_codigo));
				if (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura_nome)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setNome((String) funcionarioquinto_arquivoassinatura_nome);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura_descricao)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setDescricao((String) funcionarioquinto_arquivoassinatura_descricao);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura_dataupload)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setDataUpload((Date) funcionarioquinto_arquivoassinatura_dataupload);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura_pastabasearquivo)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setPastaBaseArquivo((String) funcionarioquinto_arquivoassinatura_pastabasearquivo);
				}
				if (Uteis.isAtributoPreenchido(funcionarioquinto_arquivoassinatura_servidorarquivoonline)) {
					expedicaoDiploma.getFuncionarioQuartoVO().getArquivoAssinaturaVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf((String) funcionarioquinto_arquivoassinatura_servidorarquivoonline));
				}
			}
		}
		/**
		 * montagem dados do cargo do funcionario principal da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) cargofuncionarioprincipal_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargofuncionarioprincipal)) {
			expedicaoDiploma.getCargoFuncionarioPrincipalVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioprincipal) ? expedicaoDiploma_cargofuncionarioprincipal : cargofuncionarioprincipal_codigo));
			if (Uteis.isAtributoPreenchido(cargofuncionarioprincipal_nome)) {
				expedicaoDiploma.getCargoFuncionarioPrincipalVO().setNome((String) cargofuncionarioprincipal_nome);
			}
		}
		/**
		 * montagem dados do cargo do funcionario secundario da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) cargofuncionariosecundario_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargofuncionarioterceiro)) {
			expedicaoDiploma.getCargoFuncionarioSecundarioVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioterceiro) ? expedicaoDiploma_cargofuncionarioterceiro : cargofuncionariosecundario_codigo));
			if (Uteis.isAtributoPreenchido(cargofuncionariosecundario_nome)) {
				expedicaoDiploma.getCargoFuncionarioSecundarioVO().setNome((String) cargofuncionariosecundario_nome);
			}
		}
		/**
		 * montagem dados do cargo do funcionario terceiro da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) cargofuncionarioterceiro_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargofuncionarioterceiro)) {
			expedicaoDiploma.getCargoFuncionarioTerceiroVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioterceiro) ? expedicaoDiploma_cargofuncionarioterceiro : cargofuncionarioterceiro_codigo));
			if (Uteis.isAtributoPreenchido(cargofuncionarioterceiro_nome)) {
				expedicaoDiploma.getCargoFuncionarioTerceiroVO().setNome((String) cargofuncionarioterceiro_nome);
			}
		}
		/**
		 * montagem dados do cargo do funcionario quarto da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) cargofuncionarioquarto_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargofuncionarioquarto)) {
			expedicaoDiploma.getCargoFuncionarioQuartoVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioquarto) ? expedicaoDiploma_cargofuncionarioquarto : cargofuncionarioquarto_codigo));
			if (Uteis.isAtributoPreenchido(cargofuncionarioquarto_nome)) {
				expedicaoDiploma.getCargoFuncionarioQuartoVO().setNome((String) cargofuncionarioquarto_nome);
			}
		}
		/**
		 * montagem dados do cargo do funcionario quinto da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) cargofuncionarioquinto_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargofuncionarioquinto)) {
			expedicaoDiploma.getCargoFuncionarioQuintoVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargofuncionarioquinto) ? expedicaoDiploma_cargofuncionarioquinto : cargofuncionarioquinto_codigo));
			if (Uteis.isAtributoPreenchido(cargofuncionarioquinto_nome)) {
				expedicaoDiploma.getCargoFuncionarioQuintoVO().setNome((String) cargofuncionarioquinto_nome);
			}
		}
		/**
		 * montagem dados do responsavel cadastro da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) responsavelcadastro_codigo) || Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_responsavelcadastro)) {
			expedicaoDiploma.getResponsavelCadastro().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_responsavelcadastro) ? expedicaoDiploma_responsavelcadastro : responsavelcadastro_codigo));
			if (Uteis.isAtributoPreenchido(responsavelcadastro_nome)) {
				expedicaoDiploma.getResponsavelCadastro().setNome((String) responsavelcadastro_nome);
			}
			if (Uteis.isAtributoPreenchido(responsavelcadastro_pessoa)) {
				expedicaoDiploma.getResponsavelCadastro().getPessoa().setCodigo((Integer) responsavelcadastro_pessoa);
			}
		}
		/**
		 * montagem dados do reitor da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_reitorregistrodiplomaviaanterior) || Uteis.isAtributoPreenchido((Integer) reitorregistrodiplomaviaanterior_codigo)) {
			expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_reitorregistrodiplomaviaanterior) ? expedicaoDiploma_reitorregistrodiplomaviaanterior : reitorregistrodiplomaviaanterior_codigo));
			if (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_matricula)) {
				expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().setMatricula((String) reitorregistrodiplomaviaanterior_matricula);
			}
			if (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_pessoa)) {
				expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setCodigo((Integer) reitorregistrodiplomaviaanterior_pessoa);
			}
			if (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_escolaridade)) {
				expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().setEscolaridade((String) reitorregistrodiplomaviaanterior_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) reitorregistrodiplomaviaanterior_pessoa) || Uteis.isAtributoPreenchido((Integer) reitorregistrodiplomaviaanterior_pessoa_codigo)) {
				expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_pessoa) ? reitorregistrodiplomaviaanterior_pessoa : reitorregistrodiplomaviaanterior_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_pessoa_cpf)) {
					expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setCPF((String) reitorregistrodiplomaviaanterior_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(reitorregistrodiplomaviaanterior_pessoa_nome)) {
					expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setNome((String) reitorregistrodiplomaviaanterior_pessoa_nome);
				}
			}
		}
		/**
		 * montagem dados da secretaria registro da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_secretariaregistrodiplomaviaanterior) || Uteis.isAtributoPreenchido((Integer) secretariaregistrodiplomaviaanterior_codigo)) {
			expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_secretariaregistrodiplomaviaanterior) ? expedicaoDiploma_secretariaregistrodiplomaviaanterior : secretariaregistrodiplomaviaanterior_codigo));
			if (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_matricula)) {
				expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().setMatricula((String) secretariaregistrodiplomaviaanterior_matricula);
			}
			if (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_pessoa)) {
				expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setCodigo((Integer) secretariaregistrodiplomaviaanterior_pessoa);
			}
			if (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_escolaridade)) {
				expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().setEscolaridade((String) secretariaregistrodiplomaviaanterior_escolaridade);
			}
			if (Uteis.isAtributoPreenchido((Integer) reitorregistrodiplomaviaanterior_pessoa) || Uteis.isAtributoPreenchido((Integer) secretariaregistrodiplomaviaanterior_pessoa_codigo)) {
				expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_pessoa) ? secretariaregistrodiplomaviaanterior_pessoa : secretariaregistrodiplomaviaanterior_pessoa_codigo));
				if (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_pessoa_cpf)) {
					expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setCPF((String) secretariaregistrodiplomaviaanterior_pessoa_cpf);
				}
				if (Uteis.isAtributoPreenchido(secretariaregistrodiplomaviaanterior_pessoa_nome)) {
					expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setNome((String) secretariaregistrodiplomaviaanterior_pessoa_nome);
				}
			}
		}
		/**
		 * montagem dados do cargo do reitor da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cargoreitorregistrodiplomaviaanterior) || Uteis.isAtributoPreenchido((Integer) cargoreitorregistrodiplomaviaanterior_codigo)) {
			expedicaoDiploma.getCargoReitorRegistroDiplomaViaAnterior().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cargoreitorregistrodiplomaviaanterior) ? expedicaoDiploma_cargoreitorregistrodiplomaviaanterior : cargoreitorregistrodiplomaviaanterior_codigo));
			if (Uteis.isAtributoPreenchido(cargoreitorregistrodiplomaviaanterior_nome)) {
				expedicaoDiploma.getCargoReitorRegistroDiplomaViaAnterior().setNome((String) cargoreitorregistrodiplomaviaanterior_nome);
			}
		}
		/**
		 * montagem dados da matricula da expedição diploma
		 */
		if (Uteis.isAtributoPreenchido((String) matricula_matricula) || Uteis.isAtributoPreenchido((String) expedicaoDiploma_matricula)) {
			expedicaoDiploma.getMatricula().setMatricula((String) (Uteis.isAtributoPreenchido(expedicaoDiploma_matricula) ? expedicaoDiploma_matricula : matricula_matricula));
			if (Uteis.isAtributoPreenchido(matricula_turno)) {
				expedicaoDiploma.getMatricula().getTurno().setCodigo((Integer) matricula_turno);
			}
			if (Uteis.isAtributoPreenchido(matricula_usuario)) {
				expedicaoDiploma.getMatricula().getUsuario().setCodigo((Integer) matricula_usuario);
			}
			if (Uteis.isAtributoPreenchido(matricula_inscricao)) {
				expedicaoDiploma.getMatricula().getInscricao().setCodigo((Integer) matricula_inscricao);
			}
			if (Uteis.isAtributoPreenchido(matricula_situacaofinanceira)) {
				expedicaoDiploma.getMatricula().setSituacaoFinanceira((String) matricula_situacaofinanceira);
			}
			if (Uteis.isAtributoPreenchido(matricula_situacao)) {
				expedicaoDiploma.getMatricula().setSituacao((String) matricula_situacao);
			}
			if (Uteis.isAtributoPreenchido(matricula_data)) {
				expedicaoDiploma.getMatricula().setData((Date) matricula_data);
			}
			if (Uteis.isAtributoPreenchido(matricula_curso)) {
				expedicaoDiploma.getMatricula().getCurso().setCodigo((Integer) matricula_curso);
			}
			if (Uteis.isAtributoPreenchido(matricula_unidadeensino)) {
				expedicaoDiploma.getMatricula().getUnidadeEnsino().setCodigo((Integer) matricula_unidadeensino);
			}
			if (Uteis.isAtributoPreenchido(matricula_aluno)) {
				expedicaoDiploma.getMatricula().getAluno().setCodigo((Integer) matricula_aluno);
			}
			if (Uteis.isAtributoPreenchido(matricula_tranferenciaentrada)) {
				expedicaoDiploma.getMatricula().getTransferenciaEntrada().setCodigo((Integer) matricula_tranferenciaentrada);
			}
			if (Uteis.isAtributoPreenchido(matricula_formaingresso)) {
				expedicaoDiploma.getMatricula().setFormaIngresso((String) matricula_formaingresso);
			}
			if (Uteis.isAtributoPreenchido(matricula_atividadecomplementar)) {
				expedicaoDiploma.getMatricula().setAtividadeComplementar((String) matricula_atividadecomplementar);
			}
			if (Uteis.isAtributoPreenchido(matricula_anoingresso)) {
				expedicaoDiploma.getMatricula().setAnoIngresso((String) matricula_anoingresso);
			}
			if (Uteis.isAtributoPreenchido(matricula_semestreingresso)) {
				expedicaoDiploma.getMatricula().setSemestreIngresso((String) matricula_semestreingresso);
			}
			if (Uteis.isAtributoPreenchido(matricula_anoconclusao)) {
				expedicaoDiploma.getMatricula().setAnoConclusao((String) matricula_anoconclusao);
			}
			if (Uteis.isAtributoPreenchido(matricula_semestreconclusao)) {
				expedicaoDiploma.getMatricula().setSemestreConclusao((String) matricula_semestreconclusao);
			}
			if (Uteis.isAtributoPreenchido(matricula_disciplinasprocseletivo)) {
				expedicaoDiploma.getMatricula().setDisciplinasProcSeletivo((String) matricula_disciplinasprocseletivo);
			}
			if (matricula_fezenade != null) {
				expedicaoDiploma.getMatricula().setFezEnade((Boolean) matricula_fezenade);
			}
			if (Uteis.isAtributoPreenchido(matricula_dataenade)) {
				expedicaoDiploma.getMatricula().setDataEnade((Date) matricula_dataenade);
			}
			if (Uteis.isAtributoPreenchido(matricula_notaenade)) {
				expedicaoDiploma.getMatricula().setNotaEnade((Double) matricula_notaenade);
			}
			if (Uteis.isAtributoPreenchido(matricula_enade)) {
				expedicaoDiploma.getMatricula().getEnadeVO().setCodigo((Integer) matricula_enade);
			}
			if (matricula_alunoabandonoucurso != null) {
				expedicaoDiploma.getMatricula().setAlunoAbandonouCurso((Boolean) matricula_alunoabandonoucurso);
			}
			if (Uteis.isAtributoPreenchido(matricula_observacaocomplementar)) {
				expedicaoDiploma.getMatricula().setObservacaoComplementar((String) matricula_observacaocomplementar);
			}
			if (Uteis.isAtributoPreenchido(matricula_localarmazenamentodocumentosmatricula)) {
				expedicaoDiploma.getMatricula().setLocalArmazenamentoDocumentosMatricula((String) matricula_localarmazenamentodocumentosmatricula);
			}
			if (Uteis.isAtributoPreenchido(matricula_datainiciocurso)) {
				expedicaoDiploma.getMatricula().setDataInicioCurso((Date) matricula_datainiciocurso);
			}
			if (Uteis.isAtributoPreenchido(matricula_dataconclusaocurso)) {
				expedicaoDiploma.getMatricula().setDataConclusaoCurso((Date) matricula_dataconclusaocurso);
			}
			if (Uteis.isAtributoPreenchido(matricula_formacaoacademica)) {
				expedicaoDiploma.getMatricula().getFormacaoAcademica().setCodigo((Integer) matricula_formacaoacademica);
			}
			if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso)) {
				expedicaoDiploma.getMatricula().getAutorizacaoCurso().setCodigo((Integer) matricula_autorizacaocurso);
			}
			if (Uteis.isAtributoPreenchido(matricula_tipomatricula)) {
				expedicaoDiploma.getMatricula().setTipoMatricula((String) matricula_tipomatricula);
			}
			if (matricula_matriculasuspensa != null) {
				expedicaoDiploma.getMatricula().setMatriculaSuspensa((Boolean) matricula_matriculasuspensa);
			}
			if (Uteis.isAtributoPreenchido(matricula_databasesuspensao)) {
				expedicaoDiploma.getMatricula().setDataBaseSuspensao((Date) matricula_databasesuspensao);
			}
			if (Uteis.isAtributoPreenchido(matricula_datacolacaograu)) {
				expedicaoDiploma.getMatricula().setDataColacaoGrau((Date) matricula_datacolacaograu);
			}
			if (Uteis.isAtributoPreenchido(matricula_mesingresso)) {
				expedicaoDiploma.getMatricula().setMesIngresso((String) matricula_mesingresso);
			}
			if (Uteis.isAtributoPreenchido(matricula_gradecurricularatual)) {
				expedicaoDiploma.getMatricula().getGradeCurricularAtual().setCodigo((Integer) matricula_gradecurricularatual);
			}
			if (Uteis.isAtributoPreenchido(matricula_observacaodiploma)) {
				expedicaoDiploma.getMatricula().setObservacaoDiploma((String) matricula_observacaodiploma);
			}
			if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento)) {
				expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setCodigo((Integer) matricula_renovacaoreconhecimento);
			}
			if (Uteis.isAtributoPreenchido(matricula_notaenem)) {
				if (matricula_notaenem instanceof BigDecimal) {
					expedicaoDiploma.getMatricula().setNotaEnem(Double.valueOf(((BigDecimal) matricula_notaenem).toString()));
				} else if (matricula_notaenem instanceof Float) {
					expedicaoDiploma.getMatricula().setNotaEnem(Double.valueOf((Float) matricula_notaenem));
				} else {
					expedicaoDiploma.getMatricula().setNotaEnem((Double) matricula_notaenem);
				}
			}
			if (matricula_matriculaserasa != null) {
				expedicaoDiploma.getMatricula().setMatriculaSerasa((Boolean) matricula_matriculaserasa);
			}
			if (Uteis.isAtributoPreenchido(matricula_consultor)) {
				expedicaoDiploma.getMatricula().getConsultor().setCodigo((Integer) matricula_consultor);
			}
			if (Uteis.isAtributoPreenchido(matricula_tipotrabalhoconclusaocurso)) {
				expedicaoDiploma.getMatricula().setTipoTrabalhoConclusaoCurso((String) matricula_tipotrabalhoconclusaocurso);
			}
			if (matricula_naoenviarmensagemcobranca != null) {
				expedicaoDiploma.getMatricula().setNaoEnviarMensagemCobranca((Boolean) matricula_naoenviarmensagemcobranca);
			}
			if (matricula_alunoconcluiudisciplinasregulares != null) {
				expedicaoDiploma.getMatricula().setAlunoConcluiuDisciplinasRegulares((Boolean) matricula_alunoconcluiudisciplinasregulares);
			}
			if (Uteis.isAtributoPreenchido(matricula_qtddiasadiarbloqueio)) {
				expedicaoDiploma.getMatricula().setQtdDiasAdiarBloqueio((Integer) matricula_qtddiasadiarbloqueio);
			}
			if (matricula_canceladofinanceiro != null) {
				expedicaoDiploma.getMatricula().setCanceladoFinanceiro((Boolean) matricula_canceladofinanceiro);
			}
			if (Uteis.isAtributoPreenchido(matricula_dataemissaohistorico)) {
				expedicaoDiploma.getMatricula().setDataEmissaoHistorico((Date) matricula_dataemissaohistorico);
			}
			if (Uteis.isAtributoPreenchido(matricula_codigofinanceiromatricula)) {
				expedicaoDiploma.getMatricula().setCodigoFinanceiroMatricula((String) matricula_codigofinanceiromatricula);
			}
			if (matricula_bloqueioporsolicitacaoliberacaomatricula != null) {
				expedicaoDiploma.getMatricula().setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica((Boolean) matricula_bloqueioporsolicitacaoliberacaomatricula);
			}
			if (Uteis.isAtributoPreenchido(matricula_dataprocessoseletivo)) {
				expedicaoDiploma.getMatricula().setDataProcessoSeletivo((Date) matricula_dataprocessoseletivo);
			}
			if (Uteis.isAtributoPreenchido(matricula_totalpontoprocseletivo)) {
				if (matricula_totalpontoprocseletivo instanceof BigDecimal) {
					expedicaoDiploma.getMatricula().setTotalPontoProcSeletivo(Double.valueOf(((BigDecimal) matricula_totalpontoprocseletivo).toString()));
				} else if (matricula_totalpontoprocseletivo instanceof Float) {
					expedicaoDiploma.getMatricula().setTotalPontoProcSeletivo(Double.valueOf((Float) matricula_totalpontoprocseletivo));
				} else {
					expedicaoDiploma.getMatricula().setTotalPontoProcSeletivo((Double) matricula_totalpontoprocseletivo);
				}
			}
			if (Uteis.isAtributoPreenchido(matricula_classificacaoingresso)) {
				expedicaoDiploma.getMatricula().setClassificacaoIngresso((Integer) matricula_classificacaoingresso);
			}
			if (Uteis.isAtributoPreenchido(matricula_notamonografia)) {
				if (matricula_notamonografia instanceof BigDecimal) {
					expedicaoDiploma.getMatricula().setNotaMonografia(Double.valueOf(((BigDecimal) matricula_notamonografia).toString()));
				} else if (matricula_notamonografia instanceof Float) {
					expedicaoDiploma.getMatricula().setNotaMonografia(Double.valueOf((Float) matricula_notamonografia));
				} else {
					expedicaoDiploma.getMatricula().setNotaMonografia((Double) matricula_notamonografia);
				}
			}
			if (Uteis.isAtributoPreenchido(matricula_nomemonografia)) {
				expedicaoDiploma.getMatricula().setTituloMonografia((String) matricula_nomemonografia);
			}
			if (Uteis.isAtributoPreenchido(matricula_orientadormonografia)) {
				expedicaoDiploma.getMatricula().setOrientadorMonografia((String) matricula_orientadormonografia);
			}
			if (Uteis.isAtributoPreenchido(matricula_cargahorariamonografia)) {
				expedicaoDiploma.getMatricula().setCargaHorariaMonografia((Integer) matricula_cargahorariamonografia);
			}
			if (Uteis.isAtributoPreenchido(matricula_titulacaoorientadormonografia)) {
				expedicaoDiploma.getMatricula().setTitulacaoOrientadorMonografia((String) matricula_titulacaoorientadormonografia);
			}
			if (matricula_permitirinclusaoexclusaodisciplinasrenovacao != null) {
				expedicaoDiploma.getMatricula().setPermitirInclusaoExclusaoDisciplinasRenovacao((Boolean) matricula_permitirinclusaoexclusaodisciplinasrenovacao);;
			}
			if (Uteis.isAtributoPreenchido(matricula_localprocessoseletivo)) {
				expedicaoDiploma.getMatricula().setLocalProcessoSeletivo((String) matricula_localprocessoseletivo);
			}
			if (Uteis.isAtributoPreenchido(matricula_horascomplementares)) {
				if (matricula_horascomplementares instanceof BigDecimal) {
					expedicaoDiploma.getMatricula().setHorasComplementares(Double.valueOf(((BigDecimal) matricula_horascomplementares).toString()));
				} else if (matricula_horascomplementares instanceof Float) {
					expedicaoDiploma.getMatricula().setHorasComplementares(Double.valueOf((Float) matricula_horascomplementares));
				} else if (matricula_horascomplementares instanceof Integer) {
					expedicaoDiploma.getMatricula().setHorasComplementares(Double.valueOf((Integer) matricula_horascomplementares));
				} else {
					expedicaoDiploma.getMatricula().setHorasComplementares((Double) matricula_horascomplementares);
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_aluno_codigo) || Uteis.isAtributoPreenchido((Integer) matricula_aluno)) {
				expedicaoDiploma.getMatricula().getAluno().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_aluno) ? matricula_aluno : matricula_aluno_codigo));
				if (Uteis.isAtributoPreenchido(matricula_aluno_tituloeleitoral)) {
					expedicaoDiploma.getMatricula().getAluno().setTituloEleitoral((String) matricula_aluno_tituloeleitoral);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_orgaoemissor)) {
					expedicaoDiploma.getMatricula().getAluno().setOrgaoEmissor((String) matricula_aluno_orgaoemissor);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_estadoemissaorg)) {
					expedicaoDiploma.getMatricula().getAluno().setEstadoEmissaoRG((String) matricula_aluno_estadoemissaorg);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_dataemissaorg)) {
					expedicaoDiploma.getMatricula().getAluno().setDataEmissaoRG((Date) matricula_aluno_dataemissaorg);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_rg)) {
					expedicaoDiploma.getMatricula().getAluno().setRG((String) matricula_aluno_rg);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_cpf)) {
					expedicaoDiploma.getMatricula().getAluno().setCPF((String) matricula_aluno_cpf);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_nacionalidade)) {
					expedicaoDiploma.getMatricula().getAluno().getNacionalidade().setCodigo((Integer) matricula_aluno_nacionalidade);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade)) {
					expedicaoDiploma.getMatricula().getAluno().getNaturalidade().setCodigo((Integer) matricula_aluno_naturalidade);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_datanasc)) {
					expedicaoDiploma.getMatricula().getAluno().setDataNasc((Date) matricula_aluno_datanasc);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_email)) {
					expedicaoDiploma.getMatricula().getAluno().setEmail((String) matricula_aluno_email);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_celular)) {
					expedicaoDiploma.getMatricula().getAluno().setCelular((String) matricula_aluno_celular);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_telefonerecado)) {
					expedicaoDiploma.getMatricula().getAluno().setTelefoneRecado((String) matricula_aluno_telefonerecado);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_telefoneres)) {
					expedicaoDiploma.getMatricula().getAluno().setTelefoneRes((String) matricula_aluno_telefoneres);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_telefonecomer)) {
					expedicaoDiploma.getMatricula().getAluno().setTelefoneComer((String) matricula_aluno_telefonecomer);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_estadocivil)) {
					expedicaoDiploma.getMatricula().getAluno().setEstadoCivil((String) matricula_aluno_estadocivil);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_sexo)) {
					expedicaoDiploma.getMatricula().getAluno().setSexo((String) matricula_aluno_sexo);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_cidade)) {
					expedicaoDiploma.getMatricula().getAluno().getCidade().setCodigo((Integer) matricula_aluno_cidade);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_complemento)) {
					expedicaoDiploma.getMatricula().getAluno().setComplemento((String) matricula_aluno_complemento);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_cep)) {
					expedicaoDiploma.getMatricula().getAluno().setCEP((String) matricula_aluno_cep);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_numero)) {
					expedicaoDiploma.getMatricula().getAluno().setNumero((String) matricula_aluno_numero);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_setor)) {
					expedicaoDiploma.getMatricula().getAluno().setSetor((String) matricula_aluno_setor);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_endereco)) {
					expedicaoDiploma.getMatricula().getAluno().setEndereco((String) matricula_aluno_endereco);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_nome)) {
					expedicaoDiploma.getMatricula().getAluno().setNome((String) matricula_aluno_nome);
				}
				if (matricula_aluno_aluno != null) {
					expedicaoDiploma.getMatricula().getAluno().setAluno((Boolean) matricula_aluno_aluno);
				}
				if (matricula_aluno_professor != null) {
					expedicaoDiploma.getMatricula().getAluno().setProfessor((Boolean) matricula_aluno_professor);
				}
				if (matricula_aluno_funcionario != null) {
					expedicaoDiploma.getMatricula().getAluno().setFuncionario((Boolean) matricula_aluno_funcionario);
				}
				if (matricula_aluno_candidato != null) {
					expedicaoDiploma.getMatricula().getAluno().setCandidato((Boolean) matricula_aluno_candidato);
				}
				if (matricula_aluno_ativo != null) {
					expedicaoDiploma.getMatricula().getAluno().setAtivo((Boolean) matricula_aluno_ativo);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_arquivoimagem)) {
					expedicaoDiploma.getMatricula().getAluno().getArquivoImagem().setCodigo((Integer) matricula_aluno_arquivoimagem);
				}
				if (matricula_aluno_coordenador != null) {
					expedicaoDiploma.getMatricula().getAluno().setCoordenador((Boolean) matricula_aluno_coordenador);
				}
				if (Uteis.isAtributoPreenchido(matricula_aluno_nomebatismo)) {
					expedicaoDiploma.getMatricula().getAluno().setNomeBatismo((String) matricula_aluno_nomebatismo);
				}
//				if (Uteis.isAtributoPreenchido(matricula_aluno_utilizardocumentoestrangeiro)) {
//					expedicaoDiploma.getMatricula().getAluno().setUtilizarDocumentoEstrangeiro((Boolean) matricula_aluno_utilizardocumentoestrangeiro);
//				}
//				if (Uteis.isAtributoPreenchido(matricula_aluno_tipodocumentoestrangeiro)) {
//					expedicaoDiploma.getMatricula().getAluno().setTipoDocumentoEstrangeiro(TipoDocumentoEstrangeiroEnum.valueOf((String) matricula_aluno_tipodocumentoestrangeiro));
//				}
//				if (Uteis.isAtributoPreenchido(matricula_aluno_numerodocumentoestrangeiro)) {
//					expedicaoDiploma.getMatricula().getAluno().setNumeroDocumentoEstrangeiro((String) matricula_aluno_numerodocumentoestrangeiro);
//				}
				if (Uteis.isAtributoPreenchido((Integer) matricula_aluno_nacionalidade) || Uteis.isAtributoPreenchido((Integer) matricula_aluno_nacionalidade_codigo)) {
					expedicaoDiploma.getMatricula().getAluno().getNacionalidade().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_aluno_nacionalidade) ? matricula_aluno_nacionalidade : matricula_aluno_nacionalidade_codigo));
					if (Uteis.isAtributoPreenchido(matricula_aluno_nacionalidade_nome)) {
						expedicaoDiploma.getMatricula().getAluno().getNacionalidade().setNome((String) matricula_aluno_nacionalidade_nome);
					}
					if (Uteis.isAtributoPreenchido(matricula_aluno_nacionalidade_nacionalidade)) {
						expedicaoDiploma.getMatricula().getAluno().getNacionalidade().setNacionalidade((String) matricula_aluno_nacionalidade_nacionalidade);
					}
				}
				if (Uteis.isAtributoPreenchido((Integer) matricula_aluno_naturalidade) || Uteis.isAtributoPreenchido((Integer) matricula_aluno_naturalidade_codigo)) {
					expedicaoDiploma.getMatricula().getAluno().getNaturalidade().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade) ? matricula_aluno_naturalidade : matricula_aluno_naturalidade_codigo));
					if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_nome)) {
						expedicaoDiploma.getMatricula().getAluno().getNaturalidade().setNome((String) matricula_aluno_naturalidade_nome);
					}
					if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_codigoibge)) {
						expedicaoDiploma.getMatricula().getAluno().getNaturalidade().setCodigoIBGE((String) matricula_aluno_naturalidade_codigoibge);
					}
					if (Uteis.isAtributoPreenchido((Integer) matricula_aluno_naturalidade_estado) || Uteis.isAtributoPreenchido((Integer) matricula_aluno_naturalidade_estado_codigo)) {
						expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getEstado().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_estado) ? matricula_aluno_naturalidade_estado : matricula_aluno_naturalidade_estado_codigo));
						if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_estado_nome)) {
							expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getEstado().setNome((String) matricula_aluno_naturalidade_estado_nome);
						}
						if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_estado_codigoibge)) {
							expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getEstado().setCodigoIBGE((String) matricula_aluno_naturalidade_estado_codigoibge);
						}
						if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_estado_sigla)) {
							expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getEstado().setSigla((String) matricula_aluno_naturalidade_estado_sigla);
						}
						if (Uteis.isAtributoPreenchido(matricula_aluno_naturalidade_estado_paiz)) {
							expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getEstado().getPaiz().setCodigo((Integer) matricula_aluno_naturalidade_estado_paiz);
						}
					}
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_autorizacaocurso) || Uteis.isAtributoPreenchido((Integer) matricula_autorizacaocurso_codigo)) {
				expedicaoDiploma.getMatricula().getAutorizacaoCurso().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_autorizacaocurso) ? matricula_autorizacaocurso : matricula_autorizacaocurso_codigo));
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_nome)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setNome((String) matricula_autorizacaocurso_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_data)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setData((Date) matricula_autorizacaocurso_data);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_curso)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setCurso((Integer) matricula_autorizacaocurso_curso);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_tipoautorizacaocursoenum)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf((String) matricula_autorizacaocurso_tipoautorizacaocursoenum));
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_numero)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setNumero((String) matricula_autorizacaocurso_numero);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_datacredenciamento)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setDataCredenciamento((Date) matricula_autorizacaocurso_datacredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_veiculopublicacao)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setVeiculoPublicacao((String) matricula_autorizacaocurso_veiculopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_secaopublicacao)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setSecaoPublicacao((Integer) matricula_autorizacaocurso_secaopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_paginapublicacao)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setPaginaPublicacao((Integer) matricula_autorizacaocurso_paginapublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_numerodou)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setNumeroDOU((Integer) matricula_autorizacaocurso_numerodou);
				}
				if (matricula_autorizacaocurso_emtramitacao != null) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setEmTramitacao((Boolean) matricula_autorizacaocurso_emtramitacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_datacadastro)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setDataCadastro((Date) matricula_autorizacaocurso_datacadastro);
				}
				if (Uteis.isAtributoPreenchido(matricula_autorizacaocurso_dataprotocolo)) {
					expedicaoDiploma.getMatricula().getAutorizacaoCurso().setDataProtocolo((Date) matricula_autorizacaocurso_dataprotocolo);
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_renovacaoreconhecimento) || Uteis.isAtributoPreenchido((Integer) matricula_renovacaoreconhecimento_codigo)) {
				expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento) ? matricula_renovacaoreconhecimento : matricula_renovacaoreconhecimento_codigo));
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_nome)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setNome((String) matricula_renovacaoreconhecimento_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_data)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setData((Date) matricula_renovacaoreconhecimento_data);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_curso)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setCurso((Integer) matricula_renovacaoreconhecimento_curso);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_tipoautorizacaocursoenum)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf((String) matricula_renovacaoreconhecimento_tipoautorizacaocursoenum));
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_numero)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setNumero((String) matricula_renovacaoreconhecimento_numero);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_datacredenciamento)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setDataCredenciamento((Date) matricula_renovacaoreconhecimento_datacredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_veiculopublicacao)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setVeiculoPublicacao((String) matricula_renovacaoreconhecimento_veiculopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_secaopublicacao)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setSecaoPublicacao((Integer) matricula_renovacaoreconhecimento_secaopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_paginapublicacao)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setPaginaPublicacao((Integer) matricula_renovacaoreconhecimento_paginapublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_numerodou)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setNumeroDOU((Integer) matricula_renovacaoreconhecimento_numerodou);
				}
				if (matricula_renovacaoreconhecimento_emtramitacao != null) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setEmTramitacao((Boolean) matricula_renovacaoreconhecimento_emtramitacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_datacadastro)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setDataCadastro((Date) matricula_renovacaoreconhecimento_datacadastro);
				}
				if (Uteis.isAtributoPreenchido(matricula_renovacaoreconhecimento_dataprotocolo)) {
					expedicaoDiploma.getMatricula().getRenovacaoReconhecimentoVO().setDataProtocolo((Date) matricula_renovacaoreconhecimento_dataprotocolo);
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_unidadeensino_codigo) || Uteis.isAtributoPreenchido((Integer) matricula_unidadeensino)) {
				expedicaoDiploma.getMatricula().getUnidadeEnsino().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_unidadeensino) ? matricula_unidadeensino : matricula_unidadeensino_codigo));
				if (matricula_unidadeensino_matriz != null) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setMatriz((Boolean) matricula_unidadeensino_matriz);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_email)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setEmail((String) matricula_unidadeensino_email);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cnpj)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCNPJ((String) matricula_unidadeensino_cnpj);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_tipoempresa)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setTipoEmpresa((String) matricula_unidadeensino_tipoempresa);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cep)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCEP((String) matricula_unidadeensino_cep);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cidade)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().getCidade().setCodigo((Integer) matricula_unidadeensino_cidade);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_complemento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setComplemento((String) matricula_unidadeensino_complemento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numero)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumero((String) matricula_unidadeensino_numero);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_setor)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setSetor((String) matricula_unidadeensino_setor);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_endereco)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setEndereco((String) matricula_unidadeensino_endereco);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_razaosocial)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setRazaoSocial((String) matricula_unidadeensino_razaosocial);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_nome)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNome((String) matricula_unidadeensino_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_abreviatura)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setAbreviatura((String) matricula_unidadeensino_abreviatura);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_configuracoes)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().getConfiguracoes().setCodigo((Integer) matricula_unidadeensino_configuracoes);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_codigoies)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCodigoIES((Integer) matricula_unidadeensino_codigoies);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_credenciamentoportaria)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCredenciamentoPortaria((String) matricula_unidadeensino_credenciamentoportaria);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_datapublicacaodo)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setDataPublicacaoDO((Date) matricula_unidadeensino_datapublicacaodo);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_mantenedora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setMantenedora((String) matricula_unidadeensino_mantenedora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_credenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCredenciamento((String) matricula_unidadeensino_credenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_nomeexpedicaodiploma)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNomeExpedicaoDiploma((String) matricula_unidadeensino_nomeexpedicaodiploma);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_codigoiesmantenedora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCodigoIESMantenedora((Integer) matricula_unidadeensino_codigoiesmantenedora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cnpjmantenedora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCnpjMantenedora((String) matricula_unidadeensino_cnpjmantenedora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_unidadecertificadora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setUnidadeCertificadora((String) matricula_unidadeensino_unidadecertificadora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cnpjunidadecertificadora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCnpjUnidadeCertificadora((String) matricula_unidadeensino_cnpjunidadecertificadora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_codigoiesunidadecertificadora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCodigoIESUnidadeCertificadora((Integer) matricula_unidadeensino_codigoiesunidadecertificadora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_configuracaoged)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().getConfiguracaoGEDVO().setCodigo((Integer) matricula_unidadeensino_configuracaoged);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_tipoautorizacaoenum)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setTipoAutorizacaoEnum(TipoAutorizacaoCursoEnum.valueOf((String) matricula_unidadeensino_tipoautorizacaoenum));
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numerocredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroCredenciamento((String) matricula_unidadeensino_numerocredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_datacredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setDataCredenciamento((Date) matricula_unidadeensino_datacredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_veiculopublicacaocredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setVeiculoPublicacaoCredenciamento((String) matricula_unidadeensino_veiculopublicacaocredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_secaopublicacaocredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setSecaoPublicacaoCredenciamento((Integer) matricula_unidadeensino_secaopublicacaocredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_paginapublicacaocredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setPaginaPublicacaoCredenciamento((Integer) matricula_unidadeensino_paginapublicacaocredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numerodoucredenciamento)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroDOUCredenciamento((Integer) matricula_unidadeensino_numerodoucredenciamento);
				}
				if (matricula_unidadeensino_informardadosregistradora != null) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setInformarDadosRegistradora((Boolean) matricula_unidadeensino_informardadosregistradora);
				}
				if (mat_uni_utilizarenderecounidadeensinoregistradora != null) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setUtilizarEnderecoUnidadeEnsinoRegistradora((Boolean) mat_uni_utilizarenderecounidadeensinoregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cepregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCepRegistradora((String) matricula_unidadeensino_cepregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cidaderegistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().getCidadeRegistradora().setCodigo((Integer) matricula_unidadeensino_cidaderegistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_complementoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setComplementoRegistradora((String) matricula_unidadeensino_complementoregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_bairroregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setBairroMantenedoraRegistradora((String) matricula_unidadeensino_bairroregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_enderecoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setEnderecoRegistradora((String) matricula_unidadeensino_enderecoregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numeroregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroRegistradora((String) matricula_unidadeensino_numeroregistradora);
				}
				if (matricula_unidadeensino_utilizarcredenciamentounidadeensino != null) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setUtilizarCredenciamentoUnidadeEnsino((Boolean) matricula_unidadeensino_utilizarcredenciamentounidadeensino);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numerocredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroCredenciamentoRegistradora((String) matricula_unidadeensino_numerocredenciamentoregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_datacredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setDataCredenciamentoRegistradora((Date) matricula_unidadeensino_datacredenciamentoregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_datapublicacaodoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setDataPublicacaoDORegistradora((Date) matricula_unidadeensino_datapublicacaodoregistradora);
				}
				if (Uteis.isAtributoPreenchido(mat_uni_veiculopublicacaocredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setVeiculoPublicacaoCredenciamentoRegistradora((String) mat_uni_veiculopublicacaocredenciamentoregistradora);
				}
				if (Uteis.isAtributoPreenchido(mat_uni_secaopublicacaocredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setSecaoPublicacaoCredenciamentoRegistradora((Integer) mat_uni_secaopublicacaocredenciamentoregistradora);
				}
				if (Uteis.isAtributoPreenchido(mat_uni_paginapublicacaocredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setPaginaPublicacaoCredenciamentoRegistradora((Integer) mat_uni_paginapublicacaocredenciamentoregistradora);
				}
				if (Uteis.isAtributoPreenchido(mat_uni_numeropublicacaocredenciamentoregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroPublicacaoCredenciamentoRegistradora((Integer) mat_uni_numeropublicacaocredenciamentoregistradora);
				}
				if (matricula_unidadeensino_utilizarmantenedoraunidadeensino != null) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setUtilizarMantenedoraUnidadeEnsino((Boolean) matricula_unidadeensino_utilizarmantenedoraunidadeensino);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_mantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setMantenedoraRegistradora((String) matricula_unidadeensino_mantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cnpjmantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCnpjMantenedoraRegistradora((String) matricula_unidadeensino_cnpjmantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cepmantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setCepMantenedoraRegistradora((String) matricula_unidadeensino_cepmantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_enderecomantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setEnderecoMantenedoraRegistradora((String) matricula_unidadeensino_enderecomantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_numeromantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setNumeroMantenedoraRegistradora((String) matricula_unidadeensino_numeromantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_cidademantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().getCidadeMantenedoraRegistradora().setCodigo((Integer) matricula_unidadeensino_cidademantenedoraregistradora);
				}
				if (Uteis.isAtributoPreenchido(matricula_unidadeensino_complementomantenedoraregistradora)) {
					expedicaoDiploma.getMatricula().getUnidadeEnsino().setComplementoMantenedoraRegistradora((String) matricula_unidadeensino_complementomantenedoraregistradora);
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_turno_codigo) || Uteis.isAtributoPreenchido((Integer) matricula_turno)) {
				expedicaoDiploma.getMatricula().getTurno().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_turno) ? matricula_turno : matricula_turno_codigo));
				if (Uteis.isAtributoPreenchido(matricula_turno_duracaoaula)) {
					expedicaoDiploma.getMatricula().getTurno().setDuracaoAula((Integer) matricula_turno_duracaoaula);
				}
				if (Uteis.isAtributoPreenchido(matricula_turno_nraulas)) {
					expedicaoDiploma.getMatricula().getTurno().setNrAulas((Integer) matricula_turno_nraulas);
				}
				if (Uteis.isAtributoPreenchido(matricula_turno_nome)) {
					expedicaoDiploma.getMatricula().getTurno().setNome((String) matricula_turno_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_turno_tipohorario)) {
					expedicaoDiploma.getMatricula().getTurno().setTipoHorario(TipoHorarioEnum.valueOf((String) matricula_turno_tipohorario));
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_curso_codigo) || Uteis.isAtributoPreenchido((Integer) matricula_curso)) {
				expedicaoDiploma.getMatricula().getCurso().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_curso) ? matricula_curso : matricula_curso_codigo));
				if (Uteis.isAtributoPreenchido(matricula_curso_configuracaoacademico)) {
					expedicaoDiploma.getMatricula().getCurso().getConfiguracaoAcademico().setCodigo((Integer) matricula_curso_configuracaoacademico);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_areaconhecimento)) {
					expedicaoDiploma.getMatricula().getCurso().getAreaConhecimento().setCodigo((Integer) matricula_curso_areaconhecimento);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_periodicidade)) {
					expedicaoDiploma.getMatricula().getCurso().setPeriodicidade((String) matricula_curso_periodicidade);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datacriacao)) {
					expedicaoDiploma.getMatricula().getCurso().setDataCriacao((Date) matricula_curso_datacriacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_nrregistrointerno)) {
					expedicaoDiploma.getMatricula().getCurso().setNrRegistroInterno((String) matricula_curso_nrregistrointerno);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datapublicacaodo)) {
					expedicaoDiploma.getMatricula().getCurso().setDataPublicacaoDO((Date) matricula_curso_datapublicacaodo);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_nrperiodoletivo)) {
					expedicaoDiploma.getMatricula().getCurso().setNrPeriodoLetivo((Integer) matricula_curso_nrperiodoletivo);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_niveleducacional)) {
					expedicaoDiploma.getMatricula().getCurso().setNivelEducacional((String) matricula_curso_niveleducacional);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_publicoalvo)) {
					expedicaoDiploma.getMatricula().getCurso().setPublicoAlvo((String) matricula_curso_publicoalvo);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_descricao)) {
					expedicaoDiploma.getMatricula().getCurso().setDescricao((String) matricula_curso_descricao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_objetivos)) {
					expedicaoDiploma.getMatricula().getCurso().setObjetivos((String) matricula_curso_objetivos);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_nome)) {
					expedicaoDiploma.getMatricula().getCurso().setNome((String) matricula_curso_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_titulo)) {
					expedicaoDiploma.getMatricula().getCurso().setTitulo((String) matricula_curso_titulo);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_abreviatura)) {
					expedicaoDiploma.getMatricula().getCurso().setAbreviatura((String) matricula_curso_abreviatura);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_titulacaodoformando)) {
					expedicaoDiploma.getMatricula().getCurso().setTitulacaoDoFormando((String) matricula_curso_titulacaodoformando);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_titulacaodoformandofeminino)) {
					expedicaoDiploma.getMatricula().getCurso().setTitulacaoDoFormandoFeminino((String) matricula_curso_titulacaodoformandofeminino);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_nomedocumentacao)) {
					expedicaoDiploma.getMatricula().getCurso().setNomeDocumentacao((String) matricula_curso_nomedocumentacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_titulacaomasculinoapresentardiploma)) {
					expedicaoDiploma.getMatricula().getCurso().setTitulacaoMasculinoApresentarDiploma((String) matricula_curso_titulacaomasculinoapresentardiploma);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_titulacaofemininoapresentardiploma)) {
					expedicaoDiploma.getMatricula().getCurso().setTitulacaoFemininoApresentarDiploma((String) matricula_curso_titulacaofemininoapresentardiploma);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_modalidadecurso)) {
					expedicaoDiploma.getMatricula().getCurso().setModalidadeCurso(ModalidadeDisciplinaEnum.valueOf((String) matricula_curso_modalidadecurso));
				}
				if (matricula_curso_possuicodigoemec != null) {
					expedicaoDiploma.getMatricula().getCurso().setPossuiCodigoEMEC((Boolean) matricula_curso_possuicodigoemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_codigoemec)) {
					expedicaoDiploma.getMatricula().getCurso().setCodigoEMEC((Integer) matricula_curso_codigoemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_numeroprocessoemec)) {
					expedicaoDiploma.getMatricula().getCurso().setNumeroProcessoEMEC((Integer) matricula_curso_numeroprocessoemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_tipoprocessoemec)) {
					expedicaoDiploma.getMatricula().getCurso().setTipoProcessoEMEC((String) matricula_curso_tipoprocessoemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datacadastroemec)) {
					expedicaoDiploma.getMatricula().getCurso().setDataCadastroEMEC((Date) matricula_curso_datacadastroemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_dataprotocoloemec)) {
					expedicaoDiploma.getMatricula().getCurso().setDataProtocoloEMEC((Date) matricula_curso_dataprotocoloemec);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_tipoautorizacaocursoenum)) {
					expedicaoDiploma.getMatricula().getCurso().setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf((String) matricula_curso_tipoautorizacaocursoenum));
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_numeroautorizacao)) {
					expedicaoDiploma.getMatricula().getCurso().setNumeroAutorizacao((String) matricula_curso_numeroautorizacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datacredenciamento)) {
					expedicaoDiploma.getMatricula().getCurso().setDataCredenciamento((Date) matricula_curso_datacredenciamento);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_veiculopublicacao)) {
					expedicaoDiploma.getMatricula().getCurso().setVeiculoPublicacao((String) matricula_curso_veiculopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_secaopublicacao)) {
					expedicaoDiploma.getMatricula().getCurso().setSecaoPublicacao((Integer) matricula_curso_secaopublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_paginapublicacao)) {
					expedicaoDiploma.getMatricula().getCurso().setPaginaPublicacao((Integer) matricula_curso_paginapublicacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_numerodou)) {
					expedicaoDiploma.getMatricula().getCurso().setNumeroDOU((Integer) matricula_curso_numerodou);
				}
				if (matricula_curso_autorizacaoresolucaoemtramitacao != null) {
					expedicaoDiploma.getMatricula().getCurso().setAutorizacaoResolucaoEmTramitacao((Boolean) matricula_curso_autorizacaoresolucaoemtramitacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_numeroprocessoautorizacaoresolucao)) {
					expedicaoDiploma.getMatricula().getCurso().setNumeroProcessoAutorizacaoResolucao((String) matricula_curso_numeroprocessoautorizacaoresolucao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_tipoprocessoautorizacaoresolucao)) {
					expedicaoDiploma.getMatricula().getCurso().setTipoProcessoAutorizacaoResolucao((String) matricula_curso_tipoprocessoautorizacaoresolucao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datacadastroautorizacaoresolucao)) {
					expedicaoDiploma.getMatricula().getCurso().setDataCadastroAutorizacaoResolucao((Date) matricula_curso_datacadastroautorizacaoresolucao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_dataprotocoloautorizacaoresolucao)) {
					expedicaoDiploma.getMatricula().getCurso().setDataProtocoloAutorizacaoResolucao((Date) matricula_curso_dataprotocoloautorizacaoresolucao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_habilitacao)) {
					expedicaoDiploma.getMatricula().getCurso().setHabilitacao((String) matricula_curso_habilitacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_curso_datahabilitacao)) {
					expedicaoDiploma.getMatricula().getCurso().setDataHabilitacao((Date) matricula_curso_datahabilitacao);
				}
			}
			if (Uteis.isAtributoPreenchido((Integer) matricula_gradecurricular_codigo) || Uteis.isAtributoPreenchido((Integer) matricula_gradecurricularatual)) {
				expedicaoDiploma.getMatricula().getGradeCurricularAtual().setCodigo((Integer) (Uteis.isAtributoPreenchido(matricula_gradecurricularatual) ? matricula_gradecurricularatual : matricula_gradecurricular_codigo));
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_nome)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setNome((String) matricula_gradecurricular_nome);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_curso)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().getCursoVO().setCodigo((Integer) matricula_gradecurricular_curso);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_situacao)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setSituacao((String) matricula_gradecurricular_situacao);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_datacadastro)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setDataCadastro((Date) matricula_gradecurricular_datacadastro);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_cargahoraria)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setCargaHoraria((Integer) matricula_gradecurricular_cargahoraria);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_creditos)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setCreditos((Integer) matricula_gradecurricular_creditos);
				}
				if (Uteis.isAtributoPreenchido(mat_gra_totalcargahorariaatividadecomplementar)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setTotalCargaHorariaAtividadeComplementar((Integer) mat_gra_totalcargahorariaatividadecomplementar);
				}
				if (Uteis.isAtributoPreenchido(matricula_gradecurricular_totalcargahorariaestagio)) {
					expedicaoDiploma.getMatricula().getGradeCurricularAtual().setTotalCargaHorariaEstagio((Integer) matricula_gradecurricular_totalcargahorariaestagio);
				}
			}
		}
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_cidadepta) || Uteis.isAtributoPreenchido((Integer) cidadepta_codigo)) {
			expedicaoDiploma.getCidadePTA().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_cidadepta) ? expedicaoDiploma_cidadepta : cidadepta_codigo));
			if (Uteis.isAtributoPreenchido(cidadepta_nome)) {
				expedicaoDiploma.getCidadePTA().setNome((String) cidadepta_nome);
			}
			if (Uteis.isAtributoPreenchido(cidadepta_codigoibge)) {
				expedicaoDiploma.getCidadePTA().setCodigoIBGE((String) cidadepta_codigoibge);
			}
			if (Uteis.isAtributoPreenchido((Integer) cidadepta_estado) || Uteis.isAtributoPreenchido((Integer) cidadepta_estado_codigo)) {
				expedicaoDiploma.getCidadePTA().getEstado().setCodigo((Integer) (Uteis.isAtributoPreenchido(cidadepta_estado) ? cidadepta_estado : cidadepta_estado_codigo));
				if (Uteis.isAtributoPreenchido(cidadepta_estado_nome)) {
					expedicaoDiploma.getCidadePTA().getEstado().setNome((String) cidadepta_estado_nome);
				}
				if (Uteis.isAtributoPreenchido(cidadepta_estado_codigoibge)) {
					expedicaoDiploma.getCidadePTA().getEstado().setCodigoIBGE((String) cidadepta_estado_codigoibge);
				}
				if (Uteis.isAtributoPreenchido(cidadepta_estado_sigla)) {
					expedicaoDiploma.getCidadePTA().getEstado().setSigla((String) cidadepta_estado_sigla);
				}
				if (Uteis.isAtributoPreenchido(cidadepta_estado_paiz)) {
					expedicaoDiploma.getCidadePTA().getEstado().getPaiz().setCodigo((Integer) cidadepta_estado_paiz);
				}

			}
		}
		if (Uteis.isAtributoPreenchido((Integer) expedicaoDiploma_responsavelanulacao) || Uteis.isAtributoPreenchido((Integer) responsavelanulacao_codigo)) {
			expedicaoDiploma.getResponsavelAnulacao().setCodigo((Integer) (Uteis.isAtributoPreenchido(expedicaoDiploma_responsavelanulacao) ? expedicaoDiploma_responsavelanulacao : responsavelanulacao_codigo));	
			expedicaoDiploma.getResponsavelAnulacao().setNome((String) responsavelanulacao_nome);
			if (Uteis.isAtributoPreenchido((Integer) responsavelanulacao_pessoa) || Uteis.isAtributoPreenchido((Integer) responsavelanulacao_pessoa_codigo)) {
				expedicaoDiploma.getResponsavelAnulacao().getPessoa().setCodigo((Integer) (Uteis.isAtributoPreenchido(responsavelanulacao_pessoa) ? responsavelanulacao_pessoa : responsavelanulacao_pessoa_codigo));
				expedicaoDiploma.getResponsavelAnulacao().getPessoa().setNome((String) responsavelanulacao_pessoa_nome);
			}
		}
	}

	public DataModelo getControleConsultaOtimizado() {
		if (controleConsultaOtimizado == null) {
			controleConsultaOtimizado = new DataModelo();
		}
		return controleConsultaOtimizado;
	}

	public void setControleConsultaOtimizado(DataModelo controleConsultaOtimizado) {
		this.controleConsultaOtimizado = controleConsultaOtimizado;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Date getDataPeriodoInicio() {
		return dataPeriodoInicio;
	}

	public void setDataPeriodoInicio(Date dataPeriodoInicio) {
		this.dataPeriodoInicio = dataPeriodoInicio;
	}

	public Date getDataPeriodoFim() {
		return dataPeriodoFim;
	}

	public void setDataPeriodoFim(Date dataPeriodoFim) {
		this.dataPeriodoFim = dataPeriodoFim;
	}

	public Date getDataPeriodoAnulacaoInicio() {
		return dataPeriodoAnulacaoInicio;
	}

	public void setDataPeriodoAnulacaoInicio(Date dataPeriodoAnulacaoInicio) {
		this.dataPeriodoAnulacaoInicio = dataPeriodoAnulacaoInicio;
	}

	public Date getDataPeriodoAnulacaoFim() {
		return dataPeriodoAnulacaoFim;
	}

	public void setDataPeriodoAnulacaoFim(Date dataPeriodoAnulacaoFim) {
		this.dataPeriodoAnulacaoFim = dataPeriodoAnulacaoFim;
	}

	public String getCodigoVerificacao() {
		if (codigoVerificacao == null) {
			codigoVerificacao = Constantes.EMPTY;
		}
		return codigoVerificacao;
	}

	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}

	public String getNumeroRegistro() {
		if (numeroRegistro == null) {
			numeroRegistro = Constantes.EMPTY;
		}
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getNumeroProcesso() {
		if (numeroProcesso == null) {
			numeroProcesso = Constantes.EMPTY;
		}
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public DataModelo getControleConsultaCurso() {
		if (controleConsultaCurso == null) {
			controleConsultaCurso = new DataModelo();
		}
		return controleConsultaCurso;
	}

	public void setControleConsultaCurso(DataModelo controleConsultaCurso) {
		this.controleConsultaCurso = controleConsultaCurso;
	}

	public TipoNivelEducacional getTipoNivelEducacionalCurso() {
		return tipoNivelEducacionalCurso;
	}

	public void setTipoNivelEducacionalCurso(TipoNivelEducacional tipoNivelEducacionalCurso) {
		this.tipoNivelEducacionalCurso = tipoNivelEducacionalCurso;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<>(0);
			tipoConsultaComboCurso.add(new SelectItem("nomeCurso", "Nome Curso"));
			tipoConsultaComboCurso.add(new SelectItem("areaConhecimento", "Área Conhecimento"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getTipoConsultaComboNivelEducacional() {
		if (tipoConsultaComboNivelEducacional == null) {
			tipoConsultaComboNivelEducacional = new ArrayList<>(0);
			for (TipoNivelEducacional obj : TipoNivelEducacional.values()) {
				tipoConsultaComboNivelEducacional.add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		}
		return tipoConsultaComboNivelEducacional;
	}

	public DataModelo getControleConsultaMatricula() {
		if (controleConsultaMatricula == null) {
			controleConsultaMatricula = new DataModelo();
		}
		return controleConsultaMatricula;
	}

	public void setControleConsultaMatricula(DataModelo controleConsultaMatricula) {
		this.controleConsultaMatricula = controleConsultaMatricula;
	}

	public List<SelectItem> getTipoConsultaComboMatricula() {
		if (tipoConsultaComboMatricula == null) {
			tipoConsultaComboMatricula = new ArrayList<>(0);
			tipoConsultaComboMatricula.add(new SelectItem("nomePessoa", "Nome Aluno"));
			tipoConsultaComboMatricula.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboMatricula.add(new SelectItem("cpfAluno", "Cpf Aluno"));
			tipoConsultaComboMatricula.add(new SelectItem("nomeCurso", "Nome Curso"));
		}
		return tipoConsultaComboMatricula;
	}

	public void setTipoConsultaComboMatricula(List<SelectItem> tipoConsultaComboMatricula) {
		this.tipoConsultaComboMatricula = tipoConsultaComboMatricula;
	}

	public Boolean getPendenteAssinatura() {
		if (pendenteAssinatura == null) {
			pendenteAssinatura = Boolean.FALSE;
		}
		return pendenteAssinatura;
	}

	public void setPendenteAssinatura(Boolean pendenteAssinatura) {
		this.pendenteAssinatura = pendenteAssinatura;
	}

	public Boolean getValido() {
		if (valido == null) {
			valido = Boolean.FALSE;
		}
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}

	public Boolean getAnulado() {
		if (anulado == null) {
			anulado = Boolean.FALSE;
		}
		return anulado;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	public List<SelectItem> getTipoConsultaComboLimitePagina() {
		if (tipoConsultaComboLimitePagina == null) {
			tipoConsultaComboLimitePagina = new ArrayList<>(0);
			tipoConsultaComboLimitePagina.add(new SelectItem(10, "10"));
			tipoConsultaComboLimitePagina.add(new SelectItem(25, "25"));
			tipoConsultaComboLimitePagina.add(new SelectItem(50, "50"));
			tipoConsultaComboLimitePagina.add(new SelectItem(100, "100"));
		}
		return tipoConsultaComboLimitePagina;
	}

	public String getTipoData() {
		if (tipoData == null) {
			tipoData = Constantes.EMPTY;
		}
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public List<SelectItem> getTipoConsultaComboTipoData() {
		if (tipoConsultaComboTipoData == null) {
			tipoConsultaComboTipoData = new ArrayList<>(0);
			tipoConsultaComboTipoData.add(new SelectItem("dataRegistro", "Data Registro"));
			tipoConsultaComboTipoData.add(new SelectItem("dataCadastro", "Data Cadastro"));
			tipoConsultaComboTipoData.add(new SelectItem("dataExpedicao", "Data Expedição"));
		}
		return tipoConsultaComboTipoData;
	}

	public String getVia() {
		if (via == null) {
			via = Constantes.EMPTY;
		}
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public void limparDadosDataModeloMatricula() {
		setControleConsultaMatricula(new DataModelo());
	}

	public void limparDadosDataModeloCurso() {
		setControleConsultaCurso(new DataModelo());
	}

	public void limparDadosMatricula() {
		setMatriculaVO(new MatriculaVO());
	}

	public void limparDadosCurso() {
		setCursoVO(new CursoVO());
	}

	public String getDataConsultaApresentar() {
		if (getTipoData().equals("dataRegistro")) {
			return "Período Data Registro";
		} else if (getTipoData().equals("dataCadastro")) {
			return "Período Data Cadastro";
		} else {
			return "Período Data Expedição";
		}
	}

	public void consultarAlunoPorMatriculaControleConsulta() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setCursoVO(objAluno.getCurso());
			setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
		} catch (Exception e) {
			setMatriculaVO(new MatriculaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarMatriculaControleConsulta() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaVO(obj);
			setCursoVO(obj.getCurso());
			setUnidadeEnsinoVO(obj.getUnidadeEnsino());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarMatriculaOtimizado() {
		try {
			getControleConsultaMatricula().setLimitePorPagina(8);
			getFacadeFactory().getMatriculaFacade().consultaControleConsultaOtimizadoMatricula(getControleConsultaMatricula(), getUnidadeEnsinoVO().getCodigo(), Boolean.FALSE);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListenerMatricula(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaMatricula().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaMatricula().setPage(DataScrollEvent.getPage());
		consultarMatriculaOtimizado();
	}

	public void consultarCursoOtimizado() {
		try {
			getControleConsultaCurso().setLimitePorPagina(8);
			getFacadeFactory().getCursoFacade().consultaControleConsultaOtimizadoCurso(getControleConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), getTipoNivelEducacionalCurso());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListenerCurso(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaCurso().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaCurso().setPage(DataScrollEvent.getPage());
		consultarCursoOtimizado();
	}

	public void selecionarCursoControleConsulta() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setMatriculaVO(new MatriculaVO());
			setCursoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

}
