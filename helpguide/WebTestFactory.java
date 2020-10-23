package com.moodys.testservices;


import com.moodys.servicecloud.SFDC_TestSuite;
import org.testng.annotations.Factory;
import java.util.LinkedHashMap;


public class WebTestFactory {

    @Factory(dataProvider = "profiles",
            dataProviderClass =com.moodys.pageobjects.sharedservices.TestDataProvider.class )
    public Object[] createInstances(LinkedHashMap<String, String> user) {

        return new Object[]{new SFDC_TestSuite(user)};
        }

}

 