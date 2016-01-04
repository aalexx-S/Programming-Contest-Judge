package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class ProblemManager extends DatabaseManager {

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:problem.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Problem " +
                "(ProblemID STRING PRIMARY KEY NOT NULL," +
                " TimeLimit DOUBLE  NOT NULL," +
                " MemoryLimit   INT NOT NULL," +
                " Input     STRING  NOT NULL," +
                " Output    STRING  NOT NULL," +
                " SpecialJudge  STRING," +
                " Timestamp INT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Problem (ProblemID,TimeLimit,MemoryLimit,Input,Output,SpecialJudge,Timestamp)" + 
                        "VALUES (?, ?, ?, ?, ?, ?, ?);"); 
                stmt.setString(1, entry.get("problem_id"));
                stmt.setString(2, entry.get("time_limit"));
                stmt.setString(3, entry.get("memory_limit"));
                stmt.setString(4, entry.get("input"));
                stmt.setString(5, entry.get("output"));
                stmt.setString(6, entry.get("special_judge"));
                stmt.setString(7, entry.get("time_stamp"));

                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public void updateEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                int nTS = Integer.parseInt(entry.get("time_stamp"));

                stmt = c.prepareStatement("SELECT Timestamp FROM Problem WHERE ProblemID = ?;");
                stmt.setString(1, entry.get("problem_id"));
                ResultSet rs = stmt.executeQuery();

                if (!rs.next() || rs.getInt("Timestamp") > nTS) {
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
                rs.close();
                stmt.close();

                List<String> setType = new ArrayList<String>();
                List<String> setValue = new ArrayList<String>();
                if (entry.get("time_limit") != null) {
                    setType.add("TimeLimit");
                    setValue.add(entry.get("time_limit"));
                }
                if (entry.get("memory_limit") != null) {
                    setType.add("MemoryLimit");
                    setValue.add(entry.get("memory_limit"));
                }
                if (entry.get("input") != null) {
                    setType.add("Input");
                    setValue.add(entry.get("input"));
                }
                if (entry.get("output") != null) {
                    setType.add("Output");
                    setValue.add(entry.get("output"));
                }
                if (entry.get("special_judge") != null) {
                    setType.add("SpecialJudge");
                    setValue.add(entry.get("special_judge"));
                }

                String sql = "UPDATE Problem SET ";
                for (int i = 0; i < setType.size(); i++) {
                    sql += setType.get(i) + " = ?, ";
                }
                sql +=  "Timestamp = " + entry.get("time_stamp") + " WHERE ProblemID = '" + entry.get("problem_id") + "';";

                stmt = c.prepareStatement(sql);
                for (int i = 0; i < setValue.size(); i++) {
                    stmt.setString(i + 1, setValue.get(i));
                }
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public List<Map<String, String>> queryAll() {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Problem;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("memory_limit", Integer.toString(me));
                    entry.put("time_limit", Double.toString(te));
                    entry.put("input", input);
                    entry.put("output", output);
                    entry.put("special_judge", sj);
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public List<String> queryAllId() {
        Connection c = null;
        Statement stmt = null;
        List<String> response = new ArrayList<String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT ProblemID FROM Problem;" );
                while (rs.next()) {
                    String pid = rs.getString("ProblemID");
                    response.add(pid);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }
    public List<Map<String, String>> sync(int time_stamp) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Problem WHERE Timestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("memory_limit", Integer.toString(me));
                    entry.put("time_limit", Double.toString(te));
                    entry.put("input", input);
                    entry.put("output", output);
                    entry.put("special_judge", sj);
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }
    public Map<String, String> getProblemById(String problem_id) {
        Connection c = null;
        PreparedStatement stmt = null;
        Map<String, String> response = new HashMap<String, String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Problem WHERE ProblemID = ?;");
                stmt.setString(1, problem_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    response.put("problem_id", pid);
                    response.put("time_stamp", Integer.toString(time));
                    response.put("memory_limit", Integer.toString(me));
                    response.put("time_limit", Double.toString(te));
                    response.put("input", input);
                    response.put("output", output);
                    response.put("special_judge", sj);
                    
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }
    
    public void flushTable() {
        Connection c = null;
        Statement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DROP TABLE IF EXISTS Problem;";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }
}
