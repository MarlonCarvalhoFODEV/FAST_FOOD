package apis.pagarme.transaction;

public class MetaData {

    private int antifraudAnalysisID ;
    private String kind ;
    private String idProduct ;
    private Environment environment ;

    public int getAntifraudAnalysisID() {
        return antifraudAnalysisID;
    }

    public void setAntifraudAnalysisID(int antifraudAnalysisID) {
        this.antifraudAnalysisID = antifraudAnalysisID;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
