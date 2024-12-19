package auto.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReportManager {

    public static void main(String[] args) {
        try {
            String currentPath = System.getProperty("user.dir") + "/test-output/HtmlReport/ExtentHtml.html";
            File file = new File(currentPath);

// read file
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");

            }
            fileReader.close();
            bufferedReader.close();

            String outputPath = null;
            if (System.getProperty("os.name").contains("Windows")) {
                outputPath = System.getProperty("user.dir") + "\\test-output";
            } else {
                outputPath = System.getProperty("user.dir") + "/test-output";
            }
            System.out.println("--" + outputPath);
            outputPath = outputPath.replaceAll("\\\\", "\\\\\\\\");
            System.out.println("--" + outputPath);

            String updatedStr = builder.toString().replaceAll(outputPath, ".\\\\..");

// write into file
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(updatedStr);
            bufferedWriter.flush();
            fileWriter.close();
            bufferedWriter.close();

            if (System.getProperty("MachineType") != null && System.getProperty("MachineType").equals("local")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy-hh_mm_aaa");
                String dateStr = dateFormat.format(new Date());

                String countryCode = "";
                String cucumberOptions = System.getProperty("cucumber.options");

                if (cucumberOptions.contains("UK")) {

                    countryCode = "GBR";
                } else if (cucumberOptions.contains("NL")) {
                    countryCode = "NLD";

                } else if (cucumberOptions.contains("NO")) {
                    countryCode = "NOR";

                } else if (cucumberOptions.contains("CHDE")) {
                    countryCode = "CHDE";

                } else if (cucumberOptions.contains("CHIT")) {
                    countryCode = "CHIT";

                } else if (cucumberOptions.contains("CHFR")) {
                    countryCode = "CHFR";

                } else if (cucumberOptions.contains("FR")) {

                    countryCode = "FRA";

                } else if (cucumberOptions.contains("DE")) {
                    countryCode = "DEU";

                }

                if (cucumberOptions.contains("NewPers")) {

                    countryCode = countryCode + "_NP";
                }
                if (cucumberOptions.contains("ExiPers")) {
                    countryCode = countryCode + "_EP";

                }
                if (cucumberOptions.contains("NewBusi")) {
                    countryCode = countryCode + "_NB";

                }
                if (cucumberOptions.contains("ExiBusi")) {
                    countryCode = countryCode + "_EB";

                }

                if (cucumberOptions.contains("active-qa2")) {
                    countryCode = "QA2_" + countryCode;

                } else if (cucumberOptions.contains("active-prod")) {
                    countryCode = "PROD_" + countryCode;
                } else {
                    countryCode = "PERF_" + countryCode;

                }
                File sourceFile = new File(System.getProperty("user.dir") + "/test-output");
                File destFile = new File(
                        System.getProperty("user.dir") + "/" + countryCode + "_test-output_" + dateStr);

                System.out.println("source file:" + sourceFile);
                System.out.println("destination file:" + destFile);
                if (sourceFile.renameTo(destFile)) {
                    System.out.println("File Renamed successfully");
                } else {
                    System.out.println("Failed to rename file");
// throw new FileSystemException(
// System.getProperty("user.dir") + "/" + countryCode + "_test-output_" +
// dateStr);

                }
//
// Path source = Paths.get(System.getProperty("user.dir") + "/test-output");
// Files.move(source,
// source.resolveSibling(
// System.getProperty("user.dir") + "/" + countryCode + "_test-output_" +
// dateStr),
// StandardCopyOption.REPLACE_EXISTING);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
