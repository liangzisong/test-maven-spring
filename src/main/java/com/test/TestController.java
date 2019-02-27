package com.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/test.html")
    public String listUI(ModelMap modelMap) throws IOException {
        // 1、从spring容器中获得FreeMarkerConfigurer对象。
        // 2、从FreeMarkerConfigurer对象中获得Configuration对象。
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        File file = new File("/run/media/liang/liang/idea-IU-183.4588.61/projects/test-maven-spring/webapp/WEB-INF/html");
        File[] files = file.listFiles();
        Boolean b = false;
        for (int i = 0; i < files.length; i++) {
            if(files[i].isFile()){
                //yes
                if(files[i].getName().substring(0,files[i].getName().lastIndexOf(".")).equals("hi")){
                    b = true;
                    long lastModified = files[i].lastModified();
                    long l = System.currentTimeMillis() - lastModified;
                    // 1MM
                    if(l>(60000)){
                        CreateTemplet(modelMap, configuration);
                    }
                }
            }
        }
        if(!b){
            CreateTemplet(modelMap, configuration);
        }
        return "html/hi.html";
    }

    private void CreateTemplet(ModelMap modelMap, Configuration configuration) throws IOException {
        // 3、使用Configuration对象获得Template对象。
        Template template = configuration.getTemplate("/ftl/hi.ftl");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 4、创建数据集
        modelMap.put("name", simpleDateFormat.format(new Date()));
        // 5、创建输出文件的Writer对象。
        Writer out = null;
        try {
            out = new FileWriter(new File("/run/media/liang/liang/idea-IU-183.4588.61/projects/test-maven-spring/webapp/WEB-INF/html/hi.html"));
            // 6、调用模板对象的process方法，生成文件。
            template.process(modelMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 7、关闭流。
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
