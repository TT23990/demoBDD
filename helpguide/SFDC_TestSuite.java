package com.moodys.servicecloud;


import com.moodys.interfaces.*;
import com.moodys.pageobjects.lightning.BaseObjectsLPO;
import com.moodys.pageobjects.lightning.HomeLPO;
import com.moodys.pageobjects.lightning.LoginLPO;
import com.moodys.pageobjects.lightning.agreements.AgreementsHomeLPO;
import com.moodys.pageobjects.lightning.cases.CasesHomeLPO;
import com.moodys.pageobjects.lightning.opportunities.OpportunitiesHomeLPO;
import com.moodys.pageobjects.lightning.products.ProductsHomeLPO;
import com.moodys.pageobjects.lightning.proposals.ProposalsHomeLPO;
import com.moodys.pageobjects.sharedservices.TestDataProvider;
import com.moodys.selenium.base.pageframework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static com.moodys.pageobjects.lightning.BaseObjectsLPO.*;

public class SFDC_TestSuite extends BaseTest {

    Home home;
    LinkedHashMap<String, String> profiles;
    static boolean continueTest = false;
    static boolean continueAction = false;

    //    @Factory(dataProvider = "profiles",
//            dataProviderClass =com.moodys.pageobjects.sharedservices.TestDataProvider.class )
    public SFDC_TestSuite(LinkedHashMap<String, String> user) {
        profiles = user;
    }

    @BeforeClass
    public void beforeClass() {
        BaseObjectsLPO.profiles = profiles.get("Profiles");
        System.out.println("Logged in application user: " + profiles.get("Profiles"));
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("****************** before Method ******************");
        Assert.assertTrue(new LoginLPO().fillOutLoginForm(profiles.get("Username"),
                profiles.get("Password")), "Click on login button");
        home = new HomeLPO();
        Assert.assertTrue(home.validateSelectedSandbox(profiles.get("Sandbox")),
                profiles.get("Sandbox") + " sandbox");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("****************** after Method ******************\n" +
                "Closing application after test method");
        home.logoutFromApplication();
//        Assert.assertTrue(home.logoutFromApplication(), "Logout successful");
    }


