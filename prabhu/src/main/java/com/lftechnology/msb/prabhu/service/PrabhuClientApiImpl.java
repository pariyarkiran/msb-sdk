package com.lftechnology.msb.prabhu.service;

import com.lftechnology.msb.prabhu.constant.PrabhuConstant;
import com.lftechnology.msb.prabhu.dto.*;
import com.lftechnology.msb.prabhu.webservices.*;
import com.lftechnology.msb.prabhu.utils.MSBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Kiran Pariyar <kiranpariyar@lftechnology.com>
 */
@Stateless
public class PrabhuClientApiImpl implements PrabhuClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrabhuClientApiImpl.class);
    private static final String ONE_UNIT="1";

    @Override
    public TransactionResponse createTransaction(Credential credential, TransactionDetail transactionDetail) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ReturnCreateTXN returnCreateTXN = iRemitServiceSoap.sendTransaction(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                credential.getAgentSessionId(),
                transactionDetail.getAgentTxnId(),
                transactionDetail.getBankLocationId(),
                transactionDetail.getSenderName(),
                transactionDetail.getSenderAddress(),
                transactionDetail.getSenderMobile(),
                transactionDetail.getSenderCity(),
                transactionDetail.getSenderCountry(),
                transactionDetail.getSenderIdType(),
                transactionDetail.getSenderIdNumber(),
                transactionDetail.getSenderIdIsuueDate(),
                transactionDetail.getSenderIdExpireDate(),
                transactionDetail.getSenderDateofBirth(),
                transactionDetail.getReceiverName(),
                transactionDetail.getReceiverAddress(),
                transactionDetail.getReceiverContactNumber(),
                transactionDetail.getReceiverCity(),
                transactionDetail.getReceiverCountry(),
                transactionDetail.getTransferAmount(),
                transactionDetail.getPaymentMode(),
                transactionDetail.getBankId(),
                transactionDetail.getBankAccountNumber(),
                transactionDetail.getCalcBy(), "",
                transactionDetail.getBankBranchName(),
                transactionDetail.getBankBranchId(),
                transactionDetail.getSenderOccupation(),
                transactionDetail.getSenderSourceOfFund(),
                transactionDetail.getSenderBeneficiaryRelationship(),
                transactionDetail.getPurposeOfRemittance(),
                "", "",
                transactionDetail.getSenderSSN()
        );
        return MSBUtil.mapToTransactionResponse(returnCreateTXN);
    }

    @Override
    public TransactionResponse getDetails(Credential credential, TransactionDetail transactionDetail) {
        return null;
    }

    @Override
    public Boolean amendTransaction(Credential credential, TransactionAmendmentDetail amendmentRequest) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ReturnTXNAMEND response = iRemitServiceSoap.amendmentRequest(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                credential.getAgentSessionId(),
                amendmentRequest.getMsbTxnId(),
                amendmentRequest.getFieldName(),
                amendmentRequest.getFieldValue()
        );
        return PrabhuConstant.SUCCESS.equalsIgnoreCase(response.getCODE());
    }

    @Override
    public List<Agent> getAgents(Credential credential, BankInfo bankInfo) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ArrayOfReturnAGENTLIST arrayOfReturnAGENTLIST = iRemitServiceSoap.getAgentList(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                credential.getAgentSessionId(),
                bankInfo.getPaymentType(),
                bankInfo.getPayoutCountry(),
                bankInfo.getBankName(),
                bankInfo.getBranchState()
        );
        List<ReturnAGENTLIST> returnAGENTLIST = arrayOfReturnAGENTLIST.getReturnAGENTLIST();
        return MSBUtil.mapToListOfAgent(returnAGENTLIST);
    }

    @Override
    public CancelResponse cancelTransaction(Credential credential, CancelTransactionDetail cancelTransactionDetail) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ReturnTXNCancel returnTXNCancel = iRemitServiceSoap.cancelTransactionv2(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                cancelTransactionDetail.getMsbTxnId(),
                credential.getAgentSessionId(),
                cancelTransactionDetail.getCancelComment()
        );

        if(returnTXNCancel.getCODE().equals(PrabhuConstant.SUCCESS)){
            return MSBUtil.mapToCancelTransactionResponse(returnTXNCancel);
        }else {
            LOGGER.debug("Could not cancel transaction in MSB. {}" , returnTXNCancel);
            CancelResponse cancelResponse = new CancelResponse();
            cancelResponse.setCode(PrabhuConstant.FAILED);
            cancelResponse.setMsbTxnId(cancelTransactionDetail.getMsbTxnId());
            return cancelResponse;
        }
    }

    @Override
    public Boolean acknowledgeTransaction(Credential credential, String msbTxnId) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ReturnTXNAuth authorizedConfirmedResponse = iRemitServiceSoap.authorizedConfirmed(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                msbTxnId,
                credential.getAgentSessionId()
        );
        return PrabhuConstant.SUCCESS.equalsIgnoreCase(authorizedConfirmedResponse.getCODE());
    }

    @Override
    public BigDecimal getExchangeRate(Credential credential, String destinationCountryName) {
        IRemitService iRemitService =new IRemitService();
        IRemitServiceSoap iRemitServiceSoap = iRemitService.getIRemitServiceSoap();
        ReturnFOREX forex =iRemitServiceSoap.getEXRate(
                credential.getAgentCode(),
                credential.getAgentUserId(),
                credential.getAgentPassword(),
                "",
                "",
                "",
                credential.getAgentLocationId(),
                destinationCountryName,
                "",
                "",
                ""
        );
        return new BigDecimal(forex.getEXCHANGERATE());
    }
}