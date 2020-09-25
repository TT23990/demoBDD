//package org.mytest.services;
//
//
//import com.moodys.selenium.base.configuration.ConfigUtil;
//import com.moodys.selenium.base.lib.AES;
//import com.moodys.selenium.base.pageframework.BaseSteps;
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import java.io.File;
//import java.io.IOException;
//import java.security.InvalidParameterException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import static com.moodys.pageobjects.lightning.BaseObjectsLPO.wd;
//
//public class Utils extends BaseSteps {
//
//    public String decryptPassword(String sPassword) {
//        String sKeyLocation = ConfigUtil.config.get("env.key.location").trim();
//        if (sKeyLocation.isEmpty()) {
//            throw new InvalidParameterException("Key Location can't be empty");
//        } else {
//            return AES.decryptPassword(sPassword.trim(), sKeyLocation);
//        }
//    }
//
//    public static void takeScreenshotAtEndOfTest() throws IOException {
//        File scrFile = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);
//        String currentDir = System.getProperty("user.dir");
//        FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));
//    }
//
//    public static String getCurrentDate(String value) {
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
////        String date = simpleDateFormat.format(new Date());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        Calendar cal = Calendar.getInstance();
//        List<String> items = Arrays.asList(value.split(" "));
//        if(items.size()==4){
//            int count=Integer.parseInt(items.get(1).concat(items.get(2)));
//            cal.add(items.get(3).equalsIgnoreCase("Day")?Calendar.DATE:
//                    items.get(3).equalsIgnoreCase("Month")?Calendar.MONTH:
//                    items.get(3).equalsIgnoreCase("Year")?Calendar.YEAR:Calendar.YEAR, count);
//        }
//        return simpleDateFormat.format(cal.getTime());
//    }
//}
//
//