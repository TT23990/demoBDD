package com.moodys.interfaces;

public interface Home {
    public boolean logoutFromApplication();
    public boolean validateSelectedSandbox(String value);
    public boolean validateSelectedApplication(String application);
    public boolean searchItemList(String item,String value);
    public boolean selectApplication(String appName);

}
 