    @Test(dataProvider = "test cases",
            dataProviderClass = com.moodys.pageobjects.sharedservices.TestDataProvider.class)
    public void testCase(LinkedHashMap<String, String> testCaseData) {
        TestDataProvider data = new TestDataProvider();
        try {
//        data.getSplitDataInHashMap(testCaseData.get("Test Steps")).entrySet().forEach(testSteps -> {
            for (LinkedHashMap<String, String> eachStep : data.getSplitDataInArrayList(testCaseData.get("Test Steps"))) {
                continueAction = false;
                switch (eachStep.get("feature")) {
                    case "Home":
                        for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                            switch (each_Action.get("Action")) {
                                case "Select":
                                    Assert.assertTrue(home.selectApplication(each_Action.get("Application")), each_Action.get("Application") + " selected");
                                    Assert.assertTrue(home.validateSelectedApplication(each_Action.get("Application")), each_Action.get("Application") + " present");
                                    break;
                            }
                        }
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;
                    case "Cases":
                        Cases cases = new CasesHomeLPO();
                        for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                            if(continueTest || continueAction) getOutputValue("Case Number");
                            if (!continueTest && !continueAction && !each_Action.get("Action").equalsIgnoreCase("New")) {
                                Assert.assertTrue(home.searchItemList("Cases", each_Action.get("Case Number")), "Search item selected");
                                Assert.assertTrue(cases.validateOpenItemNumber("Case Number", each_Action.get("Case Number")));
                                getOutputValue("Case Number");
//                                continueAction = true;
                            }
                            switch (each_Action.get("Action")) {
                                case "New":
                                    if (!continueTest)
                                        Assert.assertTrue(home.selectApplication("Cases"));
                                    Assert.assertTrue(cases.selectCaseAction("New"), "Select new case");
                                    Assert.assertTrue(cases.createNewCase(each_Action));
                                    continueAction = true;
                                    continueTest = false;
                                    break;
                                case "Edit":
                                    Assert.assertTrue(cases.selectCaseAction("Edit"));
                                    Assert.assertTrue(cases.editCaseDetails(each_Action));
                                    continueTest = false;
                                    break;
                                case "Feed":
                                    Assert.assertTrue(cases.selectCaseAction("Feed"));
                                    Assert.assertTrue(cases.selectSubAction(each_Action.get("Sub Action")));
                                    switch (each_Action.get("Sub Action")==null?"skip":each_Action.get("Sub Action")) {
                                        case "Post":
                                           Assert.assertTrue(cases.feedPost(each_Action));
                                            continueTest = false;
                                            break;
                                        case "Email":
                                            Assert.assertTrue(cases.feedEmail(each_Action));
                                            continueTest = false;
                                            break;
                                        case "Update Case":
                                            continueTest = false;
                                        case "Create Contact":
                                            continueTest = true;
                                        case "Add Files/Emails":
                                            continueTest = false;
                                            break;
                                    }
                                    break;
                            }
                            continueAction=true;
                        }
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;

                    case "Knowledge":
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;

                    case "Opportunities":
                        Opportunities opportunity = new OpportunitiesHomeLPO();
                        for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                            if(continueTest || continueAction) getOutputValue("Opportunity Name");

                            if (!continueTest && !continueAction && !each_Action.get("Action").equalsIgnoreCase("New")) {
                                Assert.assertTrue(home.searchItemList("Opportunities", each_Action.get("Opportunity Name")), "Search item selected");
                                Assert.assertTrue(opportunity.validateOpenOpportunityName(each_Action.get("Opportunity Name")));
                                getOutputValue("Opportunity Name");
//                                continueAction = true;
                            }
                            switch (each_Action.get("Action")) {
                                case "New":
                                    if (!continueTest)
                                        Assert.assertTrue(home.selectApplication("Opportunities"));
                                    Assert.assertTrue(opportunity.selectOpportunitiesAction("New"));
                                    Assert.assertTrue(opportunity.createNewOpportunity(each_Action));
//                                    continueAction = true;
                                    continueTest = false;
                                    break;
                                case "Edit":
                                    Assert.assertTrue(opportunity.selectOpportunitiesAction("Edit"));
                                    Assert.assertTrue(opportunity.editOpportunityDetails(each_Action));
                                    continueTest = false;
                                    break;
                                case "Change Owner":
                                    Assert.assertTrue(opportunity.selectOpportunitiesAction("Change Owner"));
                                    Assert.assertTrue(opportunity.changeOwner(each_Action));
                                    continueTest = false;
                                    break;
                                case "Create Quote/Proposal":
                                    Assert.assertTrue(opportunity.selectOpportunitiesAction("Create Quote/Proposal"));
                                    continueTest = true;
                                    break;
                            }
                            continueAction=true;
                        }
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;

                    case "Proposals":
                        Proposals proposal = new ProposalsHomeLPO();
                        for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                            if(continueTest || continueAction) getOutputValue("Proposal ID");
                            if(!continueTest && !continueAction &&! each_Action.get("Action").equalsIgnoreCase("New")){
                                Assert.assertTrue(home.searchItemList("Proposals", each_Action.get("Proposal ID")), "Search item selected");
                                Assert.assertTrue(proposal.validateOpenProposalName(each_Action.get("Proposal ID")));
                                getOutputValue("Proposal ID");

                            }
                            switch (each_Action.get("Action")) {
                                case "Accept":
                                    Assert.assertTrue(proposal.verifyAndAcceptProposal(each_Action));
                                    continueTest = false;
                                    break;
                                case "Edit":
                                    Assert.assertTrue(proposal.selectProposalAction("Edit"));
                                    Assert.assertTrue(proposal.editProposalDetails(each_Action));
//                                    continueAction = true;
                                    continueTest = false;
                                    break;
                                case "Create Agreement With Line Items":
                                    Assert.assertTrue(proposal.createAgreementWithLineItems());
                                    continueTest = true;
                                    break;
                                case "Configure Quote Products":
                                    Assert.assertTrue(proposal.selectProposalAction("Configure Quote Products"));
                                    continueTest = true;
                                    break;
                            }
                            continueAction=true;
                        }
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;
                    case "Products":
                        Products product = new ProductsHomeLPO();
                        for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                            if(continueTest) applicationData.put("Products",eachStep.get("data"));
//                            if(!continueTest && !continueAction && !each_Action.get("Action").equalsIgnoreCase("New")){
//                                Assert.assertTrue(home.searchItemList("Products", each_Action.get("Products ID")), "Search item selected");
//                                Assert.assertTrue(product.validateOpenProductName(each_Action.get("Products ID")));
//                                getOutputValue("Opportunity Name");
//                                continueAction=true;
//                            }
                            switch (each_Action.get("Action")) {
                                case "New":
                                    if (!continueTest)
                                        Assert.assertTrue(home.selectApplication("Products"));
                                    Assert.assertTrue(product.selectProductAction("New"));
                                    Assert.assertTrue(product.selectNewProduct(each_Action));
                                    continueAction = true;
                                    continueTest = false;
                                    break;
                                case "Edit":
                                    Assert.assertTrue(product.selectProductAction("Edit"));
                                    Assert.assertTrue(product.editProductDetails(each_Action));
                                    continueTest = false;
                                    break;
                                case "Add":
                                    Assert.assertTrue(product.selectProductAction("Catalog Products"));
                                    Assert.assertTrue(product.selectNewProduct(each_Action));
                                    if (each_Action.get("Sub Action").equalsIgnoreCase("More Products"))
                                        Assert.assertTrue(product.addMoreProducts());
                                    if (each_Action.get("Sub Action").equalsIgnoreCase("Finalize"))
                                        Assert.assertTrue(product.finalize(each_Action));
                                    continueTest = true;
                                    break;
                            }
                            continueAction=true;
                        }
                        outputData.put(eachStep.get("feature"), applicationData);
                        break;

                case "Agreements":
                    Agreements agreements = new AgreementsHomeLPO();
                    for (LinkedHashMap<String, String> each_Action : data.getActionDataInArrayList(eachStep.get("feature"), eachStep.get("data"), testCaseData)) {
                    if(continueTest || continueAction) getOutputValue("Agreement Name");
                        if (!continueTest && !continueAction && !each_Action.get("Action").equalsIgnoreCase("New") &&
                                !each_Action.get("Action").equalsIgnoreCase("Add")) {
                            Assert.assertTrue(home.searchItemList("Agreements", each_Action.get("Agreement Name")), "Search item selected");
                            Assert.assertTrue(agreements.validateOpenAgreementName(each_Action.get("Agreement Name")));
                            getOutputValue("Agreement Name");
//                            continueAction=true;
                        }
                        switch (each_Action.get("Action")) {
                            case "New":
                                if(!continueTest)
                                Assert.assertTrue(home.selectApplication("Agreements"));
                                Assert.assertTrue(agreements.selectAgreementAction("New"));
                                Assert.assertTrue(agreements.createNewAgreement(each_Action));
//                                continueAction=true;
                                continueTest=false;
                                break;
                            case "Edit":
                                Assert.assertTrue(agreements.selectAgreementAction("Edit"));
                                Assert.assertTrue(agreements.editAgreementDetails(each_Action));
                                continueTest=false;
                                break;
                            case "Add":
                                Assert.assertTrue(agreements.selectAgreementType(each_Action));
                                continueTest=true;
                                break;
                            case "Change Owner":
                                Assert.assertTrue(agreements.selectAgreementAction("Change Owner"));
                                Assert.assertTrue(agreements.changeOwner(each_Action));
                                continueTest=false;
                                break;
                            case "Create Quote/Proposal":
                                Assert.assertTrue(agreements.selectAgreementAction("Create Quote/Proposal"));
                                continueTest=true;
                                break;
                            case "Generate":
                                Assert.assertTrue(agreements.selectAgreementStep("Generate"));
                                switch (each_Action.get("Sub Action")==null?"skip":each_Action.get("Sub Action")) {
                                    case "Generate Agreement":
                                        Assert.assertTrue(agreements.generateAgreement(each_Action));
                                        continueTest=false;
                                        break;
                                }
                               continueTest=false;
                                break;

                            case "Send For Review":
                                Assert.assertTrue(agreements.selectAgreementStep("Send For Review"));
                                Assert.assertTrue(agreements.selectDocumentsForReview(each_Action));
                                continueTest=false;
                                break;
                            case "Submit for Approval":
                                Assert.assertTrue(agreements.selectAgreementAction("Submit for Approval"));
                                Assert.assertTrue(agreements.submitForApproval(each_Action));
                                switch (each_Action.get("Sub Action")==null?"skip":each_Action.get("Sub Action")) {
                                    case "Approve":
                                        Assert.assertTrue(agreements.agreementApprovalAction(each_Action));
                                        continueTest=false;
                                        break;
                                }
                                continueTest=false;
                                break;
                            case "Legal Contracting Entity":
                                Assert.assertTrue(agreements.selectAgreementAction("Legal Contracting Entity"));
                                switch (each_Action.get("Sub Action")==null?"skip":each_Action.get("Sub Action")) {
                                    case "New Legal Contracting Entity":
                                        Assert.assertTrue(agreements.selectAgreementAction("New Legal Contracting Entity"));
                                        Assert.assertTrue(agreements.newLegalContractingEntity(each_Action));
                                        continueTest=false;
                                        break;
                                    case "Select":
                                        Assert.assertTrue(agreements.selectAgreementAction("New "));
                                        continueTest=false;
                                        break;
                                }

                                continueTest=false;
                                break;
                        }
                        continueAction=true;
                    }
                    outputData.put(eachStep.get("feature"),applicationData);
                    break;
                }
            }
        } finally {
            data.setTestResultDataInExcel(testCaseData);
        }

    }
}

 