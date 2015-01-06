package com.letzbuild.HKS;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

import javax.mail.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Created by venky on 29/12/14.
 */
public class ConfigurationUtils {
    public static Properties readConfigFile() {
        Properties prop = null;
        InputStream input = null;

        try {

            String filename = "config.properties";
            input = ConfigurationUtils.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return prop;
            }

            prop = new Properties();
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return prop;
    }

    public static Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(ConfigurationUtils.class, "/freemarker");
        return retVal;
    }

    public static String processFreemarkerTemplate(String templateName, SimpleHash root) throws Exception {
        String ret = "";

        Configuration cfg = ConfigurationUtils.createFreemarkerConfiguration();
        Template template = cfg.getTemplate(templateName);

        Writer out = new StringWriter();
        template.process(root, out);

        return out.toString();
    }

    public static void sendEmail(String subject, String emailBody, Map<String, String> recipients) {
        Properties p = ConfigurationUtils.readConfigFile();

        final Email email = new Email();
        final String username = p.getProperty("emailUsername");
        final String password = p.getProperty("emailPassword");
        email.setFromAddress(p.getProperty("fromEmailName"), p.getProperty("fromEmailUsername"));

        email.setSubject(subject);

        for (Map.Entry<String, String> recipient : recipients.entrySet()) {
            String name = recipient.getKey();
            String emailTo = recipient.getValue();
            email.addRecipient(name, emailTo, Message.RecipientType.TO);
        }

        new Mailer("smtp.gmail.com", 465, username, password, TransportStrategy.SMTP_SSL).sendMail(email);
    }
}
