package utn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
//import utn.session.BackOfficeSessionFilter;
//import utn.session.ClientPortalSessionFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@org.springframework.context.annotation.Configuration
@PropertySource("app.properties")
public class configuration {

    /*@Autowired
    ClientPortalSessionFilter clientPortalSessionFilter;
    @Autowired
    BackOfficeSessionFilter backOfficeSessionFilter;*/
    @Value("${db.driver}")
    String driver;
    @Value("${db.name}")
    String db;
    @Value("${db.host}")
    String host;
    @Value("${db.port}")
    int port;
    @Value("${db.user}")
    String username;
    @Value("${db.password}")
    String password;

    @Bean
    public Connection getConnection() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName(driver).newInstance();
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, username, password);
        return con;
    }

    /*@Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(clientPortalSessionFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean backOffice(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(backOfficeSessionFilter);
        registration.addUrlPatterns("/backoffice/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean Antenna(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(backOfficeSessionFilter);
        registration.addUrlPatterns("/antenna/*");
        return registration;
    }*/
}
