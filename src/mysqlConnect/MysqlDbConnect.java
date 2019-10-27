package mysqlConnect;

import java.sql.*;

public class MysqlDbConnect
{
	private  Connection conn;

	private  Connection getConnection()
	{
		String url = "jdbc:mysql://localhost:3306/timeclock";
		String user = "root";
		String password = "123456";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		try
		{
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public  void execute(String sqlString)
	{
		getConnection();
		PreparedStatement preparedStatement = null;
		try
		{
			preparedStatement = conn.prepareStatement(sqlString);
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			preparedStatement.execute();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  ResultSet getResultSet(String sqlString)
	{
		getConnection();
		ResultSet rSet = null;
		try
		{
			rSet = conn.prepareStatement(sqlString).executeQuery();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rSet;
	}

	public  void closeConnection()
	{
		if(conn!=null)
			try
			{
				conn.close();
			}
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
	}

}
