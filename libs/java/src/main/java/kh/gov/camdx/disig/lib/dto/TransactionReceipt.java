package kh.gov.camdx.disig.lib.dto;

import lombok.Data;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

@Data
public class TransactionReceipt {
    private String transactionHash;
    private String transactionIndex;
    private String blockHash;
    private String blockNumber;
    private String cumulativeGasUsed;
    private String gasUsed;
    private String contractAddress;
    private String root;
    private String status;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;
    private String revertReason;
    private String type;
    private String effectiveGasPrice;
}
