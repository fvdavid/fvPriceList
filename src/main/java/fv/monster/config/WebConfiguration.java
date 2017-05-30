package fv.monster.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
    
    public JasperReportsViewResolver jasperResolver() {
        JasperReportsViewResolver resolver = new JasperReportsViewResolver();

        resolver.setViewClass(JasperReportsMultiFormatView.class);
        resolver.setOrder(0); // cari template jasper dulu, kalau tidak ketemu, baru cari template thymeleaf
        resolver.setViewNames("report/*");
        resolver.setPrefix("classpath:/");
        resolver.setSuffix(".jrxml");
        resolver.setReportDataKey("dataDalamReport");

        return resolver;
    }
}
