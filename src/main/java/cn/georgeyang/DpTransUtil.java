package cn.georgeyang;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 480*800,160dpi的手机纵向，宽度是320dp
 * values-w400dp是指，宽度至少400dp
 * 如果将400设置成设计时的宽度，那么就会有w???/w400,将比例乘进去得到400dp的一套尺寸
 * 生xml,从最小320开始生成,增量20,最大nexus6p的520，开始生成10个xml
 * Created by yangsp on 2016/10/24.
 */

public class DpTransUtil {
    private final static String TEMPLATE =  "   <dimen name=\"x{0}\">{1}dp</dimen>\n";
    private final static String TEMPLATEF = "   <dimen name=\"f{0}\">{1}sp</dimen>\n";
    private final static String TEMPLATEP = "   <dimen name=\"p{0}\">{1}dp</dimen>\n";
    private final static String TEMPLATE_DIR = "values-w{0}dp";
    private final static String FILENAME = "dps.xml";
    private final static String ENDCHAR = "</resources>";


    /**
     * 批量生成xml
     *
     * @param dir
     * @param designWidth
     * @param minWidthDp
     * @param maxWidthDp
     * @param increase
     * @return
     */
    public static boolean generate(String dir, int designWidth, int minWidthDp, int maxWidthDp, int increase) {
        boolean ret = true;
        for (int i = minWidthDp; i <= maxWidthDp; i += increase) {
            ret = generate(dir, designWidth, i);
        }
        return ret;
    }

    /**
     * 生成指定像素密度的xml
     *
     * @param dir
     * @param designWidth
     * @param tagWidthDp
     * @return
     */
    public static boolean generate(String dir, int designWidth, int tagWidthDp) {
        File fileDir = new File(dir + File.separator
                + TEMPLATE_DIR.replace("{0}", tagWidthDp + ""));
        fileDir.mkdirs();

        float proportion = tagWidthDp * 1f / designWidth;

        File xmlFile = new File(fileDir.getAbsolutePath(), FILENAME);

        System.out.println("try generate XmlFile:" + tagWidthDp + ">>" + xmlFile.getAbsolutePath());
        return generateXmlFile(xmlFile, designWidth, proportion);
    }

    /**
     * 追加百分比，如果1%-100%未满足需要，出现200%的情况，用此方法
     *
     * @return
     */
    public static boolean appendPercent(String dir, float percent) {
        try {
            File currFile = new File(dir);
            if (currFile.isDirectory()) {
                File[] files = currFile.listFiles();
                for (File file : files) {
                    appendPercent(file.getAbsolutePath(), percent);
                }
            } else if (FILENAME.equalsIgnoreCase(currFile.getName())) {
                System.out.println("try append Percent:" + currFile.getAbsolutePath());
                String content = readFile(currFile);
                Pattern pattern = Pattern.compile("<dimen name=\"p100\">(\\d+.\\d+)dp.*>");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String value = matcher.group(1);
                    float proportion = Float.valueOf(value) / 100;
                    float realWidth = ((int) (percent * proportion * 100)) / 100;
                    String append = TEMPLATEP.replace("{0}", String.valueOf(percent).replace(".", "_")).replace("{1}", realWidth + "");

                    RandomAccessFile raf = new RandomAccessFile(currFile, "rw");
                    raf.seek(currFile.length() - ENDCHAR.length());
                    raf.writeBytes(append);
                    raf.writeBytes(ENDCHAR);
                    raf.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 追加像素值，如果1-design宽度未满足需要，出现很高的高度需要显示，用此方法
     *
     * @return
     */
    public static boolean appendXValue(String dir, int px) {
        try {
            File currFile = new File(dir);
            if (currFile.isDirectory()) {
                File[] files = currFile.listFiles();
                for (File file : files) {
                    appendXValue(file.getAbsolutePath(), px);
                }
            } else if (FILENAME.equalsIgnoreCase(currFile.getName())) {
                System.out.println("try append XValue:" + currFile.getAbsolutePath());
                String content = readFile(currFile);
                Pattern pattern = Pattern.compile("<dimen name=\"x(\\d+.\\d+)\">(\\d+.\\d+)dp.*>");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    float proportion = Float.valueOf(value) / Float.valueOf(key);
                    float realWidth = ((int) (px * proportion * 100)) / 100;
                    String append = TEMPLATE.replace("{0}", px + "").replace("{1}", realWidth + "");

                    RandomAccessFile raf = new RandomAccessFile(currFile, "rw");
                    raf.seek(currFile.length() - ENDCHAR.length());
                    raf.writeBytes(append);
                    raf.writeBytes(ENDCHAR);
                    raf.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成xml
     * @param xmlFile
     * @param maxWidth
     * @param proportion 比例
     * @return
     */
    private static boolean generateXmlFile(File xmlFile, int maxWidth, float proportion) {
        StringBuffer output = new StringBuffer();
        output.append("<resources>\n");
        for (int i = 1; i <= 100; i++) {
            float realWidth = (maxWidth * proportion * i) / 100f;
            realWidth = ((int) realWidth * 100) / 100f;
            output.append(TEMPLATEP.replace("{0}", i + "").replace("{1}", realWidth + ""));
        }
        for (int i = 1; i <= maxWidth; i++) {
            float realWidth = (int) ((i * proportion) * 100) / 100f;
            output.append(TEMPLATE.replace("{0}", i + "").replace("{1}", realWidth + ""));
            output.append(TEMPLATEF.replace("{0}", i + "").replace("{1}", realWidth + ""));
        }
        output.append("</resources>");

        return writeToFile(xmlFile, output.toString());
    }

    private static String readFile(File file) {
        String content = "";
        try {
            Long fileLens = file.length();
            byte[] bytes = new byte[fileLens.intValue()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(bytes);
            fs.close();
            content = new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


    private static boolean writeToFile(File file, String content) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.print(content);
            pw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
        String path = "./res";
        System.out.println("生成结果 0:" + generate(path, 720, 320, 520, 10));
        System.out.println("生成结果 1:" + generate(path, 720, 120));
        System.out.println("生成结果 2:" + appendPercent(path, 200));
        System.out.println("生成结果 3:" + appendXValue(path, 800));
        System.out.println("生成结果 4:" + appendXValue(path, 1003));
        System.out.println("生成结果 4:" + appendXValue(path, 778));
        System.out.println("生成结果 5:" + appendXValue(path, 755));
    }



    /**
     * 传统算法，如果720的设计,2px=1sp(备用方法)
     * @param dir
     * @return
     */
    public static boolean generateSps(String dir) {
        File fileDir = new File(dir + File.separator + "values");
        fileDir.mkdirs();
        File spFile = new File(fileDir.getAbsolutePath(), "sps.xml");
        StringBuffer output = new StringBuffer();
        output.append("<resources>\n");
        for (int i = 1; i <= 720; i++) {
            String string = String.format("   <dimen name=\"f%d\">%ssp</dimen>\n",new Object[]{i,(i*10.0/20) + ""});
            output.append(string);
        }
        output.append("</resources>");

        return writeToFile(spFile, output.toString());
    }
}
