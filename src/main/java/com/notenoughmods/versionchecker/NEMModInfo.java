package com.notenoughmods.versionchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NEMModInfo {

    private String name;
    private String version;
    private String longurl;
    private String shorturl;
    private String[] aliases;
    private String comment;
    private String modid;

    private Boolean upToDate = null;

    public boolean isUpToDate(String modVersion, String displayVersion) {
        if(getUpToDate() != null) return getUpToDate();
        if(modVersion.equals("%VERSION%") || displayVersion.equals("%VERSION%")) {
            setUpToDate(false);
            return false;
        }

        modVersion = Utils.patchVersion(modVersion);
        displayVersion = Utils.patchVersion(displayVersion);

        Pattern versionPattern = Pattern.compile(Utils.allVersionRegex);
        Matcher matcher;
        String actualModVersion;
        boolean modVersionValid;
        if(modVersion.equals(displayVersion)) {
            actualModVersion = modVersion;
            matcher = versionPattern.matcher(actualModVersion);
            modVersionValid = matcher.matches();
        } else {
            matcher = versionPattern.matcher(modVersion);
            modVersionValid = matcher.matches();
            matcher = versionPattern.matcher(displayVersion);
            boolean displayModVersionValid = matcher.matches();
            if(modVersionValid && displayModVersionValid) {
                // Take newer or more detailed version
                actualModVersion = (Utils.isNewer(modVersion, displayVersion) ? modVersion : displayVersion);
            } else if(modVersionValid) {
                actualModVersion = modVersion;
            } else if(displayModVersionValid) {
                actualModVersion = displayVersion;
                modVersionValid = true;
            } else {
                actualModVersion = modVersion + "/" + displayVersion;
            }
        }

        if(modVersionValid) {
            boolean result = isUpToDate(actualModVersion);
            setUpToDate(result);
            return result;
        }
        setUpToDate(false);
        return false;
    }

    private boolean isUpToDate(String modVersion) {
        Pattern versionPattern = Pattern.compile(Utils.allVersionRegex);
        Matcher matcher;
        matcher = versionPattern.matcher(modVersion);
        boolean modVersionValid = matcher.matches();
        if(modVersionValid) {
            return !Utils.isNewer(version, modVersion);
        }
        return false;
    }

    private void setUpToDate(Boolean utd) { this.upToDate = utd; }

    private Boolean getUpToDate() { return upToDate; }

    public String getName() { return name; }

    public String getVersion() { return version; }

    public String getLongurl() { return longurl; }

    public String shorturl() { return shorturl; }

    public String[] getAliases() { return aliases; }

    public String getComment() { return comment; }

    public String getModid() { return modid; }
}
