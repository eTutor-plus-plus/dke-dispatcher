package at.jku.dke.etutor.modules.xquery.test;/*
 * Created on 16.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XSLException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import etutor.modules.xquery.util.XMLUtil;

/**
 * @author nitsche (16.06.2005)
 */
public class XQTest implements Serializable {

    int newOne;
    static final long serialVersionUID = 7346780935361818820L;
    
    public static void main(String[] args) {
    }

    public static void newOne() {
    }

    public static void test3() {
        try {
            XMLDocument doc = XMLUtil.parse(new File("J:/etutor_rmi_modules/modules/xquery/classes/etutor/resources/modules/xquery/temp/xq03_uid50038_7577.xsl").toURL(), false);
            String matchValue = "/result[1]/filiale[1]/prodGruppe[2]/ean[16]";
            String xPath1 = "//xsl:template[@match='" + matchValue + "']";
            String xPath2_1 = "//xsl:template/xsl:copy-of[@select='" + matchValue + "']";
            String xPath2_2 = "//xsl:template//xsl:copy-of[@select='" + matchValue + "']";
            String xPath3_1 = "//xsl:template//xsl:element[xsl:value-of/@select and @name='{name(" + matchValue + ")}']";
            String xPath3_2 = "//xsl:template/xsl:element[xsl:value-of/@select and @name='{name(" + matchValue + ")}']";
            checkXPath(xPath1, doc);
            checkXPath(xPath2_1, doc);
            checkXPath(xPath2_2, doc);
            checkXPath(xPath3_1, doc);
            checkXPath(xPath3_2, doc);
            
            doc = XMLUtil.parse(new File("C:/Programme/eclipse/workspace/etutor_legacy_australia/deployment/test.xsl").toURL(), false);
            matchValue = "1";
            xPath1 = "//xsl:b[@match='" + matchValue + "']";
            xPath2_1 = "//xsl:b/xsl:c[@select='" + matchValue + "']";
            xPath2_2 = "//xsl:b//xsl:c[@select='" + matchValue + "']";
            xPath3_1 = "//xsl:b//xsl:d[xsl:e/@select and @name='{name(" + matchValue + ")}']";
            xPath3_2 = "//xsl:b/xsl:d[xsl:e/@select and @name='{name(" + matchValue + ")}']";
            checkXPath(xPath1, doc);
            checkXPath(xPath2_1, doc);
            checkXPath(xPath2_2, doc);
            checkXPath(xPath3_1, doc);
            checkXPath(xPath3_2, doc);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void checkXPath(String xPath, XMLDocument doc) {
        try {
            NodeList list = doc.selectNodes(xPath, (XMLElement)doc.getDocumentElement());
            System.out.println("Length: " + list.getLength() + "; XPath: " + xPath);
        } catch (XSLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void test2() {
        deserialize();
    }
    
    public static void serialize() {
        XQTest t = new XQTest();
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(new File("D:/test.out"));
            oout = new ObjectOutputStream(fout);
            oout.writeObject(t);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oout != null) {
                    oout.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }            
        }
    }
    
    public static void deserialize() {
        FileInputStream fin = null;
        ObjectInputStream oin = null;
        
        try {
            fin = new FileInputStream(new File("D:/test.out"));
            oin = new ObjectInputStream(fin);
            XQTest t = (XQTest)oin.readObject();
            Method[] methods = t.getClass().getMethods();
            System.out.println(methods.length + " methods:");
            for (int i = 0; i < methods.length; i++) {
                System.out.println(methods[i]);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oin != null) {
                    oin.close();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
    }

    public static void test1() {
        Date date = new Date();
        
        Locale localeGE = Locale.GERMANY;
        Locale localeFR = Locale.FRANCE;
        Locale localeEN = new Locale("en", "US" );
 
        print(date, localeGE);
        print(date, localeFR);
        print(date, localeEN);
        //printTZ();
    }
    public static void print(Date date, Locale locale) {
        
        TimeZone zone1 = TimeZone.getDefault();
        TimeZone zone2 = TimeZone.getTimeZone("GMT+02");
        
        DateFormat dateFormat1 =
            DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT,
            locale);
        dateFormat1.setTimeZone(zone1);
        
        DateFormat dateFormat2 =
            DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.FULL);
        dateFormat2.setTimeZone(zone2);
        
        System.out.println(
                "Locale: " + locale.getCountry()
                + "; Country: " + locale.getLanguage()
				+ "; Variant: " + locale.getVariant()
                + "; ISO3Country: " + locale.getISO3Country()
                + "; ISO3Language: " + locale.getISO3Language()
                );
        System.out.println(dateFormat1.format(date));
        System.out.println(dateFormat2.format(date));
        System.out.println(DateFormat.getDateTimeInstance().format(date, new StringBuffer("??? "), new FieldPosition(DateFormat.YEAR_FIELD)));
        System.out.println("-----------------------");
    }
    
    public static void printTZ() {
        String[] ids = TimeZone.getAvailableIDs();
        Arrays.sort(ids);
        for (int i = 0; i < ids.length; i++) {
            System.out.println((i + 1) + ": " + ids[i]);
        }
        System.out.println("--------------");
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            System.out.println((i + 1) + ": " + locales[i]);
        }
    }
    public int getNewOne() {
        return newOne;
    }
    public void setNewOne(int newOne) {
        this.newOne = newOne;
    }
    
    public static void displayClassInfo(Class clazz, StringBuffer results) {
        System.out.println("--------------------------------------------");
        System.out.println("--------------------------------------------");
        // Print out some codebase info for the ProbeHome
        ClassLoader cl = clazz.getClassLoader();
        results
                .append("\n" + clazz.getName() + "("
                        + Integer.toHexString(clazz.hashCode())
                        + ").ClassLoader=" + cl);
        ClassLoader parent = cl;
        while (parent != null) {
            results.append("\n.." + parent);
            /*
             * URL[] urls = getClassLoaderURLs(parent); int length = urls !=
             * null ? urls.length : 0; for(int u = 0; u < length; u ++) {
             * results.append("\n...."+urls[u]); }
             */
            if (parent != null)
                parent = parent.getParent();
        }
        CodeSource clazzCS = clazz.getProtectionDomain().getCodeSource();
        if (clazzCS != null)
            results.append("\n++++CodeSource: " + clazzCS);
        else
            results.append("\n++++Null CodeSource");

        results.append("\nImplemented Interfaces:");
        Class[] ifaces = clazz.getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
            Class iface = ifaces[i];
            results.append("\n++" + iface + "("
                    + Integer.toHexString(iface.hashCode()) + ")");
            ClassLoader loader = ifaces[i].getClassLoader();
            results.append("\n++++ClassLoader: " + loader);
            ProtectionDomain pd = ifaces[i].getProtectionDomain();
            CodeSource cs = pd.getCodeSource();
            if (cs != null)
                results.append("\n++++CodeSource: " + cs);
            else
                results.append("\n++++Null CodeSource");
        }
        System.out.println(results);
    }
}
