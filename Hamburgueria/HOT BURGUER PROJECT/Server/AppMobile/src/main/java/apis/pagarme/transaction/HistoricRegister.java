package apis.pagarme.transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HistoricRegister implements Serializable {

    private String id;
    private String dateCreated;
    private String dateUpdated;
    private String status;
    private String failReason;
    private String type;
    private String model;
    private String modelId ;
    private String groupId;
    private String nextGroupID ;
    private String requestID ;
    private String startedAT ;
    private String endedAt ;
    private String processor ;
    private String processorResponseCode ;
    private boolean rollbacked;

    private MetaData metaData ;

    public HistoricRegister(JSONObject ob) throws JSONException {
        metaData = new MetaData();

        id = ob.getString("id");
        dateCreated = ob.getString("date_created");
        dateUpdated = ob.getString("date_updated");
        status = ob.getString("status");
        failReason = ob.getString("fail_reason");
        type = ob.getString("type");
        rollbacked = ob.getBoolean("rollbacked");
        model = ob.getString("model");
        modelId = ob.getString("model_id");
        groupId = ob.getString("group_id");
        nextGroupID = ob.getString("next_group_id");
        requestID = ob.getString("request_id");
        startedAT = ob.getString("started_at");
        endedAt = ob.getString("ended_at");
        processor = ob.getString("processor");
        processorResponseCode = ob.getString("processor_response_code");

        JSONObject metadata = ob.getJSONObject("metadata");
        this.metaData.setKind(metadata.getString("kind"));
        this.metaData.setAntifraudAnalysisID(metadata.getInt("antifraud_analysis_id"));

        JSONObject environment = metadata.getJSONObject("environment");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNextGroupID() {
        return nextGroupID;
    }

    public void setNextGroupID(String nextGroupID) {
        this.nextGroupID = nextGroupID;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getStartedAT() {
        return startedAT;
    }

    public void setStartedAT(String startedAT) {
        this.startedAT = startedAT;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public void setProcessorResponseCode(String processorResponseCode) {
        this.processorResponseCode = processorResponseCode;
    }

    public boolean isRollbacked() {
        return rollbacked;
    }

    public void setRollbacked(boolean rollbacked) {
        this.rollbacked = rollbacked;
    }
}
