package utn.dao.mysql;

public class MySQLUtils {

    //-----------------USERS--------------------
    public static String BASE_USER_QUERY = "select * from users u inner join cities c inner join provinces as pro on pro.id_province = c.id_province_fk on u.id_city_fk = c.id_city ";
    public static String GET_BY_ID_USER_QUERY = BASE_USER_QUERY + " where u.id_user = ?";
    public static String GET_BY_USERNAME_USER_QUERY = BASE_USER_QUERY + "  where username = ? and pwd=?";
    public static final String INSERT_USER_QUERY = "insert into users(first_name,surname,dni,birthdate,username,pwd,email,user_type,user_status,id_city_fk) values (?,?,?,?,?,?,?,?,?,?);";
    public static final String UPDATE_USER_STATUS_QUERY = "update users set user_status=2 where id_user =?";
    public static final String UPDATE_USER_QUERY = "update users set first_name=?, surname=?, dni=?, birthdate=?, username=?,pwd=?,email=?,user_type=?,user_status=?,id_city_fk=? where id_user=?";
    public static final String GET_BY_USERNAME = "select first_name from users where username=?";
    public static final String GET_MOST_CALLED_NUMBER = "select f.line_number_to ,u.first_name,u.surname " +
            "from users as u \n" +
            "join (select line_number_to, id_line_number_from_fk\n" +
            "\t  from phonecalls\n" +
            "\t  where line_number_from=?\n" +
            "\t  group by line_number_to\n" +
            "\t  order by count(line_number_to) desc\n" +
            "\t  limit 1) as f\n" +
            "on f.id_line_number_from_fk = u.id_user";
    //--------------------CITIES------------------------
    public static final String BASE_CITY_QUERY = "select * from cities c inner join provinces p on p.id_province=c.id_province_fk"; //getAll
    public static final String GET_CITY_BY_PREFIX = BASE_CITY_QUERY + " where prefix = ?";
    public static final String INSERT_CITY_QUERY = "insert into cities (city_name,prefix,id_province_fk) values(?,?,?)";
    public static final String UPDATE_CITY_QUERY = "update cities set city_name=?, prefix=?, id_province_fk=? where id_city=?";
    public static final String REMOVE_CITY_QUERY = "delete from cities where id_city=?";
    public static final String GETBYID_CITY_QUERY = BASE_CITY_QUERY + " where id_city=?";
    //-------------------PROVIENCES--------------------
    public static final String INSERT_PROVINCE_QUERY = "insert into provinces(province_name) values(?)";
    public static final String REMOVE_PROVINCE_QUERY = "delete from provinces where id_province=?";
    public static final String UPDATE_PROVINCE_QUERY = "update provinces set province_name=? where id_province=?";
    public static final String GETBYID_PROVINCE_QUERY = "select * from provinces where id_province=?";
    public static final String GETALL_PROVINCE_QUERY = "select * from provinces";
    //--------------------USER_LINES-------------------
    public static final String GET_USERLINE_BYNUMBER_QUERY = "select line_number from user_lines where line_number=?";
    public static final String BASE_USERLINE_QUERY = "select * from user_lines ul inner join users u on u.id_user=ul.id_client_fk"; //getAll
    public static final String INSERT_USERLINE_QUERY = "insert into user_lines(line_number,type_line,line_status,id_client_fk) values(?,?,?,?)";
    public static final String UPDATE_USERLINE_STATUS_QUERY = "update user_lines set line_status=3 where id_user_line=?";
    public static final String SUSPEND_USERLINE_QUERY = "update user_lines set line_status=2 where id_client_fk=?";
    public static final String UPDATE_USERLINE_QUERY = "update user_lines set line_number=?, type_line=?, line_status=?, id_client_fk=? where id_user_line=?";
    public static final String GETBYID_USERLINE_QUERY = BASE_USERLINE_QUERY + " where id_user_line=?";
    //---------------------RATES----------------------
    public static final String BASE_RATES_QUERY = "select * from rates "; //getAll me parece que en casos como este que tenemos dos fk de una misma tabla(cities) conviene traernos la info de las cuidades a la hora de mapear el obj por un getByID.
    public static final String INSERT_RATES_QUERY = "insert into rates(price_per_min,cost_per_min,id_city_from_fk,id_city_to_fk) values(?,?,?,?)";
    public static final String REMOVE_RATES_QUERY = "delete from rates where id_rate=?";
    public static final String UPDATE_RATES_QUERY = "update rates set price_per_min=?, cost_per_min=?, id_city_from_fk=?, id_city_to_fk=? where id_rate=?";
    public static final String GETBYID_RATES_QUERY = BASE_RATES_QUERY + " where id_rate=?";
    public static final String GET_RATE_BY_CITY_QUERY = "select * from rates as r where r.id_city_from_fk = ? and r.id_city_to_fk = ?";
    //-------------------INVOICES---------------------
    public static final String BASE_INVOICES_QUERY = "select * from invoices inv inner join user_lines ul on ul.id_user_line=inv.id_line_fk"; //getAll
    public static final String INSERT_INVOICES_QUERY = "insert into invoices(call_count,price_cost,price_total,date_emission,date_expiration,invoice_status,id_line_fk) values(?,?,?,?,?,?,?)";
    //public static final String REMOVE_INVOICES_QUERY = "delete from invoices where id_invoice=?"; esta tiene baja logica.
    public static final String UPDATE_INVOICES_QUERY = "update invoices set call_count=?, price_cost=?, price_total=?, date_emission=?, date_expiration=?, invoice_status=?,id_line_fk=? where id_invoice=?";
    public static final String GETBYID_INVOICES_QUERY = BASE_INVOICES_QUERY + " where id_invoice=?";
    public static final String GETALLBYID_INVOICES_QUERY = BASE_INVOICES_QUERY + " where ul.id_client_fk=?";
    //-------------------PHONECALLS-------------------
    public static final String BASE_PHONECALLS_QUERY = "select * from phonecalls"; //getAll me traigo los datos de la factura pero las cuidades y las lineas conviene traerlas a la hora de mapear el obj
    public static final String INSERT_PHONECALLS_QUERY = "insert into phonecalls(line_number_from,line_number_to,duration,call_date) values(?,?,?,?)";
    public static final String REMOVE_PHONECALLS_QUERY = "delete from phonecalls where id_phonecall=?";
    public static final String UPDATE_PHONECALLS_QUERY = "update phonecalls set line_number_from=?, line_number_to=?, id_line_number_from_fk=?, id_line_number_to_fk=?, id_city_from_fk=?, id_city_to_fk=?, duration=?, call_date=?, id_invoice_fk=? where id_phonecall=?";
    public static final String GETBYID_PHONECALLS_QUERY = BASE_PHONECALLS_QUERY + " where id_phonecall=?";
    public static final String GETBYID_USERPHONECALLS_QUERY = "select p.id_phonecall,p.line_number_from,p.line_number_to,p.id_city_from_fk,p.id_city_to_fk,p.duration,p.call_date,p.total_price\n" +
            "from phonecalls as p\n" +
            "join user_lines as ul\n" +
            "on p.id_line_number_from_fk=ul.id_user_line\n" +
            "join users as u\n" +
            "on ul.id_client_fk=u.id_user\n" +
            "where u.id_user=?";

}
