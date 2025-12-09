package webservice.mundipagg.v2.EnumTypes;

/**
 * Enumerador que especifica a operação que será realizada no gerenciamento da transação.
 */
public enum ManageOperationEnum {
       
    /**
     * Captura
     */
    Capture,
    
    /**
     * Cancelamento
     */
    Cancel;
    
    private ManageOperationEnum() {}
}
