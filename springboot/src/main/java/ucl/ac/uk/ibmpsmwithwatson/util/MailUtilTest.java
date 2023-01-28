package ucl.ac.uk.ibmpsmwithwatson.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;

public class MailUtilTest {
    public static void main(String[] args) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("mail.html");
        String content = template.render(Dict.create().set("content", "A piece of content replaced by template render"));
        String text = MailUtil.send("tianang.chen.19@ucl.ac.uk", "subject", content, true);
        System.out.println(text);
    }
}
