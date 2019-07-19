package org.f3tool.tdiff;

import java.io.File;
import java.io.IOException;

import static org.f3tool.tdiff.TableDiff.removeFileExtention;

public class Main {
    public static void main(String[] args) throws DiffException, IOException {


        //ios_ifa,ios_ifv,android_id,google_aid,windows_aid,os_name,os_version,device_manufacturer,device_model,device_type,device_locale,application_id,app_version_name,app_package_name,event_name,event_json,event_datetime,event_timestamp,event_receive_datetime,
        // connection_type,operator_name,mcc,mnc,country_iso_code,city,appmetrica_device_id,profile_id,device_ipv6,event_receive_timestamp

        String configFile = "/home/dimav/workspace/tableDiff/compare.conf";
        TableDiff tableDiff = new TableDiff(configFile);

//        String inputFile1 = "/home/dimav/Downloads/events-2019-07-16-1563501312632.csv";
        String inputFile1 = "/home/dimav/Downloads/events-2019-07-16-1563501312632.100.csv";
//        String inputFile2 = "/home/dimav/Downloads/events-2019-07-16-1563516347059.csv";
        String inputFile2 = "/home/dimav/Downloads/events-2019-07-16-1563516347059.100.csv";
        String outputFile = "/home/dimav/workspace/tableDiff/result.txt";
        String tmpdirectory = "/home/dimav/workspace/tableDiff/temp";


//        String configFile = argc[1];
//        String inputFile1 = argc[2];
//        String inputFile2 = argc[3];
//        String outputFile = argc[4];
//        String tmpdirectory = argc[5];

//        long numberLinePerFile = Integer.parseInt(argc[6]);
        long numberLinePerFile = 300_000_000L;
//        int maxNumberOfFile = Integer.parseInt(argc[7]);
        int maxNumberOfFile = 100;

        boolean keepTmp = true;

//        if (argc[8].equalsIgnoreCase("Y")) keepTmp = true;

        TableSort sorter = new TableSort(configFile);
        ComparisonConfig config = ComparisonConfig.getInstance();

        File tmpfile1 = File.createTempFile(removeFileExtention(new File(inputFile1).getName()) + "_tmp", "flatfile", new File(tmpdirectory));
        if (!keepTmp) tmpfile1.deleteOnExit();

        sorter.sort(inputFile1, tmpfile1.getAbsolutePath(), tmpdirectory, numberLinePerFile,
                maxNumberOfFile, true, keepTmp, config.getLeftDelimiter(),
                config.getLeftEscape(), config.getLeftKeyGenerator(), config.getLeftFilter());

        File tmpfile2 = File.createTempFile(removeFileExtention(new File(inputFile2).getName()) + "_tmp", "flatfile", new File(tmpdirectory));
        if (!keepTmp) tmpfile2.deleteOnExit();

        sorter.sort(inputFile2, tmpfile2.getAbsolutePath(), tmpdirectory, numberLinePerFile,
                maxNumberOfFile, true, keepTmp, config.getRightDelimiter(),
                config.getRightEscape(), config.getRightKeyGenerator(), config.getRightFilter());

        TableDiff diff = new TableDiff(configFile);
        diff.diff(new FirstRowKeyGenerator(), new FirstRowKeyGenerator(), null, null, tmpfile1.getAbsolutePath(),
                tmpfile2.getAbsolutePath(), outputFile);
    }
}
