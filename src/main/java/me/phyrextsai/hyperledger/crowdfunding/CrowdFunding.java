package me.phyrextsai.hyperledger.crowdfunding;

import me.phyrextsai.hyperledger.crowdfunding.data.Campaign;
import me.phyrextsai.hyperledger.crowdfunding.data.Contribute;
import me.phyrextsai.hyperledger.crowdfunding.helper.CampaignHelper;
import me.phyrextsai.hyperledger.crowdfunding.helper.ContributeHelper;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CrowdFunding extends ChaincodeBase {

    private static Log log = LogFactory.getLog(CrowdFunding.class);

    @Override
    public String run(ChaincodeStub stub, String function, String[] args) {
        log.info("run, function:" + function);
        CampaignHelper campaignHelper = new CampaignHelper();
        ContributeHelper contributeHelper = new ContributeHelper();
        Campaign campaign = null;
        Contribute contribute = null;

        switch (function) {
            case "init":
                log.info("init");

                /**
                 * create table schema
                 *
                 * TODO: check Member, only admin
                 */
                campaignHelper.create(stub);
                contributeHelper.create(stub);
                break;
            case "campaign":
                // TODO: create a crowd funding
                log.info("campaign");

                /**
                 * create campaign, owner is the wallet address to collect money
                 *
                 * campaign
                 * owner (wallet address)
                 * fundingGoal
                 *
                 * {
                 *   "Function":"campaign",
                 *   "Args":[
                 *     "STRING:owner wallet address",
                 *     "STRING:info what is this campaign for",
                 *     "INT:funding amount"
                 *   ]
                 * }
                 *
                 */
                campaign = campaignHelper.parse(args);
                if (campaign == null) {
                    return "{\"Error\":\"Wrong arguments.\"}";
                } else {
                    if (campaignHelper.insert(stub, campaign)) {
                        log.info("Insert record, CampaignId : " + campaign.getCampaignId());
                        return "{\"Data\":\"Create Campaign success, uuid : " + campaign.getCampaignId() + ".\"}";
                    } else {
                        return "{\"Error\":\"Create Campaign failed.\"}";
                    }
                }
            case "contribute":
                // TODO: add money in one of the crowd funding
                log.info("contribute");

                /**
                 * transfer money from campaign owner's wallet address
                 *
                 * campaign
                 * contributor
                 * amount
                 */
                contribute = contributeHelper.doContribute(args);
                if (contribute == null) {
                    return "{\"Error\":\"Wrong arguments.\"}";
                } else {
                    if (contributeHelper.insert(stub, contribute)) {
                        log.info("Insert record, ContributeId : " + contribute.getCampaignId());
                        return "{\"Data\":\"Create Contribute success, uuid : " + contribute.getCampaignId() + ".\"}";
                    } else {
                        return "{\"Error\":\"Create Contribute failed.\"}";
                    }
                }
            case "refund":
                log.info("refund");

                /**
                 * refund to single person, transfer from campaign owner's wallet address to contributor
                 *
                 * campaign
                 * contributor
                 * refund
                 */
                contribute = contributeHelper.doRefund(args);
                if (contribute == null) {
                    return "{\"Error\":\"Wrong arguments.\"}";
                } else {
                    if (contributeHelper.insert(stub, contribute)) {
                        log.info("Insert record, ContributeId : " + contribute.getCampaignId());
                        return "{\"Data\":\"Create Contribute success, uuid : " + contribute.getCampaignId() + ".\"}";
                    } else {
                        return "{\"Error\":\"Create Contribute failed.\"}";
                    }
                }
            case "payout":
                log.info("payout");

                /**
                 * EVENT trigger by system
                 * camapign wallet withdraw
                 *
                 * campaign
                 * amount
                 */

                break;
            default:
                log.error("No matching case for function:"+function);
        }
        return null;
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        // TODO: show personal crowd funding details, personal creating, personal funding.
        log.info("query, function:" + function);
        switch (function) {
            case "campaignInfo" :
                // load from campaign data from chaincode
                if (args.length == 1) {
                    log.info("CASE : campaignInfo, ID : " + args[0]);
                    CampaignHelper helper = new CampaignHelper();
                    return helper.get(stub, args[0]).getInfo();
                }
                return "No data!";
            case "campaignOwner" :
                if (args.length == 1) {
                    log.info("CASE : campaignOwner, ID : " + args[0]);
                    CampaignHelper helper = new CampaignHelper();
                    return helper.get(stub, args[0]).getOwner();
                }
                return "No data!";
            case "campaignGoal" :
                if (args.length == 1) {
                    log.info("CASE : campaignGoal, ID : " + args[0]);
                    CampaignHelper helper = new CampaignHelper();
                    return String.valueOf(helper.get(stub, args[0]).getFundingAmount());
                }
                return "No data!";
            case "campaignDetail" :
                // TODO: more information
                return stub.getState(CampaignHelper.TOTAL + ":" + args[0]);
            case "contribute" :
                // TODO: show campaign contibute detail
                return "";
            default:
                log.error("No matching case for function:"+function);
                break;
        }
        return "";
    }

    @Override
    public String getChaincodeID() {
        return "CrowdFunding";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Crowdfunding! starting : " + args);
        log.info("Crowdfunding starting......");
        new CrowdFunding().start(args);
    }
}
