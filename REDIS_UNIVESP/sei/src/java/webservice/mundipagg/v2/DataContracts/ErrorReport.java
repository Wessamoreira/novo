package webservice.mundipagg.v2.DataContracts;

import java.util.ArrayList;
import java.util.List;

import webservice.mundipagg.v2.EnumTypes.ErrorCategoryEnum;

/**
 * Relação de erros 
 */
public final class ErrorReport {

    /**
     * Construtor da classe
     */
    public ErrorReport() {
        this.setErrorItemCollection(new ArrayList());
    }
    
    /**
     * Categoria do erro
     */
    public ErrorCategoryEnum Category;
    
    /**
     * Coleção de Erros
     */
    public List<ErrorItem> ErrorItemCollection;

    /**
     * Recupera categoria do erro
     * @return 
     */
    public ErrorCategoryEnum getCategory() {
        return Category;
    }

    /**
     * Altera categoria do erro
     * @param Category 
     */
    public void setCategory(ErrorCategoryEnum Category) {
        this.Category = Category;
    }

    /**
     * Recupera coleção de erros
     * @return 
     */
    public List<ErrorItem> getErrorItemCollection() {
        return ErrorItemCollection;
    }

    /**
     * Altera coleção de erros
     * @param ErrorItemCollection 
     */
    public void setErrorItemCollection(List<ErrorItem> ErrorItemCollection) {
        this.ErrorItemCollection = ErrorItemCollection;
    }   
}
