package negocio.comuns.basico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TagEtiquetaEnum {
	
	NUMERO_INSCRICAO_PROCESSO_SELETIVO, 
    TAG_LABEL_FIXO1,
    TAG_LABEL_FIXO2,
    TAG_LABEL_FIXO3,
    VIA2_DIPLOMA,
    NUMERO_PROCESSO_DIPLOMA,
    NUMERO_REGISTRO_DIPLOMA,
    DATA_EMISSAO_DIPLOMA,
    CIDADE_UNIDADE_ENSINO,
    ESTADO_UNIDADE_ENSINO,
	NOME_PESSOA,
	CPF_PESSOA,
	RG_PESSOA,
	NOME_CURSO,
	NOME_TURNO,
	NOME_UNIDADE_ENSINO,
	DATA,
	HORA,
	SALA,
	LOCAL,
	TURMA,
	MATRICULA,
	PERIODO_LETIVO,
	GRADE_CURRICULAR,
	ENDERECO,
	BAIRRO,
	CEP,
	COMPLEMENTO,
	TELEFONE,
	CELULAR,
	CIDADE,
	ESTADO,
	NUMERO,
	CANHOTO, 
	DISCIPLINA,	
	CARGA_HORARIA_DISC,
	HORA_AULA_DISC,
	PROFESSOR,
	TURNO,
	ANO,
	SEMESTRE,
	TUR_DIS_PROF_COD_BARRA,
	//Dados Biblioteca
	BIB_AUTOR,
	BIB_TITULO,
	BIB_CODIGO_BARRAS,
	BIB_CLASSIFICACAO,
	BIB_PHA,	
	BIB_EXEMPLAR,
	BIB_VOLUME,
	BIB_EDICAO,
	BIB_ANO_PUBLICACAO,
	BIB_NR_PAGINAS_EXEMPLAR,
	BIB_ISBN_EXEMPLAR,
	BIB_ISSN_EXEMPLAR,
	BIB_NUMERO_CODIGO_BARRA,
	BIB_MES,
	BIB_ANOVOLUME,
	BIB_EDICAOESPECIAL,
	BIB_TITULOEXEMPLAR,
	BIB_EDICAOEXEMPLAR, 
	BIB_ANOPUBLICACAOEXEMPLAR, 
	BIB_NOME_UNIDADE_ENSINO, 
	BIB_SIGLA_UNIDADE_ENSINO,
	BIB_SIGLA_TIPO_CATALOGO,
	BIB_ABREVIACAO_TITULO,
	BIB_NOME_BIBLIOTECA,
	DATA_NASCIMENTO,
	VALIDADE,
	FOTO,
	EXPEDICAO,
	IMAGEM_FUNDO,
	PIS,
	TITULO,
	EMAIL,
	EMAIL2,
	BIB_FASCICULO;
	//TODA TAG QUE FOR CRIADA DEVE SER INCLUÍDA NO MÉTODO DE IMPRESSÃO DE ETIQUETA
	// getUtilizarTagEtiqueta
	
	public String getValorApresentar(){
	    return UteisJSF.internacionalizar("enum_TagEtiquetaEnum_"+this.name());
	}
	
	public static List<SelectItem> getTagEtiquetaPorModulo(ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum){	       
	        switch (moduloLayoutEtiquetaEnum) {
                case INSCRICAO_SELETIVO: 
                    return montarListaSelectItem(new TagEtiquetaEnum[]{TagEtiquetaEnum.NUMERO_INSCRICAO_PROCESSO_SELETIVO,
                           TagEtiquetaEnum.NOME_PESSOA, 
                           TagEtiquetaEnum.CPF_PESSOA, 
                           TagEtiquetaEnum.RG_PESSOA, 
                           TagEtiquetaEnum.NOME_CURSO, 
                           TagEtiquetaEnum.NOME_TURNO, 
                           TagEtiquetaEnum.NOME_UNIDADE_ENSINO, 
                           TagEtiquetaEnum.DATA, 
                           TagEtiquetaEnum.HORA,
                           TagEtiquetaEnum.SALA,
                           TagEtiquetaEnum.TURMA,
                           TagEtiquetaEnum.CANHOTO,
                           TagEtiquetaEnum.DISCIPLINA,
                           TagEtiquetaEnum.PROFESSOR
                   });
                case CARTA_COBRANCA: 
                	return montarListaSelectItem(new TagEtiquetaEnum[]{
                			TagEtiquetaEnum.NOME_PESSOA, 
                			TagEtiquetaEnum.CPF_PESSOA, 
                			TagEtiquetaEnum.RG_PESSOA, 
                			TagEtiquetaEnum.NOME_CURSO, 
                			TagEtiquetaEnum.NOME_TURNO, 
                			TagEtiquetaEnum.NOME_UNIDADE_ENSINO, 
                			TagEtiquetaEnum.ENDERECO, 
                			TagEtiquetaEnum.BAIRRO, 
                			TagEtiquetaEnum.CEP, 
                			TagEtiquetaEnum.COMPLEMENTO, 
                            TagEtiquetaEnum.CIDADE,
                            TagEtiquetaEnum.ESTADO,
                            TagEtiquetaEnum.NUMERO
                	});				   
                case BIBLIOTECA: 
                	return montarListaSelectItem(new TagEtiquetaEnum[]{TagEtiquetaEnum.BIB_AUTOR,
                			TagEtiquetaEnum.BIB_CLASSIFICACAO, 
                			TagEtiquetaEnum.BIB_CODIGO_BARRAS, 
                			TagEtiquetaEnum.BIB_EDICAO, 
                			TagEtiquetaEnum.BIB_EXEMPLAR, 
                			TagEtiquetaEnum.BIB_NUMERO_CODIGO_BARRA, 
                			TagEtiquetaEnum.BIB_PHA, 
                			TagEtiquetaEnum.BIB_TITULO, 
                			TagEtiquetaEnum.BIB_ANO_PUBLICACAO, 
                			TagEtiquetaEnum.BIB_VOLUME,
                			TagEtiquetaEnum.BIB_ANOVOLUME,
                			TagEtiquetaEnum.BIB_MES,
                			TagEtiquetaEnum.BIB_EDICAOESPECIAL,
                			TagEtiquetaEnum.BIB_TITULOEXEMPLAR,
                			TagEtiquetaEnum.BIB_EDICAOEXEMPLAR, 
                			TagEtiquetaEnum.BIB_ANOPUBLICACAOEXEMPLAR, 
                			TagEtiquetaEnum.BIB_NR_PAGINAS_EXEMPLAR, 
                			TagEtiquetaEnum.BIB_ISBN_EXEMPLAR, 
                			TagEtiquetaEnum.BIB_ISSN_EXEMPLAR, 
                			TagEtiquetaEnum.BIB_NOME_UNIDADE_ENSINO, 
                			TagEtiquetaEnum.BIB_SIGLA_UNIDADE_ENSINO,  
                			TagEtiquetaEnum.BIB_SIGLA_TIPO_CATALOGO,  
                			TagEtiquetaEnum.BIB_ABREVIACAO_TITULO,
                			TagEtiquetaEnum.BIB_NOME_BIBLIOTECA,
                			TagEtiquetaEnum.BIB_FASCICULO
                	});                		
                case MATRICULA: 
                	return montarListaSelectItem(new TagEtiquetaEnum[]{
                			TagEtiquetaEnum.NOME_PESSOA, 
                			TagEtiquetaEnum.CPF_PESSOA, 
                			TagEtiquetaEnum.RG_PESSOA, 
                			TagEtiquetaEnum.NOME_CURSO, 
                			TagEtiquetaEnum.NOME_TURNO, 
                			TagEtiquetaEnum.NOME_UNIDADE_ENSINO, 
                			TagEtiquetaEnum.PERIODO_LETIVO, 
                			TagEtiquetaEnum.ENDERECO, 
                			TagEtiquetaEnum.BAIRRO, 
                			TagEtiquetaEnum.CEP, 
                			TagEtiquetaEnum.COMPLEMENTO, 
                			TagEtiquetaEnum.TELEFONE, 
                			TagEtiquetaEnum.CELULAR, 
                			TagEtiquetaEnum.MATRICULA,
                            TagEtiquetaEnum.TURMA,
                            TagEtiquetaEnum.CIDADE,
                            TagEtiquetaEnum.ESTADO,
                            TagEtiquetaEnum.NUMERO,
                            TagEtiquetaEnum.TAG_LABEL_FIXO1,
                            TagEtiquetaEnum.VIA2_DIPLOMA,
                            TagEtiquetaEnum.TAG_LABEL_FIXO2,
                            TagEtiquetaEnum.TAG_LABEL_FIXO3,                            
                            TagEtiquetaEnum.NUMERO_PROCESSO_DIPLOMA,
                            TagEtiquetaEnum.NUMERO_REGISTRO_DIPLOMA,
                            TagEtiquetaEnum.DATA_EMISSAO_DIPLOMA,
                            TagEtiquetaEnum.CIDADE_UNIDADE_ENSINO,
                            TagEtiquetaEnum.ESTADO_UNIDADE_ENSINO                            
                	});
                case ANIVERSARIANTE:
                	return montarListaSelectItem(new TagEtiquetaEnum[]{
                			TagEtiquetaEnum.NOME_PESSOA, 
                			TagEtiquetaEnum.ENDERECO, 
                			TagEtiquetaEnum.BAIRRO, 
                			TagEtiquetaEnum.CEP, 
                			TagEtiquetaEnum.COMPLEMENTO, 
                			TagEtiquetaEnum.TELEFONE, 
                			TagEtiquetaEnum.CELULAR, 
                            TagEtiquetaEnum.CIDADE,
                            TagEtiquetaEnum.ESTADO,
                            TagEtiquetaEnum.NUMERO,
                            TagEtiquetaEnum.EMAIL,
                            TagEtiquetaEnum.EMAIL2,
                            TagEtiquetaEnum.DATA_NASCIMENTO
                	});
                case PROVA: 
                    return montarListaSelectItem(new TagEtiquetaEnum[]{TagEtiquetaEnum.NOME_PESSOA, 
                           TagEtiquetaEnum.TURMA,
                           TagEtiquetaEnum.MATRICULA,
                           TagEtiquetaEnum.DISCIPLINA,
                           TagEtiquetaEnum.PROFESSOR,
                           TagEtiquetaEnum.PERIODO_LETIVO,
                           TagEtiquetaEnum.TURNO
                   });
                case CARTEIRA_ESTUDANTIL:
                	return montarListaSelectItem(new TagEtiquetaEnum[]{TagEtiquetaEnum.NOME_PESSOA, 
                            TagEtiquetaEnum.MATRICULA,
                            TagEtiquetaEnum.NOME_CURSO,
                            TagEtiquetaEnum.DATA_NASCIMENTO,
                            TagEtiquetaEnum.VALIDADE,
                            TagEtiquetaEnum.FOTO,
                            TagEtiquetaEnum.EXPEDICAO,
                            TagEtiquetaEnum.RG_PESSOA,
                            TagEtiquetaEnum.IMAGEM_FUNDO,
                            TagEtiquetaEnum.CPF_PESSOA,
                            TagEtiquetaEnum.PIS,
                            TagEtiquetaEnum.TITULO,
                            TagEtiquetaEnum.PERIODO_LETIVO,
                            TagEtiquetaEnum.TURNO,
                            TagEtiquetaEnum.TURMA,
                            TagEtiquetaEnum.ANO,
                            TagEtiquetaEnum.SEMESTRE,
                            TagEtiquetaEnum.ENDERECO,
                            TagEtiquetaEnum.NUMERO,
                            TagEtiquetaEnum.COMPLEMENTO,
                            TagEtiquetaEnum.BAIRRO,
                            TagEtiquetaEnum.CIDADE,
                            TagEtiquetaEnum.ESTADO
                    });
                case CRONOGRAMA_AULA:
                	return montarListaSelectItem(new TagEtiquetaEnum[]{
                			TagEtiquetaEnum.NOME_UNIDADE_ENSINO,
                			TagEtiquetaEnum.NOME_CURSO,
                			TagEtiquetaEnum.NOME_TURNO,
                			TagEtiquetaEnum.TURMA,
                			TagEtiquetaEnum.GRADE_CURRICULAR,
                			TagEtiquetaEnum.PERIODO_LETIVO,
                			TagEtiquetaEnum.DISCIPLINA,
                			TagEtiquetaEnum.CARGA_HORARIA_DISC,
                			TagEtiquetaEnum.HORA_AULA_DISC,
                			TagEtiquetaEnum.ANO,
                			TagEtiquetaEnum.SEMESTRE,
                			TagEtiquetaEnum.PROFESSOR,
                			TagEtiquetaEnum.SALA,
                			TagEtiquetaEnum.LOCAL,                			
                			TagEtiquetaEnum.TUR_DIS_PROF_COD_BARRA                			
                			
                	});
                default:
                    return montarListaSelectItem(TagEtiquetaEnum.values());
            }
	}
	
	private static List<SelectItem> montarListaSelectItem(TagEtiquetaEnum[] tags){
	    List<SelectItem> tagList = new ArrayList<SelectItem>();
	    for(TagEtiquetaEnum tag:tags){
	        tagList.add(new SelectItem(tag, tag.getValorApresentar()));
	    }
	    return tagList;
	}
	

}
